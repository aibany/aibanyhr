package com.aibany.hr.common;

import com.agileai.hotweb.domain.core.Role;
import com.agileai.hotweb.domain.core.User;
import java.util.List;

public class PrivilegeHelper
{
  private User user = null;
  
  public PrivilegeHelper(User user) { this.user = user; }
  
  public boolean isHRMASTER()
  {
    boolean result = false;
    
    if (this.user.getUserCode().equals("admin")) {
      return true;
    }
    
    List<Role> roleList = this.user.getRoleList();
    for (int i = 0; i < roleList.size(); i++) {
      Role role = (Role)roleList.get(i);
      if ("HR_MASTER".equals(role.getRoleCode())) {
        result = true;
        break;
      }
    }
    return result;
  }
  
  public boolean isApprove() { boolean result = false;
    
    if (this.user.getUserCode().equals("admin")) {
      return true;
    }
    
    List<Role> roleList = this.user.getRoleList();
    for (int i = 0; i < roleList.size(); i++) {
      Role role = (Role)roleList.get(i);
      if ("APPROVE".equals(role.getRoleCode())) {
        result = true;
        break;
      }
    }
    return result;
  }
  
  public boolean isSalMaster() { boolean result = false;
    
    if (this.user.getUserCode().equals("admin")) {
      return true;
    }
    
    List<Role> roleList = this.user.getRoleList();
    for (int i = 0; i < roleList.size(); i++) {
      Role role = (Role)roleList.get(i);
      if ("SALARY_MASTER".equals(role.getRoleCode())) {
        result = true;
        break;
      }
    }
    return result;
  }
}
