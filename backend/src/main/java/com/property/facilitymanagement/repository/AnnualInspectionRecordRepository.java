package com.property.facilitymanagement.repository;

import com.property.facilitymanagement.entity.AnnualInspectionRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnnualInspectionRecordRepository extends JpaRepository<AnnualInspectionRecord, Long> {
    List<AnnualInspectionRecord> findByFacilityId(Long facilityId);
    List<AnnualInspectionRecord> findByFacilityType(String facilityType);
    List<AnnualInspectionRecord> findByQualificationStatus(String qualificationStatus);
    List<AnnualInspectionRecord> findByInspectionDateBetween(LocalDate startDate, LocalDate endDate);
    List<AnnualInspectionRecord> findByExpiryDateBefore(LocalDate date);
    List<AnnualInspectionRecord> findByFacilityIdOrderByInspectionDateDesc(Long facilityId, Pageable pageable);
    default Optional<AnnualInspectionRecord> findLatestByFacilityId(Long facilityId) {
        List<AnnualInspectionRecord> records = findByFacilityIdOrderByInspectionDateDesc(facilityId, org.springframework.data.domain.PageRequest.of(0, 1));
        return records.isEmpty() ? Optional.empty() : Optional.of(records.get(0));
    }
}