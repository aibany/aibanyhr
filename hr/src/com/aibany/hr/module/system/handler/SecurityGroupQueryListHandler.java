package com.aibany.hr.module.system.handler;

import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.QueryModelListHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.module.system.service.SecurityGroupQuery;

public class SecurityGroupQueryListHandler extends QueryModelListHandler
{
  public SecurityGroupQueryListHandler()
  {
    this.serviceId = buildServiceId(SecurityGroupQuery.class);
  }
  
  protected void processPageAttributes(com.agileai.domain.DataParam param) {
    initMappingItem("GRP_STATE", 
      FormSelectFactory.create("SYS_VALID_TYPE").getContent());
  }
  
  @PageAction
  public ViewRenderer addGroupTreeRelation(com.agileai.domain.DataParam param) { String roleId = param.get("roleId");
    String groupIds = param.get("groupIds");
    getService().addGroupTreeRelation(roleId, groupIds.split(","));
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer delGroupTreeRelation(com.agileai.domain.DataParam param) { String roleId = param.get("ROLE_ID");
    String groupId = param.get("GRP_ID");
    getService().delGroupTreeRelation(roleId, groupId);
    return prepareDisplay(param);
  }
  
  protected void initParameters(com.agileai.domain.DataParam param) { initParamItem(param, "roleId", ""); }
  
  protected SecurityGroupQuery getService()
  {
    return (SecurityGroupQuery)lookupService(getServiceId());
  }
}
