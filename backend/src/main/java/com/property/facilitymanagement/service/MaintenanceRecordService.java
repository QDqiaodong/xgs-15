package com.property.facilitymanagement.service;

import com.property.facilitymanagement.entity.MaintenanceRecord;
import com.property.facilitymanagement.repository.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceRecordService {

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceRecordRepository.findAll();
    }

    public Optional<MaintenanceRecord> getMaintenanceRecordById(Long id) {
        return maintenanceRecordRepository.findById(id);
    }

    public List<MaintenanceRecord> getMaintenanceRecordsByFacilityId(Long facilityId) {
        return maintenanceRecordRepository.findByFacilityId(facilityId);
    }

    public List<MaintenanceRecord> getMaintenanceRecordsByStatus(String status) {
        return maintenanceRecordRepository.findByStatus(status);
    }

    public List<MaintenanceRecord> getMaintenanceRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        return maintenanceRecordRepository.findByRepairDateBetween(startDate, endDate);
    }

    public List<MaintenanceRecord> getMaintenanceRecordsByAcceptanceStatus(String acceptanceStatus) {
        return maintenanceRecordRepository.findByAcceptanceStatus(acceptanceStatus);
    }

    public MaintenanceRecord saveMaintenanceRecord(MaintenanceRecord maintenanceRecord) {
        return maintenanceRecordRepository.save(maintenanceRecord);
    }

    public MaintenanceRecord updateMaintenanceRecord(Long id, MaintenanceRecord details) {
        MaintenanceRecord record = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance record not found"));
        record.setFaultDescription(details.getFaultDescription());
        record.setRepairDate(details.getRepairDate());
        record.setReplacedParts(details.getReplacedParts());
        record.setRepairCost(details.getRepairCost());
        record.setRepairPerson(details.getRepairPerson());
        record.setAcceptanceStatus(details.getAcceptanceStatus());
        record.setAcceptanceDate(details.getAcceptanceDate());
        record.setStatus(details.getStatus());
        record.setRemarks(details.getRemarks());
        return maintenanceRecordRepository.save(record);
    }

    public void deleteMaintenanceRecord(Long id) {
        maintenanceRecordRepository.deleteById(id);
    }
}