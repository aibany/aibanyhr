package com.aibany.hr.controller;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.frame.OnlineCounterService;
import com.agileai.hotweb.common.BeanFactory;
import com.agileai.hotweb.common.Constants;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.StringUtil;
import com.aibany.hr.common.CommonHandler;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public class NavigaterHandler
  extends CommonHandler
{
  public static final String LOGIN_USER = "loginUser";
  
  public ViewRenderer prepareDisplay(DataParam param)
  {
    setAttribute("loginUser", getUser());
    String onlineAccount = this.dispatchServlet.getInitParameter("onlineCounterBeanId");
    if (!StringUtil.isNullOrEmpty(onlineAccount)) {
      OnlineCounterService onlineCounterService = (OnlineCounterService)BeanFactory.instance().getBean(onlineAccount);
      int onlineCount = onlineCounterService.queryOnlineCount();
      setAttribute("onlineCount", Integer.valueOf(onlineCount));
    }
    return new LocalRenderer(getPage());
  }
  
  public ViewRenderer doLogoutAction(DataParam param) {
    String casServerLogoutUrl = this.dispatchServlet.getServletContext().getInitParameter("casServerLogoutUrl");
    if (!StringUtil.isNullOrEmpty(casServerLogoutUrl)) {
      clearSession();
      String serverName = this.dispatchServlet.getServletContext().getInitParameter("serverName");
      String redirectUrl = casServerLogoutUrl + "?service=" + serverName + this.dispatchServlet.getServletContext().getContextPath();
      return new RedirectRenderer(redirectUrl);
    }
    return new RedirectRenderer(getHandlerURL(Constants.FrameHandlers.LoginHandlerId) + "&actionType=logout");
  }
}
