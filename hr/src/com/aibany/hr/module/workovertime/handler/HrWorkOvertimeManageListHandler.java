package com.aibany.hr.module.workovertime.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.DispatchRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.common.CommonListHandler;
import com.aibany.hr.common.PrivilegeHelper;
import com.aibany.hr.module.workovertime.service.HrWorkOvertimeManage;
import java.util.Date;
import java.util.List;

public class HrWorkOvertimeManageListHandler
  extends CommonListHandler
{
  public HrWorkOvertimeManageListHandler()
  {
    this.editHandlerClazz = HrWorkOvertimeManageEditHandler.class;
    this.serviceId = buildServiceId(HrWorkOvertimeManage.class);
  }
  
  protected void processPageAttributes(DataParam param) {
    initMappingItem("APP_RESULT", 
      FormSelectFactory.create("APP_RESULT").getContent());
    initMappingItem("STATE", 
      FormSelectFactory.create("STATE").getContent());
    setAttribute("STATE", FormSelectFactory.create("STATE")
      .addSelectedValue(param.get("STATE")));
  }
  
  public ViewRenderer prepareDisplay(DataParam param)
  {
    User user = (User)getUser();
    PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
    if ((privilegeHelper.isApprove()) || (user.getUserCode().equals("admin"))) {
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
    
    if (listshow) {
      param.put("edate", DateUtil.getDateByType(9, new Date(new Date().getTime() + 604800000L)));
    }
    
    setAttribute("canSignIn", Boolean.valueOf(true));
    setAttributes(param);
    List<DataRow> rsList = getService().findRecords(param);
    
    setRsList(rsList);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  public ViewRenderer doInsertRequestAction(DataParam param) {
    storeParam(param);
    return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&" + "operaType" + "=" + "insert");
  }
  
  public ViewRenderer UpdateRequest(DataParam param) {
    storeParam(param);
    return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&" + 
      "operaType" + "=" + "update");
  }
  
  public ViewRenderer doDetailRequestAction(DataParam param) { storeParam(param);
    return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&" + 
      "operaType" + "=detail&comeFrome=detail");
  }
  
  public ViewRenderer doApproveRequestAction(DataParam param) { storeParam(param);
    return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&" + 
      "operaType" + "=approve&comeFrome=approve");
  }
  
  public ViewRenderer doRevokeApproveRequestAction(DataParam param) { storeParam(param);
    return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&" + 
      "operaType" + "=revokeApproval&comeFrome=revokeApproval");
  }
  
  public ViewRenderer doDeleteAction(DataParam param) { storeParam(param);
    getService().deletRecord(param);
    return new RedirectRenderer(getHandlerURL(getClass()));
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
  
  protected HrWorkOvertimeManage getService() {
    return (HrWorkOvertimeManage)lookupService(getServiceId());
  }
}
