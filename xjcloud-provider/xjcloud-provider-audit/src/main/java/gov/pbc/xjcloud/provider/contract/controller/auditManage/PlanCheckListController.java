package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckListNew;
import gov.pbc.xjcloud.provider.contract.entity.PlanTimeTemp;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanFile;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.service.impl.PlanCheckListServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.PlanTimeTempServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.utils.R2;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/audit-api/planCheckList")
public class PlanCheckListController {

    @Resource
    private PlanCheckListServiceImpl planCheckListService;

    @Resource
    private PlanInfoServiceImpl planInfoService;

    @Autowired
    private AuditActivitiService auditActivitiService;

    @Resource
    private PlanTimeTempServiceImpl planTimeTempService;

    @Autowired
    private PlanManagementServiceImpl planManagementService;

    /**
     * 获取审计计划
     *
     * @return
     */
    @ApiOperation("审计页面信息")
    @GetMapping(value = {"page", ""})
    public R<Page<PlanCheckListNew>> index(HttpServletRequest request, PlanCheckListNew query, Page<PlanCheckListNew> page) {
        PageUtil.initPage(page);
        int type = Integer.parseInt(request.getParameter("type"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        String status = request.getParameter("status");
        try {
            page = planCheckListService.selectAll(page, query, type, userId, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    @GetMapping(value = {"statuses", ""})
    public R<Page<PlanCheckListNew>> statuses(PlanCheckListNew query, Page<PlanCheckListNew> page, String statuses) {
        PageUtil.initPage(page);
        String[] arry = statuses.split(",");
        try {
            page = planCheckListService.selectByStatuses(page, query, arry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    @PostMapping("/saveOrEditPlan")
    public R<Boolean> saveOrEditPlan(PlanCheckListNew planCheckList) {
        R<Boolean> r = new R<>();
        try {
            if (planCheckList.getId() == 0) {
                int code = (int) ((Math.random() * 9 + 1) * 1000);
                planCheckList.setProjectCode("PROJECT-" + code);
                Calendar date = Calendar.getInstance();
                String year = String.valueOf(date.get(Calendar.YEAR));
                planCheckList.setAuditYear(year);
            }
            String fileUri= planCheckList.getFileUri();
            planCheckListService.validate(planCheckList, r);//  此处没有对字段添加约束，所以不会生效
            if (planCheckList.getId() == 0) {
                planCheckList.setDelFlag(DelConstants.EXITED);
                planCheckList.setStatus("0");
                planCheckList.setCreatedTime(new Date());
                planCheckListService.saveReturnPK(planCheckList);
                int planId = planCheckList.getId();
                List<PlanInfo> planInfoList = new ArrayList<>();
                PlanInfo planInfo1 = new PlanInfo();
                planInfo1.setUserId(planCheckList.getImpUserId());
                planInfo1.setStatusUser("1001");
                planInfo1.setPlanId(planId);
                planInfo1.setType(0);
                planInfoList.add(planInfo1);
                planInfoService.saveBatch(planInfoList);
            } else {
                planCheckListService.updatePlanById(planCheckList);
            }
            if(StringUtils.isNotBlank(fileUri)){
                PlanFile planFile = new PlanFile();
                planFile.setId(IdGenUtil.uuid());
                planFile.setTaskId(0);
                planFile.setTaskName("计划呈报");
                planFile.setFileUri(fileUri);
                planFile.setBizKey(planCheckList.getId());
                planFile.setUploadUser(planCheckList.getCreatedUsername());
                planFile.setCreatedTime(new Date());
                planManagementService.addFileLog(planFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }

    /**
     * 更改状态
     * @param ids
     * @return
     */
    @PostMapping("/changePlanStatusByIds")
    public R<Boolean> changePlanStatusByIds(HttpServletRequest request, @RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "statusUser", required = true) String statusUser, @RequestParam(name = "userId", required = true) int userId) {
        R<Boolean> r = new R<>();
        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                PlanCheckListNew plan = planCheckListService.selectById(Integer.valueOf(id));
                if (userId == plan.getImpAdminId()) {
                    planInfoService.updatePlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpAdminId()), statusUser);
                    if (statusUser.equals("1002")) { //确认
                        planInfoService.updatePlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpUserId()), "1003");

                        //实施一般员工
                        PlanInfo planInfo1 = new PlanInfo();
                        planInfo1.setUserId(plan.getImpUserId());
                        planInfo1.setStatusUser("1001"); //待完善
                        planInfo1.setPlanId(plan.getId());
                        planInfo1.setType(1);
                        planInfoService.save(planInfo1);
                        //实施管理员
                        PlanInfo planInfo2 = new PlanInfo();
                        planInfo2.setUserId(plan.getImpAdminId());
                        planInfo2.setStatusUser("1001"); //待审核
                        planInfo2.setPlanId(plan.getId());
                        planInfo2.setType(1);
                        planInfoService.save(planInfo2);
                        //审计对象管理员
                        PlanInfo planInfo3 = new PlanInfo();
                        planInfo3.setUserId(plan.getAuditAdminId());
                        planInfo3.setStatusUser("1004"); //待审核
                        planInfo3.setPlanId(plan.getId());
                        planInfo3.setType(1);
                        planInfoService.save(planInfo3);

                        //项目启动时间
                        plan.setStartTime(new Date());
                        plan.setStatus("1001"); //正在实施
                        planCheckListService.updatePlanById(plan);

                        PlanTimeTemp planTimeTemp = planTimeTempService.getByPlanId(plan.getId());
                        if (planTimeTemp == null) {
                            planTimeTemp = new PlanTimeTemp();
                            planTimeTemp.setPlanId(plan.getId());
                            planTimeTemp.setStartTimeAll(new Date());
                            planTimeTempService.save(planTimeTemp);
                        }

                    } else if (statusUser.equals("1003")) { //驳回
                        PlanInfo planInfo = planInfoService.getPlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpUserId()));
                        if (request.getParameter("opinion") != null) {
                            planInfo.setOpinion(request.getParameter("opinion"));
                        }
                        planInfo.setStatusUser("1004");
                        planInfoService.updateById(planInfo);

//                        planInfoService.updatePlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpUserId()), "1004");
                    }
                } else if (userId == plan.getImpUserId()) {
                    planInfoService.updatePlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpUserId()), statusUser);
                    if (statusUser.equals("1002")) { //上报
                        PlanInfo planInfo = planInfoService.getPlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpAdminId()));
                        if (planInfo == null) {
                            PlanInfo planInfo1 = new PlanInfo();
                            planInfo1.setUserId(plan.getImpAdminId());
                            planInfo1.setStatusUser("1001");
                            planInfo1.setPlanId(plan.getId());
                            planInfo1.setType(0);
                            planInfoService.save(planInfo1);
                        } else {
                            planInfoService.updatePlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpAdminId()), "1002");
                        }
                    }
                }

                //启动流程
                if (userId == plan.getImpAdminId() && statusUser.equals("1002")) {
                    int createdBy = plan.getCreatedBy(); //创建人
                    int impUserAssignee = plan.getImpUserId(); //
                    int implLeaderAssignee = plan.getImpAdminId(); //
                    int auditUserAssignee = plan.getAuditUserId(); //
                    int auditLeaderAssignee = plan.getAuditAdminId(); //

                    JSONObject varsJSONObject = new JSONObject();
                    varsJSONObject.put("impUserAssignee", impUserAssignee);
                    varsJSONObject.put("impLeaderAssignee", implLeaderAssignee);
                    varsJSONObject.put("auditUserAssignee", auditUserAssignee);
                    varsJSONObject.put("auditLeaderAssignee", auditLeaderAssignee);
                    varsJSONObject.put("createdBy", createdBy);
                    varsJSONObject.put("auditStatus", PlanStatusEnum.PLAN_AUDIT_PASS.getCode());
                    varsJSONObject.put("delayDate", null);
                    varsJSONObject.put("projectName", plan.getProjectName());
                    varsJSONObject.put("projectCode", plan.getProjectCode());
                    varsJSONObject.put("implementingAgencyId", plan.getImplementingAgencyId());
                    varsJSONObject.put("auditObjectId", plan.getAuditObjectId());
                    varsJSONObject.put("auditNatureId", plan.getAuditNatureId());
                    varsJSONObject.put("auditYear", plan.getAuditYear());
                    varsJSONObject.put("status", plan.getStatus());

                    String vars = varsJSONObject.toJSONString();
                    //启动流程
                    R2<Boolean> auditApply = auditActivitiService.start("auditApply", Integer.valueOf(id), vars);
                    if(!auditApply.getData()){
                        return r.setMsg("流程启动失败:"+auditApply.getMsg());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r.setData(true);
    }

}
