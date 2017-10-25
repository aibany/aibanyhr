<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta charset="utf-8">
<title>工具面板</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/jsp/inc/resource4jqm.inc.jsp"%>
<script	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=j6x1iGtGaxQcxaEDgbHRKDUp"></script>
<script type="text/javascript">
function scanQRCode(){
	if ($("#inited").val()!='false'){
		wx.scanQRCode({
		    needResult: 0, 
		    //scanType: ["qrCode","barCode"],
		    scanType: ["qrCode"],
		    success: function (res) {
		    	var result = res.resultStr;
			}
		});			
	}else{
		alert("微信JSSDK初始化失败！");			
	}
}
var theURL = null;
function redirct8AttacheLocaltion(url)
{
	theURL = url
    var geolocation = new BMap.Geolocation();  
    geolocation.getCurrentPosition(function(r){
        if(this.getStatus() == BMAP_STATUS_SUCCESS){  
        	doRedirct(r.point);
        }
        else {  
            alert('failed'+this.getStatus());  
        }
    },{enableHighAccuracy: true});  
}

function doRedirct(point)
{
	var location=point.lng+","+point.lat;
	window.location.href = theURL+"&location="+location;
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post" style="padding:7px 5px">
<div id="status" class="status"></div>
<div>
	<ol data-role="listview">
		<li><a href="javascript:scanQRCode()" data-ajax="false">扫二维码</a></li>
		<li><a href="<%=request.getContextPath()%>/index?CurrentLocation" data-ajax="false">我的位置</a></li>
		<li><a href="javascript:redirct8AttacheLocaltion('<%=request.getContextPath()%>/index?WeatherForecast')" data-ajax="false">天气预报</a></li>
		<li><a href="javascript:redirct8AttacheLocaltion('<%=request.getContextPath()%>/index?AroundSearch')" data-ajax="false">周边搜索</a></li>
		<li><a href="javascript:redirct8AttacheLocaltion('<%=request.getContextPath()%>/index?HotMovie')" data-ajax="false">热映电影</a></li>
	</ol>
</div>
<input type="hidden" name="url" id="url" value="" />
<input type="hidden" name="inited" id="inited" value="false" />
<input type="hidden" name="actionType" id="actionType" value=""/>
</form>
</body>
</html>
<script type="text/javascript">
$("#url").val(window.location.href);
postRequest("form1",{actionType:"init",onComplete:function(responseText){
	if (responseText != 'fail'){
		var jsonObject = $.parseJSON(responseText);
		wx.config({
		    debug:false, 
		    appId:jsonObject.appId, 
		    timestamp:jsonObject.timestamp, 
		    nonceStr:jsonObject.nonceStr,
		    signature:jsonObject.signature,
		    jsApiList: ['scanQRCode','getLocation','openLocation']
		});
		$("#inited").val("true");
	}
}});
</script>