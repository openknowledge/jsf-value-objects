package de.openknowledge.jsf;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import javax.faces.webapp.FacesServlet;

@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class ValueObjectComponentTest {

  @Drone
  private WebDriver browser;
  
  @Deployment
  public static WebArchive deployment() {
    return ShrinkWrap.create(WebArchive.class)
        .addAsWebResource("test.xhtml")
        .setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class)
          .addDefaultNamespaces()
          .createServlet()
            .servletName("Faces Servlet")
            .servletClass(FacesServlet.class.getName())
          .up()
          .createServletMapping()
            .servletName("Faces Servlet")
            .urlPattern("*.xhtml")
          .up()
          .exportAsString()));
  }

  @Test
  public void page(@InitialPage TestPage page) {

  }

  @Test
  public void testComponent() {

  }
}
