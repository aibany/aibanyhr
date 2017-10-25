package com.aibany.weixin.service;

import com.agileai.domain.DataParam;
import com.aibany.hr.common.CommonHandler;
import com.aibany.utils.HTTPRequest;
import com.aibany.weixin.core.MessageEventHandler;
import com.aibany.weixin.model.Constans;
import com.aibany.weixin.model.TextMessage;
import com.aibany.weixin.tool.MessageBuilder;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BizMessageEventHandler
  extends MessageEventHandler
{
  protected static List<String> RecognizableText = new ArrayList();
  
  public BizMessageEventHandler() {
    if (RecognizableText.isEmpty()) {
      RecognizableText.add("signin");
      RecognizableText.add("signout");
      RecognizableText.add("bind");
    }
  }
  
  public String handleSubscribe(Map<String, String> requestMap) {
    String result = null;
    
    String fromUserName = (String)requestMap.get("FromUserName");
    String toUserName = (String)requestMap.get("ToUserName");
    
    TextMessage textMessage = new TextMessage();
    textMessage.setToUserName(fromUserName);
    textMessage.setFromUserName(toUserName);
    textMessage.setCreateTime(new Date().getTime());
    textMessage.setMsgType("text");
    textMessage.setFuncFlag(0);
    
    StringBuffer contentMsg = new StringBuffer();
    contentMsg.append("感谢关注！您可以通过此公众号进行签到签退，了解周边信息等！商业合作请联系微信liulibo99\n");
    contentMsg.append("欢迎访问<a href=\"http://www.aibany.com\">电脑网站</a>。");
    
    textMessage.setContent(contentMsg.toString());
    result = MessageBuilder.textMessageToXml(textMessage);
    
    return result;
  }
  
  public String handleTextMessage(Map<String, String> requestMap) {
    String result = null;
    String content = (String)requestMap.get("Content");
    String Event = (String)requestMap.get("Event");
    if ("CLICK".equals(Event)) {
      content = (String)requestMap.get("EventKey");
    }
    System.out.println("content:" + content + " event:" + Event);
    if (RecognizableText.contains(content))
    {
      TextMessage textMessage = StringCodeManager.parseCode(content, requestMap);
      result = MessageBuilder.textMessageToXml(textMessage);
    }
    else {
      String fromUserName = (String)requestMap.get("FromUserName");
      String toUserName = (String)requestMap.get("ToUserName");
      TextMessage textMessage = new TextMessage();
      textMessage.setToUserName(fromUserName);
      textMessage.setFromUserName(toUserName);
      textMessage.setCreateTime(new Date().getTime());
      textMessage.setMsgType("text");
      textMessage.setFuncFlag(0);
      
      StringBuffer contentMsg = new StringBuffer();
      contentMsg.append("亲，您的输入不能被识别[捂脸]");
      
      textMessage.setContent(contentMsg.toString());
      result = MessageBuilder.textMessageToXml(textMessage);
    }
    return result;
  }
  
  public String handleLocationEvent(Map<String, String> requestMap) {
    String result = null;
    
    String openId = (String)requestMap.get("FromUserName");
    double latitude = Double.parseDouble((String)requestMap.get("Latitude"));
    double longitude = Double.parseDouble((String)requestMap.get("Longitude"));
    double precision = Double.parseDouble((String)requestMap.get("Precision"));
    
    HashMap<String, Object> row = new HashMap();
    row.put("Latitude", Double.valueOf(latitude));
    row.put("Longitude", Double.valueOf(longitude));
    row.put("Precision", Double.valueOf(precision));
    row.put("receiveTime", new Date());
    LocationCache.put(openId, row);
    
    return result;
  }
  
  public String handleMenuClickEvent(String eventKey, Map<String, String> requestMap)
  {
    String content = (String)requestMap.get("Content");
    TextMessage textMessage = StringCodeManager.parseCode(content, requestMap);
    String result = MessageBuilder.textMessageToXml(textMessage);
    return result;
  }
  
  public String handleQrCodeEvent(Map<String, String> requestMap)
  {
    String fromUserName = (String)requestMap.get("FromUserName");
    String toUserName = (String)requestMap.get("ToUserName");
    String EventKey = (String)requestMap.get("EventKey");
    String ScanResult = (String)requestMap.get("ScanResult");
    Map<String, String> urlParams = HTTPRequest.URLRequest(ScanResult);
    
    StringBuffer contentMsg = new StringBuffer();
    String appId = (String)urlParams.get("s");
    
    if ((EventKey.contains("sign")) && (urlParams.size() > 0) && (Constans.Configs.APPID.equals(appId)))
    {
      String version = (String)urlParams.get("v");
      
      String geo = (String)urlParams.get("geo");
      String name = (String)urlParams.get("name");
      DataParam param = new DataParam();
      param.put("openId", fromUserName);
      String address = name + "(扫二维码)";
      param.put("address", address);
      param.put("location", geo);
      param.put("version", version);
      if (EventKey.equals("signin")) {
        WxSignInHandler signInHandler = new WxSignInHandler();
        String result = signInHandler.signInService(param, true);
        System.out.println("signinresult:" + result);
        if ("未绑定".equals(result)) {
          contentMsg.append("签到失败，请先绑定账号!");
        } else if ("未定位".equals(result)) {
          contentMsg.append("签到失败，二维码有误!");
        } else if ("重复签到".equals(result)) {
          contentMsg.append("签到失败，重复签到!");
        } else if ("夜班签到成功".equals(result)) {
          contentMsg.append("夜班签到成功！下班前记得签退哦！\n<a href='" + Constans.Configs.HOSTS + "/hr/index?WxSignIn&openId=" + fromUserName + "'>查看签到记录</a>");
        } else if ("签到成功".equals(result)) {
          contentMsg.append("签到成功！下班前记得签退哦！\n<a href='" + Constans.Configs.HOSTS + "/hr/index?WxSignIn&openId=" + fromUserName + "'>查看签到记录</a>");
        }
      } else if (EventKey.equals("signout")) {
        WxSignOutHandler outHandler = new WxSignOutHandler();
        String result = outHandler.signInService(param, true);
        System.out.println("signoutresult:" + result);
        if ("未绑定".equals(result)) {
          contentMsg.append("签退失败，请先绑定账号!");
        } else if ("未定位".equals(result)) {
          contentMsg.append("签退失败，二维码有误!");
        } else if ("重复签到".equals(result)) {
          contentMsg.append("签到失败，重复签到!");
        } else if ("未签到，无法签退".equals(result)) {
          contentMsg.append("签退失败，请先签到!");
        } else if ("夜班签到成功".equals(result)) {
          if ((param.get("ATD_OVERTIME") != null) && (Double.parseDouble(param.get("ATD_OVERTIME")) > 0.0D)) {
            contentMsg.append("夜班签退成功！您工作时间了<a>" + param.get("ATD_WORKTIME") + "</a>小时，已为您自动上报<a>" + param.get("ATD_OVERTIME") + "</a>小时加班。\n<a href='" + Constans.Configs.HOSTS + "/hr/index?WxSignOut&openId=" + fromUserName + "'>查看签退记录</a>");
          } else {
            contentMsg.append("夜班签退成功！您工作时间了<a>" + param.get("ATD_WORKTIME") + "</a>小时，\n<a href='" + Constans.Configs.HOSTS + "/hr/index?WxSignOut&openId=" + fromUserName + "'>查看签退记录</a>");
          }
        } else if ("签到成功".equals(result)) {
          if ((param.get("ATD_OVERTIME") != null) && (Double.parseDouble(param.get("ATD_OVERTIME")) > 0.0D)) {
            contentMsg.append("签退成功！您工作时间了<a>" + param.get("ATD_WORKTIME") + "</a>小时，已为您自动上报<a>" + param.get("ATD_OVERTIME") + "</a>小时加班。\n<a href='" + Constans.Configs.HOSTS + "/hr/index?WxSignOut&openId=" + fromUserName + "'>查看签退记录</a>");
          } else {
            contentMsg.append("签退成功！您工作时间了<a>" + param.get("ATD_WORKTIME") + "</a>小时，\n<a href='" + Constans.Configs.HOSTS + "/hr/index?WxSignOut&openId=" + fromUserName + "'>查看签退记录</a>");
          }
        }
      } else if (EventKey.equals("signone")) {
        WxSignOneHandler handler = new WxSignOneHandler();
        String result = handler.signInService(param);
        CommonHandler.writeSystemLog("签到结果:" + result, param.get("USER_CODE"), param.get("USER_NAME"));
        
        if ("未绑定".equals(result)) {
          contentMsg.append("签退失败，请先绑定账号!");
        } else if ("未定位".equals(result)) {
          contentMsg.append("签退失败，二维码有误!");
        } else if ("重复签到".equals(result)) {
          contentMsg.append("签到失败，重复签到! \n<a href='" + Constans.Configs.HOSTS + "/hr/index?WxSignOne'>查看考勤记录</a>\n如果考勤有误，可以在10分钟后再签到");
        } else if ("非签到时间".equals(result)) {
          contentMsg.append("签到失败，非签到时间!");
        } else if ("白班签到成功".equals(result)) {
          contentMsg.append("白班签到成功！下班前记得签退哦！");
        } else if ("夜班签到成功".equals(result)) {
          contentMsg.append("夜班签到成功！下班前记得签退哦！");
        } else if ("白班签退成功".equals(result)) {
          if ((param.get("ATD_OVERTIME") != null) && (Double.parseDouble(param.get("ATD_OVERTIME")) > 0.0D)) {
            contentMsg.append("白班签退成功！您工作时间了 " + param.get("ATD_WORKTIME") + " 小时，已为您自动上报<a>" + param.get("ATD_OVERTIME") + "</a>小时加班。");
          } else {
            contentMsg.append("白班签退成功！您工作时间了 " + param.get("ATD_WORKTIME") + " 小时。");
          }
        } else if ("夜班签退成功".equals(result)) {
          if ((param.get("ATD_OVERTIME") != null) && (Double.parseDouble(param.get("ATD_OVERTIME")) > 0.0D)) {
            contentMsg.append("夜班签退成功！您工作时间了 " + param.get("ATD_WORKTIME") + " 小时，已为您自动上报<a>" + param.get("ATD_OVERTIME") + "</a>小时加班。");
          } else {
            contentMsg.append("夜班签退成功！您工作时间了 " + param.get("ATD_WORKTIME") + " 小时。");
          }
        }
      }
    }
    if (contentMsg.length() == 0) {
      contentMsg.append("亲，您的操作无法识别[捂脸]");
    }
    
    System.out.println("wx响应串:" + contentMsg.toString());
    
    TextMessage textMessage = new TextMessage();
    textMessage.setToUserName(fromUserName);
    textMessage.setFromUserName(toUserName);
    textMessage.setCreateTime(new Date().getTime());
    textMessage.setMsgType("text");
    textMessage.setFuncFlag(0);
    textMessage.setContent(contentMsg.toString());
    
    String result = MessageBuilder.textMessageToXml(textMessage);
    return result;
  }
}
