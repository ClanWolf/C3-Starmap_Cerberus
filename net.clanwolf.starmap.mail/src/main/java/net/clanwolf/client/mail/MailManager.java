/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : https://www.clanwolf.net                           |
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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.client.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.Properties;

public class MailManager {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static String mailServer = null;
	private static String mailUser = null;
	private static String mailPassword = null;

	public MailManager() {
		//
	}

	public static void setMailCredentials(String server, String user, String password) {
		mailServer = server;
		mailUser = user;
		mailPassword = password;
	}

//	private static boolean validEmailAdress(String emailadress) {
//		boolean valid = false;
//		try {
//			InternetAddress internetAddress = new InternetAddress(emailadress);
//			internetAddress.validate();
//			valid = true;
//		} catch (AddressException e) {
//			e.printStackTrace();
//		}
//		return valid;
//	}

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
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.host", mailServer);
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.port", "587");
			props.setProperty("mail.smtp.ssl.enable", "false");
			props.setProperty("mail.smtp.socketFactory.port", "587");
//			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.socketFactory.fallback", "true");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.connectiontimeout", "600000");
			props.setProperty("mail.smtp.timeout", "600000");
			props.setProperty("mail.debug", "false");

			session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailUser, mailPassword);
				}
			});

			from = new InternetAddress(mail.sender());
			msg = new MimeMessage(session);
			msg.setFrom(from);
			msg.setHeader("X-Priority", Integer.toString(mail.priority()));
			if (mail.replyTo() != null && mail.replyTo().trim().length() > 0) {
				msg.setReplyTo(new Address[] { new InternetAddress(mail.replyTo()) });
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
			transport.connect(mailServer, mailUser, mailPassword);
			transport.sendMessage(msg, msg.getAllRecipients());

			result = true;
			logger.info("Successfully sent email from " + mail.sender() + " to " + mail.recipients());
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("Failed to send email.", e);
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					logger.info("Failed to close Mail smtp Transport: " + e.getMessage());
				}
			}
		}
		return result;
	}

	public static boolean sendMail(String sender, String[] receivers, String subject, String content, boolean html) {
		if (mailServer == null || mailUser == null || mailPassword == null) {
			Properties mProperties = new Properties();
			try {
				mProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("mail.properties"));
				String server = mProperties.getProperty("mail_server");
				String user = mProperties.getProperty("mail_user");
				String pw = mProperties.getProperty("mail_pw");
				MailManager.setMailCredentials(server, user, pw);
			} catch (IOException e) {
				logger.error("Failed to read mail properties", e);
				return false;
			}
		}

		Mail mail = new Mail();
		mail.setSender(sender);
		for (String receiver : receivers) {
			mail.addRecipient(receiver);
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
