package gov.pbc.xjcloud.provider.contract.entity.check;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="user_attention_check")
@AllArgsConstructor
@NoArgsConstructor
public class UserAttentionCheck implements Serializable,Cloneable{
    /** 乐观锁 */
    private Integer revision ;
    /** 创建人 */
    private Integer createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private Integer updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;
    /** 用户id */
    private Integer userId ;
    /** 清单id */
    private String checkId ;

}