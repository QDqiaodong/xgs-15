package com.property.facilitymanagement.service;

import com.property.facilitymanagement.dto.FacilityDTO;
import com.property.facilitymanagement.entity.AnnualInspectionRecord;
import com.property.facilitymanagement.entity.Facility;
import com.property.facilitymanagement.entity.InspectionRecord;
import com.property.facilitymanagement.entity.MaintenanceRecord;
import com.property.facilitymanagement.repository.AnnualInspectionRecordRepository;
import com.property.facilitymanagement.repository.FacilityRepository;
import com.property.facilitymanagement.repository.InspectionRecordRepository;
import com.property.facilitymanagement.repository.MaintenanceRecordRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private InspectionRecordRepository inspectionRecordRepository;

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Autowired
    private AnnualInspectionRecordRepository annualInspectionRecordRepository;

    private static final Map<String, String> TYPE_CODE_MAP = new HashMap<>();
    private static final Set<String> SUPERVISED_TYPES = new HashSet<>();

    static {
        TYPE_CODE_MAP.put("电梯", "DT");
        TYPE_CODE_MAP.put("消防柜", "XF");
        TYPE_CODE_MAP.put("水泵", "SB");
        TYPE_CODE_MAP.put("监控摄像头", "JK");
        TYPE_CODE_MAP.put("照明设施", "ZM");
        TYPE_CODE_MAP.put("压力容器", "YL");

        SUPERVISED_TYPES.add("电梯");
        SUPERVISED_TYPES.add("消防柜");
        SUPERVISED_TYPES.add("压力容器");
    }

    public List<FacilityDTO> getAllFacilities() {
        return facilityRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Facility> getFacilityById(Long id) {
        return facilityRepository.findById(id);
    }

    public Optional<FacilityDTO> getFacilityDTOById(Long id) {
        return facilityRepository.findById(id).map(this::convertToDTO);
    }

    @Cacheable(value = "facility", key = "#facilityCode")
    public Optional<Facility> getFacilityByCode(String facilityCode) {
        return facilityRepository.findByFacilityCode(facilityCode);
    }

    public List<FacilityDTO> getFacilitiesByBuilding(String building) {
        return facilityRepository.findByBuilding(building).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FacilityDTO> getFacilitiesByBuildingAndFloor(String building, Integer floor) {
        return facilityRepository.findByBuildingAndFloor(building, floor).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FacilityDTO> getFacilitiesByType(String facilityType) {
        return facilityRepository.findByFacilityType(facilityType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FacilityDTO> getFacilitiesByBuildingAndType(String building, String facilityType) {
        return facilityRepository.findByBuildingAndFacilityType(building, facilityType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FacilityDTO> getFacilitiesByBuildingFloorAndType(String building, Integer floor, String facilityType) {
        return facilityRepository.findByBuildingAndFloorAndFacilityType(building, floor, facilityType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "facility", key = "#facility.facilityCode")
    public Facility saveFacility(Facility facility) {
        validateFacilityCode(facility);
        return facilityRepository.save(facility);
    }

    @CacheEvict(value = "facility", allEntries = true)
    public Facility updateFacility(Long id, Facility facilityDetails) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facility not found"));
        String oldCode = facility.getFacilityCode();
        String newCode = facilityDetails.getFacilityCode();
        if (newCode != null && !newCode.equals(oldCode)) {
            validateFacilityCode(facilityDetails);
            Optional<Facility> existingWithCode = facilityRepository.findByFacilityCode(newCode);
            if (existingWithCode.isPresent() && !existingWithCode.get().getId().equals(id)) {
                throw new IllegalStateException("Facility code already exists");
            }
        }
        facility.setFacilityCode(facilityDetails.getFacilityCode());
        facility.setFacilityName(facilityDetails.getFacilityName());
        facility.setFacilityType(facilityDetails.getFacilityType());
        facility.setBuilding(facilityDetails.getBuilding());
        facility.setFloor(facilityDetails.getFloor());
        facility.setLocation(facilityDetails.getLocation());
        facility.setInstallationDate(facilityDetails.getInstallationDate());
        facility.setAnnualInspectionCycle(facilityDetails.getAnnualInspectionCycle());
        facility.setManufacturer(facilityDetails.getManufacturer());
        facility.setModel(facilityDetails.getModel());
        facility.setStatus(facilityDetails.getStatus());
        facility.setRemarks(facilityDetails.getRemarks());
        return facilityRepository.save(facility);
    }

    @CacheEvict(value = "facility", allEntries = true)
    public void deleteFacility(Long id) {
        facilityRepository.deleteById(id);
    }

    public boolean existsByCode(String facilityCode) {
        return facilityRepository.existsByFacilityCode(facilityCode);
    }

    public void validateFacilityCode(Facility facility) {
        String code = facility.getFacilityCode();
        String type = facility.getFacilityType();
        String building = facility.getBuilding();

        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("设施编号不能为空");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("设施类型不能为空");
        }
        if (building == null || building.trim().isEmpty()) {
            throw new IllegalArgumentException("所在楼栋不能为空");
        }

        String typeCode = TYPE_CODE_MAP.get(type);
        if (typeCode == null) {
            throw new IllegalArgumentException("未知的设施类型: " + type);
        }

        String buildingCode = building.replaceAll("[^0-9A-Za-z]", "");
        if (buildingCode.isEmpty()) {
            throw new IllegalArgumentException("楼栋名称必须包含字母或数字");
        }

        String regex = "^" + typeCode + "-" + Pattern.quote(buildingCode) + "-\\d{3,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                String.format("设施编号格式错误，应为: %s-%s-XXX(至少3位序号)", typeCode, buildingCode)
            );
        }
    }

    private FacilityDTO convertToDTO(Facility facility) {
        FacilityDTO dto = new FacilityDTO();
        BeanUtils.copyProperties(facility, dto);

        dto.setIsSupervised(SUPERVISED_TYPES.contains(facility.getFacilityType()));
        dto.setNextAnnualInspectionDate(calculateNextAnnualInspectionDate(facility));
        dto.setLatestInspectionResult(getLatestInspectionResult(facility.getId()));
        dto.setLatestMaintenanceStatus(getLatestMaintenanceStatus(facility.getId()));

        return dto;
    }

    private LocalDate calculateNextAnnualInspectionDate(Facility facility) {
        Optional<AnnualInspectionRecord> latestAnnual = annualInspectionRecordRepository
                .findLatestByFacilityId(facility.getId());

        if (latestAnnual.isPresent() && latestAnnual.get().getInspectionDate() != null) {
            Integer cycle = facility.getAnnualInspectionCycle() != null ? facility.getAnnualInspectionCycle() : 12;
            return latestAnnual.get().getInspectionDate().plusMonths(cycle);
        }

        if (facility.getInstallationDate() != null) {
            Integer cycle = facility.getAnnualInspectionCycle() != null ? facility.getAnnualInspectionCycle() : 12;
            return facility.getInstallationDate().plusMonths(cycle);
        }

        return null;
    }

    private String getLatestInspectionResult(Long facilityId) {
        Optional<InspectionRecord> latest = inspectionRecordRepository.findLatestByFacilityId(facilityId);
        if (latest.isPresent()) {
            InspectionRecord record = latest.get();
            if (Boolean.TRUE.equals(record.getHasAbnormal())) {
                return "异常";
            }
            boolean allNormal = "正常".equals(record.getAppearanceStatus())
                    && "正常".equals(record.getOperationStatus())
                    && "正常".equals(record.getSafetyParts());
            return allNormal ? "正常" : "待整改";
        }
        return "未巡检";
    }

    private String getLatestMaintenanceStatus(Long facilityId) {
        Optional<MaintenanceRecord> latest = maintenanceRecordRepository.findLatestByFacilityId(facilityId);
        return latest.map(MaintenanceRecord::getStatus).orElse("无维修记录");
    }
}
