package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.CommonConstants;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.constants.PlanConstants;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryFlow;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.feign.dept.RemoteDeptService;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.service.impl.PlanCheckListServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.DeptUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.vo.DeptVO;
import gov.pbc.xjcloud.provider.contract.vo.KeyValue;
import gov.pbc.xjcloud.provider.contract.vo.PlanCheckListVO;
import gov.pbc.xjcloud.provider.contract.vo.TreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private PlanCheckListServiceImpl planCheckListService;

    @Autowired
    private DeptUtil deptUtil;

    @Autowired
    private UserCenterService userCenterService;

    @Autowired
    private RemoteDeptService remoteDeptService;

    @Autowired
    private EntryServiceImpl entryService;

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
    public R<Boolean> uncheck(String userId, String checkStr) {
        if (StringUtils.isBlank(checkStr)) {
            return R.failed("关注列数为空");
        }
        if (StringUtils.isBlank(userId)) {
            return R.failed("用户不存在");
        }
        try {
            planManagementService.cancelCheckAttention(userId, checkStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(Boolean.TRUE);
    }
    @ApiOperation("保存报告")
    @PutMapping(value = {"deptYearReport/save"})
    public R<Boolean> deptYearReportSave(String deptId, String auditYear, String content) {
        try {
            planManagementService.saveDeptYearReport(deptId, auditYear, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(Boolean.TRUE);
    }
    @ApiOperation("获取报告")
    @PutMapping(value = {"deptYearReport/load"})
    public R<String> deptYearReportLoad(String deptId, String auditYear) {
        String content = "";
        try {
            content = planManagementService.loadDeptYearReportContent(deptId, auditYear);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(content);
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
            List<Map<String, Object>> planListold = planManagementService.selectEntryByQuery(query, page.getCurrent() - 1, page.getSize());
            List<EntryInfo> list = entryService.list();
            Map<String, EntryInfo> entryMap = list.stream().filter(e -> StringUtils.isNotBlank((String) e.getConcatName()))
                    .collect(Collectors.toMap(e -> e.getId(), e -> e));
            for (Map<String, Object> plan : planListold) {
                PlanCheckListVO planCheckListDTO = changeToDTO(null);
                Field[] declaredFields = planCheckListDTO.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    String name = field.getName();
                    if (null != plan.get(name)) {
                        String objVal = plan.get(name).toString();
                        if (StringUtils.isNotBlank(objVal) && null != entryMap.get(objVal)) {
                            plan.put(name, entryMap.get(objVal).getConcatName());
                        }
                    }
                }
                planList.add(plan);
            }
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
        List<Map<String, Object>> maps = planManagementService.selectEntryByCategoryId(categoryId);
        maps.stream().filter(Objects::nonNull).forEach(e -> {
            String name = (String) e.get("name");
            String name1 = (String) e.get("name1");
            String name2 = (String) e.get("name2");
            String name3 = (String) e.get("name3");
            if (StringUtils.isNotBlank(name1)) {
                name += '-' + name1;
            }
            if (StringUtils.isNotBlank(name2)) {
                name += '-' + name2;
            }
            if (StringUtils.isNotBlank(name3)) {
                name += '-' + name3;
            }
            e.put("name", name);
        });

        return R.ok(maps);
    }

    @GetMapping("/selectEntryById")
    public R<Map<String, Object>> selectEntryById(@RequestParam(name = "id", required = true) String id) {
        Map<String, Object> maps = planManagementService.selectEntryById(id);
        return R.ok(maps);
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
     * 创建编辑问题
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
//                int num = planManagementService.saveReturnPK(planCheckList);
                planManagementService.save(planCheckList);
            } else {
                planCheckList = planManagementService.getById(planCheckList.getId());
                if (planCheckList != null) {
                    planManagementService.updateById(planCheckList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }

    /**
     * 更新问题
     *
     * @return
     */
    @ApiOperation("更新问题")
    @PostMapping("/updatePlan")
    public R<Boolean> updatePlan(PlanCheckList planCheckList) {
        R<Boolean> r = new R<>();
        try {
            PlanCheckList planCheckListOld = planManagementService.getById(planCheckList.getId());
            if (planCheckListOld != null) {
                planCheckListOld.setFrequency(planCheckList.getFrequency());
                planCheckListOld.setRectifyWay(planCheckList.getRectifyWay());
//                planCheckListOld.setPlanTime(planCheckList.getPlanTime());
                planCheckListOld.setRectifyResult(planCheckList.getRectifyResult());
                planManagementService.updateById(planCheckListOld);
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
     * 用户行为：删除问题
     *
     * @param id
     * @return
     */
    @ApiOperation("用户删除问题")
    @DeleteMapping("deletePlan")
    public R<Boolean> deletePlan(String ids) {
        R<Boolean> r = new R<>();
        Boolean b;
        if (StringUtils.isBlank(ids)) {
            throw new NullArgumentException("请求参数不存在");
        } else {
            b = planManagementService.removeByIds(Arrays.asList(StringUtils.split(ids,",")));
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
            PlanCheckListVO planCheckListDTO = changeToDTO(planOne);
//            planCheckListDTO.setImplementingAgencyName(dept.getData().get("name").toString());
            r.setData(planCheckListDTO);
        } catch (Exception e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;
    }

    private String getFieldMethodType(String name, String type) {
        StringBuffer method = new StringBuffer();
        char c = name.charAt(0);
        String s = String.valueOf(c).toUpperCase();
        method.append(type).append(s).append(name.substring(1));
        return method.toString();
    }

    @ApiOperation("获取问题详细信息")
    @GetMapping("detail/{id}")
    public R<PlanCheckListVO> getPLanInfoDetailById(@PathVariable String id) {
        R<PlanCheckListVO> r = new R<>();
        try {
            if (StringUtils.isBlank(id)) {
                return r.failed("参数错误，请检查");
            }
            List<EntryInfo> list = entryService.list();
            Map<String, EntryInfo> entryMap = list.stream().filter(e -> StringUtils.isNotBlank((String) e.getConcatName()))
                    .collect(Collectors.toMap(e -> e.getId(), e -> e));
            PlanCheckList planOne = planManagementService.getById(id);
            PlanCheckListVO planCheckListDTO = changeToDTO(planOne);
            Field[] declaredFields = planCheckListDTO.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                String name = field.getName();
                field.setAccessible(true);
                String fieldSetter = getFieldMethodType(name, CommonConstants.SETTER);
                Class<?> type = field.getType();
                if (type.equals(String.class)) {
                    Object invoke = field.get(planCheckListDTO);
                    if (null != entryMap.get(invoke)) {
                        Method methodSetter = planCheckListDTO.getClass().getDeclaredMethod(fieldSetter, type);
                        methodSetter.invoke(planCheckListDTO, (Object) entryMap.get(invoke).getConcatName());
                    }
                }
            }
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

    @ApiOperation("超时提醒个数统计")
    @GetMapping("deadcount/{userId}")
    public R getDeadCount(@PathVariable(name = "userId") String userId) {
        if (StringUtils.isBlank(userId)) {
            return R.ok(0);
        }
        int deadlinePlan = planManagementService.getDeadlinePlan(userId);
        return R.ok(deadlinePlan);
    }

    @ApiOperation("超时提醒分页")
    @GetMapping("deadcount/{userId}/page")
    public R<Page<PlanCheckList>> getDeadCountPage(@PathVariable("userId") String userId, Map<String, Object> params) {
        if (StringUtils.isBlank(userId)) {
            return R.ok(new Page<>());
        }
        Page<PlanCheckList> page = new Page();
        String current = params.getOrDefault("current", "1").toString();
        String size = params.getOrDefault("size", "10").toString();
        page.setCurrent(Long.parseLong(current));
        page.setSize(Long.parseLong(size));
        Date now = DateTime.now().toDate();
        Map<String, Object> query = new HashMap<>();
        query.put("userId", userId);
        query.put("now", now);
        query.put("status", PlanStatusEnum.FILE.getCode());
        Page<PlanCheckList> result = planManagementService.getDeadlinePlanPage(query, page);
        return R.ok(result);
    }

    @GetMapping("findAllDeptGroup")
    public R findAllDeptGroup() {
        List<PlanCheckList> list = planManagementService.list();
        JSONObject data = new JSONObject();
        JSONArray impData = new JSONArray();
        JSONArray auditData = new JSONArray();
        Map<String, List<PlanCheckList>> impCollect = list.stream().filter(e -> StringUtils.isNotBlank(e.getImplementingAgencyId())).collect(Collectors.groupingBy(e -> e.getImplementingAgencyId()));
        Map<String, List<PlanCheckList>> auditCollect = list.stream().filter(e -> StringUtils.isNotBlank(e.getAuditObjectId())).collect(Collectors.groupingBy(e -> e.getAuditObjectId()));
        impCollect.entrySet().stream().forEach(e -> {
            gov.pbc.xjcloud.provider.contract.utils.R impR = userCenterService.dept(Integer.parseInt(e.getKey()));
            impData.add(impR.getData());
        });
        auditCollect.entrySet().stream().forEach(e -> {
            gov.pbc.xjcloud.provider.contract.utils.R impR = userCenterService.dept(Integer.parseInt(e.getKey()));
            auditData.add(impR.getData());
        });
        data.put("impData", impData);
        data.put("auditData", auditData);
        return new R().setData(data);
    }


    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 分组
     *
     * @param params
     * @return
     */
    @GetMapping("planList/groupList")
    public R planListGroup(@RequestParam Map<String, Object> params) {
        List<Map<String, Object>> mapList = planManagementService.groupCount(params);
        List<EntryInfo> list = entryService.list();
        Map<String, EntryInfo> entryMap = list.stream().filter(e -> StringUtils.isNotBlank((String) e.getConcatName()))
                .collect(Collectors.toMap(e -> e.getId(), e -> e));
        JSONObject jsonObject = new JSONObject();
        List<String> deptKey = Arrays.asList("implementingAgencyId", "auditObjectId");
        AtomicBoolean isGroupByDept = new AtomicBoolean(false);
        deptKey.forEach(e -> {
            if ("all".equals(params.get(e))) {
                isGroupByDept.set(true);
                return;
            }
        });
        if (null != mapList && !mapList.isEmpty()) {

            mapList.stream().forEach(e -> {
                if (isGroupByDept.get()) {
                    gov.pbc.xjcloud.provider.contract.utils.R rdept = userCenterService.dept(Integer.parseInt(e.get("name").toString()));
                    JSONObject deptJSON = (JSONObject) JSONObject.toJSON(rdept);
                    if (null != deptJSON && "0".equals(deptJSON.get("code").toString())) {
                        JSONObject deptData = (JSONObject) JSONObject.toJSON(deptJSON.get("data"));
                        e.put("name",deptData.get("name"));
                    }
                } else {
                    if (entryMap.containsKey(e.get("name"))) {
                        e.put("name",entryMap.get(e.get("name")).getConcatName());
                    }
                }
            });

        }
        List<Object> legend = new ArrayList<>();
        List<Map<String, Object>> tableData = new ArrayList<>();
        AtomicInteger total = new AtomicInteger();
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        mapList.forEach(e -> {
            int num =Integer.parseInt(e.get("value").toString());
            total.getAndAdd(num);
        });
        mapList.forEach(e -> {
            String name = (String) e.get("name");
            name = name==null?"全部":name;
            e.put("name",name);
            legend.add(name==null?"全部":name);
            int num = Integer.parseInt(e.get("value").toString());
            Map<String, Object> row = new HashMap<>();
            e.entrySet().stream().forEach(es ->row.put(es.getKey(), es.getValue()));
            String percent = "0%";
            if(total.get()!=0){
                percent = numberFormat.format(((double) num) / ((double) total.get()));
            }
            row.put("percent", percent);
            tableData.add(row);
        });
        jsonObject.put("count", mapList.size());
        jsonObject.put("tableData", tableData);
        jsonObject.put("legend", legend);
        jsonObject.put("records", mapList);
        return new R().setData(jsonObject);
    }

    private boolean filterOverTime(Map<String, Object> e, Map<String, Object> params) {
        boolean result=true;
        String overTime =(String) params.get("overTime");
        if(null!=overTime&&!"all".equals(overTime)){

        }
        return true;
    }

    /**
     * 导出统计表
     * @param params
     * @param response
     */
    @GetMapping("export/groupcount")
    public void export(@RequestParam Map<String, Object> params, HttpServletResponse response){
        List<Map<String, Object>> mapList = planManagementService.groupCount(params);
        List<EntryInfo> list = entryService.list();
        Map<String, EntryInfo> entryMap = list.stream().filter(e -> StringUtils.isNotBlank((String) e.getConcatName()))
                .collect(Collectors.toMap(e -> e.getId(), e -> e));
        JSONObject jsonObject = new JSONObject();
        List<String> deptKey = Arrays.asList("implementingAgencyId", "auditObjectId");
        AtomicBoolean isGroupByDept = new AtomicBoolean(false);
        deptKey.forEach(e -> {
            if ("all".equals(params.get(e))) {
                isGroupByDept.set(true);
                return;
            }
        });
        if (null != mapList && !mapList.isEmpty()) {

            mapList.stream().forEach(e -> {
                if (isGroupByDept.get()) {
                    gov.pbc.xjcloud.provider.contract.utils.R rdept = userCenterService.dept(Integer.parseInt(e.get("name").toString()));
                    JSONObject deptJSON = (JSONObject) JSONObject.toJSON(rdept);
                    if (null != deptJSON && "0".equals(deptJSON.get("code").toString())) {
                        JSONObject deptData = (JSONObject) JSONObject.toJSON(deptJSON.get("data"));
                        e.put("name",deptData.get("name"));
                    }
                } else {
                    if (entryMap.containsKey(e.get("name"))) {
                        e.put("name",entryMap.get(e.get("name")).getConcatName());
                    }
                }
            });

        }
        List<Map<String, Object>> tableData = new ArrayList<>();
        List<KeyValue> queryList = initQuery(params,deptKey,entryMap);

        AtomicInteger total = new AtomicInteger();
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        mapList.forEach(e -> {
            int num =Integer.parseInt(e.get("value").toString());
            total.getAndAdd(num);
        });
        mapList.forEach(e -> {
            String name = (String) e.get("name");
            name = name==null?"全部":name;
            e.put("name",name);
            int num = Integer.parseInt(e.get("value").toString());
            Map<String, Object> row = new HashMap<>();
            e.entrySet().stream().forEach(es ->row.put(es.getKey(), es.getValue()));
            String percent = "0%";
            if(total.get()!=0){
                percent = numberFormat.format(((double) num) / ((double) total.get()));
            }
            row.put("percent", percent);
            tableData.add(row);
        });
        jsonObject.put("tableData", tableData);

        exportFile(response,queryList,tableData);

    }

    private void exportFile(HttpServletResponse response, List<KeyValue> queryList, List<Map<String, Object>> tableData) {
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(1000);
        sxssfWorkbook.createSheet("数据");
        SXSSFSheet sheet = sxssfWorkbook.getSheetAt(0);
        //添加结果数据
        AtomicInteger lastRowNumber = new AtomicInteger(0);
        createQueryHeader(lastRowNumber,sxssfWorkbook,sheet,queryList);
        createTableData(lastRowNumber,sxssfWorkbook,sheet,tableData);
        OutputStream out =null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("统计查询列表.xlsx", "UTF-8"));
            out = response.getOutputStream();
            sxssfWorkbook.write(out);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createTableData(AtomicInteger lastRowNum, SXSSFWorkbook sxssfWorkbook, SXSSFSheet sheet, List<Map<String, Object>> tableData) {
        Iterator<Map<String, Object>> dataIt = tableData.iterator();
        SXSSFRow dataTipRow = sheet.createRow(lastRowNum.getAndIncrement());
        SXSSFCell dataHeadCell = dataTipRow.createCell(0);
        dataHeadCell.setCellValue("查询结果");
        dataHeadCell.setCellStyle(getCellStyle(sxssfWorkbook,true));
        SXSSFRow dataHeader = sheet.createRow(lastRowNum.getAndIncrement());
        String[] strings = new String[]
                {"序号","类型","个数","占比"};
        initHeader(strings,dataHeader,getCellStyle(sxssfWorkbook,true));
        CellStyle normalStyle = getCellStyle(sxssfWorkbook,false);
        int index =1;
        while (dataIt.hasNext()){
            Map<String, Object> data = dataIt.next();
            SXSSFRow row = sheet.createRow(lastRowNum.getAndIncrement());
            SXSSFCell var0 = row.createCell(0);
            SXSSFCell var1 = row.createCell(1);
            SXSSFCell var2 = row.createCell(2);
            SXSSFCell var3 = row.createCell(3);
            var0.setCellValue(index++);
            var1.setCellValue((String) data.get("name"));
            var2.setCellValue((long)data.get("value"));
            var3.setCellValue((String) data.get("percent"));
            var0.setCellStyle(getCellStyle(sxssfWorkbook,false));
            var1.setCellStyle(getCellStyle(sxssfWorkbook,false));
            var2.setCellStyle(getCellStyle(sxssfWorkbook,false));
        }
    }

    private void initHeader(String[] tip, SXSSFRow dataHeader,CellStyle cellStyle) {
        for(int i = 0 ; i<tip.length;i++){
            SXSSFCell cell = dataHeader.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(tip[i]);
        }
    }

    private int createQueryHeader(AtomicInteger rowNum, SXSSFWorkbook sxssfWorkbook, SXSSFSheet sheet, List<KeyValue> queryList) {
        if(null!=queryList&&!queryList.isEmpty()){
            Row row=sheet.createRow(rowNum.getAndIncrement());
            Cell cell = row.createCell(0);
            cell.setCellValue("查询条件");
            cell.setCellStyle(getCellStyle(sxssfWorkbook,true));
            for (KeyValue e:queryList){
                int curCellIndex = 0;
                Row queryRow=sheet.createRow(rowNum.getAndIncrement());
                Cell var1 = queryRow.createCell(curCellIndex++);
                var1.setCellValue(e.getKey()+":");
                var1.setCellStyle(getCellStyle(sxssfWorkbook,false));
                Cell var2 = queryRow.createCell(curCellIndex++);
                var2.setCellStyle(getCellStyle(sxssfWorkbook,false));
                String value =(String) e.getValue();
                var2.setCellValue("all".equalsIgnoreCase(value)?"全部条件":value);
            }
        }

        return rowNum.get();
    }

    private CellStyle getCellStyle(Workbook workbook,boolean bold){
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(bold);//加粗
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.BLACK.getIndex());
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }


    /**
     * 处理空字符串
     *
     * @param string
     * @return
     */
    private String parseText(String string) {
        if (string == null) {
            return "";
        }
        return string.trim();
    }

    /**
     * 导出查询列表字段
     *
     * @param params
     * @param deptKey
     * @param entryMap
     * @return
     */
    private List<KeyValue> initQuery(Map<String, Object> params, List<String> deptKey, Map<String, EntryInfo> entryMap) {
        params.remove("groupBy");
        List<KeyValue> result=new ArrayList<>();
        Map<String,String> keyMap = JSONUtil.toBean(JSONUtil.toJsonStr(new PlanConstants()),Map.class);
        params.keySet().stream().forEach(e->{
            String key = e;
            Object value = params.get(key);
            KeyValue keyValue =new KeyValue();
            keyValue.setValue(value);
            keyValue.setKey(keyMap.get(key));
            result.add(keyValue);
            if(deptKey.contains(key)){
                gov.pbc.xjcloud.provider.contract.utils.R rdept = userCenterService.dept(Integer.parseInt((String) value));
                JSONObject deptJSON = (JSONObject) JSONObject.toJSON(rdept);
                if (null != deptJSON && "0".equals(deptJSON.get("code").toString())) {
                    JSONObject deptData = (JSONObject) JSONObject.toJSON(deptJSON.get("data"));
                    keyValue.setValue(deptData.get("name"));
                }
            }else {
                if(entryMap.containsKey(value)){
                    keyValue.setValue(entryMap.get(value));
                }
            }
        });
        return result;
    }
}
