<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>编码管理</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function changeTypeId(){
	postRequest('form1',{actionType:'changeTypeId',onComplete:function(responseText){
		var rspArray = responseText.split(",");		
		var characterLimit = rspArray[0];
		if ("C" == characterLimit){
			charValidator.add("CODE_ID");
			numValidator.remove("CODE_ID");
			charNumValidator.remove("CODE_ID");
			rawCharValidator.remove("CODE_ID");
		}else if ("N" == characterLimit){
			charValidator.remove("CODE_ID");
			numValidator.add("CODE_ID");
			charNumValidator.remove("CODE_ID");
			rawCharValidator.remove("CODE_ID");
		}else if ("B"== characterLimit){
			charValidator.remove("CODE_ID");
			numValidator.remove("CODE_ID");
			charNumValidator.add("CODE_ID");
			rawCharValidator.remove("CODE_ID");
		}else if ("A" == characterLimit){
			charValidator.remove("CODE_ID");
			numValidator.remove("CODE_ID");
			charNumValidator.remove("CODE_ID");
			rawCharValidator.add("CODE_ID");			
		}
		var lengthLimit = rspArray[1];
		lengthValidators[0].set(lengthLimit).add("CODE_ID");
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
    <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" align="center" onClick="enableSave()"><input value="&nbsp;" type="button" class="editImgBtn" id="modifyImgBtn" title="编辑" />编辑</td>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" align="center"onClick="doSubmit({actionType:'save',checkUnique:'yes'})"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存"  />保存</td>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" align="center" onClick="goToBack();" ><input value="&nbsp;" type="button" class="backImgBtn" title="返回"/>返回</td>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0"  cellpadding="0">
  <tr>
    <th width="100" nowrap>编码标识</th>
    <td><%if ("insert".equals(pageBean.getOperaType())){%><input label="编码标识" name="CODE_ID" type="text" id="CODE_ID" value="<%=pageBean.inputValue("CODE_ID")%>" /><%}else{%><input disabled="disabled" name="CODE_ID_NAME" type="text" id="CODE_ID_NAME" value="<%=pageBean.inputValue("CODE_ID")%>" /><input type="hidden" name="CODE_ID" id="CODE_ID" value="<%=pageBean.inputValue("CODE_ID")%>"/><%}%>
    </td>
  </tr>
  <tr>
    <th width="100" nowrap>编码值</th>
    <td><input label="编码值" name="CODE_NAME" type="text" id="CODE_NAME" value="<%=pageBean.inputValue("CODE_NAME")%>" /></td>
  </tr>
  <tr>
    <th width="100" nowrap>编码类型</th>
    <td><%if ("insert".equals(pageBean.getOperaType())){%><select onchange="changeTypeId()" name="CODE_TYPE_ID" id="CODE_TYPE_ID" lable="编码类型"><%=pageBean.selectValue("TYPE_ID")%></select><%}else{%><input name="TYPE_NAME" type="text" disabled="disabled" id="TYPE_NAME" value="<%=pageBean.selectText("TYPE_ID",pageBean.selectedValue("TYPE_ID"))%>" /><input type="hidden" name="CODE_TYPE_ID" id="CODE_TYPE_ID" value="<%=pageBean.selectedValue("TYPE_ID")%>"/><%}%></td>
  </tr>
  <tr>
    <th width="100" nowrap>编码排序</th>
    <td><input label="编码排序" name="CODE_SORT" type="text" id="CODE_SORT" value="<%=pageBean.inputValue("CODE_SORT")%>" /></td>
  </tr>
  <tr>
    <th width="100" nowrap>是否有效</th>
    <td><select label="是否有效" name="CODE_FLAG" id="CODE_FLAG"><%=pageBean.selectValue("CODE_FLAG")%></select></td>
  </tr>
  <tr>
    <th width="100" nowrap>编码描述</th>
    <td><textarea name="CODE_DESC" rows="4" cols="40" id="CODE_DESC"><%=pageBean.inputValue("CODE_DESC")%></textarea></td>
  </tr>    
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" name="manageType" id="manageType" value="<%=pageBean.inputValue("manageType")%>" />
</form>
<script language="JavaScript">
requiredValidator.add("CODE_ID","CODE_NAME","CODE_TYPE_ID","CODE_SORT","CODE_FLAG");
intValidator.add("CODE_SORT");
initDetailOpertionImage();
<%
String characterLimit = pageBean.getStringValue("CHARACTER_LIMIT");
String lengthLimit = pageBean.getStringValue("LEGNTT_LIMIT");
if ("C".equals(characterLimit)){
%>
charValidator.add("CODE_ID");
<%}else if ("N".equals(characterLimit)){%>
numValidator.add("CODE_ID");
<%}else if ("B".equals(characterLimit)){%>
charNumValidator.add("CODE_ID");
<%}else if ("A".equals(characterLimit)){%>
rawCharValidator.add("CODE_ID");
<%}%>
lengthValidators[0].set(<%=lengthLimit%>).add("CODE_ID");
<%
String isEditable = pageBean.getStringValue("IS_EDITABLE");
if ("N".equals(isEditable)){
%>
disableButton('modifyImgBtn');
disableButton('saveImgBtn');
<%}%>
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
