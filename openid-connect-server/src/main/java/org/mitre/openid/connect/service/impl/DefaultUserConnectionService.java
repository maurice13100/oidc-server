package org.mitre.openid.connect.service.impl;

import java.util.Collection;

import org.mitre.openid.connect.model.UserConnection;
import org.mitre.openid.connect.repository.UserConnectionRepository;
import org.mitre.openid.connect.service.UserConnectionService;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultUserConnectionService implements UserConnectionService {

	@Autowired
	private UserConnectionRepository userConnectionRepository;

	@Override
	public Collection<UserConnection> getByClientId(String clientId) {
		return userConnectionRepository.getByClientId(clientId);
	}

	@Override
	public UserConnection save(UserConnection userConnection) {
		return userConnectionRepository.save(userConnection);
	}

}
