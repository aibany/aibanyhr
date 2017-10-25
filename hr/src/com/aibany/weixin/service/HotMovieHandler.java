package com.aibany.weixin.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
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

public class HotMovieHandler
  extends CommonHandler
{
  public ViewRenderer prepareDisplay(DataParam param)
  {
    try
    {
      String location = param.get("location");
      String url = "http://api.map.baidu.com/telematics/v3/movie?qt=hot_movie&location=" + location + "&output=json&ak=" + Constans.Configs.BAIDU_KEY;
      System.out.println("url is " + url);
      HttpClientHelper httpClientHelper = new HttpClientHelper();
      String jsonStr = httpClientHelper.retrieveGetReponsetText(url);
      JSONObject jsonObject = new JSONObject(jsonStr);
      if ("Success".equals(jsonObject.getString("status"))) {
        JSONObject content = jsonObject.getJSONObject("result");
        String cityname = content.getString("cityname");
        String date = jsonObject.getString("date");
        
        setAttribute("cityname", cityname);
        setAttribute("date", date);
        
        JSONArray movieDatas = content.getJSONArray("movie");
        List<DataRow> records = new ArrayList();
        for (int i = 0; i < movieDatas.length(); i++) {
          DataRow row = new DataRow();
          JSONObject movieData = movieDatas.getJSONObject(i);
          row.put("movie_name", movieData.getString("movie_name"));
          row.put("movie_type", movieData.getString("movie_type"));
          row.put("movie_release_date", movieData.getString("movie_release_date"));
          row.put("movie_nation", movieData.getString("movie_nation"));
          
          row.put("movie_starring", movieData.getString("movie_starring"));
          row.put("movie_length", movieData.getString("movie_length"));
          row.put("movie_picture", movieData.getString("movie_picture"));
          row.put("movie_score", movieData.getString("movie_score"));
          row.put("movie_director", movieData.getString("movie_director"));
          row.put("movie_tags", movieData.getString("movie_tags"));
          
          row.put("movie_tags", movieData.getString("movie_tags"));
          row.put("movie_message", movieData.getString("movie_message"));
          records.add(row);
        }
        setRsList(records);
      } else {
        setErrorMsg("获取热映电影数据失败！");
      }
    }
    catch (Exception ex) {
      this.log.error(ex.getLocalizedMessage(), ex);
    }
    return new LocalRenderer(getPage());
  }
}
