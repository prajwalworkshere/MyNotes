# 🌳 ]Binary Search Tree (BST) Deletion — Complete Notes

## 1. What is BST Deletion?

**BST deletion** means removing a particular node/value from a Binary Search Tree while maintaining the **BST property**.

### BST Property

For every node:

```text
Left subtree  <  Node  <  Right subtree
```

Example:

```text
             15
            /  \
          10    20
         /     /
        8     16
       /
      4
```

Here:

* Everything on the left of `15` is smaller than `15`.
* Everything on the right of `15` is greater than `15`.
* The same rule applies recursively to every node.

---

# 2. BST Delete Code

```java
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

    // Node found

    // Case 1: No children
    if (current.left == null && current.right == null) {
        return null;
    }

    // Case 2: Only right child
    if (current.left == null) {
        return current.right;
    }

    // Case 2: Only left child
    if (current.right == null) {
        return current.left;
    }

    // Case 3: Two children
    int successorValue = minValue(current.right);

    current.data = successorValue;

    current.right = deleteRecursive(
        current.right,
        successorValue
    );

    return current;
}

private int minValue(Node node) {

    Node current = node;

    while (current.left != null) {
        current = current.left;
    }

    return current.data;
}
```

---

# 3. The Three Functions

There are three important functions:

```text
delete()
    ↓
deleteRecursive()
    ↓
minValue()
```

Their responsibilities are different.

| Function                          | Purpose                               |
| --------------------------------- | ------------------------------------- |
| `delete(value)`                   | Starts the deletion                   |
| `deleteRecursive(current, value)` | Searches and deletes the node         |
| `minValue(node)`                  | Finds the smallest value in a subtree |

---

# 4. `delete()` Function

```java
public void delete(int value) {
    root = deleteRecursive(root, value);
}
```

This is the function that the user calls.

For example:

```java
t.delete(10);
```

Internally:

```text
delete(10)
    ↓
deleteRecursive(root, 10)
```

---

## Why do we write:

```java
root = deleteRecursive(root, value);
```

instead of:

```java
deleteRecursive(root, value);
```

Because the **root itself might change**.

For example:

```text
       15
      /  \
    10    20
```

Suppose:

```java
delete(15);
```

The node `15` is the root.

After deletion, another node may become the root.

Therefore:

```java
root = deleteRecursive(root, value);
```

means:

> "Delete the value from the tree and give me back the node that should become the root."

---

# 5. The Most Important Idea

This is the most important concept in recursive BST deletion:

```java
deleteRecursive(current, value)
```

doesn't simply mean:

> "Delete something."

It means:

> **"Delete `value` from the subtree whose root is `current`, and return the node that should become the root of that subtree after deletion."**

This is why we have statements such as:

```java
current.left = deleteRecursive(current.left, value);
```

and:

```java
current.right = deleteRecursive(current.right, value);
```

and:

```java
return current;
```

---

# 6. Understand `current`

Suppose the tree is:

```text
             15
            /  \
          10    20
         /
        8
       /
      4
```

When recursion starts:

```java
deleteRecursive(root, 4);
```

Since:

```text
root = 15
```

we get:

```text
current = 15
```

Then:

```java
deleteRecursive(current.left, 4);
```

Now:

```text
current = 10
```

Then:

```text
current = 8
```

Then:

```text
current = 4
```

So `current` changes for every recursive call.

```text
current = 15
     ↓
current = 10
     ↓
current = 8
     ↓
current = 4
```

---

# 7. Understand `value`

`value` is the value we want to delete.

Example:

```java
t.delete(4);
```

Then:

```text
value = 4
```

The recursive calls keep carrying the same value:

```text
current = 15, value = 4
current = 10, value = 4
current = 8,  value = 4
current = 4,  value = 4
```

---

# 8. Searching for the Node

The first part of the function is:

```java
if (current == null) {
    return null;
}
```

Then:

```java
if (value < current.data) {
    current.left = deleteRecursive(current.left, value);
    return current;
}
```

Then:

```java
if (value > current.data) {
    current.right = deleteRecursive(current.right, value);
    return current;
}
```

Finally, if neither condition is true:

```text
value == current.data
```

So we found the node.

---

# 9. Why Compare `<` and `>`?

Suppose:

