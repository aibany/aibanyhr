package com.aibany.hr.module.system.service;

import com.agileai.hotweb.bizmoduler.core.QueryModelService;

public abstract interface SecurityUserQuery
  extends QueryModelService
{
  public abstract void addUserTreeRelation(String paramString, String[] paramArrayOfString);
  
  public abstract void delUserTreeRelation(String paramString1, String paramString2);
}
