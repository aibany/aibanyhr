package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.domain.core.Profile;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.CryptionUtil;
import com.aibany.hr.common.CommonHandler;

public class ModifyPasswordHandler
  extends CommonHandler
{
  public ViewRenderer prepareDisplay(DataParam param)
  {
    Profile profile = getProfile();
    User user = (User)profile.getUser();
    String userCode = user.getUserCode();
    setAttribute("userCode", userCode);
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer confirmOldPwd(DataParam param) {
    String responseText = "fail";
    String userCode = param.get("userCode");
    SecurityAuthorizationConfig authorizationConfig = (SecurityAuthorizationConfig)lookupService(SecurityAuthorizationConfig.class);
    DataRow row = authorizationConfig.retriveUserInfoRecord(userCode);
    String userOldPwdInDb = row.stringValue("USER_PWD");
    String oldUserPwd = param.get("oldUserPwd");
    String encryptPwd = CryptionUtil.md5Hex(oldUserPwd);
    if (encryptPwd.equals(userOldPwdInDb)) {
      responseText = "success";
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer modifyPassword(DataParam param) {
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
