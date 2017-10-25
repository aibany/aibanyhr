package com.aibany.weixin.core;

import com.aibany.weixin.model.Constans;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MessageEventHandler
{
  protected static final HashMap<String, HashMap<String, Object>> LocationCache = new HashMap();
  
  protected HttpServlet brokerServlet = null;
  protected HttpServletRequest request = null;
  protected HttpServletResponse response = null;
  
  public void setContextProperties(HttpServlet brokerServlet, HttpServletRequest request, HttpServletResponse response)
  {
    this.brokerServlet = brokerServlet;
    this.request = request;
    this.response = response;
  }
  
  public static HashMap<String, Object> getLocation(String openId) {
    HashMap<String, Object> result = null;
    HashMap<String, Object> row = (HashMap)LocationCache.get(openId);
    if (row != null) {
      Date receiveTime = (Date)row.get("receiveTime");
      long currentTime = System.currentTimeMillis();
      long cacheCreateTime = receiveTime.getTime();
      long diffTime = currentTime - cacheCreateTime;
      int cacheMinutes = Integer.parseInt(Constans.Configs.LOCAION_CACHE_MINUTES);
      long cacheTimeRange = cacheMinutes * 60 * 1000;
      if (diffTime < cacheTimeRange) {
        result = row;
      }
    }
    return result;
  }
  
  public String handleQrCodeEvent(Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleSubscribe(Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleUnsubscribe(Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleMenuClickEvent(String eventKey, Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleMenuViewEvent(String eventKey, Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleLocationEvent(Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleTextMessage(Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleImageMessage(Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleVoiceMessage(Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleVideoMessage(Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleLocationMessage(Map<String, String> requestMap) {
    String result = null;
    return result;
  }
  
  public String handleLinkMessage(Map<String, String> requestMap) {
    String result = null;
    return result;
  }
}
