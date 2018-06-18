/*******************************************************************************
 * Copyright 2016 The MITRE Corporation and the MIT Internet Trust Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package org.mitre.openid.connect.repository.impl;

import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.UserInfoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static org.mitre.util.jpa.JpaUtil.saveOrUpdate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.mitre.util.jpa.JpaUtil.getSingleResult;

/**
 * JPA UserInfo repository implementation
 * 
 * @author Michael Joseph Walsh
 *
 */
@Repository("jpaUserInfoRepository")
public class JpaUserInfoRepository implements UserInfoRepository {

  @PersistenceContext(unitName = "defaultPersistenceUnit")
  private EntityManager manager;

  /**
   * Get a single UserInfo object by its username
   */
  @Override
  public UserInfo getByUsername(String username) {
    TypedQuery<DefaultUserInfo> query =
        manager.createNamedQuery(DefaultUserInfo.QUERY_BY_USERNAME, DefaultUserInfo.class);
    query.setParameter(DefaultUserInfo.PARAM_USERNAME, username);

    return getSingleResult(query.getResultList());

  }

  /**
   * Get a single UserInfo object by its email address
   */
  @Override
  public UserInfo getByEmailAddress(String email) {
    TypedQuery<DefaultUserInfo> query =
        manager.createNamedQuery(DefaultUserInfo.QUERY_BY_EMAIL, DefaultUserInfo.class);
    query.setParameter(DefaultUserInfo.PARAM_EMAIL, email);

    return getSingleResult(query.getResultList());
  }

  @Override
  public UserInfo getByPhoneNumber(String phoneNumber) {
    TypedQuery<DefaultUserInfo> query =
        manager.createNamedQuery(DefaultUserInfo.QUERY_BY_PHONE_NUMBER, DefaultUserInfo.class);
    query.setParameter(DefaultUserInfo.PARAM_PHONE_NUMBER, phoneNumber);

    return getSingleResult(query.getResultList());
  }

  /**
   * Register a new user
   * 
   * @param newUser user information to register
   * @return UserId
   */
  @Override
  @Transactional(value = "defaultTransactionManager")
  public UserInfo registerNewUser(UserInfo newUser) {
    manager.merge(newUser);
    manager.flush();

    return newUser;
  }

  @Override
  @Transactional(value = "defaultTransactionManager")
  public UserInfo updateUser(DefaultUserInfo userToUpdate) {
    // manager.merge(userToUpdate);
    // manager.flush();

    saveOrUpdate(userToUpdate.getId(), manager, userToUpdate);

    return userToUpdate;
  }

  @Override
  public UserInfo getById(String id) {
    TypedQuery<DefaultUserInfo> query =
        manager.createNamedQuery(DefaultUserInfo.QUERY_BY_ID, DefaultUserInfo.class);
    query.setParameter(DefaultUserInfo.PARAM_ID, new Long(id));
    return getSingleResult(query.getResultList());
  }

  @Override
  public UserInfo getBySub(String sub) {
    TypedQuery<DefaultUserInfo> query =
        manager.createNamedQuery(DefaultUserInfo.QUERY_BY_SUB, DefaultUserInfo.class);
    query.setParameter(DefaultUserInfo.PARAM_SUB, sub);
    return getSingleResult(query.getResultList());
  }
}
