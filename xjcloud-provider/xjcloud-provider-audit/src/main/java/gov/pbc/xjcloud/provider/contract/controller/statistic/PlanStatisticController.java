/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package gov.pbc.xjcloud.provider.contract.controller.statistic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**   
 * @Description:           
 * @author: duanj
 * @date:   2020年01月18日 下午4:24:55
 */
@RestController
@RequestMapping("/audit-api/planStatistic")
public class PlanStatisticController {

    @Resource
    private PlanManagementService planManagementService;

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
            if(StringUtils.contains(query.getImplementingAgencyId(),"all")){
                query.setImplementingAgencyId("");
                groupFilters.add("implementing_agency_id");
                groupFieldFilters.add("agency_entry.name");
                subtitles.add("实施机构");
            }
            if(StringUtils.equals(query.getAuditNatureId(),"all")){
                query.setAuditNatureId("");
                groupFilters.add("audit_nature_id");
                groupFieldFilters.add("audit_nature_entry.name");
                subtitles.add("审计性质");
            }
            if(StringUtils.equals(query.getAuditObjectId(),"all")){
                query.setAuditObjectId("");
                groupFilters.add("audit_object_id");
                groupFieldFilters.add("audit_entry.name");
                subtitles.add("审计对象");
            }
            if(StringUtils.equals(query.getProblemSeverityId(),"all")){
                query.setProblemSeverityId("");
                groupFilters.add("problem_severity_id");
                groupFieldFilters.add("problem_severity_entry.name");
                subtitles.add("严重程度");
            }
            if(StringUtils.equals(query.getRectifySituationId(),"all")){
                query.setRectifySituationId("");
                groupFilters.add("rectify_situation_id");
                groupFieldFilters.add("rectify_situation_entry.name");
                subtitles.add("整改情况");
            }
            JSONObject data = new JSONObject();

            if(null!=groupFilters){
                if(groupFilters.size()==1){ //一维生成扇形图
                    data.put("type",0);
                    String groupField = groupFilters.get(0);
                    String groupName = groupFieldFilters.get(0);
                    data.put("subtitle", subtitles.get(0));
                    //根据groupName统计问题数
                    List<Map<String,Object>> groupList = planManagementService.groupCountEntryByQuery(query, groupName, groupField);
                    List<String> nameList = new ArrayList<>();
                    JSONArray statisticJArr = new JSONArray();
                    for (Map<String,Object> groupData : groupList){
                        if(groupData.containsKey("name")) {
                            nameList.add(groupData.get("name").toString());
                            JSONObject statisticJson = new JSONObject();
                            statisticJson.put("name", groupData.get("name"));
                            statisticJson.put("value", groupData.get("value"));
                            statisticJArr.add(statisticJson);
                        }
                    }
                    data.put("statisticData",statisticJArr);
                    data.put("legendData",nameList.toArray());
                }else if (groupFilters.size()==2){ //二维生成柱状
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
     * 不同状态审计统计个数
     * @return
     */
    @ApiOperation("不同状态审计统计个数")
    @PostMapping("/countOfPlan")
    public R countOfPlan(String agenceId, HttpServletResponse response) {
        R<JSONObject> r = new R<>();
        try {
            JSONObject data = new JSONObject();
            List<Map<String,Object>> resultList = planManagementService.countPlan(agenceId);
            if(null!=resultList&&resultList.size()>0){
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
    public R planList(Page<Map<String, Object>> page){
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            resultList =  planManagementService.statisticPlanReport(page.getCurrent()-1, page.getSize());
            page.setRecords(resultList);
            page.setTotal(Long.valueOf(planManagementService.countStatisticPlanReport()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }
}
