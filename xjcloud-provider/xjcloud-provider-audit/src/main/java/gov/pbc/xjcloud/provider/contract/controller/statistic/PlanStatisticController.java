/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package gov.pbc.xjcloud.provider.contract.controller.statistic;

import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import gov.pbc.xjcloud.provider.contract.utils.DeptUtil;
import gov.pbc.xjcloud.provider.contract.vo.DeptVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description:
 * @author: duanj
 * @date: 2020年01月18日 下午4:24:55
 */
@RestController
@RequestMapping("/audit-api/planStatistic")
public class PlanStatisticController {

    @Resource
    private PlanManagementService planManagementService;
    private final DeptUtil deptUtil;

    private final UserCenterService userCenterService;

    public PlanStatisticController(DeptUtil deptUtil, UserCenterService userCenterService) {
        this.deptUtil = deptUtil;
        this.userCenterService = userCenterService;
    }

    @Value("${audit.superDeptId}")
    private int superDeptId;

    /**
     * 审计统计图表
     * @return
     */
    @ApiOperation("审计统计图表")
    @RequestMapping("/questionStatistic")
    public R questionStatistic(PlanCheckList query, HttpServletResponse response) {
        R<JSONObject> r = new R<>();
        try {
            List<String> groupFilters = new ArrayList<String>();
            List<String> groupFieldFilters = new ArrayList<String>();
            List<String> subtitles = new ArrayList<String>();
            AtomicBoolean hasDept = new AtomicBoolean(false);
            if (StringUtils.contains(query.getImplementingAgencyId(), "all")) {
                query.setImplementingAgencyId("");
                groupFilters.add("implementing_agency_id");
                groupFieldFilters.add("pcl.implementing_agency_id");
                subtitles.add("实施机构");
                hasDept.set(true);
            }
            if (StringUtils.equals(query.getAuditNatureId(), "all")) {
                query.setAuditNatureId("");
                groupFilters.add("audit_nature_id");
                groupFieldFilters.add("audit_nature_entry.name");
                subtitles.add("审计性质");
            }
            if (StringUtils.equals(query.getAuditObjectId(), "all")) {
                hasDept.set(true);
                query.setAuditObjectId("");
                groupFilters.add("audit_object_id");
//                groupFieldFilters.add("audit_entry.name");
                groupFieldFilters.add("pcl.audit_object_id");
                subtitles.add("审计对象");
            }
            if (StringUtils.equals(query.getAuditObjectIdNew(), "all")) {
                hasDept.set(true);
                query.setAuditObjectId("");
                groupFilters.add("audit_object_id_new");
//                groupFieldFilters.add("audit_entry.name");
                groupFieldFilters.add("pcl.audit_object_id_new");
                subtitles.add("审计对象中支");
            }
            if (StringUtils.equals(query.getProblemSeverityId(), "all")) {
                query.setProblemSeverityId("");
                groupFilters.add("problem_severity_id");
                groupFieldFilters.add("problem_severity_entry.name");
                subtitles.add("严重程度");
            }
            if (StringUtils.equals(query.getRectifySituationId(), "all")) {
                query.setRectifySituationId("");
                groupFilters.add("rectify_situation_id");
                groupFieldFilters.add("rectify_situation_entry.name");
                subtitles.add("整改情况");
            }
            if (StringUtils.equals(query.getProjectType(), "all")) {
                query.setProjectType("");
                groupFilters.add("project_type");
                groupFieldFilters.add("project_type_entry.name");
                subtitles.add("项目类型");
            }
            //判断是否是数字串
            Pattern p=Pattern.compile("\\d+");
            JSONObject data = new JSONObject();
            if (null != groupFilters) {
                if (groupFilters.size() == 1) { //一维生成扇形图
                    data.put("type", 0);
                    String groupField = groupFilters.get(0);
                    String groupName = groupFieldFilters.get(0);
                    data.put("subtitle", subtitles.get(0));
                    //根据groupName统计问题数
                    List<Map<String, Object>> groupList = planManagementService.groupCountEntryByQuery(query, groupName, groupField);
                    List<String> nameList = new ArrayList<>();
                    JSONArray statisticJArr = new JSONArray();
                    for (Map<String, Object> groupData : groupList) {
                        if (groupData.containsKey("name")) {

                            if (hasDept.get()&&p.matcher((String)groupData.get("name")).matches()) {
                                gov.pbc.xjcloud.provider.contract.utils.R rdept = userCenterService.dept(Integer.parseInt(groupData.get("name").toString()));
                                JSONObject deptJSON = (JSONObject) JSONObject.toJSON(rdept);
                                if (null != deptJSON && "0".equals(deptJSON.get("code").toString())) {
                                    JSONObject deptData = (JSONObject) JSONObject.toJSON(deptJSON.get("data"));
                                    groupData.put("name", deptData.get("name"));
                                }
                            }

                            nameList.add(groupData.get("name").toString());
                            JSONObject statisticJson = new JSONObject();
                            statisticJson.put("name", groupData.get("name"));
                            statisticJson.put("value", groupData.get("value"));
                            statisticJArr.add(statisticJson);
                        } else {
                            nameList.add(groupData.get("id").toString());
                            JSONObject statisticJson = new JSONObject();
                            statisticJson.put("name", groupData.get("id"));
                            statisticJson.put("value", groupData.get("value"));
                            statisticJArr.add(statisticJson);
                        }
                    }
                    data.put("statisticData", statisticJArr);
                    data.put("legendData", nameList.toArray());
                } else if (groupFilters.size() == 2) { //二维生成柱状
//                    data.put("type",1);
//                    data.put("legendData",new String[]{"销量", "产量"});
//                    data.put("xData",new String[]{"周一", "周二", "周三", "周四"});
//                    JSONArray statisticJArr = new JSONArray();
//                    JSONObject statisticJson1 = new JSONObject();
//                    statisticJson1.put("name","销量");
//                    statisticJson1.put("type","bar");
//                    statisticJson1.put("data",new String[]{"10", "20", "30", "40"});
////            statisticJson1.put("name","单价");
////            statisticJson1.put("value","335");
//                    JSONObject statisticJson2 = new JSONObject();
//                    statisticJson2.put("name","产量");
//                    statisticJson2.put("type","bar");
//                    statisticJson2.put("data",new String[]{"50", "30", "20", "100"});
////            statisticJson2.put("name","总价");
////            statisticJson2.put("value","310");
//                    JSONObject statisticJson3 = new JSONObject();
////            statisticJson3.put("name","销量");
////            statisticJson3.put("value","234");
//                    JSONObject statisticJson4 = new JSONObject();
////            statisticJson4.put("name","产量");
////            statisticJson4.put("value","135");
//                    statisticJArr.add(statisticJson1);
//                    statisticJArr.add(statisticJson2);
////            statisticJArr.add(statisticJson3);
////            statisticJArr.add(statisticJson4);
//                    data.put("statisticData",statisticJArr);
                }
            }
            r.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 基本情况
     * @return
     */
    @ApiOperation("基本情况")
    @RequestMapping("/questionStatisticBasic")
    public R questionStatisticBasic(String auditYear) {
        R<JSONObject> r = new R<>();
        try {
            JSONObject data = new JSONObject();
            List<Map<String, Object>> list = planManagementService.groupCountEntry(auditYear);
//            List<Map<String, Object>> list = new ArrayList<>();
            data.put("statisticData", list);
            r.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * 项目类型
     * @return
     */
    @ApiOperation("项目类型")
    @RequestMapping("/questionStatisticProType")
    public R questionStatisticProType(String auditYear, String deptId) {
        R<JSONObject> r = new R<>();
        try {
            JSONObject data = new JSONObject();
            List<Map<String, Object>> list = planManagementService.groupCountProType(auditYear, deptId);
            data.put("statisticData", list);
            r.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 项目类型
     * @return
     */
    @ApiOperation("项目名称")
    @RequestMapping("/questionStatisticProName")
    public R questionStatisticProName(String auditYear, String deptId) {
        R<JSONObject> r = new R<>();
        try {
            JSONObject data = new JSONObject();
            List<Map<String, Object>> list = planManagementService.groupCountProName(auditYear, deptId);
            List<Map<String,Object>> result = new ArrayList<>();
            Map<Object, List<Map<String, Object>>> all = list.stream().collect(Collectors.groupingBy(e -> e.get("name")));
            all.keySet().stream().forEach(key->{
                List<Map<String, Object>> mapList = all.get(key);
                Map<String,Object> dataMap = Maps.newHashMap();
                dataMap.put("name",key);
                long wzg =mapList.stream().filter(e1 -> !"1003".equals(e1.get("status"))).count();
                long cs = mapList.stream().filter(e1 -> "1005".equals(e1.get("status"))).count();
                dataMap.put("wts",mapList.size());
                dataMap.put("wzg",wzg);
                dataMap.put("cs",cs);
                result.add(dataMap);
            });
            data.put("statisticData", result);
//            data.put("statisticData", list);
            r.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * 不同状态审计统计个数
     * @return
     */
    @ApiOperation("不同状态审计统计个数")
    @PostMapping("/countOfPlan")
    public R countOfPlan(String agenceId, String auditYear, HttpServletResponse response) {
        R<JSONObject> r = new R<>();
        try {
            JSONObject data = new JSONObject();
            List<Map<String, Object>> resultList = planManagementService.countPlan(agenceId, auditYear);
            if (null != resultList && resultList.size() > 0) {
                data = JSONObject.parseObject(JSONObject.toJSONString(resultList.get(0)));
            }
            r.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    @ApiOperation("审计查询")
    @GetMapping(value = {"report", ""})
    public R planList(Page<Map<String, Object>> page, String auditYear, int deptId,Boolean isSuperAdmin) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Long resultCount = 0L;
        try {
            if (deptId != 0 && deptId != 10000) {
                List<Map<String, Object>> resultListOld =  planManagementService.statisticPlanReportByDeptId(null, null, auditYear, deptId);
                for (int i = 0; i < resultListOld.size(); i++) {
                    Map<String, Object> data = resultListOld.get(i);
                    Map<String, Object> m = planManagementService.selectProNumAndOverTime(Integer.valueOf(String.valueOf(data.get("implementingAgencyId"))), auditYear);
                    data.put("projectSum", String.valueOf(m.get("projectSum")));
                    data.put("implementingAgencyId", data.get("implementingAgencyId").toString());
                    resultList.add(data);
                }
                resultCount = Long.valueOf(planManagementService.countStatisticPlanReportByDeptId(deptId));
            } else {
                //总的汇总报告
                List<Map<String, Object>> resultListStatisAll = planManagementService.statisticPlanReportAll(null, null, auditYear);
                Map<String, Object> allData = resultListStatisAll.get(0);
                Map<String, Object> m1 = planManagementService.selectProNumAndOverTime(-1, auditYear);
                allData.put("projectSum", String.valueOf(m1.get("projectSum")));
                resultList.add(allData);

                //获取机构
                //按dept查询问题数 key
                List<DeptVO> deptChild = deptUtil.findChildBank(0, "中支");
                List<Map<String, Object>> resultListStatis = planManagementService.statisticPlanReport(null, null, auditYear);
                Map<Integer, Map<String, Object>> datagroup = new HashMap<>();
                for (Map<String, Object> statisData : resultListStatis) {
                    Map<String, Object> m = planManagementService.selectProNumAndOverTime(Integer.valueOf(String.valueOf(statisData.get("implementingAgencyId"))), auditYear);
                    statisData.put("projectSum", String.valueOf(m.get("projectSum")));
                    statisData.put("implementingAgencyId", statisData.get("implementingAgencyId").toString());
                    datagroup.put(Integer.valueOf(statisData.get("implementingAgencyId").toString()), statisData);
                }
                for (DeptVO deptVO : deptChild) {
                    Map<String, Object> data = new HashMap<>();
                    if (datagroup.containsKey(deptVO.getDeptId())) {
                        resultList.add(datagroup.get(deptVO.getDeptId()));
                    } else {
                        Map<String, Object> m = planManagementService.selectProNumAndOverTime(deptVO.getDeptId(), auditYear);
                        data.put("projectSum", String.valueOf(m.get("projectSum")));
                        data.put("overTimeNum", "0");
                        data.put("implementingAgencyId", deptVO.getDeptId());
                        data.put("projectCount", "0");
                        data.put("finishCount", "0");
                        data.put("noFinishCount", "0");
                        data.put("timeoutCount", "0");
                        resultList.add(data);
                    }
                }
                // 如若不是超管，则只显示自己所在的审计单位的报告
                if(BooleanUtil.isFalse(isSuperAdmin)){
                    List<Map<String, Object>> collect = resultList.stream().filter(e -> e.get("implementingAgencyId").equals(deptId)).collect(Collectors.toList());
                    resultList = collect;
                }
                resultCount = Long.valueOf(planManagementService.countStatisticPlanReport());
            }
            page.setRecords(resultList);
            page.setTotal(resultCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }
}
