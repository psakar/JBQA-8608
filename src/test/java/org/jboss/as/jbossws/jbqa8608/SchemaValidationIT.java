package org.jboss.as.jbossws.jbqa8608;
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import static org.jboss.as.jbossws.jbqa8608.HelloWSImpl.*;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
@RunAsClient
public class SchemaValidationIT
{
  private static final String name = "schemaValidation";
  private static final String NAMESPACE = TestUtils.createNamespaceFromPackageOfClass(HelloWS.class);
  private static final QName SERVICE_QNAME = new QName(NAMESPACE, HelloWS.class.getSimpleName());
  private static final String ENDPOINT_URL = "http://" + TestUtils.getServerBindAddress() + ":" + TestUtils.getServerBindPort() + "/" + name + "/hello";
  private static final String WSDL_URL = ENDPOINT_URL + "?wsdl";
  private HelloWS port;

  @Deployment
  static WebArchive createDeployment() throws Exception {

    String contextPath = "src/main/webapp";
    WebArchive archive = ShrinkWrap
        .create(WebArchive.class, name + ".war")
        .addAsWebInfResource(new File(contextPath, "WEB-INF/web.xml"))
        .addAsWebInfResource(new File(contextPath, "WEB-INF/jbossws-cxf-only-schema-validation.xml"), "jbossws-cxf.xml")
        .addClass(CustomSchemaValidationEventHandler.class)
        .addClass(HelloWS.class)
        .addClass(HelloWSImpl.class)
        .addClass(OutputName.class)
        .addClass(InputName.class)
        ;
    return TestUtils.backupArchiveForDebug(archive);
  }

  @Before
  public void before() throws Exception {
    URL wsdl = new URL(WSDL_URL);
    Service service = Service.create(wsdl, SERVICE_QNAME);
    QName portQName = new QName(NAMESPACE, "hello");
    port = service.getPort(portQName, HelloWS.class);
  }

  @Test
  public void testOutput() throws Exception
  {
    String inputName = "name";
    OutputName result = port.hello(inputName);
    assertEquals(inputName, result.getName());
  }

  @Test(expected = SOAPFaultException.class)
  public void testOutputValidationFails() throws Exception
  {
    String inputName = SET_OUPUT_NAME_TO_NULL;
    port.hello(inputName);
  }

  @Test
  public void testOuputForInputName() throws Exception
  {
    String inputName = "name";
    OutputName result = port.helloValidateInput(new InputName(inputName));
    assertEquals(inputName, result.getName());
  }

  @Test
  public void testInputNameValidationFailureIsHandledByCustomSchemaValidationHandler() throws Exception
  {
    OutputName result = port.helloValidateInput(null);
    assertEquals(NULL_INPUT, result.getName());
  }

  @Test(expected = SOAPFaultException.class)
  public void testInputNameValidationFails() throws Exception
  {
    OutputName result = port.helloValidateInput(new InputName());
    assertEquals(NULL_INPUT_NAME, result.getName());
  }

  @Test(expected = SOAPFaultException.class)
  public void testOutputValidationFailsForInputName() throws Exception
  {
    String inputName = HelloWSImpl.SET_OUPUT_NAME_TO_NULL;
    port.helloValidateInput(new InputName(inputName));
  }

}
