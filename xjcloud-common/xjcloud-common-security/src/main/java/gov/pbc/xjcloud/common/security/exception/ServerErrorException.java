package gov.pbc.xjcloud.common.security.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.pbc.xjcloud.common.security.component.XjcloudAuth2ExceptionSerializer;

@JsonSerialize(using = XjcloudAuth2ExceptionSerializer.class)
public class ServerErrorException extends XjcloudAuth2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -474991049684969843L;

	public ServerErrorException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "server_error";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}

}
