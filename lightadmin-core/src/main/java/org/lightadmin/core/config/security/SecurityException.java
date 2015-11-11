package org.lightadmin.core.config.security;


import org.lightadmin.core.FrameworkException;

public class SecurityException extends FrameworkException {

	private static final long serialVersionUID = 116217185780715338L;
	private static final String defaultCode = "Sec000";
	public static final String authenticationErrorCode = "Sec001";
	public static final String authorizationErrorCode = "Sec002";

	public SecurityException() {
		super(defaultCode);
	}

	public SecurityException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}

	public SecurityException(String message, String errorCode) {
		super(message, errorCode);
	}

	public SecurityException(String message, Throwable cause) {
		super(message,defaultCode, cause);
	}

	public SecurityException(String message) {
		super(message,defaultCode);
	}

	public SecurityException(Throwable cause, String errorCode) {
		super(cause, errorCode);
	}

	public SecurityException(Throwable cause) {
		super(cause,defaultCode);
	}

	
}
