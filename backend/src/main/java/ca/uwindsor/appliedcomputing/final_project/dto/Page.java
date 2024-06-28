package ca.uwindsor.appliedcomputing.final_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Page<T> {
    public int total;
    public T data;
}
