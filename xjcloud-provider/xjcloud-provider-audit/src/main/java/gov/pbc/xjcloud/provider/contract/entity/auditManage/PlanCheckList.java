package gov.pbc.xjcloud.provider.contract.entity.auditManage;

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
    @Id
    @GeneratedValue
    @Column(name = "id")
    private String id ;
    /** 项目编号 */
    @Column(name = "project_code")
    private Integer projectCode ;
    /** 项目名称 */
    @Column(name = "project_name")
    private Integer projectName ;
    /** 实施机构 */
    @Column(name = "implementing_agency")
    private String implementingAgency ;
    /** 审计对象 */
    @Column(name = "audit_object")
    private String auditObject ;
    /** 审计性质 */
    @Column(name = "audit_nature")
    private String auditNature ;
    /** 审计年度 */
    @Column(name = "audit_year")
    private String auditYear ;
    /** 状态 */
    @Column(name = "status")
    private String status ;
    /** 问题词条 */
    @Column(name = "question_entry")
    private String questionEntry ;
    /** 问题严重程度 */
    @Column(name = "problem_severity")
    private String problemSeverity ;
    /** 整改情况 */
    @Column(name = "rectify_situation")
    private String rectifySituation ;
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
    @Column(name = "audit_classification")
    private String auditClassification ;
    /** 审计经验 */
    @Column(name = "auditing_experience")
    private String auditingExperience ;
    /** 风险评估 */
    @Column(name = "risk_assessment")
    private String riskAssessment ;
    /** 逻辑删除;0-未删除，1-已删除 */
    @Column(name="del_flag")
    @TableLogic
    private String delFlag ;

}
