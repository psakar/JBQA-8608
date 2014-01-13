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
public class CustomSchemaValidationIT
{
  private static final String name = "CustomSchemaValidation";
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
        //.setManifest(new StringAsset("Manifest-Version: 1.0\n" + "Dependencies: org.apache.cxf, org.jboss.modules\n"))//FIXME is modules needed ?
        .addAsWebInfResource(new File(contextPath, "WEB-INF/web.xml"))
        .addAsWebInfResource(new File(contextPath, "WEB-INF/jbossws-cxf.xml"))
        //.addAsManifestResource(new File(contextPath, "WEB-INF/jbossws-cxf.xml"), "cxf/cxf.xml")
        //.addAsWebInfResource(new File(contextPath, "WEB-INF/jboss-deployment-structure.xml"))
        //.addAsManifestResource(new StringAsset("Dependencies: org.springframework.spring\n"), "MANIFEST.MF")
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

  @Test
  public void testOutputValidationFailureIsHandledByCustomSchemaValidationHandler() throws Exception
  {
    String inputName = SET_OUPUT_NAME_TO_NULL;
    OutputName result = port.hello(inputName);
    assertEquals(null, result.getName());
  }

  @Test
  public void testOuputForInputName() throws Exception
  {
    String inputName = "name";
    OutputName result = port.helloValidateInput(new InputName(inputName));
    assertEquals(inputName, result.getName());
  }

  @Test
  public void testOutputForMissingInputName() throws Exception
  {
    OutputName result = port.helloValidateInput(null);
    assertEquals(NULL_INPUT, result.getName());
  }

  @Test
  public void testInputNameValidationFailureIsHandledByCustomSchemaValidationHandler() throws Exception
  {
    OutputName result = port.helloValidateInput(new InputName());
    assertEquals(NULL_INPUT_NAME, result.getName());
  }

  @Test
  public void testOutputValidationFailreIsHandledByCustomSchemaValidationHandlerForInputName() throws Exception
  {
    String inputName = HelloWSImpl.SET_OUPUT_NAME_TO_NULL;
    OutputName result = port.helloValidateInput(new InputName(inputName));
    assertEquals(null, result.getName());
  }

}
