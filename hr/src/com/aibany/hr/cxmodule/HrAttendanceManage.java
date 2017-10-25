package com.aibany.hr.cxmodule;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardService;
import java.util.List;

public abstract interface HrAttendanceManage
  extends StandardService
{
  public abstract DataRow retrieveUserInfo(DataParam paramDataParam);
  
  public abstract void bindUserWxOpenId(String paramString1, String paramString2);
  
  public abstract List<DataRow> attendanceStatisticsRecords(DataParam paramDataParam);
  
  public abstract List<DataRow> getAttendanceStatInfo(String paramString);
  
  public abstract List<DataRow> getAttendContentData(String paramString1, String paramString2);
  
  public abstract List<DataRow> getLeaveContentData(String paramString1, String paramString2);
  
  public abstract List<DataRow> getOverworkContentData(String paramString1, String paramString2);
  
  public abstract void createLocationRecord(DataParam paramDataParam);
  
  public abstract List<DataRow> findCurrentDaySigninInfos(String paramString);
  
  public abstract List<DataRow> findUserInfos(String paramString);
  
  public abstract List<DataRow> findSignLocationInfos(String paramString);
  
  public abstract List<DataRow> findCurrentDaySignOutInfos(String paramString1, String paramString2);
  
  public abstract DataRow findActiveUserId(String paramString);
}
