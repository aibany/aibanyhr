
/**********************************************************************************
//  HotKeyListener
//  usage:
//  new HotKeyListener.CtrBack('formId',function(){
//	  	doSubmit('return '); //  your return function 
//  });
//  new HotKeyListener.CtrEnter('formId',function(){
//		doSubmit('submit');   // your submit function  
//	});
//
//  add the scripts on body's onload event
//  or  put them on the end of html 
//
//  note: it's depentds on prototype.js
***********************************************************************************/

var HotKeyListener = {}
//ctr+enter 
HotKeyListener.CtrEnter = function (formId,handler){
	$(formId).observe('keydown',function(evt){
		evt = evt || event;
		var key = evt.keyCode||evt.which||evt.charCode;
		if (evt.ctrlKey && key == 13){
			handler.apply(handler.arguments)
		}
	});
}
// ctr + back
HotKeyListener.CtrBack = function (formId,handler){
	$(formId).observe('keydown',function(evt){
		evt = evt || event;
		var key = evt.keyCode||evt.which||evt.charCode;
		if (evt.ctrlKey && key == 8){
			handler.apply(handler.arguments)
		}
	});
}

/**********************************************************************************
//EnterCursor end
***********************************************************************************/


/**********************************************************************************
//  EnterKeyListener start
//  usage:
//	new  EnterKeyListener('theFormId');
//
//  add the scripts on body's onload event
//  or  put them on the end of html 
***********************************************************************************/
function EnterKeyListener(formId,defaultFocusId){
	$(formId).observe('keydown',function(evt){
		var isIE = document.all ? true : false;
		var key;
		var srcobj;
		if (isIE){
			key = event.keyCode;
			srcobj=event.srcElement;
		}
		else{
			key = evt.which;
			srcobj=evt.target;
		} 

		if(key == 13 && srcobj.type != 'button' && srcobj.type != 'submit' 
			&& srcobj.type != 'reset' && srcobj.type != 'textarea' && srcobj.type != ''){
			if(isIE){
				event.keyCode=9;
			}
			else{                        
				var el=EnterKeyListener.getNextElement(evt.target);
				if (el.type != 'hidden'){
					EnterKeyListener.focusSelect(el);
				}
				else{
					while (el.type=='hidden'){
						el = EnterKeyListener.getNextElement(el);
					}
					EnterKeyListener.focusSelect(el);
				}
				return false;
			}
		}
	});
	if (defaultFocusId){
		var defObj = document.getElementById(defaultFocusId);
		EnterKeyListener.focusSelect(defObj);
	}else{
		EnterKeyListener.selectFirstElement(formId);	
	}
}
EnterKeyListener.focusSelect= function(el){
	if (el.tagName == 'SELECT'){
		el.focus();
	}else{
		el.select();
	}		
}
EnterKeyListener.getNextElement = function(field) {
	var form = field.form;
	for (var e = 0; e < form.elements.length; e++) {
		if (field == form.elements[e]){
			break;
		}
	}
	return form.elements[++e % form.elements.length];
}
EnterKeyListener.selectFirstElement = function(formId){
	var form = document.getElementById(formId);
	var el = form.elements[0];
	if (el.type != 'hidden' && el.type != 'button' 
		&& el.type != 'submit' && el.type != 'reset'){
		EnterKeyListener.focusSelect(el);
	}
	else{
		while (el.type == 'hidden' || el.type == 'button' 
			|| el.type == 'submit' || el.type == 'reset'){
			el= EnterKeyListener.getNextElement(el);
		}
		EnterKeyListener.focusSelect(el);
	}
}


/**********************************************************************************
//EnterKeyListener end
***********************************************************************************/


