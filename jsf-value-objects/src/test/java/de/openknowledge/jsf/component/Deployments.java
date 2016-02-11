package de.openknowledge.jsf.component;

import de.openknowledge.domain.AbstractValueObject;
import de.openknowledge.domain.address.Address;
import de.openknowledge.domain.telephone.PhoneNumber;
import de.openknowledge.jsf.component.address.AddressController;
import de.openknowledge.jsf.component.telephone.PhoneNumberController;
import de.openknowledge.jsf.converter.AbstractSimpleValueObjectConverter;
import de.openknowledge.jsf.converter.address.CityNameConverter;
import de.openknowledge.jsf.converter.telephone.AreaCodeConverter;
import de.openknowledge.jsf.validator.telephone.PhoneNumberValidator;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;


/*
 * Copyright (C) Arne Limburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
import javax.faces.webapp.FacesServlet;

public class Deployments {

  public static WebArchive jsfDeployment() {
    PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
    return ShrinkWrap.create(WebArchive.class)
        .addAsLibraries(pom.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile())
        .addClass(ValueObjectComponent.class)
        .addAsWebResource("META-INF/resources/ok/valueObject.xhtml", "resources/ok/valueObject.xhtml")
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
        .setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class)
          .addDefaultNamespaces()
          .createContextParam()
            .paramName("javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL")
            .paramValue("true")
          .up()
          .createServlet()
            .servletName("Faces Servlet")
            .servletClass(FacesServlet.class.getName())
          .up()
          .createServletMapping()
            .servletName("Faces Servlet")
            .urlPattern("*.xhtml")
          .up()
          .exportAsString()));
  }

  public static WebArchive phoneNumberDeployment() {
    return jsfDeployment()        
        .addPackage(AbstractValueObject.class.getPackage())
        .addClass(PhoneNumberController.class)
        .addPackage(PhoneNumber.class.getPackage())
        .addPackage(PhoneNumberValidator.class.getPackage())
        .addClass(AbstractSimpleValueObjectConverter.class)
        .addPackage(AreaCodeConverter.class.getPackage());
  }

  public static WebArchive addressDeployment() {
    return jsfDeployment()        
        .addPackage(AbstractValueObject.class.getPackage())
        .addClass(AddressController.class)
        .addPackage(Address.class.getPackage())
        .addClass(AbstractSimpleValueObjectConverter.class)
        .addPackage(CityNameConverter.class.getPackage());
  }
}
