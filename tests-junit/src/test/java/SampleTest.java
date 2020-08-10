import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SampleTest {

    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(SampleTest.class);

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        logger.info("Драйвер поднят");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        logger.info("Неявное ожидание выставили в 3 секунд");
    }

    @Test
    public void checkPage() {
        // Открыть в Chrome сайт Яндекс.Маркет - раздел "Мобильные телефоны"
        driver.get("https://market.yandex.ru/catalog--mobilnye-telefony/54726/list");
        logger.info("Открыта страница market.yandex на разделе \"Мобильные телефоны\"");

        WebDriverWait wait = new WebDriverWait(driver, 60L, 250L);

        // Отфильтровать список товаров: Xiaomi и HUAWEI (вместо RedMi)
        driver.findElement(By.xpath("//div[@data-zone-name='search-filters-aside']//span[text()='Xiaomi']")).click();
        driver.findElement(By.xpath("//div[@data-zone-name='search-filters-aside']//span[text()='HUAWEI']")).click();
        driver.findElement(By.xpath("//div[@data-zone-name='search-filters-aside']//span[text()='смартфон']")).click();
        // Ждем пока список загрузится
        wait.until(driver -> driver.findElements(By.xpath("//div[@data-zone-name='SearchResults']/div/div")).size() < 2);
        logger.info("Отфильтровали список товаров по Xiaomi и HUAWEI (вместо RedMi)");

        // Отсортировать по цене (от меньшей к большей)
        driver.findElement(By.xpath("//button[text()='по цене']")).click();
        // Ждем пока список загрузится
        wait.until(driver -> driver.findElements(By.xpath("//div[@data-zone-name='SearchResults']/div/div")).size() < 2);
        logger.info("Отфильтровали список по цене");

        // Добавить первый в списке Xiaomi
        addItemToCompare("Xiaomi");

        // Добавить первый в списке HUAWEI
        addItemToCompare("HUAWEI");

        // Перейти в раздел Сравнение
        driver.findElement(By.tagName("body")).sendKeys(Keys.CONTROL,Keys.HOME);
        driver.findElement(By.xpath("//div[@data-apiary-widget-id='/content/header/compareButton']/a")).click();
        // -- Проверить, что в списке товаров 2 позиции
        List<WebElement> el = driver.findElements(By.xpath("//div[@data-apiary-widget-id='/content/compareContent']//img[@alt]"));
        assertTrue(el.size() == 2);

        // Нажать на опцию "все характеристики" и проверить, что в списке характеристик появилась позиция "Операционная система"
        assertTrue(isElementDisplayed("Все характеристики"));

        // Нажать на опцию "различающиеся характеристики" и проверить, что позиция "Операционная система" не отображается в списке характеристик
        assertFalse(isElementDisplayed("Различающиеся характеристики"));
    }

    private void addItemToCompare(String str) {
        String item = "//div[@data-zone-name='SearchResults']//article//div[text()='" + str + "'][1]//..//..//div[@aria-label][1]";
        String info = "//div[@data-apiary-widget-id='/content/popupInformer']//div[contains(text(),'добавлен к сравнению')]";

        WebDriverWait wait = new WebDriverWait(driver, 10L, 100L);
        // Добавить в список
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(item)));
        driver.findElement(By.xpath(item)).click();

        // Проверить, что отобразилась плашка "Товар {имя товара} добавлен к сравнению"
        assertTrue(driver.findElement(By.xpath(info)).isDisplayed());
        logger.info("В сравнение добавлен первый в списке товар марки: " + str);
    }

    private boolean isElementDisplayed(String str) {
        // Нажать на опцию
        driver.findElement(By.xpath("//button[text()='" + str + "']")).click();
        // -- Проверить, что позиция "Операционная система" не отображается в списке характеристик
        try {
            Boolean position = driver.findElement(By.xpath("//div[text()='Операционная система']")).isDisplayed();
            logger.info("Отображается ли на вкладке " + str + " позиция Операционная система: " + position);
            return position;
        } catch (Exception e) {
            logger.info("На вкладке " + str + " позиция Операционная система отсутствует");
            return false;
        }
    }

    @After
    public void setDown() {
        if (driver != null) {
            logger.info("Пока");
            driver.quit();
        }
    }

}
