package com.aibany.hr.module.leaverecord.service;

import com.agileai.hotweb.bizmoduler.core.StandardService;
import java.util.List;
import java.util.Map;

public abstract interface HrLeaveRecordManage
  extends StandardService
{
  public abstract void insertRecords(List<Map<String, Object>> paramList);
}
