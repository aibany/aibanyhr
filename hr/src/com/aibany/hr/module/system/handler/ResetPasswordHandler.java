package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.CryptionUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.module.system.service.SecurityGroupManage;

public class ResetPasswordHandler
  extends CommonHandler
{
  public ViewRenderer prepareDisplay(DataParam param)
  {
    String userId = param.get("USER_ID");
    DataParam queryParam = new DataParam(new Object[] { "USER_ID", userId });
    SecurityGroupManage groupManage = (SecurityGroupManage)lookupService(SecurityGroupManage.class);
    DataRow row = groupManage.getContentRecord("SecurityUser", queryParam);
    String userCode = row.stringValue("USER_CODE");
    setAttribute("userCode", userCode);
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer resetPassword(DataParam param) {
    String responseText = "fail";
    String userCode = param.get("userCode");
    SecurityAuthorizationConfig authorizationConfig = (SecurityAuthorizationConfig)lookupService(SecurityAuthorizationConfig.class);
    String newUserPwd = param.get("newUserPwd");
    String encryptPwd = CryptionUtil.md5Hex(newUserPwd);
    authorizationConfig.modifyUserPassword(userCode, encryptPwd);
    responseText = "success";
    return new AjaxRenderer(responseText);
  }
}
