package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.PlanTimeTemp;

public interface PlanTimeTempService extends IService<PlanTimeTemp> {

    PlanTimeTemp getByPlanId(int planId);

}
