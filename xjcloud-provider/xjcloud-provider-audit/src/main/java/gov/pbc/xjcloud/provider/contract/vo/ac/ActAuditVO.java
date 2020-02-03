package gov.pbc.xjcloud.provider.contract.vo.ac;

import gov.pbc.xjcloud.provider.activiti.api.vo.ActVO;
import lombok.Data;

import java.util.Date;

/**
 * 审计流程
 */
@Data
public class ActAuditVO extends ActVO {

    private int auditStatus;

    private Date delayDate;

    private String delayRemarks;
}
