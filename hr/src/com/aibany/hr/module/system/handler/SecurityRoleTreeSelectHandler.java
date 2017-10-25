package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.cxmodule.SecurityRoleTreeSelect;
import java.util.List;

public class SecurityRoleTreeSelectHandler
  extends TreeSelectHandler
{
  public SecurityRoleTreeSelectHandler()
  {
    this.serviceId = buildServiceId(SecurityRoleTreeSelect.class);
    this.isMuilSelect = true;
    this.checkRelParentNode = false;
  }
  
  protected TreeBuilder provideTreeBuilder(DataParam param) {
    List<DataRow> records = getService().queryPickTreeRecords(param);
    TreeBuilder treeBuilder = new TreeBuilder(records, "ROLE_ID", 
      "ROLE_NAME", "ROLE_PID");
    
    return treeBuilder;
  }
  
  @PageAction
  public ViewRenderer addRoleRequest(DataParam param) {
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    SecurityAuthorizationConfig groupQuery = (SecurityAuthorizationConfig)lookupService(SecurityAuthorizationConfig.class);
    List<DataRow> records = groupQuery.retrieveRoleList(resourceType, resourceId);
    for (int i = 0; i < records.size(); i++) {
      DataRow row = (DataRow)records.get(i);
      String groupId = row.stringValue("ROLE_ID");
      this.invisiableCheckBoxIdList.add(groupId);
    }
    return prepareDisplay(param);
  }
  
  protected SecurityRoleTreeSelect getService() {
    return (SecurityRoleTreeSelect)lookupService(getServiceId());
  }
}
