package runners;

import containers.TestSessionData;
import helpers.Hooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import providers.DuckDuckGoDataProvider;

public class DuckDuckGoTestRunner extends Hooks {

    private final Logger log = LogManager.getLogger(DuckDuckGoTestRunner.class);

    @Test(dataProvider = "DuckDuckGo", dataProviderClass = DuckDuckGoDataProvider.class)
    public void searchDuckDuckGoByKeyword(String keyword) throws InterruptedException {
        String duckDuckGoSearchEngine = TestSessionData.testParameters.get(0).get("DuckDuckGo Search Engine URL");
        pages.duckDuckGoHomePage.goToDuckDuckGoHomePage(duckDuckGoSearchEngine);
        log.info("Navigate to: " + duckDuckGoSearchEngine);

        pages.duckDuckGoHomePage.searchByKeyword(keyword);
        log.info("Search by keyword: " + keyword);
    }

}
