package com.redhat.gss.jaxws;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class MyValidationEventHandler implements ValidationEventHandler {

  //private static Logger log = Logger.getLogger(MyValidationEventHandler.class);

  @Override
  public boolean handleEvent(ValidationEvent event) {
    //log.warn(event.getMessage());
    System.err.println(event.getMessage());
    throw new RuntimeException("FIXME");//FIXME
//    return true;
  }
}
