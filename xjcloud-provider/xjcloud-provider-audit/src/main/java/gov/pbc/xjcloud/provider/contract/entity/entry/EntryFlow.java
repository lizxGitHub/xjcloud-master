package gov.pbc.xjcloud.provider.contract.entity.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.enumutils.StateEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "entry_flow")
public class EntryFlow implements Serializable, Cloneable {
    /**
     * 乐观锁
     */
    @Column(name = "revision")
    private int revision;

    /**
     * 乐观锁
     */
    @Column(name = "type_code")
    private String typeCode;
    /**
     * 创建人
     */
    @Column(name = "created_by")
    private String createdBy;
    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;
    /**
     * 更新人
     */
    @Column(name = "updated_By")
    private String updatedBy;
    /**
     * 更新时间
     */
    @Column(name = "updated_Time")
    private Date updatedTime;
    /**
     * 主键
     */
    @Column(name = "id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 词条主键
     */
    @Column(name = "entry_Fk")
    private String entryFk;
    /**
     * 词条名称
     */
    @Column(name = "name")
    @Length(max = 32, min = 1,message = "词条名称字符长度介于1~32个字符之间")
    private String name;
    /**
     * 词条分类
     */
    @Column(name = "category_Fk")
    @NotNull(message = "词条分类不能为空！")
    private Integer categoryFk;
    /**
     * 词条说明
     */
    @Column(name = "remarks")
    @Length(max = 128,message = "词条说明不超过128个字符")
    private String remarks;
    /**
     * 用户行为;0-删除，1-新增，2-修改
     */
    @Column(name = "user_Opt")
    private String userOpt;
    /**
     * 流程实例
     */
    @Column(name = "instance_Id")
    private String instanceId;
    /**
     * 发起人;用户名
     */
    @Column(name = "apply_User")
    private String applyUser;
    /**
     * 审核人;用户名
     */
    @Column(name = "audit_User")
    private String auditUser;
    /**
     * 审核状态;0-待审批，1-通过，2-拒绝
     */
    @Column(name = "audit_Status")
    private String auditStatus;
    /**
     * 逻辑删除;0-未删除，1-已删除
     */
    @Column(name = "del_Flag",columnDefinition = "varchar default 0")
    @TableLogic
    private String delFlag;

    public EntryInfo getEntryInfo() {
        EntryInfo entryInfo = new EntryInfo();
        entryInfo.setId(this.getEntryFk());
        entryInfo.setCreatedTime(new Date());
        entryInfo.setCreatedBy(this.getCreatedBy());
        entryInfo.setTypeCode(this.getTypeCode());
        entryInfo.setCategoryFk(this.getCategoryFk());
        entryInfo.setName(this.getName());
        entryInfo.setRemarks(this.getRemarks());
        entryInfo.setRevision(this.getRevision());
        entryInfo.setDelFlag(DelConstants.EXITED);
        return entryInfo;
    }
}