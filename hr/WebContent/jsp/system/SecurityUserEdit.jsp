<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function saveRecord(){
	if (!validate()){
		return;
	}
	showSplash();
	postRequest('form1',{actionType:'checkUnique',onComplete:function(responseText){
		if (responseText == ''){
			postRequest('form1',{actionType:'save',onComplete:function(responseText){
				if (responseText == 'success'){
					parent.refreshContent();
					parent.PopupBox.closeCurrent();
				}
			}});		
		}else{
			hideSplash();
			writeErrorMsg(responseText);
		}
	}});	
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
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="saveRecord()"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="javascript:parent.PopupBox.closeCurrent();"><input value="&nbsp;" type="button" class="closeImgBtn" title="关闭" />关闭</td>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>编码</th>
	<td><input id="USER_CODE" label="编码" name="USER_CODE" type="text" value="<%=pageBean.inputValue("USER_CODE")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>名称</th>
	<td><input id="USER_NAME" label="名称" name="USER_NAME" type="text" value="<%=pageBean.inputValue("USER_NAME")%>" size="24" class="text" />
</td>
</tr>
<%
if(pageBean.isOnCreateMode()){
%>
<tr>
	<th width="100" nowrap>密码</th>
	<td><input id="USER_PWD" label="密码" name="USER_PWD" type="password" value="<%=pageBean.inputValue("USER_PWD")%>" size="24" class="text" /></td>
</tr>
<%}%>
<tr>
	<th width="100" nowrap>性别</th>
	<td><select id="USER_SEX" label="性别" name="USER_SEX" class="select"><%=pageBean.selectValue("USER_SEX")%></select>
</td>
</tr>
<tr>
	<th width="100" nowrap>状态</th>
	<td><select id="USER_STATE" label="状态" name="USER_STATE" class="select"><%=pageBean.selectValue("USER_STATE")%></select>
</td>
</tr>
<tr>
	<th width="100" nowrap>排序</th>
	<td><input id="USER_SORT" label="排序" name="USER_SORT" type="text" value="<%=pageBean.inputValue("USER_SORT")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>邮件</th>
	<td><input id="USER_MAIL" label="邮件" name="USER_MAIL" type="text" value="<%=pageBean.inputValue("USER_MAIL")%>" size="32" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>电话</th>
	<td><input id="USER_PHONE" label="电话" name="USER_PHONE" type="text" value="<%=pageBean.inputValue("USER_PHONE")%>" size="32" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>描述</th>
	<td><textarea id="USER_DESC" label="描述" name="USER_DESC" cols="40" rows="4" class="textarea"><%=pageBean.inputValue("USER_DESC")%></textarea>
</td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" name="GRP_ID" id="GRP_ID" value="<%=pageBean.inputValue("GRP_ID")%>"/>
<input type="hidden" id="USER_ID" name="USER_ID" value="<%=pageBean.inputValue4DetailOrUpdate("USER_ID","")%>" />
</form>
<script language="javascript">
requiredValidator.add("USER_CODE");
requiredValidator.add("USER_NAME");
requiredValidator.add("USER_PWD");
requiredValidator.add("USER_SEX");
requiredValidator.add("USER_STATE");
requiredValidator.add("USER_SORT");
intValidator.add("USER_SORT");
initDetailOpertionImage();
<%
if(pageBean.isOnCreateMode()){
%>
$("#USER_PWD").val('');
<%}%>
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
