package com.property.facilitymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "inspection_templates", indexes = {
    @Index(name = "idx_template_type", columnList = "facility_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facility_type", nullable = false, length = 50)
    private String facilityType;

    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;

    @Column(name = "inspection_items", columnDefinition = "TEXT")
    private String inspectionItems;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}