```text
             15
            /  \
          10    20
```

We want to delete:

```text
10
```

At `15`:

```text
10 < 15
```

Therefore:

```java
current.left
```

We go left.

---

Suppose we want to delete:

```text
20
```

At `15`:

```text
20 > 15
```

Therefore:

```java
current.right
```

We go right.

---

Suppose:

```text
current.data = 10
value = 10
```

Then:

```text
value < current.data   → false

value > current.data   → false
```

Therefore:

```text
value == current.data
```

The node has been found.

---

# 10. Recursive Structure

The function follows this general pattern:

```text
             Current Node
                  |
        ┌─────────┴─────────┐
        ↓                   ↓
   value < current      value > current
        ↓                   ↓
      LEFT                RIGHT
        ↓                   ↓
      recurse             recurse
        ↓                   ↓
       return              return
```

If equal:

```text
value == current.data
          ↓
      Node found
          ↓
   Handle deletion case
```

---

# 11. The Three Deletion Cases

Once we find the node, there are exactly three important situations:

```text
CASE 1 → Node has 0 children

CASE 2 → Node has 1 child

CASE 3 → Node has 2 children
```

---

# 12. CASE 1 — Leaf Node

A leaf node has:

```text
0 children
```

Example:

```text
       8
      /
     4
```

Node `4` is a leaf.

Therefore:

```java
current.left == null
current.right == null
```

Code:

```java
if (current.left == null && current.right == null) {
    return null;
}
```

---

# 13. Why `return null`?

This is extremely important.

Suppose:

```text
       8
      /
     4
```

We delete `4`.

Before:

```text
8.left → 4
```

The recursive call for `4` does:

```java
return null;
```

The parent `8` receives that:

```java
8.left = deleteRecursive(4, 4);
```

So:

```text
8.left = null
```

Tree becomes:

```text
    8
   /
 null
```

or simply:

```text
    8
```

Therefore:

```java
return null;
```

means:

> "There should be no node at this subtree position anymore."

---

# 14. CASE 2 — Node Has Only One Child

There are two possibilities.

## Only right child

Example:

```text
      10
        \
         15
```

If we delete `10`, we want:

```text
      15
```

Code:

```java
if (current.left == null) {
    return current.right;
}
```

Suppose:

```text
current = 10
current.right = 15
```

Then:

```java
return current.right;
```

means:

```text
return 15
```

The parent can connect directly to `15`.

---

## Only left child

Example:

```text
       10
      /
     8
```

Delete `10`.

We want:

```text
      8
```

Code:

```java
if (current.right == null) {
    return current.left;
}
```

Therefore:

```text
return current.left
```

means:

> "Replace this node with its left child."

---

# 15. CASE 3 — Node Has Two Children

This is the most important case.

Example:

```text
             15
            /  \
          10    20
               /
              16
```

Suppose:

```java
delete(15);
```

Node `15` has:

```text
left  = 10
right = 20
```

So it has two children.

We cannot simply return one child because both subtrees contain important nodes.

We use the **inorder successor**.

---

# 16. What is Inorder Successor?

For a node, the inorder successor is:

> The smallest value in its right subtree.

Example:

```text
             15
            /  \
          10    20
               /
              16
```

Right subtree of `15`:

```text
       20
      /
     16
```

Smallest value:

```text
16
```

Therefore:

```text
successor = 16
```

---

# 17. `minValue()` Function

Code:

```java
private int minValue(Node node) {

    Node current = node;

    while (current.left != null) {
        current = current.left;
    }

    return current.data;
}
```

Its job is:

> Find the smallest value in a subtree.

---

# 18. How `minValue()` Works

Suppose:

```text
       20
      /
     16
    /
   12
```

Start:

```java
current = 20;
```

Check:

```java
current.left != null
```

Yes.

Move:

```java
current = current.left;
```

Now:

```text
current = 16
```

Again:

```text
16.left != null
```

Yes.

Move:

```text
current = 12
```

Now:

```text
12.left == null
```

Stop.

Then:

```java
return current.data;
```

returns:

```text
12
```

---

# 19. Why Does Leftmost Mean Minimum?

In a BST:

```text
             20
            /
          16
         /
       12
```

Every left node is smaller.

Therefore:

```text
20 > 16 > 12
```

The smallest value is the **leftmost node**.

