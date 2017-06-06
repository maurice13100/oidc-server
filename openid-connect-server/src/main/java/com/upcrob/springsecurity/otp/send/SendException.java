package com.upcrob.springsecurity.otp.send;

/**
 * Describes an Exception that occurs while attempting to send a token.
 */
public class SendException extends RuntimeException {

	/**
	 * UID.
	 */
	private static final long serialVersionUID = 1312431939606183146L;

	public SendException(String msg) {
		super(msg);
	}

	public SendException(String msg, Throwable e) {
		super(msg, e);
	}
}
