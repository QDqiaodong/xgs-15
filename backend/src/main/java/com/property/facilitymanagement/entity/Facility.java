package com.property.facilitymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "facilities", indexes = {
    @Index(name = "idx_facility_type", columnList = "facility_type"),
    @Index(name = "idx_building", columnList = "building"),
    @Index(name = "idx_floor", columnList = "floor"),
    @Index(name = "idx_facility_code", columnList = "facility_code")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facility_code", unique = true, nullable = false, length = 50)
    private String facilityCode;

    @Column(name = "facility_name", nullable = false, length = 100)
    private String facilityName;

    @Column(name = "facility_type", nullable = false, length = 50)
    private String facilityType;

    @Column(name = "building", nullable = false, length = 50)
    private String building;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "installation_date")
    private LocalDate installationDate;

    @Column(name = "annual_inspection_cycle")
    private Integer annualInspectionCycle;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Column(name = "model", length = 50)
    private String model;

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
            status = "正常";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}