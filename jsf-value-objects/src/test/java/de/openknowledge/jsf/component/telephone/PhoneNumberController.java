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

import de.openknowledge.domain.telephone.AreaCode;
import de.openknowledge.domain.telephone.PhoneNumber;
import de.openknowledge.domain.telephone.SubscriberNumber;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@ViewScoped
public class PhoneNumberController implements Serializable {

  public static final PhoneNumber INITIAL_VALUE = new PhoneNumber(new AreaCode("0441"), new SubscriberNumber("4082100"));
  private PhoneNumber phoneNumber;
  
  @PostConstruct
  private void initializePhoneNumber() throws MalformedURLException {
    if (FacesContext.getCurrentInstance().getExternalContext().getResource("/empty.txt") == null) {
      phoneNumber = INITIAL_VALUE;
    }
  }

  public String getPhoneNumberString() {
    return Objects.toString(phoneNumber);
  }

  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }
  
  public void setPhoneNumber(PhoneNumber phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void save() {
    FacesContext.getCurrentInstance().addMessage("submit", new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully saved", "The data was successfully saved"));
  }
}
