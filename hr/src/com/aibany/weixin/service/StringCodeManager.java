package com.aibany.weixin.service;

import com.aibany.weixin.model.Constans;
import com.aibany.weixin.model.TextMessage;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map;

public class StringCodeManager
{
  public static TextMessage parseCode(String code, Map<String, String> requestMap)
  {
    String fromUserName = (String)requestMap.get("FromUserName");
    String toUserName = (String)requestMap.get("ToUserName");
    String EventKey = (String)requestMap.get("EventKey");
    String Event = (String)requestMap.get("Event");
    String content = (String)requestMap.get("Content");
    
    StringBuffer contentMsg = new StringBuffer();
    
    if ("signin".equals(EventKey)) {
      contentMsg.append("<a href='" + Constans.Configs.HOSTS + "/hr/index?WxSignIn&openId=" + fromUserName + "'>签到请点击</a>");
    } else if ("signout".equals(EventKey)) {
      contentMsg.append("<a href='" + Constans.Configs.HOSTS + "/hr/index?WxSignOut&openId=" + fromUserName + "'>签退请点击</a>");
    } else if ("bind".equals(EventKey)) {
      contentMsg.append("<a href='" + Constans.Configs.HOSTS + "/hr/index?WxBind&openId=" + fromUserName + "'>绑定工号请点击</a>");
    }
    
    System.out.println("wx响应串:" + contentMsg.toString());
    
    TextMessage textMessage = new TextMessage();
    textMessage.setToUserName(fromUserName);
    textMessage.setFromUserName(toUserName);
    textMessage.setCreateTime(new Date().getTime());
    textMessage.setMsgType("text");
    textMessage.setFuncFlag(0);
    textMessage.setContent(contentMsg.toString());
    return textMessage;
  }
}
