package com.aibany.weixin.tool;

import com.aibany.weixin.model.Constans;
import java.io.PrintStream;
import java.net.URLEncoder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MenuHelper
{
  private String appId = null;
  
  public MenuHelper(String appId) {
    this.appId = appId;
  }
  
  public String createMenu(String params, String accessToken) throws Exception {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    try {
      httpClient = HttpClientManager.getSSLHttpClient();
      HttpPost httpost = HttpClientManager.getPostMethod("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken);
      httpost.setEntity(new StringEntity(params, "UTF-8"));
      response = httpClient.execute(httpost);
      String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
      JSONObject demoJson = new JSONObject(jsonStr);
      return demoJson.getString("errmsg");
    } finally {
      if (response != null) {
        response.close();
      }
      if (httpClient != null) {
        httpClient.close();
      }
    }
  }
  
  public String getMenuInfo(String accessToken) throws Exception {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    try {
      httpClient = HttpClientManager.getSSLHttpClient();
      HttpGet get = HttpClientManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + accessToken);
      response = httpClient.execute(get);
      String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
      return jsonStr;
    } finally {
      if (response != null) {
        response.close();
      }
      if (httpClient != null) {
        httpClient.close();
      }
    }
  }
  
  public String deleteMenuInfo(String accessToken) throws Exception {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    try {
      httpClient = HttpClientManager.getSSLHttpClient();
      HttpGet get = HttpClientManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + accessToken);
      response = httpClient.execute(get);
      String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
      JSONObject demoJson = new JSONObject(jsonStr);
      return demoJson.getString("errmsg");
    } finally {
      if (response != null) {
        response.close();
      }
      if (httpClient != null) {
        httpClient.close();
      }
    }
  }
  
  private String buildViewURL(String redirectURL) {
    StringBuffer sb = new StringBuffer();
    try {
      redirectURL = URLEncoder.encode(redirectURL, "utf-8");
    } catch (Exception e) {
      e.printStackTrace();
    }
    sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=").append(this.appId).append("&redirect_uri=").append(redirectURL).append("&response_type=code&scope=snsapi_base&state=1#wechat_redirect");
    return sb.toString();
  }
  
  public static void main(String[] args) throws JSONException {
    Constans.Configs.APPID = "wx92308c4ab4e3de40";
    Constans.Configs.APPSECRET = "e157a1b4587e13f0f36138a8d6d6a7b5";
    
    String appId = Constans.Configs.APPID;
    MenuHelper menuHelper = new MenuHelper(appId);
    
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    sb.append(" \"button\":[");
    sb.append("     {");
    sb.append("         \"name\":\"移动办公\",");
    sb.append("         \"sub_button\":[");
    sb.append("             {");
    sb.append("                 \"type\":\"scancode_waitmsg\",");
    sb.append("                 \"name\":\"一键打卡\",");
    sb.append("                 \"key\":\"").append("signone").append("\"");
    sb.append("             },");
    sb.append("             {");
    sb.append("                 \"type\":\"view\",");
    sb.append("                 \"name\":\"我的考勤\",");
    sb.append("                 \"url\":\"").append("http://aibany.com/hr/index?WxSignOne").append("\"");
    sb.append("             },");
    sb.append("             {");
    sb.append("                 \"type\":\"click\",");
    sb.append("                 \"name\":\"绑定工号\",");
    sb.append("                 \"key\":\"").append("bind").append("\"");
    sb.append("             }");
    sb.append("         ]");
    sb.append("     },");
    sb.append("     {");
    sb.append("         \"name\":\"系统主页\",");
    sb.append("         \"type\":\"view\",");
    sb.append("         \"url\":\"").append("http://aibany.com/hr").append("\"");
    sb.append("     },");
    sb.append("     {");
    sb.append("         \"name\":\"工具服务\",");
    sb.append("         \"sub_button\":[");
    sb.append("             {");
    sb.append("                 \"type\":\"view\",");
    sb.append("                 \"name\":\"公司公告\",");
    sb.append("                 \"url\":\"http://aibany.com/hr/index?Notice\"");
    sb.append("             },");
    sb.append("             {");
    sb.append("                 \"type\":\"scancode_waitmsg\",");
    sb.append("                 \"name\":\"扫码签到\",");
    sb.append("                 \"key\":\"").append("signin").append("\"");
    sb.append("             },");
    sb.append("             {");
    sb.append("                 \"type\":\"scancode_waitmsg\",");
    sb.append("                 \"name\":\"扫码签退\",");
    sb.append("                 \"key\":\"").append("signout").append("\"");
    sb.append("             },");
    sb.append("             {");
    sb.append("                 \"type\":\"click\",");
    sb.append("                 \"name\":\"定位签到\",");
    sb.append("                 \"key\":\"").append("signin").append("\"");
    sb.append("             },");
    sb.append("             {");
    sb.append("                 \"type\":\"click\",");
    sb.append("                 \"name\":\"定位签退\",");
    sb.append("                 \"key\":\"").append("signout").append("\"");
    sb.append("             }");
    sb.append("         ]");
    sb.append("     }");
    sb.append(" ]");
    sb.append("}");
    System.out.println(sb);
    try
    {
      String result = "";
      String accessToken = SecurityAuthHelper.getAccessToken();
//      result = menuHelper.deleteMenuInfo(accessToken);
      result = menuHelper.createMenu(sb.toString(), accessToken);
      
      System.out.println(result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
