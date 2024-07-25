package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.data_structure.AVLTreeWordFrequency;
import ca.uwindsor.appliedcomputing.final_project.dto.PageRankingData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service class to handle page ranking based on keyword frequencies
 */
@Service
public class PageRankingService {

    // Map to store product links and their associated AVLTreeWordFrequency of keyword frequencies
    private Map<String, AVLTreeWordFrequency<String>> productKeywordFrequencies = new HashMap<>();

    // Max-heap (PriorityQueue) to store products ranked by their calculated ranks
    private PriorityQueue<Map.Entry<String, Integer>> maxHeap = new PriorityQueue<>(
            Comparator.comparingInt((Map.Entry<String, Integer> entry) -> entry.getValue()).reversed());

    // File path for the CSV dataset containing product data
    private final String csvFilePath = Objects.requireNonNull(getClass().getClassLoader()
            .getResource("data/merged_dataset.csv")).getFile();

    /**
     * Constructor for PageRankingService.
     * Initializes the service by parsing the CSV dataset and populating productKeywordFrequencies.
     */
    public PageRankingService() {
        parseCSV();
    }

    /**
     * Parses the CSV dataset to build keyword frequency AVL trees for each product.
     * Each product's name and description are processed to extract keywords.
     * Keywords are inserted into an AVLTreeWordFrequency and stored in productKeywordFrequencies.
     */
    private void parseCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] headers = reader.readNext(); // Assuming first row is header

            String[] record;
            while ((record = reader.readNext()) != null) {
                String productLink = record[5]; // Assuming index 3 contains product link
                String productName = record[0].toLowerCase(); // Assuming index 0 contains product name
                String productDescription = record.length > 6 ? record[6].toLowerCase() : ""; // Assuming index 4 contains product description

                // Split combined product name and description into individual keywords
                String[] keywords = (productName + " " + productDescription).split("\\s+");
                AVLTreeWordFrequency<String> keywordTree = new AVLTreeWordFrequency<>();

                // Insert each keyword into the AVL tree for the current product
                for (String keyword : keywords) {
                    if (!keyword.isEmpty()) {
                        keywordTree.insert(keyword);
                    }
                }

                // Store the AVL tree of keyword frequencies for the current product
                productKeywordFrequencies.put(productLink, keywordTree);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates page ranks for products based on the provided search keywords.
     * Ranks are determined by the frequency of each keyword in the product's AVLTreeWordFrequency.
     * Returns a list of product links sorted by their ranks in descending order.
     *
     * @param searchKeywords Array of keywords to search for and rank products
     * @return List of product links sorted by rank
     */
    public List<PageRankingData> calculatePageRanks(String[] searchKeywords, int limit) {
        maxHeap.clear();
        // Iterate through each product in productKeywordFrequencies
        for (String productLink : productKeywordFrequencies.keySet()) {
            AVLTreeWordFrequency<String> keywordTree = productKeywordFrequencies.get(productLink);
            int rank = 0;

            // Calculate rank for the current product based on search keywords
            for (String keyword : searchKeywords) {
                if (keywordTree.contains(keyword)) {
                    rank += keywordTree.findFrequency(keyword); // Increase rank by keyword frequency
                    rank += 1; // Additional rank for keyword presence
                }
            }

            // Add product link and its calculated rank to the maxHeap
            maxHeap.add(new AbstractMap.SimpleEntry<>(productLink, rank));
        }

        // Retrieve top-ranked product links from maxHeap
        List<PageRankingData> topRankedProducts = new ArrayList<>();
        int count = 0;
        while (!maxHeap.isEmpty() && count < limit) {
            Map.Entry<String, Integer> entry = maxHeap.poll();
            PageRankingData rankingData = new PageRankingData();
            rankingData.setUrl(entry.getKey());
            rankingData.setFrequencyOfSearchKeyword(entry.getValue());
            rankingData.setKeyword(Arrays.toString(searchKeywords));
            topRankedProducts.add(rankingData);
            count++;
        }

        return topRankedProducts;
    }
}
