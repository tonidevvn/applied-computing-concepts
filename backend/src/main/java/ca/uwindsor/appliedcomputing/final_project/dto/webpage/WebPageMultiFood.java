package ca.uwindsor.appliedcomputing.final_project.dto.webpage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * MainPage class representing the main page of the multifoodwindsor website.
 * This class contains web elements found on the page and their respective locators.
 * The elements are initialized using the PageFactory in Selenium.
 * Page URL: http://www.multifoodwindsor.com
 */
public class WebPageMultiFood {
    /**
     * Web element for the product image
     */
    @FindBy(css = ".product-image img")
    public WebElement productImage;

    /**
     * Web element for the product price
     */
    @FindBy(css = "span.price")
    public WebElement productPrice;

    /**
     * Web element for the product name
     */
    @FindBy(css = "h2.product-name")
    public WebElement productName;

    /**
     * List of web elements representing products
     */
    @FindBys(@FindBy(css = "ul.products-grid .item"))
    public List<WebElement> products;

    /**
     * Initializes the web elements using the given WebDriver instance.
     *
     * @param driver The WebDriver instance to be used for initializing the web elements.
     */
    public WebPageMultiFood(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}