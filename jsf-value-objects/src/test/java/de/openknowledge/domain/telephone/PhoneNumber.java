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
package de.openknowledge.domain.telephone;

import de.openknowledge.domain.AbstractValueObject;


public class PhoneNumber extends AbstractValueObject {

  private AreaCode areaCode;
  private SubscriberNumber subscriberNumber;
  
  protected PhoneNumber() {
    // for frameworks
  }

  public PhoneNumber(AreaCode areaCode, SubscriberNumber subscriberNumber) {
    if (areaCode != null && subscriberNumber != null && areaCode.toString().length() > subscriberNumber.toString().length()) {
      throw new IllegalArgumentException("subsriber number length must be greater than area code length");
    }
    // note: area code and phone number may be null for testing purpose
    // in real world projects, a notNull-check should be placed here
    this.areaCode = areaCode;
    this.subscriberNumber = subscriberNumber;
  }

  @Override
  protected Object[] values() {
    return new Object[] {areaCode, subscriberNumber};
  }
}
