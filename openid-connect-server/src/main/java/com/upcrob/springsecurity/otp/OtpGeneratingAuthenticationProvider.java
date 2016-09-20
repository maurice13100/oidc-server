package com.upcrob.springsecurity.otp;

import com.upcrob.springsecurity.otp.otpgenerator.DefaultOtpGenerator;
import com.upcrob.springsecurity.otp.otpgenerator.OtpGenerator;
import com.upcrob.springsecurity.otp.send.SendStrategy;
import com.upcrob.springsecurity.otp.tokenstore.Tokenstore;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * This wraps another AuthenticationProvider in order to wrap authenticated tokens
 * with a PreOtpAuthenticationToken.  This indicates that the first phase of authentication
 * has been completed, but OTP verification has yet to take place.
 */
public class OtpGeneratingAuthenticationProvider extends DaoAuthenticationProvider implements EnvironmentAware {

	private Tokenstore tokenstore;
	private SendStrategy sendStrategy;
	private OtpGenerator gen;
	private static final int DEFAULT_OTP_LENGTH = 5;
	private static final Logger logger = LoggerFactory.getLogger(OtpGeneratingAuthenticationProvider.class);
	private UserInfoService userInfoService;
	private Environment environment;

	public OtpGeneratingAuthenticationProvider(
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
		this.setUserDetailsService(userDetailsService);
		this.tokenstore = tokenstore;
		this.sendStrategy = sendStrategy;
		this.gen = new DefaultOtpGenerator(DEFAULT_OTP_LENGTH);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication auth = super.authenticate(authentication);
		if (auth.isAuthenticated()) {
			// Generate OTP token
			String otp = gen.generateToken();
			tokenstore.putToken(auth.getName(), otp);
			UserInfo userInfo = userInfoService.getByUsername(auth.getName());
			logger.warn(otp + " " + auth.getName());
			if (environment.getActiveProfiles() == null || environment.getActiveProfiles().length == 0) {
				sendStrategy.send(otp, userInfo.getPhoneNumber());
			} else {
				for (String profile : environment.getActiveProfiles()) {
					if (!profile.equals("dev")) {
						sendStrategy.send(otp, userInfo.getPhoneNumber());
					}
				}
			}
		}
		return new PreOtpAuthenticationToken(auth);
	}

	public void setOtpGenerator(OtpGenerator generator) {
		if (generator == null) {
			throw new IllegalArgumentException("OTP generator instance cannot be null.");
		}
		gen = generator;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
