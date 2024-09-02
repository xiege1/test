package com.example.foodtruck.util;

import com.example.foodtruck.api.domain.FoodTruck;
import com.example.foodtruck.api.domain.FoodTruckCreateForm;
import com.example.foodtruck.constant.FoodTruckStatus;
import com.example.foodtruck.repository.entity.AddressEntity;
import com.example.foodtruck.repository.entity.FoodTruckAuditEntity;
import com.example.foodtruck.repository.entity.FoodTruckEntity;
import com.example.foodtruck.repository.entity.FoodTruckProxy;
import org.springframework.util.StringUtils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectConvertor {

    public static void assemble(List<FoodTruck> foodTrucks, List<AddressEntity> addressEntities) {
        Map<Integer, AddressEntity> addressEntityMap = addressEntities.stream()
                .collect(Collectors.toMap(AddressEntity::getLocationId, Function.identity(), (old, new1) -> new1));
        foodTrucks.forEach(foodTruck -> {
            AddressEntity entity = addressEntityMap.get(foodTruck.getLocationId());
            if (Objects.nonNull(entity)) {
                foodTruck.setBlock(entity.getBlock());
                foodTruck.setLot(entity.getLot());
                foodTruck.setLatitude(entity.getLatitude());
                foodTruck.setLongitude(entity.getLongitude());
                foodTruck.setAddress(entity.getAddress());
                foodTruck.setFirePreventionDistricts(entity.getFirePreventionDistricts());
                foodTruck.setPoliceDistricts(entity.getPoliceDistricts());
                foodTruck.setSupervisorDistricts(entity.getSupervisorDistricts());
                foodTruck.setZipCode(entity.getZipCode());
                foodTruck.setNeighborhoods(entity.getNeighborhoods());
            }
        });
    }
    public static FoodTruck toFoodTruck(FoodTruckProxy foodTruckProxy) {
        FoodTruck foodTruck =  new FoodTruck();
        foodTruck.setLocationId(foodTruckProxy.getLocationId());
        foodTruck.setLocationDescription(foodTruckProxy.getLocationDescription());
        foodTruck.setApplicant(foodTruckProxy.getApplicant());
        foodTruck.setFacilityType(foodTruckProxy.getFacilityType());
        foodTruck.setCnn(foodTruckProxy.getCnn());
        foodTruck.setFoodItems(List.of(StringUtils.split(",", foodTruckProxy.getFoodItems())));
        foodTruck.setDaysHours(foodTruckProxy.getDaysHours());
        foodTruck.setApproved(foodTruckProxy.getApproved());
        foodTruck.setReceived(foodTruckProxy.getReceived());
        foodTruck.setPriorPermit(foodTruckProxy.getPriorPermit());
        foodTruck.setExpirationDate(foodTruckProxy.getExpirationDate());
        foodTruck.setPermit(foodTruckProxy.getPermit());
        foodTruck.setStatus(foodTruckProxy.getStatus());
        return foodTruck;
    }

    public static FoodTruck toFoodTruck(FoodTruckEntity entity,
                                        FoodTruckAuditEntity auditEntity,
                                        AddressEntity addressEntity) {
        if (Objects.isNull(entity))  {
            return null;
        }
        FoodTruck foodTruck =  new FoodTruck();
        foodTruck.setLocationId(entity.getLocationId());
        foodTruck.setLocationDescription(entity.getLocationDescription());
        foodTruck.setApplicant(entity.getApplicant());
        foodTruck.setFacilityType(entity.getFacilityType());
        foodTruck.setCnn(foodTruck.getCnn());
        foodTruck.setFoodItems(List.of(Objects.requireNonNull(StringUtils.split(entity.getFoodItems(), ","))));
        foodTruck.setDaysHours(entity.getDaysHours());

        if (Objects.nonNull(auditEntity)) {
            foodTruck.setApproved(auditEntity.getApproved());
            foodTruck.setReceived(auditEntity.getReceived());
            foodTruck.setPriorPermit(auditEntity.getPriorPermit());
            foodTruck.setExpirationDate(auditEntity.getExpirationDate());
            foodTruck.setPermit(auditEntity.getPermit());
            foodTruck.setStatus(auditEntity.getStatus());
        }

        if (Objects.nonNull(addressEntity)) {
            foodTruck.setBlock(addressEntity.getBlock());
            foodTruck.setLot(addressEntity.getLot());
            foodTruck.setBlockLot(addressEntity.getBlock()+""+ addressEntity.getLot());
            foodTruck.setLatitude(addressEntity.getLatitude());
            foodTruck.setLongitude(addressEntity.getLongitude());
            foodTruck.setLocation(addressEntity.getLatitude()+","+ addressEntity.getLongitude());
            foodTruck.setAddress(foodTruck.getAddress());
            foodTruck.setFirePreventionDistricts(addressEntity.getFirePreventionDistricts());
            foodTruck.setPoliceDistricts(addressEntity.getPoliceDistricts());
            foodTruck.setSupervisorDistricts(addressEntity.getSupervisorDistricts());
            foodTruck.setZipCode(addressEntity.getZipCode());
            foodTruck.setNeighborhoods(addressEntity.getNeighborhoods());
        }
        return foodTruck;
    }

    public static FoodTruckAuditEntity toAuditEntity(FoodTruckCreateForm foodTruck) {
        FoodTruckAuditEntity entity = new FoodTruckAuditEntity();
        entity.setReceived(ZonedDateTime.now(ZoneOffset.UTC));
        entity.setStatus(FoodTruckStatus.REQUESTED.toString());
        return entity;
    }

    public static FoodTruckEntity toFoodTruckEntity(FoodTruckCreateForm foodTruck) {
        FoodTruckEntity entity = new FoodTruckEntity();
        entity.setLocationDescription(foodTruck.getLocationDescription());
        entity.setApplicant(foodTruck.getApplicant());
        entity.setFacilityType(foodTruck.getFacilityType());
        entity.setCnn(foodTruck.getCnn());
        entity.setFoodItems(String.join(",", foodTruck.getFoodItems()));
        entity.setDaysHours(foodTruck.getDaysHours());
        return entity;
    }

    // If there are too many properties, you can use external utility classes
    public static AddressEntity toAddressEntity(FoodTruckCreateForm foodTruck) {
        AddressEntity entity = new AddressEntity();
        entity.setBlock(foodTruck.getBlock());
        entity.setLot(foodTruck.getLot());
        entity.setLatitude(foodTruck.getLatitude());
        entity.setLongitude(foodTruck.getLongitude());
        entity.setAddress(foodTruck.getAddress());
        entity.setFirePreventionDistricts(foodTruck.getFirePreventionDistricts());
        entity.setPoliceDistricts(foodTruck.getPoliceDistricts());
        entity.setSupervisorDistricts(foodTruck.getSupervisorDistricts());
        entity.setZipCode(foodTruck.getZipCode());
        entity.setNeighborhoods(foodTruck.getNeighborhoods());
        return entity;
    }
}
