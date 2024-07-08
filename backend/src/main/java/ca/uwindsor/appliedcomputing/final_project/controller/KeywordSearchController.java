package ca.uwindsor.appliedcomputing.final_project.controller;
import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import ca.uwindsor.appliedcomputing.final_project.dto.WebCrawlerData;
import ca.uwindsor.appliedcomputing.final_project.service.KeywordService;
import ca.uwindsor.appliedcomputing.final_project.service.SearchFrequencyService;
import ca.uwindsor.appliedcomputing.final_project.service.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/keyword-search")
public class KeywordSearchController {
    @Autowired
    private KeywordService keywordService;

    @Autowired
    private WebCrawlerService webCrawlerService;

    @GetMapping(path = "/count")
    public KeywordSearchData getKeywordSearchCount(@RequestParam("q") String searchKeyword) throws Exception {
        return keywordService.setKeywordSearch(searchKeyword);
    }

    @GetMapping(path = "/frequency")
    public KeywordSearchData getKeywordSearchFrequency(@RequestParam("q") String keywords, @RequestParam("url") String url) {
        KeywordSearchData searchFData = keywordService.setKeywordSearch(keywords);
        WebCrawlerData wcData = webCrawlerService.crawlWebUrl(url);
        // Split the keywords string into an array of search keywords
        String[] searchKeywords = keywords.split("\\s+");
        String[] words = wcData.getHtmlContents().split("\\s+");

        Map<String, Integer> wordsMap = SearchFrequencyService.extractKeywords(words);
        // Calculate page ranks and return top-ranked product links
        int frequencyTotal = 0;
        for (String eachKw : searchKeywords) {
            frequencyTotal += wordsMap.getOrDefault(eachKw.toLowerCase(), 0);
        }
        searchFData.setFrequency(frequencyTotal);
        searchFData.setUrl(url);
        return searchFData;
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
