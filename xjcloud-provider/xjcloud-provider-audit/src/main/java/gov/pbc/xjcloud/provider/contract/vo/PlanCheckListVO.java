package gov.pbc.xjcloud.provider.contract.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PlanCheckListVO  {


    /** 创建人 */
    @Column(name = "created_by")
    private int createdBy ;
    /** 创建时间 */
    @Column(name = "created_time")
    private Date createdTime ;
    /** 更新人 */
    @Column(name = "update_by")
    private int updatedBy ;
    /** 更新时间 */
    @Column(name = "update_time")
    private Date updatedTime ;
    /** 主键 */
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="id")
    @SequenceGenerator(name="id",sequenceName="S_QUENSE",allocationSize=1)
    private int id;
    /** 项目编号 */
    @Column(name = "project_code")
    private String projectCode ;
    /** 项目名称 */
    @Column(name = "project_name")
    private String projectName ;
    /** 项目类型 */
    @Column(name = "project_type")
    private String projectType ;
    /** 实施机构 */
    @Column(name = "implementing_agency_id")
    private String implementingAgencyId ;
    /** 审计对象 */
    @Column(name = "audit_object_id")
    private String auditObjectId ;
    /** 审计性质 */
    @Column(name = "audit_nature_id")
    private String auditNatureId ;
    /** 审计年度 */
    @Column(name = "audit_year")
    private String auditYear ;
    /** 状态 */
    @Column(name = "status")
    private String status ;
    /** 问题词条 */
    @Column(name = "question_entry_id")
    private String questionEntryId ;
    /** 问题严重程度 */
    @Column(name = "problem_severity_id")
    private String problemSeverityId ;
    /** 整改情况 */
    @Column(name = "rectify_situation_id")
    private String rectifySituationId ;
    /** 问题定性 */
    @Column(name = "problem_characterization")
    private String problemCharacterization ;
    /** 问题描述 */
    @Column(name = "problem_description")
    private String problemDescription ;
    /** 可能影响 */
    @Column(name = "may_affect")
    private String mayAffect ;
    /** 整改建议 */
    @Column(name = "rectification_suggestions")
    private String rectificationSuggestions ;
    /** 审计依据 */
    @Column(name = "audit_basis")
    private String auditBasis ;
    /** 审计分类 */
    @Column(name = "audit_classification_id")
    private String auditClassificationId ;
    /** 审计经验 */
    @Column(name = "auditing_experience")
    private String auditingExperience ;
    /** 风险评估 */
    @Column(name = "risk_assessment_id")
    private String riskAssessmentId ;
    /** 逻辑删除;0-未删除，1-已删除 */
    @Column(name="del_flag")
    @TableLogic
    private String delFlag ;
    /**
     * 流程实例 ACTIVITI 流程实例
     */
    @Column(name = "instance_id")
    private String instanceId;

    @TableField(exist = false)
    private String attentionId;
    /**
     * 延期意见（领导填写）
     */
    private String delayRemarks;
    /**
     * 实施机构一般员工
     */
    private int impUserId;
    /**
     * 实施机构管理员
     */
    private int impAdminId;
    /**
     * 审计对象一般员工
     */
    private int auditUserId;
    /**
     * 审计对象管理员
     */
    private int auditAdminId;
    /**
     * 出现频次
     */
    private String frequency;
    /**
     * 整改措施
     */
    private String rectifyWay;
    /**
     * 计划整改时长
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date planTime;
    /**
     * 整改结果
     */
    private String rectifyResult;
    /**
     * 延期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date delayDate;

    private String implementingAgencyName;

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getImplementingAgencyId() {
        return implementingAgencyId;
    }

    public void setImplementingAgencyId(String implementingAgencyId) {
        this.implementingAgencyId = implementingAgencyId;
    }

    public String getAuditObjectId() {
        return auditObjectId;
    }

    public void setAuditObjectId(String auditObjectId) {
        this.auditObjectId = auditObjectId;
    }

    public String getAuditNatureId() {
        return auditNatureId;
    }

    public void setAuditNatureId(String auditNatureId) {
        this.auditNatureId = auditNatureId;
    }

    public String getAuditYear() {
        return auditYear;
    }

    public void setAuditYear(String auditYear) {
        this.auditYear = auditYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuestionEntryId() {
        return questionEntryId;
    }

    public void setQuestionEntryId(String questionEntryId) {
        this.questionEntryId = questionEntryId;
    }

    public String getProblemSeverityId() {
        return problemSeverityId;
    }

    public void setProblemSeverityId(String problemSeverityId) {
        this.problemSeverityId = problemSeverityId;
    }

    public String getRectifySituationId() {
        return rectifySituationId;
    }

    public void setRectifySituationId(String rectifySituationId) {
        this.rectifySituationId = rectifySituationId;
    }

    public String getProblemCharacterization() {
        return problemCharacterization;
    }

    public void setProblemCharacterization(String problemCharacterization) {
        this.problemCharacterization = problemCharacterization;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getMayAffect() {
        return mayAffect;
    }

    public void setMayAffect(String mayAffect) {
        this.mayAffect = mayAffect;
    }

    public String getRectificationSuggestions() {
        return rectificationSuggestions;
    }

    public void setRectificationSuggestions(String rectificationSuggestions) {
        this.rectificationSuggestions = rectificationSuggestions;
    }

    public String getAuditBasis() {
        return auditBasis;
    }

    public void setAuditBasis(String auditBasis) {
        this.auditBasis = auditBasis;
    }

    public String getAuditClassificationId() {
        return auditClassificationId;
    }

    public void setAuditClassificationId(String auditClassificationId) {
        this.auditClassificationId = auditClassificationId;
    }

    public String getAuditingExperience() {
        return auditingExperience;
    }

    public void setAuditingExperience(String auditingExperience) {
        this.auditingExperience = auditingExperience;
    }

    public String getRiskAssessmentId() {
        return riskAssessmentId;
    }

    public void setRiskAssessmentId(String riskAssessmentId) {
        this.riskAssessmentId = riskAssessmentId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getAttentionId() {
        return attentionId;
    }

    public void setAttentionId(String attentionId) {
        this.attentionId = attentionId;
    }

    public String getDelayRemarks() {
        return delayRemarks;
    }

    public void setDelayRemarks(String delayRemarks) {
        this.delayRemarks = delayRemarks;
    }

    public int getImpUserId() {
        return impUserId;
    }

    public void setImpUserId(int impUserId) {
        this.impUserId = impUserId;
    }

    public int getImpAdminId() {
        return impAdminId;
    }

    public void setImpAdminId(int impAdminId) {
        this.impAdminId = impAdminId;
    }

    public int getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(int auditUserId) {
        this.auditUserId = auditUserId;
    }

    public int getAuditAdminId() {
        return auditAdminId;
    }

    public void setAuditAdminId(int auditAdminId) {
        this.auditAdminId = auditAdminId;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRectifyWay() {
        return rectifyWay;
    }

    public void setRectifyWay(String rectifyWay) {
        this.rectifyWay = rectifyWay;
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    public String getRectifyResult() {
        return rectifyResult;
    }

    public void setRectifyResult(String rectifyResult) {
        this.rectifyResult = rectifyResult;
    }

    public Date getDelayDate() {
        return delayDate;
    }

    public void setDelayDate(Date delayDate) {
        this.delayDate = delayDate;
    }

    public String getImplementingAgencyName() {
        return implementingAgencyName;
    }

    public void setImplementingAgencyName(String implementingAgencyName) {
        this.implementingAgencyName = implementingAgencyName;
    }
}
