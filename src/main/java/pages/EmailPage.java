package pages;

import lombok.Getter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class EmailPage {

    private final WebDriver webDriver;
    //кнопка отправить письмо
    @FindBy(xpath = "/html/body/div[1]/div/div[2]/div/div/div/div[3]/div[1]/div[1]/div/button")
    private WebElement sendEmailButton;
    //кнопка подтвердить отправку
    @FindBy(xpath = "/html/body/div[17]/div/div/div[2]/button[1]/span")
    private WebElement confirmSendEmailButton;
    //закрыть окно после отправки
    @FindBy(xpath = "/html/body/div[10]/div/div/div[2]/div[2]/div/div/div[1]/span/span")
    private WebElement closeSendEmailWindowButton;
    //кому отправлять
    @FindBy(xpath = "/html/body/div[1]/div/div[2]/div/div/div/div[2]/div[3]/div[2]/div/div/div[1]/div/div[2]/div/div/label/div/div/input")
    private WebElement destinationEmail;
    //тема письма
    @FindBy(xpath = "/html/body/div[1]/div/div[2]/div/div/div/div[2]/div[3]/div[3]/div[1]/div[2]/div/input")
    private WebElement topicEmail;
    // кнопка ответить на письмо
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[2]/span/div[2]/div/div/div/div/div/div/div[2]/div[1]/div[5]/div/div[1]/div[1]/div[1]/span/span/span[2]")
    //кнопка переслать письмо
    private WebElement replyEmailButton;
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[2]/span/div[2]/div/div/div/div/div/div/div[2]/div[1]/div[5]/div/div[1]/div[1]/div[2]/span/span/span[2]")
    private WebElement forwardEmailButton;
    //Тема письма при его просмотре
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[2]/span/div[2]/div/div/div/div/div/div/div[1]/div[3]/h2")
    private WebElement topicEmailInViewing;
    //Кому отправлять при ответе
    @FindBy(xpath = "/html/body/div[1]/div/div[2]/div/div/div/div[2]/div[3]/div[2]/div/div/div[1]/div/div[2]/div/div[1]/div/div/div/div/span")
    private WebElement destinationEmailInReply;
    //данные в теле письма
    @FindBy(xpath = "/html/body/div[1]/div/div[2]/div/div/div/div[2]/div[3]/div[5]/div/div/div[2]/div[1]")
    private WebElement emailBody;

    @FindBy(xpath = "/html/body/div[6]/div/div[1]/div[1]/div/div[2]/span/div[2]/div/div/div/div/div/div/div[2]/div[1]/div[4]/div[2]/div/div/div/div/div/div/div/div/div/div/div[1]")
    private WebElement emailBodyViewing;
    @FindBy(xpath = "/html/body/div[1]/div/div[2]/div/div/div/div[2]/div[3]/div[5]/div/div/div[2]/div[1]/div[1]")
    private WebElement emailBodyFirefox;
    //из поиска вернуться в корзину входящих
    @FindBy(xpath = "/html/body/div[5]/div/div[1]/div[1]/div/div[2]/span/div[1]/div[1]/div/div/div/div[1]/div/div[2]/a/div/div")
    private WebElement backToIncomeBucket;
    public EmailPage(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
    }

    public void setDestinationEmail(String email) {
        destinationEmail.sendKeys(email);
    }

    public void setTopicEmail(String topic){
        topicEmail.sendKeys(topic);
    }
}
