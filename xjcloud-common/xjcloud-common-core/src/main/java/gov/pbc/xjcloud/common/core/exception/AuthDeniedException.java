package gov.pbc.xjcloud.common.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthDeniedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AuthDeniedException(String message) {
		super(message);
	}

	public AuthDeniedException(Throwable cause) {
		super(cause);
	}

	public AuthDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
