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
import java.util.concurrent.Callable;

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
  public Object getValue() {
    return inCompositeComponentContext(new Callable<Object>() {
      public Object call() {
        return ValueObjectComponent.super.getValue();
      }
    });
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
  public void setLocalValueSet(final boolean localValueSet) {
    super.setLocalValueSet(localValueSet);
    forEachChild(new ChildProcessor() {

      public void process(UIInput child) {
        child.setLocalValueSet(localValueSet);;
      }
    });
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
  public void processValidators(FacesContext context) {
    if (getSubmittedValue() == null) {
      return;
    }
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

  protected void processChildValidators(final FacesContext context) {
    boolean wasNull = false;
    boolean wasLocalValueSet = isLocalValueSet();
    Object value = getValue();
    if (value == null) {
      super.setValue(newInstance(context));
      wasNull = true;
    }
    try {
      pushComponentToEL(context, this);
      forEachChild(new ChildProcessor() {

        public void process(UIInput child) {
          child.processValidators(context);
        }
      });
    } finally {
      popComponentFromEL(context);
      if (wasNull) {
        super.setValue(null);
        setLocalValueSet(wasLocalValueSet);
      }
    }
  }
  
  @Override
  protected void validateValue(final FacesContext context, final Object newValue) {
    inCompositeComponentContext(new Runnable() {
      public void run() {
        ValueObjectComponent.super.validateValue(context, newValue);
      }
    });
  }

  @Override
  protected Object getConvertedValue(final FacesContext context, Object submittedValue) throws ConverterException {
    final List<UIInput> parameters = new ArrayList<>();
    final boolean[] isValid = {true};
    forEachChild(new ChildProcessor() {

      public void process(UIInput child) {
        isValid[0] &= child.isValid();
        if (isValid[0]) {
          parameters.add(child);
        }
      }
    });
    if (isValid[0]) {
      return newInstance(context, parameters.toArray(new UIInput[parameters.size()]));
    } else {
      setValid(false);
      return null;
    }
  }

  protected Object newInstance(final FacesContext context, UIInput... parameterComponents) {
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
    Class<?> type = inCompositeComponentContext(new Callable<Class<?>>() {
      public Class<?> call() throws Exception {
        return getValueExpression("value").getType(context.getELContext());
      }
    });
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
    ValueExpression oldValueExpression = getValueExpression("value");
    String parentValueExpression = oldValueExpression.getExpressionString();
    String resolvedParentValueExpression = getResolvedExpressionString();
    String prefix = resolvedParentValueExpression.substring(0, resolvedParentValueExpression.lastIndexOf('}')) + '.';
    
    boolean replaceValueExpression = false;
    if (parentValueExpression.startsWith(CC_ATTRS_VALUE)) {
      ValueExpression valueExpression = createValueExpression(resolvedParentValueExpression, oldValueExpression.getExpectedType());
      setValueExpression("value", valueExpression);
      replaceValueExpression = true;
    }
    for (UIComponent component : getFacet(COMPOSITE_FACET_NAME).getChildren()) {
      forEachChild(component, processor, prefix);
    }
    if (replaceValueExpression) {
      setValueExpression("value", oldValueExpression);
    }
  }

  private void forEachChild(UIComponent parent, ChildProcessor processor, String prefix) {
    if (parent instanceof UIInput) {
      UIInput input = (UIInput)parent;
      ValueExpression childValueExpression = input.getValueExpression("value");
      String childValueExpressionString = input.getValueExpression("value").getExpressionString();
      if (childValueExpressionString.startsWith(prefix)) {
        // lookup has to be directed to local value, so replace value expression with #{cc.attrs.value...}
        // TODO maybe cache new value expression?
        ValueExpression valueExpression
          = createValueExpression(childValueExpressionString.replace(prefix, CC_ATTRS_VALUE), childValueExpression.getExpectedType());
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

  private String getResolvedExpressionString() {
    ValueExpression valueExpression = getValueExpression("value");
    String expressionString = valueExpression.getExpressionString();
    
    if (!expressionString.startsWith(CC_ATTRS_VALUE)) {
      return expressionString;
    }
    UIComponent parent = getParent();
    while (!(parent instanceof ValueObjectComponent) && parent != null) {
      parent = parent.getParent();
    }
    String parentExpressionString = parent.getValueExpression("value").getExpressionString();
    return parentExpressionString.substring(0, parentExpressionString.lastIndexOf('}'))
        + expressionString.substring(CC_ATTRS_VALUE.length() - 1);
  }

  protected ValueExpression createValueExpression(String expressionString, Class<?> expectedType) {
    ExpressionFactory expressionFactory = getFacesContext().getApplication().getExpressionFactory();
    ELContext elContext = getFacesContext().getELContext();
    return expressionFactory.createValueExpression(elContext, expressionString, expectedType);
  }

  protected void inCompositeComponentContext(final Runnable runnable) {
    inCompositeComponentContext(new Callable<Void>() {
      public Void call() {
        runnable.run();
        return null;
      }
    });
  }

  protected <R> R inCompositeComponentContext(Callable<R> callable) {
    boolean componentPopped = false;
    if (getValueExpression("value").getExpressionString().startsWith(CC_ATTRS_VALUE)
        && getCurrentCompositeComponent(getFacesContext()) == this) {
      popComponentFromEL(getFacesContext());
      componentPopped = true;
    }
    try {
      return callable.call();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      if (componentPopped) {
        pushComponentToEL(getFacesContext(), this);
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