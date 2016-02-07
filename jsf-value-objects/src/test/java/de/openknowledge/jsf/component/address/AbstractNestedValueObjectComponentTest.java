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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;

public class AbstractNestedValueObjectComponentTest {

  @Drone
  private WebDriver browser;

  public void validAddress(AbstractAddressTestPage page) {
    assertThat(browser.getTitle(), is("JSF Nested Value Object Sample"));

    page.getAddress().getStreet().setName("Poststrasse");
    page.getAddress().getStreet().setNumber("1");
    page.getAddress().getCity().setZipCode("26121");
    page.getAddress().getCity().setName("Oldenburg");
    page.submit();
    
    assertThat(page.getAddress().getStreet().getNameMessage(), is(""));
    assertThat(page.getAddress().getStreet().getNumberMessage(), is(""));
    assertThat(page.getAddress().getStreetMessage(), is(""));
    assertThat(page.getAddress().getCity().getZipCodeMessage(), is(""));
    assertThat(page.getAddress().getCity().getNameMessage(), is(""));
    assertThat(page.getAddress().getCityMessage(), is(""));
    assertThat(page.getAddressMessage(), is(""));
    assertThat(page.getSubmitMessage(), is("The data was successfully saved"));

    assertThat(page.getResult(), is("Poststrasse 1, 26121 Oldenburg"));
  }
}
