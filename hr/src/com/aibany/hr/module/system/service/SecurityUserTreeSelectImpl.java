package com.aibany.hr.module.system.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeSelectServiceImpl;
import com.agileai.hotweb.common.DaoHelper;
import com.aibany.hr.cxmodule.SecurityUserTreeSelect;
import java.util.List;

public class SecurityUserTreeSelectImpl
  extends TreeSelectServiceImpl
  implements SecurityUserTreeSelect
{
  public List<DataRow> findChildGroupRecords(String parentId)
  {
    String statementId = this.sqlNameSpace + ".findChildGroupRecords";
    DataParam param = new DataParam(new Object[] { "parentId", parentId });
    List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
    return result;
  }
}
