package com.property.facilitymanagement.controller;

import com.property.facilitymanagement.entity.MaintenanceRecord;
import com.property.facilitymanagement.service.MaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "*")
public class MaintenanceRecordController {

    @Autowired
    private MaintenanceRecordService maintenanceRecordService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @GetMapping
    public ResponseEntity<List<MaintenanceRecord>> getAllMaintenanceRecords() {
        return ResponseEntity.ok(maintenanceRecordService.getAllMaintenanceRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecord> getMaintenanceRecordById(@PathVariable Long id) {
        return maintenanceRecordService.getMaintenanceRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<MaintenanceRecord>> getMaintenanceRecordsByFacilityId(@PathVariable Long facilityId) {
        return ResponseEntity.ok(maintenanceRecordService.getMaintenanceRecordsByFacilityId(facilityId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MaintenanceRecord>> getMaintenanceRecordsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(maintenanceRecordService.getMaintenanceRecordsByStatus(status));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<MaintenanceRecord>> getMaintenanceRecordsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok(maintenanceRecordService.getMaintenanceRecordsByDateRange(start, end));
    }

    @GetMapping("/acceptance/{acceptanceStatus}")
    public ResponseEntity<List<MaintenanceRecord>> getMaintenanceRecordsByAcceptanceStatus(@PathVariable String acceptanceStatus) {
        return ResponseEntity.ok(maintenanceRecordService.getMaintenanceRecordsByAcceptanceStatus(acceptanceStatus));
    }

    @PostMapping
    public ResponseEntity<MaintenanceRecord> createMaintenanceRecord(@RequestBody MaintenanceRecord record) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceRecordService.saveMaintenanceRecord(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecord> updateMaintenanceRecord(@PathVariable Long id, @RequestBody MaintenanceRecord record) {
        try {
            return ResponseEntity.ok(maintenanceRecordService.updateMaintenanceRecord(id, record));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceRecord(@PathVariable Long id) {
        maintenanceRecordService.deleteMaintenanceRecord(id);
        return ResponseEntity.noContent().build();
    }
}