package com.property.facilitymanagement.repository;

import com.property.facilitymanagement.entity.AnnualInspectionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnnualInspectionRecordRepository extends JpaRepository<AnnualInspectionRecord, Long> {
    List<AnnualInspectionRecord> findByFacilityId(Long facilityId);
    List<AnnualInspectionRecord> findByFacilityType(String facilityType);
    List<AnnualInspectionRecord> findByQualificationStatus(String qualificationStatus);
    List<AnnualInspectionRecord> findByInspectionDateBetween(LocalDate startDate, LocalDate endDate);
    List<AnnualInspectionRecord> findByExpiryDateBefore(LocalDate date);
}