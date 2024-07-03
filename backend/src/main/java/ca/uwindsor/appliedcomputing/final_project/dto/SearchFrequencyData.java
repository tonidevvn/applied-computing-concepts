package ca.uwindsor.appliedcomputing.final_project.dto;

import lombok.Data;

@Data
public class SearchFrequencyData {
    private String word;
    private int frequency;
    private String url;
    private String searchTime;

    @Override
    public String toString() {
        return "SearchFrequencyData {" +
                "name='" + word + '\'' +
                ", count='" + frequency + '\'' +
                ", url='" + url + '\'' +
                ", searchTime=" + searchTime + '\'' +
                '}';
    }
}
