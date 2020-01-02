package gov.pbc.xjcloud.common.security.feign;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;

import gov.pbc.xjcloud.common.core.constant.CommonConstants;
import gov.pbc.xjcloud.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class XjcloudFeignFallbackMethod implements MethodInterceptor {
	private Class<?> type;
	private Throwable cause;

	@Nullable
	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
		log.error("Fallback class:[{}] method:[{}] message:[{}]",
				type.getName(), method.getName(), cause.getMessage());

		if (R.class == method.getReturnType()) {
			final R result = cause instanceof XjcloudFeignException ?
					((XjcloudFeignException) cause).getResult() : R.builder()
					.code(CommonConstants.FAIL)
					.msg(cause.getMessage()).build();
			return result;
		}
		return null;
	}
}
