package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.aibany.hr.module.system.service.SecurityGroupManage;
import java.util.List;

public class SecurityGroupPickHandler
  extends TreeSelectHandler
{
  public SecurityGroupPickHandler()
  {
    this.serviceId = buildServiceId(SecurityGroupManage.class);
    this.isMuilSelect = false;
  }
  
  protected TreeBuilder provideTreeBuilder(DataParam param) {
    List<DataRow> records = getService().queryPickTreeRecords(param);
    TreeBuilder treeBuilder = new TreeBuilder(records, "GRP_ID", 
      "GRP_NAME", "GRP_PID");
    
    String excludeId = param.get("GRP_ID");
    treeBuilder.getExcludeIds().add(excludeId);
    
    return treeBuilder;
  }
  
  protected SecurityGroupManage getService() {
    return (SecurityGroupManage)lookupService(getServiceId());
  }
}
