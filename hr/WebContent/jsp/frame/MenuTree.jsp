<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<%
String menuTreeSyntax = pageBean.getStringValue("menuTreeSyntax");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>功能菜单</title>
<link rel="stylesheet" href="css/dtree.css" type="text/css"/>
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="js/util.js"></script>
<script type="text/javascript" src="js/dtree.js"></script>
<script type="text/javascript">
function showFunction(funcId)
{
	if (parent.mainFrame.showSplash){
		parent.mainFrame.showSplash('正在加载页面…，请等候！');	
	}	
	sendRequest('index?MenuTree&actionType=showFunction&funcId='+funcId);
}
</script>
<style>
.treeheader {
	background: url(images/index/treeheader.jpg) repeat-x;
	height:25px;
	font-size:13px;
	text-align:left;
	padding-left:5px;
}
</style>
</head>
<body style="margin:0px;">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><div style="height:100%;overflow:auto;padding-left:5px;padding-top:3px;border-right:1px solid #005BAC;"><%=menuTreeSyntax%></div></td>
  </tr>
</table>
</body>
</html>
