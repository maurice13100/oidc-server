package com.upcrob.springsecurity.otp;

import com.upcrob.springsecurity.otp.tokenstore.Tokenstore;
import org.mitre.openid.connect.filter.AuthorizationRequestFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

import static org.mitre.openid.connect.web.AuthenticationTimeStamper.AUTH_TIMESTAMP;

/**
 * This class validates that an OTP token entered by a user is valid.  If it is,
 * the user will be redirected to the success URL or the stored redirect URL.  Otherwise, they will be redirected
 * to the failure URL.
 */
public class OtpValidationFilter extends OncePerRequestFilter {

	public static final String DEFAULT_OTP_PARAMETER_NAME = "otptoken";
	public String otpParameterName = DEFAULT_OTP_PARAMETER_NAME;
	private Tokenstore tokenstore;
	private String endpoint;
	private String successUrl;
	private String failureUrl;
	private RequestCache requestCache = new HttpSessionRequestCache();

	public OtpValidationFilter(Tokenstore tokenstore, String endpoint, String successUrl, String failureUrl) {
		this.tokenstore = tokenstore;
		this.endpoint = endpoint;
		this.successUrl = successUrl;
		this.failureUrl = failureUrl;
	}
	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		// Make sure validation endpoint was requested before continuing
		String path = request.getRequestURI().substring(request.getContextPath().length());
		if (!path.equals(endpoint)) {
			chain.doFilter(request, response);
			return;
		}
		
		// Get token from request
		String token = request.getParameter(otpParameterName);
		if (token == null) {
			response.sendRedirect(failureUrl);
			return;
		}
		
		// Get username from security context
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			response.sendRedirect(failureUrl);
			return;
		}
		if (!(auth instanceof PreOtpAuthenticationToken)) {
			response.sendRedirect(failureUrl);
			return;
		}
		PreOtpAuthenticationToken authToken = (PreOtpAuthenticationToken) auth;
		String username = authToken.getName();
		
		// Validate token
		if (tokenstore.isTokenValid(username, token)) {
			SecurityContextHolder.getContext().setAuthentication(authToken.getEmbeddedToken());

			HttpSession session = request.getSession();

			Date authTimestamp = new Date();
			session.setAttribute(AUTH_TIMESTAMP, authTimestamp);

			if (session.getAttribute(AuthorizationRequestFilter.PROMPT_REQUESTED) != null) {
				session.setAttribute(AuthorizationRequestFilter.PROMPTED, Boolean.TRUE);
				session.removeAttribute(AuthorizationRequestFilter.PROMPT_REQUESTED);
			}

			logger.info("Successful Authentication of " + username + " at " + authTimestamp.toString());

			SavedRequest savedRequest = requestCache.getRequest(request, response);
			if (savedRequest != null) {
				String redirectUrl = savedRequest.getRedirectUrl();
				response.sendRedirect(redirectUrl);
				logger.info("Redirect to : " + redirectUrl);
			} else {
				response.sendRedirect(successUrl);
				logger.info("Redirect to : " + successUrl);
			}

		} else {
			SecurityContextHolder.getContext().setAuthentication(null);
			response.sendRedirect(failureUrl);
		}
	}
}
