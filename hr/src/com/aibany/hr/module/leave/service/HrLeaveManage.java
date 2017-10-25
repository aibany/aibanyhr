package com.aibany.hr.module.leave.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardService;
import java.util.List;

public abstract interface HrLeaveManage
  extends StandardService
{
  public abstract DataRow getNowRecord(DataParam paramDataParam);
  
  public abstract void approveRecord(DataParam paramDataParam);
  
  public abstract List<DataRow> findLeaveList(DataParam paramDataParam);
  
  public abstract void submitRecord(DataParam paramDataParam);
  
  public abstract DataRow getAllowanceYear(DataParam paramDataParam);
  
  public abstract void updateAllowanceYear(DataParam paramDataParam);
}
