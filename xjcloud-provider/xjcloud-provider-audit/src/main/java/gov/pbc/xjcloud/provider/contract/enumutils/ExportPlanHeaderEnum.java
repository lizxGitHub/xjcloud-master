package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ExportPlanHeaderEnum {

    var1("审计年度", "audit_year", true,false),
    var2("机构层级", "agency_level", false,false),
    var3("进点时间", "enter_time", false,false),
    var4("项目类型", "project_type", true,true),
    var23("审计性质", "audit_nature_id", true,true),
    var5("审计对象中支", "audit_object_id_new", false,false),
    var5_1("审计对象部门", "audit_object_id", false,false),
    var6("组织形式", "org_type", false,false),
    var7("项目名称", "project_name", true,false),
    var8("问题定性", "problem_characterization", true,false),
    var9("问题类型", "question_type", true,true),
    var10("问题描述", "problem_description", false,false),
    var11("问题词条一级", "question_entry_id1", true,false),
    var12("问题词条二级", "question_entry_id2", false,false),
    var13("问题词条三级", "question_entry_id3", false,false),
    var14("问题词条四级", "question_entry_id4", false,false),
    var15("主要负责人责任类型", "manager_duty_type", false,false),
    var16("整改情况", "rectify_situation_id", true,false),
    var17("问题严重程度", "problem_severity_id", false,true),
    var18("风险类别", "risk_type", false,false),
    var19("所属职能", "function_type", false,false),
    var20("可能影响", "may_affect", false,false),
    var21("整改意见", "rectification_suggestions", false,false),
    var22("审计建议", "audit_suggestions", false,false);

    @Setter
    private String name;
    @Setter
    private String column;

    @Setter
    private boolean required;

    @Getter
    private Boolean isEntry;


}
