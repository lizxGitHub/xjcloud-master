package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DeptConstants;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.SHLeaderStateEnum;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.feign.dept.RemoteDeptService;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditPlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditProjectInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.DeptUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.utils.R2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    RemoteDeptService remoteDeptService;

    @Autowired
    private DeptUtil deptUtil;

    @Value("${audit.flow-key:auditApply}")
    private String auditFlowDefKey;

    @ApiOperation("审计页面信息")
    @GetMapping(value = {"page", ""})
    public R<Page<PlanCheckList>> index(PlanCheckList query, Page<PlanCheckList> page) {
        PageUtil.initPage(page);
        try {
            page = planManagementService.selectPlanCheckListByAdmin(page, query);
//            page.getRecords().stream().forEach(e->{
//                TreeVO treeVO = deptUtil.getDeptMap().get(Integer.parseInt(e.getAuditObjectId()));
//                if(null != treeVO){
//                    e.setAuditObjectId(treeVO.getLabel());
//                }else {
//                    e.setAuditObjectId("其他");
//                }
//            });
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
     * 更改状态
     * @param ids
     * @return
     */
    @PostMapping("/changePlanStatusByIds")
    public R<Boolean> changePlanStatusByIds(@RequestParam(name = "ids", required = true) String ids, @RequestParam(name = "status", required = true) String status) {
        R<Boolean> r = new R<>();
        return r.setData(true);
    }

    /**
     * 获取实施机构
     * @return
     */
    @GetMapping("/getImplementingAgency")
    public JSONObject getImplementingAgency () {
        JSONObject jsonObject = new JSONObject();
        //按dept查询问题数 key
        List deptChild = deptUtil.findChildBank(0, "中支");
        jsonObject.put("implementingAgency", deptChild);
        return jsonObject;
    }

    /**
     * 根据实施机构id获取审计对象
     * @return
     */
    @GetMapping("/getAuditObjectByIAId")
    public JSONObject getAuditObjectByIAId (@RequestParam(name = "IAId", required = true) int IAId ) {
        JSONObject jsonObject = new JSONObject();
        //按dept查询问题数 key
        List deptChild = deptUtil.findChildBank(IAId, "");
        jsonObject.put("AuditObject", deptChild);
        return jsonObject;
    }

    /**
     *
     */
    @GetMapping("/XJInfo")
    public JSONObject getXJInfo(String auditYear) {
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
            List deptChild = deptUtil.findChildBank(Integer.parseInt(key), "");
//            List<Map<String, Object>> shortPlans = planManagementService.getShortPlans(deptChild, "", auditYear);
            List<Map<String, Object>> shortPlans = planManagementService.getShortPlansNew(Integer.parseInt(key), "", auditYear);

            //返回的结果是所有问题的list 根据code来判断是未完成

            int total = 0;
            int notRectified = 0; //未整改问题个数
            int rectifieding = 0; //正在整改问题个数
            for (Map<String, Object> shortPlan : shortPlans) {
                int status = Integer.valueOf(shortPlan.get("status").toString());
                if (0 != status) {
                    total += 1;
                    if (1004 == status || 1005 == status) { //未整改
                        notRectified += 1;
                    } else if (1001 == status || 1002 == status) { //正在整改
                        rectifieding += 1;
                    }
                }
            }
            double percentage = 0;
            if (total != 0) {
                percentage = new BigDecimal((float) (notRectified+rectifieding) / total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            jsonObjectIn.put("deptId", key); //地区id
            jsonObjectIn.put("name", value); //地区名称
            jsonObjectIn.put("dept", key); //地区id
            jsonObjectIn.put("total", total); //总问题数
            jsonObjectIn.put("notRectified", notRectified); //未整改问题数
            jsonObjectIn.put("rectifieding", rectifieding); //正在整改问题数
            jsonObjectIn.put("value", percentage); //督促整改占比
            jSONArray.add(jsonObjectIn);
        }
        jsonObject.put("data", jSONArray);
        return jsonObject;

    }

    /**
     * 根据planId与userId获取状态
     *
     * @param
     * @return
     */
    @GetMapping("/getByPlanUserId")
    public gov.pbc.xjcloud.provider.contract.utils.R<Map<String, Object>> getByPlanUserId(@RequestParam(name = "planId", required = true) int planId, @RequestParam(name = "userId", required = true) int userId) {
//        List<Map<String, Object>> auditPlanInfo = auditPlanInfoServiceImpl.getByPlanUserId(planId, userId);
        List<Map<String, Object>> auditPlanInfo = auditPlanInfoServiceImpl.getByPlanId(planId);
        return new gov.pbc.xjcloud.provider.contract.utils.R().setData(auditPlanInfo);
    }

    @GetMapping("/getDeptInfoById")
    public R getDeptInfoById(@RequestParam(name = "id", required = true) int id) {
        return R.ok(remoteDeptService.dept(id).getData());
    }
}
