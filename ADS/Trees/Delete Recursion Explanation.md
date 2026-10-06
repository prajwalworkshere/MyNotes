 `delete(10)` => **function call ko step-by-step**

Tree :

```text
             15
            /  \
          10    20
         /     /
        8     16
       /
      4
```

We call:

```java
delete(10);
```

---

## Step 1 — `delete()` call

Tumhara code:

```java
public void delete(int value) {
    root = deleteRecursive(root, value);
}
```

So:

```java
delete(10);
```

becomes:

```java
root = deleteRecursive(15, 10);
```

Because:

```text
root
 ↓
15
```

So currently:

```text
current = 15
value   = 10
```

---

# Step 2 — `current = 15`

Code:

```java
if (current == null) {
    return null;
}
```

Is `15 == null`?

❌ No.

Next:

```java
if (value < current.data)
```

Check:

```text
10 < 15
```

✅ Yes.

So:

```java
current.left = deleteRecursive(current.left, value);
```

`15.left` is `10`.

Therefore:

```java
deleteRecursive(10, 10);
```

Tree:

```text
             15  ← current
            /  \
          10    20
```

---

# Step 3 — `current = 10`

Now:

```text
current = 10
value = 10
```

First:

```java
if (current == null)
```

❌ No.

Then:

```java
if (value < current.data)
```

```text
10 < 10
```

❌ No.

Then:

```java
if (value > current.data)
```

```text
10 > 10
```

❌ No.

So we've reached the important point:

```text
value == current.data
```

🎯 **10 mil gaya!**

```text
             15
            /
          10 ← FOUND
         /
        8
       /
      4
```

---

# Step 4 — Check children of 10

Now code:

```java
if (current.left == null && current.right == null) {
    return null;
}
```

For `10`:

```text
10.left  = 8
10.right = null
```

So:

```text
left == null?   ❌
right == null?  ✅
```

Both are NOT null.

Therefore first condition:

```java
if (current.left == null && current.right == null)
```

is:

```text
false
```

So `return null` **Nahi hua**.

---

# Step 5 — Check right child

Next:

```java
if (current.left == null) {
    return current.right;
}
```

Is:

```text
10.left == null
```

❌ No.

So skip.

---

# Step 6 — Check left child

Next:

```java
if (current.right == null) {
    return current.left;
}
```

Is:

```text
10.right == null
```

✅ YES!

Therefore:

```java
return current.left;
```

And:

```text
current.left = 8
```

So function returns:

```text
8
```

---

# 🔥 Ab sabse important part

Hum originally `15` ke andar the.

Remember this line?

```java
current.left = deleteRecursive(current.left, value);
```

At `15`:

```text
        15
       /
      10
```

Humne `10` ko delete karne bheja:

```text
deleteRecursive(10, 10)
```

10 ne bola:

```text
"Main delete ho raha hoon.
Meri jagah 8 ko laga do."
```

So it returned:

```text
8
```

Now the line at `15` becomes effectively:

```java
current.left = 8;
```

Result:

```text
             15
            /  \
           8    20
          /    /
         4    16
```

🎯 **10 delete ho gaya!**

---

# 🔄 Full flow visually

```text
delete(10)
     ↓
deleteRecursive(15, 10)
     ↓
10 < 15
     ↓
go LEFT
     ↓
deleteRecursive(10, 10)
     ↓
10 == 10
     ↓
FOUND 10
     ↓
10 has:
left  = 8
right = null
     ↓
return current.left
     ↓
return 8
     ↓
15.left = 8
```

Final:

```text
BEFORE:

             15
            /  \
          10    20
         /     /
        8     16
       /
      4


AFTER delete(10):

             15
            /  \
           8    20
          /    /
         4    16
```

---

## 🧠 Ab `return null` kyu nahi hua?

Because **10 leaf nahi tha**.

10 ke paas:

```text
left = 8
right = null
```

So 10 has **one child**.

Therefore:

```java
return current.left;
```

execute hua.

### Agar hum `delete(8)` karte:

```text
    8
   /
  4
```

8 ke paas bhi one child hai → `return 4`.

### Agar hum `delete(4)` karte:

```text
4
```

4 ke **zero children** hain → `return null`.

Yahi 3 cases ka core hai:

```text
0 children → return null

1 left child → return current.left

1 right child → return current.right

2 children → successor wala case
```

**`delete(10)` mein humne 1-child case dekha.**


