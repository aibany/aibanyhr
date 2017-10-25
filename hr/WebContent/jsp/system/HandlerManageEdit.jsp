<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" import="java.util.*" %>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<%
String currentSubTableId = pageBean.getStringValue("currentSubTableId");
String currentSubTableIndex = pageBean.getStringValue("currentSubTableIndex");
%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>控制器列表</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function saveMasterRecord(){
	if (validate()){
		postRequest('form1',{actionType:'saveMasterRecord',onComplete:function(responseText){
			if ("fail" != responseText){
				alert('保存成功！');
				$('#operaType').val('update');
				$('#HANLER_ID').val(responseText);
				doSubmit({actionType:'prepareDisplay'});
			}else{
				alert('保存操作出错啦！');
			}
		}});
	}
}
function changeSubTable(subTableId){
	$('#currentSubTableId').val(subTableId);
	doSubmit({actionType:'changeSubTable'});
}
function refreshPage(){
	doSubmit({actionType:'changeSubTable'});
}
function addEntryRecord(subTableId){
	$('#currentSubTableId').val(subTableId);
	doSubmit({actionType:'addEntryRecord'});
}
function deleteEntryRecord(subTableId){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	if (confirm('确认要删除该条记录吗？')){
		$('#currentSubTableId').val(subTableId);
		doSubmit({actionType:'deleteEntryRecord'});	
	}
}
function saveEntryRecords(subTableId){
	if(checkEentryRecords(subTableId)){
		$('#currentSubTableId').val(subTableId);
		doSubmit({actionType:'saveEntryRecords'});		
	}
}
function checkEentryRecords(subTableId){
	var result = true;
	var currentRecordSize = $('#currentRecordSize').val();
	if ("SysOperation"==subTableId){
		for (var i=0;i < currentRecordSize;i++){
			if (validation.checkNull($("#OPER_CODE"+"_"+i).val())){
				writeErrorMsg($("#OPER_CODE"+"_"+i).attr("label")+"不能为空!");
				selectOrFocus('OPER_CODE'+'_'+i);
				return false;
			}
			if (validation.checkNull($("#OPER_ACTIONTPYE"+"_"+i).val())){
				writeErrorMsg($("#OPER_ACTIONTPYE"+"_"+i).attr("label")+"不能为空!");
				selectOrFocus('OPER_ACTIONTPYE'+'_'+i);
				return false;
			}
			if (validation.checkNull($("#OPER_NAME"+"_"+i).val())){
				writeErrorMsg($("#OPER_NAME"+"_"+i).attr("label")+"不能为空!");
				selectOrFocus('OPER_NAME'+'_'+i);
				return false;
			}
		}
	}
	return result;
}
var insertSubRecordBox;
function insertSubRecordRequest(title,handlerId){
	if (!insertSubRecordBox){
		insertSubRecordBox = new PopupBox('insertSubRecordBox',title,{size:'normal',height:'300px',top:'10px'});
	}
	var url = 'index?'+handlerId+'&operaType=insert&HANLER_ID='+$('#HANLER_ID').val();
	insertSubRecordBox.sendRequest(url);	
}
var copySubRecordBox;
function copySubRecordRequest(title,handlerId,subPKField){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	if (!copySubRecordBox){
		copySubRecordBox = new PopupBox('copySubRecordBox',title,{size:'normal',height:'300px',top:'10px'});
	}
	var url = 'index?'+handlerId+'&operaType=insert&'+subPKField+'='+$("#"+subPKField).val();
	copySubRecordBox.sendRequest(url);	
}
var viewSubRecordBox;
function viewSubRecordRequest(operaType,title,handlerId,subPKField){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	if (!viewSubRecordBox){
		viewSubRecordBox = new PopupBox('viewSubRecordBox',title,{size:'normal',height:'300px',top:'10px'});
	}
	var url = 'index?'+handlerId+'&operaType='+operaType+'&'+subPKField+'='+$("#"+subPKField).val();
	viewSubRecordBox.sendRequest(url);
}
function deleteSubRecord(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	if (confirm('确认要删除该条记录吗？')){
		doSubmit({actionType:'deleteSubRecord'});	
	}
}
function doMoveUp(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	doSubmit({actionType:'moveUp'});
}
function doMoveDown(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}	
	doSubmit({actionType:'moveDown'});
}
var configSecurityBox;
function configSecurityRequest(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	var index = $("#currentRecordIndex").val();
	var operationId = $("#OPER_ID_"+index).val();
	if (!isValid(operationId)){
		writeErrorMsg('该条记录状态不正确，请检查！');
		return;
	}
	if (!configSecurityBox){
		configSecurityBox = new PopupBox('configSecurityBox','安全配置',{size:'normal',width:'700px',height:'450px',top:'2px'});
	}
	var url = 'index?SecurityAuthorizationConfig&resourceType=Operation&resourceId='+operationId;
	configSecurityBox.sendRequest(url);	
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ParamBar__" style="float: right;">&nbsp;</div>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="enableSave()" ><input value="&nbsp;" type="button" class="editImgBtn" id="modifyImgBtn" title="编辑" />编辑</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="saveMasterRecord();"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="goToBack();"><input value="&nbsp;" type="button" class="backImgBtn" title="返回" />返回</td>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>编码</th>
	<td><input id="HANLER_CODE" label="编码" name="HANLER_CODE" type="text" value="<%=pageBean.inputValue("HANLER_CODE")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>类型</th>
	<td>&nbsp;<%=pageBean.selectRadio("HANLER_TYPE")%>
</td>
</tr>
<tr>
	<th width="100" nowrap>扩展URL</th>
	<td><input name="HANLER_URL" type="text" class="textarea" id="HANLER_URL" value="<%=pageBean.inputValue("HANLER_URL")%>" size="40" label="扩展URL" /></td>
</tr>
</table>

<%if (!"insert".equals(pageBean.getOperaType())){%>
<div class="photobg1" id="tabHeader">
 <div class="newarticle1" onclick="changeSubTable('SysOperation')">页面操作定义</div>
</div>
<%if ("SysOperation".equals(currentSubTableId)){ %>
<div class="photobox newarticlebox" id="Layer0" style="height:auto;">    
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="addEntryRecord('SysOperation')"><input value="&nbsp;" title="新增" type="button" class="createImgBtn" />新增</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="deleteEntryRecord('SysOperation')"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" />删除</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="S" align="center" onClick="saveEntryRecords('SysOperation')"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="C" align="center" onclick="refreshPage()"><input value="&nbsp;" title="取消" type="button" class="cancelImgBtn" />取消</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doMoveUp()" class="bartdx" align="center"><input id="upImgBtn" value="&nbsp;" title="上移" type="button" class="upImgBtn" style="margin-right:0px;" />上移</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doMoveDown()" class="bartdx" align="center"><input id="downImgBtn" value="&nbsp;" title="下移" type="button" class="downImgBtn" style="margin-right:0px;" />下移</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="configSecurityRequest()" class="bartdx" align="center"><input id="assignImgBtn" value="&nbsp;" title="安全设置" type="button" class="assignImgBtn" style="margin-right:0px;" />安全设置</td>         
</tr>   
   </table>
</div>
<div style="margin:2px;">
<table border="0" cellpadding="0" cellspacing="0" class="dataTable ecSide" id="dataTable">
<thead>
  <tr>
    <th width="80" align="center" nowrap="nowrap">序号</th>
	<th width="100" align="center">按钮编码</th>
	<th width="100" align="center">动作编码</th>
	<th width="100" align="center">操作名称</th>
  </tr>
</thead>
<tbody>
<%
List paramRecords = (List)pageBean.getAttribute("SysOperationRecords");
pageBean.setRsList(paramRecords);
int paramSize = pageBean.listSize();
for (int i=0;i < paramSize;i++){
%>
  <tr onmouseout="ECSideUtil.unlightRow(this);" onmouseover="ECSideUtil.lightRow(this);" onclick="ECSideUtil.selectRow(this,'form1');selectRow(this,{currentRecordIndex:'<%=i%>'})">
	<td style="text-align:center"><%=i+1%>
<input type="hidden" id="OPER_ID_<%=i%>" label="OPER_ID" name="OPER_ID_<%=i%>" value="<%=pageBean.inputValue(i,"OPER_ID")%>" />
<input type="hidden" id="HANLER_ID_<%=i%>" label="HANLER_ID" name="HANLER_ID_<%=i%>" value="<%=pageBean.inputValue(i,"HANLER_ID")%>" />
<input type="hidden" id="OPER_SORT_<%=i%>" label="OPER_SORT" name="OPER_SORT_<%=i%>" value="<%=pageBean.inputValue(i,"OPER_SORT")%>" />
	<input id="state_<%=i%>" name="state_<%=i%>" type="hidden" value="<%=pageBean.inputValue(i,"_state")%>" />
	</td>
	<td><input id="OPER_CODE_<%=i%>" label="按钮编码" name="OPER_CODE_<%=i%>" type="text" value="<%=pageBean.inputValue(i,"OPER_CODE")%>" size="24" class="text" />
</td>
	<td><input id="OPER_ACTIONTPYE_<%=i%>" label="动作编码" name="OPER_ACTIONTPYE_<%=i%>" type="text" value="<%=pageBean.inputValue(i,"OPER_ACTIONTPYE")%>" size="24" class="text" />
</td>
	<td><input id="OPER_NAME_<%=i%>" label="操作名称" name="OPER_NAME_<%=i%>" type="text" value="<%=pageBean.inputValue(i,"OPER_NAME")%>" size="24" class="text" />
</td>
  </tr>
<%}%>
</tbody>  
</table>
</div>
</div>
<input type="hidden" id="currentRecordSize" name="currentRecordSize" value="<%=pageBean.listSize()%>" />
<input type="hidden" id="currentRecordIndex" name="currentRecordIndex" value="" />
<script language="javascript">
<%for (int i=0;i < paramSize;i++){%>
	$("input[id$='_<%=i%>'],select[id$='_<%=i%>']").change(function(){
		if ($("#state_<%=i%>").val()==""){
			$("#state_<%=i%>").val('update');
		}
	});
<%}%>
setRsIdTag('currentRecordIndex');
</script>
<%}%>
<input type="hidden" id="currentSubTableId" name="currentSubTableId" value="<%=pageBean.inputValue("currentSubTableId")%>" />
<script language="javascript">
new Tab('tab','tabHeader','Layer',<%=currentSubTableIndex%>);
</script>
<%}%>

<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" id="HANLER_ID" name="HANLER_ID" value="<%=pageBean.inputValue4DetailOrUpdate("HANLER_ID","")%>" />
<input type="hidden" id="FUNC_ID" name="FUNC_ID" value="<%=pageBean.inputValue("FUNC_ID")%>" />
</form>
<script language="javascript">
requiredValidator.add("HANLER_CODE");
initDetailOpertionImage();
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
