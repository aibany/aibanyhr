package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.core.QueryModelService;
import com.agileai.hotweb.controller.core.QueryModelListHandler;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import java.util.Date;
import java.util.List;

public class SysLogQueryListHandler extends QueryModelListHandler
{
  public SysLogQueryListHandler()
  {
    this.detailHandlerClazz = SysLogQueryDetailHandler.class;
    this.serviceId = "systemLogQuery";
  }
  
  public ViewRenderer prepareDisplay(com.agileai.domain.DataParam param) {
    User user = (User)getUser();
    
    mergeParam(param);
    initParameters(param);
    setAttributes(param);
    List<DataRow> rsList = getService().findRecords(param);
    setRsList(rsList);
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer refresh(com.agileai.domain.DataParam param) {
    return doQueryAction(param);
  }
  
  protected void initParameters(com.agileai.domain.DataParam param) {
    Date yestoday = DateUtil.getDateAdd(new Date(), 1, -1);
    initParamItem(param, "OPER_STIME", DateUtil.getDateByType(9, yestoday));
    Date tomorrow = DateUtil.getDateAdd(new Date(), 1, 1);
    initParamItem(param, "OPER_ETIME", DateUtil.getDateByType(9, tomorrow));
  }
}
