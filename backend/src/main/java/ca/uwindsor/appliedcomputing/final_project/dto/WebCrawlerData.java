package ca.uwindsor.appliedcomputing.final_project.dto;

import lombok.Data;

@Data
public class WebCrawlerData {
    private String url;
    private String htmlContents;
    private String time;

    @Override
    public String toString() {
        return "WebCrawlerData {" +
                "url='" + url + '\'' +
                ", htmlContents='" + htmlContents + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}