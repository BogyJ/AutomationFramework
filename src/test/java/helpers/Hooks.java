package helpers;

import containers.PomContainer;
import containers.TestSessionData;
import helpers.file.ExcelHelper;
import helpers.ui.BaseDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import poms.SharedMethods;

import java.util.ArrayList;

public class Hooks extends BaseDriver {
    protected TestSessionData testSessionData;
    protected PomContainer pages;

    @BeforeSuite
    public void beforeAllTests() {
        TestSessionData.testParameters = ExcelHelper
                .getDataFromExcel("src/test/resources/testParameters.xlsx", "Parameters");
        TestSessionData.searchKeywordsBySearchEngine.put("Google", new ArrayList<>());
        TestSessionData.searchKeywordsBySearchEngine.put("DuckDuckGo", new ArrayList<>());
    }

    @BeforeMethod
    public void setUp() {
        testSessionData = new TestSessionData();
        driver = initializeDriver(TestSessionData.testParameters.get(0).get("Browser"));
        testSessionData.setDriver(driver);
        testSessionData.setPomContainer(new PomContainer(driver));
        pages = testSessionData.getPomContainer();
        driver.manage().deleteAllCookies();
    }

    @AfterMethod
    public void tearDown() {
        testSessionData.getDriver().quit();
    }

    @AfterSuite
    public void logAllSearchResults() {
        SharedMethods.logCommonResults();
    }
}
