package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.StateEnum;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditPlanInfoServiceImpl;
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
@RequestMapping("/audit-api/auditPlan")
public class AuditPlanInfoController {

    @Resource
    private AuditPlanInfoServiceImpl auditPlanInfoServiceImpl;

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
                    auditPlanInfoServiceImpl.updateByPlanId(planId, "1", "1005");
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

    @ApiOperation("获取问题信息")
    @PostMapping("/approvalPlan")
    public R<Boolean> approvalPlan(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "roleId", required = true) String roleId) {
        R<Boolean> r = new R<>();
        try {
            auditPlanInfoServiceImpl.updateById(id, "1007");
            AuditPlanInfo auditPlanInfo = auditPlanInfoServiceImpl.getById(id, roleId);
            String planId = auditPlanInfo.getPlanCheckList().getId();
            auditPlanInfoServiceImpl.updateByPlanId(planId, "1", "1003");
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
            }
            auditPlanInfoServiceImpl.validate(auditPlanInfo, r);
            auditPlanInfoServiceImpl.saveOrUpdate(auditPlanInfo);
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }

}
