package com.aibany.hr.filter;

import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.SystemLogService;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.common.BeanFactory;
import com.agileai.hotweb.common.HotwebAuthHelper;
import com.agileai.hotweb.domain.core.Profile;
import com.agileai.hotweb.domain.core.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jasig.cas.client.authentication.AttributePrincipal;

public class CASGeneralCasFilter
  implements Filter
{
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    AttributePrincipal attributePrincipal = (AttributePrincipal)httpRequest.getUserPrincipal();
    if (attributePrincipal != null) {
      String loginName = attributePrincipal.getName();
      String fromIpAddress = request.getLocalAddr();
      HttpSession session = httpRequest.getSession();
      Profile profile = (Profile)session.getAttribute("__HotwebProfile__");
      
      if (profile == null) {
        User user = new User();
        profile = new Profile(loginName, fromIpAddress, user);
        SecurityAuthorizationConfig authorizationConfig = (SecurityAuthorizationConfig)BeanFactory.instance().getBean("securityAuthorizationConfigService");
        DataRow userRow = authorizationConfig.retrieveUserRecord(loginName);
        
        HotwebAuthHelper hotwebAuthHelper = new HotwebAuthHelper(user);
        hotwebAuthHelper.initAuthedUser(userRow, user);
        
        writeSystemLog(fromIpAddress, user, "CAS认证权限初始化", "initAuth");
        
        session.setAttribute("__HotwebProfile__", profile);
      }
    }
    chain.doFilter(request, response);
  }
  
  protected void writeSystemLog(String ipAddress, User user, String content, String actionType) {
    String userId = user.getUserCode();
    String userName = user.getUserName();
    SystemLogService logService = (SystemLogService)BeanFactory.instance().getBean("sysLogService");
    logService.insertLogRecord(ipAddress, userId, userName, content, actionType);
  }
  
  public void destroy() {}
  
  public void init(FilterConfig arg0)
    throws ServletException
  {}
}
