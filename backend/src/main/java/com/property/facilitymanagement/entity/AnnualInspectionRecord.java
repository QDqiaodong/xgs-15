package com.property.facilitymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "annual_inspection_records", indexes = {
    @Index(name = "idx_annual_facility", columnList = "facility_id"),
    @Index(name = "idx_annual_date", columnList = "inspection_date"),
    @Index(name = "idx_annual_status", columnList = "qualification_status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualInspectionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facility_id", nullable = false)
    private Long facilityId;

    @Column(name = "facility_code", length = 50)
    private String facilityCode;

    @Column(name = "facility_name", length = 100)
    private String facilityName;

    @Column(name = "facility_type", length = 50)
    private String facilityType;

    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @Column(name = "inspection_agency", nullable = false, length = 100)
    private String inspectionAgency;

    @Column(name = "inspector", length = 50)
    private String inspector;

    @Column(name = "qualification_status", length = 20)
    private String qualificationStatus;

    @Column(name = "report_number", length = 50)
    private String reportNumber;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "inspection_items", length = 500)
    private String inspectionItems;

    @Column(name = "defects_found", length = 500)
    private String defectsFound;

    @Column(name = "rectification_status", length = 20)
    private String rectificationStatus;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
        if (qualificationStatus == null) {
            qualificationStatus = "待检测";
        }
        if (rectificationStatus == null) {
            rectificationStatus = "无缺陷";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}