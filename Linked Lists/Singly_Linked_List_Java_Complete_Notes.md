# Singly Linked List in Java --- Complete Notes

## 1. What is a Linked List?

A **Linked List** is a linear data structure made up of **nodes**.

Each node contains:

1.  `data` --- the value stored in the node.
2.  `next` --- a reference to the next node.

### Basic node

``` text
┌──────────┬─────────────┐
│   data   │    next     │
└──────────┴─────────────┘
```

Example:

``` text
head
 ↓
┌──────┬─────┐     ┌──────┬─────┐     ┌──────┬──────┐
│  10  │  ●──┼────→│  20  │  ●──┼────→│  30  │ null │
└──────┴─────┘     └──────┴─────┘     └──────┴──────┘
```

The last node points to `null` because there is no node after it.

------------------------------------------------------------------------

# 2. Node Class

The basic Java node is:

``` java
class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}
```

### Explanation

``` java
int data;
```

Stores the value.

``` java
Node next;
```

Stores a reference to another `Node`.

``` java
this.data = data;
```

Stores the constructor value inside the node.

``` java
this.next = null;
```

Initially, the node does not point to another node.

------------------------------------------------------------------------

# 3. What is `head`?

`head` stores the reference to the **first node**.

``` java
Node head = null;
```

Initially:

``` text
head
 ↓
null
```

After adding `10`:

``` text
head
 ↓
┌──────┬──────┐
│  10  │ null │
└──────┴──────┘
```

For three nodes:

``` text
head
 ↓
[10 | ●] → [20 | ●] → [30 | null]
```

### Important rule

``` text
head = first node
last node.next = null
```

------------------------------------------------------------------------

# 4. Complete Operations

The menu in the question contains:

``` text
1. Insert at beginning
2. Insert at end
3. Insert at position
4. Delete at beginning
5. Delete at end
6. Delete by value
7. Search
8. Reverse
9. Display
10. Size
11. Exit
```

We will implement all of them.

------------------------------------------------------------------------

# 5. Complete Menu-Driven Java Program

For this program, **positions are 1-based**.

That means:

``` text
Position 1 → first node
Position 2 → second node
Position 3 → third node
```

