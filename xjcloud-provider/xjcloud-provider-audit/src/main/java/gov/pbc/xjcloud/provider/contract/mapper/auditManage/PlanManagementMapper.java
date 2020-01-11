package gov.pbc.xjcloud.provider.contract.mapper.auditManage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanCheckList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanManagementMapper extends BaseMapper<PlanCheckList> {

    /**
     * 查询分页对象
     * @param page
     * @param query
     * @return
     */
    List<PlanCheckList> selectPlanCheckList(@Param("page") Page page, @Param("query") PlanCheckList query);

}
