package org.mitre.openid.connect.service.impl;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserConnection;
import org.mitre.openid.connect.repository.UserConnectionRepository;
import org.mitre.openid.connect.service.UserConnectionService;
import org.mitre.openid.connect.service.UserInfoService;
import org.mitre.openid.connect.util.AcrEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import com.upcrob.springsecurity.otp.PreOtpAuthenticationToken;

@Service
public class DefaultUserConnectionService implements UserConnectionService {

	private static final Logger logger = LoggerFactory.getLogger(DefaultUserConnectionService.class);

	@Autowired
	private UserConnectionRepository userConnectionRepository;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private ClientDetailsEntityService clientService;

	@Override
	public Collection<UserConnection> getByClientId(String clientId) {
		return userConnectionRepository.getByClientId(clientId);
	}

	@Override
	public UserConnection save(UserConnection userConnection) {
		return userConnectionRepository.save(userConnection);
	}

	public boolean isLoggedBySMS(HttpServletRequest httpServletRequest) {
		return httpServletRequest.getSession().getAttribute("acr") != null
				&& ((String) httpServletRequest.getSession().getAttribute("acr")).contains(AcrEnum.SMS.getValue());
	}

	public Date successfulConnection(HttpServletRequest httpServletRequest, Authentication authentication) {

		Date authTimestamp = new Date();
		UserConnection userConnection = new UserConnection();
		userConnection.setDate(authTimestamp);
		userConnection.setUserId(getUserId(httpServletRequest, authentication));
		userConnection.setClientId(getClientId(authentication));
		userConnection.setConnectionType(isLoggedBySMS(httpServletRequest) ? UserConnection.ConnectionType.SMS
				: UserConnection.ConnectionType.PWD);
		save(userConnection);

		return authTimestamp;
	}

	public Date failureConnection(HttpServletRequest httpServletRequest, Authentication authentication) {

		Date authTimestamp = new Date();
		UserConnection userConnection = new UserConnection();
		userConnection.setDate(authTimestamp);
		userConnection.setUserId(getUserId(httpServletRequest, authentication));
		userConnection.setClientId(getClientId(authentication));
		userConnection.setConnectionType(isLoggedBySMS(httpServletRequest) ? UserConnection.ConnectionType.SMS
				: UserConnection.ConnectionType.PWD);
		save(userConnection);

		return authTimestamp;
	}

	private String getUserId(HttpServletRequest httpServletRequest, Authentication authentication) {

		Object user = httpServletRequest.getAttribute("userInfo");
		if (user != null && user instanceof DefaultUserInfo) {
			return Long.toString(((DefaultUserInfo) user).getId());
		}

		// get user with auth.getName
		return Long.toString(((DefaultUserInfo) userInfoService.getByUsername(authentication.getName())).getId());
	}

	private String getClientId(Authentication authentication) {

		OAuth2Authentication o2a = (OAuth2Authentication) authentication;
		String authClientId = o2a.getOAuth2Request().getClientId();
		return clientService.loadClientByClientId(authClientId).getClientId();

	}

}
