package org.mitre.openid.connect.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.User;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.service.UserInfoService;
import org.mitre.openid.connect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upcrob.springsecurity.otp.send.SmsSendStrategy;

@Service
public class RegistrationBySmsService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private SmsSendStrategy smsSendStrategy;

	/**
	 * Register a new user with his phone number.
	 * 
	 * @param phoneNumber
	 *            The user phone number
	 * @return The user info
	 */
	public UserInfo registerNewUser(String phoneNumber) {

		final String pwd = generateTemporaryPassword();

		final User user = new User();
		user.setUsername(phoneNumber);
		user.setPassword(pwd);
		user.setEnabled(true);
		UserInfo userInfo = new DefaultUserInfo();
		userInfo.setPreferredUsername(phoneNumber);
		userInfo.setEmail(phoneNumber + "@xconnect.com");
		userInfo.setPhoneNumber(phoneNumber);
		userInfo.setSub(UUID.randomUUID().toString().replace("-", ""));
		userService.registerNewUser(user);
		userInfoService.registerNewUser(userInfo);

		smsSendStrategy.sendSms(String.format("Your temporary password is %s.", pwd), phoneNumber);

		return userInfo;
	}

	/**
	 * @return a temporary password
	 */
	private String generateTemporaryPassword() {
		return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
	}
}
