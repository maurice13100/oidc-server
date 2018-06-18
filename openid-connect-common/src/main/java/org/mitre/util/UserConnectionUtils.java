package org.mitre.util;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.mitre.openid.connect.model.UserConnection;
import org.mitre.openid.connect.model.UserConnection.ConnectionType;
import org.springframework.security.core.Authentication;

public final class UserConnectionUtils {

	public static boolean isLoggedBySMS(String acr) {
		return acr != null && acr.contains("LOA_2");
	}

	public static UserConnection successfulConnection(String userId, String clientId, String acr) {
		UserConnection userConnection = new UserConnection();
		userConnection.setDate(new Date());
		userConnection.setUserId(userId);
		userConnection.setClientId(clientId);
		userConnection.setConnectionType(getConnectionType(acr));
		return userConnection;
	}

	public static UserConnection failureConnection(HttpServletRequest httpServletRequest, Authentication authentication,
			Date authTimestamp) {
		UserConnection userConnection = new UserConnection();
		userConnection.setDate(authTimestamp);
		// userConnection.setUserId(getUserId(httpServletRequest,
		// authentication));
		// userConnection.setClientId(getClientId(authentication));
//		userConnection.setConnectionType(getConnectionType(httpServletRequest));
		return userConnection;
	}

	private static ConnectionType getConnectionType(String acr) {
		return isLoggedBySMS(acr) ? UserConnection.ConnectionType.SMS : UserConnection.ConnectionType.PWD;
	}

	// private String getUserId(HttpServletRequest httpServletRequest,
	// Authentication authentication) {
	//
	// Object user = httpServletRequest.getAttribute("userInfo");
	// if (user != null && user instanceof DefaultUserInfo) {
	// return Long.toString(((DefaultUserInfo) user).getId());
	// }
	//
	// // get user with auth.getName
	// return Long.toString(((DefaultUserInfo)
	// userInfoService.getByUsername(authentication.getName())).getId());
	// }
	//
	// private String getClientId(Authentication authentication) {
	// OAuth2Authentication o2a = (OAuth2Authentication) authentication;
	// String authClientId = o2a.getOAuth2Request().getClientId();
	// return clientService.loadClientByClientId(authClientId).getClientId();
	//
	// }
	
}
