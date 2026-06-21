package com.property.facilitymanagement.controller;

import com.property.facilitymanagement.entity.Facility;
import com.property.facilitymanagement.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@CrossOrigin(origins = "*")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping
    public ResponseEntity<List<Facility>> getAllFacilities() {
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable Long id) {
        return facilityService.getFacilityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{facilityCode}")
    public ResponseEntity<Facility> getFacilityByCode(@PathVariable String facilityCode) {
        return facilityService.getFacilityByCode(facilityCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/building/{building}")
    public ResponseEntity<List<Facility>> getFacilitiesByBuilding(@PathVariable String building) {
        return ResponseEntity.ok(facilityService.getFacilitiesByBuilding(building));
    }

    @GetMapping("/building/{building}/floor/{floor}")
    public ResponseEntity<List<Facility>> getFacilitiesByBuildingAndFloor(
            @PathVariable String building,
            @PathVariable Integer floor) {
        return ResponseEntity.ok(facilityService.getFacilitiesByBuildingAndFloor(building, floor));
    }

    @GetMapping("/type/{facilityType}")
    public ResponseEntity<List<Facility>> getFacilitiesByType(@PathVariable String facilityType) {
        return ResponseEntity.ok(facilityService.getFacilitiesByType(facilityType));
    }

    @GetMapping("/building/{building}/type/{facilityType}")
    public ResponseEntity<List<Facility>> getFacilitiesByBuildingAndType(
            @PathVariable String building,
            @PathVariable String facilityType) {
        return ResponseEntity.ok(facilityService.getFacilitiesByBuildingAndType(building, facilityType));
    }

    @GetMapping("/building/{building}/floor/{floor}/type/{facilityType}")
    public ResponseEntity<List<Facility>> getFacilitiesByBuildingFloorAndType(
            @PathVariable String building,
            @PathVariable Integer floor,
            @PathVariable String facilityType) {
        return ResponseEntity.ok(facilityService.getFacilitiesByBuildingFloorAndType(building, floor, facilityType));
    }

    @PostMapping
    public ResponseEntity<Facility> createFacility(@RequestBody Facility facility) {
        if (facilityService.existsByCode(facility.getFacilityCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.saveFacility(facility));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facility> updateFacility(@PathVariable Long id, @RequestBody Facility facility) {
        try {
            return ResponseEntity.ok(facilityService.updateFacility(id, facility));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }
}