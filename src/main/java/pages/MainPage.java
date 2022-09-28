package pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class MainPage {
    private final WebDriver webDriver;
    //почта пользователя
    @FindBy(xpath = "/html/body/div[3]/div[1]/div/div[2]/div/span[2]")
    private WebElement usernameField;

    //входящие
    @FindBy(xpath = "/html/body/div[6]/div/div[1]/div[1]/div/div[2]/span/div[1]/div[1]/div/div/div/div[2]/div/div/div/div/nav/a[1]")
    private WebElement inboxButton;

    //написать письмо
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[2]/span/div[1]/div[1]/div/div/div/div[1]/div/div/a")
    private WebElement writeEmailButtion;

    //первое письмо в почте
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[2]/span/div[2]/div/div/div/div/div[1]/div/div/div/div[1]/div/div/a[1]/div[4]/div")
    private WebElement firstEmailInListButton;

    //отчистить содержимое входящих
    @FindBy(xpath = "/html/body/div[6]/div/div[1]/div[1]/div/div[1]/span/div[2]/table/tbody/tr/td[1]/span[1]/div/span/span/span[2]")
    private WebElement selectAllEmailsButton;

    //подтвердить отчистку содержимого входящих
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[1]/span/div[2]/table/tbody/tr/td/span[2]/div[1]/span/span/span[2]")
    private WebElement deleteAllEmailsButton;

    @FindBy(xpath = "/html/body/div[10]/div/div/div[2]/div[2]/div/div/div[4]/div[1]/span/span/span")
    private WebElement confirmDeleteAllEmailsButton;

    //цифра в количестве ответов
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[2]/span/div[2]/div/div/div/div/div[1]/div/div/div/div[1]/div/div/a[1]/div[4]/div/div[3]/div/div/div/span/span")
    private WebElement numberOfReply;

    //найти в почте нажать
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[1]/span/div[4]/div/div[1]/div")
    private WebElement searchSpan;

    //поле ввода для поиска в почте
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[1]/span/div[4]/div/div[3]/div/div/span/span/div/input")
    private WebElement searchInput;
    //кнопка найти в почте
    @FindBy(xpath = "/html/body/div[6]/div/div[1]/div[1]/div/div[1]/span/div[4]/div/div[3]/div/span[2]")
    private WebElement searchButton;
    //Вернуться
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[1]/span/div[1]/div/div/div/span/div/span/span/span[2]")
    private WebElement backToSearchButton;
    //Сбросить фильтр
    @FindBy(xpath = "/html/body/div[6]/div/div[1]/div[1]/div/div[1]/span/div[4]/div/div[1]/span")
    private WebElement deleteSearchingFilter;
    //В спам
    @FindBy(xpath = "/html/body/div[11]/div/div/div/div/div[6]")
    private WebElement moveToSpamButton;

    //Картинка нет писем
    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[2]/span/div[2]/div/div/div/div/div[1]/div/div/div/div/div/div/div[2]/span")
    private WebElement noEmailsSpan;

    //Перейти в корзину спам
    @FindBy(xpath = "/html/body/div[6]/div/div[1]/div[1]/div/div[2]/span/div[1]/div[1]/div/div/div/div[2]/div/div/div/div/nav/a[8]")
    private WebElement goToSpamButton;

    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[1]/span/div[2]/table/tbody/tr/td/span[2]/div[2]")
    private WebElement moveToBucket;

    @FindBy(xpath = "//*[@id=\"sideBarContent\"]/div/nav/a[9]")
    private WebElement goToRubbishBucket;

    @FindBy(xpath = "/html/body/div[10]/div/div/div[2]/div[2]/div/div/div[4]/div[1]/span/span/span")
    private WebElement deleteEmailButton;

    @FindBy(xpath = "//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[2]/span/div[2]/div/div/div/div/div[1]/div/div/div/div/div/div/div[2]/span")
    private WebElement noItemsInRubbishBucket;
    public MainPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
    }

    public void setSearchInput(String text) {
        searchInput.sendKeys(text);
    }
}
