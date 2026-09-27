# Stack in Java — Complete Detailed Notes

---

# 1. What is a Stack?

A **Stack** is a linear data structure in which insertion and deletion happen from the **same end**, called the **TOP**.

The Stack follows:

> **LIFO — Last In, First Out**

That means:

* The element inserted **last** is removed **first**.
* The element inserted **first** is removed **last**.

### Real-life example

Think about a stack of plates:

```text
        ┌─────────┐
        │ Plate 4 │  ← TOP
        ├─────────┤
        │ Plate 3 │
        ├─────────┤
        │ Plate 2 │
        ├─────────┤
        │ Plate 1 │
        └─────────┘
```

If you want to remove a plate, you remove Plate 4 first.

You cannot directly remove Plate 1 without first removing the plates above it.

Therefore:

```text
Last inserted → First removed
```

That's **LIFO**.

---

# 2. Why Do We Need a Stack?

Suppose we have normal data:

```text
10 20 30 40
```

Sometimes we don't care about processing from the beginning.

Instead, we need:

> "Whatever came most recently should be processed first."

Examples:

* Undo operation
* Browser back operation
* Function calls
* Recursion
* Parentheses matching
* Expression evaluation
* Expression conversion
* Backtracking
* DFS
* Temporary storage
* Parsing
* Compiler processing

All of these naturally fit the **LIFO** model.

---

# 3. Basic Structure of a Stack

Suppose we insert:

```text
10
20
30
40
```

The stack becomes:

```text
        TOP
         ↓
       ┌────┐
       │ 40 │
       ├────┤
       │ 30 │
       ├────┤
       │ 20 │
       ├────┤
       │ 10 │
       └────┘
```

The next element removed will be:

```text
40
```

Then:

```text
30
```

Then:

```text
20
```

Then:

```text
10
```

---

# 4. Important Stack Terminology

Before learning Java's `Stack`, understand these terms.

## 4.1 Push

**Push** means inserting an element into the Stack.

Example:

```text
push(10)
push(20)
push(30)
```

Stack:

```text
TOP
 ↓
30
20
10
```

---

## 4.2 Pop

**Pop** means removing the element from the top.

```text
pop()
```

If stack is:

```text
30 ← TOP
20
10
```

then:

```text
pop()
```

removes `30`.

Stack becomes:

```text
20 ← TOP
10
```

---

## 4.3 Peek

**Peek** means looking at the top element **without removing it**.

```text
peek()
```

Example:

```text
30 ← TOP
20
10
```

`peek()` returns:

```text
30
```

but stack remains:

```text
30 ← TOP
20
10
```

### Important difference

```text
pop()  → returns + removes
peek() → returns + does NOT remove
```

---

# 5. Stack Operations

The fundamental Stack operations are:

| Operation   | Meaning             |
| ----------- | ------------------- |
| `push()`    | Insert element      |
| `pop()`     | Remove top element  |
| `peek()`    | View top element    |
| `isEmpty()` | Check whether empty |
| `size()`    | Number of elements  |

Depending on implementation, additional operations may exist.

---

# 6. Stack Example Step-by-Step

Start with an empty Stack:

```text
EMPTY
```

### Operation 1

```text
push(10)
```

```text
TOP
 ↓
10
```

### Operation 2

```text
push(20)
```

```text
TOP
 ↓
20
10
```

### Operation 3

```text
push(30)
```

```text
TOP
 ↓
30
20
10
```

### Operation 4

```text
peek()
```

Returns:

```text
30
```

Stack remains:

```text
30
20
10
```

### Operation 5

```text
pop()
```

Returns:

```text
30
```

Stack becomes:

```text
20
10
```

### Operation 6

```text
pop()
```

Returns:

```text
20
```

Stack:

```text
10
```

This is LIFO in action.

---

# 7. Stack as an Abstract Data Type

This is an important theoretical distinction.

A **Stack is primarily an ADT (Abstract Data Type)**.

An ADT describes:

> **What operations are available and what behavior they have.**

It does not necessarily dictate exactly how the data must be stored internally.

For example, a Stack can be implemented using:

```text
Array
Linked List
Dynamic Array
Java Vector
Deque
```

The external behavior can still be:

```text
push
pop
peek
```

Therefore:

```text
Stack = behavior / abstraction
```

while:

```text
Array / Linked List / ArrayDeque = possible implementation
```

---

# 8. Stack vs Array

An Array is a data structure that provides indexed access.

Example:

```java
int[] arr = {10, 20, 30, 40};
```

You can do:

```java
arr[0]
arr[1]
arr[2]
```

A Stack is different.

It restricts access according to LIFO behavior.

Conceptually:

```text
Array:

10 20 30 40
↑     ↑     ↑
Can access using index


Stack:

40 ← TOP
30
20
10

Access is intended through TOP
```

This restriction is actually useful because it gives us controlled behavior.

---

# 9. Stack in Java

Java provides a class:

```java
java.util.Stack
```

You can import it:

```java
import java.util.Stack;
```

Then:

```java
Stack<Integer> stack = new Stack<>();
```

Now we have a Stack containing `Integer` objects.

---

# 10. Why `Stack<Integer>` Instead of `Stack<int>`?

Java Collections work with **objects**, not primitive types.

Therefore:

```java
Stack<Integer>
```

is valid.

But:

```java
Stack<int>
```

is invalid.

Java automatically performs boxing/unboxing when appropriate.

For example:

```java
stack.push(10);
```

The primitive:

```java
10
```

is boxed into:

```java
Integer
```

Conceptually:

```text
int 10
 ↓ boxing
Integer object
 ↓
Stack
```

---

# 11. Creating a Java Stack

```java
import java.util.Stack;

public class Main {
    public static void main(String[] args) {

        Stack<Integer> stack = new Stack<>();

    }
}
```

At this point:

```text
stack
 ↓
empty Stack
```

---

# 12. `push()` — Insert Element

Syntax:

```java
stack.push(element);
```

Example:

```java
Stack<Integer> stack = new Stack<>();

stack.push(10);
stack.push(20);
stack.push(30);
```

Stack:

```text
TOP
 ↓
30
20
10
```

### Execution

First:

```java
stack.push(10);
```

```text
10
```

Then:

```java
stack.push(20);
```

```text
20
10
```

