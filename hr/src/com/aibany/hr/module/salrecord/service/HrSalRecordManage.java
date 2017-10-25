package com.aibany.hr.module.salrecord.service;

import com.agileai.hotweb.bizmoduler.core.StandardService;
import java.util.List;
import java.util.Map;

public abstract interface HrSalRecordManage
  extends StandardService
{
  public abstract void insertRecords(List<Map<String, Object>> paramList);
}
