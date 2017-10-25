package com.aibany.hr.module.workovertime.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardService;
import java.util.List;

public abstract interface HrWorkOvertimeManage
  extends StandardService
{
  public abstract DataRow getNowRecord(DataParam paramDataParam);
  
  public abstract void approveRecord(DataParam paramDataParam);
  
  public abstract List<DataRow> findOvertimeList(DataParam paramDataParam);
  
  public abstract void submitRecord(DataParam paramDataParam);
}
