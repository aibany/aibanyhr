<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>用户列表</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
var addUserTreeBox;
function showAddUserTreeBoxBox(){
	if (!addUserTreeBox){
		addUserTreeBox = new PopupBox('addUserTreeBox','请选择目标目录',{size:'normal',height:'410px',width:'310px',top:'3px'});
	}
	var handlerId = "SecurityUserTreeSelect";
	var url = 'index?'+handlerId+'&roleId='+$("#roleId").val();
	addUserTreeBox.sendRequest(url);
}
function addUserTreeRelation(userIds){
	if (userIds != ""){
		$('#userIds').val(userIds);
		doSubmit({actionType:'addUserTreeRelation'});
	}
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="showAddUserTreeBoxBox();"><input value="&nbsp;" type="button" class="addImgBtn" id="addImgBtn" title="添加" />添加</td>
<td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="doRequest('delUserTreeRelation');"><input value="&nbsp;" type="button" class="delImgBtn" id="delImgBtn" title="删除" />删除</td>   
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="R" align="center" onclick="doSubmit({actionType:'prepareDisplay'})"><input value="&nbsp;" type="button" class="cancelImgBtn" id="cancelImgBtn" title="刷新" />刷新</td>   
</tr>
</table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="用户列表.csv"
retrieveRowsCallback="process" xlsFileName="用户列表.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="337px" 
>
<ec:row styleClass="odd" oncontextmenu="selectRow(this,{ROLE_ID:'${row.ROLE_ID}',USER_ID:'${row.USER_ID}'});refreshConextmenu()" onclick="selectRow(this,{ROLE_ID:'${row.ROLE_ID}',USER_ID:'${row.USER_ID}'})">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="100" property="USER_CODE" title="用户编码"   />
	<ec:column width="100" property="USER_NAME" title="用户名称"   />
	<ec:column width="100" property="USER_STATE" title="用户状态"   mappingItem="USER_STATE"/>
</ec:row>
</ec:table>
<input type="hidden" name="ROLE_ID" id="ROLE_ID" value="" />
<input type="hidden" name="USER_ID" id="USER_ID" value="" />
<input type="hidden" name="actionType" id="actionType" />
<input type="hidden" id="roleId" name="roleId" value="<%=pageBean.inputValue("roleId")%>" />
<input type="hidden" id="userIds" name="userIds" value="" />
<script language="JavaScript">
setRsIdTag('ROLE_ID,USER_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
