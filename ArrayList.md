# Java `ArrayList` — Complete Detailed Notes

We’ll learn `ArrayList` from **absolute basics → internal working → memory → methods → resizing → complexity → iteration → generics → comparisons → pitfalls → interview questions → practice**.

---

# 1. What is an `ArrayList`?

`ArrayList` is a **resizable array implementation** provided by the Java Collections Framework.

It is present in:

```java
java.util.ArrayList
```

Example:

```java
ArrayList<Integer> numbers = new ArrayList<>();

numbers.add(10);
numbers.add(20);
numbers.add(30);

System.out.println(numbers);
```

Output:

```text
[10, 20, 30]
```

The important idea is:

> An `ArrayList` behaves like a dynamically growing array.

A normal Java array has a **fixed size**:

```java
int[] arr = new int[5];
```

Once created, its length cannot change.

But an `ArrayList` can grow and shrink logically:

```java
ArrayList<Integer> list = new ArrayList<>();

list.add(10);
list.add(20);
list.add(30);
```

You don't have to specify the final number of elements beforehand.

---

# 2. Why was `ArrayList` needed?

Suppose you use an array:

```java
int[] marks = new int[5];
```

You have room for exactly 5 elements.

What if you later need 6?

You cannot do:

```java
arr.length = 6;   // ❌
```

Java arrays have fixed length.

You would have to create another array:

```java
int[] newArr = new int[10];
```

and manually copy the elements.

`ArrayList` handles this resizing process internally.

So instead of manually doing:

```text
old array
   ↓
create bigger array
   ↓
copy elements
   ↓
replace old array
```

`ArrayList` manages this for you.

---

# 3. Where does `ArrayList` belong?

`ArrayList` is part of the **Java Collections Framework**.

A simplified hierarchy is:

```text
Iterable
   |
Collection
   |
List
   |
ArrayList
```

More accurately:

```text
Iterable
   |
Collection
   |
List
   |
ArrayList
```

`ArrayList` is a **class**.

`List` is an **interface**.

Therefore, this is very common:

```java
List<Integer> list = new ArrayList<>();
```

instead of:

```java
ArrayList<Integer> list = new ArrayList<>();
```

We'll understand why later.

---

# 4. Importing `ArrayList`

Because `ArrayList` is inside `java.util`, you normally import it:

```java
import java.util.ArrayList;
```

Then:

```java
ArrayList<Integer> list = new ArrayList<>();
```

Alternatively:

```java
import java.util.*;
```

Then all commonly used classes from `java.util` become available.

Example:

```java
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        ArrayList<Integer> numbers = new ArrayList<>();

        numbers.add(10);
        numbers.add(20);
        numbers.add(30);

        System.out.println(numbers);
    }
}
```

Output:

```text
[10, 20, 30]
```

---

# 5. Array vs ArrayList

This distinction is extremely important.

| Feature                       | Array  | ArrayList          |
| ----------------------------- | ------ | ------------------ |
| Size                          | Fixed  | Dynamically grows  |
| Primitive types               | Yes    | No directly        |
| Objects                       | Yes    | Yes                |
| `length`                      | Yes    | No                 |
| `size()`                      | No     | Yes                |
| `add()`                       | No     | Yes                |
| `remove()`                    | No     | Yes                |
| Part of Collections Framework | No     | Yes                |
| Resizing                      | Manual | Internally handled |
| Generics                      | No     | Yes                |
| Random access                 | Fast   | Fast               |

Example array:

```java
int[] arr = new int[5];

System.out.println(arr.length);
```

Example ArrayList:

```java
ArrayList<Integer> list = new ArrayList<>();

System.out.println(list.size());
```

Remember:

```text
Array → length
ArrayList → size()
```

---

# 6. Creating an ArrayList

## Basic syntax

```java
ArrayList<Type> variableName = new ArrayList<>();
```

Example:

```java
ArrayList<Integer> numbers = new ArrayList<>();
```

For strings:

```java
ArrayList<String> names = new ArrayList<>();
```

For doubles:

```java
ArrayList<Double> prices = new ArrayList<>();
```

For custom objects:

```java
ArrayList<Student> students = new ArrayList<>();
```

---

# 7. Why do we write `<Integer>`?

This is called a **generic type parameter**.

```java
ArrayList<Integer>
```

means:

> This ArrayList is intended to store `Integer` objects.

For example:

```java
ArrayList<String> names = new ArrayList<>();
```

means:

```text
This list stores String objects.
```

Then:

```java
names.add("Prajwal");
names.add("Rahul");
names.add("Amit");
```

But:

```java
names.add(100);   // ❌
```

will not compile.

Generics provide **compile-time type safety**.

---

# 8. Why can't we use `int`?

You cannot write:

```java
ArrayList<int> list;   // ❌
```

because Java generics work with **reference types**, not primitive types.

So we use wrapper classes.

```text
int     → Integer
double  → Double
char    → Character
boolean → Boolean
long    → Long
float   → Float
short   → Short
byte    → Byte
```

Therefore:

```java
ArrayList<Integer>
ArrayList<Double>
ArrayList<Character>
ArrayList<Boolean>
```

---

# 9. Autoboxing

Consider:

```java
ArrayList<Integer> list = new ArrayList<>();

list.add(10);
```

`10` is an `int` primitive.

But the list expects:

```java
Integer
```

Java automatically converts:

```text
int
 ↓
Integer
```

This is called **autoboxing**.

Conceptually:

```java
list.add(Integer.valueOf(10));
```

Java performs this conversion automatically.

---

# 10. Unboxing

Now:

```java
Integer x = list.get(0);
```

is straightforward.

But:

```java
int x = list.get(0);
```

also works.

Java automatically converts:

```text
Integer
   ↓
 int
```

This is called **unboxing**.

Conceptually:

```java
int x = list.get(0).intValue();
```

---

# 11. Adding elements — `add()`

The most basic ArrayList operation is:

```java
add()
```

Example:

```java
ArrayList<String> names = new ArrayList<>();

names.add("Prajwal");
names.add("Rahul");
names.add("Amit");
```

The list becomes:

```text
[Prajwal, Rahul, Amit]
```

Elements are inserted at the end by default.

---

# 12. Indexing

ArrayList uses **zero-based indexing**, just like arrays.

```java
ArrayList<String> names = new ArrayList<>();

names.add("Prajwal"); // index 0
names.add("Rahul");   // index 1
names.add("Amit");    // index 2
```

Representation:

