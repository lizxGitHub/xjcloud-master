package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditProjectInfo;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.AuditProjectInfoMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.AuditProjectInfoService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class AuditProjectInfoServiceImpl extends IBaseServiceImpl<AuditProjectInfoMapper, AuditProjectInfo> implements AuditProjectInfoService {

    @Resource
    private AuditProjectInfoMapper auditProjectInfoMapper;

    @Override
    public Page<AuditProjectInfo> selectAuditProject(Page page, AuditProjectInfo query) {
        List<AuditProjectInfo> list =  auditProjectInfoMapper.selectAuditProject(page, query);
        page.setRecords(list);
        return page;
    }

    @Override
    public AuditProjectInfo getById(String id, String roleId) {
        return auditProjectInfoMapper.getById(id, roleId);
    }

    @Override
    public void updateById(String id, String status) {
        auditProjectInfoMapper.updateById(id, status);
    }

    @Override
    public void updateByPlanId(String planId, String roleId, String status) {
        auditProjectInfoMapper.updateByPlanId(planId, roleId, status);
    }

    @Override
    public void insertA(AuditProjectInfo query) {
        auditProjectInfoMapper.insertA(query);
    }
}
