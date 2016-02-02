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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.jboss.arquillian.warp.Warp.initiate;
import static org.jboss.arquillian.warp.client.filter.http.HttpFilters.request;
import static org.junit.Assert.assertThat;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.client.filter.http.HttpMethod;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class ValueObjectComponentEmptyValueTest {

  @Drone
  private WebDriver browser;
  
  @Deployment
  public static WebArchive deployment() {
    return Deployments.phoneNumberDeployment()
        .addAsWebResource("emptyPhoneNumberTest.xhtml");
  }

  @Test
  public void validPhoneNumber(final @InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    initiate(new Activity() {
      public void perform() {
        page.getPhoneNumber().setAreaCode("040");
        page.getPhoneNumber().setSubscriberNumber("123456");
        page.submit();
        assertThat(page.getAreaCodeMessage().getText(), is(""));
        assertThat(page.getSubscriberNumberMessage().getText(), is(""));
        assertThat(page.getPhoneNumberMessage().getText(), is(""));
        assertThat(page.getSubmitMessage().getText(), is("The data was successfully saved"));
      }
    })
    .observe(request().method().equal(HttpMethod.POST))
    .inspect(new Inspection() {

      private static final long serialVersionUID = 1L;

      @Inject
      private PhoneNumberController controller;

      @AfterPhase(Phase.UPDATE_MODEL_VALUES)
      public void verifyPhoneNumberIsSet() {
        assertThat(controller.getEmptyPhoneNumber().getAreaCode().toString(), is("040"));
        assertThat(controller.getEmptyPhoneNumber().getSubscriberNumber().toString(), is("123456"));
        controller.setEmptyPhoneNumber(null);
      }
    });
  }

  @Test
  public void emptyAreaCode(final @InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    initiate(new Activity() {
      public void perform() {
        page.getPhoneNumber().setAreaCode("");
        page.getPhoneNumber().setSubscriberNumber("123456");
        page.submit();
        assertThat(page.getAreaCodeMessage().getText(), is(""));
        assertThat(page.getSubscriberNumberMessage().getText(), is(""));
        assertThat(page.getPhoneNumberMessage().getText(), is(""));
        assertThat(page.getSubmitMessage().getText(), is("The data was successfully saved"));
      }
    })
    .observe(request().method().equal(HttpMethod.POST))
    .inspect(new Inspection() {

      private static final long serialVersionUID = 1L;

      @Inject
      private PhoneNumberController controller;

      @AfterPhase(Phase.UPDATE_MODEL_VALUES)
      public void verifyPhoneNumberIsSet() {
        assertThat(controller.getEmptyPhoneNumber().getAreaCode(), is(nullValue()));
        assertThat(controller.getEmptyPhoneNumber().getSubscriberNumber().toString(), is("123456"));
        controller.setEmptyPhoneNumber(null);
      }
    });
  }

  @Test
  public void inconvertibleAreaCode(final @InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    initiate(new Activity() {
      public void perform() {
        page.getPhoneNumber().setAreaCode("123");
        page.getPhoneNumber().setSubscriberNumber("123456");
        page.submit();
        assertThat(page.getAreaCodeMessage().getText(), is("area code must start with 0"));
        assertThat(page.getSubscriberNumberMessage().getText(), is(""));
        assertThat(page.getPhoneNumberMessage().getText(), is("could not create phone number"));
        assertThat(page.getSubmitMessage().getText(), is(""));
      }
    })
    .observe(request().method().equal(HttpMethod.POST))
    .inspect(new Inspection() {

      private static final long serialVersionUID = 1L;

      @Inject
      private PhoneNumberController controller;

      @AfterServlet
      public void verifyPhoneNumberIsEmpty() {
        assertThat(controller.getEmptyPhoneNumber(), is(nullValue()));
      }
    });
  }

  @Test
  public void invalidAreaCode(final @InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    initiate(new Activity() {
      public void perform() {
        page.getPhoneNumber().setAreaCode("01234");
        page.getPhoneNumber().setSubscriberNumber("123456");
        page.submit();
        assertThat(page.getAreaCodeMessage().getText(), is("area code may not be '01234'"));
        assertThat(page.getSubscriberNumberMessage().getText(), is(""));
        assertThat(page.getPhoneNumberMessage().getText(), is("could not create phone number"));
        assertThat(page.getSubmitMessage().getText(), is(""));
      }
    })
    .observe(request().method().equal(HttpMethod.POST))
    .inspect(new Inspection() {

      private static final long serialVersionUID = 1L;

      @Inject
      private PhoneNumberController controller;

      @AfterServlet
      public void verifyPhoneNumberIsEmpty() {
        assertThat(controller.getEmptyPhoneNumber(), is(nullValue()));
      }
    });
  }

  @Test
  public void emptySubscriberNumber(final @InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    initiate(new Activity() {
      public void perform() {
        page.getPhoneNumber().setAreaCode("040");
        page.getPhoneNumber().setSubscriberNumber("");
        page.submit();
        assertThat(page.getAreaCodeMessage().getText(), is(""));
        assertThat(page.getSubscriberNumberMessage().getText(), is(""));
        assertThat(page.getPhoneNumberMessage().getText(), is(""));
        assertThat(page.getSubmitMessage().getText(), is("The data was successfully saved"));
      }
    })
    .observe(request().method().equal(HttpMethod.POST))
    .inspect(new Inspection() {

      private static final long serialVersionUID = 1L;

      @Inject
      private PhoneNumberController controller;

      @AfterPhase(Phase.UPDATE_MODEL_VALUES)
      public void verifyPhoneNumberIsSet() {
        assertThat(controller.getEmptyPhoneNumber().getAreaCode().toString(), is("040"));
        assertThat(controller.getEmptyPhoneNumber().getSubscriberNumber(), is(nullValue()));
        controller.setEmptyPhoneNumber(null);
      }
    });
  }

  @Test
  public void inconvertibleSubscriberNumber(final @InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    initiate(new Activity() {
      public void perform() {
        page.getPhoneNumber().setAreaCode("040");
        page.getPhoneNumber().setSubscriberNumber("12345");
        page.submit();
        assertThat(page.getAreaCodeMessage().getText(), is(""));
        assertThat(page.getSubscriberNumberMessage().getText(), is("subscriber number may not be '12345'"));
        assertThat(page.getPhoneNumberMessage().getText(), is("could not create phone number"));
        assertThat(page.getSubmitMessage().getText(), is(""));
      }
    })
    .observe(request().method().equal(HttpMethod.POST))
    .inspect(new Inspection() {

      private static final long serialVersionUID = 1L;

      @Inject
      private PhoneNumberController controller;

      @AfterServlet
      public void verifyPhoneNumberIsEmpty() {
        assertThat(controller.getEmptyPhoneNumber(), is(nullValue()));
      }
    });
  }

  @Test
  public void invalidSubscriberNumber(final @InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    initiate(new Activity() {
      public void perform() {
        page.getPhoneNumber().setAreaCode("040");
        page.getPhoneNumber().setSubscriberNumber("0123456");
        page.submit();
        assertThat(page.getAreaCodeMessage().getText(), is(""));
        assertThat(page.getSubscriberNumberMessage().getText(), is("subscriber number may not start with 0"));
        assertThat(page.getPhoneNumberMessage().getText(), is("could not create phone number"));
        assertThat(page.getSubmitMessage().getText(), is(""));
      }
    })
    .observe(request().method().equal(HttpMethod.POST))
    .inspect(new Inspection() {

      private static final long serialVersionUID = 1L;

      @Inject
      private PhoneNumberController controller;

      @AfterServlet
      public void verifyPhoneNumberIsEmpty() {
        assertThat(controller.getEmptyPhoneNumber(), is(nullValue()));
      }
    });
  }

  @Test
  public void emptyPhoneNumber(final @InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    initiate(new Activity() {
      public void perform() {
        page.getPhoneNumber().setAreaCode("");
        page.getPhoneNumber().setSubscriberNumber("");
        page.submit();
        assertThat(page.getAreaCodeMessage().getText(), is(""));
        assertThat(page.getSubscriberNumberMessage().getText(), is(""));
        assertThat(page.getPhoneNumberMessage().getText(), is(""));
        assertThat(page.getSubmitMessage().getText(), is("The data was successfully saved"));
      }
    })
    .observe(request().method().equal(HttpMethod.POST))
    .inspect(new Inspection() {

      private static final long serialVersionUID = 1L;

      @Inject
      private PhoneNumberController controller;

      @AfterServlet
      public void verifyPhoneNumberIsEmpty() {
        assertThat(controller.getEmptyPhoneNumber(), is(nullValue()));
      }
    });
  }

  @Test
  public void inconvertiblePhoneNumber(final @InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    initiate(new Activity() {
      public void perform() {
        page.getPhoneNumber().setAreaCode("012345");
        page.getPhoneNumber().setSubscriberNumber("1234");
        page.submit();
        assertThat(page.getAreaCodeMessage().getText(), is(""));
        assertThat(page.getSubscriberNumberMessage().getText(), is(""));
        assertThat(page.getPhoneNumberMessage().getText(), is("could not create phone number"));
        assertThat(page.getSubmitMessage().getText(), is(""));
      }
    })
    .observe(request().method().equal(HttpMethod.POST))
    .inspect(new Inspection() {

      private static final long serialVersionUID = 1L;

      @Inject
      private PhoneNumberController controller;

      @AfterServlet
      public void verifyPhoneNumberIsEmpty() {
        assertThat(controller.getEmptyPhoneNumber(), is(nullValue()));
      }
    });
  }

  @Test
  public void invalidPhoneNumber(final @InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getTitle().getText(), is("JSF Value Object Sample"));
    initiate(new Activity() {
      public void perform() {
        page.getPhoneNumber().setAreaCode("0123");
        page.getPhoneNumber().setSubscriberNumber("456789");
        page.submit();
        assertThat(page.getAreaCodeMessage().getText(), is(""));
        assertThat(page.getSubscriberNumberMessage().getText(), is(""));
        assertThat(page.getPhoneNumberMessage().getText(), is("phone number may not be '0123 456789'"));
        assertThat(page.getSubmitMessage().getText(), is(""));
      }
    })
    .observe(request().method().equal(HttpMethod.POST))
    .inspect(new Inspection() {

      private static final long serialVersionUID = 1L;

      @Inject
      private PhoneNumberController controller;

      @AfterServlet
      public void verifyPhoneNumberIsEmpty() {
        assertThat(controller.getEmptyPhoneNumber(), is(nullValue()));
      }
    });
  }
}
