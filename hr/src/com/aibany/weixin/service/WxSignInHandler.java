package com.aibany.weixin.service;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.agileai.util.ListUtil;
import com.agileai.util.MapUtil;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.cxmodule.HrAttendanceManage;
import com.aibany.weixin.tool.LocationHelper;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

public class WxSignInHandler
  extends CommonHandler
{
  public static final String wx_nobind = "未绑定";
  public static final String wx_signsuccess = "签到成功";
  public static final String wx_sign_night_success = "夜班签到成功";
  public static final String wx_noaddress = "未定位";
  public static final String wx_signrepeat = "重复签到";
  public static final String wx_nosiginout = "未签到，无法签退";
  
  public ViewRenderer prepareDisplay(DataParam param)
  {
    try
    {
      System.out.println(param);
      
      String openId = param.get("openId");
      if (openId != null)
      {
        getSessionAttributes().put("openId", openId);
      } else {
        openId = (String)getSessionAttributes().get("openId");
      }
      if (openId == null) {
        return new LocalRenderer(getPage());
      }
      param.put("openId", openId);
      
      System.out.println("openId is " + openId);
      
      String currentDate = DateUtil.getDateByType(9, new Date());
      String result = signInService(param, false);
      if ("未绑定".equals(result)) {
        setErrorMsg("没有跟微信绑定，点击这里<a href='javascript:bindWxUser()'>配置用户绑定</a>");
      } else if ("未定位".equals(result)) {
        setErrorMsg("获取地理位置失败，请尝试重新签到！");
      } else if ("重复签到".equals(result))
      {
        setAttribute("resultMsg", "今日已签到，不能再次签到!");
        setAttribute("date", currentDate);
        setAttribute("week", getWeek(currentDate));
        DataParam adtDateParam = new DataParam(new Object[] { "adtDate", currentDate });
        List<DataRow> records = findRecords(adtDateParam);
        setRsList(records);
      } else if ("签到成功".equals(result)) {
        setAttribute("resultMsg", "签到成功！热爱生活、开心工作！");
        setAttribute("date", currentDate);
        setAttribute("week", getWeek(currentDate));
        DataParam adtDateParam = new DataParam(new Object[] { "adtDate", currentDate });
        List<DataRow> records = findRecords(adtDateParam);
        setRsList(records);
      }
    }
    catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new LocalRenderer(getPage());
  }
  
  public String signInService(DataParam param, boolean canRepeat)
  {
    String openId = param.get("openId");
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    DataParam queryParam = new DataParam(new Object[] { "openId", openId });
    DataRow userRow = attendanceManage.retrieveUserInfo(queryParam);
    
    if (MapUtil.isNullOrEmpty(userRow)) {
      return "未绑定";
    }
    synchronized (WxSignInHandler.class)
    {
      String userId = userRow.getString("USER_ID");
      String currentDate = DateUtil.getDateByType(9, new Date());
      String currentTime = DateUtil.getDateByType(10, new Date());
      String time = currentTime.substring(11);
      queryParam = new DataParam();
      queryParam.put("adtDate", currentDate);
      queryParam.put("currentUser", userId);
      queryParam.put("expression", "and 1=1");
      List<DataRow> attendanceRow = attendanceManage.findRecords(queryParam);
      if (ListUtil.isNullOrEmpty(attendanceRow))
      {
        String address = param.get("address");
        if (StringUtil.isEmpty(address)) {
          return "未定位";
        }
        String location = param.get("location");
        
        DataParam dataParam = new DataParam();
        String atdId = KeyGenerator.instance().genKey();
        dataParam.put("ATD_ID", atdId);
        dataParam.put("ATD_DATE", currentDate);
        dataParam.put("USER_ID", userId);
        dataParam.put("ATD_IN_TIME", new Date());
        dataParam.put("ATD_IN_PLACE", address);
        dataParam.put("ATD_IN_COORDINATE", location);
        if (time.compareTo("18:00:00") > 0) {
          dataParam.put("ATD_IN_HOUSE", "夜班");
        } else {
          dataParam.put("ATD_IN_HOUSE", "白班");
        }
        attendanceManage.createRecord(dataParam);
        
        writeSystemLog("微信" + dataParam.get("ATD_IN_HOUSE") + "签到(" + param.get("version") + ")", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
        
        return "签到成功";
      }
      
      if ((canRepeat) && (attendanceRow.size() == 1) && (time.compareTo("18:00:00") > 0) && (!((DataRow)attendanceRow.get(0)).getString("ATD_IN_HOUSE").contains("夜班"))) {
        String address = param.get("address");
        String location = param.get("location");
        DataParam dataParam = new DataParam();
        String atdId = KeyGenerator.instance().genKey();
        dataParam.put("ATD_ID", atdId);
        dataParam.put("ATD_DATE", currentDate);
        dataParam.put("USER_ID", userId);
        dataParam.put("ATD_IN_TIME", new Date());
        dataParam.put("ATD_IN_PLACE", address);
        dataParam.put("ATD_IN_COORDINATE", location);
        dataParam.put("ATD_IN_HOUSE", "夜班");
        attendanceManage.createRecord(dataParam);
        writeSystemLog("微信夜班签到(" + param.get("version") + ")", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
        return "夜班签到成功";
      }
      return "重复签到";
    }
  }
  
  @PageAction
  public ViewRenderer showBeforeDay(DataParam param)
  {
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    String date = param.get("date");
    if (date == null) {
      date = DateUtil.getDateByType(9, new Date());
    }
    Date tempDate = DateUtil.getDateAdd(DateUtil.getDate(date), 1, -1);
    date = DateUtil.getDateByType(9, tempDate);
    setAttribute("date", date);
    setAttribute("week", getWeek(date));
    DataParam adtDateParam = new DataParam(new Object[] { "adtDate", date });
    List<DataRow> records = findRecords(adtDateParam);
    setRsList(records);
    
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer showNextDay(DataParam param) {
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    String date = param.get("date");
    if (date == null) {
      date = DateUtil.getDateByType(9, new Date());
    }
    Date tempDate = DateUtil.getDateAdd(DateUtil.getDate(date), 1, 1);
    date = DateUtil.getDateByType(9, tempDate);
    setAttribute("date", date);
    setAttribute("week", getWeek(date));
    DataParam adtDateParam = new DataParam(new Object[] { "adtDate", date });
    List<DataRow> records = findRecords(adtDateParam);
    setRsList(records);
    
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer showToday(DataParam param) {
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    String date = DateUtil.getDateByType(9, new Date());
    setAttribute("date", date);
    setAttribute("week", getWeek(date));
    DataParam adtDateParam = new DataParam(new Object[] { "adtDate", date });
    List<DataRow> records = findRecords(adtDateParam);
    setRsList(records);
    
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer theMonth(DataParam param) {
    String date = param.get("date");
    if (date == null) {
      date = DateUtil.getDateByType(9, new Date());
    }
    setAttribute("date", date);
    String atdDate = date;
    Date selectDate = DateUtil.getDate(atdDate);
    Date startDate = DateUtil.getBeginOfMonth(selectDate);
    Date endDate = DateUtil.getEndOfMonth(selectDate);
    param.put("sdate", startDate);
    param.put("edate", endDate);
    List<DataRow> records = findRecords(param);
    setRsList(records);
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer getLocation(DataParam param)
  {
    String latitude = param.get("Latitude");
    String longitude = param.get("Longitude");
    JSONObject object = new JSONObject();
    try {
      if ((StringUtil.isEmpty(latitude)) || (StringUtil.isEmpty(longitude))) {
        object.put("error", "获取不到地理位置信息，请确认允许本公众号获取您的位置信息！");
      } else {
        List<String> addresses = LocationHelper.getAddressList(latitude, longitude);
        if (addresses.size() == 0) {
          object.put("error", "获取不到地理位置名称");
        } else {
          object.put("addresses", addresses);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new AjaxRenderer(object.toString());
  }
  
  private String getWeek(String date) {
    Date dateTemp = DateUtil.getDateTime(date);
    String week = DateUtil.getWeekText(dateTemp);
    return week;
  }
  
  private List<DataRow> findRecords(DataParam adtDateParam) {
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    List<DataRow> records = attendanceManage.findRecords(adtDateParam);
    for (DataRow dataRow : records) {
      String coor = dataRow.getString("ATD_IN_COORDINATE");
      String addr = dataRow.getString("ATD_IN_PLACE");
      if (StringUtil.isNotEmpty(coor)) {
        dataRow.put("mapurl", StringCompressTool.mapImageUrl(coor, addr));
      }
    }
    return records;
  }
  
  protected void setErrorMsg(String errorMsg) {
    this.request.getSession().setAttribute("errorMsg", errorMsg);
  }
}
