package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PlanManagementService extends IService<PlanCheckList> {

    Page<PlanCheckList> selectPlanCheckList(Page page, PlanCheckList query);

    List<Map<String, Object>> selectEntryByCategoryId(String categoryId);

    List<Map<String, Object>>  selectEntryByQuery(PlanCheckList query, Long pageStart, Long pageNo);

    int  countEntryByQuery(PlanCheckList query);

    List<Map<String, Object>> groupCountEntryByQuery(PlanCheckList query, String groupName, String groupField);

    List<Map<String, Object>> countPlan(String agencyId);

    List<Map<String, Object>> statisticPlanReport( @Param("pageStart") Long pageStart, @Param("pageNo") Long pageNo);

    int countStatisticPlanReport();
}
