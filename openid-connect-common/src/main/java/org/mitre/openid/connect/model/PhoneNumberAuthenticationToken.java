package org.mitre.openid.connect.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;

public class PhoneNumberAuthenticationToken extends AbstractAuthenticationToken {
	private String phoneNumber;

	public PhoneNumberAuthenticationToken(String phoneNumber, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.phoneNumber = phoneNumber;
		super.setAuthenticated(true);
	}

	public PhoneNumberAuthenticationToken(String phoneNumber) {
		super(null);
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
		return null;
	}

	@Override
	public Object getPrincipal() {
		return phoneNumber;
	}
}
