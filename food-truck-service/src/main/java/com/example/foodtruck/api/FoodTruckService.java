package com.example.foodtruck.api;

import com.example.foodtruck.api.domain.*;

import java.util.List;

public interface FoodTruckService {

    FoodTruck createFoodTruck(FoodTruckCreateForm foodTruck);

    FoodTruck getFoodTruckByLocation(Integer locationId);

    FoodTruck updateFoodTruck(FoodTruckUpdateForm foodTruck);

    void removeFoodTruckByLocation(Integer locationId);

    List<FoodTruck> getFoodTrucks(FoodTruckVisitForm form);

    List<FoodTruck> searchFoodTrucks(FoodTruckSearchForm form);
}