Then:

```java
stack.push(30);
```

```text
30
20
10
```

---

# 13. `pop()` — Remove Top Element

Syntax:

```java
stack.pop();
```

Example:

```java
Stack<Integer> stack = new Stack<>();

stack.push(10);
stack.push(20);
stack.push(30);

int x = stack.pop();

System.out.println(x);
```

Output:

```text
30
```

Why?

Because:

```text
30 ← TOP
20
10
```

`pop()` removes the top.

After `pop()`:

```text
20 ← TOP
10
```

---

# 14. `pop()` Returns the Removed Element

This is important.

`pop()` doesn't merely remove.

It also **returns** the removed element.

```java
int x = stack.pop();
```

If:

```text
30
20
10
```

then:

```java
x = 30
```

and Stack becomes:

```text
20
10
```

So:

```text
pop()
=
remove top
+
return removed value
```

---

# 15. `peek()` — View Top Element

Syntax:

```java
stack.peek();
```

Example:

```java
Stack<Integer> stack = new Stack<>();

stack.push(10);
stack.push(20);
stack.push(30);

System.out.println(stack.peek());
```

Output:

```text
30
```

Stack remains:

```text
30
20
10
```

So:

```text
peek()
→ reads top

pop()
→ reads + removes top
```

---

# 16. `isEmpty()`

Syntax:

```java
stack.isEmpty()
```

Returns:

```text
true
```

if Stack contains no elements.

Example:

```java
Stack<Integer> stack = new Stack<>();

System.out.println(stack.isEmpty());
```

Output:

```text
true
```

After:

```java
stack.push(10);
```

then:

```java
System.out.println(stack.isEmpty());
```

Output:

```text
false
```

---

# 17. `size()`

Syntax:

```java
stack.size();
```

Example:

```java
stack.push(10);
stack.push(20);
stack.push(30);

System.out.println(stack.size());
```

Output:

```text
3
```

---

# 18. Complete Basic Stack Program

```java
import java.util.Stack;

public class Main {

    public static void main(String[] args) {

        Stack<Integer> stack = new Stack<>();

        stack.push(10);
        stack.push(20);
        stack.push(30);

        System.out.println("Stack: " + stack);

        System.out.println("Top: " + stack.peek());

        System.out.println("Removed: " + stack.pop());

        System.out.println("Stack after pop: " + stack);

        System.out.println("Size: " + stack.size());

        System.out.println("Empty: " + stack.isEmpty());
    }
}
```

Output:

```text
Stack: [10, 20, 30]
Top: 30
Removed: 30
Stack after pop: [10, 20]
Size: 2
Empty: false
```

### Important observation

Java prints:

```text
[10, 20, 30]
```

from bottom to top.

So:

```text
[10, 20, 30]
          ↑
         TOP
```

The textual representation can look like an ordinary list, but Stack operations still follow LIFO.

---

# 19. Stack Internal Representation

This is where many beginners get confused.

When we write:

```java
Stack<Integer> stack = new Stack<>();
```

we have a reference variable:

```text
stack
  │
  ↓
Stack object
```

The Stack object internally maintains storage for its elements.

For the legacy Java `Stack` implementation, that storage comes through its inheritance from `Vector`.

Conceptually:

```text
stack reference
       │
       ↓
┌─────────────────────────┐
│ Stack object             │
│                         │
│ underlying Vector       │
│                         │
│ [10, 20, 30]            │
└─────────────────────────┘
```

The logical top is represented by the last element.

So:

```text
[10, 20, 30]
          ↑
         TOP
```

---

# 20. Important: Java `Stack` Is a Legacy Class

This is one of the most important interview points.

Java's:

```java
java.util.Stack
```

is an older collection class.

It extends:

```java
Vector
```

Conceptually:

```text
Object
   ↓
AbstractCollection
   ↓
AbstractList
   ↓
Vector
   ↓
Stack
```

So:

```java
Stack<E> extends Vector<E>
```

This means Java's Stack inherits many operations from `Vector` and its parent classes.

---

# 21. Why Is `Stack` Considered Legacy?

Because `Stack` was designed in the older Java collection architecture.

Modern Java generally recommends using:

```java
Deque<E>
```

for stack behavior.

A common implementation is:

```java
ArrayDeque<E>
```

Example:

```java
Deque<Integer> stack = new ArrayDeque<>();
```

Then:

```java
stack.push(10);
stack.push(20);
stack.push(30);
```

and:

```java
stack.pop();
stack.peek();
```

This gives Stack behavior.

---

# 22. `Stack` vs `Deque`

This distinction is extremely important.

### Old/legacy style

```java
Stack<Integer> stack = new Stack<>();
```

### Modern preferred approach

```java
Deque<Integer> stack = new ArrayDeque<>();
```

Why?

Because `Deque` is designed specifically for insertion/removal at both ends and can naturally represent a Stack or Queue.

---

# 23. What is `Deque`?

`Deque` means:

> **Double Ended Queue**

Pronounced roughly:

> "deck"

It allows insertion and removal from both ends.

Conceptually:

```text
FRONT                         BACK
 ↓                              ↓
10   20   30   40   50
```

You can operate from either end.

But if you use only one end:

```text
push()
pop()
peek()
```

then you can use the `Deque` as a Stack.

---

# 24. `ArrayDeque` as a Stack

Example:

```java
import java.util.ArrayDeque;
import java.util.Deque;

public class Main {

    public static void main(String[] args) {

        Deque<Integer> stack = new ArrayDeque<>();

        stack.push(10);
        stack.push(20);
        stack.push(30);

        System.out.println(stack.peek());
        System.out.println(stack.pop());
    }
}
```

Output:

```text
30
30
```

Stack after `pop()`:

```text
20
10
```

---

# 25. Why Declare `Deque` Instead of `ArrayDeque`?

This is the **programming to interface** principle.

Instead of:

```java
ArrayDeque<Integer> stack = new ArrayDeque<>();
```

we normally write:

```java
Deque<Integer> stack = new ArrayDeque<>();
```

Because:

```text
Deque = interface
ArrayDeque = implementation
```

The variable depends on the abstraction.

This gives more flexibility.

---

# 26. Important Difference: Stack vs ArrayDeque

