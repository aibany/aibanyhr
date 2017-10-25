<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
var operaRequestBox;
function openContentRequestBox(operaType,title,handlerId,subPKField){
	if ('insert' != operaType && !isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	if (!operaRequestBox){
		operaRequestBox = new PopupBox('operaRequestBox',title,{size:'big',top:'2px'});
	}
	var columnIdValue = $("#curColumnId").val();
	if ('insert' == operaType){
		columnIdValue = $("#columnId").val();
	}
	var url = 'index?'+handlerId+'&GRP_ID='+columnIdValue+'&operaType='+operaType+'&'+subPKField+'='+$("#"+subPKField).val();
	operaRequestBox.sendRequest(url);
}

var resetPasswordBox;
function openResetPasswordBox(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	var title = '重置用户密码';
	if (!resetPasswordBox){
		resetPasswordBox = new PopupBox('resetPasswordBox',title,{size:'normal',top:'2px'});
	}
	var url = 'index?ResetPassword&USER_ID='+$("#USER_ID").val();
	resetPasswordBox.sendRequest(url);
}

function showFilterBox(){
	$('#filterBox').show();
	var clientWidth = $(document.body).width();
	var tuneLeft = (clientWidth - $("#filterBox").width())/2-2;	
	$("#filterBox").css('left',tuneLeft);	
}
function doRemoveContent(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	if (confirm('确认要移除该条记录吗？')){
		postRequest('form1',{actionType:'isLastRelation',onComplete:function(responseText){
			if (responseText == 'true'){
				if (confirm('该信息只有一条关联记录，确认要删除吗？')){
					doSubmit({actionType:'delete'});
				}
			}else{
				doSubmit({actionType:'removeContent'});
			}
		}});
	}
}

function isSelectedTree(){
	if (isValid($('#columnId').val())){
		return true;
	}else{
		return false;
	}
}
var operaTreeBox;
function openTreeRequestBox(operaType){
	var title = "树节点管理";
	var handlerId = "SecurityGroupEdit";
	
	if ('insert' != operaType && !isSelectedTree()){
		writeErrorMsg('请先选中一个树节点!');
		return;
	}
	if (!operaTreeBox){
		operaTreeBox = new PopupBox('operaTreeBox',title,{size:'normal',width:'500px',height:'360px',top:'2px'});
	}
	var url = '';
	if ('insert' == operaType){
		url = 'index?'+handlerId+'&operaType='+operaType+'&GRP_PID='+$("#columnId").val();
	}else{
		url = 'index?'+handlerId+'&operaType='+operaType+'&GRP_ID='+$("#columnId").val();
	}
	operaTreeBox.sendRequest(url);	
}
function refreshTree(){
	doQuery();
}
function refreshContent(curNodeId){
	if (curNodeId){
		$('#columnId').val(curNodeId);
	}
	doSubmit({actionType:'query'});
}
function deleteTreeNode(){
	if (!isSelectedTree()){
		writeErrorMsg('请先选中一个树节点!');
		return;
	}
	if (confirm('确认要删除该节点吗？')){
		postRequest('form1',{actionType:'deleteTreeNode',onComplete:function(responseText){
			if (responseText == 'success'){
				$('#columnId').val("");
				doQuery();
			}else if (responseText == 'hasChild'){
				writeErrorMsg('该节点还有子节点，不能删除！');
			}else if (responseText == 'hasContent'){
				writeErrorMsg('有信息关联该分组，不能删除！');
			}
		}});	
	}
}
var targetTreeBox;
function openTargetTreeBox(curAction){
	var columnIdValue = $("#columnId").val();
	if (!isSelectedTree()){
		writeErrorMsg('请先选中一个树节点!');
		return;
	}
	if (curAction == 'copyContent' || curAction == 'moveContent'){
		if (!isSelectedRow()){
			writeErrorMsg('请先选中一条记录!');
			return;
		}
		columnIdValue = $("#curColumnId").val()
	}	
	if (!targetTreeBox){
		targetTreeBox = new PopupBox('targetTreeBox','请选择目标分组',{size:'normal',width:'300px',top:'2px'});
	}
	var handlerId = "SecurityGroupPick";
	var url = 'index?'+handlerId+'&GRP_ID='+columnIdValue;
	targetTreeBox.sendRequest(url);
	$("#actionType").val(curAction);
}
function doChangeParent(){
	var curAction = $('#actionType').val();
	postRequest('form1',{actionType:curAction,onComplete:function(responseText){
		if (responseText == 'success'){
			if (curAction == 'moveTree'){
				refreshTree();			
			}else{
				refreshContent();		
			}
		}else {
			writeErrorMsg('迁移父节点出错啦！');
		}
	}});
}
function moveRequest(moveAction){
	postRequest('form1',{actionType:moveAction,onComplete:function(responseText){
		if (responseText == 'success'){
			refreshTree();
		}else if (responseText == 'isFirstNode'){
			writeErrorMsg('该节点是同级第一个节点，不能上移！');
		}else if (responseText == 'isLastNode'){
			writeErrorMsg('该节点是同级最后一个节点，不能下移！');
		}
	}});
}
function clearFilter(){
	$("#filterBox input[type!='button'],select").val('');
}
function changeTab(tabId){
	$('#_tabId_').val(tabId);
	refreshContent();
}
function saveTreeBaseRecord(){
	postRequest('form1',{actionType:'saveTreeBaseRecord',onComplete:function(responseText){
		if (responseText == 'success'){
			refreshTree();
		}else {
			writeErrorMsg('保存基本信息出错啦！');
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
    <h3 class="portletTitle">&nbsp;&nbsp;分组列表</h3>        
        <div id="treeArea" style="overflow:auto; height:420px;width:230px;background-color:#F9F9F9;padding-top:5px;padding-left:5px;">
    <%=pageBean.getStringValue("menuTreeSyntax")%></div>
    </div>
    <b class="b9"></b>
    </div>
    <input type="hidden" id="columnId" name="columnId" value="<%=pageBean.inputValue("columnId")%>" />
    <input type="hidden" id="targetParentId" name="targetParentId" value="" />     
    </td>
	<td width="85%" valign="top">
<div class="photobg1" id="tabHeader">
<div class="newarticle1" onclick="changeTab('_base_')">基本信息</div>
<div class="newarticle1" onclick="changeTab('SecurityUser')">用户信息</div>
</div>	
<div class="photobox newarticlebox" id="Layer<%=pageBean.inputValue("_tabIndex_")%>" style="height:427px;padding:1px;">
<%if ("_base_".equals(pageBean.inputValue("_tabId_"))){%>
<div id="__ToolBar__">
<table  id="_TreeToolBar_" border="0" cellpadding="0" cellspacing="1">
    <tr>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="openTreeRequestBox('insert')"><input value="&nbsp;" title="新增" type="button" class="newImgBtn" style="margin-right:0px;" />新增</td>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="saveTreeBaseRecord()"><input value="&nbsp;" title="保存" type="button" class="saveImgBtn" style="margin-right:0px;" />保存</td>
<%if (!pageBean.isTrue(pageBean.inputValue("isRootColumnId"))){%>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="deleteTreeNode()"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" style="margin-right:0px;" />删除</td>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="openTargetTreeBox('moveTree')"><input value="&nbsp;" title="迁移" type="button" class="moveImgBtn" style="margin-right:0px;" />迁移</td>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="moveRequest('moveUp')"><input value="&nbsp;" title="上移" type="button" class="upImgBtn" style="margin-right:0px;" />上移</td>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="moveRequest('moveDown')"><input value="&nbsp;" title="下移" type="button" class="downImgBtn" style="margin-right:0px;" />下移</td>   
<%}%>
    </tr>
    </table>
</div>
<div style="margin:auto 2px;">
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>编码</th>
	<td><input name="GRP_CODE" type="text" class="text" id="GRP_CODE" value="<%=pageBean.inputValue("GRP_CODE")%>" size="24" readonly="readonly" label="编码" /></td>
</tr>
<tr>
	<th width="100" nowrap>名称</th>
	<td><input id="GRP_NAME" label="名称" name="GRP_NAME" type="text" value="<%=pageBean.inputValue("GRP_NAME")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>状态</th>
	<td><select id="GRP_STATE" label="状态" name="GRP_STATE" class="select"><%=pageBean.selectValue("GRP_STATE")%></select>
</td>
</tr>
<tr>
	<th width="100" nowrap>描述</th>
	<td><textarea id="GRP_DESC" label="描述" name="GRP_DESC" cols="40" rows="4" class="textarea"><%=pageBean.inputValue("GRP_DESC")%></textarea>
</td>
</tr>
</table>
<input type="hidden" id="GRP_ID" name="GRP_ID" value="<%=pageBean.inputValue4DetailOrUpdate("GRP_ID","")%>" />
<input type="hidden" id="GRP_SORT" name="GRP_SORT" value="<%=pageBean.inputValue("GRP_SORT")%>" />
<input type="hidden" id="GRP_PID" name="GRP_PID" value="<%=pageBean.inputValue("GRP_PID")%>" />
<input type="hidden" id="_tabId_" name="_tabId_" value="<%=pageBean.inputValue("_tabId_")%>" />
</div>
<%}%>
<%if ("SecurityUser".equals(pageBean.inputValue("_tabId_"))){%>
<div id="__ToolBar__">
<span style="float:left">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="openContentRequestBox('insert','用户信息','SecurityUserEdit','USER_ID')"><input value="&nbsp;" title="新增" type="button" class="createImgBtn" style="margin-right:" />新增</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="E" align="center" onclick="openContentRequestBox('update','用户信息','SecurityUserEdit','USER_ID')"><input value="&nbsp;" title="编辑" type="button" class="editImgBtn" />编辑</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="C" align="center" onclick="openContentRequestBox('copy','用户信息','SecurityUserEdit','USER_ID')"><input value="&nbsp;" title="复制" type="button" class="copyImgBtn" />复制</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="V" align="center" onclick="openContentRequestBox('detail','用户信息','SecurityUserEdit','USER_ID')"><input value="&nbsp;" title="查看" type="button" class="detailImgBtn" />查看</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="W" align="center" onclick="openResetPasswordBox()"><input value="&nbsp;" title="重置密码" type="button" class="resetPasswordImgBtn" />重置</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="F" align="center" onclick="showFilterBox()"><input value="&nbsp;" title="过滤" type="button" class="filterImgBtn" />过滤</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="doDelete($('#'+rsIdTagId).val());"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" />删除</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="R" align="center" onclick="doRemoveContent()"><input value="&nbsp;" title="删除" type="button" class="removeImgBtn" />移除</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="Z" align="center" onclick="openTargetTreeBox('copyContent')"><input value="&nbsp;" title="分发" type="button" class="addImgBtn" />分发</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="M" align="center" onclick="openTargetTreeBox('moveContent')"><input value="&nbsp;" title="迁移" type="button" class="moveImgBtn" />迁移</td>
</tr>
</table>
</span>
<span style="float:right;height:28px;line-height:28px;"><input style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;" name="showChildNodeRecords" type="checkbox" id="showChildNodeRecords" onclick="doQuery()" value="Y" <%=pageBean.checked(pageBean.inputValue("showChildNodeRecords"))%> />&nbsp;显示子节点记录&nbsp;</span>
</div>
<div style="padding:0 2px;">
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="用户信息.csv"
retrieveRowsCallback="process" xlsFileName="用户信息.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="337px" 
>
<ec:row styleClass="odd" ondblclick="clearSelection();openContentRequestBox('detail','用户信息','SecurityUserEdit','USER_ID')" oncontextmenu="selectRow(this,{USER_ID:'${row.USER_ID}',curColumnId:'${row.GRP_ID}'});refreshConextmenu()" onclick="selectRow(this,{USER_ID:'${row.USER_ID}',curColumnId:'${row.GRP_ID}'})">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="100" property="USER_CODE" title="编码"   />
	<ec:column width="100" property="USER_NAME" title="名称"   />
	<ec:column width="100" property="USER_SEX" title="性别"   mappingItem="USER_SEX"/>
	<ec:column width="100" property="USER_STATE" title="状态"   mappingItem="USER_STATE"/>
	<ec:column width="100" property="GRP_NAME" title="所属群组"   />
</ec:row>
</ec:table>
<div id="filterBox" class="sharp color2" style="position:absolute;top:30px;display:none; z-index:10; width:480px;">
<b class="b9"></b>
<div class="content">
<h3>&nbsp;&nbsp;条件过滤框</h3>
<table class="detailTable" cellpadding="0" cellspacing="0" style="width:99%;margin:1px;">
<tr>
	<th width="100" nowrap>用户编码</th>
	<td><input id="userCode" label="用户编码" name="userCode" type="text" value="<%=pageBean.inputValue("userCode")%>" size="10" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>用户名称</th>
	<td><input id="userName" label="用户名称" name="userName" type="text" value="<%=pageBean.inputValue("userName")%>" size="10" class="text" />
</td>
</tr>
</table>
<div style="width:100%;text-align:center;">
<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
&nbsp;&nbsp;
<input type="button" name="button" id="button" value="清空" class="formbutton" onclick="clearFilter()" />
&nbsp;&nbsp;<input type="button" name="button" id="button" value="关闭" class="formbutton" onclick="javascript:$('#filterBox').hide();" /></div>
</div>
<b class="b9"></b>
</div>
<input type="hidden" id="_tabId_" name="_tabId_" value="<%=pageBean.inputValue("_tabId_")%>" />
<input type="hidden" name="USER_ID" id="USER_ID" value="" />
<input type="hidden" name="curColumnId" id="curColumnId" value="" />
<script language="JavaScript">
setRsIdTag('USER_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
</script>
</div>
<%}%>
</div>
</td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" />
<script language="javascript">
var tab = new Tab('tab','tabHeader','Layer',0);
tab.focus(<%=pageBean.inputValue("_tabIndex_")%>);
$(function(){
	resetTabHeight(80);
	resetTreeHeight(80);
});
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
