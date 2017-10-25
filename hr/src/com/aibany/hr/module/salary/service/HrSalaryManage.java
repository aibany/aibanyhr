package com.aibany.hr.module.salary.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardService;
import java.util.List;

public abstract interface HrSalaryManage
  extends StandardService
{
  public abstract DataRow retrieveValidDays(String paramString1, String paramString2);
  
  public abstract void createValidDayRecord(DataParam paramDataParam);
  
  public abstract void updateValidDayRecord(DataParam paramDataParam);
  
  public abstract void gatherData(String paramString1, String paramString2);
  
  public abstract void computeTotalMoney(String paramString);
  
  public abstract void approveRecord(DataParam paramDataParam);
  
  public abstract void revokeApprovalRecords(String paramString);
  
  public abstract List<DataRow> findSalaryList(DataParam paramDataParam);
  
  public abstract DataRow getLastSalayInfo(DataParam paramDataParam);
}
