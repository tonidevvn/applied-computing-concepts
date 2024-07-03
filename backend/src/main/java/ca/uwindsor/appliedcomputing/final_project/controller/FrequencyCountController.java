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
import java.util.Map;


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
        // Split the keywords string into an array of search keywords
        String[] words = wcData.getHtmlContents().split("\\s+");

        Map<String, Integer> wordsMap = SearchFrequencyService.extractKeywords(words);
        // Calculate page ranks and return top-ranked product links
        searchFData.setSearchTime(LocalDateTime.now().toString());
        searchFData.setWord(keyword);
        searchFData.setFrequency(wordsMap.getOrDefault(keyword, 0));
        searchFData.setUrl(url);
        return searchFData;
    }
}
