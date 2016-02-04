package de.openknowledge.jsf.component;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
public class ValueObjectComponentEmptyValueTest extends AbstractValueObjectComponentTest {
  
  @Deployment
  public static WebArchive deployment() {
    return Deployments.phoneNumberDeployment()
        .addAsWebResource("emptyPhoneNumberTest.xhtml");
  }

  @Test
  public void validPhoneNumber(@InitialPage EmptyPhoneNumberTestPage page) {
    assertThat(page.getPhoneNumber().getAreaCode(), is(""));
    assertThat(page.getPhoneNumber().getSubscriberNumber(), is(""));
    super.validPhoneNumber(page);
  }

  @Test
  public void emptyAreaCode(@InitialPage EmptyPhoneNumberTestPage page) {
    super.emptyAreaCode(page);
  }

  @Test
  public void inconvertibleAreaCode(@InitialPage EmptyPhoneNumberTestPage page) {
    super.inconvertibleAreaCode(page);
  }

  @Test
  public void invalidAreaCode(final @InitialPage EmptyPhoneNumberTestPage page) {
    super.invalidAreaCode(page);
  }

  @Test
  public void emptySubscriberNumber(@InitialPage EmptyPhoneNumberTestPage page) {
    super.emptySubscriberNumber(page);
  }

  @Test
  public void inconvertibleSubscriberNumber(@InitialPage EmptyPhoneNumberTestPage page) {
    super.inconvertibleSubscriberNumber(page);
  }

  @Test
  public void invalidSubscriberNumber(@InitialPage EmptyPhoneNumberTestPage page) {
    super.invalidSubscriberNumber(page);
  }

  @Test
  public void emptyPhoneNumber(@InitialPage EmptyPhoneNumberTestPage page) {
    super.emptyPhoneNumber(page);
  }

  @Test
  public void inconvertiblePhoneNumber(@InitialPage EmptyPhoneNumberTestPage page) {
    super.inconvertiblePhoneNumber(page);
  }

  @Test
  public void invalidPhoneNumber(@InitialPage EmptyPhoneNumberTestPage page) {
    super.invalidPhoneNumber(page);
  } 

  @Override
  protected String initialValue() {
    return "null";
  }
}
