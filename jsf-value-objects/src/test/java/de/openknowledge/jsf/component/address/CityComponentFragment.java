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

public class CityComponentFragment {

  @FindBy(css = "input[id$=':zipCode']")
  private WebElement zipCode;
  
  @FindBy(css = "span[id$=':zipCodeMessage']")
  private WebElement zipCodeMessage;

  @FindBy(css = "input[id$=':cityName']")
  private WebElement cityName;

  @FindBy(css = "span[id$=':cityNameMessage']")
  private WebElement cityNameMessage;

  public String getZipCode() {
    return zipCode.getAttribute("value");
  }

  public void setZipCode(String code) {
    zipCode.clear();
    zipCode.sendKeys(code);
  }

  public String getZipCodeMessage() {
    return zipCodeMessage.getText();
  }

  public String getName() {
    return cityName.getAttribute("value");
  }

  public void setName(String number) {
    cityName.clear();
    cityName.sendKeys(number);
  }

  public String getNameMessage() {
    return cityNameMessage.getText();
  }
}
