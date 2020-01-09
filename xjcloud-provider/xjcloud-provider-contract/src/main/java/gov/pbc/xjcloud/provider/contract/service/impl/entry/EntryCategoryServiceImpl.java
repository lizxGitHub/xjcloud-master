package gov.pbc.xjcloud.provider.contract.service.impl.entry;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryCategory;
import gov.pbc.xjcloud.provider.contract.mapper.entry.EntryCategoryMapper;
import gov.pbc.xjcloud.provider.contract.service.entry.EntryCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {Exception.class})
public class EntryCategoryServiceImpl extends ServiceImpl<EntryCategoryMapper, EntryCategory> implements EntryCategoryService {
}
