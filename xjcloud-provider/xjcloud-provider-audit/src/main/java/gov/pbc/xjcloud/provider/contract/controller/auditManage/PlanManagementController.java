package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.enumutils.StateEnum;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Api("审计计划管理")
@RestController
@RequestMapping("/audit-api/planManage")
public class PlanManagementController {

    @Resource
    private PlanManagementServiceImpl planManagementService;

    /**
     * 获取审计计划
     * @return
     */
    @ApiOperation("审计页面信息")
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
     * 获取审计计划
     * @return
     */
    @ApiOperation("审计查询")
    @GetMapping(value = {"planList", ""})
    public R planList(PlanCheckList query, Page<Map<String, Object>> page){
        List<Map<String, Object>> planList = new ArrayList<Map<String, Object>>();
        try {
            planList =  planManagementService.selectEntryByQuery(query, page.getCurrent()-1, page.getSize());
            page.setRecords(planList);
            page.setTotal(Long.valueOf(planManagementService.countEntryByQuery(query)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }
    /**
     * 获取实施机构
     * @return
     */
    @ApiOperation("获取实施机构")
    @GetMapping("/selectEntryByCategoryId")
    public R<List<Map<String, Object>>> selectEntryByCategoryId(@RequestParam(name = "categoryId", required = true) String categoryId) {
        return R.ok(planManagementService.selectEntryByCategoryId(categoryId));
    }

    /**
     * 创建问题
     * @return
     */
    @ApiOperation("保存问题")
    @PostMapping("/saveOrEditPlan")
    public R<Boolean> saveOrEditPlan(PlanCheckList planCheckList){
        R<Boolean> r = new R<>();
        try {
            if (StringUtils.isBlank(planCheckList.getId())) {
                int code = (int) ((Math.random()*9+1)*1000);
                planCheckList.setProjectCode("PROJECT" + String.valueOf(code));
                planCheckList.setDelFlag(DelConstants.EXITED);
                planCheckList.setNormalStatus(StateEnum.SH_NORMAL_NO_PRESENTATION.getCode());
            }
            planManagementService.validate(planCheckList,r);
            planManagementService.saveOrUpdate(planCheckList);
        }catch (Exception e){
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }

    /**
     * 用户行为：删除问题
     *
     * @param id
     * @return
     */
    @ApiOperation("用户删除问题")
    @DeleteMapping("user_delete/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        R<Boolean> r = new R<>();
        Boolean b;
        if (StringUtils.isBlank(id)) {
            throw new NullArgumentException("请求参数不存在");
        } else {
            b = planManagementService.removeById(id);
        }
        return r.setData(b);
    }

    /**
     * 根据id获取问题信息
     *
     * @param id
     * @return
     */
    @ApiOperation("获取问题信息")
    @GetMapping("/{id}")
    public R<PlanCheckList> getPLanInfoById(@PathVariable String id) {
        R<PlanCheckList> r = new R<>();
        try {
            if (StringUtils.isBlank(id)) {
                return r.failed("参数错误，请检查");
            }
            PlanCheckList planOne = planManagementService.getById(id);
            r.setData(planOne);
        } catch (Exception e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;
    }
}