``` java
import java.util.Scanner;

public class Main {

    // Node class
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    // Head of linked list
    static Node head = null;

    // 1. Insert at beginning
    static void insertAtBeginning(int data) {

        Node newNode = new Node(data);

        newNode.next = head;
        head = newNode;
    }

    // 2. Insert at end
    static void insertAtEnd(int data) {

        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
            return;
        }

        Node current = head;

        while (current.next != null) {
            current = current.next;
        }

        current.next = newNode;
    }

    // 3. Insert at position
    static void insertAtPosition(int data, int position) {

        if (position < 1) {
            System.out.println("Invalid position");
            return;
        }

        if (position == 1) {
            insertAtBeginning(data);
            return;
        }

        Node newNode = new Node(data);
        Node current = head;

        for (int i = 1; i < position - 1 && current != null; i++) {
            current = current.next;
        }

        if (current == null) {
            System.out.println("Invalid position");
            return;
        }

        newNode.next = current.next;
        current.next = newNode;
    }

    // 4. Delete at beginning
    static void deleteAtBeginning() {

        if (head == null) {
            System.out.println("List is empty");
            return;
        }

        head = head.next;
    }

    // 5. Delete at end
    static void deleteAtEnd() {

        if (head == null) {
            System.out.println("List is empty");
            return;
        }

        if (head.next == null) {
            head = null;
            return;
        }

        Node current = head;

        while (current.next.next != null) {
            current = current.next;
        }

        current.next = null;
    }

    // 6. Delete by value
    static void deleteByValue(int value) {

        if (head == null) {
            System.out.println("List is empty");
            return;
        }

        if (head.data == value) {
            head = head.next;
            return;
        }

        Node current = head;

        while (current.next != null && current.next.data != value) {
            current = current.next;
        }

        if (current.next == null) {
            System.out.println("Value not found");
            return;
        }

        current.next = current.next.next;
    }

    // 7. Search
    static void search(int value) {

        Node current = head;
        int position = 1;

        while (current != null) {

            if (current.data == value) {
                System.out.println("Value found at position " + position);
                return;
            }

            current = current.next;
            position++;
        }

        System.out.println("Value not found");
    }

    // 8. Reverse
    static void reverse() {

        Node previous = null;
        Node current = head;

        while (current != null) {

            Node nextNode = current.next;

            current.next = previous;

            previous = current;
            current = nextNode;
        }

        head = previous;
    }

    // 9. Display
    static void display() {

        if (head == null) {
            System.out.println("List is empty");
            return;
        }

        Node current = head;

        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }

        System.out.println("null");
    }

    // 10. Size
    static int size() {

        int count = 0;
        Node current = head;

        while (current != null) {
            count++;
            current = current.next;
        }

        return count;
    }

    // Main method
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== Singly Linked List =====");
            System.out.println("1. Insert at beginning");
            System.out.println("2. Insert at end");
            System.out.println("3. Insert at position");
            System.out.println("4. Delete at beginning");
            System.out.println("5. Delete at end");
            System.out.println("6. Delete by value");
            System.out.println("7. Search");
            System.out.println("8. Reverse");
            System.out.println("9. Display");
            System.out.println("10. Size");
            System.out.println("11. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter value: ");
                    int value1 = sc.nextInt();
                    insertAtBeginning(value1);
                    break;

                case 2:
                    System.out.print("Enter value: ");
                    int value2 = sc.nextInt();
                    insertAtEnd(value2);
                    break;

                case 3:
                    System.out.print("Enter value: ");
                    int value3 = sc.nextInt();

                    System.out.print("Enter position: ");
                    int position = sc.nextInt();

                    insertAtPosition(value3, position);
                    break;

                case 4:
                    deleteAtBeginning();
                    break;

                case 5:
                    deleteAtEnd();
                    break;

                case 6:
                    System.out.print("Enter value to delete: ");
                    int deleteValue = sc.nextInt();

                    deleteByValue(deleteValue);
                    break;

                case 7:
                    System.out.print("Enter value to search: ");
                    int searchValue = sc.nextInt();

                    search(searchValue);
                    break;

                case 8:
                    reverse();
                    System.out.println("List reversed");
                    break;

                case 9:
                    display();
                    break;

                case 10:
                    System.out.println("Size = " + size());
                    break;

                case 11:
                    System.out.println("Program ended.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
```

------------------------------------------------------------------------

# 6. Operation 1 --- Insert at Beginning

Suppose the list is:

``` text
head
 ↓
10 → 20 → 30 → null
```

We want to insert `5`.

## Step 1

Create a new node:

``` java
Node newNode = new Node(5);
```

``` text
newNode
   ↓
[5 | null]

head
 ↓
[10] → [20] → [30] → null
```

## Step 2

``` java
newNode.next = head;
```

Now:

``` text
newNode
   ↓
[5 | ●] ─────→ [10] → [20] → [30] → null
```

## Step 3

``` java
head = newNode;
```

Final:

``` text
head
 ↓
[5] → [10] → [20] → [30] → null
```

### Code

``` java
static void insertAtBeginning(int data) {

    Node newNode = new Node(data);

    newNode.next = head;
    head = newNode;
}
```

### Complexity

``` text
Time:  O(1)
Space: O(1)
```

Why `O(1)`?

We don't travel through the list. We directly modify `head`.

------------------------------------------------------------------------

# 7. Operation 2 --- Insert at End

Suppose:

``` text
head
 ↓
10 → 20 → 30 → null
```

Insert `40`.

Create:

``` text
newNode
   ↓
[40 | null]
```

Start from head:

``` text
current
   ↓
10 → 20 → 30 → null
```

Move:

``` text
current
          ↓
10 → 20 → 30 → null
```

`30.next == null`, so `30` is the last node.

Then:

``` java
current.next = newNode;
```

Final:

``` text
head
 ↓
10 → 20 → 30 → 40 → null
```

### Code

``` java
static void insertAtEnd(int data) {

    Node newNode = new Node(data);

    if (head == null) {
        head = newNode;
        return;
    }

    Node current = head;

    while (current.next != null) {
        current = current.next;
    }

    current.next = newNode;
}
```

