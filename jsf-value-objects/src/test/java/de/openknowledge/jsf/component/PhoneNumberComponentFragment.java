package de.openknowledge.jsf.component;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PhoneNumberComponentFragment {

  @FindBy(css = "input[id$=':areaCode']")
  private WebElement areaCode;

  @FindBy(css = "input[id$=':subscriberNumber']")
  private WebElement subscriberNumber;

  public String getAreaCode() {
    return areaCode.getAttribute("value");
  }

  public void setAreaCode(String code) {
    areaCode.sendKeys(code);
  }

  public String getSubscriberNumber() {
    return subscriberNumber.getAttribute("value");
  }

  public void setSubscriberNumber(String number) {
    subscriberNumber.sendKeys(number);
  }
}
