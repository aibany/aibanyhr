<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>角色管理</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function doRefresh(nodeId){
	$('#ROLE_ID').val(nodeId);
	doSubmit({actionType:'refresh'});
}
var targetTreeBox;
function showParentSelectBox(){
	if (!targetTreeBox){
		targetTreeBox = new PopupBox('targetTreeBox','请选择目标目录',{size:'normal',width:'300px',top:'3px'});
	}
	var handlerId = "SecurityRoleParentSelect";
	var url = 'index?'+handlerId+'&ROLE_ID='+$("#ROLE_ID").val();
	targetTreeBox.sendRequest(url);
}
function doChangeParent(){
	postRequest('form1',{actionType:'changeParent',onComplete:function(responseText){
		if (responseText == 'success'){
			doRefresh($('#ROLE_ID').val());			
		}else {
			alert('迁移父节点出错啦！');
		}
	}});
}
function doSave(){
	if (checkSave()){
		$("#operaType").val('update');
		doSubmit({actionType:'save',checkUnique:'true'});
	}
}
function checkSave(){
	var result = true;
if (validation.checkNull($('#ROLE_CODE').val())){
	writeErrorMsg($("#ROLE_CODE").attr("label")+"不能为空!");
	selectOrFocus('ROLE_CODE');
	return false;
}
if (validation.checkNull($('#ROLE_NAME').val())){
	writeErrorMsg($("#ROLE_NAME").attr("label")+"不能为空!");
	selectOrFocus('ROLE_NAME');
	return false;
}
if (validation.checkNull($('#ROLE_STATE').val())){
	writeErrorMsg($("#ROLE_STATE").attr("label")+"不能为空!");
	selectOrFocus('ROLE_STATE');
	return false;
}
if ($('#ROLE_DESC').val().length > 128){
	writeErrorMsg($("#ROLE_DESC").attr("label")+"长度不能大于"+128+"!");
	selectOrFocus('ROLE_DESC');
	return false;
}
	return result;
}
function doMoveUp(){
	doSubmit({actionType:'moveUp'});
}
function doMoveDown(){
	doSubmit({actionType:'moveDown'});
}
function doDelete(){
	if (confirm('确定要进行节点删除操做吗？')){
		doSubmit({actionType:'delete'});
	}
}
function doInsertChild(){
	if (checkInsertChild()){
		$("#operaType").val('insert');
		doSubmit({actionType:'insertChild',checkUnique:'true'});
	}
}
function checkInsertChild(){
	var result = true;
if (validation.checkNull($('#CHILD_ROLE_CODE').val())){
	writeErrorMsg($("#CHILD_ROLE_CODE").attr("label")+"不能为空!");
	selectOrFocus('CHILD_ROLE_CODE');
	return false;
}
if (validation.checkNull($('#CHILD_ROLE_NAME').val())){
	writeErrorMsg($("#CHILD_ROLE_NAME").attr("label")+"不能为空!");
	selectOrFocus('CHILD_ROLE_NAME');
	return false;
}
if (validation.checkNull($('#CHILD_ROLE_STATE').val())){
	writeErrorMsg($("#CHILD_ROLE_STATE").attr("label")+"不能为空!");
	selectOrFocus('CHILD_ROLE_STATE');
	return false;
}
if ($('#CHILD_ROLE_DESC').val().length > 128){
	writeErrorMsg($("#CHILD_ROLE_DESC").attr("label")+"长度不能大于"+128+"!");
	selectOrFocus('CHILD_ROLE_DESC');
	return false;
}
	return result;
}
function doCancel(){
	doRefresh($('#ROLE_ID').val());
}
function changeTab(tabId){
	$('#currentTabId').val(tabId);
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<table width="100%" style="margin:0px;">
<tr>
	<td valign="top">
	<div id="leftTree" class="sharp color2" style="margin-top:0px;">
	<b class="b1"></b><b class="b2"></b><b class="b3"></b><b class="b4"></b>
    <div class="content">
    <h3 class="portletTitle">&nbsp;&nbsp;分组列表</h3>        
	<div id="treeArea" style="overflow:auto; height:420px;width:230px;background-color:#F9F9F9;padding-top:5px;padding-left:5px;">
	<%=pageBean.getStringValue("menuTreeSyntax")%></div>
    <b class="b9"></b>
    </div>
    </div>
	</td>
	<td width="85%" valign="top">
<div class="photobg1" id="tabHeader">
    <div class="newarticle1" onclick="changeTab('0')">基本信息</div>
    <div class="newarticle1" onclick="changeTab('1')">关联用户</div>
    <div class="newarticle1" onclick="changeTab('2')">关联群组</div>
</div>
<div class="photobox newarticlebox" id="Layer0" style="height:427px;">
	<div style="padding:0 5px 5px 5px; margin-top:9px;">
	    <div id="__ToolBar__" style="margin-top: 2px">
   		<table border="0" cellpadding="0" cellspacing="1">
	    <tr>
		<td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doSave()"<%}%> class="bartdx" hotKey="E" align="center"><input id="saveImgBtn" value="&nbsp;" title="保存" type="button" class="saveImgBtn" style="margin-right:0px;" />保存</td>
	    <td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doDelete()"<%}%> class="bartdx" hotKey="D" align="center"><input id="delImgBtn" value="&nbsp;" title="删除" type="button" class="delImgBtn" style="margin-right:0px;" />删除</td>
		    
	    <td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="showParentSelectBox()"<%}%> class="bartdx" align="center"><input id="moveImgBtn" value="&nbsp;" title="迁移" type="button" class="moveImgBtn" style="margin-right:0px;" />迁移</td>
	    <td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doMoveUp()"<%}%> class="bartdx" align="center"><input id="upImgBtn" value="&nbsp;" title="上移" type="button" class="upImgBtn" style="margin-right:0px;" />上移</td>
	    <td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doMoveDown()"<%}%> class="bartdx" align="center"><input id="downImgBtn" value="&nbsp;" title="下移" type="button" class="downImgBtn" style="margin-right:0px;" />下移</td>
	    </tr></table></div>    
	    <table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>编码</th>
	<td><input id="ROLE_CODE" label="编码" name="ROLE_CODE" type="text" value="<%=pageBean.inputValue("ROLE_CODE")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>名称</th>
	<td><input id="ROLE_NAME" label="名称" name="ROLE_NAME" type="text" value="<%=pageBean.inputValue("ROLE_NAME")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>状态</th>
	<td><select id="ROLE_STATE" label="状态" name="ROLE_STATE" class="select"><%=pageBean.selectValue("ROLE_STATE")%></select>
