package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import ca.uwindsor.appliedcomputing.final_project.dto.WebCrawlerData;
import ca.uwindsor.appliedcomputing.final_project.service.KeywordService;
import ca.uwindsor.appliedcomputing.final_project.service.SearchFrequencyService;
import ca.uwindsor.appliedcomputing.final_project.service.WebCrawlerService;
import ca.uwindsor.appliedcomputing.final_project.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/frequency-count")
public class FrequencyCountController {
    @Autowired
    private KeywordService keywordService;

    @Autowired
    private WebCrawlerService webCrawlerService;

    @GetMapping
    public ResponseEntity<KeywordSearchData> getSearchFrequency(@RequestParam("q") String keyword, @RequestParam("url") String url) {
        if (!ValidatorUtil.isValidHtmlUrl(url)) {
            return ResponseEntity.badRequest().build();
        }
        KeywordSearchData searchFData = keywordService.setKeywordSearch(keyword);
        WebCrawlerData wcData = webCrawlerService.crawlWebUrl(url);
        String htmlContents = wcData.getHtmlContents();
        // Split the keywords string into an array of search keywords
        // Method 1: using hashmap to track occurrences of keywords
        // String[] words = wcData.getHtmlContents().split("\\s+");
        // Map<String, Integer> wordsMap = SearchFrequencyService.extractKeywords(words);
        // int occurrences = wordsMap.getOrDefault(keyword, 0);

        // Method 2: using BoyerMoore
        int occurrences = SearchFrequencyService.searchPattern(htmlContents, keyword);
        // Calculate page ranks and return top-ranked product links
        searchFData.setSearchTime(LocalDateTime.now().toString());
        searchFData.setKeyword(keyword);
        searchFData.setFrequency(occurrences);
        searchFData.setUrl(url);
        return ResponseEntity.ok(searchFData);
    }
}
