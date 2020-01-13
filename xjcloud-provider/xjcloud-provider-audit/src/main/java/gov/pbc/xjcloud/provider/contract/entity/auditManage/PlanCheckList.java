package gov.pbc.xjcloud.provider.contract.entity.auditManage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import gov.pbc.xjcloud.provider.contract.enumutils.StateEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="plan_check_list")
public class PlanCheckList implements Serializable,Cloneable{

    /** 乐观锁 */
    @Column(name = "revision")
    private Integer revision ;
    /** 创建人 */
    @Column(name = "created_by")
    private String createdBy ;
    /** 创建时间 */
    @Column(name = "created_time")
    private Date createdTime ;
    /** 更新人 */
    @Column(name = "update_by")
    private String updatedBy ;
    /** 更新时间 */
    @Column(name = "update_time")
    private Date updatedTime ;
    /** 主键 */
    @TableId(value = "id", type = IdType.UUID)
    @Column(name = "id")
    private String id;
    /** 项目编号 */
    @Column(name = "project_code")
    private String projectCode ;
    /** 项目名称 */
    @Column(name = "project_name")
    private String projectName ;
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

}
