package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.mapper.auditManage.AuditPlanInfoMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.AuditPlanInfoService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class AuditPlanInfoServiceImpl extends IBaseServiceImpl<AuditPlanInfoMapper, AuditPlanInfo> implements AuditPlanInfoService {

    @Resource
    private AuditPlanInfoMapper auditPlanInfoMapper;

    @Override
    public Page<AuditPlanInfo> selectAuditPlanInfoList(Page page, AuditPlanInfo query) {
        List<AuditPlanInfo> list =  auditPlanInfoMapper.selectAuditPlanInfoList(page, query);
        page.setRecords(list);
        return page;
    }
}
