package runners;

import containers.TestSessionData;
import helpers.Hooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import providers.GoogleDataProvider;

import java.util.*;
import java.util.stream.Collectors;

import static org.testng.Assert.assertNotNull;

public class GoogleTestRunner extends Hooks {

    private final Logger log = LogManager.getLogger(GoogleTestRunner.class);

    @Test(dataProvider = "Google", dataProviderClass = GoogleDataProvider.class)
    public void searchGoogleByKeyword(String keyword) {
        String googleSearchEngineUrl = TestSessionData.testParameters.get(0).get("Google Search Engine URL");
        pages.googleHomePage.goToGoogleHomePage(googleSearchEngineUrl);
        log.info("Navigate to: {}", googleSearchEngineUrl);

        pages.googleHomePage.searchByKeyword(keyword);
        log.info("----------------------- Search by keyword: {} -----------------------", keyword);

        List<List<Map<String, String>>> results = pages.googleHomePage.getSearchResults();
        assertNotNull(results, "No results containing \"" + keyword + "\" were found.");
        log.info("Found {} results", results.size());

        List<String> keywords = Arrays.stream(keyword.split(" "))
                .map(el -> el.toLowerCase(Locale.ROOT)).toList();
        List<Map<String, String>> resultsContainingKeyword = new ArrayList<>();
        List<Map<String, String>> resultsNotContainingKeyword = new ArrayList<>();
        for (int j = 0; j < results.size(); j++) {
            for (int i = 0; i < results.get(j).size(); i++) {
                if (keywords.stream().anyMatch(results.get(j).get(i).get("websiteTitle").toLowerCase(Locale.ROOT)::contains) ||
                        keywords.stream().anyMatch(results.get(j).get(i).get("url").toLowerCase(Locale.ROOT)::contains) ||
                        keywords.stream().anyMatch(results.get(j).get(i).getOrDefault("shortDescription", ".").toLowerCase(Locale.ROOT)::contains)) {
                    resultsContainingKeyword.add(results.get(j).get(i));
                } else {
                    resultsNotContainingKeyword.add(results.get(j).get(i));
                }
            }
        }

        log.info("----------------------- Results containing: {} -----------------------", keyword);
        resultsContainingKeyword.forEach(el -> {
            log.info("Website Title: {}", el.get("websiteTitle"));
            log.info("Website Url: {}", el.get("url"));
            log.info("Short Description: {}", el.getOrDefault("shortDescription", "No description available."));
        });

        log.info("----------------------- Results not containing: {} -----------------------", keyword);
        resultsNotContainingKeyword.forEach(el -> {
            log.info("Website Title: {}", el.get("websiteTitle"));
            log.info("Website Url: {}", el.get("url"));
            log.info("Short Description: {}", el.getOrDefault("shortDescription", "No description available."));
        });
    }

}
