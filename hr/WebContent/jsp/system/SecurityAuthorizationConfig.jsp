<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>安全管理</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<%
String height = pageBean.getStringValue("height", "350");
%>
<script language="javascript">
var addUserTreeBox;
function showAddUserTreeBoxBox(){
	if (!addUserTreeBox){
		addUserTreeBox = new PopupBox('addUserTreeBox','请选择用户',{size:'normal',height:'410px',width:'310px',top:'3px'});
	}
	var handlerId = "SecurityUserTreeSelect";
	var url = 'index?'+handlerId+'&actionType=addUserRequest&resourceType='+$("#resourceType").val()+'&resourceId='+$("#resourceId").val();
	addUserTreeBox.sendRequest(url);
}
function addUserTreeRelation(userIds){
	if (userIds != ""){
		$('#userIds').val(userIds);
		postRequest('form1',{actionType:'addUserAuthRelation',dataType:'script',onComplete:function(responseText){
			hideSplash();
		}});		
	}
}


var addGroupTreeBox;
function showAddGroupTreeBoxBox(){
	if (!addGroupTreeBox){
		addGroupTreeBox = new PopupBox('addGroupTreeBox','请选择群组',{size:'normal',width:'300px',top:'3px'});
	}
	var handlerId = "SecurityGroupTreeSelect";
	var url = 'index?'+handlerId+'&actionType=addGroupRequest&resourceType='+$("#resourceType").val()+'&resourceId='+$("#resourceId").val();
	addGroupTreeBox.sendRequest(url);
}
function addGroupTreeRelation(groupIds){
	if (groupIds != ""){
		$('#groupIds').val(groupIds);
		showSplash();
		postRequest('form1',{actionType:'addGroupAuthRelation',dataType:'script',onComplete:function(responseText){
			hideSplash();
		}});		
	}
}

var addRoleTreeBox;
function showAddRoleTreeBoxBox(){
	if (!addRoleTreeBox){
		addRoleTreeBox = new PopupBox('addRoleTreeBox','请选择角色',{size:'normal',width:'300px',top:'3px'});
	}
	var handlerId = "SecurityRoleTreeSelect";
	var url = 'index?'+handlerId+'&actionType=addRoleRequest&resourceType='+$("#resourceType").val()+'&resourceId='+$("#resourceId").val();
	addRoleTreeBox.sendRequest(url);
}
function addRoleTreeRelation(roleIds){
	if (roleIds != ""){
		$('#roleIds').val(roleIds);
		showSplash();
		postRequest('form1',{actionType:'addRoleAuthRelation',dataType:'script',onComplete:function(responseText){
			hideSplash();
		}});
	}
}
function deleteRelation(selectId,param){
	var obj = ele(selectId);
	var selectedIndex = obj.selectedIndex;
	if (selectedIndex == -1){
		writeErrorMsg("请先选中一条记录！");
		return;
	}	
	if (confirm('确认要进行删除操作吗？')){
		doSubmit(param);
	}
}
function emptyRelation(param){
	if (confirm('确认要进行清空操作吗？')){
		doSubmit(param);
	}	
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<table width="100%" border="0">
  <tr>
    <td width="33%" valign="top">
	<fieldset style="padding:0 5px 5px 5px; margin-top:9px;">
	  <legend style="font-weight: bolder;">角色列表</legend>
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="showAddRoleTreeBoxBox();"><input value="&nbsp;" type="button" class="addImgBtn" id="addImgBtn" title="添加" />添加</td>
<td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="deleteRelation('roleList',{actionType:'delRoleAuthRelation'});"><input value="&nbsp;" type="button" class="delImgBtn" id="delImgBtn" title="删除" />删除</td>   
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="emptyRelation({actionType:'delRoleAuthRelations'});"><input value="&nbsp;" type="button" class="cancelImgBtn" id="cancelImgBtn" title="刷新" />清空</td>   
</tr>
</table>
<select name="roleList" size="20" id="roleList" style="width:100%;height:<%=height%>px">
<%=pageBean.selectValue("roleList") %>
</select>
	</fieldset>          
    </td>
    <td width="33%" valign="top">
	<fieldset style="padding:0 5px 5px 5px; margin-top:9px;">
	  <legend style="font-weight: bolder;">用户列表</legend>
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="showAddUserTreeBoxBox();"><input value="&nbsp;" type="button" class="addImgBtn" id="addImgBtn" title="添加" />添加</td>
<td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="deleteRelation('userList',{actionType:'delUserAuthRelation'});"><input value="&nbsp;" type="button" class="delImgBtn" id="delImgBtn" title="删除" />删除</td>   
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="emptyRelation({actionType:'delUserAuthRelations'});"><input value="&nbsp;" type="button" class="cancelImgBtn" id="cancelImgBtn" title="刷新" />清空</td>   
</tr>
</table>
<select name="userList" size="20" id="userList" style="width:100%;height:<%=height%>px">
<%=pageBean.selectValue("userList") %>
</select>      
	</fieldset>      
    </td>
    <td width="33%" valign="top">
	<fieldset style="padding:0 5px 5px 5px; margin-top:9px;">
	  <legend style="font-weight: bolder;">群组列表</legend>
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="showAddGroupTreeBoxBox();"><input value="&nbsp;" type="button" class="addImgBtn" id="addImgBtn" title="添加" />添加</td>
<td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="deleteRelation('groupList',{actionType:'delGroupAuthRelation'});"><input value="&nbsp;" type="button" class="delImgBtn" id="delImgBtn" title="删除" />删除</td>   
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="emptyRelation({actionType:'delGroupAuthRelations'});"><input value="&nbsp;" type="button" class="cancelImgBtn" id="cancelImgBtn" title="刷新" />清空</td>   
</tr>
</table>
<select name="groupList" size="20" id="groupList" style="width:100%;height:<%=height%>px">
<%=pageBean.selectValue("groupList") %>
</select>      
	</fieldset>      
    </td>
  </tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="resourceType" id="resourceType" value="<%=pageBean.inputValue("resourceType")%>"/>
<input type="hidden" name="resourceId" id="resourceId" value="<%=pageBean.inputValue("resourceId")%>"/>

<input type="hidden" name="userIds" id="userIds" value=""/>
<input type="hidden" name="groupIds" id="groupIds" value=""/>
<input type="hidden" name="roleIds" id="roleIds" value=""/>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
