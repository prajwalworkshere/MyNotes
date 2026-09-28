# Arrays as Stack By InsertAt(), DeleteAt() By Shifting the Index
```
import java.util.Arrays;

/**
 * Day 2 - Arrays: the most basic data structure.
 * - Fixed size, contiguous block, index -> element in O(1)
 * - Insert / delete in the middle needs SHIFTING -> O(n)
 */
public class ArrayBasics {

    static int shifts;   // counts element moves

    // Insert value at index, shifting everything after it one step right. O(n)
    static void insertAt(int[] a, int size, int index, int value) {
        for (int i = size; i > index; i--) {   // walk from the end towards index
            a[i] = a[i - 1];
            shifts++;
        }
        a[index] = value;
    }

    // Delete element at index, shifting everything after it one step left. O(n)
    static void deleteAt(int[] a, int size, int index) {
        for (int i = index; i < size - 1; i++) {
            a[i] = a[i + 1];
            shifts++;
        }
        a[size - 1] = 0;                        // optional: clear the freed slot
    }

    public static void main(String[] args) {
        // 1. Java arrays are objects with a fixed length and default values
        int[] marks = new int[5];
        String[] names = new String[3];
        System.out.println("int defaults    : " + Arrays.toString(marks));
        System.out.println("String defaults : " + Arrays.toString(names));
        System.out.println("length is fixed : " + marks.length);

        // 2. Random access: a[i] is O(1) - the JVM computes base + i * elementSize
        int[] a = {10, 20, 30, 40, 0, 0, 0, 0};     // capacity 8, 4 used
        int size = 4;
        System.out.println("\na[2] = " + a[2] + "   (one step, no loop)");

        // 3. Insert in the middle: shifting
        shifts = 0;
        insertAt(a, size, 1, 15); size++;
        System.out.println("insert 15 at 1  : " + Arrays.toString(a) + "  shifts = " + shifts);

        shifts = 0;
        insertAt(a, size, size, 50); size++;
        System.out.println("insert 50 at end: " + Arrays.toString(a) + "  shifts = " + shifts);

        shifts = 0;
        deleteAt(a, size, 0); size--;
        System.out.println("delete index 0  : " + Arrays.toString(a) + "  shifts = " + shifts);

        // 4. Bounds are always checked in Java (unlike C)
        try {
            a[8] = 99;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\nBounds check    : " + e.getMessage());
        }

        // 5. 2-D arrays are arrays of arrays - rows can have different lengths (jagged)
        int[][] triangle = new int[4][];
        for (int r = 0; r < triangle.length; r++) {
            triangle[r] = new int[r + 1];
            triangle[r][0] = triangle[r][r] = 1;
            for (int c = 1; c < r; c++) triangle[r][c] = triangle[r - 1][c - 1] + triangle[r - 1][c];
        }
        System.out.println("\nJagged 2-D array (Pascal's triangle):");
        for (int[] row : triangle) System.out.println("  " + Arrays.toString(row));

        // 6. Growing an array = make a bigger one and copy (O(n))
        int[] bigger = Arrays.copyOf(a, a.length * 2);
        System.out.println("\nArrays.copyOf to grow: " + Arrays.toString(bigger));
    }
}

```

## 1. Introduction

An array is one of the most basic data structures in Java.

An array stores multiple values of the **same type** in a fixed-size structure.

For example:

```java
int[] a = {10, 20, 30, 40};
```

Conceptually:

```text
Index:    0    1    2    3
          ↓    ↓    ↓    ↓
Array:   10   20   30   40
```

Important properties of Java arrays:

* Arrays have a **fixed length**.
* Array indexing starts from `0`.
* Accessing an element using an index is **O(1)**.
* Inserting/deleting in the middle requires shifting elements.
* Therefore, middle insertion/deletion is generally **O(n)**.
* Java performs **bounds checking** on every array access.
* A 2-D array in Java is actually an **array of arrays**.
* Rows of a 2-D array can have different lengths.
* To "grow" an array, we create another larger array and copy the elements.

---

# 2. Importing `Arrays`

```java
import java.util.Arrays;
```

The `Arrays` class belongs to:

```text
java.util
```

It provides useful utility methods for working with arrays.

This program mainly uses:

```java
Arrays.toString()
```

and

```java
Arrays.copyOf()
```

---

## 2.1 `Arrays.toString()`

Suppose:

```java
int[] a = {10, 20, 30};
```

If we write:

```java
System.out.println(a);
```

