import java.util.*;	

class Stack {
    // Nested node class (static is a best practice if it doesn't need outer class instance access)
    static class Node {
        int data;
        Node next;
    
        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    Node top;

    void push(int value) {
        Node newnode = new Node(value);
        if (top == null) {
            top = newnode;
            System.out.println("Element " + value + " pushed successfully.\n");
            return;
        }
        newnode.next = top; 
        top = newnode; 
        System.out.println("Element " + value + " pushed successfully.\n");
    }

    int pop() {
        if (top == null) {
            System.out.println("Stack Underflow!");		
            return -1;
        }
        int poppedElement = top.data;
        top = top.next;
        return poppedElement;
    }

    int peek() {
        if (top == null) {
            System.out.println("Stack is Empty!");		
            return -1;
        }
        return top.data;
    }

    void display() {
        Node current = top;
        if (current == null) {
            System.out.println("Stack is Empty\n");
            return;
        }
        System.out.print("Elements Of Stack (Top to Bottom): ");
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println("\n");
    }
}

public class StackUsingLL {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Stack stack = new Stack();
        boolean condition = true;

        while (condition) {
            System.out.println("--- MENU ---");
            System.out.println("1. Add Element (Push)");
            System.out.println("2. Delete Element (Pop)");
            System.out.println("3. Show Top Element (Peek)");
            System.out.println("4. Display Elements");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter Element to Add: ");
                    stack.push(sc.nextInt());
                    break;
                case 2:
                    int popped = stack.pop();
                    if (popped != -1) {
                        System.out.println("Element Popped: " + popped + "\n");
                    }
                    break;
                case 3:
                    int topVal = stack.peek();
                    if (topVal != -1) {
                        System.out.println("Top Element: " + topVal + "\n");
                    }
                    break;
                case 4:
                    stack.display();
                    break;
                case 5:
                    System.out.println("Exiting program...");
                    condition = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.\n");
            }
        }
        sc.close();
    }
}

/*
In Java, declaring a class inside another class makes it a nested class. By default, if you don't use the static keyword, it becomes an inner class.
When we say “static is a best practice if it doesn't need outer class instance access,” it boils down to two main things: memory efficiency and architectural independence.
Here is a plain breakdown of what that actually means for your code:
--------------------------------------------------------------------------------------------------------
1. Memory and Pointers (Under the Hood)
-------------------------------------------------------------------------------------------------------
• Without static (class Node): Every single node object you create holds a secret, invisible pointer to the specific Stack instance that created it. Even if your node only holds an int data and a Node next, it is dragging around an extra reference to the entire Stack object in memory.

• With static (static class Node): The node becomes completely independent of the Stack instance. It only allocates memory for its own fields (data and next). If you create 10,000 nodes, saving that one extra pointer per node prevents unnecessary memory bloat.
-------------------------------------------------------------------------------------------------------
2. Access Restrictions
-------------------------------------------------------------------------------------------------------
• An inner class (non-static) has full access to the outer class's variables—even private ones. If Node wasn't static, code inside Node could directly read or change things inside Stack.

• A static nested class cannot look inside the outer class's instances. Since a node in a stack only cares about its own data and its next neighbor, it has absolutely no business looking at or changing anything else in the Stack class. Making it static enforces this clean separation.

======================================================================================================
A Simple Analogy 🏠
=======================================================================================================
Think of the outer class Stack as a House and the nested class Node as a Brick.
• Non-static (class Node): The brick is permanently glued to that specific house. To even look at the brick, you have to reference the whole house.
• Static (static class Node): The brick is just a brick. It can exist on its own blueprint. You happen to be using it to build a house (Stack), but the brick itself doesn't need to know anything about the plumbing or wiring of the house to do its job.
Rule of Thumb for Data Structures
Whenever you write helper classes for data structures—like a Node for a Stack, Queue, or Linked List, or a TreeNode for a Binary Tree—always make them static. They are purely structural containers and don't need to interact with the main data structure class.
Would you like to see what happens syntactically if you try to create a non-static node vs a static node from outside the class, or should we jump into writing a Queue implementation using this exact same static Node design?
=========================================================================================================
So, what is the actual difference when you use new Stack()?
=========================================================================================================

The difference isn't when they are created, but how they behave in memory once they are created.

=> Scenario A: Non-static (class Node)
---------------------------------------

When you eventually call stack.push(10), Java creates a node. Because it is non-static, that new node is physically linked to that specific stack object. It looks like this in memory:
text
[ stack Object ] <─────── (Secret Link) ─────── [ node Object (10) ]
Use code with caution.
If you create a second stack (Stack stack2 = new Stack();), a node pushed to stack2 cannot exist without being attached to stack2.

=> Scenario B: Static (static class Node)
-----------------------------------------

When you call stack.push(10), Java creates a node. Because it is static, it is completely independent. It does not look back at the stack. The stack simply holds a pointer to it:
text
[ stack Object ] ─────── (Pointer to Top) ──────> [ Node Object (10) ]
Use code with caution.
To sum it up:
• new Stack() only allocates memory for the stack controller (the top pointer).
• new Node() only happens inside the push() method.
• Using static doesn't change when the nodes are born; it just ensures that when they are born, they don't carry useless memory overhead linking them back to the parent stack wrapper.
Does this distinction between the Stack container and the individual Nodes make sense? If you're ready, we can use this exact static class Node blueprint to build a Queue (FIFO) next, or we can look at how to reverse your stack. Which one sounds better?


*/
