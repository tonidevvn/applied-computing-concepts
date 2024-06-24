package ca.uwindsor.appliedcomputing.final_project.controller;

import ca.uwindsor.appliedcomputing.final_project.dto.PageRankingData;
import ca.uwindsor.appliedcomputing.final_project.service.PageRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/page-ranking")
public class PageRankingController {
    @Autowired
    private PageRankingService pageRankingService;

    @GetMapping
    public List<PageRankingData> getPageRanking(@RequestParam("search") String keywords, @RequestParam(required = false, defaultValue = "10") int limit) {
        // Split the keywords string into an array of search keywords
        String[] searchKeywords = keywords.split("\\s+");

        // Calculate page ranks and return top-ranked product links
        return pageRankingService.calculatePageRanks(searchKeywords, limit);
    }
}