### Complexity

``` text
Time:  O(n)
Space: O(1)
```

We may have to visit every node to reach the last node.

------------------------------------------------------------------------

# 8. Operation 3 --- Insert at Position

Suppose:

``` text
Position: 1    2    3    4
           ↓    ↓    ↓    ↓
List:     10 → 20 → 30 → 40 → null
```

Insert `25` at position `3`.

We need:

``` text
10 → 20 → 25 → 30 → 40 → null
```

The important idea is:

``` text
Before:

10 → 20 → 30
     ↑
   current
```

Create:

``` text
newNode = 25
```

Then:

``` java
newNode.next = current.next;
```

gives:

``` text
20 ─────────→ 30
      ↑
     newNode
      25
```

More clearly:

``` text
10 → 20 → 30 → 40
     ↑
   current

25 → 30
```

Then:

``` java
current.next = newNode;
```

Final:

``` text
10 → 20 → 25 → 30 → 40 → null
```

### Code

``` java
static void insertAtPosition(int data, int position) {

    if (position < 1) {
        System.out.println("Invalid position");
        return;
    }

    if (position == 1) {
        insertAtBeginning(data);
        return;
    }

    Node newNode = new Node(data);
    Node current = head;

    for (int i = 1; i < position - 1 && current != null; i++) {
        current = current.next;
    }

    if (current == null) {
        System.out.println("Invalid position");
        return;
    }

    newNode.next = current.next;
    current.next = newNode;
}
```

### Complexity

``` text
Time:  O(n)
Space: O(1)
```

In the worst case we travel through the list to reach the required
position.

------------------------------------------------------------------------

# 9. Operation 4 --- Delete at Beginning

Suppose:

``` text
head
 ↓
10 → 20 → 30 → null
```

Delete first node.

We simply do:

``` java
head = head.next;
```

Before:

``` text
head
 ↓
10 → 20 → 30 → null
```

After:

``` text
head
 ↓
20 → 30 → null
```

The old `10` node is no longer connected to the list.

### Code

``` java
static void deleteAtBeginning() {

    if (head == null) {
        System.out.println("List is empty");
        return;
    }

    head = head.next;
}
```

### Complexity

``` text
Time:  O(1)
Space: O(1)
```

------------------------------------------------------------------------

# 10. Operation 5 --- Delete at End

Suppose:

``` text
10 → 20 → 30 → null
```

We want to delete `30`.

We cannot directly go backward because this is a **singly** linked list.

So we find the second-last node:

``` text
10 → 20 → 30 → null
     ↑
   current
```

Then:

``` java
current.next = null;
```

Result:

``` text
10 → 20 → null
```

### Code

``` java
static void deleteAtEnd() {

    if (head == null) {
        System.out.println("List is empty");
        return;
    }

    if (head.next == null) {
        head = null;
        return;
    }

    Node current = head;

    while (current.next.next != null) {
        current = current.next;
    }

    current.next = null;
}
```

### Why `current.next.next`?

Suppose:

``` text
10 → 20 → 30 → null
          ↑
        current
```

Then:

``` text
current.next
```

means `30`.

And:

``` text
current.next.next
```

means `30.next`, which is `null`.

Therefore, the loop stops at `20`.

### Complexity

``` text
Time:  O(n)
Space: O(1)
```

------------------------------------------------------------------------

# 11. Operation 6 --- Delete by Value

Suppose:

``` text
10 → 20 → 30 → 40 → null
```

Delete value `30`.

We need to find the node before `30`:

``` text
10 → 20 → 30 → 40
     ↑    ↑
 current  delete
```

Then:

``` java
current.next = current.next.next;
```

Before:

``` text
20 → 30 → 40
```

After:

``` text
20 ─────────→ 40
```

Final:

``` text
10 → 20 → 40 → null
```

### Code

``` java
static void deleteByValue(int value) {

    if (head == null) {
        System.out.println("List is empty");
        return;
    }

    if (head.data == value) {
        head = head.next;
        return;
    }

    Node current = head;

    while (current.next != null && current.next.data != value) {
        current = current.next;
    }

    if (current.next == null) {
        System.out.println("Value not found");
        return;
    }

    current.next = current.next.next;
}
```

