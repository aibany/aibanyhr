package com.aibany.hr.module.workovertime.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonEditHandler;
import com.aibany.hr.module.workovertime.service.HrWorkOvertimeManage;
import com.aibany.utils.RequestDayType;
import java.util.Date;

public class HrWorkOvertimeManageEditHandler
  extends CommonEditHandler
{
  public HrWorkOvertimeManageEditHandler()
  {
    this.listHandlerClass = HrWorkOvertimeManageListHandler.class;
    this.serviceId = buildServiceId(HrWorkOvertimeManage.class);
  }
  
  public ViewRenderer prepareDisplay(DataParam param) {
    String operaType = param.get("operaType");
    User user = (User)getUser();
    param.put("currentDate", 
      DateUtil.getDateByType(10, new Date()));
    param.put("USER_CODE", user.getUserId());
    if ("insert".equals(operaType)) {
      setAttribute("doEdit", Boolean.valueOf(true));
      setAttribute("doSignIn", Boolean.valueOf(false));
      setAttribute("doApprove", Boolean.valueOf(false));
      setAttribute("doSubmit", Boolean.valueOf(false));
    }
    if ("update".equals(operaType)) {
      setAttribute("doEdit", Boolean.valueOf(true));
      setAttribute("doSignIn", Boolean.valueOf(false));
      setAttribute("doApprove", Boolean.valueOf(false));
      setAttribute("doSubmit", Boolean.valueOf(true));
      if (isReqRecordOperaType(operaType)) {
        DataRow record = getService().getNowRecord(param);
        setAttributes(record);
      }
    }
    if ("approve".equals(operaType)) {
      setAttribute("isApprove", Boolean.valueOf(true));
      setAttribute("doSignIn", Boolean.valueOf(true));
      setAttribute("doEdit", Boolean.valueOf(false));
      setAttribute("doApprove", Boolean.valueOf(true));
      if (!isReqRecordOperaType(operaType)) {
        DataRow record = getService().getNowRecord(param);
        setAttributes(record);
        setAttribute(
          "WOT_APPROVER_NAME", 
          getAttribute("WOT_APPROVER_NAME", 
          user.getUserName()));
        setAttribute("WOT_APPROVER", 
          getAttribute("WOT_APPROVER", user.getUserId()));
        if (getAttribute("WOT_APP_TIME") == null) {
          String date = DateUtil.getDateByType(
            11, new Date());
          setAttribute("WOT_APP_TIME", date);
        }
      }
    } else {
      setAttribute("isApprove", Boolean.valueOf(false));
    }
    if ("revokeApproval".equals(operaType)) {
      DataRow record = getService().getNowRecord(param);
      setAttributes(record);
      setAttribute("doRevokeApprove", Boolean.valueOf(true));
    }
    if (("detail".equals(operaType)) && 
      (isReqRecordOperaType(operaType))) {
      DataRow record = getService().getNowRecord(param);
      if (record != null) {
        if (record.get("STATE").equals("approved")) {
          setAttribute("doSignIn", Boolean.valueOf(true));
        }
        if ((record.get("STATE").equals("drafe")) && 
          (user.getUserId().equals(record.get("USER_ID")))) {
          setAttribute("doEdit", Boolean.valueOf(true));
          setAttribute("doSubmit", Boolean.valueOf(true));
        }
        
        if (record.get("STATE").equals("submitted")) {
          setAttribute("doApprove", Boolean.valueOf(true));
          setAttribute("doSignIn", Boolean.valueOf(true));
          DataRow records = getService().getNowRecord(param);
          setAttributes(records);
          setAttribute(
            "WOT_APPROVER_NAME", 
            getAttribute("WOT_APPROVER_NAME", 
            user.getUserName()));
          setAttribute("WOT_APPROVER", 
            getAttribute("WOT_APPROVER", user.getUserId()));
          if (getAttribute("WOT_APP_TIME") == null) {
            String date = DateUtil.getDateByType(
              11, new Date());
            setAttribute("WOT_APP_TIME", date);
          }
        }
        if (record.get("STATE").equals("approved")) {
          setAttribute("doSignIn", Boolean.valueOf(true));
          setAttribute("doRevokeApprove", Boolean.valueOf(true));
        }
      }
      setAttributes(record);
    }
    
    setOperaType(operaType);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(DataParam param) {
    User user = (User)getUser();
    setAttribute("USER_ID_NAME", 
      getAttribute("USER_ID_NAME", user.getUserName()));
    setAttribute("USER_ID", 
      getAttribute("USER_ID", user.getUserId()));
    
    String date = DateUtil.getDateByType(11, 
      new Date());
    if (getAttribute("WOT_DATE") == null) {
      setAttribute("WOT_DATE", date);
    }
    FormSelect form = FormSelectFactory.create("WOT_TIME")
      .addSelectedValue(getOperaAttributeValue("WOT_TIME", ""));
    
    setAttribute("WOT_TIME", form);
    setAttribute("WOT_TIME_COMPANY", FormSelectFactory.create("WOT_TIME_COMPANY")
      .addSelectedValue(getOperaAttributeValue("WOT_TIME_COMPANY", "DAY")));
    setAttribute("APP_RESULT", FormSelectFactory.create("APP_RESULT").addSelectedValue(
      getAttributeValue("APP_RESULT", "YES")));
    setAttribute("STATE", FormSelectFactory.create("STATE").addSelectedValue(
      getOperaAttributeValue("STATE", "drafe")));
  }
  
  public ViewRenderer doSaveMasterRecordAction(DataParam param)
  {
    dealWithDays(param);
    
    if (StringUtil.isEmpty(param.get("STATE"))) {
      param.put("STATE", "drafe");
    }
    
    String operateType = param.get("operaType");
    String responseText = "fail";
    if ("insert".equals(operateType)) {
      getService().createRecord(param);
      responseText = param.get("WOT_ID");
    }
    else if ("update".equals(operateType)) {
      getService().updateRecord(param);
      responseText = param.get("WOT_ID");
    } else if ("detail".equals(operateType)) {
      getService().updateRecord(param);
      responseText = param.get("WOT_ID");
    }
    return new AjaxRenderer(responseText);
  }
  
  public ViewRenderer detail(DataParam param) { getService().getNowRecord(param);
    return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  @PageAction
  public ViewRenderer submit(DataParam param) {
    dealWithDays(param);
    
    param.put("STATE", "submitted");
    getService().updateRecord(param);
    
    writeSystemLog("提交加班申请");
    return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  @PageAction
  public ViewRenderer approve(DataParam param) { param.put("STATE", "approved");
    getService().approveRecord(param);
    
    writeSystemLog("通过加班申请");
    return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  @PageAction
  public ViewRenderer revokeApproval(DataParam param) { param.put("STATE", "drafe");
    param.put("WOT_APPROVER", "");
    param.put("WOT_APP_TIME", "");
    param.put("APP_RESULT", "");
    param.put("WOT_APP_OPINION", "");
    getService().approveRecord(param);
    writeSystemLog("拒绝加班申请");
    return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  @PageAction
  public ViewRenderer drafe(DataParam param) { param.put("STATE", "drafe");
    param.put("WOT_APPROVER", "");
    param.put("WOT_APP_TIME", "");
    param.put("APP_RESULT", "");
    param.put("WOT_APP_OPINION", "");
    getService().approveRecord(param);
    return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  protected HrWorkOvertimeManage getService() { return (HrWorkOvertimeManage)lookupService(getServiceId()); }
  
  protected void dealWithDays(DataParam param)
  {
    String startDay = param.get("WOT_OVERTIME_DATE");
    String num = param.get("WOT_TIME");
    String unit = param.get("WOT_TIME_COMPANY");
    
    if ((unit.equals("day")) && (Double.parseDouble(num) > 1.0D)) {
      int round = Math.round(Float.parseFloat(num));
      String[] daysArray = new String[round];
      for (int i = 0; i < round; i++) {
        Date tempDate = DateUtil.getDateAdd(DateUtil.getDate(startDay), 1, i);
        String day = DateUtil.getDateByType(4, tempDate);
        daysArray[i] = day;
      }
      String dayType = RequestDayType.getMinDayType(daysArray);
      param.put("WOT_DAYTYPE", dayType);
    }
    else {
      String days = DateUtil.getDateByType(4, DateUtil.getDate(startDay));
      String dayType = RequestDayType.getMinDayType(new String[] { days });
      param.put("WOT_DAYTYPE", dayType);
    }
    double hours = 0.0D;
    if (unit.equals("day")) {
      hours = Double.parseDouble(num) * 8.0D;
    } else {
      hours = Double.parseDouble(num);
    }
    param.put("WOT_CALC_HOURS", Double.valueOf(hours));
  }
}
