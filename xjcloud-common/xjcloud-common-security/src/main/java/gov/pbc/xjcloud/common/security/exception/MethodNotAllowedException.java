package gov.pbc.xjcloud.common.security.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.pbc.xjcloud.common.security.component.XjcloudAuth2ExceptionSerializer;

@JsonSerialize(using = XjcloudAuth2ExceptionSerializer.class)
public class MethodNotAllowedException extends XjcloudAuth2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7526491067170928425L;

	public MethodNotAllowedException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "method_not_allowed";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.METHOD_NOT_ALLOWED.value();
	}

}
