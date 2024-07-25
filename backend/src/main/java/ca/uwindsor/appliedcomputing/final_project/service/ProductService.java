package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.config.ScraperConfig;
import ca.uwindsor.appliedcomputing.final_project.dto.webpage.WebPageMultiFood;
import ca.uwindsor.appliedcomputing.final_project.dto.webpage.WebPageZehrs;
import ca.uwindsor.appliedcomputing.final_project.dto.PriceConditionItem;
import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import ca.uwindsor.appliedcomputing.final_project.repository.ProductRepository;
import ca.uwindsor.appliedcomputing.final_project.spec.ProductSpecification;
import ca.uwindsor.appliedcomputing.final_project.util.PriceUtil;
import ca.uwindsor.appliedcomputing.final_project.util.WebDriverHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    private static final ScraperConfig scraperConfig = new ScraperConfig();
    private static WebDriver driver;
    private static WebDriverHelper webDriverHelper;
    private static int DEMO_LIMIT = 2;

    //Reading data from property file to a list
    @Value("#{'${website.urls}'.split(',')}")
    List<String> urls;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private void webDriverInit() {
        if (driver == null) {
            driver = scraperConfig.setupWebDriver(true);
            WebDriverHelper.init(driver);
        }
    }

    private void webDriverRelease() {
        WebDriverHelper.shutDownScraper();
        driver = null;
    }

    // Save operation
    ProductData saveProduct(ProductData product) {
        return productRepository.save(product);
    }

    public Page<ProductData> findProducts(String q, String category, String store, Pageable pageable) {
        String[] qParts = q.trim().split("\\s+");
        String priceQuery = Arrays.stream(qParts).filter(kw -> !PriceUtil.parsePriceQuery(kw).isEmpty()).findFirst().orElse("");
        String kwQuery = Arrays.stream(qParts).filter(kw -> !kw.contains("price:")).collect(Collectors.joining(" "));
        ArrayList<PriceConditionItem> items = PriceUtil.parsePriceQuery(priceQuery);
        Specification<ProductData> spec = Specification.where(ProductSpecification.hasName(kwQuery));
        if (!priceQuery.isBlank()) {
            Double minPrice = items.stream().filter(item -> item.op.equals(">=")).map(item -> item.value).findFirst().orElse( 0.0);
            Double maxPrice = items.stream().filter(item -> item.op.equals("<=")).map(item -> item.value).findFirst().orElse(Double.MAX_VALUE);
            if (minPrice <= maxPrice) {
                spec = spec.and(ProductSpecification.hasPriceBetween(minPrice, maxPrice));
            }
        }
        if (!category.isBlank()) {
            spec = spec.and(ProductSpecification.hasCategory(category));
        }
        if (!store.isBlank()) {
            spec = spec.and(ProductSpecification.hasStore(store));
        }
        return productRepository.findAll(spec, pageable);
    }

    public List<ProductData> getProductsByKeyword(String keyword) throws Exception {
        // init web driver
        webDriverInit();

        List<ProductData> responseProducts = new ArrayList<>();
        //Traversing through the urls
        for (String url : urls) {
            String fullUrl = url + keyword;
            driver.get(fullUrl);
            if (url.contains("zehrs")) {
                extractDataFromZehrs(responseProducts);
            } else if (url.contains("multifood")) {
                extractDataFromMultifood(responseProducts);
            }
        }
        // release web driver
        webDriverRelease();

        return responseProducts;
    }

    private void extractDataFromZehrs(List<ProductData> responseProducts) {
        // Find product elements
        WebPageZehrs webPageZehrs = new WebPageZehrs(driver);

        int limitCheck = 1;
        List<WebElement> searchProducts = webPageZehrs.products;
        WebDriverHelper.waitInSeconds(10);
        for (WebElement product : searchProducts) {
            ProductData responseProduct = new ProductData();
            responseProduct.setStore("zehrs");
            responseProduct.setCategory("Misc");
            try {
                WebElement we = WebDriverHelper.getRelatedElementIfExist(product, By.cssSelector("h3.chakra-heading"));
                if (we != null && !StringUtils.isEmpty(we.getText())) {
                    // trick to load full element
                    WebDriverHelper.moveToElement(we);
                    responseProduct.setName(we.getText());
                }

                we = WebDriverHelper.getRelatedElementIfExist(product, By.cssSelector("div[data-testid=\"price-product-tile\"] span > span"));
                assert we != null;
                responseProduct.setPrice(Double.parseDouble(we.getText().replaceAll("[^\\d.]", "")));

                we = WebDriverHelper.getRelatedElementIfExist(product, By.cssSelector("div[data-testid='product-image'] > img"));
                assert we != null;
                WebDriverHelper.waitUntilElementPresent(we);
                if (!StringUtils.isEmpty(we.getAttribute("src"))) {
                    responseProduct.setImage(we.getAttribute("src"));
                }

                we = WebDriverHelper.getRelatedElementIfExist(product, By.cssSelector("a.chakra-linkbox__overlay"));
                assert we != null;
                WebDriverHelper.waitUntilElementPresent(we);
                String productLink = we.getAttribute("href");
                if (!StringUtils.isEmpty(productLink)) {
                    responseProduct.setUrl(productLink);
                }
            } catch (Exception ex) {
                // do nothing
            }

            if (responseProduct.getName() != null) {
                // save to H2
                responseProduct = saveProduct(responseProduct);
                responseProducts.add(responseProduct);
                limitCheck++;
                if (limitCheck > DEMO_LIMIT) {
                    break;
                }
            }
        }
    }

    private void extractDataFromMultifood(List<ProductData> responseProducts) {
        // Find product elements
        WebPageMultiFood webPage = new WebPageMultiFood(driver);

        int limitCheck = 1;
        List<WebElement> products = webPage.products;
        WebDriverHelper.waitInSeconds(10);
        for (WebElement product : products) {
            ProductData responseProduct = new ProductData();
            responseProduct.setStore("multifood");
            responseProduct.setCategory("Misc");
            try {
                WebElement we = WebDriverHelper.getRelatedElementIfExist(product, By.cssSelector("h2.product-name"));
                if (we != null) {
                    // trick to load full element
                    WebDriverHelper.moveToElement(we);
                    responseProduct.setName(we.getText());
                }

                we = WebDriverHelper.getRelatedElementIfExist(product, By.cssSelector("span.price"));
                if (we != null) {
                    String refinedPrice = we.getText().replaceAll("[^\\d.]", "");
                    responseProduct.setPrice(Double.parseDouble(refinedPrice));
                }

                we = WebDriverHelper.getRelatedElementIfExist(product, By.cssSelector(".product-image"));
                if (we != null) {
                    responseProduct.setUrl(we.getAttribute("href"));
                }

                we = WebDriverHelper.getRelatedElementIfExist(product, By.cssSelector(".product-image img"));
                if (we != null) {
                    responseProduct.setImage(we.getAttribute("src"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (responseProduct.getName() != null) {
                // save to H2
                responseProduct = saveProduct(responseProduct);
                responseProducts.add(responseProduct);
                limitCheck++;
                if (limitCheck > DEMO_LIMIT) {
                    break;
                }
            }
        }
    }
}
