package org.jboss.as.jbossws.jbqa8608;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "output")
public class OutputName {
  private String name;

  public OutputName() {
  }

  public OutputName(String name) {
    this.name = name;
  }

  @XmlElement(nillable = false, required = true)
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
