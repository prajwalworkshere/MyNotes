import java.util.*;

class Queue {
    // This is our helper container class for each item in the queue
    static class Node {
        int data;  // Stores the actual number
        Node next; // Stores the address/pointer to the next node in line

        Node(int data) {
            this.data = data;
            this.next = null; // By default, a new item doesn't point to anyone yet
        }
    }

    Node front; // Points to the very first person in line (we delete from here)
    Node rear;  // Points to the very last person in line (we add to here)

    // 1. Add an element to the back of the line (Enqueue)
    public void enqueue(int value) {
        Node newnode = new Node(value); // Create a fresh new node
        
        // If the line is completely empty, this new person is both the front and the back!
        if (front == null) {
            front = newnode;
            rear = newnode;
        } else {
            // If people are already in line:
            rear.next = newnode; // Make the current last person point to the newcomer
            rear = newnode;      // Move the 'rear' label to this new person
        }
        System.out.println("Element " + value + " added to queue.\n");
    }

    // 2. Remove an element from the front of the line (Dequeue)
    public int dequeue() {
        // If front is pointing to nothing, the queue is completely empty
        if (front == null) {
            System.out.println("Queue Underflow! (Queue is Empty)");
            return -1;
        }
        
        int removedData = front.data; // Save the number so we can return it
        front = front.next;           // Move the 'front' label to the next person in line
        
        // If the queue just became empty because we removed the last item:
        if (front == null) {
            rear = null; // Clean up the rear label too so it doesn't get stuck
        }
        
        return removedData;
    }

    // 3. Just look at the front item without removing it
    public int peek() {
        if (front == null) {
            System.out.println("Queue is Empty!");
            return -1;
        }
        return front.data;
    }

    // 4. Print out all the active numbers from Front to Rear
    public void display() {
        if (front == null) {
            System.out.println("Queue is Empty\n");
            return;
        }
        
        Node current = front; // Start walking from the front of the line
        System.out.print("Queue Elements (Front to Rear): ");
        
        // Keep moving forward until you hit the end (null)
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next; // Move to the next person
        }
        System.out.println("\n");
    }
}

public class SimpleQueueUsingLL {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Queue queue = new Queue();
        boolean condition = true;

        while (condition) {
            System.out.println("--- QUEUE MENU ---");
            System.out.println("1. Insert Element (Enqueue)");
            System.out.println("2. Delete Element (Dequeue)");
            System.out.println("3. Show Front Element (Peek)");
            System.out.println("4. Display Elements");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter Element to Insert: ");
                    queue.enqueue(sc.nextInt());
                    break;
                case 2:
                    int removed = queue.dequeue();
                    if (removed != -1) {
                        System.out.println("Element Removed: " + removed + "\n");
                    }
                    break;
                case 3:
                    int frontVal = queue.peek();
                    if (frontVal != -1) {
                        System.out.println("Front Element: " + frontVal + "\n");
                    }
                    break;
                case 4:
                    queue.display();
                    break;
                case 5:
                    System.out.println("Exiting program...");
                    condition = false;
                    break;
                default:
                    System.out.println("Invalid choice! Try again.\n");
            }
        }
        sc.close();
    }
}