/**********************************************************************************
//  DirectKeyListener start
//  usage:
//  new DirectKeyListener('myTabe',1,1,0,0);
//
//  add the scripts on body's onload event
//  or  put them on the end of html 
***********************************************************************************/
function DirectKeyListener(targetTableId,topOffset,rightOffset,bottomOffset,leftOffset){
	this.dataTableId = targetTableId;
	this.dataTable = document.getElementById(targetTableId);	
	this.cursorPositionX = topOffset;
	this.cursorPositionY = leftOffset;
	
	this.targetLeftOffset = leftOffset;
	this.targetTopOffset = rightOffset;
	this.targetRightOffset = rightOffset;
	this.targetBottomOffset = bottomOffset;
	
	this.targetCellLength = 0;
		
	if (this.dataTable.rows.length > topOffset){
		this.targetCellLength = this.dataTable.rows[topOffset].cells.length;
	}else{
		this.targetCellLength = this.dataTable.rows[0].cells.length;
	}
		
	this.targetRowLength = this.dataTable.rows.length  - this.targetBottomOffset;
	this.targetCellLength = this.targetCellLength - this.targetRightOffset;
	
	var directKeyListener = this;
	var processEvent = function(inputObj){
		$(inputObj).observe('keydown',function(evt){
			DirectKeyListener.move(directKeyListener,evt);
		});
		$(inputObj).observe('focus',function(){
			DirectKeyListener.focus(directKeyListener,inputObj);
		});		
	}

	for (var i = topOffset;i < this.targetRowLength;i++){
		for (var j = leftOffset;j < this.targetCellLength;j++){
			var inputObj = this.dataTable.rows[i].cells[j].children[0];
			new processEvent(inputObj);
		}
	}
}
DirectKeyListener.move = function(directKeyListener,events) {	
    var keycode = events.charCode || events.keyCode;
    if ( keycode == 37 || keycode == 38 || keycode == 39 || keycode == 40) {
		var dataTable = directKeyListener.dataTable;
		var x = directKeyListener.cursorPositionX;
		var y = directKeyListener.cursorPositionY;
		var curObj = dataTable.rows[x].cells[y].children[0];
        if ( keycode == 37 ) {
			//go left
			if (curObj.tagName.toLowerCase() == 'select'){				
				if (directKeyListener.cursorPositionY > directKeyListener.targetLeftOffset) {
					directKeyListener.cursorPositionY--;
					dataTable.rows[directKeyListener.cursorPositionX].cells[directKeyListener.cursorPositionY].children[0].focus();
				}
			}
			else{
				var cursorPos = DirectKeyListener.getCursorPos(events);	
				if (cursorPos == 0){
					if (directKeyListener.cursorPositionY > directKeyListener.targetLeftOffset) {
						directKeyListener.cursorPositionY--;
						dataTable.rows[directKeyListener.cursorPositionX].cells[directKeyListener.cursorPositionY].children[0].focus();
					}
				}
			}
        }
        else if (keycode == 38 ) {
            //go top
			if (directKeyListener.cursorPositionX > directKeyListener.targetTopOffset) {
				if (dataTable.rows[directKeyListener.cursorPositionX-1].style.display == 'none'){
					return;
				}
                directKeyListener.cursorPositionX--;
				dataTable.rows[directKeyListener.cursorPositionX].cells[directKeyListener.cursorPositionY].children[0].focus();
            }            
        }
        else if (keycode == 39) {
		    //go right
			if (curObj.tagName.toLowerCase() == 'select'){
				if (directKeyListener.cursorPositionY < (directKeyListener.targetCellLength-1)) {
					directKeyListener.cursorPositionY++;
					dataTable.rows[directKeyListener.cursorPositionX].cells[directKeyListener.cursorPositionY].children[0].focus();
				}
			}
			else{				
				var cursorPos = DirectKeyListener.getCursorPos(events);	
				if (curObj.value.length <= cursorPos){
					if (directKeyListener.cursorPositionY < (directKeyListener.targetCellLength-1)) {
						directKeyListener.cursorPositionY++;
						dataTable.rows[directKeyListener.cursorPositionX].cells[directKeyListener.cursorPositionY].children[0].focus();
					}					
				}			
			}
        }
        else if (keycode == 40) {
            //go down
			if (directKeyListener.cursorPositionX < (directKeyListener.targetRowLength-1)) {
				if (dataTable.rows[directKeyListener.cursorPositionX+1].style.display == 'none'){
					return;
				}
                directKeyListener.cursorPositionX++;
				dataTable.rows[directKeyListener.cursorPositionX].cells[directKeyListener.cursorPositionY].children[0].focus();
            }
        }
    }
}

DirectKeyListener.focus = function (directKeyListener,obj){
	var tdObj = obj.parentElement;
	var trObj = tdObj.parentElement;
	directKeyListener.cursorPositionX = trObj.rowIndex;
	directKeyListener.cursorPositionY = tdObj.cellIndex;
}

DirectKeyListener.getCursorPos = function (event) {
	var pos = 0;
	var obj= event.target||event.srcElement;  
	if(document.selection){      
		if(obj.tagName!=undefined && obj.tagName=='INPUT'){    
			var  s=document.selection.createRange();     
			s.setEndPoint("StartToStart",obj.createTextRange());    
			pos=s.text.length;   
		}else{    
			var rng = obj.createTextRange();
			rng.moveToPoint(event.x,event.y);                  
			rng.moveStart("character",-obj.value.length);    
			pos=rng.text.length ;   
		}
	}else{     
		pos=obj.selectionStart;
	}
	return pos;
}
/**********************************************************************************
//
// DirectKeyListener end
//
***********************************************************************************/
