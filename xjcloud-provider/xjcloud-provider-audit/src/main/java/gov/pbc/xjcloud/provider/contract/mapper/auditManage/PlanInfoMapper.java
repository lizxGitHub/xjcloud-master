package gov.pbc.xjcloud.provider.contract.mapper.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanInfoMapper extends IBaseMapper<PlanInfo> {
    /**
     * 查询分页对象
     * @param page
     * @param query
     * @return
     */
    List<PlanInfo> selectPlanInfoList(@Param("page") Page page, @Param("query") PlanInfo query);

    PlanInfo getById(@Param("id") String id);

    void updateByPlanId(@Param("planId") String planId, @Param("status") String status);

    void insertA(@Param("query") PlanInfo query);
}
