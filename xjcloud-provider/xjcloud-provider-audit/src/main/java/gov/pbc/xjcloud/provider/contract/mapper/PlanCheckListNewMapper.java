package gov.pbc.xjcloud.provider.contract.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckListNew;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanCheckListNewMapper extends IBaseMapper<PlanCheckListNew> {

    List<PlanCheckListNew> selectAll(Page page, @Param("query") PlanCheckListNew query, @Param("type") int type, @Param("userId") int userId, @Param("status") String status);

    PlanCheckListNew selectById(@Param("id") int id);

    int saveReturnPK(@Param("planCheckList") PlanCheckListNew planCheckList);
}
