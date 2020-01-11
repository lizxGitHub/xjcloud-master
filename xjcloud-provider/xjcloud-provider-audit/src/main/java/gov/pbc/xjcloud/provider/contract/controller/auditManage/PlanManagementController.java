package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping("/audit-api/planManage")
public class PlanManagementController {

    @Resource
    private PlanManagementServiceImpl planManagementService;

    /**
     * 获取审计计划
     * @return
     */
    @GetMapping(value = {"page", ""})
    public R<Page<PlanCheckList>> index(PlanCheckList query, Page<PlanCheckList> page) {
        PageUtil.initPage(page);
        try {
            page = planManagementService.selectPlanCheckList(page, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    /**
     * 获取实施机构
     * @return
     */
    @GetMapping("/selectEntryByCategoryId")
    public R<List<Map<String, Object>>> selectEntryByCategoryId(@RequestParam(name = "categoryId", required = true) String categoryId) {
        return R.ok(planManagementService.selectEntryByCategoryId(categoryId));
    }

    /**
     * 创建问题
     * @return
     */
    @PostMapping("/saveAndEditPlan")
    public R<Boolean> saveAndEditPlan(PlanCheckList planCheckList){
        R<Boolean> r = new R<>();
        try {
            planManagementService.validate(planCheckList,r);
            planCheckList.setDelFlag(DelConstants.EXITED);
            planManagementService.save(planCheckList);
        }catch (Exception e){
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }
}
