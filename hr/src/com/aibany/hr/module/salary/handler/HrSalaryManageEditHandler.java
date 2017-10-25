package com.aibany.hr.module.salary.handler;

import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.aibany.hr.common.CommonEditHandler;

public class HrSalaryManageEditHandler extends CommonEditHandler
{
  public com.agileai.hotweb.renders.ViewRenderer prepareDisplay(com.agileai.domain.DataParam param)
  {
    String operaType = param.get("operaType");
    if ("approve".equals(operaType)) {
      setAttribute("isComeFromApprove", Boolean.valueOf(true));
      if (!isReqRecordOperaType(operaType)) {
        DataRow record = getService().getRecord(param);
        setAttributes(record);
      }
    } else {
      setAttribute("isComeFromApprove", Boolean.valueOf(false));
    }
    if (isReqRecordOperaType(operaType)) {
      DataRow record = getService().getRecord(param);
      setAttributes(record);
    }
    if (operaType.equals("detail")) {
      setAttribute("isComeFromDetail", Boolean.valueOf(false));
    } else {
      setAttribute("isComeFromDetail", Boolean.valueOf(true));
    }
    
    setAttribute("isComeFromUpdate", Boolean.valueOf(true));
    User user = (User)getUser();
    com.aibany.hr.common.PrivilegeHelper privilegeHelper = new com.aibany.hr.common.PrivilegeHelper(user);
    if (!privilegeHelper.isSalMaster()) {
      setAttribute("hasRight", Boolean.valueOf(false));
    } else {
      setAttribute("hasRight", Boolean.valueOf(true));
    }
    setOperaType(operaType);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  public HrSalaryManageEditHandler()
  {
    this.listHandlerClass = HrSalaryManageListHandler.class;
    this.serviceId = buildServiceId(com.aibany.hr.module.salary.service.HrSalaryManage.class);
  }
  
  protected void processPageAttributes(com.agileai.domain.DataParam param) {
    setAttribute("SAL_STATE", FormSelectFactory.create("SAL_STATE")
      .addSelectedValue(getOperaAttributeValue("SAL_STATE", "")));
  }
  
  protected com.aibany.hr.module.salary.service.HrSalaryManage getService() {
    return (com.aibany.hr.module.salary.service.HrSalaryManage)lookupService(getServiceId());
  }
  
  @PageAction
  public com.agileai.hotweb.renders.ViewRenderer doSaveAction(com.agileai.domain.DataParam param) {
    String operateType = param.get("operaType");
    if ("insert".equals(operateType)) {
      getService().createRecord(param);
    } else if ("update".equals(operateType)) {
      getService().updateRecord(param);
    }
    String masterRecordId = param.get("SAL_ID");
    getService().computeTotalMoney(masterRecordId);
    writeSystemLog("保存薪资");
    return new com.agileai.hotweb.renders.RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  @PageAction
  public com.agileai.hotweb.renders.ViewRenderer approve(com.agileai.domain.DataParam param) {
    param.put("SAL_STATE", "1");
    getService().approveRecord(param);
    writeSystemLog("核准薪资");
    return new com.agileai.hotweb.renders.RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  @PageAction
  public com.agileai.hotweb.renders.ViewRenderer revokeApproval(com.agileai.domain.DataParam param) {
    param.put("SAL_STATE", "0");
    getService().approveRecord(param);
    writeSystemLog("反核准薪资");
    return new com.agileai.hotweb.renders.RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
}
