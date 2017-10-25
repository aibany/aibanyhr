<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta charset="utf-8">
<title>搜索结果</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/jsp/inc/resource4jqm.inc.jsp"%>
<link href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" rel="stylesheet" />
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=j6x1iGtGaxQcxaEDgbHRKDUp"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>
</head>
<body>
<%
String errorMsg = (String)request.getAttribute("errorMsg");
if (errorMsg != null){
%>
<div id="status" class="status">
<%=errorMsg%>
</div>
<%}else{%>
<div data-role="page" id="page">
</div>
<script type="text/javascript">
$(function() {
	
    if(navigator.geolocation) {  
		var map = new BMap.Map("page");  
       	var point = new BMap.Point(<%=pageBean.inputValue("longitude")%>,<%=pageBean.inputValue("latitude")%>);  
       	
       	var mk = new BMap.Marker(point);
       	
    	var label = new BMap.Label("我在这里",{offset:new BMap.Size(20,-10)});
      	label.setStyle({
      		"color":"green", 
      		"fontSize":"14px",
      		"border":"1",
      		"textAlign":"center"
		});
      	mk.setLabel(label);
      	
       	/*
      	var searchInfoWindow = new BMapLib.SearchInfoWindow(map, "", {
       		title: "我的位置", //标题
       		panel : "panel", //检索结果面板
       		enableAutoPan : true, //自动平移
       		searchTypes :[
       			BMAPLIB_TAB_FROM_HERE, //从这里出发
       			BMAPLIB_TAB_SEARCH   //周边检索
       		]
       	});
        mk.addEventListener("click", function(e){
      	   searchInfoWindow.open(mk);
         })
		*/

		mk.enableDragging();
		map.panTo(point);
		map.addOverlay(mk);
		map.centerAndZoom(point,16); 

		map.addOverlay(new BMap.Circle(point,<%=pageBean.inputValue("radius")%>));
		
        <%
    	for(int i=0;i < pageBean.getRsList().size();i++){
    	%>
    	var point<%=i%> = new BMap.Point(<%=pageBean.inputValue(i,"longitude")%>,<%=pageBean.inputValue(i,"latitude")%>);
    	
    	var marker<%=i%> = new BMap.Marker(point<%=i%>);
    	var label<%=i%> = new BMap.Label("<%=pageBean.inputValue(i,"name")%>",{offset:new BMap.Size(20,-10)});
      	label<%=i%>.setStyle({
      		"color":"red", 
      		"fontSize":"13px",
      		"border":"1",
      		"textAlign":"center"
		});
    	marker<%=i%>.setLabel(label<%=i%>);
      	map.addOverlay(marker<%=i%>);
      	<%}%>
      	
    }  
});  
</script>
<%}%>
</body>
</html>