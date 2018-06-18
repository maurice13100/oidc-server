package org.mitre.openid.connect.service.impl;

import org.mitre.openid.connect.model.Authority;
import org.mitre.openid.connect.model.User;
import org.mitre.openid.connect.repository.UserRepository;
import org.mitre.openid.connect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {

	private final UserRepository userRepository;

	@Autowired
	public DefaultUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User registerNewUser(User newUser) {
		return userRepository.registerUser(newUser);
	}

	@Override
	public Authority retrieveUserFromUsername(String username) {
		return userRepository.getUserAuthorityFromUsername(username);
	}

	@Override
	public Authority registerUserAuthority(Authority authority) {
		return userRepository.registerRole(authority);
	}

	@Override
	public User updateUser(User userToUpdate) {
		return userRepository.update(userToUpdate);
	}

	@Override
	public User retrieveExistantUserFromUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
