package poms;

import helpers.ui.BasePageObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DuckDuckGoHomePage extends BasePageObject {
    @FindBy(css = "#search_form_input_homepage")
    WebElement searchInput;

    public DuckDuckGoHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);;
        waitForLoad(1L);
    }

    public void goToDuckDuckGoHomePage(String url) {
        driver.get(url);
        driver.manage().window().maximize();
    }

    public void searchByKeyword(String keyword) {
        waitToBeClickable(searchInput);
        searchInput.sendKeys(keyword);
        searchInput.sendKeys(Keys.ENTER);
    }

}
