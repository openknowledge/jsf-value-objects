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
package de.openknowledge.jsf.converter.telephone;

import de.openknowledge.domain.telephone.AreaCode;
import de.openknowledge.jsf.converter.AbstractSimpleValueObjectConverter;

import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = AreaCode.class)
public class AreaCodeConverter extends AbstractSimpleValueObjectConverter<AreaCode> {

}
