package providers;

import helpers.file.ExcelHelper;
import org.testng.annotations.DataProvider;

public class DuckDuckGoDataProvider {

    @DataProvider(name = "DuckDuckGo")
    public static Object[][] getDuckDuckGoKeywordsData() {
        return ExcelHelper.getTestData("DuckDuckGo");
    }

}
