package gov.pbc.xjcloud.common.security.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.pbc.xjcloud.common.security.component.XjcloudAuth2ExceptionSerializer;

@JsonSerialize(using = XjcloudAuth2ExceptionSerializer.class)
public class ForbiddenException extends XjcloudAuth2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8449712763907299746L;

	public ForbiddenException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "access_denied";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.FORBIDDEN.value();
	}

}

