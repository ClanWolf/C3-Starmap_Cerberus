/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : http://www.clanwolf.net                            |
 * GitHub      : https://github.com/ClanWolf                        |
 * ---------------------------------------------------------------- |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * C3 includes libraries and source code by various authors.        |
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.mail;

import net.clanwolf.starmap.client.logging.C3Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Handles sending of emails.
 *
 * @author Christian
 */
public class MailManager {
	private static String mailServer = null;
	private static String mailUser = null;
	private static String mailPassword = null;

	/**
	 * Empty constructor.
	 */
	public MailManager() {
		//
	}

	/**
	 * Sets the mail server to be used.
	 */
	public static void setMailCredentials(String server, String user, String password) {
		mailServer = server;
		mailUser = user;
		mailPassword = password;
	}

	/**
	 * Checks email adress for validity.
	 */
	private static boolean validEmailAdress(String emailadress) {
		boolean valid = false;
		try {
			InternetAddress internetAddress = new InternetAddress(emailadress);
			internetAddress.validate();
			valid = true;
		} catch (AddressException e) {
			C3Logger.exception("Invalid Email Adress " + emailadress, e);
		}
		return valid;
	}

	/**
	 * Sends off a previouisly created Mail object.
	 */
	private static boolean dispatch(Mail mail) {
		boolean result = false;

		javax.mail.Session session;
		Properties props;
		InternetAddress to;
		InternetAddress from;
		MimeMessage msg;

		Transport transport = null;

		try {
			props = new Properties();
			props.put("mail.smtp.host", mailServer);
			props.put("mail.smtp.connectiontimeout", "600000");
			props.put("mail.smtp.timeout", "600000");
			session = javax.mail.Session.getDefaultInstance(props, null);

			from = new InternetAddress(mail.sender());
			msg = new MimeMessage(session);
			msg.setFrom(from);
			msg.setHeader("X-Priority", Integer.toString(mail.priority()));
			if (mail.replyTo() != null && mail.replyTo().trim().length() > 0) {
				msg.setReplyTo(new Address[]{new InternetAddress(mail.replyTo())});
			}
			String[] recs = mail.recipients().split(";");
			for (String rec : recs) {
				to = new InternetAddress(rec);
				msg.addRecipient(Message.RecipientType.TO, to);
			}
			msg.setSubject(mail.subject(), "UTF-8");

			if (!mail.html().isEmpty()) {
				msg.setContent(mail.html(), "text/html; charset=UTF-8");
			} else {
				msg.setText(mail.text(), "UTF-8");
			}
			msg.setSentDate(new Date());
			msg.saveChanges();

			transport = session.getTransport("smtp");
			transport.connect(mailUser, mailPassword);
			transport.sendMessage(msg, msg.getAllRecipients());

			result = true;
			C3Logger.info("Successfully sent email from " + mail.sender() + " to " + mail.recipients());
		} catch (Exception e) {
			C3Logger.exception("Failed to send email " + e.getMessage(), e);
		} catch (Throwable t) {
			C3Logger.exception("Failed to send email " + t.getMessage(), t);
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					C3Logger.exception("Failed to close Mail smtp Transport: ", e);
				}
			}
		}
		return result;
	}

	/**
	 * Sends a mail.
	 */
	public static boolean sendMail(String sender, String[] receivers, String subject, String content, boolean html, boolean breakOnError) {
		if (mailServer == null || mailUser == null || mailPassword == null) {
			Properties mProperties = new Properties();
			try {
				InputStream is = MailManager.class.getResourceAsStream("/mail.properties");
				mProperties.load(is);
				String server = mProperties.getProperty("mail_server");
				String user = mProperties.getProperty("mail_user");
				String pw = mProperties.getProperty("mail_pw");
				MailManager.setMailCredentials(server, user, pw);
			} catch (IOException e) {
				C3Logger.exception("Failed to read mail properties. Not sending any mails.", e);
				return false;
			}
		}

		Mail mail = new Mail();
		if (MailManager.validEmailAdress(sender)) {
			mail.setSender(sender);
		} else {
			C3Logger.warning("Email Adress of Sender is not valid: " + sender);
			if (breakOnError) {
				throw new RuntimeException("Email Adress of Sender is not valid: " + sender);
			}
		}
		for (String receiver : receivers) {
			if (MailManager.validEmailAdress(receiver)) {
				mail.addRecipient(receiver);
			} else {
				C3Logger.warning("Email Adress of Receiver is not valid: " + receiver);
				if (breakOnError) {
					throw new RuntimeException("Email Adress of Receiver is not valid: " + receiver);
				}
			}
		}
		mail.setSubject(subject);

		if (content != null) {
			if (html) {
				mail.setHTML(content);
			} else {
				mail.setText(content);
			}
		}

		return MailManager.dispatch(mail);
	}
}
