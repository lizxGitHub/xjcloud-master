package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import gov.pbc.xjcloud.common.core.constant.PaginationConstants;
import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.activiti.api.feign.RemoteProcessService;
import gov.pbc.xjcloud.provider.activiti.api.vo.ActVO;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.usercenter.api.feign.RemoteUserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 接口审核
 */
@RestController
@RequestMapping("audit-api/task")
public class TaskController {
    /**
     * 流程服务接口
     */
    RemoteProcessService remoteProcessService;

    RemoteUserService remoteUserService;

    @Value("${audit.flow-key:auditApply}")
    private String auditFlowDefKey;


    /**
     * 分页获取流程待办任务
     *
     * @param params 分页参数
     * @return
     */
    @GetMapping("todo")
    public R<List<ActVO>> LustToDo(Map<String, Object> params) {
        R r = null;
        try {
            r = remoteProcessService.todo(auditFlowDefKey, params);
        } catch (Exception e) {
            r=new R().setData(new ArrayList<>());
            e.printStackTrace();
        }

        return r;
    }

    /**
     * 办理流程任务
     *
     * @param params 分页参数
     * @return
     */
    @PostMapping("complete")
    public R complete(String taskId, Map<String, Object> params) {
        R complete = remoteProcessService.complete(taskId, String.valueOf(params));
        return complete;
    }

}
