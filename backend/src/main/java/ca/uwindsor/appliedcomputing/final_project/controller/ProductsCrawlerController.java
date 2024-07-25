package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import ca.uwindsor.appliedcomputing.final_project.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/products")
@Slf4j
public class ProductsCrawlerController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<ProductData> getProducts(@RequestParam(required = false, name = "q") String q,
                                         @RequestParam(required = false, name = "category") String category,
                                         @RequestParam(required = false, name = "store") String store,
                                         @RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "price");
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.findProducts(q, category, store, pageable);
    }

    @GetMapping(path = "/scraping")
    public ResponseEntity<List<ProductData>> getProductsByKeyword(@RequestParam("q") String searchKeyword) throws Exception {
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            log.error("Search query is empty or null.");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.getProductsByKeyword(searchKeyword));
    }

}
