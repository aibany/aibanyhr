var ECSideMessage={
	ENCODING		: "UTF-8",
	WAITTING_MSG	: "正在提交...",
	FILTERCLEAR_TEXT: "清除全部过滤条件",
	SORTASC_TEXT	: "升序排列",
	SORTDESC_TEXT	: "降序排列",
	SORTDEFAULT_TEXT: "清除排序状态",
	ERR_PAGENO		: "跳转页数只能是 1 至 #{1} 的整数!",
	EXPORT_CONFIRM	: "处理全部数据吗?\n\n( \"取消\" 为处理当前页面)",
	OVER_MAXEXPORT	: "数据总数超过了所允许的最大值( #{1} 条 ).",
	SHADOWROW_FAILED: "无法取得相关信息",
	NO_RECORD_UPDATE: "没有记录需要被更新!",
	UPDATE_CONFIRM	: "确定要执行保存操作吗?",
	NEARPAGE_TITLE	: ""
};


(function(){
	window.undefined = window.undefined;
	window.isIE=!!(window.attachEvent && !window.opera);
	if (window.isIE) {
	  window.attachEvent('onunload', $_IEGC);
	  /* window.attachEvent('onload', $_IEGC); */
	}
	if ($_E){
		$_E_original=$_E;
	}

	if (!window.isIE && window.Node){
		Node.prototype.swapNode=function(node){
			var nextSibling=this.nextSibling;
			var parentNode=this.parentNode;
			node.parentNode.replaceChild(this,node);
			parentNode.insertBefore(node,nextSibling);
		};
	}
	if (!window.isIE && window.HTMLElement){
		HTMLElement.prototype.__defineGetter__("innerText", function(){	return this.textContent;});
	}

})();

function $_IEGC(){
	CollectGarbage();
}


var ECSideConstants={
	EMPTY_FUNCTION : function(){},
	FORM_ID : "form1",
	EC_ID : "ec",
	ETI_ID : "eti",
	ETI_PAGE_FLAG : "eti_p",
	SORT_PREFIX : "_s_",
	FILTER_PREFIX : "_f_",
	ACTION : "a",
	FILTER_ACTION : "fa",
    CLEAR_ACTION  : "ca",

	PAGEFIELD_SUFFIX : "_p",
	EXPORT_IFRAME_SUFFIX : "_ecs_export_iframe",
	SHADOW_ROW : "_shadowRow",
	HIDE_HEADER_ROW : "_hideListRow",
	

	DEFALUT_ADD_TEMPLATE : "add_template",


	AJAX_ZONE_BEGIN : "_begin_ ",
	AJAX_ZONE_END : " _end_",
	AJAX_ZONE_PREFIX : "<!-- ECS_AJAX_ZONE_PREFIX_",
	AJAX_ZONE_SUFFIX : "_ECS_AJAX_ZONE_SUFFIX -->",
	MIN_COL_WIDTH : 10,
	SCROLLBAR_WIDTH :18,
	SCROLL_SPEED : 50,
	MIN_COLWIDTH : "30",
	AJAX_HEADER :['useAjaxPrep','true'],
	ROW_HIGHLIGHT_CLASS : "highlight",
	ROW_SELECTLIGHT_CLASS : "selectlight",
	DRAG_BUTTON_COLOR : "#3366ff",

	HEAD_ZONE_SUFFIX   : "_headZone",
	BODY_ZONE_SUFFIX   : "_bodyZone",
	FOOT_ZONE_SUFFIX	: "_footZone",

	LIST_HEIGHT_FIXED : window.isIE?0:0 ,
	LIST_WIDTH_FIXED : window.isIE?0:1 ,
	IE_WIDTH_FIX_A : 1,
	IE_WIDTH_FIX_B : 2,
	FF_WIDTH_FIX_A : -3,
	FF_WIDTH_FIX_B : -6,
	OFFSET_A		: 2
};



var $_E=function(){
  var elements = [];
  for (var i = 0; i < arguments.length; i++) {
    var element = arguments[i];
    if (typeof(element) == 'string') {
		var elemId=element;
		element = document.getElementById(elemId);
		if (element==null){
			element = document.getElementsByName(elemId);
			if (element.length>0){	
				element=element[0];	
			}else{
				element=null;
			}
		}
	}

    if (arguments.length == 1) {return element;}
    elements.push(element);
  }

  return elements;
};


var ECSideList={};

