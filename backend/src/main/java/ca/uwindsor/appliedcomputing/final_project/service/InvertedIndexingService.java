package ca.uwindsor.appliedcomputing.final_project.service;

import ca.uwindsor.appliedcomputing.final_project.data_structure.Trie;
import ca.uwindsor.appliedcomputing.final_project.util.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class InvertedIndexingService {

    private final Trie trie;

    public InvertedIndexingService() {
        this.trie = new Trie();
        readMergedCsv();
    }

    // Read merged_csv file
    public void readMergedCsv() {
        Path csvFilePath = Resource.getMergedDataSet();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath.toFile()))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                // Skip the header line if present
                if (lineNumber == 0) {
                    lineNumber++;
                    continue;
                }
                String[] columns = line.split(",", 7);
                // Check if the row has enough columns
                if (columns.length >= 5) {
                    // Add product name and description to trie
                    addToTrie(columns[0], lineNumber, columns[0]);  // Adding name and description
                }
                lineNumber++;
            }
        } catch (IOException e) {
            log.error("Error reading CSV file", e);
        }
    }

    // Helper method to add words to the trie
    private void addToTrie(String text, int docId, String name) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        String[] terms = text.trim().split("\\s+");
        for (String term : terms) {
            trie.insert(term.toLowerCase(), docId, name);
        }
    }

    public Set<Map<String, String>> search(String query) {
        Set<Integer> docIds =  trie.search(query);
        Set<Map<String, String>> result = new HashSet<>();
        for(int docId : docIds) {
            Map<String, String> doc = new HashMap<>();
            Map<String, String> text = trie.getText(docId);
            doc.put("name", text.get("name"));
            doc.put("docId", String.valueOf(docId));
            result.add(doc);
        }
        return result;
    }
}
