import java.util.ArrayDeque;
import java.util.Deque;

class BasicIntTree {
    private static class Node {
        int data;
        Node left;
        Node right;

        Node(int data) {
            this.data = data;
        }
    }

    private Node root;

    public void insert(int value) {
        root = insertRecursive(root, value);
    }

    private Node insertRecursive(Node current, int value) {
        if (current == null) {
            return new Node(value);
        }

        if (value < current.data) {
            current.left = insertRecursive(current.left, value);
        } else if (value > current.data) {
            current.right = insertRecursive(current.right, value);
        }

        return current;
    }

    public boolean search(int value) {
        return searchRecursive(root, value);
    }

    private boolean searchRecursive(Node current, int value) {
        if (current == null) {
            return false;
        }

        if (current.data == value) {
            return true;
        }

        if (value < current.data) {
            return searchRecursive(current.left, value);
        }

        return searchRecursive(current.right, value);
    }

    /**
     * Deletes a value from the BST if it exists.
     *
     * Deletion cases:
     * 1) Leaf node: remove directly.
     * 2) One child: replace node with its child.
     * 3) Two children: replace with inorder successor, then delete successor.
     */
    public void delete(int value) {
        root = deleteRecursive(root, value);
    }

    private Node deleteRecursive(Node current, int value) {
        if (current == null) {
            return null;
        }
        if (value < current.data) {
            current.left = deleteRecursive(current.left, value);
            return current;
        }
        if (value > current.data) {
            current.right = deleteRecursive(current.right, value);
            return current;
        }
        if (current.left == null && current.right == null) {
            return null;
        }
        if (current.left == null) {
            return current.right;
        }
        if (current.right == null) {
            return current.left;
        }

        int successorValue = minValue(current.right);
        current.data = successorValue;
        current.right = deleteRecursive(current.right, successorValue);
        return current;
    }

    private int minValue(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current.data;
    }

    /**
     * Returns the minimum value in the BST.
     * Throws IllegalStateException when the tree is empty.
     */
    public int min() {
        if (root == null) {
            throw new IllegalStateException("Tree is empty");
        }
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.data;
    }

    /**
     * Returns the maximum value in the BST.
     * Throws IllegalStateException when the tree is empty.
     */
    public int max() {
        if (root == null) {
            throw new IllegalStateException("Tree is empty");
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.data;
    }

    /**
     * Returns the height of the tree.
     * Height is measured in edges: empty tree = -1, single node tree = 0.
     */
    public int height() {
        return heightRecursive(root);
    }

    private int heightRecursive(Node current) {
        if (current == null) {
            return -1;
        }
        int leftHeight = heightRecursive(current.left);
        int rightHeight = heightRecursive(current.right);
        return 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * Returns depth of a value from the root.
     * Root depth = 0. Returns -1 if value is not found.
     */
    public int depth(int value) {
        Node current = root;
        int depth = 0;

        while (current != null) {
            if (value == current.data) {
                return depth;
            }

            if (value < current.data) {
                current = current.left;
            } else {
                current = current.right;
            }
            depth++;
        }

        return -1;
    }

    /**
     * Prints the tree using inorder traversal.
     *
     * Traversal technique: left subtree, current node, right subtree.
     * For a BST, this visits values in ascending order.
     */
    public void inorder() {
        inorderRecursive(root);
        System.out.println();
    }

    private void inorderRecursive(Node current) {
        if (current == null) {
            return;
        }

        inorderRecursive(current.left);
        System.out.print(current.data + " ");
        inorderRecursive(current.right);
    }

    /**
     * Prints the tree using iterative inorder traversal.
     *
     * Uses an explicit stack to simulate the recursive call stack.
     */
    public void inorderIterative() {
        Deque<Node> stack = new ArrayDeque<>();
        Node current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            System.out.print(current.data + " ");
            current = current.right;
        }

        System.out.println();
    }

    /**
     * Prints the tree using preorder traversal.
     *
     * Traversal technique: current node, left subtree, right subtree.
     * This is useful for showing the tree structure or copying the tree.
     */
    public void preorder() {
        preorderRecursive(root);
        System.out.println();
    }

    private void preorderRecursive(Node current) {
        if (current == null) {
            return;
        }

        System.out.print(current.data + " ");
        preorderRecursive(current.left);
        preorderRecursive(current.right);
    }

    /**
     * Prints the tree using postorder traversal.
     *
     * Traversal technique: left subtree, right subtree, current node.
     * This is useful for deleting or processing children before the parent.
     */
    public void postorder() {
        postorderRecursive(root);
        System.out.println();
    }

    private void postorderRecursive(Node current) {
        if (current == null) {
            return;
        }

        postorderRecursive(current.left);
        postorderRecursive(current.right);
        System.out.print(current.data + " ");
    }
}

public class BasicIntTreeDemo {
    public static void main(String[] args) {
        BasicIntTree tree = new BasicIntTree();

        tree.insert(50); // root
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);

        System.out.print("Inorder traversal: ");
        tree.inorder();

        System.out.print("Iterative inorder traversal: ");
        tree.inorderIterative();

        System.out.print("Preorder traversal: ");
        tree.preorder();

        System.out.print("Postorder traversal: ");
        tree.postorder();

        int key = 40;
        System.out.println("Search " + key + ": " + tree.search(key));

        key = 90;
        System.out.println("Search " + key + ": " + tree.search(key));

        System.out.println("Minimum value: " + tree.min());
        System.out.println("Maximum value: " + tree.max());
        System.out.println("Tree height: " + tree.height());

        key = 60;
        System.out.println("Depth of " + key + ": " + tree.depth(key));

        key = 90;
        System.out.println("Depth of " + key + ": " + tree.depth(key));
    }
}        
//         15
//        /  \
//      10    20
//     /     /
//    8     16
//   /
//  4
