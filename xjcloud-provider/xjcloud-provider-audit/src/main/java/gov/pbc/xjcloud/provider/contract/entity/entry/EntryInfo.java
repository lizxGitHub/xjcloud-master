package gov.pbc.xjcloud.provider.contract.entity.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
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
    @Length(max = 32, min = 1, message = "词条名称字符长度介于1~32个字符之间")
    @NotNull
    private String name;

    @Column(name = "name1")
    @Length(max = 32, min = 1, message = "词条名称二级字符长度介于1~32个字符之间")
    private String name1;

    @Column(name = "name2")
    @Length(max = 32, message = "词条名称三级字符长度介于1~32个字符之间")
    private String name2;

    @Column(name = "name3")
    @Length(max = 32, message = "词条名称四级字符长度介于1~32个字符之间")
    private String name3;
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
    @Length(max = 128, message = "词条说明不超过128个字符")
    private String remarks;

    /**
     * 逻辑删除;0-未删除，1-已删除
     */
    @Column(name = "del_flag")
    @TableLogic
    private String delFlag;
    /**
     * 组合名称
     */
    @TableField(exist = false)
    private String concatName;

    public String getConcatName() {
        String name =this.name;
        if(StringUtils.isNotBlank(this.name1)){
            name+='-'+this.name1;
        }
        if(StringUtils.isNotBlank(this.name2)){
            name+='-'+this.name2;
        }
        if(StringUtils.isNotBlank(this.name3)){
            name+='-'+this.name3;
        }
        return name;
    }
}