```text
Index:     0          1       2
          ┌──────────┬───────┬───────┐
List  →   │ Prajwal  │ Rahul │ Amit  │
          └──────────┴───────┴───────┘
```

---

# 13. `get()`

To retrieve an element:

```java
get(index)
```

Example:

```java
ArrayList<String> names = new ArrayList<>();

names.add("Prajwal");
names.add("Rahul");
names.add("Amit");

System.out.println(names.get(0));
System.out.println(names.get(1));
System.out.println(names.get(2));
```

Output:

```text
Prajwal
Rahul
Amit
```

Important:

```java
names.get(3);
```

causes:

```text
IndexOutOfBoundsException
```

because index `3` does not exist.

---

# 14. `set()`

`set()` replaces an existing element.

Syntax:

```java
list.set(index, newValue);
```

Example:

```java
ArrayList<String> names = new ArrayList<>();

names.add("Prajwal");
names.add("Rahul");
names.add("Amit");

names.set(1, "Rohit");

System.out.println(names);
```

Output:

```text
[Prajwal, Rohit, Amit]
```

Important distinction:

```java
set()
```

**replaces** an element.

It does not increase the list size.

---

# 15. `add(index, element)`

You can insert an element at a specific position.

```java
list.add(index, element);
```

Example:

```java
ArrayList<String> names = new ArrayList<>();

names.add("Prajwal");
names.add("Rahul");
names.add("Amit");

names.add(1, "Rohit");

System.out.println(names);
```

Before:

```text
0 → Prajwal
1 → Rahul
2 → Amit
```

After:

```text
0 → Prajwal
1 → Rohit
2 → Rahul
3 → Amit
```

The existing elements from that index onward are shifted to the right.

---

# 16. `size()`

To find the number of elements:

```java
list.size()
```

Example:

```java
ArrayList<Integer> numbers = new ArrayList<>();

numbers.add(10);
numbers.add(20);
numbers.add(30);

System.out.println(numbers.size());
```

Output:

```text
3
```

Do not write:

```java
numbers.length
```

That's for arrays.

Correct:

```java
numbers.size()
```

---

# 17. `remove()`

There are two important forms:

```java
remove(index)
```

and

```java
remove(object)
```

This is one of the most important ArrayList interview areas.

---

## 17.1 Removing by index

```java
ArrayList<String> names = new ArrayList<>();

names.add("A");
names.add("B");
names.add("C");

names.remove(1);

System.out.println(names);
```

Output:

```text
[A, C]
```

`B` was removed.

---

# 18. Removing by object

Example:

```java
ArrayList<String> names = new ArrayList<>();

names.add("A");
names.add("B");
names.add("C");

names.remove("B");

System.out.println(names);
```

Output:

```text
[A, C]
```

Here Java removes the first matching object.

---

# 19. The famous `remove(1)` problem

Consider:

```java
ArrayList<Integer> numbers = new ArrayList<>();

numbers.add(10);
numbers.add(20);
numbers.add(30);

numbers.remove(1);
```

What gets removed?

`20`.

Why?

Because:

```java
remove(int index)
```

matches the integer argument.

So:

```java
numbers.remove(1);
```

means:

> Remove the element at index 1.

It does **not** mean remove the value `1`.

---

# 20. How to remove the integer value `20`

Use:

```java
numbers.remove(Integer.valueOf(20));
```

Example:

```java
ArrayList<Integer> numbers = new ArrayList<>();

numbers.add(10);
numbers.add(20);
numbers.add(30);

numbers.remove(Integer.valueOf(20));

System.out.println(numbers);
```

Output:

```text
[10, 30]
```

This is a very common interview question.

---

# 21. `contains()`

Checks whether an element exists.

```java
list.contains(value)
```

Example:

```java
ArrayList<String> names = new ArrayList<>();

names.add("Prajwal");
names.add("Rahul");

System.out.println(names.contains("Rahul"));
System.out.println(names.contains("Amit"));
```

Output:

```text
true
false
```

---

# 22. `indexOf()`

Returns the index of the first occurrence.

```java
ArrayList<String> names = new ArrayList<>();

names.add("A");
names.add("B");
names.add("A");

System.out.println(names.indexOf("A"));
```

Output:

```text
0
```

If the element isn't found:

```java
System.out.println(names.indexOf("X"));
```

Output:

```text
-1
```

---

# 23. `lastIndexOf()`

Returns the index of the last occurrence.

```java
ArrayList<String> names = new ArrayList<>();

names.add("A");
names.add("B");
names.add("A");

System.out.println(names.lastIndexOf("A"));
```

Output:

```text
2
```

---

# 24. `isEmpty()`

Checks whether the list contains zero elements.

```java
ArrayList<String> list = new ArrayList<>();

System.out.println(list.isEmpty());
```

Output:

```text
true
```

After adding:

```java
list.add("Hello");

System.out.println(list.isEmpty());
```

Output:

```text
false
```

---

# 25. `clear()`

Removes all elements.

```java
ArrayList<String> names = new ArrayList<>();

names.add("A");
names.add("B");
names.add("C");

names.clear();

System.out.println(names);
```

Output:

```text
[]
```

And:

```java
names.size()
```

becomes:

```text
0
```

---

# 26. Does `clear()` destroy the ArrayList object?

No.

It removes the elements from the list.

The ArrayList object itself still exists.

```java
names.clear();

names.add("X");
```

is perfectly valid.

---

# 27. Allowing duplicate elements

ArrayList allows duplicates.

```java
ArrayList<String> names = new ArrayList<>();

names.add("A");
names.add("B");
names.add("A");
names.add("A");
```

Result:

```text
[A, B, A, A]
```

Unlike `Set`, `ArrayList` does not automatically eliminate duplicates.

---

# 28. Maintaining insertion order

ArrayList maintains the order in which elements were added.

```java
list.add("C");
list.add("A");
list.add("B");
```

Output:

```text
[C, A, B]
```

It doesn't automatically sort them.

---

# 29. Can ArrayList contain `null`?

Yes.

Example:

```java
ArrayList<String> list = new ArrayList<>();

list.add("A");
list.add(null);
list.add("B");
list.add(null);

System.out.println(list);
```

Output:

```text
[A, null, B, null]
```

ArrayList can contain multiple `null` references.

---

# 30. Can ArrayList contain objects?

Absolutely.

For example:

```java
class Student {

    String name;
    int age;

    Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

Then:

```java
ArrayList<Student> students = new ArrayList<>();

