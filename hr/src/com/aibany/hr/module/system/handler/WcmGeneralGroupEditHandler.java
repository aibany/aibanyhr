package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.controller.core.TreeAndContentColumnEditHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.aibany.hr.module.system.service.WcmGeneralGroup8ContentManage;

public class WcmGeneralGroupEditHandler extends TreeAndContentColumnEditHandler
{
  public WcmGeneralGroupEditHandler()
  {
    this.serviceId = buildServiceId(WcmGeneralGroup8ContentManage.class);
    this.columnIdField = "GRP_ID";
    this.columnParentIdField = "GRP_PID";
  }
  
  protected void processPageAttributes(DataParam param) {
    FormSelect isSystem = FormSelectFactory.create("BOOL_DEFINE");
    String GRP_IS_SYSTEM = getAttributeValue("GRP_IS_SYSTEM", "Y");
    isSystem.addSelectedValue(GRP_IS_SYSTEM);
    setAttribute("GRP_IS_SYSTEM", isSystem);
    setAttribute("GRP_RES_TYPE_DESC", getAttribute("GRP_RES_TYPE_DESC", "所有"));
    setAttribute("GRP_RES_TYPE_EXTS", getAttribute("GRP_RES_TYPE_EXTS", "*.*"));
    setAttribute("GRP_RES_SIZE_LIMIT", getAttribute("GRP_RES_SIZE_LIMIT", "2M"));
  }
  
  protected WcmGeneralGroup8ContentManage getService() {
    return (WcmGeneralGroup8ContentManage)lookupService(getServiceId());
  }
}
