package gov.pbc.xjcloud.provider.contract.service.impl.entry;

import com.baomidou.mybatisplus.core.metadata.IPage;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryFlow;
import gov.pbc.xjcloud.provider.contract.mapper.entry.EntryFlowMapper;
import gov.pbc.xjcloud.provider.contract.service.entry.EntryFlowService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryFlowVO;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class EntryFlowServiceIml extends IBaseServiceImpl<EntryFlowMapper, EntryFlow> implements EntryFlowService {

    @Resource
    EntryFlowMapper entryFlowMapper;

    /**
     * 词条流程信息获取
     * @param page
     * @param query
     * @return
     */
    public IPage<EntryFlowVO> selectEntryInfo(IPage<EntryFlowVO> page, EntryFlow query) {
        List<EntryFlowVO> list = entryFlowMapper.selectEntryInfo(page,query);
        page.setRecords(list);
        return page;
    }
}