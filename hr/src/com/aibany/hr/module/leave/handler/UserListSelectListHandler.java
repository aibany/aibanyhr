package com.aibany.hr.module.leave.handler;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.controller.core.PickFillModelHandler;
import com.aibany.hr.cxmodule.UserListSelect;

public class UserListSelectListHandler extends PickFillModelHandler
{
  public UserListSelectListHandler()
  {
    this.serviceId = buildServiceId(UserListSelect.class);
  }
  
  protected void processPageAttributes(DataParam param) {}
  
  protected void initParameters(DataParam param)
  {
    initParamItem(param, "userName", "");
  }
  
  protected UserListSelect getService() {
    return (UserListSelect)lookupService(getServiceId());
  }
}
