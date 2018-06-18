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
package org.mitre.openid.connect.web;

import java.util.ArrayList;
import java.util.Collection;

import org.mitre.openid.connect.model.UserConnection;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.impl.JpaUserInfoRepository;
import org.mitre.openid.connect.service.UserConnectionService;
import org.mitre.openid.connect.service.impl.DefaultUserService;
import org.mitre.openid.connect.view.JsonEntityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Michael Jett <mjett@mitre.org>
 */

@Controller
@RequestMapping("/" + UserConnectionAPI.URL)
@PreAuthorize("hasRole('ROLE_USER')")
public class UserConnectionAPI {

  public static final String URL = RootController.API_URL + "/userconnections";

  /**
   * Logger for this class
   */
  private static final Logger logger = LoggerFactory.getLogger(ClientAPI.class);

  @Autowired
  private UserConnectionService userConnectionService;

  @Autowired
  private JpaUserInfoRepository jpaUserInfoRepository;


  private DefaultUserService userService;

  /**
   * Get a list of all user connections by client id
   */
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public String getByClientId(@PathVariable("id") Long id, Model m, Authentication auth) {

    logger.info("Getting user connections for clientId=" + id);

    String clientId = Long.toString(id);
    Collection<UserConnection> userConnections = userConnectionService.getByClientId(clientId);
    ArrayList<UserConnectionDTO> userConnectionDTOs = new ArrayList<>();
    for (UserConnection userConnection : userConnections) {
      UserInfo userInfo = jpaUserInfoRepository.getById(userConnection.getUserId());

      UserConnectionDTO dto = new UserConnectionDTO();
      dto.setClientId(clientId);
      dto.setDate(userConnection.getDate());
      dto.setSub(userInfo.getSub());
      dto.setUsername(userInfo.getPreferredUsername());
      dto.setPhoneNumber(userInfo.getPhoneNumber());

      userConnectionDTOs.add(dto);
    }
    m.addAttribute(JsonEntityView.ENTITY, userConnectionDTOs);

    return JsonEntityView.VIEWNAME;
  }



}