So:

```java
while (current.left != null)
```

keeps moving left.

---

# 20. Two-Child Deletion Step-by-Step

Code:

```java
int successorValue = minValue(current.right);
```

Suppose:

```text
             15
            /  \
          10    20
               /
              16
```

Then:

```text
current = 15
```

and:

```java
current.right
```

is:

```text
20
```

So:

```java
minValue(20)
```

returns:

```text
16
```

Therefore:

```text
successorValue = 16
```

---

# 21. Copy Successor Value

Next:

```java
current.data = successorValue;
```

So:

```text
15 → 16
```

Temporary tree:

```text
             16
            /  \
          10    20
               /
              16
```

Notice that we now have **two 16s temporarily**.

That is okay.

We haven't finished deletion yet.

---

# 22. Delete the Duplicate Successor

Next:

```java
current.right =
    deleteRecursive(current.right, successorValue);
```

This means:

> "Now go into the right subtree and remove the original successor node."

So:

```text
             16
            /  \
          10    20
               /
              16
```

Search for `16`:

```text
20
 ↓
16
```

The original `16` is a leaf.

Therefore:

```java
return null;
```

Then `20` updates:

```text
20.left = null
```

Final tree:

```text
             16
            /  \
          10    20
```

---

# 23. Full Meaning of Two-Child Code

```java
int successorValue = minValue(current.right);

current.data = successorValue;

current.right =
    deleteRecursive(current.right, successorValue);

return current;
```

In simple English:

```text
1. Find the smallest value in the right subtree.
2. Copy that value into the node being deleted.
3. Delete the original successor node.
4. Return the current node.
```

---

# 24. The Most Important Line in Recursion

Consider:

```java
current.left = deleteRecursive(current.left, value);
```

Suppose:

```text
             15
            /
          10
         /
        8
       /
      4
```

We call:

```java
deleteRecursive(15, 4)
```

Since:

```text
4 < 15
```

we do:

```java
current.left = deleteRecursive(current.left, 4);
```

which becomes:

```java
15.left = deleteRecursive(10, 4);
```

The call goes deeper.

Eventually:

```text
deleteRecursive(4, 4)
```

returns:

```java
null
```

Now the paused statement becomes:

```java
15.left = ?
```

But the immediate parent is `8`:

```java
8.left = null;
```

Then `8` returns.

Then:

```java
10.left = 8;
```

Then:

```java
15.left = 10;
```

This is called **repairing the tree while recursion returns**.

---

# 25. "Search Down, Repair Up"

A very useful way to remember recursive BST deletion:

```text
              SEARCH DOWN
                   ↓
              Find target
                   ↓
             Delete/replace
                   ↓
              REPAIR UP
```

For example:

```text
15
 ↓
10
 ↓
8
 ↓
4   ← delete here
```

Then:

```text
4   → returns null
↑
8   → repairs left
↑
10  → repairs left
↑
15  → repairs left
```

---

# 26. STACK — How Recursion Actually Works

Suppose:

```java
delete(4);
```

Tree:

```text
             15
            /  \
          10    20
         /
        8
       /
      4
```

Calls:

```text
delete(4)
     ↓
deleteRecursive(15,4)
     ↓
deleteRecursive(10,4)
     ↓
deleteRecursive(8,4)
     ↓
deleteRecursive(4,4)
```

The stack looks approximately like:

```text
┌────────────────────────────┐
│ deleteRecursive(4,4)       │ ← TOP
├────────────────────────────┤
│ deleteRecursive(8,4)       │
├────────────────────────────┤
│ deleteRecursive(10,4)      │
├────────────────────────────┤
│ deleteRecursive(15,4)      │
├────────────────────────────┤
│ delete(4)                  │
└────────────────────────────┘
```

The last function called is the first one that returns.

This is **LIFO**:

```text
Last In → First Out
```

---

# 27. Dry Run — `delete(4)`

Initial tree:

```text
             15
            /  \
          10    20
         /     /
        8     16
       /
      4
```

Call:

```java
delete(4);
```

---

## Step 1

```java
root = deleteRecursive(root, 4);
```

Initially:

```text
root = 15
```

Therefore:

```java
deleteRecursive(15, 4)
```

Compare:

```text
4 < 15
```

Go left:

