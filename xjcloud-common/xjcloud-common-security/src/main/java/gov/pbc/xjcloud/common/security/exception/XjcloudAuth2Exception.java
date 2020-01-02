package gov.pbc.xjcloud.common.security.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import gov.pbc.xjcloud.common.security.component.XjcloudAuth2ExceptionSerializer;
import lombok.Getter;

@JsonSerialize(using = XjcloudAuth2ExceptionSerializer.class)
public class XjcloudAuth2Exception extends OAuth2Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9011851317521273997L;
	
	@Getter
	private String errorCode;

	public XjcloudAuth2Exception(String msg) {
		super(msg);
	}

	public XjcloudAuth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}
}
