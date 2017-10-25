package com.aibany.hr.module.system.handler;

import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;
import com.agileai.hotweb.controller.core.TreeAndContentManageEditHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.CryptionUtil;
import java.util.Map;

public class SecurityUserEditHandler extends TreeAndContentManageEditHandler
{
  public SecurityUserEditHandler()
  {
    this.serviceId = buildServiceId(com.aibany.hr.module.system.service.SecurityGroupManage.class);
    this.tabId = "SecurityUser";
    this.columnIdField = "GRP_ID";
    this.contentIdField = "USER_ID";
  }
  
  protected void processPageAttributes(com.agileai.domain.DataParam param) {
    setAttribute("USER_SEX", 
      FormSelectFactory.create("USER_SEX")
      .addSelectedValue(getOperaAttributeValue("USER_SEX", 
      "M")));
    setAttribute("USER_STATE", 
      FormSelectFactory.create("SYS_VALID_TYPE")
      .addSelectedValue(getOperaAttributeValue("USER_STATE", 
      "1")));
  }
  
  protected com.aibany.hr.module.system.service.SecurityGroupManage getService() {
    return (com.aibany.hr.module.system.service.SecurityGroupManage)lookupService(getServiceId());
  }
  
  public ViewRenderer doSaveAction(com.agileai.domain.DataParam param) {
    String rspText = "success";
    TreeAndContentManage service = getService();
    String operateType = param.get("operaType");
    
    if ("insert".equals(operateType)) {
      String colIdField = (String)service.getTabIdAndColFieldMapping().get(this.tabId);
      String USER_PWD = param.get("USER_PWD");
      param.put("USER_PWD", CryptionUtil.md5Hex(USER_PWD));
      String columnId = param.get(this.columnIdField);
      param.put(colIdField, columnId);
      getService().createtContentRecord(this.tabId, param);
    }
    else if ("update".equals(operateType)) {
      getService().updatetContentRecord(this.tabId, param);
    }
    return new AjaxRenderer(rspText);
  }
}
