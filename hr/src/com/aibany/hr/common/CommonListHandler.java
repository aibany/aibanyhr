package com.aibany.hr.common;

import javax.servlet.http.HttpServletRequest;

public class CommonListHandler extends com.agileai.hotweb.controller.core.StandardListHandler
{
  protected void writeSystemLog(String content)
  {
    String actionType = getActionType();
    String ipAddress = this.request.getRemoteAddr();
    com.agileai.hotweb.domain.core.User user = (com.agileai.hotweb.domain.core.User)getUser();
    String userId = "";
    String userName = "";
    if (user != null) {
      userId = user.getUserCode();
      userName = user.getUserName();
    }
    com.agileai.hotweb.bizmoduler.core.SystemLogService logService = (com.agileai.hotweb.bizmoduler.core.SystemLogService)lookupService("sysLogService");
    logService.insertLogRecord(ipAddress, userId, userName, content, actionType);
  }
}