---
# Now Differnet

---

Bilkul 👍 Ab **same original tree se `delete(4)`** karte hain.

Original tree:

```text
             15
            /  \
          10    20
         /     /
        8     16
       /
      4
```

We call:

```java
delete(4);
```

---

## Step 1 — `delete(4)`

```java
public void delete(int value) {
    root = deleteRecursive(root, value);
}
```

So:

```text
deleteRecursive(15, 4)
```

```text
current = 15
value = 4
```

Check:

```text
4 < 15
```

Yes → **LEFT**

```java
current.left = deleteRecursive(current.left, 4);
```

So:

```text
deleteRecursive(10, 4)
```

---

## Step 2 — `current = 10`

```text
        15
       /
     10  ← current
     /
    8
   /
  4
```

Check:

```text
4 < 10
```

Yes → **LEFT**

```java
current.left = deleteRecursive(current.left, 4);
```

So:

```text
deleteRecursive(8, 4)
```

---

## Step 3 — `current = 8`

```text
        10
        /
       8  ← current
      /
     4
```

Check:

```text
4 < 8
```

Yes → **LEFT**

```java
current.left = deleteRecursive(current.left, 4);
```

So:

```text
deleteRecursive(4, 4)
```

---

# Step 4 — `current = 4` 🎯

Now:

```text
      8
     /
    4  ← current
```

Check:

```java
if (current == null)
```

No.

Then:

```java
if (value < current.data)
```

```text
4 < 4
```

❌ No.

Then:

```java
if (value > current.data)
```

```text
4 > 4
```

❌ No.

Therefore:

```text
4 == 4
```

🎯 **4 mil gaya!**

---

# Step 5 — Check children

Now this code:

```java
if (current.left == null && current.right == null) {
    return null;
}
```

4 ka tree:

```text
      4
     / \
   null null
```

So:

```text
current.left  == null ✅
current.right == null ✅
```

Therefore:

```java
return null;
```

🔥 **YAHAN `return null` EXECUTE HUA.**

---

# Step 6 — `null` kahan gaya?

Ab dhyaan se.

Hum `8` se aaye the:

```text
      8
     /
    4
```

8 ke code mein tha:

```java
current.left = deleteRecursive(current.left, 4);
```

`deleteRecursive(4,4)` ne return kiya:

```text
null
```

So:

```java
8.left = null;
```

Tree:

```text
      8
```

**4 delete ho gaya.**

---

# Step 7 — Ab `8` return karega

8 ka function:

```java
return current;
```

So:

```text
return 8
```

Ye `10` ke paas jayega.

10 mein:

```java
current.left = deleteRecursive(current.left, 4);
```

Recursive call ne return kiya `8`.

So effectively:

```java
10.left = 8;
```

Tree:

```text
       10
       /
      8
```

---

# Step 8 — 10 return karega

```java
return current;
```

So:

```text
return 10
```

15 ke paas jayega.

15:

```java
current.left = deleteRecursive(current.left, 4);
```

return hua `10`.

So:

```java
15.left = 10;
```

Final tree:

```text
             15
            /  \
          10    20
         /     /
        8     16
```

---

# 🔥 Pure execution ko ek line mein dekho

```text
delete(4)
   ↓
15
   ↓ LEFT
10
   ↓ LEFT
8
   ↓ LEFT
4
   ↓
4 FOUND
   ↓
4 has 0 children
   ↓
return null
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
```

## Sabse important visualization

### Before:

```text
15
│
└── 10
     │
     └── 8
          │
          └── 4
```

### 4 ke function mein:

```text
current = 4

left  = null
right = null

        4
       / \
    null null
```

Therefore:

```java
return null;
```

### Parent `8` receives that `null`:

```text
8.left = null;
```

So:

```text
    8
   /
 null
```

### Final:

```text
             15
            /  \
          10    20
         /     /
        8     16
```

**Yahi actual magic hai recursion ka:** `4` ne `null` return kiya → `8.left` update hua → `8` return hua → `10.left` update hua → `10` return hua → `15.left` update hua.

---
# Using Stack Visualization
---

Bilkul. 🔥 Ab **stack ke andar kya ho raha hai** woh visualize karte hain.

Hum dono cases dekhenge:

1. `delete(4)` → **0 children → `return null`**
2. `delete(10)` → **1 child → `return 8`**

Sabse important rule:

