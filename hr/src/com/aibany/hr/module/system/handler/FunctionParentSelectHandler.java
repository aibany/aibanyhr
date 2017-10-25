package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.aibany.hr.cxmodule.FunctionTreeManage;
import java.util.List;

public class FunctionParentSelectHandler
  extends TreeSelectHandler
{
  public FunctionParentSelectHandler()
  {
    this.serviceId = buildServiceId(FunctionTreeManage.class);
    this.isMuilSelect = false;
  }
  
  protected TreeBuilder provideTreeBuilder(DataParam param) {
    List<DataRow> records = getService().queryPickTreeRecords(param);
    TreeBuilder treeBuilder = new TreeBuilder(records, "FUNC_ID", 
      "FUNC_NAME", "FUNC_PID");
    
    String excludeId = param.get("FUNC_ID");
    treeBuilder.getExcludeIds().add(excludeId);
    
    return treeBuilder;
  }
  
  protected FunctionTreeManage getService() {
    return (FunctionTreeManage)lookupService(getServiceId());
  }
}
