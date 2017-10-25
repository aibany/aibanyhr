package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.controller.core.TreeAndContentColumnEditHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;

public class SecurityGroupEditHandler extends TreeAndContentColumnEditHandler
{
  public SecurityGroupEditHandler()
  {
    this.serviceId = buildServiceId(com.aibany.hr.module.system.service.SecurityGroupManage.class);
    this.columnIdField = "GRP_ID";
    this.columnParentIdField = "GRP_PID";
  }
  
  protected void processPageAttributes(DataParam param) {
    setAttribute("GRP_STATE", 
      FormSelectFactory.create("SYS_VALID_TYPE")
      .addSelectedValue(getOperaAttributeValue("GRP_STATE", 
      "1")));
  }
  
  protected com.aibany.hr.module.system.service.SecurityGroupManage getService() {
    return (com.aibany.hr.module.system.service.SecurityGroupManage)lookupService(getServiceId());
  }
}
