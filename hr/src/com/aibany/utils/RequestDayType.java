package com.aibany.utils;

import com.agileai.util.StringUtil;
import java.io.PrintStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RequestDayType
{
  public static int[] dayType(String[] YYYYMMDD)
  {
    int[] type = new int[YYYYMMDD.length];
    
    String host = "http://api.goseek.cn/Tools/holiday";
    StringBuilder sb = new StringBuilder();
    sb.append("date=");
    String[] arrayOfString = YYYYMMDD;int j = YYYYMMDD.length; for (int i = 0; i < j; i++) { String days = arrayOfString[i];
      sb.append(days);
      sb.append(",");
    }
    if (sb.length() > 2) {
      sb.delete(sb.length() - 1, sb.length());
    }
    
    String s = HTTPRequest.sendGet(host, sb.toString());
    System.out.println(s);
    
    if (StringUtil.isNotEmpty(s)) {
      try {
        JSONObject jsonObject = new JSONObject(s);
        
        type[0] = jsonObject.getInt("data");
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return type;
  }
  
  public static String[] dayTypeString(String[] YYYYMMDD)
  {
    int[] type = dayType(YYYYMMDD);
    String[] strings = new String[YYYYMMDD.length];
    
    for (int i = 0; i < type.length; i++) {
      if (type[i] == 0) {
        strings[i] = "工作日";
      } else if (type[i] == 1) {
        strings[i] = "周末";
      } else if (type[i] == 2) {
        strings[i] = "节假日";
      }
    }
    return strings;
  }
  
  public static String getMinDayType(String[] YYYYMMDD)
  {
    int[] dayTypes = dayType(YYYYMMDD);
    
    int type = 2;
    for (int i = 0; i < dayTypes.length; i++) {
      int mytype = dayTypes[i];
      if (type > mytype) {
        type = mytype;
      }
    }
    String mString = "工作日";
    if (type == 0) {
      mString = "工作日";
    } else if (type == 1) {
      mString = "周末";
    } else if (type == 2) {
      mString = "节假日";
    }
    return mString;
  }
  
  public static String getTomcatWebappsPath(HttpServletRequest request)
  {
    String tomcatRoot = request.getSession().getServletContext().getRealPath("/");
    String[] foo = tomcatRoot.split("/");
    StringBuilder tomcatWebAppsBuilder = new StringBuilder();
    int i = 0;
    for (String paths : foo) {
      i++;
      if (i != foo.length) {
        tomcatWebAppsBuilder.append(paths);
        tomcatWebAppsBuilder.append("/");
      }
    }
    String tomcatWebApps = tomcatWebAppsBuilder.toString();
    return tomcatWebApps;
  }
  
  public static void main(String[] args) throws Exception {
    String[] result = dayTypeString(new String[] { "20170722" });
    System.out.println(result[0]);
  }
}
