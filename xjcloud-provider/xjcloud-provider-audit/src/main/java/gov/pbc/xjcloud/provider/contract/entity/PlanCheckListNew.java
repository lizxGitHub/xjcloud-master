package gov.pbc.xjcloud.provider.contract.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Table(name="plan_check_list")
public class PlanCheckListNew implements Serializable,Cloneable{

    /** 主键 */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="id")
    @SequenceGenerator(name="id",sequenceName="S_QUENSE",allocationSize=1)
    private int id;
    /** 创建人 */
    private int createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新时间 */
    private Date updatedTime ;
    /** 项目编号 */
    private String projectCode ;
    /** 项目名称 */
    private String projectName ;
    /** 项目类型 */
    private String projectType ;
    /** 实施机构 */
    private String implementingAgencyId ;
    /** 审计对象 */
    private String auditObjectId ;
    /** 审计性质 */
    private String auditNatureId ;
    /** 审计年度 */
    private String auditYear ;
    /** 状态 */
    private String status ;
    /** 问题词条 */
    private String questionEntryId ;
    /** 问题严重程度 */
    private String problemSeverityId ;
    /** 整改情况 */
    private String rectifySituationId ;
    /** 问题定性 */
    private String problemCharacterization ;
    /** 问题描述 */
    private String problemDescription ;
    /** 可能影响 */
    private String mayAffect ;
    /** 整改建议 */
    private String rectificationSuggestions ;
    /** 审计依据 */
    private String auditBasis ;
    /** 审计分类 */
    private String auditClassificationId ;
    /** 审计经验 */
    private String auditingExperience ;
    /** 风险评估 */
    private String riskAssessmentId ;
    /** 逻辑删除;0-未删除，1-已删除 */
    @Column(name="del_flag")
    @TableLogic
    private String delFlag ;
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
    private String rectifyMan;
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
     * 延期时间
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private String delayDate;

    /**
     * taskId
     */
    private String taskId;
    /**
     * taskName
     */
    @TableField(exist = false)
    private String taskName;

    /**
     * auditStatus
     */
    private String auditStatus;
    /**
     * 驳回反馈
     */
    @TableField(exist = false)
    private String rollbackText;

    /** 启动时间 */
    private Date startTime ;
    /** 归档时间 */
    private Date archiveTime ;
    /** 结果录入时间 */
    private Date resultEnterTime ;

    private Set<PlanInfo> planInfos;

    @TableField(exist = false)
    private String fileUri;
    @TableField(exist = false)
    private String createdUsername;



}
