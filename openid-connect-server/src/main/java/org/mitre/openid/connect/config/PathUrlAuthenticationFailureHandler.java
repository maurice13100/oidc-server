package org.mitre.openid.connect.config;

import static org.mitre.openid.connect.config.PathLoginAuthenticationEntryPoint.LOGIN_SMS;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.openid.connect.service.impl.DefaultUserConnectionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class PathUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private DefaultUserConnectionService defaultUserConnectionService;

	public PathUrlAuthenticationFailureHandler(DefaultUserConnectionService defaultUserConnectionService) {
		this.defaultUserConnectionService = defaultUserConnectionService;
	}

	public PathUrlAuthenticationFailureHandler(String defaultFailureUrl,
			DefaultUserConnectionService defaultUserConnectionService) {
		super(defaultFailureUrl);
		this.defaultUserConnectionService = defaultUserConnectionService;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth instanceof OAuth2Authentication) {
			defaultUserConnectionService.failureConnection(request, auth);
		}

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
