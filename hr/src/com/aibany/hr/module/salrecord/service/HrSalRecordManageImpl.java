package com.aibany.hr.module.salrecord.service;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;
import com.agileai.hotweb.common.DaoHelper;
import java.util.List;
import java.util.Map;

public class HrSalRecordManageImpl
  extends StandardServiceImpl
  implements HrSalRecordManage
{
  public void insertRecords(List<Map<String, Object>> list)
  {
    for (Map<String, Object> map : list) {
      String funcId = KeyGenerator.instance().genKey();
      map.put("REC_ID", funcId);
    }
    DataParam param = new DataParam();
    param.put("list", list);
    
    String statementId = this.sqlNameSpace + "." + "insertRecords";
    this.daoHelper.insertRecord(statementId, param);
  }
}
