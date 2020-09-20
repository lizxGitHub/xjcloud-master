package gov.pbc.xjcloud.provider.contract.entity.auditManage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name="audit_plan_info")
public class AuditPlanInfo {

    @TableId(value = "id", type = IdType.UUID)
    @Column(name = "id")
    private String id;

    private int planId;

    private int userId;

    private String opinion;

    private int nextUserId;

    private String nextUserName;

    private Date createTime;

    private int taskCode;

    private String taskName;

}
