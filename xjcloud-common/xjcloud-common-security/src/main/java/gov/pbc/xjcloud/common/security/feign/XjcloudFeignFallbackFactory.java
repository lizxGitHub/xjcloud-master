package gov.pbc.xjcloud.common.security.feign;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cglib.proxy.Enhancer;

import feign.hystrix.FallbackFactory;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class XjcloudFeignFallbackFactory<T> implements FallbackFactory<T> {
	public static final XjcloudFeignFallbackFactory INSTANCE = new XjcloudFeignFallbackFactory();
	private static final ConcurrentMap<Class<?>, Object> FALLBACK_MAP = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public T create(final Class<?> type, final Throwable cause) {
		return (T) FALLBACK_MAP.computeIfAbsent(type, key -> {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(key);
			enhancer.setCallback(new XjcloudFeignFallbackMethod(type, cause));
			return enhancer.create();
		});
	}

	@Override
	public T create(Throwable cause) {
		return null;
	}
}
