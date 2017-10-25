package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.aibany.hr.module.system.service.SecurityRoleTreeManage;
import java.util.List;

public class SecurityRoleParentSelectHandler
  extends TreeSelectHandler
{
  public SecurityRoleParentSelectHandler()
  {
    this.serviceId = buildServiceId(SecurityRoleTreeManage.class);
    this.isMuilSelect = false;
  }
  
  protected TreeBuilder provideTreeBuilder(DataParam param) {
    List<DataRow> records = getService().queryPickTreeRecords(param);
    TreeBuilder treeBuilder = new TreeBuilder(records, "ROLE_ID", 
      "ROLE_NAME", "ROLE_PID");
    
    String excludeId = param.get("ROLE_ID");
    treeBuilder.getExcludeIds().add(excludeId);
    
    return treeBuilder;
  }
  
  protected SecurityRoleTreeManage getService() {
    return (SecurityRoleTreeManage)lookupService(getServiceId());
  }
}
