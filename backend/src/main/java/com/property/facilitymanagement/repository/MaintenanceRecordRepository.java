package com.property.facilitymanagement.repository;

import com.property.facilitymanagement.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByFacilityId(Long facilityId);
    List<MaintenanceRecord> findByStatus(String status);
    List<MaintenanceRecord> findByRepairDateBetween(LocalDate startDate, LocalDate endDate);
    List<MaintenanceRecord> findByAcceptanceStatus(String acceptanceStatus);
}