| Feature               | `Stack`                      | `ArrayDeque`       |
| --------------------- | ---------------------------- | ------------------ |
| Type                  | Class                        | Class              |
| Parent                | `Vector`                     | Implements `Deque` |
| Design                | Legacy                       | Modern collection  |
| Stack operations      | Yes                          | Yes                |
| Queue operations      | Not its main purpose         | Yes                |
| `push()`              | Yes                          | Yes                |
| `pop()`               | Yes                          | Yes                |
| `peek()`              | Yes                          | Yes                |
| Allows `null`         | Yes                          | No                 |
| Synchronization       | Synchronized methods         | Not synchronized   |
| Typical modern choice | Usually avoided for new code | Common choice      |

---

# 27. Why Doesn't `ArrayDeque` Allow `null`?

This is an important edge case.

```java
Deque<Integer> stack = new ArrayDeque<>();
```

Then:

```java
stack.push(null);
```

throws:

```text
NullPointerException
```

Why?

Because `null` is used to represent absence in some queue/deque operations, and allowing it would create ambiguity.

For Stack usage, this usually isn't a problem because Stack data can simply be restricted to non-null values.

---

# 28. Does `Stack` Allow `null`?

Yes.

```java
Stack<Integer> stack = new Stack<>();

stack.push(null);
```

is allowed.

This is another difference between the legacy `Stack` and `ArrayDeque`.

---

# 29. Stack Overflow

There are two related concepts that students often confuse.

## Stack data structure overflow

If you implement a **fixed-size Stack using an array**, and the Stack is full, another `push()` cannot be performed.

Example:

```text
Capacity = 3

[30]
[20]
[10]
```

Trying:

```text
push(40)
```

causes **Stack Overflow** in the data-structure sense.

---

# 30. Stack Underflow

Suppose:

```text
Stack = empty
```

Then:

```java
pop();
```

There is no element to remove.

This is called:

> **Stack Underflow**

With Java's `Stack`, calling `pop()` on an empty Stack results in:

```text
EmptyStackException
```

Example:

```java
Stack<Integer> stack = new Stack<>();

stack.pop();
```

throws:

```text
java.util.EmptyStackException
```

---

# 31. `peek()` on Empty Stack

Similarly:

```java
Stack<Integer> stack = new Stack<>();

stack.peek();
```

also results in:

```text
EmptyStackException
```

Therefore, a safe pattern is:

```java
if (!stack.isEmpty()) {
    System.out.println(stack.peek());
}
```

---

# 32. Safe Pop

Instead of blindly:

```java
int x = stack.pop();
```

check:

```java
if (!stack.isEmpty()) {
    int x = stack.pop();
}
```

This prevents underflow.

---

# 33. Stack Implementation Using Array

Now let's understand how a Stack actually works conceptually.

Suppose:

```java
int[] stack = new int[5];
```

We need a variable:

```java
int top = -1;
```

Why `-1`?

Because initially there is no valid index occupied.

```text
Array:

index:   0   1   2   3   4
        ─────────────────────
value:  [ ] [ ] [ ] [ ] [ ]

top = -1
```

---

# 34. Push in Array-Based Stack

To push:

```text
10
```

we first increment:

```text
top = top + 1
```

So:

```text
top = 0
```

Then:

```java
stack[top] = 10;
```

Array:

```text
index:   0   1   2   3   4
        ─────────────────────
value:  [10][ ][ ][ ][ ]
         ↑
        top
```

---

# 35. Push 20

Initially:

```text
top = 0
```

Push:

```text
20
```

Increment:

```text
top = 1
```

Then:

```text
stack[1] = 20
```

Array:

```text
index:   0    1   2   3   4
        ──────────────────────
value:  [10] [20][ ][ ][ ]
              ↑
             top
```

---

# 36. Push 30

```text
top = 2
```

Array:

```text
index:   0    1    2   3   4
        ───────────────────────
value:  [10] [20] [30][ ][ ]
                   ↑
                  top
```

So the top element is:

```java
stack[top]
```

which gives:

```text
30
```

---

# 37. Pop in Array-Based Stack

Suppose:

```text
index:   0    1    2
        ───────────────
value:  [10] [20] [30]
                   ↑
                  top
```

`top = 2`.

To pop:

```java
int value = stack[top];
top--;
```

So:

```text
value = 30
top = 1
```

Logical Stack:

```text
20 ← TOP
10
```

Notice something important:

The old `30` might still physically exist in the array slot.

But because:

```text
top = 1
```

it is no longer considered part of the Stack.

This distinction between **physical storage** and **logical contents** is very important.

---

# 38. Complete Stack Using Array

```java
class StackArray {

    private int[] arr;
    private int top;

    StackArray(int capacity) {
        arr = new int[capacity];
        top = -1;
    }

    public void push(int value) {

        if (top == arr.length - 1) {
            throw new RuntimeException("Stack Overflow");
        }

        top++;
        arr[top] = value;
    }

    public int pop() {

        if (top == -1) {
            throw new RuntimeException("Stack Underflow");
        }

        int value = arr[top];
        top--;

        return value;
    }

    public int peek() {

        if (top == -1) {
            throw new RuntimeException("Stack Underflow");
        }

        return arr[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top + 1;
    }
}
```

---

# 39. Understanding the Array Implementation

Suppose:

```java
StackArray stack = new StackArray(5);
```

Internally:

```text
arr
 ↓
┌────┬────┬────┬────┬────┐
│    │    │    │    │    │
└────┴────┴────┴────┴────┘
 0    1    2    3    4

top = -1
```

After:

```java
stack.push(10);
stack.push(20);
stack.push(30);
```

we have:

```text
┌────┬────┬────┬────┬────┐
│ 10 │ 20 │ 30 │    │    │
└────┴────┴────┴────┴────┘
           ↑
          top
```

`top = 2`.

---

# 40. Complexity of Basic Stack Operations

For a properly implemented Stack:

| Operation | Time Complexity |
| --------- | --------------: |
| Push      |            O(1) |
| Pop       |            O(1) |
| Peek      |            O(1) |
| isEmpty   |            O(1) |
| Size      |            O(1) |

Why?

Because we only operate at the top.

We don't need to shift all elements.

---

# 41. Why Push Is O(1)?

Suppose:

```text
10
20
30
40
50
60
70
80
90
```

