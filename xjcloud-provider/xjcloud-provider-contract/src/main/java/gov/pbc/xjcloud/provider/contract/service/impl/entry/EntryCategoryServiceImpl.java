package gov.pbc.xjcloud.provider.contract.service.impl.entry;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryCategory;
import gov.pbc.xjcloud.provider.contract.mapper.entry.EntryCategoryMapper;
import gov.pbc.xjcloud.provider.contract.service.entry.EntryCategoryService;
import org.springframework.stereotype.Service;

@Service
public class EntryCategoryServiceImpl extends ServiceImpl<EntryCategoryMapper, EntryCategory> implements EntryCategoryService {
}
