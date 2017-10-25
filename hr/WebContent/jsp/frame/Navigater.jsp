<%@ page contentType="text/html; charset=UTF-8" errorPage="/jsp/frame/Error.jsp" %>
<%@ page import="com.agileai.hotweb.domain.core.User"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<%
User user = (User)pageBean.getAttribute("loginUser");
String userName = user.getUserName();
%>
<html>
<head>
<title>导航条</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="js/util.js"></script>
<script type="text/javascript">
function hiddenMenu()
{       
	if (parent.document.getElementById('main').cols == '237,*')
	{
		$('#spliterImg').attr('src','images/index/spliter1.gif');
		parent.document.getElementById('main').cols = '*,100%';
	}
	else
	{
		$('#spliterImg').attr('src','images/index/spliter0.gif');
		parent.document.getElementById('main').cols =  '237,*';
	}
}
function logout(){
	parent.location.href='<%=pageBean.getHandlerURL()%>&actionType=logout';
}
function showPage(url){
	parent.mainFrame.location.href= url;
}
</script>
<style>
a {
	color:#FFC;
	font-weight:bold;
}
</style>
</head>
<body style="margin:0px;">
<form id="form1" name="form1" method="post" action="<%=pageBean.getHandlerURL()%>">
<table  border="0" cellpadding="0" cellspacing="0" width="100%" style="color:white;" background="images/index/navigaterbg.jpg">
<tr >
<td height="25" width="10">
<img src="images/index/spliter1.gif" width="8" height="25" border="0" onClick="javascript:hiddenMenu();" id="spliterImg" name="spliterImg"></td> 
<td width="70%" height="25">
当前功能：<span id="currentPath">系统主页面</span>
</td>
<td width="200" nowrap align="right">&nbsp;&nbsp;&nbsp;&nbsp;欢迎：<%=userName%></td>
<td width="50" align="right" nowrap><input class="formbutton" style="width:58px;" type="button" value="退 出" onClick="logout()"/>&nbsp;</td>
</tr>
</table>
</form>
</body>
</html>
