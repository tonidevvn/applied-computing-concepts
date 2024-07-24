package ca.uwindsor.appliedcomputing.final_project.data_structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Splay Tree, a self-adjusting binary search tree with the additional property that
 * recently accessed elements are quick to access again.
 *
 * @param <T> the type of the key stored in the tree, which must be comparable
 */
public class SplayTree<T extends Comparable<T>> {
    private SplayTreeNode<T> root;

    /**
     * Constructor to initialize the splay tree.
     */
    public SplayTree() {
        this.root = null;
    }

    /**
     * Performs a right rotation on the given node.
     *
     * @param x the node to be rotated
     * @return the new root after rotation
     */
    private SplayTreeNode<T> rightRotate(SplayTreeNode<T> x) {
        SplayTreeNode<T> y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    /**
     * Performs a left rotation on the given node.
     *
     * @param x the node to be rotated
     * @return the new root after rotation
     */
    private SplayTreeNode<T> leftRotate(SplayTreeNode<T> x) {
        SplayTreeNode<T> y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    /**
     * Splays the tree with the given key, bringing the node with the key to the root.
     *
     * @param root the root of the tree to be splayed
     * @param key  the key to be splayed to the root
     * @return the new root after splaying
     */
    private SplayTreeNode<T> splay(SplayTreeNode<T> root, T key) {
        if (root == null || root.key.compareTo(key) == 0)
            return root;

        if (root.key.compareTo(key) > 0) {
            if (root.left == null)
                return root;

            if (root.left.key.compareTo(key) > 0) {
                root.left.left = splay(root.left.left, key);
                root = rightRotate(root);
            } else if (root.left.key.compareTo(key) < 0) {
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null)
                    root.left = leftRotate(root.left);
            }
            return (root.left == null) ? root : rightRotate(root);
        } else {
            if (root.right == null)
                return root;

            if (root.right.key.compareTo(key) > 0) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null)
                    root.right = rightRotate(root.right);
            } else if (root.right.key.compareTo(key) < 0) {
                root.right.right = splay(root.right.right, key);
                root = leftRotate(root);
            }
            return (root.right == null) ? root : leftRotate(root);
        }
    }

    /**
     * Inserts a new key into the splay tree.
     *
     * @param key the key to be inserted
     */
    public void insert(T key) {
        root = insert(root, key);
    }

    /**
     * Helper method to insert a new key into the splay tree.
     *
     * @param root the root of the tree
     * @param key  the key to be inserted
     * @return the new root after insertion
     */
    private SplayTreeNode<T> insert(SplayTreeNode<T> root, T key) {
        if (root == null)
            return new SplayTreeNode<>(key);

        root = splay(root, key);

        if (root.key.compareTo(key) == 0) {
            root.frequency++;  // Duplicate; increment frequency
            return root;
        }

        SplayTreeNode<T> newNode = new SplayTreeNode<>(key);
        if (root.key.compareTo(key) > 0) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }

        return newNode;
    }

    /**
     * Returns the root of the splay tree.
     *
     * @return the root of the splay tree
     */
    public SplayTreeNode<T> getRoot() {
        return root;
    }

    /**
     * Get the frequency of an item in the tree.
     *
     * @param x the item to get the frequency for.
     * @return the frequency of the item, or 0 if not found.
     */
    public int findFrequency(T x) {
        return findFrequency(x, root);
    }

    /**
     * Internal method to get the frequency of an item in a subtree.
     *
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return the frequency of the item in the subtree.
     */
    private int findFrequency(T x, SplayTreeNode<T> t) {
        while (t != null) {
            int compareResult = x.compareTo(t.key);

            if (compareResult < 0)
                t = t.left;
            else if (compareResult > 0)
                t = t.right;
            else
                return t.frequency;    // Match
        }

        return 0;   // No match
    }

    /**
     * Perform an in-order traversal to collect nodes in a list.
     *
     * @return a list of SplayTreeNodes sorted by their keys.
     */
    public List<SplayTreeNode<T>> inOrderTraversal() {
        List<SplayTreeNode<T>> nodeList = new ArrayList<>();
        inOrderTraversal(root, nodeList);
        return nodeList;
    }

    /**
     * Perform an in-order traversal to collect nodes in a list.
     *
     * @return a list of SplayTreeNodes sorted by their keys
     */
    private void inOrderTraversal(SplayTreeNode<T> node, List<SplayTreeNode<T>> nodeList) {
        if (node != null) {
            inOrderTraversal(node.left, nodeList);
            nodeList.add(node);
            inOrderTraversal(node.right, nodeList);
        }
    }

    /**
     * Get a sorted list of nodes based on their frequency.
     *
     * @return a list of SplayTreeNodes sorted by frequency in descending order.
     */
    public List<SplayTreeNode<T>> getSortedNodesByFrequency() {
        List<SplayTreeNode<T>> nodeList = inOrderTraversal();
        nodeList.sort((a, b) -> Integer.compare(b.frequency, a.frequency));
        return nodeList;
    }
}
