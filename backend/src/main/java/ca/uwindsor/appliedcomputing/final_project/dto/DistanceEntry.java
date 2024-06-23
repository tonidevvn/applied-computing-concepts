package ca.uwindsor.appliedcomputing.final_project.dto;

import lombok.Data;

@Data
public class DistanceEntry {
    private final int distance;
    private final String word;

    @Override
    public String toString() {
        return "DistanceEntry {" +
                "distance='" + distance + '\'' +
                ", word='" + word + '\'' +
                '}';
    }
}
