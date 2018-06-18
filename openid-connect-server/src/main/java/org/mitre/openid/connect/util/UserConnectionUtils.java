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

package org.mitre.openid.connect.util;

import java.util.ArrayList;
import java.util.Collection;

import org.mitre.openid.connect.model.UserConnection;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.impl.JpaUserInfoRepository;
import org.mitre.openid.connect.web.UserConnectionDTO;


public class UserConnectionUtils {

  public static Collection<UserConnectionDTO> toUserConnectionDTOs(
      Collection<UserConnection> userConnections, JpaUserInfoRepository jpaUserInfoRepository) {

    ArrayList<UserConnectionDTO> userConnectionDTOs = new ArrayList<>();
    for (UserConnection userConnection : userConnections) {
      UserInfo userInfo = jpaUserInfoRepository.getById(userConnection.getUserId());

      UserConnectionDTO dto = new UserConnectionDTO();
      dto.setClientId(userConnection.getClientId());
      dto.setDate(userConnection.getDate());
      dto.setSub(userInfo.getSub());
      dto.setUsername(userInfo.getPreferredUsername());
      dto.setPhoneNumber(userInfo.getPhoneNumber());

      userConnectionDTOs.add(dto);
    }

    return userConnectionDTOs;
  }
}
