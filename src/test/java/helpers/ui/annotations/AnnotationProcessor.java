package helpers.ui.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

/**
 * Processor class to make using Page Objects simpler and easier.
 */
public class AnnotationProcessor {

    /**
     * Method for processing of Page Object for implementing functionality of @FindByDataTestId.
     * Require Page Object class which use annotation and returning same Page Object class with
     * functional WebElement variables.
     *
     * @param pomEntity annotated Page Object
     * @return pomEntity functional Page Object
     */
    public static <T> T annotatePOM(T pomEntity) {
        Class<?> pomClass = pomEntity.getClass();

        try {
            Field driverField = pomClass.getField("driver");
            RemoteWebDriver driver = (RemoteWebDriver) driverField.get(pomEntity);
            for (Field field : pomClass.getDeclaredFields()) {
                FindByDataTestId annotation = field.getAnnotation(FindByDataTestId.class);
                if (annotation != null) {
                    field.setAccessible(true);

                    ClassLoader loader = pomClass.getClassLoader();
                    MyElementLocator locator = new MyElementLocator(driver, field);

                    if (List.class.isAssignableFrom(field.getType())) {
                        InvocationHandler handler = new LocatingElementListHandler(locator);
                        List<WebElement> proxy;
                        proxy = (List<WebElement>) Proxy
                                .newProxyInstance(loader, new Class[]{List.class}, handler);
                        field.set(pomEntity, proxy);
                    } else {
                        InvocationHandler handler = new LocatingElementHandler(locator);
                        WebElement proxy;
                        proxy = (WebElement) Proxy.newProxyInstance(
                                loader, new Class[]{WebElement.class, WrapsElement.class, Locatable.class},
                                handler);
                        field.set(pomEntity, proxy);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return pomEntity;
    }
}
