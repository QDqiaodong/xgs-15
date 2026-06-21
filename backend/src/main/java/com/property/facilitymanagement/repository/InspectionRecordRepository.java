package com.property.facilitymanagement.repository;

import com.property.facilitymanagement.entity.InspectionRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InspectionRecordRepository extends JpaRepository<InspectionRecord, Long> {
    List<InspectionRecord> findByFacilityId(Long facilityId);
    List<InspectionRecord> findByBuilding(String building);
    List<InspectionRecord> findByInspectionDateBetween(LocalDate startDate, LocalDate endDate);
    List<InspectionRecord> findByHasAbnormal(Boolean hasAbnormal);
    List<InspectionRecord> findByBuildingAndInspectionDateBetween(String building, LocalDate startDate, LocalDate endDate);
    List<InspectionRecord> findByFacilityIdOrderByInspectionDateDesc(Long facilityId, Pageable pageable);
    default Optional<InspectionRecord> findLatestByFacilityId(Long facilityId) {
        List<InspectionRecord> records = findByFacilityIdOrderByInspectionDateDesc(facilityId, org.springframework.data.domain.PageRequest.of(0, 1));
        return records.isEmpty() ? Optional.empty() : Optional.of(records.get(0));
    }
}