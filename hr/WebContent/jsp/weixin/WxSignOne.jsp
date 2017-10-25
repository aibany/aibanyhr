<!doctype html>
<%@page import="com.agileai.util.StringUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request"
	class="com.agileai.hotweb.domain.PageBean" />
<html>
<head>
<meta charset="utf-8">
<title> </title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/jsp/inc/resource4jqm.inc.jsp"%>


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

table {
	border-collapse: collapse;
	border-spacing: 0;
	border-left: 1px solid #ccc;
	border-top: 1px solid #ccc;
	background: #ffffff;
	margin:10px auto;
	width: 100%;
}

th, td {
	border-right: 1px solid #ccc;
	border-bottom: 1px solid #ccc;
	padding: 3px 0px;
	text-align: center;
}

th {
	font-weight: bold;
	background: #dfdfdf;
}
</style>
<script type="text/javascript">
	$(function() {
		
<%if (session.getAttribute("errorMsg") != null) {%>
	$("#status").css("color", "red");
	$("#status").show();
<%} else if (pageBean.getStringValue("resultMsg") != null) {%>
	$("#status").css("color", "green");
		$("#status").show();
<%}%>

<%if (pageBean.getStringValue("userName") != null) {%>
	$("title").html("<%=pageBean.getAttribute("userName")%>的考勤记录"); 
<%}%>

	});

	function showBeforeDay() {
		doSubmit({
			actionType : 'showBeforeMonth'
		});
	}
	function showNextDay() {
		doSubmit({
			actionType : 'showNextMonth'
		});
	}
	function showTheMoth() {
		doSubmit({
			actionType : 'theMonth'
		});
	}
	
</script>
</head>
<body>
	<form action="index?WxSignOne" name="form1" id="form1" method="post"
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
		<div class="ui-grid-b" style="margin: 10px 0px 5px 0px;">
			<div class="ui-block-a" style="text-align: center;">
				<a href="javascript:showBeforeDay()" rel="external"
					data-ajax="false">上一月</a>
			</div>
			<div class="ui-block-b" style="text-align: center;">
				<a href="javascript:showTheMoth()" rel="external" data-ajax="false">本
					月</a>
			</div>
			<div class="ui-block-c" style="text-align: center;">
				<a href="javascript:showNextDay()" rel="external" data-ajax="false">下一月</a>
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
				<table>
				<tr>
					<th>日期</th>
					<th>签到</th>
					<th>签退</th>
					<th>类型</th>
					<th>加班</th>
				</tr>
				<%
					for (int i = 0; i < pageBean.getRsList().size(); i++) {
						String intime = pageBean.inputTime(i, "ATD_IN_TIME");
						String outtime = pageBean.inputTime(i, "ATD_OUT_TIME");
						if(StringUtil.isNotEmpty(intime) && intime.length() > 15) {
							intime = intime.substring(11,16);
						}
						if(StringUtil.isNotEmpty(outtime) && outtime.length() > 15) {
							outtime = outtime.substring(11,16);
						}
						System.out.println(intime + "," + outtime);
				%>
				<tr>
					<td><%=pageBean.inputTime(i, "ATD_IN_DAY").substring(5,10)%></td>
					<td>
					<%=intime%>
					</td>
					<td>
					<%=outtime%>
					</td>
					<td><%=pageBean.inputTime(i, "ATD_IN_HOUSE")%></td>
					<td><%=pageBean.inputTime(i, "ATD_OVERTIME")%></td>
				</tr>
				<%
					}
				%>
				</table>
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
</body>
</html>