### Important

``` java
current.next = current.next.next;
```

means:

> Skip the node after `current`.

Example:

``` text
Before:

current
   ↓
[20] → [30] → [40]

After:

current
   ↓
[20] ─────────→ [40]
```

### Complexity

``` text
Time:  O(n)
Space: O(1)
```

------------------------------------------------------------------------

# 12. Operation 7 --- Search

Suppose:

``` text
10 → 20 → 30 → 40 → null
```

Search for `30`.

Start:

``` text
current
   ↓
10 → 20 → 30 → 40 → null
```

Check:

``` text
10 == 30? No
```

Move:

``` text
10 → 20 → 30 → 40
      ↑
    current
```

Check:

``` text
20 == 30? No
```

Move:

``` text
10 → 20 → 30 → 40
           ↑
         current
```

Check:

``` text
30 == 30? YES
```

So position is `3`.

### Code

``` java
static void search(int value) {

    Node current = head;
    int position = 1;

    while (current != null) {

        if (current.data == value) {
            System.out.println("Value found at position " + position);
            return;
        }

        current = current.next;
        position++;
    }

    System.out.println("Value not found");
}
```

### Complexity

``` text
Best case:    O(1)
Worst case:   O(n)
Space:        O(1)
```

If the value is the first element, we find it immediately.

------------------------------------------------------------------------

# 13. Operation 8 --- Reverse

This is one of the most important linked-list operations.

Suppose:

``` text
10 → 20 → 30 → null
```

We want:

``` text
30 → 20 → 10 → null
```

We use three references:

``` java
Node previous = null;
Node current = head;
Node nextNode;
```

Initial:

``` text
previous     current
   ↓            ↓
 null          10 → 20 → 30 → null
```

## Step 1

Save next:

``` java
nextNode = current.next;
```

``` text
nextNode
   ↓
20 → 30 → null
```

Reverse current link:

``` java
current.next = previous;
```

Now:

``` text
10 → null
```

Move:

``` java
previous = current;
current = nextNode;
```

Now:

``` text
previous
   ↓
  10 → null

current
   ↓
  20 → 30 → null
```

## Step 2

Reverse `20`:

``` text
previous
   ↓
10 ← 20      30 → null
     ↑
   current
```

After moving:

``` text
previous
   ↓
10 ← 20

current
   ↓
30 → null
```

## Step 3

Reverse `30`:

``` text
30 → 20 → 10 → null
```

Finally:

``` java
head = previous;
```

So:

``` text
head
 ↓
30 → 20 → 10 → null
```

### Code

``` java
static void reverse() {

    Node previous = null;
    Node current = head;

    while (current != null) {

        Node nextNode = current.next;

        current.next = previous;

        previous = current;
        current = nextNode;
    }

    head = previous;
}
```

### Complexity

``` text
Time:  O(n)
Space: O(1)
```

This is an **in-place reversal** because we don't create new nodes.

------------------------------------------------------------------------

# 14. Operation 9 --- Display

Display means traversing the list from `head` until `null`.

Example:

``` text
head
 ↓
10 → 20 → 30 → null
```

Code:

``` java
static void display() {

    if (head == null) {
        System.out.println("List is empty");
        return;
    }

    Node current = head;

    while (current != null) {
        System.out.print(current.data + " -> ");
        current = current.next;
    }

    System.out.println("null");
}
```

### Working

``` text
current = head

10 → 20 → 30 → null
↑
current

print 10
```

Then:

``` text
10 → 20 → 30 → null
     ↑
   current

print 20
```

Then:

``` text
10 → 20 → 30 → null
          ↑
        current

print 30
```

Then:

``` text
current = null
```

Loop stops.

Output:

``` text
10 -> 20 -> 30 -> null
```

### Complexity

``` text
Time:  O(n)
Space: O(1)
```

------------------------------------------------------------------------

# 15. Operation 10 --- Size

Size means the **number of nodes** in the linked list.

Example:

``` text
10 → 20 → 30 → 40 → null
```

Size = `4`.

### Code

