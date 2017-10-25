package com.aibany.hr.controller;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.common.HotwebAuthHelper;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonHandler;
import java.util.HashMap;

public class MenuTreeHandler
  extends CommonHandler
{
  public static final String FUNC_ID_TAG = "funcId";
  
  public ViewRenderer prepareDisplay(DataParam param)
  {
    User user = (User)getUser();
    HotwebAuthHelper menuHelper = new HotwebAuthHelper(user);
    String menuTreeSyntax = menuHelper.getTreeSyntax();
    setAttribute("menuTreeSyntax", menuTreeSyntax);
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer showFunction(DataParam param) {
    String funcId = param.get("funcId");
    User user = (User)getUser();
    HotwebAuthHelper userHelper = new HotwebAuthHelper(user);
    userHelper.setCurrentFuncId(funcId);
    FuncMenu function = userHelper.getFunction(funcId);
    
    String funcURL = function.getFuncUrl();
    String handlerURL = funcURL;
    
    getSessionAttributes().remove("PARAM_TRACE");
    StringBuffer responseText = new StringBuffer();
    StringBuffer script = new StringBuffer();
    script.append("parent.mainFrame.location.href='").append(handlerURL).append("';");
    String currentVisitPath = buildCurrentPath(function);
    script.append("parent.topright.document.getElementById('currentPath').innerHTML=\"").append(currentVisitPath).append("\";");
    responseText.append(script);
    return new AjaxRenderer(responseText.toString());
  }
  
  private String buildCurrentPath(FuncMenu function) {
    String result = null;
    StringBuffer path = new StringBuffer();
    
    String funcURL = function.getFuncUrl();
    String funcName = function.getFuncName();
    path.append("<a href=javascript:void(parent.mainFrame.location.href='").append(funcURL).append("')>").append(funcName).append("</a>");
    
    String funcParentId = function.getFuncPid();
    if (!StringUtil.isNullOrEmpty(funcParentId)) {
      User user = (User)getUser();
      HotwebAuthHelper userHelper = new HotwebAuthHelper(user);
      FuncMenu parent = userHelper.getFunction(funcParentId);
      if (parent != null) {
        uploopMenuItem(path, parent);
      }
    }
    result = path.toString();
    return result;
  }
  
  private void uploopMenuItem(StringBuffer path, FuncMenu function) { StringBuffer temp = new StringBuffer();
    if (function.isFolder()) {
      if (StringUtil.isNullOrEmpty(function.getFuncPid())) {
        String funcURL = "index?MainWin";
        temp.append("<a href=javascript:showPage('").append(funcURL).append("')>").append(function.getFuncName()).append("</a>&nbsp;--> &nbsp;");
      } else {
        temp.append(function.getFuncName()).append("&nbsp;-->&nbsp;");
      }
    } else {
      temp.append("<a href=javascript:showPage('").append(function.getFuncUrl()).append("')>").append(function.getFuncName()).append("</a> &nbsp; --> &nbsp;");
    }
    path.insert(0, temp.toString());
    String funcParentId = function.getFuncPid();
    if (!StringUtil.isNullOrEmpty(funcParentId)) {
      User user = (User)getUser();
      HotwebAuthHelper userHelper = new HotwebAuthHelper(user);
      FuncMenu parent = userHelper.getFunction(funcParentId);
      uploopMenuItem(path, parent);
    }
  }
}
