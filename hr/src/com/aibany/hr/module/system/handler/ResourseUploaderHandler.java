package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.FileUploadHandler;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;
import com.aibany.hr.module.system.service.WcmGeneralGroup8ContentManage;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ResourseUploaderHandler
  extends FileUploadHandler
{
  public ViewRenderer prepareDisplay(DataParam param)
  {
    String grpId = param.get("GRP_ID");
    WcmGeneralGroup8ContentManage resourseManage = (WcmGeneralGroup8ContentManage)lookupService(WcmGeneralGroup8ContentManage.class);
    DataParam queryParam = new DataParam(new Object[] { "GRP_ID", grpId });
    DataRow row = resourseManage.queryTreeRecord(queryParam);
    String resTypeDesc = row.stringValue("GRP_RES_TYPE_DESC");
    String resTypeExts = row.stringValue("GRP_RES_TYPE_EXTS");
    setAttributes(param);
    setAttribute("GRP_RES_TYPE_DESC", resTypeDesc);
    setAttribute("GRP_RES_TYPE_EXTS", resTypeExts);
    return new LocalRenderer(getPage());
  }
  
  @PageAction
  public ViewRenderer uploadFile(DataParam param) {
    String responseText = "fail";
    try {
      DiskFileItemFactory fac = new DiskFileItemFactory();
      ServletFileUpload fileFileUpload = new ServletFileUpload(fac);
      fileFileUpload.setHeaderEncoding("utf-8");
      List fileList = null;
      fileList = fileFileUpload.parseRequest(this.request);
      
      Iterator it = fileList.iterator();
      String name = "";
      String fileFullPath = null;
      
      File filePath = null;
      while (it.hasNext()) {
        FileItem item = (FileItem)it.next();
        if (item.isFormField()) {
          String fieldName = item.getFieldName();
          if (fieldName.equals("columnId")) {
            String columnId = item.getString();
            this.resourceParam.put("GRP_ID", columnId);
            filePath = buildResourseSavePath(columnId);
          }
        }
        else {
          name = item.getName();
          
          this.resourceParam.put("RES_NAME", name);
          String location = this.resourceRelativePath + "/" + name;
          this.resourceParam.put("RES_LOCATION", location);
          
          fileFullPath = filePath.getAbsolutePath() + File.separator + name;
          long resourceSize = item.getSize();
          this.resourceParam.put("RES_SIZE", String.valueOf(resourceSize));
          String suffix = name.substring(name.lastIndexOf("."));
          this.resourceParam.put("RES_SUFFIX", suffix);
          this.resourceParam.put("RES_SHAREABLE", "Y");
          File tempFile = new File(fileFullPath);
          if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
          }
          item.write(tempFile);
          
          insertResourceRecord();
        } }
      responseText = "success";
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new AjaxRenderer(responseText);
  }
  
  public void insertResourceRecord() {
    WcmGeneralGroup8ContentManage resourseManage = (WcmGeneralGroup8ContentManage)lookupService(WcmGeneralGroup8ContentManage.class);
    resourseManage.createtContentRecord("WcmGeneralResource", this.resourceParam);
  }
  
  protected File buildResourseSavePath(String columnId)
  {
    File result = null;
    String resourseFilePath = File.separator + "workdata" + 
      File.separator + "image" + File.separator + "resourse" + File.separator + columnId + 
      File.separator + DateUtil.getYear() + File.separator + DateUtil.getMonth() + File.separator + DateUtil.getDay();
    
    this.resourceRelativePath = 
      ("https://img.aibany.com:8090//resourse/" + columnId + "/" + DateUtil.getYear() + "/" + DateUtil.getMonth() + "/" + DateUtil.getDay());
    result = new File(resourseFilePath);
    return result;
  }
}
