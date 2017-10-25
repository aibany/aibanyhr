package com.aibany.hr.controller;

import com.agileai.common.AppConfig;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.frame.FunctionManage;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.common.BeanFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.domain.core.Resource;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.common.CommonHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MenuDataProviderHandler
  extends CommonHandler
{
  public ViewRenderer prepareDisplay(DataParam param)
  {
    String userId = param.get("userId");
    String folderId = param.get("folderId");
    SecurityAuthorizationConfig authorizationConfig = 
      (SecurityAuthorizationConfig)lookupService("securityAuthorizationConfigService");
    DataRow userRow = authorizationConfig.retrieveUserRecord(userId);
    User user = new User();
    initAuthedUser(userRow, user);
    List<FuncMenu> funcRecords = getAuthedFuncList(user);
    
    TreeBuilder treeBuilder = new TreeBuilder(funcRecords, "funcId", "funcName", "funcPid");
    treeBuilder.setTypeKey("funcType");
    treeBuilder.setPojoList(true);
    treeBuilder.setRootId(folderId);
    TreeModel treeModel = treeBuilder.buildTreeModel();
    List<TreeModel> treeModels = treeModel.getChildren();
    JSONObject jsonObject = new JSONObject();
    try {
      JSONArray jsonArray = new JSONArray();
      jsonObject.put("menus", jsonArray);
      
      BeanFactory beanFactory = BeanFactory.instance();
      AppConfig appConfig = beanFactory.getAppConfig();
      String menuURLPrefix = appConfig.getConfig("GlobalConfig", "MenuURLPrefix");
      
      for (int i = 0; i < treeModels.size(); i++) {
        TreeModel topChildModel = (TreeModel)treeModels.get(i);
        JSONObject topJsonChild = new JSONObject();
        jsonArray.put(topJsonChild);
        buildJsonObjects(topChildModel, topJsonChild, menuURLPrefix);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    String responseText = jsonObject.toString();
    return new AjaxRenderer(responseText);
  }
  
  private void buildJsonObjects(TreeModel treeModel, JSONObject jsonObject, String menuURLPrefix) throws JSONException
  {
    jsonObject.put("id", treeModel.getId());
    jsonObject.put("text", treeModel.getName());
    String funcURL = (String)treeModel.getProperty().get("funcUrl");
    if (funcURL.startsWith("index")) {
      funcURL = menuURLPrefix + funcURL + "&__function_id__=" + treeModel.getId();
    }
    jsonObject.put("url", funcURL);
    jsonObject.put("height", "");
    
    List<TreeModel> children = treeModel.getChildren();
    if (!children.isEmpty()) {
      JSONArray jsonArray = new JSONArray();
      jsonObject.put("menus", jsonArray);
      for (TreeModel child : children) {
        JSONObject jsonChild = new JSONObject();
        jsonArray.put(jsonChild);
        buildJsonObjects(child, jsonChild, menuURLPrefix);
      }
    }
  }
  
  private List<FuncMenu> getAuthedFuncList(User user)
  {
    List<FuncMenu> result = null;
    FunctionManage functionTreeManage = (FunctionManage)lookupService("functionTreeManageService");
    if (user.isAdmin()) {
      result = functionTreeManage.getFuncMenuList();
    } else {
      List<FuncMenu> funcs = new ArrayList();
      List<FuncMenu> funcList = functionTreeManage.getFuncMenuList();
      for (int i = 0; i < funcList.size(); i++) {
        FuncMenu function = (FuncMenu)funcList.get(i);
        String functionId = function.getFuncId();
        if (user.containResouce("Menu", functionId)) {
          funcs.add(function);
        }
      }
      result = funcs;
    }
    return result;
  }
  
  private void initAuthedUser(DataRow userRow, User user) {
    BeanFactory beanFactory = BeanFactory.instance();
    SecurityAuthorizationConfig authorizationConfig = 
      (SecurityAuthorizationConfig)beanFactory.getBean("securityAuthorizationConfigService");
    String userId = userRow.stringValue("USER_ID");
    String userCode = userRow.stringValue("USER_CODE");
    String userName = userRow.stringValue("USER_NAME");
    user.setUserId(userId);
    user.setUserCode(userCode);
    user.setUserName(userName);
    
    FunctionManage functionTreeManage = (FunctionManage)beanFactory.getBean("functionTreeManageService");
    List<String> menuItemIdList = authorizationConfig.retrieveMenuIdList(userId);
    
    if ((menuItemIdList != null) && 
      (menuItemIdList.contains("00000000-0000-0000-00000000000000000"))) {
      String functionId = "00000000-0000-0000-00000000000000000";
      FuncMenu funcMenu = functionTreeManage.getFunction(functionId);
      HashMap<String, Resource> menuResources = getResourceRef(user, "Menu");
      
      Resource resource = new Resource();
      resource.setResouceType("Menu");
      resource.setResouceId(funcMenu.getFuncId());
      resource.setResouceName(funcMenu.getFuncName());
      menuResources.put(funcMenu.getFuncId(), resource);
      
      List<FuncMenu> children = funcMenu.getChildren();
      processFuncMenu(user, children, menuItemIdList);
    }
  }
  
  private void processFuncMenu(User user, List<FuncMenu> funcMenuList, List<String> funcMenuIdList)
  {
    for (int i = 0; i < funcMenuList.size(); i++) {
      FuncMenu funcMenu = (FuncMenu)funcMenuList.get(i);
      String curFuncId = funcMenu.getFuncId();
      if (funcMenuIdList.contains(curFuncId)) {
        HashMap<String, Resource> menuResources = getResourceRef(user, "Menu");
        Resource menuResource = new Resource();
        menuResource.setResouceType("Menu");
        String menuId = funcMenu.getFuncId();
        menuResource.setResouceId(menuId);
        menuResource.setResouceName(funcMenu.getFuncName());
        menuResources.put(menuId, menuResource);
      }
      else {
        loopFuncMenuList(funcMenu, funcMenuList, funcMenuIdList, user);
      }
      
      if ((user.containResouce("Menu", curFuncId)) && 
        (funcMenu.isFolder())) {
        List<FuncMenu> curFuncMenuList = funcMenu.getChildren();
        if (curFuncMenuList.size() > 0) {
          processFuncMenu(user, curFuncMenuList, funcMenuIdList);
        }
      }
    }
  }
  
  private void loopFuncMenuList(FuncMenu curMenuItem, List<FuncMenu> funcMenuList, List<String> funcMenuIdList, User user)
  {
    boolean isMatched = false;
    for (int i = 0; i < funcMenuList.size(); i++) {
      FuncMenu menuItem = (FuncMenu)funcMenuList.get(i);
      String menuItemId = menuItem.getFuncId();
      if (funcMenuIdList.contains(menuItemId)) {
        isMatched = true;
        break;
      }
    }
    
    if (!isMatched) {
      HashMap<String, Resource> menuResources = getResourceRef(user, "Menu");
      Resource menuResource = new Resource();
      menuResource.setResouceType("Menu");
      String menuId = curMenuItem.getFuncId();
      menuResource.setResouceId(menuId);
      menuResource.setResouceName(curMenuItem.getFuncName());
      menuResources.put(menuId, menuResource);
    }
  }
  
  private HashMap<String, Resource> getResourceRef(User user, String resourceType)
  {
    HashMap<String, Resource> result = null;
    if (!user.getResoucesRefs().containsKey(resourceType)) {
      HashMap<String, Resource> temp = new HashMap();
      user.getResoucesRefs().put(resourceType, temp);
    }
    result = (HashMap)user.getResoucesRefs().get(resourceType);
    return result;
  }
}
