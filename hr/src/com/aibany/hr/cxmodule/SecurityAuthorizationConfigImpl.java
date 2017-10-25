package com.aibany.hr.cxmodule;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.BaseService;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.common.DaoHelper;
import java.util.ArrayList;
import java.util.List;

public class SecurityAuthorizationConfigImpl
  extends BaseService
  implements SecurityAuthorizationConfig
{
  public List<DataRow> retrieveGroupList(String resourceType, String resourceId)
  {
    String statementId = this.sqlNameSpace + ".retrieveGroupList";
    DataParam param = new DataParam(new Object[] { "resourceType", resourceType, "resourceId", resourceId });
    return this.daoHelper.queryRecords(statementId, param);
  }
  
  public List<DataRow> retrieveRoleList(String resourceType, String resourceId)
  {
    String statementId = this.sqlNameSpace + ".retrieveRoleList";
    DataParam param = new DataParam(new Object[] { "resourceType", resourceType, "resourceId", resourceId });
    return this.daoHelper.queryRecords(statementId, param);
  }
  
  public List<DataRow> retrieveUserList(String resourceType, String resourceId)
  {
    String statementId = this.sqlNameSpace + ".retrieveUserList";
    DataParam param = new DataParam(new Object[] { "resourceType", resourceType, "resourceId", resourceId });
    return this.daoHelper.queryRecords(statementId, param);
  }
  
  public void addUserAuthRelation(String resourceType, String resourceId, List<String> userIdList)
  {
    String statementId = this.sqlNameSpace + ".addUserAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < userIdList.size(); i++) {
      String userId = (String)userIdList.get(i);
      DataParam param = new DataParam();
      param.put("USER_AUTH_ID", KeyGenerator.instance().genKey());
      param.put("USER_ID", userId);
      param.put("RES_TYPE", resourceType);
      param.put("RES_ID", resourceId);
      paramList.add(param);
    }
    this.daoHelper.batchInsert(statementId, paramList);
  }
  
  public void addRoleAuthRelation(String resourceType, String resourceId, List<String> roleIdList)
  {
    String statementId = this.sqlNameSpace + ".addRoleAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < roleIdList.size(); i++) {
      String roleId = (String)roleIdList.get(i);
      DataParam param = new DataParam();
      param.put("ROLE_AUTH_ID", KeyGenerator.instance().genKey());
      param.put("ROLE_ID", roleId);
      param.put("RES_TYPE", resourceType);
      param.put("RES_ID", resourceId);
      paramList.add(param);
    }
    this.daoHelper.batchInsert(statementId, paramList);
  }
  
  public void addGroupAuthRelation(String resourceType, String resourceId, List<String> groupIdList)
  {
    String statementId = this.sqlNameSpace + ".addGroupAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < groupIdList.size(); i++) {
      String groupId = (String)groupIdList.get(i);
      DataParam param = new DataParam();
      param.put("GRP_AUTH_ID", KeyGenerator.instance().genKey());
      param.put("GRP_ID", groupId);
      param.put("RES_TYPE", resourceType);
      param.put("RES_ID", resourceId);
      paramList.add(param);
    }
    this.daoHelper.batchInsert(statementId, paramList);
  }
  
  public void delUserAuthRelation(String resourceType, String resourceId, String userId)
  {
    String statementId = this.sqlNameSpace + ".delUserAuthRelation";
    DataParam param = new DataParam();
    param.put("USER_ID", userId);
    param.put("RES_TYPE", resourceType);
    param.put("RES_ID", resourceId);
    this.daoHelper.deleteRecords(statementId, param);
  }
  
  public void delRoleAuthRelation(String resourceType, String resourceId, String roleId)
  {
    String statementId = this.sqlNameSpace + ".delRoleAuthRelation";
    DataParam param = new DataParam();
    param.put("ROLE_ID", roleId);
    param.put("RES_TYPE", resourceType);
    param.put("RES_ID", resourceId);
    this.daoHelper.deleteRecords(statementId, param);
  }
  
  public void delGroupAuthRelation(String resourceType, String resourceId, String groupId)
  {
    String statementId = this.sqlNameSpace + ".delGroupAuthRelation";
    DataParam param = new DataParam();
    param.put("GRP_ID", groupId);
    param.put("RES_TYPE", resourceType);
    param.put("RES_ID", resourceId);
    this.daoHelper.deleteRecords(statementId, param);
  }
  
  public void delUserAuthRelations(String resourceType, String resourceId)
  {
    String statementId = this.sqlNameSpace + ".delUserAuthRelation";
    DataParam param = new DataParam();
    param.put("RES_TYPE", resourceType);
    param.put("RES_ID", resourceId);
    this.daoHelper.deleteRecords(statementId, param);
  }
  
  public void delRoleAuthRelations(String resourceType, String resourceId)
  {
    String statementId = this.sqlNameSpace + ".delRoleAuthRelation";
    DataParam param = new DataParam();
    param.put("RES_TYPE", resourceType);
    param.put("RES_ID", resourceId);
    this.daoHelper.deleteRecords(statementId, param);
  }
  
  public void delGroupAuthRelations(String resourceType, String resourceId)
  {
    String statementId = this.sqlNameSpace + ".delGroupAuthRelation";
    DataParam param = new DataParam();
    param.put("RES_TYPE", resourceType);
    param.put("RES_ID", resourceId);
    this.daoHelper.deleteRecords(statementId, param);
  }
  
  public void delPortletAuthRelations(String portletId)
  {
    DataParam delParam = new DataParam();
    delParam.put("RES_TYPE", "Portlet");
    delParam.put("RES_ID", portletId);
    
    String statementId = this.sqlNameSpace + ".delRoleAuthRelation";
    this.daoHelper.deleteRecords(statementId, delParam);
    
    statementId = this.sqlNameSpace + ".delUserAuthRelation";
    this.daoHelper.deleteRecords(statementId, delParam);
    
    statementId = this.sqlNameSpace + ".delGroupAuthRelation";
    this.daoHelper.deleteRecords(statementId, delParam);
  }
  
  public DataRow retrieveUserRecord(String userCode)
  {
    String statementId = this.sqlNameSpace + ".getSecurityUserRecord";
    DataParam param = new DataParam(new Object[] { "USER_CODE", userCode, "USER_STATE", "1" });
    return this.daoHelper.getRecord(statementId, param);
  }
  
  public List<DataRow> retrieveRoleRecords(String userId)
  {
    String statementId = this.sqlNameSpace + ".retrieveRoleRecords";
    DataParam param = new DataParam(new Object[] { "userId", userId });
    return this.daoHelper.queryRecords(statementId, param);
  }
  
  public List<DataRow> retrieveGroupRecords(String userId)
  {
    String statementId = this.sqlNameSpace + ".retrieveGroupRecords";
    DataParam param = new DataParam(new Object[] { "userId", userId });
    return this.daoHelper.queryRecords(statementId, param);
  }
  
  public List<String> retrieveMenuIdList(String userId)
  {
    List<String> result = new ArrayList();
    String statementId = this.sqlNameSpace + ".retrieveMenuIdList";
    DataParam param = new DataParam(new Object[] { "userId", userId });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    for (int i = 0; i < records.size(); i++) {
      DataRow row = (DataRow)records.get(i);
      String menuId = row.stringValue("FUNC_ID");
      result.add(menuId);
    }
    return result;
  }
  
  public List<String> retrieveHandlerIdList(String userId) {
    List<String> result = new ArrayList();
    String statementId = this.sqlNameSpace + ".retrieveHandlerIdList";
    DataParam param = new DataParam(new Object[] { "userId", userId });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    for (int i = 0; i < records.size(); i++) {
      DataRow row = (DataRow)records.get(i);
      String menuId = row.stringValue("HANDLER_ID");
      result.add(menuId);
    }
    return result;
  }
  
  public List<String> retrieveOperationIdList(String userId)
  {
    List<String> result = new ArrayList();
    String statementId = this.sqlNameSpace + ".retrieveOperationIdList";
    DataParam param = new DataParam(new Object[] { "userId", userId });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    for (int i = 0; i < records.size(); i++) {
      DataRow row = (DataRow)records.get(i);
      String menuId = row.stringValue("OPER_ID");
      result.add(menuId);
    }
    return result;
  }
  
  public void addUserAuthRelation(List<String> resourceTypes, List<String> resourceIds, List<String> userIdList)
  {
    String statementId = this.sqlNameSpace + ".addUserAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < resourceTypes.size(); i++) {
      String resourceType = (String)resourceTypes.get(i);
      String resourceId = (String)resourceIds.get(i);
      for (int j = 0; j < userIdList.size(); j++) {
        String userId = (String)userIdList.get(j);
        DataParam param = new DataParam();
        param.put("USER_AUTH_ID", KeyGenerator.instance().genKey());
        param.put("USER_ID", userId);
        param.put("RES_TYPE", resourceType);
        param.put("RES_ID", resourceId);
        paramList.add(param);
      }
    }
    this.daoHelper.batchInsert(statementId, paramList);
  }
  
  public void addRoleAuthRelation(List<String> resourceTypes, List<String> resourceIds, List<String> roleIdList)
  {
    String statementId = this.sqlNameSpace + ".addRoleAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < resourceTypes.size(); i++) {
      String resourceType = (String)resourceTypes.get(i);
      String resourceId = (String)resourceIds.get(i);
      for (int j = 0; j < roleIdList.size(); j++) {
        String roleId = (String)roleIdList.get(j);
        DataParam param = new DataParam();
        param.put("ROLE_AUTH_ID", KeyGenerator.instance().genKey());
        param.put("ROLE_ID", roleId);
        param.put("RES_TYPE", resourceType);
        param.put("RES_ID", resourceId);
        paramList.add(param);
      }
    }
    this.daoHelper.batchInsert(statementId, paramList);
  }
  
  public void addGroupAuthRelation(List<String> resourceTypes, List<String> resourceIds, List<String> groupIdList)
  {
    String statementId = this.sqlNameSpace + ".addGroupAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < resourceTypes.size(); i++) {
      String resourceType = (String)resourceTypes.get(i);
      String resourceId = (String)resourceIds.get(i);
      for (int j = 0; j < groupIdList.size(); j++) {
        String groupId = (String)groupIdList.get(j);
        DataParam param = new DataParam();
        param.put("GRP_AUTH_ID", KeyGenerator.instance().genKey());
        param.put("GRP_ID", groupId);
        param.put("RES_TYPE", resourceType);
        param.put("RES_ID", resourceId);
        paramList.add(param);
      }
    }
    this.daoHelper.batchInsert(statementId, paramList);
  }
  
  public void delUserAuthRelation(List<String> resourceTypes, List<String> resourceIds, String userId)
  {
    String statementId = this.sqlNameSpace + ".delUserAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < resourceTypes.size(); i++) {
      String resourceType = (String)resourceTypes.get(i);
      String resourceId = (String)resourceIds.get(i);
      DataParam param = new DataParam();
      param.put("USER_ID", userId);
      param.put("RES_TYPE", resourceType);
      param.put("RES_ID", resourceId);
      paramList.add(param);
    }
    this.daoHelper.batchDelete(statementId, paramList);
  }
  
  public void delRoleAuthRelation(List<String> resourceTypes, List<String> resourceIds, String roleId)
  {
    String statementId = this.sqlNameSpace + ".delRoleAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < resourceTypes.size(); i++) {
      String resourceType = (String)resourceTypes.get(i);
      String resourceId = (String)resourceIds.get(i);
      
      DataParam param = new DataParam();
      param.put("ROLE_ID", roleId);
      param.put("RES_TYPE", resourceType);
      param.put("RES_ID", resourceId);
      paramList.add(param);
    }
    this.daoHelper.batchDelete(statementId, paramList);
  }
  
  public void delGroupAuthRelation(List<String> resourceTypes, List<String> resourceIds, String groupId)
  {
    String statementId = this.sqlNameSpace + ".delGroupAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < resourceTypes.size(); i++) {
      String resourceType = (String)resourceTypes.get(i);
      String resourceId = (String)resourceIds.get(i);
      
      DataParam param = new DataParam();
      param.put("GRP_ID", groupId);
      param.put("RES_TYPE", resourceType);
      param.put("RES_ID", resourceId);
      
      paramList.add(param);
    }
    this.daoHelper.batchDelete(statementId, paramList);
  }
  
  public void delUserAuthRelations(List<String> resourceTypes, List<String> resourceIds)
  {
    String statementId = this.sqlNameSpace + ".delUserAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < resourceTypes.size(); i++) {
      String resourceType = (String)resourceTypes.get(i);
      String resourceId = (String)resourceIds.get(i);
      DataParam param = new DataParam();
      param.put("RES_TYPE", resourceType);
      param.put("RES_ID", resourceId);
      
      paramList.add(param);
    }
    this.daoHelper.batchDelete(statementId, paramList);
  }
  
  public void delRoleAuthRelations(List<String> resourceTypes, List<String> resourceIds)
  {
    String statementId = this.sqlNameSpace + ".delRoleAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < resourceTypes.size(); i++) {
      String resourceType = (String)resourceTypes.get(i);
      String resourceId = (String)resourceIds.get(i);
      DataParam param = new DataParam();
      param.put("RES_TYPE", resourceType);
      param.put("RES_ID", resourceId);
      
      paramList.add(param);
    }
    this.daoHelper.batchDelete(statementId, paramList);
  }
  
  public void delGroupAuthRelations(List<String> resourceTypes, List<String> resourceIds)
  {
    String statementId = this.sqlNameSpace + ".delGroupAuthRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < resourceTypes.size(); i++) {
      String resourceType = (String)resourceTypes.get(i);
      String resourceId = (String)resourceIds.get(i);
      DataParam param = new DataParam();
      param.put("RES_TYPE", resourceType);
      param.put("RES_ID", resourceId);
      
      paramList.add(param);
    }
    this.daoHelper.batchDelete(statementId, paramList);
  }
  
  public DataRow retriveUserInfoRecord(String userCode)
  {
    String statementId = "SecurityGroup8Associates.getSecurityUserRecord";
    DataParam param = new DataParam(new Object[] { "USER_CODE", userCode });
    return this.daoHelper.getRecord(statementId, param);
  }
  
  public void modifyUserPassword(String userCode, String userPwd)
  {
    String statementId = "SecurityGroup8Associates.updateSecurityUserPassword";
    DataParam param = new DataParam(new Object[] { "USER_CODE", userCode, "USER_PWD", userPwd });
    this.daoHelper.updateRecord(statementId, param);
  }
}
