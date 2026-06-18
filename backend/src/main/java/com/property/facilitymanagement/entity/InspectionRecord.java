package com.property.facilitymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "inspection_records", indexes = {
    @Index(name = "idx_inspection_facility", columnList = "facility_id"),
    @Index(name = "idx_inspection_date", columnList = "inspection_date"),
    @Index(name = "idx_inspection_building", columnList = "building")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facility_id", nullable = false)
    private Long facilityId;

    @Column(name = "facility_code", length = 50)
    private String facilityCode;

    @Column(name = "facility_name", length = 100)
    private String facilityName;

    @Column(name = "building", nullable = false, length = 50)
    private String building;

    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @Column(name = "appearance_status", length = 20)
    private String appearanceStatus;

    @Column(name = "operation_status", length = 20)
    private String operationStatus;

    @Column(name = "safety_parts", length = 20)
    private String safetyParts;

    @Column(name = "has_abnormal")
    private Boolean hasAbnormal;

    @Column(name = "abnormal_description", length = 500)
    private String abnormalDescription;

    @Column(name = "inspector", length = 50)
    private String inspector;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        if (hasAbnormal == null) {
            hasAbnormal = false;
        }
    }
}