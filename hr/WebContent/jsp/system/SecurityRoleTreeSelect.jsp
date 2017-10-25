<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function selectRequest(idValue){
	parent.addRoleTreeRelation(idValue);
	parent.PopupBox.closeCurrent();
}
function setSelectTempValue(idValue,nameValue){
	ele('targetIdValue').value = idValue;
}
function doSelectRequest(){
	fillIds();
	if (!isValid(ele('targetIdValue').value)){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	selectRequest(ele('targetIdValue').value);
}

var tree;
function fillIds()
{
	var chk = tree.Nodes;
	var tempId = '';
	for(var o in chk) {
		if(chk[o].checked){
			tempId=tempId+','+chk[o].ID;
		}
	}
	if(tempId.length > 0)
	{
		$('#targetIdValue').val(tempId.substring(1,tempId.length));
	}
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<table class="detailTable" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top" nowrap>
<div id="selectTreeContainer" style="height:312px;overflow-y:auto" ondblclick="doSelectRequest()">
<script language="javascript">
<%=pageBean.inputValue("pickTreeSyntax")%>
</script>
</div>	
	</td>
  </tr>
  <tr>
    <td align="center">
    <input class="formbutton" type="button" name="Button23" value="确定" onclick="doSelectRequest()"/>&nbsp; &nbsp;
    <input class="formbutton" type="button" name="Button22" value="关闭" onclick="javascript:parent.PopupBox.closeCurrent();"/></td>
  </tr>
</table>
<input type="hidden" name="actionType" id="actionType" />
<input type="hidden" name="targetIdValue" id="targetIdValue" value="" />
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
