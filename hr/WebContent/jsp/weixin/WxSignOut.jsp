<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request"
	class="com.agileai.hotweb.domain.PageBean" />
<html>
<head>
<meta charset="utf-8">
<title>微信签退</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/jsp/inc/resource4jqm.inc.jsp"%>
<script type="text/javascript"
	src="https://api.map.baidu.com/api?v=2.0&ak=UjymBYD3091SXF9ZW5FwpDlG"></script>
<script type="text/javascript"
	src="https://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>

<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"
	type="text/javascript"></script>


<style type="text/css">
.ui-select .ui-btn {
	padding: 3px 2px;
}

.ui-btn ui-input-btn {
	margin-top: 3px;
}

.ui-select .ui-btn span {
	text-align: left;
	padding-left: 3px;
}

#status {
	color: blue;
	border-style: solid;
	border-width: 0px;
	border-color: blue;
	display: none;
}
</style>
<script type="text/javascript">
	$(function() {
		
		 if(navigator.geolocation)
	        {
	            var geolocation = new BMap.Geolocation();  
	            var getGeoLocation = function geoLocation(callback) {
	                 geolocation.getCurrentPosition(function(r){  
	                       if(this.getStatus() == BMAP_STATUS_SUCCESS){  
	                           callback(true,r.point.lng,r.point.lat);
	                       }
	                       else {  
	                           alert('failed'+this.getStatus());  
	                       }          
	                   },{enableHighAccuracy: true})  
	                
	            }
	            
	            var map = new BMap.Map("bmap");
	            var callback = function locationCallBack(status , lng, lat) {
	                if(status == true) {
	                    
	                    //显示地图  
	                    var point = new BMap.Point(lng,lat);  
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
	                    map.panTo(point);  
	                    map.enableScrollWheelZoom();
	                    mk.enableDragging();

	                    mk.addEventListener("click", function(e){
	                       
	                    })
	                
	                    $("#bmap").css({"background":"white","width":"100%", "height":"200px"});
	                    
	    				$.post("resource?WxSignIn",{actionType:"getLocation","Latitude":lat,"Longitude":lng}, function(data){
	    					if(data.error) {
	    						$("#tip").css("color", "red").html(data.error);
	    					}else {
	    						var html = "";
	    						html = "请选择您的位置: <input type='button' value='签退' onclick='javascript:signclick()'/><br/>\n";
	    						html = html +	"<form id='sign' name='sign' method='POST' action='resource?WxSignOut'>\n";
	    						html = html + "<input type='hidden' id='location' name='location' value='"+lng +"," + lat + "' />";
	    						for(var i = 0; i < data.addresses.length; i++) {
	    							var addr = data.addresses[i];
	    							var check = "";
	    							if(i == 0) {
	    								check=" checked='checked' ";
	    							}
	    							html = html + "<p><input type='radio' id='address'" + check +
	    										 " name='address' value='" + addr +
	    										 "' />"+addr+
	    										 "</p>";
	    						}
	    						html=html + "</form>"
	    						$("#tip").css("color", "black").html(html);
	    						<%if (request.getAttribute("errorMsg") != null && request.getAttribute("errorMsg").toString().contains("微信绑定")) {%>
	    						$("#tip").hide();
	    						 $("#bmap").hide();
	    						<%}%>
	    					}
	    				}, "JSON");
	                }else {
	                    alert("定位失败，请检查定位服务");
	                }
	            }
	            
	            getGeoLocation(callback);
	            var timer = setInterval(function() {
	                getGeoLocation(callback);
	            },3000);
	            
	            setTimeout(function(){
	                clearInterval(timer);
	            },9000);
	        }
<%if (session.getAttribute("errorMsg") != null) {%>
	$("#status").css("color", "red");
	$("#status").show();
	<%if (session.getAttribute("errorMsg").toString().contains("地理位置")) {%>
		$("#tip").show();
	 	$("#bmap").show();
		$("#status").hide();
	<%}%>
<%} else if (pageBean.getStringValue("resultMsg") != null) {%>
	$("#status").css("color", "green");
		$("#status").show();
<%}%>
	});

	function showBeforeDay() {
		doSubmit({
			actionType : 'showBeforeDay'
		});
	}
	function showNextDay() {
		doSubmit({
			actionType : 'showNextDay'
		});
	}
	function showTheMoth() {
		doSubmit({
			actionType : 'theMonth'
		});
	}
	function showToday() {
		doSubmit({
			actionType : 'showToday'
		});
	}
	function bindWxUser() {
		window.location.href = "resource?WxBind";
	}
	
	function signclick() {
		$("#sign").submit();
	}
