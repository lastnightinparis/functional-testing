package pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class LoginPage {
    private final WebDriver webDriver;
    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div/div/div/div/form/div[2]/div[2]/div[1]/div/div/div/div/div/div[1]/div/input")
    private WebElement loginField;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div/div/div/div/form/div[2]/div[2]/div[3]/div/div/div[1]/button")
    private WebElement enterPasswordButton;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div/div/div/div/form/div[2]/div/div[2]/div/div/div/div/div/input")
    private WebElement passwordField;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div/div/div/div/form/div[2]/div/div[3]/div/div/div[1]/div/button")
    private WebElement signInButton;


    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.webDriver = driver;
    }

    public void setLogin(String login) {
        loginField.sendKeys(login);
    }

    public void setPassword(String password) {
        passwordField.sendKeys(password);
    }

}
