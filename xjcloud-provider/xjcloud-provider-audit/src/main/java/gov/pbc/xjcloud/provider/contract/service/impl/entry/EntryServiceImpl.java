package gov.pbc.xjcloud.provider.contract.service.impl.entry;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.mapper.entry.EntryMapper;
import gov.pbc.xjcloud.provider.contract.service.entry.EntryService;
import gov.pbc.xjcloud.provider.contract.service.impl.IBaseServiceImpl;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class EntryServiceImpl extends IBaseServiceImpl<EntryMapper, EntryInfo> implements EntryService {
    @Resource
    private EntryMapper entryMapper;
    /**
     * 自定义分页查询
     * @param page 分页对象
     * @param query 查询参数
     * @return
     */
    public Page<EntryInfoVO> selectEntryInfo(Page page, EntryInfoVO query) {
        List<EntryInfoVO> list = entryMapper.selectEntryInfo(page,query);
        page.setRecords(list);
        return page;
    }

    public List<EntryInfo> listAll(){
        return entryMapper.listAll();
    }
}
