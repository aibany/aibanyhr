package com.aibany.hr.module.salary.service;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataMap;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;
import com.agileai.hotweb.common.DaoHelper;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.util.StringUtil;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HrSalaryManageImpl
  extends StandardServiceImpl
  implements HrSalaryManage
{
  public DataRow retrieveValidDays(String year, String month)
  {
    DataParam param = new DataParam(new Object[] { "year", year, "month", month });
    String statementId = this.sqlNameSpace + "." + "retrieveValidDays";
    DataRow row = this.daoHelper.getRecord(statementId, param);
    return row;
  }
  
  public void createValidDayRecord(DataParam param) {
    String statementId = this.sqlNameSpace + "." + "insertValidDaysRecord";
    processDataType(param, this.tableName);
    processPrimaryKeys(param);
    this.daoHelper.insertRecord(statementId, param);
  }
  
  public void updateValidDayRecord(DataParam param) {
    String statementId = this.sqlNameSpace + "." + "updateValidDaysRecord";
    processDataType(param, this.tableName);
    this.daoHelper.updateRecord(statementId, param);
  }
  
  public void gatherData(String year, String month)
  {
    KeyGenerator keyGenerator = new KeyGenerator();
    
    String statementId = this.sqlNameSpace + "." + "findMasterDaysRecords";
    List<DataRow> basicRecords = this.daoHelper.queryRecords(statementId, 
      new DataParam(new Object[] { "year", year, "month", month }));
    
    DataRow validDaysRow = retrieveValidDays(year, month);
    
    DataParam salCalcParam = new DataParam();
    salCalcParam.put("TYPE_ID", "SAL_OVERTIME_CALC");
    FormSelect typeIdFormSelect = FormSelectFactory.create("util.getCodeList", salCalcParam);
    DataMap dataMap = typeIdFormSelect.getContent();
    String normalCalc = dataMap.getString("普通");
    String weekendCalc = dataMap.getString("周末");
    String holidayCalc = dataMap.getString("节假日");
    if (StringUtil.isEmpty(normalCalc)) {
      normalCalc = "15";
    }
    if (StringUtil.isEmpty(weekendCalc)) {
      weekendCalc = "20";
    }
    if (StringUtil.isEmpty(holidayCalc)) {
      holidayCalc = "25";
    }
    
    String yearMonth = year + "-" + month;
    statementId = this.sqlNameSpace + "." + "workDayRecords";
    HashMap<String, DataRow> workDaysMap = this.daoHelper.queryRecords(
      "USER_ID", statementId, new DataParam(new Object[] { "yearMonth", yearMonth }));
    
    statementId = this.sqlNameSpace + "." + "leaveDayRecords";
    HashMap<String, DataRow> leaveDaysMap = this.daoHelper.queryRecords(
      "USER_ID", statementId, new DataParam(new Object[] { "yearMonth", yearMonth }));
    
    statementId = this.sqlNameSpace + "." + "overTimeDayRecords";
    HashMap<String, DataRow> overTimeDaysMap = this.daoHelper.queryRecords(
      "USER_ID", statementId, new DataParam(new Object[] { "yearMonth", yearMonth, "dayType", "工作日" }));
    HashMap<String, DataRow> overTimeWeekMap = this.daoHelper.queryRecords(
      "USER_ID", statementId, new DataParam(new Object[] { "yearMonth", yearMonth, "dayType", "周末" }));
    HashMap<String, DataRow> overTimeHolidayMap = this.daoHelper.queryRecords(
      "USER_ID", statementId, new DataParam(new Object[] { "yearMonth", yearMonth, "dayType", "节假日" }));
    
    statementId = this.sqlNameSpace + "." + "existsDataRecords";
    HashMap<String, DataRow> existsDataMap = this.daoHelper.queryRecords(
      "SAL_USER", statementId, new DataParam(new Object[] { "year", year, "month", month }));
    
    BigDecimal zero = new BigDecimal("0.0");
    
    List<DataParam> insertParamList = new ArrayList();
    List<DataParam> updateParamList = new ArrayList();
    
    for (int i = 0; i < basicRecords.size(); i++) {
      DataRow row = (DataRow)basicRecords.get(i);
      DataParam dataParam = new DataParam();
      dataParam.put("SAL_ID", keyGenerator.genKey());
      
      String userCode = row.getString("USER_ID");
      dataParam.put("SAL_USER", userCode);
      
      dataParam.put("SAL_YEAR", year);
      dataParam.put("SAL_MONTH", month);
      
      dataParam.put("SAL_VALID_DAYS", validDaysRow.get("VALID_DAYS"));
      
      DataRow workDaysRow = (DataRow)workDaysMap.get(userCode);
      if (workDaysRow == null) {
        dataParam.put("SAL_WORK_DAYS", zero);
      } else {
        dataParam.put("SAL_WORK_DAYS", workDaysRow.get("WORK_DAYS"));
      }
      
      DataRow leaveDaysRow = (DataRow)leaveDaysMap.get(userCode);
      if (leaveDaysRow == null) {
        dataParam.put("SAL_DEDUCT_NOWORKDAYS", zero);
        dataParam.put("SAL_DEDUCT_NOWORK", zero);
      } else {
        dataParam.put("SAL_DEDUCT_NOWORKDAYS", leaveDaysRow.get("LEAVE_DAYS"));
        Double leaveDays = Double.valueOf(Double.parseDouble((String) leaveDaysRow.get("LEAVE_DAYS")));
        
        BigDecimal basicSal = (BigDecimal)dataParam.getObject("SAL_BASIC");
        double salPerDay = 180.0D;
        if ((basicSal != null) && (basicSal.doubleValue() > 0.0D)) {
          salPerDay = basicSal.doubleValue() / Double.parseDouble((String) validDaysRow.get("VALID_DAYS"));
        }
        Double leaveCost = Double.valueOf(leaveDays.doubleValue() * salPerDay);
        dataParam.put("SAL_DEDUCT_NOWORK", new BigDecimal(leaveCost));
      }
      double overDayMoney = 0.0D;
      
      DataRow overTimeDaysRow = (DataRow)overTimeDaysMap.get(userCode);
      if (overTimeDaysRow == null) {
        dataParam.put("SAL_OVERTIME_NORMAL", zero);
      } else {
        dataParam.put("SAL_OVERTIME_NORMAL", overTimeDaysRow.get("WOT_DAYS"));
        overDayMoney = Double.parseDouble((String) overTimeDaysRow.get("WOT_DAYS")) * Double.parseDouble(normalCalc);
      }
      
      DataRow overTimeWeekRow = (DataRow)overTimeWeekMap.get(userCode);
      if (overTimeWeekRow == null) {
        dataParam.put("SAL_OVERTIME_WEEKEND", new BigDecimal("0.0"));
      } else {
        dataParam.put("SAL_OVERTIME_WEEKEND", overTimeWeekRow.get("WOT_DAYS"));
        overDayMoney += Double.parseDouble((String) overTimeWeekRow.get("WOT_DAYS")) * Double.parseDouble(weekendCalc);
      }
      
      DataRow overTimeHolidayRow = (DataRow)overTimeHolidayMap.get(userCode);
      if (overTimeHolidayRow == null) {
        dataParam.put("SAL_OVERTIME_HOLIDAY", new BigDecimal("0.0"));
      } else {
        dataParam.put("SAL_OVERTIME_HOLIDAY", overTimeHolidayRow.get("WOT_DAYS"));
        overDayMoney += Double.parseDouble((String) overTimeHolidayRow.get("WOT_DAYS")) * Double.parseDouble(holidayCalc);
      }
      
      dataParam.put("SAL_OVERTIME", new BigDecimal(overDayMoney));
      
      dataParam.put("SAL_AWARD_ALLDAYS", row.get("EMP_AWARD_ALLDAYS"));
      
      dataParam.put("SAL_BASIC", row.get("EMP_BASIC"));
      
      dataParam.put("SAL_ALLOWANCE_MIDROOM", row.get("EMP_ALLOWANCE_MIDROOM"));
      
      dataParam.put("SAL_ALLOWANCE_NIGHT", row.get("EMP_ALLOWANCE_NIGHT"));
      
      dataParam.put("SAL_ALLOWANCE_OTHER", row.get("EMP_ALLOWANCE_OTHER"));
      
      dataParam.put("SAL_PERFORMANCE", row.get("EMP_PERFORMANCE"));
      
      dataParam.put("SAL_SUBSIDY", row.get("EMP_SUBSIDY"));
      
      dataParam.put("SAL_ALLOWANCE", row.get("EMP_ALLOWANCE"));
      
      dataParam.put("SAL_ALLOWANCE_OUT", zero);
      
      dataParam.put("SAL_ALLOWANCE_AIR", zero);
      
      dataParam.put("SAL_ALLOWANCE_ALL", zero);
      
      dataParam.put("SAL_ALLOWANCE_YEAR", row.get("EMP_ALLOWANCE_YEAR"));
      
      dataParam.put("SAL_ALLOWANCE_RECORD", zero);
      
      dataParam.put("SAL_ALLOWANCE_RECORD_WHAT", "");
      
      dataParam.put("SAL_DEDUCT_EAT", zero);
      
      dataParam.put("SAL_DEDUCT_SIGNCARD", zero);
      
      dataParam.put("SAL_DEDUCT_LEAVE", zero);
      
      dataParam.put("SAL_INSURE", row.get("EMP_INSURE"));
      
      dataParam.put("SAL_FUND_HOUSE", row.get("EMP_FUND_HOUSE"));
      
      dataParam.put("SAL_FUND_ILL", zero);
      
      dataParam.put("SAL_DEDUCT_BUYCARD", zero);
      
      dataParam.put("SAL_DEDUCT_LATER", zero);
      
      dataParam.put("SAL_TAX", row.get("EMP_TAX"));
      
      dataParam.put("SAL_STATE", "0");
      
      BigDecimal total = (BigDecimal)dataParam.getObject("SAL_BASIC");
      total = total.add((BigDecimal)dataParam.getObject("SAL_OVERTIME"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_AWARD_ALLDAYS"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_ALLOWANCE_MIDROOM"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_ALLOWANCE_NIGHT"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_ALLOWANCE_OTHER"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_PERFORMANCE"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_SUBSIDY"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_ALLOWANCE"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_ALLOWANCE_OUT"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_ALLOWANCE_AIR"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_ALLOWANCE_ALL"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_ALLOWANCE_YEAR"));
      total = total.add((BigDecimal)dataParam.getObject("SAL_ALLOWANCE_RECORD"));
      
      BigDecimal decrease = (BigDecimal)dataParam.getObject("SAL_DEDUCT_NOWORK");
      decrease = decrease.add((BigDecimal)dataParam.getObject("SAL_DEDUCT_EAT"));
      decrease = decrease.add((BigDecimal)dataParam.getObject("SAL_DEDUCT_SIGNCARD"));
      decrease = decrease.add((BigDecimal)dataParam.getObject("SAL_DEDUCT_LEAVE"));
      decrease = decrease.add((BigDecimal)dataParam.getObject("SAL_INSURE"));
      decrease = decrease.add((BigDecimal)dataParam.getObject("SAL_FUND_HOUSE"));
      decrease = decrease.add((BigDecimal)dataParam.getObject("SAL_FUND_ILL"));
      decrease = decrease.add((BigDecimal)dataParam.getObject("SAL_DEDUCT_BUYCARD"));
      decrease = decrease.add((BigDecimal)dataParam.getObject("SAL_DEDUCT_LATER"));
      decrease = decrease.add((BigDecimal)dataParam.getObject("SAL_TAX"));
      
      BigDecimal recent = total.subtract(decrease);
      
      dataParam.put("SAL_TOTAL", total);
      dataParam.put("SAL_ACTUAL", recent);
      dataParam.put("SAL_DECEASE", decrease);
      
      if (existsDataMap.containsKey(userCode)) {
        updateParamList.add(dataParam);
      } else {
        insertParamList.add(dataParam);
      }
    }
    if (insertParamList.size() > 0) {
      statementId = this.sqlNameSpace + ".insertRecord";
      System.out.println(insertParamList);
      this.daoHelper.batchInsert(statementId, insertParamList);
    }
    
    if (updateParamList.size() > 0) {
      statementId = this.sqlNameSpace + "." + "validupdateRecord";
      this.daoHelper.batchUpdate(statementId, updateParamList);
    }
    
    Iterator<String> iter = existsDataMap.keySet().iterator();
    while (iter.hasNext()) {
      String key = (String)iter.next();
      DataRow dataRow = (DataRow)existsDataMap.get(key);
      String empParticipateSalary = (String)dataRow.get("EMP_PARTICIPATE_SALARY");
      if ("N".equals(empParticipateSalary)) {
        DataParam param = new DataParam();
        param.put("SAL_ID", dataRow.get("SAL_ID"));
        statementId = this.sqlNameSpace + "." + "deleteRecord";
        this.daoHelper.deleteRecords(statementId, param);
      }
    }
  }
  
  public void computeTotalMoney(String masterRecordId)
  {
    String statementId = this.sqlNameSpace + "." + "getRecord";
    DataParam param = new DataParam(new Object[] { "SAL_ID", masterRecordId });
    DataRow dataParam = this.daoHelper.getRecord(statementId, param);
    
    BigDecimal total = (BigDecimal)dataParam.get("SAL_BASIC");
    total = total.add((BigDecimal)dataParam.get("SAL_OVERTIME"));
    total = total.add((BigDecimal)dataParam.get("SAL_AWARD_ALLDAYS"));
    total = total.add((BigDecimal)dataParam.get("SAL_ALLOWANCE_MIDROOM"));
    total = total.add((BigDecimal)dataParam.get("SAL_ALLOWANCE_NIGHT"));
    total = total.add((BigDecimal)dataParam.get("SAL_ALLOWANCE_OTHER"));
    total = total.add((BigDecimal)dataParam.get("SAL_PERFORMANCE"));
    total = total.add((BigDecimal)dataParam.get("SAL_SUBSIDY"));
    total = total.add((BigDecimal)dataParam.get("SAL_ALLOWANCE"));
    total = total.add((BigDecimal)dataParam.get("SAL_ALLOWANCE_OUT"));
    total = total.add((BigDecimal)dataParam.get("SAL_ALLOWANCE_AIR"));
    total = total.add((BigDecimal)dataParam.get("SAL_ALLOWANCE_ALL"));
    total = total.add((BigDecimal)dataParam.get("SAL_ALLOWANCE_YEAR"));
    total = total.add((BigDecimal)dataParam.get("SAL_ALLOWANCE_RECORD"));
    
    BigDecimal decrease = (BigDecimal)dataParam.get("SAL_DEDUCT_NOWORK");
    decrease = decrease.add((BigDecimal)dataParam.get("SAL_DEDUCT_EAT"));
    decrease = decrease.add((BigDecimal)dataParam.get("SAL_DEDUCT_SIGNCARD"));
    decrease = decrease.add((BigDecimal)dataParam.get("SAL_DEDUCT_LEAVE"));
    decrease = decrease.add((BigDecimal)dataParam.get("SAL_INSURE"));
    decrease = decrease.add((BigDecimal)dataParam.get("SAL_FUND_HOUSE"));
    decrease = decrease.add((BigDecimal)dataParam.get("SAL_FUND_ILL"));
    decrease = decrease.add((BigDecimal)dataParam.get("SAL_DEDUCT_BUYCARD"));
    decrease = decrease.add((BigDecimal)dataParam.get("SAL_DEDUCT_LATER"));
    decrease = decrease.add((BigDecimal)dataParam.get("SAL_TAX"));
    
    BigDecimal recent = total.subtract(decrease);
    
    statementId = this.sqlNameSpace + "." + "updateBonusRecord";
    DataParam updateParam = new DataParam();
    updateParam.put("SAL_TOTAL", total);
    updateParam.put("SAL_DECEASE", decrease);
    updateParam.put("SAL_ACTUAL", recent);
    this.daoHelper.updateRecord(statementId, updateParam);
  }
  
  public void approveRecord(DataParam param)
  {
    String statementId = this.sqlNameSpace + "." + "approveRecord";
    processDataType(param, this.tableName);
    this.daoHelper.updateRecord(statementId, param);
  }
  
  public void revokeApprovalRecords(String salId)
  {
    DataParam param = new DataParam(new Object[] { "SAL_ID", salId, "SAL_STATE", "0" });
    String statementId = this.sqlNameSpace + "." + "revokeApprovalRecord";
    processDataType(param, this.tableName);
    this.daoHelper.updateRecord(statementId, param);
  }
  
  public List<DataRow> findSalaryList(DataParam param)
  {
    String statementId = this.sqlNameSpace + "." + "findSalaryList";
    List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
    return result;
  }
  
  public DataRow getLastSalayInfo(DataParam param)
  {
    String statementId = this.sqlNameSpace + "." + "getLastSalayInfo";
    DataRow dataRow = this.daoHelper.getRecord(statementId, param);
    return dataRow;
  }
}
