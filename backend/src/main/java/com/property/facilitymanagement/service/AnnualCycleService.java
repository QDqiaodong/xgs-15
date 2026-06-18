package com.property.facilitymanagement.service;

import com.property.facilitymanagement.entity.AnnualCycle;
import com.property.facilitymanagement.repository.AnnualCycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnualCycleService {

    @Autowired
    private AnnualCycleRepository annualCycleRepository;

    public List<AnnualCycle> getAllAnnualCycles() {
        return annualCycleRepository.findAll();
    }

    public Optional<AnnualCycle> getAnnualCycleById(Long id) {
        return annualCycleRepository.findById(id);
    }

    @Cacheable(value = "annualCycle", key = "#facilityType")
    public Optional<AnnualCycle> getAnnualCycleByFacilityType(String facilityType) {
        return annualCycleRepository.findByFacilityType(facilityType);
    }

    public List<AnnualCycle> getAnnualCyclesByCategory(String category) {
        return annualCycleRepository.findByFacilityCategory(category);
    }

    public AnnualCycle saveAnnualCycle(AnnualCycle cycle) {
        return annualCycleRepository.save(cycle);
    }

    public AnnualCycle updateAnnualCycle(Long id, AnnualCycle details) {
        AnnualCycle cycle = annualCycleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annual cycle not found"));
        cycle.setFacilityType(details.getFacilityType());
        cycle.setFacilityCategory(details.getFacilityCategory());
        cycle.setCycleMonths(details.getCycleMonths());
        cycle.setRegulatoryRequirement(details.getRegulatoryRequirement());
        cycle.setDescription(details.getDescription());
        return annualCycleRepository.save(cycle);
    }

    public void deleteAnnualCycle(Long id) {
        annualCycleRepository.deleteById(id);
    }
}