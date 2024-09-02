package com.example.foodtruck.repository;

import com.example.foodtruck.repository.entity.FoodTruckEntity;
import com.example.foodtruck.repository.entity.FoodTruckProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTruckRepository extends JpaRepository<FoodTruckEntity, Integer> {

    @Query(value = "select f.location_id, f.location_desc, f.applicant, f.facility_type, f.cnn, f.food_items, f.service_time, " +
            "a.approval_date, a.apply_date, a.prior_permit, a.expiration_date, a.permit, a.status" +
            "from food_truck f left join food_truck_audit a on a.location_id = f.location_id" +
            "where f.applicant = ?1 and a.status = ?2" +
            "limit ?3 , ?4",
            countQuery = "select count(f.location_id)" +
                    "from food_truck f left join food_truck_audit a on a.location_id = f.location_id" +
                    "where f.applicant = ?1 and a.status = ?2",
            nativeQuery = true)
    Page<FoodTruckProxy> findFoodTruckEntitiesByPagination(String applicant, String status, Integer page, Integer pageSize);

    List<FoodTruckEntity> findByLocationIdIn(List<Integer> locationIds);
}
