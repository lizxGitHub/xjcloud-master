package gov.pbc.xjcloud.provider.contract.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckListNew;
import org.apache.ibatis.annotations.Param;

public interface PlanCheckListService extends IService<PlanCheckListNew> {

    Page<PlanCheckListNew> selectAll(Page page, PlanCheckListNew query, int type, int userId, String status);

    PlanCheckListNew selectById(int id);

    int saveReturnPK(PlanCheckListNew planCheckList);

    void updatePlanById(PlanCheckListNew plan);

    Page<PlanCheckListNew> selectByStatuses(Page page, PlanCheckListNew query, String[] Statuses);

}