To push `100`, we don't inspect all elements.

We simply place it at the top.

```text
100 ← TOP
90
80
...
```

Constant amount of work.

Therefore:

```text
O(1)
```

---

# 42. Why Pop Is O(1)?

To remove the top:

```text
100 ← TOP
90
80
```

we only remove/update the top.

We don't move:

```text
90
80
...
```

Therefore:

```text
O(1)
```

---

# 43. Why Stack Is Efficient

Stack restricts access.

Instead of allowing arbitrary insertion/removal:

```text
insert anywhere
delete anywhere
```

it says:

> Only work at the top.

This restriction makes the fundamental operations extremely efficient.

---

# 44. Stack Using Linked List

A Stack can also be implemented using a Linked List.

Suppose:

```text
TOP
 ↓
30 → 20 → 10 → null
```

Push `40`:

```text
TOP
 ↓
40 → 30 → 20 → 10 → null
```

Pop:

```text
TOP
 ↓
30 → 20 → 10 → null
```

The head of the Linked List acts as the Stack's top.

---

# 45. Why Use the Head as TOP?

Because inserting/removing at the head of a linked list is:

```text
O(1)
```

If we used the tail without maintaining a tail reference, removing the last node could require traversal.

Therefore:

```text
Linked List head = Stack TOP
```

is a natural implementation.

---

# 46. Stack and Recursion

One of the most important applications of Stack is **function call management**.

Consider:

```java
static void A() {
    B();
}

static void B() {
    C();
}

static void C() {
    System.out.println("Hello");
}
```

Execution:

```text
main()
  ↓
A()
  ↓
B()
  ↓
C()
```

The JVM maintains call information using the **Java call stack**.

Conceptually:

```text
TOP
┌─────────────┐
│ C() frame   │
├─────────────┤
│ B() frame   │
├─────────────┤
│ A() frame   │
├─────────────┤
│ main()      │
└─────────────┘
```

When `C()` finishes, its frame is removed first.

Then `B()`.

Then `A()`.

This is LIFO.

---

# 47. Important: Java Collection Stack vs JVM Call Stack

Do not confuse these.

### `java.util.Stack`

A Java collection used to store elements:

```java
Stack<Integer> stack
```

### JVM call stack

A runtime memory structure used to manage method execution.

```text
main()
methodA()
methodB()
```

They are **different concepts**.

Both follow LIFO-like behavior, but they serve different purposes.

---

# 48. Recursion Uses the Call Stack

Example:

```java
static void fun(int n) {

    if (n == 0)
        return;

    System.out.println(n);

    fun(n - 1);
}
```

Call:

```java
fun(3);
```

Execution:

```text
fun(3)
  ↓
fun(2)
  ↓
fun(1)
  ↓
fun(0)
```

Conceptually:

```text
TOP
fun(0)
fun(1)
fun(2)
fun(3)
main
```

When `fun(0)` returns:

```text
fun(0) removed
```

Then:

```text
fun(1) removed
```

Then:

```text
fun(2) removed
```

This is LIFO.

---

# 49. Application: Undo Operation

Suppose a text editor performs:

```text
Type A
Type B
Type C
```

Each operation can be pushed:

```text
TOP
Type C
Type B
Type A
```

Press Undo:

```text
pop()
```

removes:

```text
Type C
```

Another Undo:

```text
Type B
```

Thus:

```text
Latest action → undone first
```

Stack is perfect for this.

---

# 50. Application: Browser Back Button

Suppose you visit:

```text
Google
 ↓
YouTube
 ↓
Amazon
 ↓
GitHub
```

History can conceptually be represented as:

```text
TOP
GitHub
Amazon
YouTube
Google
```

Press Back:

```text
GitHub removed
```

Now:

```text
Amazon
```

is the current previous location.

Again:

```text
Last visited → first returned from
```

---

# 51. Application: Parentheses Matching

Consider:

```text
{ [ ( ) ] }
```

We can use a Stack.

When opening brackets appear:

```text
{
[
(
```

push them.

When closing bracket appears:

```text
)
```

check the top:

```text
(
```

They match.

Pop it.

Then:

```text
]
```

matches:

```text
[
```

Then:

```text
}
```

matches:

```text
{
```

At the end, if Stack is empty, the brackets are balanced.

---

# 52. Parentheses Matching Example

Input:

```text
{[()]}
```

Process:

```text
{ → push
[ → push
( → push
) → matches (, pop
] → matches [, pop
} → matches {, pop
```

Final:

```text
Stack = empty
```

Therefore:

```text
Balanced
```

---

# 53. Unbalanced Example

Input:

```text
{[(])}
```

Process:

```text
{ → push
[ → push
( → push
] → expected matching (
```

But top is:

```text
(
```

and `]` does not match `(`.

Therefore:

```text
Not Balanced
```

This is a classic Stack problem.

---

# 54. Code: Balanced Parentheses

```java
import java.util.Stack;

public class ParenthesesCheck {

    static boolean isBalanced(String str) {

        Stack<Character> stack = new Stack<>();

        for (char ch : str.toCharArray()) {

            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            }

            else if (ch == ')' || ch == ']' || ch == '}') {

                if (stack.isEmpty()) {
                    return false;
                }

                char top = stack.pop();

                if ((ch == ')' && top != '(') ||
                    (ch == ']' && top != '[') ||
                    (ch == '}' && top != '{')) {

                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {

        System.out.println(isBalanced("{[()]}"));
        System.out.println(isBalanced("{[(])}"));
    }
}
```

Output:

```text
true
false
```

---

# 55. Logic Behind Parentheses Problem

The important logic is:

### Opening bracket

```java
stack.push(ch);
```

because we need to remember it.

### Closing bracket

We need the **most recently opened bracket**.

That's exactly what Stack gives us:

```java
char top = stack.pop();
```

Therefore the Stack naturally solves the problem.

---

# 56. Stack for String Reversal

Another classic application.

Input:

```text
HELLO
```

Push each character:

```text
TOP
O
L
L
E
H
```

Then pop:

```text
O
L
L
E
H
```

Result:

```text
OLLEH
```

---

# 57. Code: Reverse a String

