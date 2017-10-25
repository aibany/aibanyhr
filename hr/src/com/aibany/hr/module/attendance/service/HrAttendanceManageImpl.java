package com.aibany.hr.module.attendance.service;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;
import com.agileai.hotweb.common.DaoHelper;
import com.agileai.util.DateUtil;
import com.aibany.hr.cxmodule.HrAttendanceManage;
import java.util.List;

public class HrAttendanceManageImpl
  extends StandardServiceImpl
  implements HrAttendanceManage
{
  public DataRow retrieveUserInfo(DataParam param)
  {
    String statementId = "SecurityUserQuery.retrieveUserInfo";
    DataRow row = this.daoHelper.getRecord(statementId, param);
    return row;
  }
  
  public void bindUserWxOpenId(String userCode, String wxOpenId)
  {
    DataParam param = new DataParam();
    String statementId = "SecurityUserQuery.updateUserInfo";
    param.put(new Object[] { "openId", wxOpenId, "userCode", userCode });
    this.daoHelper.updateRecord(statementId, param);
  }
  
  public List<DataRow> attendanceStatisticsRecords(DataParam param)
  {
    String statementId = this.sqlNameSpace + "." + "attendanceStatisticsRecords";
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    return records;
  }
  
  public List<DataRow> getAttendanceStatInfo(String date)
  {
    String statementId = this.sqlNameSpace + "." + "getAttendanceStatInfoRecords";
    String startDate = null;
    String endDate = null;
    if (date.endsWith("12")) {
      startDate = date + "-01";
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(startDate), 3, 1));
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(endDate), 2, -11));
    } else {
      startDate = date + "-01";
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(startDate), 2, 1));
    }
    DataParam param = new DataParam(new Object[] { "startDate", startDate, "endDate", endDate });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    return records;
  }
  
  public List<DataRow> getAttendContentData(String code, String date)
  {
    String statementId = this.sqlNameSpace + "." + "getAttendContentDataRecords";
    String startDate = null;
    String endDate = null;
    if (date.endsWith("12")) {
      startDate = date + "-01";
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(startDate), 3, 1));
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(endDate), 2, -11));
    } else {
      startDate = date + "-01";
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(startDate), 2, 1));
    }
    DataParam param = new DataParam(new Object[] { "code", code, "startDate", startDate, "endDate", endDate });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    return records;
  }
  
  public List<DataRow> getLeaveContentData(String code, String date)
  {
    String statementId = this.sqlNameSpace + "." + "getLeaveContentDataRecords";
    String startDate = null;
    String endDate = null;
    if (date.endsWith("12")) {
      startDate = date + "-01";
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(startDate), 3, 1));
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(endDate), 2, -11));
    } else {
      startDate = date + "-01";
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(startDate), 2, 1));
    }
    DataParam param = new DataParam(new Object[] { "code", code, "startDate", startDate, "endDate", endDate });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    return records;
  }
  
  public List<DataRow> getOverworkContentData(String code, String date)
  {
    String statementId = this.sqlNameSpace + "." + "getOverworkContentDataRecords";
    String startDate = null;
    String endDate = null;
    if (date.endsWith("12")) {
      startDate = date + "-01";
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(startDate), 3, 1));
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(endDate), 2, -11));
    } else {
      startDate = date + "-01";
      endDate = DateUtil.getDateByType(9, DateUtil.getDateAdd(DateUtil.getDate(startDate), 2, 1));
    }
    DataParam param = new DataParam(new Object[] { "code", code, "startDate", startDate, "endDate", endDate });
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    return records;
  }
  
  public void createLocationRecord(DataParam param)
  {
    String statementId = this.sqlNameSpace + "." + "createLocationRecord";
    param.put("LOCAT_ID", KeyGenerator.instance().genKey());
    this.daoHelper.insertRecord(statementId, param);
  }
  
  public List<DataRow> findCurrentDaySigninInfos(String adtDate)
  {
    DataParam param = new DataParam(new Object[] { "adtDate", adtDate });
    String statementId = this.sqlNameSpace + "." + "findRecords";
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    return records;
  }
  
  public List<DataRow> findUserInfos(String userCodes)
  {
    DataParam param = new DataParam(new Object[] { "userCodes", userCodes });
    String statementId = this.sqlNameSpace + "." + "findUserInfos";
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    return records;
  }
  
  public List<DataRow> findSignLocationInfos(String userId)
  {
    DataParam param = new DataParam(new Object[] { "userId", userId });
    String statementId = this.sqlNameSpace + "." + "findSignLocationInfos";
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    return records;
  }
  
  public List<DataRow> findCurrentDaySignOutInfos(String expression, String adtDate)
  {
    DataParam param = new DataParam(new Object[] { "expression", expression, "adtDate", adtDate });
    String statementId = this.sqlNameSpace + "." + "findRecords";
    List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
    return records;
  }
  
  public DataRow findActiveUserId(String userCode) {
    DataParam param = new DataParam(new Object[] { "userCode", userCode });
    String statementId = this.sqlNameSpace + "." + "findActiveUserId";
    return this.daoHelper.getRecord(statementId, param);
  }
}
