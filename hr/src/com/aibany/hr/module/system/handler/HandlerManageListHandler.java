package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.controller.core.MasterSubListHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.cxmodule.FunctionTreeManage;
import com.aibany.hr.module.system.service.HandlerManage;
import java.util.ArrayList;
import java.util.List;

public class HandlerManageListHandler
  extends MasterSubListHandler
{
  public HandlerManageListHandler()
  {
    this.editHandlerClazz = HandlerManageEditHandler.class;
    this.serviceId = buildServiceId(HandlerManage.class);
  }
  
  private SecurityAuthorizationConfig getSecurityAuthorizationConfig() {
    return (SecurityAuthorizationConfig)lookupService(SecurityAuthorizationConfig.class);
  }
  
  @PageAction
  public ViewRenderer synchronousSecurity(DataParam param) {
    String funcId = param.get("funcId");
    List<String> handlerIdList = getService().retrieveHandlerIdList(funcId);
    
    List<String> userIdList = getService().retrieveUserIdList(funcId);
    List<String> roleIdList = getService().retrieveRoleIdList(funcId);
    List<String> groupIdList = getService().retrieveGroupIdList(funcId);
    
    List<String> resourceTypes = new ArrayList();
    List<String> resourceIds = new ArrayList();
    for (int i = 0; i < handlerIdList.size(); i++) {
      String resourceId = (String)handlerIdList.get(i);
      String handlerId = resourceId;
      
      resourceTypes.add("Handler");
      resourceIds.add(handlerId);
    }
    if (!userIdList.isEmpty()) {
      getSecurityAuthorizationConfig().addUserAuthRelation(resourceTypes, resourceIds, userIdList);
    }
    if (!roleIdList.isEmpty()) {
      getSecurityAuthorizationConfig().addRoleAuthRelation(resourceTypes, resourceIds, roleIdList);
    }
    if (!groupIdList.isEmpty()) {
      getSecurityAuthorizationConfig().addGroupAuthRelation(resourceTypes, resourceIds, groupIdList);
    }
    return prepareDisplay(param);
  }
  
  protected void processPageAttributes(DataParam param) {
    initMappingItem("HANLER_TYPE", 
      FormSelectFactory.create("HANDLER_TYPE").getContent());
  }
  
  public ViewRenderer doDeleteAction(DataParam param) { ((FunctionTreeManage)lookupService(FunctionTreeManage.class)).clearFuncTreeCache();
    return super.doDeleteAction(param);
  }
  
  protected void initParameters(DataParam param) { initParamItem(param, "funcId", ""); }
  
  protected HandlerManage getService()
  {
    return (HandlerManage)lookupService(getServiceId());
  }
}
