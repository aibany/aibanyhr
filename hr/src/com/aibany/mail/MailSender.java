
package com.aibany.mail;

import java.io.PrintStream;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportAdapter;
import javax.mail.event.TransportEvent;
import javax.mail.internet.MimeMessage;

public class MailSender {
	public static void sendMail(final MimeMessage message, Session session) throws Exception {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("ready to send mail" + Thread.currentThread().getName());
					if (MailConfig.get("openemail").equals("true")) {
						Transport transport = session.getTransport();

						final String info = "(" + message.getSubject() + ")";

						transport.addTransportListener(new TransportAdapter() {
							public void messageDelivered(TransportEvent event) {
								System.out.println("邮件发送成功! " + info);
							}

							public void messageNotDelivered(TransportEvent event) {
								System.out.println("邮件发送失败! " + info);
							}

							public void messagePartiallyDelivered(TransportEvent event) {
								System.out.println("邮件部分发送成功! " + info);
							}
						});
						transport.connect(MailConfig.get("hostemail"), MailConfig.get("hostpassword"));
						transport.sendMessage(message, message.getAllRecipients());

						transport.close();
					} else {
						System.err.println("邮件功能已关闭，将不再发送邮件!");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
}
