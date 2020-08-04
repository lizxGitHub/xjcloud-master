package gov.pbc.xjcloud.provider.contract.dto;

import lombok.Data;

@Data
public class PlanCheckListDTO {
    private String planId;
    private String planName;
    private Integer impParentId;
    private Integer impId;
    private Integer auditId;
    private Integer auditParentId;
    private Integer days;
}
