package gov.pbc.xjcloud.provider.contract.schedule;

import gov.pbc.xjcloud.provider.contract.controller.socket.WebSocketServer;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
//@EnableScheduling
public class PlanOutTimeSchedule {

//    @Autowired
    private PlanManagementServiceImpl planManagementService;

//    @Autowired
    private WebSocketServer socketServer;

    /**
     * 十分钟
     */
//    @Scheduled(fixedRate = 1000*60*10)
    public void sendDeadLineCount(){

    }


}
