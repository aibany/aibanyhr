package com.aibany.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;
import com.agileai.hotweb.controller.core.TreeAndContentManageListHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.ListUtil;
import com.agileai.util.StringUtil;
import com.aibany.hr.module.system.service.WcmGeneralGroup8ContentManage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WcmGeneralGroup8ContentListHandler extends TreeAndContentManageListHandler
{
  public WcmGeneralGroup8ContentListHandler()
  {
    this.serviceId = buildServiceId(WcmGeneralGroup8ContentManage.class);
    this.rootColumnId = "77777777-7777-7777-7777-777777777777";
    this.defaultTabId = "WcmGeneralResource";
    this.columnIdField = "GRP_ID";
    this.columnNameField = "GRP_NAME";
    this.columnParentIdField = "GRP_PID";
    this.columnSortField = "GRP_ORDERNO";
  }
  
  public ViewRenderer prepareDisplay(DataParam param)
  {
    initParameters(param);
    setAttributes(param);
    String rootColumnId = param.get("rootColumnId", this.rootColumnId);
    String columnId = param.get("columnId", rootColumnId);
    setAttribute("columnId", columnId);
    setAttribute("isRootColumnId", String.valueOf(this.rootColumnId.equals(columnId)));
    
    TreeBuilder treeBuilder = provideTreeBuilder(param);
    TreeModel treeModel = treeBuilder.buildTreeModel();
    TreeModel filterTreeModel = null;
    TreeModel childModel = (TreeModel)treeModel.getChildrenMap().get(columnId);
    if (childModel != null) {
      filterTreeModel = childModel;
    } else {
      filterTreeModel = treeModel;
    }
    
    String menuTreeSyntax = getTreeSyntax(treeModel, new StringBuffer());
    setAttribute("menuTreeSyntax", menuTreeSyntax);
    String tabId = param.get("_tabId_", this.defaultTabId);
    
    if (!"_base_".equals(tabId)) {
      param.put("columnId", columnId);
      List<DataRow> rsList = getService().findContentRecords(filterTreeModel, tabId, param);
      setRsList(rsList);
    } else {
      DataParam queryParam = new DataParam(new Object[] { this.columnIdField, columnId });
      DataRow row = getService().queryTreeRecord(queryParam);
      setAttributes(row);
    }
    
    setAttribute("_tabId_", tabId);
    setAttribute("_tabIndex_", Integer.valueOf(getTabIndex(tabId)));
    
    FormSelect isShareable = FormSelectFactory.create("BOOL_DEFINE");
    initMappingItem("RES_SHAREABLE", isShareable.getContent());
    
    processPageAttributes(param);
    return new LocalRenderer(getPage());
  }
  
  protected String getTreeSyntax(TreeModel treeModel, StringBuffer treeSyntax)
  {
    String result = null;
    try {
      treeSyntax.append("<script type='text/javascript'>");
      treeSyntax.append("d = new dTree('d');");
      String rootId = treeModel.getId();
      String rootName = treeModel.getName();
      treeSyntax.append("d.add('" + rootId + "',-1,'" + rootName + "',\"javascript:refreshContent('" + rootId + "')\");");
      treeSyntax.append("\r\n");
      buildTreeSyntax(treeSyntax, treeModel);
      treeSyntax.append("\r\n");
      treeSyntax.append("$('#treeArea').html(d.toString());");
      treeSyntax.append("\r\n");
      String currentColumnId = getAttributeValue("columnId");
      
      treeSyntax.append("d.s(d.getIndex('").append(currentColumnId).append("'));");
      treeSyntax.append("\r\n");
      
      treeSyntax.append("</script>");
      result = treeSyntax.toString();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }
  
  protected void processPageAttributes(DataParam param) {
    String tabId = param.get("_tabId_", this.defaultTabId);
    
    "WcmGeneralResource".equals(tabId);
  }
  
  protected void initParameters(DataParam param)
  {
    String tabId = param.get("_tabId_", this.defaultTabId);
    
    if ("WcmGeneralResource".equals(tabId)) {
      initParamItem(param, "resName", "");
      initParamItem(param, "suffix", "");
    }
  }
  
  public ViewRenderer doDeleteTreeNodeAction(DataParam param) {
    String rspText = "success";
    String columnId = param.get("columnId");
    TreeAndContentManage service = getService();
    DataParam queryParam = new DataParam(new Object[] { this.columnIdField, columnId });
    DataRow row = getService().queryTreeRecord(queryParam);
    String isSystem = row.stringValue("GRP_IS_SYSTEM");
    if ("Y".equals(isSystem)) {
      rspText = "isSystem";
      return new AjaxRenderer(rspText);
    }
    List<DataRow> childRecords = service.queryChildTreeRecords(columnId);
    if (!ListUtil.isNullOrEmpty(childRecords)) {
      rspText = "hasChild";
      return new AjaxRenderer(rspText);
    }
    List<String> tabList = getTabList();
    for (int i = 0; i < tabList.size(); i++) {
      String tabId = (String)tabList.get(i);
      if (!"_base_".equals(tabId)) {
        List<DataRow> records = service.findContentRecords(null, tabId, new DataParam(new Object[] { "columnId", columnId }));
        if (!ListUtil.isNullOrEmpty(records)) {
          rspText = "hasContent";
          return new AjaxRenderer(rspText);
        }
      } }
    service.deleteTreeRecord(columnId);
    return new AjaxRenderer(rspText);
  }
  
  protected TreeBuilder provideTreeBuilder(DataParam param)
  {
    WcmGeneralGroup8ContentManage service = getService();
    List<DataRow> menuRecords = service.findTreeRecords(new DataParam());
    TreeBuilder treeBuilder = new TreeBuilder(menuRecords, 
      this.columnIdField, 
      this.columnNameField, 
      this.columnParentIdField);
    
    String rootColumnId = param.get("rootColumnId");
    if (!StringUtil.isNullOrEmpty(rootColumnId)) {
      treeBuilder.setRootId(rootColumnId);
    }
    return treeBuilder;
  }
  
  protected List<String> getTabList() {
    List<String> result = new ArrayList();
    result.add("WcmGeneralResource");
    
    return result;
  }
  
  protected WcmGeneralGroup8ContentManage getService() {
    return (WcmGeneralGroup8ContentManage)lookupService(getServiceId());
  }
}
