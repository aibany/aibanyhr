package com.aibany.weixin.service;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.common.CommonHandler;

public class CurrentLocationHandler
  extends CommonHandler
{
  public ViewRenderer prepareDisplay(DataParam param)
  {
    return new LocalRenderer(getPage());
  }
}
