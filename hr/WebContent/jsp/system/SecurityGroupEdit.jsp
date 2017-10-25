<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function saveTreeRecord(){
	if (!validate()){
		return;
	}
	showSplash();
	postRequest('form1',{actionType:'checkUnique',onComplete:function(responseText){
		if (responseText == ''){
			postRequest('form1',{actionType:'save',onComplete:function(responseText){
				if ('success' == responseText){
					parent.refreshTree();
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
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="saveTreeRecord()"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="javascript:parent.PopupBox.closeCurrent();"><input value="&nbsp;" type="button" class="closeImgBtn" title="关闭" />关闭</td>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>编码</th>
	<td><input id="GRP_CODE" label="编码" name="GRP_CODE" type="text" value="<%=pageBean.inputValue("GRP_CODE")%>" size="24" class="text" />
</td>
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
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" id="GRP_ID" name="GRP_ID" value="<%=pageBean.inputValue4DetailOrUpdate("GRP_ID","")%>" />
<input type="hidden" id="GRP_SORT" name="GRP_SORT" value="<%=pageBean.inputValue("GRP_SORT")%>" />
<input type="hidden" id="GRP_PID" name="GRP_PID" value="<%=pageBean.inputValue("GRP_PID")%>" />
</form>
<script language="javascript">
requiredValidator.add("GRP_CODE");
requiredValidator.add("GRP_NAME");
requiredValidator.add("GRP_STATE");
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
