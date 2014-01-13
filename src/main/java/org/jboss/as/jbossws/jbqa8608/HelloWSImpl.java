/*
 * To the extent possible under law, Red Hat, Inc. has dedicated all copyright
 * to this software to the public domain worldwide, pursuant to the CC0 Public
 * Domain Dedication. This software is distributed without any warranty.  See
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package org.jboss.as.jbossws.jbqa8608;

//import javax.xml.ws.spi.Provider;

//import org.jboss.logging.Logger;
//import javax.naming.InitialContext;
//import org.apache.cxf.annotations.EndpointProperty;
//import org.apache.cxf.annotations.EndpointProperties;

// @org.apache.cxf.annotations.Logging(pretty=true)
// @org.apache.cxf.feature.Features(features={"org.apache.cxf.feature.LoggingFeature"})
// @org.jboss.ws.annotation.SchemaValidation
@javax.jws.WebService(serviceName="HelloWS", portName="hello")
// @EndpointProperties (
  // @EndpointProperty(key="set-jaxb-validation-event-handler", value={"false"})
  // @EndpointProperty(key="jaxb-validation-event-handler", value="org.jboss.as.jbossws.jbqa8608.CustomSchemaValidationEventHandler")
// )
public class HelloWSImpl implements HelloWS {
  public static final String SET_OUPUT_NAME_TO_NULL = "SET_OUPUT_NAME_TO_NULL";

  public static final String NULL_INPUT = "NULL_INPUT";
  public static final String NULL_INPUT_NAME = "NULL_INPUT_NAME";

  //private final Logger log = Logger.getLogger(this.getClass().getName());

  @Override
  public OutputName hello(String name) {
    System.err.println("Hello, " + name);
    if (name == null)
      return new OutputName(NULL_INPUT);
    if (SET_OUPUT_NAME_TO_NULL.equals(name))
      return new OutputName();
    return new OutputName(name) ;
  }

  @Override
  public OutputName helloValidateInput(InputName input) {
    System.err.println("Hello, " + (input == null ? NULL_INPUT_NAME : input.getName()));
    if (input == null)
      return new OutputName(NULL_INPUT);
    if (input.getName() == null)
      return new OutputName(NULL_INPUT_NAME);
    if (SET_OUPUT_NAME_TO_NULL.equals(input.getName()))
      return new OutputName();
    return new OutputName(input.getName()) ;
  }


}
