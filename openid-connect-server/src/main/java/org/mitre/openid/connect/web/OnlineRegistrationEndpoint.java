package org.mitre.openid.connect.web;

import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.mitre.oauth2.repository.AuthenticationHolderRepository;
import org.mitre.openid.connect.model.Address;
import org.mitre.openid.connect.model.Authority;
import org.mitre.openid.connect.model.DefaultAddress;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.User;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.service.UserInfoService;
import org.mitre.openid.connect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = OnlineRegistrationEndpoint.URL)
public class OnlineRegistrationEndpoint {

	public static final String URL = "registration";

	private final UserInfoService userInfoService;

	private final UserService userService;

	@Autowired
	public OnlineRegistrationEndpoint(UserService userService, UserInfoService userInfoService,
			AuthenticationHolderRepository authenticationHolderRepository) {
		this.userService = userService;
		this.userInfoService = userInfoService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String displayRegitrationPage(Model model) {
		UserDTO userDTO = new UserDTO();
		model.addAttribute("user", userDTO);
		return "registration";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView registerNewUser(@ModelAttribute("user") @Valid UserDTO accountDTO, BindingResult result) {
		if (!result.hasErrors()) {
			User user = mapDtoToUser(accountDTO);
			UserInfo userInfo = mapDtoToUserInfo(accountDTO);
			userInfo.setSub(UUID.randomUUID().toString().replace("-", ""));

			userService.registerNewUser(user);
			userInfoService.registerNewUser(userInfo);
			userService.registerUserAuthority(new Authority(user, "ROLE_USER"));

		} else {
			return new ModelAndView("registration", "user", accountDTO);
		}

		return new ModelAndView("registration_success", "user", accountDTO);
	}

	private UserInfo mapDtoToUserInfo(UserDTO userDTO) {
		final UserInfo userInfo = new DefaultUserInfo();
		userInfo.setPreferredUsername(userDTO.getUserName());
		userInfo.setEmail(userDTO.getEmail());
		final String rwandaNumber = "25" + StringUtils.substring(userDTO.getPhone(), 1);
		userInfo.setPhoneNumber(rwandaNumber);
		userInfo.setName(userDTO.getFamilyName() + " " + userDTO.getMiddleName());
		userInfo.setGivenName(userDTO.getGivenName());
		userInfo.setFamilyName(userDTO.getFamilyName());
		userInfo.setMiddleName(userDTO.getMiddleName());
		userInfo.setGender(userDTO.getGender());
		userInfo.setBirthdate(userDTO.getBirthdate());
		userInfo.setAddress(mapDtoToAddress(userDTO));
		return userInfo;
	}

	private User mapDtoToUser(UserDTO userDTO) {
		final User user = new User();
		user.setUsername(userDTO.getUserName());
		user.setPassword(userDTO.getPassword());
		user.setEnabled(true);
		return user;
	}

	private Address mapDtoToAddress(UserDTO userDTO) {
		final Address address = new DefaultAddress();
		address.setStreetAddress(userDTO.getStreetAddress());
		address.setLocality(userDTO.getLocality());
		address.setRegion(userDTO.getRegion());
		address.setPostalCode(userDTO.getPostalCode());
		address.setCountry(userDTO.getCountry());
		return address;
	}
}
