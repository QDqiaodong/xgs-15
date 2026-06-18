package com.property.facilitymanagement.service;

import com.property.facilitymanagement.entity.InspectionTemplate;
import com.property.facilitymanagement.repository.InspectionTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InspectionTemplateService {

    @Autowired
    private InspectionTemplateRepository inspectionTemplateRepository;

    public List<InspectionTemplate> getAllInspectionTemplates() {
        return inspectionTemplateRepository.findAll();
    }

    public Optional<InspectionTemplate> getInspectionTemplateById(Long id) {
        return inspectionTemplateRepository.findById(id);
    }

    @Cacheable(value = "inspectionTemplate", key = "#facilityType")
    public Optional<InspectionTemplate> getInspectionTemplateByFacilityType(String facilityType) {
        return inspectionTemplateRepository.findByFacilityType(facilityType);
    }

    public List<InspectionTemplate> getActiveInspectionTemplates() {
        return inspectionTemplateRepository.findByIsActive(true);
    }

    public InspectionTemplate saveInspectionTemplate(InspectionTemplate template) {
        return inspectionTemplateRepository.save(template);
    }

    public InspectionTemplate updateInspectionTemplate(Long id, InspectionTemplate details) {
        InspectionTemplate template = inspectionTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspection template not found"));
        template.setFacilityType(details.getFacilityType());
        template.setTemplateName(details.getTemplateName());
        template.setInspectionItems(details.getInspectionItems());
        template.setDescription(details.getDescription());
        template.setIsActive(details.getIsActive());
        return inspectionTemplateRepository.save(template);
    }

    public void deleteInspectionTemplate(Long id) {
        inspectionTemplateRepository.deleteById(id);
    }
}