package gov.pbc.xjcloud.provider.contract.entity.auditManage;

import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="plan_info")
public class PlanInfo {

    private String id;

    private PlanCheckList planCheckList;

    private String status;

    private String opinion;

}
