package com.aibany.weixin.tool;

import com.aibany.weixin.model.Constans;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;

public class SecurityAuthHelper
{
  private static String TokenIdKey = "tokenId";
  private static String CacheTimeKey = "cacheTime";
  private static HashMap<String, Object> AccessTokenCache = new HashMap();
  
  public static String getOpenId(String code) throws Exception {
    String openId = null;
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    try {
      String appId = Constans.Configs.APPID;
      String appSecret = Constans.Configs.APPSECRET;
      String openid_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
      httpClient = HttpClientManager.getSSLHttpClient();
      HttpGet get = HttpClientManager.getGetMethod(openid_url);
      response = httpClient.execute(get);
      String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
      JSONObject jsonObject = new JSONObject(jsonStr);
      openId = jsonObject.getString("openid");
    } finally {
      if (response != null) {
        response.close();
      }
      if (httpClient != null) {
        httpClient.close();
      }
    }
    return openId;
  }
  
  public static String getAccessToken() throws Exception {
    String result = null;
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    try {
      String appId = Constans.Configs.APPID;
      String appSecret = Constans.Configs.APPSECRET;
      if (!AccessTokenCache.containsKey("tokenId")) {
        HttpGet get = HttpClientManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret);
        httpClient = HttpClientManager.getSSLHttpClient();
        response = httpClient.execute(get);
        String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
        JSONObject jsonObject = new JSONObject(jsonStr);
        result = jsonObject.getString("access_token");
        
        AccessTokenCache.put(TokenIdKey, result);
        AccessTokenCache.put(CacheTimeKey, new Date());
      } else {
        Date cacheTime = (Date)AccessTokenCache.get(CacheTimeKey);
        
        long currentTime = System.currentTimeMillis();
        long cacheCreateTime = cacheTime.getTime();
        long diffTime = currentTime - cacheCreateTime;
        int cacheMinutes = Integer.parseInt(Constans.Configs.ACCESSTOKEN_CACHE_MINUTES);
        long cacheTimeRange = cacheMinutes * 60 * 1000;
        
        if (diffTime < cacheTimeRange) {
          result = (String)AccessTokenCache.get(TokenIdKey);
        } else {
          httpClient = HttpClientManager.getSSLHttpClient();
          HttpGet get = HttpClientManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret);
          response = httpClient.execute(get);
          String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
          JSONObject jsonObject = new JSONObject(jsonStr);
          result = jsonObject.getString("access_token");
          
          AccessTokenCache.put(TokenIdKey, result);
          AccessTokenCache.put(CacheTimeKey, new Date());
        }
      }
    } finally {
      if (response != null) {
        response.close();
      }
      if (httpClient != null) {
        httpClient.close();
      }
    }
    return result;
  }
  
  public static String getJsapiTicket(String accessToken) throws Exception {
    String result = null;
    String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    try {
      httpClient = HttpClientManager.getSSLHttpClient();
      HttpGet get = HttpClientManager.getGetMethod(url);
      response = httpClient.execute(get);
      String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
      JSONObject jsonObject = new JSONObject(jsonStr);
      if ("ok".equals(jsonObject.get("errmsg"))) {
        result = jsonObject.getString("ticket");
      }
    } finally {
      if (response != null) {
        response.close();
      }
      if (httpClient != null) {
        httpClient.close();
      }
    }
    return result;
  }
  
  public static String signJsApi(String jsapi_ticket, String nonceStr, String timestamp, String url) {
    String signature = null;
    
    String string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
    try {
      MessageDigest crypt = MessageDigest.getInstance("SHA-1");
      crypt.reset();
      crypt.update(string1.getBytes("UTF-8"));
      signature = byteToHex(crypt.digest());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return signature;
  }
  
  private static String byteToHex(byte[] hash) {
    Formatter formatter = new Formatter();
    byte[] arrayOfByte = hash;int j = hash.length; for (int i = 0; i < j; i++) { byte b = arrayOfByte[i];
      formatter.format("%02x", new Object[] { Byte.valueOf(b) });
    }
    String result = formatter.toString();
    formatter.close();
    return result;
  }
}
