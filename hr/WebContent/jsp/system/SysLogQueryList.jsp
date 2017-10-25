<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec" %>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>系统日志</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script type="text/javascript">
var detailBox;
function showDetailBox(){
	if (!isSelectedRow()){
		writeErrorMsg("请先选中一条记录！");
		return;
	}
	if (!detailBox){
		detailBox = new PopupBox('detailBox','明细信息查看',{size:'big',height:'400px',top:'30px'});
	}
	var url = "<%=pageBean.getHandlerURL()%>&actionType=viewDetail&ID="+$('#ID').val();
	detailBox.sendRequest(url);
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table class="toolTable" border="0" cellpadding="0" cellspacing="1">
  <tr>
    <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" align="center" onClick="showDetailBox()"><input id="viewDetail" value="&nbsp;" type="button" class="detailImgBtn" title="查看" />查看</td>
	<td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" align="center" onClick="doSubmit({actionType:'refresh'});"><input id="refreshImgBtn" value="&nbsp;" type="button" class="refreshImgBtn" title="刷新" />刷新</td>      
  </tr>
</table>
</div>
<div id="__ParamBar__">
<table class="queryTable">
<tr><td>
<input name="OPER_STIME" type="text" id="OPER_STIME" value="<%=pageBean.inputValue("OPER_STIME")%>" size="10" maxlength="10" /><img id="OPER_STIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框"> 至 <input name="OPER_ETIME" type="text" id="OPER_ETIME" value="<%=pageBean.inputValue("OPER_ETIME")%>" size="10" maxlength="10" /><img id="OPER_ETIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框">
IP地址<input name="IP_ADDTRESS" type="text" id="IP_ADDTRESS" value="<%=pageBean.inputValue("IP_ADDTRESS")%>" size="10" />
<%if(pageBean.getAttribute("userId") == null){ %>
用户ID<input name="USER_ID" type="text" id="USER_ID" value="<%=pageBean.inputValue("USER_ID")%>" size="6" />
<%} %>
功能名称<input name="FUNC_NAME" type="text" id="FUNC_NAME" value="<%=pageBean.inputValue("FUNC_NAME")%>" size="8" />
操作<input name="ACTION_TYPE" type="text" id="ACTION_TYPE" value="<%=pageBean.inputValue("ACTION_TYPE")%>" size="6" />
&nbsp;<input type="button" name="button" id="button" value="查询" class="formbutton" onClick="doQuery()"></td>
</tr>
</table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" 
retrieveRowsCallback="process" 
xlsFileName="系统日志.xls" 
csvFileName="系统日志.csv"
useAjax="true"
doPreload="false"
width="100%" 
listWidth="100%" 
height="391px" 
sortable="true"
rowsDisplayed="15"
excludeParameters="actionType"
>
<ec:row styleClass="odd" onclick="selectRow(this,{ID:'${row.ID}'})" ondblclick="clearSelection();showDetailBox()">
    <ec:column width="30" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
    <ec:column width="150" property="OPER_TIME" cell="date" format="yyyy-MM-dd HH:mm:ss" title="操作时间"/>
    <ec:column width="150" property="IP_ADDTRESS" title="所在地址"/>
    <ec:column width="80" property="USER_ID" title="用户标识"/>
    <ec:column width="80" property="USER_NAME" title="用户名称"/>
    <ec:column width="150" property="FUNC_NAME" title="功能名称"/>
    <ec:column width="120" property="ACTION_TYPE" title="操作类型"/>        
</ec:row> 
</ec:table>
<input type="hidden" name="ID" id="ID" value=""/>
<input type="hidden" name="actionType" id="actionType" />
</form>
<script language="javascript">
datetimeValidators[0].set("yyyy-MM-dd").add("OPER_STIME");
datetimeValidators[0].set("yyyy-MM-dd").add("OPER_ETIME");
initCalendar('OPER_STIME','%Y-%m-%d','OPER_STIMEPicker');
initCalendar('OPER_ETIME','%Y-%m-%d','OPER_ETIMEPicker');
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>