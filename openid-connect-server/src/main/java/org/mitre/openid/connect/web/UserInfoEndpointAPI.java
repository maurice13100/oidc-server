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

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.mitre.openid.connect.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authlete.common.api.AuthleteApiFactory;
import com.authlete.jaxrs.BaseUserInfoEndpoint;

/**
 * OpenID Connect UserInfo endpoint, as specified in Standard sec 5 and Messages sec 2.4.
 * 
 * @author AANGANES
 *
 */
@Controller
@RequestMapping("/api/" + UserInfoEndpointAPI.URL)
public class UserInfoEndpointAPI extends BaseUserInfoEndpoint {

  public static final String URL = "userinfo";


  @Autowired
  private UserInfoRepository userInfoRepository;

  /**
   * Logger for this class
   */
  private static final Logger logger = LoggerFactory.getLogger(UserInfoEndpointAPI.class);


  /**
   * The userinfo endpoint for {@code GET} method.
   *
   * @see <a href="http://openid.net/specs/openid-connect-core-1_0.html#UserInfoRequest" >OpenID
   *      Connect Core 1.0, 5.3.1. UserInfo Request</a>
   */
  @GET
  public Response get(
      @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization,
      @QueryParam("access_token") String accessToken) {
    // Handle the userinfo request.
    return handle(extractAccessToken(authorization, accessToken));
  }


  /**
   * The userinfo endpoint for {@code POST} method.
   *
   * @see <a href="http://openid.net/specs/openid-connect-core-1_0.html#UserInfoRequest" >OpenID
   *      Connect Core 1.0, 5.3.1. UserInfo Request</a>
   */
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public Response post(
      @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization,
      @FormParam("access_token") String accessToken) {
    // Handle the userinfo request.
    return handle(extractAccessToken(authorization, accessToken));
  }


  /**
   * Handle the userinfo request.
   */
  private Response handle(String accessToken) {
    return handle(AuthleteApiFactory.getDefaultApi(),
        new UserInfoRequestHandlerSpiImpl(userInfoRepository), accessToken);
  }

}
