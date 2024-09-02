package com.example.foodtruck.api.domain;

import lombok.Data;

@Data
public class FoodTruckVisitForm {

    private String applicant;
    private String status;

    private Integer page;
    private Integer pageSize;
}
