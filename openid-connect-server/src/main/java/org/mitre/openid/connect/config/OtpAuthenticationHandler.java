package org.mitre.openid.connect.config;

import static org.mitre.openid.connect.web.AuthenticationTimeStamper.AUTH_TIMESTAMP;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.openid.connect.filter.AuthorizationRequestFilter;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserConnection;
import org.mitre.openid.connect.service.UserConnectionService;
import org.mitre.openid.connect.util.AcrEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class OtpAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private String otpUrl;
	private UserConnectionService userConnectionService;
	private ClientDetailsEntityService clientService;

	public OtpAuthenticationHandler() {
	}

	public OtpAuthenticationHandler(String defaultTargetUrl, String otpUrl) {
		this.setDefaultTargetUrl(defaultTargetUrl);
		this.otpUrl = otpUrl;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {

		Date authTimestamp = new Date();
		UserConnection userConnection = new UserConnection();
		userConnection.setDate(authTimestamp);
		userConnection.setUserId(getUserId(httpServletRequest));
		userConnection.setClientId(getClientId(authentication));
		userConnectionService.save(userConnection);

		if (httpServletRequest.getSession().getAttribute("acr") != null
				&& ((String) httpServletRequest.getSession().getAttribute("acr")).contains(AcrEnum.SMS.getValue())) {
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

	private String getUserId(HttpServletRequest httpServletRequest) {

		Object user = httpServletRequest.getAttribute("userInfo");
		if (user != null && user instanceof DefaultUserInfo) {
			return Long.toString(((DefaultUserInfo) user).getId());
		}

		// get user with auth.getName
		return "";
	}

	private String getClientId(Authentication authentication) {

		if (authentication instanceof OAuth2Authentication) {
			OAuth2Authentication o2a = (OAuth2Authentication) authentication;
			String authClientId = o2a.getOAuth2Request().getClientId();
			return clientService.loadClientByClientId(authClientId).getClientId();
		}

		return clientService.loadClientByClientId(authentication.getName()).getClientId();
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
