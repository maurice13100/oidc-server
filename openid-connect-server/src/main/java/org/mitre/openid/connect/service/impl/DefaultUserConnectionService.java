package org.mitre.openid.connect.service.impl;

import java.io.IOException;
import java.util.Collection;

import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.openid.connect.model.UserConnection;
import org.mitre.openid.connect.repository.UserConnectionRepository;
import org.mitre.openid.connect.service.UserConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class DefaultUserConnectionService implements UserConnectionService {

	private static final Logger logger = LoggerFactory.getLogger(DefaultUserConnectionService.class);

	@Autowired
	private UserConnectionRepository userConnectionRepository;

	@Autowired
	private ClientDetailsEntityService clientDetailsEntityService;

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
			logger.info("Contacts " + Joiner.on('\n').join(client.getContacts()));
			logger.info("User connection list " + Joiner.on('\n').join(getByClientId(client.getClientId())));
			sendEmail("rambertmaurice@gmail.com");
		}
	}

	private void sendEmail(String emailTo) {

		Email from = new Email("contact@fiatope.com");
		String subject = "User's login history";
		Email to = new Email(emailTo);
		Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid("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");
		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
//			Response response = sg.api(request);
//			logger.info("Status code : " + response.getStatusCode());
//			logger.info("Body : " + response.getBody());
//			logger.info("Headers : " + response.getHeaders());
		} catch (IOException ex) {
			logger.error("Error occured during sending email", ex);
		}
	}

}
