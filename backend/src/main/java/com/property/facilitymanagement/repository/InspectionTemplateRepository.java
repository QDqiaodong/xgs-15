package com.property.facilitymanagement.repository;

import com.property.facilitymanagement.entity.InspectionTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InspectionTemplateRepository extends JpaRepository<InspectionTemplate, Long> {
    Optional<InspectionTemplate> findByFacilityType(String facilityType);
    List<InspectionTemplate> findByIsActive(Boolean isActive);
}