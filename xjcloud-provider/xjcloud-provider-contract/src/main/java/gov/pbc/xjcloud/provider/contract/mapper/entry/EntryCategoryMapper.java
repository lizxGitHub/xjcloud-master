package gov.pbc.xjcloud.provider.contract.mapper.entry;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryCategory;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EntryCategoryMapper extends BaseMapper<EntryCategory> {


}
