package ca.uwindsor.appliedcomputing.final_project.data_structure;

import lombok.Getter;

/**
 * Represents a node in a splay tree.
 *
 * @param <T> the type of the key stored in the node, which must be comparable
 */
public class SplayTreeNode<T extends Comparable<T>> {
    public T key; // The key stored in the node
    public SplayTreeNode<T> left, right; // Left and right child nodes
    @Getter
    int frequency; // Frequency of the element, initialized to 1

    /**
     * Constructor to initialize the node with a key.
     * Initializes the left and right child nodes to null and sets the frequency to 1.
     *
     * @param key the key to be stored in the node
     */
    public SplayTreeNode(T key) {
        this.key = key;
        this.left = this.right = null;
        this.frequency = 1;
    }
}