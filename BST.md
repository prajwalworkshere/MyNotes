# Binary Search Tree
This code is implementing a **Binary Search Tree (BST) in Java**. I’ll teach it like we’re sitting together and building the tree from scratch—first the idea, then the code, then a dry run with diagrams.

The file has three major parts:

1. `Node` → represents one tree node
2. `BasicIntTree` → contains all BST operations
3. `BasicIntTreeDemo` → creates a tree and tests those operations 

---

# 🌳 1. First understand what a Binary Search Tree is

Imagine we insert these numbers:

```text
50
30
70
20
40
60
80
```

A BST arranges them like this:

```text
                50
              /    \
            30      70
           /  \    /  \
         20   40  60   80
```

The **BST rule** is:

```text
LEFT < ROOT < RIGHT
```

For every node:

```text
             50
           /    \
       smaller  greater
```

So:

```text
20 < 30 < 40 < 50 < 60 < 70 < 80
```

This rule is what makes searching efficient.

---

# 🧱 2. Imports

Your code starts with:

```java
import java.util.ArrayDeque;
import java.util.Deque;
```

These are used later for **iterative inorder traversal**.

Think of `Deque` as a data structure that we are using like a **stack**.

We'll come back to this.

---

# 🧩 3. The `Node` class

Now the important part:

```java
class BasicIntTree {
    private static class Node {
        int data;
        Node left;
        Node right;

        Node(int data) {
            this.data = data;
        }
    }
```

Let's understand this very slowly.

## What is a Node?

A node is basically a box:

```text
+----------------------+
|       Node           |
|                      |
|  data                |
|  left  ---> Node     |
|  right ---> Node     |
+----------------------+
```

In your code:

```java
int data;
Node left;
Node right;
```

means every node contains:

```text
data  → value
left  → reference to left child
right → reference to right child
```

For example:

```text
       +---------+
       |   50    |
       |         |
       | left    |-----> 30
       | right   |-----> 70
       +---------+
```

---

# 🧠 4. Why is `left` of type `Node`?

This may look strange:

```java
Node left;
Node right;
```

You might ask:

> "Why is a Node storing another Node?"

Because trees are made of nodes connected to other nodes.

For example:

```text
        50
       /  \
     30    70
```

The node `50` needs to remember:

```text
left  → 30
right → 70
```

So:

```java
Node left;
Node right;
```

are references to other nodes.

---

# 🏗️ 5. Node constructor

```java
Node(int data) {
    this.data = data;
}
```

If we do:

```java
new Node(50)
```

we create:

```text
+---------+
|   50    |
|         |
| left=null
| right=null
+---------+
```

Initially:

```text
left  = null
right = null
```

because we haven't connected any children yet.

---

# 🌳 6. The root

Next:

```java
private Node root;
```

This is **very important**.

`root` stores the starting node of the entire tree.

Initially:

```text
root
  |
  ↓
null
```

After:

```java
tree.insert(50);
```

we get:

```text
root
 |
 ↓
50
```

After more insertions:

```text
        root
          |
          ↓
          50
         /  \
       30    70
```

So remember:

> **Root is the first/top node from which we access the entire tree.**

---

# 🌱 7. INSERT

Now let's understand:

```java
public void insert(int value) {
    root = insertRecursive(root, value);
}
```

This is the public method that the user calls.

For example:

```java
tree.insert(50);
```

Internally:

```text
insert(50)
   ↓
insertRecursive(root, 50)
```

The interesting logic is here:

```java
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
```

Let's dry-run it.

---

# 🟢 Insert 50

Initially:

```text
root = null
```

Call:

```java
tree.insert(50);
```

Then:

```java
insertRecursive(null, 50)
```

Check:

```java
if (current == null)
```

YES.

So:

```java
return new Node(50);
```

That new node becomes root because:

```java
root = insertRecursive(root, value);
```

Now:

```text
       root
         |
         ↓
        50
```

---

# 🟢 Insert 30

Now:

```java
tree.insert(30);
```

We start at:

```text
       50
```

Check:

```java
30 < 50
```

YES.

Therefore:

```java
current.left = insertRecursive(current.left, 30);
```

`current.left` is currently `null`.

So:

```text
insertRecursive(null, 30)
```

creates:

```text
       50
      /
    30
```

---

# 🟢 Insert 70

```java
tree.insert(70);
```

Start:

```text
      50
```

Check:

```text
70 < 50 ? NO
70 > 50 ? YES
```

Therefore go right.

```text
       50
      /  \
    30    70
```

---

# 🟢 Insert 20

Start:

```text
        50
       /
     30
```

Compare:

```text
20 < 50
```

go left.

Now at `30`:

```text
20 < 30
```

go left.

There is nothing there:

```text
null
```

Create `20`.

Result:

```text
        50
       /  \
     30    70
    /
   20
```

---

# 🟢 Insert 40

Compare:

```text
40 < 50 → LEFT
40 > 30 → RIGHT
```

Result:

```text
        50
       /  \
     30    70
    / \
   20 40
```

---

# 🟢 Insert 60

```text
60 < 50 ? NO
60 > 50 ? YES
```

Go right.

At 70:

```text
60 < 70
```

Go left.

```text
        50
       /  \
     30    70
    / \    /
   20 40  60
```

---

# 🟢 Insert 80

```text
80 > 50
80 > 70
```

So:

```text
        50
       /  \
     30    70
    / \    / \
   20 40  60 80
```

This is our final tree.

---

# 🔍 8. SEARCH

Your code:

```java
public boolean search(int value) {
    return searchRecursive(root, value);
}
```

The actual logic:

```java
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
```

The key idea:

> **Don't search everywhere. Use the BST rule.**

Suppose:

```java
tree.search(40);
```

Start:

```text
        50
```

Is:

```text
40 == 50 ? NO
```

Is:

```text
40 < 50 ? YES
```

Go LEFT:

```text
       30
```

Now:

```text
40 == 30 ? NO
40 < 30 ? NO
```

Therefore go RIGHT:

```text
       40
```

Now:

```text
40 == 40
```

Return:

```java
true
```

So:

```text
Search 40: true
```

---

# ❌ Search 90

Start:

```text
50
```

```text
90 > 50 → right
```

At:

```text
70
```

```text
90 > 70 → right
```

At:

```text
80
```

```text
90 > 80 → right
```

But:

```text
80.right = null
```

Therefore:

```java
if (current == null)
    return false;
```

Result:

```text
Search 90: false
```

---

# 🗑️ 9. DELETE

This is the most important/complex operation.

Your code handles **three deletion cases**. 

### Case 1 — Leaf

A node with no children:

```text
    20
```

Delete it:

```text
    null
```

---

### Case 2 — One child

Example:

```text
     30
       \
        40
```

Delete `30`.

We replace it with its child:

```text
     40
```

---

### Case 3 — Two children

Example:

```text
       50
      /  \
    30    70
         /  \
        60   80
```

If we delete `70`, we can't simply remove it.

We find its **inorder successor**.

The successor is the smallest value in the right subtree.

For `70`:

```text
       70
      /  \
    60    80
```

Right subtree:

```text
80
```

Minimum = `80`.

So replace:

```text
70 → 80
```

Then delete the original `80`.

The implementation does this with `minValue(current.right)` and then recursively removes that successor. 

---

# 🔎 10. `minValue()`

```java
private int minValue(Node node) {
    Node current = node;

    while (current.left != null) {
        current = current.left;
    }

    return current.data;
}
```

BST rule:

```text
smaller values → LEFT
```

Therefore minimum value is always the **leftmost node**.

For:

```text
        50
       /
     30
    /
   20
```

Keep moving left:

```text
50
 ↓
30
 ↓
20
```

No more left:

```text
20.left == null
```

Therefore:

```text
minimum = 20
```

---

# 📈 11. `min()`

Your public method:

```java
public int min()
```

does basically the same thing, but starts from `root`.

```java
Node current = root;

while (current.left != null) {
    current = current.left;
}

return current.data;
```

So:

```text
        50
       /  \
     30    70
    / \
   20 40
```

Leftmost:

```text
50 → 30 → 20
```

Answer:

```text
20
```

---

# 📉 12. `max()`

Maximum is the opposite.

BST rule:

```text
larger values → RIGHT
```

So:

```java
while (current.right != null) {
    current = current.right;
}
```

For:

```text
        50
       /  \
     30    70
          / \
         60 80
```

Go:

```text
50
 ↓
70
 ↓
80
```

Therefore:

```text
maximum = 80
```

---

# 📏 13. HEIGHT

Your code:

```java
public int height() {
    return heightRecursive(root);
}
```

Then:

```java
private int heightRecursive(Node current) {
    if (current == null) {
        return -1;
    }

    int leftHeight = heightRecursive(current.left);
    int rightHeight = heightRecursive(current.right);

    return 1 + Math.max(leftHeight, rightHeight);
}
```

This calculates the **longest path from root to a leaf in edges**. Your code specifically defines:

```text
empty tree = -1
one-node tree = 0
```



For your tree:

```text
                50
              /    \
            30      70
           /  \    /  \
         20   40  60   80
```

Longest path:

```text
50 → 30 → 20
```

That's:

```text
2 edges
```

So:

```text
height = 2
```

---

# 📍 14. DEPTH

Depth asks:

> **How far is this node from the root?**

Root has depth:

```text
0
```

Your code starts:

```java
int depth = 0;
```



For:

```text
                50
              /    \
            30      70
           /  \    /  \
         20   40  60   80
```

Depths:

```text
50 → 0

30 → 1
70 → 1

20 → 2
40 → 2
60 → 2
80 → 2
```

So:

```java
tree.depth(60)
```