</script>
</head>
<body>
	<form action="resource?WxSignOut" name="form1" id="form1" method="post"
		style="padding: 7px 5px">
		<div id="status" class="status">
			<%
				String statusMsg = (String) session.getAttribute("errorMsg");
				if (statusMsg == null) {
					statusMsg = pageBean.getStringValue("resultMsg");
				}
			%>
			<%=statusMsg%>
			<%
				session.setAttribute("errorMsg", null);
			%>
		</div>
		<%
			if (pageBean.isValid(pageBean.inputTime("date"))) {
		%>
		<input type="hidden" name="date" id="date"
			value="<%=pageBean.inputTime("date")%>" style="text-align: center;">
		<div class="ui-grid-a" style="margin: 10px 0px 5px 0px;">
			<div class="ui-block-a" style="text-align: center;"><%=pageBean.inputTime("date")%></div>
			<div class="ui-block-b" style="text-align: center;"><%=pageBean.inputValue("week")%></div>
		</div>
		<div class="ui-grid-c" style="margin: 10px 0px 5px 0px;">
			<div class="ui-block-a" style="text-align: center;">
				<a href="javascript:showBeforeDay()" rel="external"
					data-ajax="false">上一天</a>
			</div>
			<div class="ui-block-b" style="text-align: center;">
				<a href="javascript:showToday()" rel="external" data-ajax="false">今
					天</a>
			</div>
			<div class="ui-block-c" style="text-align: center;">
				<a href="javascript:showNextDay()" rel="external" data-ajax="false">下一天</a>
			</div>
			<div class="ui-block-d" style="text-align: center;">
				<a href="javascript:showTheMoth()" rel="external" data-ajax="false">当月</a>
			</div>
		</div>
		<%
			}
		%>
		<%
			if (pageBean.isValid(pageBean.inputTime("date"))) {
		%>
		<div>
			<ul data-role="listview">
				<%
					if (pageBean.getRsList() != null && pageBean.getRsList().size() > 0) {
				%>
				<%
					for (int i = 0; i < pageBean.getRsList().size(); i++) {
				%>
				<li>
					<div>
						<span style="float: right;"><%=pageBean.inputTime(i, "ATD_OUT_TIME").substring(5, 16)%><span
							style="margin-left: 15px">（<%=i + 1%>）
						</span></span> <span style="color: #FF8C00"><%=pageBean.labelValue(i, "USER_ID_NAME")%></span>
					</div>
					<div><%=pageBean.inputTime(i, "ATD_OUT_PLACE")%></div>
					<div style="width:100%;height: 185px;margin-top: 5px;margin-bottom: 0px;">
						<img alt="" src="<%=pageBean.labelValue(i, "mapurl")%>" style="display:block;width:100%;"/>
					</div>
				</li>
				<%
					}
				%>
				<%}else{%>
				<li>
					<div>没有记录！</div>
				</li>
				<%}%>
			</ul>
		</div>
		<%}%>
		<input type="hidden" name="actionType" id="actionType" value="" />
	</form>
	<div id="bmap" style="display: none;"></div>
	<p id="tip" style="display: none;">正在定位中...</p>
</body>
</html>