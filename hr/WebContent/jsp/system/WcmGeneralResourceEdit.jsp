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
	postRequest('form1',{actionType:'save',onComplete:function(responseText){
		if (responseText == 'success'){
			parent.refreshContent();
			parent.PopupBox.closeCurrent();
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
	<th width="100" nowrap>名称</th>
	<td><input name="RES_NAME" type="text" class="text" id="RES_NAME" value="<%=pageBean.inputValue("RES_NAME")%>" size="24" readonly="readonly" label="名称" /></td>
</tr>
<tr>
	<th width="100" nowrap>共享</th>
	<td><%=pageBean.selectRadio("RES_SHAREABLE")%></td>
</tr>
<tr>
	<th width="100" nowrap>大小</th>
	<td><input name="RES_SIZE" type="text" class="text" id="RES_SIZE" value="<%=pageBean.inputValue("RES_SIZE")%>" size="24" readonly="readonly" label="大小" />
	byte</td>
</tr>
<tr>
	<th width="100" nowrap>后缀</th>
	<td><input name="RES_SUFFIX" type="text" class="text" id="RES_SUFFIX" value="<%=pageBean.inputValue("RES_SUFFIX")%>" size="24" readonly="readonly" label="后缀" /></td>
</tr>
<tr>
	<th width="100" nowrap>位置</th>
	<td><input name="RES_LOCATION" type="text" class="text" id="RES_LOCATION" value="<%=pageBean.inputValue("RES_LOCATION")%>" size="65" readonly="readonly" label="位置" /></td>
</tr>
<tr>
	<th width="100" nowrap>描述</th>
	<td><textarea id="RES_DESCRIPTION" label="描述" name="RES_DESCRIPTION" cols="65" rows="3" class="textarea"><%=pageBean.inputValue("RES_DESCRIPTION")%></textarea>
</td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" name="GRP_ID" id="GRP_ID" value="<%=pageBean.inputValue("GRP_ID")%>"/>
<input type="hidden" id="RES_ID" name="RES_ID" value="<%=pageBean.inputValue4DetailOrUpdate("RES_ID","")%>" />
</form>
<script language="javascript">
requiredValidator.add("RES_NAME");
initDetailOpertionImage();
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
