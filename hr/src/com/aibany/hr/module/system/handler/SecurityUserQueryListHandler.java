package com.aibany.hr.module.system.handler;

import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.QueryModelListHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.module.system.service.SecurityUserQuery;

public class SecurityUserQueryListHandler extends QueryModelListHandler
{
  public SecurityUserQueryListHandler()
  {
    this.serviceId = buildServiceId(SecurityUserQuery.class);
  }
  
  protected void processPageAttributes(com.agileai.domain.DataParam param) {
    initMappingItem("USER_STATE", 
      FormSelectFactory.create("SYS_VALID_TYPE").getContent());
  }
  
  @PageAction
  public ViewRenderer addUserTreeRelation(com.agileai.domain.DataParam param) { String roleId = param.get("roleId");
    String userIds = param.get("userIds");
    getService().addUserTreeRelation(roleId, userIds.split(","));
    return prepareDisplay(param);
  }
  
  public void addUserRole(com.agileai.domain.DataParam param) {
    String roleId = param.get("ROLE_ID");
    String userId = param.get("USER_ID");
    getService().addUserTreeRelation(roleId, userId.split(","));
  }
  
  @PageAction
  public ViewRenderer delUserTreeRelation(com.agileai.domain.DataParam param) { String roleId = param.get("ROLE_ID");
    String userId = param.get("USER_ID");
    getService().delUserTreeRelation(roleId, userId);
    return prepareDisplay(param);
  }
  
  protected void initParameters(com.agileai.domain.DataParam param) { initParamItem(param, "roleId", ""); }
  
  protected SecurityUserQuery getService()
  {
    return (SecurityUserQuery)lookupService(getServiceId());
  }
}
