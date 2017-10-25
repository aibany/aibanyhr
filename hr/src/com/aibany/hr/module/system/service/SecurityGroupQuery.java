package com.aibany.hr.module.system.service;

import com.agileai.hotweb.bizmoduler.core.QueryModelService;

public abstract interface SecurityGroupQuery
  extends QueryModelService
{
  public abstract void addGroupTreeRelation(String paramString, String[] paramArrayOfString);
  
  public abstract void delGroupTreeRelation(String paramString1, String paramString2);
}
