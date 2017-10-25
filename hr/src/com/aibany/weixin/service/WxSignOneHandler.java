package com.aibany.weixin.service;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.common.Constants;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.agileai.util.MapUtil;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.cxmodule.HrAttendanceManage;
import com.aibany.hr.module.workovertime.handler.HrWorkOvertimeManageEditHandler;
import com.aibany.utils.CookieTool;
import java.util.Date;
import java.util.List;

public class WxSignOneHandler
  extends CommonHandler
{
  public static final String wx_nobind = "未绑定";
  public static final String wx_noaddress = "未定位";
  public static final String wx_signsuccess = "白班签到成功";
  public static final String wx_signoutsuccess = "白班签退成功";
  public static final String wx_sign_night_success = "夜班签到成功";
  public static final String wx_signout_night_success = "夜班签退成功";
  public static final String wx_signrepeat = "重复签到";
  public static final String wx_sign_notime = "非签到时间";
  public static final int wx_minutes = 10;
  
  public ViewRenderer prepareDisplay(DataParam param)
  {
    return theMonth(param);
  }
  
  @PageAction
  public ViewRenderer theMonth(DataParam param)
  {
    String userId = StringCompressTool.decodePassword(CookieTool.getCookieValueByName(this.request, "hri"));
    String userName = StringCompressTool.decodePassword(CookieTool.getCookieValueByName(this.request, "hrn"));
    String userCode = StringCompressTool.decodePassword(CookieTool.getCookieValueByName(this.request, "hrc"));
    if ((userId == null) || (userId.equals("admin"))) {
      return new RedirectRenderer(getHandlerURL(Constants.FrameHandlers.LoginHandlerId));
    }
    
    String date = param.get("date");
    if ((date == null) || ("theMonth".equals(param.get("actionType")))) {
      date = DateUtil.getDateByType(9, new Date());
    }
    setAttribute("date", date);
    String atdDate = date;
    Date selectDate = DateUtil.getDate(atdDate);
    Date startDate = DateUtil.getBeginOfMonth(selectDate);
    Date endDate = DateUtil.getEndOfMonth(selectDate);
    param.put("sdate", startDate);
    param.put("edate", endDate);
    param.put("currentUser", userId);
    
    List<DataRow> records = findRecords(param);
    setRsList(records);
    setAttribute("userName", userName);
    writeSystemLog("查看签到记录" + date, userCode, userName);
    
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer showBeforeMonth(DataParam param) {
    String date = param.get("date");
    if (date == null) {
      date = DateUtil.getDateByType(9, new Date());
    }
    Date tempDate = DateUtil.getDateAdd(DateUtil.getDate(date), 2, -1);
    date = DateUtil.getDateByType(9, tempDate);
    param.put("date", date);
    return theMonth(param);
  }
  
  @PageAction
  public ViewRenderer showNextMonth(DataParam param) {
    String date = param.get("date");
    if (date == null) {
      date = DateUtil.getDateByType(9, new Date());
    }
    Date tempDate = DateUtil.getDateAdd(DateUtil.getDate(date), 2, 1);
    date = DateUtil.getDateByType(9, tempDate);
    param.put("date", date);
    return theMonth(param);
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
      String outcoor = dataRow.getString("ATD_OUT_COORDINATE");
      String outaddr = dataRow.getString("ATD_OUT_PLACE");
      if (StringUtil.isNotEmpty(outcoor)) {
        dataRow.put("mapurl2", StringCompressTool.mapImageUrl(outcoor, outaddr));
      }
    }
    return records;
  }
  
  public String signInService(DataParam param)
  {
    String openId = param.get("openId");
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    DataParam queryParam = new DataParam(new Object[] { "openId", openId });
    DataRow userRow = attendanceManage.retrieveUserInfo(queryParam);
    String userId = userRow.getString("USER_ID");
    queryParam.put("currentUser", userId);
    queryParam.put("expression", "and 1=1");
    
    String currentDate = DateUtil.getDateByType(9, new Date());
    String currentTime = DateUtil.getDateByType(10, new Date());
    
    String nowtime = currentTime.substring(11);
    
    String address = param.get("address");
    String location = param.get("location");
    
    if (MapUtil.isNullOrEmpty(userRow))
      return "未绑定";
    if (StringUtil.isEmpty(address)) {
      return "未定位";
    }
    synchronized (WxSignOneHandler.class)
    {
      Date tempDate = DateUtil.getDateAdd(new Date(), 1, -1);
      String lastDate = DateUtil.getDateByType(9, tempDate);
      queryParam.put("adtDate", lastDate);
      List<DataRow> lastDayRows = attendanceManage.findRecords(queryParam);
      
      queryParam.replace("adtDate", currentDate);
      List<DataRow> todayRows = attendanceManage.findRecords(queryParam);
      
      param.put("USER_CODE", userRow.getString("USER_CODE"));
      param.put("USER_NAME", userRow.getString("USER_NAME"));
      
      if (nowtime.compareTo("14:00:00") <= 0)
      {
        if ((todayRows != null) && (todayRows.size() == 1) && (((DataRow)todayRows.get(0)).get("ATD_IN_TIME") != null)) {
          Date lastin = (Date)((DataRow)todayRows.get(0)).get("ATD_IN_TIME");
          long minitue = (new Date().getTime() - lastin.getTime()) / 1000L / 60L;
          
          if (minitue < 10L) {
            return "重复签到";
          }
          
          if ((((DataRow)todayRows.get(0)).get("ATD_IN_TIME") != null) && (((DataRow)todayRows.get(0)).get("ATD_OUT_TIME") == null))
          {
            DataParam dataParam = new DataParam();
            String atdId = ((DataRow)todayRows.get(0)).getString("ATD_ID");
            dataParam.put("ATD_ID", atdId);
            dataParam.put("ATD_OUT_TIME", new Date());
            dataParam.put("ATD_IN_TIME", ((DataRow)todayRows.get(0)).get("ATD_IN_TIME"));
            dataParam.put("ATD_IN_HOUSE", ((DataRow)todayRows.get(0)).get("ATD_IN_HOUSE"));
            dataParam.put("ATD_IN_PLACE", ((DataRow)todayRows.get(0)).get("ATD_IN_PLACE"));
            dataParam.put("ATD_OUT_PLACE", address);
            dataParam.put("ATD_OUT_COORDINATE", location);
            fixOverTimeHour(dataParam);
            attendanceManage.updateRecord(dataParam);
            writeSystemLog("(一键)微信" + dataParam.get("ATD_IN_HOUSE") + "签退(码" + param.get("version") + ")", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
            
            reportWorkOverTime(userRow, dataParam);
            param.put("ATD_OVERTIME", dataParam.get("ATD_OVERTIME"));
            param.put("ATD_WORKTIME", dataParam.get("ATD_WORKTIME"));
            
            return "白班签退成功";
          }
        }
        
        if ((lastDayRows.size() > 0) && (((DataRow)lastDayRows.get(lastDayRows.size() - 1)).get("ATD_OUT_TIME") != null)) {
          Date lastout = (Date)((DataRow)lastDayRows.get(lastDayRows.size() - 1)).get("ATD_OUT_TIME");
          long minitue = (new Date().getTime() - lastout.getTime()) / 1000L / 60L;
          if (minitue < 10L) {
            return "重复签到";
          }
        }
        
        if (((todayRows == null) || (todayRows.size() == 0)) && 
          (lastDayRows.size() > 0) && 
          (((DataRow)lastDayRows.get(lastDayRows.size() - 1)).getString("ATD_IN_HOUSE").contains("夜班")) && 
          (((DataRow)lastDayRows.get(lastDayRows.size() - 1)).get("ATD_OUT_TIME") == null))
        {
          DataParam dataParam = new DataParam();
          String atdId = ((DataRow)lastDayRows.get(lastDayRows.size() - 1)).getString("ATD_ID");
          dataParam.put("ATD_ID", atdId);
          dataParam.put("ATD_OUT_TIME", new Date());
          dataParam.put("ATD_IN_TIME", ((DataRow)lastDayRows.get(lastDayRows.size() - 1)).get("ATD_IN_TIME"));
          dataParam.put("ATD_IN_HOUSE", ((DataRow)lastDayRows.get(lastDayRows.size() - 1)).get("ATD_IN_HOUSE"));
          dataParam.put("ATD_IN_PLACE", ((DataRow)lastDayRows.get(lastDayRows.size() - 1)).get("ATD_IN_PLACE"));
          dataParam.put("ATD_OUT_PLACE", address);
          dataParam.put("ATD_OUT_COORDINATE", location);
          fixOverTimeHour(dataParam);
          attendanceManage.updateRecord(dataParam);
          writeSystemLog("(一键)微信" + dataParam.get("ATD_IN_HOUSE") + "签退(码" + param.get("version") + ")", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
          
          reportWorkOverTime(userRow, dataParam);
          param.put("ATD_OVERTIME", dataParam.get("ATD_OVERTIME"));
          param.put("ATD_WORKTIME", dataParam.get("ATD_WORKTIME"));
          
          return "夜班签退成功";
        }
        
        if ((todayRows == null) || (todayRows.size() == 0))
        {
          DataParam dataParam = new DataParam();
          String atdId = KeyGenerator.instance().genKey();
          dataParam.put("ATD_ID", atdId);
          dataParam.put("ATD_DATE", currentDate);
          dataParam.put("USER_ID", userId);
          dataParam.put("ATD_IN_TIME", new Date());
          dataParam.put("ATD_IN_PLACE", address);
          dataParam.put("ATD_IN_COORDINATE", location);
          dataParam.put("ATD_IN_HOUSE", "白班");
          attendanceManage.createRecord(dataParam);
          writeSystemLog("(一键)微信" + dataParam.get("ATD_IN_HOUSE") + "签到(码" + param.get("version") + ")", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
          
          return "白班签到成功";
        }
      }
      else if (nowtime.compareTo("14:01:00") >= 0)
      {
        if ((todayRows != null) && (todayRows.size() == 1) && (((DataRow)todayRows.get(0)).get("ATD_IN_TIME") != null) && (((DataRow)todayRows.get(0)).get("ATD_OUT_TIME") == null) && (((DataRow)todayRows.get(0)).getString("ATD_IN_HOUSE").contains("白班")))
        {
          DataParam dataParam = new DataParam();
          String atdId = ((DataRow)todayRows.get(0)).getString("ATD_ID");
          dataParam.put("ATD_ID", atdId);
          dataParam.put("ATD_OUT_TIME", new Date());
          dataParam.put("ATD_IN_TIME", ((DataRow)todayRows.get(0)).get("ATD_IN_TIME"));
          dataParam.put("ATD_IN_HOUSE", ((DataRow)todayRows.get(0)).get("ATD_IN_HOUSE"));
          dataParam.put("ATD_IN_PLACE", ((DataRow)todayRows.get(0)).get("ATD_IN_PLACE"));
          dataParam.put("ATD_OUT_PLACE", address);
          dataParam.put("ATD_OUT_COORDINATE", location);
          fixOverTimeHour(dataParam);
          attendanceManage.updateRecord(dataParam);
          writeSystemLog("(一键)微信" + dataParam.get("ATD_IN_HOUSE") + "签退(码" + param.get("version") + ")", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
          
          reportWorkOverTime(userRow, dataParam);
          param.put("ATD_OVERTIME", dataParam.get("ATD_OVERTIME"));
          param.put("ATD_WORKTIME", dataParam.get("ATD_WORKTIME"));
          
          return "白班签退成功";
        }
        
        if ((todayRows != null) && (
          (todayRows.size() > 2) || ((todayRows.size() == 2) && (((DataRow)todayRows.get(todayRows.size() - 1)).get("ATD_OUT_TIME") != null))))
          return "重复签到";
        if ((todayRows != null) && (todayRows.size() == 1) && (((DataRow)todayRows.get(0)).get("ATD_OUT_TIME") != null)) {
          Date lastout = (Date)((DataRow)todayRows.get(0)).get("ATD_OUT_TIME");
          long minitue = (new Date().getTime() - lastout.getTime()) / 1000L / 60L;
          if (minitue < 10L) {
            return "重复签到";
          }
        }
        
        if ((todayRows.size() == 0) || ((todayRows.size() == 1) && (((DataRow)todayRows.get(0)).getString("ATD_IN_HOUSE").contains("白班"))))
        {
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
          writeSystemLog("(一键)微信" + dataParam.get("ATD_IN_HOUSE") + "签到(码" + param.get("version") + ")", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
          
          return "夜班签到成功"; }
        if ((todayRows != null) && (todayRows.size() > 0) && (((DataRow)todayRows.get(todayRows.size() - 1)).get("ATD_OUT_TIME") == null))
        {
          DataParam dataParam = new DataParam();
          String atdId = ((DataRow)todayRows.get(todayRows.size() - 1)).getString("ATD_ID");
          dataParam.put("ATD_ID", atdId);
          dataParam.put("ATD_OUT_TIME", new Date());
          dataParam.put("ATD_IN_TIME", ((DataRow)todayRows.get(todayRows.size() - 1)).get("ATD_IN_TIME"));
          dataParam.put("ATD_IN_HOUSE", ((DataRow)todayRows.get(todayRows.size() - 1)).get("ATD_IN_HOUSE"));
          dataParam.put("ATD_IN_PLACE", ((DataRow)todayRows.get(todayRows.size() - 1)).get("ATD_IN_PLACE"));
          dataParam.put("ATD_OUT_PLACE", address);
          dataParam.put("ATD_OUT_COORDINATE", location);
          fixOverTimeHour(dataParam);
          attendanceManage.updateRecord(dataParam);
          writeSystemLog("(一键)微信" + dataParam.get("ATD_IN_HOUSE") + "签退(码" + param.get("version") + ")", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
          
          reportWorkOverTime(userRow, dataParam);
          param.put("ATD_OVERTIME", dataParam.get("ATD_OVERTIME"));
          param.put("ATD_WORKTIME", dataParam.get("ATD_WORKTIME"));
          
          return "夜班签退成功";
        }
      } else {
        return "非签到时间";
      }
      return "重复签到";
    }
  }
  
  public void reportWorkOverTime(DataRow userRow, DataParam dataParam)
  {
    if ((dataParam.get("ATD_OVERTIME") != null) && (Double.parseDouble(dataParam.get("ATD_OVERTIME")) > 0.0D)) {
      DataParam param = new DataParam();
      param.put("operaType", "insert");
      param.put("USER_ID_NAME", userRow.getString("USER_NAME"));
      param.put("USER_ID", userRow.getString("USER_ID"));
      param.put("STATE", "submitted");
      param.put("WOT_TIME", dataParam.get("ATD_OVERTIME"));
      String type = dataParam.get("ATD_IN_HOUSE");
      if (type.equals("白班")) {
        param.put("WOT_DESC", "自动上报白班加班时间");
      } else {
        param.put("WOT_DESC", "自动上报夜班加班时间");
      }
      param.put("WOT_TIME_COMPANY", "hour");
      param.put("WOT_PLACE", dataParam.get("ATD_OUT_PLACE"));
      param.put("WOT_DAYTYPE", "工作日");
      String currentDate = DateUtil.getDateByType(9, new Date());
      param.put("WOT_OVERTIME_DATE", currentDate);
      param.put("WOT_DATE", new Date());
      
      HrWorkOvertimeManageEditHandler handler = new HrWorkOvertimeManageEditHandler();
      handler.doSaveMasterRecordAction(param);
    }
  }
  
  public void fixOverTimeHour(DataParam dataParam)
  {
    Date in = (Date)dataParam.getObject("ATD_IN_TIME");
    Date out = (Date)dataParam.getObject("ATD_OUT_TIME");
    long minitue = (out.getTime() - in.getTime()) / 1000L / 60L;
    double overHour = 0.0D;
    double workHour = 0.0D;
    if (minitue > 0L) {
      long hour = minitue / 60L;
      overHour = hour;
      workHour = hour;
      long left = minitue - hour * 60L;
      if (left > 30L) {
        overHour += 0.5D;
      }
      String type = dataParam.get("ATD_IN_HOUSE");
      if (("白班".contains(type)) || (StringUtil.isEmpty(type))) {
        overHour -= 9.0D;
        workHour -= 1.0D;
        if (overHour > 3.0D) {
          overHour = 3.0D;
        }
      } else {
        overHour -= 8.5D;
        workHour -= 0.5D;
        if (overHour > 3.5D) {
          overHour = 3.5D;
        }
      }
      if (overHour < 0.0D) {
        overHour = 0.0D;
      }
      if (workHour < 0.0D) {
        workHour = 0.0D;
      }
    }
    dataParam.put("ATD_OVERTIME", overHour);
    dataParam.put("ATD_WORKTIME", workHour);
  }
}
