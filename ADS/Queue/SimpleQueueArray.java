import java.util.*;

class SimpleQueue {
    int array[];
    int front;
    int rear;
    int capacity;

    // This sets up our queue when we first make it
    SimpleQueue(int size) {
        this.capacity = size;
        array = new int[capacity];
        
        // We start front at 0 and rear at -1. 
        // This way, the first time you add a number, rear bumps up to index 0!
        front = 0;
        rear = -1;
    }

    // 1. Add an element to the back of the line
    public void enqueue(int data) {
        // Check if the back pointer has reached the very end of the array
        if (rear == capacity - 1) {
            System.out.println("Queue Overflow! (The array is full at the back)\n");
            return;
        }

        rear++;             // Move the back pointer forward to open a new spot
        array[rear] = data; // Put the new number into that open spot
        System.out.println("Element " + data + " added to queue.\n");
    }

    // 2. Remove an element from the front of the line
    public int dequeue() {
        /* 
         * YOUR CORE LOGIC:
         * 
         * Why "front > rear"? 
         * Think of it this way: every time you delete a number, 'front' moves up. 
         * If you delete everything, 'front' will pass 'rear'. 
         * When front gets ahead of rear, it means there are absolutely no numbers left!
         * 
         * Fun fact: At the very start, front is 0 and rear is -1. 
         * Since 0 is bigger than -1, this check also perfectly protects a brand new empty queue!
         */
        if (front > rear) {
            System.out.println("Queue Underflow! (Queue is completely Empty)\n");
            return -1;
        }

        int removedElement = array[front];
        front++; // Move the front pointer forward to "forget" the old number
        
        /* 
         * THE RESET FIX:
         * The exact moment the queue becomes completely empty (front > rear),
         * we teleport the pointers back to 0 and -1. 
         * This cleans the slate so you can use the array spaces from the beginning again!
         */
        if (front > rear) {
            front = 0;
            rear = -1;
        }
        
        return removedElement;
    }

    // 3. Look at the item at the front of the line without deleting it
    public int peek() {
        // Use your empty check again to make sure there's a number to see
        if (front > rear) {
            System.out.println("Queue is Empty!\n");
            return -1;
        }
        return array[front];
    }

    // 4. Print out all active numbers from Front to Rear
    public void display() {
        if (front > rear) {
            System.out.println("Queue is Empty\n");
            return;
        }

        System.out.print("Queue Elements (Front to Rear): ");
        // Only loop from where the front is currently sitting up to where the rear is
        for (int i = front; i <= rear; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("\n");
    }
}

public class SimpleQueueArray {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Size Of Queue: ");
        SimpleQueue queue = new SimpleQueue(sc.nextInt());
        boolean condition = true;

        while (condition) {
            System.out.println("--- SIMPLE QUEUE MENU ---");
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