students.add(new Student("Prajwal", 21));
students.add(new Student("Rahul", 22));
```

Now the list stores references to `Student` objects.

---

# 31. Important memory concept

Suppose:

```java
ArrayList<Student> students = new ArrayList<>();

Student s1 = new Student("A", 20);

students.add(s1);
```

Conceptually:

```text
Stack
──────────────

students ───────────────┐
s1 ──────────────────────┤
                         ↓
                     ArrayList object
                         |
                         ↓
                    internal array
                         |
                         ↓
                       s1 reference
                         |
                         ↓
                    Student object
```

The ArrayList does not magically store the entire object inside itself.

For object types, the underlying array stores **references to objects**.

---

# 32. Internal structure of ArrayList

This is extremely important.

Conceptually, an ArrayList contains something similar to:

```java
Object[] elementData;
```

Internally, ArrayList maintains an array to hold its elements.

Conceptually:

```text
ArrayList object
      |
      | elementData
      ↓
┌──────┬──────┬──────┬──────┬──────┐
│  A   │  B   │  C   │ null │ null │
└──────┴──────┴──────┴──────┴──────┘
   0      1      2
```

The logical list contains:

```text
[A, B, C]
```

while the internal backing array may have additional unused capacity.

---

# 33. Size vs Capacity

This is one of the most important concepts.

Suppose:

```java
ArrayList<Integer> list = new ArrayList<>();
```

Then:

```text
size = 0
```

After:

```java
list.add(10);
list.add(20);
list.add(30);
```

logical size:

```text
size = 3
```

But internally, the backing array can have a capacity greater than 3.

So:

```text
SIZE
=
number of actual elements

CAPACITY
=
number of elements the internal array can currently hold
before another resize is necessary
```

These are different concepts.

---

# 34. Why does ArrayList need capacity?

Imagine every `add()` required creating a brand-new array.

Suppose:

```text
Add 1 → create array
Add 2 → create bigger array + copy
Add 3 → create bigger array + copy
Add 4 → create bigger array + copy
...
```

That would be inefficient.

Instead, ArrayList allocates extra capacity.

Conceptually:

```text
Size = 3

Internal capacity = 10

┌────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐
│ 10 │ 20 │ 30 │    │    │    │    │    │    │    │
└────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────
  ↑ actual elements
```

Then several more additions can occur without immediately allocating a new array.

---

# 35. How ArrayList grows

When the internal array becomes full and another element must be added, ArrayList creates a larger backing array and copies the existing elements.

Conceptually:

```text
OLD ARRAY

[10][20][30][40][50]
 ↑ full
```

New larger array:

```text
[10][20][30][40][50][ ][ ][ ][ ]
```

Then:

```text
old backing array
       ↓
copy elements
       ↓
new backing array
```

The ArrayList then uses the new backing array.

---

# 36. Growth factor

The exact implementation details can depend on the Java version, but modern OpenJDK implementations traditionally grow the backing array by approximately **1.5×** when expansion is required.

For example, conceptually:

```text
old capacity = 10

new capacity ≈ 15
```

The important point for understanding is:

> ArrayList does not increase its backing array by exactly one element every time.

It grows in larger chunks to make repeated additions efficient.

For production/interview discussion, avoid assuming a particular growth number as a universal Java specification guarantee.

---

# 37. Why is `add()` considered O(1)?

Usually:

```java
list.add(value);
```

is **amortized O(1)**.

Most additions simply put the element into the next available slot:

```text
[10][20][30][ ][ ]
             ↑
             add here
```

That's constant-time work.

Occasionally, resizing happens:

```text
old array
    ↓
create bigger array
    ↓
copy n elements
```

That particular operation is O(n).

But resizing doesn't happen on every insertion.

Therefore:

> ArrayList insertion at the end is amortized O(1).

---

# 38. What does "amortized O(1)" mean?

This is important for interviews.

Suppose 10,000 insertions happen.

Most operations are cheap:

```text
add → O(1)
add → O(1)
add → O(1)
...
```

Occasionally:

```text
resize → O(n)
```

When the expensive resizing cost is distributed across many insertions, the average cost per insertion remains constant.

Therefore:

```text
add at end → amortized O(1)
```

---

# 39. Accessing an element

ArrayList provides fast random access.

```java
list.get(500);
```

The internal array can directly access the corresponding position.

Therefore:

```text
get(index) → O(1)
```

Conceptually:

```text
index
 ↓
backing array
 ↓
direct location
```

This is one major advantage of ArrayList.

---

# 40. Inserting in the middle

Suppose:

```text
[A][B][C][D][E]
```

You execute:

```java
list.add(2, "X");
```

The elements must shift:

```text
Before:

[A][B][C][D][E]

After:

[A][B][X][C][D][E]
```

`C`, `D`, and `E` need to move.

Therefore:

```text
insert at beginning/middle → O(n)
```

in the general case.

---

# 41. Removing from the middle

Suppose:

```text
[A][B][C][D][E]
```

Remove index 1:

```java
list.remove(1);
```

After removal:

```text
[A][C][D][E]
```

Elements after the removed element must shift left.

Therefore:

```text
remove from middle → O(n)
```

---

# 42. ArrayList time complexity

| Operation           | Typical complexity |
| ------------------- | -----------------: |
| `get(index)`        |               O(1) |
| `set(index, value)` |               O(1) |
| `add(value)` at end |     Amortized O(1) |
| `add(index, value)` |               O(n) |
| `remove(index)`     |               O(n) |
| `contains(value)`   |               O(n) |
| `indexOf(value)`    |               O(n) |
| `clear()`           |     O(n) generally |
| Iteration           |               O(n) |

Why is `contains()` O(n)?

Because ArrayList generally searches sequentially:

```text
A → B → C → D → E
```

It may need to inspect every element.

---

# 43. `ensureCapacity()`

If you know beforehand that your ArrayList will hold many elements, you can request sufficient capacity.

Example:

```java
ArrayList<Integer> list = new ArrayList<>();

list.ensureCapacity(1000);
```

This tells the ArrayList to ensure enough internal capacity for approximately 1000 elements.

It can reduce repeated resizing when you know the expected size.

Important:

```java
ensureCapacity()
```

does **not** mean the logical size becomes 1000.

After:

```java
list.ensureCapacity(1000);
```

the list is still:

```text
size = 0
```

You cannot do:

```java
list.get(500);  // ❌
```

unless an actual element was added there.

---

# 44. Initial capacity

You can also specify an initial capacity:

```java
ArrayList<Integer> list = new ArrayList<>(1000);
```

This means:

> Start with capacity sufficient for approximately 1000 elements.

It does **not** mean:

```text
size = 1000
```

The size is still:

```text
0
```

This distinction is extremely important.

---

# 45. Initial capacity example

```java
ArrayList<Integer> list = new ArrayList<>(100);

