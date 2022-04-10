package runners;

import containers.TestSessionData;
import helpers.Hooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import providers.DuckDuckGoDataProvider;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertNotNull;

public class DuckDuckGoTestRunner extends Hooks {

    private final Logger log = LogManager.getLogger(DuckDuckGoTestRunner.class);

    @Test(dataProvider = "DuckDuckGo", dataProviderClass = DuckDuckGoDataProvider.class)
    public void searchDuckDuckGoByKeyword(String keyword) {
        String duckDuckGoSearchEngine = TestSessionData
                .testParameters.get(0).get("DuckDuckGo Search Engine URL");
        pages.duckDuckGoHomePage.openWebPage(duckDuckGoSearchEngine);
        pages.duckDuckGoHomePage.searchDuckDuckGoByKeyword(keyword);
        TestSessionData.searchKeyword = keyword;
        List<Map<String, String>> results = pages.duckDuckGoHomePage.getSearchResults();
        assertNotNull(results, "No results containing \"" + keyword + "\" were found.");
    }

}
