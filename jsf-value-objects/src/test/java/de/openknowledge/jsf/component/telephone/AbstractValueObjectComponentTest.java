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
package de.openknowledge.jsf.component.telephone;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;

public abstract class AbstractValueObjectComponentTest {

  @Drone
  private WebDriver browser;

  public void validPhoneNumber(final AbstractPhoneNumberTestPage page) {
    assertThat(browser.getTitle(), is("JSF Value Object Sample"));

    page.getPhoneNumber().setAreaCode("040");
    page.getPhoneNumber().setSubscriberNumber("123456");
    page.submit();
    
    assertThat(page.getResult(), is("040 123456"));
    
    assertThat(page.getPhoneNumber().getAreaCodeMessage(), is(""));
    assertThat(page.getPhoneNumber().getSubscriberNumberMessage(), is(""));
    assertThat(page.getPhoneNumberMessage(), is(""));
    assertThat(page.getSubmitMessage(), is("The data was successfully saved"));
  }

  public void emptyAreaCode(final AbstractPhoneNumberTestPage page) {
    assertThat(browser.getTitle(), is("JSF Value Object Sample"));

    page.getPhoneNumber().setAreaCode("");
    page.getPhoneNumber().setSubscriberNumber("123456");
    page.submit();

    assertThat(page.getResult(), is("null 123456"));
    
    assertThat(page.getPhoneNumber().getAreaCodeMessage(), is(""));
    assertThat(page.getPhoneNumber().getSubscriberNumberMessage(), is(""));
    assertThat(page.getPhoneNumberMessage(), is(""));
    assertThat(page.getSubmitMessage(), is("The data was successfully saved"));
  }

  public void inconvertibleAreaCode(final AbstractPhoneNumberTestPage page) {
    assertThat(browser.getTitle(), is("JSF Value Object Sample"));

    page.getPhoneNumber().setAreaCode("123");
    page.getPhoneNumber().setSubscriberNumber("123456");
    page.submit();

    assertThat(page.getResult(), is(initialValue()));

    assertThat(page.getPhoneNumber().getAreaCodeMessage(), is("area code must start with 0"));
    assertThat(page.getPhoneNumber().getSubscriberNumberMessage(), is(""));
    assertThat(page.getPhoneNumberMessage(), is("could not create phone number"));
    assertThat(page.getSubmitMessage(), is(""));
  }

  public void invalidAreaCode(final AbstractPhoneNumberTestPage page) {
    assertThat(browser.getTitle(), is("JSF Value Object Sample"));

    page.getPhoneNumber().setAreaCode("01234");
    page.getPhoneNumber().setSubscriberNumber("123456");
    page.submit();

    assertThat(page.getResult(), is(initialValue()));

    assertThat(page.getPhoneNumber().getAreaCodeMessage(), is("area code may not be '01234'"));
    assertThat(page.getPhoneNumber().getSubscriberNumberMessage(), is(""));
    assertThat(page.getPhoneNumberMessage(), is("could not create phone number"));
    assertThat(page.getSubmitMessage(), is(""));
   }

  public void emptySubscriberNumber(final AbstractPhoneNumberTestPage page) {
    assertThat(browser.getTitle(), is("JSF Value Object Sample"));
 
    page.getPhoneNumber().setAreaCode("040");
    page.getPhoneNumber().setSubscriberNumber("");
    page.submit();

    assertThat(page.getResult(), is("040 null"));

    assertThat(page.getPhoneNumber().getAreaCodeMessage(), is(""));
    assertThat(page.getPhoneNumber().getSubscriberNumberMessage(), is(""));
    assertThat(page.getPhoneNumberMessage(), is(""));
    assertThat(page.getSubmitMessage(), is("The data was successfully saved"));
  }

  public void inconvertibleSubscriberNumber(final AbstractPhoneNumberTestPage page) {
    assertThat(browser.getTitle(), is("JSF Value Object Sample"));

    page.getPhoneNumber().setAreaCode("040");
    page.getPhoneNumber().setSubscriberNumber("12345");
    page.submit();

    assertThat(page.getResult(), is(initialValue()));
    
    assertThat(page.getPhoneNumber().getAreaCodeMessage(), is(""));
    assertThat(page.getPhoneNumber().getSubscriberNumberMessage(), is("subscriber number may not be '12345'"));
    assertThat(page.getPhoneNumberMessage(), is("could not create phone number"));
    assertThat(page.getSubmitMessage(), is(""));
  }

  public void invalidSubscriberNumber(final AbstractPhoneNumberTestPage page) {

    assertThat(browser.getTitle(), is("JSF Value Object Sample"));

    page.getPhoneNumber().setAreaCode("040");
    page.getPhoneNumber().setSubscriberNumber("0123456");
    page.submit();

    assertThat(page.getResult(), is(initialValue()));
    
    assertThat(page.getPhoneNumber().getAreaCodeMessage(), is(""));
    assertThat(page.getPhoneNumber().getSubscriberNumberMessage(), is("subscriber number may not start with 0"));
    assertThat(page.getPhoneNumberMessage(), is("could not create phone number"));
    assertThat(page.getSubmitMessage(), is(""));
  }

  public void emptyPhoneNumber(final AbstractPhoneNumberTestPage page) {
    assertThat(browser.getTitle(), is("JSF Value Object Sample"));

    page.getPhoneNumber().setAreaCode("");
    page.getPhoneNumber().setSubscriberNumber("");
    page.submit();

    assertThat(page.getResult(), is("null"));

    assertThat(page.getPhoneNumber().getAreaCodeMessage(), is(""));
    assertThat(page.getPhoneNumber().getSubscriberNumberMessage(), is(""));
    assertThat(page.getPhoneNumberMessage(), is(""));
    assertThat(page.getSubmitMessage(), is("The data was successfully saved"));
  }

  public void inconvertiblePhoneNumber(final AbstractPhoneNumberTestPage page) {
    assertThat(browser.getTitle(), is("JSF Value Object Sample"));

    page.getPhoneNumber().setAreaCode("012345");
    page.getPhoneNumber().setSubscriberNumber("1234");
    page.submit();

    assertThat(page.getResult(), is(initialValue()));

    assertThat(page.getPhoneNumber().getAreaCodeMessage(), is(""));
    assertThat(page.getPhoneNumber().getSubscriberNumberMessage(), is(""));
    assertThat(page.getPhoneNumberMessage(), is("could not create phone number"));
    assertThat(page.getSubmitMessage(), is(""));
  }

  public void invalidPhoneNumber(final AbstractPhoneNumberTestPage page) {
    assertThat(browser.getTitle(), is("JSF Value Object Sample"));    

    page.getPhoneNumber().setAreaCode("0123");
    page.getPhoneNumber().setSubscriberNumber("456789");
    page.submit();

    assertThat(page.getResult(), is(initialValue()));
    
    assertThat(page.getPhoneNumber().getAreaCodeMessage(), is(""));
    assertThat(page.getPhoneNumber().getSubscriberNumberMessage(), is(""));
    assertThat(page.getPhoneNumberMessage(), is("phone number may not be '0123 456789'"));
    assertThat(page.getSubmitMessage(), is(""));
  }
  
  protected abstract String initialValue();
}
