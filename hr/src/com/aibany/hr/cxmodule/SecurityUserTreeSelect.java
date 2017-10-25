package com.aibany.hr.cxmodule;

import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeSelectService;
import java.util.List;

public abstract interface SecurityUserTreeSelect
  extends TreeSelectService
{
  public abstract List<DataRow> findChildGroupRecords(String paramString);
}
