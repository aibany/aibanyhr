package com.aibany.mail;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class MailTest
{
  public static void main(String[] args)
    throws Exception
  {
    String sendAddr = "644531343@qq.com";
    
    Session session = MailCreator.getSession();
    
    MimeMessage message = MailCreator.createMimeMessage(session, sendAddr, "你好啊", "这是一封测试邮件");
    
    MailSender.sendMail(message, session);
  }
}