Java does not print the array elements in the normal readable form.

Instead:

```java
System.out.println(Arrays.toString(a));
```

produces:

```text
[10, 20, 30]
```

So this method is mainly being used in this program to **display array contents**.

---

# 3. Class Declaration

```java
public class ArrayBasics {
```

This defines the class `ArrayBasics`.

The methods and variables used by the program are declared inside this class.

---

# 4. `shifts` Variable

```java
static int shifts;
```

This variable counts how many elements were moved during insertion or deletion.

It is mainly used for **demonstration**.

For example:

```text
Before:

[10, 20, 30, 40]
```

If we insert `15` at index `1`:

```text
[10, 15, 20, 30, 40]
```

We had to move:

```text
20 → right
30 → right
40 → right
```

So:

```text
shifts = 3
```

The `shifts` variable is not required for an actual insertion algorithm. It is included to show us how much work the array operation performs.

---

# 5. `insertAt()` Function

## Code

```java
static void insertAt(int[] a, int size, int index, int value) {

    for (int i = size; i > index; i--) {
        a[i] = a[i - 1];
        shifts++;
    }

    a[index] = value;
}
```

This function inserts a new value into an existing array at a specified index.

---

## 5.1 Parameters of `insertAt()`

The function has four parameters:

```java
insertAt(int[] a, int size, int index, int value)
```

### `a`

```java
int[] a
```

The array in which we want to insert the value.

### `size`

```java
int size
```

The number of elements that are currently considered **used**.

This is not the same as `a.length`.

For example:

```java
int[] a = {10, 20, 30, 40, 0, 0, 0, 0};
int size = 4;
```

Here:

```text
Array length = 8
Used elements = 4
```

Conceptually:

```text
Index:   0    1    2    3    4    5    6    7
         ↓    ↓    ↓    ↓
Data:   10   20   30   40    0    0    0    0
         <----------->
            size = 4
```

### `index`

The position where we want to insert the new value.

### `value`

The actual value that we want to insert.

---

# 6. Logic of `insertAt()`

Suppose:

```text
Array:

[10, 20, 30, 40, 0, 0, 0, 0]

size = 4
```

We want:

```text
insert 15 at index 1
```

The desired result is:

```text
[10, 15, 20, 30, 40, 0, 0, 0]
```

But there is already `20` at index `1`.

Therefore, we must first create an empty position.

We do this by shifting elements to the right.

---

## 6.1 The `for` loop

```java
for (int i = size; i > index; i--) {
```

Notice that:

```java
i--
```

is used.

This means the loop moves **backwards**.

For:

```text
size = 4
index = 1
```

the values of `i` will be:

```text
4
3
2
```

---

## 6.2 First iteration

```text
i = 4
```

Execute:

```java
a[4] = a[3];
```

So:

```text
40 → index 4
```

Array becomes:

```text
[10, 20, 30, 40, 40, 0, 0, 0]
```

Then:

```java
shifts++;
```

So:

```text
shifts = 1
```

---

## 6.3 Second iteration

```text
i = 3
```

Execute:

```java
a[3] = a[2];
```

Therefore:

```text
30 → index 3
```

Array:

```text
[10, 20, 30, 30, 40, 0, 0, 0]
```

Now:

```text
shifts = 2
```

---

## 6.4 Third iteration

```text
i = 2
```

Execute:

```java
a[2] = a[1];
```

Therefore:

```text
20 → index 2
```

Array:

```text
[10, 20, 20, 30, 40, 0, 0, 0]
```

Now:

```text
shifts = 3
```

---

## 6.5 Loop stops

Now:

```text
i = 1
```

Condition:

```java
i > index
```

becomes:

```text
1 > 1
```

which is false.

The loop stops.

---

## 6.6 Put the new value

Now:

```java
a[index] = value;
```

means:

```java
a[1] = 15;
```

Final result:

```text
[10, 15, 20, 30, 40, 0, 0, 0]
```

So the complete insertion was:

```text
Before:

[10, 20, 30, 40, 0, 0, 0, 0]

Shift 40:

[10, 20, 30, 40, 40, 0, 0, 0]

Shift 30:

[10, 20, 30, 30, 40, 0, 0, 0]

Shift 20:

[10, 20, 20, 30, 40, 0, 0, 0]

Insert 15:

[10, 15, 20, 30, 40, 0, 0, 0]
```

---

# 7. Why Does `insertAt()` Shift From Right to Left?

This is extremely important.

Suppose:

