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
package de.openknowledge.jsf.validator.telephone;

import de.openknowledge.domain.telephone.SubscriberNumber;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("de.openknowledge.SubscriberNumberValidator")
public class SubscriberNumberValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    SubscriberNumber subscriberNumber = (SubscriberNumber)value;
    if (subscriberNumber != null && subscriberNumber.toString().equals("12345")) {
      throw new ValidatorException(new FacesMessage("subscriber number may not be '12345'"));
    }
  }
}
