package gov.pbc.xjcloud.provider.contract.entity.entry;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name="entry_category")
public class EntryCategory implements Serializable,Cloneable{
    /** 乐观锁 */
    @Column(name = "revision")
    private Integer revision ;

    /** 乐观锁 */
    @Column(name = "def_key")
    private String defKey ;

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
    private Integer id ;
    /** 上级分类 */
    @Column(name = "parent_id")
    private Integer parentId ;
    /** 词条分级;有几级就允许有几个子类 */
    @Column(name = "level")
    private Integer level ;
    /** 名称 */
    @Column(name = "name")
    private String name ;
    /** 逻辑删除;逻辑删除字段（1-已删除，2-未删除） */
    @TableLogic
    private String delFlag ;}
