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
package de.openknowledge.jsf.converter;

import de.openknowledge.domain.AbstractSimpleValueObject;
import de.openknowledge.domain.SimpleValueObjectBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * A base class for {@link Converter}s that convert @link {@link AbstractSimpleValueObject}s.
 * Subclasses must not implement any method. They just need to specify the value type
 * as generic type argument for the superclass and add the {@link FacesConverter} annotation.
 * If <tt>MyValueObject</tt
 * 
 * @author Arne Limburg - open knowledge GmbH
 */
public class AbstractSimpleValueObjectConverter<V extends AbstractSimpleValueObject<?>> implements Converter {

  private SimpleValueObjectBuilder<V> builder;

  public AbstractSimpleValueObjectConverter() {
      ParameterizedType converterSuperclass = (ParameterizedType)getClass().getGenericSuperclass();
      Class<V> valueObjectClass = (Class<V>)converterSuperclass.getActualTypeArguments()[0];
      builder = SimpleValueObjectBuilder.forType(valueObjectClass);
  }

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
      if (value == null) {
          return null;
      }
      try {
          return builder.build(value);
      } catch (InvocationTargetException e) {
          if (e.getTargetException() instanceof IllegalArgumentException || e.getTargetException() instanceof NullPointerException) {
              String text = e.getTargetException().getLocalizedMessage();
              FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", text);
              throw new ConverterException(message);
          } else {
              throw new ConverterException(e.getTargetException());
          }
      } catch (ReflectiveOperationException ex) {
          throw new ConverterException(ex);
      }
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
      if (value == null) {
          return null;
      }
      return value.toString();
  }

}
