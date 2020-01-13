package gov.pbc.xjcloud.provider.contract.service.impl.entry;

import gov.pbc.xjcloud.provider.contract.entity.entry.EntryFlow;
import gov.pbc.xjcloud.provider.contract.mapper.entry.EntryFlowMapper;
import gov.pbc.xjcloud.provider.contract.service.entry.EntryFlowService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class EntryFlowServiceIml extends IBaseServiceImpl<EntryFlowMapper, EntryFlow> implements EntryFlowService {

    @Resource
    EntryFlowMapper entryFlowMapper;

}