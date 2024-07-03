package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeywordService {

    public List<KeywordSearchData> setKeywordSearched(String keyword) {
        return SearchFrequencyService.performSearchQueries(keyword);
    }

    public List<KeywordSearchData> getRecentKeywordsSearched() {
        return SearchFrequencyService.topRecentSearchQueries();
    }

    public List<KeywordSearchData> getTopKeywordsSearched(int limit) {
        return SearchFrequencyService.topSearchQueries(limit);
    }
}
