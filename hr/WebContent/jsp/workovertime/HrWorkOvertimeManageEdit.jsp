<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>加班申请</title>
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
function revokeApproval(){
	doSubmit({actionType:'revokeApproval'});
}
function saveMasterRecord(){
	if (validate()){
		if (ele("currentSubTableId")){
			var subTableId = $("#currentSubTableId").val();
			if (!checkEntryRecords(subTableId)){
				return;
			}
		}
		showSplash();
		postRequest('form1',{actionType:'saveMasterRecord',onComplete:function(responseText){
			if ("fail" != responseText){
				$('#operaType').val('update');
				$('#WOT_ID').val(responseText);
				doSubmit({actionType:'prepareDisplay'});
			}else{
				hideSplash();
				writeErrorMsg('保存操作出错啦！');
			}
		}});
	}
}
</script>
</head>
<body >
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ParamBar__" style="float: right;">&nbsp;</div>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
<%if(pageBean.getBoolValue("doEdit")){%>
  <aeai:previlege code="save"><td  align="center"class="bartdx"onclick="saveMasterRecord()" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;" type="button" class="saveImgBtn" id="savedrafeImgBtn" title="保存" />保存</td></aeai:previlege>
<%}%>
<%if(pageBean.getBoolValue("doSubmit")){%>
  <aeai:previlege code="submit"><td  align="center"class="bartdx"onclick="stateSubmit();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="submitImgBtn" id="submitImgBtn" title="提交" />提交</td></aeai:previlege>
<%} %>
<%if(pageBean.getBoolValue("doApprove")){ %>
  <aeai:previlege code="resubmit"><td  align="center"class="bartdx"onclick="stateDrafe();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="reSubmittedImgBtn" id="drafeImgBtn" title="反提交" />反提交</td></aeai:previlege>
  <aeai:previlege code="approve"><td  align="center"class="bartdx"onclick="stateApprove();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="approveImgBtn" id="approveImgBtn" title="核准" />核准</td></aeai:previlege>
<%} %>
<%if(pageBean.getBoolValue("doRevokeApprove")){ %>
   <aeai:previlege code="revokeApproval"><td  align="center"class="bartdx" onclick="revokeApproval()" onmouseover="onMover(this);" onmouseout="onMout(this);"  ><input value="&nbsp;"type="button" class="revokeApproveImgBtn" id="revokeApproval" title="反核准" />反核准</td></aeai:previlege>
<%}%>
  <aeai:previlege code="back"><td  align="center"class="bartdx"onclick="goToBack();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="backImgBtn" title="返回" />返回</td></aeai:previlege>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
				<th width="100" nowrap>加班人姓名</th>
				<td><input name="USER_ID_NAME" type="text" class="text"
					id="USER_ID_NAME"
					value="<%=pageBean.inputValue("USER_ID_NAME")%>" size="24"
					readonly="readonly" label="姓名" /> <input id="USER_ID" label="姓名"
					name="USER_ID" type="hidden"
					value="<%=pageBean.inputValue("USER_ID")%>" size="24" class="text" />
				</td>
			</tr>
<tr>
	<th width="100" nowrap>申请时间</th>
	<td><input id="WOT_DATE" label="申请加班时间" name="WOT_DATE" type="text" value="<%=pageBean.inputDate("WOT_DATE")%>" size="24" class="text" readonly="readonly"/>
</td>
</tr>
<tr>
	<th width="100" nowrap>加班地点</th>
	<td><input id="WOT_PLACE" label="加班地点" name="WOT_PLACE" type="text" value="<%=pageBean.inputValue("WOT_PLACE")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>加班日期</th>
	<td><input id="WOT_OVERTIME_DATE" label="加班日期" name="WOT_OVERTIME_DATE" type="text" value="<%=pageBean.inputDate("WOT_OVERTIME_DATE")%>" size="24" class="text" /><img id="WOT_OVERTIME_DATEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
	<span style="margin-left: 20px;margin-top: 5px;color: red;font-size: 15dip;">注意：普通工作日、周末、节假日请分开申请，否则一律按低等级日期类型计算!</span>
