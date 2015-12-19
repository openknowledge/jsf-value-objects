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
package de.openknowledge.domain;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;

/**
 * A base class for value object that contain one simple value.
 * 
 * @author Arne Limburg - open knowledge GmbH
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractSimpleValueObject<V extends Comparable<V>>
  implements Serializable, Comparable<AbstractSimpleValueObject<V>> {

  private V value;

  protected AbstractSimpleValueObject() {
    // for subclassing
  }

  public AbstractSimpleValueObject(V value) {
    this.value = validate(value);
  }

  /**
   * Validation and normalization callback used during construction of the value object. Subclasses may override this
   * method to alter validation and normalization.
   *
   * @param parameterValue The constructor value.
   * @return The validated and normalized value.
   */
  protected V validate(V parameterValue) {
    return notNull(parameterValue, "value must not be null");
  }

  protected V getValue() {
    return value;
  }

  @Override
  public String toString() {
    return getValue().toString();
  }

  @Override
  public int hashCode() {
    return getValue().hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || !(object.getClass().isAssignableFrom(getClass()) || getClass().isAssignableFrom(object.getClass()))) {
      return false;
    }
    AbstractSimpleValueObject<V> valueObject = (AbstractSimpleValueObject<V>)object;
    return valueObject.getValue().equals(getValue());
  }

  @Override
  public int compareTo(AbstractSimpleValueObject<V> object) {
    return getValue().compareTo(object.getValue());
  }
}
