package com.property.facilitymanagement.controller;

import com.property.facilitymanagement.dto.FacilityDTO;
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
    public ResponseEntity<List<FacilityDTO>> getAllFacilities() {
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityDTO> getFacilityById(@PathVariable Long id) {
        return facilityService.getFacilityDTOById(id)
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
    public ResponseEntity<List<FacilityDTO>> getFacilitiesByBuilding(@PathVariable String building) {
        return ResponseEntity.ok(facilityService.getFacilitiesByBuilding(building));
    }

    @GetMapping("/building/{building}/floor/{floor}")
    public ResponseEntity<List<FacilityDTO>> getFacilitiesByBuildingAndFloor(
            @PathVariable String building,
            @PathVariable Integer floor) {
        return ResponseEntity.ok(facilityService.getFacilitiesByBuildingAndFloor(building, floor));
    }

    @GetMapping("/type/{facilityType}")
    public ResponseEntity<List<FacilityDTO>> getFacilitiesByType(@PathVariable String facilityType) {
        return ResponseEntity.ok(facilityService.getFacilitiesByType(facilityType));
    }

    @GetMapping("/building/{building}/type/{facilityType}")
    public ResponseEntity<List<FacilityDTO>> getFacilitiesByBuildingAndType(
            @PathVariable String building,
            @PathVariable String facilityType) {
        return ResponseEntity.ok(facilityService.getFacilitiesByBuildingAndType(building, facilityType));
    }

    @GetMapping("/building/{building}/floor/{floor}/type/{facilityType}")
    public ResponseEntity<List<FacilityDTO>> getFacilitiesByBuildingFloorAndType(
            @PathVariable String building,
            @PathVariable Integer floor,
            @PathVariable String facilityType) {
        return ResponseEntity.ok(facilityService.getFacilitiesByBuildingFloorAndType(building, floor, facilityType));
    }

    @PostMapping
    public ResponseEntity<?> createFacility(@RequestBody Facility facility) {
        try {
            if (facilityService.existsByCode(facility.getFacilityCode())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("设施编号已存在，禁止重复编号进入台账");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.saveFacility(facility));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFacility(@PathVariable Long id, @RequestBody Facility facility) {
        try {
            return ResponseEntity.ok(facilityService.updateFacility(id, facility));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("设施编号已存在，禁止重复编号进入台账");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
