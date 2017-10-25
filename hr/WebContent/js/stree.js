/*============================
	Author : fason
	Email		: fason_pfx@hotmail.com
============================*/

var Icon = {
	root :	"root.gif",
	folderopen :  "folderopen.gif",
	folderclosed :  "folderclosed.gif",
	Rminus:	  "Rminus.gif",
	Rplus:	 "Rplus.gif",
	minusbottom:	   "Lminus.gif",
	plusbottom:	  "Lplus.gif",
	minus:	 "minus.gif",
	plus:	 "plus.gif",
	join:	 "T.gif",
	joinbottom:	 "L.gif",
	blank:	 "blank.gif",
	line:	 "I.gif"
};

window.TV = [];
function sTree()
{
	this.id = window.TV.length;
	window.TV[this.id] = this;
	this.target = "_self";
	this.checkbox = false;
	this.Nodes ={ '0' : { ID : '0', ParentID : '-1', Text : null, Href : null, Image : null, childNodes: new Array(),checkAble : false} };
	this.checkRelParentNode = true;
}
var tv = sTree.prototype;
tv.setTarget = function(v) {
	this.target = v;
};
tv.setCheckbox = function(v) {
	this.checkbox = v;
};
tv.setName = function(v) {
	this.name = v;
};
var addedPath=false;
tv.setImagePath = function(sPath) {
	if (!addedPath){
		for(o in Icon){
			tmp = sPath + Icon[o];
	//		Icon[o] = new Image();
			Icon[o] = tmp;
		}
		addedPath = true;
	}
};
tv.add = function(iD,ParentiD,sText,sHref,sImage,checkable) {
	this.Nodes[iD] = { ID : iD, ParentID : ParentiD, Text : sText, Href : sHref, Image : sImage , childNodes: new Array() , open : false , checked : false,checkAble : checkable};
	var ch = this.getNode(ParentiD).childNodes;
	ch[ch.length] = this.Nodes[iD];
};
tv.getNode = function(sKey) {
	if (typeof this.Nodes[sKey] != "undefined")
	{
		return this.Nodes[sKey];
	}
	return null;
};
tv.getParentNode = function(ID) {
	var key = this.getNode(ID).ParentID;
	if(this.getNode(key) == null) return null;
	return this.getNode(key);
}
tv.hasChildNodes = function(sKey) {
	return this.getNode(sKey).childNodes.length > 0;
};
tv.isLastNode = function(sKey) {
	var node = this.getNode(sKey);
	var par = this.getParentNode(sKey);
	if(par == null) node.isLast = true;
	else if (typeof node["isLast"] == "undefined") {
		for(var i = 0;i<par.childNodes.length;i++)
			if(par.childNodes[i].ID == sKey) break;
		node.isLast =  (i == par.childNodes.length-1)
	}
	return node.isLast;
};
tv.getRoot = function(ID) {
	var par = this.getParentNode(ID);
	if (this.getNode(ID).ParentID == 0)
	{
		return this.getNode(ID);
	}
	else
	{
		return this.getRoot(par.ID);
	}
}
tv.drawNode = function(ID) {
	var html = "";
	var node = this.getNode(ID);
	var rootid = this.getRoot(ID).ID;
	var hc = this.hasChildNodes(ID);
	var checkable = node.checkAble;
	
	html += '<div class="TreeNode" nowrap>'+this.drawIndent(ID)+
				'<a id="node'+ID+'" class="Anchor" ondblclick="window.TV['+this.id+'].openHandler(\''+ID+'\')"><img id="folder'+ID+'" src="'+( node.Image ? node.Image : Icon.folderclosed)+'" align="absmiddle">'+
				(checkable ? ('<input class=TreeNodecheckbox type=checkbox id="checkbox'+ID+'" name="'+this.name+'" onclick="window.TV['+this.id+'].oncheck(\''+ID+'\')">') : '')+				
				'<span>'+ node.Text +'</span></a></div>\n'
	if (hc) {
		var io = ID ==  rootid;
		node.open = io;
		html += ('<div id="container'+ID+'" style="display:'+(io ? '' : 'none')+'">\n');
		html += this.addNode(ID);
		html += '</div>\n';
	}
	return html;
}

