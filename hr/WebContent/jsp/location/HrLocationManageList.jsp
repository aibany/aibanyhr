<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>定位查看</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ParamBar__">
<table class="queryTable"><tr><td>
&nbsp;起止日期<input id="sdate" label="开始时间" name="sdate" type="text" value="<%=pageBean.inputDate("sdate")%>" size="10" class="text" readonly="readonly"/><img id="sdatePicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
-<input id="edate" label="截止时间" name="edate" type="text" value="<%=pageBean.inputDate("edate")%>" size="10" class="text"  readonly="readonly"/><img id="edatePicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
&nbsp;姓名 <input id="userName" label="姓名 " name="userName" type="text" value="<%=pageBean.inputDate("userName")%>" size="24" class="text" ondblclick="emptyText('userName')" /><input type="hidden" label="人员选择" id="USER_CODE" name="USER_CODE" value="<%=pageBean.inputValue("USER_CODE")%>" /><img id="userIdSelectImage" src="images/sta.gif" width="16" height="16" onclick="openUserIdBox()" />
&nbsp;<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
</td></tr></table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="定位查看.csv"
retrieveRowsCallback="process" xlsFileName="定位查看.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="${ec_rd == null ?30:ec_rd}"
listWidth="100%" 
height="510px"
>
<ec:row styleClass="odd" oncontextmenu="selectRow(this,{LOCAT_ID:'${row.LOCAT_ID}'});refreshConextmenu()" onclick="selectRow(this,{LOCAT_ID:'${row.LOCAT_ID}'})">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="50" property="USER_NAME" title="姓名"   />
	<ec:column width="50" property="LOCAT_TIME" title="时间" cell="date" format="yyyy-MM-dd HH:mm" />
	<ec:column width="100" property="LOCAT_PLACE" title="地点"   />
</ec:row>
</ec:table>
<input type="hidden" name="LOCAT_ID" id="LOCAT_ID" value="" />
<input type="hidden" name="actionType" id="actionType" />
<script language="JavaScript">
setRsIdTag('LOCAT_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
initCalendar('sdate','%Y-%m-%d','sdatePicker');
datetimeValidators[0].set("yyyy-MM-dd").add("sdate");
initCalendar('edate','%Y-%m-%d','edatePicker');
datetimeValidators[0].set("yyyy-MM-dd").add("edate");
var userIdBox;
function openUserIdBox(){
	var handlerId = "UserListSelectList"; 
	if (!userIdBox){
		userIdBox = new PopupBox('userIdBox','请选择人员      ',{size:'normal',width:'300',top:'2px'});
	}
	var url = 'index?'+handlerId+'&targetId=USER_CODE&targetName=userName';
	userIdBox.sendRequest(url);
} 
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
