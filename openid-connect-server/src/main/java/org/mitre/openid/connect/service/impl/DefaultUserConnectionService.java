package org.mitre.openid.connect.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.openid.connect.model.UserConnection;
import org.mitre.openid.connect.repository.UserConnectionRepository;
import org.mitre.openid.connect.repository.impl.JpaUserInfoRepository;
import org.mitre.openid.connect.service.UserConnectionService;
import org.mitre.openid.connect.util.UserConnectionUtils;
import org.mitre.openid.connect.web.UserConnectionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import dnl.utils.text.table.SeparatorPolicy;
import dnl.utils.text.table.TextTable;

@Service
public class DefaultUserConnectionService implements UserConnectionService {

  private static final Logger logger = LoggerFactory.getLogger(DefaultUserConnectionService.class);

  @Autowired
  private UserConnectionRepository userConnectionRepository;

  @Autowired
  private ClientDetailsEntityService clientDetailsEntityService;

  @Autowired
  private JpaUserInfoRepository jpaUserInfoRepository;

  @Override
  public Collection<UserConnection> getByClientId(String clientId) {
    return userConnectionRepository.getByClientId(clientId);
  }

  @Override
  public UserConnection save(UserConnection userConnection) {
    return userConnectionRepository.save(userConnection);
  }

  @Override
  public void sendUserConnectionByEmail() {
    for (ClientDetailsEntity client : clientDetailsEntityService.getAllClients()) {

      Collection<UserConnectionDTO> userConnections = UserConnectionUtils.toUserConnectionDTOs(
          getByClientId(String.valueOf(client.getId())), jpaUserInfoRepository);

      logger.info(client.getClientName() + client.getId());
      if (!userConnections.isEmpty()) {
        String content = toString(client.getClientName(), userConnections);
        logger.info("content : " + content);
         sendEmail("rambertmaurice@gmail.com", client.getClientName(), content);
         sendEmail("contact@fiatope.com", client.getClientName(), content);
         for (String contact : client.getContacts()) {
          sendEmail(contact, client.getClientName(), content);
          logger.info("contact : " + contact + " Client name " + client.getClientName());
         }
      }
    }
  }


  private static String getMonthName() {
    return Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
  }

  private String toString(String client, Collection<UserConnectionDTO> userConnectionDTOs) {
    StringBuilder userConnectionsAsString = new StringBuilder("Dear Customer, \n\n");
    userConnectionsAsString.append("Please find your connection history for the month "
        + getMonthName() + " to " + client + " :\n\n");

    String[] colmuns = {"Sub", "Username", " Phone number", "Date"};
    String[][] data = new String[userConnectionDTOs.size()][colmuns.length];
    for (int i = 0; i < userConnectionDTOs.size(); ++i) {
      UserConnectionDTO dto = (UserConnectionDTO) userConnectionDTOs.toArray()[i];
      data[i][0] = dto.getSub();
      data[i][1] = dto.getUsername();
      data[i][2] = dto.getPhoneNumber();
      data[i][3] = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(dto.getDate());
    }


    String content = "";

    try {
      TextTable tt = new TextTable(colmuns, data);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(baos, true, "utf-8");
      tt.printTable(ps, 10);
      content = new String(baos.toByteArray(), StandardCharsets.UTF_8);
      ps.close();
    } catch (UnsupportedEncodingException e) {
      logger.error("Encoding UTF-8 is not supported !!!!", e);
    }


    userConnectionsAsString.append(content.replace("\n", "\n\n"));
    userConnectionsAsString.append("\n\nBest regards,\n\nThe XConnect team");
    return userConnectionsAsString.toString();
  }

  private void sendEmail(String emailTo, String client, String body) {

    Email from = new Email("contact@fiatope.com");
    String subject = "Login history " + client;
    Email to = new Email(emailTo);
    Content content = new Content("text/plain", body);
    Mail mail = new Mail(from, subject, to, content);

    SendGrid sg =
        new SendGrid("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");
    Request request = new Request();

    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      logger.info("Status code : " + response.getStatusCode());
      logger.info("Body : " + response.getBody());
      logger.info("Headers : " + response.getHeaders());
    } catch (IOException ex) {
      logger.error("Error occured during sending email", ex);
    }
  }

}
