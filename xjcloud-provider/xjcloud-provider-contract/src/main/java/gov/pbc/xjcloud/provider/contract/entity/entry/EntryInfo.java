package gov.pbc.xjcloud.provider.contract.entity.entry;

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
    private Integer revision ;
    /** 创建人;username */
    private String createdBy ;
    /** 创建时间;创建时间 */
    private Date createdTime ;
    /** 更新人;username */
    private String updatedBy ;
    /** 更新时间;更新时间 */
    private Date updatedTime ;
    /** 词条名称;词条名称 */
    private String name ;
    /** 词条类型;分类词条 */
    private String type_ ;
    /** 类型编号 */
    private String typeCode ;
    /** 主键 */
    @Id
    @GeneratedValue
    private Integer id ;
    /** 父级主键 */
    private Integer parentId ;
    /** 词条说明 */
    private String remarks ;
    /** 审核状态;0-新建（待审核），1-审核通过，2-不通过 */
    private Integer auditStatus ;
    /** 逻辑删除;0-未删除，1-已删除 */
    private String delFlag ;
    /** 操作记录;0-删除，1-新增，2-修改（结合审核状态作为审核条目显示界面） */
    private String optType ;

}
