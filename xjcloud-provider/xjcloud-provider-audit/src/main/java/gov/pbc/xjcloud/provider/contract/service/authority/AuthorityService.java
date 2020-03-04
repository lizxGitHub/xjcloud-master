package gov.pbc.xjcloud.provider.contract.service.authority;

import gov.pbc.xjcloud.provider.contract.mapper.authority.AuthorityMapper;

import java.util.List;

public interface AuthorityService extends AuthorityMapper {
    List<String> getMenuByRoleId(int[] roleId);
}
