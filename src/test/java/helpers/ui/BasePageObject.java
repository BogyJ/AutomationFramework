package helpers.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Base64;
import java.util.List;

public class BasePageObject {
    public WebDriverWait wait;
    public WebDriver driver;
    private JavascriptExecutor js;
    private final Logger log = LogManager.getLogger(BasePageObject.class);

    public void openWebPage(String url) {
        log.info("Navigate to: {}", url);
        driver.get(url);
        driver.manage().window().maximize();
    }

    /**
     * Wait for element to be clickable
     *
     * @param element
     * @return WebElement after wait
     */
    public boolean waitToBeClickable(WebElement element, Long waitTime) {
        try {
            log.info("Wait up to {} second(s) for an element to be clickable", waitTime);
            wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
            return wait.until(ExpectedConditions.elementToBeClickable(element)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            System.out.println("Element not found.");
            return false;
        }
    }

    /**
     * Wait to finish page loanding after JavaScript executor is finished
     *
     * @param waitTime
     */
    public void waitForLoad(long waitTime) {
        log.info("Wait {} second(s) to load the page", waitTime);
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState")
                                .toString().equals("complete");
                    }
                };
        try {
            Thread.sleep(2000);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
            wait.until(expectation);
        } catch (Throwable error) {
        }
    }


    /**
     * Method will try to find clickable element 3 times
     *
     * @param by By identifier of WebElement
     * @return WebElement
     */
    public WebElement retryFindElement(By by) {
        WebElement el = null;
        int attempts = 0;
        while (attempts < 5) {
            try {
                el = driver.findElement(by);
                if (el != null) {
                    break;
                }
            } catch (StaleElementReferenceException e) {
                el = driver.findElement(by);
            }
            attempts++;
        }
        return el;
    }

    public WebElement retryFindElementByDataHeaderFeature(String dataHeaderFeature) {
        By by = By.cssSelector("[data-header-feature='" + dataHeaderFeature + "']");

        return retryFindElement(by);
    }

    public WebElement retryFindElementByDataContentFeature(String dataContentFeature) {
        By by = By.cssSelector("[data-content-feature='" + dataContentFeature + "']");

        return retryFindElement(by);
    }

    public WebElement retryFindElementByDataTestId(String dataTestId) {
        By by = By.cssSelector("[data-testid='" + dataTestId + "']");

        return retryFindElement(by);
    }

    /**
     * Find WebElement inside WebElement Method will try to find clickable element 3 times
     *
     * @param by By identifier of WebElement
     * @return WebElement
     */
    public WebElement retryFindElement(WebElement element, By by) {
        WebElement el = null;
        int attempts = 0;
        while (attempts < 3) {
            try {
                el = element.findElement(by);
                if (el != null) {
                    break;
                }
            } catch (StaleElementReferenceException e) {
                el = element.findElement(by);
            }
            attempts++;
        }
        return el;
    }

    public WebElement retryFindElementByDataTestId(WebElement element, String dataTestId) {
        By by = By.cssSelector("[data-testid='" + dataTestId + "']");

        return retryFindElement(element, by);
    }

    public List<WebElement> retryFindElements(By by) {
        List<WebElement> el = null;
        int attempts = 0;
        while (attempts < 3) {
            try {
                el = driver.findElements(by);
                if (el != null) {
                    break;
                }
            } catch (StaleElementReferenceException e) {
                el = driver.findElements(by);
            }
            attempts++;
        }
        return el;
    }

    public List<WebElement> retryFindElementsByDataTestId(String dataTestId) {
        By by = By.cssSelector("[data-testid='" + dataTestId + "']");

        return retryFindElements(by);
    }

    public List<WebElement> retryFindElements(By by, WebElement element) {
        List<WebElement> el = null;
        int attempts = 0;
        while (attempts < 3) {
            try {
                el = element.findElements(by);
                if (el != null) {
                    break;
                }
            } catch (StaleElementReferenceException e) {
                el = element.findElements(by);
            }
            attempts++;
        }
        return el;
    }

    public List<WebElement> retryFindElementsByDataTestId(String dataTestId, WebElement element) {
        By by = By.cssSelector("[data-testid='" + dataTestId + "']");

        return retryFindElements(by, element);
    }

    public void waitUntilPageIsLoaded(WebElement webElement) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    public void scrollToElement(WebElement element) {
        js = (JavascriptExecutor) driver;
        String jsScroll = String.format("window.scroll(0, %s)", element.getLocation().getY() - 75);
        js.executeScript(jsScroll);
    }

    /**
     * Method will return vale from local storage when key is inserted
     *
     * @param key
     * @return String with value for specific key
     */
    public String getItemFromLocalStorage(String key) {
        js = (JavascriptExecutor) driver;
        return (String) js.executeScript(String.format(
                "return localStorage.getItem('%s');", key));
    }

    /**
     * Method will delete vale from local storage for provided key
     *
     * @param key
     * @return String with value for specific key
     */
    public void deleteItemFromLocalStorage(String key) {
        LocalStorage localStorage = ((WebStorage) driver).getLocalStorage();
        localStorage.removeItem(key);
    }


    /**
     * Method will perform Click on Element via JavaScript executor
     *
     * @param element
     */
    public void performClick(WebElement element) {
        WebElement ele = element;
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", ele);
    }

    public String getCurrentPageUrl() {
        return driver.getCurrentUrl();
    }


    public void waitForInvisibilityOfElement(WebElement element, int waitIntervalInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(waitIntervalInSeconds))
                .until(ExpectedConditions.invisibilityOf(element));
    }

    public void waitForVisibilityOfElement(WebElement element, int waitIntervalInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(waitIntervalInSeconds))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void insertInAppLocalStorage(String key, String value) {
        LocalStorage local = ((WebStorage) driver).getLocalStorage();
        local.setItem(key, value);
    }

    public String getDecodedStorageItem(String storageKey) {
        String encodedStorageValue = getItemFromLocalStorage(storageKey);
        byte[] decodedStorageValueBytes = Base64.getDecoder().decode(encodedStorageValue);
        return new String(decodedStorageValueBytes);
    }

    public WebElement getParentElementOf(WebElement element) {
        return (WebElement) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].parentNode;", element);
    }

    /**
     * @param locator  - Locator strategy for the elements
     * @param waitTime - How much to wait time is given for each element with given locator
     * @return - List of web elements matching given locator
     */
    public List<WebElement> waitForElementsToBeVisible(By locator, int waitTime) {
        return new WebDriverWait(driver, Duration.ofSeconds(waitTime))
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }
}
