package com.example.foodtruck.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class FoodTruckCreateForm {

    private String locationDescription;
    private String applicant;
    private String facilityType;
    private String cnn;

    private Integer block;
    private Integer lot;

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
