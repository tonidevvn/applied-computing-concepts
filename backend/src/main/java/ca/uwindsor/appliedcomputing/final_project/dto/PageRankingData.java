package ca.uwindsor.appliedcomputing.final_project.dto;

import lombok.Data;

@Data
public class PageRankingData {
    private String keyword;
    private String url;
    private int frequencyOfSearchKeyword;

    @Override
    public String toString() {
        return "PageRankingData {" +
                "url='" + keyword + '\'' +
                "url='" + url + '\'' +
                ", frequencyOfSearchKeyword='" + frequencyOfSearchKeyword + '\'' +
                '}';
    }
}