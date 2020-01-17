package gov.pbc.xjcloud.provider.contract.entity.auditManage;

import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="audit_plan_info")
public class AuditPlanInfo {

    private String id;

    private PlanCheckList planCheckList;

    private String status;

    private String roleId;

    private String opinion;

}
