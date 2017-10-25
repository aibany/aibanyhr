package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeAndContentManageListHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.aibany.hr.module.system.service.SecurityGroupManage;
import java.util.ArrayList;
import java.util.List;

public class SecurityGroupListHandler
  extends TreeAndContentManageListHandler
{
  public SecurityGroupListHandler()
  {
    this.serviceId = buildServiceId(SecurityGroupManage.class);
    this.rootColumnId = "00000000-0000-0000-00000000000000000";
    this.defaultTabId = "_base_";
    this.columnIdField = "GRP_ID";
    this.columnNameField = "GRP_NAME";
    this.columnParentIdField = "GRP_PID";
    this.columnSortField = "GRP_SORT";
  }
  
  protected void processPageAttributes(DataParam param) {
    String tabId = param.get("_tabId_", this.defaultTabId);
    
    if ("SecurityUser".equals(tabId)) {
      initMappingItem("USER_SEX", 
        FormSelectFactory.create("USER_SEX").getContent());
      initMappingItem("USER_STATE", 
        FormSelectFactory.create("SYS_VALID_TYPE")
        .getContent());
    }
    
    setAttribute("GRP_STATE", 
      FormSelectFactory.create("SYS_VALID_TYPE")
      .addSelectedValue(getOperaAttributeValue("GRP_STATE", 
      "1")));
  }
  
  protected void initParameters(DataParam param) {
    String tabId = param.get("_tabId_", this.defaultTabId);
    
    if ("SecurityUser".equals(tabId)) {
      initParamItem(param, "columnId", "");
      initParamItem(param, "userCode", "");
      initParamItem(param, "userName", "");
    }
  }
  
  protected TreeBuilder provideTreeBuilder(DataParam param) {
    SecurityGroupManage service = getService();
    List<DataRow> menuRecords = service.findTreeRecords(new DataParam());
    TreeBuilder treeBuilder = new TreeBuilder(menuRecords, 
      this.columnIdField, 
      this.columnNameField, 
      this.columnParentIdField);
    
    return treeBuilder;
  }
  
  protected List<String> getTabList() {
    List<String> result = new ArrayList();
    result.add("_base_");
    result.add("SecurityUser");
    
    return result;
  }
  
  protected SecurityGroupManage getService() {
    return (SecurityGroupManage)lookupService(getServiceId());
  }
}
