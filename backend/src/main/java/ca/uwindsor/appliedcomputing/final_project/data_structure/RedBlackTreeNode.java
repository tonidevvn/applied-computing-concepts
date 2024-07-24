package ca.uwindsor.appliedcomputing.final_project.data_structure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedBlackTreeNode {
    String key;
    int count;
    RedBlackTreeNode left, right, parent;
    boolean isRed;

    RedBlackTreeNode(String key) {
        this.key = key;
        this.count = 1;
        this.isRed = true; // New nodes are initially red
        this.left = this.right = this.parent = null;
    }
}