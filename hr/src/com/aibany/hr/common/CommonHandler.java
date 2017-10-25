package com.aibany.hr.common;

import com.agileai.hotweb.bizmoduler.core.SystemLogService;
import com.agileai.hotweb.controller.core.BaseHandler;
import com.agileai.hotweb.controller.core.SimpleHandler;
import javax.servlet.http.HttpServletRequest;

public class CommonHandler extends SimpleHandler
{
  private static SystemLogService logService = null;
  
  public CommonHandler()
  {
    logService = (SystemLogService)lookupService("sysLogService");
  }
  
  public static void writeSystemLog(String content, String userId, String userName) {
    logService.insertLogRecord("", userId, userName, content, "");
  }
  
  protected void writeSystemLog(String content) {
    String actionType = getActionType();
    String ipAddress = "";
    if (this.request != null) {
      ipAddress = this.request.getRemoteAddr();
    }
    com.agileai.hotweb.domain.core.User user = (com.agileai.hotweb.domain.core.User)getUser();
    String userId = "";
    String userName = "";
    if (user != null) {
      userId = user.getUserCode();
      userName = user.getUserName();
    }
    logService.insertLogRecord(ipAddress, userId, userName, content, actionType);
  }
  
  public static void writeSystemLog(String content, String actionType, BaseHandler handler) {
    String ipAddress = handler.getRequest().getRemoteAddr();
    com.agileai.hotweb.domain.core.User user = (com.agileai.hotweb.domain.core.User)handler.getUser();
    String userId = "";
    String userName = "";
    if (user != null) {
      userId = user.getUserCode();
      userName = user.getUserName();
    }
    logService.insertLogRecord(ipAddress, userId, userName, content, actionType);
  }
}
