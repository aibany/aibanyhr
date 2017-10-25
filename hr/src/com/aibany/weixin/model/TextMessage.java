package com.aibany.weixin.model;

public class TextMessage extends BaseMessage {
  private String Content;
  
  public String getContent() {
    return this.Content;
  }
  
  public void setContent(String content) {
    this.Content = content;
  }
}
