package org.mitre.openid.connect.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;

public class PhoneNumberAuthenticationToken extends AbstractAuthenticationToken {
	private String phoneNumber;
	private String principal;
	private final String credentials;


	public PhoneNumberAuthenticationToken(String principal, String credentials, String phoneNumber, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		this.phoneNumber = phoneNumber;

		super.setAuthenticated(true);
	}

	public PhoneNumberAuthenticationToken(String principal, String credentials, String phoneNumber) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		this.phoneNumber = phoneNumber;
		setAuthenticated(false);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if(isAuthenticated) {
			throw new IllegalArgumentException("Once created you cannot set this token to authenticated. Create a new instance using the constructor which takes a GrantedAuthority list will mark this as authenticated.");
		} else {
			super.setAuthenticated(false);
		}
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
}
