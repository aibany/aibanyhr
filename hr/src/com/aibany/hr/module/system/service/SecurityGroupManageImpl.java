package com.aibany.hr.module.system.service;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManageImpl;
import com.agileai.hotweb.common.DaoHelper;

public class SecurityGroupManageImpl extends TreeAndContentManageImpl implements SecurityGroupManage
{
  public SecurityGroupManageImpl()
  {
    this.columnIdField = "GRP_ID";
    this.columnParentIdField = "GRP_PID";
    this.columnSortField = "GRP_SORT";
  }
  
  public void deletContentRecord(String tabId, DataParam param) { super.deletContentRecord(tabId, param);
    if ("SecurityUser".equals(tabId)) {
      String statementId = "SecurityAuthorizationConfig.delUserAuthRelation";
      String userId = param.get("USER_ID");
      DataParam deleteParam = new DataParam(new Object[] { "USER_ID", userId });
      this.daoHelper.deleteRecords(statementId, deleteParam);
    }
  }
  
  public void deleteTreeRecord(String currentId) {
    super.deleteTreeRecord(currentId);
    String statementId = "SecurityAuthorizationConfig.delGroupAuthRelation";
    DataParam deleteParam = new DataParam(new Object[] { "GRP_ID", currentId });
    this.daoHelper.deleteRecords(statementId, deleteParam);
  }
}
