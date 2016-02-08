package de.openknowledge.jsf.component.telephone;

import de.openknowledge.jsf.component.Deployments;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class ValueObjectComponentEmptyValueTest extends AbstractValueObjectComponentTest {
  
  @Deployment
  public static WebArchive deployment() {
    System.out.println(Deployments.phoneNumberDeployment()
        .addAsWebResource(EmptyAsset.INSTANCE, "empty.txt")
        .addAsWebResource("emptyPhoneNumberTest.xhtml", "phoneNumber.xhtml").toString(true));
    return Deployments.phoneNumberDeployment()
        .addAsWebResource(EmptyAsset.INSTANCE, "empty.txt")
        .addAsWebResource("emptyPhoneNumberTest.xhtml", "phoneNumber.xhtml");
  }

  @Override
  protected String initialValue() {
    return "null";
  }
}
