<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<%
String sessionId = session.getId();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>文件上传</title>
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Cache-Control" content="no-cache" />
<style>
.bigHref{
	font-size: 14px;
	font-weight: bold;
}
</style>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link href="uploader/uploadify.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-1.7.1.js" language="javascript"></script>
<script language="javascript" type="text/javascript">  
document.write("<script type='text/javascript' " +"src='uploader/jquery.uploadify.js?" + new Date() + "'></s" + "cript>");  
</script>
</head>
<body>
<div id="fileQueue">
</div>
<input type="file" name="uploadify" id="uploadify" />
<p>
<a class="bigHref" href="javascript:doSequenceUpload()">上 传</a> | <a  class="bigHref" href="javascript:$('#uploadify').uploadify('cancel')">取 消</a>
<br></br>
<span>最多5个文件批量上传</span>
</p>
</body>
</html>
<script type="text/javascript">
var batchUploadCount = 0;
var sucessCount = 0;
$(document).ready(function() {
	$("#uploadify").uploadify({
		'height':30,
		'swf':'<%=request.getContextPath()%>/uploader/uploadify.swf',
		'uploader':'<%=request.getContextPath()%>/index;jsessionid=<%=sessionId%>?ResourseUploader&actionType=uploadFile',
		'width':80,
		//'fileTypeDesc':'Image Files',
		//'fileTypeExts':'*.gif; *.jpg; *.png',
		'fileTypeDesc':'<%=pageBean.inputValue("GRP_RES_TYPE_DESC")%>',
		'fileTypeExts':'<%=pageBean.inputValue("GRP_RES_TYPE_EXTS")%>',
		'auto':false,
		'queueID':'fileQueue',
		'queueSizeLimit':5,
		'removeTimeout':1,
		'multi':true,
		'simUploadLimit':5,
		'formData':{'columnId':'<%=pageBean.inputValue("GRP_ID")%>'},
		'sizeLimit':'<%=pageBean.inputValue("GRP_RES_SIZE_LIMIT")%>',
		'buttonText':'选择文件',	
        'onSelect': function (file) {
        	
        },
        /*
        'onSelectError': function (file, errorCode, errorMsg) {  
            switch (errorCode) {  
                case -100:  
                    alert("上传的文件数量已经超出系统限制的" + jQuery('#file_upload').uploadify('settings', 'queueSizeLimit') + "个文件！");  
                    break;  
                case -110:  
                    alert("文件 [" + file.name + "] 大小超出系统限制的" + jQuery('#uploadify').uploadify('settings', 'fileSizeLimit') + "大小！");  
                    break;  
                case -120:  
                    alert("文件 [" + file.name + "] 大小异常！");  
                    break;  
                case -130:  
                    alert("文件 [" + file.name + "] 类型不正确！");  
                    break;  
            }  
	      },
        */
		'onUploadSuccess': function(fileObj, response, data) {//当单个文件上传完成后触发
            //event:事件对象(the event object)
            //ID:该文件在文件队列中的唯一表示
            //fileObj:选中文件的对象，他包含的属性列表
            //response:服务器端返回的Response文本，我这里返回的是处理过的文件名称
            //data：文件队列详细信息和文件上传的一般数据
           // alert("文件:" + fileObj.name + " 上传成功！");
			//alert("response is " + response);
			$('#uploadify').uploadify('upload');
        },
        'onUploadStart': function (file) {
        	  //alert("文件:" + file.name + " 上传开始！");
        },
        'onQueueComplete':function(queueData){
			if (queueData.uploadsSuccessful >= batchUploadCount){
        		parent.refreshTree();
        	}
        },    
        'onError': function(event, queueID, fileObj) {//当单个文件上传出错时触发
            alert("文件:" + fileObj.name + " 上传失败！");
        }
	});
});

function doSequenceUpload(){
	batchUploadCount = $(".uploadify-queue-item").size();
	$('#uploadify').uploadify('upload');
}

</script>