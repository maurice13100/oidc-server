package org.mitre.openid.connect.web;

import javax.ws.rs.WebApplicationException;

import org.springframework.http.ResponseEntity;

import com.authlete.common.api.AuthleteApi;
import com.authlete.common.api.AuthleteApiFactory;
import com.authlete.jaxrs.AccessTokenInfo;
import com.authlete.jaxrs.BaseResourceEndpoint;


public class SpringResourceEndpoint extends BaseResourceEndpoint
{
    /**
     * Get an instance of the {@link AuthleteApi} interface.
     *
     * <p>
     * Change the implementation of this method or override
     * this method as necessary.
     * </p>
     *
     * @return
     *         An instance of the {@link AuthleteApi} interface.
     */
    protected AuthleteApi getAuthleteApi()
    {
        // Get an implementation of AuthleteApi interface.
        return AuthleteApiFactory.getDefaultApi();
    }


    /**
     * Validate an access token.
     *
     * @param accessToken
     *         An access token to be validated.
     *
     * @return
     *         Information about the access token. If you want to
     *         get more information, call
     *         {@code AuthleteApi.introspect(IntrospectionRequest)}
     *         directly.
     *
     * @throws WebApplicationException
     *         The access token is invalid.
     */
    protected AccessTokenInfo validateAccessToken(String accessToken)
    {
        // Call a method defined in the super class.
        return validateAccessToken(getAuthleteApi(), accessToken);
    }


    /**
     * Validate an access token.
     *
     * @param accessToken
     *         An access token to be validated.
     *
     * @param requiredScopes
     *         Scopes that the access token must have.
     *
     * @return
     *         Information about the access token. If you want to
     *         get more information, call
     *         {@code AuthleteApi.introspect(IntrospectionRequest)}
     *         directly.
     *
     * @throws WebApplicationException
     *         The access token is invalid.
     */
    protected AccessTokenInfo validateAccessToken(
            String accessToken, String[] requiredScopes)
    {
        // Call a method defined in the super class.
        return validateAccessToken(
                getAuthleteApi(), accessToken, requiredScopes);
    }


    /**
     * Validate an access token.
     *
     * @param accessToken
     *         An access token to be validated.
     *
     * @param requiredScopes
     *         Scopes that the access token must have.
     *
     * @param requiredSubject
     *         Subject (unique identifier of a user) that the
     *         access token must be associated with.
     *
     * @return
     *         Information about the access token. If you want to
     *         get more information, call
     *         {@code AuthleteApi.introspect(IntrospectionRequest)}
     *         directly.
     *
     * @throws WebApplicationException
     *         The access token is invalid.
     */
    protected AccessTokenInfo validateAccessToken(
            String accessToken, String[] requiredScopes, String requiredSubject)
    {
        // Call a method defined in the super class.
        return validateAccessToken(
                getAuthleteApi(), accessToken, requiredScopes, requiredSubject);
    }


    /**
     * Convert a {@link WebApplicationException} instance to
     * a {@link ResponseEntity} instance.
     */
    protected ResponseEntity<?> toResponseEntity(WebApplicationException exception)
    {
        return null;//new ResponseEntityBuilder().exception(exception).build();
    }


    @Override
    protected void onError(WebApplicationException exception)
    {
        // Overriding this method to suppress exception.printStackTrace().
    }
}