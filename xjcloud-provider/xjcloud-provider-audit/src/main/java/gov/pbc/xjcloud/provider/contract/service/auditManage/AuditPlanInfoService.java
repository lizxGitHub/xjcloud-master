package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;

public interface AuditPlanInfoService extends IService<AuditPlanInfo> {

    Page<AuditPlanInfo> selectAuditPlanInfoList(Page page, AuditPlanInfo query);

}
