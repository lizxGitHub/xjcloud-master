package gov.pbc.xjcloud.provider.contract.mapper.entry;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EntryMapper extends BaseMapper<EntryInfo> {

}
