<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>有效天数</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script type="text/javascript">
$(function(){
    numValidator.add("VALID_DAYS");
	requiredValidator.add("VALID_DAYS");
});

function saveValidDays(){
	postRequest('form1',{actionType:'save',onComplete:function(responseText){
		if (responseText == 'success'){
			window.parent.location.reload();
		}else{
			showMessage('保存出错啦！');
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
	<aeai:previlege code="save"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="saveValidDays()" ><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td></aeai:previlege>
	<aeai:previlege code="close"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="parent.PopupBox.closeCurrent()"><input value="&nbsp;" type="button" class="closeImgBtn" title="关闭" />关闭</td></aeai:previlege>
</tr>
</table>
</div>
<table cellpadding="0" cellspacing="0" class="detailTable">
  <tr>
    <th width="100" nowrap="nowrap">年</th>
    <td><input name="VALID_YEAR" type="text" class="text" id="VALID_YEAR" value="<%=pageBean.inputValue("VALID_YEAR")%>" size="24" readonly="readonly" label="年" /></td>
  </tr>
  <tr>
    <th width="100" nowrap="nowrap">月</th>
    <td><input name="VALID_MONTH" type="text" class="text" id="VALID_MONTH" value="<%=pageBean.inputValue("VALID_MONTH")%>" size="24" readonly="readonly" label="月" /></td>
  </tr>
  <tr>
    <th width="100" nowrap="nowrap">有效工作天数</th>
    <td><input id="VALID_DAYS" label="有效工作天数" name="VALID_DAYS" type="text" value="<%=pageBean.inputValue("VALID_DAYS")%>" size="24" class="text" /></td>
  </tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" name="year" id="year" value="<%=pageBean.inputValue("year")%>"/>
<input type="hidden" name="month" id="month" value="<%=pageBean.inputValue("month")%>"/>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
