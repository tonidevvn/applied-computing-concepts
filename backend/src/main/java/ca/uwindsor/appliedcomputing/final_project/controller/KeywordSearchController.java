package ca.uwindsor.appliedcomputing.final_project.controller;
import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import ca.uwindsor.appliedcomputing.final_project.service.KeywordService;
import ca.uwindsor.appliedcomputing.final_project.service.WebCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/keyword-search")
@Slf4j
public class KeywordSearchController {
    @Autowired
    private KeywordService keywordService;

    @Autowired
    private WebCrawlerService webCrawlerService;

    @GetMapping(path = "/count")
    public ResponseEntity<KeywordSearchData> getKeywordSearchCount(@RequestParam("q") String searchKeyword) throws Exception {
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            log.error("Search query is empty or null.");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(keywordService.setKeywordSearch(searchKeyword));
    }

    @GetMapping(path = "/list")
    public List<KeywordSearchData> getKeywordSearchedList(@RequestParam("q") String type, @RequestParam(required = false, defaultValue = "10") int limit) throws Exception {
        if (type != null && type.equalsIgnoreCase("top")) {
            return keywordService.getTopKeywordsSearched(limit);
        } else if (type != null && type.equalsIgnoreCase("recent")) {
            return keywordService.getRecentKeywordsSearched();
        }
        return List.of();
    }
}
