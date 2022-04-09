package helpers.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.List;

public class BaseDriver {
    protected WebDriver driver;

    /**
     * Method will initialize driver depending on desired operating system
     *
     * @param desiredDriver operating system or docker env, check switch statement
     */
    protected WebDriver initializeDriver(String desiredDriver) {
        switch (desiredDriver) {
            case "headlessChrome" -> {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(chromeOptions(true));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            }
            case "headlessFirefox" -> {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(firefoxOptions(true));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            }
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(chromeOptions(false));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(firefoxOptions(false));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            }
            default -> throw new IllegalStateException(
                    "Unexpected value: " + desiredDriver);
        }
        return driver;
    }

    private ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(headless);
        chromeOptions.setExperimentalOption("excludeSwitches", List.of("disable-popup-blocking", "enable-automation"));
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--ignore-certificate-errors");
        return chromeOptions;
    }

    private FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--disable-extensions");
        firefoxOptions.addArguments("--ignore-certificate-errors");
        firefoxOptions.addArguments("--disable-infobars");
        firefoxOptions.addArguments("--disable-gpu");
        firefoxOptions.setAcceptInsecureCerts(headless);
        return firefoxOptions;
    }
}
