<%@ page pageEncoding="utf-8"%>
<script language="javascript">
function getElementsClass(classnames) {
	var classobj = new Array();//定义数组 
	var classint = 0;//定义数组的下标 
	var tags = document.getElementsByTagName("*");//获取HTML的所有标签 
	for ( var i in tags) {//对标签进行遍历 
		if (tags[i].nodeType == 1) {//判断节点类型 
			if (tags[i].getAttribute("class") == classnames)//判断和需要CLASS名字相同的，并组成一个数组 
			{
				classobj[classint] = tags[i];
				classint++;
			}
		}
	}
	return classobj;//返回组成的数组 
}

var signInTds = getElementsClass("signinTime");
var signOutTds = getElementsClass("signoutTime");
if (signInTds.length > 0 && signOutTds == signOutTds) {
	for (var i = 0; i < signInTds.length; i++) {
		var inTd = signInTds[i];
		var outTd = signOutTds[i];
		var inText = inTd.innerText;
		var outText = outTd.innerText;
		
		if (inText > "08:00") {
			inTd.style.color="red";
		}
		if(inText >= "17:00") {
			inTd.style.color="green";
		}
		outTd.style.color="green";
		if(inText < "17:00" && outText < "17:00") {
			outTd.style.color="red";
		}
		if(inText >="17:00" && outText < "04:30") {
			outTd.style.color="red";
		}
	}
	
}

</script>