package org.mitre.openid.connect.filter;

import static org.mitre.openid.connect.request.ConnectRequestParameters.ACR;
import static org.mitre.openid.connect.request.ConnectRequestParameters.ACR_VALUES;
import static org.mitre.openid.connect.request.ConnectRequestParameters.AMR;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.openid.connect.model.PhoneNumberAuthenticationToken;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.service.UserInfoService;
import org.mitre.openid.connect.service.impl.RegistrationBySmsService;
import org.mitre.openid.connect.util.AcrEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.Assert;

public class SmsUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private RegistrationBySmsService registrationBySmsService;

	private static final Logger logger = LoggerFactory.getLogger(SmsUsernamePasswordAuthenticationFilter.class);

	public static final String SPRING_SECURITY_FORM_PHONE_NUMBER_KEY = "j_phone_number";
	public static final String AMR_SMS = AcrEnum.SMS.getValue();
	public static final String AMR_PWD = AcrEnum.PWD.getValue();

	private String phoneNumberParameter = "j_phone_number";

	protected SmsUsernamePasswordAuthenticationFilter() {
		super("/j_spring_security_check");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String value = request.getParameter("connectionType");
		logger.info(value);
		if (value != null && value.contains("SMS")) {
			request.getSession().setAttribute(ACR_VALUES, value);		
			super.doFilter(req, res, chain);
			return;
		}

		String acr_value = (String) request.getSession().getAttribute(ACR_VALUES);
		if (acr_value == null) {
			setValueInSession(request, AMR, AMR_PWD);
			setValueInSession(request, ACR, AMR_PWD);
			chain.doFilter(request, response);
		} else {
			int valuesNumber = acr_value.split(" ").length;
			boolean hasSms = false;
			if (acr_value.contains(AcrEnum.PWD.getValue())) {
				setValueInSession(request, AMR, AMR_PWD);
				setValueInSession(request, ACR, AMR_PWD);
			}

			if (acr_value.contains(AcrEnum.SMS.getValue())) {
				setValueInSession(request, AMR, AMR_SMS);
				setValueInSession(request, ACR, AMR_SMS);
				hasSms = true;
			}

			if (valuesNumber == 1 && hasSms) {
				super.doFilter(req, res, chain);
			} else { // valuesNumber > 1
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws AuthenticationException {
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

			UserInfo userInfo = userInfoService.getByPhoneNumber(phoneNumber);

			// User don't exist
			if (userInfo == null) {
				userInfo = registrationBySmsService.registerNewUser(phoneNumber);
			}

			return this.getAuthenticationManager().authenticate(
					new PhoneNumberAuthenticationToken(userInfo.getPreferredUsername(), null, phoneNumber));
		}
	}

	private void setValueInSession(HttpServletRequest request, String key, String value) {
		request.getSession().setAttribute(key, value);
	}

	private String obtainPhoneNumber(HttpServletRequest request) {
		return request.getParameter(phoneNumberParameter);
	}
}
