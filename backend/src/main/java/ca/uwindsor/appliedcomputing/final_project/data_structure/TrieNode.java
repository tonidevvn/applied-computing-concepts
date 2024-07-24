package ca.uwindsor.appliedcomputing.final_project.data_structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class TrieNode {
    Map<Character, TrieNode> children;
    Set<Integer> documentIds;
    Map<Integer, String> names;
    Map<Integer, String> descriptions;

    public TrieNode() {
        this.children = new HashMap<>();
        this.documentIds = new HashSet<>();
        this.names = new HashMap<>();
        this.descriptions = new HashMap<>();
    }
}