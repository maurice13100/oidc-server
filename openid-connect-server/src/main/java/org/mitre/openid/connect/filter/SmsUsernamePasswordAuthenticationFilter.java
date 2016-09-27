package org.mitre.openid.connect.filter;

import org.mitre.openid.connect.model.PhoneNumberAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mitre.openid.connect.request.ConnectRequestParameters.*;

public class SmsUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LoggerFactory.getLogger(SmsUsernamePasswordAuthenticationFilter.class);

	public static final String SPRING_SECURITY_FORM_PHONE_NUMBER_KEY = "j_phone_number";
	public static final String AMR_SMS = "sms";
	public static final String AMR_PWD = "pwd";

	private String phoneNumberParameter = "j_phone_number";

	protected SmsUsernamePasswordAuthenticationFilter() {
		super("/j_spring_security_check");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String acr_value = (String) request.getSession().getAttribute(ACR_VALUES);
		if (acr_value == null || acr_value.equals(AMR_PWD)) {
			setValueInSession(request, AMR, AMR_PWD);
			setValueInSession(request, ACR, AMR_PWD);
			chain.doFilter(request, response);
		} else if (acr_value.equals(AMR_SMS)) {
			super.doFilter(req, res, chain);
		}
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
		logger.debug("Attempting authentication");
		Authentication authentication = null;
		String acr_value = (String) httpServletRequest.getSession().getAttribute(ACR_VALUES);

		if (acr_value != null) {
			authentication = this.smsAuthenticationAttempt(httpServletRequest, httpServletResponse);
			setValueInSession(httpServletRequest, AMR, AMR_SMS);
			setValueInSession(httpServletRequest, ACR, AMR_SMS);

		} else {
			throw new AuthenticationServiceException("acr_values should have been set but they weren't");
		}

		return authentication;
	}

	protected void setDetails(HttpServletRequest request, PhoneNumberAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	public void setPhoneNumberParameter(String phoneNumberParameter) {
		Assert.hasText(phoneNumberParameter, "Phone number parameter must not be empty or null");
		this.phoneNumberParameter = phoneNumberParameter;
	}

	private Authentication smsAuthenticationAttempt(HttpServletRequest request, HttpServletResponse response) {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		} else {
			String phoneNumber = this.obtainPhoneNumber(request);
			if (phoneNumber == null) {
				phoneNumber = "";
			}

			phoneNumber = phoneNumber.trim();
			PhoneNumberAuthenticationToken authRequest = new PhoneNumberAuthenticationToken(phoneNumber);
			return this.getAuthenticationManager().authenticate(authRequest);
		}
	}

	private void setValueInSession(HttpServletRequest request, String key, String value) {
		request.getSession().setAttribute(key, value);
	}

	private String obtainPhoneNumber(HttpServletRequest request) {
		return request.getParameter(phoneNumberParameter);
	}
}
