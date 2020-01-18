package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditProjectInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.StateEnum;
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
@Api("审计项目管理")
@RestController
@RequestMapping("/audit-api/auditProject")
public class AuditProjectInfoController {

    @Resource
    private AuditProjectInfoServiceImpl auditProjectInfoServiceImpl;

    @ApiOperation("审计页面信息")
    @GetMapping(value = {"page", ""})
    public R<Page<AuditProjectInfo>> index(AuditProjectInfo query, Page<AuditProjectInfo> page) {
        PageUtil.initPage(page);
        try {
            page = auditProjectInfoServiceImpl.selectAuditProject(page, query);
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
    public R<AuditProjectInfo> getPLanInfoById(@PathVariable String id, @RequestParam(name = "roleId", required = true) String roleId) {
        R<AuditProjectInfo> r = new R<>();
        try {
            if (StringUtils.isBlank(id)) {
                return r.failed("参数错误，请检查");
            }
            AuditProjectInfo auditPlanInfo = auditProjectInfoServiceImpl.getById(id, roleId);
            r.setData(auditPlanInfo);
        } catch (Exception e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;

    }

    @ApiOperation("完善项目")
    @PostMapping("/improvePlan")
    public R<Boolean> improvePlan(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "roleId", required = true) String roleId) {
        R<Boolean> r = new R<>();
        try {
            auditProjectInfoServiceImpl.updateById(id, "1001");
            AuditProjectInfo auditPlanInfo = auditProjectInfoServiceImpl.getById(id, roleId);
            String planId = auditPlanInfo.getPlanCheckList().getId();
            auditProjectInfoServiceImpl.updateByPlanId(planId, "2", "1005");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r.setData(true);
    }

    @ApiOperation("获取问题信息")
    @PostMapping("/approvalPlan")
    public R<Boolean> approvalPlan(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "roleId", required = true) String roleId, String status) {
        R<Boolean> r = new R<>();
        try {
            AuditProjectInfo auditPlanInfo = auditProjectInfoServiceImpl.getById(id, roleId);
            String planId = auditPlanInfo.getPlanCheckList().getId();
            if (StringUtils.isNotBlank(status) && "1005".equals(status) && "2".equals(roleId)) {
                auditProjectInfoServiceImpl.updateById(id, "1006");
                auditProjectInfoServiceImpl.updateByPlanId(planId, "1", "1002");
                auditProjectInfoServiceImpl.updateByPlanId(planId, "3", "1001");
                auditProjectInfoServiceImpl.updateByPlanId(planId, "4", "1005");
            } else if (StringUtils.isNotBlank(status) && "1005".equals(status) && "4".equals(roleId)) {
                auditProjectInfoServiceImpl.updateById(id, "1005");
            } else if (StringUtils.isNotBlank(status) && "1001".equals(status) && "3".equals(roleId)) {
                auditProjectInfoServiceImpl.updateById(id, "1002");
                auditProjectInfoServiceImpl.updateByPlanId(planId, "1", "1008");
            } else if (StringUtils.isNotBlank(status) && "1008".equals(status) && "1".equals(roleId)) {
                auditProjectInfoServiceImpl.updateById(id, "1007");
                auditProjectInfoServiceImpl.updateByPlanId(planId, "3", "1004");
                auditProjectInfoServiceImpl.updateByPlanId(planId, "2", "1007");
                auditProjectInfoServiceImpl.updateByPlanId(planId, "4", "1006");
            }

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
            auditProjectInfoServiceImpl.updateById(id, "1006");
            AuditProjectInfo auditPlanInfo = auditProjectInfoServiceImpl.getById(id, roleId);
            String planId = auditPlanInfo.getPlanCheckList().getId();
            auditProjectInfoServiceImpl.updateByPlanId(planId, "1", "1004");
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
    public R<Boolean> saveOrUpdate(AuditProjectInfo auditPlanInfo) {
        R<Boolean> r = new R<>();
        try {
            if (StringUtils.isBlank(auditPlanInfo.getId())) {
                int code = (int) ((Math.random() * 9 + 1) * 1000);
                auditPlanInfo.getPlanCheckList().setProjectCode("PROJECT" + String.valueOf(code));
                auditPlanInfo.getPlanCheckList().setDelFlag(DelConstants.EXITED);
                auditPlanInfo.setStatus(StateEnum.SH_NORMAL_NO_PRESENTATION.getCode());
            }
            auditProjectInfoServiceImpl.validate(auditPlanInfo, r);
            auditProjectInfoServiceImpl.saveOrUpdate(auditPlanInfo);
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }

}
