package com.aibany.mail;

import com.agileai.util.StringUtil;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MailCreator
{
  public static Session getSession()
  {
    Properties props = new Properties();
    props.setProperty("mail.transport.protocol", "smtp");
    props.setProperty("mail.smtp.host", MailConfig.get("hostsmtp"));
    props.setProperty("mail.smtp.auth", "true");
    
    Session session = Session.getDefaultInstance(props);
    session.setDebug(true);
    return session;
  }
  
  public static MimeMessage createMimeMessage(Session session, String receiveMail, String title, String messageBody)
    throws Exception
  {
    MimeMessage message = new MimeMessage(session);
    
    message.setFrom(new InternetAddress(MailConfig.get("hostemail"), MailConfig.get("hostrename"), 
      "UTF-8"));
    
    message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "收件人昵称", "UTF-8"));
    
    message.setSubject(title, "UTF-8");
    
    message.setContent(messageBody, "text/html;charset=UTF-8");
    
    message.setSentDate(new Date());
    
    message.saveChanges();
    
    return message;
  }
  
  public static MimeMessage createMimeMessage(Session session, String receiveMail, String title, String messageBody, String imgpath, String filepath)
    throws Exception
  {
    MimeMessage message = new MimeMessage(session);
    
    message.setFrom(new InternetAddress(MailConfig.get("hostemail"), MailConfig.get("hostrename"), 
      "UTF-8"));
    
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiveMail, "收件人昵称", "UTF-8"));
    
    message.setSubject(title, "UTF-8");
    
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(messageBody);
    MimeBodyPart text = new MimeBodyPart();
    MimeMultipart mutipart = new MimeMultipart();
    MimeMultipart maxPart = new MimeMultipart();
    if (StringUtil.isNotEmpty(imgpath)) {
      if (imgpath.startsWith("http")) {
        stringBuilder.append("<br/><img src='" + imgpath + "'/>");
      } else {
        MimeBodyPart image = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource(imgpath));
        image.setDataHandler(dh);
        image.setContentID("image_fairy_tail");
        stringBuilder.append("<br/><img src='cid:image_fairy_tail'/>");
        mutipart.addBodyPart(image);
        mutipart.setSubType("related");
      }
    }
    
    if (StringUtil.isNotEmpty(filepath)) {
      if (filepath.startsWith("http")) {
        stringBuilder.append("<br/>附件地址:<a href='" + filepath + "'>邮件附件</a>");
      } else {
        MimeBodyPart attachment = new MimeBodyPart();
        DataHandler dh2 = new DataHandler(new FileDataSource(filepath));
        attachment.setDataHandler(dh2);
        attachment.setFileName(MimeUtility.encodeText(dh2.getName()));
        maxPart.addBodyPart(attachment);
      }
    }
    text.setContent(stringBuilder.toString(), "text/html;charset=UTF-8");
    mutipart.addBodyPart(text);
    
    MimeBodyPart text_image = new MimeBodyPart();
    text_image.setContent(mutipart);
    
    maxPart.addBodyPart(text_image);
    maxPart.setSubType("mixed");
    
    message.setContent(maxPart);
    
    message.setSentDate(new Date());
    
    message.saveChanges();
    
    return message;
  }
}
