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
package de.openknowledge.jsf.component.telephone;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import de.openknowledge.jsf.component.Deployments;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class ValueObjectComponentSimpleTest extends AbstractValueObjectComponentTest {
  
  @Deployment
  public static WebArchive deployment() {
    return Deployments.phoneNumberDeployment()
        .addAsWebResource("phoneNumberTest.xhtml");
  }

  @Test
  public void validPhoneNumber(@InitialPage PhoneNumberTestPage page) {
    super.validPhoneNumber(page);
  }

  @Test
  public void emptyAreaCode(@InitialPage PhoneNumberTestPage page) {
    super.emptyAreaCode(page);
  }

  @Test
  public void inconvertibleAreaCode(@InitialPage PhoneNumberTestPage page) {
    super.inconvertibleAreaCode(page);
  }

  @Test
  public void invalidAreaCode(final @InitialPage PhoneNumberTestPage page) {
    super.invalidAreaCode(page);
  }

  @Test
  public void emptySubscriberNumber(@InitialPage PhoneNumberTestPage page) {
    super.emptySubscriberNumber(page);
  }

  @Test
  public void inconvertibleSubscriberNumber(@InitialPage PhoneNumberTestPage page) {
    super.inconvertibleSubscriberNumber(page);
  }

  @Test
  public void invalidSubscriberNumber(@InitialPage PhoneNumberTestPage page) {
    super.invalidSubscriberNumber(page);
  }

  @Test
  public void emptyPhoneNumber(@InitialPage PhoneNumberTestPage page) {
    assertThat(page.getPhoneNumber().getAreaCode(), is("0441"));
    assertThat(page.getPhoneNumber().getSubscriberNumber(), is("4082100"));
    super.emptyPhoneNumber(page);
  }

  @Test
  public void inconvertiblePhoneNumber(@InitialPage PhoneNumberTestPage page) {
    super.inconvertiblePhoneNumber(page);
  }

  @Test
  public void invalidPhoneNumber(@InitialPage PhoneNumberTestPage page) {
    super.invalidPhoneNumber(page);
  }

  @Override
  protected String initialValue() {
    return PhoneNumberController.INITIAL_VALUE.toString();
  }
}
