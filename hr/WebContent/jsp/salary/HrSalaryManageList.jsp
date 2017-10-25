<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>薪资管理</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script type="text/javascript">
var setValidDaysBox;
function setValidDays(){
	var title = '有效天数';
	if (!setValidDaysBox){
		setValidDaysBox = new PopupBox('setValidDaysBox',title,{size:'normal',height:'300px',top:'10px'});
	}
	var year = $("#salDate").val().substring(0,4);
	var month = $("#salDate").val().substring(5,7);
	var url = "index?HrValidDays&actionType=prepareDisplay&year="+year+"&month="+month;
	setValidDaysBox.sendRequest(url);	
}
function gatherData(){	
	doSubmit({actionType:'gather'});
}
function revokeApproval(){
	if (actionType != insertRequestActionValue && !isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	doSubmit({actionType:'revokeApproval'});
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table class="toolTable" border="0" cellpadding="0" cellspacing="1">
<tr>
<%
		System.out.println("薪资汇总条件" + pageBean.getBoolValue("validDays") + ", " + pageBean.getBoolValue("hasRight") + "," + pageBean.getBoolValue("canGather"));
	%>

<%if(pageBean.getBoolValue("hasRight")){
	%>
<%if(pageBean.getBoolValue("canGather")){
	%>
   <aeai:previlege code="summary"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="gatherData()" <%if(!pageBean.getBoolValue("validDays")){%>hidden="hidden"<%}%> ><input id="gatherImgBtn" value="&nbsp;" title="汇总" type="button"  class="salaryImgBtn" />汇总</td></aeai:previlege>
   <%}%>
   <aeai:previlege code="edit"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="E" align="center" onclick="doRequest('updateRequest')"><input value="&nbsp;" title="编辑" type="button" id="edit" class="editImgBtn"  />编辑</td></aeai:previlege>
   <aeai:previlege code="approve"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="R" align="center" onclick="doRequest('approveRequest')"><input value="&nbsp;" title="核准" type="button" id="approve" class="approveImgBtn"   />核准</td></aeai:previlege>
   <aeai:previlege code="revokeApproval"><td  align="center"class="bartdx" onclick="revokeApproval()" onmouseover="onMover(this);" onmouseout="onMout(this);"  ><input value="&nbsp;"type="button" class="revokeApproveImgBtn" id="revokeApproval" title="反核准" />反核准</td></aeai:previlege>
   <aeai:previlege code="validDays"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="setValidDays()" ><input value="&nbsp;" title="设置有效天数" type="button" class="validdaysImgBtn" />设置有效天数</td></aeai:previlege>
   <aeai:previlege code="delete"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="doDelete($('#'+rsIdTagId).val());"><input id="deleteImgBtn" value="&nbsp;" title="删除"  type="button"  class="delImgBtn" />删除</td></aeai:previlege>
   <%}%>
   <aeai:previlege code="detail"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="V" align="center" onclick="doRequest('viewDetail')"><input value="&nbsp;" title="查看" type="button" class="detailImgBtn" />查看</td></aeai:previlege>    
</tr>
</table>
</div>
<div id="__ParamBar__">
<table class="queryTable"><tr><td>
&nbsp;年月<input id="salDate" label="时间" name="salDate" type="text" value="<%=pageBean.inputValue("salDate")%>"  size="10" class="text" />
&nbsp;<input type="submit" name="button" id="button" value="上一月" class="formbutton" onclick="doSubmit({actionType:'beforeMonth'})" />
&nbsp;<input type="submit" name="button" id="button" value="下一月" class="formbutton" onclick="doSubmit({actionType:'nextMonth'})" />
&nbsp;&nbsp;
&nbsp;状态<select id="salState" label="状态" name="salState" class="select" onchange="doQuery()"><%=pageBean.selectValue("salState")%></select>
&nbsp;<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
</td></tr></table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="薪资管理.csv"
retrieveRowsCallback="process" xlsFileName="薪资管理.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="${ec_rd == null ?15:ec_rd}"
listWidth="100%" 
height="391px"
>
<ec:row styleClass="odd" ondblclick="clearSelection();doRequest('viewDetail')" oncontextmenu="selectRow(this,{SAL_ID:'${row.SAL_ID}'});controlApproveBtn('${row.SAL_STATE}');refreshConextmenu()" onclick="selectRow(this,{SAL_ID:'${row.SAL_ID}'});controlApproveBtn('${row.SAL_STATE}');">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="100" property="SAL_YEAR_MONTH" title="年月"   />
	<ec:column width="100" property="SAL_NAME"  title="人员姓名"   />
	<ec:column width="100" property="SAL_CODE"  title="工号"   />
	<ec:column width="100" property="EMP_NOW_DEPT_NAME"  title="部门"   />
	<ec:column width="100" property="SAL_TOTAL" style="text-align:right;background-color:yellow;" title="工资总额"   />
	<ec:column width="100" property="SAL_ACTUAL" style="text-align:right;background-color:yellow;" title="实发工资"   />
	<ec:column width="100" property="SAL_BASIC" style="text-align:right;" title="底薪"   />
	<ec:column width="100" property="SAL_VALID_DAYS" style="text-align:right;" title="标准出勤"   />
	<ec:column width="100" property="SAL_WORK_DAYS" style="text-align:right;" title="实际出勤"   />
	<ec:column width="100" property="SAL_OVERTIME_NORMAL" style="text-align:right;" title="普通加班(小时)"   />
	<ec:column width="100" property="SAL_OVERTIME_WEEKEND" style="text-align:right;" title="周末加班(小时)"   />
	<ec:column width="100" property="SAL_OVERTIME_HOLIDAY" style="text-align:right;" title="法定加班(小时)"   />
	<ec:column width="100" property="SAL_OVERTIME" style="text-align:right;" title="加班费"   />
	<ec:column width="100" property="SAL_AWARD_ALLDAYS" style="text-align:right;" title="全勤奖"   />
	<ec:column width="100" property="SAL_PERFORMANCE" style="text-align:right;" title="浮动绩效"   />
	<ec:column width="100" property="SAL_ALLOWANCE_MIDROOM" style="text-align:right;" title="中班津贴"   />
	<ec:column width="100" property="SAL_ALLOWANCE_NIGHT" style="text-align:right;" title="夜班津贴"   />
	<ec:column width="100" property="SAL_ALLOWANCE_OTHER" style="text-align:right;" title="其他津贴"   />
	<ec:column width="100" property="SAL_SUBSIDY" style="text-align:right;" title="职务津贴"   />
	<ec:column width="100" property="SAL_ALLOWANCE" style="text-align:right;" title="技能津贴"   />
	<ec:column width="100" property="SAL_ALLOWANCE_OUT" style="text-align:right;" title="出差津贴"   />
	<ec:column width="100" property="SAL_ALLOWANCE_AIR" style="text-align:right;" title="空调补助"   />
	<ec:column width="100" property="SAL_ALLOWANCE_ALL" style="text-align:right;" title="综合补助"   />
	<ec:column width="100" property="SAL_ALLOWANCE_YEAR" style="text-align:right;" title="年资补助"   />
	<ec:column width="100" property="SAL_ALLOWANCE_RECORD" style="text-align:right;" title="记点奖励"   />
	<ec:column width="100" property="SAL_ALLOWANCE_RECORD_WHAT" style="text-align:right;" title="记功"   />
	<ec:column width="100" property="SAL_DEDUCT_RECORD_WHAT" style="text-align:right;" title="记过"   />
	<ec:column width="100" property="SAL_DEDUCT_NOWORKDAYS" style="text-align:right;" title="请假天数"   />
	<ec:column width="100" property="SAL_DEDUCT_NOWORK" style="text-align:right;" title="请假扣款"   />
	<ec:column width="100" property="SAL_DEDUCT_EAT" style="text-align:right;" title="扣餐费"   />
	<ec:column width="100" property="SAL_DEDUCT_SIGNCARD" style="text-align:right;" title="签卡扣款"   />
	<ec:column width="100" property="SAL_DEDUCT_LEAVE" style="text-align:right;" title="离职扣款"   />
	<ec:column width="100" property="SAL_INSURE" style="text-align:right;" title="交保险"   />
	<ec:column width="100" property="SAL_FUND_HOUSE" style="text-align:right;" title="公积金"   />
	<ec:column width="100" property="SAL_FUND_ILL" style="text-align:right;" title="大病基金"   />
	<ec:column width="100" property="SAL_DEDUCT_BUYCARD" style="text-align:right;" title="重新办卡扣款"   />
	<ec:column width="100" property="SAL_DEDUCT_LATER" style="text-align:right;" title="迟到早退扣款"   />
	<ec:column width="100" property="SAL_TAX" style="text-align:right;" title="个人所得税"   />
	<ec:column width="100" property="SAL_DECEASE" style="text-align:right;" title="扣款总计"   />
	<ec:column width="100" property="SAL_STATE" title="状态"   mappingItem="SAL_STATE"/>
</ec:row>
</ec:table>
<input type="hidden" name="SAL_ID" id="SAL_ID" value="" />
<input type="hidden" name="actionType" id="actionType" />
<script language="JavaScript">
function controlApproveBtn(stateResult){
if (stateResult == '1'){
		disableButton("approve");
		enableButton("revokeApproval");
		disableButton("deleteImgBtn");
	}else{
		enableButton("approve");
		disableButton("revokeApproval");
		enableButton("deleteImgBtn");
	}

if (stateResult == '1'){
		disableButton("edit");
	}else{
		enableButton("edit");
	}
}
setRsIdTag('SAL_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
