package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.data_structure.RedBlackTree;
import ca.uwindsor.appliedcomputing.final_project.data_structure.RedBlackTreeNode;
import ca.uwindsor.appliedcomputing.final_project.util.Resource;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class to provide word completion suggestions using a Red-Black Tree.
 */
@Service
public class WordCompletionService {
    private final RedBlackTree wordFrequencies = new RedBlackTree();

    /**
     * Constructor initializes the word frequencies by reading from a CSV file.
     */
    public WordCompletionService() {
        List<String> words = readVocabulary("data/merged_dataset.csv");
        words.forEach(wordFrequencies::insert);
    }

    /**
     * Reads the vocabulary from a CSV file located in the resources directory.
     *
     * @param filePath the path to the CSV file
     * @return a list of words read from the CSV file
     */
    private List<String> readVocabulary(String filePath) {
        try {
            return Resource.readVocabularyFromCSV(
                    Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(filePath)).toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error reading vocabulary from CSV", e);
        }
    }

    /**
     * Provides word suggestions based on the given prefix.
     *
     * @param prefix the prefix to search for
     * @return a list of suggested words sorted by frequency in descending order
     */
    public List<String> getWordSuggestions(String prefix) {
        try {
            return wordFrequencies.search(prefix).stream()
                    .sorted(Comparator.comparingInt(RedBlackTreeNode::getCount).reversed())
                    .map(RedBlackTreeNode::getKey)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