```java
import java.util.Stack;

public class ReverseString {

    static String reverse(String str) {

        Stack<Character> stack = new Stack<>();

        for (char ch : str.toCharArray()) {
            stack.push(ch);
        }

        StringBuilder result = new StringBuilder();

        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }

        return result.toString();
    }

    public static void main(String[] args) {

        System.out.println(reverse("HELLO"));
    }
}
```

Output:

```text
OLLEH
```

---

# 58. Why Does This Work?

Original:

```text
H E L L O
```

Insertion order:

```text
H
E
L
L
O
```

Top becomes:

```text
O
```

Therefore removal order:

```text
O
L
L
E
H
```

So:

```text
Insertion order ≠ removal order
```

Instead:

```text
Removal order = reverse of insertion order
```

That's the fundamental property of LIFO.

---

# 59. Stack for Decimal → Binary

Another classic problem.

Suppose:

```text
10
```

Repeatedly divide by 2:

```text
10 / 2 → remainder 0
5  / 2 → remainder 1
2  / 2 → remainder 0
1  / 2 → remainder 1
```

Remainders are obtained:

```text
0 1 0 1
```

But binary representation requires:

```text
1010
```

The remainders need to be read **in reverse order**.

Stack naturally reverses them.

---

# 60. Expression Evaluation

Stack is heavily used in:

```text
Infix
Prefix
Postfix
```

expressions.

For example:

```text
2 + 3
```

is infix.

Postfix:

```text
2 3 +
```

Prefix:

```text
+ 2 3
```

Stacks are particularly important for expression parsing and evaluation.

---

# 61. Postfix Evaluation

Consider:

```text
2 3 +
```

Algorithm:

```text
2 → push
3 → push
+ → pop 3
    pop 2
    calculate 2 + 3
    push 5
```

Stack becomes:

```text
5
```

Answer:

```text
5
```

---

# 62. More Complex Postfix

Expression:

```text
2 3 4 * +
```

Meaning:

```text
2 + (3 * 4)
```

Process:

```text
2 → push

3 → push

4 → push
```

Stack:

```text
4
3
2
```

`*`:

```text
4 pop
3 pop
3 * 4 = 12
push 12
```

Stack:

```text
12
2
```

`+`:

```text
12 pop
2 pop
2 + 12 = 14
push 14
```

Final:

```text
14
```

---

# 63. Backtracking

Stack is also useful when we need to go backward through decisions.

Examples:

* Maze solving
* DFS
* Path finding
* Undoing decisions
* Browser history
* Puzzle solving

General idea:

```text
Decision 1
   ↓
Decision 2
   ↓
Decision 3
   ↓
Dead end
   ↓
Go back to Decision 3
```

The most recent decision is reversed first.

Again:

```text
LIFO
```

---

# 64. DFS and Stack

Depth First Search can be implemented using:

```text
Recursion
```

or explicitly using:

```text
Stack
```

For example:

```text
A
├── B
│   ├── D
│   └── E
└── C
```

DFS goes deep before going broad.

An explicit Stack can store nodes that need to be explored.

---

# 65. Stack and Queue Difference

This is a very common interview question.

### Stack

```text
LIFO
```

### Queue

```text
FIFO
```

Queue means:

> First In, First Out.

Example:

```text
10 → 20 → 30 → 40
↑
front
```

`10` leaves first.

Stack:

```text
40 ← TOP
30
20
10
```

`40` leaves first.

---

# 66. Stack vs Queue

| Feature          | Stack           | Queue            |
| ---------------- | --------------- | ---------------- |
| Principle        | LIFO            | FIFO             |
| Insert           | Top             | Rear             |
| Remove           | Top             | Front            |
| Main operation   | Push            | Enqueue          |
| Remove operation | Pop             | Dequeue          |
| Typical example  | Undo            | Printer queue    |
| Access direction | One logical end | Two logical ends |

---

# 67. `push()` vs `add()`

For `Stack`:

```java
stack.push(10);
```

is the natural Stack operation.

Because `Stack` inherits from `Vector`, it also has methods such as:

```java
add()
addElement()
insertElementAt()
```

But using arbitrary inherited list operations can violate the **logical Stack abstraction**.

For example:

```java
stack.add(0, 100);
```

You have now inserted an element somewhere other than the logical top.

Therefore, when using Stack as a Stack, prefer:

```java
push()
pop()
peek()
```

rather than treating it like a random-access list.

---

# 68. Why `Stack` Being a `Vector` Can Be Problematic

Conceptually:

```text
Stack
  ↓
Vector
  ↓
List-like operations
```

A Stack should ideally restrict you to:

```text
push
pop
peek
```

But because Java's `Stack` inherits methods from `Vector`/`List`, you can perform operations that aren't natural Stack operations.

For example:

```java
stack.remove(0);
```

This is not normal Stack behavior.

That's one reason modern Java code commonly uses:

```java
Deque<Integer> stack = new ArrayDeque<>();
```

---

# 69. `ArrayDeque` Stack Operations

With:

```java
Deque<Integer> stack = new ArrayDeque<>();
```

use:

```java
push()
pop()
peek()
```

Example:

```java
stack.push(10);
stack.push(20);
stack.push(30);

System.out.println(stack.peek());
System.out.println(stack.pop());
```

Output:

```text
30
30
```

---

# 70. Important `Deque` Methods

A `Deque` provides corresponding methods.

For the Stack side:

```text
push() → add at front
pop()  → remove from front
peek() → inspect front
```

There are also methods such as:

```text
addFirst()
removeFirst()
peekFirst()
```

which operate explicitly on the front.

So:

```java
stack.push(10);
```

is equivalent in effect to adding at the front.

---

# 71. Why `ArrayDeque` Is a Good Stack Implementation

`ArrayDeque` is backed by an array-like circular structure and is designed for efficient operations at both ends.

For Stack usage:

```text
push → one end
pop  → same end
peek → same end
```

Thus the core Stack operations are efficient.

You don't need to manually manage:

```text
top
```

because `ArrayDeque` manages the structure internally.

---

# 72. Generic Stack

Stacks can hold any reference type.

```java
Stack<String> names = new Stack<>();
```

or:

```java
Stack<Double> prices = new Stack<>();
```

or:

```java
Stack<Character> chars = new Stack<>();
```

or:

```java
Stack<Person> people = new Stack<>();
```

The generic type determines what elements can be stored.

