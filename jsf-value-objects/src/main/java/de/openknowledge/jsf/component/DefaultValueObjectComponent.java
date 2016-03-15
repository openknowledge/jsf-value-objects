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

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 * @author Arne Limburg - open knowledge GmbH
 */
@FacesComponent("de.openknowledge.jsf.component.DefaultValueObjectComponent")
public class DefaultValueObjectComponent extends ValueObjectComponent {

  @Override
  public void processDecodes(FacesContext context) {
    super.processDecodes(context);
    ValueExpression oldValueExpression = getValueExpression("value");
    String parentValueExpression = oldValueExpression.getExpressionString();
    String resolvedParentValueExpression = getResolvedExpressionString();
    final String prefix = parentValueExpression.substring(0, parentValueExpression.lastIndexOf('}')) + '.';
    final String resolvedPrefix = resolvedParentValueExpression.substring(0, resolvedParentValueExpression.lastIndexOf('}')) + '.';
    
    boolean replaceValueExpression = false;
    if (parentValueExpression.startsWith(CC_ATTRS_VALUE)) {
      ValueExpression valueExpression = createValueExpression(resolvedParentValueExpression, oldValueExpression.getExpectedType());
      setValueExpression("value", valueExpression);
      replaceValueExpression = true;
    }
    if (replaceValueExpression) {
      setValueExpression("value", oldValueExpression);
    }
    forEachChild(new ChildProcessor() {

      public void process(UIInput child) {
        ValueExpression childValueExpression = child.getValueExpression("value");
        String childValueExpressionString = childValueExpression.getExpressionString();
        if (childValueExpressionString.startsWith(prefix) || childValueExpressionString.startsWith(resolvedPrefix)) {
          // lookup has to be directed to local value, so replace value expression with #{cc.attrs.value...}
          // TODO maybe cache new value expression?
          String replacement = childValueExpressionString.startsWith(prefix) ? prefix : resolvedPrefix;
          ValueExpression valueExpression
            = createValueExpression(childValueExpressionString.replace(replacement, CC_ATTRS_VALUE), childValueExpression.getExpectedType());
          child.setValueExpression("value", valueExpression);
        }
      }
    });
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
}