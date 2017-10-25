package com.aibany.hr.module.information.handler;

import com.agileai.domain.DataMap;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.MasterSubEditMainHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.common.PrivilegeHelper;
import com.aibany.hr.module.information.service.HrEmployeeManage;
import com.aibany.hr.module.salrecord.service.HrSalRecordManage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HrEmployeeManageEditHandler extends MasterSubEditMainHandler
{
  private String recordServiceId = null;
  
  public HrEmployeeManageEditHandler()
  {
    this.listHandlerClass = HrEmployeeManageListHandler.class;
    this.serviceId = buildServiceId(HrEmployeeManage.class);
    this.recordServiceId = buildServiceId(HrSalRecordManage.class);
    this.baseTablePK = "EMP_ID";
    this.defaultTabId = "_base";
  }
  
  public ViewRenderer prepareDisplay(com.agileai.domain.DataParam param) {
    String operaType = param.get("operaType");
    if ("insert".equals(operaType)) {
      setAttribute("doDetail", Boolean.valueOf(true));
      setAttribute("onlyRead", "");
    }
    if ("update".equals(operaType)) {
      setAttribute("doDetail", Boolean.valueOf(true));
      setAttribute("onlyRead", "readonly");
      if (isReqRecordOperaType(operaType)) {
        DataRow record = getService().getMasterRecord(param);
        setAttributes(record);
      }
    }
    if ("approve".equals(operaType)) {
      setAttribute("isApprove", Boolean.valueOf(true));
      setAttribute("doApprove", Boolean.valueOf(true));
      if (!isReqRecordOperaType(operaType)) {
        DataRow record = getService().getMasterRecord(param);
        setAttributes(record);
      }
    }
    if ("detail".equals(operaType)) {
      setAttribute("onlyRead", "readonly");
      if (isReqRecordOperaType(operaType)) {
        DataRow record = getService().getMasterRecord(param);
        if (record.get("EMP_STATE").equals("drafe")) {
          setAttribute("doDetail", Boolean.valueOf(true));
          setAttribute("doApprove", Boolean.valueOf(true));
        }
        if (record.get("EMP_STATE").equals("drafe")) {
          setAttribute("doDetail", Boolean.valueOf(true));
          setAttribute("doApprove", Boolean.valueOf(true));
        }
        if (record.get("EMP_STATE").equals("approved")) {
          setAttribute("doDetail", Boolean.valueOf(false));
          setAttribute("doApprove", Boolean.valueOf(false));
          setAttribute("doRevokeApprove", Boolean.valueOf(true));
        }
        setAttributes(record);
      }
    }
    String currentSubTableId = param.get("currentSubTableId", this.defaultTabId);
    if (!currentSubTableId.equals("_base")) {
      String subRecordsKey = currentSubTableId + "Records";
      if (!getAttributesContainer().containsKey(subRecordsKey)) {
        List<DataRow> subRecords = getService().findSubRecords(
          currentSubTableId, param);
        setAttribute(currentSubTableId + "Records", subRecords);
      }
    }
    setAttribute("currentSubTableId", currentSubTableId);
    setAttribute("currentSubTableIndex", 
      getTabIndex(currentSubTableId));
    String operateType = param.get("operaType");
    setOperaType(operateType);
    processPageAttributes(param);
    
    User user = (User)getUser();
    PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
    if ((privilegeHelper.isApprove()) && (!privilegeHelper.isHRMASTER())) {
      String emp_code = getAttributeValue("EMP_CODE");
      if (user.getUserCode().equals(emp_code)) {
        return new LocalRenderer(getPage());
      }
      this.request.getSession().setAttribute("errorMsg", "您只能编辑/查看自己的资料");
      return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
    }
    
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(com.agileai.domain.DataParam param) {
    setAttribute("EMP_SEX", FormSelectFactory.create("USER_SEX")
      .addSelectedValue(getOperaAttributeValue("EMP_SEX", "M")));
    setAttribute("EMP_PARTY", FormSelectFactory.create("EMP_PARTY")
      .addSelectedValue(getOperaAttributeValue("EMP_PARTY", "")));
    setAttribute(
      "EMP_MARITAL_STATUS", 
      
      FormSelectFactory.create("EMP_MARITAL_STATUS")
      .addSelectedValue(
      getOperaAttributeValue("EMP_MARITAL_STATUS", "")));
    setAttribute("EMP_EDUCATION", FormSelectFactory.create("EMP_EDUCATION")
      .addSelectedValue(getOperaAttributeValue("EMP_EDUCATION", "")));
    setAttribute("EMP_STATE", FormSelectFactory.create("EMP_STATE")
      .addSelectedValue(getOperaAttributeValue("EMP_STATE", "drafe")));
    setAttribute(
      "EMP_WORK_STATE", 
      FormSelectFactory.create("EMP_WORK_STATE").addSelectedValue(
      getOperaAttributeValue("EMP_WORK_STATE", "")));
    setAttribute(
      "EMP_PARTICIPATE_SALARY", 
      FormSelectFactory.create("BOOL_DEFINE").addSelectedValue(
      getOperaAttributeValue("EMP_PARTICIPATE_SALARY", "Y")));
    BigDecimal empMoney = new BigDecimal("0.00");
    if (getAttribute("EMP_BASIC") == null) {
      setAttribute("EMP_BASIC", empMoney);
    }
    if (getAttribute("EMP_PERFORMANCE") == null) {
      setAttribute("EMP_PERFORMANCE", empMoney);
    }
    if (getAttribute("EMP_SUBSIDY") == null) {
      setAttribute("EMP_SUBSIDY", empMoney);
    }
    if (getAttribute("EMP_TAX") == null) {
      setAttribute("EMP_TAX", empMoney);
    }
    if (getAttribute("EMP_INSURE") == null) {
      setAttribute("EMP_INSURE", empMoney);
    }
    if (getAttribute("EMP_ALLOWANCE") == null) {
      setAttribute("EMP_ALLOWANCE", empMoney);
    }
    if (getAttribute("EMP_AWARD_ALLDAYS") == null) {
      setAttribute("EMP_AWARD_ALLDAYS", empMoney);
    }
    if (getAttribute("EMP_ALLOWANCE_MIDROOM") == null) {
      setAttribute("EMP_ALLOWANCE_MIDROOM", empMoney);
    }
    if (getAttribute("EMP_ALLOWANCE_NIGHT") == null) {
      setAttribute("EMP_ALLOWANCE_NIGHT", empMoney);
    }
    if (getAttribute("EMP_ALLOWANCE_OTHER") == null) {
      setAttribute("EMP_ALLOWANCE_OTHER", empMoney);
    }
    if (getAttribute("EMP_FUND_HOUSE") == null) {
      setAttribute("EMP_FUND_HOUSE", empMoney);
    }
    if (getAttribute("EMP_ALLOWANCE_YEAR") == null) {
      setAttribute("EMP_ALLOWANCE_YEAR", empMoney);
    }
  }
  
  @PageAction
  public ViewRenderer approve(com.agileai.domain.DataParam param)
  {
    param.put("EMP_STATE", "approved");
    getService().approveRecord(param);
    CommonHandler.writeSystemLog("核准基础信息", getActionType(), this);
    return new RedirectRenderer(getHandlerURL(this.listHandlerClass));
  }
  
  protected String[] getEntryEditFields(String currentSubTableId) {
    List<String> temp = new ArrayList();
    
    return (String[])temp.toArray(new String[0]);
  }
  
  protected String getEntryEditTablePK(String currentSubTableId) {
    HashMap<String, String> primaryKeys = new HashMap();
    primaryKeys.put("HrEducation", "EDU_ID");
    primaryKeys.put("HrExperience", "EXP_ID");
    primaryKeys.put("HrWorkPerformance", "PER_ID");
    
    return (String)primaryKeys.get(currentSubTableId);
  }
  
  protected String getEntryEditForeignKey(String currentSubTableId) {
    HashMap<String, String> foreignKeys = new HashMap();
    foreignKeys.put("HrEducation", "EMP_ID");
    foreignKeys.put("HrExperience", "EMP_ID");
    foreignKeys.put("HrWorkPerformance", "EMP_ID");
    
    return (String)foreignKeys.get(currentSubTableId);
  }
  
  public ViewRenderer doSaveMasterRecordAction(com.agileai.domain.DataParam param) { String operateType = param.get("operaType");
    String responseText = "fail";
    
    String empCode = param.get("EMP_CODE");
    com.agileai.domain.DataParam repeatParam = new com.agileai.domain.DataParam(new Object[] { "EMP_CODE", empCode });
    DataRow record = getService().getMasterRecord(repeatParam);
    if ("insert".equals(operateType)) {
      if ((record != null) && (record.size() > 0)) {
        responseText = "repeat";
      } else {
        getService().createMasterRecord(param);
        responseText = param.get(this.baseTablePK);
      }
    } else if ("update".equals(operateType)) {
      if (param.get("EMP_BASIC") == "") {
        param.put("EMP_BASIC", "0.00");
      }
      if (param.get("EMP_PERFORMANCE") == "") {
        param.put("EMP_PERFORMANCE", "0.00");
      }
      if (param.get("EMP_SUBSIDY") == "") {
        param.put("EMP_SUBSIDY", "0.00");
      }
      if (param.get("EMP_TAX") == "") {
        param.put("EMP_TAX", "0.00");
      }
      if (param.get("EMP_INSURE") == "") {
        param.put("EMP_INSURE", "0.00");
      }
      if (param.get("EMP_ALLOWANCE") == "") {
        param.put("EMP_ALLOWANCE", "0.00");
      }
      if (param.get("EMP_AWARD_ALLDAYS") == "") {
        param.put("EMP_AWARD_ALLDAYS", "0.00");
      }
      if (param.get("EMP_ALLOWANCE_MIDROOM") == "") {
        param.put("EMP_ALLOWANCE_MIDROOM", "0.00");
      }
      if (param.get("EMP_ALLOWANCE_NIGHT") == "") {
        param.put("EMP_ALLOWANCE_NIGHT", "0.00");
      }
      if (param.get("EMP_ALLOWANCE_OTHER") == "") {
        param.put("EMP_ALLOWANCE_OTHER", "0.00");
      }
      if (param.get("EMP_FUND_HOUSE") == "") {
        param.put("EMP_FUND_HOUSE", "0.00");
      }
      if (param.get("EMP_ALLOWANCE_YEAR") == "") {
        param.put("EMP_ALLOWANCE_YEAR", "0.00");
      }
      
      getService().updateMasterRecord(param);
      saveSubRecords(param);
      responseText = param.get(this.baseTablePK);
    } else if ("detail".equals(operateType)) {
      if (param.get("EMP_BASIC") == "") {
        param.put("EMP_BASIC", "0.00");
      }
      if (param.get("EMP_PERFORMANCE") == "") {
        param.put("EMP_PERFORMANCE", "0.00");
      }
      if (param.get("EMP_SUBSIDY") == "") {
        param.put("EMP_SUBSIDY", "0.00");
      }
      if (param.get("EMP_TAX") == "") {
        param.put("EMP_TAX", "0.00");
      }
      if (param.get("EMP_INSURE") == "") {
        param.put("EMP_INSURE", "0.00");
      }
      if (param.get("EMP_ALLOWANCE") == "") {
        param.put("EMP_ALLOWANCE", "0.00");
      }
      if (param.get("EMP_AWARD_ALLDAYS") == "") {
        param.put("EMP_AWARD_ALLDAYS", "0.00");
      }
      if (param.get("EMP_ALLOWANCE_MIDROOM") == "") {
        param.put("EMP_ALLOWANCE_MIDROOM", "0.00");
      }
      if (param.get("EMP_ALLOWANCE_NIGHT") == "") {
        param.put("EMP_ALLOWANCE_NIGHT", "0.00");
      }
      if (param.get("EMP_ALLOWANCE_OTHER") == "") {
        param.put("EMP_ALLOWANCE_OTHER", "0.00");
      }
      if (param.get("EMP_FUND_HOUSE") == "") {
        param.put("EMP_FUND_HOUSE", "0.00");
      }
      if (param.get("EMP_ALLOWANCE_YEAR") == "") {
        param.put("EMP_ALLOWANCE_YEAR", "0.00");
      }
      getService().updateMasterRecord(param);
      saveSubRecords(param);
      responseText = param.get(this.baseTablePK);
    }
    
    if (("detail".equals(operateType)) || ("update".equals(operateType))) {
      List<Map<String, Object>> recordList = new ArrayList();
      generateRecordMapByDouble(((BigDecimal)record.get("EMP_BASIC")).doubleValue(), Double.parseDouble(param.get("EMP_BASIC")), "基本工资", param, recordList);
      generateRecordMap(record.getString("EMP_NOW_JOB"), param.get("EMP_NOW_JOB"), "工作岗位", param, recordList);
      generateRecordMap(record.getString("EMP_NOW_DEPT_NAME"), param.get("EMP_NOW_DEPT_NAME"), "部门", param, recordList);
      generateRecordMapByDouble(((BigDecimal)record.get("EMP_PERFORMANCE")).doubleValue(), Double.parseDouble(param.get("EMP_PERFORMANCE")), "浮动绩效", param, recordList);
      generateRecordMapByDouble(((BigDecimal)record.get("EMP_SUBSIDY")).doubleValue(), Double.parseDouble(param.get("EMP_SUBSIDY")), "职务津贴", param, recordList);
      generateRecordMapByDouble(((BigDecimal)record.get("EMP_ALLOWANCE")).doubleValue(), Double.parseDouble(param.get("EMP_ALLOWANCE")), "技能津贴", param, recordList);
      generateRecordMapByDouble(((BigDecimal)record.get("EMP_ALLOWANCE_MIDROOM")).doubleValue(), Double.parseDouble(param.get("EMP_ALLOWANCE_MIDROOM")), "中班津贴", param, recordList);
      generateRecordMapByDouble(((BigDecimal)record.get("EMP_ALLOWANCE_NIGHT")).doubleValue(), Double.parseDouble(param.get("EMP_ALLOWANCE_NIGHT")), "夜班津贴", param, recordList);
      generateRecordMapByDouble(((BigDecimal)record.get("EMP_ALLOWANCE_OTHER")).doubleValue(), Double.parseDouble(param.get("EMP_ALLOWANCE_OTHER")), "其他津贴", param, recordList);
      
      saveSalRecordList(recordList);
    }
    
    CommonHandler.writeSystemLog("保存基础信息", getActionType(), this);
    return new AjaxRenderer(responseText);
  }
  
  protected HrEmployeeManage getService() {
    return (HrEmployeeManage)lookupService(getServiceId());
  }
  
  protected HrSalRecordManage getRecordService() {
    return (HrSalRecordManage)lookupService(this.recordServiceId);
  }
  
  @PageAction
  public ViewRenderer revokeApproval(com.agileai.domain.DataParam param) {
    String empId = param.get("EMP_ID");
    getService().revokeApprovalRecords(empId);
    CommonHandler.writeSystemLog("反核准基础信息", getActionType(), this);
    return prepareDisplay(param);
  }
  
  protected void saveSalRecordList(List<Map<String, Object>> list) {
    if ((list != null) && (list.size() > 0)) {
      getRecordService().insertRecords(list);
    }
  }
  
  private void generateRecordMapByDouble(double oldValue, double nValue, String desc, com.agileai.domain.DataParam param, List<Map<String, Object>> recordList)
  {
    if (oldValue != nValue) {
      Map<String, Object> map = new HashMap();
      map.put("SAL_CODE", param.get("EMP_CODE"));
      map.put("SAL_NAME", param.get("EMP_NAME"));
      map.put("REC_TYPE", desc);
      map.put("OLD_VALUE", Double.valueOf(oldValue));
      map.put("NEW_VALUE", Double.valueOf(nValue));
      map.put("OP_TIME", new Date());
      map.put("OP_NAME", ((User)getUser()).getUserName());
      recordList.add(map);
    }
  }
  
  private void generateRecordMap(String oldValue, String nValue, String desc, com.agileai.domain.DataParam param, List<Map<String, Object>> recordList) {
    if (!oldValue.equals(nValue)) {
      Map<String, Object> map = new HashMap();
      map.put("SAL_CODE", param.get("EMP_CODE"));
      map.put("SAL_NAME", param.get("EMP_NAME"));
      map.put("REC_TYPE", desc);
      map.put("OLD_VALUE", oldValue);
      map.put("NEW_VALUE", nValue);
      map.put("OP_TIME", new Date());
      map.put("OP_NAME", ((User)getUser()).getUserName());
      recordList.add(map);
    }
  }
}
