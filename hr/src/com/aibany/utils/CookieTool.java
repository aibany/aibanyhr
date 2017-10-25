package com.aibany.utils;

import com.agileai.util.StringUtil;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieTool
{
  public static void addCookie(HttpServletResponse response, String key, String value, int ageSeconds)
  {
    Cookie cookie = new Cookie(key, value);
    cookie.setPath("/");
    if (ageSeconds > 0) cookie.setMaxAge(ageSeconds);
    response.addCookie(cookie);
  }
  
  public static void removeCookie(HttpServletResponse response, String key)
  {
    addCookie(response, key, null, 0);
  }
  
  public static String getCookieValueByName(HttpServletRequest request, String name)
  {
    Cookie cookie = getCookieByName(request, name);
    if (cookie != null) {
      return cookie.getValue();
    }
    return null;
  }
  
  public static boolean isWeiXinBrowser(HttpServletRequest request) {
    String agent = request.getHeader("User-Agent");
    if ((StringUtil.isNotEmpty(agent)) && (agent.toLowerCase().contains("micromessenger"))) {
      return true;
    }
    return false;
  }
  
  public static Cookie getCookieByName(HttpServletRequest request, String name) {
    Map<String, Cookie> cookieMap = ReadCookieMap(request);
    if (cookieMap.containsKey(name)) {
      Cookie cookie = (Cookie)cookieMap.get(name);
      return cookie;
    }
    return null;
  }
  
  private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request)
  {
    Map<String, Cookie> cookieMap = new HashMap();
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        cookieMap.put(cookie.getName(), cookie);
      }
    }
    return cookieMap;
  }
}
