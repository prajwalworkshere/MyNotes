# Trees - Basic Overview 
---
 ## 1. What is a Tree?

 A **tree** is a non-linear data structure made of **nodes** connected by **edges**.

 Unlike arrays or linked lists, which are usually linear:

```
Array:     A → B → C → D
```

 a tree can branch:

```
             A
           /   \
          B     C
        /  \     \
       D    E     F
```

 Here:

 - `A` is the **root**
- `B, C` are children of `A`
- `D, E` are children of `B`
- `F` is a child of `C`

---

 # 2\. Basic Tree Terminology

 These terms are extremely important.

 ### Node

 An individual element in a tree.

```
     A
```

 `A` is a node.

 ### Edge

 A connection between two nodes.

```
A ─── B
```

 The connection between `A` and `B` is an edge.

 ### Root

 The topmost node.

```
        A       ← Root
       / \
      B   C
```

 A tree has **exactly one root**.

 ### Parent

 A node directly above another node.

```
      A
     / \
    B   C
```

 `A` is the parent of `B` and `C`.

 ### Child

 A node directly below another node.

 `B` and `C` are children of `A`.

 ### Siblings

 Nodes having the same parent.

```
       A
     / | \
    B  C  D
```

 `B`, `C`, and `D` are siblings.

 ### Leaf / Terminal Node

 A node with **no children**.

```
       A
      / \
     B   C
        / \
       D   E
```

 Leaves are:

```
B, D, E
```

 ### Internal Node

 A node having at least one child.

 Here:

```
A, C
```

 are internal nodes.

---

 # 3\. Degree

 The **degree of a node** = number of children it has.

```
       A
     / | \
    B  C  D
```

 Degree of `A` = **3**

 Degree of `B` = **0**

 ### Degree of a tree

 The degree of a tree is the **maximum degree of any node**.

 Example:

```
          A
        / | \
       B  C  D
          |
          E
```

 Maximum degree = 3.

 Therefore, degree of the tree = **3**.

---

 # 4\. Path

 A **path** is a sequence of nodes connected by edges.

```
        A
       / \
      B   C
     / \
    D   E
```

 Path from `A` to `E`:

```
A → B → E
```

 Number of edges in this path = **2**.

---

 # 5\. Depth

 The **depth of a node** is the number of edges from the root to that node.

 Using:

```
        A
       / \
      B   C
     / \
    D   E
```

 We usually take:

```
Depth(A) = 0
Depth(B) = 1
Depth(C) = 1
Depth(D) = 2
Depth(E) = 2
```

 So:

 > **Depth = distance from root to node**

---

 # 6\. Height

 The **height of a node** is the number of edges on the longest path from that node down to a leaf.

```
        A
       / \
      B   C
     / \
    D   E
```

 For `D`:

```
D
```

 No edges downward.

```
Height(D) = 0
```

 For `B`:

```
B → D
```

 or

```
B → E
```

 So:

```
Height(B) = 1
```

 For `A`:

```
A → B → D
```

 has 2 edges.

 Therefore:

```
Height(A) = 2
```

 ### Important distinction

```
Depth → measured from root downward
Height → measured from node downward to deepest leaf
```

---

 # 7\. Subtree

 A node and all of its descendants form a **subtree**.

```
          A
        /   \
       B     C
      / \
     D   E
```

 The subtree rooted at `B` is:

```
       B
      / \
     D   E
```

---

 # 8\. Ancestor and Descendant

 Consider:

```
        A
       /
      B
     /
    C
   /
  D
```

 For `D`:

 Ancestors:

```
C, B, A
```

 Descendants of `B`:

```
C, D
```

 So:

 > Ancestor = node above you\
>  Descendant = node below you

---

 # 9\. Important Properties of Trees

 For a tree containing **N nodes**:

 ### Number of edges

```
Edges = N - 1
```

 Example:

