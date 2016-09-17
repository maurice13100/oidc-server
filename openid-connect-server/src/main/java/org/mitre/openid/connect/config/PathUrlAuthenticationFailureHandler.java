package org.mitre.openid.connect.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mitre.openid.connect.config.PathLoginAuthenticationEntryPoint.LOGIN_SMS;

public class PathUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	public PathUrlAuthenticationFailureHandler() {
	}

	public PathUrlAuthenticationFailureHandler(String defaultFailureUrl) {
		super(defaultFailureUrl);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		if (!getFailureUrlFromPath(request).isEmpty()) {
			setDefaultFailureUrl(getFailureUrlFromPath(request));
		}
		super.onAuthenticationFailure(request, response, exception);
	}

	private String getFailureUrlFromPath(HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		if (referer.equals(LOGIN_SMS)) {
			return LOGIN_SMS + "?error=failure";
		}
		return "";
	}
}
