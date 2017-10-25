<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="javax.ccpp.SetAttribute"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>考勤系统</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script type="text/javascript">
var editBox;
function showEditBox(operType){
	clearSelection();
	if (!editBox){
		editBox = new PopupBox('editBox','详细信息',{size:'big',height:'400px',top:'30px'});
	}
	
	/*alert($("#ATD_ID").val());*/
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	
	var url = "<%=pageBean.getHandlerURL()%>&actionType="+operType+"&ATD_ID="+$("#ATD_ID").val();
	editBox.sendRequest(url);
}
function exportFile(fileType){
	$("#fileType").val(fileType);
	doSubmit({actionType:'export'});
	hideSplash();
	setTimeout(function(){
		$("#fileType").val("");
		$("#actionType").val("");
	},1000);
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table class="toolTable" border="0" cellpadding="0" cellspacing="1">
<tr>
   <aeai:previlege code="signIn"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="showEditBox('insertRequest')"><input id="singInImgBtn" value="&nbsp;" title="签到" type="button" class="createImgBtn" />签到</td></aeai:previlege>
   <aeai:previlege code="signOut"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="E" align="center" onclick="showEditBox('updateRequest')"><input id="singOutImgBtn" value="&nbsp;" title="签退" type="button" class="editImgBtn" />签退</td></aeai:previlege>
   <%if(pageBean.getBoolValue("hasRight")){%>
	   <aeai:previlege code="signUpdate"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="E" align="center" onclick="showEditBox('updateRequest')"><input id="singOutImgBtn" value="&nbsp;" title="签退" type="button" class="editImgBtn" />编辑</td></aeai:previlege>
   <%}%>
   <aeai:previlege code="exportWord"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="e" align="center" onclick="exportFile('word')"><input value="&nbsp;" title="导出WORD" type="button" class="wordImgBtn" style="margin-right:" />导出WORD</td></aeai:previlege>
   <aeai:previlege code="exportPdf"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="p" align="center" onclick="exportFile('pdf')"><input value="&nbsp;" title="导出PDF" type="button" class="pdfImgBtn" style="margin-right:" />导出PDF</td></aeai:previlege>
</tr>
</table>
</div>
<div id="__ParamBar__">
<table class="queryTable"><tr>
  <td>
&nbsp;日期
<input id="adtDate" label="签到时间" name="adtDate"   type="text" value="<%=pageBean.inputValue("adtDate")%>" size="10" class="text" /><img id="adtDatePicker" style="vertical-align:middle" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
&nbsp;<input id="userName" label="查询" name="userName" type="text" placeholder="输入姓名或工号查询该月记录" value="<%=pageBean.inputDate("userName")%>" size="24" class="text" ondblclick="emptyText('userName')" />
&nbsp;<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
&nbsp;<input type="submit" name="button" id="button" value="上一日" class="formbutton" onclick="doSubmit({actionType:'beforeDay'})" />
&nbsp;<input type="submit" name="button" id="button" value="下一日" class="formbutton" onclick="doSubmit({actionType:'nextDay'})" />
&nbsp;<input type="submit" name="button" id="button" value="当月" class="formbutton" onclick="doSubmit({actionType:'theMonth'})" />

</td>
</tr>
</table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="考勤系统.csv"
retrieveRowsCallback="process" xlsFileName="考勤系统.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="${ec_rd == null ?15:ec_rd}"
listWidth="100%" 
height="391px"
>
<ec:row styleClass="odd" onclick="selectRow(this,{ATD_ID:'${row.ATD_ID}'})">
	<ec:column width="30" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="50" property="USER_ID_NAME" title="姓名"  />
	<ec:column width="50" property="USER_ID_CODE" title="工号"  />
	<ec:column width="50" property="ATD_IN_DAY" title="日期" cell="date" format="MM-dd" />
	<ec:column width="50" property="ATD_IN_TIME" title="签到时间" cell="date" format="HH:mm" styleClass="signinTime" style="color:green;"/>
	<ec:column width="50" property="ATD_OUT_TIME" title="签退时间" cell="date" format="HH:mm" styleClass="signoutTime" style="color:green;" />
	<ec:column width="30" property="ATD_OVERTIME" title="申报加班"   />
	<ec:column width="50" property="ATD_IN_HOUSE" title="类型"   />
	<ec:column width="150" property="ATD_IN_PLACE" title="签到地点"   />
	<ec:column width="150" property="ATD_OUT_PLACE" title="签退地点"   />
</ec:row>
</ec:table>
<input type="hidden" name="ATD_ID" id="ATD_ID" value=""/>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" id="fileType" name="fileType" value=""/>
<input type="hidden" id="theMonth" name="theMonth" value="<%=pageBean.getAttribute("theMonth") %>"/>
<script language="JavaScript">
setRsIdTag('ATD_ID');
initCalendar('adtDate','%Y-%m-%d','adtDatePicker');
datetimeValidators[0].set("yyyy-MM-dd").add("adtDate");
<%
if(!pageBean.getBoolValue("canSignIn")){
%>
disableButton("singInImgBtn");
<%}%>

<%
if(!pageBean.getBoolValue("canSignOut")){
%>
disableButton("singOutImgBtn");
<%}%>
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
<%@include file="/jsp/inc/ecfix.jsp"%>