```java
current.left =
    deleteRecursive(current.left, 4);
```

So:

```text
deleteRecursive(10,4)
```

---

## Step 2

Current:

```text
10
```

Compare:

```text
4 < 10
```

Go left:

```text
deleteRecursive(8,4)
```

---

## Step 3

Current:

```text
8
```

Compare:

```text
4 < 8
```

Go left:

```text
deleteRecursive(4,4)
```

---

## Step 4 — Node Found

Current:

```text
4
```

Compare:

```text
4 == 4
```

Node found.

Check children:

```text
4.left  = null
4.right = null
```

Leaf node.

Therefore:

```java
return null;
```

---

# 28. Returning From `delete(4)`

Now recursion starts returning.

### Node 4

```text
4 → return null
```

Parent is `8`.

So:

```java
8.left = null;
```

Tree part:

```text
    8
```

Then:

```java
return 8;
```

---

### Node 10

It receives:

```text
8
```

Therefore:

```java
10.left = 8;
```

Then:

```java
return 10;
```

---

### Node 15

It receives:

```text
10
```

Therefore:

```java
15.left = 10;
```

Then:

```java
return 15;
```

---

### Finally

The original call:

```java
root = deleteRecursive(root, 4);
```

receives:

```text
15
```

Therefore:

```text
root = 15
```

---

# 29. Final Tree After `delete(4)`

```text
             15
            /  \
          10    20
         /     /
        8     16
```

---

# 30. Complete Call/Return Diagram

```text
CALL DOWN
────────────────────────

delete(4)
   ↓
deleteRecursive(15,4)
   ↓
deleteRecursive(10,4)
   ↓
deleteRecursive(8,4)
   ↓
deleteRecursive(4,4)


RETURN UP
────────────────────────

deleteRecursive(4,4)
        │
        └── return null
                ↓
        8.left = null
                ↓
        return 8
                ↓
        10.left = 8
                ↓
        return 10
                ↓
        15.left = 10
                ↓
        return 15
                ↓
        root = 15
```

---

# 31. Dry Run — `delete(10)`

Initial:

```text
             15
            /  \
          10    20
         /     /
        8     16
       /
      4
```

Call:

```java
delete(10);
```

---

## Step 1

At `15`:

```text
10 < 15
```

Go left:

```text
deleteRecursive(10,10)
```

---

## Step 2

At `10`:

```text
10 == 10
```

Node found.

Check:

```text
10.left  = 8
10.right = null
```

So it has only one child:

```text
       10
      /
     8
```

Code:

```java
if (current.right == null) {
    return current.left;
}
```

Therefore:

```text
return 8;
```

---

# 32. What Does Parent 15 Do?

We originally had:

```java
current.left =
    deleteRecursive(current.left, value);
```

For `15`:

```java
15.left = deleteRecursive(10,10);
```

The recursive call returns:

```text
8
```

Therefore:

```java
15.left = 8;
```

Final:

```text
             15
            /  \
           8    20
          /    /
         4    16
```

---

# 33. Why `return current`?

Suppose we are at:

```text
             15
            /
          10
         /
        8
```

and delete `8`.

After deletion:

```text
             15
            /
          10
```

The node `10` is still the root of its subtree.

Therefore:

```java
return current;
```

returns:

```text
10
```

It tells the parent:

> "I am still the root of this subtree."

---

# 34. What Does `return current` NOT Mean?

It does **not** mean:

> "Return to the beginning."

It does **not** mean:

> "Return the whole tree."

It means:

> "Return this Node object as the root of the current subtree."

---

# 35. Meaning of Every Return

This is an extremely useful table.

| Return statement       | Meaning                           |
| ---------------------- | --------------------------------- |
| `return null`          | This subtree is now empty         |
| `return current.left`  | Replace current with left child   |
| `return current.right` | Replace current with right child  |
| `return current`       | Current node remains subtree root |

---

# 36. Visual Meaning of Returns

## `return null`

Before:

```text
    8
   /
  4
```

After:

```text
    8
```

---

## `return current.left`

Before:

```text
    10
    /
   8
```

After:

```text
    8
```

---

## `return current.right`

Before:

```text
    10
      \
       15
```

After:

```text
    15
```

---

## `return current`

Before:

```text
    15
   /
  10
```

