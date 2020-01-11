package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanCheckList;

public interface PlanManagementService extends IService<PlanCheckList> {

    Page<PlanCheckList> selectPlanCheckList(Page page, PlanCheckList query);

}
