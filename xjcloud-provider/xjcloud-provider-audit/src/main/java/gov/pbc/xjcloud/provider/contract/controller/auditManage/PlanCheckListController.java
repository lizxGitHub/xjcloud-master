package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckListNew;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.service.impl.PlanCheckListServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    @PostMapping("/saveOrEditPlan")
    public R<Boolean> saveOrEditPlan(PlanCheckListNew planCheckList) {
        R<Boolean> r = new R<>();
        try {
            if (planCheckList.getId() == 0) {
                int code = (int) ((Math.random() * 9 + 1) * 1000);
                planCheckList.setProjectCode("PROJECT-" + code);
            }
            planCheckListService.validate(planCheckList, r);//  此处没有对字段添加约束，所以不会生效
            if (planCheckList.getId() == 0) {
                planCheckList.setDelFlag(DelConstants.EXITED);
                planCheckList.setStatus("0");
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
                planCheckList = planCheckListService.getById(planCheckList.getId());
                if (planCheckList != null) {
                    planCheckListService.updateById(planCheckList);
                }
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
    public R<Boolean> changePlanStatusByIds(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "statusUser", required = true) String statusUser, @RequestParam(name = "userId", required = true) int userId) {
        R<Boolean> r = new R<>();
        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                PlanCheckListNew plan = planCheckListService.selectById(Integer.valueOf(id));
                if (userId == plan.getImpAdminId()) {
                    planInfoService.updateByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpAdminId()), statusUser);
                    if (statusUser.equals("1002")) {
                        planInfoService.updateByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpUserId()), "1003");
                    } else if (statusUser.equals("1003")) {
                        planInfoService.updateByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpUserId()), "1004");
                    }
                } else if (userId == plan.getImpUserId()) {
                    planInfoService.updateByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpUserId()), statusUser);
                    if (statusUser.equals("1002")) {
                        PlanInfo planInfo = planInfoService.getByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpAdminId()));
                        if (planInfo == null) {
                            PlanInfo planInfo1 = new PlanInfo();
                            planInfo1.setUserId(plan.getImpAdminId());
                            planInfo1.setStatusUser("1001");
                            planInfo1.setPlanId(plan.getId());
                            planInfo1.setType(0);
                            planInfoService.save(planInfo1);
                        } else {
                            planInfoService.updateByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpAdminId()), "1001");
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
                    varsJSONObject.put("auditStatus", PlanStatusEnum.PLAN_TOBE_AUDITED.getCode());
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
//                    R2<Boolean> auditApply = auditActivitiService.start("auditApply", Integer.valueOf(id), vars);
//                    if(!auditApply.getData()){
//                        return r.setMsg("流程启动失败:"+auditApply.getMsg());
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r.setData(true);
    }

}
