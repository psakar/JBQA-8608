package org.jboss.as.jbossws.jbqa8608;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class CustomSchemaValidationEventHandler implements ValidationEventHandler {

  public CustomSchemaValidationEventHandler() {
    info(getClass().getSimpleName() + " created");
  }

  private void info(String info) {
    System.err.println(info);
  }

  @Override
  public boolean handleEvent(ValidationEvent event) {
    info(getClass().getSimpleName() + " handle successfully event " + event);
    return true;
  }
}
