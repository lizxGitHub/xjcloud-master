package gov.pbc.xjcloud.provider.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
import gov.pbc.xjcloud.provider.contract.service.IBaseService;

public class IBaseServiceImpl<T> extends ServiceImpl<IBaseMapper<T>,T>  implements IBaseService<T> {
}
