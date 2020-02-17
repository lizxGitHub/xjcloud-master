package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanInfoServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 *
 */
@Api("审计计划管理")
@RestController
@RequestMapping("/audit-api/planInfo/")
public class PlanInfoController {

    @Resource
    private PlanInfoServiceImpl planInfoService;

    @GetMapping("user/opinion")
    public R<PlanInfo> userOpinion(PlanInfo planInfo) {
        QueryWrapper<PlanInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", planInfo.getUserId());
        queryWrapper.eq("plan_id", planInfo.getPlanId());
        queryWrapper.eq("type", planInfo.getType());
        queryWrapper.eq("status_user", planInfo.getStatusUser());
        try {
            PlanInfo result = planInfoService.getOne(queryWrapper);
            return R.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

}