---

# 73. Stack of Objects

Suppose:

```java
class Student {

    String name;

    Student(String name) {
        this.name = name;
    }
}
```

Then:

```java
Stack<Student> stack = new Stack<>();

stack.push(new Student("A"));
stack.push(new Student("B"));
```

Top:

```text
Student B
```

This demonstrates that Stack stores **references to objects**.

---

# 74. Memory View of `Stack<Student>`

Conceptually:

```text
Stack object
     │
     ↓
[ reference A ][ reference B ]
                       │
                       ↓
                  Student object
                  name = "B"
```

The collection stores references to objects rather than copying the complete objects into the collection.

This is consistent with how Java object references work.

---

# 75. Stack Does Not Automatically Make Objects Immutable

Suppose:

```java
Stack<Student> stack = new Stack<>();
```

The Stack controls how references are added/removed.

It does not make the `Student` objects immutable.

If:

```java
Student s = new Student("Prajwal");

stack.push(s);
```

then:

```text
s ─────────┐
           ↓
      Student object
           ↑
Stack ─────┘
```

Both can refer to the same object.

---

# 76. Searching in a Stack

`Stack` inherits methods such as:

```java
search()
contains()
```

Example:

```java
Stack<Integer> stack = new Stack<>();

stack.push(10);
stack.push(20);
stack.push(30);

System.out.println(stack.search(20));
```

The result is based on the Stack's top-oriented search semantics.

For:

```text
[10, 20, 30]
          ↑ TOP
```

searching `30` gives:

```text
1
```

because it is one position from the top.

Searching `20` gives:

```text
2
```

Searching `10` gives:

```text
3
```

---

# 77. `search()` Is Not an Index

This is an interview trap.

```java
stack.search(element)
```

does not return the normal zero-based array index.

It returns the **1-based position from the top**.

Example:

```text
TOP
 ↓
30 → position 1
20 → position 2
10 → position 3
```

Therefore:

```java
stack.search(30) → 1
stack.search(20) → 2
stack.search(10) → 3
```

If the element isn't present, it returns:

```text
-1
```

---

# 78. Duplicate Elements

Stack allows duplicates.

```java
Stack<Integer> stack = new Stack<>();

stack.push(10);
stack.push(20);
stack.push(10);
```

Stack:

```text
10 ← TOP
20
10
```

No uniqueness restriction exists.

This differs from a Set.

---

# 79. Stack vs Set

### Stack

```text
LIFO
Duplicates allowed
Ordered by insertion/top relationship
```

### Set

```text
Uniqueness
Duplicates not allowed
```

Example:

```text
Stack:
10
20
10
```

valid.

Set:

```text
10
20
10
```

the second `10` is not retained as another distinct element.

---

# 80. Stack vs ArrayList

Both can technically store ordered elements.

But their intended abstraction is different.

### ArrayList

Designed for:

```text
index-based access
dynamic list
```

### Stack

Designed for:

```text
LIFO
push/pop/peek
```

So if your problem says:

> "Process the most recently added element first"

think:

```text
Stack
```

---

# 81. Stack vs LinkedList

A `LinkedList` can also be used as a Stack because it implements `Deque`.

Example:

```java
Deque<Integer> stack = new LinkedList<>();
```

Then:

```java
stack.push(10);
stack.push(20);
stack.pop();
```

works.

However, for typical Stack usage, `ArrayDeque` is generally the more natural modern implementation.

---

# 82. Three Common Java Approaches

### 1. Legacy Stack

```java
Stack<Integer> stack = new Stack<>();
```

### 2. Modern preferred Stack abstraction

```java
Deque<Integer> stack = new ArrayDeque<>();
```

### 3. LinkedList as Deque

```java
Deque<Integer> stack = new LinkedList<>();
```

The second approach is generally the one you should recognize as the modern standard.

---

# 83. Stack Operation Table

For `Stack`:

| Method             | Purpose                          |
| ------------------ | -------------------------------- |
| `push(E)`          | Push element                     |
| `pop()`            | Remove and return top            |
| `peek()`           | Return top without removing      |
| `empty()`          | Check whether empty              |
| `isEmpty()`        | Inherited collection-style check |
| `search(Object)`   | Position from top                |
| `size()`           | Number of elements               |
| `contains(Object)` | Check element                    |
| `clear()`          | Remove all elements              |

---

# 84. `empty()` vs `isEmpty()`

This is a subtle Java point.

`Stack` provides:

```java
empty()
```

while collection classes commonly use:

```java
isEmpty()
```

Example:

```java
stack.empty();
```

and:

```java
stack.isEmpty();
```

Both can tell you whether the Stack has no elements.

For modern collection-oriented code, you'll commonly encounter:

```java
isEmpty()
```

---

# 85. `clear()`

To remove all elements:

```java
stack.clear();
```

Example:

```java
Stack<Integer> stack = new Stack<>();

stack.push(10);
stack.push(20);
stack.push(30);

stack.clear();
```

Now:

```text
Stack = empty
```

and:

```java
stack.isEmpty()
```

returns:

```text
true
```

---

# 86. Iterating Over a Stack

Because `Stack` is a collection, you can iterate through it.

Example:

```java
for (Integer value : stack) {
    System.out.println(value);
}
```

Be careful:

> Iteration order is not the same thing as repeatedly popping.

For Stack algorithms, if your goal is:

> "Process elements according to LIFO"

then use:

```java
while (!stack.isEmpty()) {
    System.out.println(stack.pop());
}
```

That explicitly follows Stack behavior.

---

# 87. Example: Iteration vs Pop

Suppose:

```java
Stack<Integer> stack = new Stack<>();

stack.push(10);
stack.push(20);
stack.push(30);
```

Logical Stack:

```text
30 ← TOP
20
10
```

A normal iteration can produce:

```text
10
20
30
```

while:

```java
while (!stack.isEmpty()) {
    System.out.println(stack.pop());
}
```

produces:

```text
30
20
10
```

This distinction is very important.

---

# 88. Stack and Thread Safety

Another advanced topic.

`Stack` inherits from `Vector`, whose methods are synchronized.

Therefore Java's legacy `Stack` has synchronization characteristics.

But:

```java
ArrayDeque
```

is not synchronized.

