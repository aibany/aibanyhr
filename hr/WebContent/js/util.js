var formTagId = 'form1';
var actionTypeTagId = 'actionType';
var operaTypeTagId = 'operaType';
var rsIdTagId = 'ID';
var copyRequestActionValue = 'copyRequest';
var insertRequestActionValue = 'insertRequest';
var updateRequestActionValue = 'updateRequest';
var viewDetailActionValue = 'viewDetail';
var insertActionValue = 'insert';
var updateActionValue = 'update';
var detailActionValue = 'detail';
var deleteActionValue = 'delete';
var checkPrimaryKeyActionValue = 'checkPrimaryKey';
var callBackHandlerId = 'callBackHandler';
var modifyImgBtnTagId = 'modifyImgBtn';
var saveImgBtnTagId = 'saveImgBtn';

var waitMsg = "正在执行后台操作，请稍候……";
var deleteMsg = "正在删除数据，请稍候……";
var confirmMsg = "您确认要删除数据吗？";
var currentRow = null;

if (window.jQuery) {
	jQuery.fn.extend({
		enable : function() {
			return this.each(function() {
				this.disabled = false;
			});
		},
		disable : function() {
			return this.each(function() {
				this.disabled = true;
			});
		},
		check : function() {
			return this.each(function() {
				this.checked = true;
			});
		},
		uncheck : function() {
			return this.each(function() {
				this.checked = false;
			});
		}
	});
}

function clearSelection() {
	if (document.selection && document.selection.empty) {
		document.selection.empty();
	} else if (window.getSelection) {
		var sel = window.getSelection();
		sel.removeAllRanges();
	}
}

function onMover(st) {
	st.style.backgroundColor = '#FFDDA6';
}
function onMout(st) {
	st.style.backgroundColor = '';
}
function showMessage(msg) {
	$('#errorMsg')
			.html(
					'<span style="float:left;color:#FF0000;">'
							+ msg
							+ '</span><span style="float:right;color:#000">双击关闭提示</span>');
	$('#errorMsg').show();
}
function hideErrorMsg() {
	$('#errorMsg').css({
		display : 'none'
	});
}
function winClose() {
	window.opener = null;
	window.open('', '_self');
	window.close()
}
function showSplash() {
	var numargs = arguments.length;
	var showMessage = true;
	var info = waitMsg;
	if (numargs > 0) {
		info = arguments[0];
	}
	if (numargs == 2) {
		showMessage = arguments[1];
	} else {
		showMessage = true;
	}
	if (!document.getElementById('splashLayer')) {
		initMaskLayer();
	}

	$('#infoTD').html(info);
	if (showMessage) {
		$('#messageDiv').css({
			"z-index" : 9999,
			display : 'block'
		});
	}
	$('#splashLayer').css({
		"z-index" : 900,
		display : 'block',
		opacity : '0.5'
	});
}
function showMaskLayer(pOpacity) {
	if (!ele('splashLayer')) {
		initMaskLayer();
	}
	var curOpacity = '0.5';
	if (pOpacity) {
		curOpacity = pOpacity;
	}
	$('#splashLayer').css({
		"z-index" : 900,
		display : 'block',
		opacity : curOpacity
	});
}
function hideMaskLayer() {
	$('#splashLayer').hide();
}
function hideSplash() {
	if ($('#splashLayer')) {
		$('#splashLayer').hide();
		$('#messageDiv').hide();
	}
}
function initMaskLayer() {
	$("<div id='splashLayer'></div>").appendTo("body");

	$('#splashLayer').css({
		display : 'none',
		position : 'absolute',
		'background-color' : '#EEE',
		height : '100%',
		width : '100%',
		top : '0px',
		left : '0px'
	});
	$(
			"<div id='messageDiv' style='position: absolute;display:none;left:50%;top:50%;margin-left:-125px;margin-top:-120px;z-index:999;'><table width='250' border='0' align='center' cellpadding='0' cellspacing='1' bordercolor='#FF0000' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td height='60' style='font-size:12px;' id='infoTD' align='center' bordercolor='#1860A8' bgcolor='#DEE8F6'>"
					+ waitMsg + "</td></tr></table></div>").appendTo("body");
}
function ele(id) {
	return document.getElementById(id);
}

function resetHeight(object, diffPading) {
	object.height($("#form1").height() - diffPading);
}
function resetTabHeight(diffPading) {
	$(".photobox").height($("#form1").height() - diffPading);
}
function resetRightAreaHeight(diffPading) {
	$("#rightArea").height($("#form1").height() - diffPading);
}
function resetTreeHeight(diffPading) {
	$("#leftTree").height($("#form1").height() - diffPading);
	$("#treeArea").height($("#leftTree").height() - 7);
}

