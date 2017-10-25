package com.aibany.hr.module.information.service;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.MasterSubServiceImpl;
import com.agileai.hotweb.common.DaoHelper;
import com.agileai.util.CryptionUtil;
import com.aibany.hr.module.system.handler.SecurityUserQueryListHandler;
import java.util.ArrayList;
import java.util.List;

public class HrEmployeeManageImpl
  extends MasterSubServiceImpl
  implements HrEmployeeManage
{
  public void createMasterRecord(DataParam param)
  {
    String statementId = this.sqlNameSpace + "." + "insertMasterRecord";
    processDataType(param, this.tableName);
    processPrimaryKeys(this.tableName, param);
    this.daoHelper.insertRecord(statementId, param);
    
    statementId = this.sqlNameSpace + "." + "insertSecurityUserRecord";
    DataParam userParam = new DataParam();
    String userId = KeyGenerator.instance().genKey();
    userParam.put("USER_ID", userId);
    userParam.put("USER_CODE", param.get("EMP_CODE"));
    userParam.put("USER_NAME", param.get("EMP_NAME"));
    String encPassword = CryptionUtil.md5Hex(param.get("EMP_CODE"));
    userParam.put("USER_PWD", encPassword);
    userParam.put("USER_SEX", param.get("EMP_SEX"));
    String strDesc = "普通用户";
    userParam.put("USER_DESC", strDesc);
    String strState = "1";
    userParam.put("USER_STATE", strState);
    String strSort = "3";
    userParam.put("USER_SORT", strSort);
    userParam.put("USER_MAIL", param.get("EMP_EMAIL"));
    userParam.put("USER_PHONE", param.get("EMP_TEL"));
    this.daoHelper.insertRecord(statementId, userParam);
    
    statementId = this.sqlNameSpace + "." + "insertSecurityUserGroupRelRecord";
    userParam.put("GRP_ID", param.get("EMP_NOW_DEPT"));
    userParam.put("USER_ID", param.get("USER_ID"));
    this.daoHelper.insertRecord(statementId, userParam);
    
    SecurityUserQueryListHandler handler = new SecurityUserQueryListHandler();
    userParam.put("ROLE_ID", "61729215-7F01-417B-A7A9-C4B4E0B9A0A3");
    handler.addUserRole(userParam);
  }
  
  public void updateMasterRecord(DataParam param) {
    String statementId = this.sqlNameSpace + "." + "updateMasterRecord";
    processDataType(param, this.tableName);
    this.daoHelper.updateRecord(statementId, param);
    
    statementId = this.sqlNameSpace + "." + "updateSecurityUserRecord";
    DataParam userParam = new DataParam();
    userParam.put("USER_CODE", param.get("EMP_CODE"));
    userParam.put("USER_NAME", param.get("EMP_NAME"));
    String encPassword = CryptionUtil.md5Hex(param.get("EMP_CODE"));
    userParam.put("USER_PWD", encPassword);
    userParam.put("USER_SEX", param.get("EMP_SEX"));
    String strDesc = "普通用户";
    userParam.put("USER_DESC", strDesc);
    String strSort = "3";
    userParam.put("USER_SORT", strSort);
    userParam.put("USER_MAIL", param.get("EMP_EMAIL"));
    userParam.put("USER_PHONE", param.get("EMP_TEL"));
    this.daoHelper.updateRecord(statementId, param);
  }
  
  public void approveRecord(DataParam param) { String statementId = this.sqlNameSpace + "." + "approveRecord";
    processDataType(param, this.tableName);
    this.daoHelper.updateRecord(statementId, param);
  }
  
  public void revokeApprovalRecords(String empId) {
    DataParam param = new DataParam(new Object[] { "EMP_ID", empId, "EMP_STATE", "drafe" });
    String statementId = this.sqlNameSpace + "." + "revokeApprovalRecord";
    processDataType(param, this.tableName);
    this.daoHelper.updateRecord(statementId, param);
  }
  
  public String[] getTableIds()
  {
    List<String> temp = new ArrayList();
    
    temp.add("_base");
    temp.add("HrEducation");
    temp.add("HrExperience");
    temp.add("HrWorkPerformance");
    
    return (String[])temp.toArray(new String[0]);
  }
}
