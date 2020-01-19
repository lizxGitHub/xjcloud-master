package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 */
@Api("审计计划管理")
@RestController
@RequestMapping("/audit-api/planInfo/")
public class PlanInfoController {

    @Resource
    private PlanInfoServiceImpl planInfoServiceImpl;

    @ApiOperation("审计页面信息")
    @GetMapping(value = {"page", ""})
    public R<Page<PlanInfo>> index(PlanInfo query, Page<PlanInfo> page) {
        PageUtil.initPage(page);
        try {
            page = planInfoServiceImpl.selectPlanInfoList(page, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

}
