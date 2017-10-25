<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<title>仂凯人力资源系统</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Cache-Control" content="no-cache" />
<link rel="stylesheet" type="text/css" href="css/login.css" />
<link rel="stylesheet" type="text/css" href="css/validate.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<script src="js/jquery-1.7.1.js" language="javascript"></script>
<script src="js/util.js" language="javascript"></script>
<script src="js/validate/processor.js" language="javascript"></script>
<script src="js/validate/validator.js" language="javascript"></script>
<script src="js/validate/filter.js" language="javascript"></script>
<script src="js/validate/validator.config.js" language="javascript"></script>
<style>
body{text-align:center}
#postionDiv{width:800px;margin-left:auto;margin-right:auto}
</style>
<script language="javascript">
function checkForRefresh() {
  if (top.location.href != self.location.href) {
    	top.location.href=self.location.href;
  }
}
function changeValideCode(){
	document.getElementById('valideCodeImg').src = "safecode?"+Math.floor(Math.random()*100000);
}
</script>
</head>
<body id="cas" onLoad="checkForRefresh();" style="margin:0px;">
<div id="top">
    <div id="main">
        <div id="login-form">
            <div id="login-form-content">
			<form id="form1" name="form1" class="login" method="post" action="<%=pageBean.getHandlerURL()%>">
			<%@include file="/jsp/inc/message.inc.jsp"%>
			<p class="label">
			<span class="tlabel">用&nbsp;&nbsp;户</span>
			<input id="userId" label="用户" name="userId" class="required" tabindex="1" accesskey="n" type="text" value="" size="25" autocomplete="off">
			</p>
			<p class="label">
			<span class="tlabel">密&nbsp;&nbsp;码</span>
			<input id="userPwd" label="密码" name="userPwd" class="required" onkeydown="keyDownLogin(event)" tabindex="2" accesskey="p" type="password" value="" size="25" autocomplete="off">
			</p>
			<p class="label">
			<span class="tlabel">验证码</span>
			<span>
			<input id="valideCode" style="width:121px;" label="验证码" placeholder="可以不填" name="valideCode" class="required" onkeydown="keyDownLogin(event)" tabindex="2" accesskey="y" type="text" value="" size="30" maxlength="16" autocomplete="off">
			<img style="vertical-align: top;margin-left:2px;height: 27px;" id="valideCodeImg" onclick="changeValideCode();" title="点击切换验证码图片" src="safecode"></img>
			</span>
			</p>
			<p style="margin-top:23px;margin-left: 48px;">
			<input class="btn-submit" name="button" accesskey="l" value="登  录" tabindex="4" type="button" onClick="doSubmit()">
			<input class="btn-reset" name="reset" accesskey="c" value="重  置" tabindex="5" type="reset">
			</p>
			<input type="hidden" name="actionType" id="actionType" value="login"/>
			</form>
            </div>
        </div>
    </div>
	<div id="bottom">
	    <div id="bottom-content" style="color:#FFF;">©libo 2016-2020 昆山仂凯电子有限公司</div>
	</div>
</div>
</body>
</html>
<script language="javascript">
requiredValidator.add("userId");
requiredValidator.add("userPwd");
/* requiredValidator.add("valideCode"); */
$('#userId').focus();
var pos = $('#userId').position();
$('#errorMsg').css({position:'absolute',width:'300px',height:'80px',top:(pos.top-2),left:(pos.left+$('#userId').width())+45})

function keyDownLogin(e)
{
  var key = window.event ? e.keyCode:e.which;
  if (key == 13)
    {
        doSubmit();
    }
} 
</script>