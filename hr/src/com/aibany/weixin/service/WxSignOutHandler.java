package com.aibany.weixin.service;

import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.agileai.util.ListUtil;
import com.agileai.util.MapUtil;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.cxmodule.HrAttendanceManage;
import com.aibany.hr.module.workovertime.handler.HrWorkOvertimeManageEditHandler;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class WxSignOutHandler extends CommonHandler
{
  public ViewRenderer prepareDisplay(com.agileai.domain.DataParam param)
  {
    try
    {
      System.out.println(param);
      
      String openId = param.get("openId");
      if (openId != null) {
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
        setErrorMsg("获取地理位置失败，请尝试重新签退！");
      } else if ("重复签到".equals(result)) {
        setAttribute("resultMsg", "已签退过！不能再次签退! ^_^");
        setAttribute("date", currentDate);
        setAttribute("week", getWeek(currentDate));
        com.agileai.domain.DataParam adtDateParam = new com.agileai.domain.DataParam(new Object[] { "adtDate", currentDate });
        adtDateParam.put("expression", " and ATD_OUT_TIME is not null");
        List<com.agileai.domain.DataRow> records = findRecords(adtDateParam);
        setRsList(records);
      } else if ("未签到，无法签退".equals(result)) {
        setErrorMsg("您还未签到，请先签到！");
      } else if ("签到成功".equals(result)) {
        setAttribute("resultMsg", "签退成功！珍爱自己，好好休息！");
        setAttribute("date", currentDate);
        setAttribute("week", getWeek(currentDate));
        com.agileai.domain.DataParam adtDateParam = new com.agileai.domain.DataParam(new Object[] { "adtDate", currentDate });
        adtDateParam.put("expression", " and ATD_OUT_TIME is not null");
        List<com.agileai.domain.DataRow> records = findRecords(adtDateParam);
        setRsList(records);
      }
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new LocalRenderer(getPage());
  }
  
  public String signInService(com.agileai.domain.DataParam param, boolean canRepeat)
  {
    String openId = param.get("openId");
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    com.agileai.domain.DataParam queryParam = new com.agileai.domain.DataParam(new Object[] { "openId", openId });
    com.agileai.domain.DataRow userRow = attendanceManage.retrieveUserInfo(queryParam);
    
    if (MapUtil.isNullOrEmpty(userRow)) {
      return "未绑定";
    }
    synchronized (WxSignOutHandler.class)
    {
      String userId = userRow.getString("USER_ID");
      String currentDate = DateUtil.getDateByType(9, new Date());
      String currentTime = DateUtil.getDateByType(10, new Date());
      String time = currentTime.substring(11);
      List<com.agileai.domain.DataRow> listRow = null;
      
      queryParam = new com.agileai.domain.DataParam();
      queryParam.put("currentUser", userId);
      queryParam.put("adtDate", currentDate);
      queryParam.put("expression", "and 1=1");
      
      if (time.compareTo("11:00:00") <= 0) {
        Date tempDate = DateUtil.getDateAdd(new Date(), 1, -1);
        String lastDate = DateUtil.getDateByType(9, tempDate);
        queryParam.replace("adtDate", lastDate);
        listRow = attendanceManage.findRecords(queryParam);
        if ((listRow.size() == 0) || 
          (((com.agileai.domain.DataRow)listRow.get(listRow.size() - 1)).get("ATD_OUT_TIME") != null) || 
          (!((String) ((com.agileai.domain.DataRow)listRow.get(listRow.size() - 1)).get("ATD_IN_HOUSE")).contains("夜班"))) {
          queryParam.replace("adtDate", currentDate);
          listRow = attendanceManage.findRecords(queryParam);
        }
      } else {
        listRow = attendanceManage.findRecords(queryParam);
      }
      if (ListUtil.isNullOrEmpty(listRow)) {
        return "未签到，无法签退";
      }
      if ((listRow.size() == 1) && (((com.agileai.domain.DataRow)listRow.get(0)).get("ATD_OUT_TIME") == null))
      {
        String address = param.get("address");
        if (StringUtil.isEmpty(address)) {
          return "未定位";
        }
        String location = param.get("location");
        com.agileai.domain.DataParam dataParam = new com.agileai.domain.DataParam();
        String atdId = ((com.agileai.domain.DataRow)listRow.get(0)).getString("ATD_ID");
        dataParam.put("ATD_ID", atdId);
        dataParam.put("ATD_OUT_TIME", new Date());
        dataParam.put("ATD_IN_TIME", ((com.agileai.domain.DataRow)listRow.get(0)).get("ATD_IN_TIME"));
        dataParam.put("ATD_IN_HOUSE", ((com.agileai.domain.DataRow)listRow.get(0)).get("ATD_IN_HOUSE"));
        dataParam.put("ATD_IN_PLACE", ((com.agileai.domain.DataRow)listRow.get(0)).get("ATD_IN_PLACE"));
        dataParam.put("ATD_OUT_PLACE", address);
        dataParam.put("ATD_OUT_COORDINATE", location);
        fixOverTimeHour(dataParam);
        attendanceManage.updateRecord(dataParam);
        writeSystemLog("微信" + dataParam.get("ATD_IN_HOUSE") + "签退(" + param.get("version") + ")", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
        
        reportWorkOverTime(userRow, dataParam);
        param.put("ATD_OVERTIME", dataParam.get("ATD_OVERTIME"));
        param.put("ATD_WORKTIME", dataParam.get("ATD_WORKTIME"));
        
        if (dataParam.get("ATD_IN_HOUSE").contains("白班")) {
          return "签到成功";
        }
        return "夜班签到成功";
      }
      
      if ((listRow.size() == 2) && (canRepeat) && (((com.agileai.domain.DataRow)listRow.get(1)).get("ATD_OUT_TIME") == null)) {
        String address = param.get("address");
        String location = param.get("location");
        com.agileai.domain.DataParam dataParam = new com.agileai.domain.DataParam();
        String atdId = ((com.agileai.domain.DataRow)listRow.get(1)).getString("ATD_ID");
        dataParam.put("ATD_ID", atdId);
        dataParam.put("ATD_IN_TIME", ((com.agileai.domain.DataRow)listRow.get(1)).get("ATD_IN_TIME"));
        dataParam.put("ATD_IN_HOUSE", ((com.agileai.domain.DataRow)listRow.get(1)).get("ATD_IN_HOUSE"));
        dataParam.put("ATD_IN_PLACE", ((com.agileai.domain.DataRow)listRow.get(1)).get("ATD_IN_PLACE"));
        dataParam.put("ATD_OUT_TIME", new Date());
        dataParam.put("ATD_OUT_PLACE", address);
        dataParam.put("ATD_OUT_COORDINATE", location);
        fixOverTimeHour(dataParam);
        attendanceManage.updateRecord(dataParam);
        writeSystemLog("微信夜班签退(" + param.get("version") + ")", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
        
        reportWorkOverTime(userRow, dataParam);
        param.put("ATD_OVERTIME", dataParam.get("ATD_OVERTIME"));
        param.put("ATD_WORKTIME", dataParam.get("ATD_WORKTIME"));
        return "夜班签到成功";
      }
      return "重复签到";
    }
  }
  
  public void reportWorkOverTime(com.agileai.domain.DataRow userRow, com.agileai.domain.DataParam dataParam)
  {
    if ((dataParam.get("ATD_OVERTIME") != null) && (Double.parseDouble(dataParam.get("ATD_OVERTIME")) > 0.0D)) {
      com.agileai.domain.DataParam param = new com.agileai.domain.DataParam();
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
  
  public void fixOverTimeHour(com.agileai.domain.DataParam dataParam)
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
  
  @PageAction
  public ViewRenderer showBeforeDay(com.agileai.domain.DataParam param) {
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    String date = param.get("date");
    if (date == null) {
      date = DateUtil.getDateByType(9, new Date());
    }
    Date tempDate = DateUtil.getDateAdd(DateUtil.getDate(date), 1, -1);
    date = DateUtil.getDateByType(9, tempDate);
    setAttribute("date", date);
    setAttribute("week", getWeek(date));
    com.agileai.domain.DataParam adtDateParam = new com.agileai.domain.DataParam(new Object[] { "adtDate", date });
    adtDateParam.put("expression", " and ATD_OUT_TIME is not null");
    List<com.agileai.domain.DataRow> records = findRecords(adtDateParam);
    setRsList(records);
    
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer showNextDay(com.agileai.domain.DataParam param) {
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    String date = param.get("date");
    if (date == null) {
      date = DateUtil.getDateByType(9, new Date());
    }
    Date tempDate = DateUtil.getDateAdd(DateUtil.getDate(date), 1, 1);
    date = DateUtil.getDateByType(9, tempDate);
    setAttribute("date", date);
    setAttribute("week", getWeek(date));
    com.agileai.domain.DataParam adtDateParam = new com.agileai.domain.DataParam(new Object[] { "adtDate", date });
    adtDateParam.put("expression", " and ATD_OUT_TIME is not null");
    List<com.agileai.domain.DataRow> records = findRecords(adtDateParam);
    setRsList(records);
    
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer showToday(com.agileai.domain.DataParam param) {
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    String date = DateUtil.getDateByType(9, new Date());
    setAttribute("date", date);
    setAttribute("week", getWeek(date));
    com.agileai.domain.DataParam adtDateParam = new com.agileai.domain.DataParam(new Object[] { "adtDate", date });
    adtDateParam.put("expression", " and ATD_OUT_TIME is not null");
    List<com.agileai.domain.DataRow> records = findRecords(adtDateParam);
    setRsList(records);
    
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer theMonth(com.agileai.domain.DataParam param) {
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
    param.put("expression", " and ATD_OUT_TIME is not null");
    List<com.agileai.domain.DataRow> records = findRecords(param);
    setRsList(records);
    return new LocalRenderer(getPage());
  }
  
  private String getWeek(String date) {
    Date dateTemp = DateUtil.getDateTime(date);
    String week = DateUtil.getWeekText(dateTemp);
    return week;
  }
  
  private List<com.agileai.domain.DataRow> findRecords(com.agileai.domain.DataParam adtDateParam) {
    HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
    List<com.agileai.domain.DataRow> records = attendanceManage.findRecords(adtDateParam);
    for (com.agileai.domain.DataRow dataRow : records) {
      String coor = dataRow.getString("ATD_OUT_COORDINATE");
      String addr = dataRow.getString("ATD_OUT_PLACE");
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
