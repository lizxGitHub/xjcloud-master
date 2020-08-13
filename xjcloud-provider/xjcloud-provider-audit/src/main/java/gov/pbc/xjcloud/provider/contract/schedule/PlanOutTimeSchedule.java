package gov.pbc.xjcloud.provider.contract.schedule;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import gov.pbc.xjcloud.provider.contract.aop.AopTip;
import gov.pbc.xjcloud.provider.contract.config.AuditTipConfiguration;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.constants.OverTimeConstants;
import gov.pbc.xjcloud.provider.contract.dto.PlanCheckListDTO;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.PlanOverTimeTip;
import gov.pbc.xjcloud.provider.contract.exceptions.AppException;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.feign.dept.RemoteDeptService;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author PaungMiao
 */
@Component
@EnableScheduling
@Slf4j
@SuppressWarnings("all")
public class PlanOutTimeSchedule {

    @Resource
    private PlanManagementServiceImpl planManagementService;
    @Resource
    private RemoteDeptService deptService;
    @Resource
    private AuditActivitiService auditActivitiService;
    @Resource
    private UserCenterService userCenterService;
    @Resource
    private AuditTipConfiguration tipConfiguration;

    @Autowired
    private AopTip aopTip;


    @Value("${audit.tip.key:auditTip}")
    private String tipKey;

    /**
     * 十分钟
     */
//    @Scheduled(fixedRate = 1000*60*10)
    public void sendDeadLineCount() {

    }

