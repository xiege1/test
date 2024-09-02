package com.example.foodtruck.dai;

import com.example.foodtruck.api.domain.*;
import com.example.foodtruck.repository.AddressRepository;
import com.example.foodtruck.repository.FoodTruckAuditRepository;
import com.example.foodtruck.repository.FoodTruckRepository;
import com.example.foodtruck.repository.entity.AddressEntity;
import com.example.foodtruck.repository.entity.FoodTruckAuditEntity;
import com.example.foodtruck.repository.entity.FoodTruckEntity;
import com.example.foodtruck.util.ObjectConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.foodtruck.util.ObjectConvertor.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TheFoodTruckDai implements FoodTruckDai {

    private final FoodTruckRepository repository;
    private final FoodTruckAuditRepository auditRepository;
    private final AddressRepository addressRepository;

    private final StringRedisTemplate stringRedisTemplate;

    private static final String FOOD_TRUCK_LOCATION_KEY = "food-truck-location";

    @Value("${food-truck.location.distance.radius}")
    private Integer radius;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FoodTruck createFoodTruck(FoodTruckCreateForm foodTruck) {
        FoodTruckEntity entity = toFoodTruckEntity(foodTruck);
        repository.save(entity);

        FoodTruckAuditEntity auditEntity = toAuditEntity(foodTruck);
        auditEntity.setLocationId(entity.getLocationId());
        auditRepository.save(auditEntity);

        AddressEntity addressEntity = toAddressEntity(foodTruck);
        auditEntity.setLocationId(entity.getLocationId());
        addressRepository.save(addressEntity);

        updateFoodTruckLocation(entity.getLocationId(), foodTruck.getLatitude(), foodTruck.getLongitude());

        return toFoodTruck(entity, auditEntity, addressEntity);
    }

    private void updateFoodTruckLocation(Integer locationId, Double latitude, Double longitude) {
        try {
            stringRedisTemplate.opsForGeo().add(FOOD_TRUCK_LOCATION_KEY, new Point(longitude, latitude), String.valueOf(locationId));
        } catch (Exception e) {
            log.error("failed to update food truck location, locationId={}, lat={}, lon={}", locationId, latitude, longitude, e);
            //TODO
            // We can send a message to MQ and then consume the message in a spin manner to ensure that the food truck location is synchronized to the cache
            // Or save the failed data to the database, then start a scheduled job to load these date to retry writing to cache, to ensure success
        }
    }

    @Override
    public FoodTruck getFoodTruckByLocation(Integer locationId) {
        return repository.findById(locationId).map(entity  -> {
            FoodTruckAuditEntity auditEntity = auditRepository.findById(locationId).orElse(null);
            AddressEntity addressEntity =  addressRepository.findById(locationId).orElse(null);
            return toFoodTruck(entity, auditEntity, addressEntity);
        }).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FoodTruck updateFoodTruck(FoodTruckUpdateForm foodTruck) {
        return repository.findById(foodTruck.getLocationId()).map(entity -> {
            entity.setFoodItems(String.join(",", foodTruck.getFoodItems()));
            entity.setDaysHours(foodTruck.getDaysHours());

            repository.save(entity);

            AddressEntity address = addressRepository.findById(foodTruck.getLocationId()).map(addressEntity -> {
                addressEntity.setLatitude(foodTruck.getLatitude());
                addressEntity.setLongitude(foodTruck.getLongitude());
                addressEntity.setFirePreventionDistricts(foodTruck.getFirePreventionDistricts());
                addressEntity.setPoliceDistricts(foodTruck.getPoliceDistricts());
                addressEntity.setSupervisorDistricts(foodTruck.getSupervisorDistricts());
                addressEntity.setAddress(foodTruck.getAddress());
                addressEntity.setZipCode(foodTruck.getZipCode());
                addressEntity.setNeighborhoods(foodTruck.getNeighborhoods());

                addressRepository.save(addressEntity);

                return addressEntity;
            }).orElse(null);

            return toFoodTruck(entity, null, address);
        }).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeFoodTruckByLocation(Integer locationId) {
        repository.deleteById(locationId);
        auditRepository.deleteById(locationId);
        addressRepository.deleteById(locationId);
    }

    @Override
    public List<FoodTruck> getFoodTrucks(FoodTruckVisitForm form) {
        List<FoodTruck> foodTrucks = repository.findFoodTruckEntitiesByPagination(form.getApplicant(), form.getStatus(), form.getPage(), form.getPageSize())
                .map(ObjectConvertor::toFoodTruck).stream().toList();
        List<AddressEntity> addressEntities = addressRepository.findAllById(foodTrucks.stream().map(FoodTruck::getLocationId).collect(Collectors.toList()));
        assemble(foodTrucks, addressEntities);
        return foodTrucks;
    }

    @Override
    public List<FoodTruck> searchFoodTrucks(FoodTruckSearchForm form) {
        List<Integer> locationIds = getNearByLocations(form);
        var foodTruckEntities = repository.findByLocationIdIn(locationIds);
        Map<Integer, FoodTruckAuditEntity> auditEntitiesMap = auditRepository.findByLocationIdIn(locationIds).stream()
                .collect(Collectors.toMap(FoodTruckAuditEntity::getLocationId, Function.identity(), (a, b) ->  b));
        Map<Integer, AddressEntity> addressEntitiesMap = addressRepository.findByLocationIdIn(locationIds).stream()
                .collect(Collectors.toMap(AddressEntity::getLocationId, Function.identity(), (a, b) -> b));

        return foodTruckEntities.stream()
                .map(foodTruckEntity ->
                        toFoodTruck(foodTruckEntity,
                                auditEntitiesMap.get(foodTruckEntity.getLocationId()),
                                addressEntitiesMap.get(foodTruckEntity.getLocationId())
                        ))
                .collect(Collectors.toList());
    }

    private List<Integer> getNearByLocations(FoodTruckSearchForm form) {
        String[] locations = StringUtils.split(",", form.getLocation());
        Double longitude = Double.valueOf(locations[0]);
        Double latitude = Double.valueOf(locations[1]);

        Distance distance = new Distance(radius, Metrics.KILOMETERS);
        Circle circle = new Circle(new Point(longitude, latitude), distance);

        List<Integer> nearbyFoodTrucks = new ArrayList<>();
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = null;
        try {
            geoResults = stringRedisTemplate.opsForGeo().radius(FOOD_TRUCK_LOCATION_KEY, circle);
        } catch (Exception e) {
            log.error("failed to load nearby food-truck, location={}", form.getLocation(), e);
        }
        if (Objects.isNull(geoResults)) {
            return Collections.emptyList();
        }
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoResult : geoResults.getContent()) {
            String locationId = geoResult.getContent().getName();
            nearbyFoodTrucks.add(Integer.parseInt(locationId));
        }
        return nearbyFoodTrucks;
    }
}
