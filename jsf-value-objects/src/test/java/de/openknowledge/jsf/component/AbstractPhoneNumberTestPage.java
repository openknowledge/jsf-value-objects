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
import static org.jboss.arquillian.graphene.Graphene.waitGui;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

public abstract class AbstractPhoneNumberTestPage {

  @FindBy(id = "title")
  private WebElement title;
  
  @FindBy(id = "phoneNumber")
  private PhoneNumberComponentFragment phoneNumber;

  @FindBy(id = "phoneNumber:areaCodeMessage")
  private WebElement areaCodeMessage;

  @FindBy(id = "phoneNumber:subscriberNumberMessage")
  private WebElement subscriberNumberMessage;

  @FindBy(id = "phoneNumberMessage")
  private WebElement phoneNumberMessage;
  
  @FindBy(id = "submit")
  private WebElement submit;

  @FindBy(id = "submitMessage")
  private WebElement submitMessage;

  public WebElement getTitle() {
    return title;
  }
  
  public PhoneNumberComponentFragment getPhoneNumber() {
    return phoneNumber;
  }

  public WebElement getAreaCodeMessage() {
    return areaCodeMessage;
  }
  
  public WebElement getSubscriberNumberMessage() {
    return subscriberNumberMessage;
  }
  
  public WebElement getPhoneNumberMessage() {
    return phoneNumberMessage;
  }

  public WebElement getSubmitMessage() {
    return submitMessage;
  }

  public void submit() {
    submit.click();
    waitGui().withTimeout(10, TimeUnit.SECONDS);
  }
}
