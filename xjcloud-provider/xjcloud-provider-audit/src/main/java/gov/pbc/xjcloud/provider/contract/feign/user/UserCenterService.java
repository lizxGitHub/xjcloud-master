package gov.pbc.xjcloud.provider.contract.feign.user;

import gov.pbc.xjcloud.provider.contract.config.FeignClientConfig;
import gov.pbc.xjcloud.provider.contract.utils.R;
import gov.pbc.xjcloud.provider.usercenter.api.entity.SysLog;
import gov.pbc.xjcloud.provider.usercenter.api.entity.SysRole;
import gov.pbc.xjcloud.provider.usercenter.api.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 远程调用
 */
@FeignClient(value = "xjcloud-provider-usercenter",configuration =FeignClientConfig.class )
public interface UserCenterService {

    /**
     * user
     * @param paramInteger
     * @return
     */
    @GetMapping({"/users/{id}"})
    R user(@PathVariable("id") Integer paramInteger);

    @GetMapping({"/users/info/{username}"})
    R<UserVO> info(@PathVariable("username") String paramString1, @RequestHeader("from") String paramString2);

    @GetMapping({"/users/ancestor/{username}"})
    R ancestorUsers(@PathVariable("username") String paramString);

    @GetMapping({"/users/dept/{deptId}/role/{roleName}"})
    R getUsersByRoleNameAndDept(@PathVariable("deptId") Integer paramInteger, @PathVariable("roleName") String paramString);

    @GetMapping({"/users/minDeptId/{minDeptId}/maxDeptId/{maxDeptId}/role/{roleName}"})
    R getUsersByRoleNameInDepts(@PathVariable("roleName") String paramString, @PathVariable("minDeptId") Integer paramInteger1, @PathVariable("maxDeptId") Integer paramInteger2);

    /**
     * role
     * @param paramList
     * @return
     */
    @GetMapping({"/role/ids"})
    R<List<SysRole>> list(@RequestParam("ids") List<String> paramList);

    @GetMapping({"/role/ids/permission/{permission}"})
    R<List<Integer>> listIds(@RequestParam("permission") String paramString);

    /**
     * log
     * @param paramSysLog
     * @param paramString
     * @return
     */
    @PostMapping({"/log/save"})
    R<Boolean> saveLog(@RequestBody SysLog paramSysLog, @RequestHeader("from") String paramString);

    /**
     * dept
     * @param paramInteger
     * @return
     */
    @GetMapping({"/depts/{id}"})
    R dept(@PathVariable("id") Integer paramInteger);

    @GetMapping({"/depts/parent/{parentId}/recursive/{recursive}"})
    R children(@PathVariable("parentId") Integer paramInteger, @PathVariable("recursive") boolean paramBoolean);

}
