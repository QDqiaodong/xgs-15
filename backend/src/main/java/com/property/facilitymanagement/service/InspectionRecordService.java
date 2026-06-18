package com.property.facilitymanagement.service;

import com.property.facilitymanagement.entity.InspectionRecord;
import com.property.facilitymanagement.repository.InspectionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InspectionRecordService {

    @Autowired
    private InspectionRecordRepository inspectionRecordRepository;

    public List<InspectionRecord> getAllInspectionRecords() {
        return inspectionRecordRepository.findAll();
    }

    public Optional<InspectionRecord> getInspectionRecordById(Long id) {
        return inspectionRecordRepository.findById(id);
    }

    public List<InspectionRecord> getInspectionRecordsByFacilityId(Long facilityId) {
        return inspectionRecordRepository.findByFacilityId(facilityId);
    }

    public List<InspectionRecord> getInspectionRecordsByBuilding(String building) {
        return inspectionRecordRepository.findByBuilding(building);
    }

    public List<InspectionRecord> getInspectionRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        return inspectionRecordRepository.findByInspectionDateBetween(startDate, endDate);
    }

    public List<InspectionRecord> getInspectionRecordsByAbnormal(Boolean hasAbnormal) {
        return inspectionRecordRepository.findByHasAbnormal(hasAbnormal);
    }

    public List<InspectionRecord> getInspectionRecordsByBuildingAndDate(String building, LocalDate startDate, LocalDate endDate) {
        return inspectionRecordRepository.findByBuildingAndInspectionDateBetween(building, startDate, endDate);
    }

    public InspectionRecord saveInspectionRecord(InspectionRecord inspectionRecord) {
        return inspectionRecordRepository.save(inspectionRecord);
    }

    public List<InspectionRecord> saveInspectionRecords(List<InspectionRecord> records) {
        return inspectionRecordRepository.saveAll(records);
    }

    public InspectionRecord updateInspectionRecord(Long id, InspectionRecord details) {
        InspectionRecord record = inspectionRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspection record not found"));
        record.setAppearanceStatus(details.getAppearanceStatus());
        record.setOperationStatus(details.getOperationStatus());
        record.setSafetyParts(details.getSafetyParts());
        record.setHasAbnormal(details.getHasAbnormal());
        record.setAbnormalDescription(details.getAbnormalDescription());
        record.setInspector(details.getInspector());
        record.setRemarks(details.getRemarks());
        return inspectionRecordRepository.save(record);
    }

    public void deleteInspectionRecord(Long id) {
        inspectionRecordRepository.deleteById(id);
    }
}