var ECSide=function(formid){
	var Me=this;
	Me.findAjaxZoneAtClient=false;
	Me.ETI_ID=ECSideConstants.ETI_ID;
	Me.ETI_PAGE_FLAG=ECSideConstants.ETI_PAGE_FLAG;

	Me.MIN_COL_WIDTH=80;

	Me.onLoad=null;

	if (!formid||formid==''){
		formid=ECSideConstants.FORM_ID;
	}
	Me.id=formid;
	Me.EXPORT_IFRAME_ID=ECSideConstants.EC_ID+ ECSideConstants.EXPORT_IFRAME_SUFFIX;
	Me.SHADOWROW_ID=ECSideConstants.EC_ID+ECSideConstants.SHADOW_ROW;

	Me.ECForm=null;
	Me.ECMainContent=null;
	Me.selectedRow=null;

	Me.sortedColumn=null;
	Me.sortType="default";
	Me.sortedColumnHearderId=null;

	Me.afterFillForm=null;

	//Me.resizeWay="resizeSibling";
	Me.resizeWay="resizeTable";

	Me.listWidth=0;
	Me.isClassic=false;
	Me.canResizeColWidth=false;


	Me.useAjax=true;
	Me.doPreload=true;
	Me.doPreloadPrev=false;

	Me.doPrep="temp";
	Me.doPrepPrev="temp";


	Me.isDebug=false;

	

	Me.prepState={next : 0 ,prev : 0};
	Me.prepPage={next : 0 ,prev : 0};
	Me.prepareaName={};
	Me.pageFieldName=ECSideConstants.EC_ID + ECSideConstants.PAGEFIELD_SUFFIX;

	Me.totalPagesFieldName=ECSideConstants.EC_ID+"_totalpages";

	Me.prepareaName['next']=ECSideConstants.EC_ID+"_ec_preparea_n";
	Me.prepareaName['prev']=ECSideConstants.EC_ID+"_ec_preparea_p";

	Me.scrollList=false;
	Me.orgListHeight=0;
	Me.listHeight=0;
	Me.minHeight=0;
	Me.columnNum=0;

	ECSideList[formid]=Me;

	Me.buildPrepArea=function(){
		if (!Me.doPreload){
			return;
		}

		var hasPrepareaNext=document.getElementById(this.prepareaName['next']);
		if (!hasPrepareaNext){
			var ta=document.createElement("textarea");
			ta.id=this.prepareaName['next'];
			ta.disabled=true;
			ta.style.display="none";
			document.body.appendChild(ta);
		}


		var hasPrepareaPrev=document.getElementById(this.prepareaName['prev']);
		if (!hasPrepareaPrev){
			var tb=document.createElement("textarea");
			tb.id=this.prepareaName['prev'];
			tb.disabled=true;
			tb.style.display="none";
			document.body.appendChild(tb);
		}

		/* for Debug */
		if(Me.isDebug){
			ta=document.getElementById(this.prepareaName['next']);
			tb=document.getElementById(this.prepareaName['prev']);
			ta.disabled=false;
			ta.style.display="inline";
			ta.rows=10;
			ta.cols=50;
			tb.disabled=false;
			tb.style.display="inline";
			tb.rows=10;
			tb.cols=50;
		}

	};

	Me.goPage=function(){
    	var newPageNO = $_E(Me.pageFieldName).value;
		
		var key=null;

		if(newPageNO==Me.prepPage['next'] && Me.prepState['next']==2){
			key='next';
		}else if(newPageNO== Me.prepPage['prev'] && Me.prepState['prev']==2 && Me.doPreloadPrev){
			key='prev';
		}
		
    	if (key!==null){
			try{
				var newhtml=$_E(Me.prepareaName[key]).value;
				if (newhtml==''){	$_E(Me.id).submit(); return;	}
				Me.ECMainContent.innerHTML=newhtml;
				Me.prepState[key]=0;
				Me.init();
				var originalRequest={};
				originalRequest.responseText=newhtml;
				if (Me.afterFillForm && typeof(Me.afterFillForm)=="function"){
					Me.afterFillForm(originalRequest);
				}
				window.setTimeout(Me.ajaxPrepSubmit,10);
				if (typeof(Me.onLoad)=="function"){
					Me.onLoad();
				}
				Me.handleResize();
				Me.hideWaitingBar();
			}catch(ex){
				$_E(Me.pageFieldName).value=newPageNO;
				Me.ajaxSubmit();
				/* $_E(Me.id).submit(); */
			}
    	}else{
	    	/* $_E(Me.id).submit(); */
			Me.ajaxSubmit();
    	}

 	};

	Me.dealResponse={
		'next'	: function(originalRequest){
			$_E(Me.prepareaName['next']).value =ECSideUtil.cutText(originalRequest.responseText,Me.id);
			Me.prepState['next']=2;
			Me.doingAjaxSubmit=false;

		},
		'prev'	: function(originalRequest){
			$_E(Me.prepareaName['prev']).value =ECSideUtil.cutText(originalRequest.responseText,Me.id);
			Me.prepState['prev']=2;
			Me.doingAjaxSubmit=false;
		}
	};
    

	Me.ajaxPrepSubmit=function(){
		if (!Me.doPreload){
			return;
		}
		Me.ajaxPrep(1);
		Me.ajaxPrep(-1);
	};

    Me.ajaxPrep=function(which){

		var key;

		if (which==1){
			key='next';
		}else if (which==-1 && Me.doPreloadPrev){
			key='prev';
		}else{
			return;
		}
		Me.prepState[key]=1;
		Me.prepPage[key]=$_E(Me.pageFieldName).value/1+which;
		if (Me.prepPage[key]<1 || Me.prepPage[key]>($_E(Me.totalPagesFieldName).value/1)) {
			 return;
		}
		$_E(Me.pageFieldName).value=Me.prepPage[key];


		Me.ajaxSubmit(Me.dealResponse[key],true);

		$_E(Me.pageFieldName).value=Me.prepPage[key]-which;
	};
	
 	

	Me.doingAjaxSubmit=false;
	Me.ajaxSubmit=function(resfunc,asy,parameter){
		if (!Me.useAjax){
			Me.ECForm.submit();
			return;
		}

		if (!asy){
			asy=false;
		}
		if (!resfunc){
			resfunc=Me.fillForm;
		}
		if(!asy && Me.doingAjaxSubmit){
			/*
			alert("the last ajax request is not complete. try later.");
			return;
			*/
		}
		Me.doingAjaxSubmit=true;

		/* fix a prototype bug */
		//pars=pars+'&'+appPara;
		//pars=pars.replace(/(^|&)_=(&|$)/g,'$1'+'$2');
		//pars=pars.replace(/&+/g,'&');
		/* end of fix a prototype bug */
		//alert(url+"  "+pars)
		//ECSideUtil.formSubmit(Me.ECForm,resfunc,"post",asy,parameter);
		if (Me.findAjaxZoneAtClient===false){
			if (!parameter){
				parameter={};
			}
			if (typeof(parameter)=='string'){
				parameter+="&findAjaxZoneAtClient=false&";
			}else{
				parameter.findAjaxZoneAtClient="false";
			}
			
		}
		Form.request(Me.ECForm,{method:'post',
			requestHeaders:ECSideConstants.AJAX_HEADER,
			asynchronous: asy,
			parameters: parameter,
			onComplete :resfunc });

	};



	Me.fillForm=function(originalRequest){

		var newhtml=ECSideUtil.cutText(originalRequest.responseText,Me.id);
		if (newhtml==''){
			Me.hideWaitingBar();
			return;	
		}
		if (newhtml.indexOf("Exception:")==0){
			Me.hideWaitingBar();
			Me.ECMainContent.innerHTML=newhtml;
			return;
		}

		Me.ECMainContent.innerHTML=newhtml;
		Me.init();

		if (Me.afterFillForm && typeof(Me.afterFillForm )=="function"){
			Me.afterFillForm(originalRequest);
		}
		
		Me.doingAjaxSubmit=false;

		Me.handleResize();
		Me.hideWaitingBar();


		/*
		Me.initList();
		if (typeof(Me.onLoad)=="function"){
			Me.onLoad();
		}
		*/
	};
	
	Me.currentShadowRowParent=null;
	Me.currentShadowEventSrc=null;
	Me.autoCloseOtherShadowRow=true;

	Me.getTotalPage=function(){
		return Me.ECForm[Me.totalPagesFieldName].value/1;
	};
	Me.getPageNo=function(){
		if (!Me.ECForm[Me.pageFieldName]){
			return 1;
		}
		return Me.ECForm[Me.pageFieldName].value/1;
	};
	Me.setPageNo=function(pageNo){
		Me.ECForm[Me.pageFieldName]=pageNo;
	};

	Me.showShadowRowCallBack=function(formid,crow,shadowRow,eventSrc){};
	Me.hideShadowRowCallBack=function(formid,crow,shadowRow,eventSrc){};
	Me.firstShowShadowRowCallBack=function(formid,crow,shadowRow,eventSrc){};

	Me.beforeFilter=function(formid){};


	Me.beforeSave=function(formid){};

	Me.beforeSubmit=function(formid,crow,shadowRow,eventSrc){};




 	Me.init=function(){
		Me.ECForm=document.getElementById(ECSideConstants.FORM_ID);
		
		if (!Me.ECForm)	{
			/* alert("ERR: tableId=\""+Me.id+"\" not exist!"); */
			return;
		}
		Me.doPreload=Me.doPrep=="temp"?Me.doPreload:Me.doPrep;
		Me.doPreloadPrev=Me.doPrepPrev=="temp"?Me.doPreloadPrev:Me.doPrepPrev;
		Me.DEFAULT_ACTION=Me.ECForm.getAttribute("action");
		Me.ECMainContent=document.getElementById(ECSideConstants.EC_ID+"_main_content");

		if (window.isIE){
			var hideHeader=document.getElementById(Me.id+ECSideConstants.HIDE_HEADER_ROW);
			if (hideHeader){
				hideHeader.style.display="none";
			}
		}
		if (window.frameElement && window.frameElement.name==Me.EXPORT_IFRAME_ID){
			Me.ECForm.style.visibility ="visible";
			ECSideUtil.printFrame(window.frameElement.contentWindow);
			return;
		}

		if (Me.sortedColumnHearderId){
			var sortedHeader=document.getElementById(Me.sortedColumnHearderId);
			if (sortedHeader && Me.sortType && Me.sortType!='' && Me.sortType!='default' ){
				var newHtml=ECSideUtil.trimString(sortedHeader.innerHTML,-1)+"&#160;<div class=\"sort"+Me.sortType.toUpperCase()+"\"></div>";
				sortedHeader.innerHTML=newHtml;
			}
		}
		
		if (!Me.useAjax){
			Me.doPreload=false;
		}

		if (typeof(Me.ajaxSubmit)!="function"){
			Me.useAjax=false;
			Me.ajaxSubmit=function(){
				Me.ECForm.submit();
			};
		}

		if (Me.useAjax){
			Me.buildPrepArea();
			Me.ajaxPrepSubmit();
		}

		Me.ECListHeadZone=document.getElementById(ECSideConstants.EC_ID+ ECSideConstants.HEAD_ZONE_SUFFIX);
		Me.ECListBodyZone=document.getElementById(ECSideConstants.EC_ID+ ECSideConstants.BODY_ZONE_SUFFIX);
		Me.ECListHead=document.getElementById(ECSideConstants.EC_ID+"_table_head");
		Me.ECListBody=document.getElementById(ECSideConstants.EC_ID+"_table_body");
		Me.ECListFoot=document.getElementById(ECSideConstants.EC_ID+"_table_foot");
		Me.ECListToolbarTable=document.getElementById(ECSideConstants.EC_ID+"_toolbarTable");
		Me.ECListToolbarShadow=document.getElementById(ECSideConstants.EC_ID+"_toolbarShadow");
		if (Me.ECListToolbarTable){
			Me.ECListToolbarShadow.style.height=Me.ECListToolbarTable.offsetHeight+ 2 +"px";
		}
		Me.orgListHeight=Me.ECListBody.scrollHeight;
		Me.initWaitingBar();				
		
		Me.initList();

		Me.columnHandler();
		Me.listWidth=Me.ECListHead.parentNode.clientWidth;
		Me.listHeight=Me.orgListHeight;	
		Me.ECForm.style.visibility ="visible";	
	};

Me.waitingBar=null;
Me.waitingBarCore=null;
Me.waitingShowTimes=0;

Me.initWaitingBar=function(){
	Me.waitingShowTimes=0;
	Me.waitingBar=document.getElementById(ECSideConstants.EC_ID+"_waitingBar");
	Me.waitingBar.setAttribute("big","false");

	Me.waitingBarCore=document.getElementById(ECSideConstants.EC_ID+"_waitingBarCore");

	Me.waitingBarCore.innerHTML=ECSideMessage.WAITTING_MSG;

	var wLeft=ECSideUtil.getPosLeft(Me.ECForm);
	var wTop=ECSideUtil.getPosTop(Me.ECForm);
	Me.waitingBar.style.left=wLeft+"px";
	Me.waitingBar.style.top=wTop+"px";
};

Me.resizeWaitinBar=function(){
	if (Me.waitingBar && Me.waitingBar.getAttribute("big")=="true"){
		var w=Me.ECForm.offsetWidth;
		var h=Me.ECForm.offsetHeight;
		var cw=Me.waitingBarCore.offsetWidth;
		var ch=Me.waitingBarCore.offsetHeight;
		Me.waitingBar.style.width=w +"px";
		Me.waitingBar.style.height=h-2 +"px";		
		Me.waitingBarCore.style.left= parseInt(Me.waitingBar.style.left)+(w-cw-50)/2 + "px";
		Me.waitingBarCore.style.top=parseInt(Me.waitingBar.style.top)+ (h-ch-40)/2 + "px";
	}
};


Me.showWaitingBar=function(){
	Me.waitingShowTimes++;
	Me.waitingBar.style.height="";
	Me.waitingBar.style.width="";
	Me.waitingBar.setAttribute("big","false");
	if (Me.ECForm[Me.id+"_rd"])	{
		Me.ECForm[Me.id+"_rd"].style.display="";
	}
	Me.waitingBarCore.style.left=Me.waitingBar.style.left;
	Me.waitingBarCore.style.top=Me.waitingBar.style.top;
	Me.waitingBar.style.display="block";
	Me.waitingBarCore.style.display="block";
};
Me.showBigWaitingBar=function(){
	Me.waitingShowTimes++;
	Me.waitingBar.setAttribute("big","true");
	Me.resizeWaitinBar();
	if (Me.ECForm[Me.id+"_rd"])	{
		Me.ECForm[Me.id+"_rd"].style.display="none";
	}
	Me.waitingBar.style.display="block";
	Me.waitingBarCore.style.display="block";
};

Me.hideWaitingBar=function(){
	Me.waitingShowTimes--;
	if (Me.waitingShowTimes<1){
		Me.waitingBar.setAttribute("big","false");
		Me.waitingBar.style.display="none";
		Me.waitingBarCore.style.display="none";
		Me.waitingShowTimes=0;
		if (Me.ECForm[Me.id+"_rd"])	{
			Me.ECForm[Me.id+"_rd"].style.display="";
		}
	}
};


	Me.autoFitHeight=function(){
		/*
		if ( Me.ECListBodyZone.offsetHeight>=Me.ECListBody.parentNode.scrollHeight ){
			var dh=Me.ECListBodyZone.offsetHeight-Me.ECListBodyZone.clientHeight+ECSideConstants.LIST_HEIGHT_FIXED;
			if (dh <=2 && Me.ECListBodyZone.offsetWidth-Me.ECListBodyZone.clientWidth>2){
				dh=ECSideConstants.SCROLLBAR_WIDTH;
			}
			var tHeight=Me.ECListBody.parentNode.scrollHeight+dh;
			tHeight=tHeight<Me.minHeight/1?Me.minHeight/1:tHeight;
			if (tHeight < 20){
				tHeight = 0;
			}
			Me.ECListBodyZone.style.height= tHeight+"px";
		}*/
	};

Me.columnHandler=function(){
		Me.MIN_COL_WIDTH=document.getElementById("minColWidth").value;
		//Me.ECForm.getAttribute("minColWidth");
		if (document.getElementById("canResizeColWidth")){			
			Me.canResizeColWidth=document.getElementById("canResizeColWidth").value;
			//Me.ECForm.getAttribute("canResizeColWidth");		
		}
		else{
			Me.canResizeColWidth = false;
		}
		if ( Me.canResizeColWidth=="true" || Me.canResizeColWidth===true){
			Me.canResizeColWidth=true;
			ECSideUtil.initSeparateLine();
			Me.ECListHead.parentNode.style.tableLayout="fixed";
			Me.ECListBody.parentNode.style.tableLayout="fixed";
			ECSideUtil.resizeInit();
		}else{
			Me.canResizeColWidth=false;
		}
		if (Me.ECListHead && Me.ECListHead.rows){
			var cells=Me.ECListHead.rows[0].cells;
			Me.columnNum=cells.length;
			for (var i=0;i<Me.columnNum;i++){
				if (cells[i].getAttribute("group")=="true"){
					ECSideUtil.groupByCol(Me.ECListBody.rows,i);
				}
			}
		}
}

	Me.handleScroll=function(){
		//Me.ECListHeadZone.scrollLeft=Me.ECListBodyZone.scrollLeft;
	};

	Me.resizeHeader=function(){
		/*
		var fixWidth=window.isIE?2:0;		
		Me.ECListHeadZone.style.width=Me.ECListBodyZone.clientWidth+ fixWidth +"px";
		Me.ECListHeadZone.style.width = Me.ECListBodyZone.clientWidth + "px";
		*/
	};


	Me.initList=function(){
		if (!Me.ECListHeadZone || !Me.ECListBodyZone)	{
			Me.isClassic=true;
		}

		if (!Me.isClassic){
			
			Me.resizeWay="resizeTable";
			ECSideUtil_addEvent(Me.ECListBodyZone,"scroll",Me.handleScroll);
			ECSideUtil_addEvent(window,"resize",Me.handleResize);
			if (window.isIE){
				Me.ECListHead.parentNode.style.tableLayout="fixed";
				ECSideUtil_addEvent(Me.ECListBodyZone,"resize",Me.handleResize);
			}else{
				//Me.ECListHead.parentNode.style.tableLayout="fixed";
				//Me.ECListBody.parentNode.style.tableLayout="fixed";
			}

			//Me.autoFitHeight();
			Me.handleResize();

			
			Me.orgListHeight=ECSideUtil.parseIntOrZero(Me.ECListBodyZone.style.height);
		}
	};




	Me.resized=0;	
	Me.autoFited = 0;
	Me.handleResize=function(){		
		Me.resizeWaitinBar();
		if (Me.isClassic || !Me.ECListBodyZone ||  !Me.ECListHeadZone){
			return;
		}
		
		//Fix IE bug
		if (window.isIE && Me.resized>0){
			Me.resizeHeader();
			Me.resized=0;
			return;
		}
		
		if (Me.autoFited == 0){
			Me.autoFitHeight();
			Me.autoFited = 1;
		}
		else{
			Me.autoFited = 0;
		}
		

		if (window.isIE && Me.ECListToolbarTable){
				if(Me.ECListBodyZone.offsetWidth +1 <Me.ECListToolbarTable.clientWidth){
					Me.ECListToolbarShadow.style.display="block";
					Me.ECListToolbarTable.parentNode.style.position="absolute";
				}else{
					Me.ECListToolbarShadow.style.display="none";
					Me.ECListToolbarTable.parentNode.style.position="static";
				}
		}

		//Fix IE bug
		//Me.resizeHeader();
		//ECSideUtil.syncRowsWidth(Me.ECListHead.rows,Me.ECListBody.rows);
		Me.resized=1;
	};

Me.updateCallBack=function(responseObj){
	var rs=ECSideUtil.responseHandler(responseObj);
	var tableId=ECSideUtil.trimString(rs[0]);
	var ecsideObj=ECSideUtil.getGridObj(tableId);
	for (var i=1;i<rs.length ;i+=3 ){
		var resultCode=ECSideUtil.trimString(rs[i]+"");
		if (resultCode=="END OF org.ecside.defaultAjaxResopnse"){
			break;
		}
		var recordKey=ECSideUtil.trimString(rs[i+1]+"");
		var message=ECSideUtil.trimString(rs[i+2]+"");
		if (resultCode=="1" || resultCode=="Success") {
			//Success
			ECSideUtil.getRemoveUpdatedClassRows(ecsideObj.forUpdateRows,recordKey);
		}else{
			//Fail
			if (message.length>1){
				alert(message);
			}
		}
	}
	ecsideObj.hideWaitingBar();
};

Me.insertCallBack=function(responseObj){
	var rs=ECSideUtil.responseHandler(responseObj);
	var tableId=ECSideUtil.trimString(rs[0]);
	var ecsideObj=ECSideUtil.getGridObj(tableId);
	for (var i=1;i<rs.length ;i+=3 ){
		var resultCode=ECSideUtil.trimString(rs[i]+"");
		if (resultCode=="END OF org.ecside.defaultAjaxResopnse"){
			break;
		}
		var recordKey=ECSideUtil.trimString(rs[i+1]+"");
		var message=ECSideUtil.trimString(rs[i+2]+"");
		if (resultCode=="1" || resultCode=="Success") {
			//Success
			ECSideUtil.getRemoveInsertedClassRows(ecsideObj.forInsertRows,recordKey);
		}else{
			//Fail
			if (message.length>1){
				alert(message);
			}
		}
	}
	ecsideObj.hideWaitingBar();
};

Me.deleteCallBack=function(responseObj){
	var rs=ECSideUtil.responseHandler(responseObj);
	var tableId=ECSideUtil.trimString(rs[0]);
	var ecsideObj=ECSideUtil.getGridObj(tableId);
	for (var i=1;i<rs.length ;i+=3 ){
		var resultCode=ECSideUtil.trimString(rs[i]+"");
		if (resultCode=="END OF org.ecside.defaultAjaxResopnse"){
			break;
		}
		var recordKey=ECSideUtil.trimString(rs[i+1]+"");
		var message=ECSideUtil.trimString(rs[i+2]+"");
		if (resultCode=="1" || resultCode=="Success") {
			//Success
			ECSideUtil.getRemoveDeletedRows(ecsideObj.forDeleteRows,recordKey);
		}else{
			//Fail
			if (message.length>1){
				alert(message);
			}
		}
	}
	ecsideObj.hideWaitingBar();
};


};    


