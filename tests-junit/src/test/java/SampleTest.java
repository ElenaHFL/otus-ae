import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import config.ServerConfig;

import static org.junit.Assert.assertEquals;

public class SampleTest {

    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(SampleTest.class);
    private ServerConfig cfg = ConfigFactory.create(ServerConfig.class);

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        logger.info("Драйвер поднят");
    }

    @Test
    public void checkPage() {
        driver.get(cfg.url());
        logger.info("Открыта страница otus");

        logger.info("Проверка title страницы otus");
        String actualTitle = driver.getTitle();
        String expectedTitle = "Онлайн‑курсы для профессионалов, дистанционное обучение современным профессиям";
        assertEquals("При сравнении title что-то пошло не так...", expectedTitle, actualTitle);
    }

    @After
    public void setDown() {
        if (driver != null) {
            logger.info("Пока");
            driver.quit();
        }
    }

}
