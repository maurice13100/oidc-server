package org.mitre.openid.connect.repository.impl;

import org.mitre.openid.connect.model.Authority;
import org.mitre.openid.connect.model.User;
import org.mitre.openid.connect.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import static org.mitre.util.jpa.JpaUtil.saveOrUpdate;

@Repository("jpaUserRepository")
public class JpaUserRepository implements UserRepository {

	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(JpaUserRepository.class);

	@PersistenceContext(unitName = "defaultPersistenceUnit")
	private EntityManager entityManager;

	@Override
	@Transactional(value = "defaultTransactionManager")
	public User registerUser(User user) {
		logger.info("Registrating {}", user);
		entityManager.persist(user);
		entityManager.flush();

		return user;
	}

	@Override
	@Transactional(value = "defaultTransactionManager")
	public Authority registerRole(Authority authority) {
		logger.info("Authority {}", authority);
		entityManager.persist(authority);
		entityManager.flush();

		return authority;
	}

	@Override
	@Transactional(value = "defaultTransactionManager", readOnly = true)
	public Authority getUserAuthorityFromUsername(String username) {
		TypedQuery<Authority> query = entityManager.createNamedQuery(Authority.QUERY_BY_NAME, Authority.class);
		query.setParameter("username", username);

		return query.getSingleResult();
	}

	@Override
	@Transactional(value = "defaultTransactionManager")
	public User update(User user) {
		return saveOrUpdate(user.getUsername(), entityManager, user);
	}

	@Override
	@Transactional(value = "defaultTransactionManager", readOnly = true)
	public User findByUsername(String username) {
		TypedQuery<User> query = entityManager.createNamedQuery(User.QUERY_BY_NAME, User.class);
		query.setParameter("username", username);

		return query.getSingleResult();
	}
}
