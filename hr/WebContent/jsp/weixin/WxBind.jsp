<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta charset="utf-8">
<title>用户绑定设置</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/jsp/inc/resource4jqm.inc.jsp"%>
<style type="text/css">
.ui-select .ui-btn{
	padding: 8px 2px;
}
.ui-btn ui-input-btn{
	margin-top: 10px;
}
.ui-select .ui-btn span{
	text-align:left;
	padding-left:3px;
}
label.error{
	color:red;
}
#status{
	color:blue;
	margin-top:0px;
	border-style:solid; 
	border-width:0px; 
	border-color:blue;
	display:none;
}
</style>
<script type="text/javascript">
$(function(){	
	
	$("#form1").validate({submitHandler:function(){
			$("#submitButton").attr("disabled","disabled");
			$("#status").html("正在提交，请稍等……");
			$("#status").show();
			postRequest("form1",{actionType:"bindWxUser",onComplete:function(responseText){
				if ("success"==responseText){
					$("#status").css("color","green");
					if (confirm("绑定成功！签到不？")){
						doSubmit({actionType:'signIn'});
					}else{
						window.location.href = "resource?WxSignIn&actionType=showToday"
					}
				}else{
					$("#status").css("color","red");
					$("#status").html(responseText);
					$("#submitButton").removeAttr("disabled");
				}
			}});
		},errorPlacement: function(error, element) {  
			if (element.is("select")){
				error.insertBefore(element.parent().parent());
			}
			else if (element.is("input")){
				error.insertBefore(element.parent());			
			}			
		},wrapper: "span"
	});
});
function changeValideCode(){
	document.getElementById('valideCodeImg').src = "safecode?"+Math.floor(Math.random()*100000);
}
</script>
</head>
<body>
<form action="resource?WxBind" name="form1" id="form1" method="post" style="padding:7px 5px">
<div class="error"></div>
<div id="status" class="status"></div>
<label for="userId">用户名：</label>
<input id="userId" name="userId" class="required" tabindex="1" accesskey="n" type="text" value="" size="25" autocomplete="off" autocapitalize="off" >
<label for="userPwd">密 &nbsp;&nbsp;码：</label>
<input id="userPwd" name="userPwd" class="required" onkeydown="keyDownLogin(event)" tabindex="2" accesskey="p" type="password" value="" size="25" autocomplete="off" autocapitalize="off" >
<label for="valideCode">验证码：</label>
<input id="valideCode" name="valideCode" class="required" onkeydown="keyDownLogin(event)" tabindex="2" accesskey="y" type="text" value="" size="30" maxlength="16" autocomplete="off" autocapitalize="off" >
<img style="vertical-align: top;margin-left:2px;height: 27px;" id="valideCodeImg" onclick="changeValideCode();" title="点击切换验证码图片" src="safecode"></img>
<fieldset class="ui-grid">
    <button id="submitButton" type="submit" data-theme="b">提交</button>
</fieldset>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="openId" id="openId" value="<%=request.getParameter("openId") %>"/>
</form>
</body>
</html>