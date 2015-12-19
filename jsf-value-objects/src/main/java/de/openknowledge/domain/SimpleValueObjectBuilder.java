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

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
public final class SimpleValueObjectBuilder<V extends AbstractSimpleValueObject<?>> {

    private Constructor<V> valueObjectConstructor;

    private Constructor<?> simpleValueConstructor;

    private SimpleValueObjectBuilder(Class<V> valueObjectClass) {
        ParameterizedType valueObjectSuperclass = (ParameterizedType)valueObjectClass.getGenericSuperclass();
        Class<?> simpleClass = (Class<?>)valueObjectSuperclass.getActualTypeArguments()[0];
        try {
            valueObjectConstructor = valueObjectClass.getConstructor(simpleClass);
        } catch (NoSuchMethodException ex) {
            if (ClassUtils.isPrimitiveWrapper(simpleClass)) {
                try {
                    valueObjectConstructor = valueObjectClass.getConstructor(ClassUtils.wrapperToPrimitive(simpleClass));
                } catch (NoSuchMethodException e) {
                    throw new IllegalStateException("Value Object " + valueObjectClass.getName()
                            + " requires " + simpleClass.getSimpleName() + "-Constructor to be used with Converter/Adapter");
                }
            } else {
                throw new IllegalStateException("Value Object " + valueObjectClass.getName()
                        + " requires " + simpleClass.getSimpleName() + "-Constructor to be used with Converter/Adapter");
            }
        }
        if (simpleClass.isPrimitive()) {
            simpleClass = ClassUtils.primitiveToWrapper(simpleClass);
        }
        try {
            simpleValueConstructor = simpleClass.getConstructor(String.class);
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("Value Object simple type " + simpleClass.getName()
                    + " requires String-Constructor to be used with JSF Converter");
        }
    }

    public static <V extends AbstractSimpleValueObject<?>> SimpleValueObjectBuilder<V> forType(Class<V> valueObjectClass) {
        return new SimpleValueObjectBuilder<V>(valueObjectClass);
    }

    public V build(String value) throws ReflectiveOperationException {
        return build(simpleValueConstructor.newInstance(value));
    }

    public V build(Object value) throws ReflectiveOperationException {
        return valueObjectConstructor.newInstance(value);
    }
}