System.out.println(list.size());
```

Output:

```text
0
```

Why?

Because capacity and size are different.

```text
capacity ≈ 100
size = 0
```

---

# 46. Constructor variations

Common constructors include:

### Empty ArrayList

```java
ArrayList<Integer> list = new ArrayList<>();
```

### Initial capacity

```java
ArrayList<Integer> list = new ArrayList<>(50);
```

### From another collection

```java
ArrayList<Integer> list =
        new ArrayList<>(anotherCollection);
```

Example:

```java
ArrayList<Integer> a = new ArrayList<>();

a.add(10);
a.add(20);

ArrayList<Integer> b = new ArrayList<>(a);

System.out.println(b);
```

Output:

```text
[10, 20]
```

This creates a new ArrayList containing the elements of `a`.

---

# 47. Important: copying objects is not deep copying

Suppose:

```java
ArrayList<Student> a = new ArrayList<>();

Student s = new Student("Prajwal", 21);

a.add(s);

ArrayList<Student> b = new ArrayList<>(a);
```

Both lists contain references to the same `Student` object.

Conceptually:

```text
a ──→ [ reference ──→ Student object ]

b ──→ [ reference ──→ Student object ]
                         ↑
                      same object
```

Creating the new ArrayList does not automatically clone every Student.

This is a **shallow copy of the element references**.

---

# 48. Iterating through ArrayList

There are several ways.

---

## 48.1 Traditional `for`

```java
ArrayList<String> names = new ArrayList<>();

names.add("A");
names.add("B");
names.add("C");

for (int i = 0; i < names.size(); i++) {
    System.out.println(names.get(i));
}
```

This is useful when you need the index.

---

# 49. Enhanced `for` loop

```java
for (String name : names) {
    System.out.println(name);
}
```

This is cleaner when you don't need the index.

---

# 50. `Iterator`

```java
Iterator<String> iterator = names.iterator();

while (iterator.hasNext()) {
    String name = iterator.next();
    System.out.println(name);
}
```

Need:

```java
import java.util.Iterator;
```

This becomes particularly important when removing elements safely while iterating.

---

# 51. Removing while iterating

This can cause problems:

```java
for (String name : names) {

    if (name.equals("A")) {
        names.remove(name);
    }
}
```

Modifying an ArrayList structurally while using its normal iterator-based iteration can result in:

```text
ConcurrentModificationException
```

A safe approach is `Iterator.remove()`.

```java
Iterator<String> it = names.iterator();

while (it.hasNext()) {

    String name = it.next();

    if (name.equals("A")) {
        it.remove();
    }
}
```

Here the iterator itself performs the removal.

---

# 52. `removeIf()`

Modern Java also provides:

```java
removeIf()
```

Example:

```java
ArrayList<Integer> numbers = new ArrayList<>();

numbers.add(10);
numbers.add(15);
numbers.add(20);
numbers.add(25);

numbers.removeIf(n -> n % 2 == 0);

System.out.println(numbers);
```

Output:

```text
[15, 25]
```

The lambda:

```java
n -> n % 2 == 0
```

means:

> Remove elements for which this condition is true.

---

# 53. `forEach()`

You can use:

```java
list.forEach(item -> System.out.println(item));
```

Example:

```java
ArrayList<String> names = new ArrayList<>();

names.add("A");
names.add("B");
names.add("C");

names.forEach(name -> System.out.println(name));
```

This uses a lambda expression.

---

# 54. `toArray()`

You can convert an ArrayList into an array.

Example:

```java
ArrayList<String> names = new ArrayList<>();

names.add("A");
names.add("B");
names.add("C");

String[] arr = names.toArray(new String[0]);
```

Now:

```text
ArrayList
    ↓
String[]
```

This is useful when an API expects an array instead of a collection.

---

# 55. `subList()`

You can obtain a view of part of an ArrayList.

```java
ArrayList<Integer> numbers = new ArrayList<>();

numbers.add(10);
numbers.add(20);
numbers.add(30);
numbers.add(40);
numbers.add(50);

List<Integer> part = numbers.subList(1, 4);

System.out.println(part);
```

Output:

```text
[20, 30, 40]
```

Important:

```java
subList(fromIndex, toIndex)
```

uses:

```text
fromIndex → inclusive
toIndex   → exclusive
```

So:

```java
subList(1, 4)
```

means:

```text
1, 2, 3
```

---

# 56. Very important: `subList()` is generally a view

It is not simply an independent copy.

The returned sublist is backed by the original list.

Conceptually:

```text
Original:
[A][B][C][D][E]
     └──────┘
      subList
```

Changes can be reflected between the sublist and original list.

Therefore, be careful when structurally modifying either one.

---

# 57. Sorting an ArrayList

You can use:

```java
Collections.sort(list);
```

Example:

```java
ArrayList<Integer> numbers = new ArrayList<>();

numbers.add(50);
numbers.add(10);
numbers.add(30);
numbers.add(20);

Collections.sort(numbers);

System.out.println(numbers);
```

Output:

```text
[10, 20, 30, 50]
```

Import:

```java
import java.util.Collections;
```

---

# 58. Reverse an ArrayList

```java
Collections.reverse(list);
```

Example:

```java
ArrayList<Integer> numbers = new ArrayList<>();

numbers.add(10);
numbers.add(20);
numbers.add(30);

Collections.reverse(numbers);

System.out.println(numbers);
```

Output:

```text
[30, 20, 10]
```

---

# 59. `Collections` vs `Collection`

These names confuse beginners.

### `Collection`

`Collection` is an interface.

```java
Collection<Integer>
```

### `Collections`

`Collections` is a utility class containing static methods.

For example:

```java
Collections.sort(list);
Collections.reverse(list);
Collections.shuffle(list);
```

Remember:

```text
Collection  → interface
Collections → utility class
```

---

# 60. `List` reference vs `ArrayList` reference

You will frequently see:

```java
List<Integer> list = new ArrayList<>();
```

Why?

Because `List` is an interface and `ArrayList` is an implementation.

```text
List
 ↑
interface

ArrayList
 ↑
