package com.example.foodtruck.repository;

import com.example.foodtruck.repository.entity.FoodTruckAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTruckAuditRepository extends JpaRepository<FoodTruckAuditEntity, Integer> {
    List<FoodTruckAuditEntity> findByLocationIdIn(List<Integer> locationIds);
}
