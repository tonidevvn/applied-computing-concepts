package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.SearchFrequencyData;
import ca.uwindsor.appliedcomputing.final_project.dto.WebCrawlerData;
import ca.uwindsor.appliedcomputing.final_project.service.SearchFrequencyService;
import ca.uwindsor.appliedcomputing.final_project.service.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/frequency-count")
public class FrequencyCountController {
    @Autowired
    private SearchFrequencyService searchFrequencyService;

    @Autowired
    private WebCrawlerService webCrawlerService;

    @GetMapping
    public SearchFrequencyData getSearchFrequency(@RequestParam("q") String keyword, @RequestParam("url") String url) {
        SearchFrequencyData searchFData = new SearchFrequencyData();
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
        searchFData.setWord(keyword);
        searchFData.setFrequency(occurrences);
        searchFData.setUrl(url);
        return searchFData;
    }
}