If you need concurrent access, you should choose an appropriate concurrency design rather than assuming every Stack implementation is thread-safe.

For normal single-threaded algorithm problems, this distinction usually doesn't affect the algorithm.

---

# 89. Stack and Synchronization

Don't confuse:

```text
synchronized methods
```

with:

```text
Stack data structure
```

LIFO is the **logical behavior**.

Synchronization is a **concurrency property**.

These are separate concepts.

---

# 90. Fixed vs Dynamic Stack

A manually implemented Stack using:

```java
int[] arr = new int[5];
```

has fixed capacity.

```text
Capacity = 5
```

Once full:

```text
Stack Overflow
```

But Java collection implementations manage their internal storage dynamically.

So users normally don't manually handle fixed capacity when using:

```java
Stack
```

or:

```java
ArrayDeque
```

---

# 91. Array Resizing Concept

Dynamic array-based implementations can maintain an internal array.

Suppose capacity is:

```text
4
```

and it becomes full:

```text
[10][20][30][40]
```

To accommodate more elements, the implementation can allocate a larger array and copy elements.

Conceptually:

```text
old array
[10][20][30][40]

        ↓ resize

new larger storage
[10][20][30][40][ ][ ][ ][ ]
```

A resize operation itself can take:

```text
O(n)
```

because elements may need to be copied.

But dynamic-array implementations are designed so that push operations have good **amortized** performance.

---

# 92. Amortized O(1)

This is an interview-level concept.

A particular insertion during resizing may cost:

```text
O(n)
```

But resizing does not happen on every insertion.

Across a large sequence of insertions, the average cost per insertion is generally:

```text
O(1) amortized
```

So don't blindly say:

> "Every possible internal operation is always exactly O(1)."

Instead distinguish:

```text
Typical stack push → O(1)
Dynamic resizing event → potentially O(n)
Amortized push → O(1)
```

---

# 93. Stack Space Complexity

If Stack contains:

```text
n
```

elements, additional storage is:

```text
O(n)
```

because all `n` elements/references need to be stored.

So:

```text
Time:
push → O(1)
pop  → O(1)
peek → O(1)

Space:
O(n)
```

---

# 94. Parentheses Algorithm Complexity

For:

```text
String length = n
```

we scan the string once.

Therefore:

```text
Time = O(n)
```

In the worst case, all opening brackets can be stored:

```text
Space = O(n)
```

So:

```text
Time → O(n)
Space → O(n)
```

---

# 95. String Reversal Complexity

For a string of length `n`:

```text
push each character → O(n)
pop each character  → O(n)
```

Total:

```text
O(n)
```

Additional Stack storage:

```text
O(n)
```

---

# 96. Common Stack Mistakes

## Mistake 1: Thinking Stack is FIFO

Wrong:

```text
First In → First Out
```

That's Queue.

Stack:

```text
Last In → First Out
```

---

## Mistake 2: Confusing `peek()` and `pop()`

```text
peek → doesn't remove
pop  → removes
```

---

## Mistake 3: Calling `pop()` on empty Stack

Can cause:

```text
EmptyStackException
```

---

## Mistake 4: Thinking `search()` returns index

It returns:

```text
1-based position from top
```

---

## Mistake 5: Confusing Java Stack with JVM stack

These are different concepts.

---

# 97. Important Stack Mental Model

Whenever you see:

```text
push(A)
push(B)
push(C)
pop()
```

think:

```text
push A

A
```

then:

```text
B
A
```

then:

```text
C
B
A
```

then:

```text
pop → C
```

Final:

```text
B
A
```

Always visualize the **TOP**.

---

# 98. One Complete Example

```java
import java.util.Stack;

public class Main {

    public static void main(String[] args) {

        Stack<String> stack = new Stack<>();

        stack.push("Java");
        stack.push("Python");
        stack.push("C++");

        System.out.println("Stack = " + stack);

        System.out.println("Top = " + stack.peek());

        String removed = stack.pop();

        System.out.println("Removed = " + removed);

        System.out.println("Top after pop = " + stack.peek());

        System.out.println("Size = " + stack.size());

        System.out.println("Empty = " + stack.isEmpty());
    }
}
```

Execution:

### After Java

```text
Java
```

### After Python

```text
Python
Java
```

### After C++

```text
C++
Python
Java
```

`peek()`:

```text
C++
```

`pop()`:

```text
C++
```

Remaining:

```text
Python
Java
```

---

# 99. The Most Important Conceptual Connection

The Stack concept connects directly to many other Java topics:

```text
Stack
 │
 ├── Recursion
 │
 ├── Method calls
 │
 ├── Exception stack traces
 │
 ├── Backtracking
 │
 ├── DFS
 │
 ├── Parentheses matching
 │
 ├── Expression evaluation
 │
 ├── Undo
 │
 ├── Browser history
 │
 └── Parsing
```

So Stack isn't merely another Collection.

It is a fundamental computational pattern:

> **When the most recently stored information must be processed first, Stack is a natural abstraction.**

---

# 100. Interview: What is a Stack?

### Good interview answer

> A Stack is a linear data structure that follows the LIFO principle, which means Last In, First Out. In a Stack, insertion and deletion are performed from the same end called the top. The primary operations are push, pop, and peek. Push inserts an element at the top, pop removes and returns the top element, and peek returns the top element without removing it. Stack operations are generally O(1). Common applications include recursion, function-call management, undo operations, backtracking, DFS, parentheses matching, and expression evaluation.

---

# 101. Interview: Why is Stack LIFO?

> Stack is LIFO because the element inserted most recently becomes the element at the top. Since insertion and removal happen from the same end, that newest element must be removed before the older elements underneath it. This is why the last inserted element is the first one removed.

---

# 102. Interview: Stack vs Queue?

> A Stack follows LIFO, while a Queue follows FIFO. In a Stack, insertion and deletion occur at the top, whereas in a Queue, insertion generally occurs at the rear and deletion occurs from the front. Stack is useful for problems such as undo, recursion, DFS and backtracking, while Queue is useful when elements should be processed in arrival order, such as scheduling and BFS.

---

# 103. Interview: What is the difference between `peek()` and `pop()`?

> Both access the top element, but `peek()` only returns the top element without removing it, whereas `pop()` returns the top element and removes it from the Stack. Therefore, repeated `peek()` calls return the same top element until another operation changes the Stack, while repeated `pop()` calls remove elements one by one.

