<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec" %>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>编码管理</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function changeTypeGroup(){
	postRequest('form1',{actionType:'changeTypeGroup',onComplete:function(rspText){
		$('#TYPE_ID').html(rspText);
	}});
}
function controlButton(trObj,params,isEditable){
	selectRow(trObj,params);
	if (isEditable == 'Y'){
		enableButton('editImgBtn');
		enableButton('delImgBtn');
	}else{
		disableButton('editImgBtn');
		disableButton('delImgBtn');		
	}
	$('#IS_EDITABLE').val(isEditable);
}
function showCodeType(){
	if (isValid($("#CODE_TYPE_ID").val())){
		doRequest('showCodeType');	
	}
	else if (isValid($("#TYPE_ID").val())){
		$("#CODE_TYPE_ID").val($("#TYPE_ID").val());
		doSubmit({actionType:'showCodeType'});
	}
	else{
		doRequest('showCodeType');
	}
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table class="toolTable" border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" hotKey="A" align="center" onClick="doRequest('insertRequest')"><input value="&nbsp;" title="新增" type="button" class="createImgBtn" />新增</td>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" hotKey="E" align="center" onClick="doRequest('updateRequest')"><input value="&nbsp;" id="editImgBtn" title="编辑" type="button" class="editImgBtn" />编辑</td>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" hotKey="C" align="center" onClick="doRequest('copyRequest')"><input value="&nbsp;" title="复制" type="button" class="copyImgBtn"  />复制</td>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" hotKey="V" align="center" onClick="doRequest('viewDetail')"><input value="&nbsp;" title="查看" type="button" class="detailImgBtn"  />查看</td>
<%if ("normal".equals(pageBean.inputValue("manageType"))){%>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" align="center" onClick="showCodeType()" ><input value="&nbsp;" id="showCodeList" title="查看类别" type="button" class="relateImgBtn"/>查看类别</td>  
<%}%>      
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" hotKey="D" align="center" onClick="doDelete($('#'+rsIdTagId).val());"><input value="&nbsp;" id="delImgBtn" title="删除" type="button" class="delImgBtn" />删除</td>
</tr>
</table>
</div>
<div id="__ParamBar__">
<table class="queryTable">
<tr><td>
<%if ("normal".equals(pageBean.inputValue("manageType"))){%>
分组<select name="TYPE_GROUP" id="TYPE_GROUP" onchange="changeTypeGroup()"><%=pageBean.selectValue("TYPE_GROUP")%></select>&nbsp;&nbsp;类型<select name="TYPE_ID" id="TYPE_ID" onChange="doQuery()"><%=pageBean.selectValue("TYPE_ID")%></select>
<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
<%}else if ("byType".equals(pageBean.inputValue("manageType"))){%>
<input type="hidden" name="TYPE_ID" id="TYPE_ID" value="<%=pageBean.inputValue("TYPE_ID")%>" />
<%}else if ("byGroup".equals(pageBean.inputValue("manageType"))){%>
<input type="hidden" name="TYPE_GROUP" id="TYPE_GROUP" value="<%=pageBean.inputValue("TYPE_GROUP")%>" />
类型<select name="TYPE_ID" id="TYPE_ID" onChange="doQuery()"><%=pageBean.selectValue("TYPE_ID")%></select>
<%}%></td>
</tr>
</table>
</div>
<ec:table 
var="row"
items="pageBean.rsList" 
retrieveRowsCallback="process" 
form="form1"
xlsFileName="编码管理.xls" 
csvFileName="编码管理.csv"
useAjax="true"
doPreload="false"
width="100%" 
listWidth="100%"
sortable="true" 
height="391px" 
rowsDisplayed="15"
excludeParameters="actionType"
>
<ec:row styleClass="odd" onclick="controlButton(this,{CODE_ID:'${row.CODE_ID}',CODE_TYPE_ID:'${row.TYPE_ID}'},'${row.IS_EDITABLE}')" oncontextmenu="controlButton(this,{CODE_ID:'${row.CODE_ID}',CODE_TYPE_ID:'${row.TYPE_ID}'},'${row.IS_EDITABLE}');refreshConextmenu()" ondblclick="clearSelection();doRequest('viewDetail')">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
    <ec:column width="300" property="CODE_ID" title="编码标识"/>
    <ec:column width="300" property="CODE_NAME" title="编码值"/>
    <ec:column width="120" property="TYPE_ID" mappingItem="TYPE_ID" title="编码类型"/>
    <ec:column width="70" property="IS_EDITABLE" title="是否可编辑"/>
    <ec:column width="70" property="CODE_FLAG" mappingItem="CODE_FLAG" title="是否有效"/>
    <ec:column width="60" property="CODE_SORT" title="编码排序"/>
</ec:row>
</ec:table>
<input type="hidden" name="CODE_ID" id="CODE_ID" />
<input type="hidden" name="CODE_TYPE_ID" id="CODE_TYPE_ID" />
<input type="hidden" name="IS_EDITABLE" id="IS_EDITABLE" />
<input type="hidden" name="manageType" id="manageType" value="<%=pageBean.inputValue("manageType")%>" />
<input type="hidden" name="actionType" id="actionType" value="" />
</form>
<script language="javascript">
setRsIdTag('CODE_ID','CODE_TYPE_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>