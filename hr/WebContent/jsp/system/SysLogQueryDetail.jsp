<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>系统日志</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
</head>
<body>
<script language="javascript">
wrTitle('系统日志');
</script>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<table class="detailTable" cellspacing="0"  cellpadding="0">
  <tr>
    <th width="100" nowrap >操作时间</th>
    <td width="80%"><%=pageBean.inputTime("OPER_TIME")%></td>
  </tr>
  <tr>
    <th width="100" nowrap >所在地址</th>
    <td width="80%"><%=pageBean.labelValue("IP_ADDTRESS")%></td>
  </tr>
  <tr>
    <th width="100" nowrap >操作人ID</th>
    <td><%=pageBean.labelValue("USER_ID")%></td>
  </tr>
  <tr>
    <th width="100" nowrap >操作人名称</th>
    <td><%=pageBean.labelValue("USER_NAME")%></td>
  </tr>
  <tr>
    <th width="100" nowrap >功能名称</th>
    <td><%=pageBean.labelValue("FUNC_NAME")%></td>
  </tr>
  <tr>
    <th width="100" nowrap >操作类型</th>
    <td><%=pageBean.labelValue("ACTION_TYPE")%></td>
  </tr>      
</table>
<table width="100%" border="0">
  <tr>
    <td align="center"><input type="button" name="button" id="button" value="关闭窗口" class="formbutton" onClick="parent.PopupBox.closeCurrent()"></td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value="<%=pageBean.getOperaType()%>"/>
</form>
</body>
</html>
