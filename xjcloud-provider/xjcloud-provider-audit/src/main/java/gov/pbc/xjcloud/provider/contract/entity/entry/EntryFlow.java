package gov.pbc.xjcloud.provider.contract.entity.entry;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @Id
    @Column(name = "id")
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
    private String name;
    /**
     * 词条分类
     */
    @Column(name = "category_Fk")
    private String categoryFk;
    /**
     * 词条说明
     */
    @Column(name = "remarks")
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
    @Column(name = "del_Flag")
    @TableLogic
    private String delFlag;

}