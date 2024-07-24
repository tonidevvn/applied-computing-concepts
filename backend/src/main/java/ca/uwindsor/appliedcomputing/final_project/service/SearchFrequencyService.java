package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.data_structure.SplayTree;
import ca.uwindsor.appliedcomputing.final_project.data_structure.SplayTreeNode;
import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * The SearchFrequency class is responsible for:
 * - Handling user search queries to find matching websites and display the top results.
 * - Tracking and displaying the top 10 recent and most frequent search queries.
 */
@Service
public class SearchFrequencyService {
    /**
     * The maximum number of search queries to display.
     */
    public static final int MAX_DISPLAY_QUERIES = 10;

    private static int[] right_most_occurrence;

    // Preprocess pattern for Boyer-Moore
    private static void preprocessPattern(String patt) {
        int pattern_length = patt.length();
        right_most_occurrence = new int[Character.MAX_VALUE + 1]; // Initialize right array
        // Set default values
        Arrays.fill(right_most_occurrence, -1);
        for (int j = 0; j < pattern_length; j++) {
            right_most_occurrence[patt.charAt(j)] = j; // To Set rightmost occurrences
        }
    }

    // Boyer-Moore pattern search algorithm
    public static int searchPattern(String text, String pattern) {
        preprocessPattern(pattern);

        text = text.toLowerCase(); // Convert text to lowercase
        int text_length = text.length();
        int pattern_length = pattern.length();
        int count = 0;
        int skip;

        for (int i = 0; i <= text_length - pattern_length; i += skip) {
            skip = 0;
            for (int j = pattern_length - 1; j >= 0; j--) {
                if (pattern.charAt(j) != text.charAt(i + j)) {
                    skip = Math.max(1, j - right_most_occurrence[text.charAt(i + j)]); // Calculate skip value
                    break;
                }
            }
            if (skip == 0) {
                count++; // Pattern found
                skip = pattern_length; // Move to next match
            }
        }
        return count; // Return total occurrences
    }

    private static SplayTree<String> searchFrequencyTree = new SplayTree<>();

    private static final LinkedList<String> recentSearchQueries = new LinkedList<>();

    /**
     * Maintain the list of recent search queries.
     * Ensures the list does not exceed MAX_DISPLAY_QUERIES in size.
     *
     * @param query               the new search query to add
     */
    private static void updateRecentSearchQueries(String query) {
        if (recentSearchQueries.size() == MAX_DISPLAY_QUERIES) {
            recentSearchQueries.removeLast();
        }
        recentSearchQueries.addFirst(query);
    }

    /**
     * Handles search queries from the user.
     * Tracks the frequency of search queries and displays matching websites.
     * Displays the top 10 recent search queries and top 10 most frequent search queries.
     *
     * @return Set of Keyword Data
     */
    public static KeywordSearchData performSearchQueries(String query) {
        String now = LocalDateTime.now().toString();
        // Trim the search query before processing
        query = query.trim();
        // Insert & update query count in the search tree
        searchFrequencyTree.insert(query);
        // Update recent search queries list
        updateRecentSearchQueries(query + "," + now);

        KeywordSearchData kwData = new KeywordSearchData();
        kwData.setKeyword(query);
        kwData.setCount(searchFrequencyTree.findFrequency(query));
        kwData.setSearchTime(now);
        return kwData;
    }

    /**
     * Retrieves the top search queries based on their frequency.
     * This method uses the AVL tree to get the top K search queries.
     *
     * @return a set of KeywordData representing the top search queries and their frequencies
     */
    public static List<KeywordSearchData> topSearchQueries(int limit) {
        List<KeywordSearchData> kwsData = new ArrayList<>();
        List<SplayTreeNode<String>> sortedNodes = searchFrequencyTree.getSortedNodesByFrequency();

        int count = 0;
        for (SplayTreeNode<String> node : sortedNodes) {
            KeywordSearchData kwData = new KeywordSearchData();
            kwData.setKeyword(node.key);
            kwData.setCount(node.getFrequency());
            kwData.setSearchTime(LocalDateTime.now().toString());
            kwsData.add(kwData);
            count ++;
            if (count > limit) {
                break;
            }
        }
        return kwsData;
    }

    /**
     * Retrieves the top recent search queries based on their frequency.
     * This method uses the provided list to get the top recent search queries
     * and returns them in the same order as they appear in the list.
     *
     * @return a set of KeywordData representing the top recent search queries and their frequencies, in the same order as the input list
     */
    public static List<KeywordSearchData> topRecentSearchQueries() {
        List<KeywordSearchData> response = new ArrayList<>();
        for (String recentSearchQuery : recentSearchQueries) {
            KeywordSearchData kwData = new KeywordSearchData();
            String[] kwt = recentSearchQuery.split(",");
            kwData.setKeyword(kwt[0]);
            kwData.setCount(1);
            kwData.setSearchTime(kwt[1]);
            response.add(kwData);
        }
        return response;
    }
}