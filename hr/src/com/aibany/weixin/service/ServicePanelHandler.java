package com.aibany.weixin.service;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.common.CommonHandler;
import com.aibany.weixin.model.Constans;
import com.aibany.weixin.tool.SecurityAuthHelper;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

public class ServicePanelHandler
  extends CommonHandler
{
  @PageAction
  public ViewRenderer init(DataParam param)
  {
    String responseText = "fail";
    String nonceStr = UUID.randomUUID().toString();
    String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
    String url = param.get("url");
    try {
      JSONObject jsonObject = new JSONObject();
      String accessToken = SecurityAuthHelper.getAccessToken();
      String jsapi_ticket = SecurityAuthHelper.getJsapiTicket(accessToken);
      String signature = SecurityAuthHelper.signJsApi(jsapi_ticket, nonceStr, timestamp, url);
      
      jsonObject.put("appId", Constans.Configs.APPID);
      jsonObject.put("timestamp", timestamp);
      jsonObject.put("nonceStr", nonceStr);
      jsonObject.put("signature", signature);
      
      responseText = jsonObject.toString();
    } catch (Exception e) {
      this.log.error(e.getLocalizedMessage(), e);
    }
    return new AjaxRenderer(responseText);
  }
}
