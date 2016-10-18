package com.upcrob.springsecurity.otp;

import com.upcrob.springsecurity.otp.otpgenerator.DefaultOtpGenerator;
import com.upcrob.springsecurity.otp.otpgenerator.OtpGenerator;
import com.upcrob.springsecurity.otp.send.SendStrategy;
import com.upcrob.springsecurity.otp.tokenstore.Tokenstore;
import org.mitre.openid.connect.model.PhoneNumberAuthenticationToken;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class PhoneNumberAuthenticationProvider implements AuthenticationProvider, EnvironmentAware {

	private Tokenstore tokenstore;
	private SendStrategy sendStrategy;
	private OtpGenerator gen;
	private static final int DEFAULT_OTP_LENGTH = 5;
	private static final Logger logger = LoggerFactory.getLogger(OtpGeneratingAuthenticationProvider.class);
	private UserInfoService userInfoService;
	private UserDetailsService userDetailsService;
	private Environment environment;

	public PhoneNumberAuthenticationProvider(
		UserInfoService userInfoService,
		UserDetailsService userDetailsService,
		Tokenstore tokenstore,
		SendStrategy sendStrategy) {
		if (tokenstore == null) {
			throw new IllegalArgumentException("Tokenstore must not be null.");
		}
		if (sendStrategy == null) {
			throw new IllegalArgumentException("SendStrategy must not be null.");
		}
		this.userInfoService = userInfoService;
		this.userDetailsService = userDetailsService;
		this.tokenstore = tokenstore;
		this.sendStrategy = sendStrategy;
		this.gen = new DefaultOtpGenerator(DEFAULT_OTP_LENGTH);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		PhoneNumberAuthenticationToken authenticationToken = (PhoneNumberAuthenticationToken) authentication;

		String phoneNumber = authenticationToken.getPhoneNumber();
		UserInfo userInfo = userInfoService.getByPhoneNumber(phoneNumber);
		if (userInfo != null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getPreferredUsername());
			Authentication resultAuthentication = new PhoneNumberAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), phoneNumber, userDetails.getAuthorities());

			// Generate OTP token
			String otp = gen.generateToken();
			tokenstore.putToken(resultAuthentication.getName(), otp);

			logger.warn(otp + " " + resultAuthentication.getName());

			for (String profile : environment.getActiveProfiles()) {
				if (!profile.equals("dev")) {
					sendStrategy.send(otp, userInfo.getPhoneNumber());
				}
			}
			return new PreOtpAuthenticationToken(resultAuthentication);
		}

		throw new AuthenticationCredentialsNotFoundException("Phone number not found");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(PhoneNumberAuthenticationToken.class);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
