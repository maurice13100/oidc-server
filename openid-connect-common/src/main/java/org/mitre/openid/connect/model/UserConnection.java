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
package org.mitre.openid.connect.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "user_connections")
@NamedQueries({
		@NamedQuery(name = UserConnection.QUERY_BY_CLIENT_ID, query = "select uc from UserConnection uc where uc.clientId = :"
				+ UserConnection.PARAM_CLIENT_ID) })
public class UserConnection {

	public static final String QUERY_BY_CLIENT_ID = "UserConnection.getByClientId";
	public static final String PARAM_CLIENT_ID = "clientId";

	private Long id;
	private Date date;
	private String userId;
	private String clientId;

	/**
	 * Empty constructor
	 */
	public UserConnection() {

	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the connection date
	 */
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiration")
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            The connection date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the user id
	 */
	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}

	/**
	 * @param user
	 *            id The user id to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the client id
	 */
	@Column(name = "client_id")
	public String getClientId() {
		return clientId;
	}

	/**
	 * @param client
	 *            id The client id to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
