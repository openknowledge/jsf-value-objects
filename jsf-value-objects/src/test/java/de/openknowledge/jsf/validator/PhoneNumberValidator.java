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
package de.openknowledge.jsf.validator;

import de.openknowledge.domain.telephone.PhoneNumber;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("de.openknowledge.PhoneNumberValidator")
public class PhoneNumberValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    PhoneNumber phoneNumber = (PhoneNumber)value;
    if (phoneNumber != null
        && phoneNumber.getAreaCode() != null
        && phoneNumber.getSubscriberNumber() != null
        && phoneNumber.getAreaCode().toString().equals("0123")
        && phoneNumber.getSubscriberNumber().toString().equals("456789")) {
      throw new ValidatorException(new FacesMessage("phone number may not be '0123 456789'"));
    }
  }
}
