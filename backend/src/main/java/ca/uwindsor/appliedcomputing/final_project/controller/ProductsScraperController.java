package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import ca.uwindsor.appliedcomputing.final_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/products")
public class ProductsScraperController {
    @Autowired
    private ProductService productService;

    @GetMapping(path = "")
    public List<ProductData> getProducts(@RequestParam(required = false, defaultValue = "10") int limit, @RequestParam(name = "q", required = false) String query) {
        if (query != null && !query.trim().isEmpty()) {
            return productService.getProducts(limit, query);
        } else {
            return productService.getProducts(limit);
        }
    }

    @GetMapping(path = "/low-to-high")
    public List<ProductData> getProductsFromLowToHighPrice() throws Exception {
        return productService.getSortedProductsByPrice(0);
    }

    @GetMapping(path = "/high-to-low")
    public List<ProductData> getProductsFromHighToLowPrice() throws Exception {
        return productService.getSortedProductsByPrice(1);
    }

    @GetMapping(path = "/scraping")
    public List<ProductData> getProductsByKeyword(@RequestParam("q") String searchKeyword) throws Exception {
        return productService.getProductsByKeyword(searchKeyword);
    }

}