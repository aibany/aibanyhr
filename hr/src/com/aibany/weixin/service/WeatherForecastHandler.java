package com.aibany.weixin.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.aibany.hr.common.CommonHandler;
import com.aibany.weixin.model.Constans;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class WeatherForecastHandler
  extends CommonHandler
{
  public ViewRenderer prepareDisplay(DataParam param)
  {
    try
    {
      String location = param.get("location");
      String url = "http://api.map.baidu.com/telematics/v3/weather?location=" + location + "&output=json&ak=" + Constans.Configs.BAIDU_KEY;
      System.out.println("url is " + url);
      HttpClientHelper httpClientHelper = new HttpClientHelper();
      String jsonStr = httpClientHelper.retrieveGetReponsetText(url);
      JSONObject jsonObject = new JSONObject(jsonStr);
      if ("success".equals(jsonObject.getString("status"))) {
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        if (jsonArray.length() == 1) {
          JSONObject content = (JSONObject)jsonArray.get(0);
          String currentCity = content.getString("currentCity");
          String pm25 = content.getString("pm25");
          
          setAttribute("currentCity", currentCity);
          setAttribute("pm25", pm25);
          
          JSONArray weatherDatas = content.getJSONArray("weather_data");
          List<DataRow> records = new ArrayList();
          boolean isOnDay = isOnDay();
          for (int i = 0; i < weatherDatas.length(); i++) {
            DataRow row = new DataRow();
            JSONObject weatherData = weatherDatas.getJSONObject(i);
            row.put("date", weatherData.getString("date"));
            if (isOnDay) {
              row.put("weatherPictureUrl", weatherData.getString("dayPictureUrl"));
            } else {
              row.put("weatherPictureUrl", weatherData.getString("nightPictureUrl"));
            }
            row.put("weather", weatherData.getString("weather"));
            row.put("wind", weatherData.getString("wind"));
            row.put("temperature", weatherData.getString("temperature"));
            records.add(row);
          }
          setRsList(records);
        }
      } else {
        setErrorMsg("获取天气预报数据失败！");
      }
    }
    catch (Exception ex) {
      this.log.error(ex.getLocalizedMessage(), ex);
    }
    return new LocalRenderer(getPage());
  }
  
  private boolean isOnDay() {
    int hour = DateUtil.getHour();
    return (hour >= 6) && (hour < 18);
  }
}
