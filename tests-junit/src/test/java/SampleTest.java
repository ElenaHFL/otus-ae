import config.ServerConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class SampleTest {

    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(SampleTest.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);

    @Before
    public void setUp() {
        String browser = System.getProperty("browser");
        String options = System.getProperty("options");
        logger.info("Run with browser: " + browser);
        logger.info("Run with options: " + options);

        driver = WebDriverFactory.create(browser, options);
        logger.info("Драйвер поднят");
    }

    @Test
    public void openBrowser() {
        driver.get(cfg.url());
        logger.info("Открыта страница otus");
    }

    @After
    public void setDown() {
        if (driver != null) {
            logger.info("Пока");
            driver.quit();
        }
    }

}
