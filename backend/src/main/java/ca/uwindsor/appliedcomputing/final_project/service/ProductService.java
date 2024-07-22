package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.config.ScraperConfig;
import ca.uwindsor.appliedcomputing.final_project.dto.MainPage;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        String[] qParts = q.split("\\s+");
        String priceQuery = Arrays.stream(qParts).filter(kw -> !PriceUtil.parsePriceQuery(kw).isEmpty()).findFirst().orElse("");
        String kwQuery = Arrays.stream(qParts).filter(kw -> !kw.contains("price")).collect(Collectors.joining(" "));
        ArrayList<PriceConditionItem> items = PriceUtil.parsePriceQuery(priceQuery);
        Double minPrice = items.stream().filter(item -> item.op.equals(">=")).map(item -> item.value).findFirst().orElse( 0.0);
        Double maxPrice = items.stream().filter(item -> item.op.equals("<=")).map(item -> item.value).findFirst().orElse(Double.MAX_VALUE);
        Specification<ProductData> spec = Specification.where(ProductSpecification.hasName(kwQuery))
                .and(ProductSpecification.hasPrice(minPrice, maxPrice));
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
            }
        }
        // release web driver
        webDriverRelease();

        return responseProducts;
    }

    private void extractDataFromZehrs(List<ProductData> responseProducts) {
        // Find product elements
        MainPage mainPage = new MainPage(driver);

        int limitCheck = 1;
        List<WebElement> searchProducts = mainPage.searchProducts;
        WebDriverHelper.waitInSeconds(10);
        for (WebElement product : searchProducts) {

            ProductData responseProduct = new ProductData();
            try {
                WebElement we = product.findElement(By.cssSelector("span.product-name__item--name"));
                if (we != null && !StringUtils.isEmpty(we.getText())) {
                    // trick to load full element
                    WebDriverHelper.moveToElement(we);
                    responseProduct.setName(we.getText());
                }

                try {
                    we = product.findElement(By.cssSelector("span.selling-price-list__item__price--now-price__value"));
                } catch (NoSuchElementException e) {
                    try {
                        we = product.findElement(By.cssSelector("span.selling-price-list__item__price--sale__value"));
                    } catch (NoSuchElementException e2) {
                        // do nothing
                    }
                }
                assert we != null;
                responseProduct.setPrice(Double.parseDouble(we.getText()));

                we = WebDriverHelper.getRelatedElementIfExist(product, By.className("responsive-image--product-tile-image"));
                assert we != null;
                WebDriverHelper.waitUntilElementPresent(we);
                if (!StringUtils.isEmpty(we.getAttribute("src"))) {
                    responseProduct.setImage(we.getAttribute("src"));
                }

                we = WebDriverHelper.getRelatedElementIfExist(product, By.className("product-tile__details__info__name__link"));
                assert we != null;
                WebDriverHelper.waitUntilElementPresent(we);
                String productLink = we.getAttribute("href");
                if (!StringUtils.isEmpty(productLink)) {
                    responseProduct.setUrl(productLink);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (responseProduct.getName() != null) {
                // save to H2
                responseProduct = saveProduct(responseProduct);
                responseProducts.add(responseProduct);
                limitCheck++;
                if (limitCheck > 10) {
                    break;
                }
            }
        }

        for (ProductData product : responseProducts) {
            if (product.getUrl() != null) {
                // get product details
                try {
                    WebDriverHelper.loadUrlAndWait(product.getUrl());
                    WebElement disclaimerDiv = mainPage.productDisclaimer;
                    WebDriverHelper.moveToElement(disclaimerDiv);
                    WebDriverHelper.waitUntilElementPresent(disclaimerDiv);
                    WebElement descriptionDiv = mainPage.productDescription;
                    WebDriverHelper.waitUntilElementPresent(descriptionDiv);
                    assert descriptionDiv != null;
                    product.setDescription(descriptionDiv.getText().replaceAll("\n", " "));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // && responseProduct.getBrand() != null && responseProduct.getPrice() != null && responseProduct.getImage() != null && responseProduct.getUrl() != null) {
                // String[] data = { product.getName(), product.getPrice(), product.getImage(), product.getUrl(), product.getDescription() };
                // writer.writeNext(data);
            }
        }
    }
}
