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
		operaRequestBox = new PopupBox('operaRequestBox',title,{size:'big',top:'3px'});
	}
	columnIdValue = $("#columnId").val();	
	var url = 'index?'+handlerId+'&GRP_ID='+columnIdValue+'&operaType='+operaType+'&'+subPKField+'='+$("#"+subPKField).val();
	operaRequestBox.sendRequest(url);
}
var uploadResourceRequestBox;
function openUploadResourceRequestBox(){
	var title = "上载资源";
	if (!uploadResourceRequestBox){
		uploadResourceRequestBox = new PopupBox('uploadResourceRequestBox',title,{size:'big',width:'260px',height:'400px',top:'3px'});
	}
	var columnIdValue = $("#columnId").val();
	var url = 'index?ResourseUploader&GRP_ID='+columnIdValue;
	uploadResourceRequestBox.sendRequest(url);	
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
	var handlerId = "WcmGeneralGroupEdit";
	
	if ('insert' != operaType && !isSelectedTree()){
		writeErrorMsg('请先选中一个树节点!');
		return;
	}
	if (!operaTreeBox){
		operaTreeBox = new PopupBox('operaTreeBox',title,{size:'normal',width:'500px',height:'360px',top:'3px'});
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
				
			}else if (responseText == 'isSystem'){
				writeErrorMsg('该节点是内置节点不能删除！');
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
		targetTreeBox = new PopupBox('targetTreeBox','请选择目标分组',{size:'normal',width:'300px',top:'3px'});
	}
	var handlerId = "WcmGeneralGroupPick";
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
				refreshContent($("#targetParentId").val());		
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

</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<table width="100%" style="margin:0px;padding:0px;" cellpadding="1" cellspacing="0">
<tr>
	<td valign="top">
    <div id="leftTree" class="sharp color2" style="margin-top:0px;">
	<b class="b1"></b><b class="b2"></b><b class="b3"></b><b class="b4"></b>
    <div class="content">
    <h3 class="portletTitle">&nbsp;&nbsp;分组列表 </h3>
<% if (!pageBean.isValid(pageBean.inputValue("rootColumnId"))){%>
	<table id="_TreeToolBar_" border="0" cellpadding="0" cellspacing="0" >
    <tr>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="openTreeRequestBox('insert')"><input value="&nbsp;" title="新增" type="button" class="newImgBtn" style="margin-right:0px;" />增</td>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="E" align="center" onclick="openTreeRequestBox('update')"><input value="&nbsp;" title="编辑" type="button" class="modifyImgBtn" style="margin-right:0px;" />编</td>
<%if (!pageBean.isTrue(pageBean.inputValue("isRootColumnId"))){%>    
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="deleteTreeNode()"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" style="margin-right:0px;" />删</td>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="M" align="center" onclick="openTargetTreeBox('moveTree')"><input value="&nbsp;" title="迁移" type="button" class="moveImgBtn" style="margin-right:0px;" />移</td>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="moveRequest('moveUp')"><input value="&nbsp;" title="上移" type="button" class="upImgBtn" style="margin-right:0px;" />上</td>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="moveRequest('moveDown')"><input value="&nbsp;" title="下移" type="button" class="downImgBtn" style="margin-right:0px;" />下</td>
<%}%>       
    </tr>
    </table>
<%}%>    
    <div id="treeArea" style="overflow:auto; height:300px;width:230px;background-color:#F9F9F9;padding-top:5px;padding-left:5px;">
    <%=pageBean.getStringValue("menuTreeSyntax")%></div>
    </div>
    <b class="b9"></b>
    </div>
    <input type="hidden" id="columnId" name="columnId" value="<%=pageBean.inputValue("columnId")%>" />
    <input type="hidden" id="targetParentId" name="targetParentId" value="" />
    </td>
	<td width="85%" valign="top">
<div id="__ToolBar__">
<table class="toolTable" border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="openUploadResourceRequestBox()"><input value="&nbsp;" title="上载" type="button" class="createImgBtn" style="margin-right:" />上载</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="V" align="center" onclick="openContentRequestBox('detail','资源列表','WcmGeneralResourceEdit','RES_ID')"><input value="&nbsp;" title="查看" type="button" class="detailImgBtn" />查看</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="F" align="center" onclick="showFilterBox()"><input value="&nbsp;" title="过滤" type="button" class="filterImgBtn" />过滤</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="doDelete($('#'+rsIdTagId).val());"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" />删除</td>
</tr>
</table>
</div> 
<div id="rightArea">
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="资源列表.csv"
retrieveRowsCallback="process" xlsFileName="资源列表.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="373px"
>
<ec:row styleClass="odd" ondblclick="clearSelection();openContentRequestBox('detail','资源列表','WcmGeneralResourceEdit','RES_ID')" oncontextmenu="selectRow(this,{RES_ID:'${row.RES_ID}',curColumnId:'${row.RES_ID}'});refreshConextmenu()" onclick="selectRow(this,{RES_ID:'${row.RES_ID}',curColumnId:'${row.RES_ID}'})">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="200" property="RES_NAME" title="名称"   />
	<ec:column width="80" property="RES_SIZE" title="大小"   />
	<ec:column width="80" property="RES_SUFFIX" title="后缀"   />
	<ec:column width="80" property="RES_SHAREABLE" title="共享"  mappingItem="RES_SHAREABLE" />
</ec:row>
</ec:table>

<div id="filterBox" class="sharp color2" style="position:absolute;top:30px;display:none; z-index:10; width:480px;">
<b class="b9"></b>
<div class="content">
<h3>&nbsp;&nbsp;条件过滤框</h3>
<table class="detailTable" cellpadding="0" cellspacing="0" style="width:99%;margin:1px;">
<tr>
	<th width="100" nowrap>名称</th>
	<td><input id="resName" label="名称" name="resName" type="text" value="<%=pageBean.inputValue("resName")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>后缀</th>
	<td><input id="suffix" label="后缀" name="suffix" type="text" value="<%=pageBean.inputValue("suffix")%>" size="24" class="text" />
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
<input type="hidden" name="RES_ID" id="RES_ID" value="" />
<input type="hidden" name="curColumnId" id="curColumnId" value="" />
<input type="hidden" name="rootColumnId" id="rootColumnId" value="<%=pageBean.inputValue("rootColumnId")%>" />
<script language="JavaScript">
setRsIdTag('RES_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
$(window).load(function() {
	setTimeout(function(){
		<% if (!pageBean.isValid(pageBean.inputValue("rootColumnId"))){%>		
		resetTreeHeight(100);
		resetRightAreaHeight(80);
		<%}else{%> 
		resetTreeHeight(70);
		resetRightAreaHeight(50);
		<%}%> 
	},1);	
});
</script>
</div>
</td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" />
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
