<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec" %>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>编码类型</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function deleteRecord(){
	var typeId = $('#'+rsIdTagId).val();
	if (!isValid(typeId)){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	
	postRequest('form1',{actionType:'checkHasData',onComplete:function(rspText){
		if (rspText == 'true'){
			writeErrorMsg('该类别下还有编码定义，不能删除!');
		}else{
			doDelete(typeId);
		}
	}});
}
function controlButton(trObj,params,isUniteAdmin){
	selectRow(trObj,params);
	if (isUniteAdmin == 'Y'){
		enableButton('showCodeList');
	}else{
		disableButton('showCodeList');	
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
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" hotKey="A" align="center" onClick="doRequest('insertRequest')"><input value="&nbsp;" title="新增" type="button" class="createImgBtn" id="createImgBtn"/>新增</td>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" hotKey="E" align="center" onClick="doRequest('updateRequest')"><input value="&nbsp;" title="编辑" type="button" class="editImgBtn" id="editImgBtn"  />编辑</td>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" hotKey="C" align="center" onClick="doRequest('copyRequest')"><input value="&nbsp;" title="复制" type="button" class="copyImgBtn" id="copyImgBtn"  />复制</td>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" hotKey="V" align="center" onClick="doRequest('viewDetail')" ><input value="&nbsp;" title="查看" type="button" class="detailImgBtn" id="detailImgBtn" />查看</td>
   <td id="showCodeListTd" onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" align="center" onClick="doRequest('showCodeList')" ><input value="&nbsp;" id="showCodeList" title="查看编码" type="button" class="relateImgBtn"/>查看编码</td>      
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" hotKey="D" align="center" onClick="deleteRecord()"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" id="delImgBtn"  />删除</td>
</tr>
</table>
</div>
<div id="__ParamBar__">
<table class="queryTable">
<tr><td>
<select name="TYPE_GROUP" id="TYPE_GROUP" onChange="doQuery()"><%=pageBean.selectValue("TYPE_GROUP")%></select>
<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
</td></tr>
</table>
</div>
<ec:table 
var="row"
items="pageBean.rsList" 
retrieveRowsCallback="process" 
form="form1"
xlsFileName="编码类型.xls" 
csvFileName="编码类型.csv"
useAjax="true"
doPreload="false"
width="100%" 
listWidth="100%" 
sortable="true" 
height="391px" 
rowsDisplayed="15"
excludeParameters="actionType"
>
<ec:row styleClass="odd" onclick="controlButton(this,'${row.TYPE_ID}','${row.IS_UNITEADMIN}')" oncontextmenu="controlButton(this,'${row.TYPE_ID}','${row.IS_UNITEADMIN}');refreshConextmenu()" ondblclick="clearSelection();doRequest('viewDetail')">
    <ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
    <ec:column width="150" property="TYPE_ID" title="类型标识"/>
    <ec:column width="150" property="TYPE_NAME" title="类型名称"/>
    <ec:column width="150" property="TYPE_GROUP" mappingItem="TYPE_GROUP" title="类型分组"/>
    <ec:column width="100" property="EXTEND_SQL" mappingItem="EXTEND_SQL" title="是否SQL扩展"/> 
</ec:row> 
</ec:table>
<input type="hidden" name="TYPE_ID" id="TYPE_ID" />
<input type="hidden" name="actionType" id="actionType" />
</form>
<script language="javascript">
setRsIdTag('TYPE_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>