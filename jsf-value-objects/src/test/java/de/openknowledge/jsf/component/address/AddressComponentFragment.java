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

public class AddressComponentFragment {

  @FindBy(css = "div[id$=':street']")
  private StreetComponentFragment street;
  
  @FindBy(css = "span[id$=':streetMessage']")
  private WebElement streetMessage;

  @FindBy(css = "div[id$=':city']")
  private CityComponentFragment city;
  
  @FindBy(css = "span[id$=':cityMessage']")
  private WebElement cityMessage;

  public StreetComponentFragment getStreet() {
    return street;
  }
  
  public String getStreetMessage() {
    return streetMessage.getText();
  }

  public CityComponentFragment getCity() {
    return city;
  }
  
  public String getCityMessage() {
    return cityMessage.getText();
  }
}
