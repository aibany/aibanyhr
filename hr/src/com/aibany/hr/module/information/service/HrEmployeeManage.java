package com.aibany.hr.module.information.service;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.MasterSubService;

public abstract interface HrEmployeeManage
  extends MasterSubService
{
  public abstract void approveRecord(DataParam paramDataParam);
  
  public abstract void revokeApprovalRecords(String paramString);
}
