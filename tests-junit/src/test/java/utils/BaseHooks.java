package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class BaseHooks {
    protected static WebDriver driver;

    protected static String login;
    protected static String password;

    @BeforeEach
    public void loadConfig() throws Throwable {
        SuiteConfiguration config = new SuiteConfiguration();
        login = config.getProperty("login");
        password = config.getProperty("password");
    }

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        if (driver != null) {
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        }
    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}