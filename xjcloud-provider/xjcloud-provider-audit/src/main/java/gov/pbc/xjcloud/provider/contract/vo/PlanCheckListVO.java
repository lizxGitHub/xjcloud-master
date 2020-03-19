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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

    /** 审计对象 */
    @TableField(exist = false)
    private String auditObjectName ;
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

    @TableField(exist = false)
    private String rectifySituationName ;

    /**
     * 整改评价
     */
    private String rectifyEvaluation;

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
    private String planTime;
    /**
     * 整改结果
     */
    private String rectifyResult;
    /**
     * 延期时间(天)
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private String delayDate;

    private String implementingAgencyName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

}
