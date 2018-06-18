package org.mitre.openid.connect.web;

import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.UserInfoRepository;

import com.authlete.jaxrs.spi.UserInfoRequestHandlerSpiAdapter;

public class UserInfoRequestHandlerSpiImpl extends UserInfoRequestHandlerSpiAdapter {

  private UserInfo user;

  private UserInfoRepository userInfoRepository;

  public UserInfoRequestHandlerSpiImpl(UserInfoRepository userInfoRepository) {
    this.userInfoRepository = userInfoRepository;
  }

  @Override
  public void prepareUserClaims(String sub, String[] claimNames) {
    // Look up a user who has the subject.
    user = userInfoRepository.getBySub(sub);
  }


  @Override
  public Object getUserClaim(String claimName, String languageTag) {
    // If looking up a user has failed in prepareUserClaims().
    if (user == null) {
      // No claim is available.
      return null;
    }

    // Get the value of the claim.
    return user.getClaim(claimName, languageTag);
  }
}
