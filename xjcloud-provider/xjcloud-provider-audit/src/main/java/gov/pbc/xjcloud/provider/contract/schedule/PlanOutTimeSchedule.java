package gov.pbc.xjcloud.provider.contract.schedule;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import gov.pbc.xjcloud.provider.contract.controller.socket.WebSocketServer;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckListNew;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class PlanOutTimeSchedule {

    @Autowired
    private PlanManagementServiceImpl planManagementService;

//    @Autowired
    private WebSocketServer socketServer;

    /**
     * 十分钟
     */
//    @Scheduled(fixedRate = 1000*60*10)
    public void sendDeadLineCount(){

    }

    /**
     * 频次统计定时任务 五分钟一次
     */
    @Scheduled(fixedRate = 1000*60*5)
    public void sequencyCount(){
        List<PlanCheckList> list = planManagementService.list();
        Map<String, List<PlanCheckList>> collect = list.stream().
                filter(Objects::nonNull)
                .filter(e-> StringUtils.isNotBlank(e.getProjectType())&&StringUtils.isNotBlank(e.getAuditNatureId()))
                .collect(Collectors.groupingBy(e ->e.getProjectType() + e.getAuditNatureId()));
        list.stream().forEach(e->{
            String key = e.getProjectType()+e.getAuditNatureId();
            if(null!=collect.get(key)){
                e.setFrequency(String.valueOf(collect.get(key).size()));
            }
        });
        planManagementService.updateBatchById(list);
        System.out.println(String.format("更新频次成功,共更新 %d 个",list.size()));
    }


}