After:

```text
    15
   /
  10
```

The subtree root remains `15`.

---

# 37. Why Assignment Is Necessary

Consider:

```java
current.left = deleteRecursive(current.left, value);
```

This has two parts.

### Part 1 — Recursive call

```java
deleteRecursive(current.left, value)
```

means:

> Go into the left subtree and perform deletion.

### Part 2 — Assignment

```java
current.left = ...
```

means:

> Attach the returned node back to my left pointer.

Together:

```java
current.left =
    deleteRecursive(current.left, value);
```

means:

> "Delete the value from my left subtree and connect whatever comes back as my new left child."

---

# 38. Same Idea for Right

```java
current.right =
    deleteRecursive(current.right, value);
```

means:

> "Delete from my right subtree and attach the returned node as my right child."

---

# 39. Why Can't We Just Write:

```java
deleteRecursive(current.left, value);
```

Because the subtree may change.

Example:

```text
      10
     /
    8
   /
  4
```

Delete `8`.

After deletion:

```text
      10
     /
    4
```

The parent's `left` pointer must change:

```text
10.left
```

from:

```text
8
```

to:

```text
4
```

Therefore:

```java
current.left =
    deleteRecursive(current.left, value);
```

is necessary.

---

# 40. Complete Two-Child Dry Run

Tree:

```text
             15
            /  \
          10    20
               /
              16
```

Call:

```java
delete(15);
```

---

## Step 1 — Find 15

```text
current = 15
value = 15
```

Therefore:

```text
value == current.data
```

Node found.

---

## Step 2 — Check children

```text
left  = 10
right = 20
```

Both exist.

Therefore:

```text
TWO CHILDREN
```

---

## Step 3 — Find successor

```java
int successorValue =
    minValue(current.right);
```

Right subtree:

```text
       20
      /
     16
```

Minimum:

```text
16
```

Therefore:

```text
successorValue = 16
```

---

## Step 4 — Copy successor

```java
current.data = successorValue;
```

So:

```text
15 → 16
```

Temporary tree:

```text
             16
            /  \
          10    20
               /
              16
```

---

## Step 5 — Delete Original 16

```java
current.right =
    deleteRecursive(current.right, successorValue);
```

This becomes:

```java
20.left =
    deleteRecursive(16,16);
```

At `16`:

```text
leaf
```

So:

```java
return null;
```

Therefore:

```text
20.left = null
```

---

## Final Tree

```text
             16
            /  \
          10    20
```

---

# 41. Important Concept — We Don't Physically Move the Node

In this implementation:

```java
current.data = successorValue;
```

we are copying the **value**.

We are not moving the actual Node object.

For example:

```text
Original:

Node A
data = 15

Node B
data = 16
```

After:

```java
current.data = 16;
```

we get:

```text
Node A
data = 16

Node B
data = 16
```

Then Node B is deleted.

---

# 42. Why Use the Inorder Successor?

The successor is the smallest value in the right subtree.

Example:

```text
             15
            /  \
          10    20
               /  \
              16   25
```

Right subtree:

```text
       20
      /  \
     16   25
```

Smallest:

```text
16
```

Replacing `15` with `16` keeps the BST property:

```text
10 < 16 < 20
```

---

# 43. Alternative: Inorder Predecessor

Another valid approach is to use the **inorder predecessor**.

The predecessor is:

> The largest value in the left subtree.

Example:

```text
             15
            /  \
          10    20
         / \
        8  12
```

Largest value in left subtree:

```text
12
```

So we could replace:

```text
15 → 12
```

Both approaches are valid:

```text
Successor    → minimum of right subtree

Predecessor  → maximum of left subtree
```

This implementation uses the **successor**.

---

# 44. Complete Flow of `deleteRecursive()`

```text
                 current == null?
                       |
                 YES → return null
                       |
                      NO
                       ↓
             value < current.data?
                  /          \
                YES          NO
                 ↓            ↓
              Go LEFT    value > current.data?
                               /       \
                             YES       NO
                              ↓         ↓
                           Go RIGHT   Node found
                                         ↓
                                  Check children
                                         ↓
                     ┌───────────────────┼──────────────────┐
                     ↓                   ↓                  ↓
                   0 child             1 child            2 children
                     ↓                   ↓                  ↓
                return null       return child       successor
                                                          ↓
                                                    copy value
                                                          ↓
                                                   delete successor
                                                          ↓
                                                   return current
```

