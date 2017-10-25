package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.system.FuncHandler;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.domain.system.Operation;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.ListUtil;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.cxmodule.FunctionTreeManage;
import java.util.ArrayList;
import java.util.List;

public class SecurityAuthorizationConfigHandler
  extends CommonHandler
{
  private FunctionTreeManage getFunctionTreeManage()
  {
    return (FunctionTreeManage)lookupService(FunctionTreeManage.class);
  }
  
  public ViewRenderer prepareDisplay(DataParam param) {
    setAttributes(param);
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    FormSelect roleSelect = getRoleSelect(resourceType, resourceId);
    setAttribute("roleList", roleSelect);
    
    FormSelect groupSelect = getGroupSelect(resourceType, resourceId);
    setAttribute("groupList", groupSelect);
    
    FormSelect userSelect = getUserSelect(resourceType, resourceId);
    setAttribute("userList", userSelect);
    
    setAttribute("resourceType", resourceType);
    setAttribute("resourceId", resourceId);
    
    return new LocalRenderer(getPage());
  }
  
  private FormSelect getRoleSelect(String resourceType, String resourceId) {
    List<DataRow> roleList = getService().retrieveRoleList(resourceType, resourceId);
    FormSelect roleSelect = new FormSelect();
    roleSelect.setKeyColumnName("ROLE_ID");
    roleSelect.setValueColumnName("ROLE_NAME");
    roleSelect.addHasBlankValue(false);
    roleSelect.putValues(roleList);
    return roleSelect;
  }
  
  private FormSelect getUserSelect(String resourceType, String resourceId) {
    List<DataRow> userList = getService().retrieveUserList(resourceType, resourceId);
    FormSelect userSelect = new FormSelect();
    userSelect.setKeyColumnName("USER_ID");
    userSelect.setValueColumnName("USER_NAME");
    userSelect.addHasBlankValue(false);
    userSelect.putValues(userList);
    return userSelect;
  }
  
  private FormSelect getGroupSelect(String resourceType, String resourceId) {
    List<DataRow> groupList = getService().retrieveGroupList(resourceType, resourceId);
    FormSelect groupSelect = new FormSelect();
    groupSelect.setKeyColumnName("GRP_ID");
    groupSelect.setValueColumnName("GRP_NAME");
    groupSelect.addHasBlankValue(false);
    groupSelect.putValues(groupList);
    return groupSelect;
  }
  
  @PageAction
  public ViewRenderer addUserAuthRelation(DataParam param) {
    String responseText = "";
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    String userIds = param.get("userIds");
    List<String> userIdList = new ArrayList();
    ListUtil.addArrayToList(userIdList, userIds.split(","));
    if ("Operation".equals(resourceType)) {
      String operationId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnAddOperationAuth(operationId, resourceTypes, resourceIds);
      getService().addUserAuthRelation(resourceTypes, resourceIds, userIdList);
    }
    else if ("Handler".equals(resourceType)) {
      String handlerId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnAddHandlerAuth(handlerId, resourceTypes, resourceIds);
      getService().addUserAuthRelation(resourceTypes, resourceIds, userIdList);
    }
    else if ("Menu".equals(resourceType)) {
      String menuItemId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnAddMenuAuth(menuItemId, resourceTypes, resourceIds);
      getService().addUserAuthRelation(resourceTypes, resourceIds, userIdList);
    } else {
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      resourceTypes.add(resourceType);
      resourceIds.add(resourceId);
      getService().addUserAuthRelation(resourceTypes, resourceIds, userIdList);
    }
    FormSelect userSelect = getUserSelect(resourceType, resourceId);
    responseText = userSelect.getScriptSyntax("userList");
    return new AjaxRenderer(responseText);
  }
  
  private void packResoucesOnAddOperationAuth(String operationId, List<String> resourceTypes, List<String> resourceIds) {
    Operation operation = getFunctionTreeManage().getOperation(operationId);
    resourceTypes.add("Operation");
    resourceIds.add(operationId);
    
    String handlerId = operation.getHandlerId();
    resourceTypes.add("Handler");
    resourceIds.add(handlerId);
    
    FuncHandler funcHandler = getFunctionTreeManage().getFuncHandler(handlerId);
    String functionId = funcHandler.getFunctionId();
    
    FuncMenu funcMenu = getFunctionTreeManage().getFunction(functionId);
    resourceTypes.add("Menu");
    resourceIds.add(functionId);
    
    buildMenuParent(funcMenu, resourceTypes, resourceIds);
  }
  
  private void packResoucesOnAddHandlerAuth(String handlerId, List<String> resourceTypes, List<String> resourceIds) {
    resourceTypes.add("Handler");
    resourceIds.add(handlerId);
    
    FuncHandler funcHandler = getFunctionTreeManage().getFuncHandler(handlerId);
    String functionId = funcHandler.getFunctionId();
    
    FuncMenu funcMenu = getFunctionTreeManage().getFunction(functionId);
    resourceTypes.add("Menu");
    resourceIds.add(functionId);
    
    buildMenuParent(funcMenu, resourceTypes, resourceIds);
  }
  
  private void packResoucesOnAddMenuAuth(String menuItemId, List<String> resourceTypes, List<String> resourceIds) {
    FuncMenu funcMenu = getFunctionTreeManage().getFunction(menuItemId);
    resourceTypes.add("Menu");
    resourceIds.add(menuItemId);
    buildMenuParent(funcMenu, resourceTypes, resourceIds);
  }
  
  private void buildMenuParent(FuncMenu funcMenu, List<String> resourceTypes, List<String> resourceIds) {
    if (!StringUtil.isNullOrEmpty(funcMenu.getFuncPid())) {
      String parrentId = funcMenu.getFuncPid();
      FuncMenu parentFuncMenu = getFunctionTreeManage().getFunction(parrentId);
      resourceTypes.add("Menu");
      resourceIds.add(parentFuncMenu.getFuncId());
      buildMenuParent(parentFuncMenu, resourceTypes, resourceIds);
    }
  }
  
  @PageAction
  public ViewRenderer addRoleAuthRelation(DataParam param) {
    String responseText = "";
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    String roleIds = param.get("roleIds");
    List<String> roleIdList = new ArrayList();
    ListUtil.addArrayToList(roleIdList, roleIds.split(","));
    
    if ("Operation".equals(resourceType)) {
      String operationId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnAddOperationAuth(operationId, resourceTypes, resourceIds);
      getService().addRoleAuthRelation(resourceTypes, resourceIds, roleIdList);
    }
    else if ("Handler".equals(resourceType)) {
      String handlerId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnAddHandlerAuth(handlerId, resourceTypes, resourceIds);
      getService().addRoleAuthRelation(resourceTypes, resourceIds, roleIdList);
    }
    else if ("Menu".equals(resourceType)) {
      String menuItemId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnAddMenuAuth(menuItemId, resourceTypes, resourceIds);
      getService().addRoleAuthRelation(resourceTypes, resourceIds, roleIdList);
    } else {
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      resourceTypes.add(resourceType);
      resourceIds.add(resourceId);
      getService().addRoleAuthRelation(resourceTypes, resourceIds, roleIdList);
    }
    
    FormSelect roleSelect = getRoleSelect(resourceType, resourceId);
    responseText = roleSelect.getScriptSyntax("roleList");
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer addGroupAuthRelation(DataParam param) {
    String responseText = "";
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    String groupIds = param.get("groupIds");
    List<String> groupIdList = new ArrayList();
    ListUtil.addArrayToList(groupIdList, groupIds.split(","));
    
    if ("Operation".equals(resourceType)) {
      String operationId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnAddOperationAuth(operationId, resourceTypes, resourceIds);
      getService().addGroupAuthRelation(resourceTypes, resourceIds, groupIdList);
    }
    else if ("Handler".equals(resourceType)) {
      String handlerId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnAddHandlerAuth(handlerId, resourceTypes, resourceIds);
      getService().addGroupAuthRelation(resourceTypes, resourceIds, groupIdList);
    }
    else if ("Menu".equals(resourceType)) {
      String menuItemId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnAddMenuAuth(menuItemId, resourceTypes, resourceIds);
      getService().addGroupAuthRelation(resourceTypes, resourceIds, groupIdList);
    } else {
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      resourceTypes.add(resourceType);
      resourceIds.add(resourceId);
      
      getService().addGroupAuthRelation(resourceTypes, resourceIds, groupIdList);
    }
    
    FormSelect groupSelect = getGroupSelect(resourceType, resourceId);
    responseText = groupSelect.getScriptSyntax("groupList");
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer delUserAuthRelation(DataParam param)
  {
    String userId = param.get("userList");
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    
    if ("Menu".equals(resourceType)) {
      String menuItemId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
      
      getService().delUserAuthRelation(resourceTypes, resourceIds, userId);
    }
    else if ("Handler".equals(resourceType)) {
      String handlerId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
      getService().delUserAuthRelation(resourceTypes, resourceIds, userId);
    }
    else if ("Operation".equals(resourceType)) {
      getService().delUserAuthRelation(resourceType, resourceId, userId);
    } else {
      getService().delUserAuthRelation(resourceType, resourceId, userId);
    }
    return prepareDisplay(param);
  }
  
  private void packResoucesOnDelHandlerAuth(String handlerId, List<String> resourceTypes, List<String> resourceIds) {
    resourceTypes.add("Handler");
    resourceIds.add(handlerId);
    
    FuncHandler funcHandler = getFunctionTreeManage().getFuncHandler(handlerId);
    List<Operation> operations = funcHandler.getOperations();
    for (int j = 0; j < operations.size(); j++) {
      Operation operation = (Operation)operations.get(j);
      String operationId = operation.getOperId();
      resourceTypes.add("Operation");
      resourceIds.add(operationId);
    }
  }
  
  private void packResoucesOnDelMenuAuth(String menuItemId, List<String> resourceTypes, List<String> resourceIds)
  {
    resourceTypes.add("Menu");
    resourceIds.add(menuItemId);
    
    FuncMenu menuItem = getFunctionTreeManage().getFunction(menuItemId);
    buildMenuChildren(menuItem, resourceTypes, resourceIds);
  }
  
  private void buildMenuChildren(FuncMenu menuItem, List<String> resourceTypes, List<String> resourceIds) {
    if (menuItem.isFolder()) {
      if (menuItem.getChildren().size() > 0) {
        List<FuncMenu> menuItems = menuItem.getChildren();
        for (int i = 0; i < menuItems.size(); i++) {
          FuncMenu childMenuItem = (FuncMenu)menuItems.get(i);
          String childMenuItemId = childMenuItem.getFuncId();
          resourceTypes.add("Menu");
          resourceIds.add(childMenuItemId);
          
          buildMenuChildren(childMenuItem, resourceTypes, resourceIds);
        }
      }
    } else {
      List<FuncHandler> funcHandlers = menuItem.getHandlers();
      for (int i = 0; i < funcHandlers.size(); i++) {
        FuncHandler funcHandler = (FuncHandler)funcHandlers.get(i);
        String handlerId = funcHandler.getHandlerId();
        resourceTypes.add("Handler");
        resourceIds.add(handlerId);
        
        List<Operation> operations = funcHandler.getOperations();
        for (int j = 0; j < operations.size(); j++) {
          Operation operation = (Operation)operations.get(j);
          String operationId = operation.getOperId();
          resourceTypes.add("Operation");
          resourceIds.add(operationId);
        }
      }
    }
  }
  
  @PageAction
  public ViewRenderer delRoleAuthRelation(DataParam param) {
    String roleId = param.get("roleList");
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    
    if ("Menu".equals(resourceType)) {
      String menuItemId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
      getService().delRoleAuthRelation(resourceTypes, resourceIds, roleId);
    }
    else if ("Handler".equals(resourceType)) {
      String handlerId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
      getService().delRoleAuthRelation(resourceTypes, resourceIds, roleId);
    }
    else if ("Operation".equals(resourceType)) {
      getService().delRoleAuthRelation(resourceType, resourceId, roleId);
    } else {
      getService().delRoleAuthRelation(resourceType, resourceId, roleId);
    }
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer delGroupAuthRelation(DataParam param) {
    String groupId = param.get("groupList");
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    
    if ("Menu".equals(resourceType)) {
      String menuItemId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
      getService().delGroupAuthRelation(resourceTypes, resourceIds, groupId);
    }
    else if ("Handler".equals(resourceType)) {
      String handlerId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
      getService().delGroupAuthRelation(resourceTypes, resourceIds, groupId);
    }
    else if ("Operation".equals(resourceType)) {
      getService().delGroupAuthRelation(resourceType, resourceId, groupId);
    } else {
      getService().delGroupAuthRelation(resourceType, resourceId, groupId);
    }
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer delUserAuthRelations(DataParam param)
  {
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    
    if ("Menu".equals(resourceType)) {
      String menuItemId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
      getService().delUserAuthRelations(resourceTypes, resourceIds);
    }
    else if ("Handler".equals(resourceType)) {
      String handlerId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
      getService().delUserAuthRelations(resourceTypes, resourceIds);
    }
    else if ("Operation".equals(resourceType)) {
      getService().delUserAuthRelations(resourceType, resourceId);
    } else {
      getService().delUserAuthRelations(resourceType, resourceId);
    }
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer delRoleAuthRelations(DataParam param) {
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    
    if ("Menu".equals(resourceType)) {
      String menuItemId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
      
      getService().delRoleAuthRelations(resourceTypes, resourceIds);
    }
    else if ("Handler".equals(resourceType)) {
      String handlerId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
      getService().delRoleAuthRelations(resourceTypes, resourceIds);
    }
    else if ("Operation".equals(resourceType)) {
      getService().delRoleAuthRelations(resourceType, resourceId);
    } else {
      getService().delRoleAuthRelations(resourceType, resourceId);
    }
    return prepareDisplay(param);
  }
  
  @PageAction
  public ViewRenderer delGroupAuthRelations(DataParam param) {
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    
    if ("Menu".equals(resourceType)) {
      String menuItemId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
      
      getService().delGroupAuthRelations(resourceTypes, resourceIds);
    }
    else if ("Handler".equals(resourceType)) {
      String handlerId = resourceId;
      List<String> resourceTypes = new ArrayList();
      List<String> resourceIds = new ArrayList();
      packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
      getService().delGroupAuthRelations(resourceTypes, resourceIds);
    }
    else if ("Operation".equals(resourceType)) {
      getService().delGroupAuthRelations(resourceType, resourceId);
    } else {
      getService().delGroupAuthRelations(resourceType, resourceId);
    }
    return prepareDisplay(param);
  }
  
  protected SecurityAuthorizationConfig getService() {
    return (SecurityAuthorizationConfig)lookupService(SecurityAuthorizationConfig.class);
  }
}
