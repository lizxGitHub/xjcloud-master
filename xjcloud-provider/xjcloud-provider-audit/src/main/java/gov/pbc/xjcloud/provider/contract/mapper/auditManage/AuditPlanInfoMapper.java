package gov.pbc.xjcloud.provider.contract.mapper.auditManage;

import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AuditPlanInfoMapper extends IBaseMapper<AuditPlanInfo> {

    List<Map<String, Object>> getByPlanUserId(@Param("planId") int planId, @Param("userId") int userId);

    List<Map<String, Object>> getByPlanId(@Param("planId") int planId);
}
