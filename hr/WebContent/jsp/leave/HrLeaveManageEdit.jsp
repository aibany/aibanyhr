<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>请假申请</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function stateSubmit(){
	doSubmit({actionType:'submit'});
}
function stateApprove(){
	doSubmit({actionType:'approve'});
}
function stateDrafe(){
	doSubmit({actionType:'drafe'});
}
function save(){
	postRequest('form1',{actionType:'save',onComplete:function(responseText){
		if ("fail" != responseText){
			$('#operaType').val('update');
			$('#LEA_ID').val(responseText);
			doSubmit({actionType:'prepareDisplay'});
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
 <%if(pageBean.getBoolValue("doInsertEdit")){%>
  <aeai:previlege code="save"><td  align="center"class="bartdx" onclick="save()" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="saveImgBtn" id="savedrafeImgBtn" title="保存" />保存</td></aeai:previlege>
  <%}%>
 <%if(pageBean.getBoolValue("doSubmit")){%> 
  <aeai:previlege code="submit"><td  align="center"class="bartdx"onclick="stateSubmit();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="submitImgBtn" id="submittedImgBtn" title="提交" />提交</td></aeai:previlege>
  <%}%>
  <%if(pageBean.getBoolValue("doApprove")){ %>
  <aeai:previlege code="resubmit"><td  align="center"class="bartdx"onclick="stateDrafe();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="reSubmittedImgBtn" id="drafeImgBtn" title="反提交" />反提交</td></aeai:previlege>
  <aeai:previlege code="approve"><td  align="center"class="bartdx"onclick="stateApprove();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="approveImgBtn" id="approveImgBtn" title="核准" />核准</td></aeai:previlege>
  <%} %>
  <aeai:previlege code="back"><td  align="center"class="bartdx"onclick="goToBack();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="backImgBtn" title="返回" />返回</td></aeai:previlege>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>请假人</th>
<td><input name="USER_ID_NAME" type="text" class="text"id="USER_ID_NAME"value="<%=pageBean.inputValue("USER_ID_NAME")%>" size="24"readonly="readonly" label="请假申请人" />
    <input id="USER_ID" label="请假人"name="USER_ID" type="hidden"value="<%=pageBean.inputValue("USER_ID")%>" size="24" class="text" />
</td>

</tr>
<tr>
	<th width="100" nowrap>申请时间</th>
	<td><input id="LEA_DATE" label="申请时间" name="LEA_DATE" type="text" value="<%=pageBean.inputDate("LEA_DATE")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>请假类型</th>
	<td><select id="LEA_TYPE" label="请假类型" name="LEA_TYPE" class="select"><%=pageBean.selectValue("LEA_TYPE")%></select>
	<span style="margin-left: 20px;margin-top: 5px;color: green;font-size: 15dip;">剩余年假天数:<%=pageBean.getAttribute("SAL_ALLOWANCE_YEAR")%></span>
</td>
</tr>
<tr>
	<th width="100" nowrap>请假日期</th>
	<td><input id="LEA_SDATE" label="请假日期" name="LEA_SDATE" type="text" value="<%=pageBean.inputDate("LEA_SDATE")%>" size="24" class="text" /><img id="LEA_SDATEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
</tr>
<tr>
	<th width="100" nowrap>结束日期</th>
	<td><input id="LEA_EDATE" label="结束日期" name="LEA_EDATE" type="text" value="<%=pageBean.inputDate("LEA_EDATE")%>" size="24" class="text" /><img id="LEA_EDATEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
</tr>
<tr>
	<th width="100" nowrap>请假天数</th>
<td><input name="LEA_DAYS" type="text" class="text" id="LEA_DAYS"value="<%=pageBean.inputValue("LEA_DAYS")%>" size="24" label="请假天数" /></td>
</tr>

<tr>
	<th width="100" nowrap>状态</th>
<td><input id="STATE_TEXT" label="状态" name="STATE_TEXT" type="text" value="<%=pageBean.selectedText("STATE")%>" size="24"  class="text" readonly="readonly"/>
	<input id="STATE" label="状态" name="STATE" type="hidden" value="<%=pageBean.selectedValue("STATE")%>" />
</td>
</tr>
<tr>
	<th width="100" nowrap>请假原因</th>
	<td><textarea id="LEA_CAUSE" label="请假原因" name="LEA_CAUSE" cols="60" rows="5" class="textarea"><%=pageBean.inputValue("LEA_CAUSE")%></textarea>
</td>
</tr>
<%if(pageBean.getBoolValue("doSignIn")){
	%>
<tr>
	<th width="100" nowrap>核准人</th>
<td><input name="LEA_APPOVER_NAME" type="text" class="text"
					id="LEA_APPOVER_NAME"
					value="<%=pageBean.inputValue("LEA_APPOVER_NAME")%>" size="24"
					readonly="readonly" label="核准人" /> <input id="LEA_APPROVER" label="姓名"
					name="LEA_APPOVER" type="hidden"
					value="<%=pageBean.inputValue("LEA_APPOVER")%>" size="24" class="text" />
				</td>


</tr>
<tr>
	<th width="100" nowrap>核准时间</th>
	<td><input id="LEA_APP_TIME" label="核准时间" name="LEA_APP_TIME" type="text" value="<%=pageBean.inputTime("LEA_APP_TIME")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>核准结果</th>
	<td><select id="APP_RESULT" label="核准结果" name="APP_RESULT" class="select"><%=pageBean.selectValue("APP_RESULT")%></select>
</td>
</tr>
<tr>
	<th width="100" nowrap>核准意见</th>
	<td><textarea id="LEA_APP_OPINION" label="核准意见" name="LEA_APP_OPINION" cols="60" rows="5" class="textarea"><%=pageBean.inputValue("LEA_APP_OPINION")%></textarea>
</td>
</tr>
<%
}%>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" id="LEA_ID" name="LEA_ID" value="<%=pageBean.inputValue("LEA_ID")%>" />
</form>
<script language="javascript">
$('#LEA_CAUSE').inputlimiter({
	limit: 100,
	remText: '还可以输入  %n 字 /',
	limitText: '%n 字',
	zeroPlural: false
	});
$('#LEA_APP_OPINION').inputlimiter({
	limit: 100,
	remText: '还可以输入  %n 字 /',
	limitText: '%n 字',
	zeroPlural: false
	});
requiredValidator.add("USER_ID");
requiredValidator.add("LEA_DATE");
requiredValidator.add("LEA_TYPE");
requiredValidator.add("LEA_SDATE");
requiredValidator.add("LEA_EDATE");
requiredValidator.add("LEA_DAYS");
numValidator.add("LEA_DAYS");
requiredValidator.add("LEA_CAUSE");
requiredValidator.add("APP_RESULT");
initCalendar('LEA_SDATE','%Y-%m-%d','LEA_SDATEPicker');
datetimeValidators[0].set("yyyy-MM-dd").add("LEA_SDATE");
initCalendar('LEA_EDATE','%Y-%m-%d','LEA_EDATEPicker');
datetimeValidators[0].set("yyyy-MM-dd").add("LEA_EDATE");
initDetailOpertionImage();
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
<<script type="text/javascript">
<%if(pageBean.getAttribute("errorMsg") != null){%>
	alert("<%=pageBean.getAttribute("errorMsg")%>");
<%}%> 
</script>

