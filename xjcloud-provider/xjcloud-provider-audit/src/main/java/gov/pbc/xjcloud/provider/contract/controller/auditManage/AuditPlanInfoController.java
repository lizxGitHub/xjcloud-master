package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditProjectInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.StateEnum;
import gov.pbc.xjcloud.provider.contract.service.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditPlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditProjectInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @Resource
    private AuditActivitiService auditActivitiService;

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
    public R<AuditPlanInfo> getPLanInfoById(@PathVariable String id, @RequestParam(name = "roleId", required = true) String roleId) {
        R<AuditPlanInfo> r = new R<>();
        try {
            if (StringUtils.isBlank(id)) {
                return r.failed("参数错误，请检查");
            }
            AuditPlanInfo auditPlanInfo = auditPlanInfoServiceImpl.getById(id, roleId);
            r.setData(auditPlanInfo);
        } catch (Exception e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;

    }

    @ApiOperation("获取问题信息")
    @PostMapping("/reportPlan")
    public R<Boolean> reportPlan(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "roleId", required = true) String roleId) {
        R<Boolean> r = new R<>();
        String[] idArray = ids.split(",");
        try {
            for (String id : idArray) {
                AuditPlanInfo auditPlanInfo = auditPlanInfoServiceImpl.getById(id, roleId);
                if ("1004".equals(auditPlanInfo.getStatus())) { //如果是被驳回 证明存在记录 不用新增
                    String planId = auditPlanInfo.getPlanCheckList().getId();
                    auditPlanInfoServiceImpl.updateByPlanId(planId, "1", "1002"); //待审核
                    auditPlanInfoServiceImpl.updateByPlanId(planId, "2", "1005"); //待审核
                } else {
                    auditPlanInfo.setRoleId("2");
                    auditPlanInfo.setStatus("1005");
                    auditPlanInfoServiceImpl.insertA(auditPlanInfo);
                }
                auditPlanInfoServiceImpl.updateById(id, "1002");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r.setData(true);
    }

    @ApiOperation("批准问题信息")
    @PostMapping("/approvalPlan")
    public R<Boolean> approvalPlan(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "roleId", required = true) String roleId) {
        R<Boolean> r = new R<>();
        try {
            auditPlanInfoServiceImpl.updateById(id, "1007");
            AuditPlanInfo auditPlanInfo = auditPlanInfoServiceImpl.getById(id, roleId);
            PlanCheckList planCheckList = auditPlanInfo.getPlanCheckList();
            String planId = planCheckList.getId();
            auditPlanInfoServiceImpl.updateByPlanId(planId, "1", "1003");

            //审核部门一般员工
            AuditProjectInfo auditProjectInfo = auditProjectInfoServiceImpl.getById(id, "1");
            if (auditProjectInfo != null) {
                auditPlanInfoServiceImpl.updateByPlanId(planId, "1", "1001");
            } else {
                auditProjectInfo = new AuditProjectInfo();
                auditProjectInfo.setRoleId("1");
                auditProjectInfo.setStatus("1001");
                auditProjectInfo.setPlanCheckList(planCheckList);
                auditProjectInfo.setOpinion("");
                auditProjectInfoServiceImpl.insertA(auditProjectInfo);
            }
            //审核部门领导
            AuditProjectInfo auditProjectInfo2 = auditProjectInfoServiceImpl.getById(id, "2");
            if (auditProjectInfo2 != null) {
                auditPlanInfoServiceImpl.updateByPlanId(planId, "2", "1004");
            } else {
                auditProjectInfo2 = new AuditProjectInfo();
                auditProjectInfo2.setRoleId("2");
                auditProjectInfo2.setStatus("1004");
                auditProjectInfo2.setPlanCheckList(planCheckList);
                auditProjectInfo2.setOpinion("");
                auditProjectInfoServiceImpl.insertA(auditProjectInfo2);
            }

            //被审核部门一般员工
            AuditProjectInfo auditProjectInfo3 = auditProjectInfoServiceImpl.getById(id, "3");
            if (auditProjectInfo3 != null) {
                auditPlanInfoServiceImpl.updateByPlanId(planId, "3", "1006");
            } else {
                auditProjectInfo3 = new AuditProjectInfo();
                auditProjectInfo3.setRoleId("3");
                auditProjectInfo3.setStatus("1006");
                auditProjectInfo3.setPlanCheckList(planCheckList);
                auditProjectInfo3.setOpinion("");
                auditProjectInfoServiceImpl.insertA(auditProjectInfo3);
            }
            //被审核部门领导
            AuditProjectInfo auditProjectInfo4 = auditProjectInfoServiceImpl.getById(id, "4");
            if (auditProjectInfo4 != null) {
                auditPlanInfoServiceImpl.updateByPlanId(planId, "4", "1004");
            } else {
                auditProjectInfo4 = new AuditProjectInfo();
                auditProjectInfo4.setRoleId("4");
                auditProjectInfo4.setStatus("1004");
                auditProjectInfo4.setPlanCheckList(planCheckList);
                auditProjectInfo4.setOpinion("");
                auditProjectInfoServiceImpl.insertA(auditProjectInfo4);
            }

            //activity import gov.pbc.xjcloud.common.core.util.R;
//            gov.pbc.xjcloud.common.core.util.R a = auditActivitiService.start("auditPlan", 1, "");
//            System.out.println(a);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return r.setData(true);
    }

    @ApiOperation("获取问题信息")
    @PostMapping("/rejectPlan")
    public R<Boolean> rejectPlan(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "roleId", required = true) String roleId) {
        R<Boolean> r = new R<>();
        try {
            auditPlanInfoServiceImpl.updateById(id, "1006");
            AuditPlanInfo auditPlanInfo = auditPlanInfoServiceImpl.getById(id, roleId);
            String planId = auditPlanInfo.getPlanCheckList().getId();
            auditPlanInfoServiceImpl.updateByPlanId(planId, "1", "1004");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r.setData(true);
    }


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
            }else {
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

}
