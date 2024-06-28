package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.config.ScraperConfig;
import ca.uwindsor.appliedcomputing.final_project.dto.MainPage;
import ca.uwindsor.appliedcomputing.final_project.dto.Page;
import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import ca.uwindsor.appliedcomputing.final_project.util.Resource;
import ca.uwindsor.appliedcomputing.final_project.util.WebDriverHelper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final ScraperConfig scraperConfig = new ScraperConfig();
    private static WebDriver driver;
    private static WebDriverHelper webDriverHelper;
    private Set<ProductData> products = new HashSet<>();

    //Reading data from property file to a list
    @Value("#{'${website.urls}'.split(',')}")
    List<String> urls;

    public ProductServiceImpl() {
        products = _getProducts();
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

    public Set<ProductData> getProductsFromMultifood(List<String[]> records) {
        Set<ProductData> responseProducts = new HashSet<>();
        for (String[] record : records) {
            ProductData responseProduct = new ProductData();
            responseProduct.setName(record[0]);
            responseProduct.setPrice(record[1]);
            responseProduct.setImage(record[2]);
            responseProduct.setUrl(record[3]);
            responseProducts.add(responseProduct);
        }
        return responseProducts;
    }

    public Set<ProductData> getProductsFromZeher(List<String[]> records) {
        Set<ProductData> responseProducts = new HashSet<>();
        for (String[] record : records) {
            ProductData responseProduct = new ProductData();
            responseProduct.setName(record[1]);
            responseProduct.setBrand(record[2]);
            responseProduct.setPrice(record[3]);
            responseProduct.setImage(record[4]);
            responseProduct.setUrl(record[5]);
            responseProduct.setDescription(record[6]);
            responseProducts.add(responseProduct);
        }
        return responseProducts;
    }

    public Set<ProductData> getProductsFromLoblaws(List<String[]> records) {
        Set<ProductData> responseProducts = new HashSet<>();
        for (String[] record : records) {
            ProductData responseProduct = new ProductData();
            responseProduct.setName(record[0]);
            responseProduct.setPrice(record[1]);
            responseProduct.setImage(record[2]);
            responseProducts.add(responseProduct);
        }
        return responseProducts;
    }

    public Set<ProductData> getProductsFromNoFrills(List<String[]> records) {
        Set<ProductData> responseProducts = new HashSet<>();
        for (String[] record : records) {
            ProductData responseProduct = new ProductData();
            responseProduct.setName(record[0]);
            responseProduct.setPrice(record[1]);
            responseProduct.setImage(record[2]);
            responseProduct.setUrl(record[3]);
            responseProduct.setDescription(record[4]);
            responseProducts.add(responseProduct);
        }
        return responseProducts;
    }

    public Set<ProductData> getProductsFromFoodBasic(List<String[]> records) {
        Set<ProductData> responseProducts = new HashSet<>();
        for (String[] record : records) {
            ProductData responseProduct = new ProductData();
            responseProduct.setName(record[0]);
            responseProduct.setPrice(record[1]);
            responseProduct.setImage(record[2]);
            responseProduct.setUrl(record[3]);
            responseProducts.add(responseProduct);
        }
        return responseProducts;
    }

    @Override
    public Page<Set<ProductData>> getProducts(String keyword, int page, int limit) {
        Set<ProductData> data;
        if (keyword == null) {
            data = new HashSet<>(products);
        } else {
            data = products.stream()
                    .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toSet());
        }
        return new Page<>(data.size(), data.stream().skip((long) page * limit)
                .limit(limit).collect(Collectors.toSet()));
    }

    public Set<ProductData> _getProducts() {
        // init web driver
        webDriverInit();

        Set<ProductData> responseProducts = new HashSet<>();

        // Walk through resource csv files
        Resource.walkResources().forEach(path -> {
            final String absolutePath = path.toString();
            try (CSVReader csvReader = new CSVReader(new FileReader(path.toFile()))) {
                List<String[]> records = csvReader.readAll();
                records.removeFirst(); // Remove header row
                if (absolutePath.contains("multifood")) {
                    responseProducts.addAll(getProductsFromMultifood(records));
                } else if (absolutePath.contains("zehrs")) {
                    responseProducts.addAll(getProductsFromZeher(records));
                } else if (absolutePath.contains("loblaws")) {
                    responseProducts.addAll(getProductsFromLoblaws(records));
                } else if (absolutePath.contains("nofrills")) {
                    responseProducts.addAll(getProductsFromNoFrills(records));
                } else if (absolutePath.contains("foodbasic")) {
                    responseProducts.addAll(getProductsFromFoodBasic(records));
                }
            } catch (IOException | CsvException e) {
                e.printStackTrace();
            }
        });

        return responseProducts;
    }

    @Override
    public Set<ProductData> getProductsByKeyword(String keyword) throws Exception {
        // init web driver
        webDriverInit();

        Set<ProductData> responseProducts = new HashSet<>();
        //Traversing through the urls
        for (String url: urls) {
            String fullUrl = url + keyword;
            driver.get(fullUrl);
            if (url.contains("zehrs")) {
                WebDriverHelper.waitUntilExpectedPageLoaded(fullUrl, By.className("responsive-image--product-tile-image"));
                extractDataFromZehrs(responseProducts);
            }
        }
        // release web driver
        webDriverRelease();

        return responseProducts;
    }

    private void extractDataFromZehrs(Set<ProductData> responseProducts) {
        try {
            // Find product elements
            MainPage mainPage = new MainPage(driver);

            String csvFile = "data/products_zehrs.csv";
            CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
            String[] header = {"No", "Product Name", "Brand", "Price", "Image URL", "Product URL", "Product Details"};
            writer.writeNext(header);

            int count = 1;
            int limitCheck = 1;
            List<WebElement> searchProducts = mainPage.searchProducts;
            WebDriverHelper.waitInSeconds(10);
            for (WebElement product: searchProducts) {

                ProductData responseProduct = new ProductData();
                try {
                    WebElement we = product.findElement(By.cssSelector("span.product-name__item--name"));
                    if (we != null && !StringUtils.isEmpty(we.getText()) ) {
                        // trick to load full element
                        WebDriverHelper.moveToElement(we);
                        responseProduct.setName(we.getText());
                    }

                    we = product.findElement(By.cssSelector("span.product-name__item--brand"));
                    if (we != null && !StringUtils.isEmpty(we.getText()) ) {
                        responseProduct.setBrand(we.getText());
                    }

                    try {
                        we = product.findElement(By.cssSelector("span.selling-price-list__item__price--now-price__value"));
                    } catch (NoSuchElementException e) {
                        try {
                            we = product.findElement(By.cssSelector("span.selling-price-list__item__price--sale__value"));
                        }  catch (NoSuchElementException e2) {
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
                    responseProducts.add(responseProduct);
                    limitCheck++;
                    if (limitCheck > 30) {
                        break;
                    }
                }
            }

            for (ProductData product: responseProducts) {
                if (product.getUrl() != null) {
                    // get product details
                    driver.get(product.getUrl());
                    WebDriverHelper.waitUntilExpectedPageLoaded(product.getUrl(), By.className("product-description-text__text"));
                    WebElement descriptionDiv = driver.findElement(By.cssSelector("div.product-description-text__text"));
                    assert descriptionDiv != null;
                    product.setDescription(descriptionDiv.getText());
                    WebDriverHelper.waitInSeconds(10);

                    // && responseProduct.getBrand() != null && responseProduct.getPrice() != null && responseProduct.getImage() != null && responseProduct.getUrl() != null) {
                    String[] data = { String.valueOf(count++), product.getName(), product.getBrand(), product.getPrice(), product.getImage(), product.getUrl(), product.getDescription() };
                    writer.writeNext(data);
                }
            }

            // Close CSV writer and browser
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}