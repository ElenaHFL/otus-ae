import com.google.common.base.Strings;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class WebDriverFactory {

    // Допустимые имена браузеров
    enum DriverName {
        CHROME,
        FIREFOX,
        IE
    }

    public static WebDriver create(String webDriverName) {
        return create(webDriverName, null);
    }

    public static WebDriver create(String webDriverName, String options) {

        webDriverName = normalizeValue(webDriverName);
        System.out.println("Ищем:" + webDriverName);

        if (webDriverName.equals(String.valueOf(DriverName.CHROME))) {
            WebDriverManager.chromedriver().setup();
            if (options == null) return new ChromeDriver();
            else return new ChromeDriver(new ChromeOptions().addArguments(options));
        } else if (webDriverName.equals(String.valueOf(DriverName.FIREFOX))) {
            WebDriverManager.firefoxdriver().setup();
            if (options == null) return new FirefoxDriver();
            else return new FirefoxDriver(new FirefoxOptions().addArguments(options));
        } else if (webDriverName.equals(String.valueOf(DriverName.IE))) {
            WebDriverManager.iedriver().setup();
            // Для IE нет метода addArguments, видать там иначе устроено (T_T)
            // NOTE: шоб заработал, надо выровнить уровень защиты + сделать масштаб 100%
            return new InternetExplorerDriver();
        } else {
            System.out.println("Тест поддерживает только браузеры CHROME/FIREFOX/IE. По умолчанию будет запущен CHROME.");
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
        }
    }

    protected static String normalizeValue(String value) {
        value = Strings.nullToEmpty(value)
                .toUpperCase()
                .replace("'", "");
        return value;
    }
}
