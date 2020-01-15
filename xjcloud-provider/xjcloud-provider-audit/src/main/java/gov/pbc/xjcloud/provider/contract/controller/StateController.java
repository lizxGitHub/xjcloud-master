package gov.pbc.xjcloud.provider.contract.controller;

import com.baomidou.mybatisplus.extension.api.R;
import gov.pbc.xjcloud.provider.contract.entity.State;
import gov.pbc.xjcloud.provider.contract.enumutils.EnumUtils;
import gov.pbc.xjcloud.provider.contract.enumutils.SHLeaderStateEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.SHNormalStateEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.StateEnum;
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
}
