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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
@FacesComponent("de.openknowledge.jsf.component.ValueObjectComponent")
public class ValueObjectComponent extends UIInput implements NamingContainer {

  private static final String CC_ATTRS_VALUE = "#{cc.attrs.value.";

  @Override
  public String getFamily() {
    return UINamingContainer.COMPONENT_FAMILY;
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    super.encodeBegin(context);
    if (!isValid()) {
      return;
    }
    Object value = getValue();
    if (value == null) {
      forEachChild(new ChildProcessor() {
        public void process(UIInput child) {
          child.setValue(null);
          child.setLocalValueSet(false);
        }
      });
    }
  }

  @Override
  public void setValue(Object value) {
    super.setValue(value);
    if (value == null) {
      forEachChild(new ChildProcessor() {
        public void process(UIInput child) {
          child.setValue(null);
        }
      });
    } else {
      forEachChild(new ChildProcessor() {
        public void process(UIInput child) {
          ValueExpression original = child.getValueExpression("value");
          ExpressionFactory expressionFactory = getFacesContext().getApplication().getExpressionFactory();
          ELContext elContext = getFacesContext().getELContext();
          ValueExpression valueExpression = expressionFactory.createValueExpression(
              elContext, original.getExpressionString().replace("cc.attrs.", "cc."), original.getExpectedType());
          child.setValue(valueExpression.getValue(elContext));
        }
      });
    }
  }

  @Override
  public void processDecodes(FacesContext context) {
    super.processDecodes(context);
    final StringBuilder submittedValue = new StringBuilder();
    forEachChild(new ChildProcessor() {
      public void process(UIInput child) {
        if (child.getSubmittedValue() != null) {
          submittedValue.append(child.getSubmittedValue()).append(getAttributes().get("submittedValueDelimiter"));
        }
      }
    });
    if (submittedValue.length() > 0) {
      setSubmittedValue(submittedValue.toString());
    }
  }

  @Override
  public void processUpdates(FacesContext context) {
    forEachChild(new ChildProcessor() {
      public void process(UIInput child) {
        child.setLocalValueSet(false);
      }
    });
    super.processUpdates(context);
  }

  @Override
  public void resetValue() {
    super.resetValue();
    forEachChild(new ChildProcessor() {
      public void process(UIInput child) {
        child.resetValue();
      }
    });
  }

  @Override
  public void processValidators(FacesContext context) {
    processChildValidators(context);
    final Map<UIInput, Object> oldSubmittedValues = new HashMap<>();
    forEachChild(new ChildProcessor() {
      public void process(UIInput child) {
        oldSubmittedValues.put(child, child.getSubmittedValue());
        child.setSubmittedValue(null);
      }
    });
    super.processValidators(context);
    forEachChild(new ChildProcessor() {
      public void process(UIInput child) {
        child.setSubmittedValue(oldSubmittedValues.get(child));
      }
    });
  }

  protected void processChildValidators(final FacesContext context) {
    boolean wasNull = false;
    boolean wasLocalValueSet = isLocalValueSet();
    try {
      pushComponentToEL(context, this);
      if (getValue() == null) {
        super.setValue(newInstance(context));
        wasNull = true;
      }
      forEachChild(new ChildProcessor() {
        public void process(UIInput child) {
          child.processValidators(context);
        }
      });
    } finally {
      if (wasNull) {
        super.setValue(null);
        setLocalValueSet(wasLocalValueSet);
      }
      popComponentFromEL(context);
    }
  }

  @Override
  protected Object getConvertedValue(final FacesContext context, Object submittedValue) throws ConverterException {
    final Map<String, Object> parameters = new LinkedHashMap<>();
    forEachChild(new ChildProcessor() {
      public void process(UIInput child) {
        String expressionString = child.getValueExpression("value").getExpressionString().replace(CC_ATTRS_VALUE, "");
        parameters.put(expressionString.substring(0, expressionString.length() - 1), child.getValue());
      }
    });
    return newInstance(context, parameters);
  }

  protected Object newInstance(FacesContext context) {
    return newInstance(context, Collections.emptyMap());
  }

  protected Object newInstance(FacesContext context, Map<String, Object> parameters) {
    return newInstance(context, parameters.values());
  }

  protected Object newInstance(FacesContext context, Object... parameters) {
    List<Class<?>> parameterTypes = new ArrayList<>();
    for (Object parameter : parameters) {
      parameterTypes.add(parameter.getClass());
    }
    Class<?> type = getValueExpression("value").getType(context.getELContext());
    try {
      Constructor<?> constructor = type.getDeclaredConstructor(parameterTypes.toArray(new Class[parameterTypes.size()]));
      if (!constructor.isAccessible()) {
        constructor.setAccessible(true);
      }
      return constructor.newInstance(parameters);
    } catch (NoSuchMethodException e) {
      throw newConstructorNotFoundException(type, parameterTypes);
    } catch (InvocationTargetException e) {
      throw new ConverterException(e.getTargetException());
    } catch (ReflectiveOperationException e) {
      throw new ConverterException(e);
    }
  }

  protected void forEachChild(ChildProcessor processor) {
    for (UIComponent component : getFacet(COMPOSITE_FACET_NAME).getChildren()) {
      if (component instanceof UIInput) {
        UIInput input = (UIInput)component;
        if (input.getValueExpression("value").getExpressionString().startsWith(CC_ATTRS_VALUE)) {
          processor.process(input);
        }
      }
    }
  }

  private IllegalStateException newConstructorNotFoundException(Class<?> type, List<Class<?>> parameterTypes) {
    if (parameterTypes.isEmpty()) {
      return new IllegalStateException("No-arg constructor not found for " + type.getName());
    } else {
      StringBuilder messageBuilder = new StringBuilder()
          .append("Constructor ")
          .append(type.getName())
          .append('(');
      for (Class<?> parameterType : parameterTypes) {
        messageBuilder.append(parameterType.getName()).append(',');
      }
      messageBuilder.setCharAt(messageBuilder.length() - 1, ')');
      messageBuilder.append(" not found.");
      return new IllegalStateException(messageBuilder.toString());
    }
  }

  public interface ChildProcessor {

    void process(UIInput child);
  }
}