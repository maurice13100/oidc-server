package org.mitre.openid.connect.repository;


import org.mitre.openid.connect.model.Authority;
import org.mitre.openid.connect.model.User;

/**
 * Created by hieu on 01/09/16.
 */
public interface UserRepository {
	public User registerUser(User user);
	public Authority registerRole(Authority authority);
	public Authority getUserAuthorityFromUsername(String username);
}
