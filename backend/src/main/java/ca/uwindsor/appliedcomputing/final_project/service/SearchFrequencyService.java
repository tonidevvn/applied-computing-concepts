package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.data_structure.AVLTreeWordFrequency;
import ca.uwindsor.appliedcomputing.final_project.data_structure.AVLTreeWordFrequency.AvlNode;
import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import ca.uwindsor.appliedcomputing.final_project.util.Sorting;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
     * The path to the directory containing the CSV files.
     */
    public static final String CSV_FILE_PATH = "./data/";

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

    /**
     * A map storing the keyword frequencies for each URL.
     * The keys are keywords, and the values are maps that map URLs to the frequency of the keyword in that URL.
     */
    private static final Map<String, Map<String, Integer>> keywordToUrlsMap = new HashMap<>();

    private static AVLTreeWordFrequency<String> searchFrequencyTree = new AVLTreeWordFrequency<>();

    private static final LinkedList<String> recentSearchQueries = new LinkedList<>();

    public static void initKwService() {
        // Load keywords from CSV file(s) (data.csv)
        readKeywordsFromCsv();
    }

    /**
     * Extracts keywords from a given file and counts their frequencies.
     *
     * @param filepath the path to the file from which to extract keywords
     * @return a map of keywords and their frequencies
     * @throws IOException if an I/O error occurs
     */
    private static Map<String, Integer> extractKeywordsFromFile(String filepath) throws IOException {
        Map<String, Integer> keywordFrequencies = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;
        while ((line = reader.readLine()) != null) {
            // Split each word further by spaces and remove any special characters
            List<String> stringList = Arrays.stream(line.split(" "))
                    .flatMap(word -> Arrays.stream(word.split(",")))
                    .map(word -> word.replaceAll("[^a-zA-Z0-9'-]", ""))
                    .filter(word -> !word.isEmpty())
                    .toList();
            for (String key : stringList) {
                keywordFrequencies.put(key, keywordFrequencies.getOrDefault(key, 0) + 1);
            }
        }
        reader.close();
        return keywordFrequencies;
    }

    /**
     * Extracts keywords from an array of strings, ignoring strings that start with '$' or 'http'.
     *
     * @param strings the array of strings from which to extract keywords
     * @return a list of extracted keywords
     */
    public static Map<String, Integer> extractKeywords(String[] strings) {
        Map<String, Integer> keywordFrequencies = new HashMap<>();
        for (String str : strings) {
            if (!str.startsWith("$") && !str.startsWith("http")) {
                List<String> stringList = Arrays.stream(str.split("\\s+"))
                        //.flatMap(word -> Arrays.stream(word.split("-")))
                        .map(word -> word.replaceAll("[^a-zA-Z0-9'-]", ""))
                        .filter(word -> !word.isEmpty())
                        .toList();
                for (String key : stringList) {
                    String lowerKey = key.toLowerCase();
                    keywordFrequencies.put(lowerKey, keywordFrequencies.getOrDefault(lowerKey, 0) + 1);
                }
            }
        }
        return keywordFrequencies;
    }

    /**
     * Reads keywords and their frequencies from a CSV file.
     * Populates the keywordToUrlsMap with keywords and the URLs where they appear.
     */
    public static void readKeywordsFromCsv() {
        try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH + "zehrs.csv"))) {
            csvReader.readNext(); // Skip the header
            List<String[]> rows = csvReader.readAll();
            Map<String, Integer> keywords = new HashMap<>();
            for (String[] row : rows) {
                if (row.length == 6) {
                    String url = row[5];
                    keywords = extractKeywords(row);
                    for (Map.Entry<String, Integer> entry : keywords.entrySet()) {
                        String keyword = entry.getKey();
                        int count = entry.getValue();
                        // case in-sensitive (convert keyword to lower case
                        keywordToUrlsMap.computeIfAbsent(keyword.toLowerCase(), k -> new HashMap<>()).put(url, count);
                    }
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays websites containing the given query, sorted by the number of occurrences.
     *
     * @param query the search query
     */
    private static void displayMatchingWebsites(String query) {
        Map<String, Integer> urls = SearchFrequencyService.keywordToUrlsMap.getOrDefault(query.toLowerCase(), Collections.emptyMap());
        List<Map.Entry<String, Integer>> sortedUrls = new ArrayList<>(urls.entrySet());
        Sorting.heapSort(sortedUrls);
        int searchResultSizeDisplay = Math.min(MAX_DISPLAY_QUERIES, sortedUrls.size());
        if (searchResultSizeDisplay > 0) {
            sortedUrls.sort((a, b) -> b.getValue() - a.getValue());
            System.out.println("Websites containing the query '" + query + "':");
            for (int j = 0; j < searchResultSizeDisplay; j++) {
                Map.Entry<String, Integer> entry = sortedUrls.get(j);
                System.out.println(entry.getKey() + " (occurrences: " + entry.getValue() + ")");
            }
        } else {
            System.out.println("No websites matching the query '" + query + "'.");
        }
    }

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
     * Prints top 10 of recent search queries.
     */
    private static void printRecentSearchQueries() {
        for (String recentSearchQuery : recentSearchQueries) {
            System.out.println(recentSearchQuery);
        }
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
        PriorityQueue<AvlNode<String>> maxHeap = new PriorityQueue<>((x, y) -> y.getFrequency() - x.getFrequency());
        searchFrequencyTree.inOrderTraversal(maxHeap);

        for (int j = 0; j < limit && !maxHeap.isEmpty(); j++) {
            AVLTreeWordFrequency.AvlNode<String> node = maxHeap.poll();
            KeywordSearchData kwData = new KeywordSearchData();
            kwData.setKeyword(node.getElement());
            kwData.setCount(node.getFrequency());
            kwData.setSearchTime(LocalDateTime.now().toString());
            kwsData.add(kwData);
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