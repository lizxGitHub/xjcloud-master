package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.vo.PlanCheckListVO;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.feign.dept.RemoteDeptService;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditPlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.DeptUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.vo.DeptVO;
import gov.pbc.xjcloud.provider.contract.vo.TreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DeptUtil deptUtil;

    @Autowired
    private UserCenterService userCenterService;

    @Autowired
    private RemoteDeptService remoteDeptService;

    /**
     * 获取审计计划
     *
     * @return
     */
    @ApiOperation("审计页面信息")
    @GetMapping(value = {"page", ""})
    public R<Page<PlanCheckList>> index(PlanCheckList query, Page<PlanCheckList> page) {
        if (query.getCreatedBy() == 0) {
            return R.ok(page);
        }
        PageUtil.initPage(page);
        try {
            page = planManagementService.selectPlanCheckList(page, query);
            page.getRecords().stream().forEach(e->{
                TreeVO treeVO = deptUtil.getDeptMap().get(e.getAuditObjectId());
                if(null != treeVO){
                    e.setAuditObjectId(treeVO.getLabel());
                }else {
                    e.setAuditObjectId("其他");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    @ApiOperation("计划完成分类分页")
    @GetMapping(value = {"{type}/page", ""})
    public R<Page<PlanCheckList>> typePage(@PathVariable("type") String type, @RequestParam Map<String, Object> query) {
        Page<PlanCheckList> page = new Page<>();
        try {
            String current = query.getOrDefault("current", "1").toString();
            String size = query.getOrDefault("size", "10").toString();
            page.setCurrent(Long.parseLong(current));
            page.setSize(Long.parseLong(size));
            query.put("type", type);
            if (StringUtils.isNotBlank(query.get("deptId").toString())) {
                List deptChild = deptUtil.findChildBank(Integer.parseInt(query.get("deptId").toString()), "支行");
                query.put("auditObj", deptChild);
            }

            page = planManagementService.selectTypePage(page, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    @ApiOperation("关注列表分页")
    @GetMapping(value = {"/attention/page", ""})
    public R<Page<PlanCheckList>> attentionPage(@RequestParam Map<String, Object> query) {
        Page<PlanCheckList> page = new Page<>();
        try {
            String current = query.getOrDefault("current", "1").toString();
            String size = query.getOrDefault("size", "10").toString();
            page.setCurrent(Long.parseLong(current));
            page.setSize(Long.parseLong(size));
            Map<Integer, TreeVO> deptMap = deptUtil.getDeptMap();
            page = planManagementService.selectAttentionPage(page, query);
            page.getRecords().stream().filter(e -> e.getImplementingAgencyId() != null && e.getAuditObjectId() != null).forEach(e -> {
                e.setImplementingAgencyId(deptMap.get(Integer.valueOf(e.getImplementingAgencyId())).getLabel());
                e.setAuditObjectId(deptMap.get(Integer.parseInt(e.getAuditObjectId())).getLabel());
            });
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
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
            planManagementService.addCheckAttention(userId, checkStr);
        } catch (Exception e) {
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

            planList = planManagementService.selectEntryByQuery(query, page.getCurrent() - 1, page.getSize());
            planList = planManagementService.selectEntryByQuery(query, page.getCurrent() - 1, page.getSize());
            page.setRecords(planList);
            page.setTotal(Long.valueOf(planManagementService.countEntryByQuery(query)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    /**
     * @return
     */
    @ApiOperation("获取类别")
    @GetMapping("/selectEntryByCategoryId")
    public R<List<Map<String, Object>>> selectEntryByCategoryId(@RequestParam(name = "categoryId", required = true) String categoryId) {
        return R.ok(planManagementService.selectEntryByCategoryId(categoryId));
    }

    /**
     * @return
     */
    @ApiOperation("获取部门角色")
    @GetMapping("/getUsersByRoleNameAndDept")
    public R getUsersByRoleNameAndDept(@RequestParam(name = "deptId", required = true) int deptId, @RequestParam(name = "roleName", required = true) String roleName) {
        return R.ok(userCenterService.getUsersByRoleNameAndDept(deptId, roleName).getData());
    }

    /**
     * 审计对象
     *
     * @return
     */
    @ApiOperation("审计对象")
    @GetMapping("/getAuditObject")
    public R<List<DeptVO>> getAuditObject(@RequestParam(name = "deptId", required = true) Integer deptId, String lastFilter) {
        if (StringUtils.isBlank(lastFilter)) {
            lastFilter = "支行";
        }
        List<DeptVO> deptList = deptUtil.findChildBank(deptId, lastFilter);
        return R.ok(deptList);
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
            if (planCheckList.getId() == 0) {
                int code = (int) ((Math.random() * 9 + 1) * 1000);
                planCheckList.setProjectCode("PROJECT-" + code);
            }
            planManagementService.validate(planCheckList, r);//  此处没有对字段添加约束，所以不会生效
            if (planCheckList.getId() == 0) {
                planCheckList.setStatus(String.valueOf(PlanStatusEnum.PLAN_UN_SUBMIT.getCode()));
                planCheckList.setDelFlag(DelConstants.EXITED);
                planManagementService.save(planCheckList);
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
    public R<PlanCheckListVO> getPLanInfoById(@PathVariable String id) {
        R<PlanCheckListVO> r = new R<>();
        try {
            if (StringUtils.isBlank(id)) {
                return r.failed("参数错误，请检查");
            }
            PlanCheckList planOne = planManagementService.getById(id);
            gov.pbc.xjcloud.common.core.util.R<Map<String,Object>> dept = remoteDeptService.dept(Integer.parseInt(planOne.getImplementingAgencyId()));
            PlanCheckListVO planCheckListDTO = changeToDTO(planOne);
            planCheckListDTO.setImplementingAgencyName(dept.getData().get("name").toString());
            r.setData(planCheckListDTO);
        } catch (Exception e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;
    }

    private PlanCheckListVO changeToDTO(PlanCheckList checkList) {
        if (null == checkList) {
            return new PlanCheckListVO();
        }
        String s = JSONObject.toJSONString(checkList);
        return JSONUtil.toBean(s, PlanCheckListVO.class);
    }
}