returns:

```text
2
```

But:

```java
tree.depth(90)
```

returns:

```text
-1
```

because `90` doesn't exist.

---

# 🔄 15. INORDER TRAVERSAL

This is one of the most important tree concepts.

Your code:

```java
inorderRecursive(current.left);
System.out.print(current.data + " ");
inorderRecursive(current.right);
```

The rule is:

```text
LEFT → ROOT → RIGHT
```

Remember:

> **LNR = Left Node Right**

For:

```text
        50
       /  \
     30    70
    / \    / \
   20 40  60 80
```

Visit:

```text
20
30
40
50
60
70
80
```

Output:

```text
20 30 40 50 60 70 80
```

And this is a special property of BST:

> **Inorder traversal gives values in ascending order.**

The recursive implementation is in lines 185–192. 

---

# 🔄 16. Iterative Inorder

You also have:

```java
public void inorderIterative()
```

Instead of recursion, it uses:

```java
Deque<Node> stack = new ArrayDeque<>();
```

Why?

Because recursion internally uses a **call stack**.

This version creates the stack manually.

The important section is:

```java
while (current != null) {
    stack.push(current);
    current = current.left;
}
```

It keeps going left:

```text
50
 ↓
30
 ↓
20
 ↓
null
```

Stack becomes:

```text
TOP
20
30
50
```

Then:

```java
current = stack.pop();
```

takes:

```text
20
```

Print:

```text
20
```

Then move right.

This simulates the recursive inorder process. 

---

# 🔵 17. PREORDER

Preorder rule:

```text
ROOT → LEFT → RIGHT
```

Your code:

```java
System.out.print(current.data + " ");
preorderRecursive(current.left);
preorderRecursive(current.right);
```

For:

```text
        50
       /  \
     30    70
    / \    / \
   20 40  60 80
```

Visit:

```text
50
30
20
40
70
60
80
```

Output:

```text
50 30 20 40 70 60 80
```



---

# 🟣 18. POSTORDER

Postorder rule:

```text
LEFT → RIGHT → ROOT
```

For our tree:

```text
        50
       /  \
     30    70
    / \    / \
   20 40  60 80
```

Visit:

```text
20
40
30
60
80
70
50
```

Output:

```text
20 40 30 60 80 70 50
```

The code follows exactly this order:

```java
postorderRecursive(current.left);
postorderRecursive(current.right);
System.out.print(current.data + " ");
```



---

# 🧪 19. Now let's understand `main()`

The main method starts:

```java
BasicIntTree tree = new BasicIntTree();
```

This creates our tree.

Initially:

```text
tree
 |
root
 |
null
```

Then:

```java
tree.insert(50);
tree.insert(30);
tree.insert(70);
tree.insert(20);
tree.insert(40);
tree.insert(60);
tree.insert(80);
```

creates:

```text
                50
              /    \
            30      70
           /  \    /  \
         20   40  60   80
```



Then the program tests all the operations.

---

# 🖥️ Expected output

The traversal section produces:

```text
Inorder traversal: 20 30 40 50 60 70 80
Iterative inorder traversal: 20 30 40 50 60 70 80
Preorder traversal: 50 30 20 40 70 60 80
Postorder traversal: 20 40 30 60 80 70 50
```

Then:

```text
Search 40: true
Search 90: false
Minimum value: 20
Maximum value: 80
Tree height: 2
Depth of 60: 2
Depth of 90: -1
```

Those calls are shown in the demo code. 

---

# 🧠 The whole code in ONE picture

This is the mental model I want you to remember:

```text
                 BasicIntTree
                      |
                      ↓
                    root
                      |
                      ↓
                     50
                   /    \
                 30      70
                /  \    /  \
              20   40  60   80
```

And each node is:

```text
        Node
   ┌─────────────┐
   │ data = 50   │
   │             │
   │ left  ──────┼────→ Node(30)
   │ right ──────┼────→ Node(70)
   └─────────────┘
```

---

# ⭐ The 8 things you should remember

| Operation  | Main idea                          |
| ---------- | ---------------------------------- |
| `insert()` | Smaller → left, larger → right     |
| `search()` | Compare and choose left/right      |
| `delete()` | Handle 0, 1, or 2 children         |
| `min()`    | Go completely left                 |
| `max()`    | Go completely right                |
| `height()` | Longest path downward              |
| `depth()`  | Distance from root                 |
| Traversals | Different orders of visiting nodes |

And traversal:

```text
INORDER    = LEFT → ROOT → RIGHT
PREORDER   = ROOT → LEFT → RIGHT
POSTORDER  = LEFT → RIGHT → ROOT
```

The **most important thing before memorizing this code** is to become comfortable with just these three ideas:

```text
1. What is a Node?
2. What does root point to?
3. How does LEFT/RIGHT maintain the BST rule?
```

Once those three are clear, almost the entire code becomes logical rather than something you have to memorize.
