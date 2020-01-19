package gov.pbc.xjcloud.provider.contract.entity.auditManage;

import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="audit_project_info")
public class AuditProjectInfo {

    private String id;

    private PlanCheckList planCheckList;

    private String status;

    private String roleId;

    private String opinion;

}
