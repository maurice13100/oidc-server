/*******************************************************************************
 * Copyright 2016 The MITRE Corporation
 *   and the MIT Internet Trust Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.mitre.openid.connect.repository.impl;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mitre.openid.connect.model.UserConnection;
import org.mitre.openid.connect.repository.UserConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA UserConnection repository implementation
 * 
 * @author mrambert
 *
 */
@Repository
public class JpaUserConnectionRepository implements UserConnectionRepository {

	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(JpaUserConnectionRepository.class);

	@PersistenceContext(unitName = "defaultPersistenceUnit")
	private EntityManager entityManager;

	@Override
	@Transactional(value = "defaultTransactionManager")
	public Collection<UserConnection> getByClientId(String clientId) {
		TypedQuery<UserConnection> query = entityManager.createNamedQuery(UserConnection.QUERY_BY_CLIENT_ID,
				UserConnection.class);
		query.setParameter(UserConnection.PARAM_CLIENT_ID, clientId);
		return query.getResultList();
	}

	@Override
	@Transactional(value = "defaultTransactionManager")
	public UserConnection save(UserConnection userConnection) {
		logger.info("Saving {}", userConnection);
		entityManager.persist(userConnection);
		entityManager.flush();
		return userConnection;
	}
}
