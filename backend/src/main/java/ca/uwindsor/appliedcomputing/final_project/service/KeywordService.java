package ca.uwindsor.appliedcomputing.final_project.service;


import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;

import java.util.Set;

public interface KeywordService {
    Set<KeywordSearchData> setKeywordSearched(String keyword);
    Set<KeywordSearchData> getRecentKeywordsSearched();
    Set<KeywordSearchData> getTopKeywordsSearched();
}