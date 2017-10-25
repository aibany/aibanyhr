package com.aibany.hr.module.location.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.aibany.hr.common.CommonListHandler;
import com.aibany.hr.module.location.service.HrLocationManage;
import java.util.Date;
import java.util.List;

public class HrLocationManageListHandler
  extends CommonListHandler
{
  public HrLocationManageListHandler()
  {
    this.serviceId = buildServiceId(HrLocationManage.class);
  }
  
  public ViewRenderer prepareDisplay(DataParam param)
  {
    mergeParam(param);
    initParameters(param);
    setAttributes(param);
    List<DataRow> rsList = getService().findRecords(param);
    setRsList(rsList);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(DataParam param) {}
  
  protected void initParameters(DataParam param)
  {
    User user = (User)getUser();
    initParamItem(param, "userName", user.getUserName());
    initParamItem(
      param, 
      "sdate", 
      DateUtil.getDateByType(9, 
      DateUtil.getBeginOfMonth(new Date())));
    initParamItem(
      param, 
      "edate", 
      DateUtil.getDateByType(9, 
      DateUtil.getDateAdd(new Date(), 1, 1)));
  }
  
  protected HrLocationManage getService() {
    return (HrLocationManage)lookupService(getServiceId());
  }
}
