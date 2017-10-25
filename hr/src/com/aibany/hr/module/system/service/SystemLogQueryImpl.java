package com.aibany.hr.module.system.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.QueryModelServiceImpl;
import com.agileai.hotweb.common.DaoHelper;
import com.agileai.util.StringUtil;
import java.util.List;
import org.ecside.core.bean.PageList;

public class SystemLogQueryImpl extends QueryModelServiceImpl implements SystemLogQuery
{
  public PageList<DataRow> findRecords(DataParam param)
  {
    String ec_p = param.get("ec_p");
    if (StringUtil.isNullOrEmpty(ec_p)) {
      param.put("ec_p", "1");
    }
    String ec_rd = param.get("ec_rd");
    if (StringUtil.isNullOrEmpty(ec_rd)) {
      param.put("ec_rd", "15");
    }
    
    String statementId = this.sqlNameSpace + "." + "queryRecordsCount";
    DataRow countRow = this.daoHelper.getRecord(statementId, param);
    int totalCount = countRow.getInt("TOTAL_COUNT");
    
    int currentPageNum = param.getInt("ec_p");
    int pageSizePerPage = param.getInt("ec_rd");
    
    int startNum = (currentPageNum - 1) * pageSizePerPage + 1;
    int endNum = currentPageNum * pageSizePerPage;
    if (endNum > totalCount) {
      endNum = totalCount;
    }
    param.put("_startNum_", Integer.valueOf(startNum - 1));
    param.put("_endNum_", Integer.valueOf(endNum));
    int pageSize = endNum - startNum + 1;
    pageSize = pageSize > 0 ? pageSize : 15;
    param.put("_stepRange_", Integer.valueOf(pageSize));
    
    PageList<DataRow> result = new PageList();
    statementId = this.sqlNameSpace + "." + "queryRecords";
    List<DataRow> temp = this.daoHelper.queryRecords(statementId, param);
    result.addAll(temp);
    result.setTotalNumber(Integer.valueOf(totalCount));
    
    return result;
  }
}
