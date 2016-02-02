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

import de.openknowledge.domain.telephone.AreaCode;
import de.openknowledge.domain.telephone.PhoneNumber;
import de.openknowledge.domain.telephone.SubscriberNumber;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@ApplicationScoped
public class PhoneNumberController {

  private PhoneNumber phoneNumber;
  
  @PostConstruct
  private void initializePhoneNumber() {
    if (FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().endsWith("/phoneNumberTest.xhtml")) {
      phoneNumber = new PhoneNumber(new AreaCode("0441"), new SubscriberNumber("4082100"));
    }
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
