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
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Location("phoneNumber.xhtml")
public class PhoneNumberTestPage {

  @FindBy(id = "phoneNumber")
  private PhoneNumberComponentFragment phoneNumber;

  @FindBy(id = "phoneNumberMessage")
  private WebElement phoneNumberMessage;
  
  @FindBy(id = "submit")
  private WebElement submit;

  @FindBy(id = "submitMessage")
  private WebElement submitMessage;
  
  @FindBy(id = "result")
  private WebElement result;

  public PhoneNumberComponentFragment getPhoneNumber() {
    return phoneNumber;
  }

  public String getPhoneNumberMessage() {
    return phoneNumberMessage.getText();
  }

  public String getSubmitMessage() {
    return submitMessage.getText();
  }

  public String getResult() {
    return result.getText();
  }

  public void submit() {
    guardHttp(submit).click();
  }
}
