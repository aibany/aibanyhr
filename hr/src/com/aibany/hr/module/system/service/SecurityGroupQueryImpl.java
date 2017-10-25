package com.aibany.hr.module.system.service;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.QueryModelServiceImpl;
import com.agileai.hotweb.common.DaoHelper;
import java.util.ArrayList;
import java.util.List;

public class SecurityGroupQueryImpl
  extends QueryModelServiceImpl
  implements SecurityGroupQuery
{
  public void addGroupTreeRelation(String roleId, String[] groupIds)
  {
    String statementId = this.sqlNameSpace + ".addGroupTreeRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < groupIds.length; i++) {
      DataParam dataParam = new DataParam(new Object[] { "roleId", roleId, "groupId", groupIds[i] });
      paramList.add(dataParam);
    }
    this.daoHelper.batchInsert(statementId, paramList);
  }
  
  public void delGroupTreeRelation(String roleId, String groupId)
  {
    String statementId = this.sqlNameSpace + ".delGroupTreeRelation";
    DataParam param = new DataParam(new Object[] { "roleId", roleId, "groupId", groupId });
    this.daoHelper.deleteRecords(statementId, param);
  }
}
