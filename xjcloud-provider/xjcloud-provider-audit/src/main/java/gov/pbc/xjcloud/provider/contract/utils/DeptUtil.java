package gov.pbc.xjcloud.provider.contract.utils;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import gov.pbc.xjcloud.common.core.util.R;
import gov.pbc.xjcloud.provider.contract.vo.DeptVO;
import gov.pbc.xjcloud.provider.usercenter.api.feign.RemoteDeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DeptUtil {

    @Autowired
    RemoteDeptService remoteDeptService;

    public List<DeptVO> findChildBank(Integer deptId, String lastFilter) {
        if (null == deptId) {
            return new ArrayList<>();
        }
        R r = remoteDeptService.children(deptId, false);
        ArrayList<Map<String, Object>> data = (ArrayList) r.getData();
        List<DeptVO> collect = data.stream().filter(Objects::nonNull).map(e -> {
            DeptVO deptVO = JSONUtil.toBean(JSONUtil.toJsonStr(e), DeptVO.class);
            return deptVO;
        }).filter(e -> e.getName().lastIndexOf(lastFilter) > -1).collect(Collectors.toList());
        return collect;
    }

}
