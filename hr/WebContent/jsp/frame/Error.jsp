<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<html>
<head>
<title>系统报错</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
function checkIsTop(){
	if (top.location.href != self.location.href) {
		top.location.href=self.location.href;
	}
}
</script>
</head>
<body onLoad="checkIsTop()" style="background-color:#F7F7F7">
<div style="background-image: url('<%=request.getContextPath()%>/images/error-bg.jpg');background-repeat:no-repeat;background-position:50% 0% ;height:500px;">
<div style="padding-top:250px;padding-left: 350px;">
<table align="center" width="450" border="0" cellpadding="0" cellspacing="0" bordercolordark="#FFFFFF" bordercolorlight="#0093D9">
  <tr> 
    <td width="40" height="50" align="center" style=" font-size:16px; font-weight:bold;"><img src="<%=request.getContextPath()%>/images/error.png" width="32" height="32"></td>
    <td style=" font-size:16px; font-weight:bold;">出现错误啦！</td>
  </tr>
</table>  
<table align="center" width="450" border="1" cellpadding="0" cellspacing="0" bordercolordark="#FFFFFF" bordercolorlight="#0093D9">
    <tr> 
    <td height="24">&nbsp;错误码： <%=request.getAttribute("javax.servlet.error.status_code")%></td>
  </tr>
    <tr>
      <td height="24">&nbsp;信息： <%=request.getAttribute("javax.servlet.error.message")%></td>
  </tr>
    <tr>
      <td height="24">&nbsp;异常类型： <%=request.getAttribute("javax.servlet.error.exception_type")%></td>
  </tr>
    <tr>
      <td height="24" align="right">请及时联系管理员处理！</td>
  </tr>  
</table>
</div>
</div>
</body>
</html>
