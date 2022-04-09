package poms;

import helpers.ui.BasePageObject;
import helpers.ui.annotations.FindByDataHeaderFeature;
import org.apache.commons.collections4.map.HashedMap;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoogleHomePage extends BasePageObject {
    @FindBy(css = "input[name=\"q\"]")
    WebElement searchInput;

    // first link can be representation of fragment link so sometimes depending on keyword it won't match in allRootLinks
    @FindBy(css = ".g > div > div > div > a")
    WebElement firstRootLink;

    @FindByDataHeaderFeature("0")
    List<WebElement> allRootLinks;

    @FindBy(css = "div[data-content-feature=\"1\"] > div")
    List<WebElement> shortDescriptions;

    public GoogleHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        waitForLoad(1L);
    }

    public void goToGoogleHomePage(String url) {
        driver.get(url);
        driver.manage().window().maximize();
    }

    public void searchByKeyword(String keyword) {
        waitToBeClickable(searchInput);
        searchInput.sendKeys(keyword);
        searchInput.sendKeys(Keys.ENTER);
    }

    public List<List<Map<String, String>>> getSearchResults() {
        List<WebElement> groupedLinks = new ArrayList<>();
        groupedLinks.add(firstRootLink);

        boolean isDisplayedFirstLink = false;
        try {
            isDisplayedFirstLink = waitToBeClickable(firstRootLink).isDisplayed();
            if (isDisplayedFirstLink) groupedLinks.addAll(allRootLinks);
        } catch (NoSuchElementException e) {
            allRootLinks.remove(0);
            groupedLinks.addAll(allRootLinks);
        }

        if (groupedLinks.size() > 10) groupedLinks.subList(10, groupedLinks.size()).clear();

        List<List<Map<String, String>>> resultsInfo = new ArrayList<>();

        for (int i = 0; i < groupedLinks.size(); i++) {
            List<Map<String, String>> results = new ArrayList<>();
            Map<String, String> resultDetails = new HashedMap<>();

            resultDetails.put("websiteTitle", groupedLinks.get(i).findElement(By.cssSelector("h3")).getText());
            resultDetails.put("url", groupedLinks.get(i).findElement(By.cssSelector("cite")).getText().split("›")[0]);

            if (!isDisplayedFirstLink) {
                resultDetails.put("shortDescription", shortDescriptions.get(i).getText());
            } else {
                if (i < shortDescriptions.size() && i > 0) {
                    resultDetails.put("shortDescription", shortDescriptions.get(i).getText());
                }
            }

            System.out.println("SHORT DESC SIZE =========================== " + shortDescriptions.size());
            results.add(resultDetails);
            resultsInfo.add(results);
        }

        return resultsInfo;
    }
}
