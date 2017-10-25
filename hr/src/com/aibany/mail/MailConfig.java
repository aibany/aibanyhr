package com.aibany.mail;

import com.aibany.weixin.service.StringCompressTool;
import java.io.PrintStream;
import java.util.Properties;

public class MailConfig
{
  private static Properties properties = null;
  public static final String HOST_EMAIL = "hostemail";
  public static final String HOST_PASSWORD = "hostpassword";
  public static final String HOST_SMTP = "hostsmtp";
  public static final String HOST_NAME = "hostrename";
  public static final String OPEN_EMAI = "openemail";
  
  static
  {
    synchronized (MailConfig.class) {
      loadConfig();
    }
  }
  
  private static void loadConfig() {
    try {
      Properties properties = new Properties();
      properties.load(MailConfig.class.getClassLoader().getResourceAsStream("mailconfig.properties"));
      properties = properties;
      System.out.println("加载邮件配置成功");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static String get(String key) {
    if ("hostpassword".equals(key)) {
      String password = properties.getProperty(key);
      return StringCompressTool.decodePassword(password);
    }
    return properties.getProperty(key);
  }
}
