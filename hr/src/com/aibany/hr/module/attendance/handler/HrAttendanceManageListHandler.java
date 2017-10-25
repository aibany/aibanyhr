package com.aibany.hr.module.attendance.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.NullRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.common.CommonListHandler;
import com.aibany.hr.common.ExportFileHelper;
import com.aibany.hr.common.PrivilegeHelper;
import com.aibany.hr.cxmodule.HrAttendanceManage;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public class HrAttendanceManageListHandler
  extends CommonListHandler
{
  public HrAttendanceManageListHandler()
  {
    this.editHandlerClazz = HrAttendanceManageEditHandler.class;
    this.serviceId = buildServiceId(HrAttendanceManage.class);
  }
  
  public ViewRenderer prepareDisplay(DataParam param) {
    mergeParam(param);
    initParameters(param);
    
    User user = (User)getUser();
    String paramteDate = param.get("adtDate");
    
    setAttribute("canSignIn", Boolean.valueOf(true));
    setAttribute("canSignOut", Boolean.valueOf(true));
    String currentDate = DateUtil.getDateByType(9);
    
    String eti = param.getString("eti");
    if ("ec".equals(eti)) {
      String ec_efn = param.get("ec_efn");
      CommonHandler.writeSystemLog("导出" + ec_efn, getActionType(), this);
      if (param.getBoolean("theMonth")) {
        fixForMonth(param);
      }
    }
    
    if (paramteDate.equals(currentDate)) {
      DataParam queryParam = new DataParam();
      queryParam.put("currentDate", DateUtil.getDateByType(9, new Date()));
      queryParam.put("currentUser", user.getUserId());
      DataRow record = getService().getRecord(queryParam);
      if (record != null) {
        if (record.get("ATD_IN_TIME") != null) {
          setAttribute("canSignIn", Boolean.valueOf(false));
        }
        if (record.get("ATD_OUT_TIME") != null) {
          setAttribute("canSignOut", Boolean.valueOf(false));
        }
      } else {
        setAttribute("canSignOut", Boolean.valueOf(false));
      }
    } else {
      setAttribute("canSignIn", Boolean.valueOf(false));
      setAttribute("canSignOut", Boolean.valueOf(false));
    }
    
    PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
    if ((!privilegeHelper.isHRMASTER()) && (!privilegeHelper.isApprove()) && (!privilegeHelper.isSalMaster())) {
      param.put("currentUserCode", user.getUserCode());
    } else {
      param.put("currentUserCode", "admin");
      if (StringUtil.isNotEmpty(param.get("userName"))) {
        fixForMonth(param);
        param.replace("currentUserCode", param.get("userName"));
      }
      
      if (user.getUserCode().equals("admin")) {
        setAttribute("hasRight", Boolean.valueOf(true));
      }
    }
    
    setAttributes(param);
    
    if (param.getObject("sdate") != null) {
      param.put("theMonth", "true");
      setAttributes(param);
      param.put("adtDate", "");
    } else {
      param.put("theMonth", "");
      setAttributes(param);
    }
    
    List<DataRow> rsList = getService().findRecords(param);
    setRsList(rsList);
    
    updateAttendanceStatus();
    param.put("adtDate", paramteDate);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(DataParam param) {}
  
  protected void initParameters(DataParam param)
  {
    initParamItem(param, "adtDate", DateUtil.getDateByType(9));
  }
  
  @PageAction
  public ViewRenderer beforeDay(DataParam param) {
    String atdDate = param.get("adtDate");
    Date selectDate = DateUtil.getDate(atdDate);
    Date beforeDate = DateUtil.getDateAdd(selectDate, 1, -1);
    String targetDate = DateUtil.getDateByType(9, beforeDate);
    param.put("adtDate", targetDate);
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer nextDay(DataParam param) {
    String atdDate = param.get("adtDate");
    Date selectDate = DateUtil.getDate(atdDate);
    Date beforeDate = DateUtil.getDateAdd(selectDate, 1, 1);
    String targetDate = DateUtil.getDateByType(9, beforeDate);
    param.put("adtDate", targetDate);
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer theMonth(DataParam param) {
    fixForMonth(param);
    return prepareDisplay(param);
  }
  
  private void fixForMonth(DataParam param) {
    String atdDate = param.get("adtDate");
    Date selectDate = DateUtil.getDate(atdDate);
    Date startDate = DateUtil.getBeginOfMonth(selectDate);
    Date endDate = DateUtil.getEndOfMonth(selectDate);
    param.put("sdate", startDate);
    param.put("edate", endDate);
  }
  
  public ViewRenderer doExportAction(DataParam param)
  {
    String fileType = param.get("fileType");
    String exportFileName = "考勤表";
    try {
      Date adtDate = DateUtil.getDate(param.get("adtDate"));
      param.put("sdate", DateUtil.getDateByType(9, 
        DateUtil.getDateAdd(DateUtil.getBeginOfMonth(adtDate), 2, 0)));
      param.put("edate", DateUtil.getDateByType(9, 
        DateUtil.getDateAdd(DateUtil.getBeginOfMonth(adtDate), 2, 1)));
      List<DataRow> records = getService().attendanceStatisticsRecords(param);
      HashMap model = new HashMap();
      model.put("title", exportFileName);
      String date = DateUtil.getDateByType(9, adtDate).substring(0, 7);
      model.put("title1", date + "月考勤情况");
      String content = "";
      for (int i = 0; i < records.size(); i++) {
        DataRow dataRow = (DataRow)records.get(i);
        content = content + "<tr><td>" + (String)dataRow.get("USER_NAME") + 
          "</td><td style=\"text-align:right\">" + dataRow.get("IN_NUM").toString() + 
          "</td><td style=\"text-align:right\">" + dataRow.get("OUT_NUM").toString() + "</td></tr>";
      }
      model.put("content", content);
      
      ExportFileHelper exportFileHelper = new ExportFileHelper(this.request, this.response);
      String templateDir = exportFileHelper.geTemplateDirPath();
      if ("word".equals(fileType)) {
        String templateFile = "AttendanceDoc.ftl";
        
        ByteArrayInputStream bais = exportFileHelper.buildHtml4Doc(templateDir, templateFile, model);
        exportFileHelper.exportWord(bais, exportFileName + ".doc");
      } else {
        String templateFile = "AttendancePdf.ftl";
        
        String inputFileContent = exportFileHelper.buildHtml4Pdf(templateDir, templateFile, model);
        exportFileHelper.exportPdf(inputFileContent, exportFileName + ".pdf");
      }
      writeSystemLog("导出考勤" + fileType);
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new NullRenderer();
  }
  
  public ViewRenderer doQueryAction(DataParam param) {
    return prepareDisplay(param);
  }
  
  private void updateAttendanceStatus() {
    for (Iterator localIterator = getRsList().iterator(); localIterator.hasNext();) { Object item = localIterator.next();
      DataRow row = (DataRow)item;
      
      row.put("ATD_STATE", "正常");
    }
  }
  
  protected HrAttendanceManage getService() {
    return (HrAttendanceManage)lookupService(getServiceId());
  }
}
