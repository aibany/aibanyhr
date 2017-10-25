package com.aibany.hr.module.attendance.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonEditHandler;
import com.aibany.hr.cxmodule.HrAttendanceManage;
import java.util.Date;

public class HrAttendanceManageEditHandler
  extends CommonEditHandler
{
  public HrAttendanceManageEditHandler()
  {
    this.listHandlerClass = HrAttendanceManageListHandler.class;
    this.serviceId = buildServiceId(HrAttendanceManage.class);
  }
  
  public ViewRenderer prepareDisplay(DataParam param) {
    String operaType = param.get("operaType");
    User user = (User)getUser();
    param.put("currentDate", 
      DateUtil.getDateByType(9, new Date()));
    param.put("currentUser", user.getUserId());
    
    if (isReqRecordOperaType(operaType)) {
      if (StringUtil.isNotEmpty(param.get("ATD_ID"))) {
        param.remove("currentDate");
        param.remove("currentUser");
      }
      DataRow record = getService().getRecord(param);
      setAttributes(record);
    }
    setOperaType(operaType);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(DataParam param) {
    User user = (User)getUser();
    if ((user.getUserCode().equals("admin")) && (StringUtil.isNotEmpty(param.get("ATD_ID")))) {
      setAttribute("hasRight", Boolean.valueOf(true));
      return;
    }
    
    setAttribute("USER_ID_NAME", 
      getAttribute("USER_ID_NAME", user.getUserName()));
    setAttribute("USER_ID", 
      getAttribute("USER_ID", user.getUserId()));
    
    if (getAttribute("ATD_IN_TIME") == null) {
      setAttribute("ATD_IN_TIME", new Date());
      setAttribute("doSignIn", Boolean.valueOf(true));
    } else {
      setAttribute("ATD_OUT_TIME", new Date());
      setAttribute("doSignIn", Boolean.valueOf(false));
    }
    
    String adtDate = (String)getAttribute("ATD_DATE", DateUtil.getDateByType(9, new Date()));
    setAttribute("ATD_DATE", adtDate);
  }
  
  public ViewRenderer doSaveAction(DataParam param)
  {
    Object signIn = param.get("ATD_IN_TIME");
    Object signOut = param.get("ATD_OUT_TIME");
    boolean check = false;
    if ((signIn != null) && ((signIn instanceof String)) && (StringUtil.isNotEmpty((String) signIn))) {
      Date in = DateUtil.getDate((String)signIn);
      param.replace("ATD_IN_TIME", in);
      
      String currentTime = DateUtil.getDateByType(10, in);
      String time = currentTime.substring(11);
      if (time.compareTo("17:00:00") > 0) {
        param.put("ATD_IN_HOUSE", "夜班(订正)");
      } else {
        param.put("ATD_IN_HOUSE", "白班(订正)");
      }
      check = true;
    }
    if ((signOut != null) && ((signOut instanceof String)) && (StringUtil.isNotEmpty((String) signOut))) {
      Date out = DateUtil.getDate((String)signOut);
      param.replace("ATD_OUT_TIME", out);
      check = true;
    }
    if (check) {
      writeSystemLog("订正签到:" + param.get("USER_ID_NAME"));
    } else {
      writeSystemLog("电脑签到");
    }
    
    if (((signIn == null) || (StringUtil.isEmpty((String) signIn))) && ((signOut == null) || (StringUtil.isEmpty((String) signOut)))) {
      getService().deletRecord(param);
      writeSystemLog("删除签到:" + param.get("USER_ID_NAME"));
      return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
    }
    return super.doSaveAction(param);
  }
  
  protected HrAttendanceManage getService()
  {
    return (HrAttendanceManage)lookupService(getServiceId());
  }
}
