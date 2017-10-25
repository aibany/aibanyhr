/**
* 右键操作菜单
* 实例化：var contextMenu = new ContextMenu('contextMenu','treeContainer');
* 添加菜单项：contextMenu.addItem("theMenuItemId","theName","theParentMenuItemId",function(){
		YourJsFunction
	});	
* 添加分割符号：contextMenu.addSperator("theParentMenuItemId");
* 关联右键菜单和页面对象：
*	$('#YourTargetHtmlObjectId').bind('contextmenu',function(evt){
*			contextMenu.onContextMenu(evt);
*	});
* 本菜单支持多级菜单（级数不限），可以通过调用contextMenu.addMenu()来实现……
*/
function ContextMenu(menuName,effectArea){
	this.menuName = menuName;
	this.effectArea = null;
	if (effectArea){
		this.effectArea = effectArea;
	}
	this.clickHandlers = new Array();
	this.menuArray = new Array();
	
	eval("arr_"+ this.menuName +" = new Array();");	
	var tempStr = new Array();
	tempStr.push("<div id='"+this.menuName+"_extmenu_"+this.menuName+"' class='CM_div' pid=''>");
	tempStr.push("<table class='CM_table' border='0' cellspacing='0'>");
	tempStr.push("<tbody id='"+this.menuName+"_extTable_"+this.menuName+"'>");
	tempStr.push("</tbody></table>");
	tempStr.push("</div>");
	var tempSyntax = tempStr.join("\r\n");
	$("body").append(tempSyntax);
}
ContextMenu.prototype.containKey = function(menuItemId){
	for (var i=0;i < this.menuArray.length;i++){
		if (this.menuArray[i] == menuItemId)return true;
	}
	return false;
}
ContextMenu.prototype.clear = function(){
	$("#"+this.menuName+"_extTable_"+this.menuName).html("");
}
ContextMenu.prototype.addMenu = function(id,name,parent){
	if (this.containKey(id)){
		alert(this.menuName+"_extmenu_"+id+"already exist!");
		return;
	}
	var tempSyntax = "";
	var tempStr = new Array();
	tempStr.push("<tr id='"+this.menuName+"_p_"+id+"' class='CM_tr_out'");
	tempStr.push(" onmouseover='"+this.menuName+".pOnMouseOver(\""+id+"\",\""+parent+"\")'");
	tempStr.push(" onmouseout='"+this.menuName+".pOnMouseOut(\""+id+"\",\""+parent+"\")'");
	tempStr.push(" onmouseup='event.cancelBubble=true'");
	tempStr.push(" onclick='event.cancelBubble=true'");
	tempStr.push("><td nowrap>");
	tempStr.push("&nbsp;&nbsp;&nbsp;"+name+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td style='text-align: right;'>>");
	tempStr.push("</td></tr>");
	tempSyntax = tempStr.join("\r\n");
	var parentTableId = this.menuName+"_extTable_"+parent;
	$("#"+parentTableId).append(tempSyntax);

	tempStr = new Array();
	tempStr.push("<div id='"+this.menuName+"_extmenu_"+id+"' class='CM_div' pid='"+parent+"'>");
	tempStr.push("<table class='CM_table' border='0' cellspacing='0'>");
	tempStr.push("<tbody id='"+this.menuName+"_extTable_"+id+"'>");
	tempStr.push("</tbody></table>");
	tempStr.push("</div>");
	var parentDivId = this.menuName+"_extmenu_"+parent;
	tempSyntax = tempStr.join("\r\n");
	$("body").append(tempSyntax);
}

ContextMenu.prototype.addSperator = function(parent){
	var tempStr = new Array();	
	tempStr.push("<tr style='height:3;' class='CM_tr_out' onclick='event.cancelBubble=true'");
	tempStr.push(" onmouseover='"+this.menuName+".onSpeMouseOver(\""+parent+"\")'");
	tempStr.push(" onmouseout='"+this.menuName+".onSpeMouseOut(\""+parent+"\")'");
	tempStr.push(" onmouseup='event.cancelBubble=true'><td colspan='2'><hr class='CM_sperator'></td></tr>");
	var tempSyntax = tempStr.join("\r\n");
	var parentTableId = this.menuName+"_extTable_"+parent;
	$("#"+parentTableId).append(tempSyntax);
}

ContextMenu.prototype.addItem = function(id,name,parent,handler){
	var tempStr = new Array();	
	if (this.containKey(id)){
		alert(this.menuName+"_item_"+id+"already exist!");
		return;
	}
		
	tempStr.push("<tr id='"+this.menuName+"_item_"+id+"' class='CM_tr_out'");
	tempStr.push(" onmouseover='"+this.menuName+".onMouseOver(\""+id+"\",\""+parent+"\")'");
	tempStr.push(" onmouseout='"+this.menuName+".onMouseOut(\""+id+"\",\""+parent+"\")'");
	tempStr.push(" onclick='event.cancelBubble=true'");

	if(location == null)
		tempStr.push(" onmouseup='"+this.menuName+".onMouseUp()'");
	else
		tempStr.push(" onmouseup='"+this.menuName+".onMouseUp()'");

	tempStr.push("><td nowrap>");
	tempStr.push("&nbsp;&nbsp;&nbsp;"+name+"&nbsp;&nbsp;&nbsp;");
	tempStr.push("</td><td></td></tr>");
	var tempSyntax = tempStr.join("\r\n");	
	
	var parentTableId = this.menuName+"_extTable_"+parent;
	$("#"+parentTableId).append(tempSyntax);
	
	if(handler != null){
		this.clickHandlers[this.clickHandlers.length] = new ContextMenu.ClickHandler(id,handler);	
	}
}

ContextMenu.prototype.onMouseUp = function(){	
	this.onContextClick();
}

ContextMenu.prototype.onMouseOver = function(id,parent){
	var menuItem;
	if(parent != this.menuName){
		var parentItem;
		parentItem = ele(this.menuName+"_p_"+parent);
		parentItem.className="CM_tr_over";
	}
	menuItem = ele(this.menuName+"_item_"+id);
	menuItem.className = "CM_tr_over";
	this.hideAll(parent,true);
}
ContextMenu.prototype.onMouseOut = function(id,parent){
	var menuItem;
	menuItem = ele(this.menuName+"_item_"+id);
	menuItem.className = "CM_tr_out";
	if (ele(this.menuName+"_p_"+parent)){
		ele(this.menuName+"_p_"+parent).className='CM_tr_out';
	}
}

ContextMenu.prototype.onSpeMouseOver = function(parent){
	if(parent != this.menuName){
		var parentItem;
		parentItem = ele(this.menuName+"_p_"+parent);
		parentItem.className= "CM_tr_over";
	}
	this.hideAll(parent,true);
}
ContextMenu.prototype.onSpeMouseOut = function(parent){
	if (ele(this.menuName+"_p_"+parent)){
		ele(this.menuName+"_p_"+parent).className='CM_tr_out';
	}
}

ContextMenu.prototype.pOnMouseOver = function(id,parent){
	var menuItem;
	var menuExtend;
	var menuParent;
	if(parent != this.menuName){
		var parentItem;
	  	parentItem = ele(this.menuName+"_p_"+parent);
	  	parentItem.className="CM_tr_over";
	}
	this.hideAll(parent,true);
	menuItem = ele(this.menuName+"_p_"+id);
	menuExtend = ele(this.menuName+"_extmenu_"+id);
	menuParent = ele(this.menuName+"_extmenu_"+parent);
	menuItem.className="CM_tr_over";
	menuExtend.style.display="block";
	
	var parentPosition =$(menuParent).offset();
	var menuParentOffsetLeft = parentPosition.left;
	var menuParentOffsetTop = parentPosition.top;
	
	var itemPosition = $(menuItem).offset();	
	var menuItemOffsetLeft = itemPosition.left;
	var menuItemOffsetTop = itemPosition.top;	
	
	var eleTop;
	var eleLeft;
	var eleWidth;
	var eleHeight;
	if (document.documentElement){
		eleTop = document.documentElement.scrollTop;
		eleLeft = document.documentElement.scrollLeft;
		eleWidth = document.documentElement.clientWidth;
		eleHeight = document.documentElement.clientHeight;
	}
	else{
		eleTop = document.body.scrollTop;
		eleLeft = document.body.scrollLeft;
		eleWidth = document.body.clientWidth;
		eleHeight = document.body.clientHeight;		
	}
		
	var offsetLeft = menuItemOffsetLeft + $(menuItem).width();
	var offsetTop = menuItemOffsetTop;
	$(menuExtend).css({left:(offsetLeft+'px'),top:(offsetTop+'px')});
	
	var newWidth = $(menuExtend).children(0).width();
	var newHeight = $(menuExtend).children(0).height();
	$(menuExtend).css({width:(newWidth+'px'),height:(newHeight+'px')});
	
	if(menuExtend.style.left+$(menuExtend).width() > eleLeft + eleWidth){
		menuExtend.style.left = menuExtend.style.left - $(menuParent).width() - $(menuExtend).width() + 8;
	}
	if(menuExtend.style.left < 0){
		menuExtend.style.left = menuParentOffsetLeft + $(menuParent).width();	
	}
		
	if(menuExtend.style.top + $(menuExtend).height() > eleTop + eleHeight){
	  	menuExtend.style.top =  eleHeight - $(menuExtend).height();	
	}
	if(menuExtend.style.top < 0){
		menuExtend.style.top=0;		
	}
}
ContextMenu.prototype.pOnMouseOut =function(id,parent){
	ele(this.menuName+"_p_"+id).className='CM_tr_out';	
}
ContextMenu.ClickHandler = function (itemId,handler){
	this.itemId = itemId;
	this.handler = handler;
}
ContextMenu.prototype.hideAll = function(parent,isOnlyChild){                       
	var area;                                                            
	var temp; 
	var divNodes = $("div[pid='"+parent+"']");
	if (divNodes){
		for (var i=0;i < divNodes.length;i++){
			divNodes[i].hide();
		}
	}
	if (!isOnlyChild){
		var parentId = this.menuName+"_extmenu_"+parent;
		$("#"+parentId).hide();
	}
}

ContextMenu.prototype.onContextMenu = function(evt){
  	var event = evt || window.event;  

	this.hideAll(this.menuName,false);
	var popMenu = $("#"+this.menuName+"_extmenu_"+this.menuName);	

	var eleTop;
	var eleLeft;
	var eleWidth;
	var eleHeight;
	
	var eleTop;
	var eleLeft;
	var eleWidth;
	var eleHeight;
	if (document.documentElement){
		eleTop = document.documentElement.scrollTop;
		eleLeft = document.documentElement.scrollLeft;
		eleWidth = document.documentElement.clientWidth;
		eleHeight = document.documentElement.clientHeight;
	}
	else{
		eleTop = document.body.scrollTop;
		eleLeft = document.body.scrollLeft;
		eleWidth = document.body.clientWidth;
		eleHeight = document.body.clientHeight;		
	}
	
	var offsetLeft = eleLeft+ event.clientX;
	var offsetTop = eleTop+ event.clientY;
	$(popMenu).css({left:(offsetLeft+'px'),top:(offsetTop+'px')});
	
	popMenu.show();
	popMenu.focus();
	
	var newWidth = $(popMenu).children(0).width();
	var newHeight = $(popMenu).children(0).height();
	popMenu.css({width:(newWidth+'px'),height:(newHeight+'px')});
	
	var position = popMenu.position();
	if(position.left+popMenu.width() > eleLeft+eleWidth){
		popMenu.css({left:eleLeft+eleWidth-popMenu.width()});
	}		
	if(position.left < 0) {
		popMenu.css({left:'0px'});
	}
	if(position.top+popMenu.height() > eleTop+eleHeight){
		popMenu.css({top:eleTop+eleHeight-popMenu.height()-8});
	}
	if(position.top < 0){
		popMenu.css({top:'0px'});
	}
}
ContextMenu.prototype.onContextClick = function (){
	this.hideAll(this.menuName,false);
}
ContextMenu.prototype.setup = function (){	
	for (var i=0;i < this.clickHandlers.length;i++){
		var clickHandler = this.clickHandlers[i];
		var itmeId = clickHandler.itemId;
		var itemHandler = clickHandler.handler;
		var HanlderWrapper = function(itemObj,itemHandler){
			this.obj = itemObj;
			this.invoke = function(){
				if (typeof(itemHandler)=='string'){
					this.obj.bind('click',function(){eval(itemHandler)});
				}else{
					this.obj.bind('click',itemHandler);	
				}
			}
		}
		var hanlderWrapperInst = new HanlderWrapper($("#"+this.menuName+"_item_"+itmeId),itemHandler);
		hanlderWrapperInst.invoke();
	}
	var curMenu = this;
	$(document).bind('click',function(){
		curMenu.onContextClick();
	});
	if (this.effectArea != null){
		$("#"+this.effectArea).bind("contextmenu",function(){return false;});
	}else{
		document.oncontextmenu=function(){return false;}
	}
}

ContextMenu.prototype.updateHandler = function(theItemId,newHandler){
	for (var i=0;i < this.clickHandlers.length;i++){
		var clickHandler = this.clickHandlers[i];
		var itmeId = clickHandler.itemId;
		if (theItemId == itmeId){
			$("#"+this.menuName+"_item_"+itmeId).unbind('click');
			$("#"+this.menuName+"_item_"+itmeId).bind('click',newHandler);
			break;
		}
	}
}

ContextMenu.mousePosition= function(ev){
	  if(ev.pageX || ev.pageY){
	  	return {x:ev.pageX, y:ev.pageY};
	  }
	  return {
		  x:ev.clientX + document.body.scrollLeft - document.body.clientLeft,
		  y:ev.clientY + document.body.scrollTop - document.body.clientTop
	  };
} 