---

# 45. Complete Code With Comments

```java
public void delete(int value) {

    // Start deletion from root.
    // Returned node becomes the new root.
    root = deleteRecursive(root, value);
}


private Node deleteRecursive(Node current, int value) {

    // If subtree is empty,
    // there is nothing to delete.
    if (current == null) {
        return null;
    }


    // Value is smaller,
    // so search in left subtree.
    if (value < current.data) {

        current.left =
            deleteRecursive(current.left, value);

        // Current node still remains
        // the root of this subtree.
        return current;
    }


    // Value is greater,
    // so search in right subtree.
    if (value > current.data) {

        current.right =
            deleteRecursive(current.right, value);

        // Current node still remains
        // the root of this subtree.
        return current;
    }


    // If we reach here:
    // value == current.data
    // Therefore node is found.


    // CASE 1:
    // No children
    if (current.left == null &&
        current.right == null) {

        return null;
    }


    // CASE 2:
    // Only right child
    if (current.left == null) {

        return current.right;
    }


    // CASE 2:
    // Only left child
    if (current.right == null) {

        return current.left;
    }


    // CASE 3:
    // Two children

    // Find smallest value in right subtree.
    int successorValue =
        minValue(current.right);

    // Replace current value.
    current.data = successorValue;

    // Delete original successor.
    current.right =
        deleteRecursive(
            current.right,
            successorValue
        );

    // Current remains subtree root.
    return current;
}


private int minValue(Node node) {

    Node current = node;

    // Keep moving left.
    while (current.left != null) {
        current = current.left;
    }

    // Leftmost node has minimum value.
    return current.data;
}
```

---

# 46. Function-by-Function Explanation

## `delete()`

```java
public void delete(int value)
```

### Purpose

Starts the deletion.

### Example

```java
delete(10);
```

### Important line

```java
root = deleteRecursive(root, value);
```

---

## `deleteRecursive()`

```java
private Node deleteRecursive(Node current, int value)
```

### Purpose

Searches for the value and deletes it recursively.

### Important concept

It returns:

```text
Node
```

because the subtree root can change after deletion.

---

## `minValue()`

```java
private int minValue(Node node)
```

### Purpose

Finds the smallest value in a subtree.

### Method

Move left until:

```java
current.left == null
```

Then return:

```java
current.data
```

---

# 47. Why Does `deleteRecursive()` Return `Node`?

This is a very common viva question.

Because deletion can change the root of a subtree.

Example:

```text
    10
   /
  8
```

Delete `10`.

New subtree root:

```text
8
```

Therefore the function needs to return:

```java
Node
```

If the function were:

```java
void deleteRecursive(...)
```

it would be harder to reconnect the changed subtree.

---

# 48. Example of Subtree Root Changing

Before:

```text
       15
      /
    10
   /
  8
```

Delete:

```text
10
```

Result:

```text
       15
      /
     8
```

The subtree rooted at `10` is now rooted at `8`.

Therefore:

```java
return current.left;
```

returns `8`.

Parent:

```java
15.left = 8;
```

---

# 49. Why `root` Can Change

Suppose:

```text
      10
     /
    5
```

Delete:

```text
10
```

Result:

```text
    5
```

Originally:

```text
root = 10
```

Now:

```text
root = 5
```

That's why:

```java
root = deleteRecursive(root, value);
```

is essential.

---

# 50. What Happens If Value Doesn't Exist?

Suppose:

```text
       15
      /  \
    10    20
```

Try:

```java
delete(99);
```

At `15`:

```text
99 > 15
```

Go right.

At `20`:

```text
99 > 20
```

Go right.

But:

```text
20.right == null
```

Therefore:

```java
deleteRecursive(null, 99)
```

returns:

```java
null
```

The tree remains unchanged.

---

# 51. Dry Run — Value Not Found

```text
delete(99)
   ↓
15
   ↓
20
   ↓
null
```

At null:

```java
return null;
```

Then:

```text
20.right = null
```

Then:

```text
return 20
```

Then:

```text
15.right = 20
```

Then:

```text
return 15
```

Final tree is unchanged.

---

