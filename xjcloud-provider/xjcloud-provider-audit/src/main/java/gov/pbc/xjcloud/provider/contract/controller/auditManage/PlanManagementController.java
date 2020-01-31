package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.StateEnum;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditPlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
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

    @Resource
    private AuditPlanInfoServiceImpl auditPlanInfoServiceImpl;

    /**
     * 获取审计计划
     *
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

    @ApiOperation("计划完成分类分页")
    @GetMapping(value = {"{type}/page", ""})
    public R<Page<PlanCheckList>> typePage(@PathVariable("type") String type, @RequestParam Map<String, String> query) {
        Page<PlanCheckList> page = new Page<>();
        try {
            String current = query.getOrDefault("current", "1");
            String size = query.getOrDefault("size", "10");
            page.setCurrent(Long.parseLong(current));
            page.setSize(Long.parseLong(size));
            query.put("type", type);
            page = planManagementService.selectTypePage(page, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    @ApiOperation("关注计划")
    @PutMapping(value = {"check/attention"})
    public R<Boolean> check(String userId, String checkStr) {

        if (StringUtils.isBlank(checkStr)) {
            return R.failed("关注列数为空");
        }
        if (StringUtils.isBlank(userId)) {
            return R.failed("用户不存在");
        }
        try {
            planManagementService.addCheckAttention(userId,checkStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.ok(Boolean.TRUE);
    }

    @ApiOperation("取消关注")
    @PutMapping(value = {"uncheck/attention"})
    public R<Boolean> uncheck(@RequestBody List<Map<String, String>> uncheckList) {

        return R.ok(Boolean.TRUE);
    }

    /**
     * 获取审计计划
     *
     * @return
     */
    @ApiOperation("审计查询")
    @GetMapping(value = {"planList", ""})
    public R planList(PlanCheckList query, Page<Map<String, Object>> page) {
        List<Map<String, Object>> planList = new ArrayList<Map<String, Object>>();
        try {

            planList =  planManagementService.selectEntryByQuery(query, page.getCurrent()-1, page.getSize());
            planList = planManagementService.selectEntryByQuery(query, page.getCurrent() - 1, page.getSize());
            page.setRecords(planList);
            page.setTotal(Long.valueOf(planManagementService.countEntryByQuery(query)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    /**
     * 获取实施机构
     *
     * @return
     */
    @ApiOperation("获取实施机构")
    @GetMapping("/selectEntryByCategoryId")
    public R<List<Map<String, Object>>> selectEntryByCategoryId(@RequestParam(name = "categoryId", required = true) String categoryId) {
        return R.ok(planManagementService.selectEntryByCategoryId(categoryId));
    }

    /**
     * 创建问题
     *
     * @return
     */
    @ApiOperation("保存问题")
    @PostMapping("/saveOrEditPlan")
    public R<Boolean> saveOrEditPlan(PlanCheckList planCheckList) {
        R<Boolean> r = new R<>();
        try {
            if (StringUtils.isBlank(planCheckList.getId())) {
                int code = (int) ((Math.random() * 9 + 1) * 1000);
                planCheckList.setProjectCode("PROJECT-" + String.valueOf(code));
            }
            planManagementService.validate(planCheckList, r);//  此处没有对字段添加约束，所以不会生效
            if (StringUtils.isBlank(planCheckList.getId())) {
                planCheckList.setDelFlag(DelConstants.EXITED);
                planManagementService.save(planCheckList);
                AuditPlanInfo auditPlanInfo = new AuditPlanInfo();
                auditPlanInfo.setRoleId("1");
                auditPlanInfo.setStatus("1001");
                auditPlanInfo.setPlanCheckList(planCheckList);
                auditPlanInfo.setOpinion("");
                auditPlanInfoServiceImpl.insertA(auditPlanInfo);
            } else {
                planManagementService.updateById(planCheckList);
            }
        } catch (Exception e) {
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
