var _CURRENT_BOX_;
PopupBox.contextPath="";
function PopupBox(name,title,params){
	this.name = name;
	this.params = params;
	this.title = title;	
	this.closeBtnId = name+"ImgBtn";
	this.scroll = "no";
	
	this.divName = this.name + "Div";
	this.frmName = this.name + "Iframe";	
	
	this.width = '700px';
	this.height = '500px';
	if (params && params.size){
		if (params.size == 'small'){
			this.width = '250px';
			this.height = '280px';
		}
		else if (params.size == 'big'){
			this.width = '725px';
			this.height = '450px';			
		}
		else if (params.size == 'normal'){
			this.width = '600px';
			this.height = '400px';			
		}
	}
	if (params && params.scroll){
		this.scroll = params.scroll;
	}	
	if (params && params.height){
		this.height = params.height;
	}
	if (params && params.width){
		this.width = params.width;
	}
	var	clientHeight = $(document.body).height();
	
	if (clientHeight < 400){
		clientHeight = window.screen.availHeight - 100;
	}

	if (PopupBox.getIntNumber(this.height) > clientHeight){
		this.height = (clientHeight-2)+'px';
	}
	this.init();
	//alert("this.width is " + this.width + " this.height is " + this.height);
	this.top = (clientHeight - $("#"+this.divName).height())/2 ;
	if (params && params.top){
		this.top = params.top;
	}
	
	//alert("this.left is " + this.left + " this.top is " + this.top);
	this.resetPosition();
}

PopupBox.prototype.init=function(){
	var boxArray = new Array();
	var frmHeight;
	if (this.height.indexOf('px') > 0){
		frmHeight = parseInt(this.height.substring(0,this.height.length-2));
		frmHeight = frmHeight -26;
	}
	boxArray.push("<div id='"+this.divName+"' style='border:1px #6699CC outset;margin:0px;padding:0px;'>");	
	boxArray.push("<table width='100%' border='0' cellpadding='0' cellspacing='0'>");
	boxArray.push("  <tr ondblclick=\"$('#"+this.name+"MainArea').toggle();\" style='background-image:url("+PopupBox.contextPath+"images/box_header_bg.png);background-repeat:repeat-x'>");
	boxArray.push("    <td height='23' width='50%' style='background-image:url("+PopupBox.contextPath+"images/box_header.png); background-repeat:no-repeat;padding-top:0px;padding-left:30px;font-size:14px;font-weight:bold;color:#FFF;'>"+this.title+"</td>");
	boxArray.push("    <td height='23' width='50%' align='right' style='float: right;text-align: right;'><span style='width:20px;height:20px;vertical-align: middle;margin:0px;padding:3px 3px 0px 3px;text-align:center;display:inline-block;' onMouseMove='PopupBox.onMover(this)' onMouseOut='PopupBox.onMout(this)'><img id='"+this.closeBtnId+"' onclick='"+this.name+".closeBox()' src='"+PopupBox.contextPath+"images/close.gif' width='15' height='15' style='margin:0px;cursor:pointer' alt='关闭窗口' title='关闭窗口'/></span></td>");
	boxArray.push("  </tr>");
	boxArray.push("  <tr id='"+this.name+"MainArea' style='margin:0px;padding:0px;'>");
	boxArray.push("    <td colspan='2'>");
	boxArray.push("<iframe id="+this.frmName+" name="+this.frmName+" border='0' frameborder='0' frameSpacing='0'  width='100%' height='"+frmHeight+"px' src='' scrolling='"+this.scroll+"' style='margin:0x;padding:0px;'></iframe>");
	boxArray.push("    </td>");
	boxArray.push("  </tr>");
	boxArray.push("</table>");
	
	boxArray.push("</div>");
	var boxSyntax = boxArray.join("\r\n");
	$(document.body).append(boxSyntax);
	$("#"+this.divName).css({position:'absolute',display:'none',width:this.width,"background-color":'#FFFFFF',"z-index":'99999'});
}
PopupBox.prototype.resetPosition = function(){
	var clientWidth = $(document.body).width();
	this.left = (clientWidth - $("#"+this.divName).width())/2-2;	
	$("#"+this.divName).css({top:this.top,left:this.left});
}
PopupBox.prototype.closeBox=function(){
	$("#"+this.divName).css({display:'none'});
}
PopupBox.prototype.getIframeName = function(){
	return this.frmName;
}
PopupBox.prototype.getBoxdivName = function(){
	return this.divName;
}
PopupBox.prototype.getCloseBtnId = function(){
	return this.closeBtnId;
}
PopupBox.prototype.bindLoadEvent = function(args){
	$("#"+this.frmName).unbind('load');
	if (args && args.handler){
		$("#"+this.frmName).bind('load',args.handler);
	}else{
		var divName = this.divName;
		$("#"+this.frmName).bind('load',function(){
			$("#"+divName).show();
		});
	}
}
PopupBox.prototype.sendRequest=function(url,args){
	_CURRENT_BOX_ = this;
	$("#"+this.frmName).unbind('load');
	if (args && args.handler){
		$("#"+this.frmName).bind('load',args.handler);
	}else{
		var divName = this.divName;
		$("#"+this.frmName).bind('load',function(){
			$("#"+divName).show();
		});
	}	
	$("#"+this.frmName).attr("src",url);
}
PopupBox.prototype.responseAction=function(formId,args){
	_CURRENT_BOX_ = this;
	$("#"+this.frmName).unbind('load');
	if (args && args.handler){
		$("#"+this.frmName).bind('load',args.handler);
	}else{
		var divName = this.divName;
		$("#"+this.frmName).bind('load',function(){
			$("#"+divName).show();			
		});
	}
	$("#"+formId).attr("target","");
}
PopupBox.getIntNumber = function(value){
	var result = 0;
	if (value.indexOf(value) > 0){
		result = parseInt(value.substring(0,value.length-2));
	}else{
		result = parseInt(value);
	}
	return result;
}
PopupBox.closeCurrent=function(){
	_CURRENT_BOX_.closeBox();
}
PopupBox.onMover = function(obj){
    $(obj).css({"background-color":'#FFDDA6'});
}
PopupBox.onMout = function(obj){
    $(obj).css({"background-color":''});
}