```
5 nodes → 4 edges
10 nodes → 9 edges
100 nodes → 99 edges
```

 This is one of the most important tree properties.

 ### Why?

 The root starts with no incoming edge.

 Every other node needs exactly one edge connecting it to its parent.

 Therefore:

```
N - 1 edges
```

---

 # 10\. Types of Trees

 This is where trees become more interesting.

 The major types you should learn are:

```
Tree
│
├── General Tree
│
├── Binary Tree
│   ├── Full Binary Tree
│   ├── Complete Binary Tree
│   ├── Perfect Binary Tree
│   ├── Balanced Binary Tree
│   └── Degenerate Binary Tree
│
├── Binary Search Tree (BST)
│
├── AVL Tree
│
├── Red-Black Tree
│
├── Heap
│
├── B-Tree
└── Trie
```

 Let's understand these one by one.

---

 # 11\. General Tree

 A node can have **any number of children**.

```
             A
        /    |    \
       B     C     D
            / \
           E   F
```

 `A` has 3 children.

 `C` has 2.

 There is no fixed maximum.

---

 # 12. Binary Tree

 A **binary tree** is a tree where each node has **at most two children**.

 They are normally called:

```
left child
right child
```

 Example:

```
        A
       / \
      B   C
     / \
    D   E
```

 Each node can have:

```
0 children
1 child
2 children
```

 but never more than 2.

---

 # 13\. Full Binary Tree

 A full binary tree has a very specific rule:

 > Every node has either **0 or 2 children**.

 Example:

```
          A
        /   \
       B     C
      / \   / \
     D   E F   G
```

 Every node has either:

```
0 children
```

 or

```
2 children
```

 So this is a **full binary tree**.

 This is NOT full:

```
      A
     /
    B
```

 because `A` has exactly one child.

---

 # 14\. Complete Binary Tree

 A complete binary tree is filled:

 1. Level by level
2. From left to right

 Example:

```
        A
       / \
      B   C
     / \  /
    D  E F
```

 This is complete.

 The final level doesn't have to be completely full, but its nodes must be as far **left as possible**.

 This concept is extremely important for **heaps**.

---

 # 15\. Perfect Binary Tree

 A perfect binary tree has:

 - Every internal node has exactly 2 children
- Every leaf is at the same level

 Example:

```
          A
        /   \
       B     C
      / \   / \
     D   E F   G
```

 All leaves are at the same level.

 For height `h`, the maximum number of nodes is:

```
2^(h+1) - 1
```

 For example, height 2:

```
2^3 - 1 = 7
```

---

 # 16\. Balanced Binary Tree

 A balanced tree keeps its height relatively small.

 For example:

```
          A
        /   \
       B     C
      / \   / \
     D   E F   G
```

 is nicely balanced.

 Compare that with:

```
A
 \
  B
   \
    C
     \
      D
       \
        E
```

 The second tree behaves almost like a linked list.

 Balanced trees are important because operations can often remain around:

```
O(log n)
```

 instead of becoming:

```
O(n)
```

---

 # 17\. Degenerate / Skewed Tree

 A tree where nodes essentially form a chain.

 ### Right-skewed

```
A
 \
  B
   \
    C
     \
      D
```

 ### Left-skewed

```
      A
     /
    B
   /
  C
 /
D
```

 This can make tree operations inefficient.

---

 # 18\. Binary Search Tree (BST)

 A BST is a binary tree with an ordering rule:

```
Left subtree < Root < Right subtree
```

 Example:

```
        50
       /  \
     30    70
    / \    / \
   20 40  60 80
```

 For `50`:

```
left  → values smaller than 50
right → values greater than 50
```

 For `30`:

```
left  → 20
right → 40
```

 This ordering allows efficient searching when the tree is balanced.

---

 # 19\. Searching in a BST

 Suppose we want to find `60`.

```
        50
       /  \
     30    70
          /  \
         60   80
```

 Start at 50.

```
60 > 50
```

 Go right.

 At 70:

