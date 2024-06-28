package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.data_structure.AVLTreeWordFrequency;
import ca.uwindsor.appliedcomputing.final_project.dto.WordFrequency;
import ca.uwindsor.appliedcomputing.final_project.util.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordCompletionService {
    /**
     * This method reads a CSV file from the given path and extracts all the words from it.
     * It reads the file line by line, splits each line into words, filters out non-alphabetic words and links,
     * converts all words to lower case, and collects them into a list.
     *
     * @param csvFilePath The path of the CSV file to read.
     * @return A list of words extracted from the file. If an exception occurs during the process, it returns an empty list.
     */
    public List<String> readVocabularyFromCSV(Path csvFilePath) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath.toFile()))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                // Skip the header line if present
                if (lineNumber++ == 0) {
                    continue;
                }
                String[] columns = line.split(",");
                for (String column : columns) {
                    List<String> wordsInColumn = Arrays.stream(column.strip().split("\\s+"))
                            .filter(word -> word.matches("[a-zA-Z]+"))
                            .map(String::toLowerCase)
                            .toList();
                    words.addAll(wordsInColumn);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    /**
     * This method builds a frequency map of words. It takes a list of words as input and returns a map where the keys are the words and the values are the frequencies of each word.
     *
     * @param words The list of words to count frequencies for.
     * @return A map where the keys are the words from the input list and the values are the frequencies of each word.
     */
    public Map<String, Integer> buildWordFrequencies(List<String> words) {
        return words.stream()
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
    }

    /**
     * This method builds an AVL tree from a map of word frequencies.
     * Each key-value pair in the map is transformed into a WordFrequency object and inserted into the tree.
     *
     * @param wordFrequencies A map where the keys are words and the values are their corresponding frequencies.
     * @return An AVL tree where each node is a WordFrequency object representing a word and its frequency.
     */
    public AVLTreeWordFrequency<WordFrequency> buildTree(Map<String, Integer> wordFrequencies) {
        AVLTreeWordFrequency<WordFrequency> tree = new AVLTreeWordFrequency<>();
        wordFrequencies.forEach((word, frequency) -> {
            tree.insert(new WordFrequency(word, frequency));
        });
        return tree;
    }

    /**
     * This method collects all the words in the subtree of a given root node that start with a specified prefix.
     * It uses a recursive approach to traverse the tree in an in-order fashion (left-root-right).
     * If a word starts with the given prefix, it is added to the list of words.
     *
     * @param root The root node of the subtree to search in.
     * @param prefix The prefix that the words should start with.
     * @return A list of WordFrequency objects representing the words that start with the given prefix.
     */
    public List<WordFrequency> collectSubTreeWords(AVLTreeWordFrequency.AvlNode<WordFrequency> root, String prefix) {
        List<WordFrequency> words = new ArrayList<>();
        if (root != null) {
            words.addAll(collectSubTreeWords(root.getLeft(), prefix));
            if (root.getElement().getWord().startsWith(prefix)) {
                words.add(root.getElement());
            }
            words.addAll(collectSubTreeWords(root.getRight(), prefix));
        }
        return words;
    }

    /**
     * This method traverses the AVL tree starting from a given root node and finds the first node where the word starts with a specified prefix.
     * It uses a recursive approach to traverse the tree. If the word at the current node starts with the prefix, it returns the current node.
     * If the word at the current node is lexicographically greater than the prefix, it continues the search in the left subtree.
     * Otherwise, it continues the search in the right subtree.
     *
     * @param root The root node of the subtree to start the search from.
     * @param prefix The prefix that the word should start with.
     * @return The first node found where the word starts with the given prefix. If no such node is found, it returns null.
     */
    public AVLTreeWordFrequency.AvlNode<WordFrequency> traverseByPrefix(AVLTreeWordFrequency.AvlNode<WordFrequency> root, String prefix) {
        if (root == null) {
            return null;
        }
        if (root.getElement().getWord().startsWith(prefix)) {
            return root;
        }
        if (root.getElement().getWord().compareTo(prefix) > 0) {
            return traverseByPrefix(root.getLeft(), prefix);
        }
        return traverseByPrefix(root.getRight(), prefix);
    }

    /**
     * This method ranks the words based on their frequency of occurrence.
     * It uses a priority queue to sort the WordFrequency objects in descending order of their frequency.
     * The words are then polled from the queue and printed to the console.
     *
     * @param wordFrequencies A list of WordFrequency objects to be ranked.
     */
    public List<WordFrequency> rankWords(List<WordFrequency> wordFrequencies) {
        PriorityQueue<WordFrequency> pq = new PriorityQueue<>(Comparator.comparingInt(WordFrequency::getFrequency).reversed());
        pq.addAll(wordFrequencies);

        if (pq.isEmpty()) {
            System.out.println("No suggestions found.");
            return Collections.emptyList();
        }
        List<WordFrequency> topSuggestions = new ArrayList<>();
        while (!pq.isEmpty()) {
            topSuggestions.add(pq.poll());
        }
        return topSuggestions;
    }

    /**
     * The main method of the application.
     * It performs the following steps:
     * 1. Walks through the resources directory and reads all the words from the CSV files.
     * 2. Builds a frequency map of the words.
     * 3. Builds an AVL tree from the frequency map.
     * 4. Asks the user to enter a prefix.
     * 5. Traverses the AVL tree to find the first node where the word starts with the entered prefix.
     * 6. Collects all the words in the subtree of the found node that start with the prefix.
     * 7. Ranks the collected words based on their frequency of occurrence and prints them to the console.
     *
     * @param args command-line arguments (not used).
     */

    public List<WordFrequency> getWordSuggestions(String prefix) {
        // Read all words from the CSV files in the resources directory
        List<String> words = Resource.walkResources().stream()
                .flatMap(path -> readVocabularyFromCSV(path).stream())
                .collect(Collectors.toList());

        // Build a frequency map of the words
        Map<String, Integer> wordFrequencies = buildWordFrequencies(words);

        // Build an AVL tree from the frequency map
        AVLTreeWordFrequency<WordFrequency> tree = buildTree(wordFrequencies);

        // Traverse the AVL tree to find the first node where the word starts with the prefix
        AVLTreeWordFrequency.AvlNode<WordFrequency> node = traverseByPrefix(tree.getRoot(), prefix);

        // Collect all the words in the subtree of the found node that start with the prefix
        List<WordFrequency> subTreeWords = collectSubTreeWords(node, prefix);

        // Rank the collected words based on their frequency of occurrence and print them to the console
        return rankWords(subTreeWords);
    }
}
