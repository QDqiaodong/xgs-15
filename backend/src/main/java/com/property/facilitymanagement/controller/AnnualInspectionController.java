package com.property.facilitymanagement.controller;

import com.property.facilitymanagement.entity.AnnualInspectionRecord;
import com.property.facilitymanagement.service.AnnualInspectionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/annual-inspection")
@CrossOrigin(origins = "*")
public class AnnualInspectionController {

    @Autowired
    private AnnualInspectionRecordService annualInspectionRecordService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @GetMapping
    public ResponseEntity<List<AnnualInspectionRecord>> getAllAnnualInspectionRecords() {
        return ResponseEntity.ok(annualInspectionRecordService.getAllAnnualInspectionRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnualInspectionRecord> getAnnualInspectionRecordById(@PathVariable Long id) {
        return annualInspectionRecordService.getAnnualInspectionRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<AnnualInspectionRecord>> getAnnualInspectionRecordsByFacilityId(@PathVariable Long facilityId) {
        return ResponseEntity.ok(annualInspectionRecordService.getAnnualInspectionRecordsByFacilityId(facilityId));
    }

    @GetMapping("/type/{facilityType}")
    public ResponseEntity<List<AnnualInspectionRecord>> getAnnualInspectionRecordsByFacilityType(@PathVariable String facilityType) {
        return ResponseEntity.ok(annualInspectionRecordService.getAnnualInspectionRecordsByFacilityType(facilityType));
    }

    @GetMapping("/status/{qualificationStatus}")
    public ResponseEntity<List<AnnualInspectionRecord>> getAnnualInspectionRecordsByQualificationStatus(@PathVariable String qualificationStatus) {
        return ResponseEntity.ok(annualInspectionRecordService.getAnnualInspectionRecordsByQualificationStatus(qualificationStatus));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AnnualInspectionRecord>> getAnnualInspectionRecordsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return ResponseEntity.ok(annualInspectionRecordService.getAnnualInspectionRecordsByDateRange(start, end));
    }

    @GetMapping("/expired")
    public ResponseEntity<List<AnnualInspectionRecord>> getExpiredAnnualInspectionRecords(@RequestParam(required = false) String date) {
        LocalDate queryDate = date != null ? LocalDate.parse(date, formatter) : LocalDate.now();
        return ResponseEntity.ok(annualInspectionRecordService.getExpiredAnnualInspectionRecords(queryDate));
    }

    @PostMapping
    public ResponseEntity<AnnualInspectionRecord> createAnnualInspectionRecord(@RequestBody AnnualInspectionRecord record) {
        return ResponseEntity.status(HttpStatus.CREATED).body(annualInspectionRecordService.saveAnnualInspectionRecord(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnualInspectionRecord> updateAnnualInspectionRecord(@PathVariable Long id, @RequestBody AnnualInspectionRecord record) {
        try {
            return ResponseEntity.ok(annualInspectionRecordService.updateAnnualInspectionRecord(id, record));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnualInspectionRecord(@PathVariable Long id) {
        annualInspectionRecordService.deleteAnnualInspectionRecord(id);
        return ResponseEntity.noContent().build();
    }
}