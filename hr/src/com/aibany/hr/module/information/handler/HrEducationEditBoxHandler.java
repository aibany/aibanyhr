package com.aibany.hr.module.information.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.MasterSubEditPboxHandler;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.module.information.service.HrEmployeeManage;

public class HrEducationEditBoxHandler extends MasterSubEditPboxHandler
{
  public HrEducationEditBoxHandler()
  {
    this.serviceId = buildServiceId(HrEmployeeManage.class);
    this.subTableId = "HrEducation";
  }
  
  public ViewRenderer prepareDisplay(DataParam param) { String operaType = param.get("operaType");
    if ("update".equals(operaType)) {
      DataRow record = getService().getSubRecord(this.subTableId, param);
      setAttributes(record);
      DataParam empParam = new DataParam(new Object[] { "EMP_ID", record.get("EMP_ID") });
      DataRow empRecord = getService().getMasterRecord(empParam);
      if (empRecord.get("EMP_STATE").equals("drafe")) {
        setAttribute("doDetail", Boolean.valueOf(true));
      } else {
        setAttribute("doDetail", Boolean.valueOf(false));
      }
    }
    if ("detail".equals(operaType)) {
      DataRow record = getService().getSubRecord(this.subTableId, param);
      setAttributes(record);
      DataParam empParam = new DataParam(new Object[] { "EMP_ID", record.get("EMP_ID") });
      DataRow empRecord = getService().getMasterRecord(empParam);
      if (empRecord.get("EMP_STATE").equals("drafe")) {
        setAttribute("doDetail", Boolean.valueOf(true));
      } else {
        setAttribute("doDetail", Boolean.valueOf(false));
      }
    }
    if ("insert".equals(operaType)) {
      setAttribute("doDetail", Boolean.valueOf(true));
    }
    setOperaType(operaType);
    processPageAttributes(param);
    
    CommonHandler.writeSystemLog("学习经历" + operaType, getActionType(), this);
    return new LocalRenderer(getPage());
  }
  
  protected void processPageAttributes(DataParam param) { if (!StringUtil.isNullOrEmpty(param.get("EMP_ID"))) {
      setAttribute("EMP_ID", param.get("EMP_ID"));
    }
  }
  
  protected HrEmployeeManage getService() {
    return (HrEmployeeManage)lookupService(getServiceId());
  }
}
