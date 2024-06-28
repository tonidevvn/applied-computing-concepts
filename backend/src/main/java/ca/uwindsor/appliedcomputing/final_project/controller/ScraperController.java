package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import ca.uwindsor.appliedcomputing.final_project.dto.Page;
import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import ca.uwindsor.appliedcomputing.final_project.service.KeywordService;
import ca.uwindsor.appliedcomputing.final_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api")
public class ScraperController {
    @Autowired
    private ProductService productService;

    @Autowired
    private KeywordService keywordService;

    @GetMapping(path = "/word-complete")
    public Set<String> getWordCompleteList(@RequestParam("q") String text) throws Exception {
        // TODO
        return Set.of();
    }

    @GetMapping(path = "/word-count")
    public int getWordCount(@RequestParam("q") String url) throws Exception {
        // TODO
        return 0;
    }

    @GetMapping(path = "/keyword-search")
    public Set<KeywordSearchData> getKeywordSearchFrequency(@RequestParam("q") String searchKeyword) throws Exception {
        return keywordService.setKeywordSearched(searchKeyword);
    }

    @GetMapping(path = "/keyword-search-list")
    public Set<KeywordSearchData> getKeywordSearchedList(@RequestParam("q") String type) throws Exception {
        if (type != null && type.equalsIgnoreCase("top")) {
            return keywordService.getTopKeywordsSearched(10);
        } else if (type != null && type.equalsIgnoreCase("recent")) {}
        return keywordService.getRecentKeywordsSearched();
    }

    @GetMapping(path = "/products/scraping")
    public Set<ProductData> getProductsByKeyword(@RequestParam("q") String searchKeyword) throws Exception {
        return productService.getProductsByKeyword(searchKeyword);
    }

    @GetMapping(path = "/products")
    public Page<Set<ProductData>> getProducts(@RequestParam(value = "keyword", required = false) String keyword,
                                              @RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return productService.getProducts(keyword, page, limit);
    }

}