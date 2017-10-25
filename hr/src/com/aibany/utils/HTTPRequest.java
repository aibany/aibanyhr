package com.aibany.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPRequest
{
  public static String sendGet(String url, String param)
  {
    String result = "";
    BufferedReader in = null;
    try {
      String urlNameString = url + "?" + param;
      URL realUrl = new URL(urlNameString);
      
      URLConnection connection = realUrl.openConnection();
      
      connection.setRequestProperty("accept", "*/*");
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty("user-agent", 
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      
      connection.connect();
      
      Map<String, List<String>> map = connection.getHeaderFields();
      
      for (String key : map.keySet()) {
        System.out.println(key + "--->" + map.get(key));
      }
      
      in = new BufferedReader(new InputStreamReader(
        connection.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) { 
        result = result + line;
      }
    } catch (Exception e) {
      System.out.println("发送GET请求出现异常！" + e);
      e.printStackTrace();
      
      try
      {
        if (in != null) {
          in.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (in != null) {
          in.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    return result;
  }
  
  public static String sendPost(String url, String param)
  {
    PrintWriter out = null;
    BufferedReader in = null;
    String result = "";
    try {
      URL realUrl = new URL(url);
      
      URLConnection conn = realUrl.openConnection();
      
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent", 
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      
      conn.setDoOutput(true);
      conn.setDoInput(true);
      
      out = new PrintWriter(conn.getOutputStream());
      
      out.print(param);
      
      out.flush();
      
      in = new BufferedReader(
        new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        result = result + line;
      }
    } catch (Exception e) {
      System.out.println("发送 POST 请求出现异常！" + e);
      e.printStackTrace();
      
      try
      {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return result;
  }
  
  public static String UrlPage(String strURL)
  {
    String strPage = null;
    String[] arrSplit = null;
    
    strURL = strURL.trim().toLowerCase();
    
    arrSplit = strURL.split("[?]");
    if (strURL.length() > 0)
    {
      if (arrSplit.length > 1)
      {
        if (arrSplit[0] != null)
        {
          strPage = arrSplit[0];
        }
      }
    }
    
    return strPage;
  }
  
  private static String TruncateUrlPage(String strURL)
  {
    String strAllParam = null;
    String[] arrSplit = null;
    
    strURL = strURL.trim().toLowerCase();
    
    arrSplit = strURL.split("[?]");
    if (strURL.length() > 1)
    {
      if (arrSplit.length > 1)
      {
        if (arrSplit[1] != null)
        {
          strAllParam = arrSplit[1];
        }
      }
    }
    
    return strAllParam;
  }
  
  public static Map<String, String> URLRequest(String URL)
  {
    Map<String, String> mapRequest = new HashMap();
    
    String[] arrSplit = null;
    
    String strUrlParam = TruncateUrlPage(URL);
    if (strUrlParam == null)
    {
      return mapRequest;
    }
    
    arrSplit = strUrlParam.split("[&]");
    for (String strSplit : arrSplit)
    {
      String[] arrSplitEqual = null;
      arrSplitEqual = strSplit.split("[=]");
      
      if (arrSplitEqual.length > 1)
      {
        mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
      }
      else if (arrSplitEqual[0] != "")
      {
        mapRequest.put(arrSplitEqual[0], "");
      }
    }
    
    return mapRequest;
  }
}