```
60 < 70
```

 Go left.

 We find:

```
60
```

 Instead of checking every node, we eliminate entire subtrees.

---

 # 20\. BST Time Complexity

 For a balanced BST:

 | Operation | Average/Typical balanced case |
| --- | --- |
| Search | O(log n) |
| Insert | O(log n) |
| Delete | O(log n) |

But a badly skewed BST can become:

```
A
 \
  B
   \
    C
     \
      D
```

 Then:

```
Search = O(n)
```

 This is why **self-balancing trees** exist.

---

 # 21\. Tree Traversals

 Traversal means:

 > Visiting every node in a particular order.

 The four important traversals are:

```
1. Preorder
2. Inorder
3. Postorder
4. Level Order
```

 Consider:

```
          A
        /   \
       B     C
      / \   / \
     D   E F   G
```

---

 ## Preorder

 Rule:

```
Root → Left → Right
```

 Therefore:

```
A B D E C F G
```

 Memory trick:

 > **Pre** = root comes **before** children.

---

 ## Inorder

 Rule:

```
Left → Root → Right
```

 Therefore:

```
D B E A F C G
```

 Very important:

 > **Inorder traversal of a BST gives values in sorted order.**

 Example BST:

```
        50
       /  \
     30    70
    / \    / \
   20 40  60 80
```

 Inorder:

```
20 30 40 50 60 70 80
```

---

 ## Postorder

 Rule:

```
Left → Right → Root
```

 Result:

```
D E B F G C A
```

 Memory trick:

 > **Post** = root comes **after** children.

---

 ## Level Order

 Visit level by level:

```
A
B C
D E F G
```

 Result:

```
A B C D E F G
```

 This typically uses a **queue**.

---

 # 22\. Traversal Cheat Sheet

 For:

```
        A
       / \
      B   C
     / \
    D   E
```

 | Traversal | Rule | Result |
| --- | --- | --- |
| Preorder | Root Left Right | A B D E C |
| Inorder | Left Root Right | D B E A C |
| Postorder | Left Right Root | D E B C A |
| Level Order | Level by level | A B C D E |

Memorize these three:

```
PREORDER   = ROOT LEFT RIGHT
INORDER    = LEFT ROOT RIGHT
POSTORDER  = LEFT RIGHT ROOT
```

---

 # 23\. Why Recursion Is Common in Trees

 Trees are naturally recursive.

 A tree can be viewed as:

```
        Root
       /    \
    subtree subtree
```

 Each subtree is itself a tree.

 That's why recursive code is often natural.

 For example, conceptually:

```
traverse(node):
    if node is empty:
        return

    traverse(node.left)
    process(node)
    traverse(node.right)
```

 This is inorder traversal.

---

 # 24\. Heap

 A **heap** is a special type of complete binary tree.

 Two major types:

 ### Min Heap

 Parent ≤ children.

```
        10
       /  \
      20   30
     / \
    40 50
```

 Smallest element is at the root.

 ### Max Heap

 Parent ≥ children.

```
        50
       /  \
      30   40
     / \
    10 20
```

 Largest element is at the root.

 Heaps are commonly used to implement **priority queues**.

---

 # 25\. Heap vs BST

 This distinction is frequently tested.

 ### BST

 Main purpose:

```
efficient searching
```

 Ordering:

```
left < root < right
```

 ### Heap

 Main purpose:

```
efficient access to minimum/maximum
```

 Ordering:

```
parent ≤ children    (min heap)
```

 or

```
parent ≥ children    (max heap)
```

 A heap is **not** necessarily globally sorted.

---

 # 26\. AVL Tree

 An **AVL tree** is a self-balancing BST.

 For each node, the balance factor is:

```
height(left subtree) - height(right subtree)
```

 For an AVL tree, it must be:

```
-1, 0, or +1
```

 If it becomes unbalanced, rotations are performed.

 Example:

```
    30
   /
  20
 /
10
```

 This is unbalanced.

 A rotation can transform it into:

```
    20
   /  \
  10   30
```

 Now it is balanced.

---

 # 27\. Rotations

 Rotations are operations used to change tree structure while preserving certain ordering properties.

 Main types:

```
Left Rotation
Right Rotation
Left-Right Rotation
Right-Left Rotation
```

 They are important in:

 - AVL trees
- Red-Black trees
- self-balancing BSTs

---

 # 28\. Red-Black Tree

 A Red-Black Tree is another **self-balancing BST**.

 Each node has a color:

```
RED
BLACK
```

 It follows several structural/color rules that keep the tree approximately balanced.

 You don't need to memorize every rule immediately.

 The important idea is:

 > **AVL and Red-Black trees use balancing mechanisms to keep BST operations efficient.**

---

 # 29\. Trie

 A **Trie** is a tree commonly used for strings.

 Suppose we store:

```
cat
car
can
```

 They can share prefixes:

```
        c
        |
        a
      / | \
     t  r  n
```

 This makes tries useful for:

 - autocomplete
- dictionary lookup
- prefix searching
- spell checking

---

 # 30\. B-Tree

 A B-Tree is a balanced multi-way search tree.

 Unlike a binary tree:

```
one node → at most 2 children
```

 a B-Tree can have many children.

 They are particularly important in:

 - databases
- file systems
- storage systems

 because they are designed to work efficiently with block-based storage.

---

 # 31\. The Big Picture

 You can organize the concepts like this:

```
                         TREE
                           |
          ┌────────────────┴────────────────┐
          |                                 |
     General Tree                       Binary Tree
                                            |
                    ┌───────────────────────┼───────────────────┐
                    |                       |                   |
                 Binary                  BST                  Heap
               structures                |                    |
                                        AVL              Min / Max
                                         |
                                    Red-Black
```

 And separately:

```
String-focused → Trie

Database/storage → B-Tree
```

---

 # 32\. What You Should Memorize First

 Don't try to memorize everything at once. First master these:

 ### Level 1 — Fundamentals

 - Node
- Edge
- Root
- Parent
- Child
- Sibling
- Leaf
- Degree
- Depth
- Height
- Subtree
- Ancestor
- Descendant

 ### Level 2 — Binary Trees

 - Binary tree
- Full binary tree
- Complete binary tree
- Perfect binary tree
- Balanced tree
- Skewed tree

 ### Level 3 — Traversals

```
Preorder  = Root Left Right
Inorder   = Left Root Right
Postorder = Left Right Root
Level     = Level by level
```

 ### Level 4 — BST

 Understand:

```
Left < Root < Right
```

 and why balanced BSTs can give:

```
O(log n)
```

 operations.

 ### Level 5 — Advanced Trees

 Then learn:

```
AVL
Red-Black
Heap
Trie
B-Tree
```

---

 ## A very useful mental model

 Think of trees as **hierarchies**.

```
Company
│
├── Engineering
│   ├── Backend
│   └── Frontend
│
├── Sales
│   ├── India
│   └── USA
│
└── HR
```

 This is naturally represented as a tree.

 Then algorithms add rules to that hierarchy:

```
Binary Tree → max 2 children
BST         → ordered binary tree
Heap        → priority-oriented tree
AVL         → balanced BST
Trie        → prefix-oriented tree
B-Tree      → multi-way balanced search tree
```

 **Next, the most useful progression is:** `Binary Tree → traversals → BST → insertion/deletion → AVL rotations → Heap`. That sequence makes the later topics much easier to understand.

---
# Recursion
---

 ## 1\. First, what is recursion?

 Recursion means a function **calls itself** to solve a smaller version of the same problem.

 Simple example:

```
function f(n):
    if n == 0:
        return

    print(n)
    f(n - 1)
```

 If we call:

```
f(3)
```

 the calls are:

```
f(3)
 ↓
f(2)
 ↓
f(1)
 ↓
f(0)
```

 The important part is the **base case**:

```
if n == 0:
    return
```

 Without a base case, recursion would continue indefinitely.

---

 # 2\. Why recursion works so naturally with trees

 Consider this tree:

```
          A
        /   \
       B     C
      / \
     D   E
```

 Look at the subtree rooted at `B`:

```
       B
      / \
     D   E
```

 That is itself a tree.

 And the subtree rooted at `D`:

```
D
```

 is also a tree.

 So we can think of a tree as:

```
Tree
 ├── Root
 ├── Left Subtree
 └── Right Subtree
```

 This is exactly the kind of structure recursion handles well.

---

 # 3\. The basic tree-recursion pattern

 Suppose each node has:

```
node
node.left
node.right
```

 A very common recursive pattern is:

```
function solve(node):

    if node == null:
        return

    solve(node.left)

    solve(node.right)
```

 Think about what happens:

```
          A
        /   \
       B     C
      / \
     D   E
```

 Calling:

```
solve(A)
```

 means:

```
solve(A)
   |
   ├── solve(B)
   │     |
   │     ├── solve(D)
   │     └── solve(E)
   │
   └── solve(C)
```

 The function keeps going down until it reaches:

```
null
```

 That is our **base case**.

---

 # 4\. The most important example: tree traversal

 Let's use:

```
          A
        /   \
       B     C
      / \
     D   E
```

 ## Preorder

 Preorder means:

```
Root → Left → Right
```

 Recursive code:

```
preorder(node):
    if node == null:
        return

    print(node)
    preorder(node.left)
    preorder(node.right)
```

 Let's trace it.

 Start:

```
preorder(A)
```

 First print `A`:

```
A
```

 Then go left:

```
preorder(B)
```

 Print `B`:

```
A B
```

 Go left:

```
preorder(D)
```

 Print:

```
A B D
```

 D has no children, so:

```
preorder(null)
```

 returns.

 Then right of B:

```
preorder(E)
```

 Print:

```
A B D E
```

 Then return to A and go right:

```
preorder(C)
```

 Print:

```
A B D E C
```

 Final result:

```
A B D E C
```

---

 # 5\. Understand the "return" — this is the confusing part

 This is where beginners usually struggle.

 Consider:

```
          A
        /   \
       B     C
      / \
     D   E
```

 When we call:

```
preorder(A)
```

 we go to:

```
preorder(B)
```

 Then:

```
preorder(D)
```

 Then D finishes.

 Where do we go?

 **Back to B.**

 Why?

 Because the call to:

```
preorder(D)
```

 was made by:

```
preorder(B)
```

 So after D finishes, execution continues inside B.

 Then B calls:

```
preorder(E)
```

 After E finishes, we return to B.

 Then B finishes, so we return to A.

 Then A processes C.

---

 # 6\. Think of recursion as a stack

 This is extremely important.

 When you call:

```
preorder(A)
```

 the call is placed on the call stack.

```
A
```

 Then:

```
preorder(B)
```

```
B
A
```

 Then:

```
preorder(D)
```

```
D
B
A
```

 D finishes:

```
B
A
```

 B continues.

 Then E:

```
E
B
A
```

 E finishes:

```
B
A
```

 B finishes:

```
A
```

 Then C:

```
C
A
```

 C finishes:

```
A
```

 A finishes:

```
empty
```

 This is why tree recursion naturally uses the **call stack**.

---

 # 7\. Inorder recursion

 Inorder is:

```
Left → Root → Right
```

 So:

```
inorder(node):
    if node == null:
        return

    inorder(node.left)
    print(node)
    inorder(node.right)
```

 Notice where `print(node)` is.

```
inorder(left)
↓
print(node)
↓
inorder(right)
```

 For:

```
          A
        /   \
       B     C
      / \
     D   E
```

 we get:

```
D B E A C
```

---

 # 8\. Postorder recursion

 Postorder is:

