package com.property.facilitymanagement.repository;

import com.property.facilitymanagement.entity.InspectionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InspectionRecordRepository extends JpaRepository<InspectionRecord, Long> {
    List<InspectionRecord> findByFacilityId(Long facilityId);
    List<InspectionRecord> findByBuilding(String building);
    List<InspectionRecord> findByInspectionDateBetween(LocalDate startDate, LocalDate endDate);
    List<InspectionRecord> findByHasAbnormal(Boolean hasAbnormal);
    List<InspectionRecord> findByBuildingAndInspectionDateBetween(String building, LocalDate startDate, LocalDate endDate);
}