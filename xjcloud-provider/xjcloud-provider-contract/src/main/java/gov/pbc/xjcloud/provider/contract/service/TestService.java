package gov.pbc.xjcloud.provider.contract.service;

import java.util.List;
import java.util.Map;

/**
 * @author com.mhout.lizx
 * @version 1.0.0
 */
public interface TestService {

    Map<String, Object> selectById(String id);

    List<Map<String, Object>> selectByIds(String[] ids);
}
