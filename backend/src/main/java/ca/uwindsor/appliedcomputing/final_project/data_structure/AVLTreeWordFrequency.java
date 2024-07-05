package ca.uwindsor.appliedcomputing.final_project.data_structure;

import ca.uwindsor.appliedcomputing.final_project.dto.KeywordSearchData;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

public class AVLTreeWordFrequency<T extends Comparable<? super T>> {

    private AvlNode<T> root;
    private static final int ALLOWED_IMBALANCE = 1;

    /**
     * Constructs an empty AVL tree.
     */
    public AVLTreeWordFrequency() {
        root = null;
    }

    /**
     * Gets the root node of the tree.
     * @return the root node of the tree.
     */
    public AvlNode<T> getRoot() {
        return root;
    }

    /**
     * Inserts the specified value into the tree.
     * Duplicates are ignored.
     * @param value the value to insert.
     */
    public void insert(T value) {
        root = insert(value, root);
    }

    /**
     * Inserts the specified value into the subtree rooted at the given node.
     * @param value the value to insert.
     * @param node the root of the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<T> insert(T value, AvlNode<T> node) {
        if (node == null)
            return new AvlNode<>(value, null, null);

        int compareResult = value.compareTo(node.element);
        if (compareResult < 0)
            node.left = insert(value, node.left);
        else if (compareResult > 0)
            node.right = insert(value, node.right);
        else
            node.frequency++;  // Duplicate; increment frequency

        return balance(node);
    }

    /**
     * Balances the subtree rooted at the given node.
     * @param node the root of the subtree.
     * @return the balanced subtree.
     */
    private AvlNode<T> balance(AvlNode<T> node) {
        if (node == null)
            return node;

        if (height(node.left) - height(node.right) > ALLOWED_IMBALANCE) {
            if (height(node.left.left) >= height(node.left.right))
                node = rotateWithLeftChild(node);
            else
                node = doubleWithLeftChild(node);
        } else if (height(node.right) - height(node.left) > ALLOWED_IMBALANCE) {
            if (height(node.right.right) >= height(node.right.left))
                node = rotateWithRightChild(node);
            else
                node = doubleWithRightChild(node);
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        return node;
    }

    /**
     * Returns the height of the given node, or -1 if the node is null.
     * @param node the node.
     * @return the height of the node.
     */
    private int height(AvlNode<T> node) {
        return node == null ? -1 : node.height;
    }

    /**
     * Performs a single rotation with the left child.
     * This is a single rotation for AVL trees for case 1.
     * @param node the root of the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<T> rotateWithLeftChild(AvlNode<T> node) {
        AvlNode<T> leftNode = node.left;
        node.left = leftNode.right;
        leftNode.right = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        leftNode.height = Math.max(height(leftNode.left), node.height) + 1;
        return leftNode;
    }

    /**
     * Performs a single rotation with the right child.
     * This is a single rotation for AVL trees for case 4.
     * @param node the root of the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<T> rotateWithRightChild(AvlNode<T> node) {
        AvlNode<T> rightNode = node.right;
        node.right = rightNode.left;
        rightNode.left = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        rightNode.height = Math.max(height(rightNode.right), node.height) + 1;
        return rightNode;
    }

    /**
     * Performs a double rotation: first rotates the left child with its right child,
     * then rotates the node with its new left child.
     * This is a double rotation for AVL trees for case 2.
     * @param node the root of the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<T> doubleWithLeftChild(AvlNode<T> node) {
        node.left = rotateWithRightChild(node.left);
        return rotateWithLeftChild(node);
    }

    /**
     * Performs a double rotation: first rotates the right child with its left child,
     * then rotates the node with its new right child.
     * This is a double rotation for AVL trees for case 3.
     * @param node the root of the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<T> doubleWithRightChild(AvlNode<T> node) {
        node.right = rotateWithLeftChild(node.right);
        return rotateWithRightChild(node);
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode<T> findMin( AvlNode<T> t )
    {
        if( t == null )
            return t;

        while( t.left != null )
            t = t.left;
        return t;
    }

    public void inOrderTraversal(PriorityQueue<AvlNode<T>> maxHeap) {
        inOrderTraversal(root, maxHeap);
    }
    /**
     * Performs an in-order traversal of the subtree rooted at the given node.
     * Adds each node to the max heap.
     *
     * @param t    the root of the subtree
     * @param maxHeap the max heap to which nodes are added
     */
    private void inOrderTraversal(AvlNode<T> t, PriorityQueue<AvlNode<T>> maxHeap) {
        if (t != null) {
            inOrderTraversal(t.left, maxHeap);
            maxHeap.add(t);
            inOrderTraversal(t.right, maxHeap);
        }
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode<T> findMax( AvlNode<T> t )
    {
        if( t == null )
            return t;

        while( t.right != null )
            t = t.right;
        return t;
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if x is found.
     */
    public boolean contains( T x )
    {
        return contains( x, root );
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private boolean contains( T x, AvlNode<T> t )
    {
        while( t != null )
        {
            int compareResult = x.compareTo( t.element );

            if( compareResult < 0 )
                t = t.left;
            else if( compareResult > 0 )
                t = t.right;
            else
                return true;    // Match
        }

        return false;   // No match
    }

    /**
     * Get the frequency of an item in the tree.
     * @param x the item to get the frequency for.
     * @return the frequency of the item, or 0 if not found.
     */
    public int findFrequency( T x )
    {
        return findFrequency( x, root );
    }

    /**
     * Internal method to get the frequency of an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return the frequency of the item in the subtree.
     */
    private int findFrequency( T x, AvlNode<T> t )
    {
        while( t != null )
        {
            int compareResult = x.compareTo( t.element );

            if( compareResult < 0 )
                t = t.left;
            else if( compareResult > 0 )
                t = t.right;
            else
                return t.frequency;    // Match
        }

        return 0;   // No match
    }

    /**
     * Internal method to print a subtree in sorted order.
     * @param t the node that roots the tree.
     */
    private void printTree( AvlNode<T> t )
    {
        if( t != null )
        {
            printTree( t.left );
            System.out.println( t.element + " (" + t.frequency + ")" );
            printTree( t.right );
        }
    }

    /**
     * Represents a node in the AVL tree.
     * @param <T> the type of element held in this node.
     */
    @Data
    public static class AvlNode<T> {
        T element;          // The data in the node
        AvlNode<T> left;    // Left child
        AvlNode<T> right;   // Right child
        int height;         // Height of the node
        int frequency;    // Frequency of the element

        /**
         * Constructs a node with the specified element and no children.
         * @param element the element to store in this node.
         */
        AvlNode(T element) {
            this(element, null, null);
        }

        /**
         * Constructs a node with the specified element, left child, and right child.
         * @param element the element to store in this node.
         * @param left the left child of this node.
         * @param right the right child of this node.
         */
        AvlNode(T element, AvlNode<T> left, AvlNode<T> right) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.height = 0;
            this.frequency = 1;
        }
    }
}