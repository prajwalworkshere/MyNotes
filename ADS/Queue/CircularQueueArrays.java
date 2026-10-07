import java.util.*;

class CircularQueue {
    int array[];
    int front;
    int rear;
    int capacity;
    int size; // Keeps track of how many items are currently in our queue

    // Constructor to set up the circular queue
    CircularQueue(int size) {
        this.capacity = size;
        array = new int[capacity];
        front = 0;
        rear = -1;
        this.size = 0; // We start with absolutely zero items inside
    }

    // 1. Add an element to the back (Enqueue)
    public void enqueue(int data) {
        // If the number of items equals the capacity, our queue is full!
        if (size == capacity) {
            System.out.println("Queue Overflow! (The queue is completely full)\n");
            return;
        }

        // MAGIC WRAPAROUND: Move rear forward by 1, but use '%' to snap it back to 0 
        // if it goes past the end index of the array!
        rear = (rear + 1) % capacity;
        
        array[rear] = data; // Put the number in the newly calculated spot
        size++;             // We just added an item, so increase total size
        System.out.println("Element " + data + " added to queue.\n");
    }

    // 2. Remove an element from the front (Dequeue)
    public int dequeue() {
        // If size is 0, there is nothing in the queue to delete
        if (size == 0) {
            System.out.println("Queue Underflow! (Queue is completely Empty)\n");
            return -1;
        }

        int removedElement = array[front]; // Save the number at the front
        
        // MAGIC WRAPAROUND: Move front forward by 1, using '%' so it snaps back to 0
        // if it walks off the end of the array!
        front = (front + 1) % capacity;
        
        size--; // We just removed an item, so decrease total size

        // CLEAN-UP RESET: If the queue becomes totally empty, reset pointers back to start
        if (size == 0) {
            front = 0;
            rear = -1;
        }

        return removedElement;
    }

    // 3. Just look at the item sitting at the front
    public int peek() {
        if (size == 0) {
            System.out.println("Queue is Empty!\n");
            return -1;
        }
        return array[front];
    }

    // 4. Print all the active numbers from Front to Rear
    public void display() {
        if (size == 0) {
            System.out.println("Queue is Empty\n");
            return;
        }

        System.out.print("Queue Elements: ");
        int index = front; // Start looking from where the front is currently sitting
        
        // We run the loop exactly 'size' times since that's how many active elements exist
        for (int i = 0; i < size; i++) {
            System.out.print(array[index] + " ");
            
            // Move our reading index forward circularly!
            index = (index + 1) % capacity; 
        }
        System.out.println("\n");
    }
}

public class CircularQueueArrays {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Size Of Circular Queue: ");
        CircularQueue queue = new CircularQueue(sc.nextInt());
        boolean condition = true;

        while (condition) {
            System.out.println("--- CIRCULAR QUEUE MENU ---");
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
