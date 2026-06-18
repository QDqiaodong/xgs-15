package com.property.facilitymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "maintenance_records", indexes = {
    @Index(name = "idx_maintenance_facility", columnList = "facility_id"),
    @Index(name = "idx_maintenance_date", columnList = "repair_date"),
    @Index(name = "idx_maintenance_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facility_id", nullable = false)
    private Long facilityId;

    @Column(name = "facility_code", length = 50)
    private String facilityCode;

    @Column(name = "facility_name", length = 100)
    private String facilityName;

    @Column(name = "fault_description", nullable = false, length = 500)
    private String faultDescription;

    @Column(name = "repair_date")
    private LocalDate repairDate;

    @Column(name = "replaced_parts", length = 200)
    private String replacedParts;

    @Column(name = "repair_cost")
    private Double repairCost;

    @Column(name = "repair_person", length = 50)
    private String repairPerson;

    @Column(name = "acceptance_status", length = 20)
    private String acceptanceStatus;

    @Column(name = "acceptance_date")
    private LocalDate acceptanceDate;

    @Column(name = "status", length = 20)
    private String status;

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
        if (status == null) {
            status = "待维修";
        }
        if (acceptanceStatus == null) {
            acceptanceStatus = "未验收";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}