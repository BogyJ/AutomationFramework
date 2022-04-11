package runners;

import containers.TestSessionData;
import helpers.Hooks;
import org.testng.annotations.Test;
import poms.SharedMethods;
import providers.GoogleDataProvider;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertNotNull;

public class GoogleTestRunner extends Hooks {

    @Test(dataProvider = "Google", dataProviderClass = GoogleDataProvider.class)
    public void searchGoogleByKeyword(String keyword) {
        String googleSearchEngineUrl = TestSessionData
                .testParameters.get(0).get("Google Search Engine URL");
        pages.googleHomePage.openWebPage(googleSearchEngineUrl);
        pages.googleHomePage.searchByKeyword(keyword);
        TestSessionData.searchKeyword = keyword;
        TestSessionData.searchKeywordsBySearchEngine.get("Google").add(keyword);
        List<Map<String, String>> results = pages.googleHomePage.getSearchResults();
        assertNotNull(results, "No results containing \"" + keyword + "\" were found.");
        results.forEach(el -> el.put("keyword", keyword));
        TestSessionData.searchResults.add(results);
        SharedMethods.logResults(results);
    }

}