implementation
```

The reference type determines what operations are directly available through that reference, while the actual object determines the runtime implementation.

This is a major OOP concept: **programming to an interface**.

---

# 61. Why prefer `List`?

Instead of:

```java
ArrayList<Integer> list = new ArrayList<>();
```

we often write:

```java
List<Integer> list = new ArrayList<>();
```

This allows us to change the implementation later:

```java
List<Integer> list = new LinkedList<>();
```

without changing code that only depends on the `List` interface.

For example:

```java
void process(List<Integer> list) {
    // work with List operations
}
```

Then you can pass:

```java
ArrayList<Integer>
```

or:

```java
LinkedList<Integer>
```

---

# 62. ArrayList and `equals()`

ArrayList compares elements using their equality semantics.

For example:

```java
ArrayList<String> a = new ArrayList<>();
ArrayList<String> b = new ArrayList<>();

a.add("A");
b.add("A");

System.out.println(a.equals(b));
```

Output:

```text
true
```

ArrayList equality is based on corresponding elements and their `equals()` behavior.

---

# 63. `contains()` and `equals()`

Consider:

```java
ArrayList<String> names = new ArrayList<>();

names.add("Prajwal");

System.out.println(names.contains(new String("Prajwal")));
```

Output:

```text
true
```

Why?

Because `contains()` uses equality semantics, essentially relying on `equals()` for matching.

The two String objects can be different objects but still be equal in content.

---

# 64. Custom objects and `equals()`

Suppose:

```java
class Student {

    int id;

    Student(int id) {
        this.id = id;
    }
}
```

Then:

```java
ArrayList<Student> students = new ArrayList<>();

students.add(new Student(101));

System.out.println(students.contains(new Student(101)));
```

Without overriding `equals()`, this will generally be:

```text
false
```

because `Object.equals()` uses reference identity semantics by default.

If you want students with the same ID to be considered equal, you should implement appropriate `equals()` and `hashCode()` methods.

This is an important connection between ArrayList and the Java object equality contract.

---

# 65. `clone()`

ArrayList implements `Cloneable`, and it has a `clone()` method.

Conceptually:

```java
ArrayList<Integer> copy =
        (ArrayList<Integer>) original.clone();
```

Again, this creates a shallow copy of the list structure/references.

For immutable types such as `Integer` or `String`, this distinction is usually less noticeable. For mutable custom objects, both lists can still refer to the same objects.

---

# 66. `trimToSize()`

ArrayList has:

```java
trimToSize()
```

This can reduce the internal capacity to match the current size.

Example concept:

```text
Before:

size = 3
capacity = 20

After trimToSize():

size = 3
capacity ≈ 3
```

This can reduce unused internal storage.

However, it should not be used blindly after every operation because future growth may then require reallocations again.

---

# 67. Fail-fast behavior

ArrayList iterators are generally **fail-fast**.

Suppose:

```java
ArrayList<Integer> list = new ArrayList<>();

list.add(10);
list.add(20);
list.add(30);

Iterator<Integer> it = list.iterator();

list.add(40);

it.next();
```

This can result in:

```text
ConcurrentModificationException
```

Why?

The iterator detects that the list was structurally modified outside the iterator.

Important nuance:

> Fail-fast behavior is a bug-detection mechanism, not a synchronization mechanism or concurrency guarantee.

---

# 68. Is ArrayList thread-safe?

No.

`ArrayList` is **not synchronized** by default.

If multiple threads concurrently modify the same ArrayList without appropriate synchronization, you can encounter race conditions and inconsistent behavior.

For thread-safe use cases, you can consider alternatives such as:

```java
Collections.synchronizedList(...)
```

or concurrent collection classes where appropriate.

---

# 69. Synchronized ArrayList

Example:

```java
List<Integer> list =
        Collections.synchronizedList(new ArrayList<>());
```

This provides synchronized access to the list's operations.

But important:

> Making individual operations synchronized does not automatically make every multi-step sequence of operations atomic.

For example:

```java
if (!list.contains(x)) {
    list.add(x);
}
```

still needs appropriate external synchronization if the entire check-then-add operation must be atomic.

---

# 70. ArrayList vs Vector

Both are resizable-array-based list implementations.

Historically:

```text
Vector → synchronized
ArrayList → not synchronized
```

ArrayList is generally preferred for ordinary single-threaded or externally synchronized use cases.

Vector is a legacy class.

---

# 71. ArrayList vs LinkedList

This is a very common interview comparison.

| Feature            | ArrayList                          | LinkedList                                           |
| ------------------ | ---------------------------------- | ---------------------------------------------------- |
| Internal structure | Dynamic array                      | Doubly linked list                                   |
| `get(index)`       | O(1)                               | O(n)                                                 |
| End insertion      | Amortized O(1)                     | O(1) with appropriate end operation                  |
| Middle insertion   | O(n)                               | O(n) to locate position, then O(1) link manipulation |
| Memory             | Generally lower overhead           | Higher node overhead                                 |
| Random access      | Excellent                          | Poor                                                 |
| Cache locality     | Better                             | Poorer                                               |
| Typical use        | General-purpose list/random access | Specific linked-list/deque scenarios                 |

A common misconception is:

> "LinkedList insertion is always O(1)."

That's incomplete.

If you already have the node/iterator at the correct position, linking can be O(1).

But finding an arbitrary index is generally O(n).

---

# 72. ArrayList vs HashSet

| Feature         | ArrayList        | HashSet                                  |
| --------------- | ---------------- | ---------------------------------------- |
| Duplicates      | Allowed          | Not allowed                              |
| Index           | Yes              | No                                       |
| Insertion order | Maintained       | Not guaranteed as a general Set contract |
| `get(index)`    | Yes              | No                                       |
| Typical lookup  | O(n)             | Average O(1)                             |
| Main purpose    | Ordered sequence | Unique elements                          |

Example:

```java
ArrayList<Integer> list =
        new ArrayList<>();
```

is appropriate when duplicates/order/index matter.

```java
HashSet<Integer> set =
        new HashSet<>();
```

is appropriate when uniqueness is the main requirement.

---

# 73. ArrayList vs normal array

Use an array when:

* fixed size is appropriate
* primitive arrays are useful
* low-level/simple fixed storage is required

Use ArrayList when:

* size changes dynamically
* you need Collection APIs
* you need convenient insertion/removal
* you need generics
* you want a resizable list abstraction

---

# 74. `ArrayList` does not store primitives directly

This is worth remembering.

```java
ArrayList<int> list;       // ❌
```

Instead:

```java
ArrayList<Integer> list;   // ✅
```

The integer values are represented using `Integer` objects, with autoboxing/unboxing making the syntax convenient.

This can have memory/performance implications compared with:

```java
int[]
```

because primitive arrays store primitive values directly, while `ArrayList<Integer>` works with references to `Integer` objects.

---

# 75. Important null behavior

This works:

```java
ArrayList<String> list = new ArrayList<>();

