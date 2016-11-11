package org.mitre.openid.connect.config;

import org.mitre.openid.connect.request.ConnectRequestParameters;
import org.mitre.openid.connect.util.AcrEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathLoginAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

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
		if (attribute != null) {
			if (attribute.equals(AcrEnum.SMS.getValue())) {
				return LOGIN_SMS;
			}
		}

		return getLoginFormUrl();

	}
}
