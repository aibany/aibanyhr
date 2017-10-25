package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataMap;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.MasterSubEditMainHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.cxmodule.FunctionTreeManage;
import com.aibany.hr.module.system.service.HandlerManage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HandlerManageEditHandler extends MasterSubEditMainHandler
{
  public HandlerManageEditHandler()
  {
    this.listHandlerClass = HandlerManageListHandler.class;
    this.serviceId = buildServiceId(HandlerManage.class);
    this.baseTablePK = "HANLER_ID";
    this.defaultTabId = "SysOperation";
  }
  
  public ViewRenderer prepareDisplay(DataParam param) {
    String operaType = param.get("operaType");
    if (isReqRecordOperaType(operaType)) {
      DataRow record = getService().getMasterRecord(param);
      setAttributes(record);
    }
    String currentSubTableId = param.get("currentSubTableId", this.defaultTabId);
    if (!currentSubTableId.equals("_base")) {
      String subRecordsKey = currentSubTableId + "Records";
      if (!getAttributesContainer().containsKey(subRecordsKey)) {
        List<DataRow> subRecords = getService().findSubRecords(currentSubTableId, param);
        setAttribute(currentSubTableId + "Records", subRecords);
      }
    }
    setAttribute("FUNC_ID", param.get("funcId"));
    setAttribute("currentSubTableId", currentSubTableId);
    setAttribute("currentSubTableIndex", getTabIndex(currentSubTableId));
    String operateType = param.get("operaType");
    setOperaType(operateType);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(DataParam param) {
    setAttribute("HANLER_TYPE", 
      FormSelectFactory.create("HANDLER_TYPE")
      .addSelectedValue(getOperaAttributeValue("HANLER_TYPE", 
      "MAIN")));
  }
  
  public ViewRenderer doSaveMasterRecordAction(DataParam param) { ((FunctionTreeManage)lookupService(FunctionTreeManage.class)).clearFuncTreeCache();
    return super.doSaveMasterRecordAction(param);
  }
  
  public ViewRenderer doMoveUpAction(DataParam param) { ((FunctionTreeManage)lookupService(FunctionTreeManage.class)).clearFuncTreeCache();
    return super.doMoveUpAction(param);
  }
  
  public ViewRenderer doMoveDownAction(DataParam param) { ((FunctionTreeManage)lookupService(FunctionTreeManage.class)).clearFuncTreeCache();
    return super.doMoveDownAction(param);
  }
  
  public ViewRenderer doSaveEntryRecordsAction(DataParam param) { ((FunctionTreeManage)lookupService(FunctionTreeManage.class)).clearFuncTreeCache();
    return super.doSaveEntryRecordsAction(param);
  }
  
  public ViewRenderer doDeleteEntryRecordAction(DataParam param) { ((FunctionTreeManage)lookupService(FunctionTreeManage.class)).clearFuncTreeCache();
    return super.doDeleteEntryRecordAction(param);
  }
  
  protected String[] getEntryEditFields(String currentSubTableId) { List<String> temp = new ArrayList();
    
    if ("SysOperation".equals(currentSubTableId)) {
      temp.add("OPER_ID");
      temp.add("HANLER_ID");
      temp.add("OPER_CODE");
      temp.add("OPER_ACTIONTPYE");
      temp.add("OPER_NAME");
      temp.add("OPER_SORT");
    }
    
    return (String[])temp.toArray(new String[0]);
  }
  
  protected String getEntryEditTablePK(String currentSubTableId) {
    HashMap<String, String> primaryKeys = new HashMap();
    primaryKeys.put("SysOperation", "OPER_ID");
    
    return (String)primaryKeys.get(currentSubTableId);
  }
  
  protected String getEntryEditForeignKey(String currentSubTableId) {
    HashMap<String, String> foreignKeys = new HashMap();
    foreignKeys.put("SysOperation", "HANLER_ID");
    
    return (String)foreignKeys.get(currentSubTableId);
  }
  
  protected HandlerManage getService() {
    return (HandlerManage)lookupService(getServiceId());
  }
}
