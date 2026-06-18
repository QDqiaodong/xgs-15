package com.property.facilitymanagement.repository;

import com.property.facilitymanagement.entity.AnnualCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnualCycleRepository extends JpaRepository<AnnualCycle, Long> {
    Optional<AnnualCycle> findByFacilityType(String facilityType);
    List<AnnualCycle> findByFacilityCategory(String facilityCategory);
}