package gov.pbc.xjcloud.provider.contract.service.impl.authority;

import gov.pbc.xjcloud.provider.contract.mapper.authority.AuthorityMapper;
import gov.pbc.xjcloud.provider.contract.service.authority.AuthorityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class AuthorityServiceImpl implements AuthorityService {

    @Resource
    private AuthorityMapper authorityMapper;

    @Override
    public List<String> getMenuByRoleId(int[] roleId) {
        return authorityMapper.getMenuByRoleId(roleId);
    }
}