```
Left → Right → Root
```

 Code:

```
postorder(node):
    if node == null:
        return

    postorder(node.left)
    postorder(node.right)
    print(node)
```

 Notice:

```
left
right
print
```

 Result:

```
D E B C A
```

---

 # 9\. The magic pattern

 You can remember all three using one function:

```
function traverse(node):

    if node == null:
        return

    LEFT

    PROCESS NODE

    RIGHT
```

 Then:

 ### Preorder

```
PROCESS
LEFT
RIGHT
```

 ### Inorder

```
LEFT
PROCESS
RIGHT
```

 ### Postorder

```
LEFT
RIGHT
PROCESS
```

 That's the core idea.

---

 # 10\. Recursion for finding the number of nodes

 Suppose:

```
          A
        /   \
       B     C
      / \
     D   E
```

 We want the number of nodes.

 Think recursively:

```
number of nodes =
1 + nodes in left subtree + nodes in right subtree
```

 So:

```
count(node):
    if node == null:
        return 0

    return 1 + count(node.left) + count(node.right)
```

 For A:

```
count(A)
= 1 + count(B) + count(C)
```

 B:

```
count(B)
= 1 + count(D) + count(E)
```

 D:

```
count(D)
= 1
```

 E:

```
count(E)
= 1
```

 C:

```
count(C)
= 1
```

 Therefore:

```
count(A) = 5
```

---

 # 11\. Recursion for finding tree height

 This is another very important problem.

```
height(node):
    if node == null:
        return -1

    return 1 + max(
        height(node.left),
        height(node.right)
    )
```

 Why `max`?

 Because we want the **longest path** from the node to a leaf.

 For:

```
          A
        /   \
       B     C
      / \
     D   E
```

 We calculate:

```
height(A)
=
1 + max(height(B), height(C))
```

 `B` has height 1.

 `C` has height 0.

 Therefore:

```
height(A)
= 1 + max(1, 0)
= 2
```

---

 # 12\. The three questions to ask when solving recursive tree problems

 Whenever you see a tree recursion problem, ask:

 ### Question 1: What is my base case?

 Usually:

```
if node == null:
    return ...
```

 ### Question 2: What should I do with the current node?

 For example:

```
print(node)
```

 or:

```
return node.value
```

 ### Question 3: How do I solve the left and right subtrees?

 Usually:

```
solve(node.left)
solve(node.right)
```

 Then combine their results.

---

 # 13\. A very powerful way to think

 Don't try to mentally execute the entire tree.

 Instead, assume:

 > **"My recursive function already knows how to solve a subtree."**

 For example:

```
count(node):
    return 1 + count(node.left) + count(node.right)
```

 Don't think:

 > "How does `count(B)` work? How does `count(D)` work?"

 Just trust:

```
count(left) → gives number of nodes in left subtree
count(right) → gives number of nodes in right subtree
```

 Then your current node does:

```
1 + left + right
```

 This way of thinking is called **recursive decomposition**.

---

 # 14\. The golden formula

 For many binary-tree problems:

```
answer for current node
=
something involving
answer(left subtree)
+
answer(right subtree)
```

 For example:

 ### Count nodes

```
count = 1 + left + right
```

 ### Sum of nodes

```
sum = node.value + left + right
```

 ### Height

```
height = 1 + max(left, right)
```

 ### Minimum/maximum

 Often:

```
answer = combine(
    current node,
    left answer,
    right answer
)
```

 Once you understand this pattern, a **huge number of tree recursion problems become much easier**.

---

 ## One thing to master right now

 Take this tree:

```
              1
            /   \
           2     3
          / \     \
         4   5     6
```

 Try to manually determine:

```
1. Preorder
2. Inorder
3. Postorder
4. Level order
5. Number of nodes
6. Height
```

 The key is not memorizing answers—**trace where the recursive call goes and where it returns**. That is the foundation for essentially every recursive tree algorithm.
