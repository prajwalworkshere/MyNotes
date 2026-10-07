import java.util.*;

class CircularQueueLL {
    // This helper class creates our individual circular structural nodes
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    // We only need a rear pointer because rear.next will ALWAYS point to the front!
    Node rear;

    // 1. Add an element to the back of our circular line (Enqueue)
    public void enqueue(int value) {
        Node newnode = new Node(value);

        // Scenario A: The queue is completely empty
        if (rear == null) {
            rear = newnode;
            rear.next = rear; // The node links to itself to start the circular chain!
        } 
        // Scenario B: There are already nodes in the queue
        else {
            newnode.next = rear.next; // Connect the new node to the front node
            rear.next = newnode;      // Make the old rear point to our new node
            rear = newnode;           // Move our rear marker to this new node
        }
        System.out.println("Element " + value + " added to circular queue.\n");
    }

    // 2. Remove an element from the front of our line (Dequeue)
    public int dequeue() {
        // If rear is null, there is nothing in the circle to delete
        if (rear == null) {
            System.out.println("Queue Underflow! (Queue is Empty)\n");
            return -1;
        }

        int removedData;
        
        // Scenario A: There is only one single node left in the circle
        if (rear == rear.next) {
            removedData = rear.data;
            rear = null; // Break the circle completely and reset to empty
        } 
        // Scenario B: There are multiple nodes in the circle
        else {
            Node frontNode = rear.next;     // Find the front node (always rear.next)
            removedData = frontNode.data;   // Save its value to return it
            rear.next = frontNode.next;     // Bypass the front node by linking rear to the second node
        }

        return removedData;
    }

    // 3. Just look at the element sitting at the front of the line
    public int peek() {
        if (rear == null) {
            System.out.println("Queue is Empty!\n");
            return -1;
        }
        // Front is always sitting right next to the rear!
        return rear.next.data; 
    }

    // 4. Print out all the elements starting from Front all the way around to Rear
    public void display() {
        if (rear == null) {
            System.out.println("Queue is Empty\n");
            return;
        }

        System.out.print("Circular Queue Elements (Front to Rear): ");
        Node current = rear.next; // Start looking at the front node
        
        // Use a do-while loop so we can print the nodes and stop when we loop back to the front
        do {
            System.out.print(current.data + " ");
            current = current.next;
        } while (current != rear.next);
        
        System.out.println("\n");
    }
}

public class CircularQueueLLMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CircularQueueLL queue = new CircularQueueLL();
        boolean condition = true;

        while (condition) {
            System.out.println("--- CIRCULAR QUEUE (LL) MENU ---");
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
