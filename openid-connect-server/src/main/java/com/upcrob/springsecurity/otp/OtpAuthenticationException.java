package com.upcrob.springsecurity.otp;

import org.springframework.security.core.AuthenticationException;

public class OtpAuthenticationException extends AuthenticationException {

	/**
	 * UID.
	 */
	private static final long serialVersionUID = -5280781192892956710L;

	public OtpAuthenticationException(String msg) {
		super(msg);
	}

	public OtpAuthenticationException(String msg, Throwable e) {
		super(msg, e);
	}
}
