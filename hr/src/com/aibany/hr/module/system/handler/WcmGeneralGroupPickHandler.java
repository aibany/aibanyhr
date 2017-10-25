package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.aibany.hr.module.system.service.WcmGeneralGroup8ContentManage;
import java.util.List;

public class WcmGeneralGroupPickHandler
  extends TreeSelectHandler
{
  public WcmGeneralGroupPickHandler()
  {
    this.serviceId = buildServiceId(WcmGeneralGroup8ContentManage.class);
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
  
  protected WcmGeneralGroup8ContentManage getService() {
    return (WcmGeneralGroup8ContentManage)lookupService(getServiceId());
  }
}
