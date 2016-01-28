/*
 * Copyright (C) open knowledge GmbH
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
import java.util.HashMap;
import java.util.Iterator;
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

  private static final String SUBMITTED_VALUE_DELIMITER = "submittedValueDelimiter";

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
          if (child.isLocalValueSet()) {
            child.setValue(null);
            child.setLocalValueSet(false);
          }
        }
      });
    }
  }

  @Override
  public void processDecodes(FacesContext context) {
    super.processDecodes(context);
    final StringBuilder submittedValue = new StringBuilder();
    final Object submittedValueDelimiter
      = getAttributes().containsKey(SUBMITTED_VALUE_DELIMITER) ? getAttributes().get(SUBMITTED_VALUE_DELIMITER) : ",";
    forEachChild(new ChildProcessor() {

      public void process(UIInput child) {
        if (child.getSubmittedValue() != null) {
          submittedValue.append(child.getSubmittedValue()).append(submittedValueDelimiter);
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
        child.setValue(null);
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
    final List<UIInput> parameters = new ArrayList<>();
    forEachChild(new ChildProcessor() {

      public void process(UIInput child) {
        parameters.add(child);
      }
    });
    return newInstance(context, parameters.toArray(new UIInput[parameters.size()]));
  }

  protected Object newInstance(FacesContext context, UIInput... parameterComponents) {
    List<Object> parameterValues = new ArrayList<>();
    List<Class<?>> parameterTypes = new ArrayList<>();
    boolean allParametersNull = true;
    for (UIInput parameterComponent: parameterComponents) {
      Object parameterValue = parameterComponent.getValue();
      parameterValues.add(parameterValue);
      if (parameterValue != null) {
        allParametersNull = false;
        parameterTypes.add(parameterValue.getClass());
      } else {
        parameterTypes.add(null);
      }
    }
    if (parameterComponents.length != 0 && allParametersNull && valueIsNullWhenAllParametersAreNull()) {
      return null;
    }
    Class<?> type = getValueExpression("value").getType(context.getELContext());
    try {
      Constructor<?> constructor = getConstructor(type, parameterTypes.toArray(new Class<?>[parameterTypes.size()]));
      if (!constructor.isAccessible()) {
        constructor.setAccessible(true);
      }
      return constructor.newInstance(parameterValues.toArray());
    } catch (InvocationTargetException e) {
      throw new ConverterException(e.getTargetException());
    } catch (ReflectiveOperationException e) {
      throw new ConverterException(e);
    }
  }

  protected void forEachChild(ChildProcessor processor) {
    String parentValueExpression = getValueExpression("value").getExpressionString();
    String prefix = parentValueExpression.substring(0, parentValueExpression.lastIndexOf('}')) + '.';
    for (UIComponent component : getFacet(COMPOSITE_FACET_NAME).getChildren()) {
      forEachChild(component, processor, prefix);
    }
  }

  private void forEachChild(UIComponent parent, ChildProcessor processor, String prefix) {
    if (parent instanceof UIInput) {
      UIInput input = (UIInput)parent;
      ValueExpression childValueExpression = input.getValueExpression("value");
      String childValueExpressionString = input.getValueExpression("value").getExpressionString();
      if (childValueExpressionString.startsWith(prefix)) {
        ExpressionFactory expressionFactory = getFacesContext().getApplication().getExpressionFactory();
        ELContext elContext = getFacesContext().getELContext();
        // lookup has to be directed to local value, so replace value expression with #{cc.attrs.value...}
        // TODO maybe cache new value expression?
        ValueExpression valueExpression = expressionFactory.createValueExpression(
            elContext, childValueExpressionString.replace(prefix, CC_ATTRS_VALUE), childValueExpression.getExpectedType());
        input.setValueExpression("value", valueExpression);
        try {
          processor.process(input);
        } finally {
          // finally reset original value expression
          input.setValueExpression("value", childValueExpression);
        }
      } else if (childValueExpressionString.startsWith(CC_ATTRS_VALUE)) {
        processor.process(input);
      }
    }
    if (!(parent instanceof ValueObjectComponent)) {
      for (Iterator<UIComponent> i = parent.getFacetsAndChildren(); i.hasNext();) {
        forEachChild(i.next(), processor, prefix); 
      }
    }
  }

  protected boolean valueIsNullWhenAllParametersAreNull() {
    return true;
  }

  protected Constructor<?> getConstructor(Class<?> type, Class<?>... parameterTypes) {
    Constructor<?> constructor = null;
    for (Constructor<?> declaredConstructor: type.getDeclaredConstructors()) {
      Class<?>[] declaredParameterTypes = declaredConstructor.getParameterTypes();
      if (matchParameters(declaredParameterTypes, parameterTypes)) {
        if (constructor != null) {
          throw new IllegalStateException("More that one valid constructor found: " + constructor + " and " + declaredConstructor);
        }
        constructor = declaredConstructor;
      }
    } 
    if (constructor == null) {
      throw newConstructorNotFoundException(type, parameterTypes);
    }
    return constructor;
  }

  private boolean matchParameters(Class<?>[] declaredParameterTypes, Class<?>[] parameterTypes) {
    if (declaredParameterTypes.length != parameterTypes.length) {
      return false;
    }
    for (int i = 0; i < declaredParameterTypes.length; i++) {
      if (parameterTypes[i] != null && !declaredParameterTypes[i].isAssignableFrom(parameterTypes[i])) {
        return false;
      }
    }
    return true;
  }

  private IllegalStateException newConstructorNotFoundException(Class<?> type, Class<?>... parameterTypes) {
    if (parameterTypes.length == 0) {
      return new IllegalStateException("No-arg constructor not found for " + type.getName());
    } else {
      StringBuilder messageBuilder = new StringBuilder()
          .append("Constructor ")
          .append(type.getName())
          .append('(');
      for (Class<?> parameterType : parameterTypes) {
        if (parameterType == null) {
          messageBuilder.append("null,");
        } else {
          messageBuilder.append(parameterType.getName()).append(',');
        }
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