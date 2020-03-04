package gov.pbc.xjcloud.provider.contract.mapper.authority;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthorityMapper {

    List<String> getMenuByRoleId(@Param("array") int[] roleId);

}
