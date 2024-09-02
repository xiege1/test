package com.example.foodtruck.api;

import com.example.foodtruck.api.domain.*;
import com.example.foodtruck.dai.FoodTruckDai;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheFoodTruckService implements FoodTruckService {

    private final FoodTruckDai dai;

    @Override
    public FoodTruck createFoodTruck(FoodTruckCreateForm foodTruck) {
        return dai.createFoodTruck(foodTruck);
    }

    @Override
    public FoodTruck getFoodTruckByLocation(Integer locationId) {
        return dai.getFoodTruckByLocation(locationId);
    }

    @Override
    public FoodTruck updateFoodTruck(FoodTruckUpdateForm foodTruck) {
        return dai.updateFoodTruck(foodTruck);
    }

    @Override
    public void removeFoodTruckByLocation(Integer locationId) {
        dai.removeFoodTruckByLocation(locationId);
    }

    @Override
    public List<FoodTruck> getFoodTrucks(FoodTruckVisitForm form) {
        return dai.getFoodTrucks(form);
    }

    @Override
    public List<FoodTruck> searchFoodTrucks(FoodTruckSearchForm form) {
        return dai.searchFoodTrucks(form);
    }
}
