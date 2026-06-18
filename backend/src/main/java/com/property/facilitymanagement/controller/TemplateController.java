package com.property.facilitymanagement.controller;

import com.property.facilitymanagement.entity.InspectionTemplate;
import com.property.facilitymanagement.entity.AnnualCycle;
import com.property.facilitymanagement.service.InspectionTemplateService;
import com.property.facilitymanagement.service.AnnualCycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "*")
public class TemplateController {

    @Autowired
    private InspectionTemplateService inspectionTemplateService;

    @Autowired
    private AnnualCycleService annualCycleService;

    @GetMapping("/inspection")
    public ResponseEntity<List<InspectionTemplate>> getAllInspectionTemplates() {
        return ResponseEntity.ok(inspectionTemplateService.getAllInspectionTemplates());
    }

    @GetMapping("/inspection/{id}")
    public ResponseEntity<InspectionTemplate> getInspectionTemplateById(@PathVariable Long id) {
        return inspectionTemplateService.getInspectionTemplateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/inspection/type/{facilityType}")
    public ResponseEntity<InspectionTemplate> getInspectionTemplateByFacilityType(@PathVariable String facilityType) {
        return inspectionTemplateService.getInspectionTemplateByFacilityType(facilityType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/inspection/active")
    public ResponseEntity<List<InspectionTemplate>> getActiveInspectionTemplates() {
        return ResponseEntity.ok(inspectionTemplateService.getActiveInspectionTemplates());
    }

    @PostMapping("/inspection")
    public ResponseEntity<InspectionTemplate> createInspectionTemplate(@RequestBody InspectionTemplate template) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inspectionTemplateService.saveInspectionTemplate(template));
    }

    @PutMapping("/inspection/{id}")
    public ResponseEntity<InspectionTemplate> updateInspectionTemplate(@PathVariable Long id, @RequestBody InspectionTemplate template) {
        try {
            return ResponseEntity.ok(inspectionTemplateService.updateInspectionTemplate(id, template));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/inspection/{id}")
    public ResponseEntity<Void> deleteInspectionTemplate(@PathVariable Long id) {
        inspectionTemplateService.deleteInspectionTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/annual-cycle")
    public ResponseEntity<List<AnnualCycle>> getAllAnnualCycles() {
        return ResponseEntity.ok(annualCycleService.getAllAnnualCycles());
    }

    @GetMapping("/annual-cycle/{id}")
    public ResponseEntity<AnnualCycle> getAnnualCycleById(@PathVariable Long id) {
        return annualCycleService.getAnnualCycleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/annual-cycle/type/{facilityType}")
    public ResponseEntity<AnnualCycle> getAnnualCycleByFacilityType(@PathVariable String facilityType) {
        return annualCycleService.getAnnualCycleByFacilityType(facilityType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/annual-cycle/category/{category}")
    public ResponseEntity<List<AnnualCycle>> getAnnualCyclesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(annualCycleService.getAnnualCyclesByCategory(category));
    }

    @PostMapping("/annual-cycle")
    public ResponseEntity<AnnualCycle> createAnnualCycle(@RequestBody AnnualCycle cycle) {
        return ResponseEntity.status(HttpStatus.CREATED).body(annualCycleService.saveAnnualCycle(cycle));
    }

    @PutMapping("/annual-cycle/{id}")
    public ResponseEntity<AnnualCycle> updateAnnualCycle(@PathVariable Long id, @RequestBody AnnualCycle cycle) {
        try {
            return ResponseEntity.ok(annualCycleService.updateAnnualCycle(id, cycle));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/annual-cycle/{id}")
    public ResponseEntity<Void> deleteAnnualCycle(@PathVariable Long id) {
        annualCycleService.deleteAnnualCycle(id);
        return ResponseEntity.noContent().build();
    }
}