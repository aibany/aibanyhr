package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.controller.core.TreeAndContentManageEditHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.aibany.hr.module.system.service.WcmGeneralGroup8ContentManage;

public class WcmGeneralResourceEditHandler extends TreeAndContentManageEditHandler
{
  public WcmGeneralResourceEditHandler()
  {
    this.serviceId = buildServiceId(WcmGeneralGroup8ContentManage.class);
    this.tabId = "WcmGeneralResource";
    this.columnIdField = "GRP_ID";
    this.contentIdField = "RES_ID";
  }
  
  protected void processPageAttributes(DataParam param) {
    FormSelect isShareable = FormSelectFactory.create("BOOL_DEFINE");
    String RES_SHAREABLE = getAttributeValue("RES_SHAREABLE", "Y");
    isShareable.addSelectedValue(RES_SHAREABLE);
    setAttribute("RES_SHAREABLE", isShareable);
  }
  
  protected WcmGeneralGroup8ContentManage getService() {
    return (WcmGeneralGroup8ContentManage)lookupService(getServiceId());
  }
}
