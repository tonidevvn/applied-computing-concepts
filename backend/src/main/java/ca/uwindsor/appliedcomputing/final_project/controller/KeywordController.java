package ca.uwindsor.appliedcomputing.final_project.controller;
import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import ca.uwindsor.appliedcomputing.final_project.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/keyword-search")
public class KeywordController {
    @Autowired
    private KeywordService keywordService;

    @GetMapping(path = "")
    public List<KeywordSearchData> getKeywordSearchFrequency(@RequestParam("q") String searchKeyword) throws Exception {
        return keywordService.setKeywordSearched(searchKeyword);
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
