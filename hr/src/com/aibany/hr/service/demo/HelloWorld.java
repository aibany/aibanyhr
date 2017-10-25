package com.aibany.hr.service.demo;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public abstract interface HelloWorld
{
  @WebMethod
  public abstract String sayHi(@WebParam(name="theGirlName") String paramString);
}