```text
[10, 20, 30, 40]
```

We want to insert `15` at index `1`.

We need:

```text
20 → index 2
30 → index 3
40 → index 4
```

Therefore we start from the end:

```text
40 → 4
30 → 3
20 → 2
```

If we moved from left to right, we could overwrite values before they had been copied.

### Rule

> When shifting elements to the right, always move from right to left.

---

# 8. Why Is `insertAt()` O(n)?

Suppose we have:

```text
[10, 20, 30, 40, 50, 60, 70]
```

and insert near the beginning.

Many elements have to move:

```text
20 → right
30 → right
40 → right
50 → right
60 → right
70 → right
```

Therefore, the number of operations grows with the number of elements.

Hence:

```text
Insertion in middle/beginning = O(n)
```

---

# 9. Inserting at the End

The same function can also insert at the end.

```java
insertAt(a, size, size, 50);
```

Suppose:

```text
size = 5
```

Then:

```text
index = 5
```

The loop condition becomes:

```text
i > index
5 > 5
```

which is false immediately.

Therefore:

```text
No shifting
```

Then:

```java
a[5] = 50;
```

Result:

```text
[10, 15, 20, 30, 40, 50, 0, 0]
```

So when there is available capacity:

```text
Insert at end = O(1)
```

---

# 10. `size++`

After insertion:

```java
size++;
```

Suppose:

```text
size = 4
```

After inserting one element:

```text
size = 5
```

This means there are now five meaningful elements.

### Important distinction

`size++` does **not** increase the actual Java array.

If:

```java
a.length == 8
```

it remains:

```text
a.length == 8
```

Only our logical `size` variable changes.

---

# 11. `deleteAt()` Function

## Code

```java
static void deleteAt(int[] a, int size, int index) {

    for (int i = index; i < size - 1; i++) {
        a[i] = a[i + 1];
        shifts++;
    }

    a[size - 1] = 0;
}
```

This function deletes an element at a particular index.

Unlike insertion, deletion shifts elements **to the left**.

---

# 12. Parameters of `deleteAt()`

```java
deleteAt(int[] a, int size, int index)
```

### `a`

The array.

### `size`

Number of currently used elements.

### `index`

The position of the element we want to delete.

---

# 13. Example of `deleteAt()`

Suppose:

```text
[10, 15, 20, 30, 40, 50, 0, 0]
```

and:

```text
size = 6
```

We execute:

```java
deleteAt(a, size, 0);
```

We want to remove:

```text
10
```

Desired result:

```text
[15, 20, 30, 40, 50, 0, 0, 0]
```

---

# 14. How Deletion Works

The important statement is:

```java
a[i] = a[i + 1];
```

This means:

> Copy the element immediately to the right into the current position.

---

## Iteration 1

```text
i = 0
```

```java
a[0] = a[1];
```

So:

```text
15 → index 0
```

Array:

```text
[15, 15, 20, 30, 40, 50, 0, 0]
```

---

## Iteration 2

```text
i = 1
```

```java
a[1] = a[2];
```

So:

```text
20 → index 1
```

Array:

```text
[15, 20, 20, 30, 40, 50, 0, 0]
```

---

## Iteration 3

```java
a[2] = a[3];
```

Result:

```text
[15, 20, 30, 30, 40, 50, 0, 0]
```

---

## Iteration 4

```java
a[3] = a[4];
```

Result:

```text
[15, 20, 30, 40, 40, 50, 0, 0]
```

---

## Iteration 5

```java
a[4] = a[5];
```

Result:

```text
[15, 20, 30, 40, 50, 50, 0, 0]
```

Now the meaningful elements are:

```text
15 20 30 40 50
```

---

# 15. Clearing the Last Position

Finally:

```java
a[size - 1] = 0;
```

Since:

```text
size = 6
```

we get:

```java
a[5] = 0;
```

Result:

```text
[15, 20, 30, 40, 50, 0, 0, 0]
```

### Important

The actual deletion was performed by shifting.

This line:

```java
a[size - 1] = 0;
```

only clears the old leftover value.

---

# 16. Why Does Deletion Shift Left?

Suppose:

```text
[10, 20, 30, 40, 50]
     ↑
   delete
```

After removing `20`, we want:

```text
[10, 30, 40, 50]
```

Therefore:

```text
30 → left
40 → left
50 → left
```

### Rule

> When shifting elements to the left after deletion, move from left to right.

---

# 17. Complexity of Deletion

Deleting from the beginning:

