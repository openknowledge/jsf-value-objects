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
public class PhoneNumberComponentEmptyValueTest extends AbstractValueObjectComponentTest {
  
  @Deployment
  public static WebArchive deployment() {
    return Deployments.phoneNumberDeployment()
        .addAsWebResource("de/openknowledge/jsf/component/telephone/phoneNumber.xhtml", "resources/ok/phoneNumber.xhtml")
        .addAsWebResource("emptyPhoneNumberComponentTest.xhtml");
  }

  @Test
  public void validPhoneNumber(@InitialPage EmptyPhoneNumberComponentTestPage page) {
    assertThat(page.getPhoneNumber().getAreaCode(), is(""));
    assertThat(page.getPhoneNumber().getSubscriberNumber(), is(""));
    super.validPhoneNumber(page);
  }

  @Test
  public void emptyAreaCode(@InitialPage EmptyPhoneNumberComponentTestPage page) {
    super.emptyAreaCode(page);
  }

  @Test
  public void inconvertibleAreaCode(@InitialPage EmptyPhoneNumberComponentTestPage page) {
    super.inconvertibleAreaCode(page);
  }

  @Test
  public void invalidAreaCode(final @InitialPage EmptyPhoneNumberComponentTestPage page) {
    super.invalidAreaCode(page);
  }

  @Test
  public void emptySubscriberNumber(@InitialPage EmptyPhoneNumberComponentTestPage page) {
    super.emptySubscriberNumber(page);
  }

  @Test
  public void inconvertibleSubscriberNumber(@InitialPage EmptyPhoneNumberComponentTestPage page) {
    super.inconvertibleSubscriberNumber(page);
  }

  @Test
  public void invalidSubscriberNumber(@InitialPage EmptyPhoneNumberComponentTestPage page) {
    super.invalidSubscriberNumber(page);
  }

  @Test
  public void emptyPhoneNumber(@InitialPage EmptyPhoneNumberComponentTestPage page) {
    super.emptyPhoneNumber(page);
  }

  @Test
  public void inconvertiblePhoneNumber(@InitialPage EmptyPhoneNumberComponentTestPage page) {
    super.inconvertiblePhoneNumber(page);
  }

  @Test
  public void invalidPhoneNumber(@InitialPage EmptyPhoneNumberComponentTestPage page) {
    super.invalidPhoneNumber(page);
  } 

  @Override
  protected String initialValue() {
    return "null";
  }
}
