package com.aibany.hr.module.information.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.MasterSubListHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.DispatchRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.common.PrivilegeHelper;
import com.aibany.hr.module.information.service.HrEmployeeManage;
import com.aibany.hr.module.leaverecord.service.HrLeaveRecordManage;
import com.aibany.utils.RequestDayType;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HrEmployeeManageListHandler
  extends MasterSubListHandler
{
  public HrEmployeeManageListHandler()
  {
    this.editHandlerClazz = HrEmployeeManageEditHandler.class;
    this.serviceId = buildServiceId(HrEmployeeManage.class);
  }
  
  public ViewRenderer prepareDisplay(DataParam param) { User user = (User)getUser();
    PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
    if ((!privilegeHelper.isHRMASTER()) && (!privilegeHelper.isApprove())) {
      param.put("currentUserCode", user.getUserCode());
      setAttribute("hasRight", Boolean.valueOf(true));
    } else {
      param.put("currentUserCode", "");
    }
    
    String eti = param.getString("eti");
    if ("ec".equals(eti)) {
      String ec_efn = param.get("ec_efn");
      CommonHandler.writeSystemLog("导出" + ec_efn, getActionType(), this);
    }
    
    System.out.println(RequestDayType.getTomcatWebappsPath(this.request));
    mergeParam(param);
    initParameters(param);
    setAttributes(param);
    List<DataRow> rsList = getService().findMasterRecords(param);
    setRsList(rsList);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(DataParam param) {
    setAttribute("empSex", 
      FormSelectFactory.create("USER_SEX")
      .addSelectedValue(param.get("empSex")));
    initMappingItem("EMP_SEX", 
      FormSelectFactory.create("USER_SEX").getContent());
    initMappingItem("EMP_EDUCATION", 
      FormSelectFactory.create("EMP_EDUCATION").getContent());
    initMappingItem("EMP_STATE", 
      FormSelectFactory.create("EMP_STATE").getContent());
    setAttribute("EMP_STATE", FormSelectFactory.create("EMP_STATE")
      .addSelectedValue(param.get("EMP_STATE")));
  }
  
  public ViewRenderer doApproveRequestAction(DataParam param)
  {
    storeParam(param);
    return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&" + 
      "operaType" + "=approve&comeFrome=approve");
  }
  
  protected void initParameters(DataParam param) { initParamItem(param, "EMP_STATE", "");
    initParamItem(param, "empSex", "");
    initParamItem(param, "empName", "");
  }
  
  protected HrEmployeeManage getService() {
    return (HrEmployeeManage)lookupService(getServiceId());
  }
  
  protected HrLeaveRecordManage getLeaveRecordService() {
    return (HrLeaveRecordManage)lookupService(buildServiceId(HrLeaveRecordManage.class));
  }
  
  @PageAction
  public ViewRenderer revokeApproval(DataParam param) {
    String empId = param.get("EMP_ID");
    getService().revokeApprovalRecords(empId);
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer leaveReason(DataParam param)
  {
    DataRow row = getService().getMasterRecord(param);
    
    if (row != null) {
      List<Map<String, Object>> recordList = new ArrayList();
      Map<String, Object> map = new HashMap();
      map.put("EMP_CODE", row.get("EMP_CODE"));
      map.put("EMP_NAME", row.get("EMP_NAME"));
      map.put("EMP_JOB", row.get("EMP_NOW_DEPT_NAME") + "-" + row.get("EMP_NOW_JOB"));
      map.put("LEAVE_RESON", param.get("reason"));
      map.put("OP_TIME", new Date());
      map.put("IN_TIME", row.get("EMP_INDUCTION_TIME"));
      map.put("OP_NAME", ((User)getUser()).getUserName());
      recordList.add(map);
      getLeaveRecordService().insertRecords(recordList);
    }
    
    return new AjaxRenderer("success");
  }
}
