package poms;

import containers.TestSessionData;
import helpers.ui.BasePageObject;
import helpers.ui.annotations.FindByDataTestId;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DuckDuckGoHomePage extends BasePageObject {
    @FindBy(css = "#search_form_input_homepage")
    WebElement searchInput;

    @FindByDataTestId("result-extras-url-link")
    List<WebElement> websiteUrls;

    @FindByDataTestId("result-title-a")
    List<WebElement> websiteTitles;

    @FindBy(css = ".result__snippet")
    List<WebElement> shortDescriptions;

    private final Logger log = LogManager.getLogger(DuckDuckGoHomePage.class);

    public DuckDuckGoHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);;
    }

    public void searchDuckDuckGoByKeyword(String keyword) {
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

        log.info("Found {} results", websiteTitles.size());

        if (websiteTitles.size() > 10) websiteTitles.subList(10, websiteTitles.size()).clear();
        List<Map<String, String>> resultsInfo = new ArrayList<>();

        for (int i = 0; i < websiteTitles.size(); i++) {
            Map<String, String> resultDetails = new HashedMap<>();

            resultDetails.put("websiteTitle", websiteTitles.get(i).getText());
            resultDetails.put("url", websiteUrls.get(i).getText().split("›")[0]);
            resultDetails.put("shortDescription", shortDescriptions.get(i).getText());

            resultsInfo.add(resultDetails);
        }
        TestSessionData.searchResults.add(resultsInfo);
        SharedMethods.logResults(resultsInfo);
        return resultsInfo;
    }

}
