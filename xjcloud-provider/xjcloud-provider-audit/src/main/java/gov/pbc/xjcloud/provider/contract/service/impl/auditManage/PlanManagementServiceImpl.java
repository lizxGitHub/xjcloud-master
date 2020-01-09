package gov.pbc.xjcloud.provider.contract.service.impl.auditManage;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.mapper.auditManege.PlanManagementMapper;
import gov.pbc.xjcloud.provider.contract.service.auditManage.PlanManagementService;
import org.springframework.stereotype.Service;

@Service
public class PlanManagementServiceImpl extends ServiceImpl<PlanManagementMapper, PlanCheckList> implements PlanManagementService {
}