var ECSideUtil={};

ECSideUtil.responseHandler=function(responseObj){
	var result=responseObj.responseText;
	result=ECSideUtil.trimString(result);
	var rs=result.split("\n");
	return rs;
};

ECSideUtil.syncRowsWidth=function(rows1,rows2){
	var IE_FIX=0;
	var FF_FIX=0;
	//IE_FIX=4;
	if (rows1 && rows2 && rows1.length>0 && rows2.length>0){
			var headTDs=rows1[0].cells;
			var bodyTDs=rows2[0].cells;
		if (window.isIE){
			/*
			for (var i=0;i<headTDs.length;i++ )	{
				headTDs[i].style.width= IE_FIX + bodyTDs[i].offsetWidth +"px";
			}
			*/
		}else{
			var colNum=headTDs.length;
			for (var i=0;i<colNum;i++ )	{
				var tt;
				if (bodyTDs[i].width && bodyTDs[i].width.length>0){
					tt=parseInt(bodyTDs[i].width);
				}else{
					tt=parseInt(bodyTDs[i].style.width);
				}
				if (isNaN(tt))	{
					continue;
				}
				//tt=bodyTDs[i].offsetWidth;
				var brw=0;
				brw=headTDs[i].getAttribute("resizeColWidth")=="true"?2:(i==0?0:(i+1==colNum?2:0));
				headTDs[i].style.width=  (tt+ FF_FIX+ brw )+  "px";	
				var dw2=bodyTDs[i].clientWidth-headTDs[i].clientWidth;
				var dw=bodyTDs[i].offsetWidth-headTDs[i].offsetWidth;
				if (headTDs[i].getAttribute("resizeColWidth")!="true" && dw!=0){
					headTDs[i].style.width =(tt+ FF_FIX+ brw ) +dw+"px";
				}
				
			}

		}

	}
}

ECSideUtil.resizeAllGird=function(){
	for (var girdId in ECSideList ){
		var gird=ECSideList[girdId];
		gird.handleResize();
	}
};



ECSideUtil.getGridObj=function(formid){
	if (!formid){
		formid=ECSideConstants.FORM_ID;
	}
	return ECSideList[formid];
};

ECSideUtil.getECSideForm=function(formid){
	var ecsideObj=ECSideUtil.getGridObj(formid);
	if (ecsideObj){
		return ecsideObj.ECForm;
	}
	return null;
};




ECSideUtil.getMessage=function(name, msgs){
var msgTemplate=ECSideMessage[name];
	for (var i=1;i<arguments.length ;i++ ){
		msgTemplate=ECSideUtil.replaceAll(msgTemplate,"#{"+i+"}",arguments[i]);
	}
	return msgTemplate;
};

ECSideUtil.getTotalPages=function(formid){
	var form=ECSideUtil.getECSideForm(formid);
	try{
		return form[ECSideConstants.EC_ID+"_totalpages"].value;
	}catch(e){
		return -1;
	}
};

ECSideUtil.getTotalRows=function(formid){
	var form=ECSideUtil.getECSideForm(formid);
	try{
		return form[ECSideConstants.EC_ID+"_totalrows"].value;
	}catch(e){
		return -1;
	}
};

ECSideUtil.clearTotalRows=function(formid){
	try{
		var form=ECSideUtil.getECSideForm(formid);
		form[ECSideConstants.EC_ID+"_totalrows"].value="";
	}catch(e){
	}
};

ECSideUtil.cutText=function(text,formid){
		var ecsideObj=ECSideUtil.getGridObj(formid);
		if (text.responseText){
			text=text.responseText;
		}
		if (ecsideObj && !ecsideObj.findAjaxZoneAtClient) {
			return text;
		}

		var begin=ECSideConstants.AJAX_ZONE_PREFIX+ECSideConstants.AJAX_ZONE_BEGIN+formid +ECSideConstants.AJAX_ZONE_SUFFIX;
		var end=ECSideConstants.AJAX_ZONE_PREFIX+ECSideConstants.AJAX_ZONE_END+formid +ECSideConstants.AJAX_ZONE_SUFFIX;

        var p1 = text.indexOf(begin);
        if (p1 != -1) {
            p1+=begin.length;
            var p2 = text.indexOf(end, p1);
            if (p2!=-1){
                return text.substring(p1, p2);
            }
        }
		return text;
	};

ECSideUtil.noExport=function(formid){
	var ecsideObj=ECSideUtil.getGridObj(formid);
	var etiid;
	var form;
	if (!ecsideObj)	{
		etiid=ECSideConstants.ETI_ID;
		form=document.getElementById(formid);
	}else{
		etiid=ecsideObj.ETI_ID;
		form=ecsideObj.ECForm;
	}

	try{
		form[etiid].value="";
	}catch(e){
	}
	
};



ECSideUtil.refresh=function(formid){
	var ecsideObj=ECSideUtil.getGridObj(formid);
	var form;
	if (!ecsideObj)	{
		form=document.getElementById(formid);
	}else{
		form=ecsideObj.ECForm;
	}
	try{
    	form[ECSideConstants.EC_ID+"_totalrows"].value="";
	}catch(e){
	}
};

ECSideUtil.reload=function(formid,pageno){
	ECSideUtil.noExport(formid);
	ECSideUtil.refresh(formid);
	var ecsideObj=ECSideUtil.getGridObj(formid);
	if (!pageno){
		pageno=ecsideObj.getPageNo();
	}
	ECSideUtil.gotoPage(pageno,formid);

	ECSideUtil.showShadowRow(ecsideObj.currentShadowRowParent,ecsideObj.currentShadowEventSrc,formid);
};


ECSideUtil.gotoPage=function(pageno,formid){


var ecsideObj=ECSideUtil.getGridObj(formid);

ecsideObj.showWaitingBar();

	try{
		ECSideUtil.NearPagesBar.doHideMe();
	}catch (e3){}

	var pageid=ecsideObj.pageFieldName;
	var form=ecsideObj.ECForm;

if (!form[pageid]){
	var hiddenpageid=document.createElement("input");
	hiddenpageid.id=pageid;
	hiddenpageid.style.display="none";
	form.appendChild(hiddenpageid);
}

	form[pageid].value=pageno;
	ECSideUtil.noExport(formid);
	form.action=ecsideObj.DEFAULT_ACTION;

	try {
		if (ecsideObj.doPreload){
			ecsideObj.goPage();
		}else {
			ecsideObj.ajaxSubmit();
		}
	}catch (e){
		try {
			ecsideObj.ajaxSubmit();
		}catch (e2){
			form.submit();
		}
	}


};


ECSideUtil.gotoPageByInput=function(inputNoObj,formid){

	var form=ECSideList[formid].ECForm;
	var tempInput=null;
	if (inputNoObj.type!="text"){
		tempInput=inputNoObj.nextSibling;
		if (tempInput.type!="text"){
			tempInput=inputNoObj.previousSibling;
		}
		inputNoObj=tempInput;
	}

	var pageno=inputNoObj.value/1;
	var totalpages=form[ECSideConstants.EC_ID+"_totalpages"].value/1;
	if (!isFinite(pageno) || (pageno+"").indexOf(".")!=-1 || pageno<1 || pageno>totalpages){
		alert(ECSideUtil.getMessage("ERR_PAGENO",totalpages));
		inputNoObj.focus();
		inputNoObj.select();
		return;
	}
	if (pageno<1){
		pageno=1;
	}
	ECSideUtil.gotoPage(pageno,formid);
};




ECSideUtil.doSort=function(event,columnAlias,sortT1,formid,columnHearderId){
	
	var e = event||window.event;

	if ( ECSideUtil.startDragobj==true || ECSideUtil.Dragobj){
		return;
	}

	if (window.isIE && e.button>1) {
		return;
	}

	var asc="asc";
	var desc="desc";
	var defaultOrder="default";

	var ecsideObj=ECSideUtil.getGridObj(formid);

	var pageid=ecsideObj.pageFieldName;

	var form=ecsideObj.ECForm;

	if ( typeof(sortT1)!='string'){
		columnHearderId=sortT1.id;
		sortT1=null;
	}else if(columnHearderId && typeof(columnHearderId)!='string'){
		columnHearderId=columnHearderId.id;
	}


	var tOrder="default";

	if (sortT1){
		tOrder=sortT1;
	}else if (ecsideObj.sortedColumn==columnAlias){
		if (!ecsideObj.sortType || ecsideObj.sortType=="default"){
			tOrder="asc";
		}else if (ecsideObj.sortType=="asc"){
			tOrder="desc";
		}else if (ecsideObj.sortType=="desc"){
			tOrder="default";
		}else{
			tOrder="asc";
		}
	}else{
		tOrder="asc";
	}


	ECSideUtil.noExport(formid);
	var oAction=form.action;
	form.action=ecsideObj.DEFAULT_ACTION;

	if (ecsideObj.sortedColumn && ecsideObj.sortedColumn!=''){
		form[ECSideConstants.EC_ID+ECSideConstants.SORT_PREFIX+ecsideObj.sortedColumn].value="";
	}


	ecsideObj.sortedColumn=columnAlias;
	ecsideObj.sortType=tOrder;
	ecsideObj.sortedColumnHearderId=columnHearderId;


	if (ecsideObj.custSort){
		ecsideObj.custSort(columnAlias,tOrder);
	}else{
		form[ECSideConstants.EC_ID+ECSideConstants.SORT_PREFIX+columnAlias].value=tOrder;

	}
	try {
		ecsideObj.ajaxSubmit();
		form.action=oAction;
	}catch (e2){
		form.submit();
	}

	ECSideUtil.ColmunMenu.doHideMe();

};


