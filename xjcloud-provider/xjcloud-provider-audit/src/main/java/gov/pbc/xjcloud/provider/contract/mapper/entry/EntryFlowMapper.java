package gov.pbc.xjcloud.provider.contract.mapper.entry;

import com.baomidou.mybatisplus.core.metadata.IPage;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryFlow;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryFlowVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EntryFlowMapper extends IBaseMapper<EntryFlow> {

    /**
     * 词条流程信息获取
     * @param page
     * @param query
     * @return
     */
    List<EntryFlowVO> selectEntryInfo(@Param("page") IPage<EntryFlowVO> page, @Param("query") EntryFlow query);
}
