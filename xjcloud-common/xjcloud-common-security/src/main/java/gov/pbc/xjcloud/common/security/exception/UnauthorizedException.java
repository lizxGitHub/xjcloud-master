package gov.pbc.xjcloud.common.security.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.pbc.xjcloud.common.security.component.XjcloudAuth2ExceptionSerializer;

@JsonSerialize(using = XjcloudAuth2ExceptionSerializer.class)
public class UnauthorizedException extends XjcloudAuth2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6615968461152238442L;

	public UnauthorizedException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "unauthorized";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.UNAUTHORIZED.value();
	}

}
