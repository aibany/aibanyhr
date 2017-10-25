package com.aibany.hr.module.leave.handler;

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
import com.aibany.hr.module.leave.service.HrLeaveManage;
import java.util.Date;

public class HrLeaveManageEditHandler
  extends CommonEditHandler
{
  public HrLeaveManageEditHandler()
  {
    this.listHandlerClass = HrLeaveManageListHandler.class;
    this.serviceId = buildServiceId(HrLeaveManage.class);
  }
  
  public ViewRenderer prepareDisplay(DataParam param) {
    String operaType = param.get("operaType");
    User user = (User)getUser();
    param.put("currentDate", DateUtil.getDateByType(10, new Date()));
    param.put("USER_ID", user.getUserId());
    param.put("USER_CODE", user.getUserCode());
    
    if ("insert".equals(operaType)) {
      setAttribute("doInsertEdit", Boolean.valueOf(true));
      setAttribute("doSignIn", Boolean.valueOf(false));
      setAttribute("doApprove", Boolean.valueOf(false));
      setAttribute("doSubmit", Boolean.valueOf(false));
    }
    if ("update".equals(operaType)) {
      setAttribute("doInsertEdit", Boolean.valueOf(true));
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
      setAttribute("doInsertEdit", Boolean.valueOf(false));
      setAttribute("doApprove", Boolean.valueOf(true));
      if (!isReqRecordOperaType(operaType)) {
        DataRow record = getService().getNowRecord(param);
        setAttributes(record);
        setAttribute("LEA_APPOVER_NAME", getAttribute("LEA_APPOVER_NAME", user.getUserName()));
        setAttribute("LEA_APPOVER", getAttribute("LEA_APPOVER", user.getUserId()));
        if (getAttribute("LEA_APP_TIME") == null) {
          String date = DateUtil.getDateByType(11, new Date());
          setAttribute("LEA_APP_TIME", date);
        }
      }
    } else {
      setAttribute("isApprove", Boolean.valueOf(false));
    }
    if (("detail".equals(operaType)) && 
      (isReqRecordOperaType(operaType))) {
      DataRow record = getService().getNowRecord(param);
      if (record != null) {
        param.replace("USER_CODE", record.get("USER_ID_CODE"));
        if (record.get("STATE").equals("approved")) {
          setAttribute("doSignIn", Boolean.valueOf(true));
        }
        if ((record.get("STATE").equals("drafe")) && 
          (user.getUserId().equals(record.get("USER_ID")))) {
          setAttribute("doSubmit", Boolean.valueOf(true));
          setAttribute("doInsertEdit", Boolean.valueOf(true));
        }
        
        if (record.get("STATE").equals("submitted")) {
          setAttribute("doApprove", Boolean.valueOf(true));
          setAttribute("doSignIn", Boolean.valueOf(true));
          DataRow records = getService().getNowRecord(param);
          setAttributes(records);
          setAttribute("LEA_APPOVER_NAME", 
            getAttribute("LEA_APPOVER_NAME", user.getUserName()));
          setAttribute("LEA_APPOVER", getAttribute("LEA_APPOVER", user.getUserId()));
          if (getAttribute("LEA_APP_TIME") == null) {
            String date = DateUtil.getDateByType(11, new Date());
            setAttribute("LEA_APP_TIME", date);
          }
        }
        if (record.get("STATE").equals("approved")) {
          setAttribute("doSignIn", Boolean.valueOf(true));
        }
      }
      
      setAttributes(record);
    }
    
    getAllanceYear(param);
    
    setOperaType(operaType);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(DataParam param)
  {
    User user = (User)getUser();
    setAttribute("USER_ID_NAME", getAttribute("USER_ID_NAME", user.getUserName()));
    setAttribute("USER_ID", getAttribute("USER_ID", user.getUserId()));
    String date = DateUtil.getDateByType(11, new Date());
    setAttribute("LEA_DATE", date);
    
    setAttribute("LEA_TYPE", 
      FormSelectFactory.create("LEA_TYPE").addSelectedValue(getOperaAttributeValue("LEA_TYPE", "")));
    setAttribute("APP_RESULT", 
      FormSelectFactory.create("APP_RESULT").addSelectedValue(getAttributeValue("APP_RESULT", "YES")));
    setAttribute("STATE", 
      FormSelectFactory.create("STATE").addSelectedValue(getOperaAttributeValue("STATE", "drafe")));
  }
  
  public ViewRenderer doSaveAction(DataParam param) {
    String responseText = "fail";
    String operateType = param.get("operaType");
    if ("insert".equals(operateType)) {
      getService().createRecord(param);
      responseText = param.get("LEA_ID");
    } else if ("update".equals(operateType)) {
      getService().updateRecord(param);
      responseText = param.get("LEA_ID");
    } else if ("detail".equals(operateType)) {
      getService().updateRecord(param);
      responseText = param.get("LEA_ID");
    }
    return new AjaxRenderer(responseText);
  }
  
  public ViewRenderer detail(DataParam param) {
    getService().getNowRecord(param);
    return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  @PageAction
  public ViewRenderer submit(DataParam param) {
    param.put("STATE", "submitted");
    getService().updateRecord(param);
    writeSystemLog("提交请假申请");
    return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  @PageAction
  public ViewRenderer approve(DataParam param)
  {
    writeSystemLog("批准请假申请");
    
    DataRow record = getService().getNowRecord(param);
    if (record != null) {
      param.put("USER_CODE", record.get("USER_ID_CODE"));
      if (record.getInt("LEA_TYPE") == 2) {
        getAllanceYear(param);
        double days = Double.parseDouble(getAttributeValue("SAL_ALLOWANCE_YEAR"));
        double wantDays = Double.parseDouble(param.get("LEA_DAYS"));
        if (days < wantDays) {
          setAttribute("errorMsg", "年假天数不足，请拒绝请假！");
          return prepareDisplay(param);
        }
        param.put("DAYS", Double.valueOf(days - wantDays));
        getService().updateAllowanceYear(param);
      }
    }
    
    param.put("STATE", "approved");
    getService().approveRecord(param);
    return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  @PageAction
  public ViewRenderer drafe(DataParam param) {
    param.put("STATE", "drafe");
    getService().updateRecord(param);
    writeSystemLog("拒绝请假申请");
    return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  private void getAllanceYear(DataParam param) {
    DataRow yearDaysRow = getService().getAllowanceYear(param);
    if ((yearDaysRow == null) || (StringUtil.isEmpty((String)yearDaysRow.get("EMP_ANNUAL_LEAVE_DAYS")))) {
      setAttribute("SAL_ALLOWANCE_YEAR", "0");
    } else {
      setAttribute("SAL_ALLOWANCE_YEAR", yearDaysRow.get("EMP_ANNUAL_LEAVE_DAYS"));
    }
  }
  
  protected HrLeaveManage getService()
  {
    return (HrLeaveManage)lookupService(getServiceId());
  }
}
