package providers;

import helpers.file.ExcelHelper;
import org.testng.annotations.DataProvider;

public class GoogleDataProvider {

    @DataProvider(name = "Google")
    public static Object[][] getGoogleKeywordsData() {
        return ExcelHelper.getTestData("Google");
    }

}