```text
[10, 20, 30, 40, 50]
 ↑
delete
```

requires:

```text
20 → left
30 → left
40 → left
50 → left
```

Therefore:

```text
O(n)
```

Deleting the last used element does not require shifting:

```text
[10, 20, 30, 40, 50]
                  ↑
                delete
```

So it can be:

```text
O(1)
```

---

# 18. `main()` Method

```java
public static void main(String[] args) {
```

Execution of the Java program begins from the `main()` method.

The program demonstrates several array concepts inside `main()`.

---

# 19. Creating Arrays and Default Values

```java
int[] marks = new int[5];
String[] names = new String[3];
```

## `marks`

```java
int[] marks = new int[5];
```

Creates:

```text
[0, 0, 0, 0, 0]
```

because the default value of `int` is:

```text
0
```

## `names`

```java
String[] names = new String[3];
```

Creates:

```text
[null, null, null]
```

because the default value of a reference type is:

```text
null
```

---

# 20. Array Default Values

Some important defaults:

| Array type             | Default value |
| ---------------------- | ------------- |
| `int[]`                | `0`           |
| `long[]`               | `0`           |
| `float[]`              | `0.0`         |
| `double[]`             | `0.0`         |
| `char[]`               | `'\u0000'`    |
| `boolean[]`            | `false`       |
| Object/reference array | `null`        |

---

# 21. Fixed Length

```java
System.out.println("length is fixed : " + marks.length);
```

Output:

```text
length is fixed : 5
```

Once we create:

```java
new int[5]
```

the array length is fixed.

We cannot do:

```java
marks.length = 10;
```

This is invalid.

If we need a larger array, we must create another array.

---

# 22. Random Access

```java
int[] a = {10, 20, 30, 40, 0, 0, 0, 0};
```

The array contains:

```text
Index:   0    1    2    3    4    5    6    7
         ↓    ↓    ↓    ↓    ↓    ↓    ↓    ↓
Value:  10   20   30   40    0    0    0    0
```

Then:

```java
int size = 4;
```

means:

```text
Used = 4
Capacity = 8
```

---

# 23. Accessing `a[2]`

```java
System.out.println("a[2] = " + a[2]);
```

Index `2` contains:

```text
30
```

So:

```text
a[2] = 30
```

Array access is considered:

```text
O(1)
```

because the requested index can be accessed directly without scanning all previous elements.

---

# 24. Complete Insertion Flow

Initially:

```text
a = [10, 20, 30, 40, 0, 0, 0, 0]
size = 4
```

Call:

```java
insertAt(a, size, 1, 15);
```

Result:

```text
[10, 15, 20, 30, 40, 0, 0, 0]
```

Then:

```java
size++;
```

Therefore:

```text
size = 5
```

Number of shifts:

```text
3
```

---

# 25. Complete Second Insertion

Call:

```java
insertAt(a, size, size, 50);
```

Current:

```text
[10, 15, 20, 30, 40, 0, 0, 0]
```

Since insertion is at index `5`, which is the end of the used portion, no shifting occurs.

Result:

```text
[10, 15, 20, 30, 40, 50, 0, 0]
```

Then:

```java
size++;
```

Therefore:

```text
size = 6
```

Shifts:

```text
0
```

---

# 26. Complete Deletion Flow

Call:

```java
deleteAt(a, size, 0);
```

Before:

```text
[10, 15, 20, 30, 40, 50, 0, 0]
```

Delete:

```text
10
```

Shift:

```text
15 → index 0
20 → index 1
30 → index 2
40 → index 3
50 → index 4
```

Result:

```text
[15, 20, 30, 40, 50, 0, 0, 0]
```

Then:

```java
size--;
```

So:

```text
size = 5
```

Number of shifts:

```text
5
```

---

# 27. Array Bounds Checking

The code:

```java
try {
    a[8] = 99;
}
catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("\nBounds check : " + e.getMessage());
}
```

The array has:

```text
a.length = 8
```

Therefore valid indexes are:

```text
0 1 2 3 4 5 6 7
```

But the program tries:

```java
a[8] = 99;
```

Index `8` is invalid.

Java therefore throws:

```text
ArrayIndexOutOfBoundsException
```

The `catch` block handles it.

---

# 28. Why Does Java Check Array Bounds?

Java arrays are designed to provide memory safety.

For an access:

```java
a[index]
```

Java checks conceptually:

```text
0 <= index < a.length
```

If this condition is false, an exception occurs.

For example:

```text
length = 8

Valid:
0 <= index < 8

Therefore:
0,1,2,3,4,5,6,7
```

Invalid:

```text
-1
8
9
100
```

---

# 29. 2-D Arrays in Java

```java
int[][] triangle = new int[4][];
```

This is an important Java concept.

A 2-D array is technically an:

> **Array of arrays**

The outer array has four elements:

```text
triangle
   |
   +---- triangle[0]
   +---- triangle[1]
   +---- triangle[2]
   +---- triangle[3]
```

Initially:

```text
[null, null, null, null]
```

The rows are created separately.

---

# 30. Creating Jagged Rows

```java
for (int r = 0; r < triangle.length; r++) {
    triangle[r] = new int[r + 1];
```

For every row, its size is:

```text
r + 1
```

Therefore:

| `r` | Row size |
| --: | -------: |
|   0 |        1 |
|   1 |        2 |
|   2 |        3 |
|   3 |        4 |

So we get:

```text
Row 0 → [ ]
Row 1 → [ ][ ]
Row 2 → [ ][ ][ ]
Row 3 → [ ][ ][ ][ ]
```

This is called a **jagged array** because the rows can have different lengths.

---

# 31. Pascal's Triangle Logic

This line:

```java
triangle[r][0] = triangle[r][r] = 1;
```

sets the first and last element of every row to `1`.

For example:

```text
[1, ?, ?, 1]
```

Then the inner values are calculated using:

```java
triangle[r][c] =
    triangle[r - 1][c - 1] +
    triangle[r - 1][c];
```

For example:

```text
Previous row:

[1, 2, 1]
```

Next row:

```text
1
```

then:

```text
1 + 2 = 3
```

then:

```text
2 + 1 = 3
```

then:

```text
1
```

Therefore:

```text
[1, 3, 3, 1]
```

---

# 32. Final Jagged Array

The final structure is:

```text
[1]

[1, 1]

[1, 2, 1]

[1, 3, 3, 1]
```

Notice that every row has a different length.

Therefore:

```text
triangle[0].length = 1
triangle[1].length = 2
triangle[2].length = 3
triangle[3].length = 4
```

---

# 33. Enhanced `for` Loop

```java
for (int[] row : triangle)
    System.out.println("  " + Arrays.toString(row));
```

This means:

> Take each row from `triangle` one by one and store it in the variable `row`.

Iteration:

```text
row = [1]
row = [1, 1]
row = [1, 2, 1]
row = [1, 3, 3, 1]
```

Each row is then printed.

---

# 34. Growing an Array Using `Arrays.copyOf()`

```java
int[] bigger = Arrays.copyOf(a, a.length * 2);
```

Suppose:

```text
a.length = 8
```

Then:

```text
a.length * 2 = 16
```

So Java creates a new array:

```text
bigger = new int[16]
```

and copies the old array's contents into it.

Conceptually:

```text
Original:

a
↓
[15, 20, 30, 40, 50, 0, 0, 0]

                 COPY
                   ↓

bigger
↓
[15, 20, 30, 40, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
```

---

# 35. Does `Arrays.copyOf()` Resize the Original Array?

No.

This is extremely important.

Before:

```text
a.length = 8
```

After:

```java
int[] bigger = Arrays.copyOf(a, 16);
```

we have:

```text
a.length      = 8
bigger.length = 16
```

The original array remains unchanged.

Conceptually:

```text
a
 |
 ↓
[15,20,30,40,50,0,0,0]


bigger
 |
 ↓
[15,20,30,40,50,0,0,0,0,0,0,0,0,0,0,0]
```

There are now two different arrays.

---

# 36. Why Is `Arrays.copyOf()` O(n)?

Because the elements must be copied from the old array into the new array.

For example:

```text
Old:
[10, 20, 30, 40, 50]
```

The values have to be copied:

```text
10 → new array
20 → new array
30 → new array
40 → new array
50 → new array
```

Therefore:

```text
Arrays.copyOf() = O(n)
```

---

# 37. Complete Program Flow

The program demonstrates the following concepts in order:

```text
1. Import Arrays
       ↓
2. Create arrays
       ↓
3. Observe default values
       ↓
4. Observe fixed length
       ↓
5. Access an element using index
       ↓
6. Insert in the middle
       ↓
7. Shift elements right
       ↓
8. Insert at end
       ↓
9. Delete from beginning
       ↓
10. Shift elements left
       ↓
11. Bounds checking
       ↓
12. 2-D arrays
       ↓
13. Jagged arrays
       ↓
14. Arrays.copyOf()
       ↓
15. Create a larger array
```

