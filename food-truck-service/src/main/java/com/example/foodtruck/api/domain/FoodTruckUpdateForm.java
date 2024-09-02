package com.example.foodtruck.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class FoodTruckUpdateForm {

    private Integer locationId;

    private List<String> foodItems;

    private Double latitude;
    private Double longitude;

    private String daysHours;

    private Integer firePreventionDistricts;
    private Integer policeDistricts;
    private Integer supervisorDistricts;

    private String address;
    private Integer zipCode;
    private Integer neighborhoods;
}
