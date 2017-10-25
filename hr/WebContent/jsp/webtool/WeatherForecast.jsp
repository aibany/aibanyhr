<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta charset="utf-8">
<title>天气预报</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/jsp/inc/resource4jqm.inc.jsp"%>
<style type="text/css">
#status{
	color:red;
	border-style:solid; 
	border-width:0px; 
	border-color:blue;
	/*display:none;*/
}
</style>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%
String errorMsg = (String)request.getAttribute("errorMsg");
if (errorMsg != null){
%>
<div id="status" class="status">
<%=errorMsg%>
</div>
<%}else{%>
<div class="ui-grid-a" style="margin:10px 0px 5px 0px;">
	<div class="ui-block-a" style="text-align: center;"><%=pageBean.inputValue("currentCity")%></div>
	<div class="ui-block-b" style="text-align: center;">pm2.5：<%=pageBean.inputValue("pm25")%></div>
</div>
<div>
	<ul data-role="listview">
	<%
	for(int i=0;i < pageBean.getRsList().size();i++){
	%>	
		<li>
		<div>
		 <span style="float:left;margin-right: 10px"><img src="<%=pageBean.inputValue(i,"weatherPictureUrl")%>" /></span>
		 <span>
		     <div><%=pageBean.inputValue(i,"date")%></div>
			 <div class="ui-grid-b" style="margin:10px 0px 5px 0px;">
				<div class="ui-block-a"><%=pageBean.inputValue(i,"weather")%></div>
				<div class="ui-block-b"><%=pageBean.inputValue(i,"temperature")%></div>
				<div class="ui-block-c"><%=pageBean.inputValue(i,"wind")%></div>
			</div>
		</span>
		</div>
		</li>
	<%}%>
	</ul>
</div>
<%}%>
<input type="hidden" name="actionType" id="actionType" value=""/>
</form>
</body>
</html>
