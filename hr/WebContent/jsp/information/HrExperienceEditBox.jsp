<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
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
			hideSplash();
			parent.PopupBox.closeCurrent();
			parent.refreshPage();
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
<%if(pageBean.getBoolValue("doDetail")){ %>
   <aeai:previlege code="edit"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="enableSave()" ><input value="&nbsp;" type="button" class="editImgBtn" id="modifyImgBtn" title="编辑" />编辑</td></aeai:previlege>
   <aeai:previlege code="save"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="saveRecord()"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td></aeai:previlege>
   <aeai:previlege code="close"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="parent.PopupBox.closeCurrent();"><input value="&nbsp;" type="button" class="closeImgBtn" title="关闭" />关闭</td></aeai:previlege>
<%} %>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>入职日期</th>
	<td><input id="EXP_IN_TIME" label="入职日期" name="EXP_IN_TIME" type="text" value="<%=pageBean.inputDate("EXP_IN_TIME")%>" size="24" class="text" /><img id="EXP_IN_TIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
</tr>
<tr>
	<th width="100" nowrap>离职日期</th>
	<td><input id="EXP_OUT_TIME" label="离职日期" name="EXP_OUT_TIME" type="text" value="<%=pageBean.inputDate("EXP_OUT_TIME")%>" size="24" class="text" /><img id="EXP_OUT_TIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
</tr>
<tr>
	<th width="100" nowrap>工作经历</th>
	<td><textarea id="EXP_EXPERIENCE" label="工作经历" name="EXP_EXPERIENCE" cols="40" rows="3" class="textarea"><%=pageBean.inputValue("EXP_EXPERIENCE")%></textarea>
</td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" id="EXP_ID" name="EXP_ID" value="<%=pageBean.inputValue4DetailOrUpdate("EXP_ID","")%>" />
<input type="hidden" id="EMP_ID" name="EMP_ID" value="<%=pageBean.inputValue("EMP_ID")%>" />
</form>
<script language="javascript">
initCalendar('EXP_IN_TIME','%Y-%m-%d','EXP_IN_TIMEPicker');
initCalendar('EXP_OUT_TIME','%Y-%m-%d','EXP_OUT_TIMEPicker');
datetimeValidators[0].set("yyyy-MM-dd").add("EXP_IN_TIME");
datetimeValidators[1].set("yyyy-MM-dd").add("EXP_OUT_TIME");
initDetailOpertionImage();
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
