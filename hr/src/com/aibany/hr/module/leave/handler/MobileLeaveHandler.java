package com.aibany.hr.module.leave.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.module.leave.service.HrLeaveManage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class MobileLeaveHandler
  extends CommonHandler
{
  @PageAction
  public ViewRenderer findLeaveList(DataParam param)
  {
    String responseText = null;
    try {
      JSONObject jsonObject = new JSONObject();
      User user = (User)getUser();
      String userCode = user.getUserCode();
      param.put("userCode", userCode);
      
      List<DataRow> reList = getService().findLeaveList(param);
      JSONArray jsonArray = new JSONArray();
      for (int i = 0; i < reList.size(); i++) {
        DataRow dataRow = (DataRow)reList.get(i);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", dataRow.get("LEA_ID"));
        jsonObject1.put("type", dataRow.get("LEA_TYPE"));
        jsonObject1.put("days", dataRow.get("LEA_DAYS"));
        Date leaDate = (Date)dataRow.get("LEA_DATE");
        String date = DateUtil.getDateByType(6, leaDate);
        jsonObject1.put("date", date.substring(5));
        jsonObject1.put("state", dataRow.get("STATE"));
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
  public ViewRenderer createLeaveInfo(DataParam param) {
    String responseText = "fail";
    try {
      String inputString = getInputString();
      JSONObject jsonObject = new JSONObject(inputString);
      User user = (User)getUser();
      String userId = user.getUserId();
      
      String type = jsonObject.get("type").toString();
      
      String leaDate = DateUtil.format(11, new Date());
      
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
      String sdateUTC = jsonObject.get("sdate").toString().replace("Z", " UTC");
      Date sd = format.parse(sdateUTC);
      String sdate = DateUtil.format(9, sd);
      
      String edateUTC = jsonObject.get("edate").toString().replace("Z", " UTC");
      Date ed = format.parse(edateUTC);
      String edate = DateUtil.format(9, ed);
      
      String days = jsonObject.get("days").toString();
      String cause = jsonObject.get("cause").toString();
      DataParam createParam = new DataParam(new Object[] { "USER_ID", userId, "LEA_TYPE", type, "LEA_DATE", leaDate, "LEA_SDATE", sdate, "LEA_EDATE", edate, "LEA_DAYS", days, "LEA_CAUSE", cause });
      getService().createRecord(createParam);
      responseText = "success";
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer editLeaveInfo(DataParam param) {
    String responseText = "fail";
    try {
      String inputString = getInputString();
      JSONObject jsonObject = new JSONObject(inputString);
      User user = (User)getUser();
      String userId = user.getUserId();
      
      String id = jsonObject.get("id").toString();
      String type = jsonObject.get("type").toString();
      
      String leaDate = DateUtil.format(11, new Date());
      
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
      String sdateUTC = jsonObject.get("sdate").toString().replace("Z", " UTC");
      Date sd = format.parse(sdateUTC);
      String sdate = DateUtil.format(9, sd);
      
      String edateUTC = jsonObject.get("edate").toString().replace("Z", " UTC");
      Date ed = format.parse(edateUTC);
      String edate = DateUtil.format(9, ed);
      
      String days = jsonObject.get("days").toString();
      String cause = jsonObject.get("cause").toString();
      DataParam updateParam = new DataParam(new Object[] { "LEA_ID", id, "USER_ID", userId, "LEA_TYPE", type, "LEA_DATE", leaDate, "LEA_SDATE", sdate, "LEA_EDATE", edate, "LEA_DAYS", days, "LEA_CAUSE", cause, "STATE", "drafe" });
      getService().updateRecord(updateParam);
      responseText = "success";
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer submitLeaveInfo(DataParam param) {
    String responseText = "fail";
    try {
      param.put("STATE", "submitted");
      getService().submitRecord(param);
      responseText = "success";
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer viewLeaveInfo(DataParam param) {
    String responseText = null;
    try {
      DataRow dataRow = getService().getRecord(param);
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("id", dataRow.get("LEA_ID"));
      jsonObject.put("type", dataRow.get("LEA_TYPE"));
      jsonObject.put("sdate", dataRow.get("LEA_SDATE"));
      jsonObject.put("edate", dataRow.get("LEA_EDATE"));
      jsonObject.put("days", dataRow.get("LEA_DAYS"));
      jsonObject.put("cause", dataRow.get("LEA_CAUSE"));
      
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer deteleLeaveInfo(DataParam param)
  {
    String responseText = "fail";
    try {
      getService().deletRecord(param);
      responseText = "success";
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
  
  protected HrLeaveManage getService() {
    return (HrLeaveManage)lookupService(HrLeaveManage.class);
  }
  
  @PageAction
  public ViewRenderer initLeaveInInfo(DataParam param) {
    String responseText = null;
    try {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("sdate", DateUtil.getDateByType(9, new Date()));
      jsonObject.put("edate", DateUtil.getDateByType(9, DateUtil.getDateAdd(new Date(), 1, 1)));
      
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
}
