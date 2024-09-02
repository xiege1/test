package com.example.foodtruck.repository.entity;


import java.time.ZonedDateTime;

public interface FoodTruckProxy {

    Integer getLocationId();
    String getLocationDescription();
    String getApplicant();
    String getFacilityType();
    String getCnn();
    String getFoodItems();
    String getDaysHours();
    ZonedDateTime getApproved();
    ZonedDateTime getReceived();
    Byte getPriorPermit();
    ZonedDateTime getExpirationDate();
    String getPermit();
    String getStatus();
}
