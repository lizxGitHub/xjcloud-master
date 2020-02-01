package gov.pbc.xjcloud.provider.contract.controller;

import com.baomidou.mybatisplus.extension.api.R;
import gov.pbc.xjcloud.provider.contract.entity.State;
import gov.pbc.xjcloud.provider.contract.enumutils.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 */
@RestController
@RequestMapping("/audit-api/state")
public class StateController {

    /**
     * 获取词条分类信息
     * @return
     */
    @GetMapping("/list/{role}")
    public R<List<State>> listR(@PathVariable String role) {
        Class<?> classz;
        if ("1".equals(role)) {
            classz = SHNormalStateEnum.class;
        } else {
            classz = SHLeaderStateEnum.class;
        }
        List<State> list= EnumUtils.getEnumToList(classz);
        return R.ok(list);
    }

    /**
     * 获取计划状态分类列表
     * @return
     */
    @GetMapping("/list/planState")
    public R<List<State>> listR() {
        List<State> list= EnumUtils.getEnumToList(PlanStatusEnum.class);
        return R.ok(list);
    }
}
