package gov.pbc.xjcloud.provider.contract.entity.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "entry_info")
public class EntryInfo implements Serializable, Cloneable {
    /**
     * 乐观锁
     */
    @Column(name = "revision")
    private Integer revision;
    /**
     * 创建人;username
     */
    @Column(name = "created_by")
    private String createdBy;
    /**
     * 创建时间;创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;
    /**
     * 更新人;username
     */
    @Column(name = "updated_by")
    private String updatedBy;
    /**
     * 更新时间;更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;
    /**
     * 词条名称;词条名称
     */
    @Column(name = "name")
    @Length(max = 32, min = 1,message = "词条名称字符长度介于1~32个字符之间")
    private String name;
    /**
     * 类型编号
     */
    @Column(name = "type_code")
    private String typeCode;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    @Column(name = "id")
    private String id;
    /**
     * 词条分类
     */
    @Column(name = "category_fk")
    @NotNull(message = "词条分类不能为空！")
    private Integer categoryFk;
    /**
     * 词条说明
     */
    @Column(name = "remarks")
    @Max(value = 128,message = "词条说明不超过128个字符")
    private String remarks;
    /**
     * 词条分级
     */
    @Column(name = "level")
    private int level;
    @Column(name = "audit_status")
    /** 审核状态;0-新建（待审核），1-审核通过，2-不通过 */
    private Integer auditStatus;
    /**
     * 流程实例 ACTIVITI 流程实例
     */
    @Column(name = "instance_id")
    private String instanceId;
    /**
     * 逻辑删除;0-未删除，1-已删除
     */
    @Column(name = "del_flag")
    @TableLogic
    private String delFlag;
    /**
     * 操作记录;0-删除，1-新增，2-修改（结合审核状态作为审核条目显示界面）
     */
    @Column(name = "opt_type")
    private String optType;

}
