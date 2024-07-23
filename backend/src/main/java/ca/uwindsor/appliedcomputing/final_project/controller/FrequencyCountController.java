package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import ca.uwindsor.appliedcomputing.final_project.dto.WebCrawlerData;
import ca.uwindsor.appliedcomputing.final_project.service.KeywordService;
import ca.uwindsor.appliedcomputing.final_project.service.SearchFrequencyService;
import ca.uwindsor.appliedcomputing.final_project.service.WebCrawlerService;
import ca.uwindsor.appliedcomputing.final_project.util.ValidatorUtil;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/frequency-count")
public class FrequencyCountController {
    @Autowired
    private KeywordService keywordService;

    @Autowired
    private WebCrawlerService webCrawlerService;

    @GetMapping
    public ResponseEntity<KeywordSearchData> getSearchFrequency(@RequestParam("q") String keywords, @RequestParam("url") String url) {
        if (!ValidatorUtil.isValidHtmlUrl(url)) {
            return ResponseEntity.badRequest().build();
        }
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
        return ResponseEntity.ok(searchFData);
    }
}
