package helpers;

import containers.PomContainer;
import containers.TestSessionData;
import helpers.file.ExcelHelper;
import helpers.ui.BaseDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import poms.SharedMethods;

public class Hooks extends BaseDriver {
    protected TestSessionData testSessionData;
    protected PomContainer pages;
    private final Logger log = LogManager.getLogger(Hooks.class);

    @BeforeSuite
    public void beforeAllTests() {
        TestSessionData.testParameters = ExcelHelper
                .getDataFromExcel("src/test/resources/testParameters.xlsx", "Parameters");
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
