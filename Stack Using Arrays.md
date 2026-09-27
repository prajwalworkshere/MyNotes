```
import java.util.Scanner;

public class StackArrayIntDemo {

    private final int[] stack;
    private int top = -1;

    StackArrayIntDemo(int capacity) {
        stack = new int[capacity];
    }

    void push(int value) {
        if (top == stack.length - 1) {  
            System.out.println("Stack Overflow");
            return;
        }
        //top++;
        //stack[top++];
        stack[++top] = value; 
        System.out.println(value + " pushed onto stack");
    }

    int pop() {
        if (isEmpty()) {
            System.out.println("Stack Underflow");
            return -1;
        }

        return stack[top--]; 
    }

    int peek() {
        if (isEmpty()) {
            return -1;
        }

        return stack[top];
    }

    boolean isEmpty() {
        return top == -1;
    }

    boolean isFull() {
        return top == stack.length - 1;
    }

    void display() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return;
        }

        System.out.print("Stack elements (top to bottom): ");
        for (int i = top; i >= 0; i--) {
            System.out.print(stack[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter stack capacity: ");
        int capacity = scanner.nextInt();

        if (capacity <= 0) {
            System.out.println("Capacity must be greater than zero");
            scanner.close();
            return;
        }

        StackArrayIntDemo stack = new StackArrayIntDemo(capacity);

        while (true) {
            System.out.println();
            System.out.println("Integer Stack Using Array");
            System.out.println("1. Push");
            System.out.println("2. Pop");
            System.out.println("3. Peek");
            System.out.println("4. Display");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter value to push: ");
                    stack.push(scanner.nextInt());
                    break;
                case 2:
                    if (!stack.isEmpty()) {
                        System.out.println(stack.pop() + " popped from stack");
                    } else {
                        stack.pop();
                    }
                    break;
                case 3:
                    if (stack.isEmpty()) {
                        System.out.println("Stack is empty");
                    } else {
                        System.out.println("Top element: " + stack.peek());
                    }
                    break;
                case 4:
                    stack.display();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}

```
 **short exam-friendly explanation** of each function, with the **logic + dry run**.

 ## 1\. Constructor

```
StackArrayIntDemo(int capacity) {
    stack = new int[capacity];
}
```

 **What it does:** Creates the array for the stack.

 **Logic:**

```
capacity = 5
→ creates stack[5]
→ top is initially -1
```

 Dry run:

```
stack: [ _ ][ _ ][ _ ][ _ ][ _ ]
top = -1
```

---

 ## 2\. `push()`

```
void push(int value) {
    if (top == stack.length - 1) {
        System.out.println("Stack Overflow");
        return;
    }
    stack[++top] = value;
}
```

 **What it does:** Adds an element to the top.

 **Logic:**

 1. Check if stack is full.
2. Increase `top`.
3. Store value at `stack[top]`.

 Dry run:

```
Before:
[10][20][  ][  ][  ]
     top = 1

push(30)

++top → 2
stack[2] = 30

After:
[10][20][30][  ][  ]
         ↑
       top = 2
```

---

 ## 3\. `pop()`

```
int pop() {
    if (isEmpty()) {
        return -1;
    }
    return stack[top--];
}
```

 **What it does:** Removes the top element.

 **Logic:**

 1. Check if empty.
2. Take `stack[top]`.
3. Decrease `top`.

 Dry run:

```
Before:
[10][20][30]
         ↑
       top=2

pop()

return stack[2] → 30
top-- → 1

After:
[10][20][30]
     ↑
   top=1
```

 `30` is no longer logically in the stack.

---

 ## 4\. `peek()`

```
int peek() {
    if (isEmpty()) {
        return -1;
    }
    return stack[top];
}
```

 **What it does:** Shows the top element **without removing it**.

 Dry run:

```
[10][20][30]
         ↑
       top=2

peek()
→ stack[2]
→ 30

top remains 2
```

 ### Remember:

```
peek = look
pop  = remove
```

---

 ## 5\. `isEmpty()`

```
boolean isEmpty() {
    return top == -1;
}
```

 **What it does:** Checks whether stack is empty.

 Dry run:

```
top = -1
→ true

top = 2
→ false
```

---

 ## 6\. `isFull()`

```
boolean isFull() {
    return top == stack.length - 1;
}
```

 **What it does:** Checks whether stack is full.

 If capacity = 5:

```
Indexes:  0  1  2  3  4
                     ↑
                  last index
```

 Dry run:

```
top = 4
→ true (full)

top = 2
→ false
```

---

 ## 7\. `display()`

```
for (int i = top; i >= 0; i--) {
    System.out.print(stack[i] + " ");
}
```

 **What it does:** Prints stack from **top → bottom**.

 Dry run:

```
[10][20][30]
         ↑
       top=2

i=2 → 30
i=1 → 20
i=0 → 10

Output:
30 20 10
```

---

 ## 8\. Main `switch`

```
switch(choice)
```

 This handles the user's menu choice:

```
1 → push()
2 → pop()
3 → peek()
4 → display()
5 → exit
```

 ### Overall logic

```
             Stack
               ↓
        ┌──────────────┐
        │ top = -1     │
        └──────────────┘
               ↓
        Push → top increases
               ↓
        Pop  → top decreases
               ↓
        Peek → only checks top
               ↓
        Display → top to bottom
```

 ### ⭐ One-line exam answers

 - **Push:** Insert element at top.
- **Pop:** Remove element from top.
- **Peek:** View top element without removing.
- **isEmpty:** Checks `top == -1`.
- **isFull:** Checks `top == capacity - 1`.
- **Display:** Prints elements from top to bottom.
- **Constructor:** Creates the stack array.
