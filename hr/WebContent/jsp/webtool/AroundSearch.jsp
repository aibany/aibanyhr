<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta charset="utf-8">
<title>周边搜索</title>
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
<script type="text/javascript">
function doSearch(query){
	$("#query").val(query);
	doSubmit({actionType:'search'});
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<div class="ui-grid" style="margin:5px 5px 5px 2px;">
	<select id="radius" name="radius">
		<option value ="1000">1000米以内</option>
		<option value ="1500">1500米以内</option>
		<option value ="2000">2000米以内</option>
		<option value ="3000">3000米以内</option>
		<option value ="5000">5000米以内</option>
		<option value ="8000">8000米以内</option>
	</select>
	</div>
</div>
<div>
	<ol data-role="listview">
		<li><a href="javascript:doSearch('公交站')">公交站</a></li>
		<li><a href="javascript:doSearch('饭店')">饭店</a></li>
		<li><a href="javascript:doSearch('银行')">银行</a></li>
		<li><a href="javascript:doSearch('电影院')">电影院</a></li>
		<li><a href="javascript:doSearch('咖啡厅')">咖啡厅</a></li>
		<li><a href="javascript:doSearch('宾馆')">宾馆</a></li>
		<li><a href="javascript:doSearch('超市')">超市</a></li>
		<li><a href="javascript:doSearch('KTV')">KTV</a></li>
		<li><a href="javascript:doSearch('足道')">足道</a></li>
		<li><a href="javascript:doSearch('浴池')">浴池</a></li>
		<li><a href="javascript:doSearch('药店')">药店</a></li>
	</ol>
</div>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="query" id="query" value=""/>
<input type="hidden" name="location" id="location" value="<%=pageBean.getStringValue("location")%>"/>
</form>
</body>
</html>
