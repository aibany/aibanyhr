package com.aibany.hr.module.system.service;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.TreeManageImpl;
import com.agileai.hotweb.common.DaoHelper;

public class SecurityRoleTreeManageImpl extends TreeManageImpl implements SecurityRoleTreeManage
{
  public SecurityRoleTreeManageImpl()
  {
    this.idField = "ROLE_ID";
    this.nameField = "ROLE_NAME";
    this.parentIdField = "ROLE_PID";
    this.sortField = "ROLE_SORT";
  }
  
  public void deleteCurrentRecord(String currentId) {
    super.deleteCurrentRecord(currentId);
    String statementId = "SecurityAuthorizationConfig.delRoleAuthRelation";
    DataParam param = new DataParam(new Object[] { "ROLE_ID", currentId });
    this.daoHelper.deleteRecords(statementId, param);
  }
}
