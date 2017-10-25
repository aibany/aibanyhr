<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request"
	class="com.agileai.hotweb.domain.PageBean" />
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>考勤系统</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">

</script>
</head>
<body>
	<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1"
		method="post">
		<%@include file="/jsp/inc/message.inc.jsp"%>
		<div id="__ParamBar__" style="float: right;">&nbsp;</div>
		<div id="__ToolBar__">
			<table border="0" cellpadding="0" cellspacing="1">
				<tr>
					<aeai:previlege code="confirm"><td onmouseover="onMover(this);" onmouseout="onMout(this);"
						class="bartdx" align="center"
						onclick="doSubmit({actionType:'save',target:'_parent'})"><input value="&nbsp;"
						type="button" class="approveImgBtn" id="saveImgBtn" title="确认" />确认</td></aeai:previlege>
					<aeai:previlege code="close"><td onmouseover="onMover(this);" onmouseout="onMout(this);"
						class="bartdx" align="center" onclick="parent.PopupBox.closeCurrent()">
						<input value="&nbsp;" type="button" class="closeImgBtn" title="关闭" />关闭</td></aeai:previlege>
				</tr>
			</table>
		</div>
		<table class="detailTable" cellspacing="0" cellpadding="0">
			<tr>
				<th width="100" nowrap>考勤日期</th>
				<td><input id="ATD_DATE" label="签到日期" name="ATD_DATE"
					type="text" value="<%=pageBean.inputDate("ATD_DATE")%>" size="24"
					class="text" readonly="readonly" /></td>
			</tr>
			<tr>
				<th width="100" nowrap>姓名</th>
				<td><input name="USER_ID_NAME" type="text" class="text"
					id="USER_ID_NAME"
					value="<%=pageBean.inputValue("USER_ID_NAME")%>" size="24"
					readonly="readonly" label="姓名" /> <input id="USER_ID" label="姓名"
					name="USER_ID" type="hidden"
					value="<%=pageBean.inputValue("USER_ID")%>" size="24" class="text" />
				</td>
			</tr>
			<tr>
				<th width="100" nowrap>签到时间</th>
				<td><input id="ATD_IN_TIME" label="签到时间" name="ATD_IN_TIME"
					type="text" value="<%=pageBean.inputTime("ATD_IN_TIME")%>"
					size="24" class="text" <%if(!pageBean.getBoolValue("hasRight")){%>readonly="readonly"<%}%>  /></td>
			</tr>
			<tr>
				<th width="100" nowrap>签到地点</th>
				<td><input id="ATD_IN_PLACE" label="签到地点" name="ATD_IN_PLACE"
					type="text" value="<%=pageBean.inputValue("ATD_IN_PLACE")%>"
					size="24" class="text" <%=pageBean.readonly(!pageBean.getBoolValue("doSignIn") && !pageBean.getBoolValue("hasRight")) %> /></td>
			</tr>
			<%
				if (!pageBean.getBoolValue("doSignIn")) {
			%>
			<tr>
				<th width="100" nowrap>签退时间</th>
				<td><input name="ATD_OUT_TIME" type="text" class="text"
					id="ATD_OUT_TIME" value="<%=pageBean.inputTime("ATD_OUT_TIME")%>"
					size="24" <%if(!pageBean.getBoolValue("hasRight")){%>readonly="readonly"<%}%> label="签退时间" /></td>
			</tr>
			<tr>
				<th width="100" nowrap>签退地点</th>
				<td><input id="ATD_OUT_PLACE" label="签退地点" name="ATD_OUT_PLACE"
					type="text" value="<%=pageBean.inputValue("ATD_OUT_PLACE")%>"
					size="24" class="text" /></td>
			</tr>
			<%}%>
		</table>
		<input type="hidden" name="actionType" id="actionType" value="" /> <input
			type="hidden" name="operaType" id="operaType"
			value="<%=pageBean.getOperaType()%>" /> <input type="hidden"
			id="ATD_ID" name="ATD_ID"
			value="<%=pageBean.inputValue4DetailOrUpdate("ATD_ID", "")%>" />
	</form>
	<script language="javascript">
		requiredValidator.add("USER_ID");
		requiredValidator.add("ATD_IN_PLACE");
		<%
		if (!pageBean.getBoolValue("doSignIn")) {
		%>
		requiredValidator.add("ATD_OUT_PLACE");
		<%}%>
		initDetailOpertionImage();
	</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
