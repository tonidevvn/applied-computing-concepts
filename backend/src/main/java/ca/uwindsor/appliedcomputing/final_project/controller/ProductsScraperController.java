package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import ca.uwindsor.appliedcomputing.final_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/products")
public class ProductsScraperController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<ProductData> getProductsByPage(@RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "10") int size,
                                               @RequestParam(name = "q", required = false) String query) {
        return productService.fetchProductListByPage(query, page, size);
    }

    @GetMapping(path = "/scraping")
    public List<ProductData> getProductsByKeyword(@RequestParam("q") String searchKeyword) throws Exception {
        return productService.getProductsByKeyword(searchKeyword);
    }

}