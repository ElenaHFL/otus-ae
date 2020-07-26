import com.google.common.base.Strings;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

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

         switch (normalizeValue(webDriverName)) {
            case FIREFOX: {
                System.out.println("Запускается браузер FIREFOX.");
                WebDriverManager.firefoxdriver().setup();
                if (options == null) return new FirefoxDriver();
                else return new FirefoxDriver(new FirefoxOptions().addArguments(options));
            }
            case IE: {
                System.out.println("Запускается браузер IE.");
                WebDriverManager.iedriver().setup();
                // NOTE: шоб заработал, надо выровнить уровень защиты + сделать масштаб 100%
                if (options == null) return new InternetExplorerDriver();
                else {
                    // Например: -Doptions=ignoreZoomSetting
                    DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
                    capabilities.setCapability(options,true);
                    return new InternetExplorerDriver(new InternetExplorerOptions().merge(capabilities));
                }
            }
            default: {
                // По умолчанию CHROME
                System.out.println("Запускается браузер CHROME.");
                WebDriverManager.chromedriver().setup();
                if (options == null) return new ChromeDriver();
                else return new ChromeDriver(new ChromeOptions().addArguments(options));
            }
        }
    }

    protected static DriverName normalizeValue(String value) {
        value = Strings.nullToEmpty(value)
                .toUpperCase()
                .replace("'", "");

        try {
            return Enum.valueOf(DriverName.class, value);
        } catch(IllegalArgumentException ex) {
            System.out.println("Тест поддерживает только браузеры CHROME/FIREFOX/IE. По умолчанию будет запущен CHROME.");
            return DriverName.CHROME;
        }

    }
}
