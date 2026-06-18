package com.property.facilitymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "annual_cycles", indexes = {
    @Index(name = "idx_cycle_type", columnList = "facility_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facility_type", nullable = false, length = 50)
    private String facilityType;

    @Column(name = "facility_category", length = 50)
    private String facilityCategory;

    @Column(name = "cycle_months", nullable = false)
    private Integer cycleMonths;

    @Column(name = "regulatory_requirement", length = 500)
    private String regulatoryRequirement;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}