---

# 104. Interview: What happens if `pop()` is called on an empty Stack?

For Java's `Stack`:

```java
stack.pop();
```

when empty results in:

```text
EmptyStackException
```

Conceptually this is called:

> **Stack Underflow**

---

# 105. Interview: What is Stack Overflow?

There are two contexts.

### Data structure

When a fixed-capacity Stack has no room for another element:

```text
Stack Overflow
```

### JVM/runtime

Excessive recursive method calls can exhaust the JVM thread's stack, commonly producing:

```text
StackOverflowError
```

These are related by the word "stack" but are not the same event.

---

# 106. Very Important: `StackOverflowError` vs `EmptyStackException`

Do not confuse these.

### Empty Java Collection Stack

```java
stack.pop();
```

when empty:

```text
EmptyStackException
```

### Excessive recursion

```java
static void fun() {
    fun();
}
```

eventually:

```text
StackOverflowError
```

Why?

Because recursive method calls keep consuming call-stack space.

---

# 107. Interview: Is Java `Stack` Recommended?

A good modern answer:

> Java provides `java.util.Stack`, but it is a legacy class that extends `Vector`. For new code, when I need Stack behavior, I would generally use the `Deque` interface with an `ArrayDeque` implementation, for example `Deque<Integer> stack = new ArrayDeque<>();`. This provides the Stack operations `push`, `pop`, and `peek` without relying on the legacy `Stack` class.

---

# 108. Interview: Why Use `Deque` for Stack?

> `Deque` is a double-ended queue abstraction that supports insertion and removal from both ends. By consistently using one end through operations such as `push`, `pop`, and `peek`, it naturally behaves as a Stack. `ArrayDeque` is a modern implementation commonly used for this purpose.

---

# 109. Interview: Can Stack Store Duplicate Elements?

Yes.

```java
Stack<Integer> stack = new Stack<>();

stack.push(10);
stack.push(10);
```

Both values can exist.

Stack does not enforce uniqueness.

---

# 110. Interview: Can Stack store `null`?

For Java's `Stack`:

```java
stack.push(null);
```

is allowed.

For:

```java
ArrayDeque
```

`null` elements are not allowed.

This is a useful distinction.

---

# 111. Interview: What is the Time Complexity of Stack?

For a properly implemented Stack:

```text
push → O(1)
pop  → O(1)
peek → O(1)
isEmpty → O(1)
```

Space:

```text
O(n)
```

for `n` stored elements.

For dynamically resizing implementations, an individual resize can cost O(n), while insertion remains O(1) amortized.

---

# 112. Final Stack Cheat Sheet

```text
                 STACK
                   │
                   ↓
                 LIFO
       Last In → First Out
                   │
          ┌────────┼────────┐
          ↓        ↓        ↓
        push      pop      peek
          │        │        │
       insert    remove    view
          │        │        │
          └──── TOP ────────┘
```

### Fundamental operations

```text
push()     → insert
pop()      → remove + return top
peek()     → return top without removing
isEmpty()  → check empty
size()     → number of elements
```

### Complexity

```text
push     O(1)
pop      O(1)
peek     O(1)
isEmpty  O(1)
size     O(1)
space    O(n)
```

### Main applications

```text
Recursion
Function calls
Undo
Browser back
Backtracking
DFS
Parentheses matching
Expression evaluation
Expression conversion
String reversal
Parsing
```

### Java

Legacy:

```java
Stack<Integer> stack = new Stack<>();
```

Modern:

```java
Deque<Integer> stack = new ArrayDeque<>();
```

with:

```java
stack.push(x);
stack.pop();
stack.peek();
```

---

# 113. The One Rule You Should Never Forget

Whenever a problem says:

> **"The most recently added/visited/processed item must be handled first."**

immediately think:

```text
                 STACK
                   ↓
                  LIFO
                   ↓
             TOP is important
```

And whenever you see:

```java
push(A);
push(B);
push(C);
pop();
```

the answer is always:

```text
C
```

because:

```text
Last In → First Out
```

---

# 114. Practice Problems — Do These Yourself

These are deliberately arranged from basic to interview-level.

### Problem 1 — Basic Stack

Create a Stack of integers and:

1. Push `10, 20, 30, 40, 50`
2. Print the top
3. Pop two elements
4. Print the remaining Stack
5. Print its size

---

### Problem 2 — Reverse a String

Write:

```java
static String reverse(String str)
```

using a Stack.

Input:

```text
HELLO
```

Expected:

```text
OLLEH
```

---

### Problem 3 — Balanced Parentheses

Write:

```java
static boolean isBalanced(String str)
```

Examples:

```text
"{[()]}" → true
"{[(])}" → false
"((()))" → true
"((()"   → false
```

---

### Problem 4 — Remove Adjacent Duplicates

Given:

```text
abbaca
```

Repeatedly remove adjacent equal characters.

Expected result:

```text
ca
```

A Stack is a very natural solution.

---

### Problem 5 — Postfix Evaluation

Evaluate:

```text
2 3 1 * + 9 -
```

using a Stack.

You should manually trace the Stack after **every token** before writing the code.

---

### Problem 6 — Min Stack

Design a Stack that supports:

```text
push()
pop()
top()
getMin()
```

with `getMin()` returning the minimum element efficiently.

Example:

```text
push(5)
push(3)
push(7)
push(2)
```

Then:

```text
getMin() → 2
```

After:

```text
pop()
```

the minimum should become:

```text
3
```

This is a classic interview problem because it tests whether you understand how another piece of information can be maintained alongside Stack data.

---

## Final conceptual picture

The entire topic can be reduced to this mental model:

```text
                STACK
                  │
                  ↓
                LIFO
                  │
          ┌───────┴────────┐
          ↓                ↓
      INSERTION         REMOVAL
          ↓                ↓
        push()           pop()
          │                │
          └────── TOP ─────┘
                  │
                peek()
                  ↓
          observe top only
```

And in **modern Java**:

```java
Deque<Integer> stack = new ArrayDeque<>();
```

is the important pattern to remember, while `java.util.Stack` is the older legacy Stack class you should still understand because you will encounter it frequently in existing code, textbooks, exams, and interview questions.
