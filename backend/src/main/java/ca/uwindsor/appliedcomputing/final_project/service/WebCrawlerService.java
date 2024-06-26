package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.config.ScraperConfig;
import ca.uwindsor.appliedcomputing.final_project.dto.WebCrawlerData;
import ca.uwindsor.appliedcomputing.final_project.util.WebDriverHelper;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * The WebCrawlerService class provides functionalities to initialize a web driver,
 * crawl a web URL, and release the web driver resources.
 */
@Service
public class WebCrawlerService {

    private static WebDriver driver;

    /**
     * Initializes the web driver if it has not been initialized yet.
     * This method sets up the web driver using ScraperConfig and initializes it with WebDriverHelper.
     */
    private static void webDriverInit() {
        if (driver == null) {
            driver = ScraperConfig.setupWebDriver(true);
            WebDriverHelper.init(driver);
        }
    }

    /**
     * Releases the web driver resources by shutting down the scraper and setting the driver to null.
     */
    private static void webDriverRelease() {
        WebDriverHelper.shutDownScraper();
        driver = null;
    }

    /**
     * Crawls the specified web URL and returns the data as a WebCrawlerData object.
     * This method initializes the web driver, sets the URL and time, crawls the web URL
     * to get the HTML contents, and then releases the web driver.
     *
     * @param url the web URL to be crawled
     * @return a WebCrawlerData object containing the URL, time, and HTML contents
     */
    public static WebCrawlerData crawlWebUrl(String url) {
        webDriverInit();
        WebCrawlerData data = new WebCrawlerData();
        data.setUrl(url);
        data.setTime(LocalDateTime.now().toString());
        data.setHtmlContents(WebDriverHelper.crawlWebUrl(url));
        webDriverRelease();
        return data;
    }
}
