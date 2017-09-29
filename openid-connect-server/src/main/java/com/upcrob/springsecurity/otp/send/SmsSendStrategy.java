package com.upcrob.springsecurity.otp.send;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

/**
 * Strategy for sending OTP tokens via SMS. Tokens are sent to mobile providers
 * (via email) which subsequently send them to the user's phone via SMS text
 * message.
 *
 * Note that this implementation will try to send messages tokens to every
 * carrier, regardless of the phone number used. This may be problematic at
 * scale as carriers may choose to block certain senders if a large number of
 * failed (or even successful) attempts are made to send SMS messages from the
 * same email address. Using a dedicated service for reliable SMS transmission
 * is recommended.
 */
public class SmsSendStrategy implements SendStrategy {

	// Find your Account Sid and Token at twilio.com/console
	public static final String ACCOUNT_SID = "AC1ba5bebe835d0c5fc71b330e5e23d204";
	public static final String AUTH_TOKEN = "d276bc1c58c19e0940a4d9179b5bc3b8";

	private static final Logger logger = LoggerFactory.getLogger(SmsSendStrategy.class);

	@Override
	public void send(String token, String phoneNumber) {
		sendSms("Your connection's token is " + token + " . Enter it to login", phoneNumber);
	}

	/**
	 * Send a sms.
	 * 
	 * @param message
	 *            The content
	 * @param phoneNumber
	 *            The user phone number
	 */
	private void sendSms(String body, String phoneNumber) {
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

		// Build a filter for the MessageList
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("Body", body));
		params.add(new BasicNameValuePair("To", phoneNumber));
		params.add(new BasicNameValuePair("From", "+33644608735"));

		MessageFactory messageFactory = client.getAccount().getMessageFactory();
		Message message = null;
		try {
			message = messageFactory.create(params);
		} catch (TwilioRestException e) {
			e.printStackTrace();
		}
		logger.info(message.getSid());
	}
}