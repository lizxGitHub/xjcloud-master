package gov.pbc.xjcloud.provider.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="plan_time_temp")
public class PlanTimeTemp implements Serializable,Cloneable{

    /** 主键 */
    @TableId(value = "id", type = IdType.UUID)
    @Column(name = "id")
    private String id;
    /**
     * 计划id
     */
    private int planId;
    private Date startTimeAll ;
    private Date startTimePartOne ;
    private Date startTimePartTwo ;
    private Date endTimeAll ;
    private Float days;

}
