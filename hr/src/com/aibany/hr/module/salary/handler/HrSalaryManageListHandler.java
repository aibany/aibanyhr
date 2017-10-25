package com.aibany.hr.module.salary.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.DispatchRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.common.CommonListHandler;
import com.aibany.hr.common.PrivilegeHelper;
import com.aibany.hr.module.salary.service.HrSalaryManage;
import java.util.Date;
import java.util.List;

public class HrSalaryManageListHandler extends CommonListHandler
{
  public HrSalaryManageListHandler()
  {
    this.editHandlerClazz = HrSalaryManageEditHandler.class;
    this.serviceId = buildServiceId(HrSalaryManage.class);
  }
  
  public ViewRenderer prepareDisplay(DataParam param) {
    User user = (User)getUser();
    PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
    if (!privilegeHelper.isSalMaster()) {
      param.put("currentUserCode", user.getUserId());
      setAttribute("hasRight", Boolean.valueOf(false));
    } else {
      param.put("currentUserCode", "");
      setAttribute("hasRight", Boolean.valueOf(true));
    }
    
    String eti = param.getString("eti");
    if ("ec".equals(eti)) {
      String ec_efn = param.get("ec_efn");
      CommonHandler.writeSystemLog("导出" + ec_efn, getActionType(), this);
    }
    
    mergeParam(param);
    initParameters(param);
    setAttribute("canGather", Boolean.valueOf(true));
    String salDate = param.get("salDate");
    String paramDate = salDate.substring(0, 7);
    Date date = DateUtil.getBeginOfMonth(new Date());
    Date beforeDate = DateUtil.getDateAdd(date, 2, -1);
    String currentDate = DateUtil.getDateByType(9, 
      beforeDate);
    String currenDate = currentDate.substring(0, 7);
    if (paramDate.equals(currenDate)) {
      setAttribute("canGather", Boolean.valueOf(true));
    } else {
      setAttribute("canGather", Boolean.valueOf(false));
    }
    
    String year = salDate.substring(0, 4);
    String month = salDate.substring(5, 7);
    DataRow validDays = getService().retrieveValidDays(year, month);
    if (validDays == null) {
      setAttribute("validDays", Boolean.valueOf(false));
    } else {
      setAttribute("validDays", Boolean.valueOf(true));
    }
    param.put(new Object[] { "salYear", year, "salMonth", month });
    
    setAttributes(param);
    List<DataRow> rsList = getService().findRecords(param);
    setRsList(rsList);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(DataParam param) { setAttribute("salState", FormSelectFactory.create("SAL_STATE")
      .addSelectedValue(param.get("salState")));
    initMappingItem("SAL_STATE", FormSelectFactory.create("SAL_STATE")
      .getContent());
  }
  
  protected void initParameters(DataParam param) {
    Date date = DateUtil.getBeginOfMonth(new Date());
    Date beforeDate = DateUtil.getDateAdd(date, 2, -1);
    String salDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(
      DateUtil.getBeginOfMonth(beforeDate), 1, 9));
    String yearMonth = salDate.substring(0, 7);
    initParamItem(param, "salDate", yearMonth);
    initParamItem(param, "salState", "");
  }
  
  protected HrSalaryManage getService() {
    return (HrSalaryManage)lookupService(getServiceId());
  }
  
  @PageAction
  public ViewRenderer gather(DataParam param) {
    String salDate = param.get("salDate");
    String year = salDate.substring(0, 4);
    String month = salDate.substring(5, 7);
    getService().gatherData(year, month);
    
    writeSystemLog("薪资汇总");
    
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer beforeMonth(DataParam param) {
    String salDate = param.get("salDate");
    Date selectDate = DateUtil.getDate(salDate + "-01");
    Date beforeDate = DateUtil.getDateAdd(selectDate, 2, -1);
    String targetDate = DateUtil.getDateByType(9, 
      beforeDate);
    String yearMonth = targetDate.substring(0, 7);
    param.put("salDate", yearMonth);
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer nextMonth(DataParam param) {
    String salDate = param.get("salDate");
    Date selectDate = DateUtil.getDate(salDate + "-01");
    Date beforeDate = DateUtil.getDateAdd(selectDate, 2, 1);
    String targetDate = DateUtil.getDateByType(9, 
      beforeDate);
    String yearMonth = targetDate.substring(0, 7);
    param.put("salDate", yearMonth);
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer doApproveRequestAction(DataParam param) {
    storeParam(param);
    return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&" + 
      "operaType" + "=approve&comeFrome=approve");
  }
  
  @PageAction
  public ViewRenderer revokeApproval(DataParam param) {
    String salId = param.get("SAL_ID");
    getService().revokeApprovalRecords(salId);
    return prepareDisplay(param);
  }
}
