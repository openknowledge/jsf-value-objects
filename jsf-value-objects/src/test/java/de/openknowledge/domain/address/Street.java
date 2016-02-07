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
package de.openknowledge.domain.address;

import static org.apache.commons.lang3.Validate.notNull;

import de.openknowledge.domain.AbstractValueObject;


public class Street extends AbstractValueObject {

  private StreetName streetName;
  private StreetNumber streetNumber;
  
  protected Street() {
    // for frameworks
  }

  public Street(StreetName streetName, StreetNumber streetNumber) {
    this.streetName = notNull(streetName);
    this.streetNumber = notNull(streetNumber);
  }
  
  public StreetName getName() {
    return streetName;
  }
  
  public StreetNumber getNumber() {
    return streetNumber;
  }

  @Override
  public String toString() {
    return streetName + " " + streetNumber;
  }

  @Override
  protected Object[] values() {
    return new Object[] {streetName, streetNumber};
  }
}