---

# 38. Time Complexity Summary

| Operation           | Complexity | Reason                         |
| ------------------- | ---------: | ------------------------------ |
| Access `a[i]`       |   **O(1)** | Direct index access            |
| Update `a[i]`       |   **O(1)** | Direct index access            |
| Insert at beginning |   **O(n)** | Shift elements right           |
| Insert in middle    |   **O(n)** | Shift elements right           |
| Insert at end       |   **O(1)** | No shifting if capacity exists |
| Delete beginning    |   **O(n)** | Shift elements left            |
| Delete middle       |   **O(n)** | Shift elements left            |
| Delete last         |   **O(1)** | No shifting                    |
| `Arrays.copyOf()`   |   **O(n)** | Copies elements                |
| `a.length`          |   **O(1)** | Stored array property          |

---

# 39. Most Important Concept: `length` vs `size`

This program deliberately uses:

```java
int[] a = {10, 20, 30, 40, 0, 0, 0, 0};
int size = 4;
```

There are two different concepts here.

## `a.length`

Actual Java array length:

```text
8
```

It is fixed.

## `size`

Logical number of elements currently being used:

```text
4
```

It is just a normal integer variable maintained by our program.

After inserting:

```text
size++
```

After deleting:

```text
size--
```

But:

```text
a.length
```

doesn't change.

---

# 40. Visualizing `length` and `size`

Suppose:

```text
a.length = 8
size = 5
```

Then:

```text
Index:    0    1    2    3    4    5    6    7
          ↓    ↓    ↓    ↓    ↓
Data:    10   20   30   40   50    0    0    0
          <-------------------->
                 size = 5

          <------------------------------------>
                    length = 8
```

Therefore:

```text
length = total capacity
size   = currently used elements
```

Again, `size` is **not built into a normal Java array**. We created it ourselves.

---

# 41. Core Learning From This Program

The most important idea behind this entire program is the trade-off of arrays.

## Array gives very fast access

```java
a[500]
```

can directly access an element.

Therefore:

```text
Access = O(1)
```

But arrays have fixed positions.

If we insert something in the middle:

```text
[10, 20, 30, 40]
      ↑
    insert
```

we need to move existing elements:

```text
30 → right
40 → right
```

Therefore:

```text
Middle insertion = O(n)
```

Similarly, deletion requires elements to move left.

Therefore:

```text
Middle deletion = O(n)
```

---

# 42. Final Mental Model

Remember arrays using this model:

```text
                    ARRAY
                      |
          +-----------+-----------+
          |                       |
      Fixed Length             Indexing
          |                       |
      Cannot resize              O(1)
          |
          |
     Insert/Delete
          |
      +---+---+
      |       |
   Insert   Delete
      |       |
   Shift →  Shift ←
      |       |
    O(n)     O(n)
```

And for growing:

```text
Old Array
    |
    | Arrays.copyOf()
    ↓
New Bigger Array
    |
    ↓
Copy elements
```

The original array itself is **never resized**.

---

# 43. Interview-Level Explanation

If an interviewer asks:

### "Why is array access O(1) but insertion in the middle O(n)?"

A good answer is:

> An array provides direct indexed access because the location of an element can be determined from its index, so accessing `a[i]` takes constant time, O(1). However, when inserting an element in the middle, existing elements after that position must be shifted one position to the right to maintain their order. The number of elements that need to be shifted can grow with the size of the array, so insertion in the middle takes O(n). Similarly, deleting from the middle requires shifting subsequent elements to the left, which also takes O(n).

---

# 44. Key Rules to Remember

### Rule 1

```text
Array index starts at 0.
```

### Rule 2

```text
Last valid index = length - 1
```

### Rule 3

```text
a[i] → O(1)
```

### Rule 4

```text
Insert right → shift from RIGHT to LEFT
```

### Rule 5

```text
Delete left → shift from LEFT to RIGHT
```

### Rule 6

```text
Java arrays have fixed length.
```

### Rule 7

```text
Java checks array bounds.
```

### Rule 8

```text
2-D Java array = array of arrays.
```

### Rule 9

```text
Rows can have different lengths.
```

### Rule 10

```text
Arrays.copyOf() creates a NEW array.
```

### Rule 11

```text
size is not the same thing as array.length.
```

### Rule 12

```text
Arrays.copyOf() = O(n)
```
