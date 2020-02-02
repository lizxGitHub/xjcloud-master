package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.activiti.api.feign.RemoteProcessService;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.constants.DeptConstants;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditProjectInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.StateEnum;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditPlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditProjectInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.DeptUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Api("审计计划管理")
@RestController
@RequestMapping("/audit-api/auditPlan/")
public class AuditPlanInfoController {

    @Resource
    private AuditPlanInfoServiceImpl auditPlanInfoServiceImpl;

    @Resource
    private AuditProjectInfoServiceImpl auditProjectInfoServiceImpl;

    @Autowired
    private AuditActivitiService auditActivitiService;

    @Autowired
    private UserCenterService userCenterService;

    @Resource
    private PlanManagementService planManagementService;

    RemoteProcessService remoteProcessService;

    @Autowired
    private DeptUtil deptUtil;

    @ApiOperation("审计页面信息")
    @GetMapping(value = {"page", ""})
    public R<Page<AuditPlanInfo>> index(AuditPlanInfo query, Page<AuditPlanInfo> page) {
        PageUtil.initPage(page);
        try {
            page = auditPlanInfoServiceImpl.selectAuditPlanInfoList(page, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    /**
     * 根据id获取问题信息
     *
     * @param id
     * @return
     */
    @ApiOperation("获取问题信息")
    @GetMapping("/{id}")
    public R<AuditPlanInfo> getPLanInfoById(@PathVariable String id) {
        R<AuditPlanInfo> r = new R<>();
        try {
            if (StringUtils.isBlank(id)) {
                return r.failed("参数错误，请检查");
            }
            AuditPlanInfo auditPlanInfo = auditPlanInfoServiceImpl.getById(id);
            r.setData(auditPlanInfo);
        } catch (Exception e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;

    }

    /**
     * 上报计划
     * @param ids
     * @param
     * @return
     */
    @PostMapping("/reportPlan")
    public R<Boolean> reportPlan(@RequestParam(name = "ids", required = true) String ids) {
        R<Boolean> r = new R<>();
        String[] idArray = ids.split(",");
        try {
            for (String id : idArray) {
                PlanCheckList plan = planManagementService.getById(id);
                String implementingAgencyId = plan.getImplementingAgencyId(); //实施机构id
                String auditObjectId = plan.getAuditObjectId(); //审计对象id
                int createdBy = plan.getCreatedBy(); //创建人

//                gov.pbc.xjcloud.common.core.util.R impUserAssignee = userCenterService.getUsersByRoleNameAndDept(Integer.valueOf(implementingAgencyId), "一般用户");
//                gov.pbc.xjcloud.common.core.util.R implLeaderAssignee = userCenterService.getUsersByRoleNameAndDept(Integer.valueOf(implementingAgencyId), "管理员");
//                gov.pbc.xjcloud.common.core.util.R auditUserAssignee = userCenterService.getUsersByRoleNameAndDept(Integer.valueOf(auditObjectId), "一般用户");
//                gov.pbc.xjcloud.common.core.util.R auditLeaderAssignee = userCenterService.getUsersByRoleNameAndDept(Integer.valueOf(auditObjectId), "管理员");

//                //实施机构一般员工
//                JSONArray impUserAssigneeArray = new JSONArray();
//                List<Map<String, String>> impUserAssigneeList = (List) impUserAssignee.getData();
//                for (Map<String, String> impUserAssigneeMap : impUserAssigneeList) {
//                    impUserAssigneeArray.add(impUserAssigneeMap.get("userId"));
//                }
//                //实施机构领导
//                JSONArray implLeaderAssigneeArray = new JSONArray();
//                List<Map<String, String>> implLeaderAssigneeList = (List) implLeaderAssignee.getData();
//                for (Map<String, String> implLeaderAssigneeMap : implLeaderAssigneeList) {
//                    implLeaderAssigneeArray.add(implLeaderAssigneeMap.get("userId"));
//                }
//                //审计对象一般员工
//                JSONArray auditUserAssigneeArray = new JSONArray();
//                List<Map<String, String>> auditUserAssigneeList = (List) auditUserAssignee.getData();
//                for (Map<String, String> auditUserAssigneeMap : auditUserAssigneeList) {
//                    auditUserAssigneeArray.add(auditUserAssigneeMap.get("userId"));
//                }
//                //审计对象领导
//                JSONArray auditLeaderAssigneeArray = new JSONArray();
//                List<Map<String, String>> auditLeaderAssigneeList = (List) auditLeaderAssignee.getData();
//                for (Map<String, String> auditLeaderAssigneeMap : auditLeaderAssigneeList) {
//                    auditLeaderAssigneeArray.add(auditLeaderAssigneeMap.get("userId"));
//                }

                int impUserAssignee = plan.getImpUserId(); //
                int implLeaderAssignee = plan.getImpAdminId(); //
                int auditUserAssignee = plan.getAuditUserId(); //
                int auditLeaderAssignee = plan.getAuditAdminId(); //

                JSONObject varsJSONObject = new JSONObject();
                varsJSONObject.put("impUserAssignee", impUserAssignee);
                varsJSONObject.put("implLeaderAssignee", implLeaderAssignee);
                varsJSONObject.put("auditUserAssignee", auditUserAssignee);
                varsJSONObject.put("auditLeaderAssignee", auditLeaderAssignee);
                varsJSONObject.put("createdBy", createdBy);
                varsJSONObject.put("auditStatus", PlanStatusEnum.PLAN_TOBE_AUDITED.getCode());
                varsJSONObject.put("delayDate", null);

                String vars = varsJSONObject.toJSONString();
                //启动流程
                auditActivitiService.start("auditApply", Integer.valueOf(id), vars);
                //修改状态
                plan.setStatus(String.valueOf(PlanStatusEnum.PLAN_TOBE_AUDITED.getCode()));
                planManagementService.updateById(plan);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r.setData(true);
    }

//    @ApiOperation("批准问题信息")
//    @PostMapping("/approvalPlan")
//    public R<Boolean> approvalPlan(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "roleId", required = true) String roleId) {
//        R<Boolean> r = new R<>();
//        try {
//            auditPlanInfoServiceImpl.updateById(id, "1007");
//            AuditPlanInfo auditPlanInfo = auditPlanInfoServiceImpl.getById(id, roleId);
//            PlanCheckList planCheckList = auditPlanInfo.getPlanCheckList();
//            String planId = planCheckList.getId();
//            auditPlanInfoServiceImpl.updateByPlanId(planId, "1", "1003");
//
//            //审核部门一般员工
//            AuditProjectInfo auditProjectInfo = auditProjectInfoServiceImpl.getById(id, "1");
//            if (auditProjectInfo != null) {
//                auditPlanInfoServiceImpl.updateByPlanId(planId, "1", "1001");
//            } else {
//                auditProjectInfo = new AuditProjectInfo();
//                auditProjectInfo.setRoleId("1");
//                auditProjectInfo.setStatus("1001");
//                auditProjectInfo.setPlanCheckList(planCheckList);
//                auditProjectInfo.setOpinion("");
//                auditProjectInfoServiceImpl.insertA(auditProjectInfo);
//            }
//            //审核部门领导
//            AuditProjectInfo auditProjectInfo2 = auditProjectInfoServiceImpl.getById(id, "2");
//            if (auditProjectInfo2 != null) {
//                auditPlanInfoServiceImpl.updateByPlanId(planId, "2", "1004");
//            } else {
//                auditProjectInfo2 = new AuditProjectInfo();
//                auditProjectInfo2.setRoleId("2");
//                auditProjectInfo2.setStatus("1004");
//                auditProjectInfo2.setPlanCheckList(planCheckList);
//                auditProjectInfo2.setOpinion("");
//                auditProjectInfoServiceImpl.insertA(auditProjectInfo2);
//            }
//
//            //被审核部门一般员工
//            AuditProjectInfo auditProjectInfo3 = auditProjectInfoServiceImpl.getById(id, "3");
//            if (auditProjectInfo3 != null) {
//                auditPlanInfoServiceImpl.updateByPlanId(planId, "3", "1006");
//            } else {
//                auditProjectInfo3 = new AuditProjectInfo();
//                auditProjectInfo3.setRoleId("3");
//                auditProjectInfo3.setStatus("1006");
//                auditProjectInfo3.setPlanCheckList(planCheckList);
//                auditProjectInfo3.setOpinion("");
//                auditProjectInfoServiceImpl.insertA(auditProjectInfo3);
//            }
//            //被审核部门领导
//            AuditProjectInfo auditProjectInfo4 = auditProjectInfoServiceImpl.getById(id, "4");
//            if (auditProjectInfo4 != null) {
//                auditPlanInfoServiceImpl.updateByPlanId(planId, "4", "1004");
//            } else {
//                auditProjectInfo4 = new AuditProjectInfo();
//                auditProjectInfo4.setRoleId("4");
//                auditProjectInfo4.setStatus("1004");
//                auditProjectInfo4.setPlanCheckList(planCheckList);
//                auditProjectInfo4.setOpinion("");
//                auditProjectInfoServiceImpl.insertA(auditProjectInfo4);
//            }
//
//            //activity import gov.pbc.xjcloud.common.core.util.R;
////            gov.pbc.xjcloud.common.core.util.R a = auditActivitiService.start("auditPlan", 1, "");
//            gov.pbc.xjcloud.common.core.util.R a = remoteProcessService.start("auditPlan", 1, "");
//            System.out.println(a);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return r.setData(true);
//    }

//    @ApiOperation("获取问题信息")
//    @PostMapping("/rejectPlan")
//    public R<Boolean> rejectPlan(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "roleId", required = true) String roleId) {
//        R<Boolean> r = new R<>();
//        try {
//            auditPlanInfoServiceImpl.updateById(id, "1006");
//            AuditPlanInfo auditPlanInfo = auditPlanInfoServiceImpl.getById(id, roleId);
//            String planId = auditPlanInfo.getPlanCheckList().getId();
//            auditPlanInfoServiceImpl.updateByPlanId(planId, "1", "1004");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return r.setData(true);
//    }


    /**
     * 创建问题
     *
     * @return
     */
    @ApiOperation("保存问题")
    @PostMapping("/saveOrUpdate")
    public R<Boolean> saveOrUpdate(AuditPlanInfo auditPlanInfo) {
        R<Boolean> r = new R<>();
        try {
            if (StringUtils.isBlank(auditPlanInfo.getId())) {
                int code = (int) ((Math.random() * 9 + 1) * 1000);
                auditPlanInfo.getPlanCheckList().setProjectCode("PROJECT" + String.valueOf(code));
                auditPlanInfo.getPlanCheckList().setDelFlag(DelConstants.EXITED);
                auditPlanInfo.setStatus(StateEnum.SH_NORMAL_NO_PRESENTATION.getCode());

                auditPlanInfoServiceImpl.save(auditPlanInfo);
            } else {
                auditPlanInfoServiceImpl.validate(auditPlanInfo, r);
                auditPlanInfoServiceImpl.updateById(auditPlanInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            r.setMsg(e.getMessage());
            r.setCode(ApiErrorCode.FAILED.getCode());
        }
        return r;
    }

    /**
     *
     */
    @GetMapping("/XJInfo")
    public JSONObject getXJInfo() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        Iterator<Map.Entry<String, String>> iter = DeptConstants.deptMap.entrySet().iterator(); //遍历地区
        Map.Entry<String, String> entry;
        while (iter.hasNext()) {
            JSONObject jsonObjectIn = new JSONObject();
            entry = iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            //按dept查询问题数 key
            List deptChild = deptUtil.findChildBank(Integer.parseInt(key), "支行");
            List<Map<String, Object>> shortPlans = planManagementService.getShortPlans(deptChild, "");
            //返回的结果是所有问题的list 根据code来判断是未完成

            int total = 0;
            int notRectified = 0; //未整改问题个数
            for (Map<String, Object> shortPlan : shortPlans) {
                int status = Integer.valueOf(shortPlan.get("status").toString());
                if (PlanStatusEnum.PLAN_UN_SUBMIT.getCode() != status) {
                    total += 1;
                    if (PlanStatusEnum.FILE.getCode() != status) {
                        notRectified += 1;
                    }
                }
            }
            double percentage = 0;
            if (total != 0) {
                percentage = new BigDecimal((float)notRectified / total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            jsonObjectIn.put("deptId", key); //地区id
            jsonObjectIn.put("name", value); //地区名称
            jsonObjectIn.put("dept", key); //地区id
            jsonObjectIn.put("total", total); //总问题数
            jsonObjectIn.put("notRectified", notRectified); //未整改问题数
            jsonObjectIn.put("value", percentage); //未整改占比
            jSONArray.add(jsonObjectIn);
        }
        jsonObject.put("data", jSONArray);
        return jsonObject;

    }

}
