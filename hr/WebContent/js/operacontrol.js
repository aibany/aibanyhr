/*****************************************************
*  page action control start
*  usage:
*  put below script on the end of html  
*		pms.initPageActon('thePageId'); 
*	or  
* 	    pms.initPageActon('thePageId','theFormId'); 
*  if your formId is not "form".   
*  note: 
*  it depends prototype.js
*****************************************************/
function pms(){};
pms.initPageActon = function(params){
	if (params && params.handlerId && params.buttonIds){
		var controlType = "hiddenMode";
		var handlerId = params.handlerId;
		var buttonIds = params.buttonIds;
		if (params.controlType){
			controlType = params.controlType; 
		}
		var url = "index?Navigater&actionType=controlPage&controlType="+controlType;
		url = url + "&handlerId="+ handlerId+"&buttonIds="+buttonIds;
		sendRequest(url);		
	}
};
pms.controlPageActions = function (idPrefix,controlType){
	var inputNodes = $("input[id^='"+idPrefix+"']");
	if (inputNodes){
		for (var i=0;i < inputNodes.length;i++){
			pms.controlAction(inputNodes[i],controlType);
		}
	}
	var hrefNodes = $("a[id^='"+idPrefix+"']");
	if (hrefNodes){
		for (var i=0;i < hrefNodes.length;i++){
			pms.controlAction(hrefNodes[i],controlType);
		}
	}
	var imgNodes = $("img[id^='"+idPrefix+"']");
	if (imgNodes){
		for (var i=0;i < imgNodes.length;i++){
			pms.controlAction(imgNodes[i],controlType);
		}
	}
};
pms.controlAction=function(obj,controlType){
	if (controlType == "hiddenMode"){
		obj.hide();
	}else{
		obj.disable();
	}	
};
pms.controlPageAction = function (id,controlType){
	if (ele(id)){
		if (pms.isToolBarButton(id)){
			if (controlType == "hiddenMode"){
				$(ele(id).parentNode).hide();
			}else{
				disableButton(id);
			}		
		}else{
			if (controlType == "hiddenMode"){
				$('#'+id).hide();
			}else{
				ele(id).disabled = true;
			}
		}
	}
};
pms.isToolBarButton = function(id){
	var result = false;
	if (ele(id).parentNode){
		if (ele(id).parentNode.parentNode){
			if (ele(id).parentNode.parentNode.parentNode){	
				if (ele(id).parentNode.parentNode.parentNode.parentNode){
						if (ele(id).parentNode.parentNode.parentNode.parentNode.parentNode){
							if($(ele(id).parentNode.parentNode.parentNode.parentNode.parentNode).attr('id') == '__ToolBar__'){
							result = true;						
						}
					}	
				}
			}
		}
	}
	return result;
};
$(document.body).bind('keydown',function(evt){
	evt = evt || event;
	var code = evt.keyCode||evt.which||evt.charCode;
	var obj= event.target||event.srcElement;
	var type = obj.type;
	var flag = ((code != 8 && code != 13) || (type == 'text' && code != 13 && !obj.readOnly)
			|| (type == 'file' && code != 13 ) || (type == 'password' && code != 13 ) ||
			(type == 'textarea' && code != 13) || (type == 'submit' && code == 13));
	if (!flag){
		if ($.browser.msie){
			evt.returnValue=false;
		}
		else{
			evt.preventDefault();
		}
	}
	return flag;
});