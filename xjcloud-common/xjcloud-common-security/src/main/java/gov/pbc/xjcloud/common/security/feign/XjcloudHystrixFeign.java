package gov.pbc.xjcloud.common.security.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.netflix.hystrix.HystrixCommand;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.ResponseMapper;
import feign.Retryer;
import feign.Target;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixDelegatingContract;
import feign.hystrix.SetterFactory;

public class XjcloudHystrixFeign {

	public static XjcloudHystrixFeign.Builder builder(FeignContext feignContext) {
		return new XjcloudHystrixFeign.Builder(feignContext);
	}

	public static final class Builder extends Feign.Builder {
		private Contract contract = new Contract.Default();
		private SetterFactory setterFactory = new SetterFactory.Default();
		private final FeignContext feignContext;

		public Builder(FeignContext feignContext) {
			this.feignContext = feignContext;
		}

		/**
		 * Allows you to override hystrix properties such as thread pools and command keys.
		 */
		public XjcloudHystrixFeign.Builder setterFactory(SetterFactory setterFactory) {
			this.setterFactory = setterFactory;
			return this;
		}

		@Override
		public <T> T target(Target<T> target) {
			Class<T> targetType = target.type();
			FeignClient feignClient = AnnotatedElementUtils.getMergedAnnotation(targetType, FeignClient.class);
			String factoryName = feignClient.name();
			SetterFactory setterFactoryBean = this.getOptional(factoryName, feignContext, SetterFactory.class);
			if (setterFactoryBean != null) {
				this.setterFactory(setterFactoryBean);
			}
			Class<?> fallback = feignClient.fallback();
			if (fallback != void.class) {
				return targetWithFallback(factoryName, feignContext, target, this, fallback);
			}
			Class<?> fallbackFactory = feignClient.fallbackFactory();
			if (fallbackFactory != void.class) {
				return targetWithFallbackFactory(factoryName, feignContext, target, this, fallbackFactory);
			}
			return build().newInstance(target);
		}

		@SuppressWarnings("unchecked")
		private <T> T targetWithFallbackFactory(String feignClientName, FeignContext context,
												Target<T> target,
												XjcloudHystrixFeign.Builder builder,
												Class<?> fallbackFactoryClass) {
			FallbackFactory<? extends T> fallbackFactory = (FallbackFactory<? extends T>)
					getFromContext("fallbackFactory", feignClientName, context, fallbackFactoryClass, FallbackFactory.class);
		/* We take a sample fallback from the fallback factory to check if it returns a fallback
		that is compatible with the annotated feign interface. */
			Object exampleFallback = fallbackFactory.create(new RuntimeException());
			Assert.notNull(exampleFallback,
					String.format(
							"Incompatible fallbackFactory instance for feign client %s. Factory may not produce null!",
							feignClientName));
			if (!target.type().isAssignableFrom(exampleFallback.getClass())) {
				throw new IllegalStateException(
						String.format(
								"Incompatible fallbackFactory instance for feign client %s. Factory produces instances of '%s', but should produce instances of '%s'",
								feignClientName, exampleFallback.getClass(), target.type()));
			}
			return builder.target(target, fallbackFactory);
		}


		private <T> T targetWithFallback(String feignClientName, FeignContext context,
										 Target<T> target,
										 XjcloudHystrixFeign.Builder builder, Class<?> fallback) {
			T fallbackInstance = getFromContext("fallback", feignClientName, context, fallback, target.type());
			return builder.target(target, fallbackInstance);
		}

		@SuppressWarnings("unchecked")
		private <T> T getFromContext(String fallbackMechanism, String feignClientName, FeignContext context,
									 Class<?> beanType, Class<T> targetType) {
			Object fallbackInstance = context.getInstance(feignClientName, beanType);
			if (fallbackInstance == null) {
				throw new IllegalStateException(String.format(
						"No %s instance of type %s found for feign client %s",
						fallbackMechanism, beanType, feignClientName));
			}

			if (!targetType.isAssignableFrom(beanType)) {
				throw new IllegalStateException(
						String.format(
								"Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s",
								fallbackMechanism, beanType, targetType, feignClientName));
			}
			return (T) fallbackInstance;
		}

		@Nullable
		private <T> T getOptional(String feignClientName, FeignContext context, Class<T> beanType) {
			return context.getInstance(feignClientName, beanType);
		}

		/**
		 * @see #target(Class, String, Object)
		 */
		public <T> T target(Target<T> target, @Nullable T fallback) {
			return build(fallback != null ? new FallbackFactory.Default<T>(fallback) : null)
					.newInstance(target);
		}

		/**
		 * @see #target(Class, String, FallbackFactory)
		 */
		public <T> T target(Target<T> target, FallbackFactory<? extends T> fallbackFactory) {
			return build(fallbackFactory).newInstance(target);
		}

