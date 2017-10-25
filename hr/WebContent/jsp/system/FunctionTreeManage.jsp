<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>功能管理</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function doRefresh(nodeId){
	$('#FUNC_ID').val(nodeId);
	doSubmit({actionType:'refresh'});
}
var targetTreeBox;
function showParentSelectBox(){
	if (!targetTreeBox){
		targetTreeBox = new PopupBox('targetTreeBox','请选择目标目录',{size:'normal',width:'300px',top:'3px'});
	}
	var handlerId = "FunctionParentSelect";
	var url = 'index?'+handlerId+'&FUNC_ID='+$("#FUNC_ID").val();
	targetTreeBox.sendRequest(url);
}
function doChangeParent(){
	postRequest('form1',{actionType:'changeParent',onComplete:function(responseText){
		if (responseText == 'success'){
			doRefresh($('#FUNC_ID').val());			
		}else {
			alert('迁移父节点出错啦！');
		}
	}});
}
function doSave(){
	if (checkSave()){
		$("#operaType").val('update');
		doSubmit({actionType:'save'});
	}
}
function checkSave(){
	var result = true;
	if (validation.checkNull($('#FUNC_NAME').val())){
		writeErrorMsg($("#FUNC_NAME").attr("label")+"不能为空!");
		selectOrFocus('FUNC_NAME');
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
function doCopyCurrent(){
	doSubmit({actionType:'copyCurrent'});
}
function doDelete(){
	if (confirm('确定要进行节点删除操做吗？')){
		doSubmit({actionType:'delete'});
	}
}
function doInsertChild(){
	if (checkInsertChild()){
		$("#operaType").val('insert');
		doSubmit({actionType:'insertChild'});
	}
}
function checkInsertChild(){
	var result = true;
	if (validation.checkNull($('#CHILD_FUNC_NAME').val())){
		writeErrorMsg($("#CHILD_FUNC_NAME").attr("label")+"不能为空!");
		selectOrFocus('CHILD_FUNC_NAME');
		return false;
	}
	if ($("#CHILD_FUNC_TYPE").val()!='funcmenu' && validation.checkNull($('#CHILD_MAIN_HANDLER').val())){
		writeErrorMsg($("#CHILD_MAIN_HANDLER").attr("label")+"不能为空!");
		selectOrFocus('CHILD_MAIN_HANDLER');
		return false;
	}
	return result;
}
function doCancel(){
	doRefresh($('#FUNC_ID').val());
}
function showHandlerList(){
	if (!isValid($('#HandlerFrame').attr('src'))){
		var url = "index?HandlerManageList&funcId=<%=pageBean.inputValue("FUNC_ID")%>&randomKey="+Math.random();
		$('#HandlerFrame').attr('src',url);
	}
}
function showSecurityConfig(){
	if (!isValid($('#SecurityFrame').attr('src'))){
		var url = "index?SecurityAuthorizationConfig&resourceType=Menu&resourceId=<%=pageBean.inputValue("FUNC_ID")%>&randomKey="+Math.random();
		$('#SecurityFrame').attr('src',url);	
	}
}
function showValidTr(fieldTypeValue){
	if (fieldTypeValue=="funcmenu"){
		$("#MAIN_HANDLER_TR").css({display:"none"});
	}
	else {
		$("#MAIN_HANDLER_TR").css({display:""});		
	}
}
function focusTab(tabId){
	var reqUrl = "<%=pageBean.getHandlerURL()%>&actionType=focusTab&currentTabId="+tabId;
	sendRequest(reqUrl,{onComplete:function(responseText){
		
	}});
}
function tryDelete(){
	var reqUrl = "<%=pageBean.getHandlerURL()%>&actionType=existSecurityRelation&menuId="+$('#FUNC_ID').val();
	sendRequest(reqUrl,{onComplete:function(responseText){
		if ("Y"==responseText){
			writeErrorMsg("存在安全权限设置,请先删除权限设置！");
		}else{
			doDelete();
		}
	}});	
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
    <h3 class="portletTitle">&nbsp;&nbsp;系统功能树</h3>        
	<div id="treeArea" style="overflow:auto; width:230px;background-color:#F9F9F9;padding-top:5px;padding-left:5px;">
	<%=pageBean.getStringValue("menuTreeSyntax")%></div>
    <b class="b9"></b>
    </div>
	</td>
	<td width="85%" valign="top">
<div class="photobg1" id="tabHeader">
<div class="newarticle1" onclick="focusTab('base')">基本信息</div>
<div class="newarticle1" onclick="showSecurityConfig();focusTab('security')">安全管理</div>
<%
if ("funcnode".equals(pageBean.selectedValue("FUNC_TYPE"))){
%>
<div class="newarticle1" onclick="showHandlerList();focusTab('handler')">控制器列表</div>
<%}%>
</div>
<div class="photobox newarticlebox" id="Layer0" style="height:427px;">
	<div style="padding:0 5px 5px 5px;margin-top: 2px">
	    <div id="__ToolBar__" style="margin-top: 2px">
   		<table border="0" cellpadding="0" cellspacing="1">
	    <tr>
		<td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doSave()"<%}%> class="bartdx" hotKey="E" align="center"><input id="saveImgBtn" value="&nbsp;" title="保存" type="button" class="saveImgBtn" style="margin-right:0px;" />保存</td>
	    <td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="tryDelete()"<%}%> class="bartdx" hotKey="D" align="center"><input id="delImgBtn" value="&nbsp;" title="删除" type="button" class="delImgBtn" style="margin-right:0px;" />删除</td>
		<td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doCopyCurrent()"<%}%> class="bartdx" align="center"><input id="copyImgBtn" value="&nbsp;" title="复制" type="button" class="copyImgBtn" style="margin-right:0px;" />复制</td>    
	    <td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="showParentSelectBox()"<%}%> class="bartdx" align="center"><input id="moveImgBtn" value="&nbsp;" title="迁移" type="button" class="moveImgBtn" style="margin-right:0px;" />迁移</td>
	    <td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doMoveUp()"<%}%> class="bartdx" align="center"><input id="upImgBtn" value="&nbsp;" title="上移" type="button" class="upImgBtn" style="margin-right:0px;" />上移</td>
	    <td <%if(!pageBean.getBoolValue("isRootNode")){%> onmouseover="onMover(this);" onmouseout="onMout(this);" onclick="doMoveDown()"<%}%> class="bartdx" align="center"><input id="downImgBtn" value="&nbsp;" title="下移" type="button" class="downImgBtn" style="margin-right:0px;" />下移</td>
	    </tr></table></div>    
	    <table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>名称</th>
	<td><input id="FUNC_NAME" label="名称" name="FUNC_NAME" type="text" value="<%=pageBean.inputValue("FUNC_NAME")%>" size="32" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>类型</th>
	<td><%=pageBean.selectedText("FUNC_TYPE")%></td>
</tr>
<%if ("funcnode".equals(pageBean.selectedValue("FUNC_TYPE"))){%>
<tr>
	<th width="100" nowrap>控制器</th>
	<td><%=pageBean.inputValue("MAIN_HANDLER")%></td>
</tr>
<%}%>
<tr>
	<th width="100" nowrap>状态</th>
	<td>&nbsp;<%=pageBean.selectRadio("FUNC_STATE")%>
</td>
</tr>
<tr>
	<th width="100" nowrap>描述</th>
	<td><textarea id="FUNC_DESC" label="描述" name="FUNC_DESC" cols="40" rows="2" class="textarea"><%=pageBean.inputValue("FUNC_DESC")%></textarea>
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
	<th width="100" nowrap>名称</th>
	<td><input id="CHILD_FUNC_NAME" label="名称" name="CHILD_FUNC_NAME" type="text" value="<%=pageBean.inputValue("CHILD_FUNC_NAME")%>" size="32" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>类型</th>
	<td><select id="CHILD_FUNC_TYPE" onchange="showValidTr(this.value)" label="类型" name="CHILD_FUNC_TYPE" class="select" style="width:248px;"><%=pageBean.selectValue("CHILD_FUNC_TYPE")%></select>
</td>
</tr>
<tr id="MAIN_HANDLER_TR">
	<th width="100" nowrap>控制器</th>
	<td><input id="CHILD_MAIN_HANDLER" label="控制器" name="CHILD_MAIN_HANDLER" type="text" value="<%=pageBean.inputValue("CHILD_MAIN_HANDLER")%>" size="32" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>状态</th>
	<td>&nbsp;<%=pageBean.selectRadio("CHILD_FUNC_STATE")%>
</td>
</tr>
<tr>
	<th width="100" nowrap>描述</th>
	<td><textarea id="CHILD_FUNC_DESC" label="描述" name="CHILD_FUNC_DESC" cols="40" rows="2" class="textarea"><%=pageBean.inputValue("CHILD_FUNC_DESC")%></textarea>
</td>
</tr>
	    </table>
	</div>   
</div>
<div class="photobox newarticlebox" id="Layer1" style="height:427px;display:none;overflow:hidden;">
<iframe id="SecurityFrame" src="" width="100%" height="430" frameborder="0" scrolling="no"></iframe>
</div>
<div class="photobox newarticlebox" id="Layer2" style="height:427px;display:none;overflow:hidden;">
<iframe id="HandlerFrame" src="" width="100%" height="430" frameborder="0" scrolling="no"></iframe>
</div>        
    </td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value=""/>
<input type="hidden" id="FUNC_ID" name="FUNC_ID" value="<%=pageBean.inputValue("FUNC_ID")%>" />
<input type="hidden" id="FUNC_PID" name="FUNC_PID" value="<%=pageBean.inputValue("FUNC_PID")%>" />
<input type="hidden" id="FUNC_SORT" name="FUNC_SORT" value="<%=pageBean.inputValue("FUNC_SORT")%>" />
</form>
</body>
</html>
<script language="javascript">
var tab = new Tab('tab','tabHeader','Layer',0);
<%if(pageBean.getBoolValue("isRootNode")){%>
setImgDisabled('saveImgBtn',true);
setImgDisabled('delImgBtn',true);
setImgDisabled('copyImgBtn',true);
setImgDisabled('moveImgBtn',true);
setImgDisabled('upImgBtn',true);
setImgDisabled('downImgBtn',true);
<%}%>
$(function(){
	if ("base" == "<%=pageBean.getStringValue("currentTabId")%>"){
		tab.focus(0);
	}
	else if ("security" == "<%=pageBean.getStringValue("currentTabId")%>"){
		showSecurityConfig();
		tab.focus(1);
	}
	else if ("handler" == "<%=pageBean.getStringValue("currentTabId")%>"){
		<%if ("funcnode".equals(pageBean.selectedValue("FUNC_TYPE"))){%>
			showHandlerList();
			tab.focus(2);			
		<%}else{%>
			showSecurityConfig();	
			tab.focus(1);
		<%}%>		
	}	
	resetTabHeight(80);
	resetTreeHeight(80);
});
</script>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
<script language="javascript">
</script>
