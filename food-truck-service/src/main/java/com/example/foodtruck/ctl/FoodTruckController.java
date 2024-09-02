package com.example.foodtruck.ctl;

import com.example.foodtruck.api.FoodTruckService;
import com.example.foodtruck.api.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class FoodTruckController {

    @Autowired
    private FoodTruckService foodTruckService;

    @PostMapping("/food-trucks")
    public ResponseEntity<FoodTruck> createFoodTruck(@RequestBody FoodTruckCreateForm foodTruck) {
        FoodTruck createdTruck = foodTruckService.createFoodTruck(foodTruck);
        return new ResponseEntity<>(createdTruck, HttpStatus.CREATED);
    }

    @GetMapping("/food-trucks/{location-id}")
    public ResponseEntity<FoodTruck> getFoodTruck(@PathVariable(name = "location-id") Integer locationId) {
        FoodTruck foodTruck = foodTruckService.getFoodTruckByLocation(locationId);
        if (Objects.isNull(foodTruck)) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foodTruck, HttpStatus.OK);
    }

    /*
     Retrieves a list of all food trucks by pagination.
     This api is used by background management system
     */
    @GetMapping("/food-trucks")
    public ResponseEntity<List<FoodTruck>> listFoodTrucks(
            @RequestParam(name = "applicant") String applicant,
            @RequestParam(name = "status") String status,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "page-size", defaultValue = "20") Integer pageSize) {
        FoodTruckVisitForm form = new FoodTruckVisitForm();
        form.setApplicant(applicant);
        form.setStatus(status);

        form.setPage(page);
        form.setPageSize(pageSize);

        List<FoodTruck> foodTrucks = foodTruckService.getFoodTrucks(form);
        return new ResponseEntity<>(foodTrucks, HttpStatus.OK);
    }

    /*
     Retrieves a list of all food trucks by location and food item.
     This api is used by consumer product system
     */
    @GetMapping("/search/food-trucks")
    public ResponseEntity<List<FoodTruck>> searchFoodTrucks(
            @RequestParam(name = "location") String location,
            @RequestParam(name = "food-item") String foodItem) {
        FoodTruckSearchForm form = new FoodTruckSearchForm();
        form.setLocation(location);
        form.setFoodItem(foodItem);

        List<FoodTruck> foodTrucks = foodTruckService.searchFoodTrucks(form);
        return new ResponseEntity<>(foodTrucks, HttpStatus.OK);
    }

    @PatchMapping("/food-trucks/{location-id}")
    public ResponseEntity<FoodTruck> updateFoodTruck(@PathVariable(name = "location-id") Integer locationId,
                                                     @RequestBody FoodTruckUpdateForm foodTruck) {
        foodTruck.setLocationId(locationId);
        FoodTruck updatedFoodTruck = foodTruckService.updateFoodTruck(foodTruck);
        return new ResponseEntity<>(updatedFoodTruck, HttpStatus.OK);
    }

    @DeleteMapping("/food-trucks/{location-id}")
    public ResponseEntity<FoodTruck> removeFoodTruck(@PathVariable(name = "location-id") Integer locationId) {
        foodTruckService.removeFoodTruckByLocation(locationId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
