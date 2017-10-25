package com.aibany.hr.module.attendance.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.common.HttpClientHelper;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.cxmodule.HrAttendanceManage;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class MobileAttendanceHandler extends CommonHandler
{
  @PageAction
  public ViewRenderer findAroundBuilding(DataParam param)
  {
    String responseText = null;
    try {
      String location = param.get("location");
      String url = "http://api.map.baidu.com/place/v2/search?ak=j6x1iGtGaxQcxaEDgbHRKDUp&output=json&query=%E6%88%BF%E5%AD%90&page_size=10&page_num=0&scope=1&location=" + location + "&radius=1000";
      System.out.println("url is " + url);
      
      HttpClientHelper httpClientHelper = new HttpClientHelper();
      String jsonStr = httpClientHelper.retrieveGetReponseText(url);
      JSONObject jsonObject = new JSONObject(jsonStr);
      responseText = jsonObject.toString();
    } catch (Exception ex) {
      this.log.error(ex.getLocalizedMessage(), ex);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer signIn(DataParam param) {
    String responseText = "fail";
    try {
      String inputString = getInputString();
      JSONObject jsonObject = new JSONObject(inputString);
      User user = (User)getUser();
      String userId = user.getUserId();
      String atdDate = DateUtil.getDateByType(9, new Date());
      String atdInTime = DateUtil.getDateByType(11, new Date());
      String atdInPlace = jsonObject.getString("address");
      String house = jsonObject.getString("name");
      String lng = jsonObject.getString("lng");
      String lat = jsonObject.getString("lat");
      JSONObject jsonObject1 = new JSONObject();
      jsonObject1.put("lng", lng);
      jsonObject1.put("lat", lat);
      String coordinate = jsonObject1.toString();
      DataParam createparam = new DataParam(new Object[] { "USER_ID", userId, "ATD_DATE", atdDate, "ATD_IN_TIME", atdInTime, 
        "ATD_IN_COORDINATE", coordinate, "ATD_IN_PLACE", atdInPlace, "ATD_IN_HOUSE", house });
      
      getService().createRecord(createparam);
      responseText = "success";
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer getSignInInfo(DataParam param) {
    String responseText = null;
    try {
      JSONObject jsonObject = new JSONObject();
      User user = (User)getUser();
      String userId = user.getUserId();
      String atdDate = DateUtil.getDateByType(9, new Date());
      param.put("currentUser", userId);
      param.put("currentDate", atdDate);
      DataRow dataRow = getService().getRecord(param);
      if (dataRow == null) {
        jsonObject.put("isSignIn", "N");
        jsonObject.put("atdInTime", DateUtil.getDateByType(11, new Date()));
      } else {
        jsonObject.put("isSignIn", "Y");
        Date atdInTime = (Date)dataRow.get("ATD_IN_TIME");
        jsonObject.put("atdInTime", DateUtil.getDateByType(11, atdInTime));
        jsonObject.put("name", dataRow.get("ATD_IN_HOUSE"));
        jsonObject.put("address", dataRow.get("ATD_IN_PLACE"));
        JSONObject jsonObject1 = new JSONObject(dataRow.getString("ATD_IN_COORDINATE"));
        jsonObject.put("placeInfo", jsonObject1);
      }
      
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer signOut(DataParam param) {
    String responseText = "fail";
    try {
      String inputString = getInputString();
      JSONObject jsonObject = new JSONObject(inputString);
      
      User user = (User)getUser();
      String userId = user.getUserId();
      String atdDate = DateUtil.getDateByType(9, new Date());
      param.put("currentUser", userId);
      param.put("currentDate", atdDate);
      DataRow dataRow = getService().getRecord(param);
      String atdId = (String)dataRow.get("ATD_ID");
      
      String atdOutTime = DateUtil.getDateByType(11, new Date());
      String atdOutPlace = jsonObject.getString("address");
      String house = jsonObject.getString("name");
      String lng = jsonObject.getString("lng");
      String lat = jsonObject.getString("lat");
      JSONObject jsonObject1 = new JSONObject();
      jsonObject1.put("lng", lng);
      jsonObject1.put("lat", lat);
      String coordinate = jsonObject1.toString();
      
      DataParam updateparam = new DataParam(new Object[] { "ATD_ID", atdId, "ATD_OUT_TIME", atdOutTime, "ATD_OUT_PLACE", atdOutPlace, 
        "ATD_OUT_COORDINATE", coordinate, "ATD_OUT_HOUSE", house });
      
      getService().updateRecord(updateparam);
      responseText = "success";
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer getSignOutInfo(DataParam param) {
    String responseText = null;
    try {
      JSONObject jsonObject = new JSONObject();
      User user = (User)getUser();
      String userId = user.getUserId();
      String atdDate = DateUtil.getDateByType(9, new Date());
      param.put("currentUser", userId);
      param.put("currentDate", atdDate);
      DataRow dataRow = getService().getRecord(param);
      if (dataRow == null) {
        jsonObject.put("isSignInOpera", "N");
      } else if (dataRow.get("ATD_OUT_TIME") != null) {
        jsonObject.put("isSignInOpera", "Y");
        jsonObject.put("isSignOut", "Y");
        Date atdOutTime = (Date)dataRow.get("ATD_OUT_TIME");
        jsonObject.put("atdOutTime", DateUtil.getDateByType(11, atdOutTime));
        jsonObject.put("address", dataRow.get("ATD_OUT_PLACE"));
        jsonObject.put("name", dataRow.get("ATD_OUT_HOUSE"));
        if (StringUtil.isNotNullNotEmpty(dataRow.getString("ATD_OUT_COORDINATE"))) {
          JSONObject jsonObject1 = new JSONObject(dataRow.getString("ATD_OUT_COORDINATE"));
          jsonObject.put("placeInfo", jsonObject1);
        }
      } else {
        jsonObject.put("isSignInOpera", "Y");
        jsonObject.put("isSignOut", "N");
        jsonObject.put("atdOutTime", DateUtil.getDateByType(11, new Date()));
        if (StringUtil.isNotNullNotEmpty(dataRow.getString("ATD_OUT_COORDINATE"))) {
          JSONObject jsonObject1 = new JSONObject(dataRow.getString("ATD_OUT_COORDINATE"));
          jsonObject.put("placeInfo", jsonObject1);
        }
      }
      
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer location(DataParam param) {
    String responseText = "fail";
    try {
      String inputString = getInputString();
      JSONObject jsonObject = new JSONObject(inputString);
      User user = (User)getUser();
      
      String userId = user.getUserId();
      String locatTime = DateUtil.getDateByType(11, new Date());
      String locatPlace = jsonObject.getString("address");
      String locatHouse = jsonObject.getString("name");
      
      DataParam createparam = new DataParam(new Object[] { "USER_ID", userId, "LOCAT_TIME", locatTime, "LOCAT_PLACE", locatPlace, "LOCAT_HOUSE", locatHouse });
      
      getService().createLocationRecord(createparam);
      responseText = "success";
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer getLocationInfo(DataParam param) {
    String responseText = null;
    try {
      JSONObject jsonObject = new JSONObject();
      String locatTime = DateUtil.getDateByType(11, new Date());
      jsonObject.put("locatTime", locatTime);
      
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer findCurrentDaySigninInfos(DataParam param) {
    String responseText = null;
    try {
      String currentDate = DateUtil.getDateByType(9, new Date());
      String weekText = DateUtil.getWeekText(new Date());
      List<DataRow> records = getService().findCurrentDaySigninInfos(currentDate);
      String signInDateShow = currentDate.substring(5, 10);
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();
      if (records.size() != 0) {
        for (int i = 0; i < records.size(); i++) {
          DataRow row = (DataRow)records.get(i);
          JSONObject jsonObject11 = new JSONObject();
          jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
          String inTime = row.stringValue("ATD_IN_TIME");
          if (!inTime.isEmpty()) {
            jsonObject11.put("inTime", inTime.substring(11, 16));
          } else {
            jsonObject11.put("inTime", inTime);
          }
          jsonObject11.put("inPlace", row.stringValue("ATD_IN_PLACE"));
          jsonObject11.put("inHouse", row.stringValue("ATD_IN_HOUSE"));
          jsonArray.put(jsonObject11);
        }
      } else {
        JSONObject jsonObject11 = new JSONObject();
        jsonObject11.put("name", "无记录");
        jsonArray.put(jsonObject11);
      }
      
      jsonObject.put("signInfos", jsonArray);
      jsonObject.put("signInDate", currentDate);
      jsonObject.put("signInDateShow", signInDateShow);
      jsonObject.put("weekText", weekText);
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer findLastDaySigninInfos(DataParam param) {
    String responseText = null;
    try {
      String date = param.get("date");
      if (("null".equals(date)) || (date == null)) {
        date = DateUtil.getDateByType(9, DateUtil.getDateAdd(new Date(), 1, -1));
      } else {
        date = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDateTime(date), 1, -1));
      }
      String weekText = DateUtil.getWeekText(DateUtil.getDateTime(date));
      List<DataRow> records = getService().findCurrentDaySigninInfos(date);
      String signInDateShow = date.substring(5, 10);
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();
      if (records.size() != 0) {
        for (int i = 0; i < records.size(); i++) {
          DataRow row = (DataRow)records.get(i);
          JSONObject jsonObject11 = new JSONObject();
          jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
          String inTime = row.stringValue("ATD_IN_TIME");
          if (!inTime.isEmpty()) {
            inTime = DateUtil.getDateByType(10, row.getTimestamp("ATD_IN_TIME"));
            inTime = inTime.substring(11, 16).replaceAll("/", "-");
            jsonObject11.put("inTime", inTime);
          } else {
            jsonObject11.put("inTime", inTime);
          }
          jsonObject11.put("inPlace", row.stringValue("ATD_IN_PLACE"));
          jsonObject11.put("inHouse", row.stringValue("ATD_IN_HOUSE"));
          jsonArray.put(jsonObject11);
        }
      } else {
        JSONObject jsonObject11 = new JSONObject();
        jsonObject11.put("name", "无记录");
        jsonArray.put(jsonObject11);
      }
      
      jsonObject.put("signInfos", jsonArray);
      jsonObject.put("signInDate", date);
      jsonObject.put("signInDateShow", signInDateShow);
      jsonObject.put("weekText", weekText);
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer findFollowDaySigninInfos(DataParam param) {
    String responseText = null;
    try {
      String date = param.get("date");
      if (("null".equals(date)) || (date == null)) {
        date = DateUtil.getDateByType(9, DateUtil.getDateAdd(new Date(), 1, 1));
      } else {
        date = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDateTime(date), 1, 1));
      }
      String weekText = DateUtil.getWeekText(DateUtil.getDateTime(date));
      List<DataRow> records = getService().findCurrentDaySigninInfos(date);
      String signInDateShow = date.substring(5, 10);
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();
      if (records.size() != 0) {
        for (int i = 0; i < records.size(); i++) {
          DataRow row = (DataRow)records.get(i);
          JSONObject jsonObject11 = new JSONObject();
          jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
          String inTime = row.stringValue("ATD_IN_TIME");
          if (!inTime.isEmpty()) {
            inTime = DateUtil.getDateByType(10, row.getTimestamp("ATD_IN_TIME"));
            inTime = inTime.substring(11, 16).replaceAll("/", "-");
            jsonObject11.put("inTime", inTime);
          } else {
            jsonObject11.put("inTime", inTime);
          }
          jsonObject11.put("inPlace", row.stringValue("ATD_IN_PLACE"));
          jsonObject11.put("inHouse", row.stringValue("ATD_IN_HOUSE"));
          jsonArray.put(jsonObject11);
        }
      } else {
        JSONObject jsonObject11 = new JSONObject();
        jsonObject11.put("name", "无记录");
        jsonArray.put(jsonObject11);
      }
      
      jsonObject.put("signInfos", jsonArray);
      jsonObject.put("signInDate", date);
      jsonObject.put("signInDateShow", signInDateShow);
      jsonObject.put("weekText", weekText);
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer findLastDaySignOutInfos(DataParam param)
  {
    String responseText = null;
    try {
      String date = param.get("date");
      if (("null".equals(date)) || (date == null)) {
        date = DateUtil.getDateByType(9, DateUtil.getDateAdd(new Date(), 1, -1));
      } else {
        date = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDateTime(date), 1, -1));
      }
      String signOutDateShow = date.substring(5, 10);
      String weekText = DateUtil.getWeekText(DateUtil.getDateTime(date));
      String expression = "and ATD_OUT_TIME is not null";
      List<DataRow> records = getService().findCurrentDaySignOutInfos(expression, date);
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();
      if (records.size() != 0) {
        for (int i = 0; i < records.size(); i++) {
          DataRow row = (DataRow)records.get(i);
          JSONObject jsonObject11 = new JSONObject();
          jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
          String outTime = row.stringValue("ATD_OUT_TIME");
          if (!outTime.isEmpty()) {
            outTime = DateUtil.getDateByType(10, row.getTimestamp("ATD_OUT_TIME"));
            outTime = outTime.substring(11, 16).replaceAll("/", "-");
            jsonObject11.put("outTime", outTime);
          } else {
            jsonObject11.put("outTime", outTime);
          }
          jsonObject11.put("outPlace", row.stringValue("ATD_OUT_PLACE"));
          jsonObject11.put("outHouse", row.stringValue("ATD_OUT_HOUSE"));
          jsonArray.put(jsonObject11);
        }
      } else {
        JSONObject jsonObject11 = new JSONObject();
        jsonObject11.put("name", "无记录");
        jsonArray.put(jsonObject11);
      }
      
      jsonObject.put("signOutInfos", jsonArray);
      jsonObject.put("signOutDate", date);
      jsonObject.put("signOutDateShow", signOutDateShow);
      jsonObject.put("outWeekText", weekText);
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer findCurrentDaySignOutInfos(DataParam param) {
    String responseText = null;
    try {
      String currentDate = DateUtil.getDateByType(9, new Date());
      String weekText = DateUtil.getWeekText(new Date());
      String expression = "and ATD_OUT_TIME is not null";
      List<DataRow> records = getService().findCurrentDaySignOutInfos(expression, currentDate);
      String signOutDateShow = currentDate.substring(5, 10);
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();
      if (records.size() != 0) {
        for (int i = 0; i < records.size(); i++) {
          DataRow row = (DataRow)records.get(i);
          JSONObject jsonObject11 = new JSONObject();
          jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
          String outTime = row.stringValue("ATD_OUT_TIME");
          if (!outTime.isEmpty()) {
            jsonObject11.put("outTime", outTime.substring(11, 16));
          } else {
            jsonObject11.put("outTime", outTime);
          }
          jsonObject11.put("outPlace", row.stringValue("ATD_OUT_PLACE"));
          jsonObject11.put("outHouse", row.stringValue("ATD_OUT_HOUSE"));
          jsonArray.put(jsonObject11);
        }
      } else {
        JSONObject jsonObject11 = new JSONObject();
        jsonObject11.put("name", "无记录");
        jsonArray.put(jsonObject11);
      }
      
      jsonObject.put("signOutInfos", jsonArray);
      jsonObject.put("signOutDate", currentDate);
      jsonObject.put("signOutDateShow", signOutDateShow);
      jsonObject.put("outWeekText", weekText);
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer findFollowDaySignOutInfos(DataParam param) {
    String responseText = null;
    try {
      String date = param.get("date");
      if (("null".equals(date)) || (date == null)) {
        date = DateUtil.getDateByType(9, DateUtil.getDateAdd(new Date(), 1, 1));
      } else {
        date = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDateTime(date), 1, 1));
      }
      String weekText = DateUtil.getWeekText(DateUtil.getDateTime(date));
      List<DataRow> records = getService().findCurrentDaySigninInfos(date);
      String signOutDateShow = date.substring(5, 10);
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();
      if (records.size() != 0) {
        for (int i = 0; i < records.size(); i++) {
          DataRow row = (DataRow)records.get(i);
          JSONObject jsonObject11 = new JSONObject();
          jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
          String outTime = row.stringValue("ATD_OUT_TIME");
          if (!outTime.isEmpty()) {
            outTime = DateUtil.getDateByType(10, row.getTimestamp("ATD_OUT_TIME"));
            outTime = outTime.substring(11, 16).replaceAll("/", "-");
            jsonObject11.put("outTime", outTime);
          } else {
            jsonObject11.put("outTime", outTime);
          }
          jsonObject11.put("outPlace", row.stringValue("ATD_OUT_PLACE"));
          jsonObject11.put("outHouse", row.stringValue("ATD_OUT_HOUSE"));
          jsonArray.put(jsonObject11);
        }
      } else {
        JSONObject jsonObject11 = new JSONObject();
        jsonObject11.put("name", "无记录");
        jsonArray.put(jsonObject11);
      }
      
      jsonObject.put("signOutInfos", jsonArray);
      jsonObject.put("signOutDate", date);
      jsonObject.put("signOutDateShow", signOutDateShow);
      jsonObject.put("outWeekText", weekText);
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer findSignLocationInfos(DataParam param)
  {
    String responseText = null;
    try {
      User user = (User)getUser();
      String userId = param.get("userId");
      if ("null".equals(userId)) {
        userId = user.getUserId();
      }
      List<DataRow> records = getService().findSignLocationInfos(userId);
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();
      if (records.size() != 0) {
        for (int i = 0; i < records.size(); i++) {
          DataRow row = (DataRow)records.get(i);
          JSONObject jsonObject11 = new JSONObject();
          String time = row.stringValue("LOCAT_TIME");
          String weekText = DateUtil.getWeekText(DateUtil.getDateTime(time.substring(0, 10)));
          if (!time.isEmpty()) {
            jsonObject11.put("time", time.substring(0, 16));
          } else {
            jsonObject11.put("time", time);
          }
          jsonObject11.put("place", row.stringValue("LOCAT_PLACE"));
          jsonObject11.put("weekText", weekText);
          jsonArray.put(jsonObject11);
        }
      } else {
        JSONObject jsonObject11 = new JSONObject();
        jsonObject11.put("time", "无记录");
        jsonArray.put(jsonObject11);
      }
      
      jsonObject.put("locationInfos", jsonArray);
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer findUserInfos(DataParam param) {
    String responseText = null;
    try {
      String curUserCodes = param.get("curUserCodes");
      String userCodes = "";
      List<String> userList = new ArrayList();
      addArrayToList(userList, curUserCodes.split(","));
      for (int i = 0; i < userList.size(); i++) {
        String userCode = (String)userList.get(i);
        if (i == userList.size() - 1) {
          userCodes = userCodes + userCode;
        } else {
          userCodes = userCodes + userCode + ",";
        }
      }
      List<DataRow> records = getService().findUserInfos(userCodes);
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();
      JSONArray jsonArray2 = new JSONArray();
      if (records.size() != 0) {
        for (int i = 0; i < records.size(); i++) {
          DataRow row = (DataRow)records.get(i);
          JSONObject jsonObject11 = new JSONObject();
          jsonObject11.put("userId", row.stringValue("USER_ID"));
          jsonObject11.put("userName", row.stringValue("USER_NAME"));
          jsonObject11.put("userCode", row.stringValue("USER_CODE"));
          jsonArray.put(jsonObject11);
          jsonArray2.put(row.stringValue("USER_CODE"));
        }
      } else {
        JSONObject jsonObject11 = new JSONObject();
        jsonObject11.put("userName", "无记录");
        jsonArray.put(jsonObject11);
      }
      jsonObject.put("userInfos", jsonArray);
      jsonObject.put("userCodes", jsonArray2);
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer findActiveUserId(DataParam param) {
    String responseText = "fail";
    try {
      String userCode = param.get("userCode");
      DataRow userInfo = getService().findActiveUserId(userCode);
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("userId", userInfo.stringValue("USER_ID"));
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  public static List addArrayToList(List list, Object[] objects)
  {
    List temp = arrayToList(objects);
    list.addAll(temp);
    return list;
  }
  
  public static List arrayToList(Object[] objects) {
    List list = new ArrayList();
    for (int i = 0; i < objects.length; i++) {
      String object = "'" + objects[i] + "'";
      list.add(object);
    }
    return list;
  }
  
  protected HrAttendanceManage getService() { return (HrAttendanceManage)lookupService(HrAttendanceManage.class); }
}
