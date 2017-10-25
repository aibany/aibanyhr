package com.aibany.weixin.core;

import com.aibany.weixin.model.Constans;
import com.aibany.weixin.tool.MessageBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class MessageBrokerServlet
  extends HttpServlet
{
  private static final long serialVersionUID = 4440739483644821986L;
  private static String MessageEventHandlerClassName = null;
  protected static Logger log = Logger.getLogger(MessageBrokerServlet.class);
  
  public void init() throws ServletException
  {
    Constans.Configs.APPID = getInitParameter("APPID");
    Constans.Configs.APPSECRET = getInitParameter("APPSECRET");
    Constans.Configs.TOKEN = getInitParameter("TOKEN");
    Constans.Configs.BAIDU_KEY = getInitParameter("BAIDU_KEY");
    Constans.Configs.HOSTS = getInitParameter("HOSTS");
    
    String locaionCacheMinutes = getInitParameter("LOCAION_CACHE_MINUTES");
    if ((locaionCacheMinutes != null) && (!locaionCacheMinutes.trim().equals(""))) {
      Constans.Configs.LOCAION_CACHE_MINUTES = locaionCacheMinutes;
    }
    
    String accessTokenCacheMinutes = getInitParameter("ACCESSTOKEN_CACHE_MINUTES");
    if ((accessTokenCacheMinutes != null) && (!accessTokenCacheMinutes.trim().equals(""))) {
      Constans.Configs.ACCESSTOKEN_CACHE_MINUTES = accessTokenCacheMinutes;
    }
    
    MessageEventHandlerClassName = getInitParameter("MessageEventHandlerClassName");
  }
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String signature = request.getParameter("signature");
    String timestamp = request.getParameter("timestamp");
    String nonce = request.getParameter("nonce");
    String echostr = request.getParameter("echostr");
    PrintWriter out = response.getWriter();
    if (checkSignature(signature, timestamp, nonce)) {
      out.print(echostr);
    }
    out.close();
    out = null;
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    
    String respMessage = processRequest(request, response);
    
    PrintWriter out = response.getWriter();
    out.print(respMessage);
    
    out.close();
  }
  
  private String processRequest(HttpServletRequest request, HttpServletResponse response) {
    String respMessage = null;
    Map<String, String> requestMap = null;
    MessageEventHandler messageEventHandler = null;
    try
    {
      requestMap = MessageBuilder.parseXml(request);
      
      Class<?> handlerClass = Thread.currentThread().getContextClassLoader().loadClass(MessageEventHandlerClassName);
      messageEventHandler = (MessageEventHandler)handlerClass.newInstance();
      messageEventHandler.setContextProperties(this, request, response);
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getLocalizedMessage(), e);
      return "";
    }
    
    String msgType = (String)requestMap.get("MsgType");
    System.out.println("消息类型:" + msgType + "内容:" + requestMap);
    
    if (msgType.equals("event")) {
      String eventType = (String)requestMap.get("Event");
      if (eventType.contains("scancode")) {
        respMessage = messageEventHandler.handleQrCodeEvent(requestMap);
      }
      if (eventType.equals("subscribe")) {
        respMessage = messageEventHandler.handleSubscribe(requestMap);
      }
      else if (eventType.equals("unsubscribe")) {
        respMessage = messageEventHandler.handleUnsubscribe(requestMap);
      }
      else if (eventType.equals("CLICK")) {
        String eventKey = (String)requestMap.get("EventKey");
        respMessage = messageEventHandler.handleMenuClickEvent(eventKey, requestMap);
      }
      else if (eventType.equals("VIEW")) {
        String eventKey = (String)requestMap.get("EventKey");
        respMessage = messageEventHandler.handleMenuViewEvent(eventKey, requestMap);
      }
      else if (eventType.equals("LOCATION")) {
        respMessage = messageEventHandler.handleLocationEvent(requestMap);
      }
    }
    else if (msgType.equals("text")) {
      respMessage = messageEventHandler.handleTextMessage(requestMap);
    }
    else if (msgType.equals("image")) {
      respMessage = messageEventHandler.handleImageMessage(requestMap);
    }
    else if (msgType.equals("voice")) {
      respMessage = messageEventHandler.handleVoiceMessage(requestMap);
    }
    else if (msgType.equals("video")) {
      respMessage = messageEventHandler.handleVideoMessage(requestMap);
    }
    else if (msgType.equals("location")) {
      respMessage = messageEventHandler.handleLocationMessage(requestMap);
    }
    else if (msgType.equals("link")) {
      respMessage = messageEventHandler.handleLinkMessage(requestMap);
    }
    
    if (respMessage == null) {
      return "";
    }
    return respMessage;
  }
  
  private boolean checkSignature(String signature, String timestamp, String nonce) {
    String[] arr = { Constans.Configs.TOKEN, timestamp, nonce };
    Arrays.sort(arr);
    StringBuilder content = new StringBuilder();
    for (int i = 0; i < arr.length; i++) {
      content.append(arr[i]);
    }
    MessageDigest md = null;
    String tmpStr = null;
    try {
      md = MessageDigest.getInstance("SHA-1");
      byte[] digest = md.digest(content.toString().getBytes());
      tmpStr = byteToStr(digest);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    content = null;
    return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
  }
  
  private String byteToStr(byte[] byteArray) { String strDigest = "";
    for (int i = 0; i < byteArray.length; i++) {
      strDigest = strDigest + byteToHexStr(byteArray[i]);
    }
    return strDigest;
  }
  
  private String byteToHexStr(byte mByte) { char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    char[] tempArr = new char[2];
    tempArr[0] = Digit[(mByte >>> 4 & 0xF)];
    tempArr[1] = Digit[(mByte & 0xF)];
    
    String s = new String(tempArr);
    return s;
  }
}
