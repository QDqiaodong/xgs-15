package com.property.facilitymanagement.service;

import com.property.facilitymanagement.entity.AnnualInspectionRecord;
import com.property.facilitymanagement.repository.AnnualInspectionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AnnualInspectionRecordService {

    @Autowired
    private AnnualInspectionRecordRepository annualInspectionRecordRepository;

    public List<AnnualInspectionRecord> getAllAnnualInspectionRecords() {
        return annualInspectionRecordRepository.findAll();
    }

    public Optional<AnnualInspectionRecord> getAnnualInspectionRecordById(Long id) {
        return annualInspectionRecordRepository.findById(id);
    }

    public List<AnnualInspectionRecord> getAnnualInspectionRecordsByFacilityId(Long facilityId) {
        return annualInspectionRecordRepository.findByFacilityId(facilityId);
    }

    public List<AnnualInspectionRecord> getAnnualInspectionRecordsByFacilityType(String facilityType) {
        return annualInspectionRecordRepository.findByFacilityType(facilityType);
    }

    public List<AnnualInspectionRecord> getAnnualInspectionRecordsByQualificationStatus(String qualificationStatus) {
        return annualInspectionRecordRepository.findByQualificationStatus(qualificationStatus);
    }

    public List<AnnualInspectionRecord> getAnnualInspectionRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        return annualInspectionRecordRepository.findByInspectionDateBetween(startDate, endDate);
    }

    public List<AnnualInspectionRecord> getExpiredAnnualInspectionRecords(LocalDate date) {
        return annualInspectionRecordRepository.findByExpiryDateBefore(date);
    }

    public AnnualInspectionRecord saveAnnualInspectionRecord(AnnualInspectionRecord record) {
        return annualInspectionRecordRepository.save(record);
    }

    public AnnualInspectionRecord updateAnnualInspectionRecord(Long id, AnnualInspectionRecord details) {
        AnnualInspectionRecord record = annualInspectionRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annual inspection record not found"));
        record.setInspectionDate(details.getInspectionDate());
        record.setInspectionAgency(details.getInspectionAgency());
        record.setInspector(details.getInspector());
        record.setQualificationStatus(details.getQualificationStatus());
        record.setReportNumber(details.getReportNumber());
        record.setExpiryDate(details.getExpiryDate());
        record.setInspectionItems(details.getInspectionItems());
        record.setDefectsFound(details.getDefectsFound());
        record.setRectificationStatus(details.getRectificationStatus());
        record.setRemarks(details.getRemarks());
        return annualInspectionRecordRepository.save(record);
    }

    public void deleteAnnualInspectionRecord(Long id) {
        annualInspectionRecordRepository.deleteById(id);
    }
}