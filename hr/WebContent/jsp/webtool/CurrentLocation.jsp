<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta charset="utf-8">
<title>当前位置</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/jsp/inc/resource4jqm.inc.jsp"%>
<link href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" rel="stylesheet" />
<script	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=j6x1iGtGaxQcxaEDgbHRKDUp"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>
</head>
<body>
<div data-role="page" id="page">
</div>
</body>
</html>
<script type="text/javascript">
$(function() {
	
    if(navigator.geolocation) {  
       var map = new BMap.Map("page");  
       
      	var searchInfoWindow = new BMapLib.SearchInfoWindow(map, "", {
       		title: "我的位置", //标题
       		panel : "panel", //检索结果面板
       		enableAutoPan : true, //自动平移
       		searchTypes :[
       			BMAPLIB_TAB_FROM_HERE, //从这里出发
       			BMAPLIB_TAB_SEARCH   //周边检索
       		]
       	});
       
       var geolocation = new BMap.Geolocation();  
       geolocation.getCurrentPosition(function(r){  
           if(this.getStatus() == BMAP_STATUS_SUCCESS){  
               	var point = new BMap.Point(r.point.lng,r.point.lat);  
               	map.centerAndZoom(point,17);  
               
        	   	var mk = new BMap.Marker(point);  
        	   	
            	var label = new BMap.Label("我在这里",{offset:new BMap.Size(20,-10)});
              	label.setStyle({
              		"color":"green", 
              		"fontSize":"14px",
              		"border":"1",
              		"textAlign":"center"
        		});
              	mk.setLabel(label);
              	
               	map.addOverlay(mk);  
               	map.panTo(r.point);  
               
               	mk.enableDragging();
               	mk.addEventListener("click", function(e){
            	   searchInfoWindow.open(mk);
               	})
           }
           else {  
               alert('failed'+this.getStatus());  
           }          
       },{enableHighAccuracy: true})  
    }  
});  
</script>