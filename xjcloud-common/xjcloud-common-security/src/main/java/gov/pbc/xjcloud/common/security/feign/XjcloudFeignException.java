package gov.pbc.xjcloud.common.security.feign;

import gov.pbc.xjcloud.common.core.constant.CommonConstants;
import gov.pbc.xjcloud.common.core.util.R;
import lombok.Getter;

public class XjcloudFeignException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8678178713621638197L;
	
	@Getter
	private final R result;

	public XjcloudFeignException(R result) {
		super(result.getMsg());
		this.result = result;
	}

	public XjcloudFeignException(String message) {
		super(message);
		this.result = R.builder().code(CommonConstants.FAIL).msg(message).build();
	}

	/**
	 * 提高性能
	 *
	 * @return {Throwable}
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
