package com.aibany.hr.cxmodule;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeManageImpl;
import com.agileai.hotweb.common.DaoHelper;
import com.agileai.hotweb.common.GlobalCacheManager;
import com.agileai.hotweb.domain.system.FuncHandler;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.domain.system.Operation;
import com.agileai.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionTreeManageImpl
  extends TreeManageImpl
  implements FunctionTreeManage
{
  private static final String functionMapCacheKey = "functionMap";
  private static final String funcMenuListCacheKey = "funcMenuList";
  private static final String operationMapCacheKey = "operationMap";
  private static final String handlerMapCacheKey = "handlerMap";
  
  public FunctionTreeManageImpl()
  {
    this.idField = "FUNC_ID";
    this.nameField = "FUNC_NAME";
    this.parentIdField = "FUNC_PID";
    this.sortField = "FUNC_SORT";
  }
  
  private HashMap<String, FuncMenu> getCacheFunctionMap() {
    HashMap<String, FuncMenu> functionMap = (HashMap)GlobalCacheManager.getCacheValue("functionMap");
    return functionMap;
  }
  
  private List<FuncMenu> getCacheFuncMenuList() {
    List<FuncMenu> funcMenuList = (List)GlobalCacheManager.getCacheValue("funcMenuList");
    return funcMenuList;
  }
  
  private HashMap<String, Operation> getCacheOperationMap() {
    HashMap<String, Operation> operationMap = (HashMap)GlobalCacheManager.getCacheValue("operationMap");
    return operationMap;
  }
  
  private HashMap<String, FuncHandler> getCacheHandlerMap() {
    HashMap<String, FuncHandler> handlerMap = (HashMap)GlobalCacheManager.getCacheValue("handlerMap");
    return handlerMap;
  }
  
  public FuncMenu getFunction(String functionId) {
    init();
    return (FuncMenu)getCacheFunctionMap().get(functionId);
  }
  
  public List<FuncMenu> getFuncMenuList() {
    init();
    return getCacheFuncMenuList();
  }
  
  private synchronized void initFuntions() {
    List<DataRow> records = findTreeRecords(new DataParam());
    if (records != null) {
      HashMap<String, FuncMenu> functionMap = new HashMap();
      List<FuncMenu> funcMenuList = new ArrayList();
      for (int i = 0; i < records.size(); i++) {
        DataRow row = (DataRow)records.get(i);
        FuncMenu funcMenu = new FuncMenu();
        String funcId = row.stringValue("FUNC_ID");
        funcMenu.setFuncId(funcId);
        funcMenu.setFuncName(row.stringValue("FUNC_NAME"));
        funcMenu.setFuncPid(row.stringValue("FUNC_PID"));
        funcMenu.setFuncType(row.stringValue("FUNC_TYPE"));
        String funcUrl = null;
        
        String handlerURL = row.stringValue("HANLER_URL");
        if (!StringUtil.isNullOrEmpty(handlerURL)) {
          funcUrl = handlerURL;
        } else {
          String handlerCode = row.stringValue("HANLER_CODE");
          funcUrl = "index?" + handlerCode;
        }
        funcMenu.setFuncUrl(funcUrl);
        
        funcMenuList.add(funcMenu);
        functionMap.put(funcId, funcMenu);
      }
      for (int i = 0; i < funcMenuList.size(); i++) {
        FuncMenu funcMenu = (FuncMenu)funcMenuList.get(i);
        String parentId = funcMenu.getFuncPid();
        if (!StringUtil.isNullOrEmpty(parentId)) {
          FuncMenu parent = (FuncMenu)functionMap.get(parentId);
          parent.getChildren().add(funcMenu);
        }
      }
      
      GlobalCacheManager.putCacheValue("functionMap", functionMap);
      GlobalCacheManager.putCacheValue("funcMenuList", funcMenuList);
    }
  }
  
  private synchronized void initHandlers() {
    String statementId = "syshandler.findMasterRecords";
    List<DataRow> handlerRecords = this.daoHelper.queryRecords(statementId, new DataParam());
    HashMap<String, FuncHandler> handlerMap = new HashMap();
    if (handlerRecords != null) {
      for (int i = 0; i < handlerRecords.size(); i++) {
        DataRow row = (DataRow)handlerRecords.get(i);
        String handlerId = row.stringValue("HANLER_ID");
        String handlerCode = row.stringValue("HANLER_CODE");
        String handlerType = row.stringValue("HANLER_TYPE");
        String handlerURL = row.stringValue("HANLER_URL");
        String funcId = row.stringValue("FUNC_ID");
        FuncHandler handler = new FuncHandler();
        handler.setHandlerId(handlerId);
        handler.setHandlerCode(handlerCode);
        handler.setHandlerType(handlerType);
        handler.setHandlerURL(handlerURL);
        handler.setFunctionId(funcId);
        
        handlerMap.put(handlerId, handler);
        
        HashMap<String, FuncMenu> functionMap = getCacheFunctionMap();
        FuncMenu funcMenu = (FuncMenu)functionMap.get(funcId);
        funcMenu.getHandlers().add(handler);
      }
    } else {
      handlerMap.put("-1", new FuncHandler());
    }
    
    GlobalCacheManager.putCacheValue("handlerMap", handlerMap);
  }
  
  private synchronized void initOperatons() {
    String statementId = "syshandler.findSysOperationRecords";
    List<DataRow> operationRecords = this.daoHelper.queryRecords(statementId, new DataParam());
    HashMap<String, Operation> operationMap = new HashMap();
    if (operationRecords != null) {
      for (int i = 0; i < operationRecords.size(); i++) {
        DataRow row = (DataRow)operationRecords.get(i);
        String operaId = row.stringValue("OPER_ID");
        String handlerId = row.stringValue("HANLER_ID");
        String operaCode = row.stringValue("OPER_CODE");
        String operaName = row.stringValue("OPER_NAME");
        String actionType = row.stringValue("OPER_ACTIONTPYE");
        
        Operation operation = new Operation();
        operation.setHandlerId(handlerId);
        operation.setOperCode(operaCode);
        operation.setOperName(operaName);
        operation.setOperId(operaId);
        operation.setActionType(actionType);
        
        operationMap.put(operaId, operation);
        
        HashMap<String, FuncHandler> handlerMap = getCacheHandlerMap();
        FuncHandler funcHandler = (FuncHandler)handlerMap.get(handlerId);
        funcHandler.getOperations().add(operation);
      }
    } else {
      operationMap.put("-1", new Operation());
    }
    
    GlobalCacheManager.putCacheValue("operationMap", operationMap);
  }
  
  public void insertChildRecord(DataParam param) {
    String parentId = param.get(this.idField);
    String newMenuSort = String.valueOf(getNewMaxSort(parentId));
    String funcId = KeyGenerator.instance().genKey();
    String handlerCode = param.get("CHILD_MAIN_HANDLER");
    String handlerId = KeyGenerator.instance().genKey();
    String funcType = param.get("CHILD_FUNC_TYPE");
    param.put("CHILD_" + this.sortField, newMenuSort);
    param.put("CHILD_" + this.idField, funcId);
    if ("funcnode".equals(funcType)) {
      param.put("CHILD_MAIN_HANDLER", handlerId);
    }
    param.put("CHILD_" + this.parentIdField, param.get(this.idField));
    String statementId = this.sqlNameSpace + "." + "insertTreeRecord";
    this.daoHelper.insertRecord(statementId, param);
    
    if ("funcnode".equals(funcType)) {
      statementId = "syshandler.insertMasterRecord";
      DataParam insertParam = new DataParam();
      insertParam.put("HANLER_ID", handlerId);
      insertParam.put("HANLER_CODE", handlerCode);
      insertParam.put("FUNC_ID", funcId);
      insertParam.put("HANLER_TYPE", "MAIN");
      this.daoHelper.insertRecord(statementId, insertParam);
    }
  }
  
  public void deleteCurrentRecord(String currentId) {
    String statementId = "syshandler.findMasterRecords";
    DataParam param = new DataParam(new Object[] { "funcId", currentId });
    List<DataRow> handlerRecords = this.daoHelper.queryRecords(statementId, param);
    if ((handlerRecords != null) && (handlerRecords.size() > 0)) {
      for (int i = 0; i < handlerRecords.size(); i++) {
        DataRow row = (DataRow)handlerRecords.get(i);
        String handlerId = row.stringValue("HANLER_ID");
        String tableId = "SysOperation";
        DataParam deleteParam = new DataParam(new Object[] { "HANLER_ID", handlerId });
        statementId = "syshandler.delete" + StringUtil.upperFirst(tableId) + "Records";
        this.daoHelper.deleteRecords(statementId, deleteParam);
        
        statementId = "syshandler.deleteMasterRecord";
        this.daoHelper.deleteRecords(statementId, deleteParam);
      }
    }
    DataParam authparam = new DataParam();
    authparam.put("RES_TYPE", "Menu");
    authparam.put("RES_ID", currentId);
    
    statementId = "SecurityAuthorizationConfig.delRoleAuthRelation";
    this.daoHelper.deleteRecords(statementId, authparam);
    
    statementId = "SecurityAuthorizationConfig.delUserAuthRelation";
    this.daoHelper.deleteRecords(statementId, authparam);
    
    statementId = "SecurityAuthorizationConfig.delGroupAuthRelation";
    this.daoHelper.deleteRecords(statementId, authparam);
    
    statementId = this.sqlNameSpace + "." + "deleteTreeRecord";
    this.daoHelper.deleteRecords(statementId, currentId);
  }
  
  private void init() {
    HashMap<String, FuncMenu> functionMap = getCacheFunctionMap();
    HashMap<String, Operation> operationMap = getCacheOperationMap();
    HashMap<String, FuncHandler> handlerMap = getCacheHandlerMap();
    
    if ((functionMap == null) || (functionMap.isEmpty())) {
      initFuntions();
    }
    if ((handlerMap == null) || (handlerMap.isEmpty())) {
      initHandlers();
    }
    if ((operationMap == null) || (operationMap.isEmpty())) {
      initOperatons();
    }
  }
  
  public FuncHandler getFuncHandler(String handlerId)
  {
    init();
    HashMap<String, FuncHandler> handlerMap = getCacheHandlerMap();
    return (FuncHandler)handlerMap.get(handlerId);
  }
  
  public Operation getOperation(String operationId)
  {
    init();
    HashMap<String, Operation> operationMap = getCacheOperationMap();
    return (Operation)operationMap.get(operationId);
  }
  
  public void clearFuncTreeCache()
  {
    HashMap<String, FuncMenu> functionMap = getCacheFunctionMap();
    HashMap<String, Operation> operationMap = getCacheOperationMap();
    HashMap<String, FuncHandler> handlerMap = getCacheHandlerMap();
    List<FuncMenu> funcMenuList = getCacheFuncMenuList();
    
    funcMenuList.clear();
    functionMap.clear();
    handlerMap.clear();
    operationMap.clear();
  }
}
