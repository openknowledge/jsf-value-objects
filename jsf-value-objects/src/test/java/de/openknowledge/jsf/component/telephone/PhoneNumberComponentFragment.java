package de.openknowledge.jsf.component.telephone;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PhoneNumberComponentFragment {

  @FindBy(css = "input[id$=':areaCode']")
  private WebElement areaCode;
  
  @FindBy(css = "span[id$=':areaCodeMessage']")
  private WebElement areaCodeMessage;

  @FindBy(css = "input[id$=':subscriberNumber']")
  private WebElement subscriberNumber;

  @FindBy(css = "span[id$=':subscriberNumberMessage']")
  private WebElement subscriberNumberMessage;

  public String getAreaCode() {
    return areaCode.getAttribute("value");
  }

  public void setAreaCode(String code) {
    areaCode.clear();
    areaCode.sendKeys(code);
  }

  public String getAreaCodeMessage() {
    return areaCodeMessage.getText();
  }

  public String getSubscriberNumber() {
    return subscriberNumber.getAttribute("value");
  }

  public void setSubscriberNumber(String number) {
    subscriberNumber.clear();
    subscriberNumber.sendKeys(number);
  }

  public String getSubscriberNumberMessage() {
    return subscriberNumberMessage.getText();
  }
}