``` java
static int size() {

    int count = 0;
    Node current = head;

    while (current != null) {
        count++;
        current = current.next;
    }

    return count;
}
```

### Dry run

``` text
count = 0

current → 10
count = 1

current → 20
count = 2

current → 30
count = 3

current → 40
count = 4

current → null

return 4
```

### Complexity

``` text
Time:  O(n)
Space: O(1)
```

------------------------------------------------------------------------

# 16. Operation 11 --- Exit

Exit simply terminates the program.

``` java
case 11:
    System.out.println("Program ended.");
    sc.close();
    return;
```

`return` exits the `main()` method.

------------------------------------------------------------------------

# 17. Complete Example / Dry Run

Suppose the user performs:

``` text
1 → Insert at beginning → 20

2 → Insert at end → 30

1 → Insert at beginning → 10

3 → Insert at position → 25 at position 3

9 → Display
```

## Step 1

Insert `20` at beginning:

``` text
head
 ↓
20 → null
```

## Step 2

Insert `30` at end:

``` text
head
 ↓
20 → 30 → null
```

## Step 3

Insert `10` at beginning:

``` text
head
 ↓
10 → 20 → 30 → null
```

## Step 4

Insert `25` at position 3:

``` text
head
 ↓
10 → 20 → 25 → 30 → null
```

## Step 5

Display:

``` text
10 -> 20 -> 25 -> 30 -> null
```

------------------------------------------------------------------------

# 18. Example of Deletion

Starting list:

``` text
10 → 20 → 25 → 30 → null
```

### Delete beginning

``` text
20 → 25 → 30 → null
```

### Delete end

``` text
20 → 25 → null
```

### Delete by value `25`

``` text
20 → null
```

------------------------------------------------------------------------

# 19. Example of Reverse

Before:

``` text
head
 ↓
10 → 20 → 30 → 40 → null
```

After reverse:

``` text
head
 ↓
40 → 30 → 20 → 10 → null
```

------------------------------------------------------------------------

# 20. Complexity Table

  Operation               Time Complexity   Extra Space
  --------------------- ----------------- -------------
  Insert at beginning                O(1)          O(1)
  Insert at end                      O(n)          O(1)
  Insert at position                 O(n)          O(1)
  Delete at beginning                O(1)          O(1)
  Delete at end                      O(n)          O(1)
  Delete by value                    O(n)          O(1)
  Search                             O(n)          O(1)
  Reverse                            O(n)          O(1)
  Display                            O(n)          O(1)
  Size                               O(n)          O(1)

### Why is beginning insertion O(1)?

Because we directly change `head`:

``` java
newNode.next = head;
head = newNode;
```

No traversal is required.

### Why is beginning deletion O(1)?

Only:

``` java
head = head.next;
```

is required.

### Why are most other operations O(n)?

Because we may need to traverse the linked list node by node.

------------------------------------------------------------------------

# 21. Important Linked List Patterns to Memorize

## Pattern 1 --- Traverse

``` java
Node current = head;

while (current != null) {
    // work with current.data
    current = current.next;
}
```

Use this for:

-   Display
-   Search
-   Size
-   Many other operations

------------------------------------------------------------------------

## Pattern 2 --- Insert at beginning

``` java
newNode.next = head;
head = newNode;
```

Remember:

``` text
new node → old head
       ↓
      head
```

------------------------------------------------------------------------

## Pattern 3 --- Move to next node

``` java
current = current.next;
```

This means:

> Move the reference `current` to the next node.

------------------------------------------------------------------------

## Pattern 4 --- Skip a node

``` java
current.next = current.next.next;
```

Example:

``` text
Before:

20 → 30 → 40

After:

20 ─────→ 40
```

The `30` node is skipped.

------------------------------------------------------------------------

## Pattern 5 --- Reverse

``` java
Node previous = null;
Node current = head;

while (current != null) {

    Node nextNode = current.next;
    current.next = previous;

    previous = current;
    current = nextNode;
}

head = previous;
```

Remember:

``` text
SAVE → REVERSE → MOVE
```

------------------------------------------------------------------------

# 22. Common Mistakes

## Mistake 1

Writing:

``` java
current = null;
```

