package gov.pbc.xjcloud.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.pbc.xjcloud.common.security.component.XjcloudAuth2ExceptionSerializer;

@JsonSerialize(using = XjcloudAuth2ExceptionSerializer.class)
public class InvalidException extends XjcloudAuth2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 535381256280470603L;

	public InvalidException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_exception";
	}

	@Override
	public int getHttpErrorCode() {
		return 426;
	}

}
