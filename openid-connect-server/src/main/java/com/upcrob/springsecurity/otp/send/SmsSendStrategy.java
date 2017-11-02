package com.upcrob.springsecurity.otp.send;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Strategy for sending OTP tokens via SMS. Tokens are sent to mobile providers (via email) which
 * subsequently send them to the user's phone via SMS text message.
 *
 * Note that this implementation will try to send messages tokens to every carrier, regardless of
 * the phone number used. This may be problematic at scale as carriers may choose to block certain
 * senders if a large number of failed (or even successful) attempts are made to send SMS messages
 * from the same email address. Using a dedicated service for reliable SMS transmission is
 * recommended.
 */
public class SmsSendStrategy implements SendStrategy {

  private static final String URL = "http://41.74.172.132:8080/SMSServiceProvider/sendSMS";
  private static final String SOURCE = "FIATOP";
  private static final String CONTRACT_ID = "127433452";

  private static final Logger logger = LoggerFactory.getLogger(SmsSendStrategy.class);

  /*
   * (non-Javadoc)
   * 
   * @see com.upcrob.springsecurity.otp.send.SendStrategy#send(java.lang.String, java.lang.String)
   */
  @Override
  public void send(String token, String phoneNumber) {
    sendSms("Your connection's token is " + token + " . Enter it to login", phoneNumber);
  }

  /**
   * Send a sms.
   * 
   * @param message The content
   * @param phoneNumber The user phone number
   */
  private void sendSms(String message, String phoneNumber) {
    CloseableHttpClient client = HttpClients.createDefault();

    try {
      HttpPost httpPost = new HttpPost(URL);

      JsonObject jObject = new JsonObject();
      jObject.addProperty("src", SOURCE);
      String formattedPhoneNumber = phoneNumber;

      if (formattedPhoneNumber.startsWith("+")) {
        formattedPhoneNumber = formattedPhoneNumber.substring(1);
      }
      if (formattedPhoneNumber.startsWith("07")) {
        formattedPhoneNumber = "250" + formattedPhoneNumber.substring(1);
      }

      jObject.addProperty("dest", formattedPhoneNumber);
      jObject.addProperty("message", message);
      jObject.addProperty("wait", 0);
      jObject.addProperty("contractId", CONTRACT_ID);

      String json = new Gson().toJson(jObject);
      logger.info("Body  : " + json);
      StringEntity entity = new StringEntity(json);
      httpPost.setEntity(entity);
      httpPost.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
      httpPost.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);

      CloseableHttpResponse response = client.execute(httpPost);
      HttpEntity httpEntity = response.getEntity();
      logger.info("message={}", httpEntity != null ? EntityUtils.toString(httpEntity) : "");
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != 200) {
        logger.error("It is too bad !!! [code error={}]", statusCode);
      }
      IOUtils.closeQuietly(response);
    } catch (Exception e) {
      logger.error("Error occured during sending sms", e);
    } finally {
      IOUtils.closeQuietly(client);
    }
  }
}
