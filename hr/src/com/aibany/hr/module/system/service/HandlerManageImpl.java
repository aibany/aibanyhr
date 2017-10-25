package com.aibany.hr.module.system.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.MasterSubServiceImpl;
import com.agileai.hotweb.common.DaoHelper;
import java.util.ArrayList;
import java.util.List;

public class HandlerManageImpl
  extends MasterSubServiceImpl
  implements HandlerManage
{
  public String[] getTableIds()
  {
    List<String> temp = new ArrayList();
    temp.add("SysOperation");
    
    return (String[])temp.toArray(new String[0]);
  }
  
  public List<String> retrieveHandlerIdList(String funcId)
  {
    List<String> result = new ArrayList();
    String statementId = this.sqlNameSpace + ".retrieveHanlerIdList";
    DataParam param = new DataParam(new Object[] { "funcId", funcId });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    for (int i = 0; i < records.size(); i++) {
      DataRow row = (DataRow)records.get(i);
      String hanlerId = row.stringValue("HANLER_ID");
      result.add(hanlerId);
    }
    return result;
  }
  
  public List<String> retrieveUserIdList(String funcId)
  {
    List<String> result = new ArrayList();
    String statementId = this.sqlNameSpace + ".retrieveUserIdList";
    DataParam param = new DataParam(new Object[] { "funcId", funcId });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    for (int i = 0; i < records.size(); i++) {
      DataRow row = (DataRow)records.get(i);
      String userId = row.stringValue("USER_ID");
      result.add(userId);
    }
    return result;
  }
  
  public List<String> retrieveRoleIdList(String funcId)
  {
    List<String> result = new ArrayList();
    String statementId = this.sqlNameSpace + ".retrieveRoleIdList";
    DataParam param = new DataParam(new Object[] { "funcId", funcId });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    for (int i = 0; i < records.size(); i++) {
      DataRow row = (DataRow)records.get(i);
      String roleId = row.stringValue("ROLE_ID");
      result.add(roleId);
    }
    return result;
  }
  
  public List<String> retrieveGroupIdList(String funcId)
  {
    List<String> result = new ArrayList();
    String statementId = this.sqlNameSpace + ".retrieveGroupIdList";
    DataParam param = new DataParam(new Object[] { "funcId", funcId });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    for (int i = 0; i < records.size(); i++) {
      DataRow row = (DataRow)records.get(i);
      String roleId = row.stringValue("GRP_ID");
      result.add(roleId);
    }
    return result;
  }
}
