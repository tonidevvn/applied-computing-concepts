package ca.uwindsor.appliedcomputing.final_project.dto;

import lombok.Data;

@Data
public class KeywordSearchData {
    private String keyword;
    private int count;
    private String searchTime;

    @Override
    public String toString() {
        return "Keyword {" +
                "name='" + keyword + '\'' +
                ", count='" + count + '\'' +
                ", searchTime=" + searchTime + '\'' +
                '}';
    }
}