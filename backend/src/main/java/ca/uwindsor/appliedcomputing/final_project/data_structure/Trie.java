package ca.uwindsor.appliedcomputing.final_project.data_structure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    // Add a word to the trie with the corresponding document ID and type
    public void insert(String word, int docId, String name) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            node = node.children.computeIfAbsent(ch, c -> new TrieNode());
            node.documentIds.add(docId);
            if (name != null) {
                node.names.put(docId, name);
            }
        }
    }

    // Search for a word in the trie and return the set of document IDs
    public Set<Integer> search(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) {
                return new HashSet<>();
            }
        }
        return node.documentIds;
    }

    // Retrieve names and descriptions for a given document ID
    public Map<String, String> getText(int docId) {
        TrieNode node = root;
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            if (entry.getValue().documentIds.contains(docId)) {
                Map<String, String> textData = new HashMap<>();
                textData.put("name", entry.getValue().names.get(docId));
                return textData;
            }
        }
        return null;
    }
}
