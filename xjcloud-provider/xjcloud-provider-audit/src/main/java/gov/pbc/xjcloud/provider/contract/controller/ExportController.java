/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package gov.pbc.xjcloud.provider.contract.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import gov.pbc.xjcloud.provider.contract.service.entry.EntryFlowService;
import gov.pbc.xjcloud.provider.contract.utils.ExcelUtils;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryFlowVO;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/exportExcel")
    public void exportExcel(PlanCheckList query, HttpServletResponse response) {
        try {
            String fileName = "审计查询";
            String[] columns = new String[] { "项目名称","项目类型","项目状态","问题词条","问题严重程度", "问题定性", "问题描述", "严重程度", "机构名称", "审计对象", "审计性质", "审计依据", "审计分类", "审计经验", "年度", "机构名称", "整改情况", "风险评估"};
            String[] keys = new String[] { "project_name","project_type","statusVal","question_entry_id","problem_severity_id", "problem_characterization", "problem_description", "problem_severity_name", "agency_name", "audit_object_name", "audit_nature_name","audit_basis","audit_classification_id","auditing_experience", "audit_year", "agency_name", "rectify_situation_name", "risk_assessment_name"};
            List<Map<String, Object>> list = planManagementService.selectEntryByQuery(query, null, null);
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
