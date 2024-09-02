package com.example.foodtruck.repository;

import com.example.foodtruck.repository.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {
    List<AddressEntity> findByLocationIdIn(List<Integer> locationIds);
}