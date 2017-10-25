<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request"
	class="com.agileai.hotweb.domain.PageBean" />
<html>
<head>
<meta charset="utf-8">
<title>公司公告</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/jsp/inc/resource4jqm.inc.jsp"%>
<style type="text/css">
label.error {
	color: red;
}

#status {
	color: blue;
	margin-top: 10px;
	margin-left: 10px;
	border-style: solid;
	border-width: 0px;
	border-color: blue;
	display: inline-block;
	font-size: 18px;
}

table {
	border-collapse: collapse;
	border-spacing: 0;
	border-left: 1px solid #888;
	border-top: 1px solid #888;
	background: #efefef;
	margin-bottom:10px;
	margin-left: 10px;
}

th, td {
	border-right: 1px solid #888;
	border-bottom: 1px solid #888;
	padding: 5px 15px;
}

th {
	font-weight: bold;
	background: #ccc;
}
</style>

</head>
<body>
	<p id="status">1，
		打卡规则：</p>
	<table>
		<tr>
			<th></th>
			<th>签到时间</th>
			<th>签退时间</th>
		</tr>
		<tr>
			<th>白班</th>
			<td>06:00 ~ 14:00</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;23:59前</td>
		</tr>
		<tr>
			<th>夜班</th>
			<td>17:00 ~ 23:59</td>
			<td>次日12:00前</td>
		</tr>
	</table>
	<p id="status">
		2，请按正常流程进行签到签退，签到签退后请耐心等待操作结果，<font color="red">如果签到结果与预期不符，10分钟后可再次签到。</font>夜班签退的记录会放到前一天，与前一天的夜班签到进行配对。
	</p>
	<p id="status">
	3，加班上报规则：白班自动申请3小时，夜班自动申请3.5小时，不满0.5小时的不计入加班。超出最大申报的部分请自行登录HR系统申报。</p>
</body>
</html>