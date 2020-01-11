package gov.pbc.xjcloud.provider.contract.controller;

import com.baomidou.mybatisplus.extension.api.R;
import gov.pbc.xjcloud.provider.contract.entity.State;
import gov.pbc.xjcloud.provider.contract.enumutils.EnumUtils;
import gov.pbc.xjcloud.provider.contract.enumutils.StateEnum;
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
    @RequestMapping("/list")
    public R<List<State>> listR() {
        Class<StateEnum> classz= StateEnum.class;
        List<State> list= EnumUtils.getEnumToList(classz);
        return R.ok(list);
    }
}
