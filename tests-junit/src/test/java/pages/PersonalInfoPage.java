package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;

public class PersonalInfoPage extends AbstractPage {

    private Logger logger = LogManager.getLogger(PersonalInfoPage.class);

    public PersonalInfoPage(WebDriver driver) {
        super(driver);
    }

    public void openPersonalInfo(){
        logger.info("Переход в раздел Персональные данные");

        String avatar = ".ic-blog-default-avatar";
        String personalInfo = "a[href='/lk/biography/personal/']";

        // Ждем пока загрузится страница
        WebDriverWait wait = new WebDriverWait(driver, 10L, 250L);
        wait.until(drv -> drv.findElements(By.cssSelector(avatar)).size() > 0);

        WebElement item = driver.findElement(By.cssSelector(avatar));
        WebElement link = driver.findElement(By.cssSelector(personalInfo));

        Actions builder = new Actions(driver);
        builder.moveToElement(item).click(link);
        builder.build().perform();

        // Ждем открытия раздела "О себе"
        wait.until(drv -> drv.findElements(By.cssSelector(personalInfo)).size() > 0);
    }

    public void inputInfo(String field, String val) {
        logger.info(String.format("Добавление информации в поле: %s = %s", field, val));

        WebElement we = driver.findElement(By.cssSelector("input[name='" + field + "']"));
        we.clear();
        we.sendKeys(val);
    }

    public void addContact(String type, String val) {
        logger.info("Добавление контакта: " + type + " = " + val);

        String contact = "//div[@data-prefix='contact']//button[text()='Добавить']";

        driver.findElement(By.xpath(contact)).click();
        driver.findElement(By.xpath("//div[@data-prefix='contact']//div[@data-num][last()]//input[@type='text']")).sendKeys(val);
        driver.findElement(By.xpath("//div[@data-prefix='contact']//div[@data-num][last()]//span[text()='Способ связи']/..")).click();
        driver.findElement(By.xpath("//div[@data-prefix='contact']//div[@data-num][last()]//button[@title='" + type + "']")).click();
    }

    // Собираем список контактов
    public Map<String, String> getContacts() {
        Map<String, String> contacts = new HashMap<String, String>();
        for (WebElement we : driver.findElements(By.xpath("//div[@data-prefix='contact']//div[@data-num]"))) {
            String num = we.getAttribute("data-num");
            String key = we.findElement(By.cssSelector("input[name='contact-" + num + "-service']")).getAttribute("value");
            String value = we.findElement(By.cssSelector("input[name='contact-" + num + "-value']")).getAttribute("value");
            logger.debug("Найден контакт: " + key + " = " + value);
            contacts.put(key.toLowerCase(), value.toLowerCase());
        }
        return contacts;
    }

    public boolean validateInfo(String field, String val) {
        logger.info(String.format("Проверка содержимого поля: %s = %s", field, val));
        String str = driver.findElement(By.cssSelector("input[name='" + field + "']")).getAttribute("value");
        return str.contentEquals(val);
    }
}
