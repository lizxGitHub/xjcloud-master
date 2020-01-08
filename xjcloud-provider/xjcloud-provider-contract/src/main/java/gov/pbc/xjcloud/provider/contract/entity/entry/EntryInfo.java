package gov.pbc.xjcloud.provider.contract.entity.entry;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Table(name="entry_info")
public class EntryInfo implements Serializable,Cloneable{
    /** 乐观锁 */
    @Column(name="revision")
    private Integer revision ;
    /** 创建人;username */
    @Column(name="create_by")
    private String createdBy ;
    /** 创建时间;创建时间 */
    @Column(name="create_time")
    private Date createdTime ;
    /** 更新人;username */
    @Column(name="update_by")
    private String updatedBy ;
    /** 更新时间;更新时间 */
    @Column(name="updateTime")
    private Date updatedTime ;
    /** 词条名称;词条名称 */
    @Column(name = "name")
    private String name ;
    /** 词条类型;分类词条 */
    @Column(name="category_id")
    private String categoryId;
    /** 类型编号 */
    @Column(name="type_code")
    private String typeCode ;
    /** 主键 */
    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id ;
    /** 词条分类 */
    private Integer categoryFk ;
    /** 词条说明 */
    @Column(name="remarks")
    private String remarks ;
    @Column(name="audit_status")
    /** 审核状态;0-新建（待审核），1-审核通过，2-不通过 */
    private Integer auditStatus ;
    /** 流程实例 ACTIVITI 流程实例 */
    @Column(name="instance_id")
    private String instanceId ;
    /** 逻辑删除;0-未删除，1-已删除 */
    @Column(name="del_flag")
    @TableLogic
    private String delFlag ;
    /** 操作记录;0-删除，1-新增，2-修改（结合审核状态作为审核条目显示界面） */
    @Column(name="opt_type")
    private String optType ;

}
