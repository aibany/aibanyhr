package com.aibany.hr.module.system.service;

import com.agileai.hotweb.bizmoduler.core.MasterSubService;
import java.util.List;

public abstract interface HandlerManage
  extends MasterSubService
{
  public abstract List<String> retrieveHandlerIdList(String paramString);
  
  public abstract List<String> retrieveUserIdList(String paramString);
  
  public abstract List<String> retrieveRoleIdList(String paramString);
  
  public abstract List<String> retrieveGroupIdList(String paramString);
}
