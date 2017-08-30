package org.mitre.openid.connect.config;

import static org.mitre.openid.connect.web.AuthenticationTimeStamper.AUTH_TIMESTAMP;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mitre.openid.connect.filter.AuthorizationRequestFilter;
import org.mitre.openid.connect.service.impl.DefaultUserConnectionService;
import org.mitre.openid.connect.util.AcrEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.upcrob.springsecurity.otp.PreOtpAuthenticationToken;

public class OtpAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private String otpUrl;
	private DefaultUserConnectionService defaultUserConnectionService;

	public OtpAuthenticationHandler() {
	}

	public OtpAuthenticationHandler(String defaultTargetUrl, String otpUrl,
			DefaultUserConnectionService defaultUserConnectionService) {
		this.setDefaultTargetUrl(defaultTargetUrl);
		this.otpUrl = otpUrl;
		this.defaultUserConnectionService = defaultUserConnectionService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {

		Date authTimestamp = new Date();

		if (authentication instanceof OAuth2Authentication) {
			authTimestamp = defaultUserConnectionService.successfulConnection(httpServletRequest, authentication);
		}
		if (authentication instanceof PreOtpAuthenticationToken) {
			PreOtpAuthenticationToken auth = (PreOtpAuthenticationToken) authentication;
			String token = httpServletRequest.getParameter("otptoken");
			logger.info("token found is " + token);

		}
		if (defaultUserConnectionService.isLoggedBySMS(httpServletRequest)) {
			logger.info(
					"1 Successful Authentication of " + authentication.getName() + " at " + authTimestamp.toString());

			handleOtp(httpServletRequest, httpServletResponse, authentication);
		} else {

			HttpSession session = httpServletRequest.getSession();

			session.setAttribute(AUTH_TIMESTAMP, authTimestamp);

			if (session.getAttribute(AuthorizationRequestFilter.PROMPT_REQUESTED) != null) {
				session.setAttribute(AuthorizationRequestFilter.PROMPTED, Boolean.TRUE);
				session.removeAttribute(AuthorizationRequestFilter.PROMPT_REQUESTED);
			}

			logger.info("Successful Authentication of " + authentication.getName() + " at " + authTimestamp.toString());

			super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
		}

	}

	private void handleOtp(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String targetUrl = this.determineOtpTargetUrl(request, response);
		if (response.isCommitted()) {
			this.logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
		} else if (!targetUrl.isEmpty()) {
			this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
		}
	}

	private String determineOtpTargetUrl(HttpServletRequest request, HttpServletResponse response) {
		String acrValue = "";
		if (request.getSession().getAttribute("acr") != null) {
			acrValue = (String) request.getSession().getAttribute("acr");
		}

		if (acrValue.contains(AcrEnum.SMS.getValue())) {
			return otpUrl;
		}
		return "";
	}
}