</td>
</tr>
<tr>
	<th width="100" nowrap>加班时长</th>
	<td><select id="WOT_TIME" label="加班时长" name="WOT_TIME" class="select"><%=pageBean.selectValue("WOT_TIME")%></select>
    <select id="WOT_TIME_COMPANY" label="加班时长" name="WOT_TIME_COMPANY" class="select"><%=pageBean.selectValue("WOT_TIME_COMPANY")%></select>
</td>
</tr>
<tr>
	<th width="100" nowrap>状态</th>
	<td>
	<input id="STATE_TEXT" label="状态" name="STATE_TEXT" type="text" value="<%=pageBean.selectedText("STATE")%>" size="24"  class="text" readonly="readonly"/>
	<input id="STATE" label="状态" name="STATE" type="hidden" value="<%=pageBean.selectedValue("STATE")%>" />
   </td>
</tr>
<tr>
	<th width="100" nowrap>参与人</th>
	<td><input id="WOT_PARTICIPANT" label="参与人" name="WOT_PARTICIPANT" type="text" value="<%=pageBean.inputValue("WOT_PARTICIPANT")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>加班原因</th>
	<td><textarea id="WOT_DESC" label="加班原因" name="WOT_DESC" cols="60" rows="5" class="textarea"><%=pageBean.inputValue("WOT_DESC")%></textarea>
</td>
</tr>
<% if (pageBean.getBoolValue("doSignIn")) {
 %> 
<tr>
				<th width="100" nowrap>核准人</th>
				<td><input name="WOT_APPROVER_NAME" type="text" class="text"
					id="WOT_APPROVER_NAME"
					value="<%=pageBean.inputValue("WOT_APPROVER_NAME")%>" size="24"
					readonly="readonly" label="核准人" /> <input id="WOT_APPROVER" label="核准人"
					name="WOT_APPROVER" type="hidden"
					value="<%=pageBean.inputValue("WOT_APPROVER")%>" size="24" class="text" />
				</td>
			</tr>
<tr>
	<th width="100" nowrap>核准时间</th>
	<td><input id="WOT_APP_TIME" label="核准时间" name="WOT_APP_TIME" type="text" value="<%=pageBean.inputTime("WOT_APP_TIME")%>" size="24" class="text" readonly="readonly"/>
</td>
</tr>
<tr>
	<th width="100" nowrap>核准结果</th>
	<td><select id="APP_RESULT" label="核准结果" name="APP_RESULT" class="select"><%=pageBean.selectValue("APP_RESULT")%></select>
</td>
</tr>
<tr>
	<th width="100" nowrap>核准意见</th>
	<td><textarea id="WOT_APP_OPINION" label="核准意见" name="WOT_APP_OPINION" cols="60" rows="3" class="textarea"><%=pageBean.inputValue("WOT_APP_OPINION")%></textarea>
</td>
</tr>
<%} %>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" id="WOT_ID" name="WOT_ID" value="<%=pageBean.inputValue("WOT_ID")%>" />
</form>
<script language="javascript">
$('#WOT_DESC').inputlimiter({
limit: 100,
remText: '还可以输入  %n 字 /',
limitText: '%n 字',
zeroPlural: false
});
requiredValidator.add("USER_ID");
requiredValidator.add("WOT_DATE");
requiredValidator.add("WOT_PLACE");
requiredValidator.add("WOT_OVERTIME_DATE");
requiredValidator.add("WOT_DESC");
requiredValidator.add("WOT_TIME");
requiredValidator.add("WOT_TIME_COMPANY");
requiredValidator.add("APP_RESULT");
initCalendar('WOT_OVERTIME_DATE','%Y-%m-%d','WOT_OVERTIME_DATEPicker');
datetimeValidators[0].set("yyyy-MM-dd").add("WOT_OVERTIME_DATE");
initDetailOpertionImage();
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
