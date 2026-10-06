import java.util.*;

class Stack {
    int array[];
    int top;
    int capacity;

    Stack(int size) {
        array = new int[size];
        top = -1;
        capacity = size;
    }

    public void push(int data) {
        if (top == capacity - 1) {
            System.out.println("Stack Overflow (Stack is Full)");
            return;
        }
        // Increment top and then insert value
        array[++top] = data;
        System.out.println("Element " + data + " pushed successfully.\n");
    }

    public void pop() {
        if (top == -1) {
            System.out.println("Stack Underflow (Stack is Empty)\n");
            return;
        }
        System.out.println("Element Popped: " + array[top] + "\n");
        top--;
    }

    // Renamed from peak() to peek()
    public void peek() {
        if (top == -1) {
            System.out.println("Stack is Empty\n");
            return;
        }
        System.out.println("Top Element: " + array[top] + "\n");
    }

    public void display() {
        if (top == -1) {
            System.out.println("Stack is Empty\n");
            return;
        }
        System.out.print("Stack Elements (Top to Bottom): ");
        for (int i = top; i >= 0; i--) {
            System.out.print(array[i] + " ");
        }
        System.out.println("\n");
    }
}

public class StackByArrays {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Size Of Stack: ");
        Stack stack = new Stack(sc.nextInt());
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
                    stack.pop();
                    break;
                case 3:
                    stack.peek();
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
