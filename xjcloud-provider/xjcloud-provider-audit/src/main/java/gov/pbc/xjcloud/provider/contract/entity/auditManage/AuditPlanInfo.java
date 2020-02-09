package gov.pbc.xjcloud.provider.contract.entity.auditManage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name="audit_plan_info")
public class AuditPlanInfo {

    @TableId(value = "id", type = IdType.UUID)
    @Column(name = "id")
    private String id;

    private int planId;

    private String status;

    private int userId;

    private String opinion;

}
