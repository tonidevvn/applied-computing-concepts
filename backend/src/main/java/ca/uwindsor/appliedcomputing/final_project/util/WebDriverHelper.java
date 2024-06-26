package ca.uwindsor.appliedcomputing.final_project.util;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The WebDriverHelper class provides utility methods to interact with the web driver,
 * perform actions on web elements, and manage the web driver's state.
 */
public class WebDriverHelper {

    private static WebDriver driver;

    // Actions instance for performing complex user interactions
    private static Actions actions;

    // JavascriptExecutor instance for executing JavaScript commands
    private static JavascriptExecutor js;

    /**
     * Constructs a WebDriverHelper instance and initializes the web driver and JavaScript executor.
     *
     * @param webDriver the WebDriver instance to be used
     */
    public WebDriverHelper(WebDriver webDriver) {
        driver = webDriver;
        js = (JavascriptExecutor) driver;
    }

    /**
     * Initializes the web driver, JavaScript executor, and actions instance.
     *
     * @param webDriver the WebDriver instance to be initialized
     */
    public static void init(WebDriver webDriver) {
        driver = webDriver;
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
    }

    /**
     * Checks if an element is present on the page.
     *
     * @param locator the locator used to find the element
     * @return true if the element is present, false otherwise
     */
    public static boolean isElementPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }

    /**
     * Gets a web element if it exists on the page.
     *
     * @param locator the locator used to find the element
     * @return the web element if it exists, null otherwise
     */
    public static WebElement getElementIfExist(By locator) {
        if (isElementPresent(locator)) return driver.findElement(locator);
        return null;
    }

    /**
     * Gets a list of web elements if they exist on the page.
     *
     * @param locator the locator used to find the elements
     * @return the list of web elements if they exist, null otherwise
     */
    public static List<WebElement> getElementsIfExists(By locator) {
        if (isElementPresent(locator)) return driver.findElements(locator);
        return null;
    }

    /**
     * Gets a related web element if it exists within the specified element.
     *
     * @param element        the web element containing the related element
     * @param relatedLocator the locator used to find the related element
     * @return the related web element if it exists, null otherwise
     */
    public static WebElement getRelatedElementIfExist(WebElement element, By relatedLocator) {
        try {
            return element.findElement(relatedLocator);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Moves the mouse pointer to the specified web element.
     *
     * @param element the web element to move the mouse to
     */
    public static void moveToElement(WebElement element) {
        actions.moveToElement(element).perform();
        waitInSeconds(5);
    }

    /**
     * Waits until the specified web element is present and visible on the page.
     *
     * @param element the web element to wait for
     */
    public static void waitUntilElementPresent(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until the expected page is loaded by checking the URL and presence of an element.
     *
     * @param expectedUrl    the expected URL of the page
     * @param elementLocator the locator of the element to check for presence
     * @throws Exception if the expected page is not loaded within the timeout period
     */
    public static void waitUntilExpectedPageLoaded(String expectedUrl, By elementLocator) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            wait.until(ExpectedConditions.urlContains(expectedUrl));
            if (elementLocator != null) {
                wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator));
            }
        } catch (Exception e) {
            throw new Exception(String.format("Expected page that should contain '%s' not loaded in 30 seconds", expectedUrl));
        }
    }

    /**
     * Method to wait for a specified number of seconds.
     * Uses implicit wait to pause the test execution.
     *
     * @param seconds Number of seconds to wait.
     */
    public static void waitInSeconds(int seconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    /**
     * Waits for JavaScript and jQuery to load completely on the page.
     *
     * @return true if both JavaScript and jQuery have loaded, false otherwise
     */
    private static boolean waitForJStoLoad() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) js.executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return js.executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }

    /**
     * Navigates the web driver to the specified URL.
     *
     * @param url the URL to navigate to
     */
    public static void goToPage(String url) {
        if (url.isBlank()) throw new RuntimeException("Can't go to the page, provided url is empty or null");
        driver.get(url);
    }

    /**
     * Gets the current URL of the page the web driver is on.
     *
     * @return the current page URL
     */
    public static String getCurrentPageUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Method to click a web element using JavaScript.
     * Waits for the element to be displayed before clicking it.
     *
     * @param element The web element to be clicked.
     */
    public static void clickByJs(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> element.isDisplayed());
        if (js != null) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Crawls the specified web URL and returns the page's HTML contents as a string.
     *
     * @param url the web URL to be crawled
     * @return the HTML contents of the page
     */
    public static String crawlWebUrl(String url) {
        // Fetch the page
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(url);
        waitForJStoLoad();
        return driver.findElement(By.tagName("body")).getText().replaceAll("\n", " ");
    }

    /**
     * Shuts down the scraper by quitting the web driver.
     */
    public static void shutDownScraper() {
        driver.quit();
    }
}