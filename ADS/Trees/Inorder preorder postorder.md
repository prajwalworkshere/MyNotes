# Preorder

```text
          50
         /  \
       40    60
      /  \     \
    30   45     80
```

Preorder code:

```java
static void preorder(Node n) {
    if (n == null) return;

    System.out.print(n.data + " ");  // ① PRINT
    preorder(n.left);                // ② LEFT
    preorder(n.right);               // ③ RIGHT
}
```

### Step-by-step execution

We first call:

```java
preorder(50);
```

Unlike inorder, **preorder prints the node immediately** before going to the left.

So at `50`:

```java
System.out.print(50);
```

Output:

```text
50
```

Then it executes:

```java
preorder(50.left);
```

So we go to `40`.

---

### At 40

The first thing preorder does is print `40`:

```java
System.out.print(40);
```

Output:

```text
50 40
```

Then it goes to the left:

```java
preorder(40.left);
```

So we go to `30`.

---

### At 30

Again, the first thing it does is print `30`:

```java
System.out.print(30);
```

Output:

```text
50 40 30
```

Then:

```java
preorder(30.left);
```

`30.left` is `null`.

So:

```java
if (n == null) return;
```

The function returns to `30`.

Now it executes:

```java
preorder(30.right);
```

`30.right` is also `null`.

So it returns again.

Now `30` is completely finished.

We return to `40`.

---

### Back to 40

Remember, `40` had already been printed.

It was waiting at:

```java
preorder(40.left);
```

That call is now completely finished.

So the next statement is:

```java
preorder(40.right);
```

`40.right` is `45`.

So we go to `45`.

---

### At 45

The first thing preorder does is print `45`:

```java
System.out.print(45);
```

Output:

```text
50 40 30 45
```

Then:

```java
preorder(45.left);
```

`45.left` is `null`, so it returns.

Then:

```java
preorder(45.right);
```

`45.right` is also `null`, so it returns.

Now `45` is completely finished.

So we return to `40`.

---

### Back to 40

Both of `40`'s children have now been processed:

```text
40.left  → 30 → finished
40.right → 45 → finished
```

Therefore, `40` is completely finished.

We return to `50`.

---

### Back to 50

Remember, `50` was already printed at the very beginning.

Now its left side is finished:

```text
50.left → 40 → finished
```

So the next statement is:

```java
preorder(50.right);
```

`50.right` is `60`.

So we go to `60`.

---

### At 60

First thing preorder does:

```java
System.out.print(60);
```

Output:

```text
50 40 30 45 60
```

Then:

```java
preorder(60.left);
```

`60.left` is `null`, so it returns.

Then:

```java
preorder(60.right);
```

`60.right` is `80`.

So we go to `80`.

---

### At 80

First thing preorder does is print `80`:

```java
System.out.print(80);
```

Final output:

```text
50 40 30 45 60 80
```

Then:

```java
preorder(80.left);
```

`80.left` is `null` → return.

Then:

```java
preorder(80.right);
```

`80.right` is `null` → return.

Now `80` is finished.

We return to `60`.

`60` is finished.

We return to `50`.

`50` is finished.

The entire traversal is complete.

### Final preorder output

```text
50 40 30 45 60 80
```


# Inorder        
```
      50
     /  \
   40    60
  /  \     \
30   45     80
```

```java
static void inorder(Node n) {
    if (n == null) return;

    inorder(n.left);
    System.out.print(n.data + " ");
    inorder(n.right);
}
```

### Step-by-step execution

We first call:

```java
inorder(50);
```

It first executes:

```java
inorder(50.left);
```

So it goes to `40`.

At `40`, it again first goes to its left:

```java
inorder(40.left);
```

So it goes to `30`.

At `30`, it checks its left:

```java
inorder(30.left);
```

`30.left` is `null`, so the function returns.

Now we are back inside `inorder(30)`, so the next line executes:

```java
System.out.print(30);
```

Output:

```text
30
```

Then it checks `30.right`:

```java
inorder(30.right);
```

`30.right` is also `null`, so it returns.

Now the work of node `30` is completely finished, so we return to `40`.

---

### Back to 40

The left side of `40` has now been completely processed.

Therefore, the next line executes:

```java
System.out.print(40);
```

Output:

```text
30 40
```

Then it processes `40.right`:

```java
inorder(40.right);
```

`40.right` is `45`, so we go to `45`.

---

### At 45

First:

```java
inorder(45.left);
```

`45.left` is `null`, so it returns.

Then:

```java
System.out.print(45);
```

Output:

```text
30 40 45
```

Then:

```java
inorder(45.right);
```

`45.right` is `null`, so it returns.

Now `45` is completely finished, so we return to `40`.

Both the left and right sides of `40` are now finished, so `40` is completely finished too.

We return to `50`.

---

### Back to 50

Remember that `50` was waiting here:

```java
inorder(50.left);       // This is now finished

System.out.print(50);   // Now this executes
```

So `50` is printed:

```text
30 40 45 50
```

Then we process:

```java
inorder(50.right);
```

So we go to `60`.

---

### At 60

First:

```java
inorder(60.left);
```

`60.left` is `null`, so it returns.

Then:

```java
System.out.print(60);
```

Output:

```text
30 40 45 50 60
```

Then we process:

```java
inorder(60.right);
```

⚠️ `60.right` is **not null**. It contains `80`.

