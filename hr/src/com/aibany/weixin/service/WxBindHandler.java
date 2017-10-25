package com.aibany.weixin.service;

import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.CryptionUtil;
import com.agileai.util.MapUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.hr.cxmodule.HrAttendanceManage;
import java.io.PrintStream;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WxBindHandler extends CommonHandler
{
  public ViewRenderer prepareDisplay(com.agileai.domain.DataParam param)
  {
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer bindWxUser(com.agileai.domain.DataParam param) {
    String responseText = "fail";
    String userId = param.get("userId");
    String userPwd = param.get("userPwd");
    
    System.out.println("fuck~~~~" + param.get("openId"));
    
    String valideCode = param.get("valideCode");
    String safecode = (String)this.request.getSession().getAttribute("safecode");
    if (!valideCode.equalsIgnoreCase(safecode)) {
      responseText = "验证码不正确，请检查！";
    } else {
      SecurityAuthorizationConfig authorizationConfig = (SecurityAuthorizationConfig)lookupService("securityAuthorizationConfigService");
      DataRow userRow = authorizationConfig.retrieveUserRecord(userId);
      if (MapUtil.isNullOrEmpty(userRow)) {
        responseText = "该用户不存在！";
      } else {
        String userPwdTemp = String.valueOf(userRow.get("USER_PWD"));
        String encryptedPassword = CryptionUtil.md5Hex(userPwd);
        if (userPwdTemp.equals(encryptedPassword)) {
          HrAttendanceManage attendanceManage = (HrAttendanceManage)lookupService(HrAttendanceManage.class);
          String openId = this.request.getParameter("openId");
          if ((openId != null) && (!openId.equals("")) && (!openId.equals("null"))) {
            getSessionAttributes().put("openId", openId);
          } else {
            openId = (String)getSessionAttributes().get("openId");
          }
          attendanceManage.bindUserWxOpenId(userId, openId);
          responseText = "success";
          writeSystemLog("微信绑定", userRow.getString("USER_CODE"), userRow.getString("USER_NAME"));
        }
        else {
          responseText = "用户ID或者密码不正确！";
        }
      }
    }
    return new AjaxRenderer(responseText);
  }
  
  @PageAction
  public ViewRenderer signIn(com.agileai.domain.DataParam param) {
    return new RedirectRenderer(getHandlerURL(WxSignInHandler.class));
  }
}
