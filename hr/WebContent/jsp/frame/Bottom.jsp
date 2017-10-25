<%@ page contentType="text/html; charset=UTF-8" errorPage="/jsp/frame/Error.jsp" %>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<title>底部页面</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="js/util.js"></script>
</head>
<body style="margin:0px;">
<form id="form1" name="form1" method="post" action="<%=pageBean.getHandlerURL()%>">
<table  border="0" cellpadding="0" cellspacing="0" width="100%" style="color:white;" background="images/index/navigaterbg1.jpg">
<tr > 
<td height="23" align="center" style="color:white;">©libo 2016-2020 昆山仂凯电子有限公司</td>
</tr>
</table>
</form>
</body>
</html>
