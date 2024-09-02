package com.example.foodtruck.repository.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(schema = "assessment", name = "address")
public class AddressEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "block")
    private Integer block;
    @Column(name = "lot")
    private Integer lot;
    @Column(name = "lat")
    private Double latitude;
    @Column(name = "lon")
    private Double longitude;
    @Column(name = "address")
    private String address;
    @Column(name = "fire_prevention_district")
    private Integer firePreventionDistricts;
    @Column(name = "police_district")
    private Integer policeDistricts;
    @Column(name = "supervisor_district")
    private Integer supervisorDistricts;
    @Column(name = "zipcode")
    private Integer zipCode;
    @Column(name = "neighborhoods")
    private Integer neighborhoods;
}
