<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>薪资管理</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<style type="text/css">
.markable{
    background-color:yellow;
    color:black;
}
.markable0{
    background-color:darkorchid;
    color:white;
}

input[readonly="readonly"]{
	background-color: #e0e0e0;
}

</style>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ParamBar__" style="float: right;">&nbsp;</div>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
<%if(pageBean.getBoolValue("hasRight")){%>
   <aeai:previlege code="edit"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="enableSave()" ><input value="&nbsp;" type="button" class="editImgBtn" id="modifyImgBtn" title="编辑" />编辑</td></aeai:previlege>
   <aeai:previlege code="save"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="doSubmit({actionType:'save'})"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td></aeai:previlege>
  <%if(!(pageBean.selectedValue("SAL_STATE")).equals("1")){%>
   <aeai:previlege code="approve"><td  align="center"class="bartdx"onclick="stateApprove();" onmouseover="onMover(this);" onmouseout="onMout(this);"  ><input value="&nbsp;"type="button" class="approveImgBtn" id="SAL_STATE"  title="核准" />核准</td></aeai:previlege>
   <%}else{%>
   <aeai:previlege code="revokeApproval"><td  align="center"class="bartdx" onclick="revokeApproval()" onmouseover="onMover(this);" onmouseout="onMout(this);"  ><input value="&nbsp;"type="button" class="revokeApproveImgBtn" id="revokeApproval" title="反核准" />反核准</td></aeai:previlege>
   <%}%>
  <%}%>
   <aeai:previlege code="back"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="goToBack();"><input value="&nbsp;" type="button" class="backImgBtn" title="返回" />返回</td></aeai:previlege>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
    <th width="100" nowrap>人员姓名</th>
    <td>
    <input name="SAL_NAME" type="text" class="text" id="SAL_NAME" value="<%=pageBean.inputValue("SAL_NAME")%>" size="24" readonly='readonly' label="人员姓名" />
    <input id="SAL_USER" label="人员编码" name="SAL_USER" type="hidden" value="<%=pageBean.inputValue("SAL_USER")%>" size="24"  class="text" readonly='readonly'/>
    </td>

    <th width="100" nowrap>人员编码</th>
    <td>
    <input id="SAL_CODE" label="人员编码" name="SAL_CODE" type="text" value="<%=pageBean.inputValue("SAL_CODE")%>" size="24"  class="text" readonly='readonly'/>
    </td>

    <th width="100" nowrap>年</th>
    <td><input name="SAL_YEAR" type="text" class="text" id="SAL_YEAR" value="<%=pageBean.inputValue("SAL_YEAR")%>" size="24" readonly='readonly' label="年" />
    </td>

</tr>

<tr>
    <th width="100" nowrap>月</th>
    <td><input name="SAL_MONTH" type="text" class="text" id="SAL_MONTH" value="<%=pageBean.inputValue("SAL_MONTH")%>" size="24" readonly='readonly' label="月" />
    </td>

    <th width="100" nowrap>标准出勤</th>
    <td><input name="SAL_VALID_DAYS" type="text" class="text" id="SAL_VALID_DAYS" value="<%=pageBean.inputValue("SAL_VALID_DAYS")%>" size="24" readonly='readonly' label="标准出勤" />
    </td>

    <th width="100" nowrap>实际出勤</th>
    <td><input name="SAL_WORK_DAYS" type="text" class="text" id="SAL_WORK_DAYS" value="<%=pageBean.inputValue("SAL_WORK_DAYS")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="实际出勤" />
    </td>
</tr>

<tr>
    <th width="100" nowrap>底薪</th>
    <td><input name="SAL_BASIC" type="text" class="text" id="SAL_BASIC" value="<%=pageBean.inputValue("SAL_BASIC")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="基本工资" />
    </td>

    <th width="100" nowrap>普通加班(小时)</th>
    <td><input name="SAL_OVERTIME_NORMAL" type="text" class="text" id="SAL_OVERTIME_NORMAL" value="<%=pageBean.inputValue("SAL_OVERTIME_NORMAL")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="普通加班" />
    </td>

    <th width="100" nowrap>周末加班(小时)</th>
    <td><input name="SAL_OVERTIME_WEEKEND" type="text" class="text" id="SAL_OVERTIME_WEEKEND" value="<%=pageBean.inputValue("SAL_OVERTIME_WEEKEND")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="周末加班(小时)" />
    </td>
</tr>

<tr>
    <th width="100" nowrap>节假日加班(小时)</th>
    <td><input name="SAL_OVERTIME_HOLIDAY" type="text" class="text" id="SAL_OVERTIME_HOLIDAY" value="<%=pageBean.inputValue("SAL_OVERTIME_HOLIDAY")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="节假日加班(小时)" />
    </td>

    <th width="100" nowrap>加班费</th>
    <td><input name="SAL_OVERTIME" type="text" class="text" id="SAL_OVERTIME" value="<%=pageBean.inputValue("SAL_OVERTIME")%>" size="24" readonly='readonly' label="加班费" />
    </td>

    <th width="100" nowrap>全勤奖</th>
    <td><input name="SAL_AWARD_ALLDAYS" type="text" class="text" id="SAL_AWARD_ALLDAYS" value="<%=pageBean.inputValue("SAL_AWARD_ALLDAYS")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="全勤奖" />
    </td>