ECSideUtil.doCustomExport=function(fileName,exportAction,formid){
	var form=ECSideUtil.getECSideForm(formid);
	var otarget=form.target;
	var oaction=form.action;

	form[ECSideConstants.EC_ID+"_efn"].value=fileName;
	form.action=exportAction;

		var targetFrame=ECSideList[formid].EXPORT_IFRAME_ID;
		targetFrame=document.getElementById(targetFrame);
		if (targetFrame){
			form.target=ECSideList[formid].EXPORT_IFRAME_ID;
		}
		
	form.submit();

	form.target= otarget;
	form.action=oaction;

	ECSideUtil.noExport(formid);

};

ECSideUtil.doExportList=function(fileName,page,formid){
	var type="xls";
	ECSideUtil.doExport(type,fileName,page,formid);
};

ECSideUtil.doExport=function(type,fileName,page,formid){

	/* for compatibility */
	if (arguments.length>4){
		type=arguments[4];
		fileName=arguments[5];
	}

	
	var ecsideObj=ECSideUtil.getGridObj(formid);

	var etiid=ecsideObj.ETI_ID;
	var etip=ecsideObj.ETI_PAGE_FLAG
	var form=ecsideObj.ECForm;

	if (page==true){
		page=true;
	}else{
		page=false;
	}

	page=!confirm(ECSideMessage.EXPORT_CONFIRM);

	var maxRowsExported = document.getElementById("maxRowsExported").value;
	//form.getAttribute("maxRowsExported");
	if (page===false && maxRowsExported && ECSideUtil.parseIntOrZero(maxRowsExported)>0){
		if(ECSideUtil.parseIntOrZero(maxRowsExported)<ECSideUtil.getTotalRows(formid)){
			alert(ECSideUtil.getMessage("OVER_MAXEXPORT",ECSideUtil.parseIntOrZero(maxRowsExported)));
			return;
		}
	}

	form[ECSideConstants.EC_ID+"_ev"].value=type;
	form[ECSideConstants.EC_ID+"_efn"].value=fileName;
	form[etiid].value=ECSideConstants.EC_ID;

	if (page===true){
		form[etip].value="true";
	}else{
		form[etip].value="";
	}


	var otarget=form.target;
	form.action=ecsideObj.DEFAULT_ACTION;

	var targetFrame=ecsideObj.EXPORT_IFRAME_ID;
	targetFrame=document.getElementById(targetFrame);
	if (targetFrame){
		form.target=ecsideObj.EXPORT_IFRAME_ID;
	}
		
	form.submit();
	form.target= otarget;
	ECSideUtil.noExport(formid);

};


ECSideUtil.changeRowsDisplayed=function(formid,selectObj){

var ecsideObj=ECSideUtil.getGridObj(formid);

	var pageid=ecsideObj.pageFieldName;

	var form=ecsideObj.ECForm;
	form[ECSideConstants.EC_ID+"_crd"].value=selectObj.options[selectObj.selectedIndex].value;
	form[pageid].value='1';

	ECSideUtil.noExport(formid);
	form.action=ecsideObj.DEFAULT_ACTION;
	try {
		ecsideObj.ajaxSubmit();
	}catch (e2){
		form.submit();
	}
};

ECSideUtil.checkAll=function(checkcontrolObj,checkboxname,formid){

	var form=ECSideList[formid].ECForm;
	if (!form.elements[checkboxname]){ return;}
	var checked=false;
	if (checkcontrolObj.className=="checkedboxHeader"){
		checked=true;
		checkcontrolObj.className="checkboxHeader";
	}else{
		checkcontrolObj.className="checkedboxHeader";
	}
	if (!form.elements[checkboxname].length){ 
		if (!form.elements[checkboxname].disabled){
			form.elements[checkboxname].checked = !checked;
		}
		return;
	}
	for(i = 0; i < form.elements[checkboxname].length; i++) {
		if (!form.elements[checkboxname][i].disabled){
			form.elements[checkboxname][i].checked = !checked;
		}
	}
};


ECSideUtil.selectRow=function(rowObj,formid){
	var selectlightClassName=ECSideConstants.ROW_SELECTLIGHT_CLASS;
	var ecsideObj=ECSideUtil.getGridObj(formid);
	if (!ecsideObj || rowObj==ecsideObj.selectedRow){ return;}
	ECSideUtil.addClass(rowObj,selectlightClassName);
	ECSideUtil.removeClass(ecsideObj.selectedRow,selectlightClassName);
	ecsideObj.selectedRow=rowObj;
};

ECSideUtil.lightRow=function(rowObj){
	
	ECSideUtil.addClass(rowObj,ECSideConstants.ROW_HIGHLIGHT_CLASS);
};

ECSideUtil.unlightRow=function(rowObj){
	ECSideUtil.removeClass(rowObj,ECSideConstants.ROW_HIGHLIGHT_CLASS);
};

ECSideUtil.lightHeader=function(tdObj){
	
	var className=tdObj.className;
	if (className){
		className=className.split(" ");
		className[0]+="Over";
	}
	tdObj.className=className.join(" ");

};

ECSideUtil.unlightHeader=function(tdObj){
	
	var className=tdObj.className;
	if (className){
		className=className.split(" ");
		if (className[0].lastIndexOf("Over")==className[0].length-"Over".length){
			className[0]=className[0].substring(0,className[0].length-"Over".length);
		}
	}
	tdObj.className=className.join(" ");
};



ECSideUtil.getFirstChildElement=function(node){
	var nodeIdx=0;
	try{
		var nodeT=-1;
		while(nodeT!=1 && nodeIdx<node.childNodes.length){
			nodeT=node.childNodes[nodeIdx].nodeType;
			nodeIdx++;
		}
		nodeIdx--;
		return node.childNodes[nodeIdx];
	}catch(e){
		return node.childNodes[0];
	}
};

ECSideUtil.getNextElement=function(node){
	if (!node){
		return null;
	}
	var tnode=node.nextSibling;
	while ( tnode!=null ){
		if (tnode.nodeType==1) {
			return tnode;
		}
		tnode=tnode.nextSibling;
	}
	return null;
};



ECSideUtil.getShadowRow=function(crow,formid){
		
		var ecsideObj=ECSideUtil.getGridObj(formid);

		var hasShadow=crow.getAttribute("hasShadow");
		var shadowRow=null;
		if (hasShadow=="true"){
			var crowIndex=crow.rowIndex;
			if (ecsideObj.scrollList){
				crowIndex++;
			}
			shadowRow=crow.parentNode.rows[crowIndex];
		}
		return shadowRow;
};



ECSideUtil.ajaxRequest=function(){

};


ECSideUtil.operateECForm=function(actionUrl,resFunc,parameter,asy,formid){   


	var ecsideObj=ECSideUtil.getGridObj(formid);
	ecsideObj.showWaitingBar();
	var pageid=ecsideObj.pageFieldName;
	var form=ecsideObj.ECForm;

	if (!asy){
		asy=true;
	}
	var appPara='';
	if (parameter){
		if (typeof(parameter)=='string'){
			appPara=parameter;
		}else{
			for (var k in parameter){
				if (!form[k]){
					appPara=appPara+k+'='+parameter[k]+'&';
				}else{
					form[k].value=parameter[k];
				}
			}
		}
	}


	ECSideUtil.refresh(formid);
	form[pageid].value=1;
	ECSideUtil.noExport(formid);
	form.action=actionUrl;
	try {
		ecsideObj.ajaxSubmit(resFunc,asy,appPara);
	}catch (e){
		form.submit();
	}
	try {
		form.action=ecsideObj.DEFAULT_ACTION;
	}catch (e){
	}
};   

ECSideUtil.queryECForm=function(formid,parameter,asy){

	if (asy==null || asy==window.undefined){
		asy=true;
	}

	var ecsideObj=ECSideUtil.getGridObj(formid);
	ecsideObj.showWaitingBar();
	var pageid=ecsideObj.pageFieldName;
	var form=ecsideObj.ECForm;
	ECSideUtil.refresh(formid);
	form[pageid].value=1;
	ECSideUtil.noExport(formid);

	ecsideObj.ajaxSubmit(ecsideObj.fillForm,asy,parameter)


};



ECSideUtil.printFrame=function(frame, doctitle,onfinish) {

	if ( !frame ) { frame = window; }
	if ( !doctitle ) {
		doctitle="";
	}
		
	frame.document.title=doctitle;

	  function execOnFinish() {
		switch ( typeof(onfinish) ) {
		  case "string": execScript(onfinish); break;
		  case "function": onfinish();
		}
		if ( focused && !focused.disabled ) { 
			focused.focus(); 
		}
		frame.location="about:blank";
	  }

  if ( frame.document.readyState !== "complete" &&
       !confirm("The document to print is not downloaded yet! Continue with printing?") ) {
		execOnFinish();
		return;
  }

  if (frame.print ) { // IE5+
    var focused = document.activeElement; 
    frame.focus();
	frame.print();
    execOnFinish();
    return;
  }else{
	alert("the PRINT for IE 5.0+ Only");
  }

};


/*============ UTILS ============*/



function ECSideUtil_addEvent( obj, type, fn ) {  
  if (obj.attachEvent) {  
    obj['e'+type+fn] = fn;
    obj[type+fn] = function(){obj['e'+type+fn]( window.event );};	
    obj.attachEvent( 'on'+type, obj[type+fn] );  
  }else if(obj.addEventListener){
    obj.addEventListener( type, fn, false );  
  }
}
function ECSideUtil_stopEvent(e) {  
	if (e.stopPropagation){
		e.stopPropagation();
		e.preventDefault();
	} else {
		e.returnValue = false;
		e.cancelBubble = true;
	}
	return false;
}

function ECSideUtil_removeEvent( obj, type, fn ) {  
  if ( obj.detachEvent ) {  
    obj.detachEvent( 'on'+type, obj[type+fn] );  
    obj[type+fn] = null;  
    obj['e'+type+fn] = null;
  }else if(obj.removeEventListener){
    obj.removeEventListener( type, fn, false ); 
  }
}

ECSideUtil.trimString=function(str, wh){
		if(!str.replace){ return str; }
		if(!str.length){ return str; }
		var re = (wh > 0) ? (/^\s+/) : (wh < 0) ? (/\s+$/) : (/^\s+|\s+$/g);
		return str.replace(re, "");
};


ECSideUtil.getPosTop=function(elm) {
	var top = elm.offsetTop;
	while((elm = elm.offsetParent) != null)	{
		top += (elm.offsetTop-elm.scrollTop);
	}
	return top;
}

ECSideUtil.getPosLeft=function(elm) {
	var left = elm.offsetLeft;
	while((elm = elm.offsetParent) != null)	{
		left += (elm.offsetLeft-elm.scrollLeft);
	}
	return left;
};

ECSideUtil.getPosRight=function(elm){
	return ECSideUtil.getPosLeft(elm)+elm.offsetWidth;
};
ECSideUtil.getPosBottom=function(elm){
	return ECSideUtil.getPosTop(elm)+elm.offsetHeight;
};

