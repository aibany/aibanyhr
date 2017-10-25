<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>请假人员查询选择</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function selectRequest(idValue,nameValue){
	parent.ele(ele('targetId').value).value= idValue;
	parent.ele(ele('targetName').value).value= nameValue;
	parent.PopupBox.closeCurrent();
}
function setSelectTempValue(idValue,nameValue){
	ele('targetIdValue').value = idValue;
	ele('targetNameValue').value = nameValue;
}
function doSelectRequest(){
	if (!isValid(ele('targetIdValue').value)){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	selectRequest(ele('targetIdValue').value,ele('targetNameValue').value);
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
	<td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="V" align="center" onclick="doSelectRequest()"><input value="&nbsp;" type="button" class="saveImgBtn" title="选择" />选择</td>
	<td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="B" align="center" onclick="javascript:parent.PopupBox.closeCurrent();"><input value="&nbsp;" type="button" class="closeImgBtn" title="关闭" />关闭</td>        
</tr>
</table>
</div>
<div>
<table class="queryTable">
<tr><td>
&nbsp;姓名<input id="userName" label="姓名" name="userName" type="text" value="<%=pageBean.inputValue("userName")%>" size="24" class="text" />

&nbsp;<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
</td></tr>
</table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="请假人员查询选择.csv"
retrieveRowsCallback="process" xlsFileName="请假人员查询选择.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="auto" 
>
<ec:row styleClass="odd" ondblclick="clearSelection();selectRequest('${row.USER_CODE}','${row.USER_NAME}')" onclick="setSelectTempValue('${row.USER_CODE}','${row.USER_NAME}')">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="100" property="USER_NAME" title="姓名"   />
</ec:row>
</ec:table>
<input type="hidden" name="actionType" id="actionType" />
<input type="hidden" name="targetId" id="targetId" value="<%=pageBean.inputValue("targetId")%>" />
<input type="hidden" name="targetName" id="targetName" value="<%=pageBean.inputValue("targetName")%>" />
<input type="hidden" name="targetIdValue" id="targetIdValue" value="" />
<input type="hidden" name="targetNameValue" id="targetNameValue" value="" />
<script language="JavaScript">
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
