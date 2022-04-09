package containers;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.HashMap;

public class TestSessionData {
    private WebDriver driver;
    private PomContainer pomContainer;
    private String searchEngineUrl;
    public static ArrayList<HashMap<String, String>> testParameters = new ArrayList<>();

    public TestSessionData() {
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public PomContainer getPomContainer() {
        return pomContainer;
    }

    public void setPomContainer(PomContainer pomContainer) {
        this.pomContainer = pomContainer;
    }

    public String getSearchEngineUrl() {
        return searchEngineUrl;
    }

    public void setSearchEngineUrl(String searchEngineUrl) {
        this.searchEngineUrl = searchEngineUrl;
    }
}
