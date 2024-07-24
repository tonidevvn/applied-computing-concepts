package ca.uwindsor.appliedcomputing.final_project.dto;

import lombok.Data;

@Data
public class KeywordSearchData {
    private String keyword;
    private int count;
    private int frequency;
    private String url;
    private String searchTime;

    @Override
    public String toString() {
        return "KeywordSearchData {" +
                "name='" + keyword + '\'' +
                ", count='" + count + '\'' +
                ", frequency='" + frequency + '\'' +
                ", url='" + url + '\'' +
                ", searchTime=" + searchTime + '\'' +
                '}';
    }
}