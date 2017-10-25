package com.aibany.hr.service.demo;

public class HelloWorldImpl implements HelloWorld {
  public String sayHi(String theGirlName) {
    String result = null;
    result = "Hello " + theGirlName + " !";
    return result;
  }
}
