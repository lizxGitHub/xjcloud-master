package gov.pbc.xjcloud.provider.contract.service.impl;


import gov.pbc.xjcloud.provider.contract.mapper.TestMapper;
import gov.pbc.xjcloud.provider.contract.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author com.mhout.lizx
 */
@Service
public class TestServiceImpl implements TestService {

    @Resource
    TestMapper testMapper;

    @Override
    public Map<String, Object> selectById(String id) {
        return testMapper.selectById(id);
    }

    @Override
    public List<Map<String, Object>> selectByIds(String[] ids) {
        return testMapper.selectByIds(ids);
    }
}