So we go to `80`.

---

### At 80

First:

```java
inorder(80.left);
```

`80.left` is `null`, so it returns.

Then:

```java
System.out.print(80);
```

Output:

```text
30 40 45 50 60 80
```

Then:

```java
inorder(80.right);
```

`80.right` is `null`, so it returns.

Now `80` is finished → return to `60`.

`60` is also finished → return to `50`.

`50` is finished.

Therefore, the final inorder traversal is:

```text
30 40 45 50 60 80
```

### The main idea

For every node, the order is always:

```text
1. Go LEFT
2. Print ROOT
3. Go RIGHT
```

So:

```text
inorder(n.left);
System.out.print(n.data);
inorder(n.right);
```

means:

```text
       NODE
         |
      GO LEFT
         ↓
   LEFT FINISHES
         ↓
    PRINT NODE
         ↓
     GO RIGHT
         ↓
  RIGHT FINISHES
```

The important thing is that when a recursive call is made, the current function **waits there**. Once that recursive call finishes, execution continues from the next line.

### Step-by-step execution

For postorder, the order is:

```text
LEFT → RIGHT → ROOT
```

The most important thing to remember is:

> **We do NOT print the node when we first reach it.**
> We first finish its LEFT, then its RIGHT, and only then print the node.

---

### Start with 50

We call:

```java
postorder(50);
```

`50` is not null.

Now the first statement is:

```java
postorder(50.left);
```

So we go to `40`.

---

### At 40

`40` is not null.

Again, we first go left:

```java
postorder(40.left);
```

So we go to `30`.

---

### At 30

`30` is not null.

First:

```java
postorder(30.left);
```

`30.left` is `null`.

So:

```java
if (n == null) return;
```

We return to the `30` function.

Now it continues to the NEXT statement:

```java
postorder(30.right);
```

`30.right` is also `null`.

So it returns again.

Now both LEFT and RIGHT of `30` are finished.

Only NOW do we reach:

```java
System.out.print(30);
```

So:

```text
30
```

is printed.

### Important:

For `30`:

```text
30.left  → NULL → return
30.right → NULL → return
30        → PRINT
```

So:

```text
30
```

---

### Back to 40

Now we return to `40`.

Remember, `40` was waiting here:

```java
postorder(40.left);    // FINISHED
postorder(40.right);   // NEXT
```

So now we execute:

```java
postorder(40.right);
```

`40.right` is `45`.

So we go to `45`.

---

### At 45

First:

```java
postorder(45.left);
```

`45.left` is `null`.

Return.

Then:

```java
postorder(45.right);
```

`45.right` is also `null`.

Return.

Now both children of `45` are finished.

So finally:

```java
System.out.print(45);
```

Output:

```text
30 45
```

---

### Back to 40

Now both children of `40` are finished:

```text
40.left  → 30 → FINISHED
40.right → 45 → FINISHED
```

So NOW we print `40`:

```text
30 45 40
```

This is the important part of postorder:

```text
LEFT finished
      ↓
RIGHT finished
      ↓
PRINT ROOT
```

So `40` prints only after both `30` and `45` are completely finished.

---

### Back to 50

Now the entire left subtree of `50` is finished:

```text
        50
       /
      40
     /  \
   30    45

30 → finished
45 → finished
40 → finished
```

So we return to `50`.

But `50` still has its RIGHT subtree to process.

Therefore:

```java
postorder(50.right);
```

`50.right` is `60`.

So we go to `60`.

---

### At 60

First:

```java
postorder(60.left);
```

`60.left` is `null`.

Return.

Then:

```java
postorder(60.right);
```

`60.right` is `80`.

So we go to `80`.

---

### At 80

First:

```java
postorder(80.left);
```

`80.left` is `null`.

Return.

Then:

```java
postorder(80.right);
```

`80.right` is `null`.

Return.

Now both children of `80` are finished.

So we print `80`:

```text
30 45 40 80
```

---

### Back to 60

Now:

```text
60.left  → NULL → finished
60.right → 80   → finished
```

Both children are finished.

So now print `60`:

```text
30 45 40 80 60
```

---

### Finally back to 50

Now both children of `50` are finished:

```text
50.left  → 40 → finished
50.right → 60 → finished
```

So NOW we finally print `50`:

```text
30 45 40 80 60 50
```

Therefore, the final postorder traversal is:

```text
30 45 40 80 60 50
```

---

## 🧠 The main idea

For every node:

```java
postorder(n.left);
postorder(n.right);
System.out.print(n.data);
```

means:

```text
             NODE
               |
               ↓
           GO LEFT
               ↓
        LEFT FINISHES
               ↓
          GO RIGHT
               ↓
       RIGHT FINISHES
               ↓
          PRINT NODE
```

So remember:

```text
PREORDER
ROOT → LEFT → RIGHT
       ↓
   print FIRST


INORDER
LEFT → ROOT → RIGHT
         ↓
     print MIDDLE


POSTORDER
LEFT → RIGHT → ROOT
               ↓
           print LAST
```

### ⭐ One-line trick

**Pre = print before children**

**In = print in between children**

**Post = print after children**

```text
PREORDER:   PRINT → LEFT → RIGHT

INORDER:    LEFT → PRINT → RIGHT

POSTORDER:  LEFT → RIGHT → PRINT
```

That's the only major difference between the three.