when you wanted to move forward.

Correct:

``` java
current = current.next;
```

------------------------------------------------------------------------

## Mistake 2

Forgetting to update `head` when inserting at beginning.

Wrong:

``` java
newNode.next = head;
```

Correct:

``` java
newNode.next = head;
head = newNode;
```

------------------------------------------------------------------------

## Mistake 3

Wrong order during insertion

Suppose:

``` text
10 → 20 → 30
```

You want to insert `25` after `20`.

First:

``` java
newNode.next = current.next;
```

Then:

``` java
current.next = newNode;
```

Correct:

``` text
10 → 20 → 25 → 30
```

------------------------------------------------------------------------

## Mistake 4

Trying to use an index directly

A linked list does not normally support:

``` java
list[2]
```

like an array.

To reach position 3, traverse:

``` text
head → node 1 → node 2 → node 3
```

------------------------------------------------------------------------

# 23. Linked List vs Array

  Feature                   Array           Linked List
  ------------------------- --------------- -----------------------
  Memory                    Contiguous      Nodes can be separate
  Size                      Usually fixed   Dynamic
  Random access             Fast, O(1)      Slow, O(n)
  Insert at beginning       O(n)            O(1)
  Delete at beginning       O(n)            O(1)
  Extra pointer/reference   No              Yes
  Traversal                 Index           References

------------------------------------------------------------------------

# 24. Viva Questions

### Q1. What is a linked list?

A linked list is a linear data structure made of nodes where each node
stores data and a reference to the next node.

### Q2. What does `head` store?

`head` stores a reference to the first node.

### Q3. What does the last node contain in its `next`?

`null`.

### Q4. Why is insertion at beginning O(1)?

Because we only modify the new node's `next` and `head`.

### Q5. Why is insertion at end O(n)?

Because without a tail reference, we need to traverse to the last node.

### Q6. What is traversal?

Visiting nodes one by one from `head` until `null`.

### Q7. What does `current = current.next` do?

It moves the `current` reference to the next node.

### Q8. How do you delete a node from the middle?

Make the previous node point to the node after the node being deleted:

``` java
current.next = current.next.next;
```

### Q9. What are the three references used in reversal?

``` text
previous
current
nextNode
```

### Q10. What is the time complexity of searching?

Worst case: `O(n)`.

------------------------------------------------------------------------

# 25. Final Mental Diagram

Always imagine a singly linked list like this:

``` text
                         SINGLY LINKED LIST

                              head
                               ↓
                    ┌──────────────────┐
                    │                  │
                    ▼                  │
              ┌─────────┐       ┌─────────┐
              │  data   │       │  data   │
              │   10    │       │   20    │
              │ next ──────────→│ next ──────────→ ...
              └─────────┘       └─────────┘

                         Last node
                             ↓
                       ┌──────────┐
                       │   30     │
                       │ next=null│
                       └──────────┘
```

The most important relationship is:

``` text
head
 ↓
NODE → NODE → NODE → null
```

And every node is:

``` text
NODE = data + next
```

------------------------------------------------------------------------

# 26. One-Page Revision

``` text
SINGLY LINKED LIST
│
├── Node
│   ├── data
│   └── next
│
├── head
│   └── points to first node
│
├── Insert
│   ├── Beginning → O(1)
│   ├── End       → O(n)
│   └── Position  → O(n)
│
├── Delete
│   ├── Beginning → O(1)
│   ├── End       → O(n)
│   └── By value  → O(n)
│
├── Search        → O(n)
│
├── Reverse       → O(n)
│
├── Display       → O(n)
│
└── Size          → O(n)
```

### Core formulas

``` java
// Traverse
current = current.next;

// Insert at beginning
newNode.next = head;
head = newNode;

// Delete beginning
head = head.next;

// Skip/delete a node
current.next = current.next.next;

// Reverse
nextNode = current.next;
current.next = previous;
previous = current;
current = nextNode;
```

## Final definition for viva

> **A singly linked list is a dynamic linear data structure consisting
> of nodes, where each node contains data and a reference to the next
> node. The list starts from a `head` reference, and the `next`
> reference of the last node is `null`.**
