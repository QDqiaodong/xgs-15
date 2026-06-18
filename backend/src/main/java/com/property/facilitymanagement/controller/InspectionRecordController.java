package com.property.facilitymanagement.controller;

import com.property.facilitymanagement.entity.InspectionRecord;
import com.property.facilitymanagement.service.InspectionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/inspections")
@CrossOrigin(origins = "*")
public class InspectionRecordController {

    @Autowired
    private InspectionRecordService inspectionRecordService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @GetMapping
    public ResponseEntity<List<InspectionRecord>> getAllInspectionRecords() {
        return ResponseEntity.ok(inspectionRecordService.getAllInspectionRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionRecord> getInspectionRecordById(@PathVariable Long id) {
        return inspectionRecordService.getInspectionRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<InspectionRecord>> getInspectionRecordsByFacilityId(@PathVariable Long facilityId) {
        return ResponseEntity.ok(inspectionRecordService.getInspectionRecordsByFacilityId(facilityId));
    }

    @GetMapping("/building/{building}")
    public ResponseEntity<List<InspectionRecord>> getInspectionRecordsByBuilding(@PathVariable String building) {
        return ResponseEntity.ok(inspectionRecordService.getInspectionRecordsByBuilding(building));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<InspectionRecord>> getInspectionRecordsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok(inspectionRecordService.getInspectionRecordsByDateRange(start, end));
    }

    @GetMapping("/abnormal")
    public ResponseEntity<List<InspectionRecord>> getInspectionRecordsByAbnormal(@RequestParam Boolean hasAbnormal) {
        return ResponseEntity.ok(inspectionRecordService.getInspectionRecordsByAbnormal(hasAbnormal));
    }

    @GetMapping("/building/{building}/date-range")
    public ResponseEntity<List<InspectionRecord>> getInspectionRecordsByBuildingAndDate(
            @PathVariable String building,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok(inspectionRecordService.getInspectionRecordsByBuildingAndDate(building, start, end));
    }

    @PostMapping
    public ResponseEntity<InspectionRecord> createInspectionRecord(@RequestBody InspectionRecord record) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inspectionRecordService.saveInspectionRecord(record));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<InspectionRecord>> createInspectionRecords(@RequestBody List<InspectionRecord> records) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inspectionRecordService.saveInspectionRecords(records));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InspectionRecord> updateInspectionRecord(@PathVariable Long id, @RequestBody InspectionRecord record) {
        try {
            return ResponseEntity.ok(inspectionRecordService.updateInspectionRecord(id, record));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInspectionRecord(@PathVariable Long id) {
        inspectionRecordService.deleteInspectionRecord(id);
        return ResponseEntity.noContent().build();
    }
}