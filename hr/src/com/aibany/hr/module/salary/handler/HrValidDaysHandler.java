package com.aibany.hr.module.salary.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.module.salary.service.HrSalaryManage;
import org.apache.log4j.Logger;

public class HrValidDaysHandler
  extends CommonHandler
{
  public ViewRenderer prepareDisplay(DataParam param)
  {
    setAttributes(param);
    HrSalaryManage hrSalaryManage = (HrSalaryManage)lookupService(HrSalaryManage.class);
    String year = param.get("year");
    String month = param.get("month");
    DataRow row = hrSalaryManage.retrieveValidDays(year, month);
    if ((row != null) && (row.size() > 0)) {
      setOperaType("update");
    } else {
      setOperaType("insert");
    }
    setAttributes(row);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(DataParam param) {
    setAttribute("VALID_YEAR", getAttribute("VALID_YEAR", param.get("year")));
    setAttribute("VALID_MONTH", getAttribute("VALID_MONTH", param.get("month")));
  }
  
  public ViewRenderer doSaveAction(DataParam param) {
    String responseText = "fail";
    try {
      HrSalaryManage hrSalaryManage = (HrSalaryManage)lookupService(HrSalaryManage.class);
      String operateType = param.get("operaType");
      if ("insert".equals(operateType)) {
        hrSalaryManage.createValidDayRecord(param);
      }
      else if ("update".equals(operateType)) {
        hrSalaryManage.updateValidDayRecord(param);
      }
      responseText = "success";
      writeSystemLog("设置标准出勤:" + param.get("VALID_DAYS"));
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
}
