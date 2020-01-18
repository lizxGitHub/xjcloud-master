package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditProjectInfo;

public interface AuditProjectInfoService extends IService<AuditProjectInfo> {

    Page<AuditProjectInfo> selectAuditProject(Page page, AuditProjectInfo query);

    AuditProjectInfo getById(String id, String roleId);

    void updateById(String id, String status);

    void updateByPlanId(String planId, String roleId, String status);

    void insertA(AuditProjectInfo query);
}
