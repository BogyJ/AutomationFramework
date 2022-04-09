package runners;

import containers.TestSessionData;
import helpers.Hooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import providers.GoogleDataProvider;

import java.util.List;
import java.util.Map;

public class GoogleTestRunner extends Hooks {

    private final Logger log = LogManager.getLogger(GoogleTestRunner.class);

    @Test(dataProvider = "Google", dataProviderClass = GoogleDataProvider.class)
    public void searchGoogleByKeyword(String keyword) throws InterruptedException {
        String googleSearchEngineUrl = TestSessionData.testParameters.get(0).get("Google Search Engine URL");
        pages.googleHomePage.goToGoogleHomePage(googleSearchEngineUrl);
        log.info("Navigate to: {}", googleSearchEngineUrl);

        pages.googleHomePage.searchByKeyword(keyword);
        log.info("Search by keyword: {}", keyword);

        log.info("Fetching results");
        List<List<Map<String, String>>> results = pages.googleHomePage.getSearchResults();
        results.forEach(el -> {
            el.forEach(element -> {
                log.info("Website Title: {}", element.get("websiteTitle"));
                log.info("Website Url: {}", element.get("url"));
                log.info("Short Description: {}", element.getOrDefault("shortDescription", "No description available."));
            });
        });
    }

}
