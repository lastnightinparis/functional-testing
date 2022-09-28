package testcases;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.EmailPage;
import pages.LoginPage;
import pages.MainPage;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeleniumTests {
    public WebDriver webDriver;
    public LoginPage loginPage;
    public MainPage mainPage;

    public EmailPage emailPage;

    private final Config config = ConfigFactory.load("email.conf");

    @AfterEach
    void close() {
        webDriver.switchTo().defaultContent();
        webDriver.quit();
    }

    @BeforeEach
    void firefoxSetup() {
        if (System.getenv("browser").equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            Config config = ConfigFactory.load("email.conf");
            String url = config.getString("url");
            webDriver = new FirefoxDriver(new FirefoxOptions().setBinary("/opt/firefox/firefox-bin"));
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            webDriver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
            webDriver.get(url);
            loginPage = new LoginPage(webDriver);
            mainPage = new MainPage(webDriver);
            emailPage = new EmailPage(webDriver);
        }
    }

    @BeforeEach
    void chromeSetUp() {
        if (System.getenv("browser").equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-web-security");
            options.addArguments("--no-proxy-server");
            options.addArguments("--blink-settings=imagesEnabled=false");
            options.addArguments("--disable-extensions");
            DesiredCapabilities c = DesiredCapabilities.chrome();
            c.setCapability(ChromeOptions.CAPABILITY, options);
            Config config = ConfigFactory.load("email.conf");
            String url = config.getString("url");
            webDriver = new ChromeDriver(c);
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            webDriver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
            webDriver.get(url);
            loginPage = new LoginPage(webDriver);
            mainPage = new MainPage(webDriver);
            emailPage = new EmailPage(webDriver);
        }
    }

    @Test
    public void login() {
        prerequisite_login();
        assertEquals(getConfigValue("userData.user.login"), mainPage.getUsernameField().getText());
    }

    @Test
    public void lookIncomingLetter() {
        prerequisite_login();
        UUID uuid = UUID.randomUUID();
        sendMessageToYourSelf(uuid);
        WebDriverWait waitEmailSending = new WebDriverWait(webDriver, 20);
        waitEmailSending.until(ExpectedConditions.textToBePresentInElement(mainPage.getFirstEmailInListButton(), uuid.toString()));
        mainPage.getFirstEmailInListButton().click();
        assertEquals(uuid.toString(), emailPage.getTopicEmailInViewing().getText());
        mainPage.getInboxButton().click();
        deleteAllMessagesFromIncomingBucket();
    }

    @Test
    public void replyIncomingLetter() {
        prerequisite_login();
        UUID uuid = UUID.randomUUID();
        sendMessageToYourSelf(uuid);
        WebDriverWait waitEmailSending = new WebDriverWait(webDriver, 20);
        waitEmailSending.until(ExpectedConditions.textToBePresentInElement(mainPage.getFirstEmailInListButton(), uuid.toString()));
        mainPage.getFirstEmailInListButton().click();
        emailPage.getReplyEmailButton().click();
        assertEquals("Re: " + uuid, emailPage.getTopicEmail().getAttribute("value"));
        assertEquals("Selenium Test", emailPage.getDestinationEmailInReply().getText());
        emailPage.getSendEmailButton().click();
        Actions pause = new Actions(webDriver);
        pause.pause(Duration.ofSeconds(1)).perform();
        emailPage.getConfirmSendEmailButton().click();
        emailPage.getCloseSendEmailWindowButton().click();
        mainPage.getInboxButton().click();
        assertEquals("2", mainPage.getNumberOfReply().getText());
        deleteAllMessagesFromIncomingBucket();
    }

    @Test
    public void forwardIncomingLetter() {
        prerequisite_login();
        UUID uuid = UUID.randomUUID();
        sendMessageToYourSelf(uuid);
        WebDriverWait waitEmailSending = new WebDriverWait(webDriver, 20);
        waitEmailSending.until(ExpectedConditions.textToBePresentInElement(mainPage.getFirstEmailInListButton(), uuid.toString()));
        mainPage.getFirstEmailInListButton().click();
        emailPage.getForwardEmailButton().click();
        emailPage.setDestinationEmail("lab3_selenium_test@mail.ru");
        assertEquals("Fwd: " + uuid, emailPage.getTopicEmail().getAttribute("value"));
        emailPage.getSendEmailButton().click();
        emailPage.getCloseSendEmailWindowButton().click();
        mainPage.getInboxButton().click();
        WebDriverWait waitForwardSending = new WebDriverWait(webDriver, 20);
        waitForwardSending.until(ExpectedConditions.textToBePresentInElement(mainPage.getFirstEmailInListButton(), uuid.toString()));
        mainPage.getFirstEmailInListButton().click();
        String[] s = emailPage.getTopicEmailInViewing().getText().split(" ");
        if (s.length > 1)
            assertEquals(uuid.toString(), s[1]);
        else assertEquals(uuid.toString(), s[0]);
        mainPage.getInboxButton().click();
        deleteAllMessagesFromIncomingBucket();
    }

    @Test
    public void sendEmail() {
        prerequisite_login();
        UUID data = UUID.randomUUID();
        sendMessageToYourSelf(data, data);
        WebDriverWait waitEmailSending = new WebDriverWait(webDriver, 20);
        waitEmailSending.until(ExpectedConditions.textToBePresentInElement(mainPage.getFirstEmailInListButton(), data.toString()));
        mainPage.getFirstEmailInListButton().click();
        assertEquals(data.toString(), emailPage.getTopicEmailInViewing().getText());
        assertEquals("text", emailPage.getEmailBodyViewing().getText());
        mainPage.getInboxButton().click();
        deleteAllMessagesFromIncomingBucket();

    }

    @Test
    public void searchMessage() {
        prerequisite_login();
        UUID uuid = UUID.randomUUID();
        sendMessageToYourSelf(uuid);
        mainPage.getInboxButton().click();
        WebDriverWait waitSearchSpan = new WebDriverWait(webDriver, 20);
        waitSearchSpan.until(ExpectedConditions.elementToBeClickable(mainPage.getSearchSpan()));
        Actions spanAction = new Actions(webDriver);
        spanAction.moveToElement(mainPage.getSearchSpan()).click(mainPage.getSearchSpan()).perform();
        spanAction.pause(Duration.ofSeconds(1)).perform();
        mainPage.setSearchInput(uuid.toString());
        mainPage.getSearchButton().click();
        WebDriverWait waitEmailRendering = new WebDriverWait(webDriver, 20);
        waitEmailRendering.until(ExpectedConditions.textToBePresentInElement(mainPage.getFirstEmailInListButton(), uuid.toString()));
        webDriver.switchTo().defaultContent();
        spanAction.pause(Duration.ofSeconds(1)).perform();
        mainPage.getFirstEmailInListButton().click();
        String[] searchedEmail = emailPage.getTopicEmailInViewing().getText().split(" ");
        if (searchedEmail.length > 1) {
            assertEquals(uuid.toString(), searchedEmail[1]);

        } else {
            assertEquals(uuid.toString(), searchedEmail[0]);
        }
        WebDriverWait waitRenderingDeleteSearchingButton = new WebDriverWait(webDriver, 5);
        waitRenderingDeleteSearchingButton.until(ExpectedConditions.elementToBeClickable(mainPage.getBackToSearchButton()));
        mainPage.getBackToSearchButton().click();
        spanAction.pause(Duration.ofSeconds(1)).perform();
        mainPage.getDeleteSearchingFilter().click();
        WebDriverWait waitRenderingInbox = new WebDriverWait(webDriver, 5);
        waitRenderingInbox.until(ExpectedConditions.elementToBeClickable(mainPage.getInboxButton()));
        deleteAllMessagesFromIncomingBucket();
    }

    @Test
    public void moveToOtherBucket() {
        prerequisite_login();
        UUID uuid = UUID.randomUUID();
        sendMessageToYourSelf(uuid);
        mainPage.getInboxButton().click();
        mainPage.getSelectAllEmailsButton().click();
        mainPage.getMoveToBucket().click();
        WebElement element = webDriver.findElement(By.xpath("//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[1]/span/div[2]/table/tbody/tr/td/span[2]/div[2]/div/div/div/div/div[8]"));
        element.click();
        assertEquals("Писем нет", mainPage.getNoEmailsSpan().getText());
        mainPage.getGoToSpamButton().click();
        mainPage.getFirstEmailInListButton().click();
        assertEquals(uuid.toString(), emailPage.getTopicEmailInViewing().getText());
    }

    @Test
    public void moveToRubbishBucket() {
        prerequisite_login();
        UUID uuid = UUID.randomUUID();
        sendMessageToYourSelf(uuid);
        mainPage.getInboxButton().click();
        Actions actions = new Actions(webDriver);
        actions.moveToElement(mainPage.getSelectAllEmailsButton()).click(mainPage.getSelectAllEmailsButton()).perform();
        //mainPage.getSelectAllEmailsButton().click();
        mainPage.getMoveToBucket().click();
        WebElement element = webDriver.findElement(By.xpath("//*[@id=\"app-canvas\"]/div/div[1]/div[1]/div/div[1]/span/div[2]/table/tbody/tr/td/span[2]/div[2]/div/div/div/div/div[9]"));
        element.click();
        mainPage.getDeleteEmailButton().click();
        assertEquals("Писем нет", mainPage.getNoEmailsSpan().getText());
        mainPage.getGoToRubbishBucket().click();
        mainPage.getFirstEmailInListButton().click();
        assertEquals(uuid.toString(), emailPage.getTopicEmailInViewing().getText());
    }

    @Test
    public void deleteAllEmailsFromRubbishBucket() {
        prerequisite_login();
        UUID uuid = UUID.randomUUID();
        sendMessageToYourSelf(uuid);
        WebDriverWait waitInboxButton = new WebDriverWait(webDriver, 20);
        waitInboxButton.until(ExpectedConditions.elementToBeClickable(mainPage.getInboxButton()));
        mainPage.getInboxButton().click();
        Actions pause = new Actions(webDriver);
        pause.pause(Duration.ofSeconds(1)).perform();
        deleteAllMessagesFromIncomingBucket();
        assertEquals("Писем нет", mainPage.getNoEmailsSpan().getText());
        mainPage.getGoToRubbishBucket().click();
        mainPage.getSelectAllEmailsButton().click();
        WebDriverWait waitEnterPasswordButton = new WebDriverWait(webDriver, 20);
        waitEnterPasswordButton.until(ExpectedConditions.elementToBeClickable(mainPage.getDeleteAllEmailsButton()));
        mainPage.getDeleteAllEmailsButton().click();
        mainPage.getConfirmDeleteAllEmailsButton().click();
        assertEquals("В корзине пусто", mainPage.getNoItemsInRubbishBucket().getText());

    }

    private void prerequisite_login() {
        String login = getConfigValue("userData.user.login");
        String password = getConfigValue("userData.user.password");
        loginPage.setLogin(login);
        WebDriverWait waitEnterPasswordButton = new WebDriverWait(webDriver, 20);
        waitEnterPasswordButton.until(ExpectedConditions.elementToBeClickable(loginPage.getEnterPasswordButton()));
        loginPage.getEnterPasswordButton().click();
        loginPage.setPassword(password);
        WebDriverWait waitSignInButton = new WebDriverWait(webDriver, 20);
        waitSignInButton.until(ExpectedConditions.elementToBeClickable(loginPage.getSignInButton()));
        loginPage.getSignInButton().click();
    }

    private void sendMessageToYourSelf(UUID uuid, UUID... body) {
        Actions actions = new Actions(webDriver);
        actions.pause(Duration.ofSeconds(1)).perform();
        mainPage.getWriteEmailButtion().click();
        emailPage.setDestinationEmail(getConfigValue("userData.user.login"));
        emailPage.setTopicEmail(uuid.toString());
        if (body.length != 0) {
            actions.moveToElement(emailPage.getEmailBodyFirefox()).clickAndHold().perform();
            actions.release();
            JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;

            jsExecutor.executeScript(
                    "document.querySelector(\"div[tabindex='505']>div\").innerText='text'"
            );
            actions.perform();

        }
        emailPage.getSendEmailButton().click();
        if (body.length == 0) {
            emailPage.getConfirmSendEmailButton().click();
        }
        actions.pause(Duration.ofSeconds(1)).perform();
        emailPage.getCloseSendEmailWindowButton().click();
    }

    private void deleteAllMessagesFromIncomingBucket() {
        Actions actions = new Actions(webDriver);
        actions.pause(Duration.ofSeconds(1)).perform();
        mainPage.getSelectAllEmailsButton().click();
        mainPage.getDeleteAllEmailsButton().click();
        mainPage.getConfirmDeleteAllEmailsButton().click();
    }

    private String getConfigValue(String key) {
        return config.getString(key);
    }

}