ECSideUtil.replaceAll=function(exstr,ov,value){
	var gc=ECSideUtil.escapeRegExp(ov);
	if (gc==null || gc==''){
		return exstr;
	}
	var reReplaceGene="/"+gc+"/gm";
	var r=null;
	var cmd="r=exstr.replace("+reReplaceGene+","+ECSideUtil.escapeString(value)+")";
	eval(cmd);
	return r;
};

ECSideUtil.escapeRegExp=function(str) {
	return !str?''+str:(''+str).replace(/\\/gm, "\\\\").replace(/([\f\b\n\t\r[\^$|?*+(){}])/gm, "\\$1");
};

ECSideUtil.escapeString=function(str){ 

	return !str?''+str:('"' + (''+str).replace(/(["\\])/g, '\\$1') + '"'
		).replace(/[\f]/g, "\\f"
		).replace(/[\b]/g, "\\b"
		).replace(/[\n]/g, "\\n"
		).replace(/[\t]/g, "\\t"
		).replace(/[\r]/g, "\\r");
};


ECSideUtil.hasClass=function(object, className) {
	if (!object.className) { return false;}
	return (object.className.search('(^|\\s)' + className + '(\\s|$)') != -1);
};

ECSideUtil.removeClass=function(object,className) {
	if (!object) {return;}
	object.className = object.className.replace(new RegExp('(^|\\s)'+className+'(\\s|$)'), ' ');
};

ECSideUtil.addClass=function(object,className) {
	if (!object || ECSideUtil.hasClass(object, className)){return;}
	if (object.className) {
		object.className += ' '+className;
	} else {
		object.className = className;
	}
};


ECSideUtil.parseIntOrZero=function(num){
	return ECSideUtil.parseInt(num,0);
};
ECSideUtil.parseIntOrOne=function(num){
	return ECSideUtil.parseInt(num,1);
};
ECSideUtil.parseInt=function(num,defaultNum){
	var t=parseInt(num);
	return isNaN(t)?defaultNum:t;
};

ECSideUtil.isCollection=function(obj){
	return obj && typeof(obj) != 'string' && typeof(obj.length) == 'number';

};


ECSideUtil.appendMap=function(destination,source){
  for (var property in source) {
	  if (property in destination) {
		  if (destination[property].constructor != Array) destination[property] = [destination[property]];
          destination[property].push(source[property]);
	  }else{
		destination[property] = source[property];
	  }
  }
};



ECSideUtil.groupByCol=function(rows,colNo,startRowNo,endRowNo){
		
		if (!colNo){
			colNo=0;
		}
		if (!startRowNo || startRowNo<0){
			startRowNo=0;
		}
		if (!endRowNo||endRowNo>rows.length){
			endRowNo=rows.length;
		}
		var show="";
		var hide="none";
		var cell=null;

		var info=[];
		var startCell=rows[startRowNo].cells[colNo];
		var startRowSpan=startCell.rowSpan;

		for (var i=startRowNo+1;i<endRowNo ;i++ ){
			cell=rows[i].cells[colNo];

			if (cell.style.display==hide){
				startRowSpan+=cell.rowSpan;
			}else{
			//info.push(i+","+startRowSpan);
				startCell.rowSpan =startRowSpan;
				startCell=cell;
				startRowSpan=cell.rowSpan;
			}
		}
		//info.push(i+","+startRowSpan);
		startCell.rowSpan =startRowSpan;
		return info;
	};

ECSideUtil.ungroupByCol=function(rows,colNo,startRowNo,endRowNo){
		if (!colNo){
			colNo=0;
		}
		if (!startRowNo || startRowNo<0){
			startRowNo=0;
		}
		if (!endRowNo||endRowNo>rows.length){
			endRowNo=rows.length;
		}

		var show="";
		var hide="none";
		var cell=null;
		for (var i=startRowNo;i<endRowNo ;i++ ){
			cell=rows[i].cells[colNo];
			if (cell.style.display==hide){
				cell.style.display=show;
			}else if (cell.rowSpan>1){
				cell.rowSpan=1;
			}
		}
};





/* ===========LIST SCROLL ============= */





ECSideUtil.initSeparateLine=function(){
	var temp=document.getElementById("separateLine");
	if (!temp){
		ECSideUtil.separateLine=document.createElement("DIV");
		ECSideUtil.separateLine.id="separateLine";
		ECSideUtil.separateLine.className="separateLine";
		ECSideUtil.separateLine.style.display="none";
		document.body.appendChild(ECSideUtil.separateLine);
	}
};

/* =========== MENU ============= */
var ECSPopup=function(eid){
	var Me=this;
	Me.id=eid;
	Me.currentContent=null;

	Me.hideTimeout1=500;
	Me.hideTimeout2=200;

	Me.coreElement=null;

	Me.isShow=false;
	Me.setShow=function(){
		Me.isShow=true;
	};

	Me.setHide=function(){
		Me.isShow=false;
	};

	Me.initMe=function(){
		/*  TODO */
	};
	Me.showMe=function(){
		/*  TODO */
		Me.setShow();
	};

	Me.hideMe=function(){
			window.setTimeout(Me.tryHideMe, Me.hideTimeout1);
			Me.setHide();
	};

	Me.tryHideMe=function(){
		if (!Me.isShow){
			Me.doHideMe();
		}
	};

	Me.doHideMe=function(){
		Me.coreElement.style.display="none";
		Me.setHide();
	};
};

/*

<a href="#" onclick="ECSideUtil.gotoPage('ec',2);return false;" >2</a>

*/

ECSideUtil.NearPagesBar=new ECSPopup("nearPagesBar");
ECSideUtil.NearPagesBar.initMe=function(){
	var temp=document.getElementById("nearPagesBar");
	if (!temp){
		this.coreElement=document.createElement("DIV");
		this.coreElement.id=this.id;
		this.coreElement.className = this.id ;
		this.coreElement.style.display="none";
		document.body.appendChild(this.coreElement);
		ECSideUtil_addEvent(this.coreElement,"mouseover",this.setShow );
		ECSideUtil_addEvent(this.coreElement,"mouseout",this.hideMe);
		this.currentContent="formid : nearPages";
	}
};

ECSideUtil.NearPagesBar.createNearPagesList=function(nearPages,formid){
	var listHtml= ECSideMessage.NEARPAGE_TITLE;
	var ecsideObj=ECSideUtil.getGridObj(formid);
	var page=ecsideObj.getPageNo();
	var lastPage=ecsideObj.getTotalPage();
	var pi1=0;
	var startP=page- nearPages;
	var endP=page+ nearPages;
	if ( startP<1){
			endP=endP+(1-startP);
			startP=1;
	}
	if ( endP>lastPage){
		startP=startP-(endP-lastPage);
		endP=lastPage;
	}
	startP=startP<1?1:startP;
	 listHtml+="<nobr>";
  	for (pi1=startP;pi1<=endP;pi1++){
	  		if (pi1==page){
	    			listHtml+=" <b>"+pi1+"</b> ";
	    		}else{
	    			listHtml+="<a href=\"#\" onclick=\"ECSideUtil.gotoPage("+pi1+",'"+formid+"');return false;\" >"+pi1+"</a>";
	    		}
	 }
	 listHtml+="</nobr>";
	return listHtml;
}

ECSideUtil.NearPagesBar.showMe=function(fireObj,formid){
	var ecsideObj=ECSideUtil.getGridObj(formid);
	var nearPages=document.getElementById("nearPages").value;
	//ECSideUtil.parseIntOrZero(ecsideObj.ECForm.getAttribute("nearPages"));
	if (nearPages<2){ return ; }
	var page=ecsideObj.getPageNo();
	this.currentContent=formid+" : "+page;
	this.coreElement.innerHTML=this.createNearPagesList(nearPages,formid);
	this.setShow();
	this.coreElement.style.display="block";
	var dx=(this.coreElement.offsetWidth-fireObj.offsetWidth)/2;
	this.coreElement.style.left=ECSideUtil.getPosLeft(fireObj)-dx+document.body.scrollLeft+"px";
	this.coreElement.style.top= (ECSideUtil.getPosTop(fireObj)- this.coreElement.offsetHeight+document.body.scrollTop) +"px";
};


////////////////////////////////////////////

ECSideUtil.doFilterFocus=function(event,filterInputObj){
	var e = event||window.event;
	filterInputObj.select();
	ECSideUtil_stopEvent(e);

}

ECSideUtil.doFilter=function(event,filterInputObj,filterFieldName,formid){
	var e = event||window.event;
	if(filterInputObj.type=="button"){
		filterInputObj=filterInputObj.nextSibling;
	}else if (event.keyCode != 13){
		return;
	}
	ECSideUtil.doFilterCore(filterInputObj,filterFieldName,ECSideConstants.FILTER_ACTION,formid);
};
ECSideUtil.doClearFilter=function(event,filterInputObj,filterFieldName,formid){
	ECSideUtil.doFilterCore(filterInputObj,filterFieldName,ECSideConstants.CLEAR_ACTION,formid);
};

ECSideUtil.doFilterCore=function(filterInputObj,filterFieldName,faction,formid){

	var ecsideObj=ECSideUtil.getGridObj(formid);

	var pageid=ecsideObj.pageFieldName;

	var form=ecsideObj.ECForm;
	
	var continueFunction=true;
	if (ecsideObj.beforeFilter){
		continueFunction=ecsideObj.beforeFilter(formid);
	}
	if (continueFunction===false){
		return;
	}


	form[ECSideConstants.EC_ID+ECSideConstants.FILTER_PREFIX+ECSideConstants.ACTION].value=faction;

	form[pageid].value=1;
	ECSideUtil.clearTotalRows(formid);
	ECSideUtil.noExport(formid);
	var oAction=form.action;
	form.action=ecsideObj.DEFAULT_ACTION;

if (filterInputObj!=null){
	form[filterFieldName].value=filterInputObj.value;
}

	try {
		ecsideObj.ajaxSubmit();
		form.action=oAction;
	}catch (e2){
		form.submit();
	}
		ECSideUtil.ColmunMenu.doHideMe();
}

ECSideUtil.showColmunMenu=function(event,columnObj,formid){
	var e = event||window.event;
	ECSideUtil.ColmunMenu.showMe(columnObj,formid);
	return ECSideUtil_stopEvent(e);
}

ECSideUtil.ColmunMenu=new ECSPopup("columnMenu");
ECSideUtil.ColmunMenu.initMe=function(){
	var temp=document.getElementById("columnMenu");
	if (!temp){
		this.coreElement=document.createElement("table");
		this.coreElement.id=this.id;
		this.coreElement.className = this.id ;
		this.coreElement.style.display="none";
		this.coreElement.style.left="0px";
		this.coreElement.style.top="0px";
		var ntbody=document.createElement("tbody");
		var ntr=document.createElement("tr");
		var ntd=document.createElement("td");
		ntr.appendChild(ntd);
		ntbody.appendChild(ntr);
		this.coreElement.appendChild(ntbody);
		document.body.appendChild(this.coreElement);
		this.currentContent="formid : columnIdx";
	}
	this.setHide();
};

ECSideUtil.ColmunMenu.getFilterItem=function(menuWidth,columnObj,columnName,formid){
	var FIX_WIDTH=20;

	var filterFieldName=formid+ECSideConstants.FILTER_PREFIX+columnName;
	var filterField=$_E(filterFieldName);
	if (!filterField){
		return "";
	}
	var v=filterField.value;

	var jsfunction="ECSideUtil.doFilter(event,this,'"+filterFieldName+"','"+formid+"')";
	//this.coreElement.style.width=width+"px";
	var filterHTML="<nobr><input type=\"button\" class=\"filterIcon\" onclick=\""+jsfunction+"\" />";

	var templateId=columnObj.getAttribute("editTemplate");
	var template=document.getElementById(templateId);
	var templateText=window.isIE?template.value:template.textContent;
	templateText=ECSideUtil.trimString(templateText);

if (templateText.toLowerCase().indexOf("<select ")==0 ){
	// todo:
	templateText=templateText.substring(8);
	templateText=ECSideUtil.replaceAll(templateText," name=\""," tempname=\"");
	templateText=ECSideUtil.replaceAll(templateText," id=\""," tempid=\"");
	templateText=ECSideUtil.replaceAll(templateText," style=\""," tempstyle=\"");
	templateText=ECSideUtil.replaceAll(templateText," class=\""," tempclass=\"");
	templateText=ECSideUtil.replaceAll(templateText," value=\""+v+"\""," value=\""+v+"\" selected=\"selected\" ");
	
	filterHTML+="<select onclick=\"ECSideUtil_stopEvent(event)\" style=\"width:"+(menuWidth- FIX_WIDTH)+"px\" filterfield=\"true\" onkeypress=\""+jsfunction+"\" "+templateText;
}else{
	filterHTML+="<input type=\"text\" class=\"filterInput\" value=\""+v+"\" style=\"width:"+(menuWidth- FIX_WIDTH)+"px\" onclick=\"ECSideUtil.doFilterFocus(event,this)\" onkeypress=\""+jsfunction+"\" /></nobr>";
}
	var clearHTML="<nobr><input type=\"button\" class=\"clearIcon\" />";
	clearHTML+="<span class=\"itemText\" style=\"width:"+(menuWidth- FIX_WIDTH)+"px\" onclick=\"ECSideUtil.doClearFilter(event,this,'"+filterFieldName+"','"+formid+"')\" >"+ECSideMessage.FILTERCLEAR_TEXT+"</span></nobr>";

	return filterHTML+"<br />"+clearHTML;

};

ECSideUtil.ColmunMenu.getSortItem=function(menuWidth,columnObj,columnName,formid){

		var FIX_WIDTH=30;

	var sortFieldName=formid+ECSideConstants.SORT_PREFIX+columnName;
	var sortField=$_E(sortFieldName);
	if (!sortField){
		return "";
	}

	var jsfunctionAsc="ECSideUtil.doSort(event,'"+columnName+"','asc','"+formid+"')";
	var jsfunctionDesc="ECSideUtil.doSort(event,'"+columnName+"','desc','"+formid+"')";
	var jsfunctionDefault="ECSideUtil.doSort(event,'"+columnName+"','default','"+formid+"')";


	var ascHTML="<nobr><input type=\"button\" class=\"ascIcon\" />";
	ascHTML+="<span class=\"itemText\" style=\"width:"+(menuWidth- FIX_WIDTH)+"px\" onclick=\""+jsfunctionAsc+"\" >"+ECSideMessage.SORTASC_TEXT+"</span></nobr>";

	var descHTML="<nobr><input type=\"button\" class=\"descIcon\" />";
	descHTML+="<span class=\"itemText\" style=\"width:"+(menuWidth- FIX_WIDTH)+"px\" onclick=\""+jsfunctionDesc+"\" >"+ECSideMessage.SORTDESC_TEXT+"</span></nobr>";


	var defaultHTML="<nobr><input type=\"button\" class=\"defaultIcon\" />";
	defaultHTML+="<span class=\"itemText\" style=\"width:"+(menuWidth- FIX_WIDTH)+"px\" onclick=\""+jsfunctionDefault+"\" >"+ECSideMessage.SORTDEFAULT_TEXT+"</span></nobr>";

	return ascHTML+"<br />"+descHTML+"<br />"+defaultHTML;

};

ECSideUtil.ColmunMenu.showMe=function(fireObj,formid){
	var minWidth=100;
	var FIX_WIDTH=5;
	var menuWidth=0;

	var columnName=fireObj.getAttribute("columnName");
	if(this.currentContent==formid+" : "+columnName && this.isShow){
		ECSideUtil.ColmunMenu.doHideMe();
		return;
	}
	

	menuWidth =fireObj.offsetWidth- FIX_WIDTH;
	menuWidth=menuWidth< minWidth? minWidth :menuWidth;


	var menuZone=this.coreElement.rows[0].cells[0];

	var filterItemHTML=this.getFilterItem(menuWidth,fireObj,columnName,formid);
	var sortItemHTML=this.getSortItem(menuWidth,fireObj,columnName,formid);
	
	if (filterItemHTML=="" && sortItemHTML==""){
		return;
	}
	var menuHTML=filterItemHTML+"<hr />" + sortItemHTML;

	menuZone.innerHTML= menuHTML ;

	this.coreElement.style.top= ECSideUtil.getPosBottom(fireObj)+document.body.scrollTop +"px";
	this.coreElement.style.display="block";
	var tempLeft=ECSideUtil.getPosLeft(fireObj)+document.body.scrollLeft;
	if ( tempLeft+this.coreElement.offsetWidth >=document.body.clientWidth-2){
		tempLeft=document.body.clientWidth-2-this.coreElement.offsetWidth;
	}
	this.coreElement.style.left=tempLeft+"px";

	this.setShow();
	this.currentContent=formid+" : "+columnName;

};



/* ============ RESIZE COLUMN WIDTH ======================= */

ECSideUtil.startDragobj=false;
ECSideUtil.MinColWidth=ECSideConstants.MIN_COLWIDTH;


ECSideUtil.Dragobj=null; 
ECSideUtil.DragobjBodyCell=null;

ECSideUtil.DragobjBodyCellNext=null;
ECSideUtil.DragobjNext=null;

ECSideUtil.DragECSideObj=null;


ECSideUtil.leftC=0;
ECSideUtil.rightC=0;

ECSideUtil.startC=0;
ECSideUtil.endC=0;

ECSideUtil.StartResize=function(event,obj,formid){
	var e = event||window.event;
	if (!formid){
		formid=ECSideConstants.FORM_ID;
	}

	obj.focus();
	document.body.style.cursor = "col-resize";
	var dx=window.isIE?e.x:e.pageX;

	ECSideUtil.DragECSideObj=ECSideList[formid];

	ECSideUtil.Dragobj=obj.parentNode;
	ECSideUtil.DragobjNext = ECSideUtil.getNextElement(ECSideUtil.Dragobj);

	var way=ECSideUtil.DragECSideObj.resizeWay;
	//ECSideUtil.startC=e.screenX;

	ECSideUtil.leftC =ECSideUtil.getPosLeft( ECSideUtil.Dragobj )+parseInt(ECSideUtil.MinColWidth);
	if (way=="resizeTable" || !ECSideUtil.DragobjNext){
		if (ECSideUtil.DragECSideObj.ECListBodyZone){
			ECSideUtil.rightC =ECSideUtil.getPosRight(ECSideUtil.DragECSideObj.ECListBodyZone);
		}else{
			ECSideUtil.rightC=document.body.clientWidth;
		}

	}else{
		ECSideUtil.rightC =ECSideUtil.getPosRight( ECSideUtil.DragobjNext )-parseInt(ECSideUtil.MinColWidth);
	}
	ECSideUtil.leftC+=document.body.scrollLeft;
	ECSideUtil.rightC+=document.body.scrollLeft;

	var cellIndex=ECSideUtil.Dragobj.cellIndex;
	try{
		 ECSideUtil.DragobjBodyCell=ECSideList[formid].ECListBody.rows[0].cells[cellIndex];
	}catch(e){
		ECSideUtil.DragobjBodyCell=null;
	}
	try{
		 ECSideUtil.DragobjBodyCellNext=ECSideUtil.getNextElement(ECSideUtil.DragobjBodyCell);
	}catch(e){
		ECSideUtil.DragobjBodyCellNext=null;
	}


	ECSideUtil.MinColWidth=ECSideList[formid].MIN_COL_WIDTH;
	if (!ECSideUtil.MinColWidth||ECSideUtil.MinColWidth=='' || ECSideUtil.MinColWidth<1){
		ECSideUtil.MinColWidth=ECSideConstants.MIN_COLWIDTH;
	}


	ECSideUtil.separateLine.style.top=ECSideUtil.getPosTop(ECSideUtil.DragECSideObj.ECListHead)+2;



var dX=window.isIE?document.body.scrollLeft+e.clientX:document.body.scrollLeft+e.pageX;

//ECSideUtil.startC=ECSideUtil.getPosRight( ECSideUtil.Dragobj )-ECSideUtil.parseIntOrZero(ECSideUtil.separateLine.style.width)+document.body.scrollLeft-ECSideUtil.DragECSideObj.ECListBodyZone.scrollLeft;
ECSideUtil.startC=dX;
ECSideUtil.separateLine.style.left=ECSideUtil.startC+"px";

	var th=ECSideUtil.DragECSideObj.ECListHead.parentNode.clientHeight;
	if (ECSideUtil.DragECSideObj.ECListHead.parentNode!=ECSideUtil.DragECSideObj.ECListBody.parentNode){
		th+=ECSideUtil.DragECSideObj.ECListBodyZone.clientHeight;
	}
	ECSideUtil.separateLine.style.height=th+'px';
	ECSideUtil.separateLine.style.display="block";

	ECSideUtil.startDragobj=true;

ECSideUtil_stopEvent(e);

}


ECSideUtil.DoResize=function(event){

	var e = event||window.event;

var dX=window.isIE?document.body.scrollLeft+e.clientX:document.body.scrollLeft+e.pageX;


	if (!ECSideUtil.Dragobj || !ECSideUtil.startDragobj){
		if (ECSideUtil.separateLine){
			ECSideUtil.separateLine.style.display="none";
		}
		document.body.style.cursor = "";
		return;	
	}	

	if (dX<=ECSideUtil.leftC || dX>=ECSideUtil.rightC){
		document.body.style.cursor = "not-allowed";
		return;
	}
	if (document.body.style.cursor == "not-allowed"){
		document.body.style.cursor = "col-resize";
	}
	

	ECSideUtil.separateLine.style.left=dX+"px";

}

ECSideUtil.EndResize=function(event){

if (!ECSideUtil.Dragobj){
	ECSideUtil.startDragobj=false;
	document.body.style.cursor = "";
	return;
}


	var e = event||window.event;
	//ECSideUtil.endC=e.screenX;



	ECSideUtil.endC=ECSideUtil.parseIntOrZero(ECSideUtil.separateLine.style.left);



var dWidth=ECSideUtil.startC-ECSideUtil.endC;

var fixX=0;
if (window.isIE){
	fixX = ECSideConstants.IE_WIDTH_FIX_A;	

}else{ 
	fixX= ECSideConstants.FF_WIDTH_FIX_A;
}

var cc=0;

cc=ECSideUtil.DragobjBodyCell.clientWidth-dWidth+fixX;
ECSideUtil.Dragobj.style.width=cc+"px";
ECSideUtil.DragobjBodyCell.style.width=cc+"px";
ECSideUtil.DragobjBodyCell.width = cc+"px";

/*
	if (ECSideUtil.DragobjNext && ECSideUtil.DragECSideObj.resizeWay!="resizeTable"){
		cc=ECSideUtil.DragobjBodyCellNext.clientWidth+dWidth+fixX;
		if (cc<10){
			cc=10;
		}
		ECSideUtil.DragobjNext.style.width=cc+"px";
		ECSideUtil.DragobjBodyCellNext.style.width=cc+"px";
		ECSideUtil.DragobjBodyCellNext.width = cc+"px";
	}
*/
	if (!ECSideUtil.DragECSideObj.isClassic){
		ECSideUtil.syncRowsWidth(ECSideUtil.DragECSideObj.ECListHead.rows,ECSideUtil.DragECSideObj.ECListBody.rows);
	}


	document.body.style.cursor = "";
	ECSideUtil.separateLine.style.display="none";

	ECSideUtil.DragECSideObj.handleResize();
	
	ECSideUtil.startDragobj=false;
	ECSideUtil.Dragobj=null;
	
	ECSideUtil.DragECSideObj=null;
	ECSideUtil.DragobjBodyCell=null;
	ECSideUtil.DragobjBodyCellNext=null;
	ECSideUtil.DragobjNext=null;
	
	ECSideUtil_stopEvent(e);
}


ECSideUtil.resizeInit=function(){
	document.onmousemove = ECSideUtil.DoResize;
	document.onmouseup = ECSideUtil.EndResize;
	document.body.ondrag = function() {return false;};
	
    document.body.onselectstart = function() {
		return ECSideUtil.Dragobj==null && ECSideUtil.startDragobj==false;
	};
	 

	/*
		var e = event||window.event;
 e.cancelBubble = true   
 e.returnValue = false;   
	return   false; 
	*/
};





/* ===========EDIT CELL ============= */
ECSideUtil.getColumnName=function(cellObj,formid){
	var ecsideObj=ECSideUtil.getGridObj(formid);
	var cname='';
	try {
		var idx=cellObj.cellIndex;
		var cell=ecsideObj.ECListHead.rows[0].cells[idx];
		cname=cell.getAttribute("columnName");
	}catch(e){
		cname='';
	}
	return cname;
};

// editType =  input select checkbox radio
ECSideUtil.editCell=function(cellObj,formid,templateId){
	if (cellObj.getAttribute("editing")=="true"){
		return;
	}
	var ecsideObj=ECSideUtil.getGridObj(formid);

	cellObj.setAttribute("editing","true");
	if (!templateId){
		var idx=cellObj.cellIndex;
		var thcell=ecsideObj.ECListHead.rows[0].cells[idx];
		templateId=thcell.getAttribute("editTemplate");
	}
	var template=document.getElementById(templateId);
	var templateText=window.isIE?template.value:template.textContent;

	templateText=ECSideUtil.trimString(templateText);

	var text=window.isIE?cellObj.innerText:cellObj.textContent;
	var value=cellObj.getAttribute("cellValue");
	value=value==null?text:value;
	
	value=ECSideUtil.trimString(value);
	
	var name=ECSideUtil.getColumnName(cellObj,formid);
	if (templateText.indexOf("name=\"\"")>0){
		templateText=ECSideUtil.replaceAll(templateText,"name=\"\"","name=\""+name+"\"");
	}



var editType="input";

if (templateText.toLowerCase().indexOf("<input ")==0 ){
	if (templateText.indexOf(" type=\"checkbox\"")>0){
		editType="checkbox";
	}else if(templateText.indexOf(" type=\"radio\"")>0){
		editType="radio";
	}
}else if (templateText.toLowerCase().indexOf("<select ")==0 ){
	editType="select";
}

	if (editType=="input"){
		cellObj.innerHTML=ECSideUtil.replaceAll(templateText,"value=\"\"","value=\""+value+"\"");	
	}else if (editType=="select"){
		cellObj.innerHTML=ECSideUtil.replaceAll(templateText,"value=\""+value+"\"","value=\""+value+"\" selected=\"selected\"");	
	}else if (editType=="checkbox" || editType=="radio"){
		cellObj.innerHTML=ECSideUtil.replaceAll(templateText,"value=\""+value+"\"","value=\""+value+"\" checked=\"checked\"");	
	}
	
	ECSideUtil.getFirstChildElement(cellObj).focus();
};


ECSideUtil.updateEditCell=function(cellEditObj,editType){

	if (cellEditObj.getAttribute("filterfield")=="true"){
		return;
	}
	var cellObj=cellEditObj.parentNode;

	ECSideUtil.updateCellContent(cellObj,cellEditObj);
	
	cellObj.setAttribute("edited","true");
	cellObj.parentNode.setAttribute("edited","true");
	cellObj.setAttribute("editing","false");
	ECSideUtil.addClass(cellObj, "editedCell");
};


ECSideUtil.updateCell=function(cellObj){
	var elems=Form.getElements(cellObj);
	if (elems.length>0){
		var cellEditObj=elems[0];
		ECSideUtil.updateCellContent(cellObj,cellEditObj);
	}
};

ECSideUtil.updateCellContent=function(cellObj,elementObj){
	
		var editType=elementObj.tagName.toLowerCase();
		if (editType=="input"){
			var type=elementObj.type.toLowerCase();
			if (type=='checkbox' || type=='radio'){
				editType=type;
			}
		}
		var value=elementObj.value;
		if (editType=="input"){
			cellObj.innerHTML=elementObj.value;
		}else if (editType=="select"){
			value=elementObj.options[elementObj.selectedIndex].value;
			cellObj.innerHTML=elementObj.options[elementObj.selectedIndex].text;
		}else if (editType=="checkbox" || editType=="radio"){
			cellObj.innerHTML=elementObj.nextSibling.nodeValue;
		}else{
			cellObj.innerHTML=elementObj.innerHTML;
		}
		cellObj.setAttribute("cellValue",ECSideUtil.trimString(value));
};

ECSideUtil.getUpdatedRows=function(formid){
	var ecsideObj=ECSideUtil.getGridObj(formid);
	var erows=[];
	if (ecsideObj && ecsideObj.ECListBody){
		var rs=ecsideObj.ECListBody.rows;
		for (var i=0;i<rs.length;i++){
			if (rs[i].getAttribute("edited")=="true"){
				erows.push(rs[i]);
			}
		}
	}
	return erows;
};


ECSideUtil.getDeletedRows=function(formid){
	var ecsideObj=ECSideUtil.getGridObj(formid);
	var erows=[];
	if (ecsideObj && ecsideObj.ECListBody){
		var rs=ecsideObj.ECListBody.rows;
		for (var i=0;i<rs.length;i++){
			if (ECSideUtil.hasClass(rs[i],"del")){
				erows.push(rs[i]);
			}
		}
	}
	return erows;
};

ECSideUtil.getRemoveUpdatedClassRows=function(listRows,recordKey){

	if (listRows && listRows.length){
		for (var i=0;i<listRows.length;i++){
			if (listRows[i].getAttribute("recordKey")==ECSideUtil.trimString(recordKey)){

				listRows[i].setAttribute("edited",null);
				ECSideUtil.clearRowEditedFlag(listRows[i]);
			}
		}
	}
};

ECSideUtil.getRemoveInsertedClassRows=function(listRows,recordKey){

	if (listRows && listRows.length){
		for (var i=0;i<listRows.length;i++){
			var cells=listRows[i].cells;
			for (var j=0;j<cells.length ;j++ ){
				ECSideUtil.updateCell(cells[j]);
			}
			listRows[i].className="added";
		}
	}
};


ECSideUtil.clearRowEditedFlag=function(rowObj){
	var cs=rowObj.cells;
	for (var i=0;i<cs.length;i++){
		cs[i].setAttribute("edited","false");
		ECSideUtil.removeClass(cs[i], "editedCell");
	}
};

ECSideUtil.getRemoveDeletedRows=function(listRows,recordKey){

	if (listRows && listRows.length){
		for (var i=0;i<listRows.length;i++){
			var crow=listRows[i];
			if (crow && crow.getAttribute("recordKey")==ECSideUtil.trimString(recordKey) && ECSideUtil.hasClass(crow,"del")){
				var crowIndex=crow.rowIndex;
				if (crow.getAttribute("hasShadow")=="true" ){
					crow.parentNode.removeChild(crow.parentNode.rows[crowIndex+1]);
				}
				crow.parentNode.removeChild(crow);
			}
		}
	}
};

ECSideUtil.getRowCellsMap=function(rowObj,formid){
	var cellMap={};
	var keyvalue=rowObj.getAttribute("recordKey");
	if (keyvalue){
		cellMap["recordKey"]=keyvalue;
	}
	var cells=rowObj.cells;
	for (var i=0;i<cells.length;i++ ){
		var cellObj=cells[i];
		var name=ECSideUtil.getColumnName(cellObj,formid);
		if (!name){
			continue;
		}
		var value=cellObj.getAttribute("cellValue");
		if (!value){
			value=window.isIE?cellObj.innerText:cellObj.textContent;
		}
		if (cellMap[name]==undefined){
			cellMap[name]=value;
		}else if(ECSideUtil.isCollection(cellMap[name]) ){
			cellMap[name].push(value);
		}else{
			var temp_v=cellMap[name];
			cellMap[name]=[temp_v];
			cellMap[name].push(value);
		}

	}
	return cellMap;
};


ECSideUtil.getInsertRows=function(formid){

	var ecsideObj=ECSideUtil.getGridObj(formid);
	var erows=[];
	if (ecsideObj && ecsideObj.ECListBody){
		var rs=ecsideObj.ECListBody.rows;
		for (var i=0;i<rs.length;i++){
			if (ECSideUtil.hasClass(rs[i],"add")){
				erows.push(rs[i]);
			}
		}
	}
	return erows;
};


ECSideUtil.updateShadowRow=function(crow,shadowRow,formid,originalRequest){
	var text=originalRequest.responseText;
	if (ECSideUtil.trimString(text)!=''){
		shadowRow.cells[0].innerHTML=text;
	}else{
		shadowRow.cells[0].innerHTML= ECSideMessage.SHADOWROW_FAILED+" ( recordKey : "+crow.getAttribute("recordKey")+").";
	}
	

	var shadowRowHeight=ECSideUtil.parseIntOrZero(shadowRow.cells[0].scrollHeight);
	shadowRow.setAttribute("shadowRowHeight",shadowRowHeight);
	shadowRow.cells[0].style.height=shadowRowHeight+"px";
	

	var shadowRowHeight=ECSideUtil.parseIntOrZero(shadowRow.getAttribute("shadowRowHeight"));
var ecsideObj=ECSideUtil.getGridObj(formid);
ecsideObj.hideWaitingBar();

	//ECSideUtil.changeListHeight("+"+shadowRowHeight ,formid);
};

ECSideUtil.showShadowRow=function(crow,eventSrc,formid){
		var ecsideObj=ECSideUtil.getGridObj(formid);


		if (!crow && !eventSrc){
			return;
		}
		if(typeof(crow)=="string" ){
			crow=document.getElementById(crow);
		}
		if(typeof(eventSrc)=="string" ){
			eventSrc=document.getElementById(eventSrc);
		}
		if (crow.tagName.toLowerCase()=="td"){
			crow=crow.parentNode;
		}
		var crowIndex=crow.rowIndex;

		if (!ecsideObj.isClassic){
			crowIndex++;
		}
		var cellnum=crow.cells.length;

		var hasShadow=crow.getAttribute("hasShadow");
		var shadowRow=null;
		var isShowed=true;

		if (hasShadow=="true"){
			shadowRow=crow.parentNode.rows[crowIndex];
			if (shadowRow.style.display=="none"){
				shadowRow.style.display="";
				//if (ecsideObj.showShadowRowCallBack){
					//ecsideObj.showShadowRowCallBack(formid,crow,shadowRow,eventSrc);
				//}
				//alert(1)
				eventSrc.className="shadowRowButtonOpen";
			}else{
				shadowRow.style.display="none";
				//if (ecsideObj.hideShadowRowCallBack){
					//ecsideObj.hideShadowRowCallBack(formid,crow,shadowRow,eventSrc);
				//}	
				//alert(2)
				eventSrc.className="shadowRowButtonClose";
				isShowed=false;
			}
		
		}else{
			//alert(3)
			shadowRow=crow.parentNode.insertRow(crowIndex);
			shadowRow.className="shadowRow";
			shadowRow.style.display="";
			var newcell=document.createElement("td");
			newcell.colSpan=cellnum;
			shadowRow.appendChild(newcell);
			crow.setAttribute("hasShadow","true");
			shadowRow.setAttribute("isShadow","true");

			var url=document.getElementById("shadowRowAction").value;
			//ecsideObj.ECForm.getAttribute("shadowRowAction");
			var pars=ECSideUtil.getRowCellsMap(crow,formid);
			var updateShadowRow=ECSideUtil.updateShadowRow.bind(this,crow,shadowRow,formid);
			ECSideUtil.doAjaxUpdate(url,pars,updateShadowRow,formid);

			eventSrc.className="shadowRowButtonOpen";
			
			//if (ecsideObj.firstShowShadowRowCallBack){
			//	ecsideObj.firstShowShadowRowCallBack(formid,crow,shadowRow,eventSrc);
			//}
			
		}
		if ( isShowed && ecsideObj.autoCloseOtherShadowRow ) {
			if (ecsideObj.currentShadowRowParent && ecsideObj.currentShadowEventSrc && crow.id!=ecsideObj.currentShadowRowParent  )	{
				ECSideUtil.showShadowRow(ecsideObj.currentShadowRowParent,ecsideObj.currentShadowEventSrc,ecsideObj.id);
			}
		}
		if (isShowed){
			ecsideObj.currentShadowRowParent=crow.id;
			ecsideObj.currentShadowEventSrc=eventSrc.id;
		}else{
			ecsideObj.currentShadowRowParent=null;
			ecsideObj.currentShadowEventSrc=null;
		}

	};

/////////////////////////////////////////////////



ECSideUtil.saveGirdBatch=function(buttonObj,formid,isBatch){

	if(!confirm(ECSideMessage.UPDATE_CONFIRM)){
		return;
	}

	var ecsideObj=ECSideUtil.getGridObj(formid);
	var form=ecsideObj.ECForm;

	var urlu=document.getElementById("updateAction").value;//form.getAttribute("updateAction")+"";
	var rows=ECSideUtil.getUpdatedRows(formid);

	var urli=document.getElementById("insertAction").value;//form.getAttribute("insertAction")+"";
	var rowsi=ECSideUtil.getInsertRows(formid);

	var urld=document.getElementById("deleteAction").value;//form.getAttribute("deleteAction")+"";
	var rowsd=ECSideUtil.getDeletedRows(formid);


	var continueFunction=true;
	if (ecsideObj.beforeSave){
		continueFunction=ecsideObj.beforeSave(formid,rows,rowsi,rowsd);
	}
	if (continueFunction===false){
		return;
	}


	var parsMap;
	var useEasyDataAccessFlag="easyDataAccess=";
	if (isBatch==null || isBatch==window.undefined)	{
		isBatch=true;
	}
	var doBatch = isBatch;



	ecsideObj.forUpdateRows=rows;
	parsMap={};
	doBatch=doBatch && urlu.indexOf(useEasyDataAccessFlag)<1;
	for (var i=0;i<rows.length;i++){
		var pars=ECSideUtil.getRowCellsMap(ecsideObj.forUpdateRows[i],formid);
		if (doBatch){
			ECSideUtil.appendMap(parsMap,pars);
		}else{
			ECSideUtil.doAjaxUpdate(urlu,pars,ecsideObj.updateCallBack,formid);
		}

	}
	if (doBatch && rows.length>0){
		ECSideUtil.doAjaxUpdate(urlu,parsMap,ecsideObj.updateCallBack,formid);
	}

/* ============== */

	ecsideObj.forInsertRows=rowsi;
	parsMap={};
	doBatch=doBatch && urli.indexOf(useEasyDataAccessFlag)<1;
	for (var i=0;i< rowsi.length;i++){
		var pars=Form.serialize(ecsideObj.forInsertRows[i],true);
		if (doBatch){
			ECSideUtil.appendMap(parsMap,pars);
		}else{
			ECSideUtil.doAjaxUpdate(urli,pars,ecsideObj.insertCallBack,formid);
		}

	}
	if (doBatch && rowsi.length>0){
		ECSideUtil.doAjaxUpdate(urli,parsMap,ecsideObj.insertCallBack,formid);
	}


/* ============== */


	ecsideObj.forDeleteRows = rowsd;
	parsMap={};
	doBatch=doBatch && urld.indexOf(useEasyDataAccessFlag)<1;
	for (var i=0;i< rowsd.length;i++){
		var pars=ECSideUtil.getRowCellsMap(ecsideObj.forDeleteRows[i],formid);
		if (doBatch){
			ECSideUtil.appendMap(parsMap,pars);
		}else{
			ECSideUtil.doAjaxUpdate(urld,pars,ecsideObj.deleteCallBack,formid);
		}
	}
	if (doBatch && rowsd.length>0){
		ECSideUtil.doAjaxUpdate(urld,parsMap,ecsideObj.deleteCallBack,formid);
	}

/* ============== */

	if (rows.length<1){
		ecsideObj.forUpdateRows=[];
	}
	if (rowsi.length<1){
		ecsideObj.forInsertRows=[];
	}
	if (rowsd.length<1){
		ecsideObj.forDeleteRows=[];
	}

	if ( rows.length<1 && rowsi.length<1 && rowsd.length<1 ){
		alert(ECSideMessage.NO_RECORD_UPDATE);
	}
	
};

ECSideUtil.saveGird=ECSideUtil.saveGirdBatch;
//ECSideUtil.saveGird=ECSideUtil.saveGirdSingle;

ECSideUtil.delFromGird=function(buttonObj,formid,deleteFlags){

	var ecsideObj=ECSideUtil.getGridObj(formid);
	var form=ecsideObj.ECForm;

	if (!deleteFlags){
		deleteFlags=ecsideObj.deleteFlags;
	}
	var checkBoxList=form[deleteFlags];

	var crow=ecsideObj.selectedRow;
	if (crow && ECSideUtil.hasClass(crow,"added")){
		return;
	}
	if (crow && ECSideUtil.hasClass(crow,"add")){
		crow.parentNode.removeChild(crow);
		return;
	}

	if ( (!deleteFlags || !checkBoxList ) && crow){
		if (ECSideUtil.hasClass(crow,"del")){
			ECSideUtil.removeClass(crow,"del");
		}else{
			ECSideUtil.addClass(crow,"del");
		}
		return;
	}

	if (!checkBoxList){
		return ;
	}
	if ( ! ECSideUtil.isCollection(checkBoxList) ){
		checkBoxList=[checkBoxList];
	}

	for (var i=0;i<checkBoxList.length;i++){
		if (checkBoxList[i].checked){
			var rowObj=checkBoxList[i].parentNode.parentNode;
			if (ECSideUtil.hasClass(rowObj,"del")){
				ECSideUtil.removeClass(rowObj,"del");
			}else{
				ECSideUtil.addClass(rowObj,"del");
			}
			rowObj.className="del";
		}
	}
};

ECSideUtil.addToGird = function(buttonObj,templateId,formid){
	var ecsideObj =ECSideUtil.getGridObj(formid);
	var template=document.getElementById(templateId);

	if (!template){
		template= document.getElementById(ECSideConstants.DEFALUT_ADD_TEMPLATE);
	}

	template=template.value;

	var rowsNum=0;
	if (ecsideObj.ECListBody.rows){
		rowsNum=ecsideObj.ECListBody.rows.length
	}
	var newTr=ecsideObj.ECListBody.insertRow(rowsNum);
	ECSideUtil_addEvent( newTr,"click", ECSideUtil.selectRow.bind(this,newTr,ecsideObj.id) );

	template=template.split("<tpsp />");
	newTr.className="add";
	var cells=[];
	for (var i=0;i<ecsideObj.columnNum;i++ ){
		cells[i]=newTr.insertCell(i);
		cells[i].innerHTML=template[i];
	}
	var topTr=ECSideUtil.getPosTop(newTr);
	if (ecsideObj.ECListBodyZone){
		ecsideObj.ECListBodyZone.scrollTop=topTr;
	}
}

ECSideUtil.doAjaxUpdate=function(url,pars,callBack,formid){
	var ecsideObj=ECSideUtil.getGridObj(formid);
	
	var continueFunction=true;
	if (ecsideObj.beforeSubmit){
		continueFunction=ecsideObj.beforeSubmit(pars,formid);
	}
	if (continueFunction===false){
		return;
	}
		var myAjax = new Ajax.Request( url,{
			requestHeaders:ECSideConstants.AJAX_HEADER, 
			method: "POST", 
			asynchronous: true , 
			parameters: pars, 
			onComplete: callBack } );
		ecsideObj.showWaitingBar();
}

// It's like Function.prototype.bind.(prorotype.js)
ECSideUtil.bindFunction=function(functionObj){
	var newArgumentsT=[];
	for (var j=1;j< arguments.length;j++ ){
		newArgumentsT.push(arguments[j]);
	}
	return function(){
		for (var i = 0; i < arguments.length; i++) {
			if ( typeof(arguments[i])!="undefined" /* i!=1 ||*/ ){
				newArgumentsT[i]=arguments[i];
			}
		}
		return functionObj.apply(this,newArgumentsT);
	};
};

ECSideUtil.changeListHeight=function(height,formid){
	var ecsideObj=ECSideUtil.getGridObj(formid);
		height=height+"";
		if ( "auto"!=height){
			if (height.indexOf('+')==0){
				height=ecsideObj.listHeight+ ECSideUtil.parseIntOrZero(height.substring(1));
			}else if (height.indexOf('-')==0){
				height=ecsideObj.listHeight- ECSideUtil.parseIntOrZero(height.substring(1));
			}else if (height=="reset"){
				height=ecsideObj.orgListHeight;
			}
		}
		if (ECSideUtil.parseIntOrZero(height)>ecsideObj.ECListBody.scrollHeight-ECSideConstants.OFFSET_A || height=="auto"){
			/* divSYT.style.overflowY="hidden"; */
			height=ecsideObj.ECListBody.parentNode.scrollHeight;
			var dh=ecsideObj.ECListBodyZone.offsetHeight-ecsideObj.ECListBodyZone.clientHeight+ECSideConstants.LIST_HEIGHT_FIXED;
			if (dh <=2 && Me.ECListBodyZone.offsetWidth-Me.ECListBodyZone.clientWidth>2){
				dh=ECSideConstants.SCROLLBAR_WIDTH;
			}
			height=height/1+dh;
		}
		height=height<ecsideObj.minHeight/1?ecsideObj.minHeight/1:height;
		if (ecsideObj.ECListBodyZone){
			ecsideObj.ECListBodyZone.style.height=height+"px";
		}
		ecsideObj.listHeight=height;
		ecsideObj.handleResize();
};

ECSideUtil.onWindowload=function(){
	ECSideUtil.initAllGird();
	ECSideUtil.ColmunMenu.initMe();
	ECSideUtil.NearPagesBar.initMe();
	ECSideUtil_addEvent(document.body,"click", ECSideUtil.ColmunMenu.doHideMe);
	ECSideUtil_addEvent(window,"resize",ECSideUtil.ColmunMenu.doHideMe);
};
ECSideUtil.initAllGird=function(){
	for (var girdId in ECSideList ){
		var gird=ECSideList[girdId];
		gird.init();
	}
};
ECSideUtil.createGird=function(formid){
	var gird=ECSideUtil.getGridObj(formid);
	if (!gird){
		gird=new ECSide(formid);
	}
	if($.browser.msie){
		$(".ecSide .tableRegion tr[class!='hideListRow'] td").css({height:'22px'});
	}
	return gird;
};

ECSideUtil_addEvent(window,"resize",ECSideUtil.resizeAllGird);
ECSideUtil_addEvent(window,"load", ECSideUtil.onWindowload);

/*
(function(){
	var gird=ECSideUtil.createGird('');
	gird.useAjax=true;
	gird.doPre
})();
*/

ECSideUtil.showCalendar=function(inputObj){
    Calendar.trigger({
        inputField     :    inputObj.previousSibling.name,      // id of the input field
        ifFormat       :    "%Y-%m-%d",       // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "date_button",   // trigger for the calendar (button ID)
        singleClick    :    true, 
		onClose	: ECSideUtil.fillDate,
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    });

};


ECSideUtil.fillDate=function(calObj){
	ECSideUtil.updateEditCell(calObj.inputField);
	calObj.hide();
}


/* for compatibility */
//var ECCN = ECSide ;
var ECCN=function(formid){
 	return ECSideUtil.getGridObj(formid);
};

ECCN.init=function(){};
var ECSideUtil = ECSideUtil;