package org.mitre.openid.connect.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.mitre.openid.connect.model.*;
import org.mitre.openid.connect.service.UserInfoService;
import org.mitre.openid.connect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationBySmsService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserInfoService userInfoService;

	/**
	 * Register a new user with his phone number.
	 * 
	 * @param phoneNumber
	 *            The user phone number
	 * @return The user info
	 */
	public UserInfo registerNewUser(String phoneNumber) {

		final String email = phoneNumber + "@xconnect.com";
		final String pwd = generateTemporaryPassword();

		final User user = new User();
		user.setUsername(phoneNumber);
		user.setPassword(pwd);
		user.setEnabled(true);
		UserInfo userInfo = new DefaultUserInfo();
		userInfo.setPreferredUsername(phoneNumber);
		userInfo.setEmail(email);
		userInfo.setPhoneNumber(phoneNumber);
		userInfo.setSub(UUID.randomUUID().toString().replace("-", ""));
		userService.registerNewUser(user);
		userService.registerUserAuthority(new Authority(user, "ROLE_USER"));
		userInfoService.registerNewUser(userInfo);

		return userInfo;
	}

	/**
	 * @return a temporary password
	 */
	private String generateTemporaryPassword() {
		return RandomStringUtils.randomAlphanumeric(10).toUpperCase();
	}
}
