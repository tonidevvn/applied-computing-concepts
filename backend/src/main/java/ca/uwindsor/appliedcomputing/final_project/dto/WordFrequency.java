package ca.uwindsor.appliedcomputing.final_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents a word and its frequency of occurrence.
 * It implements the Comparable interface to allow objects of this class to be compared based on the word.
 */
@Data
@AllArgsConstructor
public class WordFrequency implements Comparable<WordFrequency> {

    /**
     * The word represented by this object.
     */
    private String word;

    /**
     * The frequency of occurrence of the word.
     */
    private Integer frequency;

    /**
     * Compares this object with the specified object for order.
     * Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * The comparison is based on the lexicographical order of the word represented by this object.
     *
     * @param other the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(WordFrequency other) {
        return this.word.compareTo(other.word);
    }
}