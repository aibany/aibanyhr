package com.aibany.weixin.service;

import com.agileai.util.CryptionUtil;
import com.agileai.util.StringUtil;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class StringCompressTool
{
  private static HashMap<Integer, String> dataMap = new HashMap();
  
  public static int put(String value)
  {
    if (StringUtil.isNotEmpty(value)) {
      int code = 0;
      for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
        String old = (String)entry.getValue();
        if (old.equals(value)) {
          code = ((Integer)entry.getKey()).intValue();
        }
      }
      if (code == 0) {
        code = random();
      }
      dataMap.put(Integer.valueOf(code), value);
      return code;
    }
    return 0;
  }
  
  public static String get(int key)
  {
    if (key != 0) {
      String value = (String)dataMap.get(Integer.valueOf(key));
      return value;
    }
    return null;
  }
  
  private static int random() {
    int max = 9999;
    int min = 1000;
    Random random = new Random();
    int s = random.nextInt(max) % (max - min + 1) + min;
    return s;
  }
  
  public static String mapImageUrl(String location, String name)
  {
    if ((StringUtil.isNotEmpty(name)) && (name.indexOf('(') != -1)) {
      name = name.substring(0, name.indexOf('('));
    } else {
      name = "位置未知";
    }
    
    StringBuilder sb = new StringBuilder();
    sb.append("http://api.map.baidu.com/staticimage/v2?ak=UjymBYD3091SXF9ZW5FwpDlG&center=");
    sb.append(location);
    sb.append("&width=380&height=200&zoom=15&scale=2&markers=|");
    sb.append(location);
    sb.append("&markerStyles=l,,red&labels=|");
    sb.append(location);
    sb.append("&labelStyles=" + name + ",0,30,0xFF5151,0xFFFFFF,0");
    return sb.toString();
  }
  
  public static void main(String[] args)
  {
    String userId = "oIa2sxN9Am8DgpglgJoMeOuVXiWQ";
    int code = put(userId);
    String dest = get(code);
    System.out.println("code:" + code + " dest:" + dest);
    
    System.out.println(encodePassword("qaz1234567"));
  }
  
  public static String encodePassword(String password)
  {
    if (password == null) {
      return null;
    }
    String secret = CryptionUtil.encryption(password, "12345678");
    return secret;
  }
  
  public static String decodePassword(String password) {
    if (password == null) {
      return null;
    }
    String secret = CryptionUtil.decryption(password, "12345678");
    return secret;
  }
}
