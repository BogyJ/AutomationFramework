# Test Automation Framework
 - Based on POM design pattern
 - Data Driven Testing approach
 - Used for functional UI testing

## Prerequisites
 - Gradle build tool 7.4.2
 - Java 17 (set `JAVA_HOME` environment variable to point to jdk root folder e.g. `C:\Program Files\Java\jdk-17.0.2`)
 - ![Environment Variables](https://github.com/BogyJ/AutomationFramework/blob/master/environment_variables.png "Environment Variables")

## Dependencies
- Dependencies are managed by Gradle and they are declared in `build.gradle` in root project folder
- seleniumVersion = '4.1.3'
- webDriverManager = '5.1.0'
- testNG = '7.5'
- apachePoi = '5.2.2'
- apachePoiOoxml = '5.2.2'
- log4j = '2.17.2'

## Project Structure
- `src/test/java/containers` used for storing test session data and instantiated page object models
- `src/test/java/helpers` generic functions, annotation processors and UI helper functions
- `src/test/java/poms` page object models representing actual web pages
- `src/test/java/providers` data providers for Data Driven Testing approach
- `src/test/java/runners` TestNG runners

## How to Run Tests 
- Before running tests make sure you have entered correct test parameters in `src/test/resources/testParameters.xlsx` Excel file
- After providing test parameters then you can enter keywords for each search engine in `src/test/resources/testData.xlsx` Excel file (note that Excel sheet name represents search engine)
- If you have locally set up gradle binary then you can `cd` into project root and run the tests by command `gradle clean test`
- If you do not have installed gradle binary locally then you can `cd` into project root folder and use `gradlew` (gradle wrapper) instead `./gradlew clean test`

## Logs
- Logs can be found in `/logs/SearchEngine.log` file
- It will be generated after test execution
- **Note** that if you execute tests in a row few time it will append results to a log file

## Workflow
- Framework was developed in a way that allows new contributors to a project to easily add tests without any overlapping or potential conflicts
- Example workflow (adding a new search engine) would be the following:
1. Add search engine test data in `src/test/resources/testData.xlsx` file
2. Add data provider class in `src/test/java/providers` e.g. `NewSearchEngineDataProvider.java`
```java
public class NewSearchEngineDataProvider {
    @DataProvider(name = "NewSearchEngine")
    public static Object[][] getDuckDuckGoKeywordsData() {
        // NewSearchEngine is sheet name in testData.xlsx file
        return ExcelHelper.getTestData("NewSearchEngine");
    }
}
```
3. Add new Page Object Model class in `src/test/java/poms` package e.g. `NewSearchEngineHomePage.java` (which extends `BasePageObject.java` class) and all corresponding methods to that particular web page
```java
public class NewSearchEngineHomePage extends BasePageObject {
    @FindBy(css = "input[name=\"q\"]")
    WebElement searchInput;
    
    public NewSearchEngineHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void searchByKeyword(String keyword) {
    }
}
```
4. Add newly created pom to a `src/test/java/containers/PomContainer.java` class
```java
public class PomContainer {
    public GoogleHomePage googleHomePage;
    public DuckDuckGoHomePage duckDuckGoHomePage;
    public NewSearchEngineHomePage newSearchEngineHomePage;

    public PomContainer(WebDriver driver) {
        this.googleHomePage = annotatePOM(new GoogleHomePage(driver));
        this.duckDuckGoHomePage = annotatePOM(new DuckDuckGoHomePage(driver));
        this.newSearchEngineHomePage = annotatePOM(new NewSearchEngineHomePage(driver));
    }
}
```
5. Add new test runner class in `src/test/java/runners` package e.g. `NewSearchEngineTestRunner.java` and it will extend `Hooks.java` class
```java
public class NewSearchEngineTestRunner extends Hooks {

    @Test(dataProvider = "NewSearchEngine", dataProviderClass = NewSearchEngineDataProvider.class)
    public void searchNewSearchEngine(String keyword) {
        pages.newSearchEngineHomePage.searchByKeyword(keyword);
    }

}
```
6. Final step add `<class name="runners.NewSearchEngineTestRunner" />` in `src/test/testng.xml` file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="All Test Suite">
    <test verbose="2" preserve-order="true" name="/home/mint/JavaProjects/AutomationFramework/src/test">
        <classes>
            <class name="runners.GoogleTestRunner" />
            <class name="runners.DuckDuckGoTestRunner" />
            <class name="runners.NewSearchEngineTestRunner" />
        </classes>
    </test>
</suite>
```
