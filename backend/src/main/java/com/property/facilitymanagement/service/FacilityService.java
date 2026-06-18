package com.property.facilitymanagement.service;

import com.property.facilitymanagement.entity.Facility;
import com.property.facilitymanagement.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public Optional<Facility> getFacilityById(Long id) {
        return facilityRepository.findById(id);
    }

    @Cacheable(value = "facility", key = "#facilityCode")
    public Optional<Facility> getFacilityByCode(String facilityCode) {
        return facilityRepository.findByFacilityCode(facilityCode);
    }

    public List<Facility> getFacilitiesByBuilding(String building) {
        return facilityRepository.findByBuilding(building);
    }

    public List<Facility> getFacilitiesByType(String facilityType) {
        return facilityRepository.findByFacilityType(facilityType);
    }

    public List<Facility> getFacilitiesByBuildingAndType(String building, String facilityType) {
        return facilityRepository.findByBuildingAndFacilityType(building, facilityType);
    }

    public Facility saveFacility(Facility facility) {
        return facilityRepository.save(facility);
    }

    public Facility updateFacility(Long id, Facility facilityDetails) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facility not found"));
        facility.setFacilityCode(facilityDetails.getFacilityCode());
        facility.setFacilityName(facilityDetails.getFacilityName());
        facility.setFacilityType(facilityDetails.getFacilityType());
        facility.setBuilding(facilityDetails.getBuilding());
        facility.setLocation(facilityDetails.getLocation());
        facility.setInstallationDate(facilityDetails.getInstallationDate());
        facility.setAnnualInspectionCycle(facilityDetails.getAnnualInspectionCycle());
        facility.setManufacturer(facilityDetails.getManufacturer());
        facility.setModel(facilityDetails.getModel());
        facility.setStatus(facilityDetails.getStatus());
        facility.setRemarks(facilityDetails.getRemarks());
        return facilityRepository.save(facility);
    }

    public void deleteFacility(Long id) {
        facilityRepository.deleteById(id);
    }

    public boolean existsByCode(String facilityCode) {
        return facilityRepository.existsByFacilityCode(facilityCode);
    }
}