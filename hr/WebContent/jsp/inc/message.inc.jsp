<%@ page pageEncoding="UTF-8"%>
<%
String errorMsg = (String)request.getAttribute("errorMsg");
if (errorMsg == null){
	errorMsg = "";	
}
%>
<div ondblclick="javascript:this.style.display='none';" id="errorMsg" style="height:16px;background-color:#CCC;width: 99%;padding:5px; margin:2px 2px 5px 2px;<%if(errorMsg.equals("")){%>display:none;<%}%>">
<span style="float:right;color:#000">双击关闭提示</span>
<span style="float:left;color:#FF0000;"><%=errorMsg%></span>
</div>