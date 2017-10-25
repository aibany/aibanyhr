<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>编码类型</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function controlExtSQL(extSQL){
	if (extSQL == "Y"){
		$("tr[extSQL='Y']").show();
		$("tr[extSQL='N']").hide();
	}else{
		$("tr[extSQL='Y']").hide();
		$("tr[extSQL='N']").show();
	}
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
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" align="center" onClick="doSubmit({actionType:'save',checkUnique:'yes'})"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td>
   <td onMouseOver="onMover(this);" onMouseOut="onMout(this);" class="bartdx" align="center" onClick="goToBack();"><input value="&nbsp;" type="button" class="backImgBtn" title="返回" />返回</td>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0"  cellpadding="0">
  <tr>
    <th width="100" nowrap>类型标识</th>
    <td><%if ("insert".equals(pageBean.getOperaType())){%><input name="TYPE_ID" label="类型标识" type="text" id="TYPE_ID" value="<%=pageBean.inputValue("TYPE_ID")%>" maxlength="32" /><%}else{%><input disabled="disabled" name="TYPE_ID_NAME" label="类型标识" type="text" id="TYPE_ID_NAME" value="<%=pageBean.inputValue("TYPE_ID")%>" maxlength="32" /><input name="TYPE_ID" label="类型标识" type="hidden" id="TYPE_ID" value="<%=pageBean.inputValue("TYPE_ID")%>" /><%}%></td>
  </tr>
  <tr>
    <th width="100" nowrap>类型名称</th>
    <td><input name="TYPE_NAME" label="类型名称" type="text" id="TYPE_NAME" value="<%=pageBean.inputValue("TYPE_NAME")%>" /></td>
  </tr>
  <tr>
    <th width="100" nowrap>类型分组</th>
    <td><select name="TYPE_GROUP" label="类型分组" id="TYPE_GROUP"><%=pageBean.selectValue("TYPE_GROUP_SELECT",pageBean.inputValue("TYPE_GROUP"))%></select></td>
  </tr>
  <tr>
    <th width="100" nowrap>是否SQL扩展</th>
    <td><select name="EXTEND_SQL" label="是否SQL扩" id="EXTEND_SQL" onchange="controlExtSQL(this.value)"><%=pageBean.selectValue("EXTEND_SQL")%></select></td>
  </tr>  
  <tr extSQL="N">
    <th width="100" nowrap>是否缓存</th>
    <td><%=pageBean.selectRadio("IS_CACHED")%></td>
  </tr>
  <tr extSQL="N">
    <th width="100" nowrap>是否统一管理</th>
    <td><%=pageBean.selectRadio("IS_UNITEADMIN_SELECT").addAlias("IS_UNITEADMIN")%></td>
  </tr>
  <tr extSQL="N">
    <th width="100" nowrap>是否可以编辑</th>
    <td><%=pageBean.selectRadio("IS_EDITABLE_SELECT").addAlias("IS_EDITABLE")%></td>
  </tr>
  <tr extSQL="N">
    <th width="100" nowrap>编码长度限制</th>
    <td><input name="LEGNTT_LIMIT" label="编码长度限制" type="text" id="LEGNTT_LIMIT" value="<%=pageBean.inputValue("LEGNTT_LIMIT")%>" /></td>
  </tr>  
  <tr extSQL="N">
    <th width="100" nowrap>编码字符限制</th>
    <td><select name="CHARACTER_LIMIT" label="编码字符限制" id="CHARACTER_LIMIT"><%=pageBean.selectValue("CHARACTER_LIMIT_SELECT",pageBean.inputValue("CHARACTER_LIMIT"))%></select></td>
  </tr>
  <tr extSQL="Y">
    <th width="100" nowrap>SQL主体</th>
    <td><textarea name="SQL_BODY" rows="4" cols="65" id="SQL_BODY"><%=pageBean.inputValue("SQL_BODY")%></textarea></td>
  </tr>
  <tr extSQL="Y">
    <th width="100" nowrap>SQL条件</th>
    <td><textarea name="SQL_COND" rows="4" cols="65" id="SQL_COND"><%=pageBean.inputValue("SQL_COND")%></textarea></td>
  </tr>              
  <tr>
    <th width="100" nowrap>类型描述</th>
    <td><textarea name="TYPE_DESC" rows="4" cols="65" id="TYPE_DESC"><%=pageBean.inputValue("TYPE_DESC")%></textarea></td>
  </tr>    
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
</form>
<script language="JavaScript">
requiredValidator.add("TYPE_ID","TYPE_NAME","TYPE_GROUP","EXTEND_SQL");
intValidator.add("LEGNTT_LIMIT");
rawCharValidator.add("TYPE_ID");
initDetailOpertionImage();
controlExtSQL("<%=pageBean.selectedValue("EXTEND_SQL")%>");
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>