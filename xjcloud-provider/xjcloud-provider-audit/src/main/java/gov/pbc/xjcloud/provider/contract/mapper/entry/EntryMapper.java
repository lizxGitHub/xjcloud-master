package gov.pbc.xjcloud.provider.contract.mapper.entry;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface EntryMapper extends BaseMapper<EntryInfo> {
    /**
     * 查询分页对象
     * @param page
     * @param query
     * @return
     */
    List<EntryInfoVO> selectEntryInfo(@Param("page") Page page, @Param("query") EntryInfoVO query);

}
