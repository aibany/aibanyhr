<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta charset="utf-8">
<title>热映电影</title>
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
	<div class="ui-block-a" style="text-align: center;"><%=pageBean.inputValue("cityname")%></div>
	<div class="ui-block-b" style="text-align: center;"><%=pageBean.inputValue("date")%></div>
</div>
<div>
	<ul data-role="listview">
	<%
	for(int i=0;i < pageBean.getRsList().size();i++){
	%>	
		<li>
		<div>
		 <span style="float:left;margin-right: 10px"><img style="width:115px;" src="<%=pageBean.inputValue(i,"movie_picture")%>" /></span>
		 <span>
		     <div><%=pageBean.inputValue(i,"movie_name")%>&nbsp;&nbsp;<%=pageBean.inputValue(i,"movie_type")%></div>
		     <div>国别：<%=pageBean.inputValue(i,"movie_nation")%>&nbsp;&nbsp;</div>
		     <div>上映：<%=pageBean.inputValue(i,"movie_release_date")%></div>
			 <div>时长：<%=pageBean.inputValue(i,"movie_length")%>&nbsp;&nbsp;评分:<%=pageBean.inputValue(i,"movie_score")%></div>
		     <div>演员：<%=pageBean.inputValue(i,"movie_starring")%></div>
			 <div>导演：<%=pageBean.inputValue(i,"movie_director")%></div>
			 <div>主题：<%=pageBean.inputValue(i,"movie_message")%></div>
		     <div>标签：<%=pageBean.inputValue(i,"movie_tags")%></div>
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
