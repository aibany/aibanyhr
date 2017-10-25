<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>控制器列表</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
var configSecurityBox;
function configSecurityRequest(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	var operationId = $("#HANLER_ID").val();
	if (!configSecurityBox){
		configSecurityBox = new PopupBox('configSecurityBox','安全配置',{size:'normal',width:'700px',height:'450px',top:'2px'});
	}
	var url = 'index?SecurityAuthorizationConfig&resourceType=Handler&height=320&resourceId='+operationId;
	configSecurityBox.sendRequest(url);	
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ParamBar__" style="float: right;">
&nbsp;<input type="hidden" id="funcId" name="funcId" value="<%=pageBean.inputValue("funcId")%>" />

&nbsp;<input type="button" name="button" id="button" value="刷新" class="formbutton" onclick="doQuery()" />
</div>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="doRequest('insertRequest')"><input value="&nbsp;" title="新增" type="button" class="createImgBtn" />新增</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="E" align="center" onclick="doRequest('updateRequest')"><input value="&nbsp;" title="编辑" type="button" class="editImgBtn" />编辑</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="C" align="center" onclick="doRequest('copyRequest')"><input value="&nbsp;" title="复制" type="button" class="copyImgBtn" />复制</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="V" align="center" onclick="doRequest('viewDetail')"><input value="&nbsp;" title="查看" type="button" class="detailImgBtn" />查看</td>   
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="doDelete($('#'+rsIdTagId).val());"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" />删除</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doSubmit({actionType:'synchronousSecurity'});" class="bartdx" align="center"><input id="syncSecurityImgBtn" value="&nbsp;" title="同步权限" type="button" class="syncSecurityImgBtn" style="margin-right:0px;" />同步权限</td>         
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="configSecurityRequest()" class="bartdx" align="center"><input id="assignImgBtn" value="&nbsp;" title="安全设置" type="button" class="assignImgBtn" style="margin-right:0px;" />安全设置</td>         
   
</tr>
</table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="控制器列表.csv"
retrieveRowsCallback="process" xlsFileName="控制器列表.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="300px"
>
<ec:row styleClass="odd" ondblclick="clearSelection();doRequest('viewDetail')" oncontextmenu="selectRow(this,{HANLER_ID:'${row.HANLER_ID}'});refreshConextmenu()" onclick="selectRow(this,{HANLER_ID:'${row.HANLER_ID}'})">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="200" property="HANLER_CODE" title="编码"   />
	<ec:column width="200" property="HANLER_TYPE" title="类型"  mappingItem="HANLER_TYPE"/>
	<ec:column width="300" property="HANLER_URL" title="扩展URL"   />
</ec:row>
</ec:table>
<input type="hidden" name="HANLER_ID" id="HANLER_ID" value="" />
<input type="hidden" name="actionType" id="actionType" />
<script language="JavaScript">
setRsIdTag('HANLER_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