# 52. Important Mental Model

Whenever you see:

```java
current.left = deleteRecursive(current.left, value);
```

think:

```text
"Go down into left subtree.
 Delete there.
 Come back with the new left-subtree root.
 Attach it to current.left."
```

Whenever you see:

```java
return current;
```

think:

```text
"My subtree still exists,
 and I am still its root."
```

Whenever you see:

```java
return null;
```

think:

```text
"This subtree position is now empty."
```

Whenever you see:

```java
return current.left;
```

think:

```text
"Replace me with my left child."
```

Whenever you see:

```java
return current.right;
```

think:

```text
"Replace me with my right child."
```

---

# 53. Recursion Stack — General Pattern

Suppose:

```text
A
↓
B
↓
C
↓
D
```

Recursive calls:

```text
A calls B
B calls C
C calls D
```

Stack:

```text
┌──────────┐
│ D        │ ← returns first
├──────────┤
│ C        │
├──────────┤
│ B        │
├──────────┤
│ A        │
└──────────┘
```

Return:

```text
D
↑
C
↑
B
↑
A
```

Therefore:

> **Recursive calls go down. Returns come back up.**

---

# 54. BST Delete = Search + Delete + Reconnect

The complete algorithm can be remembered as:

```text
1. Search for node
        ↓
2. Find node
        ↓
3. Check number of children
        ↓
   ┌────┼────┐
   ↓    ↓    ↓
   0    1    2
 child child children
   ↓    ↓    ↓
 null  child successor
              ↓
          replace + delete
        ↓
4. Reconnect subtree
```

---

# 55. Time Complexity

Let:

```text
h = height of tree
```

Searching takes:

```text
O(h)
```

Deletion also takes:

```text
O(h)
```

Finding the successor:

```text
O(h)
```

Therefore overall:

```text
O(h)
```

---

# 56. Balanced BST

For a balanced BST:

```text
h ≈ log n
```

Therefore:

```text
Deletion = O(log n)
```

---

# 57. Worst Case

If the BST becomes like a linked list:

```text
15
  \
   20
     \
      30
        \
         40
           \
            50
```

Height:

```text
h = n
```

Therefore:

```text
Deletion = O(n)
```

---

# 58. Space Complexity

Because deletion uses recursion:

```text
Space = O(h)
```

For a balanced tree:

```text
O(log n)
```

Worst case:

```text
O(n)
```

---

# 59. Complexity Table

| Operation       | Balanced | Worst Case |
| --------------- | -------: | ---------: |
| Search          | O(log n) |       O(n) |
| Insert          | O(log n) |       O(n) |
| Delete          | O(log n) |       O(n) |
| Recursive Space | O(log n) |       O(n) |

---

# 60. Common Mistakes

## Mistake 1

Writing:

```java
deleteRecursive(root, value);
```

instead of:

```java
root = deleteRecursive(root, value);
```

### Problem

If root changes, the new root is lost.

---

## Mistake 2

Writing:

```java
deleteRecursive(current.left, value);
```

instead of:

```java
current.left =
    deleteRecursive(current.left, value);
```

### Problem

The changed subtree is not reattached.

---

## Mistake 3

Thinking:

```java
return current;
```

means return to the beginning.

Wrong.

It means:

```text
Return the current Node as this subtree's root.
```

---

## Mistake 4

Thinking:

```java
return null;
```

means the whole tree becomes null.

Wrong.

It only means:

```text
The current subtree position becomes empty.
```

---

## Mistake 5

For two children, simply doing:

```java
return current.left;
```

This would lose the right subtree.

Instead, use a successor or predecessor.

---

# 61. Quick Comparison of Cases

### Case 1 — Leaf

```text
    10
   /
  5
```

Delete `5`:

```text
    10
```

Code:

```java
return null;
```

---

### Case 2 — One child

```text
    10
   /
  5
 /
2
```

Delete `5`:

```text
    10
   /
  2
```

Code:

```java
return current.left;
```

---

### Case 2 — One right child

```text
10
  \
   15
```

Delete `10`:

```text
15
```

Code:

```java
return current.right;
```

---

### Case 3 — Two children

```text
       10
      /  \
     5   15
        /
       12
```

Delete `10`.

Successor:

```text
12
```

Result:

```text
       12
      /  \
     5   15
```

