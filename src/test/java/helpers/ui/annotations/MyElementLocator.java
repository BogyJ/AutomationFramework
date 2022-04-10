package helpers.ui.annotations;

import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

public class MyElementLocator implements ElementLocator {

    private final SearchContext searchContext;
    private final By by;

    public MyElementLocator(SearchContext searchContext, Field field) {
        this.searchContext = searchContext;

        FindByDataTestId annotation = field.getAnnotation(FindByDataTestId.class);
        String dataTestId = annotation.value();
        String css = String.format("[data-testid='%s']", dataTestId);

        this.by = By.cssSelector(css);
    }

    @Override
    public WebElement findElement() {
        return searchContext.findElement(by);
    }

    @Override
    public List<WebElement> findElements() {
        return searchContext.findElements(by);
    }
}
