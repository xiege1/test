package com.example.foodtruck.repository.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(schema = "assessment", name = "food_truck")
public class FoodTruckEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "location_desc")
    private String locationDescription;
    @Column(name = "applicant")
    private String applicant;
    @Column(name = "facility_type")
    private String facilityType;
    @Column(name = "cnn")
    private String cnn;
    @Column(name = "food_items")
    private String foodItems;
    @Column(name = "service_time")
    private String daysHours;
}
