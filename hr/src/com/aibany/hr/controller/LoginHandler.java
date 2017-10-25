package com.aibany.hr.controller;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.common.Constants;
import com.agileai.hotweb.common.HotwebAuthHelper;
import com.agileai.hotweb.domain.core.Profile;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.CryptionUtil;
import com.agileai.util.MapUtil;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.utils.CookieTool;
import com.aibany.weixin.service.StringCompressTool;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginHandler
  extends CommonHandler
{
  public ViewRenderer prepareDisplay(DataParam param)
  {
    if (getProfile(this.request) != null) {
      return new RedirectRenderer(getHandlerURL(Constants.FrameHandlers.HomepageHandlerId));
    }
    
    String userId = CookieTool.getCookieValueByName(this.request, "hrc");
    String userPwd = CookieTool.getCookieValueByName(this.request, "hrp");
    if ((StringUtil.isNotEmpty(userId)) && (StringUtil.isNotEmpty(userPwd))) {
      param.put("userId", StringCompressTool.decodePassword(userId));
      param.put("userPwd", StringCompressTool.decodePassword(userPwd));
      String safecode = (String)this.request.getSession().getAttribute("safecode");
      param.put("valideCode", safecode);
      int code = doLoginService(param);
      if (code == 3) {
        String wx = CookieTool.isWeiXinBrowser(this.request) ? "wx" : "";
        writeSystemLog("读取缓存登录" + wx);
        return new RedirectRenderer(getHandlerURL(Constants.FrameHandlers.HomepageHandlerId));
      }
    }
    
    return new LocalRenderer(getPage());
  }
  
  public ViewRenderer doLoginAction(DataParam param)
  {
    ViewRenderer result = null;
    
    int code = doLoginService(param);
    if (code == 1) {
      setErrorMsg("该用户不存在!");
      result = prepareDisplay(param);
    } else if (code == 2) {
      setErrorMsg("验证码错误!");
      result = prepareDisplay(param);
    } else if (code == 3) {
      result = prepareDisplay(param);
      writeSystemLog("账号密码登录");
      result = new RedirectRenderer(getHandlerURL(Constants.FrameHandlers.HomepageHandlerId));
    } else if (code == 4) {
      setErrorMsg("用户ID或者密码不正确!");
      result = prepareDisplay(param);
    }
    return result;
  }
  
  private int doLoginService(DataParam param)
  {
    String userId = param.get("userId");
    String userPwd = param.get("userPwd");
    String valideCode = param.get("valideCode");
    
    SecurityAuthorizationConfig authorizationConfig = (SecurityAuthorizationConfig)lookupService("securityAuthorizationConfigService");
    DataRow userRow = authorizationConfig.retrieveUserRecord(userId);
    if (MapUtil.isNullOrEmpty(userRow)) {
      clearCookie();
      return 1;
    }
    String safecode = (String)this.request.getSession().getAttribute("safecode");
    boolean openCode = false;
    if ((openCode) && (!valideCode.equalsIgnoreCase(safecode))) {
      return 2;
    }
    String userPwdTemp = String.valueOf(userRow.get("USER_PWD"));
    String encryptedPassword = CryptionUtil.md5Hex(userPwd);
    if (userPwdTemp.equals(encryptedPassword)) {
      User user = new User();
      String fromIpAddress = this.request.getLocalAddr();
      Profile profile = new Profile(userId, fromIpAddress, user);
      this.request.getSession().setAttribute("__HotwebProfile__", profile);
      HotwebAuthHelper hotwebAuthHelper = new HotwebAuthHelper(user);
      hotwebAuthHelper.initAuthedUser(userRow, user);
      
      int cacheSeconds = 7200;
      if (CookieTool.isWeiXinBrowser(this.request)) {
        cacheSeconds = 864000;
      }
      CookieTool.addCookie(this.response, "hrc", StringCompressTool.encodePassword(userId), cacheSeconds);
      CookieTool.addCookie(this.response, "hrn", StringCompressTool.encodePassword(user.getUserName()), cacheSeconds);
      CookieTool.addCookie(this.response, "hri", StringCompressTool.encodePassword(user.getUserId()), cacheSeconds);
      CookieTool.addCookie(this.response, "hrp", StringCompressTool.encodePassword(userPwd), cacheSeconds);
      
      return 3;
    }
    clearCookie();
    return 4;
  }
  
  protected Profile getProfile(HttpServletRequest request)
  {
    Profile profile = null;
    HttpSession session = request.getSession(false);
    if (session != null) {
      profile = (Profile)session.getAttribute("__HotwebProfile__");
    }
    return profile;
  }
  
  public ViewRenderer doLogoutAction(DataParam param) { writeSystemLog("退出系统");
    clearCookie();
    clearSession();
    return new RedirectRenderer(getHandlerURL(Constants.FrameHandlers.LoginHandlerId));
  }
  
  private void clearCookie() {
    CookieTool.removeCookie(this.response, "hrc");
    CookieTool.removeCookie(this.response, "hrp");
    CookieTool.removeCookie(this.response, "hri");
    CookieTool.removeCookie(this.response, "hrn");
  }
}
