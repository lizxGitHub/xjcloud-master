package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanInfoServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 */
@Api("审计计划管理")
@RestController
@RequestMapping("/audit-api/planInfo/")
public class PlanInfoController {

    @Resource
    private PlanInfoServiceImpl planInfoService;


}
