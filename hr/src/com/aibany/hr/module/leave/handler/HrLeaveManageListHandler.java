package com.aibany.hr.module.leave.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
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
import com.aibany.hr.module.leave.service.HrLeaveManage;
import java.util.Date;
import java.util.List;

public class HrLeaveManageListHandler
  extends CommonListHandler
{
  public HrLeaveManageListHandler()
  {
    this.editHandlerClazz = HrLeaveManageEditHandler.class;
    this.serviceId = buildServiceId(HrLeaveManage.class);
  }
  
  protected void processPageAttributes(DataParam param) {
    initMappingItem("LEA_TYPE", 
      FormSelectFactory.create("LEA_TYPE").getContent());
    initMappingItem("APP_RESULT", 
      FormSelectFactory.create("APP_RESULT").getContent());
    initMappingItem("STATE", 
      FormSelectFactory.create("STATE").getContent());
    setAttribute("STATE", FormSelectFactory.create("STATE")
      .addSelectedValue(param.get("STATE")));
  }
  
  public ViewRenderer prepareDisplay(DataParam param) {
    User user = (User)getUser();
    PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
    if (privilegeHelper.isApprove()) {
      param.put("currentUserId", "");
      setAttribute("hasRight", Boolean.valueOf(true));
      setAttribute("userId", user.getUserId());
    } else {
      param.put("currentUserId", user.getUserId());
      setAttribute("hasRight", Boolean.valueOf(false));
    }
    
    String eti = param.getString("eti");
    if ("ec".equals(eti)) {
      String ec_efn = param.get("ec_efn");
      CommonHandler.writeSystemLog("导出" + ec_efn, getActionType(), this);
    }
    
    boolean listshow = false;
    if (param.get("sdate") == null) {
      listshow = true;
    }
    
    mergeParam(param);
    initParameters(param);
    
    setAttribute("canSignIn", Boolean.valueOf(true));
    DataParam queryParam = new DataParam();
    queryParam.put("leaDate", 
      DateUtil.getDateByType(9, new Date()));
    if (listshow) {
      param.put("edate", DateUtil.getDateByType(9, new Date(new Date().getTime() + 604800000L)));
      queryParam.put("edate", param.get("edate"));
    }
    queryParam.put("currentUser", user.getUserId());
    setAttributes(param);
    List<DataRow> rsList = getService().findRecords(param);
    setRsList(rsList);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  public ViewRenderer doInsertRequestAction(DataParam param) { String operaType = param.get("operaType");
    User user = (User)getUser();
    if ("insert".equals(operaType)) {
      DataParam queryParam = new DataParam();
      queryParam.put("currentDate", DateUtil.getDateByType(
        9, new Date()));
      queryParam.put("USER_ID", user.getUserId());
      DataRow record = getService().getNowRecord(param);
      param.putAll(record);
    }
    storeParam(param);
    return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&" + "operaType" + "=" + "insert");
  }
  
  public ViewRenderer UpdateRequest(DataParam param) { String operaType = param.get("operaType");
    if ("update".equals(operaType)) {
      DataParam queryParam = new DataParam();
      queryParam.put("currentDate", DateUtil.getDateByType(
        9, new Date()));
      queryParam.put("LEA_ID", param.get("LEA_ID"));
      DataRow record = getService().getNowRecord(param);
      param.putAll(record);
      if (record != null) {
        if (record.get("LEA_TYPE") != null) {
          setAttribute("canSignIn", Boolean.valueOf(true));
        }
        if (record.get("LEA_APP_OPINION") != null) {
          setAttribute("canApprove", Boolean.valueOf(false));
        }
      }
    }
    
    storeParam(param);
    return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&" + 
      "operaType" + "=" + "update");
  }
  
  public ViewRenderer doApproveRequestAction(DataParam param) { storeParam(param);
    return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&" + 
      "operaType" + "=approve&comeFrome=approve");
  }
  
  protected void initParameters(DataParam param) {
    initParamItem(param, "STATE", "");
    initParamItem(
      param, 
      "sdate", 
      DateUtil.getDateByType(9, 
      DateUtil.getBeginOfMonth(new Date())));
    initParamItem(
      param, 
      "edate", 
      DateUtil.getDateByType(9, 
      DateUtil.getDateAdd(new Date(), 1, 1)));
    initParamItem(param, "user_Name", "");
  }
  
  protected HrLeaveManage getService() {
    return (HrLeaveManage)lookupService(getServiceId());
  }
}
