package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.config.ScraperConfig;
import ca.uwindsor.appliedcomputing.final_project.dto.MainPage;
import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import ca.uwindsor.appliedcomputing.final_project.repository.ProductRepository;
import ca.uwindsor.appliedcomputing.final_project.util.Sorting;
import ca.uwindsor.appliedcomputing.final_project.util.WebDriverHelper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.sun.tools.javac.Main;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
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

    public List<ProductData> getProducts() {
        return getProducts(10);
    }

    public List<ProductData> getProducts(int limit) {
        // re-update limit number
        if (limit == -1)
            limit = 100;
        else if (limit <= 0)
            limit = 10;

        //List<ProductData> responseProducts = loadProductsFromCSV(limit);
        return fetchProductList(limit);
    }


    // Save operation
    ProductData saveProduct(ProductData product) {
        return productRepository.save(product);
    }

    // Read operation
    public List<ProductData> fetchProductList() {
        return (List<ProductData>)
                productRepository.findAll();
    }

    // Read operation
    public List<ProductData> fetchProductList(int limit) {
        int count = 0;
        List<ProductData> productList = new ArrayList<>();
        for (ProductData productData : fetchProductList()) {
            if (count++ < limit) {
                productList.add(productData);
            }
        }
        return productList;
    }

    public List<ProductData> loadProductsFromCSV(int limit) {
        List<ProductData> responseProducts = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(Paths.get(Main.class.getClassLoader().getResource("data/merged_dataset.csv").toURI()).toFile()))) {
            List<String[]> records = csvReader.readAll();
            records.remove(0); // Remove header row

            int count = 0;
            for (String[] record : records) {
                ProductData responseProduct = new ProductData();
                if (record.length == 5) {
                    responseProduct.setName(record[0]);
                    //responseProduct.setBrand(record[2]);
                    responseProduct.setPrice(record[1]);
                    responseProduct.setImage(record[2]);
                    responseProduct.setUrl(record[3]);
                    responseProduct.setDescription(record[4]);
                    if (responseProduct.getName() != null && count++ < limit)
                        responseProducts.add(responseProduct);
                }
            }
            return responseProducts;
        } catch (IOException | CsvException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductData> getSortedProductsByPrice(int type) {
        List<ProductData> responseProducts = getProducts();
        Sorting.quickSort(responseProducts, type);
        return responseProducts;
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
                responseProduct.setPrice(we.getText());

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