list.add(null);
```

But this:

```java
list.get(0).length();
```

will cause:

```text
NullPointerException
```

if the element at index 0 is `null`.

The ArrayList itself permits `null`; your code must handle it appropriately.

---

# 76. Generic type cannot be changed after declaration

If:

```java
ArrayList<String> list = new ArrayList<>();
```

you cannot add:

```java
list.add(100); // ❌
```

because the compiler enforces the declared generic type.

This prevents many runtime type errors.

---

# 77. Diamond operator `<>`

Instead of:

```java
ArrayList<String> names =
        new ArrayList<String>();
```

modern Java allows:

```java
ArrayList<String> names =
        new ArrayList<>();
```

The compiler infers the type parameter from the left-hand side.

This is called the **diamond operator**.

---

# 78. Adding another collection

ArrayList supports:

```java
addAll()
```

Example:

```java
ArrayList<Integer> a = new ArrayList<>();

a.add(10);
a.add(20);

ArrayList<Integer> b = new ArrayList<>();

b.add(30);
b.add(40);

a.addAll(b);

System.out.println(a);
```

Output:

```text
[10, 20, 30, 40]
```

---

# 79. `addAll(index, collection)`

You can also insert an entire collection at a particular index.

```java
a.addAll(1, b);
```

For example:

```text
A = [10, 20, 50]
B = [30, 40]

A.addAll(2, B)
```

Result:

```text
[10, 20, 30, 40, 50]
```

---

# 80. `containsAll()`

Checks whether all elements of one collection are contained in another.

```java
ArrayList<Integer> a =
        new ArrayList<>();

a.add(10);
a.add(20);
a.add(30);

ArrayList<Integer> b =
        new ArrayList<>();

b.add(10);
b.add(20);

System.out.println(a.containsAll(b));
```

Output:

```text
true
```

---

# 81. `removeAll()`

Removes all elements from the current list that are also present in another collection.

Example:

```java
ArrayList<Integer> a =
        new ArrayList<>();

a.add(10);
a.add(20);
a.add(30);
a.add(40);

ArrayList<Integer> b =
        new ArrayList<>();

b.add(20);
b.add(40);

a.removeAll(b);

System.out.println(a);
```

Output:

```text
[10, 30]
```

---

# 82. `retainAll()`

Keeps only elements that are also present in another collection.

```java
ArrayList<Integer> a =
        new ArrayList<>();

a.add(10);
a.add(20);
a.add(30);

ArrayList<Integer> b =
        new ArrayList<>();

b.add(20);
b.add(30);

a.retainAll(b);

System.out.println(a);
```

Output:

```text
[20, 30]
```

Think:

```text
removeAll → remove common elements
retainAll → retain common elements
```

---

# 83. ArrayList methods — important summary

| Method             | Purpose                             |
| ------------------ | ----------------------------------- |
| `add(E)`           | Add at end                          |
| `add(index,E)`     | Insert at index                     |
| `addAll()`         | Add collection                      |
| `get(index)`       | Retrieve element                    |
| `set(index,E)`     | Replace element                     |
| `remove(index)`    | Remove by index                     |
| `remove(Object)`   | Remove matching object              |
| `removeIf()`       | Remove based on condition           |
| `size()`           | Number of elements                  |
| `isEmpty()`        | Check whether empty                 |
| `contains()`       | Search element                      |
| `indexOf()`        | First matching index                |
| `lastIndexOf()`    | Last matching index                 |
| `clear()`          | Remove all elements                 |
| `iterator()`       | Obtain iterator                     |
| `listIterator()`   | Obtain bidirectional list iterator  |
| `subList()`        | Obtain range view                   |
| `toArray()`        | Convert to array                    |
| `addAll()`         | Add multiple elements               |
| `removeAll()`      | Remove matching collection elements |
| `retainAll()`      | Keep matching collection elements   |
| `containsAll()`    | Check collection containment        |
| `ensureCapacity()` | Ensure backing capacity             |
| `trimToSize()`     | Reduce capacity toward size         |
| `clone()`          | Shallow copy                        |

---

# 84. `ListIterator`

`Iterator` generally moves forward.

`ListIterator` can move both forward and backward.

Example:

```java
ListIterator<Integer> it = list.listIterator();

while (it.hasNext()) {
    System.out.println(it.next());
}
```

Then:

```java
while (it.hasPrevious()) {
    System.out.println(it.previous());
}
```

It can also support operations such as:

```java
add()
set()
remove()
```

while traversing the list.

---

# 85. Internal memory picture

Consider:

```java
ArrayList<String> list = new ArrayList<>();

list.add("A");
list.add("B");
list.add("C");
```

Conceptually:

```text
STACK
────────────────────

list
 │
 │ reference
 ↓
HEAP
────────────────────────────

ArrayList object
 ├── size = 3
 └── elementData
          │
          ↓
     ┌────┬────┬────┬────┬────┐
     │  A │  B │  C │null│null│
     └────┴────┴────┴────┴────┘
        0    1    2
```

For `String`, the internal array stores references to String objects.

Conceptually:

```text
elementData
   |
   +----> String "A"
   |
   +----> String "B"
   |
   +----> String "C"
```

---

# 86. What happens internally during `add()`?

Suppose:

```java
list.add("D");
```

The basic logical process is:

```text
1. Check whether backing array has enough capacity.
2. If enough capacity:
       place reference at next index.
3. Increase size.
```

If capacity is full:

```text
1. Detect insufficient capacity.
2. Calculate larger capacity.
3. Allocate a new backing array.
4. Copy existing references.
5. Make ArrayList point to new array.
6. Insert new element.
7. Increase size.
```

That's the internal idea you should understand for interviews.

---

# 87. What happens internally during `get()`?

Suppose:

```java
String x = list.get(2);
```

Conceptually:

```text
list
 ↓
backing array
 ↓
index 2
 ↓
reference stored there
 ↓