function doQuery(params) {
	if (!validate()) {
		return;
	}
	if (params && params.target) {
		$("#" + formTagId).attr('target', params.target);
	} else {
		$("#" + formTagId).attr('target', "_self");
	}
	$("#" + actionTypeTagId).val('query');
	showSplash(waitMsg);
	$("#" + formTagId).submit();
}
function goToBack() {
	var argLength = arguments.length;
	// showSplash(waitMsg); //先注释掉，因为在微信中体验不好
	$("#" + formTagId).attr('target', "_self");
	$("#" + actionTypeTagId).val('back');
	$("#" + formTagId).submit();
}
function goToMain() {
	showSplash(waitMsg);
	window.location.href = "index?MainWin";
}
function openRequest(actionType) {
	var theForm = ele(formTagId);
	if ((actionType == copyRequestActionValue
			|| actionType == updateRequestActionValue || actionType == viewDetailActionValue)
			&& !isSelectedRow()) {
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	showSplash(waitMsg);
	$("#" + actionTypeTagId).val(actionType);
	theForm.target = "_blank"
	theForm.submit();
	theForm.target = "";
	$("#" + actionTypeTagId).val("");
	hideSplash();
}

function doRequest(actionType, params) {
	var argLength = arguments.length;

	if (actionType != insertRequestActionValue && !isSelectedRow()) {
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	if (actionType == insertRequestActionValue) {
		var rsIdArray = rsIdTagId.split(",");
		for (var i = 0; i < rsIdArray.length; i++) {
			$("#" + rsIdArray[i]).val("");
		}
	}
	if (params && params.target) {
		$("#" + formTagId).attr('target', params.target);
	} else {
		showSplash(waitMsg);
		$("#" + formTagId).attr('target', "_self");
	}
	$("#" + actionTypeTagId).val(actionType);
	$("#" + formTagId).submit();
}
function writeErrorMsg(msg) {
	$('#errorMsg')
			.html(
					'<span style="float:left;color:#FF0000;">'
							+ msg
							+ '</span><span style="float:right;color:#000">双击关闭提示</span>');
	$('#errorMsg').css({
		display : 'block'
	});
	hideSplash();
}

function doSubmit(reqOptions) {
	var breakTag = false;
	var processValidate = true;
	if ($.mobile) {
		processValidate = false;
	}
	var showMessage = true;
	if (reqOptions
			&& reqOptions.doValidate
			&& (reqOptions.doValidate == 'false'
					|| reqOptions.doValidate.toLowerCase() == 'no' || reqOptions.doValidate
					.toLowerCase() == 'n')) {
		processValidate = false;
	}
	if (processValidate && !validate()) {
		return;
	}

	if (reqOptions && reqOptions.target) {
		$("#" + formTagId).attr('target', reqOptions.target);
	} else {
		$("#" + formTagId).attr('target', "_self");
	}
	if (reqOptions
			&& reqOptions.showMessage
			&& (reqOptions.showMessage == 'false'
					|| reqOptions.showMessage.toLowerCase() == 'no' || reqOptions.showMessage
					.toLowerCase() == 'n')) {
		showMessage = false;
	}

	if (reqOptions) {
		if (reqOptions.actionType) {
			$("#" + actionTypeTagId).val(reqOptions.actionType);
		}
		if (reqOptions.checkUnique
				&& (reqOptions.checkUnique.toLowerCase() == 'true'
						|| reqOptions.checkUnique.toLowerCase() == 'yes' || reqOptions.checkUnique
						.toLowerCase() == 'y')) {
			breakTag = true;
			showSplash(waitMsg, showMessage);
			$('#' + actionTypeTagId).val('checkUnique');
			postRequest(formTagId, {
				onComplete : function(responseText) {
					if (responseText == '') {
						var newReqOptions = reqOptions;
						newReqOptions.checkUnique = 'no';
						doSubmit(newReqOptions);
					} else {
						hideSplash();
						writeErrorMsg(responseText);
					}
				}
			});
		}
	}
	if (!breakTag) {
		showSplash(waitMsg, showMessage);
		ele(formTagId).submit();
	}
}
function setRsIdTag(rsIdTag) {
	rsIdTagId = rsIdTag;
	$(rsIdTagId).val('');
}
function doDelete(itemValue) {
	if (!isValid(itemValue)) {
		writeErrorMsg('请先选中一条记录!');
		return false;
	}
	$("#" + actionTypeTagId).val(deleteActionValue);
	if (confirm(confirmMsg)) {
		showSplash(deleteMsg);
		$('#' + formTagId).submit();
		return true;
	}
	return false;
}
function selectRow(rowObj, params) {
	if (currentRow == null) {
		currentRow = rowObj;
		currentRow.className = 'even';
	} else {
		currentRow.className = 'odd';
		currentRow = rowObj;
		currentRow.className = 'even';
	}

	if ('string' == typeof (arguments[1])) {
		var rsId = arguments[1];
		$("#" + rsIdTagId).val(rsId);
	} else if ('object' == typeof (arguments[1])) {
		var obj = arguments[1];
		for ( var itm in obj) {
			$("#" + itm).val(obj[itm]);
		}
	}
}

function resetBoxPostion(boxId, targetInputId) {
	var clientWidth = document.body.clientWidth;
	var tempClientHeight = $(document.body).height();
	var clientHeight = document.body.scrollHeight;

	var left = $("#" + targetInputId).offset().left;
	var top = $("#" + targetInputId).offset().top;
	var width = $("#" + targetInputId).width();

	var boxWidth = $("#" + boxId).width();
	var boxHeight = $("#" + boxId).height();
	var targetLeft = left + width / 2;
	targetLeft = targetLeft - boxWidth / 2;

	if ((targetLeft + boxWidth) >= clientWidth) {
		targetLeft = clientWidth - boxWidth - 2;
	} else if (targetLeft < 0) {
		targetLeft = 1;
	}

	var targetTop = top - 30;
	if ((targetTop + boxHeight) >= clientHeight) {
		targetTop = clientHeight - boxHeight - 2;
	}
	if (targetTop < 0) {
		targetTop = 1;
	}

	$("#" + boxId).css({
		top : targetTop,
		left : targetLeft
	});
}

function initDetailOpertionImage() {
	if (detailActionValue == $("#" + operaTypeTagId).val()) {
		if (!ele(saveImgBtnTagId)) {
			return;
		}
		setImgDisabled(saveImgBtnTagId, true);
		ele(saveImgBtnTagId).parentNode.disabled = true;

		var tdObj = ele(saveImgBtnTagId).parentNode;
		$(tdObj).data('onmouseover', $(tdObj).attr('onmouseover'));
		$(tdObj).data('onmouseout', $(tdObj).attr('onmouseout'));
		$(tdObj).data('onclick', $(tdObj).attr('onclick'));

		$(tdObj).attr('onclick', '');
		$(tdObj).attr('onmouseout', '');
		$(tdObj).attr('onmouseover', '');
		$(tdObj).css('background-color', '');
		$(tdObj).unbind();
	} else {
		if (!ele(modifyImgBtnTagId)) {
			return;
		}
		setImgDisabled(modifyImgBtnTagId, true);
		ele(modifyImgBtnTagId).parentNode.disabled = true;
		var tdObj = ele(modifyImgBtnTagId).parentNode;

		$(tdObj).data('onmouseover', $(tdObj).attr('onmouseover'));
		$(tdObj).data('onmouseout', $(tdObj).attr('onmouseout'));
		$(tdObj).data('onclick', $(tdObj).attr('onclick'));

		$(tdObj).attr('onclick', '');
		$(tdObj).attr('onmouseout', '');
		$(tdObj).attr('onmouseover', '');
		$(tdObj).css('background-color', '');
		$(tdObj).unbind();
	}
}
function textFocus(st) {
	st.style.backgroundColor = '#C4E5DE';
	st.style.color = '#000000';
}
function textBlur(st) {
	st.style.backgroundColor = '';
	st.style.color = '#000000'
}
function setColor(i, s, e, tabId) {
	var tabid = document.getElementById(tabId);
	if (tabid.rows[i].bgColor != '008080') {
		for (k = s; k < tabid.rows.length - e; k++) {
			if (k == i) {
				tabid.rows[k].bgColor = '#c4e5de';
			} else {
				tabid.rows[k].bgColor = '';
			}
		}
	}
}
function wrTitle(caption) {
	document
			.write("<table width=100% style='margin-top:10px;' border=0 align=center cellpadding=0 cellspacing=0 ><tr><td   width=100% align=center background=images/bg5.gif><table border=0 cellpadding=0 cellspacing=0><tr><td>&nbsp;&nbsp;&nbsp;</td><td bgcolor=#FFFFFF style='font-weight:bold;font-size:14px;'>"
					+ caption
					+ "</td></tr></table></td></tr><tr><td height=5></td></tr></table>");
}

function enableSave() {
	enableButton(saveImgBtnTagId);
	disableButton(modifyImgBtnTagId);

	$("#" + modifyImgBtnTagId).parent().css({
		'background-color' : ''
	});
	$("#" + operaTypeTagId).val(updateActionValue);
}
function enableButton(buttonId) {
	if (!ele(buttonId)) {
		return;
	}
	setImgDisabled(buttonId, false);
	ele(buttonId).parentNode.disabled = false;

	var tdObj = ele(buttonId).parentNode;
	if ($(tdObj).data('onclick')) {
		$(tdObj).unbind('click');
		var handler = $(tdObj).data('onclick');
		var HandlerWrapper = function(itemObj, itemHandler) {
			this.obj = itemObj;
			this.invoke = function() {
				if (typeof (itemHandler) == 'string') {
					this.obj.bind('click', function() {
						eval(itemHandler);
					});
				} else {
					this.obj.bind('click', itemHandler);
				}
			}
		}
		var handlerWrapperInst = new HandlerWrapper($(tdObj), handler);
		handlerWrapperInst.invoke();
	}
	if ($(tdObj).data('onmouseout')) {
		$(tdObj).unbind('mouseout');
		var handler = $(tdObj).data('onmouseout');
		var HandlerWrapper = function(itemObj, itemHandler) {
			this.obj = itemObj;
			this.invoke = function() {
				if (typeof (itemHandler) == 'string') {
					this.obj.bind('mouseout', function() {
						eval(itemHandler);
					});
				} else {
					this.obj.bind('mouseout', itemHandler);
				}
			}
		}
		var handlerWrapperInst = new HandlerWrapper($(tdObj), handler);
		handlerWrapperInst.invoke();
	}
	if ($(tdObj).data('onmouseover')) {
		$(tdObj).unbind('mouseover');
		var handler = $(tdObj).data('onmouseover');
		var HandlerWrapper = function(itemObj, itemHandler) {
			this.obj = itemObj;
			this.invoke = function() {
				if (typeof (itemHandler) == 'string') {
					this.obj.bind('mouseover', function() {
						eval(itemHandler)
					});
				} else {
					this.obj.bind('mouseover', itemHandler);
				}
			}
		}
		var handlerWrapperInst = new HandlerWrapper($(tdObj), handler);
		handlerWrapperInst.invoke();
	}
}
function disableButton(buttonId) {
	if (!ele(buttonId)) {
		return;
	}
	setImgDisabled(buttonId, true);
	ele(buttonId).parentNode.disabled = true;

	var tdObj = ele(buttonId).parentNode;
	if ($(tdObj).attr('onmouseover')) {
		$(tdObj).data('onmouseover', $(tdObj).attr('onmouseover'));
	}
	if ($(tdObj).attr('onmouseout')) {
		$(tdObj).data('onmouseout', $(tdObj).attr('onmouseout'));
	}
	if ($(tdObj).attr('onclick')) {
		$(tdObj).data('onclick', $(tdObj).attr('onclick'));
	}

	$(tdObj).attr('onclick', '');
	$(tdObj).attr('onmouseout', '');
	$(tdObj).attr('onmouseover', '');
	$(tdObj).unbind();
}
function setImgDisabled(btnImgId, disabledState) {
	var obj = document.getElementById(btnImgId);
	if (obj && obj.disabled != disabledState) {
		obj.disabled = disabledState;
		var className = obj.className;
		if (disabledState) {
			obj.className = className + "disabled";
		} else {
			obj.className = className.substring(0, className.length - 8);
		}
	}
}
function isValueValid(objId) {
	if (document.getElementById(objId)) {
		if ($('#' + objId).val() != 'null' && $('#' + objId).val() != '') {
			return true;
		}
	}
	return false;
}
function isSelectedRow() {
	var tagIdArray = rsIdTagId.split(',');
	for (var i = 0; i < tagIdArray.length; i++) {
		var tempTagId = tagIdArray[i];
		if (!isValueValid(tempTagId))
			return false;
	}
	return true;
}
function isValid(theValue) {
	if (theValue != 'null' && theValue != '') {
		return true;
	}
	return false;
}
function isWhitespace(s) {
	var whitespace = " \t\n\r";
	var i;
	for (i = 0; i < s.length; i++) {
		var c = s.charAt(i);
		if (whitespace.indexOf(c) >= 0) {
			return true;
		}
	}
	return false;
}
function replaceAll(text, sourceText, replaceText) {
	var pose = text.indexOf(sourceText);
	if (pose == -1) {
		return text;
	} else {
		var head = text.substring(0, pose + 1);
		head = head.replace(sourceText, replaceText);
		var tail = text.substring(pose + 1, text.length);
		head = head + replaceAll(tail, sourceText, replaceText);
		return head;
	}
}
function convertChar(parameter) {
	parameter = replaceAll(parameter, "%", "%25");
	parameter = replaceAll(parameter, "#", "%23");
	parameter = replaceAll(parameter, "&", "%26");
	parameter = replaceAll(parameter, "+", "%2B");
	return parameter;
}
function toUpperCase(formElement) {
	formElement.value = formElement.value.toUpperCase();
}
function convertChar(parameter) {
	parameter = replaceAll(parameter, "%", "%25");
	parameter = replaceAll(parameter, "#", "%23");
	parameter = replaceAll(parameter, "&", "%26");
	parameter = replaceAll(parameter, "+", "%2B");
	return parameter;
}
function LengthLimiter(textareaId, maxLength) {
	this.textAreaObj = document.getElementById(textareaId);
	this.maxLength = maxLength;
	this.attacthLimit();
}
LengthLimiter.prototype.attacthLimit = function() {
	var maxLengthTemp = this.maxLength;
	var textAreaObjTemp = this.textAreaObj;
	if (window.addEventListener) {
		this.textAreaObj.addEventListener("input", function() {
			if (textAreaObjTemp.value.length > maxLengthTemp) {
				textAreaObjTemp.value = textAreaObjTemp.value.substr(0,
						maxLengthTemp);
			}
		}, false);
	} else {
		this.textAreaObj.attachEvent("onpropertychange", function() {
			if (textAreaObjTemp.value.length > maxLengthTemp) {
				textAreaObjTemp.value = textAreaObjTemp.value.substr(0,
						maxLengthTemp);
			}
		});
	}
}
function BlankTrimer(inputId) {
	this.inputObj = document.getElementById(inputId);
	this.attachTrimer(inputId);
}
BlankTrimer.prototype.attachTrimer = function() {
	var inputObjTemp = this.inputObj;
	if (window.addEventListener) {
		this.inputObj.addEventListener("input", function() {
			var notValid = /(^\s)|(\s$)/;
			if (notValid.test(inputObjTemp.value)) {
				inputObjTemp.value = trim(inputObjTemp.value);
			}
		}, false);
	} else {
		this.inputObj.attachEvent("onblur", function() {
			inputObjTemp.value = trim(inputObjTemp.value);
		});
	}
}
function QuoteFilter(inputId) {
	this.inputObj = document.getElementById(inputId);
	this.executeFilter(inputId);
}
QuoteFilter.prototype.executeFilter = function() {
	var inputObjTemp = this.inputObj;
	if (window.addEventListener) {
		this.inputObj.addEventListener("input", function() {
			var pos = getp(inputObjTemp);
			pos = parseInt(pos) - 1;
			var value = inputObjTemp.value;
			inputObjTemp.value = trim(value).replace(/'/g, '')
					.replace(/"/g, '');
			if (inputObjTemp.value == value) {
				pos = pos + 1;
			}
			setp(inputObjTemp, pos);

		}, false);
	} else {
		this.inputObj.attachEvent("onblur", function() {
			inputObjTemp.value = trim(inputObjTemp.value).replace(/'/g, '')
					.replace(/"/g, '');
		});
		this.inputObj.attachEvent("onkeyup", function() {
			if (event.keyCode == 222) {
				var pos = getp(inputObjTemp);
				pos = parseInt(pos) - 1;
				var value = inputObjTemp.value;
				inputObjTemp.value = trim(value).replace(/'/g, '').replace(
						/"/g, '');
				if (inputObjTemp.value == value) {
					pos = pos + 1;
				}
				setp(inputObjTemp, pos);
			}
		});
	}
}
function ltrim(str) {
	for (var k = 0; k < str.length && isWhitespace(str.charAt(k)); k++)
		;
	return str.substring(k, str.length);
}
function rtrim(str) {
	for (var j = str.length - 1; j >= 0 && isWhitespace(str.charAt(j)); j--)
		;
	return str.substring(0, j + 1);
}
function trim(str) {
	return ltrim(rtrim(str));
}

function selectOrFocus(id) {
	try {
		ele(id).select();
	} catch (e) {
		ele(id).focus();
	}
}

function FormSelect(object) {
	this.obj = null;
	if (typeof (object) == 'string') {
		this.obj = ele(object);
	} else {
		this.obj = object;
	}
}
FormSelect.prototype.isExistItem = function(objItemValue) {
	var isExist = false;
	for (var i = 0; i < this.obj.options.length; i++) {
		if (this.obj.options[i].value == objItemValue) {
			isExist = true;
			break;
		}
	}
	return isExist;
}
FormSelect.prototype.addItem = function(objItemText, objItemValue) {
	if (this.isExistItem(objItemValue)) {
		alert("该Item的Value值已经存在");
	} else {
		var varItem = new Option(objItemText, objItemValue);
		this.obj.options.add(varItem);
	}
}
FormSelect.prototype.removeItem = function(objItemValue) {
	if (this.isExistItem(objItemValue)) {
		for (var i = 0; i < this.obj.options.length; i++) {
			if (this.obj.options[i].value == objItemValue) {
				this.obj.options.remove(i);
				break;
			}
		}
	} else {
		alert("不存在该项");
	}
}
FormSelect.prototype.removeSelectedItem = function() {
	var length = this.obj.options.length - 1;
	for (var i = length; i >= 0; i--) {
		if (this.obj[i].selected == true) {
			this.obj.options[i] = null;
		}
	}
}
FormSelect.prototype.updateItem = function(objItemText, objItemValue) {
	if (this.isExistItem(objItemValue)) {
		for (var i = 0; i < this.obj.options.length; i++) {
			if (this.obj.options[i].value == objItemValue) {
				this.obj.options[i].text = objItemText;
				break;
			}
		}
	} else {
		alert("不存在该项");
	}
}
FormSelect.prototype.setSelectedByText = function(objItemText) {
	var isExist = false;
	for (var i = 0; i < this.obj.options.length; i++) {
		if (this.obj.options[i].text == objItemText) {
			this.obj.options[i].selected = true;
			isExist = true;
			break;
		}
	}
}
FormSelect.prototype.setSelectedByValue = function(objItemValue) {
	this.obj.value = objItemValue;
}
FormSelect.prototype.getSelectedValue = function() {
	return this.obj.value;
}
FormSelect.prototype.getSelectedText = function() {
	return this.obj.options[this.obj.selectedIndex].text;
}
FormSelect.prototype.getSelectedIndex = function() {
	return this.obj.selectedIndex;
}
FormSelect.prototype.removeAll = function() {
	this.obj.options.length = 0;
}

CheckBoxGroup = {};
CheckBoxGroup.resetValue = function(groupId) {
	var group = document.getElementById(groupId);
	var inputArray = group.getElementsByTagName('input');
	for (i = 0; i < inputArray.length; i++) {
		var tempInput = inputArray[i];
		if (tempInput.type == 'checkbox') {
			tempInput.checked = false;
		}
	}
}

CheckBoxGroup.checkValue = function(groupId, tempId, tempText) {
	var group = document.getElementById(groupId);
	var inputArray = group.getElementsByTagName('input');
	$("#" + tempId).val('');
	$("#" + tempText).val('');
	for (i = 0; i < inputArray.length; i++) {
		var tempInput = inputArray[i];
		if (tempInput.type == 'checkbox') {
			if (tempInput.checked) {
				$("#" + tempId).val(
						$("#" + tempId).val() + "," + tempInput.value);
				$("#" + tempText).val(
						$("#" + tempText).val() + "," + tempInput.title);
			}
		}
	}
	var tempIdValue = $("#" + tempId).val();
	var tempTextValue = $("#" + tempText).val();
	if (tempIdValue.length > 0) {
		$("#" + tempId).val(tempIdValue.substring(1, tempIdValue.length));
		$("#" + tempText).val(tempTextValue.substring(1, tempTextValue.length));
	}
}

function Tab(tabName, tabHeader, paneIdPrefix, defaultFocusIndex) {
	this.tabObj;
	this.name = tabName;
	this.contentIdPrefix = paneIdPrefix;
	this.defaultIndex;
	if (defaultFocusIndex) {
		this.defaultIndex = defaultFocusIndex;
	} else {
		this.defaultIndex = 0;
	}
	if (typeof (tabHeader) == 'string') {
		this.tabObj = $("#" + tabHeader);
	} else {
		this.tabObj = tabHeader;
	}
	this.init();
}
Tab.prototype.init = function() {
	var children = this.tabObj.children();
	var count = children.length;
	for (var i = 0; i < count; i++) {
		var child = children[i];
		var caption = $(child).html();
		var divId = this.name + "_tab_" + i;
		var innerHtml;
		if (this.defaultIndex == i) {
			innerHtml = "<table height='26' border='0' cellpadding='0' cellspacing='0'><tr><td><img id='"
					+ divId
					+ "_Img01' src='images/tab/tab0_01.gif' width='6' height='26'></td><td style='cursor:pointer;background:url(images/tab/tab0_02.gif)' id='"
					+ divId
					+ "_Td02' height='26' nowrap>"
					+ caption
					+ "</td><td><img id='"
					+ divId
					+ "_Img03' src='images/tab/tab0_03.gif' width='13' height='26'></td></tr></table>";
		} else {
			innerHtml = "<table height='26' border='0' cellpadding='0' cellspacing='0'><tr><td><img id='"
					+ divId
					+ "_Img01' src='images/tab/tab_01.gif' width='6' height='26'></td><td style='cursor:pointer;background:url(images/tab/tab_02.gif)' id='"
					+ divId
					+ "_Td02' height='26' nowrap>"
					+ caption
					+ "</td><td><img id='"
					+ divId
					+ "_Img03' src='images/tab/tab_03.gif' width='13' height='26'></td></tr></table>";
		}
		$(child).html(innerHtml);
		new this.tabHeaderClickHandler(this.tabObj, this.name,
				this.contentIdPrefix, child);
	}
}
Tab.prototype.tabHeaderClickHandler = function(headerContainer, tabName,
		contentIdPrefix, curTab) {
	this.curTab = curTab;
	$(this.curTab)
			.bind(
					'click',
					function() {
						var children = headerContainer.children();
						var count = children.length;
						for (var i = 0; i < count; i++) {
							var childId = tabName + "_tab_" + i;
							var contentId = contentIdPrefix + i;
							if (document.getElementById(contentId)) {
								if (children[i] == curTab) {
									children[i].className = 'newarticle1';
									document.getElementById(childId + '_Img01').src = 'images/tab/tab0_01.gif';
									document.getElementById(childId + '_Td02').style.background = 'url(images/tab/tab0_02.gif)';
									document.getElementById(childId + '_Img03').src = 'images/tab/tab0_03.gif';
									document.getElementById(contentId).style.display = 'block';
								} else {
									children[i].className = 'newarticle2';
									document.getElementById(childId + '_Img01').src = 'images/tab/tab_01.gif';
									document.getElementById(childId + '_Td02').style.background = 'url(images/tab/tab_02.gif)';
									document.getElementById(childId + '_Img03').src = 'images/tab/tab_03.gif';
									document.getElementById(contentId).style.display = 'none';
								}
							}
						}
					});
}
Tab.prototype.focus = function(index) {
	var headerContainer = this.tabObj;
	var tabName = this.name;
	var contentIdPrefix = this.contentIdPrefix;

	var children = headerContainer.children();
	var count = children.length;
	for (var i = 0; i < count; i++) {
		var childId = tabName + "_tab_" + i;
		var contentId = contentIdPrefix + i;
		if (i == index) {
			children[i].className = 'newarticle1';
			document.getElementById(childId + '_Img01').src = 'images/tab/tab0_01.gif';
			document.getElementById(childId + '_Td02').style.background = 'url(images/tab/tab0_02.gif)';
			document.getElementById(childId + '_Img03').src = 'images/tab/tab0_03.gif';
			if (document.getElementById(contentId)) {
				document.getElementById(contentId).style.display = 'block';
			}
		} else {
			children[i].className = 'newarticle2';
			document.getElementById(childId + '_Img01').src = 'images/tab/tab_01.gif';
			document.getElementById(childId + '_Td02').style.background = 'url(images/tab/tab_02.gif)';
			document.getElementById(childId + '_Img03').src = 'images/tab/tab_03.gif';
			if (document.getElementById(contentId)) {
				document.getElementById(contentId).style.display = 'none';
			}
		}
	}
}

function stringToBoolean(string) {
	switch (string.toLowerCase()) {
	case "true":
	case "yes":
	case "1":
		return true;
	case "false":
	case "no":
	case "0":
	case null:
		return false;
	default:
		return Boolean(string);
	}
}

/*******************************************************************************
 * //native ajax start
 ******************************************************************************/
function sendRequest(targetUrl, params) {
	var theParam = '';
	if (params) {
		var asynchronousMode = true;
		if (params.asynchronous) {
			asynchronousMode = stringToBoolean(params.asynchronous);
		}
		if (params.onComplete) {
			var curDataType = "text";
			if (params.dataType) {
				curDataType = params.dataType;
			}
			$.ajax({
				url : targetUrl,
				type : 'GET',
				data : theParam,
				async : asynchronousMode,
				dataType : curDataType,
				success : params.onComplete
			});
		} else {
			var dataTypeTemp = "script";
			if (params.dataType) {
				dataTypeTemp = params.dataType;
			}
			$.ajax({
				url : targetUrl,
				type : 'GET',
				data : theParam,
				async : asynchronousMode,
				dataType : dataTypeTemp
			});
		}
	} else {
		$.ajax({
			url : targetUrl,
			type : 'GET',
			data : theParam,
			dataType : "script"
		});
	}
}
function postRequest(form, params) {
	var theForm;
	if (typeof (form) == 'string') {
		theForm = $("#" + form);
	} else {
		theForm = form;
	}
	var targetUrl = theForm.attr("action");
	if (params) {
		if (params.actionType) {
			$('#actionType').val(params.actionType);
		}
		var asynchronousMode = true;
		if (params.asynchronous) {
			asynchronousMode = stringToBoolean(params.asynchronous);
		}
		var theParam = theForm.serialize();
		if (params.onComplete) {
			var curDataType = "text";
			if (params.dataType) {
				curDataType = params.dataType;
			}
			$.ajax({
				url : targetUrl,
				type : 'POST',
				data : theParam,
				async : asynchronousMode,
				dataType : curDataType,
				success : params.onComplete
			});
		} else {
			var dataTypeTemp = "script";
			if (params.dataType) {
				dataTypeTemp = params.dataType;
			}
			$.ajax({
				url : targetUrl,
				type : 'POST',
				data : theParam,
				async : asynchronousMode,
				dataType : curDataType
			});
		}
	} else {
		$.ajax({
			url : targetUrl,
			type : 'POST',
			data : theParam,
			dataType : "script"
		});
	}
}
/*******************************************************************************
 * //native ajax end
 ******************************************************************************/

function processDashedBorder() {
	var objAs = document.getElementsByTagName("input");
	var objA;
	for (var i = 0; objA = objAs[i]; i++) {
		if (objA.type && objA.type == 'button')
			objA.onfocus = function() {
				this.blur()
			};
	}
}

/*******************************************************************************
 * page action control end
 ******************************************************************************/

function setFormAction(form, action, method) {
	if (action) {
		document.forms[form].setAttribute('action', action);
	}
	if (method) {
		document.forms[form].setAttribute('method', method);
	}
	document.forms[form].ec_eti.value = '';
}

var validation = {};

validation.checkNull = function(_value) {
	if (_value == null || _value == "")
		return true;
	return false;
}
validation.checkIP = function(strIP) {
	if (validation.checkNull(strIP))
		return false;
	var re = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g
	if (re.test(strIP)) {
		if (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256
				&& RegExp.$4 < 256)
			return true;
	}
	return false;
}
validation.checkMarkAddress = function(strMarkAddress) {
	if (checkNull(strMarkAddress))
		return false;
	var re = /^[A-Z0-9]{2}\-[A-Z0-9]{2}\-[A-Z0-9]{2}\-[A-Z0-9]{2}\-[A-Z0-9]{2}\-[A-Z0-9]{2}$/;
	if (re.test(strMarkAddress)) {
		return true;
	}
	return false;
}

validation.checkPhone = function(_phone) {
	if (checkNull(_phone)) {
		return false;
	} else if (isNaN(parseInt(_phone))) {
		return false;
	} else if (_phone.length < 8) {
		return false;
	}
	return true;
}
validation.checkEmail = function(_email) {
	if (!checkNull(_email)) {
		var elength = _email.length;
		if (elength < 5 || _email.indexOf("@") == -1
				|| _email.indexOf("@") > _email.lastIndexOf(".")) {
			alert("!");
			return false;
		}
	}
	return true;
}
validation.checkDateTime = function(theValue) {
	var dateLength = theValue.length;
	if ((dateLength != 10) && (dateLength != 16) && (dateLength != 19)) {
		return false;
	}
	var patrn = /^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}|\d{4}-\d{2}-\d{2} \d{2}:\d{2}|\d{4}-\d{2}-\d{2}|\d{4}\/\d{2}\/\d{2} \d{2}:\d{2}:\d{2}|\d{4}\/\d{2}\/\d{2} \d{2}:\d{2}|\d{4}\/\d{2}\/\d{2})$/
	if (!patrn.exec(theValue)) {
		return false;
	}
	return true;
}
validation.compareDate = function(beginDate, endDate) {
	var sDate = replaceAll(beginDate, "-", "");
	var eDate = replaceAll(endDate, "-", "");
	var intSDate = parseInt(sDate);
	var intEDate = parseInt(eDate);
	if (intEDate > intSDate) {
		return 1;
	} else if (intEDate < intSDate) {
		return -1;
	} else {
		return 0;
	}
}
validation.isValidDateRange = function(beginDate, endDate) {
	var beginValue = $("#" + beginDate).val();
	var endValue = $("#" + endDate).val();
	var diffValue = validation.compareDate(beginValue, endValue);
	if (diffValue < 0) {
		alert('终止日期不能小于开始日期!');
		return false;
	}
	return true;
}
validation.compareDateTime = function(beginTime, endTime) {
	var sTime = replaceAll(beginTime, "-", "").replaceAll(":", "");
	var eTime = replaceAll(endTime, "-", "").replaceAll(":", "");
	;
	var intSTime = parseInt(sTime);
	var intETime = parseInt(eTime);
	if (intETime > intSTime) {
		return 1;
	} else if (intETime < intSTime) {
		return -1;
	} else {
		return 0;
	}
}
validation.isValidTimeRange = function(beginTime, endTime) {
	var beginValue = $("#" + beginTime).val();
	var endValue = $("#" + endTime).val();
	var diffValue = validation.compareDate(beginValue, endValue);
	if (diffValue < 0) {
		alert('终止时间不能小于开始时间!');
		return false;
	}
	return true;
}
validation.isCharacter = function(s) {
	var i;
	var ichar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	for (i = 0; i < s.length; i++) {
		if (ichar.indexOf(s.charAt(i), 0) == -1) {
			return false;
		}
	}
	return true;
}
validation.isRawChar = function(str) {
	return (/^[0-9A-Za-z_.]+$/.test(str))
}
validation.isDigit = function(c) {
	return ((c >= "0") && (c <= "9"))
}
validation.isData = function(s) {
	var i;
	var seenDecimalPoint = false;
	if (s.length > 0) {
		if (s == "." || s == "-") {
			return false;
		}
		for (i = 0; i < s.length; i++) {
			if ((i == 0) && (s.charAt(i) == ".")) {
				return false;
			}
			if ((i == 0) && (s.charAt(i) == "-")) {
				i++;
			}
			var c = s.charAt(i);
			if ((c == ".") && !seenDecimalPoint) {
				seenDecimalPoint = true;
			} else {
				if (!validation.isDigit(c))
					return false;
			}
		}
		return true;
	} else {
		return false;
	}
}
validation.isPositiveInt = function(s) {
	// 是否为非负整数,包括0
	if (s.length > 0) {
		for (var i = 0; i < s.length; i++) {
			var c = s.charAt(i);
			if (!validation.isDigit(c))
				return false;
		}
		return true;
	} else {
		return false;
	}
}
validation.isInIntRange = function(s, smer, biger) {
	if (validation.isPositiveInt(s)) {
		if (Number(s) >= Number(smer) && Number(s) <= Number(biger)) {
			return true;
		}
	}
	return false;
}
validation.isNegativeInt = function(s) {
	// 是否负整数
	if (s.length > 0) {
		if (s.charAt(0) != "-") {
			return false;
		}
		if (!validation.isPositiveInt(s.substring(1, s.length))) {
			return false;
		}
		return true;
	} else {
		return false;
	}
}
validation.isPositiveData = function(s) {
	// 是否为正数，包括小数情况
	var patrn = /^\d+[.]?\d+|\d$/;
	if (!patrn.exec(s)) {
		alert('不是正数！');
		return false
	}
	return true;
}
validation.isNegativeData = function(s) {
	// 是否为负数，包括小数情况
	var patrn = /^-{1}\d+[.]?\d+|-{1}\d$/;
	if (!patrn.exec(s)) {
		return false
	}
	return true;
}
validation.isDigitOrChar = function(s) {
	var patrn = /^[A-Za-z0-9]+$/;
	if (!patrn.exec(s))
		return false;
	return true;
}
validation.containChinese = function(s) {
	if (/.*[\u4e00-\u9fa5]+.*$/.test(s)) {
		return true;
	}
	return false;
};
validation.checkChar8Num8Underline = function(s) {
	var reg = /^[a-zA-Z0-9_]+$/;
	if (reg.test(s)) {
		return true;
	}
	return false;
};

/** *****************************EctableMenu Start**************************** */

function EctableMenu(menuName, tableId) {
	this.keyMap = {
		Q : 81,
		W : 87,
		E : 69,
		R : 82,
		T : 84,
		A : 65,
		S : 83,
		D : 68,
		F : 70,
		G : 71,
		Z : 90,
		X : 88,
		C : 67,
		V : 86,
		B : 66
	};
	this.excludeNames = new ArrayList();
	this.handlerMap = new Map();
	this.menuName = menuName;
	this.tableId = tableId;
	eval(menuName + " = new ContextMenu('" + menuName + "','" + tableId + "');")
}
EctableMenu.prototype.addExcludeName = function(excludeName) {
	this.excludeNames.add(excludeName);
}
EctableMenu.prototype.build = function() {
	var excludeNames = this.excludeNames;
	var menuName = this.menuName;
	var handlerMap = this.handlerMap;
	var keyMap = this.keyMap;
	$("#__ToolBar__ table tr td")
			.each(
					function() {
						var text = $(this).text();
						var handler = $(this).attr('onclick');
						if (!handler) {
							handler = $(this).data('onclick');
						}
						var hotKey = $(this).attr('hotKey');
						var button = $(this).children(0);
						var isDisabled = button.attr('disabled');
						var display = $(this).css('display');
						if (!isDisabled && display != 'none') {
							if (!excludeNames.contains(text)) {
								if (text == '返回') {
									eval(menuName + ".addSperator('" + menuName
											+ "');");
								}
								if (hotKey && eval("keyMap." + hotKey)) {
									eval(menuName
											+ ".addItem(text,text+' ('+hotKey+')','"
											+ menuName + "',handler);")
								} else {
									eval(menuName + ".addItem(text,text,'"
											+ menuName + "',handler);");
								}
								handlerMap.put(hotKey, handler);
							}
						}
					});

	$('#' + this.menuName + '_extmenu_' + this.menuName).attr('tabindex', '0');

	$('#' + this.menuName + '_extmenu_' + this.menuName).bind('keydown',
			function(ievent) {
				var evt = ievent || event;
				var key = evt.keyCode || evt.which || evt.charCode;
				$('#' + menuName + '_extmenu_' + menuName).hide();
				for (var i = 0; i < handlerMap.size(); i++) {
					var element = handlerMap.element(i);
					var hotKey = element.key;
					if (key == eval('keyMap.' + hotKey)) {
						var handler = handlerMap.get(hotKey);
						if (typeof (handler) == 'string') {
							eval(handler);
						} else {
							handler.apply(handler.arguments);
						}
						break;
					}
				}
			});

	$('#' + this.tableId).bind('contextmenu', function(evt) {
		eval(menuName + ".onContextMenu(evt);");
	});
	eval(menuName + ".setup();");
}

EctableMenu.prototype.reset = function() {
	eval(this.menuName + ".clear();");
	this.handlerMap.clear();
	$('#' + this.menuName + '_extmenu_' + this.menuName).unbind('keydown');
}

/** *****************************EctableMenu End**************************** */

function buildConextmenu() {
	ectableMenu.build();
}
function refreshConextmenu() {
	ectableMenu.reset();
	ectableMenu.build();
}

/** *****************************DTreeMenu Start**************************** */

function DTreeMenu(menuName, treeAreaId) {
	this.keyMap = {
		Q : 81,
		W : 87,
		E : 69,
		R : 82,
		T : 84,
		A : 65,
		S : 83,
		D : 68,
		F : 70,
		G : 71,
		Z : 90,
		X : 88,
		C : 67,
		V : 86,
		B : 66
	};
	this.excludeNames = new ArrayList();
	this.handlerMap = new Map();
	this.menuName = menuName;
	this.treeAreaId = treeAreaId;
	eval(menuName + " = new ContextMenu('" + menuName + "','" + treeAreaId
			+ "');")
}
DTreeMenu.prototype.addExcludeName = function(excludeName) {
	this.excludeNames.add(excludeName);
}
DTreeMenu.prototype.build = function() {
	var excludeNames = this.excludeNames;
	var menuName = this.menuName;
	var handlerMap = this.handlerMap;
	var keyMap = this.keyMap;
	$("#_TreeToolBar_ tr td").each(
			function() {
				var handler = $(this).attr('onclick');
				if (!handler) {
					handler = $(this).data('onclick');
				}
				var hotKey = $(this).attr('hotKey');
				var button = $(this).children(0);
				var text = button.attr('title');
				var isDisabled = button.attr('disabled');
				var display = $(this).css('display');
				if (!isDisabled && display != 'none') {
					if (!excludeNames.contains(text)) {
						if (hotKey && eval("keyMap." + hotKey)) {
							eval(menuName
									+ ".addItem(text,text+' ('+hotKey+')','"
									+ menuName + "',handler);")
						} else {
							eval(menuName + ".addItem(text,text,'" + menuName
									+ "',handler);");
						}
						handlerMap.put(hotKey, handler);
					}
				}
			});

	$('#' + this.menuName + '_extmenu_' + this.menuName).attr('tabindex', '0');

	$('#' + this.menuName + '_extmenu_' + this.menuName).bind('keydown',
			function(ievent) {
				var evt = ievent || event;
				var key = evt.keyCode || evt.which || evt.charCode;
				$('#' + menuName + '_extmenu_' + menuName).hide();
				for (var i = 0; i < handlerMap.size(); i++) {
					var element = handlerMap.element(i);
					var hotKey = element.key;
					if (key == eval('keyMap.' + hotKey)) {
						var handler = handlerMap.get(hotKey);
						handler.apply(handler.arguments)
						break;
					}
				}
			});

	$('#' + this.treeAreaId).bind('contextmenu', function(evt) {
		eval(menuName + ".onContextMenu(evt);");
	});
	eval(menuName + ".setup();");
}

DTreeMenu.prototype.reset = function() {
	eval(this.menuName + ".clear();");
}

/** *****************************DTreeMenu End**************************** */

function refreshDTreeConextmenu() {
	dtreeContextMenu.reset();
	dtreeContextMenu.build();
}

function emptyText(inputId) {
	ele(inputId).value = '';
}

function banBackSpace(e) {
	var ev = e || window.event;
	var obj = ev.target || ev.srcElement;
	var t = obj.type || obj.getAttribute('type');
	var vReadOnly = obj.getAttribute('readonly');
	vReadOnly = (vReadOnly == "") ? false : vReadOnly;
	var flag1 = (ev.keyCode == 8
			&& (t == "password" || t == "text" || t == "textarea") && vReadOnly == "readonly") ? true
			: false;
	var flag2 = (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea") ? true
			: false;
	if (flag2) {
		return false;
	}
	if (flag1) {
		return false;
	}
}

// function __processForms__() {
// if (ele('operaType')) {
// if (ele('operaType').value == 'detail'){
// $(":input").each(function(){
// if ($(this).attr('name') != undefined){
// if ($(this).attr('readonly') == undefined){
// $(this).attr('canEdit','true');
// $(this).attr('readonly','readonly');
// }
// }
// });
// }else{
//			  $(":input[canEdit='true']").removeAttr('readonly'); 
//		  }
//		  $(':input[readonly]').css("background-color","#efefef");
//		  $(":input").each(function(){
//			  if ($(this).attr('name') != undefined){
//				  if ($(this).attr('readonly') == undefined){
//					  if ($(this).attr('canEdit') =='true'){
//						  $(this).css("background-color","#fff");		
//					  }
//				  }
//			  }
//		  });		  
//	}
//}


window.onload = function() {
	document.onkeypress = banBackSpace;
	document.onkeydown = banBackSpace;
	//__processForms__();
}

$(function() {
	$(document).dblclick(function() {
		clearSelection();
	});
})