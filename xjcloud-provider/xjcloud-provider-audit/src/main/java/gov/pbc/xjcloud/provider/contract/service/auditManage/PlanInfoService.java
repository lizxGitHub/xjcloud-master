package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;

public interface PlanInfoService extends IService<PlanInfo> {

    Page<PlanInfo> selectPlanInfoList(Page page, PlanInfo query);

    PlanInfo getById(String id);

    void updateByPlanId(String planId, String status);

    void insertA(PlanInfo query);
}