    /**
     *
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void sequencyCount() {
        List<PlanCheckList> list = planManagementService.list();
        Map<String, List<PlanCheckList>> collect = list.stream().
                filter(Objects::nonNull)
                .filter(e -> StringUtils.isNotBlank(e.getProjectType()) && StringUtils.isNotBlank(e.getAuditNatureId()))
                .collect(Collectors.groupingBy(e -> e.getProjectType() + e.getAuditNatureId()));
        list.stream().forEach(e -> {
            String key = e.getProjectType() + e.getAuditNatureId();
            if (null != collect.get(key)) {
                e.setFrequency(String.valueOf(collect.get(key).size()));
            }
        });
        if (list.isEmpty()) {
            return;
        }
        planManagementService.updateBatchById(list);
        System.out.println(String.format("更新频次成功,共更新 %d 个", list.size()));
    }

    /**
     * 频次统计定时任务 10分钟一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void sequencyOvertimeTip() {
        System.out.println("hehehehehhehe");
        Map<String, Object> params = Maps.newHashMap();
        //未归档 且超时的项目
        List<PlanCheckListDTO> deadLineList = planManagementService.findDeadlinePlanList();
        deadLineList.stream().filter(Objects::nonNull).forEach(e -> {
            int days = e.getDays();
            // TODO #苗 暂时不计算真实天数
            int month = (int) (Math.floor(days / 30.0));
//            int month = (int)(Math.random()*10);
            //超时两个月
            if (month == 2) {
                submitTask(e, OverTimeConstants.TYPE_1);
                //超时三个月 不超过6个月
            } else if (month >= 3 && month < 6) {
                submitTask(e, OverTimeConstants.TYPE_2);
            } else if (month >= 6) {
                submitTask(e, OverTimeConstants.TYPE_3);
            }
        });
    }

    private void submitTask(PlanCheckListDTO e, int overType) {
        PlanOverTimeTip dtoObj = new PlanOverTimeTip();
        dtoObj.setPlanId(e.getPlanId());
        dtoObj.setOverType(overType);
        PlanOverTimeTip timeTip = planManagementService.findCheckListTip(dtoObj);
        PlanOverTimeTip tip = new PlanOverTimeTip();
        tip.setOverType(overType);
        tip.setDelFlag(DelConstants.EXITED);
        tip.setPlanId(e.getPlanId());
        R auditDept = deptService.dept(e.getAuditId());
        R impDept = deptService.dept(e.getImpId());
        Map auditParentDept = (Map) deptService.dept(e.getAuditParentId()).getData();
        Map impParentDept = (Map) deptService.dept(e.getImpParentId()).getData();
        Map auditDeptObj = (Map) auditDept.getData();
        Map impDeptObj = (Map) impDept.getData();
        String name = MapUtil.getStr(auditDeptObj, "name");
        //联系领导
        String contactLeaderIdKey = "contactLeaderId";
        // 协管领导
        String assistLeaderIdKey = "assistLeaderId";
        // 分管领导
        String chargeLeaderIdKey = "chargeLeaderId";
        Integer contractLeader = MapUtil.getInt(auditDeptObj, contactLeaderIdKey);
        Integer contractParentLeader = MapUtil.getInt(auditParentDept, contactLeaderIdKey);
        Integer impContractLeader = MapUtil.getInt(impDeptObj, contactLeaderIdKey);
        // 分管领导
        Integer chargeLeader = MapUtil.getInt(auditDeptObj, chargeLeaderIdKey);
        Integer impChargeLeader = MapUtil.getInt(impDeptObj, chargeLeaderIdKey);
        //
        if (null != timeTip) {
            log.info("已发送提醒");
            return;
        }
        aopTip.iniToken();
        try {
            switch (overType) {
                case 1:
                    //同级审批 2个月  推送至被审计部门的分管行领导，即整改部门的分管行领导
                    if (e.getImpParentId().equals(e.getAuditParentId())) {
                        doSameLevelTip(e, overType, tip);
                    } else { //上审下 整改超时2个月，推送至被审计部门的分管行领导，即整改部门的分管行领导（下级单位）
                        tip.setTipAssignee(chargeLeader);
                    }
                    break;
                case 2:
                    //同级审批 3个月  推送至内审部门的协管行领导，无协管行领导推送至分管行领导
                    if (e.getImpParentId().equals(e.getAuditParentId())) {
                        tip.setTipAssignee(chargeLeader);
                    } else { //上审下 整改超时3个月，推送至被审计单位的行长（下级单位）
                        Integer bankLeader = getBankLeader(e.getAuditParentId());
                        tip.setTipAssignee(bankLeader);
                    }
                    break;
                case 3:
                    //同级审批 6个月 整改超时6个月，推送至行长
                    if (e.getImpParentId().equals(e.getAuditParentId())) {
                        Integer bankLeader = getBankLeader(e.getAuditParentId());
                        tip.setTipAssignee(bankLeader);
                    } else { //上审下 整改超时6个月，推送至上级行对下级行的联系行领导
                        tip.setTipAssignee(contractParentLeader);
                    }
                    break;
            }
        } catch (AppException appException) {
            log.error(appException.getMessage());
            appException.printStackTrace();
            return;
        }
        planManagementService.insertTip(tip);
        auditActivitiService.start(tipKey, tip.getId(), JSONObject.toJSONString(tip));
        log.info(JSONObject.toJSONString(tip));
        aopTip.after();
    }

    /**
     * 获取被审计部门的行长
     *
     * @param auditParentId
     * @return
     */
    public Integer getBankLeader(Integer auditParentId) {
        List list = (List) userCenterService.getUsersByRoleNameAndDept(auditParentId, tipConfiguration.getBankLeaderRole()).getData();
        if (null == list || list.isEmpty()) {
            log.error(String.format("%s 暂未设置行长", auditParentId));
            return null;
        }
        return Integer.parseInt(((Map) list.get(0)).get("userId").toString());
    }

    /**
     * 同级审批
     *
     * @param e
     * @param overType
     * @param tip
     */
    private void doSameLevelTip(PlanCheckListDTO e, int overType, PlanOverTimeTip tip) {
        //是否是县支行，如果是，先推送县支行消息
        R dept = deptService.dept(e.getAuditId());
        Map deptObj = (Map) dept.getData();
        String name = MapUtil.getStr(deptObj, "name");
        //联系领导
        Integer contractLeader = MapUtil.getInt(deptObj, "contactLeaderId");
        // 分管领导
        Integer chargeLeader = MapUtil.getInt(deptObj, "chargeLeaderId");
        if (name.contains("县支行")) {
            tip.setTipAssignee(contractLeader);
        } else {
            tip.setTipAssignee(chargeLeader);
        }
    }


}
