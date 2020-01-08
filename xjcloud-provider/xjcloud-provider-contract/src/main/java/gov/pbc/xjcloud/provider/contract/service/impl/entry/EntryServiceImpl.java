package gov.pbc.xjcloud.provider.contract.service.impl.entry;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.mapper.entry.EntryMapper;
import gov.pbc.xjcloud.provider.contract.service.entry.EntryService;
import org.springframework.stereotype.Service;

@Service
public class EntryServiceImpl extends ServiceImpl<EntryMapper, EntryInfo> implements EntryService {

}
