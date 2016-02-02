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
package de.openknowledge.jsf.component;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class ValueObjectComponentSimpleTest {

  @Drone
  private WebDriver browser;
  
  @Deployment
  public static WebArchive deployment() {
    return Deployments.phoneNumberDeployment()
        .addAsWebResource("phoneNumberTest.xhtml");
  }

  @Test
  public void page(@InitialPage PhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    page.getPhoneNumber().setAreaCode("0441");
    page.submit();
  }
}
