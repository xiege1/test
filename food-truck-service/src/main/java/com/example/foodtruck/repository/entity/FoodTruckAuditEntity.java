package com.example.foodtruck.repository.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Table(schema = "assessment", name = "food_truck_audit")
public class FoodTruckAuditEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "approval_date")
    private ZonedDateTime approved;
    @Column(name = "apply_date")
    private ZonedDateTime received;
    @Column(name = "prior_permit")
    private Byte priorPermit;
    @Column(name = "expiration_date")
    private ZonedDateTime expirationDate;
    @Column(name = "permit")
    private String permit;
    @Column(name = "status")
    private String status;
}
