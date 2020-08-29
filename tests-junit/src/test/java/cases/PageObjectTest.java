package cases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import pages.LoginPage;
import pages.PersonalInfoPage;
import utils.BaseHooks;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class PageObjectTest extends BaseHooks {

    private Logger logger = LogManager.getLogger(PageObjectTest.class);

    @Test
    public void pageObjectTest() {
        String login = System.getProperty("login");
        String password = System.getProperty("password");

        LoginPage loginPage = new LoginPage(driver);
        PersonalInfoPage personalInfoPage = new PersonalInfoPage(driver);

        // 1. Авторизоваться на сайте
        loginPage.open();
        loginPage.auth(login, password);
        // 2. Войти в личный кабинет
        personalInfoPage.openPersonalInfo();

        // 3. В разделе "О себе" заполнить все поля "Личные данные" и добавить не менее двух контактов
        personalInfoPage.inputInfo("fname", "Елена");
        personalInfoPage.inputInfo("lname", "Аверьянова");
        personalInfoPage.inputInfo("fname_latin", "Elena");
        personalInfoPage.inputInfo("lname_latin", "Averyanova");
        personalInfoPage.inputInfo("blog_name", "ElenaA");
        personalInfoPage.inputInfo("date_of_birth", "05.12.1986");

        personalInfoPage.addContact("WhatsApp", "контакт 1");
        personalInfoPage.addContact("Skype", "контакт 2");

        // 4. Нажать сохранить
        driver.findElement(By.cssSelector(".lk-cv-action-buttons button[name='continue']")).click();
        logger.info("Сохранили данные");

        // 5. Открыть https://otus.ru в "чистом браузере"
        setDown();
        logger.info("Закрыли браузер");

        setUp();
        LoginPage loginPage2 = new LoginPage(driver);
        PersonalInfoPage personalInfoPage2 = new PersonalInfoPage(driver);

        // 6. Авторизоваться на сайте
        loginPage2.open();
        loginPage2.auth(login, password);
        // 7. Войти в личный кабинет
        personalInfoPage2.openPersonalInfo();

        // 8. Проверить, что в разделе "О себе" отображаются указанные ранее данные
        logger.info("Проверка полей в блоке Персональные данные");
        assertTrue("Значение поля fname не соотв. ожиданиям", personalInfoPage2.validateInfo("fname", "Елена"));
        assertTrue("Значение поля lname не соотв. ожиданиям", personalInfoPage2.validateInfo("lname", "Аверьянова"));
        assertTrue("Значение поля fname_latin не соотв. ожиданиям", personalInfoPage2.validateInfo("fname_latin", "Elena"));
        assertTrue("Значение поля lname_latin не соотв. ожиданиям", personalInfoPage2.validateInfo("lname_latin", "Averyanova"));
        assertTrue("Значение поля blog_name не соотв. ожиданиям", personalInfoPage2.validateInfo("blog_name", "ElenaA"));
        assertTrue("Значение поля date_of_birth не соотв. ожиданиям", personalInfoPage2.validateInfo("date_of_birth", "05.12.1986"));

        logger.info("Проверка контактов");
        Map<String, String> contacts = personalInfoPage2.getContacts();

        // Проверяем, что среди контактов есть нужные
        assertTrue("Среди контактов нет WhatsApp = контакт 1", contacts.get("whatsapp").contentEquals("контакт 1"));
        assertTrue("Среди контактов нет Skype = контакт 2", contacts.get("skype").contentEquals("контакт 2"));

    }

}
