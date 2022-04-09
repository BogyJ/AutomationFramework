package containers;

import poms.DuckDuckGoHomePage;
import poms.GoogleHomePage;
import org.openqa.selenium.WebDriver;

import static helpers.ui.annotations.AnnotationProcessor.annotatePOM;

public class PomContainer {
    public GoogleHomePage googleHomePage;
    public DuckDuckGoHomePage duckDuckGoHomePage;

    public PomContainer(WebDriver driver) {
        this.googleHomePage = annotatePOM(new GoogleHomePage(driver));
        this.duckDuckGoHomePage = annotatePOM(new DuckDuckGoHomePage(driver));
    }
}