</td>
</tr>
<tr>
	<th width="100" nowrap>描述</th>
	<td><textarea id="ROLE_DESC" style="width:300px;height: 50px;" label="描述" name="ROLE_DESC" cols="40" rows="3" class="textarea"><%=pageBean.inputValue("ROLE_DESC")%></textarea>
</td>
</tr>
	    </table>
	</div>	
    
	<div style="margin-top: 10px;padding: 5px;">
	    <div id="__ToolBar__">
		<table border="0" cellpadding="0" cellspacing="1">
	    <tr>
		<td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="doInsertChild()"><input value="&nbsp;" title="新增" type="button" class="createImgBtn" style="margin-right:0px;" />新增</td>
		<td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="doCancel()"><input value="&nbsp;" title="取消" type="button" class="cancelImgBtn" style="margin-right:0px;" />取消</td>    
	    </tr></table>
	    </div>     
	    <table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>编码</th>
	<td><input id="CHILD_ROLE_CODE" label="编码" name="CHILD_ROLE_CODE" type="text" value="<%=pageBean.inputValue("CHILD_ROLE_CODE")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>名称</th>
	<td><input id="CHILD_ROLE_NAME" label="名称" name="CHILD_ROLE_NAME" type="text" value="<%=pageBean.inputValue("CHILD_ROLE_NAME")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>状态</th>
	<td><select id="CHILD_ROLE_STATE" label="状态" name="CHILD_ROLE_STATE" class="select"><%=pageBean.selectValue("CHILD_ROLE_STATE")%></select>
</td>
</tr>
<tr>
	<th width="100" nowrap>描述</th>
	<td><textarea id="CHILD_ROLE_DESC" style="width:300px;height: 40px;"  label="描述" name="CHILD_ROLE_DESC" cols="40" rows="2" class="textarea"><%=pageBean.inputValue("CHILD_ROLE_DESC")%></textarea>
</td>
</tr>
	    </table>
	</div>
    <br />
</div>
<div class="photobox newarticlebox" id="Layer1" style="height:427px;;display:none;overflow:hidden;">
<iframe id="UserFrame" src="index?SecurityUserQueryList&roleId=<%=pageBean.inputValue("ROLE_ID")%>" width="100%" height="430" frameborder="0" scrolling="no"></iframe>
</div>
<div class="photobox newarticlebox" id="Layer2" style="height:427px;display:none;overflow:hidden;">
<iframe id="GroupFrame" src="index?SecurityGroupQueryList&roleId=<%=pageBean.inputValue("ROLE_ID")%>" width="100%" height="430" frameborder="0" scrolling="no"></iframe>
</div>   
    </td>    
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value=""/>
<input type="hidden" id="ROLE_ID" name="ROLE_ID" value="<%=pageBean.inputValue("ROLE_ID")%>" />
<input type="hidden" id="ROLE_PID" name="ROLE_PID" value="<%=pageBean.inputValue("ROLE_PID")%>" />
<input type="hidden" id="ROLE_SORT" name="ROLE_SORT" value="<%=pageBean.inputValue("ROLE_SORT")%>" />
<input type="hidden" id="currentTabId" name="currentTabId" value="<%=pageBean.inputValue("currentTabId")%>" />
</form>
</body>
</html>
<script language="javascript">
<%if(pageBean.getBoolValue("isRootNode")){%>
setImgDisabled('saveImgBtn',true);
setImgDisabled('delImgBtn',true);
setImgDisabled('copyImgBtn',true);
setImgDisabled('moveImgBtn',true);
setImgDisabled('upImgBtn',true);
setImgDisabled('downImgBtn',true);
<%}%>
</script>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
<script language="javascript">
var tab = new Tab('tab','tabHeader','Layer',0);
tab.focus(<%=pageBean.inputValue("currentTabId")%>);
$(function(){
	resetTabHeight(80);
	resetTreeHeight(80);
});
</script>
