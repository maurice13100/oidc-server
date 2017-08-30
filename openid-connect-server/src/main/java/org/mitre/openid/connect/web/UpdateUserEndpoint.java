package org.mitre.openid.connect.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.mitre.oauth2.repository.AuthenticationHolderRepository;
import org.mitre.openid.connect.model.Address;
import org.mitre.openid.connect.model.DefaultAddress;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.OIDCAuthenticationToken;
import org.mitre.openid.connect.model.User;
import org.mitre.openid.connect.service.UserInfoService;
import org.mitre.openid.connect.service.UserService;
import org.mitre.openid.connect.view.HttpCodeView;
import org.mitre.openid.connect.view.JsonEntityView;
import org.mitre.openid.connect.view.JsonErrorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

@Controller
@RequestMapping(value = UpdateUserEndpoint.URL)
public class UpdateUserEndpoint {

	public static final String URL = RootController.API_URL + "/updateuser";

	private final UserInfoService userInfoService;

	private final UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UpdateUserEndpoint.class);

	@Autowired
	public UpdateUserEndpoint(UserService userService, UserInfoService userInfoService,
			AuthenticationHolderRepository authenticationHolderRepository) {
		this.userService = userService;
		this.userInfoService = userInfoService;
	}

	private Gson gson = new Gson();
	private JsonParser parser = new JsonParser();

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String updateUser(@RequestBody String jsonString, ModelMap m, Principal p) {

		JsonObject json;
		UpdateUserDTO updateUser = null;
		try {
			json = parser.parse(jsonString).getAsJsonObject();
			updateUser = gson.fromJson(json, UpdateUserDTO.class);

		} catch (JsonParseException e) {
			logger.error("updateUser failed due to JsonParseException", e);
			m.put(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
			m.put(JsonErrorView.ERROR_MESSAGE,
					"Could not update user. The server encountered a JSON syntax exception. Contact a system administrator for assistance.");
			return JsonErrorView.VIEWNAME;
		} catch (IllegalStateException e) {
			logger.error("updateUser failed due to IllegalStateException", e);
			m.put(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
			m.put(JsonErrorView.ERROR_MESSAGE,
					"Could not update user. The server encountered an IllegalStateException. Refresh and try again - if the problem persists, contact a system administrator for assistance.");
			return JsonErrorView.VIEWNAME;
		}

		if (!Strings.nullToEmpty(updateUser.getPassword()).equals(updateUser.getMatchingPassword())) {
			m.put(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
			m.put(JsonErrorView.ERROR_MESSAGE, "Password confirmation doesn't match Password.");
			return JsonErrorView.VIEWNAME;
		}
		// DefaultUserInfo loggedUser = getUser(request);
		User existantUser = userService.retrieveExistantUserFromUsername(p.getName());
		// existantUser.setUsername(user.getUsername());
		if (StringUtils.isNotBlank(updateUser.getPassword()))
			existantUser.setPassword(updateUser.getPassword());

		DefaultUserInfo userInfoToUpdate = (DefaultUserInfo) userInfoService.getByUsername(p.getName());

		mapDtoToUserInfo(userInfoToUpdate, updateUser);
		logger.info(updateUser.getFamilyName());
		logger.info(userInfoToUpdate.getFamilyName());

		logger.info("updating the user...");
		userService.updateUser(existantUser);
		logger.info("updating the user info...");
		userInfoService.updateUser(userInfoToUpdate);

		return JsonEntityView.VIEWNAME;
	}

	private void mapDtoToUserInfo(DefaultUserInfo defaultUserInfo, UpdateUserDTO userDTO) {

		if (StringUtils.isNotBlank(userDTO.getEmail()))
			defaultUserInfo.setEmail(userDTO.getEmail());

		if (StringUtils.isNotBlank(userDTO.getFamilyName()) || StringUtils.isNotBlank(userDTO.getMiddleName()))
			defaultUserInfo.setName(userDTO.getFamilyName() + " " + userDTO.getMiddleName());

		if (StringUtils.isNotBlank(userDTO.getGivenName()))
			defaultUserInfo.setGivenName(userDTO.getGivenName());

		if (StringUtils.isNotBlank(userDTO.getFamilyName()))
			defaultUserInfo.setFamilyName(userDTO.getFamilyName());

		if (StringUtils.isNotBlank(userDTO.getMiddleName()))
			defaultUserInfo.setMiddleName(userDTO.getMiddleName());

		if (StringUtils.isNotBlank(userDTO.getGender()))
			defaultUserInfo.setGender(userDTO.getGender());

		if (StringUtils.isNotBlank(userDTO.getBirthdate()))
			defaultUserInfo.setBirthdate(userDTO.getBirthdate());

		Address address = mapDtoToAddress(userDTO);
		if (address != null) {
			defaultUserInfo.setAddress(address);
		}
	}

	private User mapDtoToUser(UpdateUserDTO userDTO) {
		final User user = new User();
		user.setUsername(userDTO.getUserName());
		user.setPassword(userDTO.getPassword());
		user.setEnabled(true);
		return user;
	}

	private Address mapDtoToAddress(UpdateUserDTO userDTO) {
		boolean isEmptyOrNull = true;
		final Address address = new DefaultAddress();
		if (StringUtils.isNotBlank(userDTO.getStreetAddress())) {
			address.setStreetAddress(userDTO.getStreetAddress());
			isEmptyOrNull = false;
		}
		if (StringUtils.isNotBlank(userDTO.getStreetAddress())) {
			address.setLocality(userDTO.getLocality());
			isEmptyOrNull = false;
		}

		if (StringUtils.isNotBlank(userDTO.getStreetAddress())) {
			address.setRegion(userDTO.getRegion());
			isEmptyOrNull = false;
		}

		if (StringUtils.isNotBlank(userDTO.getStreetAddress())) {
			address.setPostalCode(userDTO.getPostalCode());
			isEmptyOrNull = false;
		}
		if (StringUtils.isNotBlank(userDTO.getStreetAddress())) {
			address.setCountry(userDTO.getCountry());
			isEmptyOrNull = false;
		}

		if (isEmptyOrNull) {
			return null;
		}

		return address;
	}

	private DefaultUserInfo getUser(HttpServletRequest request) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// logins
		if (auth instanceof OIDCAuthenticationToken) {
			// if they're logging into this server from a remote OIDC
			// server, pass through their user info
			OIDCAuthenticationToken oidc = (OIDCAuthenticationToken) auth;
			return (DefaultUserInfo) oidc.getUserInfo();
		} else {
			if (auth != null && auth.getName() != null && userInfoService != null) {
				return (DefaultUserInfo) userInfoService.getByUsername(auth.getName());

			}
		}

		return null;

	}
}
