package org.lightadmin.core;

public class FrameworkException extends RuntimeException {

	private static final long serialVersionUID = 5258897520512051640L;

	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public FrameworkException() {
		super();
	}

	public FrameworkException(String message, Throwable cause) {
		super(message, cause);
	}

	public FrameworkException(String message) {
		super(message);
	}

	public FrameworkException(Throwable cause) {
		super(cause);
	}
	
	public FrameworkException(String message, String errorCode, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public FrameworkException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public FrameworkException(Throwable cause, String errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

}
