package com.aibany.hr.module.salary.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.module.salary.service.HrSalaryManage;
import java.math.BigDecimal;
import java.util.List;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class MobileSalaryHandler
  extends CommonHandler
{
  @PageAction
  public ViewRenderer findSalaryList(DataParam param)
  {
    String responseText = null;
    try {
      JSONObject jsonObject = new JSONObject();
      User user = (User)getUser();
      String userId = user.getUserId();
      param.put("userId", userId);
      
      List<DataRow> reList = getService().findSalaryList(param);
      JSONArray jsonArray = new JSONArray();
      for (int i = 0; i < reList.size(); i++) {
        DataRow dataRow = (DataRow)reList.get(i);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", dataRow.get("SAL_ID"));
        jsonObject1.put("year", dataRow.get("SAL_YEAR"));
        jsonObject1.put("month", dataRow.get("SAL_MONTH"));
        jsonObject1.put("basic", dataRow.get("SAL_BASIC"));
        jsonObject1.put("performance", dataRow.get("SAL_PERFORMANCE"));
        jsonObject1.put("subsidy", dataRow.get("SAL_SUBSIDY"));
        jsonObject1.put("bonus", dataRow.get("SAL_BONUS"));
        jsonObject1.put("insure", dataRow.get("SAL_INSURE"));
        jsonObject1.put("remarks", dataRow.get("SAL_REMARKS"));
        jsonArray.put(jsonObject1);
      }
      jsonObject.put("datas", jsonArray);
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer getSalaryInfo(DataParam param) {
    String responseText = null;
    try {
      DataRow dataRow = getService().getRecord(param);
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("basic", dataRow.get("SAL_BASIC"));
      jsonObject.put("performance", dataRow.get("SAL_PERFORMANCE"));
      jsonObject.put("subsidy", dataRow.get("SAL_SUBSIDY"));
      jsonObject.put("bonus", dataRow.get("SAL_BONUS"));
      jsonObject.put("insure", dataRow.get("SAL_INSURE"));
      jsonObject.put("actual", dataRow.get("SAL_ACTUAL"));
      jsonObject.put("total", dataRow.get("SAL_TOTAL"));
      jsonObject.put("remarks", dataRow.get("SAL_REMARKS"));
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer getSalaryDistributionMapInfo(DataParam param) {
    String responseText = null;
    try {
      DataRow dataRow = getService().getRecord(param);
      JSONArray jsonArray = new JSONArray();
      jsonArray.put(0, dataRow.get("SAL_BASIC"));
      jsonArray.put(1, dataRow.get("SAL_PERFORMANCE"));
      jsonArray.put(2, dataRow.get("SAL_SUBSIDY"));
      int salBonus = ((BigDecimal)dataRow.get("SAL_BONUS")).intValue();
      if (salBonus < 0) {
        salBonus = 0;
      }
      jsonArray.put(3, salBonus);
      responseText = jsonArray.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer getLastSalayInfo(DataParam param) {
    String responseText = null;
    try {
      User user = (User)getUser();
      param.put("userId", user.getUserId());
      DataRow dataRow = getService().getLastSalayInfo(param);
      JSONArray jsonArray = new JSONArray();
      jsonArray.put(0, dataRow.get("SAL_BASIC"));
      jsonArray.put(1, dataRow.get("SAL_PERFORMANCE"));
      jsonArray.put(2, dataRow.get("SAL_SUBSIDY"));
      jsonArray.put(3, dataRow.get("SAL_BONUS"));
      jsonArray.put(4, dataRow.get("SAL_INSURE"));
      jsonArray.put(5, dataRow.get("SAL_TOTAL"));
      jsonArray.put(6, dataRow.get("SAL_TAX"));
      JSONArray jsonArray1 = new JSONArray();
      jsonArray1.put(jsonArray);
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("data", jsonArray1);
      jsonObject.put("month", dataRow.get("SAL_YEAR_MONTH"));
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  protected HrSalaryManage getService() {
    return (HrSalaryManage)lookupService(HrSalaryManage.class);
  }
}
