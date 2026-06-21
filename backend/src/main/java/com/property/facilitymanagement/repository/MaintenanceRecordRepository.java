package com.property.facilitymanagement.repository;

import com.property.facilitymanagement.entity.MaintenanceRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByFacilityId(Long facilityId);
    List<MaintenanceRecord> findByStatus(String status);
    List<MaintenanceRecord> findByRepairDateBetween(LocalDate startDate, LocalDate endDate);
    List<MaintenanceRecord> findByAcceptanceStatus(String acceptanceStatus);
    List<MaintenanceRecord> findByFacilityIdOrderByCreatedAtDesc(Long facilityId, Pageable pageable);
    default Optional<MaintenanceRecord> findLatestByFacilityId(Long facilityId) {
        List<MaintenanceRecord> records = findByFacilityIdOrderByCreatedAtDesc(facilityId, org.springframework.data.domain.PageRequest.of(0, 1));
        return records.isEmpty() ? Optional.empty() : Optional.of(records.get(0));
    }
}