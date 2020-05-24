package gov.pbc.xjcloud.provider.contract.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
import gov.pbc.xjcloud.provider.contract.service.IBaseService;
import gov.pbc.xjcloud.provider.contract.utils.BeanValidators;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;

public class IBaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<IBaseMapper<T>, T> implements IBaseService<T> {

    @Resource
    private Validator validator;

    /**
     * 验证接口集申请参数
     *
     * @param t
     * @param res
     */
    public void validate(T t, R res) {
        try {
            BeanValidators.validateWithException(validator, t);
        } catch (ConstraintViolationException ex) {
            List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
            String msg = StringUtils.join(messageList, "；");
            res = R.failed(msg);
        }
    }

}
