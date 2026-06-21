package com.property.facilitymanagement.repository;

import com.property.facilitymanagement.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Optional<Facility> findByFacilityCode(String facilityCode);
    List<Facility> findByBuilding(String building);
    List<Facility> findByBuildingAndFloor(String building, Integer floor);
    List<Facility> findByFacilityType(String facilityType);
    List<Facility> findByBuildingAndFacilityType(String building, String facilityType);
    List<Facility> findByBuildingAndFloorAndFacilityType(String building, Integer floor, String facilityType);
    List<Facility> findByStatus(String status);
    boolean existsByFacilityCode(String facilityCode);
}