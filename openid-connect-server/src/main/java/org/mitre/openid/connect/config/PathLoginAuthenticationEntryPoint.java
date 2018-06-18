package org.mitre.openid.connect.config;

import org.mitre.openid.connect.request.ConnectRequestParameters;
import org.mitre.openid.connect.util.AcrEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathLoginAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(PathLoginAuthenticationEntryPoint.class);

	public static final String LOGIN_SMS = "/login_sms";

	public PathLoginAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
	protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
		return getLoginFromSessionAcrValues(request);
	}

	private String getLoginFromSessionAcrValues(HttpServletRequest request) {
		String attribute = (String) request.getSession().getAttribute(ConnectRequestParameters.ACR_VALUES);
		logger.info("acr = " + attribute);
		if (attribute != null) {
			if (attribute.equals(AcrEnum.SMS.getValue())) {
				return LOGIN_SMS;
			}
		}

		return getLoginFormUrl();

	}
}
