package org.mitre.openid.connect.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = OnlineRegistrationEndpoint.URL)
public class OnlineRegistrationEndpoint {

	public static final String URL = "register";

	@RequestMapping(method = RequestMethod.GET)
	public String displayRegitrationPage(Model model) {
		UserDTO userDTO = new UserDTO();
		model.addAttribute("user", userDTO);
		return "register";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView registerNewUser(@ModelAttribute("user") @Valid UserDTO accountDTO,
										BindingResult result,
										WebRequest request,
										Errors errors) {
		createUserAccount();
		return null;
	}

	private void createUserAccount() {

	}
}
