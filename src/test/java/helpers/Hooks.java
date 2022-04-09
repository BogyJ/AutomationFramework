package helpers;

import containers.PomContainer;
import containers.TestSessionData;
import helpers.file.ExcelHelper;
import helpers.ui.BaseDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class Hooks extends BaseDriver {
    protected TestSessionData testSessionData;
    protected PomContainer pages;

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
    }

    @AfterMethod
    public void tearDown() {
        testSessionData.getDriver().manage().deleteAllCookies();
        testSessionData.getDriver().quit();
    }
}
