package org.mitre.openid.connect.service;

import org.mitre.openid.connect.model.Authority;
import org.mitre.openid.connect.model.User;

/**
 * Created by hieu on 01/09/16.
 */
public interface UserService {

	/**
	 * Register a new user
	 * @param newUser the new user
	 * @return the created user
	 */
	public User registerNewUser(User newUser);

	public Authority retrieveUserFromUsername(String username);

	public Authority registerUserAuthority(Authority authority);
}
