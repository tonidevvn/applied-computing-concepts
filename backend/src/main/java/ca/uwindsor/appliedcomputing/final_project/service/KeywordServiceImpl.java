package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class KeywordServiceImpl implements KeywordService {

    @Override
    public Set<KeywordSearchData> setKeywordSearched(String keyword) {
        return SearchFrequency.performSearchQueries(keyword);
    }

    @Override
    public Set<KeywordSearchData> getRecentKeywordsSearched() {
        return SearchFrequency.topRecentSearchQueries();
    }

    @Override
    public Set<KeywordSearchData> getTopKeywordsSearched() {
        return SearchFrequency.topSearchQueries();
    }
}
