package poms;

import containers.TestSessionData;
import helpers.ui.BasePageObject;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoogleHomePage extends BasePageObject {
    @FindBy(css = "input[name=\"q\"]")
    WebElement searchInput;

    // first link can be representation of fragment link so sometimes depending on
    // keyword it won't match in websiteTitles
    @FindBy(css = ".xpdopen .g > div > div > div > a")
    WebElement firstWebsiteTitle;

    @FindBy(css = "div[data-header-feature=\"0\"] > div > a")
    List<WebElement> websiteTitles;

    @FindBy(css = "div[data-content-feature=\"1\"] > div")
    List<WebElement> shortDescriptions;

    private final Logger log = LogManager.getLogger(GoogleHomePage.class);

    public GoogleHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void searchByKeyword(String keyword) {
        log.info("Search by keyword: {}", keyword);
        waitToBeClickable(searchInput, 5L);
        searchInput.sendKeys(keyword);
        searchInput.sendKeys(Keys.ENTER);
    }

    public List<Map<String, String>> getSearchResults() {
        if (websiteTitles.size() == 0) {
            log.info("No search results found");
            return null;
        }

        List<WebElement> groupedLinks = new ArrayList<>();

        boolean isDisplayedFirstLinkFragment = false;
        if (waitToBeClickable(firstWebsiteTitle, 5L)) {
            isDisplayedFirstLinkFragment = true;
            groupedLinks.add(firstWebsiteTitle);
        }
        groupedLinks.addAll(websiteTitles);

        log.info("Found {} results", groupedLinks.size());

        if (groupedLinks.size() > 10) groupedLinks.subList(10, groupedLinks.size()).clear();

        List<Map<String, String>> resultsInfo = new ArrayList<>();

        for (int i = 0; i < groupedLinks.size(); i++) {
            Map<String, String> resultDetails = new HashedMap<>();

            resultDetails.put("websiteTitle", groupedLinks.get(i).findElement(By.cssSelector("h3")).getText());
            resultDetails.put("url", groupedLinks.get(i).findElement(By.cssSelector("cite")).getText().split("›")[0]);

            if (isDisplayedFirstLinkFragment) {
                if (i > 0) {
                    resultDetails.put("shortDescription", shortDescriptions.get(i - 1).getText());
                }
            } else {
                resultDetails.put("shortDescription", shortDescriptions.get(i).getText());
            }

            resultsInfo.add(resultDetails);
        }
        TestSessionData.searchResults.add(resultsInfo);
        SharedMethods.logResults(resultsInfo);
        return resultsInfo;
    }
}
