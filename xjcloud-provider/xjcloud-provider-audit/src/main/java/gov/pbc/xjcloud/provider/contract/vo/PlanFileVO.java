package gov.pbc.xjcloud.provider.contract.vo;

import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanFile;
import lombok.Data;

@Data
public class PlanFileVO extends PlanFile {
    private String fileUrl;
}