</tr>

<tr>
    <th width="100" nowrap>中班津贴</th>
    <td><input name="SAL_ALLOWANCE_MIDROOM" type="text" class="text" id="SAL_ALLOWANCE_MIDROOM" value="<%=pageBean.inputValue("SAL_ALLOWANCE_MIDROOM")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="中班津贴" />
    </td>

    <th width="100" nowrap>夜班津贴</th>
    <td><input name="SAL_ALLOWANCE_NIGHT" type="text" class="text" id="SAL_ALLOWANCE_NIGHT" value="<%=pageBean.inputValue("SAL_ALLOWANCE_NIGHT")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="夜班津贴" />
    </td>

    <th width="100" nowrap>其他津贴</th>
    <td><input name="SAL_ALLOWANCE_OTHER" type="text" class="text" id="SAL_ALLOWANCE_OTHER" value="<%=pageBean.inputValue("SAL_ALLOWANCE_OTHER")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="其他津贴" />
    </td>
</tr>

<tr>
    <th width="100" nowrap>职务津贴</th>
    <td><input name="SAL_SUBSIDY" type="text" class="text" id="SAL_SUBSIDY" value="<%=pageBean.inputValue("SAL_SUBSIDY")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="职务津贴" />
    </td>

    <th width="100" nowrap>技能津贴</th>
    <td><input name="SAL_ALLOWANCE" type="text" class="text" id="SAL_ALLOWANCE" value="<%=pageBean.inputValue("SAL_ALLOWANCE")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="技能津贴" />
    </td>

    <th width="100" nowrap>出差津贴</th>
    <td><input name="SAL_ALLOWANCE_OUT" type="text" class="text" id="SAL_ALLOWANCE_OUT" value="<%=pageBean.inputValue("SAL_ALLOWANCE_OUT")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="出差津贴" />
    </td>
</tr>

<tr>
    <th width="100" nowrap>浮动绩效</th>
    <td><input name="SAL_PERFORMANCE" type="text" class="text" id="SAL_PERFORMANCE" value="<%=pageBean.inputValue("SAL_PERFORMANCE")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="浮动绩效" />
    </td>

    <th width="100" nowrap>空调补助</th>
    <td><input name="SAL_ALLOWANCE_AIR" type="text" class="text" id="SAL_ALLOWANCE_AIR" value="<%=pageBean.inputValue("SAL_ALLOWANCE_AIR")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="空调补助" />
    </td>

    <th width="100" nowrap>综合补助</th>
    <td><input name="SAL_ALLOWANCE_ALL" type="text" class="text" id="SAL_ALLOWANCE_ALL" value="<%=pageBean.inputValue("SAL_ALLOWANCE_ALL")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="综合补助" />
    </td>
</tr>

<tr>
    <th width="100" nowrap>年资补助</th>
    <td><input name="SAL_ALLOWANCE_YEAR" type="text" class="text" id="SAL_ALLOWANCE_YEAR" value="<%=pageBean.inputValue("SAL_ALLOWANCE_YEAR")%>" size="24" readonly="readonly" label="年资补助 " />
    </td>

    <th width="100" nowrap>记点奖励</th>
    <td><input name="SAL_ALLOWANCE_RECORD" type="text" class="text" id="SAL_ALLOWANCE_RECORD" value="<%=pageBean.inputValue("SAL_ALLOWANCE_RECORD")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="记点奖励" />
    </td>

    <th width="100" nowrap>记功</th>
    <td><input name="SAL_ALLOWANCE_RECORD_WHAT" type="text" class="text" id="SAL_ALLOWANCE_RECORD_WHAT" value="<%=pageBean.inputValue("SAL_ALLOWANCE_RECORD_WHAT")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="记功" />
    </td>
</tr>

<tr>
    <th width="100" nowrap>记过</th>
    <td><input name="SAL_DEDUCT_RECORD_WHAT" type="text" class="text" id="SAL_DEDUCT_RECORD_WHAT" value="<%=pageBean.inputValue("SAL_DEDUCT_RECORD_WHAT")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="记过" />
    </td>

    <th width="100" nowrap>请假扣款</th>
    <td><input name="SAL_DEDUCT_NOWORK" type="text" class="text" id="SAL_DEDUCT_NOWORK" value="<%=pageBean.inputValue("SAL_DEDUCT_NOWORK")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="请假扣款" />
    </td>

    <th width="100" nowrap>扣餐费</th>
    <td><input name="SAL_DEDUCT_EAT" type="text" class="text" id="SAL_DEDUCT_EAT" value="<%=pageBean.inputValue("SAL_DEDUCT_EAT")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="扣餐费" />
    </td>
