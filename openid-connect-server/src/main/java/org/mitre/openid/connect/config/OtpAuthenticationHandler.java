package org.mitre.openid.connect.config;

import org.mitre.openid.connect.filter.AuthorizationRequestFilter;
import org.mitre.openid.connect.util.AcrEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

import static org.mitre.openid.connect.web.AuthenticationTimeStamper.AUTH_TIMESTAMP;

public class OtpAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private String otpUrl;

	public OtpAuthenticationHandler() {
	}

	public OtpAuthenticationHandler(String defaultTargetUrl, String otpUrl) {
		this.setDefaultTargetUrl(defaultTargetUrl);
		this.otpUrl = otpUrl;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
		if (httpServletRequest.getSession().getAttribute("acr") != null && ((String) httpServletRequest.getSession().getAttribute("acr")).contains(AcrEnum.SMS.getValue())) {
			handleOtp(httpServletRequest, httpServletResponse, authentication);
		} else {
			Date authTimestamp = new Date();

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

	private void handleOtp(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
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