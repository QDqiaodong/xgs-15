package com.property.facilitymanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO {

    private Long id;
    private String facilityCode;
    private String facilityName;
    private String facilityType;
    private String building;
    private Integer floor;
    private String location;
    private LocalDate installationDate;
    private Integer annualInspectionCycle;
    private String manufacturer;
    private String model;
    private String status;
    private String remarks;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private LocalDate nextAnnualInspectionDate;
    private String latestInspectionResult;
    private String latestMaintenanceStatus;
    private Boolean isSupervised;
}
