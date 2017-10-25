package com.aibany.weixin.tool;

import com.aibany.weixin.model.Constans;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class LocationHelper
{
  public static String getAddress(String latitude, String longitude)
    throws Exception
  {
    String result = null;
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    label257:
    try { String url = "http://api.map.baidu.com/telematics/v3/reverseGeocoding?location=" + longitude + "," + latitude + "&output=json&coord_type=bd09ll&ak=" + Constans.Configs.BAIDU_KEY;
      
      System.out.println("baidu reverse geo:" + url);
      HttpGet get = HttpClientManager.getGetMethod(url);
      httpClient = HttpClientManager.getSSLHttpClient();
      response = httpClient.execute(get);
      String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
      JSONObject jsonObject = new JSONObject(jsonStr);
      if (jsonObject != null) {
        String status = jsonObject.getString("status");
        if ("Success".equals(status)) {
          result = jsonObject.getString("description");
          if (jsonObject.getJSONArray("results") != null) {
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            if (jsonArray.length() <= 1) break label257;
            JSONObject object = jsonArray.getJSONObject(1);
            String addr = object.getString("address");
            String name = object.getString("name");
            result = name + "(" + addr + ")";
          }
        }
      }
    }
    finally {
      if (response != null) {
        response.close();
      }
      if (httpClient != null) {
        httpClient.close();
      }
    }
    return result;
  }
  
  public static List<String> getAddressList(String latitude, String longitude) throws Exception
  {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    List<String> list = new ArrayList();
    try {
      String url = "http://api.map.baidu.com/telematics/v3/reverseGeocoding?location=" + longitude + "," + latitude + "&output=json&coord_type=bd09ll&ak=" + Constans.Configs.BAIDU_KEY;
      
      System.out.println("baidu reverse geo:" + url);
      HttpGet get = HttpClientManager.getGetMethod(url);
      httpClient = HttpClientManager.getSSLHttpClient();
      response = httpClient.execute(get);
      String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
      JSONObject jsonObject = new JSONObject(jsonStr);
      if (jsonObject != null) {
        String status = jsonObject.getString("status");
        if (("Success".equals(status)) && 
          (jsonObject.getJSONArray("results") != null)) {
          JSONArray jsonArray = jsonObject.getJSONArray("results");
          for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String addr = object.getString("address");
            String name = object.getString("name");
            list.add(name + "(" + addr + ")");
          }
        }
      }
    }
    finally {
      if (response != null) {
        response.close();
      }
      if (httpClient != null) {
        httpClient.close();
      }
    }
    return list;
  }
}
