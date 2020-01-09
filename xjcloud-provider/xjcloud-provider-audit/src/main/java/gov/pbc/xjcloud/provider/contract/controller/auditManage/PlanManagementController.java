package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.baomidou.mybatisplus.extension.api.R;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/audit-api/planManage")
public class PlanManagementController {

    @Resource
    private PlanManagementService planManagementService;

    /**
     * 获取审计计划
     * @return
     */
    @RequestMapping("/list")
    public R<List<PlanCheckList>> listR() {
        List<PlanCheckList> list = planManagementService.list();
        return R.ok(list);
    }
}
