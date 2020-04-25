/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package gov.pbc.xjcloud.provider.contract.controller;

import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.ExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**   
 * @Description:           
 * @author: duanj
 * @date:   2020年01月9日 下午4:24:55
 */
@RestController
@RequestMapping("/audit-api/export")
public class ExportController {

    @Resource
    private PlanManagementService planManagementService;

    @Resource
    private EntryServiceImpl entryService;

    @RequestMapping("/exportExcel")
    public void exportExcel(PlanCheckList query, HttpServletResponse response) {
        try {
            String fileName = "审计查询";
            String[] columns = new String[] {
                    "项目名称","项目类型","项目状态","问题词条",
                    "问题严重程度", "问题定性", "问题描述", "严重程度",
                    "实施机构名称","审计对象", "审计性质", "审计依据",
                    "审计分类", "审计经验", "年度","整改情况",
                    "风险评估","整改开始时间","出现频次","整改时长",
                    "超时时长","整改结果","归档评估","整改评估","风险评估"
            };
            String[] keys = new String[] {
                    "project_name", "project_type","status","question_entry_id",
                    "problem_severity_id", "problem_characterization", "problem_description","problem_severity_name",
                    "implementing_agency_id", "audit_object_id", "audit_nature_id","audit_basis",
                    "audit_classification_id","auditing_experience", "audit_year","rectify_situation_name",
                    "risk_assessment_id","start_time_all","frequency","days",
                    "over_days","rectify_result","rectify_evaluation","evaluation","risk_assessment_id"
            };
            List<Map<String, Object>> list = planManagementService.selectEntryByQuery(query, null, null);
            Map<String, String> entryNameValue = entryService.listAll().stream().filter(e -> org.apache.commons.lang3.StringUtils.isNotBlank(e.getConcatName())).
                    collect(Collectors.toMap(e -> e.getId(), e -> e.getConcatName(), (e1, e2) -> e1));
            list.stream().forEach(e->{
                e.entrySet().stream().forEach(es->{
                    if(entryNameValue.containsKey(es.getValue())){
                        es.setValue(entryNameValue.get(es.getValue()));
                    }
                });
            });
            List<Map<String, Object>> listMap = new ArrayList<>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sheetName", "sheet1");
            listMap.add(map);
            listMap.addAll(list);
            //条件名：值集合
            List<String> filterListMap = new ArrayList<>();
            if(StringUtils.isNotBlank(query.getProjectName())){
                filterListMap.add("项目名称：-"+query.getProjectName());
            }
            if(StringUtils.isNotBlank(query.getProjectType())){
                filterListMap.add("项目名称：-"+query.getProjectType());
            }
            if(StringUtils.isNotBlank(query.getCostTime())){
                String costTimeStr = query.getCostTime();
                if("all".equalsIgnoreCase(costTimeStr)){
                    costTimeStr = "全部条件";
                }else {
                   int cost = Integer.parseInt(costTimeStr);
                   switch (cost){
                       case 7:
                           costTimeStr = "六个月以上一年以内";
                           break;
                       case 8:
                           costTimeStr = "一年以上";
                       default:
                           costTimeStr = cost +"个月";
                   }
                }
                filterListMap.add("整改时长：-"+costTimeStr);
            }
            if(StringUtils.isNotBlank(query.getOverTime())){
                String costTimeStr = query.getOverTime();
                if("all".equalsIgnoreCase(costTimeStr)){
                    costTimeStr = "全部条件";
                }else {
                    int cost = Integer.parseInt(costTimeStr);
                    switch (cost){

                        case 7:
                            costTimeStr = "六个月以上";
                            break;
                        default:
                            costTimeStr = cost +"个月";
                    }
                }
                filterListMap.add("超时情况：-"+costTimeStr);
            }
            if(StringUtils.isNotBlank(query.getProblemDescription())){
                filterListMap.add("问题清单：-"+query.getProblemDescription());
            }
            if(StringUtils.isNotBlank(query.getImplementingAgencyId())&&!StringUtils.contains(query.getImplementingAgencyId(),"all")){
                filterListMap.add("实施机构：-"+query.getImplementingAgencyId());
            }
            if(StringUtils.isNotBlank(query.getAuditNatureId())&&!StringUtils.equals(query.getAuditNatureId(),"all")){
                filterListMap.add("审计性质：-"+query.getAuditNatureId());
            }
            if(StringUtils.isNotBlank(query.getAuditObjectId())&&!StringUtils.equals(query.getAuditObjectId(),"all")){
                filterListMap.add("审计对象：-"+query.getAuditObjectId());
            }
            if(StringUtils.isNotBlank(query.getProblemSeverityId())&&!StringUtils.equals(query.getProblemSeverityId(),"all")){
                filterListMap.add("严重程度：-"+query.getProblemSeverityId());
            }
            if(StringUtils.isNotBlank(query.getRectifySituationId())&&!StringUtils.equals(query.getRectifySituationId(),"all")){
                filterListMap.add("整改情况：-"+query.getProblemSeverityId());
            }
            //查询条件名：值
            ExcelUtils.getEXCELFileWithHeader(fileName, fileName, listMap, columns, keys, response,filterListMap);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