String object
```

Because an array supports direct indexed access:

```text
get(index) → O(1)
```

---

# 88. What happens internally during `remove(index)`?

Suppose:

```text
[A][B][C][D][E]
```

Execute:

```java
remove(1)
```

The element at index 1 is removed.

Then later elements shift:

```text
[A][C][D][E]
```

Conceptually:

```text
B removed
C → index 1
D → index 2
E → index 3
```

Then the old final reference is cleared to avoid retaining an unnecessary reference.

---

# 89. Why clearing references matters

Suppose:

```text
[A][B][C][D][E]
```

After removing `B`, the internal array may temporarily look conceptually like:

```text
[A][C][D][E][E]
```

The final unused slot must not continue holding a reference to `E`.

It is cleared:

```text
[A][C][D][E][null]
```

This helps prevent the removed/unused slot from unnecessarily keeping an object reachable.

This is an important internal memory-management detail.

---

# 90. ArrayList and Garbage Collection

ArrayList itself does not manually perform garbage collection.

Suppose:

```java
Student s = new Student(...);

list.add(s);

list.remove(s);
```

After the reference is removed from the list, the Student object may still be referenced somewhere else.

If no reachable references remain, it can eventually become eligible for garbage collection.

So:

```text
remove from ArrayList
        ↓
remove reference held by list
        ↓
object may become unreachable
        ↓
eligible for GC if no other references exist
```

`remove()` does not directly "delete the object from memory."

---

# 91. ArrayList and memory overhead

Compared with a primitive array:

```java
int[] arr
```

an:

```java
ArrayList<Integer>
```

can involve more memory because of:

1. ArrayList object itself
2. Backing array
3. References inside backing array
4. Integer wrapper objects for values that are not otherwise reused/referenced

This is one reason primitive arrays can be preferable for performance-sensitive numerical workloads.

---

# 92. Common beginner mistake: `size` vs `capacity`

Wrong thinking:

```java
ArrayList<Integer> list = new ArrayList<>(100);

```

means:

```text
100 elements already exist
```

Wrong.

Correct:

```text
capacity ≈ 100
size = 0
```

You still need:

```java
list.add(...)
```

to create logical elements.

---

# 93. Common mistake: `set()` on an empty list

This is invalid:

```java
ArrayList<Integer> list = new ArrayList<>();

list.set(0, 100); // ❌
```

Why?

Because `set()` replaces an **existing** element.

There is no index 0 yet.

Correct:

```java
list.add(100);
```

Then:

```java
list.set(0, 200);
```

---

# 94. Common mistake: confusing `add()` and `set()`

```java
list.add(1, "A");
```

means:

> Insert and shift existing elements.

While:

```java
list.set(1, "A");
```

means:

> Replace the element already at index 1.

Remember:

```text
add → insert
set → replace
```

---

# 95. Common mistake: invalid index

```java
ArrayList<Integer> list = new ArrayList<>();

list.add(10);

list.get(2);
```

Only index:

```text
0
```

exists.

So:

```text
IndexOutOfBoundsException
```

---

# 96. ArrayList does not automatically sort

```java
list.add(50);
list.add(10);
list.add(30);
```

produces:

```text
[50, 10, 30]
```

not:

```text
[10, 30, 50]
```

Use sorting explicitly.

---

# 97. ArrayList does not automatically remove duplicates

```java
list.add(10);
list.add(10);
list.add(20);
```

produces:

```text
[10, 10, 20]
```

If uniqueness is required, consider `Set`.

---

# 98. Complete example

Let's combine the important operations.

```java
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> names = new ArrayList<>();

        // Adding
        names.add("Prajwal");
        names.add("Rahul");
        names.add("Amit");

        System.out.println(names);

        // Access
        System.out.println(names.get(1));

        // Replace
        names.set(1, "Rohit");

        System.out.println(names);

        // Insert
        names.add(1, "Karan");

        System.out.println(names);

        // Search
        System.out.println(names.contains("Amit"));

        // Size
        System.out.println(names.size());

        // Remove by object
        names.remove("Karan");

        System.out.println(names);

        // Iterate
        for (String name : names) {
            System.out.println(name);
        }

        // Clear
        names.clear();

        System.out.println(names);
    }
}
```

---

# 99. Complete execution flow

Initially:

```text
[]
```

After:

```java
names.add("Prajwal");
```

```text
[Prajwal]
```

After:

```java
names.add("Rahul");
```

```text
[Prajwal, Rahul]
```

After:

```java
names.add("Amit");
```

```text
[Prajwal, Rahul, Amit]
```

Then:

```java
names.get(1)
```

returns:

```text
Rahul
```

Then:

```java
names.set(1, "Rohit");
```

becomes:

```text
[Prajwal, Rohit, Amit]
```

Then:

```java
names.add(1, "Karan");
```

becomes:

```text
[Prajwal, Karan, Rohit, Amit]
```

Then:

```java
names.remove("Karan");
```

becomes:

```text
[Prajwal, Rohit, Amit]
```

Finally:

```java
names.clear();
```

becomes:

```text
[]
```

---

# 100. Real-world example — storing students

```java
import java.util.ArrayList;

class Student {

    int id;
    String name;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    void display() {
        System.out.println(id + " - " + name);
    }
}

public class Main {

    public static void main(String[] args) {

        ArrayList<Student> students = new ArrayList<>();

        students.add(new Student(101, "Prajwal"));
        students.add(new Student(102, "Rahul"));
        students.add(new Student(103, "Amit"));

        for (Student s : students) {
            s.display();
        }
    }
}
```

Output:

```text
101 - Prajwal
102 - Rahul
103 - Amit
```

This demonstrates an extremely common real-world use:

```text
ArrayList
   ↓
stores references to
   ↓
Student objects
```

---

# 101. Why ArrayList is so widely used

ArrayList gives you a useful combination:

```text
Dynamic size
      +
Fast indexed access
      +
Generics
      +
Collection APIs
      +
Insertion/removal support
      +
