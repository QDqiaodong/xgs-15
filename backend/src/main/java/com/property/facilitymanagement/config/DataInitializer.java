package com.property.facilitymanagement.config;

import com.property.facilitymanagement.entity.AnnualCycle;
import com.property.facilitymanagement.entity.InspectionTemplate;
import com.property.facilitymanagement.repository.AnnualCycleRepository;
import com.property.facilitymanagement.repository.InspectionTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AnnualCycleRepository annualCycleRepository;

    @Autowired
    private InspectionTemplateRepository inspectionTemplateRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeAnnualCycles();
        initializeInspectionTemplates();
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
}