</tr>

<tr>
    <th width="100" nowrap>签卡扣款</th>
    <td><input name="SAL_DEDUCT_SIGNCARD" type="text" class="text" id="SAL_DEDUCT_SIGNCARD" value="<%=pageBean.inputValue("SAL_DEDUCT_SIGNCARD")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="签卡扣款" />
    </td>

    <th width="100" nowrap>离职扣款</th>
    <td><input name="SAL_DEDUCT_LEAVE" type="text" class="text" id="SAL_DEDUCT_LEAVE" value="<%=pageBean.inputValue("SAL_DEDUCT_LEAVE")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="离职扣款" />
    </td>

    <th width="100" nowrap>交保险</th>
    <td><input name="SAL_INSURE" type="text" class="text" id="SAL_INSURE" value="<%=pageBean.inputValue("SAL_INSURE")%>" size="24" readonly='readonly' label="交保险" />
    </td>
</tr>

<tr>
    <th width="100" nowrap>公积金</th>
    <td><input name="SAL_FUND_HOUSE" type="text" class="text" id="SAL_FUND_HOUSE" value="<%=pageBean.inputValue("SAL_FUND_HOUSE")%>" size="24"  readonly='readonly' label="公积金" />
    </td>

    <th width="100" nowrap>大病基金</th>
    <td><input name="SAL_FUND_ILL" type="text" class="text" id="SAL_FUND_ILL" value="<%=pageBean.inputValue("SAL_FUND_ILL")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="大病基金" />
    </td>

    <th width="100" nowrap>重新办卡扣款</th>
    <td><input name="SAL_DEDUCT_BUYCARD" type="text" class="text" id="SAL_DEDUCT_BUYCARD" value="<%=pageBean.inputValue("SAL_DEDUCT_BUYCARD")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="重新办卡扣款" />
    </td>
</tr>

<tr>
    <th width="100" nowrap>迟到早退扣款</th>
    <td><input name="SAL_DEDUCT_LATER" type="text" class="text" id="SAL_DEDUCT_LATER" value="<%=pageBean.inputValue("SAL_DEDUCT_LATER")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="迟到早退扣款" />
    </td>

    <th width="100" nowrap>个人所得税</th>
    <td><input name="SAL_TAX" type="text" class="text" id="SAL_TAX" value="<%=pageBean.inputValue("SAL_TAX")%>" size="24" readonly='readonly' label="个人所得税" />
    </td>

   <th width="100" height="19" nowrap>状态</th>
    <td><input id="SAL_STATE_NAME" label="状态" name="SAL_STATE_NAME" type="text" value="<%=pageBean.selectedText("SAL_STATE")%>" size="24" class="text" readonly="readonly"/>
	<input id="SAL_STATE" label="状态" name="SAL_STATE" type="hidden" value="<%=pageBean.selectedValue("SAL_STATE")%>" />
	</td>
</tr>

<tr>
   <th width="100" nowrap>总工资</th>
    <td><input name="SAL_TOTAL" type="text" class="text markable" id="SAL_TOTAL" value="<%=pageBean.inputValue("SAL_TOTAL")%>" size="24"  readonly="readonly" label="总工资" />
    </td>

     <th width="100" nowrap>实发工资</th>
    <td colspan="4"><input name="SAL_ACTUAL" type="text" class="text markable" id="SAL_ACTUAL" value="<%=pageBean.inputValue("SAL_ACTUAL")%>" size="24" <%if(!pageBean.getBoolValue("hasRight") || !pageBean.getBoolValue("isComeFromUpdate")){ %> readonly="readonly" <%}%> label="补贴" />
    </td>
    
</tr>

<tr>
    <th width="100" nowrap>备注</th>
    <td colspan="6"><textarea id="SAL_REMARKS" label="备注" name="SAL_REMARKS" cols="81" rows="5" class="textarea"><%=pageBean.inputValue("SAL_REMARKS")%></textarea>
</td>
</tr>

</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" id="SAL_ID" name="SAL_ID" value="<%=pageBean.inputValue("SAL_ID")%>" />
</form>
<script language="javascript">
$('#SAL_REMARKS').inputlimiter({
    limit: 100,
    remText: '还可以输入  %n 字 /',
    limitText: '%n 字',
    zeroPlural: false
    });
function stateApprove(){
    doSubmit({actionType:'approve'});
}
function revokeApproval(){
    doSubmit({actionType:'revokeApproval'});
}
numValidator.add("SAL_VALID_DAYS");
numValidator.add("SAL_WORK_DAYS");
numValidator.add("SAL_OVERTIME");
numValidator.add("SAL_LEAVE");
numValidator.add("SAL_BASIC");
numValidator.add("SAL_PERFORMANCE");
numValidator.add("SAL_SUBSIDY");
numValidator.add("SAL_BONUS");
numValidator.add("SAL_TOTAL");
requiredValidator.add("SAL_BONUS");
initDetailOpertionImage();
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
