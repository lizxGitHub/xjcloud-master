package gov.pbc.xjcloud.provider.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IBaseMapper<T> extends BaseMapper<T> {
}