> **Recursive function call stack mein jaata hai. Jab `return` hota hai, function stack se bahar aata hai aur returned value apne caller ko deta hai.**

---

# 🟢 CASE 1: `delete(4)`

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

## STEP 1 — Main `delete()` stack mein

```java
root = deleteRecursive(root, 4);
```

Call:

```text
┌─────────────────────────────┐
│ delete(4)                   │
│                             │
│ calls deleteRecursive(15,4) │
└─────────────────────────────┘
```

Stack:

```text
TOP
 ↓

┌─────────────────────────┐
│ deleteRecursive(15, 4) │
└─────────────────────────┘

┌─────────────────────────┐
│ delete(4)              │
└─────────────────────────┘
```

---

# STEP 2 — 15 calls 10

At `15`:

```java
if (value < current.data)
```

```text
4 < 15 ✅
```

So:

```java
current.left = deleteRecursive(current.left, value);
```

`15.left` is `10`.

Call:

```text
deleteRecursive(10,4)
```

Stack:

```text
TOP
 ↓

┌─────────────────────────┐
│ deleteRecursive(10, 4) │ ← CURRENT
└─────────────────────────┘
│
┌─────────────────────────┐
│ deleteRecursive(15, 4) │ ← WAITING
│                         │
│ current.left = ?????    │
└─────────────────────────┘
│
┌─────────────────────────┐
│ delete(4)              │
└─────────────────────────┘
```

### Notice:

`15` **ruk gaya hai**.

It is waiting for:

```java
deleteRecursive(10, 4)
```

to return something.

---

# STEP 3 — 10 calls 8

At `10`:

```text
4 < 10 ✅
```

So:

```java
current.left = deleteRecursive(current.left, 4);
```

Call:

```text
deleteRecursive(8,4)
```

Stack:

```text
TOP
 ↓

┌─────────────────────────┐
│ deleteRecursive(8, 4)  │ ← CURRENT
└─────────────────────────┘
│
┌─────────────────────────┐
│ deleteRecursive(10, 4) │ ← WAITING
│ current.left = ?????    │
└─────────────────────────┘
│
┌─────────────────────────┐
│ deleteRecursive(15, 4) │ ← WAITING
│ current.left = ?????    │
└─────────────────────────┘
│
┌─────────────────────────┐
│ delete(4)              │
└─────────────────────────┘
```

Again:

**10 ruk gaya.**

---

# STEP 4 — 8 calls 4

At `8`:

```text
4 < 8 ✅
```

So:

```java
current.left = deleteRecursive(current.left, 4);
```

Call:

```text
deleteRecursive(4,4)
```

Stack:

```text
TOP
 ↓

┌─────────────────────────┐
│ deleteRecursive(4, 4)  │ ← CURRENT
└─────────────────────────┘
│
┌─────────────────────────┐
│ deleteRecursive(8, 4)  │ ← WAITING
│ current.left = ?????    │
└─────────────────────────┘
│
┌─────────────────────────┐
│ deleteRecursive(10, 4) │ ← WAITING
│ current.left = ?????    │
└─────────────────────────┘
│
┌─────────────────────────┐
│ deleteRecursive(15, 4) │ ← WAITING
│ current.left = ?????    │
└─────────────────────────┘
│
┌─────────────────────────┐
│ delete(4)              │
└─────────────────────────┘
```

---

# STEP 5 — 4 mil gaya 🎯

Ab:

```text
current = 4
value = 4
```

So:

```text
value == current.data
```

Node:

```text
    4
   / \
 null null
```

Condition:

```java
if (current.left == null && current.right == null)
```

TRUE.

So:

```java
return null;
```

💥 **Ab 4 wala stack frame destroy/pop ho jayega.**

Before:

```text
TOP
 ↓

[ deleteRecursive(4,4) ]  ← return null
[ deleteRecursive(8,4) ]
[ deleteRecursive(10,4) ]
[ deleteRecursive(15,4) ]
[ delete(4) ]
```

After:

```text
TOP
 ↓

[ deleteRecursive(8,4) ]  ← receives NULL
[ deleteRecursive(10,4) ]
[ deleteRecursive(15,4) ]
[ delete(4) ]
```

---

# ⭐ Ab `8` ko NULL mila

8 ke code mein ye line pending thi:

```java
current.left = deleteRecursive(current.left, value);
```

Ab recursive function ne return kiya:

```text
null
```

Therefore:

```java
current.left = null;
```

So:

```text
BEFORE:

    8
   /
  4


AFTER:

    8
   /
 null
```

Then 8 ka function:

```java
return current;
```

Current = `8`.

So:

```text
return 8;
```

---

# STEP 6 — 8 bhi pop

Stack:

```text
BEFORE:

[ deleteRecursive(8,4) ]  ← return 8
[ deleteRecursive(10,4) ]
[ deleteRecursive(15,4) ]
[ delete(4) ]
```

After:

```text
[ deleteRecursive(10,4) ] ← receives 8
[ deleteRecursive(15,4) ]
[ delete(4) ]
```

10 ke pending line thi:

```java
current.left = deleteRecursive(current.left, value);
```

So:

```java
10.left = 8;
```

Then:

```java
return current;
```

Current = 10.

So:

```text
return 10;
```

---

# STEP 7 — 10 bhi pop

```text
BEFORE:

[ deleteRecursive(10,4) ] ← return 10
[ deleteRecursive(15,4) ]
[ delete(4) ]
```

After:

```text
[ deleteRecursive(15,4) ] ← receives 10
[ delete(4) ]
```

15 ki pending line:

```java
current.left = deleteRecursive(current.left, value);
```

So:

```java
15.left = 10;
```

Then:

```java
return current;
```

Current = 15.

So:

```text
return 15;
```

---

# STEP 8 — Final

```text
[ deleteRecursive(15,4) ] → return 15
```

Then `delete()`:

```java
root = deleteRecursive(root, 4);
```

gets:

```text
root = 15;
```

Final tree:

```text
             15
            /  \
          10    20
         /     /
        8     16
```

---

# 🔥 Now CASE 2: `delete(10)`

Same original tree:

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

Stack:

```text
TOP
 ↓
deleteRecursive(15,10)
delete(10)
```

---

### 15:

```text
10 < 15
```

Go left.

```text
TOP
 ↓
deleteRecursive(10,10)  ← CURRENT
deleteRecursive(15,10)  ← WAITING
delete(10)
```

---

### 10 found 🎯

```text
current = 10
value = 10
```

Tree around 10:

```text
     10
    /
   8
```

10 has:

```text
left  = 8
right = null
```

So this condition:

```java
if (current.right == null) {
    return current.left;
}
```

is TRUE.

Therefore:

```java
return 8;
```

💥 10's stack frame POP.

Before:

```text
[ deleteRecursive(10,10) ] ← return 8
[ deleteRecursive(15,10) ]
[ delete(10) ]
```

After:

```text
[ deleteRecursive(15,10) ] ← receives 8
[ delete(10) ]
```

---

# 15 receives `8`

15 had been waiting at:

```java
current.left = deleteRecursive(current.left, value);
```

Returned value:

```text
8
```

So:

```java
15.left = 8;
```

Before:

```text
15
/
10
/
8
```

After:

```text
15
/
8
```

Then:

```java
return current;
```

Current = 15.

So:

```text
return 15;
```

---

# 🧠 THE WHOLE STACK IDEA

This is the most important picture:

### `delete(4)`

```text
CALL DOWN ↓

15
 ↓
10
 ↓
8
 ↓
4  ← FOUND
```

Then return comes **back upward**:

```text
RETURN UP ↑

4  → null
       ↓
8  → 8
       ↓
10 → 10
       ↓
15 → 15
```

More accurately:

```text
                    CALL ↓

delete(4)
   │
   ▼
[15] ──────────────┐
   │               │
   ▼               │
[10] ─────────┐    │
   │          │    │
   ▼          │    │
[8] ──────┐   │    │
   │      │   │    │
   ▼      │   │    │
[4]       │   │    │
   │      │   │    │
   │ return null
   │      │   │    │
   └──────┘   │    │
      ↑       │    │
   8 gets null│    │
      │       │    │
   return 8   │    │
      ↑       │    │
      └───────┘    │
          ↑        │
      return 10    │
          ↑        │
          └────────┘
             ↑
         return 15
```

### बस यही recursion का game है:

**CALL करते waqt:**

```text
15 → 10 → 8 → 4
```

**RETURN karte waqt:**

```text
4 → 8 → 10 → 15
```

और हर parent **child ke return ko receive karke apna pointer update karta hai**.

```text
4 returns null
      ↓
8.left = null

8 returns 8
      ↓
10.left = 8

10 returns 10
      ↓
15.left = 10

15 returns 15
      ↓
root = 15
```

