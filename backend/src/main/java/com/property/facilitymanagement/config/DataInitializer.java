package com.property.facilitymanagement.config;

import com.property.facilitymanagement.entity.AnnualCycle;
import com.property.facilitymanagement.entity.Facility;
import com.property.facilitymanagement.entity.InspectionTemplate;
import com.property.facilitymanagement.repository.AnnualCycleRepository;
import com.property.facilitymanagement.repository.FacilityRepository;
import com.property.facilitymanagement.repository.InspectionTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AnnualCycleRepository annualCycleRepository;

    @Autowired
    private InspectionTemplateRepository inspectionTemplateRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeAnnualCycles();
        initializeInspectionTemplates();
        initializeFacilities();
    }

    private void initializeAnnualCycles() {
        if (annualCycleRepository.count() == 0) {
            List<AnnualCycle> cycles = new ArrayList<>();
            
            AnnualCycle c1 = new AnnualCycle();
            c1.setFacilityType("电梯");
            c1.setFacilityCategory("特种设备");
            c1.setCycleMonths(12);
            c1.setRegulatoryRequirement("根据《特种设备安全法》，电梯每年必须进行一次定期检验");
            c1.setDescription("电梯年度检验周期");
            cycles.add(c1);

            AnnualCycle c2 = new AnnualCycle();
            c2.setFacilityType("压力容器");
            c2.setFacilityCategory("特种设备");
            c2.setCycleMonths(12);
            c2.setRegulatoryRequirement("根据《特种设备安全法》，压力容器每年必须进行一次定期检验");
            c2.setDescription("压力容器年度检验周期");
            cycles.add(c2);

            AnnualCycle c3 = new AnnualCycle();
            c3.setFacilityType("消防柜");
            c3.setFacilityCategory("消防设备");
            c3.setCycleMonths(12);
            c3.setRegulatoryRequirement("根据《消防法》，消防设施每年至少进行一次全面检测");
            c3.setDescription("消防柜年度检验周期");
            cycles.add(c3);

            AnnualCycle c4 = new AnnualCycle();
            c4.setFacilityType("水泵");
            c4.setFacilityCategory("给排水设备");
            c4.setCycleMonths(12);
            c4.setRegulatoryRequirement("根据行业标准，水泵每年进行一次全面维护保养");
            c4.setDescription("水泵年度检验周期");
            cycles.add(c4);

            AnnualCycle c5 = new AnnualCycle();
            c5.setFacilityType("监控摄像头");
            c5.setFacilityCategory("安防设备");
            c5.setCycleMonths(24);
            c5.setRegulatoryRequirement("监控设备每两年进行一次专业检测");
            c5.setDescription("监控设备检验周期");
            cycles.add(c5);

            AnnualCycle c6 = new AnnualCycle();
            c6.setFacilityType("照明设施");
            c6.setFacilityCategory("电气设备");
            c6.setCycleMonths(12);
            c6.setRegulatoryRequirement("照明设施每年进行一次安全检查");
            c6.setDescription("照明设施检验周期");
            cycles.add(c6);

            annualCycleRepository.saveAll(cycles);
        }
    }

    private void initializeInspectionTemplates() {
        if (inspectionTemplateRepository.count() == 0) {
            List<InspectionTemplate> templates = new ArrayList<>();

            InspectionTemplate t1 = new InspectionTemplate();
            t1.setFacilityType("电梯");
            t1.setTemplateName("电梯月度巡检模板");
            t1.setInspectionItems("[\"外观检查\",\"运行状态\",\"门系统\",\"安全部件\",\"报警系统\",\"轿厢内部\",\"机房环境\"]");
            t1.setDescription("电梯月度例行巡检项目");
            t1.setIsActive(true);
            templates.add(t1);

            InspectionTemplate t2 = new InspectionTemplate();
            t2.setFacilityType("消防柜");
            t2.setTemplateName("消防柜月度巡检模板");
            t2.setInspectionItems("[\"外观检查\",\"灭火器压力\",\"消防栓完好性\",\"报警按钮\",\"指示灯\",\"设备清洁\"]");
            t2.setDescription("消防柜月度例行巡检项目");
            t2.setIsActive(true);
            templates.add(t2);

            InspectionTemplate t3 = new InspectionTemplate();
            t3.setFacilityType("水泵");
            t3.setTemplateName("水泵月度巡检模板");
            t3.setInspectionItems("[\"外观检查\",\"运行噪音\",\"轴承温度\",\"密封状况\",\"电气控制\",\"润滑状态\",\"进出口压力\"]");
            t3.setDescription("水泵月度例行巡检项目");
            t3.setIsActive(true);
            templates.add(t3);

            InspectionTemplate t4 = new InspectionTemplate();
            t4.setFacilityType("监控摄像头");
            t4.setTemplateName("监控设备月度巡检模板");
            t4.setInspectionItems("[\"画面清晰度\",\"录像功能\",\"夜视效果\",\"云台控制\",\"存储状态\",\"网络连接\",\"供电状态\"]");
            t4.setDescription("监控设备月度例行巡检项目");
            t4.setIsActive(true);
            templates.add(t4);

            InspectionTemplate t5 = new InspectionTemplate();
            t5.setFacilityType("照明设施");
            t5.setTemplateName("照明设施月度巡检模板");
            t5.setInspectionItems("[\"灯具完好性\",\"亮度检查\",\"线路状况\",\"开关功能\",\"应急照明\",\"接地保护\"]");
            t5.setDescription("照明设施月度例行巡检项目");
            t5.setIsActive(true);
            templates.add(t5);

            inspectionTemplateRepository.saveAll(templates);
        }
    }

    private void initializeFacilities() {
        if (facilityRepository.count() == 0) {
            List<Facility> facilities = new ArrayList<>();

            // A栋 - 1楼
            Facility f1 = new Facility();
            f1.setFacilityCode("DT-A-001");
            f1.setFacilityName("A栋1号客梯");
            f1.setFacilityType("电梯");
            f1.setBuilding("A栋");
            f1.setFloor(1);
            f1.setLocation("1楼东侧电梯厅");
            f1.setInstallationDate(LocalDate.of(2020, 3, 15));
            f1.setAnnualInspectionCycle(12);
            f1.setManufacturer("三菱电机");
            f1.setModel("LEHY-III");
            f1.setStatus("正常");
            facilities.add(f1);

            Facility f2 = new Facility();
            f2.setFacilityCode("DT-A-002");
            f2.setFacilityName("A栋2号客梯");
            f2.setFacilityType("电梯");
            f2.setBuilding("A栋");
            f2.setFloor(1);
            f2.setLocation("1楼西侧电梯厅");
            f2.setInstallationDate(LocalDate.of(2020, 3, 15));
            f2.setAnnualInspectionCycle(12);
            f2.setManufacturer("三菱电机");
            f2.setModel("LEHY-III");
            f2.setStatus("正常");
            facilities.add(f2);

            Facility f3 = new Facility();
            f3.setFacilityCode("XF-A-101");
            f3.setFacilityName("A栋1楼消防柜1号");
            f3.setFacilityType("消防柜");
            f3.setBuilding("A栋");
            f3.setFloor(1);
            f3.setLocation("1楼大厅北侧");
            f3.setInstallationDate(LocalDate.of(2019, 10, 20));
            f3.setAnnualInspectionCycle(12);
            f3.setManufacturer("海湾消防");
            f3.setModel("SG24A65");
            f3.setStatus("正常");
            facilities.add(f3);

            Facility f4 = new Facility();
            f4.setFacilityCode("XF-A-102");
            f4.setFacilityName("A栋1楼消防柜2号");
            f4.setFacilityType("消防柜");
            f4.setBuilding("A栋");
            f4.setFloor(1);
            f4.setLocation("1楼大厅南侧");
            f4.setInstallationDate(LocalDate.of(2019, 10, 20));
            f4.setAnnualInspectionCycle(12);
            f4.setManufacturer("海湾消防");
            f4.setModel("SG24A65");
            f4.setStatus("正常");
            facilities.add(f4);

            Facility f5 = new Facility();
            f5.setFacilityCode("JK-A-101");
            f5.setFacilityName("A栋1楼监控1号");
            f5.setFacilityType("监控摄像头");
            f5.setBuilding("A栋");
            f5.setFloor(1);
            f5.setLocation("1楼大厅入口");
            f5.setInstallationDate(LocalDate.of(2021, 5, 10));
            f5.setAnnualInspectionCycle(24);
            f5.setManufacturer("海康威视");
            f5.setModel("DS-2CD2T47WD-L");
            f5.setStatus("正常");
            facilities.add(f5);

            Facility f6 = new Facility();
            f6.setFacilityCode("JK-A-102");
            f6.setFacilityName("A栋1楼监控2号");
            f6.setFacilityType("监控摄像头");
            f6.setBuilding("A栋");
            f6.setFloor(1);
            f6.setLocation("1楼电梯厅");
            f6.setInstallationDate(LocalDate.of(2021, 5, 10));
            f6.setAnnualInspectionCycle(24);
            f6.setManufacturer("海康威视");
            f6.setModel("DS-2CD2T47WD-L");
            f6.setStatus("正常");
            facilities.add(f6);

            // A栋 - 2楼
            Facility f7 = new Facility();
            f7.setFacilityCode("XF-A-201");
            f7.setFacilityName("A栋2楼消防柜1号");
            f7.setFacilityType("消防柜");
            f7.setBuilding("A栋");
            f7.setFloor(2);
            f7.setLocation("2楼东侧走廊");
            f7.setInstallationDate(LocalDate.of(2019, 10, 20));
            f7.setAnnualInspectionCycle(12);
            f7.setManufacturer("海湾消防");
            f7.setModel("SG24A65");
            f7.setStatus("正常");
            facilities.add(f7);

            Facility f8 = new Facility();
            f8.setFacilityCode("XF-A-202");
            f8.setFacilityName("A栋2楼消防柜2号");
            f8.setFacilityType("消防柜");
            f8.setBuilding("A栋");
            f8.setFloor(2);
            f8.setLocation("2楼西侧走廊");
            f8.setInstallationDate(LocalDate.of(2019, 10, 20));
            f8.setAnnualInspectionCycle(12);
            f8.setManufacturer("海湾消防");
            f8.setModel("SG24A65");
            f8.setStatus("维护中");
            facilities.add(f8);

            Facility f9 = new Facility();
            f9.setFacilityCode("JK-A-201");
            f9.setFacilityName("A栋2楼监控1号");
            f9.setFacilityType("监控摄像头");
            f9.setBuilding("A栋");
            f9.setFloor(2);
            f9.setLocation("2楼走廊东");
            f9.setInstallationDate(LocalDate.of(2021, 5, 10));
            f9.setAnnualInspectionCycle(24);
            f9.setManufacturer("海康威视");
            f9.setModel("DS-2CD2T47WD-L");
            f9.setStatus("正常");
            facilities.add(f9);

            Facility f10 = new Facility();
            f10.setFacilityCode("ZM-A-201");
            f10.setFacilityName("A栋2楼照明1号");
            f10.setFacilityType("照明设施");
            f10.setBuilding("A栋");
            f10.setFloor(2);
            f10.setLocation("2楼办公区");
            f10.setInstallationDate(LocalDate.of(2020, 8, 5));
            f10.setAnnualInspectionCycle(12);
            f10.setManufacturer("欧普照明");
            f10.setModel("LED面板灯600x600");
            f10.setStatus("正常");
            facilities.add(f10);

            // A栋 - 3楼
            Facility f11 = new Facility();
            f11.setFacilityCode("SB-A-301");
            f11.setFacilityName("A栋3楼水泵1号");
            f11.setFacilityType("水泵");
            f11.setBuilding("A栋");
            f11.setFloor(3);
            f11.setLocation("3楼设备间");
            f11.setInstallationDate(LocalDate.of(2018, 12, 1));
            f11.setAnnualInspectionCycle(12);
            f11.setManufacturer("格兰富");
            f11.setModel("CR15-2");
            f11.setStatus("正常");
            facilities.add(f11);

            Facility f12 = new Facility();
            f12.setFacilityCode("XF-A-301");
            f12.setFacilityName("A栋3楼消防柜1号");
            f12.setFacilityType("消防柜");
            f12.setBuilding("A栋");
            f12.setFloor(3);
            f12.setLocation("3楼走廊");
            f12.setInstallationDate(LocalDate.of(2019, 10, 20));
            f12.setAnnualInspectionCycle(12);
            f12.setManufacturer("海湾消防");
            f12.setModel("SG24A65");
            f12.setStatus("正常");
            facilities.add(f12);

            Facility f13 = new Facility();
            f13.setFacilityCode("JK-A-301");
            f13.setFacilityName("A栋3楼监控1号");
            f13.setFacilityType("监控摄像头");
            f13.setBuilding("A栋");
            f13.setFloor(3);
            f13.setLocation("3楼设备间门口");
            f13.setInstallationDate(LocalDate.of(2021, 5, 10));
            f13.setAnnualInspectionCycle(24);
            f13.setManufacturer("海康威视");
            f13.setModel("DS-2CD2T47WD-L");
            f13.setStatus("故障");
            facilities.add(f13);

            // B栋 - 1楼
            Facility f14 = new Facility();
            f14.setFacilityCode("DT-B-001");
            f14.setFacilityName("B栋1号货梯");
            f14.setFacilityType("电梯");
            f14.setBuilding("B栋");
            f14.setFloor(1);
            f14.setLocation("1楼货梯厅");
            f14.setInstallationDate(LocalDate.of(2019, 6, 20));
            f14.setAnnualInspectionCycle(12);
            f14.setManufacturer("通力电梯");
            f14.setModel("KONE MonoSpace");
            f14.setStatus("正常");
            facilities.add(f14);

            Facility f15 = new Facility();
            f15.setFacilityCode("XF-B-101");
            f15.setFacilityName("B栋1楼消防柜1号");
            f15.setFacilityType("消防柜");
            f15.setBuilding("B栋");
            f15.setFloor(1);
            f15.setLocation("1楼仓库入口");
            f15.setInstallationDate(LocalDate.of(2019, 10, 20));
            f15.setAnnualInspectionCycle(12);
            f15.setManufacturer("海湾消防");
            f15.setModel("SG24A65");
            f15.setStatus("正常");
            facilities.add(f15);

            Facility f16 = new Facility();
            f16.setFacilityCode("SB-B-101");
            f16.setFacilityName("B栋1楼消防水泵");
            f16.setFacilityType("水泵");
            f16.setBuilding("B栋");
            f16.setFloor(1);
            f16.setLocation("1楼泵房");
            f16.setInstallationDate(LocalDate.of(2018, 10, 15));
            f16.setAnnualInspectionCycle(12);
            f16.setManufacturer("上海凯泉");
            f16.setModel("XBD8/20");
            f16.setStatus("正常");
            facilities.add(f16);

            // B栋 - 2楼
            Facility f17 = new Facility();
            f17.setFacilityCode("XF-B-201");
            f17.setFacilityName("B栋2楼消防柜1号");
            f17.setFacilityType("消防柜");
            f17.setBuilding("B栋");
            f17.setFloor(2);
            f17.setLocation("2楼生产区");
            f17.setInstallationDate(LocalDate.of(2019, 10, 20));
            f17.setAnnualInspectionCycle(12);
            f17.setManufacturer("海湾消防");
            f17.setModel("SG24A65");
            f17.setStatus("正常");
            facilities.add(f17);

            Facility f18 = new Facility();
            f18.setFacilityCode("JK-B-201");
            f18.setFacilityName("B栋2楼监控1号");
            f18.setFacilityType("监控摄像头");
            f18.setBuilding("B栋");
            f18.setFloor(2);
            f18.setLocation("2楼生产区入口");
            f18.setInstallationDate(LocalDate.of(2021, 5, 10));
            f18.setAnnualInspectionCycle(24);
            f18.setManufacturer("海康威视");
            f18.setModel("DS-2CD2T47WD-L");
            f18.setStatus("正常");
            facilities.add(f18);

            // B栋 - 3楼
            Facility f19 = new Facility();
            f19.setFacilityCode("YL-B-301");
            f19.setFacilityName("B栋3楼压力容器1号");
            f19.setFacilityType("压力容器");
            f19.setBuilding("B栋");
            f19.setFloor(3);
            f19.setLocation("3楼生产车间");
            f19.setInstallationDate(LocalDate.of(2020, 1, 10));
            f19.setAnnualInspectionCycle(12);
            f19.setManufacturer("中集集团");
            f19.setModel("C21.2-2.5");
            f19.setStatus("正常");
            facilities.add(f19);

            Facility f20 = new Facility();
            f20.setFacilityCode("XF-B-301");
            f20.setFacilityName("B栋3楼消防柜1号");
            f20.setFacilityType("消防柜");
            f20.setBuilding("B栋");
            f20.setFloor(3);
            f20.setLocation("3楼车间东侧");
            f20.setInstallationDate(LocalDate.of(2019, 10, 20));
            f20.setAnnualInspectionCycle(12);
            f20.setManufacturer("海湾消防");
            f20.setModel("SG24A65");
            f20.setStatus("正常");
            facilities.add(f20);

            facilityRepository.saveAll(facilities);
        }
    }
}