---

# 62. One-Line Meaning of Each Important Statement

```java
root = deleteRecursive(root, value);
```

> Delete from the entire tree and update the root.

---

```java
if (current == null)
```

> We reached an empty subtree.

---

```java
return null;
```

> This subtree should now be empty.

---

```java
current.left = deleteRecursive(current.left, value);
```

> Delete from left subtree and reconnect the returned subtree.

---

```java
current.right = deleteRecursive(current.right, value);
```

> Delete from right subtree and reconnect the returned subtree.

---

```java
return current;
```

> Current node remains the subtree root.

---

```java
return current.left;
```

> Replace current node with its left child.

---

```java
return current.right;
```

> Replace current node with its right child.

---

```java
int successorValue = minValue(current.right);
```

> Find the smallest value in the right subtree.

---

```java
current.data = successorValue;
```

> Replace the deleted node's value with its successor.

---

```java
current.right =
    deleteRecursive(current.right, successorValue);
```

> Remove the original successor from the right subtree.

---

# 63. Final Cheat Sheet 🧠

## BST Delete

```text
                DELETE
                   |
                   ↓
              Find node
                   |
        ┌──────────┼──────────┐
        ↓          ↓          ↓
       0 child   1 child    2 children
        ↓          ↓          ↓
      null       child     successor
                             ↓
                         copy value
                             ↓
                       delete successor
```

---

## Return Cheat Sheet

```text
return null
    ↓
No node here

return current.left
    ↓
Replace current by left child

return current.right
    ↓
Replace current by right child

return current
    ↓
Current remains subtree root
```

---

## Recursion Cheat Sheet

```text
CALL DOWN
    ↓
Search
    ↓
Find node
    ↓
Delete
    ↓
RETURN UP
    ↓
Reconnect
```

Remember:

> **Search Down → Delete → Repair Up**

---

# 64. Viva Questions

### Q1. Why does `deleteRecursive()` return `Node`?

Because deletion can change the root of the current subtree.

---

### Q2. Why do we write:

```java
root = deleteRecursive(root, value);
```

Because the root itself may be deleted or replaced.

---

### Q3. What does `return null` mean?

The current subtree becomes empty.

---

### Q4. What does `return current` mean?

The current node remains the root of the current subtree.

---

### Q5. What does:

```java
current.left = deleteRecursive(current.left, value);
```

mean?

Delete the value from the left subtree and attach the returned node as the new left child.

---

### Q6. What happens when the node is a leaf?

Return `null`.

---

### Q7. What happens when a node has only one child?

Return that child.

---

### Q8. What happens when a node has two children?

Find the inorder successor, copy its value, then delete the original successor.

---

### Q9. What is an inorder successor?

The smallest value in the right subtree.

---

### Q10. How is the successor found?

Move left repeatedly in the right subtree.

---

### Q11. Why does `minValue()` move left?

Because smaller BST values are stored on the left.

---

### Q12. What is the time complexity of deletion?

```text
O(h)
```

where `h` is the tree height.

Balanced:

```text
O(log n)
```

Worst case:

```text
O(n)
```

---

### Q13. What is the recursive space complexity?

```text
O(h)
```

because recursive calls occupy stack frames.

---

# 65. Final Mental Picture

Whenever you see:

```java
deleteRecursive(current, value)
```

imagine:

```text
              current
                 |
          ┌──────┴──────┐
          ↓             ↓
       smaller        greater
          ↓             ↓
        LEFT           RIGHT
          ↓             ↓
       recurse        recurse
          ↓             ↓
       return         return
          ↓             ↓
       reconnect     reconnect
```

When the node is found:

```text
                NODE FOUND
                    |
          ┌─────────┼─────────┐
          ↓         ↓         ↓
         0          1         2
       children   child     children
          ↓         ↓         ↓
        null      child    successor
                              ↓
                           replace
                              ↓
                        delete duplicate
```

And recursion always works like:

```text
              CALL
               ↓
              CALL
               ↓
              CALL
               ↓
             TARGET
               ↓
             RETURN
               ↑
             RETURN
               ↑
             RETURN
```

### ⭐ The one sentence to remember

> **BST recursive deletion searches downward to find the node, deletes/replaces it, and then returns upward reconnecting every affected subtree.**
