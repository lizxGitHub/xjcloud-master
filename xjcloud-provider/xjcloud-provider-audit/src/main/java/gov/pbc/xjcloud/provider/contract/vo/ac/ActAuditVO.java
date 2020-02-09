package gov.pbc.xjcloud.provider.contract.vo.ac;

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

    private String projectName;

    private String projectCode ;

    private String implementingAgencyId ;

    private String auditObjectId ;

    private String auditNatureId ;

    private String auditYear ;

    private String status ;
}
