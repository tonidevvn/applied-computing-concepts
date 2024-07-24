package ca.uwindsor.appliedcomputing.final_project.dto.webpage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * MainPage class representing the main page of the Zehrs website.
 * This class contains web elements found on the page and their respective locators.
 * The elements are initialized using the PageFactory in Selenium.
 * Page URL: https://www.zehrs.ca/
 */
public class WebPageZehrs {

    /**
     * List of web elements representing products
     */
    @FindBys(@FindBy(css = "div.chakra-linkbox"))
    public List<WebElement> products;

    /**
     * List of web elements representing search products
     */
    @FindBys(@FindBy(css = "div.product-tile"))
    public List<WebElement> searchProducts;

    /**
     * Web element product description
     */
    @FindBy(css = "div.product-description-text__text")
    public WebElement productDescription;

    /**
     * Web element product Disclaimer
     */
    @FindBy(css = "div.product-details-page-legal-disclaimer__title__label")
    public WebElement productDisclaimer;

    /**
     * Web element for the footer 'Weekly Flyer' link identified by its data-track-link-name attribute
     */
    @FindBy(css = "a[data-track-link-name=\"popular-categories:weekly-flyer\"]")
    public WebElement footerWeeklyFlyer;

    /**
     * Web element for the footer 'Contact Us' link identified by its data-track-link-name attribute
     */
    @FindBy(css = "a[data-track-link-name=\"about-us:contact-us\"]")
    public WebElement footerContactUs;

    /**
     * Initializes the web elements using the given WebDriver instance.
     *
     * @param driver The WebDriver instance to be used for initializing the web elements.
     */
    public WebPageZehrs(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}