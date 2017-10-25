package com.aibany.hr.module.system.service;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.QueryModelServiceImpl;
import com.agileai.hotweb.common.DaoHelper;
import java.util.ArrayList;
import java.util.List;

public class SecurityUserQueryImpl
  extends QueryModelServiceImpl
  implements SecurityUserQuery
{
  public void addUserTreeRelation(String roleId, String[] groupIds)
  {
    String statementId = this.sqlNameSpace + ".addUserTreeRelation";
    List<DataParam> paramList = new ArrayList();
    for (int i = 0; i < groupIds.length; i++) {
      DataParam dataParam = new DataParam(new Object[] { "roleId", roleId, "userId", groupIds[i] });
      paramList.add(dataParam);
    }
    this.daoHelper.batchInsert(statementId, paramList);
  }
  
  public void delUserTreeRelation(String roleId, String groupId)
  {
    String statementId = this.sqlNameSpace + ".delUserTreeRelation";
    DataParam param = new DataParam(new Object[] { "roleId", roleId, "userId", groupId });
    this.daoHelper.deleteRecords(statementId, param);
  }
}
