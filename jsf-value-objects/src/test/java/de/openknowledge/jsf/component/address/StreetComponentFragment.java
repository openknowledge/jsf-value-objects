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
package de.openknowledge.jsf.component.address;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class StreetComponentFragment {

  @FindBy(css = "input[id$=':streetName']")
  private WebElement streetName;
  
  @FindBy(css = "span[id$=':streetNameMessage']")
  private WebElement streetNameMessage;

  @FindBy(css = "input[id$=':streetNumber']")
  private WebElement streetNumber;

  @FindBy(css = "span[id$=':streetNumberMessage']")
  private WebElement streetNumberMessage;

  public String getName() {
    return streetName.getAttribute("value");
  }

  public void setName(String code) {
    streetName.clear();
    streetName.sendKeys(code);
  }

  public String getNameMessage() {
    return streetNameMessage.getText();
  }

  public String getNumber() {
    return streetNumber.getAttribute("value");
  }

  public void setNumber(String number) {
    streetNumber.clear();
    streetNumber.sendKeys(number);
  }

  public String getNumberMessage() {
    return streetNumberMessage.getText();
  }
}
