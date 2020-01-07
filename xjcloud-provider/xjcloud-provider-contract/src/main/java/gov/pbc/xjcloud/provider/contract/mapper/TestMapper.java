package gov.pbc.xjcloud.provider.contract.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TestMapper {

    Map<String, Object> selectById(@Param("id") String id);

    List<Map<String, Object>> selectByIds(@Param("array") String[] ids);
}
