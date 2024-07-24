package ca.uwindsor.appliedcomputing.final_project.data_structure;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a Red-Black Tree data structure to store words with their frequencies.
 */
@Getter
@Setter
public class RedBlackTree {
    private RedBlackTreeNode root;
    private RedBlackTreeNode TNULL;

    /**
     * Constructor initializes the Red-Black Tree with a TNULL node.
     */
    public RedBlackTree() {
        TNULL = new RedBlackTreeNode("");
        TNULL.isRed = false;
        root = TNULL;
    }

    /**
     * Left-rotate the subtree rooted at node x.
     *
     * @param x the root of the subtree to rotate
     */
    private void leftRotate(RedBlackTreeNode x) {
        RedBlackTreeNode y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    /**
     * Right-rotate the subtree rooted at node x.
     *
     * @param x the root of the subtree to rotate
     */
    private void rightRotate(RedBlackTreeNode x) {
        RedBlackTreeNode y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    /**
     * Fix the Red-Black Tree properties after insertion.
     *
     * @param k the newly inserted node
     */
    private void fixInsert(RedBlackTreeNode k) {
        RedBlackTreeNode u;
        while (k.parent != null && k.parent.isRed) {
            if (k.parent == k.parent.parent.left) {
                u = k.parent.parent.right;
                if (u.isRed) {
                    u.isRed = false;
                    k.parent.isRed = false;
                    k.parent.parent.isRed = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.isRed = false;
                    k.parent.parent.isRed = true;
                    rightRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.left;
                if (u.isRed) {
                    u.isRed = false;
                    k.parent.isRed = false;
                    k.parent.parent.isRed = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.isRed = false;
                    k.parent.parent.isRed = true;
                    leftRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.isRed = false;
    }

    /**
     * Insert a new word into the Red-Black Tree.
     *
     * @param key the word to insert
     */
    public void insert(String key) {
        RedBlackTreeNode node = new RedBlackTreeNode(key);
        RedBlackTreeNode y = null;
        RedBlackTreeNode x = this.root;

        while (x != TNULL) {
            y = x;
            if (node.key.compareTo(x.key) < 0) {
                x = x.left;
            } else if (node.key.compareTo(x.key) > 0) {
                x = x.right;
            } else {
                x.count++;
                return;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.key.compareTo(y.key) < 0) {
            y.left = node;
        } else {
            y.right = node;
        }

        node.left = TNULL;
        node.right = TNULL;
        node.isRed = true;
        fixInsert(node);
    }

    /**
     * Perform an in-order traversal to find all words with the given prefix.
     *
     * @param node   the current node
     * @param prefix the prefix to search for
     * @param result the list to store matching nodes
     */
    private void inOrderHelper(RedBlackTreeNode node, String prefix, List<RedBlackTreeNode> result) {
        if (node != TNULL) {
            inOrderHelper(node.left, prefix, result);
            if (node.key.startsWith(prefix)) {
                result.add(node);
            }
            inOrderHelper(node.right, prefix, result);
        }
    }

    /**
     * Search for all words that start with the given prefix.
     *
     * @param prefix the prefix to search for
     * @return a list of nodes whose keys start with the given prefix
     */
    public List<RedBlackTreeNode> search(String prefix) {
        List<RedBlackTreeNode> result = new ArrayList<>();
        inOrderHelper(this.root, prefix, result);
        return result;
    }
}
