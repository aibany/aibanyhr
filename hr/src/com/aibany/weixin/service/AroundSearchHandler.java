package com.aibany.weixin.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.aibany.hr.common.CommonHandler;
import com.aibany.weixin.model.Constans;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class AroundSearchHandler
  extends CommonHandler
{
  @PageAction
  public ViewRenderer search(DataParam param)
  {
    try
    {
      String radius = param.get("radius");
      String location = param.get("location");
      String[] locArray = location.split(",");
      String longitude = locArray[0];
      String latitude = locArray[1];
      String query = param.get("query");
      String url = "http://api.map.baidu.com/place/v2/search?ak=" + Constans.Configs.BAIDU_KEY + "&output=json&query=" + query + "&page_size=10&page_num=0&scope=1&location=" + latitude + "," + longitude + "&radius=" + radius;
      System.out.println("url is " + url);
      
      setAttribute("radius", radius);
      setAttribute("latitude", latitude);
      setAttribute("longitude", longitude);
      
      HttpClientHelper httpClientHelper = new HttpClientHelper();
      String jsonStr = httpClientHelper.retrieveGetReponsetText(url);
      JSONObject jsonObject = new JSONObject(jsonStr);
      if ("0".equals(jsonObject.getString("status")))
      {
        JSONArray resultDatas = jsonObject.getJSONArray("results");
        List<DataRow> records = new ArrayList();
        for (int i = 0; i < resultDatas.length(); i++) {
          DataRow row = new DataRow();
          JSONObject resultData = resultDatas.getJSONObject(i);
          JSONObject locationJSON = resultData.getJSONObject("location");
          row.put("latitude", locationJSON.getString("lat"));
          row.put("longitude", locationJSON.getString("lng"));
          row.put("name", resultData.getString("name"));
          row.put("address", resultData.getString("address"));
          records.add(row);
        }
        setRsList(records);
      } else {
        setErrorMsg("获取检索结果数据失败！");
      }
    } catch (Exception ex) {
      this.log.error(ex.getLocalizedMessage(), ex);
    }
    return new LocalRenderer("webtool/AroundResult.jsp");
  }
}
