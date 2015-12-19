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

import static org.apache.commons.lang3.Validate.isTrue;

import de.openknowledge.domain.AbstractSimpleValueObject;

public class AreaCode extends AbstractSimpleValueObject<String> {

  protected AreaCode() {
    // for frameworks
  }

  public AreaCode(String value) {
    super(value);
  }

  @Override
  protected String validate(String value) {
    isTrue(super.validate(value).charAt(0) == '0', "area code must start with 0");
    return value;
  }
}
