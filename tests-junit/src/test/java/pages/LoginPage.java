package pages;

import config.ServerConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends AbstractPage {

    private Logger logger = LogManager.getLogger(LoginPage.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);
    private By locator = By.cssSelector("button.header2__auth");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Открыть страницу в брайзере
    public LoginPage open() {
        driver.get(cfg.url());
        return this;
    }

    // Авторизоваться на сайте
    public void auth(String login, String password) {
        if (login == null || password == null) {
            logger.error("При запуске теста не переданы логин/пароль для авторизации на сайте.");
            logger.error("Нужно запускать как: mvn clean test -Dlogin=xxx -Dpassword=yyy");
        }

        driver.findElement(locator).click();

        driver.findElement(By.cssSelector("input[type='text']")).sendKeys(login);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button.new-button")).click();

        logger.info("Авторизация на сайте прошла успешно");
    }
}