		/**
		 * Like {@link Feign#newInstance(Target)}, except with {@link HystrixCommand
		 * fallback} support.
		 *
		 * <p>Fallbacks are known values, which you return when there's an error invoking an http
		 * method. For example, you can return a cached result as opposed to raising an error to the
		 * caller. To use this feature, pass a safe implementation of your target interface as the last
		 * parameter.
		 * <p>
		 * Here's an example:
		 * <pre>
		 * {@code
		 *
		 * // When dealing with fallbacks, it is less tedious to keep interfaces small.
		 * interface GitHub {
		 *   @RequestLine("GET /repos/{owner}/{repo}/contributors")
		 *   List<String> contributors(@Param("owner") String owner, @Param("repo") String repo);
		 * }
		 *
		 * // This instance will be invoked if there are errors of any kind.
		 * GitHub fallback = (owner, repo) -> {
		 *   if (owner.equals("Netflix") && repo.equals("feign")) {
		 *     return Arrays.asList("stuarthendren"); // inspired this approach!
		 *   } else {
		 *     return Collections.emptyList();
		 *   }
		 * };
		 *
		 * GitHub github = HystrixFeign.builder()
		 *                             ...
		 *                             .target(GitHub.class, "https://api.github.com", fallback);
		 * }</pre>
		 *
		 * @see #target(Target, Object)
		 */
		public <T> T target(Class<T> apiType, String url, T fallback) {
			return target(new Target.HardCodedTarget<T>(apiType, url), fallback);
		}

		/**
		 * Same as {@link #target(Class, String, T)}, except you can inspect a source exception before
		 * creating a fallback object.
		 */
		public <T> T target(Class<T> apiType, String url, FallbackFactory<? extends T> fallbackFactory) {
			return target(new Target.HardCodedTarget<T>(apiType, url), fallbackFactory);
		}

		@Override
		public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
			throw new UnsupportedOperationException();
		}

		@Override
		public XjcloudHystrixFeign.Builder contract(Contract contract) {
			this.contract = contract;
			return this;
		}

		@Override
		public Feign build() {
			return build(null);
		}

		/**
		 * Configures components needed for hystrix integration.
		 */
		Feign build(@Nullable final FallbackFactory<?> nullableFallbackFactory) {
			super.invocationHandlerFactory((target, dispatch) ->
					new XjcloudHystrixInvocationHandler(target, dispatch, setterFactory, nullableFallbackFactory));
			super.contract(new HystrixDelegatingContract(contract));
			return super.build();
		}

		@Override
		public XjcloudHystrixFeign.Builder logLevel(Logger.Level logLevel) {
			return (XjcloudHystrixFeign.Builder) super.logLevel(logLevel);
		}

		@Override
		public XjcloudHystrixFeign.Builder client(Client client) {
			return (XjcloudHystrixFeign.Builder) super.client(client);
		}

		@Override
		public XjcloudHystrixFeign.Builder retryer(Retryer retryer) {
			return (XjcloudHystrixFeign.Builder) super.retryer(retryer);
		}

		@Override
		public XjcloudHystrixFeign.Builder logger(Logger logger) {
			return (XjcloudHystrixFeign.Builder) super.logger(logger);
		}

		@Override
		public XjcloudHystrixFeign.Builder encoder(Encoder encoder) {
			return (XjcloudHystrixFeign.Builder) super.encoder(encoder);
		}

		@Override
		public XjcloudHystrixFeign.Builder decoder(Decoder decoder) {
			return (XjcloudHystrixFeign.Builder) super.decoder(decoder);
		}

		@Override
		public XjcloudHystrixFeign.Builder mapAndDecode(ResponseMapper mapper, Decoder decoder) {
			return (XjcloudHystrixFeign.Builder) super.mapAndDecode(mapper, decoder);
		}

		@Override
		public XjcloudHystrixFeign.Builder decode404() {
			return (XjcloudHystrixFeign.Builder) super.decode404();
		}

		@Override
		public XjcloudHystrixFeign.Builder errorDecoder(ErrorDecoder errorDecoder) {
			return (XjcloudHystrixFeign.Builder) super.errorDecoder(errorDecoder);
		}

		@Override
		public XjcloudHystrixFeign.Builder options(Request.Options options) {
			return (XjcloudHystrixFeign.Builder) super.options(options);
		}

		@Override
		public XjcloudHystrixFeign.Builder requestInterceptor(RequestInterceptor requestInterceptor) {
			return (XjcloudHystrixFeign.Builder) super.requestInterceptor(requestInterceptor);
		}

		@Override
		public XjcloudHystrixFeign.Builder requestInterceptors(Iterable<RequestInterceptor> requestInterceptors) {
			return (XjcloudHystrixFeign.Builder) super.requestInterceptors(requestInterceptors);
		}
	}

}