Easy iteration
```

That's why it is one of the most commonly used Java collection classes.

---

# 102. Important interview concepts to remember

If an interviewer asks:

### "What is ArrayList?"

You should explain:

> ArrayList is a resizable-array implementation of the List interface in Java's Collections Framework. It maintains insertion order, allows duplicate and null elements, and provides constant-time positional access using indexes in typical implementations. Elements are stored through an internally managed backing array, which is resized when its capacity becomes insufficient. Adding at the end is amortized O(1), while indexed access is O(1) and insertion/removal at arbitrary positions generally takes O(n) because elements may need to be shifted.

That's a proper interview-level answer.

---

# 103. Interview: Why is ArrayList fast for `get()`?

Answer:

> ArrayList uses a backing array internally. Because arrays support direct indexed access, the JVM can locate the element at a particular index without traversing all preceding elements. Therefore, `get(index)` is O(1).

---

# 104. Interview: Why is insertion in the middle O(n)?

Answer:

> ArrayList stores elements in contiguous positions in its backing array. When an element is inserted into the middle, existing elements from that position onward need to be shifted to make room. Therefore, insertion at an arbitrary position is generally O(n).

---

# 105. Interview: Why is `add()` at the end amortized O(1)?

Answer:

> Most additions simply place the new element in the next available position of the backing array. Occasionally, when capacity is exhausted, ArrayList allocates a larger array and copies existing elements, which costs O(n). Since resizing occurs only occasionally and its cost is distributed across many insertions, end insertion is amortized O(1).

---

# 106. Interview: Difference between size and capacity

Answer:

> Size represents the number of elements currently stored in the ArrayList, whereas capacity represents how many elements the current internal backing array can accommodate before another resize is required. For example, an ArrayList can have size 3 while its internal capacity is larger than 3.

---

# 107. Interview: Does ArrayList store objects or references?

For reference types:

> ArrayList's backing array stores references to objects rather than embedding the complete object data inside each array slot.

For primitive types:

> ArrayList cannot use primitive type parameters directly, so wrapper types such as `Integer` are used.

---

# 108. Interview: Is ArrayList synchronized?

Answer:

> No. ArrayList is not synchronized by default and is not inherently thread-safe for concurrent modifications. If multiple threads need coordinated access, appropriate synchronization or a suitable concurrent collection should be used.

---

# 109. Interview: Does ArrayList allow duplicates?

Yes.

```java
list.add("A");
list.add("A");
```

Result:

```text
[A, A]
```

---

# 110. Interview: Does ArrayList maintain insertion order?

Yes.

If:

```java
list.add("C");
list.add("A");
list.add("B");
```

the iteration order is:

```text
C
A
B
```

---

# 111. Interview: Can ArrayList contain null?

Yes.

```java
list.add(null);
```

is valid, and multiple null elements can be present.

---

# 112. Interview: ArrayList vs LinkedList

A good answer:

> ArrayList is backed by a dynamically resized array, so it provides efficient random access through indexes but arbitrary insertion/removal may require shifting elements. LinkedList uses linked nodes, so indexed access requires traversal, while insertion/removal can be efficient when the target node/position is already known. Therefore, the choice depends on the access and modification pattern rather than simply assuming one is always faster.

---

# 113. Interview: Why use `List<Integer> list = new ArrayList<>()`?

Answer:

> `List` is an interface and `ArrayList` is one implementation of that interface. Declaring the variable using the interface type reduces coupling to a specific implementation and allows the implementation to be changed later when appropriate.

---

# 114. Interview: Why can't we write `ArrayList<int>`?

Answer:

> Java generics work with reference types rather than primitive types. Therefore, primitive `int` cannot be used as a generic type argument. We use the wrapper class `Integer`, and Java provides autoboxing and unboxing to make conversions convenient.

---

# 115. Interview: Difference between `remove(1)` and `remove(Integer.valueOf(1))`

This is a favorite interview question.

Suppose:

```java
ArrayList<Integer> list = new ArrayList<>();

list.add(10);
list.add(20);
list.add(1);
```

Then:

```java
list.remove(1);
```

means:

```text
remove element at index 1
```

so `20` is removed.

But:

```java
list.remove(Integer.valueOf(1));
```

means:

```text
remove the Integer object whose value is 1
```

so the value `1` is removed.

The overloaded methods are the reason.

---

# 116. Most important ArrayList mental model

Keep this picture in your head:

```text
                 ArrayList
                    |
                    ↓
          ┌──────────────────┐
          │ ArrayList object │
          └────────┬─────────┘
                   |
                   ↓
             backing array
                   |
        ┌──────────┼──────────┐
        ↓          ↓          ↓
      index 0    index 1    index 2
        |          |          |
        ↓          ↓          ↓
      object     object     object

        ←────── size ──────→

      capacity may be larger
      than the current size
```

And remember:

```text
get(index)                  → O(1)
set(index, value)           → O(1)

add(value) at end           → amortized O(1)

add(index, value)           → O(n)
remove(index)               → O(n)

contains(value)             → O(n)
indexOf(value)              → O(n)
```

---

# 117. ArrayList vs array — final mental comparison

### Array

```text
Fixed-size
      ↓
int[] arr = new int[5]
      ↓
Cannot grow automatically
```

### ArrayList

```text
Resizable
      ↓
ArrayList<Integer> list
      ↓
Internally uses a backing array
      ↓
When capacity is exhausted
      ↓
larger array is created
      ↓
existing references/elements copied
      ↓
new element inserted
```

So don't think:

> "ArrayList is completely different from an array."

Think:

> **ArrayList is a higher-level List implementation that uses a dynamically managed array internally.**

That single idea explains a huge part of its behavior.

---

# 118. Final complete summary

`ArrayList` is a **resizable-array implementation of the `List` interface**.

It:

* belongs to `java.util`
* maintains insertion order
* allows duplicates
* allows `null`
* supports indexed access
* dynamically grows as elements are added
* uses an internal backing array
* maintains a logical `size`
* has internal `capacity`
* provides O(1) indexed access
* provides amortized O(1) addition at the end
* generally requires O(n) shifting for arbitrary insertion/removal
* does not directly support primitive generic types
* uses wrapper classes such as `Integer`
* supports autoboxing/unboxing
* is not synchronized by default
* supports iterators and list iterators
* can be sorted using collection utilities
* supports bulk operations such as `addAll`, `removeAll`, and `retainAll`
* can create range views using `subList`
* can convert itself to arrays using `toArray`
* can be used with custom objects
* relies on `equals()` for many search/removal operations
* is commonly declared through the `List` interface

### The core concept in one flow:

```text
ArrayList
   ↓
List implementation
   ↓
internally maintains backing array
   ↓
stores elements/references
   ↓
size tells how many elements exist
   ↓
capacity tells available backing storage
   ↓
when capacity is insufficient
   ↓
larger backing array is created
   ↓
existing elements/references are copied
   ↓
new element is added
```

If you understand **backing array + size vs capacity + resizing + indexing + shifting + generics + `remove(int)` vs `remove(Object)`**, you understand the core of ArrayList at a strong interview level.
