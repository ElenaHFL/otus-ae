package cases;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import pages.LoginPage;
import pages.PersonalInfoPage;
import utils.BaseHooks;

public class PageObjectTest extends BaseHooks {

    private Logger logger = LogManager.getLogger(PageObjectTest.class);

    @Test
    @Epic("GitHub")
    @Feature("Авторизация")
    @Story("Успешная авторизация и заполнение данных")
    @Description("Тест проверяет, авторизацию на сайте OTUS и заполнение раздела О себе данными")
    public void authSuccessfulTest() {
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

        setDown();
        logger.info("Закрыли браузер");
    }

    @Test
    @Epic("GitHub")
    @Feature("Авторизация")
    @Story("Авторизация не пройдена")
    @Description("Тест проверяет авторизацию на сайте OTUS, падает специально, чтобы посмотреть, как падающий тест отображается в отчетах Allure")
    public void authFailTest() {
        LoginPage loginPage = new LoginPage(driver);
        PersonalInfoPage personalInfoPage = new PersonalInfoPage(driver);

        // 1. Авторизоваться на сайте
        loginPage.open();
        loginPage.auth("login", "password");

        // 2. Войти в личный кабинет
        personalInfoPage.openPersonalInfo();
    }

}
