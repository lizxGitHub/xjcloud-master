package gov.pbc.xjcloud.provider.contract.vo;

import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import lombok.Data;

@Data
public class PlanCheckListVO extends PlanCheckList {

    private String implementingAgencyName;

}
