<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" import="java.util.*" %>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<%
String currentSubTableId = pageBean.getStringValue("currentSubTableId");
String currentSubTableIndex = pageBean.getStringValue("currentSubTableIndex");
%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>基本信息</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<style type="text/css">
.markable0{
  background-color:darkorchid;
  color:white;
}

</style>
<script language="javascript">
var grpIdBox;
function openGrpIdBox(){
  var handlerId = "DeptTreeSelect"; 
  if (!grpIdBox){
    grpIdBox = new PopupBox('grpIdBox','请选择部门',{size:'normal',width:'300',top:'2px'});
  }
  var url = 'index?'+handlerId+'&targetId=EMP_NOW_DEPT&targetName=EMP_NOW_DEPT_NAME';
  grpIdBox.sendRequest(url);
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
        if("repeat"==responseText){
          hideSplash();
          writeErrorMsg('编号重复，请检查！');
        }else{
          $('#operaType').val('update');
          $('#EMP_ID').val(responseText);
          doSubmit({actionType:'prepareDisplay'});
        }
      }else{
        hideSplash();
        writeErrorMsg('保存操作出错啦！');
      }
    }});
  }
}
function changeSubTable(subTableId){
  $('#currentSubTableId').val(subTableId);
  doSubmit({actionType:'changeSubTable'});
}
function refreshPage(){
  doSubmit({actionType:'changeSubTable'});
}
function addEntryRecord(subTableId){
  $('#currentSubTableId').val(subTableId);
  doSubmit({actionType:'addEntryRecord'});
}
function deleteEntryRecord(subTableId){
  if (!isSelectedRow()){
    writeErrorMsg('请先选中一条记录!');
    return;
  }
  if (confirm('确认要删除该条记录吗？')){
    $('#currentSubTableId').val(subTableId);
    doSubmit({actionType:'deleteEntryRecord'}); 
  }
}
function checkEntryRecords(subTableId){
  var result = true;
  var currentRecordSize = $('#currentRecordSize').val();
  return result;
}
var insertSubRecordBox;
function insertSubRecordRequest(title,handlerId){
  if (!insertSubRecordBox){
    insertSubRecordBox = new PopupBox('insertSubRecordBox',title,{size:'normal',height:'300px',top:'10px'});
  }
  var url = 'index?'+handlerId+'&operaType=insert&EMP_ID='+$('#EMP_ID').val();
  insertSubRecordBox.sendRequest(url);  
}
var copySubRecordBox;
function copySubRecordRequest(title,handlerId,subPKField){
  clearSelection();
  if (!isSelectedRow()){
    writeErrorMsg('请先选中一条记录!');
    return;
  }
  if (!copySubRecordBox){
    copySubRecordBox = new PopupBox('copySubRecordBox',title,{size:'normal',height:'300px',top:'10px'});
  }
  var url = 'index?'+handlerId+'&operaType=copy&'+subPKField+'='+$("#"+subPKField).val();
  copySubRecordBox.sendRequest(url);  
}
var viewSubRecordBox;
function viewSubRecordRequest(operaType,title,handlerId,subPKField){
  clearSelection();
  if (!isSelectedRow()){
    writeErrorMsg('请先选中一条记录!');
    return;
  }
  if (!viewSubRecordBox){
    viewSubRecordBox = new PopupBox('viewSubRecordBox',title,{size:'normal',height:'300px',top:'10px'});
  }
  var url = 'index?'+handlerId+'&operaType='+operaType+'&'+subPKField+'='+$("#"+subPKField).val();
  viewSubRecordBox.sendRequest(url);
}
function deleteSubRecord(){
  if (!isSelectedRow()){
    writeErrorMsg('请先选中一条记录!');
    return;
  }
  if (confirm('确认要删除该条记录吗？')){
    doSubmit({actionType:'deleteSubRecord'}); 
  }
}
function doMoveUp(){
  if (!isSelectedRow()){
    writeErrorMsg('请先选中一条记录!');
    return;
  }
  doSubmit({actionType:'moveUp'});
}
function doMoveDown(){
  if (!isSelectedRow()){
    writeErrorMsg('请先选中一条记录!');
    return;
  } 
  doSubmit({actionType:'moveDown'});
  
}
function stateApprove(){
  doSubmit({actionType:'approve'});
}
function saveRecord(){
  if (!validate()){
    return;
  }
  showSplash();
  postRequest('form1',{actionType:'checkUnique',onComplete:function(responseText){
    if (responseText == ''){
      postRequest('form1',{actionType:'updateMasterRecord',onComplete:function(responseText){
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
function revokeApproval(){
  doSubmit({actionType:'revokeApproval'});
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div style="padding-top:7px;">
<div id="__ParamBar__" style="float: right;">&nbsp;</div>
<div class="photobg1" id="tabHeader">
<div class="newarticle1" onclick="changeSubTable('_base')">基础信息</div>
<%if (!"insert".equals(pageBean.getOperaType())){%>
 <div class="newarticle1" onclick="changeSubTable('HrEducation')">学习经历</div>
 <div class="newarticle1" onclick="changeSubTable('HrExperience')">工作经历</div>
 <div class="newarticle1" onclick="changeSubTable('HrWorkPerformance')">工作调动</div>
<%}%>
</div>
<div class="photobox newarticlebox" id="Layer0" style="height:auto;">
<div style="margin:2px;">
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
  <%if(pageBean.getBoolValue("doDetail")){ %>
  <aeai:previlege code="edit"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="enableSave()" ><input value="&nbsp;" type="button" class="editImgBtn" id="modifyImgBtn" title="编辑" />编辑</td></aeai:previlege>
  <aeai:previlege code="save"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="saveMasterRecord()"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td></aeai:previlege>
  <%} %>
  <%if(pageBean.getBoolValue("doApprove")){ %>
  <aeai:previlege code="approve"><td  align="center"class="bartdx"onclick="stateApprove();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="approveImgBtn" id="approveImgBtn" title="核准" />核准</td></aeai:previlege>
  <%} %>
  <%if(pageBean.getBoolValue("doRevokeApprove")){ %>
  <aeai:previlege code="revokeApproval"><td  onmouseover="onMover(this);" onmouseout="onMout(this);"   align="center" class="bartdx" onclick="revokeApproval()" ><input value="&nbsp;"type="button" class="revokeApproveImgBtn" id="revokeApproval" title="反核准" />反核准</td></aeai:previlege>
  <%} %>
  <aeai:previlege code="back"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="goToBack();"><input value="&nbsp;" type="button" class="backImgBtn" title="返回" />返回</td></aeai:previlege>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">

<tr>
  <th width="100" nowrap>工号</th>
  <td><input id="EMP_CODE" label="工号" name="EMP_CODE" type="text" value="<%=pageBean.inputValue("EMP_CODE")%>" <%if("readonly".equals(pageBean.getAttribute("onlyRead"))){ %>readonly="readonly"<%} %> <%if("codereadonly".equals(pageBean.getAttribute("codeReadonly"))){ %>readonly="readonly"<%} %> size="24" class="text" />
</td>
<th width="100" nowrap>学历</th>
  <td><select id="EMP_EDUCATION" label="学历" name="EMP_EDUCATION" class="select"><%=pageBean.selectValue("EMP_EDUCATION")%></select>
</td>
</tr>

<tr>
<th width="100" nowrap>姓名</th>
  <td><input id="EMP_NAME" label="姓名" name="EMP_NAME" type="text" value="<%=pageBean.inputValue("EMP_NAME")%>" size="24" class="text" />
</td>
<th width="100" nowrap>政治面貌</th>
  <td><select id="EMP_PARTY" label="政治面貌" name="EMP_PARTY" class="select"><%=pageBean.selectValue("EMP_PARTY")%></select>
</td>
</td>
</tr>

<tr>
<th width="100" nowrap>性别</th>
  <td>&nbsp;<%=pageBean.selectRadio("EMP_SEX")%>
</td>
<th width="100" nowrap>婚姻状况</th>
  <td><select id="EMP_MARITAL_STATUS" label="婚姻状况" name="EMP_MARITAL_STATUS" class="select"><%=pageBean.selectValue("EMP_MARITAL_STATUS")%></select>
</td>
</tr>

<tr>
  <th width="100" nowrap>部门</th>
  <td><input id="EMP_NOW_DEPT_NAME" name="EMP_NOW_DEPT_NAME" type="text" value="<%=pageBean.inputValue("EMP_NOW_DEPT_NAME")%>" size="24" class="text" readonly="readonly" />
  <input type="hidden" label="部门" id="EMP_NOW_DEPT" name="EMP_NOW_DEPT" value="<%=pageBean.inputValue("EMP_NOW_DEPT")%>" />
  <img id="grpIdSelectImage" src="images/sta.gif" width="16" height="16" onclick="openGrpIdBox()"/>
</td>
<th width="100" nowrap>工作状态</th>
  <td><select id="EMP_WORK_STATE" label="工作状态" name="EMP_WORK_STATE" class="select"><%=pageBean.selectValue("EMP_WORK_STATE")%></select>
</td>
</tr>

<tr>
  <th width="100" nowrap>岗位</th>
  <td><input id="EMP_NOW_JOB" label="岗位" name="EMP_NOW_JOB" type="text" value="<%=pageBean.inputValue("EMP_NOW_JOB")%>" size="24" class="text" />
</td>
<th width="100" nowrap>底薪</th>
  <td><input name="EMP_BASIC" type="text" class="text markable0" id="EMP_BASIC" value="<%=pageBean.inputValue("EMP_BASIC")%>" size="24" maxlength="6" label="底薪" />
</td>
</tr>

<tr>
<th width="100" nowrap>电话</th>
  <td><input id="EMP_TEL" label="电话" name="EMP_TEL" type="text" value="<%=pageBean.inputValue("EMP_TEL")%>" size="24" class="text" />
</td>
<th width="100" nowrap>浮动绩效</th>
  <td><input name="EMP_PERFORMANCE" type="text" class="text markable0" id="EMP_PERFORMANCE" value="<%=pageBean.inputValue("EMP_PERFORMANCE")%>" size="24" maxlength="6" label="绩效工资" />
</td>
</tr>

<tr>
  <th width="100" nowrap>身份证号</th>
  <td><input id="EMP_ID_NUMBER" label="身份证号" name="EMP_ID_NUMBER" type="text" value="<%=pageBean.inputValue("EMP_ID_NUMBER")%>" size="24" class="text" />
</td>
<th width="100" nowrap>职务津贴</th>
  <td><input name="EMP_SUBSIDY" type="text" class="text markable0" id="EMP_SUBSIDY" value="<%=pageBean.inputValue("EMP_SUBSIDY")%>" size="24" maxlength="6" label="补贴工资" />
</td>
</tr>

<tr>
  <th width="100" nowrap>出生日期</th>
  <td><input id="EMP_BIRTHDAY" label="出生日期" name="EMP_BIRTHDAY" type="text" value="<%=pageBean.inputDate("EMP_BIRTHDAY")%>" size="24" class="text" readonly="readonly"/><img id="EMP_BIRTHDAYPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
  <th width="100" nowrap>个人所得说</th>
  <td><input name="EMP_TAX" type="text" class="text markable0" id="EMP_TAX" value="<%=pageBean.inputValue("EMP_TAX")%>" size="24" maxlength="6" label="基本工资" />
</td>
</tr>

<tr>
<th width="100" nowrap>参工时间</th>
  <td><input id="EMP_REFERENCE_TIME" label="参工时间" name="EMP_REFERENCE_TIME" type="text" value="<%=pageBean.inputDate("EMP_REFERENCE_TIME")%>" size="24" class="text" /><img id="EMP_REFERENCE_TIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
 <th width="100" nowrap>交保险</th>
  <td><input name="EMP_INSURE" type="text" class="text markable0" id="EMP_INSURE" value="<%=pageBean.inputValue("EMP_INSURE")%>" size="24" maxlength="6" label="基本工资" />
</td>
</tr>

<tr>
  <th width="100" nowrap>上岗时间</th>
  <td><input id="EMP_INDUCTION_TIME" label="上岗时间" name="EMP_INDUCTION_TIME" type="text" value="<%=pageBean.inputDate("EMP_INDUCTION_TIME")%>" size="24" class="text" /><img id="EMP_INDUCTION_TIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
<th width="100" nowrap>技能津贴</th>
  <td><input name="EMP_ALLOWANCE" type="text" class="text markable0" id="EMP_ALLOWANCE" value="<%=pageBean.inputValue("EMP_ALLOWANCE")%>" size="24" maxlength="6" label="补助标准" />
</td>
</tr>

<tr>
  <th width="100" nowrap>转正时间</th>
  <td><input id="EMP_REGULAR_TIME" label="转正" name="EMP_REGULAR_TIME" type="text" value="<%=pageBean.inputDate("EMP_REGULAR_TIME")%>" size="24" class="text" /><img id="EMP_REGULAR_TIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
<th width="100" nowrap>全勤奖</th>
  <td><input name="EMP_AWARD_ALLDAYS" type="text" class="text markable0" id="EMP_AWARD_ALLDAYS" value="<%=pageBean.inputValue("EMP_AWARD_ALLDAYS")%>" size="24" maxlength="6" label="全勤奖" />
</td>
</tr>

<tr>
<th width="100" nowrap>离职时间</th>
  <td><input id="EMP_DIMISSION_TIME" label="上岗时间" name="EMP_DIMISSION_TIME" type="text" value="<%=pageBean.inputDate("EMP_DIMISSION_TIME")%>" size="24" class="text" /><img id="EMP_DIMISSION_TIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
  <th width="100" nowrap>中班津贴</th>
  <td><input name="EMP_ALLOWANCE_MIDROOM" type="text" class="text markable0" id="EMP_ALLOWANCE_MIDROOM" value="<%=pageBean.inputValue("EMP_ALLOWANCE_MIDROOM")%>" size="24" maxlength="6" label="中班津贴" />
</td>
</tr>

<tr>
<th width="100" nowrap>年假天数</th>
  <td><input id="EMP_ANNUAL_LEAVE_DAYS" label="年假天数" name="EMP_ANNUAL_LEAVE_DAYS" type="text" value="<%=pageBean.inputValue("EMP_ANNUAL_LEAVE_DAYS")%>" size="24" class="text" />
</td>
 <th width="100" nowrap>夜班津贴</th>
  <td><input name="EMP_ALLOWANCE_NIGHT" type="text" class="text markable0" id="EMP_ALLOWANCE_NIGHT" value="<%=pageBean.inputValue("EMP_ALLOWANCE_NIGHT")%>" size="24" maxlength="6" label="夜班津贴" />
</td>
</tr>

<tr>
  <th width="100" nowrap>籍贯</th>
  <td><input id="EMP_NATIVE_PLACE" label="籍贯" name="EMP_NATIVE_PLACE" type="text" value="<%=pageBean.inputValue("EMP_NATIVE_PLACE")%>" size="24" class="text" />
</td>
 <th width="100" nowrap>其他津贴</th>
  <td><input name="EMP_ALLOWANCE_OTHER" type="text" class="text markable0" id="EMP_ALLOWANCE_OTHER" value="<%=pageBean.inputValue("EMP_ALLOWANCE_OTHER")%>" size="24" maxlength="6" label="其他津贴" />
</td>
</tr>

<tr>
  <th width="100" nowrap>民族</th>
  <td><input id="EMP_NATIONAL" label="民族" name="EMP_NATIONAL" type="text" value="<%=pageBean.inputValue("EMP_NATIONAL")%>" size="24" class="text" />
  </td>
 <th width="100" nowrap>公积金</th>
  <td><input name="EMP_FUND_HOUSE" type="text" class="text markable0" id="EMP_FUND_HOUSE" value="<%=pageBean.inputValue("EMP_FUND_HOUSE")%>" size="24" maxlength="6" label="公积金" />
</td>
</tr>

<tr>
<th width="100" nowrap>邮箱</th>
  <td><input id="EMP_EMAIL" label="邮箱" name="EMP_EMAIL" type="text" value="<%=pageBean.inputValue("EMP_EMAIL")%>" size="24" class="text" />
</td>
 <th width="100" nowrap>年资补助</th>
  <td><input name="EMP_ALLOWANCE_YEAR" type="text" class="text markable0" id="EMP_ALLOWANCE_YEAR" value="<%=pageBean.inputValue("EMP_ALLOWANCE_YEAR")%>" size="24" maxlength="6" label="年资补助" />
</td>
</tr>

<tr>
<th width="100" nowrap>薪资卡号</th>
  <td><input id="EMP_BANK_CARD" label="薪资卡号" name="EMP_BANK_CARD" type="text" value="<%=pageBean.inputValue("EMP_BANK_CARD")%>" size="30" class="text" />
</td>
 <th width="100" nowrap>薪资卡开户行</th>
  <td><input name="EMP_BANK_CARDINFO" type="text" id="EMP_BANK_CARDINFO" value="<%=pageBean.inputValue("EMP_BANK_CARDINFO")%>" size="24" maxlength="64" label="银行卡信息" />
</td>
</tr>

<tr>
<th width="100" nowrap>紧急联系人</th>
  <td><input id="EMP_EMERG_MAN" label="紧急联系人" name="EMP_EMERG_MAN" type="text" value="<%=pageBean.inputValue("EMP_EMERG_MAN")%>" size="24" class="text" />
</td>
 <th width="100" nowrap>紧急联系电话</th>
  <td><input name="EMP_EMERG_PHONE" type="text" id="EMP_EMERG_PHONE" value="<%=pageBean.inputValue("EMP_EMERG_PHONE")%>" size="24" maxlength="15" label="大病基金" />
</td>
</tr>

<tr>
  <th width="100" nowrap>信息状态</th>
  <td>
  <input id="EMP_STATE_TEXT" label="信息状态" name="EMP_STATE_TEXT" type="text" value="<%=pageBean.selectedText("EMP_STATE")%>" size="24"  class="text" readonly="readonly"/>
  <input id="EMP_STATE" label="信息状态" name="EMP_STATE" type="hidden" value="<%=pageBean.selectedValue("EMP_STATE")%>" />
   </td>
<th width="100" nowrap>是否参与薪资汇总</th>
  <td>&nbsp;<%=pageBean.selectRadio("EMP_PARTICIPATE_SALARY")%>
  </td>
</tr>

</table>
</div>
</div>
<%if (!"insert".equals(pageBean.getOperaType())){%>
<%if ("HrEducation".equals(currentSubTableId)){ %>
<div class="photobox newarticlebox" id="Layer2" style="height:auto;">
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
<%if(pageBean.getBoolValue("doDetail")){ %>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="insertSubRecordRequest('人员学习经历','HrEducationEditBox')"><input value="&nbsp;" title="新增" type="button" class="createImgBtn" />新增</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="copySubRecordRequest('人员学习经历','HrEducationEditBox','EDU_ID')"><input value="&nbsp;" title="复制" type="button" class="copyImgBtn" />复制</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="viewSubRecordRequest('update','人员学习经历','HrEducationEditBox','EDU_ID')"><input value="&nbsp;" title="编辑" type="button" class="editImgBtn" />编辑</td>      
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="deleteSubRecord()"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" />删除</td>
<%} %>
</tr>   
   </table>
</div>
<div style="margin:2px;">
<%
List param1Records = (List)pageBean.getAttribute("HrEducationRecords");
pageBean.setRsList(param1Records);
%>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="人员学习经历.csv"
retrieveRowsCallback="process" xlsFileName="人员学习经历.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize|export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="auto"
>
<ec:row styleClass="odd" ondblclick="viewSubRecordRequest('detail','人员学习经历','HrEducationEditBox','EDU_ID')" onclick="selectRow(this,{EDU_ID:'${row.EDU_ID}'})">
  <ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
  <ec:column width="100" property="EDU_IN_TIME" title="入学日期" cell="date" format="yyyy-MM-dd" />
  <ec:column width="100" property="EDU_OUT_TIME" title="截止日期" cell="date" format="yyyy-MM-dd" />
  <ec:column width="100" property="EDU_EDUCATION" title="学习经历"   />
</ec:row>
</ec:table>
</div>
</div>
<input type="hidden" name="EDU_ID" id="EDU_ID" value=""/>
<script language="javascript">
setRsIdTag('EDU_ID');
</script>
<%}%>

<%if ("HrExperience".equals(currentSubTableId)){ %>
<div class="photobox newarticlebox" id="Layer2" style="height:auto;">
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
<%if(pageBean.getBoolValue("doDetail")){ %>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="insertSubRecordRequest('员工工作经历','HrExperienceEditBox')"><input value="&nbsp;" title="新增" type="button" class="createImgBtn" />新增</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="copySubRecordRequest('员工工作经历','HrExperienceEditBox','EXP_ID')"><input value="&nbsp;" title="复制" type="button" class="copyImgBtn" />复制</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="viewSubRecordRequest('update','员工工作经历','HrExperienceEditBox','EXP_ID')"><input value="&nbsp;" title="编辑" type="button" class="editImgBtn" />编辑</td>      
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="deleteSubRecord()"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" />删除</td>
<%}%>
</tr>   
   </table>
</div>
<div style="margin:2px;">
<%
List param1Records = (List)pageBean.getAttribute("HrExperienceRecords");
pageBean.setRsList(param1Records);
%>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="员工工作经历.csv"
retrieveRowsCallback="process" xlsFileName="员工工作经历.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize|export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="auto"
>
<ec:row styleClass="odd" ondblclick="viewSubRecordRequest('detail','员工工作经历','HrExperienceEditBox','EXP_ID')" onclick="selectRow(this,{EXP_ID:'${row.EXP_ID}'})">
  <ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
  <ec:column width="100" property="EXP_IN_TIME" title="入职日期" cell="date" format="yyyy-MM-dd" />
  <ec:column width="100" property="EXP_OUT_TIME" title="离职日期" cell="date" format="yyyy-MM-dd" />
  <ec:column width="100" property="EXP_EXPERIENCE" title="工作经历"   />
</ec:row>
</ec:table>
</div>
</div>
<input type="hidden" name="EXP_ID" id="EXP_ID" value=""/>
<script language="javascript">
setRsIdTag('EXP_ID');
</script>
<%}%>

<%if ("HrWorkPerformance".equals(currentSubTableId)){ %>
<div class="photobox newarticlebox" id="Layer2" style="height:auto;">
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
<%if(pageBean.getBoolValue("doDetail")){ %>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="insertSubRecordRequest('员工工作调动','HrWorkPerformanceEditBox')"><input value="&nbsp;" title="新增" type="button" class="createImgBtn" />新增</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="copySubRecordRequest('员工工作调动','HrWorkPerformanceEditBox','PER_ID')"><input value="&nbsp;" title="复制" type="button" class="copyImgBtn" />复制</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="viewSubRecordRequest('update','员工工作调动','HrWorkPerformanceEditBox','PER_ID')"><input value="&nbsp;" title="编辑" type="button" class="editImgBtn" />编辑</td>      
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="deleteSubRecord()"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" />删除</td>
<%} %>
</tr>   
   </table>
</div>
<div style="margin:2px;">
<%
List param1Records = (List)pageBean.getAttribute("HrWorkPerformanceRecords");
pageBean.setRsList(param1Records);
%>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="员工工作调动.csv"
retrieveRowsCallback="process" xlsFileName="员工工作调动.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize|export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="auto"
>
<ec:row styleClass="odd" ondblclick="viewSubRecordRequest('detail','员工工作调动','HrWorkPerformanceEditBox','PER_ID')" onclick="selectRow(this,{PER_ID:'${row.PER_ID}'})">
  <ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
  <ec:column width="100" property="PER_IN_TIME" title="入职日期" cell="date" format="yyyy-MM-dd" />
  <ec:column width="100" property="PER_NOW_TIME" title="截止日期" cell="date" format="yyyy-MM-dd" />
  <ec:column width="100" property="PER_WORK_PERFORMANCE" title="工作表现"   />
</ec:row>
</ec:table>
</div>
</div>
<input type="hidden" name="PER_ID" id="PER_ID" value=""/>
<script language="javascript">
setRsIdTag('PER_ID');
</script>
<%}%>


<input type="hidden" id="currentSubTableId" name="currentSubTableId" value="<%=pageBean.inputValue("currentSubTableId")%>" />
<%if (!"_base".equals(pageBean.inputValue("currentSubTableId"))){%>
<script language="javascript">
$("#Layer0").hide();
</script>
<%}%>
<%}%>
<script language="javascript">
new Tab('tab','tabHeader','Layer',<%=currentSubTableIndex%>);
<%if (!"_base".equals(pageBean.inputValue("currentSubTableId"))){%>
$("#Layer0").hide();
<%}%>
</script>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" id="EMP_ID" name="EMP_ID" value="<%=pageBean.inputValue("EMP_ID")%>" />
</div>
</form>
<script language="javascript">
initCalendar('EMP_BIRTHDAY','%Y-%m-%d','EMP_BIRTHDAYPicker');
datetimeValidators[0].set("yyyy-MM-dd").add("EMP_BIRTHDAY");

initCalendar('EMP_REFERENCE_TIME','%Y-%m-%d','EMP_REFERENCE_TIMEPicker');
datetimeValidators[1].set("yyyy-MM-dd").add("EMP_REFERENCE_TIME");

initCalendar('EMP_INDUCTION_TIME','%Y-%m-%d','EMP_INDUCTION_TIMEPicker');
datetimeValidators[2].set("yyyy-MM-dd").add("EMP_INDUCTION_TIME");

initCalendar('EMP_REGULAR_TIME','%Y-%m-%d','EMP_REGULAR_TIMEPicker');
datetimeValidators[3].set("yyyy-MM-dd").add("EMP_REGULAR_TIME");

initCalendar('EMP_DIMISSION_TIME','%Y-%m-%d','EMP_DIMISSION_TIMEPicker');
datetimeValidators[3].set("yyyy-MM-dd").add("EMP_DIMISSION_TIMEPicker");
charNumValidator.add("EMP_CODE");
requiredValidator.add("EMP_CODE");
requiredValidator.add("EMP_NAME");
requiredValidator.add("EMP_NOW_DEPT");
requiredValidator.add("EMP_NOW_JOB");
numValidator.add("EMP_BASIC");
numValidator.add("EMP_PERFORMANCE");
numValidator.add("EMP_SUBSIDY");
numValidator.add("EMP_TAX");
numValidator.add("EMP_INSURE");
numValidator.add("EMP_ANNUAL_LEAVE_DAYS");
numValidator.add("EMP_ALLOWANCE");
initDetailOpertionImage();
$(function(){
  resetTabHeight(80);
});
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
