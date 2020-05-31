package gov.pbc.xjcloud.provider.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import io.swagger.models.auth.In;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Table(name="plan_check_list")
@TableName(value = "plan_check_list")
public class PlanCheckListNew implements Serializable,Cloneable{

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 创建人 */
    private Integer createdBy ;
    /** 创建时间 */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createdTime ;
    /** 更新时间 */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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
    /** 审计对象 */
    private String auditObjectIdNew ;
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
    private Integer impUserId;
    /**
     * 实施机构管理员
     */
    private Integer impAdminId;
    /**
     * 审计对象一般员工
     */
    private Integer auditUserId;
    /**
     * 审计对象管理员
     */
    private Integer auditAdminId;
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
     * 整改评价
     */
    private String rectifyEvaluation;

    /**
     * 整改评价  员工
     */
    private String evaluation;
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
     * auditStatus
     */
    private String auditStatus1;
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
    @TableField(exist = false)
    private Set<PlanInfo> planInfos;

    @TableField(exist = false)
    private String fileUri;
    @TableField(exist = false)
    private String createdUsername;


    public void setConcatQuestionEntry() {
        String name =this.questionEntryId1;
        if(StringUtils.isNotBlank(this.questionEntryId2)){
            name+='-'+this.questionEntryId2;
        }
        if(StringUtils.isNotBlank(this.questionEntryId3)){
            name+='-'+this.questionEntryId3;
        }
        if(StringUtils.isNotBlank(this.questionEntryId4)){
            name+='-'+this.questionEntryId4;
        }
        this.questionEntryId = name;
    }

    @TableField(exist = false)
    private String questionEntryId1 ;
    @TableField(exist = false)
    private String questionEntryId2 ;
    @TableField(exist = false)
    private String questionEntryId3 ;
    @TableField(exist = false)
    private String questionEntryId4 ;
    @TableField
    private String agencyLevel;
    @TableField
    private String enterTime;
    @TableField
    private String orgType;
    @TableField
    private String managerDutyType;
    @TableField
    private String riskType;
    @TableField
    private String functionType;
    @TableField
    private String auditSuggestions;
    /**
     * 问题类型
     */
    @TableField
    private String questionType;
    private String implementingAgencyName ;

    /**
     * column 转setter方法名称
     *
     * @param column
     * @return
     */
    private String generateSetterMethod(String column,String type) {
        String[] splits = column.split("_");
        StringBuffer settterName = new StringBuffer(type);
        for (String split : splits) {
            String firstChar = split.substring(0, 1);
            String otherChar = split.substring(1);
            settterName.append(firstChar.toUpperCase() + otherChar);
        }
        return settterName.toString();
    }

}
