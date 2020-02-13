package gov.pbc.xjcloud.provider.contract.mapper;

import gov.pbc.xjcloud.provider.contract.entity.PlanTimeTemp;
import org.apache.ibatis.annotations.Param;

public interface PlanTimeTempMapper extends IBaseMapper<PlanTimeTemp> {

    PlanTimeTemp getByPlanId(@Param("planId") int planId);
}
