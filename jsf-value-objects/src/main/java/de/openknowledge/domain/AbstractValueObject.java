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

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;

/**
 * A base class for value object that contain more than one value.
 * Subclasses must implement {{@link #values()} and return every value.
 * The result of this method is used to implement {@link #equals(Object)} and {@link #hashCode()}.
 * 
 * @author Arne Limburg - open knowledge GmbH
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractValueObject implements Serializable {

    private transient Object[] values;

    public int hashCode() {
        return Arrays.hashCode(getValues());
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null
                || !(object.getClass().isAssignableFrom(getClass()) || getClass().isAssignableFrom(object.getClass()))) {
            return false;
        }
        AbstractValueObject valueObject = (AbstractValueObject)object;
        return Arrays.equals(getValues(), valueObject.getValues());
    }

    /**
     * Returns all values of this value object.
     */
    protected abstract Object[] values();

    private Object[] getValues() {
        if (values == null) {
            values = values();
        }
        return values;
    }
}