tv.addNode = function(ID) {
	var node = this.getNode(ID);
	var html = "";
	for(var i = 0;i<node.childNodes.length;i++)
		html += this.drawNode(node.childNodes[i].ID);
	return html;
}

tv.drawIndent = function(ID) {
	var s = ''
	var ir = this.getRoot(ID).ID == ID;
	var hc = this.hasChildNodes(ID);
	if(this.getParentNode(ID) != null)
		s += ((hc ? '<a href="javascript:void window.TV['+this.id+'].openHandler(\''+ID+'\');" target="_self">':'')+'<img id="handler'+ID+'" src="'+ (this.hasChildNodes(ID) ? (ir ? Icon.Rminus : (this.isLastNode(ID) ? Icon.plusbottom : Icon.plus)) : (ir ? Icon.blank : (this.isLastNode(ID) ? Icon.joinbottom : Icon.join))) + '" align="absmiddle">'+(hc?'</a>':''));
	var p = this.getParentNode(ID);
	while(p != null)
	{
		if(this.getParentNode(p.ID) == null)break;
		s = ('<img src="'+(this.isLastNode(p.ID) ? Icon.blank : Icon.line) + '" align="absmiddle">')+s;
		p = this.getParentNode(p.ID);
	}
	return s;
}
tv.setSelected = function(ID) {
	if(this.selectedID) { document.getElementById("node" + this.selectedID).className = "Anchor";}
	this.selectedID = ID;
	document.getElementById("node" + ID).className = "selected";
}
tv.oncheck = function(ID) {
	var o = this.getNode(ID);
	var v = o.checked;
	o.checked = document.getElementById("checkbox" + ID).checked;
	//this.checkChildren(ID,o.checked);
	if (!v){
		if (this.checkRelParentNode){
			this.checkParent(ID);	
		}else{
			this.check(ID,true);
		}
	}else{
		this.checkChildren(ID,false);
	}
};
tv.check = function(ID,v){
	if (document.getElementById("checkbox" + ID)){
		this.getNode(ID).checked = v;	
		document.getElementById("checkbox" + ID).checked = v;	
	}	
}
tv.checkChildren = function(ID,v){
	var ch = this.getNode(ID).childNodes;
	for(var i = 0;i<ch.length;i++){
		this.check(ch[i].ID,v);
		this.checkChildren(ch[i].ID,v);
	}
}
tv.checkParent = function(ID) {
	var par = this.getParentNode(ID);
	if(ID != this.getRoot(ID).ID){
		/*
		for(var j = 0;j<par.childNodes.length;j++)
			if(!par.childNodes[j].checked) break;
		this.check(par.ID,(j == par.childNodes.length));*/
		this.check(par.ID,true)
		this.checkParent(par.ID);
	}
}

tv.openHandler = function(ID) {
	if (this.hasChildNodes(ID)) {
		if (this.getNode(ID).open) {
			this.collapse(ID);
		}
		else {
			this.expand(ID);
		}
	}
}
tv.expand = function(ID) {
	var handler = document.getElementById("handler"+ID);
	var container = document.getElementById("container"+ID);
	handler.src = this.getRoot(ID).ID == ID ? Icon.Rminus : ( this.getNode(ID).isLast ? Icon.minusbottom : Icon.minus);
	container.style.display = '';
	this.getNode(ID).open = true;
}
tv.collapse = function(ID) {
	var handler = document.getElementById("handler"+ID);
	var container = document.getElementById("container"+ID);
	handler.src = this.getRoot(ID).ID == ID ? Icon.Rplus : ( this.getNode(ID).isLast ? Icon.plusbottom : Icon.plus);
	container.style.display = 'none';
	this.getNode(ID).open = false;
}
tv.openFolder = function(ID)
{
	if(this.selectedID){ 
		if(this.getNode(this.selectedID).Image == null) {document.getElementById("folder"+this.selectedID).src = Icon.folderclosed;}
	}
	var folder = document.getElementById("folder" + ID);
	if(this.getNode(ID).Image == null) folder.src = Icon.folderopen;
	this.setSelected(ID);
	this.selectedID = ID;
	folder.parentNode.blur();
}
tv.toString = function() {
	return this.addNode(0);
}
tv.drawTree = function(containerId){
	var container = document.getElementById(containerId);
	var treeHtml = this.addNode(0);
	container.innerHTML='';
	container.innerHTML = treeHtml;
}