package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.cxmodule.SecurityGroupTreeSelect;
import com.aibany.hr.module.system.service.SecurityGroupQuery;
import java.util.List;

public class SecurityGroupTreeSelectHandler
  extends TreeSelectHandler
{
  public SecurityGroupTreeSelectHandler()
  {
    this.serviceId = buildServiceId(SecurityGroupTreeSelect.class);
    this.isMuilSelect = true;
    this.checkRelParentNode = false;
  }
  
  public ViewRenderer prepareDisplay(DataParam param) {
    SecurityGroupQuery groupQuery = (SecurityGroupQuery)lookupService(SecurityGroupQuery.class);
    List<DataRow> records = groupQuery.findRecords(param);
    for (int i = 0; i < records.size(); i++) {
      DataRow row = (DataRow)records.get(i);
      String groupId = row.stringValue("GRP_ID");
      this.invisiableCheckBoxIdList.add(groupId);
    }
    return super.prepareDisplay(param);
  }
  
  protected TreeBuilder provideTreeBuilder(DataParam param) {
    List<DataRow> records = getService().queryPickTreeRecords(param);
    TreeBuilder treeBuilder = new TreeBuilder(records, "GRP_ID", 
      "GRP_NAME", "GRP_PID");
    
    return treeBuilder;
  }
  
  @PageAction
  public ViewRenderer addGroupRequest(DataParam param) {
    String resourceType = param.get("resourceType");
    String resourceId = param.get("resourceId");
    SecurityAuthorizationConfig securityAuthorizationConfig = (SecurityAuthorizationConfig)lookupService(SecurityAuthorizationConfig.class);
    List<DataRow> records = securityAuthorizationConfig.retrieveGroupList(resourceType, resourceId);
    for (int i = 0; i < records.size(); i++) {
      DataRow row = (DataRow)records.get(i);
      String groupId = row.stringValue("GRP_ID");
      this.invisiableCheckBoxIdList.add(groupId);
    }
    return super.prepareDisplay(param);
  }
  
  protected SecurityGroupTreeSelect getService() {
    return (SecurityGroupTreeSelect)lookupService(getServiceId());
  }
}
