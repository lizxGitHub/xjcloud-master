package gov.pbc.xjcloud.provider.contract.service.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.dto.PlanCheckListDTO;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.PlanOverTimeTip;

import java.util.List;
import java.util.Map;

public interface PlanManagementService extends IService<PlanCheckList> {

    Page<PlanCheckList> selectPlanCheckList(Page page, PlanCheckList query);

    Page<PlanCheckList> selectPlanCheckListByAdmin(Page page, PlanCheckList query);

    List<Map<String, Object>> selectEntryByCategoryId(String categoryId);

    Map<String, Object> selectEntryById(String id);

    List<Map<String, Object>>  selectEntryByQuery(PlanCheckList query, Long pageStart, Long pageNo);

    int  countEntryByQuery(PlanCheckList query);

    List<Map<String, Object>> groupCountEntryByQuery(PlanCheckList query, String groupName, String groupField);

    List<Map<String, Object>> groupCountEntry(String auditYear);

    List<Map<String, Object>> groupCountProType(String auditYear, String deptId);

    List<Map<String, Object>> groupCountProName(String auditYear, String deptId);

    List<Map<String, Object>> countPlan(String agencyId,String auditYear);

    List<Map<String, Object>> statisticPlanReportByDeptId(  Long pageStart, Long pageNo, String auditYear, int deptId);

    int countStatisticPlanReport();

    List<Map<String, Object>> statisticPlanReport(  Long pageStart, Long pageNo,  String auditYear);

    int countStatisticPlanReportByDeptId(int deptId);

    List<Map<String, Object>> getShortPlans(List deptChild, String status, String auditYear);


    List<Map<String, Object>> getShortPlansNew(int implementingAgencyId, String status, String auditYear);

    int saveReturnPK(PlanCheckList planCheckList);

    List<PlanCheckListDTO> findDeadlinePlanList();

    /**
     * 查找是否已提示
     * @param dtoObj
     * @return
     */
    PlanOverTimeTip findCheckListTip(PlanOverTimeTip dtoObj);

    /**
     * 插入主键
     * @param tip
     * @return
     */
    int insertTip(PlanOverTimeTip tip);

    Map<String, Object> selectProNumAndOverTime(int implementingAgencyId, String auditYear);

    /**
     * 查找超时记录
     * @return
     */
    Boolean delOverTips();
}
