# **Session 4 – Object-Oriented Programming Concepts**

Java 17 LTS

2T + 2L + 2SL

Complete learning notes

## **Learning objectives**

By the end of this session, you should be able to:

1. Explain the object-oriented programming approach and why it is used.
2. Design classes that represent real-world entities.
3. Create objects and understand references to those objects.
4. Explain constructors, fields, methods, `this`, `super` and `static`.
5. Apply encapsulation to protect an object's state.
6. Use abstract classes and interfaces to design abstractions.
7. Implement inheritance and understand its restrictions.
8. Apply method overloading and overriding.
9. Explain compile-time and runtime polymorphism.
10. Combine all four OOP principles in a small Java application.

# **1. Introduction to Object-Oriented Programming (OOP)**

## **1.1 What is programming?**

Programming is the process of writing instructions that tell a computer how to perform a task.

For example, imagine developing a banking application. The application must:

- Register customers.
- Create bank accounts.
- Deposit money.
- Withdraw money.
- Transfer money.
- Display account balances.
- Maintain transaction records.

A simple program could be written using variables and methods. However, as the application grows, keeping all the data and operations organised becomes increasingly important.

This is where different programming approaches become useful.

## **1.2 What is Object-Oriented Programming?**

Object-Oriented Programming is a programming paradigm in which a software application is organised around objects.

An object represents an entity with:

- **State:** the information or data it maintains.
- **Behaviour:** the operations it can perform.
- **Identity:** what distinguishes it from other objects.

Let's understand this using a real-world example.

### **Example: A bank account**

A bank account has state:

- Account number.
- Account holder name.
- Account balance.
- Account type.

It also has behaviour:

- Deposit money.
- Withdraw money.
- Check balance.
- Transfer money.

A particular account is also distinguishable from another account by its identity, such as its account number.

We can represent this in Java:

```java
class BankAccount {

    // State
    private String accountNumber;
    private String accountHolder;
    private double balance;

    // Behaviour
    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public void displayBalance() {
        System.out.println("Balance: " + balance);
    }
}
```

In this example:

|Java member|Purpose|
|---|---|
|`accountNumber`|Stores account identity information|
|`accountHolder`|Stores account holder information|
|`balance`|Stores the account's current balance|
|`deposit()`|Adds money to the balance|
|`withdraw()`|Subtracts money from the balance|
|`displayBalance()`|Displays the current balance|

The class groups related information and operations into one logical unit.

**Important:** This is an introductory example. A real banking application must also validate amounts, check sufficient funds, handle transaction failures and protect against concurrent updates.

## **1.3 Why was OOP introduced?**

Consider a program that manages 1,000 bank accounts.
	
Without a structured design, you might end up with large numbers of unrelated variables:

```java
String account1Name = "Rahul";
double account1Balance = 5000;

String account2Name = "Priya";
double account2Balance = 10000;

String account3Name = "Amit";
double account3Balance = 15000;
```

You would also need separate logic to manage each account.

This creates several problems:

1. Code duplication.
2. Difficulty maintaining related data.
3. Greater possibility of accidental data modification.
4. Difficulty extending the application.
5. Poor organisation as the number of entities increases.

OOP provides a way to model related entities through classes and objects.

Instead of creating separate variables for every account, we define one `BankAccount` class and create as many objects as required.

```
BankAccount account1 = new BankAccount();
BankAccount account2 = new BankAccount();
BankAccount account3 = new BankAccount();
```

Each object has its own state.

## **1.4 Procedural programming vs object-oriented programming**

These are two different ways of organising software.

### **Procedural programming**

Procedural programming organises a program mainly around procedures or functions.

Example:

```java
public class BankingProgram {

    static double balance = 5000;

    static void deposit(double amount) {
        balance += amount;
    }

    static void withdraw(double amount) {
        balance -= amount;
    }

    public static void main(String[] args) {

        deposit(1000);
        withdraw(500);

        System.out.println(balance);
    }
}
```

Here, the data and operations are organised using static members.

This can be perfectly reasonable for a small example, but it becomes harder to manage when many accounts must maintain independent state.

### **Object-oriented programming**

```java
class BankAccount {

    private double balance;

    BankAccount(double balance) {
        this.balance = balance;
    }

    void deposit(double amount) {
        balance += amount;
    }

    void withdraw(double amount) {
        balance -= amount;
    }

    double getBalance() {
        return balance;
    }
}

public class Main {

    public static void main(String[] args) {

        BankAccount account1 = new BankAccount(5000);
        BankAccount account2 = new BankAccount(10000);

        account1.deposit(1000);
        account2.withdraw(2000);

        System.out.println(account1.getBalance());
        System.out.println(account2.getBalance());
    }
}
```

Output:

```
6000.0
8000.0
```

The two accounts maintain independent balances.

### **Detailed comparison**

| Feature           | Procedural programming                | Object-oriented programming                              |
| ----------------- | ------------------------------------- | -------------------------------------------------------- |
| Main organisation | Functions and procedures              | Classes and objects                                      |
| Data organisation | Data may be separate from functions   | State and behaviour are grouped in objects               |
| Data protection   | Depends on the program's design       | Encapsulation provides controlled access                 |
| Reusability       | Functions and modules                 | Classes, interfaces, inheritance and composition         |
| Extensibility     | Often requires modifying procedures   | Can extend designs through new implementations and types |
| Modelling         | Focuses on operations                 | Can represent entities and their relationships           |
| Best suited to    | Many straightforward procedural tasks | Systems with interacting entities and behaviour          |

OOP is not automatically better for every problem. It is useful when the system benefits from modelling entities, responsibilities, relationships and interchangeable implementations.

## 1.5 Four fundamental principles of OOP

The four commonly recognised OOP principles are:

**1. Encapsulation**

Group state and behaviour, and control access to an object's internal state.

Example: A bank account does not allow arbitrary direct changes to its balance.

**2. Abstraction**

Expose essential operations while hiding unnecessary implementation details.

Example: A customer calls `withdraw()` without needing to know every internal banking operation.

**3. Inheritance**

Create a specialised class from an existing class.

Example: `Car` and `Bike` can inherit common vehicle behaviour from `Vehicle`.

**4. Polymorphism**

Allow a common type or operation to represent different implementations.

Example: Different payment methods implement the same `pay()` operation differently.

We will examine each principle in detail later. First, we need to understand classes and objects because they form the foundation of OOP.

# 2. Classes and Objects**

## **2.1 What is a class?**

A class is a Java language construct that defines a reference type.

It describes the members that instances of that type can have, including:

- Fields.
- Methods.
- Constructors.
- Nested classes and interfaces.
- Initialisation blocks.

A class is often explained as a blueprint, but remember that this is an analogy. Technically, a class is a type declaration that defines the structure and behaviour of its instances.

### **Real-world analogy**

Consider a house-building plan.

The plan specifies:

- Number of rooms.
- Number of doors.
- Number of windows.
- Layout.
- Dimensions.

The plan itself is not a physical house.

Multiple houses can be constructed using the same plan, and each house can have different colours, furniture and occupants.

Similarly, one Java class can be used to create many objects.

### **Java example**

```java
class Car {

    // Fields
    String brand;
    String model;
    int speed;

    // Method
    void start() {
        System.out.println("Car started");
    }

    void accelerate(int increase) {
        speed = speed + increase;
    }

    void displayDetails() {
        System.out.println("Brand: " + brand);
        System.out.println("Model: " + model);
        System.out.println("Speed: " + speed);
    }
}
```

Let's understand each part.

**Class declaration**

```
class Car {
```

- `class` is the Java keyword used to declare a class.
- `Car` is the class name.
- The class body is enclosed within `{ }`.

**Fields**

```
String brand;
String model;
int speed;
```

These fields describe the data associated with each `Car` object.

**Methods**

```
void start() {
    System.out.println("Car started");
}
```

A method defines an operation that can be performed.

**Parameterized method**

```
void accelerate(int increase) {
    speed = speed + increase;
}
```

The method accepts an argument and changes the object's speed.

## **2.2 What is an object?**

An object is an instance of a class.

The class defines the type; the object is an actual instance created at runtime.

Example:

```
Car car1 = new Car();
```

This statement is particularly important. Let's break it down.

|Expression|Meaning|
|---|---|
|`Car`|Declared reference-variable type|
|`car1`|Reference variable|
|`new`|Requests creation of an object|
|`Car()`|Invokes the constructor|
|`=`|Assigns the resulting reference to the variable|

The statement creates a new `Car` object and assigns its reference to `car1`.

### **Creating and using an object**

```java
public class Main {

    public static void main(String[] args) {

        Car car1 = new Car();

        car1.brand = "Toyota";
        car1.model = "Camry";
        car1.speed = 100;

        car1.start();

        car1.accelerate(20);

        car1.displayDetails();
    }
}
```

Output:

```
Car started
Brand: Toyota
Model: Camry
Speed: 120
```

The sequence is:

1. A `Car` object is created.
2. Its reference is stored in `car1`.
3. The fields are assigned values.
4. The `start()` method is called.
5. The speed is increased.
6. The updated details are displayed.

## **2.3 Can one class create multiple objects?**

Yes. This is one of the most important advantages of classes.

```java
public class Main {

    public static void main(String[] args) {

        Car car1 = new Car();
        Car car2 = new Car();
        Car car3 = new Car();

        car1.brand = "Toyota";
        car1.model = "Camry";
        car1.speed = 100;

        car2.brand = "Honda";
        car2.model = "City";
        car2.speed = 80;

        car3.brand = "Hyundai";
        car3.model = "Creta";
        car3.speed = 90;

        car1.displayDetails();
        car2.displayDetails();
        car3.displayDetails();
    }
}
```

Each `new Car()` expression creates a separate instance.

Although all three objects are created from the same class, their instance fields can contain different values.

This is called independent object state.

## 2.4 Reference variables and objects – an essential concept

This topic is frequently tested in Java interviews.

Consider:

```java
Car car1 = new Car();
```

It is tempting to say that `car1` is the object. Technically, this is incorrect.

`car1` is a reference variable that refers to the object.

A reference is a value used to identify an object. It is not the object itself.

### Example: Two references to one object

```jAVA
class Student {

    int marks = 50;
}

public class Main {

    public static void main(String[] args) {

        Student s1 = new Student();

        Student s2 = s1;

        s2.marks = 90;

        System.out.println(s1.marks);
        System.out.println(s2.marks);
    }
}
```

Output:

```
90
90
```

Why?

First:

```
Student s1 = new Student();
```

One object is created.

Next:

```
Student s2 = s1;
```

The reference value stored in `s1` is copied into `s2`.

No new object is created.

Both references now refer to the same object.

Therefore, changing the object's field through `s2` is visible through `s1`.

**Reference aliasing**

`s1`

`s2`

Reference A

Reference B

**Same Student object**

`marks = 90`

Both references access the same object's state.
![[Session 4 – Object-Oriented Programming Concepts-1794750408564.webp]]

### Example: Two independent objects

```java
Student s1 = new Student();
Student s2 = new Student();

s1.marks = 70;
s2.marks = 90;
```

Now two objects exist, and their state is independent.

The important difference is:

```java
Student s2 = s1;          // Copies a reference
```

versus:

```java
Student s2 = new Student(); // Creates another object
```

### **What happens when a reference is reassigned?**

```
Student s1 = new Student();
Student s2 = s1;

s1 = new Student();

s1.marks = 100;

System.out.println(s2.marks);
```

Output:

```
50
```

Explanation:

1. Initially, `s1` refers to object A.
2. `s2` receives the same reference, so it also refers to A.
3. `s1 = new Student()` creates object B and changes only the reference held by `s1`.
4. `s2` still refers to object A.
5. The original object's marks remain `50`.

This distinction between modifying an object and reassigning a reference is extremely important.

## **2.5 A conceptual view of memory**

Java programmers often discuss objects as being allocated on the heap and local variables as existing in stack frames.

For learning purposes, consider:

```
public class Main {

    public static void main(String[] args) {

        Student s1 = new Student();

        s1.marks = 80;
    }
}
```

A simplified conceptual model:

**Thread stack – main() frame**

`s1` – reference value

**Heap – Student object**

`marks = 80`

This is a conceptual teaching model, not a guarantee of the JVM's physical memory layout. JVM implementations may optimise object allocation and storage.

Important distinctions:

- The local variable `s1` is part of the method's execution context.
- The `Student` instance is an object.
- The reference connects the variable to that object.
- When an object is no longer reachable, it may become eligible for garbage collection.

**Java does not allow direct pointer arithmetic or manual object deallocation in ordinary Java code.**

## **2.6 Default values of object fields**

When an object is created, its instance fields are initially assigned default values.

|Field type|Default value|Example|
|---|---|---|
|`byte`|`0`|`byte age;`|
|`short`|`0`|`short number;`|
|`int`|`0`|`int marks;`|
|`long`|`0L`|`long count;`|
|`float`|`0.0f`|`float price;`|
|`double`|`0.0d`|`double salary;`|
|`char`|`'\u0000'`|`char grade;`|
|`boolean`|`false`|`boolean active;`|
|Reference type|`null`|`String name;`|

Example:

```
class Employee {

    int id;
    String name;
    double salary;
    boolean permanent;
    char grade;

    void display() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Salary: " + salary);
        System.out.println("Permanent: " + permanent);
        System.out.println("Grade: " + grade);
    }
}

public class Main {

    public static void main(String[] args) {

        Employee e = new Employee();

        e.display();
    }
}
```

Output:

```
ID: 0
Name: null
Salary: 0.0
Permanent: false
Grade:
```

The last line displays the default null character, which is not visibly printed.

### **Important comparison: Fields vs local variables**

```
class Example {

    int value; // Field receives default value 0

    void display() {

        int number;

        // System.out.println(number);
        // Compilation error: not definitely assigned
    }
}
```

Java assigns default values to fields and array components. Local variables must be definitely assigned before they are read.

This is a compile-time language rule, not merely a convention.

## **2.7 Constructors – detailed explanation**

A constructor is a special class member used to initialise a newly created object.

It is invoked as part of object creation.

Consider:

```
class Employee {

    int id;
    String name;

    Employee() {
        id = 101;
        name = "Rahul";
    }
}
```

When we write:

```
Employee e = new Employee();
```

Java invokes the no-argument constructor as part of creating the object.

The constructor initialises the fields.

### **Constructor characteristics**

1. Its name must match the class name.
2. It has no return type.
3. It is invoked during object creation.
4. It can be overloaded.
5. It is not inherited.
6. It can have access modifiers.
7. It can invoke another constructor using `this(...)`.
8. It can invoke a superclass constructor using `super(...)`.

### **A. No-argument constructor**

```
class Student {

    int rollNumber;
    String name;

    Student() {
        rollNumber = 1;
        name = "Unknown";
    }
}
```

Usage:

```
Student s = new Student();
```

The constructor takes no arguments.

### **B. Parameterized constructor**

```
class Student {

    int rollNumber;
    String name;

    Student(int rollNumber, String name) {

        this.rollNumber = rollNumber;
        this.name = name;
    }
}
```

Usage:

```
Student s1 = new Student(101, "Rahul");
Student s2 = new Student(102, "Priya");
```

The constructor receives initial values and stores them in the new object's fields.

### **C. Constructor overloading**

A class can declare multiple constructors with different parameter lists.

```
class Product {

    int productId;
    String productName;
    double price;

    Product() {
        productId = 0;
        productName = "Unknown";
        price = 0;
    }

    Product(int productId, String productName) {
        this.productId = productId;
        this.productName = productName;
        this.price = 0;
    }

    Product(int productId, String productName, double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    void display() {
        System.out.println(
            productId + " " + productName + " " + price
        );
    }
}
```

Usage:

```
public class Main {

    public static void main(String[] args) {

        Product p1 = new Product();
        Product p2 = new Product(101, "Laptop");
        Product p3 = new Product(102, "Phone", 25000);

        p1.display();
        p2.display();
        p3.display();
    }
}
```

Output:

```
0 Unknown 0.0
101 Laptop 0.0
102 Phone 25000.0
```

The compiler selects the constructor based on the arguments supplied.

### **D. Default constructor vs no-argument constructor**

These terms are often used loosely, but they are not exactly identical in Java.

A **default constructor** is the constructor implicitly declared by the compiler when a class declares no constructors.

A **no-argument constructor** is any constructor that accepts no arguments, whether written by the programmer or generated by the compiler.

Example:

```
class Student {

    int rollNumber;
}
```

The compiler supplies a default constructor.

But:

```
class Student {

    Student(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    int rollNumber;
}
```

The compiler does not supply a default no-argument constructor.

This statement fails:

```
Student s = new Student();
```

because there is no applicable no-argument constructor.

### **E. Constructor chaining using** `this()`

Constructor chaining means one constructor invokes another constructor in the same class.

```
class Employee {

    int id;
    String name;
    double salary;

    Employee() {
        this(0, "Unknown", 0);
    }

    Employee(int id, String name) {
        this(id, name, 0);
    }

    Employee(int id, String name, double salary) {

        this.id = id;
        this.name = name;
        this.salary = salary;
    }
}
```

Here:

- The no-argument constructor delegates to the three-argument constructor.
- The two-argument constructor also delegates to the three-argument constructor.
- The actual initialisation logic is maintained in one place.

This reduces repeated code.

In Java 17, an explicit `this(...)` constructor invocation must be the first statement in the constructor body.

### **F. Constructor vs method**

|Constructor|Method|
|---|---|
|Initialises a newly created object|Performs an operation|
|Has the class name|Can have any valid method name|
|Has no return type|Has a return type or `void`|
|Invoked during object creation|Invoked through a method call|
|Cannot be inherited|Eligible instance methods can be inherited|
|Can be overloaded|Can be overloaded|
|Cannot be overridden|Eligible instance methods can be overridden|

## **2.8 The** `this` **keyword – detailed explanation**

`this` is a reference to the current object.

It is commonly used to distinguish instance fields from parameters, call another constructor, and pass or return the current object.

### **Example 1: Resolving naming conflicts**

```
class Student {

    int rollNumber;
    String name;

    Student(int rollNumber, String name) {

        this.rollNumber = rollNumber;
        this.name = name;
    }
}
```

Here:

- `this.rollNumber` refers to the instance field.
- `rollNumber` refers to the constructor parameter.
- `this.name` refers to the instance field.
- `name` refers to the constructor parameter.

### **Example 2: Calling an instance method**

```
class Calculator {

    void calculate() {
        System.out.println("Calculating");
    }

    void execute() {
        this.calculate();
    }
}
```

`this.calculate()` invokes the method on the current object.

In many cases, the `this` qualifier can be omitted:

```
void execute() {
    calculate();
}
```

### **Example 3: Returning the current object**

```
class Employee {

    int id;

    Employee setId(int id) {
        this.id = id;
        return this;
    }
}
```

Usage:

```
Employee e = new Employee().setId(101);
```

This style is sometimes used in fluent APIs.

### **Can we use** `this` **inside a static method?**

No. A static method is associated with the class rather than a particular object. It has no current instance represented by `this`.

```
class Test {

    static void display() {
        // System.out.println(this);
        // Compilation error
    }
}
```

## **2.9 Static members vs instance members**

This distinction is essential for Java programming and interviews.

### **Instance variables**

An instance variable belongs to an object.

```
class Employee {

    int id;
    String name;
}
```

Each `Employee` object has its own `id` and `name`.

### **Static variables**

A static variable belongs to the class.

```
class Employee {

    int id;
    String name;

    static String company = "ABC Technologies";
}
```

All instances access the same class variable.

Example:

```
public class Main {

    public static void main(String[] args) {

        Employee e1 = new Employee();
        Employee e2 = new Employee();

        e1.id = 101;
        e1.name = "Rahul";

        e2.id = 102;
        e2.name = "Priya";

        Employee.company = "XYZ Technologies";

        System.out.println(e1.name + " " + e1.company);
        System.out.println(e2.name + " " + e2.company);
    }
}
```

Output:

```
Rahul XYZ Technologies
Priya XYZ Technologies
```

The employees have separate instance data but share the same company field.

### **Static methods**

A static method belongs to the class.

```
class Calculator {

    static int add(int a, int b) {
        return a + b;
    }
}
```

It can be called using the class name:

```
int result = Calculator.add(10, 20);
```

### **Can a static method directly access an instance variable?**

No.

```
class Employee {

    int id;

    static void display() {

        // System.out.println(id);
        // Compilation error
    }
}
```

Why?

A static method does not automatically have a particular object on which to access the instance field.

It can access an instance field through an explicit object reference:

```
class Employee {

    int id;

    static void display(Employee employee) {
        System.out.println(employee.id);
    }
}
```

### **Comparison**

|Instance member|Static member|
|---|---|
|Associated with an object|Associated with a class|
|Each object has its own instance-field state|Static field is shared at class level|
|Instance methods can directly access instance and static members|Static methods cannot directly access instance members|
|Usually accessed through an object|Prefer access through the class name|
|Can use `this`|Cannot use `this`|

**Professional note:** Avoid using static fields as a substitute for proper object modelling. Shared mutable static state can make an application difficult to test and maintain.

# **3. Encapsulation**

Encapsulation is the first OOP principle we will study in detail.

## **3.1 Meaning of encapsulation**

Encapsulation is the practice of grouping data and the operations that work on that data within a class, while controlling how external code can access or modify the object's internal state.

It is commonly implemented using:

- `private` fields.
- Public or appropriately accessible methods.
- Input validation.
- Controlled state-changing operations.

Encapsulation is broader than simply declaring fields `private`.

The real objective is to make sure that an object can protect its own rules and maintain a valid state.

## **3.2 Real-world example: Bank account**

Suppose a bank account has a balance of ₹10,000.

The customer should be able to deposit or withdraw money, but should not be able to arbitrarily assign any value to the balance.

For example, this should not be allowed:

```
account.balance = -500000;
```

A negative balance might violate the account's business rules.

We need a design in which the account controls how its balance changes.

## **3.3 Without encapsulation**

```
class BankAccount {

    public double balance;
}

public class Main {

    public static void main(String[] args) {

        BankAccount account = new BankAccount();

        account.balance = 10000;

        account.balance = -500000;

        System.out.println(account.balance);
    }
}
```

Output:

```
-500000.0
```

The field is public, so any accessible code can modify it directly.

The `BankAccount` class has no opportunity to validate the modification.

This is a weak design when the balance must obey business rules.

## **3.4 Implementing encapsulation using** `private`

Let's redesign the account.

```
class BankAccount {

    private String accountHolder;
    private double balance;

    public BankAccount(String accountHolder, double initialBalance) {

        if (initialBalance < 0) {
            throw new IllegalArgumentException(
                "Initial balance cannot be negative"
            );
        }

        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Deposit amount must be positive"
            );
        }

        balance += amount;
    }

    public void withdraw(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Withdrawal amount must be positive"
            );
        }

        if (amount > balance) {
            throw new IllegalArgumentException(
                "Insufficient balance"
            );
        }

        balance -= amount;
    }
}
```

Usage:

```
public class Main {

    public static void main(String[] args) {

        BankAccount account =
            new BankAccount("Rahul", 10000);

        account.deposit(5000);
        account.withdraw(2000);

        System.out.println(account.getAccountHolder());
        System.out.println(account.getBalance());
    }
}
```

Output:

```
Rahul
13000.0
```

Let's understand the important parts.

**Private fields:**

```
private String accountHolder;
private double balance;
```

These fields cannot be accessed directly from unrelated external classes.

**Controlled deposit operation:**

```
public void deposit(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException(
            "Deposit amount must be positive"
        );
    }

    balance += amount;
}
```

The account checks the amount before modifying the balance.

**Controlled withdrawal operation:**

```
public void withdraw(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException(
            "Withdrawal amount must be positive"
        );
    }

    if (amount > balance) {
        throw new IllegalArgumentException(
            "Insufficient balance"
        );
    }

    balance -= amount;
}
```

The account prevents invalid withdrawals.

The important design idea is that the account owns its state and enforces its own rules.

## **3.5 Getters and setters**

A getter is a method that returns a field's value.

A setter is a method that changes a field's value.

Example:

```
class Student {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "Name cannot be empty"
            );
        }

        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {

        if (age < 0 || age > 120) {
            throw new IllegalArgumentException(
                "Invalid age"
            );
        }

        this.age = age;
    }
}
```

Usage:

```
public class Main {

    public static void main(String[] args) {

        Student student = new Student();

        student.setName("Rahul");
        student.setAge(25);

        System.out.println(student.getName());
        System.out.println(student.getAge());
    }
}
```

Output:

```
Rahul
25
```

### **Why are getters and setters useful?**

They allow the class to control access to fields.

For example, if the age must be between 0 and 120, the setter can validate the input before accepting it.

Without validation, an external class might assign an invalid value.

### **Does every private field need a setter?**

No.

Consider an employee's unique employee ID.

If the ID should never change after the object is created, it may be better to initialise it through the constructor and provide only a getter.

```
class Employee {

    private final int employeeId;
    private String name;

    Employee(int employeeId, String name) {
        this.employeeId = employeeId;
        this.name = name;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }
}
```

There is deliberately no `setEmployeeId()` method.

This prevents callers from changing the employee ID through the class's public API.

**Professional principle:** Expose only the operations that callers actually need. Do not create setters automatically for every field.

## **3.6 Access modifiers in Java**

Access modifiers control where classes, fields, methods and constructors can be accessed.

Java has four commonly discussed access levels:

1. `private`
2. Package-private (no modifier)
3. `protected`
4. `public`

### **A. Private**

A private member can be accessed directly only within the enclosing top-level class's permitted scope.

```
class Employee {

    private int salary = 50000;

    void displaySalary() {
        System.out.println(salary);
    }
}
```

The class's own method can access the private field.

External code cannot access it directly:

```
Employee e = new Employee();

// System.out.println(e.salary);
// Compilation error
```

### **B. Package-private**

When no access modifier is specified, the member has package-private access.

```
class Employee {

    int employeeId;
}
```

Code in the same package can access the field, subject to the normal rules.

Code in a different package cannot access it merely because it has a reference to the object.

### **C. Protected**

A protected member is accessible:

- Within its declaring package.
- In subclasses, subject to Java's protected-access rules.

```
class Employee {

    protected double salary;
}
```

A subclass can access an inherited protected field according to those rules.

### **D. Public**

A public member is accessible wherever the declaring type is accessible.

```
class Employee {

    public void display() {
        System.out.println("Employee details");
    }
}
```

### **Access modifier comparison**

|Access modifier|Same class|Same package|Subclass in another package|Unrelated class in another package|
|---|---|---|---|---|
|`private`|Yes|No|No direct access|No|
|package-private|Yes|Yes|No, merely by inheritance|No|
|`protected`|Yes|Yes|Yes, subject to protected-access rules|No|
|`public`|Yes|Yes|Yes|Yes, subject to type/module accessibility|

### **Important note about** `protected`

Protected access across packages has additional rules. It is not correct to say that every class in another package can access a protected member simply because it has an object reference.

For example, access through a subclass in another package is restricted by the qualifying reference type and the subclass relationship.

This is a common area of Java interview questions.

## **3.7 Advantages of encapsulation**

1. **Data protection:** Prevents uncontrolled direct access.
2. **Validation:** Ensures that values satisfy business rules.
3. **Maintainability:** Internal implementation can change without changing the public API.
4. **Flexibility:** The class can change how it stores data.
5. **Controlled operations:** Callers use meaningful methods rather than directly changing fields.
6. **Reduced coupling:** External code depends on the class's public operations rather than its internal representation.

## **3.8 Encapsulation vs data hiding**

These terms are related, but they are not identical.

|Encapsulation|Data hiding|
|---|---|
|Groups state and related behaviour within a class|Restricts direct access to internal details|
|A broader design principle|One aspect of access control|
|Commonly achieved through classes and controlled methods|Commonly supported by access modifiers such as `private`|
|Focuses on controlling how an object is used|Focuses on limiting what external code can see or access|

**Interview answer:** Encapsulation is the broader concept of combining data and related behaviour while controlling access. Data hiding is one mechanism used to protect the internal state.

# **4. Abstraction**

## **4.1 What is abstraction?**

Abstraction means exposing essential features of an object while hiding unnecessary implementation details.

It allows users of a class or component to understand what operation is available without needing to understand every internal step.

### **Real-world example: ATM**

When using an ATM, you may perform:

- Cash withdrawal.
- Balance enquiry.
- Cash deposit.
- PIN verification.

You do not need to understand the ATM's internal software, bank communication protocol, database transactions or cash-dispensing mechanism.

You interact with a simplified set of operations.

That simplified view is abstraction.

## **4.2 Why is abstraction required?**

Imagine a payment application that supports several payment methods:

- Credit card.
- UPI.
- Net banking.

Each payment method has different internal processing.

However, the application needs a common way to request a payment.

We can define a common contract:

```
interface PaymentMethod {

    void pay(double amount);
}
```

The application can call `pay()` without needing to know the exact internal steps of each payment method.

This reduces dependency on implementation details.

## **4.3 How does Java support abstraction?**

Java mainly provides two constructs for this:

1. Abstract classes.
2. Interfaces.

They both help define abstractions, but they serve different design purposes.

## **4.4 Abstract classes**

An abstract class is declared with the `abstract` keyword.

It cannot be instantiated directly.

It may contain:

- Instance fields.
- Constructors.
- Concrete methods.
- Abstract methods.
- Static methods.
- Other permitted class members.

### **Example: Vehicle**

Suppose a vehicle application supports cars, bikes and buses.

All vehicles have some common behaviour, such as starting, but the details of movement may differ.

```
abstract class Vehicle {

    protected String brand;

    Vehicle(String brand) {
        this.brand = brand;
    }

    void start() {
        System.out.println(brand + " is starting");
    }

    abstract void move();
}
```

Notice two types of methods.

**Concrete method:**

```
void start() {
    System.out.println(brand + " is starting");
}
```

It has a body, so the superclass provides an implementation.

**Abstract method:**

```
abstract void move();
```

It has no method body. It declares an operation that concrete subclasses must implement.

### **Creating subclasses**

```
class Car extends Vehicle {

    Car(String brand) {
        super(brand);
    }

    @Override
    void move() {
        System.out.println(brand + " car moves on roads");
    }
}
```

```
class Bike extends Vehicle {

    Bike(String brand) {
        super(brand);
    }

    @Override
    void move() {
        System.out.println(brand + " bike moves on roads");
    }
}
```

### **Main program**

```
public class Main {

    public static void main(String[] args) {

        Vehicle car = new Car("Toyota");
        Vehicle bike = new Bike("Honda");

        car.start();
        car.move();

        bike.start();
        bike.move();
    }
}
```

Output:

```
Toyota is starting
Toyota car moves on roads
Honda is starting
Honda bike moves on roads
```

### **What is happening?**

1. `Vehicle` defines the common abstraction.
2. `Vehicle` contains shared state in `brand`.
3. `Vehicle` provides common behaviour through `start()`.
4. `Vehicle` requires subclasses to implement `move()`.
5. `Car` and `Bike` provide their own implementations.

This is useful when closely related classes share state or common implementation but need different behaviour in some operations.

## **4.5 Rules of abstract classes**

- An abstract class cannot be instantiated directly.
- An abstract class can have constructors.
- An abstract class can have instance variables.
- It can contain abstract and concrete methods.
- It can contain static methods.
- It can extend another class.
- It can implement interfaces.
- A subclass must implement inherited abstract methods unless the subclass is also abstract.
- An abstract class does not have to contain an abstract method.

### **Example: Abstract class without abstract methods**

```
abstract class Employee {

    void display() {
        System.out.println("Employee details");
    }
}
```

This is valid Java.

The class is abstract even though it declares no abstract method.

## **4.6 Interfaces**

An interface defines a contract that a class can implement.

It describes a set of operations that implementing classes agree to support.

Example:

```
interface PaymentMethod {

    void pay(double amount);
}
```

A class implements the interface using `implements`.

```
class CreditCardPayment implements PaymentMethod {

    @Override
    public void pay(double amount) {
        System.out.println(
            "Credit card payment: " + amount
        );
    }
}
```

Another class:

```
class UpiPayment implements PaymentMethod {

    @Override
    public void pay(double amount) {
        System.out.println(
            "UPI payment: " + amount
        );
    }
}
```

The two classes implement the same contract differently.

### **Using the interface**

```
public class Main {

    public static void main(String[] args) {

        PaymentMethod p1 = new CreditCardPayment();
        PaymentMethod p2 = new UpiPayment();

        p1.pay(5000);
        p2.pay(2000);
    }
}
```

Output:

```
Credit card payment: 5000.0
UPI payment: 2000.0
```

The caller uses the common interface type, even though the objects are different classes.

## **4.7 Interface features in Java 17**

An important interview mistake is assuming that interfaces can contain only abstract methods.

Java 17 interfaces support several member types.

### **A. Abstract methods**

```
interface Printable {

    void print();
}
```

An ordinary abstract interface method is implicitly `public abstract` when declared without a body and without another applicable modifier.

### **B. Default methods**

A default method provides an implementation that implementing classes can inherit.

```
interface Printable {

    void print();

    default void showStatus() {
        System.out.println("Ready to print");
    }
}
```

### **C. Static methods**

Static methods belong to the interface itself.

```
interface Printable {

    static void information() {
        System.out.println("Printable interface");
    }
}
```

Call it using:

```
Printable.information();
```

Do not assume an interface's static method is inherited as an ordinary instance method by implementing classes.

### **D. Private methods**

Private interface methods can be used to share internal implementation between interface methods.

```
interface Printable {

    default void printDocument() {
        log();
        System.out.println("Printing document");
    }

    private void log() {
        System.out.println("Print operation started");
    }
}
```

An implementing class cannot directly call the private interface method.

### **E. Interface fields**

Fields declared in an interface are implicitly:

```
public static final
```

Example:

```
interface ApplicationConfig {

    int MAX_USERS = 100;
}
```

This is effectively:

```
interface ApplicationConfig {

    public static final int MAX_USERS = 100;
}
```

It is a constant, not an instance field belonging separately to each implementing object.

## **4.8 Multiple interface implementation**

A Java class can implement multiple interfaces.

```
interface Printable {

    void print();
}

interface Scannable {

    void scan();
}

class MultiFunctionPrinter
        implements Printable, Scannable {

    @Override
    public void print() {
        System.out.println("Printing document");
    }

    @Override
    public void scan() {
        System.out.println("Scanning document");
    }
}
```

Usage:

```
public class Main {

    public static void main(String[] args) {

        MultiFunctionPrinter printer =
            new MultiFunctionPrinter();

        printer.print();
        printer.scan();
    }
}
```

Output:

```
Printing document
Scanning document
```

This allows one class to satisfy multiple contracts.

## **4.9 Default method conflicts**

An important Java 17 interface rule concerns conflicting default methods.

Suppose two interfaces provide the same default method:

```
interface A {

    default void display() {
        System.out.println("A");
    }
}

interface B {

    default void display() {
        System.out.println("B");
    }
}
```

Now:

```
class C implements A, B {

}
```

This class does not compile because Java cannot automatically choose which conflicting default implementation should be used.

The class must resolve the conflict:

```
class C implements A, B {

    @Override
    public void display() {
        A.super.display();
        B.super.display();
    }
}
```

Or it can provide its own implementation:

```
class C implements A, B {

    @Override
    public void display() {
        System.out.println("C implementation");
    }
}
```

This is an important feature to understand when implementing multiple interfaces.

## **4.10 Abstract class vs interface – detailed comparison**

|Feature|Abstract class|Interface|
|---|---|---|
|Declaration|`abstract class`|`interface`|
|Direct instantiation|Not allowed|Not allowed|
|Constructor|Can have constructors|Cannot declare constructors|
|Instance state|Can have instance fields|Cannot have ordinary instance fields|
|Constants|Can declare constants|Fields are implicitly `public static final`|
|Abstract methods|Supported|Supported|
|Implemented methods|Can have concrete methods|Supports default, static and private methods|
|Access control|Supports normal class access modifiers|Interface members follow interface-specific rules|
|Class inheritance|A class can extend one class|A class can implement multiple interfaces|
|Main design use|Common state and implementation for related classes|Shared contracts or capabilities across classes|

### **When should we use an abstract class?**

Consider:

```
abstract class Employee {

    protected String name;
    protected double salary;

    Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    void displayDetails() {
        System.out.println(name + " " + salary);
    }

    abstract double calculateBonus();
}
```

Different employee types share common state and behaviour but calculate bonuses differently.

An abstract class is suitable because it provides shared implementation and requires specialised subclasses to implement the varying operation.

### **When should we use an interface?**

Consider:

```
interface Exportable {

    void exportData();
}
```

A report, invoice and transaction statement may all support exporting.

They do not necessarily need to share a common superclass. They can simply implement the `Exportable` contract.

### **Professional design guidance**

- Prefer an interface when you want to define a capability or contract.
- Consider an abstract class when related classes need shared state or common implementation.
- Do not create an abstract class merely because you want to prevent direct instantiation; consider whether the abstraction is meaningful.
- A class can extend one class and implement several interfaces.

# **5. Inheritance**

## **5.1 What is inheritance?**

Inheritance is an OOP mechanism in which one class derives from another class.

It allows a subclass to inherit accessible members from its superclass and specialise or extend the inherited behaviour.

Java uses the keyword `extends` for class inheritance.

### **Terminology**

|Term|Meaning|
|---|---|
|Superclass|Existing parent class|
|Subclass|New class that extends the superclass|
|Base class|Another name for superclass|
|Derived class|Another name for subclass|
|`extends`|Keyword used for class inheritance|
|`super`|Used to refer to superclass members and constructors|

## **5.2 Real-world example**

Consider an organisation that has several types of employees.

All employees have:

- Employee ID.
- Name.
- Salary.

However, different employees have different responsibilities.

A developer writes code.

A manager manages a team.

A tester tests software.

We can represent common employee details in a superclass.

```
class Employee {

    protected int id;
    protected String name;
    protected double salary;

    Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    void displayDetails() {
        System.out.println(
            id + " " + name + " " + salary
        );
    }
}
```

Now create a subclass:

```
class Developer extends Employee {

    private String programmingLanguage;

    Developer(
        int id,
        String name,
        double salary,
        String programmingLanguage
    ) {
        super(id, name, salary);
        this.programmingLanguage = programmingLanguage;
    }

    void writeCode() {
        System.out.println(
            name + " writes " + programmingLanguage
        );
    }
}
```

Main program:

```
public class Main {

    public static void main(String[] args) {

        Developer developer =
            new Developer(
                101,
                "Rahul",
                75000,
                "Java"
            );

        developer.displayDetails();
        developer.writeCode();
    }
}
```

Output:

```
101 Rahul 75000.0
Rahul writes Java
```

The `Developer` object can use the inherited `displayDetails()` method and its own `writeCode()` method.

## **5.3 Why is inheritance useful?**

1. **Code reuse:** Common behaviour can be declared once.
2. **Specialisation:** A subclass can add specialised fields and methods.
3. **Overriding:** A subclass can provide specialised implementations of inherited instance methods.
4. **Polymorphism:** Subclass instances can be used through superclass references.
5. **Organisation:** Related types can be represented through a class hierarchy.

However, inheritance should represent a genuine subtype relationship. It should not be used simply to avoid writing a few lines of code.

## **5.4 Types of inheritance**

The following structures are commonly taught in OOP.

### **A. Single inheritance**

One subclass extends one superclass.

```
class Animal {

    void eat() {
        System.out.println("Animal eats");
    }
}

class Dog extends Animal {

    void bark() {
        System.out.println("Dog barks");
    }
}
```

Relationship:

```
Animal
   |
   v
  Dog
```

### **B. Multilevel inheritance**

A class extends a subclass, forming a chain.

```
class Animal {

    void eat() {
        System.out.println("Eating");
    }
}

class Mammal extends Animal {

    void walk() {
        System.out.println("Walking");
    }
}

class Dog extends Mammal {

    void bark() {
        System.out.println("Barking");
    }
}
```

Relationship:

```
Animal
   |
   v
Mammal
   |
   v
  Dog
```

A `Dog` object can use the accessible inherited methods from both `Mammal` and `Animal`.

### **C. Hierarchical inheritance**

Multiple subclasses extend the same superclass.

```
class Animal {

    void eat() {
        System.out.println("Animal eats");
    }
}

class Dog extends Animal {

    void bark() {
        System.out.println("Dog barks");
    }
}

class Cat extends Animal {

    void meow() {
        System.out.println("Cat meows");
    }
}
```

Relationship:

```
         Animal
         /    \
        /      \
      Dog      Cat
```

Both subclasses inherit the common `eat()` method.

### **D. Multiple inheritance**

Multiple inheritance of classes means a class directly inherits from more than one class.

Java does not support this.

The following is invalid:

```
class A {

    void showA() {
        System.out.println("A");
    }
}

class B {

    void showB() {
        System.out.println("B");
    }
}

// Not valid Java:
// class C extends A, B { }
```

### **Why does Java not allow multiple class inheritance?**

One traditional explanation is the ambiguity that can arise when two parent classes provide the same inherited method implementation.

For example, if both parents contain `display()`, which implementation should the child inherit?

Java avoids this form of class-inheritance ambiguity by allowing only one direct superclass.

However, Java supports a class implementing multiple interfaces, with explicit conflict-resolution rules for default methods.

### **Multiple interface implementation**

```
interface Camera {

    void takePhoto();
}

interface MusicPlayer {

    void playMusic();
}

class SmartPhone implements Camera, MusicPlayer {

    @Override
    public void takePhoto() {
        System.out.println("Taking photo");
    }

    @Override
    public void playMusic() {
        System.out.println("Playing music");
    }
}
```

This is valid.

The class provides the required implementations.

## **5.5 The** `super` **keyword**

`super` is used within an instance context to refer to the superclass portion of the current object.

It is commonly used for:

1. Calling a superclass constructor.
2. Accessing an accessible superclass field.
3. Calling an accessible superclass method.

### **A. Calling a superclass constructor**

```
class Person {

    String name;

    Person(String name) {
        this.name = name;
    }
}

class Student extends Person {

    int rollNumber;

    Student(String name, int rollNumber) {

        super(name);

        this.rollNumber = rollNumber;
    }
}
```

The line:

```
super(name);
```

calls the constructor of `Person`.

This is important because the superclass portion of the object must be initialised as part of constructing the subclass object.

### **B. Accessing a superclass field**

```
class Parent {

    int value = 100;
}

class Child extends Parent {

    int value = 200;

    void display() {

        System.out.println(value);
        System.out.println(super.value);
    }
}
```

Output:

```
200
100
```

Explanation:

- `value` refers to the field declared in `Child`.
- `super.value` refers to the accessible field declared in `Parent`.

### **C. Calling a superclass method**

```
class Parent {

    void display() {
        System.out.println("Parent display");
    }
}

class Child extends Parent {

    @Override
    void display() {

        super.display();

        System.out.println("Child display");
    }
}
```

Output:

```
Parent display
Child display
```

The subclass calls the superclass implementation before executing its own additional logic.

## **5.6 Constructor execution order in inheritance**

This is a very important Java interview topic.

Consider:

```
class Parent {

    Parent() {
        System.out.println("Parent constructor");
    }
}

class Child extends Parent {

    Child() {
        System.out.println("Child constructor");
    }
}

public class Main {

    public static void main(String[] args) {

        Child obj = new Child();
    }
}
```

Output:

```
Parent constructor
Child constructor
```

Why does the parent constructor execute first?

A subclass object includes the superclass portion of its object state. The superclass initialisation must take place as part of constructing the subclass object.

The simplified sequence is:

1. Memory for the new object is allocated.
2. Fields receive default values.
3. The superclass construction and initialisation process runs.
4. The subclass instance fields and initialisation run.
5. The subclass constructor body completes.

The complete Java initialisation rules are more detailed than this simplified list, especially when instance initialisers and field initialisers are involved.

### **Example with parameterized constructors**

```
class Parent {

    Parent(int x) {
        System.out.println("Parent: " + x);
    }
}

class Child extends Parent {

    Child() {
        super(100);

        System.out.println("Child constructor");
    }
}
```

Output:

```
Parent: 100
Child constructor
```

The explicit `super(100)` selects the parameterized superclass constructor.

## **5.7 Important inheritance rules**

### **Rule 1: Constructors are not inherited**

A subclass does not automatically inherit the constructors of its superclass.

It must declare its own constructors as needed and invoke an accessible superclass constructor.

### **Rule 2: Private members cannot be accessed directly by subclasses**

```
class Parent {

    private int value = 100;
}

class Child extends Parent {

    void display() {

        // System.out.println(value);
        // Compilation error
    }
}
```

The private field is not directly accessible in `Child`.

The superclass can provide a method to access its value.

### **Rule 3: A class can extend only one class**

```
class A { }

class B extends A { }
```

A class may have only one direct superclass.

### **Rule 4: A final class cannot be extended**

```
final class SecurityManager {
}

// Compilation error:
// class CustomManager extends SecurityManager { }
```

### **Rule 5: A final method cannot be overridden**

```
class Parent {

    final void display() {
        System.out.println("Parent");
    }
}

class Child extends Parent {

    // Compilation error if uncommented:
    // @Override
    // void display() { }
}
```

A final method may be inherited, but it cannot be overridden.

### **Rule 6: Every ordinary Java class ultimately inherits from** `Object`

For example:

```
class Employee {
}
```

Even though the code does not explicitly say:

```
class Employee extends Object {
}
```

`Employee` directly extends `Object` when no other superclass is specified.

Useful `Object` methods include:

- `toString()`
- `equals(Object)`
- `hashCode()`
- `getClass()`

## **5.8 Inheritance vs composition**

This is a significant professional design topic.

Inheritance represents an **is-a** relationship.

Composition represents a **has-a** relationship.

Examples:

- A `Dog` is an `Animal`.
- A `Car` has an `Engine`.
- An `Order` has a collection of `OrderItem` objects.

A car should not extend an engine because a car is not an engine.

Instead, it should contain an engine.

```
class Engine {

    void start() {
        System.out.println("Engine started");
    }
}

class Car {

    private final Engine engine;

    Car() {
        engine = new Engine();
    }

    void startCar() {

        engine.start();

        System.out.println("Car started");
    }
}
```

The `Car` object uses an `Engine` object to perform part of its work.

### **Why composition can be better**

Suppose you have a car with a petrol engine, a diesel engine and an electric motor.

If you design the entire hierarchy using inheritance, it can become complicated.

Composition allows the car to contain different engine implementations or engine objects without making the car itself a type of engine.

**Professional guideline:** Prefer inheritance when there is a genuine subtype relationship. Consider composition when one object needs another object to perform part of its work.

# **6. Polymorphism**

## **6.1 Meaning of polymorphism**

Polymorphism means "many forms."

In Java, polymorphism allows the same method name or common reference type to be associated with different behaviour.

For example, consider a `PaymentMethod` type.

It may have different implementations:

- Credit card payment.
- UPI payment.
- Net banking payment.

The application can call a common method called `pay()`, but the actual behaviour depends on the selected implementation.

There are two important forms of polymorphism in Java:

1. Compile-time polymorphism.
2. Runtime polymorphism.

## **6.2 Compile-time polymorphism – method overloading**

Method overloading occurs when a class declares multiple methods with the same name but different parameter lists.

### **Example**

```
class Calculator {

    int add(int a, int b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }

    double add(double a, double b) {
        return a + b;
    }
}
```

Main program:

```
public class Main {

    public static void main(String[] args) {

        Calculator calculator = new Calculator();

        System.out.println(calculator.add(10, 20));

        System.out.println(calculator.add(10, 20, 30));

        System.out.println(calculator.add(10.5, 20.5));
    }
}
```

Output:

```
30
60
31.0
```

### **How does Java select the method?**

The compiler examines the method name, argument count, argument types and applicable conversion rules.

For example:

```
calculator.add(10, 20);
```

The arguments are both `int`, so the compiler selects:

```
int add(int a, int b)
```

For:

```
calculator.add(10.5, 20.5);
```

The arguments are `double`, so the compiler selects:

```
double add(double a, double b)
```

### **What can differ in overloaded methods?**

- Number of parameters.
- Parameter types.
- Parameter order.

Examples:

```
void display(int a) { }

void display(double a) { }

void display(int a, String b) { }

void display(String a, int b) { }
```

All are valid overloads.

### **Can we overload by changing only the return type?**

No.

```
class Calculator {

    int calculate(int a) {
        return a;
    }

    // Invalid:
    // double calculate(int a) {
    //     return a;
    // }
}
```

Both methods have the same name and parameter list.

Return type alone cannot distinguish an overload.

## **6.3 Overloading and type promotion**

Java may use permitted conversions when resolving overloaded methods.

Example:

```
class Test {

    void display(double value) {
        System.out.println("double");
    }

    void display(long value) {
        System.out.println("long");
    }
}
```

Now:

```
Test t = new Test();
t.display(10);
```

Output:

```
long
```

Why?

The argument `10` is an `int`.

It can be widened to either `long` or `double`, but the `long` overload is more specific in this situation.

This is one reason method overloading can become confusing when many numeric overloads are declared.

## **6.4 Runtime polymorphism – method overriding**

Method overriding occurs when a subclass provides an implementation of an inherited instance method, subject to Java's overriding rules.

Example:

```
class Animal {

    void sound() {
        System.out.println("Animal makes sound");
    }
}

class Dog extends Animal {

    @Override
    void sound() {
        System.out.println("Dog barks");
    }
}

class Cat extends Animal {

    @Override
    void sound() {
        System.out.println("Cat meows");
    }
}
```

Main program:

```
public class Main {

    public static void main(String[] args) {

        Animal a1 = new Dog();
        Animal a2 = new Cat();

        a1.sound();
        a2.sound();
    }
}
```

Output:

```
Dog barks
Cat meows
```

### **Why does this happen?**

Consider:

```
Animal a1 = new Dog();
```

There are two types involved.

**Reference type:**

```
Animal
```

**Runtime object type:**

```
Dog
```

The reference type controls which members are available to the compiler.

The runtime object type determines which overridden instance method implementation is selected.

When `a1.sound()` executes, Java selects the `Dog` implementation because the actual object is a `Dog`.

This behaviour is called dynamic method dispatch.

## **6.5 Reference type vs runtime object type**

Consider:

```
class Animal {

    void eat() {
        System.out.println("Animal eats");
    }
}

class Dog extends Animal {

    @Override
    void eat() {
        System.out.println("Dog eats");
    }

    void bark() {
        System.out.println("Dog barks");
    }
}
```

Now:

```
Animal animal = new Dog();

animal.eat();

// animal.bark(); // Compilation error
```

Output:

```
Dog eats
```

Why does `animal.bark()` fail?

Because the compiler checks the declared reference type, which is `Animal`.

The `Animal` class does not declare `bark()`.

Although the runtime object is a `Dog`, the reference cannot directly access a member that is not available through its declared type.

### **Safe downcasting in Java 17**

If you need to access a subclass-specific operation, you can check the runtime type:

```
if (animal instanceof Dog dog) {
    dog.bark();
}
```

Here:

- `instanceof` checks whether the object is compatible with `Dog`.
- `dog` is the pattern variable.
- Inside the successful branch, `dog` refers to the object as a `Dog`.

This pattern matching form is a standard Java 17 feature.

## **6.6 Method overriding rules**

Important rules include:

1. The overriding method must have a compatible signature.
2. The overriding method cannot reduce accessibility.
3. The return type must be the same or a permitted covariant reference type.
4. A final method cannot be overridden.
5. A private method is not overridden.
6. Static methods are hidden, not overridden.
7. Checked exceptions declared by the overriding method must obey the overriding rules.
8. The `@Override` annotation helps the compiler verify that the declaration actually overrides or implements a method.

### **Example: Accessibility**

```
class Parent {

    public void display() {
        System.out.println("Parent");
    }
}

class Child extends Parent {

    // Invalid: cannot reduce public access to protected
    // protected void display() { }
}
```

The subclass cannot reduce the method's accessibility.

### **Example: Covariant return type**

```
class Animal {
}

class Dog extends Animal {
}

class Parent {

    Animal getAnimal() {
        return new Animal();
    }
}

class Child extends Parent {

    @Override
    Dog getAnimal() {
        return new Dog();
    }
}
```

This is valid because `Dog` is a subtype of `Animal`.

## **6.7 Static method hiding**

Static methods are not dynamically dispatched as instance methods.

Consider:

```
class Parent {

    static void display() {
        System.out.println("Parent static method");
    }
}

class Child extends Parent {

    static void display() {
        System.out.println("Child static method");
    }
}
```

Now:

```
public class Main {

    public static void main(String[] args) {

        Parent obj = new Child();

        obj.display();
    }
}
```

Output:

```
Parent static method
```

The invocation is resolved using the compile-time reference type, `Parent`.

This is method hiding.

It is not runtime overriding.

For clarity, static methods should generally be called using their class names:

```
Parent.display();
Child.display();
```

## **6.8 Overloading vs overriding**

|Feature|Method overloading|Method overriding|
|---|---|---|
|Meaning|Same method name with different parameter lists|Subclass provides a specialised implementation of an inherited method|
|Inheritance required?|No|Yes, or an interface implementation relationship|
|Parameters|Must differ|Must match the overriding rules|
|Return type|May differ, but not as the sole distinction|Same or permitted covariant reference type|
|Binding|Compile-time overload resolution|Runtime dispatch for overridden instance methods|
|Static methods|Can be overloaded|Hidden, not overridden|
|Private methods|Can be overloaded|Not overridden|
|Main purpose|Provide multiple ways to perform an operation|Specialise inherited behaviour|

## **6.9 Compile-time vs runtime polymorphism**

|Compile-time polymorphism|Runtime polymorphism|
|---|---|
|Commonly achieved through overloading|Achieved through overriding|
|Method selected during compilation|Overridden instance implementation selected at runtime|
|Uses argument types and overload-resolution rules|Uses the runtime class of the object|
|Does not require inheritance|Requires an overriding relationship|
|Example: `add(int,int)` and `add(double,double)`|Example: `Animal a = new Dog(); a.sound();`|

# **7. Combining All Four OOP Principles in One Application**

Now that we understand the principles separately, let's combine them in a small payment-processing application.

This is important because real-world Java applications do not usually use OOP principles in isolation.

## **7.1 Application requirement**

Suppose we are developing a payment application.

The application must:

- Support UPI payments.
- Support credit card payments.
- Validate payment amounts.
- Process payments using a common interface.
- Allow new payment types to be added without rewriting the service.

## **7.2 Step 1: Define the abstraction**

```
interface PaymentMethod {

    void pay(double amount);
}
```

This defines the common contract.

Any class implementing `PaymentMethod` must provide the `pay()` operation.

## **7.3 Step 2: Implement UPI payment**

```
class UpiPayment implements PaymentMethod {

    @Override
    public void pay(double amount) {

        System.out.println(
            "Processing UPI payment of " + amount
        );
    }
}
```

## **7.4 Step 3: Implement credit card payment**

```
class CreditCardPayment implements PaymentMethod {

    @Override
    public void pay(double amount) {

        System.out.println(
            "Processing credit card payment of " + amount
        );
    }
}
```

Both classes satisfy the same interface but perform different implementations.

## **7.5 Step 4: Encapsulate payment processing**

```
class PaymentService {

    private final PaymentMethod paymentMethod;

    PaymentService(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void processPayment(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Payment amount must be positive"
            );
        }

        paymentMethod.pay(amount);
    }
}
```

Let's understand this class.

```
private final PaymentMethod paymentMethod;
```

- `private` restricts direct external access.
- `final` prevents reassignment after initialisation.
- `PaymentMethod` is the declared type of the field.

The constructor receives a payment implementation:

```
PaymentService(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
}
```

This is an example of constructor injection: the dependency is provided when the object is created.

The service validates the amount and delegates the payment operation.

## **7.6 Step 5: Use polymorphism**

```
public class Main {

    public static void main(String[] args) {

        PaymentMethod upi = new UpiPayment();

        PaymentService service =
            new PaymentService(upi);

        service.processPayment(5000);
    }
}
```

Output:

```
Processing UPI payment of 5000.0
```

Now change the implementation:

```
PaymentMethod card = new CreditCardPayment();

PaymentService service =
    new PaymentService(card);

service.processPayment(5000);
```

Output:

```
Processing credit card payment of 5000.0
```

The payment service does not need separate `processUpiPayment()` and `processCardPayment()` methods.

It works through the common `PaymentMethod` contract.

## **7.7 Identify the four principles**

|OOP principle|Implementation|
|---|---|
|Encapsulation|`PaymentService` keeps its dependency private|
|Abstraction|`PaymentMethod` defines the payment contract|
|Inheritance / subtype relationship|Payment classes implement the interface|
|Polymorphism|The same service works with different payment implementations|

**Professional development insight:** This design separates the payment service from the details of a particular payment method. It makes the system easier to extend and test. In production systems, payment processing would also require transaction handling, security, idempotency, logging, exception handling and integration with external payment providers.

# **8. Important Java 17 OOP Concepts and Interview Traps**

This section consolidates rules that are frequently misunderstood.

## **8.1 Can we create an object without** `new`**?**

Yes. `new` is the normal way to create an object, but Java has other object-creation mechanisms.

For example, an object may be obtained through:

- Deserialization.
- Cloning, where supported.
- Factory methods that return objects created internally.
- Reflection-based construction.

Example of a factory method:

```
class Employee {

    private Employee() {
    }

    public static Employee createEmployee() {
        return new Employee();
    }
}
```

Usage:

```
Employee e = Employee.createEmployee();
```

The caller does not use `new`, but the factory method does.

## **8.2 Can we create an object of an abstract class?**

No, not directly.

```
abstract class Animal {
}

// Invalid:
// Animal a = new Animal();
```

However, an abstract class can be used as a reference type:

```
abstract class Animal {

    abstract void sound();
}

class Dog extends Animal {

    @Override
    void sound() {
        System.out.println("Bark");
    }
}
```

```
Animal a = new Dog();
a.sound();
```

Output:

```
Bark
```

## **8.3 Can an abstract class have a constructor?**

Yes.

```
abstract class Employee {

    String name;

    Employee(String name) {
        this.name = name;
    }
}

class Developer extends Employee {

    Developer(String name) {
        super(name);
    }
}
```

The abstract class constructor runs as part of creating a subclass object.

## **8.4 Can an interface extend another interface?**

Yes.

```
interface A {

    void methodA();
}

interface B extends A {

    void methodB();
}
```

A class implementing `B` must satisfy the abstract methods inherited from `A` as well as those declared in `B`, unless it is itself abstract.

```
class C implements B {

    public void methodA() {
        System.out.println("A");
    }

    public void methodB() {
        System.out.println("B");
    }
}
```

## **8.5 Can a class extend one class and implement multiple interfaces?**

Yes.

```
class Animal {

    void eat() {
        System.out.println("Eating");
    }
}

interface Runnable {

    void run();
}

interface Swimmable {

    void swim();
}

class Dog extends Animal implements Runnable, Swimmable {

    public void run() {
        System.out.println("Running");
    }

    public void swim() {
        System.out.println("Swimming");
    }
}
```

This is valid Java.

## **8.6 Can constructors be overridden?**

No.

Constructors are not inherited, so they cannot be overridden.

They can be overloaded.

## **8.7 Can private methods be overridden?**

No.

A private method is not inherited as an accessible subclass method. A same-named method in a subclass is a separate declaration, not an override.

## **8.8 Can static methods be overridden?**

No.

Static methods can be hidden by a subclass declaration with a matching signature.

## **8.9 Can an object have multiple references?**

Yes.

```
Student s1 = new Student();
Student s2 = s1;
Student s3 = s2;
```

All three references can refer to the same object.

## **8.10 Is Java 17 purely object-oriented?**

No.

Java supports object-oriented programming, but it also has primitive types such as:

```
int
double
char
boolean
```

These primitive values are not objects.

Java also has static members and procedural-style methods.

# **9. Common Mistakes and How to Correct Them**

|Common mistake|Correct understanding|
|---|---|
|Saying a class and object are the same|A class defines a type; an object is an instance|
|Saying a reference variable is the object|A reference variable stores a reference to an object|
|Thinking `s2 = s1` creates a copy|It copies the reference value|
|Thinking every class automatically has a no-argument constructor|The compiler supplies a default constructor only when no constructor is declared|
|Thinking a constructor has a return type|Constructors have no return type|
|Thinking private means the object cannot access the field|Methods within the declaring class can access it|
|Creating setters for every field|Expose only the operations that make sense|
|Thinking abstraction and encapsulation are identical|They solve different design problems|
|Thinking Java supports multiple class inheritance|A class can directly extend only one class|
|Thinking interfaces contain only abstract methods|Java 17 interfaces also support default, static and private methods|
|Thinking static methods are overridden|Static methods are hidden|
|Thinking overloading is decided at runtime|Overload resolution occurs at compile time|
|Thinking a parent reference can access all child methods|The declared reference type controls member accessibility|
|Thinking every use of inheritance is good design|Inheritance should represent a genuine subtype relationship|

# **10. Practical Lab Exercises – Session 4**

The theory becomes meaningful only when you can implement it independently.

These exercises are designed around the session's 2 laboratory hours. You can complete the remaining challenge tasks as additional practice.

## **Lab 1 – Student Management System**

Classes, objects, constructors and encapsulation

**Problem statement**

Create a Java program to manage student details.

Create a class called `Student`.

Requirements:

1. Declare the following private fields:
    - `rollNumber`
    - `name`
    - `marks`
2. Create a parameterized constructor.
3. Create getters for all fields.
4. Create a method `updateMarks(double marks)`.
5. Accept marks only between 0 and 100.
6. Create three student objects.
7. Display their details.
8. Update the marks of one student.

**Expected design:**

```
class Student {

    private int rollNumber;
    private String name;
    private double marks;

    // Constructor

    // Getters

    // updateMarks()

    // displayDetails()
}
```

**What you should learn:**

- How to create a class.
- How to create independent objects.
- How constructors initialise fields.
- How private fields protect data.
- How methods validate and update object state.

## **Lab 2 – Shape Area Calculator**

Abstraction and polymorphism

**Problem statement**

Create an abstract class named `Shape`.

It must declare:

```
abstract double calculateArea();
```

Create three subclasses:

- `Circle`
- `Rectangle`
- `Triangle`

Use these formulas:

|Shape|Formula|
|---|---|
|Circle|\(\pi \times r \times r\)|
|Rectangle|\(length \times width\)|
|Triangle|\(\frac{1}{2} \times base \times height\)|

Store the objects in a `Shape[]` array and calculate the area of each shape using a loop.

**Expected design:**

```
Shape[] shapes = {
    new Circle(5),
    new Rectangle(10, 20),
    new Triangle(10, 5)
};

for (Shape shape : shapes) {
    System.out.println(shape.calculateArea());
}
```

**What you should learn:**

- Abstract classes.
- Abstract methods.
- Method overriding.
- Parent-type references.
- Runtime polymorphism.
- Arrays of superclass references.

## **Lab 3 – Employee Inheritance System**

Inheritance, constructors and super

**Problem statement**

Create the following class hierarchy:

```
             Employee
             /      \
            /        \
      Developer     Manager
```

The `Employee` class must contain:

- Employee ID.
- Employee name.
- Salary.
- A constructor.
- A method `displayDetails()`.
- A method `calculateBonus()`.

The `Developer` class must contain:

- Programming language.
- Its own constructor.
- An overridden `calculateBonus()` method.

The `Manager` class must contain:

- Team size.
- Its own constructor.
- An overridden `calculateBonus()` method.

**What you should learn:**

- Single and hierarchical inheritance.
- `extends`.
- `super()`.
- Method overriding.
- Runtime polymorphism.

## **Lab 4 – Payment Processing Application**

All four OOP principles

Create:

- `PaymentMethod` interface.
- `UpiPayment` class.
- `CreditCardPayment` class.
- `NetBankingPayment` class.
- `PaymentService` class.

Requirements:

1. Every payment class must implement `PaymentMethod`.
2. `PaymentService` must store a private `PaymentMethod` reference.
3. Inject the implementation through a constructor.
4. Validate the payment amount.
5. Call the common `pay()` operation.
6. Demonstrate at least three payment implementations.

**What you should learn:**

- Interface abstraction.
- Encapsulation.
- Interface implementation.
- Runtime polymorphism.
- Composition and dependency injection.

# **11. Additional Self-Learning and Programming Challenges**

These activities are intended to strengthen conceptual understanding and prepare you for technical assessments.

## **Basic level**

1. Create a `Book` class with title, author and price.
2. Create five `Book` objects and display their information.
3. Write a class with both no-argument and parameterized constructors.
4. Demonstrate constructor overloading.
5. Demonstrate the difference between instance and static variables.
6. Create a class with private fields and validated setters.

## **Intermediate level**

1. Create a `Vehicle` superclass and `Car`, `Bike` and `Bus` subclasses.
2. Demonstrate single, multilevel and hierarchical inheritance.
3. Create an abstract `Shape` class with three implementations.
4. Create an interface named `Playable` and implement it in two classes.
5. Demonstrate multiple interface implementation.
6. Demonstrate method overloading and overriding in the same application.

## **Advanced conceptual level**

1. Write a program showing that two reference variables can refer to the same object.
2. Write a program showing the difference between modifying an object and reassigning a reference.
3. Demonstrate static method hiding.
4. Demonstrate constructor execution order in inheritance.
5. Demonstrate a default method conflict between two interfaces and resolve it.
6. Demonstrate covariant return types.
7. Demonstrate composition using `Car` and `Engine`.
8. Build a small employee or banking application using all four OOP principles.

# **12. Java 17 Technical Assessment – 10 MCQs**

These questions test the concepts taught in this session. Each question has exactly four options and one best answer. The answer and explanations follow each question.

## **MCQ 1 – Object References**

**Question:** What is the output of the following program?

```
class Student {

    int marks = 50;
}

public class Main {

    public static void main(String[] args) {

        Student s1 = new Student();

        Student s2 = s1;

        s2.marks = 90;

        System.out.println(s1.marks);
    }
}
```

A. `50`

B. `90`

C. `0`

D. Compilation error

Correct answer: B – 90

**Detailed explanation:**

First:

```
Student s1 = new Student();
```

One `Student` object is created.

Next:

```
Student s2 = s1;
```

The reference value held by `s1` is copied into `s2`.

No second object is created.

Both variables refer to the same object.

When:

```
s2.marks = 90;
```

the object's field is modified.

Since `s1` refers to that same object, `s1.marks` also returns `90`.

**Why the other options are incorrect:**

- **A – 50:** This would be possible if `s1` and `s2` referred to separate objects that had not been modified through `s1`. Here they share one object.
- **C – 0:** The field was explicitly initialised to `50` and then changed to `90`.
- **D – Compilation error:** Assigning one reference variable to another of the same type is valid Java.

**Concept tested:** Reference variables, object identity, reference assignment and aliasing.

## **MCQ 2 – Constructors**

**Question:** What happens when the following program is compiled?

```
class Employee {

    Employee(int id) {
        System.out.println(id);
    }
}

public class Main {

    public static void main(String[] args) {

        Employee e = new Employee();
    }
}
```

A. Prints `0`

B. Prints `null`

C. Compilation error

D. Runtime exception

Correct answer: C – Compilation error

**Detailed explanation:**

The class declares:

```
Employee(int id)
```

This is a parameterized constructor.

Since the programmer has declared a constructor, Java does not automatically supply a default no-argument constructor.

But this statement:

```
new Employee()
```

requires an accessible constructor that accepts zero arguments.

No matching constructor exists.

Therefore, compilation fails.

**Why the other options are incorrect:**

- **A – Prints 0:** Java does not automatically invoke the parameterized constructor with a zero value.
- **B – Prints null:** No reference field is being printed, and object creation cannot complete.
- **D – Runtime exception:** The error is detected at compile time.

**Concept tested:** Default constructor generation, constructor overloading and constructor invocation.

## **MCQ 3 – Encapsulation**

**Question:** Which of the following implementations best demonstrates encapsulation?

A.

```
class Account {
    public double balance;
}
```

B.

```
class Account {

    private double balance;

    public void deposit(double amount) {

        if (amount > 0) {
            balance += amount;
        }
    }
}
```

C.

```
class Account {
    static double balance;
}
```

D.

```
class Account {
    final double balance = 1000;
}
```

Correct answer: B

**Detailed explanation:**

Option B declares the field as private:

```
private double balance;
```

External classes cannot directly access it.

The class provides a controlled operation:

```
public void deposit(double amount)
```

The method validates the amount before modifying the balance.

This demonstrates encapsulation through protected state and controlled behaviour.

**Why the other options are incorrect:**

- **A:** The field is public and can be modified directly without validation.
- **C:** `static` makes the field belong to the class. It does not provide data protection.
- **D:** `final` prevents reassignment after initialisation, but it does not demonstrate controlled state-changing behaviour.

**Concept tested:** Encapsulation, private access, data hiding and validation.

## **MCQ 4 – Abstract Classes**

**Question:** Which statement about abstract classes in Java 17 is correct?

A. An abstract class cannot contain constructors.

B. An abstract class must contain at least one abstract method.

C. An abstract class can contain both abstract and concrete methods.

D. An abstract class can be instantiated directly.

Correct answer: C

**Detailed explanation:**

An abstract class can contain methods with implementations as well as abstract methods without implementations.

Example:

```
abstract class Vehicle {

    abstract void move();

    void start() {
        System.out.println("Vehicle starting");
    }
}
```

Here:

- `move()` is abstract.
- `start()` is concrete.

A concrete subclass must implement the inherited abstract method.

**Why the other options are incorrect:**

- **A:** Abstract classes can have constructors. Those constructors execute during subclass object construction.
- **B:** An abstract class may contain no abstract methods.
- **D:** An abstract class cannot be instantiated directly.

**Concept tested:** Abstract class declarations, abstract methods and concrete methods.

## **MCQ 5 – Interfaces**

**Question:** Consider this Java 17 code:

```
interface Printable {

    void print();

    default void show() {
        System.out.println("Showing");
    }
}

class Document implements Printable {

    public void print() {
        System.out.println("Printing");
    }
}
```

Which statement is correct?

A. Compilation error because interfaces cannot contain implemented methods.

B. Compilation error because `Document` must override `show()`.

C. The code compiles, and `Document` can use the inherited default method.

D. The interface can be instantiated directly.

Correct answer: C

**Detailed explanation:**

Java 17 interfaces support default methods.

The `Document` class implements the abstract `print()` method.

It can use the default implementation of `show()` without overriding it.

Example:

```
Document d = new Document();

d.print();
d.show();
```

Output:

```
Printing
Showing
```

**Why the other options are incorrect:**

- **A:** Interfaces can contain default methods with implementations.
- **B:** An implementing class does not have to override a default method unless it needs different behaviour or must resolve a conflict.
- **D:** Interfaces cannot be directly instantiated.

**Concept tested:** Java 17 interface features, default methods and implementation.

## **MCQ 6 – Inheritance and Constructors**

**Question:** What happens when this code is compiled?

```
class Parent {

    Parent(int x) {
        System.out.println("Parent " + x);
    }
}

class Child extends Parent {

    Child() {
        System.out.println("Child");
    }
}
```

A. Compilation succeeds.

B. Compilation fails because the compiler attempts to invoke `super()`, but `Parent` has no no-argument constructor.

C. Compilation succeeds, but a runtime exception occurs.

D. The parent constructor automatically receives `0`.

Correct answer: B

**Detailed explanation:**

The `Child` constructor does not explicitly invoke a superclass constructor.

The compiler attempts to insert an implicit no-argument superclass constructor invocation.

Conceptually, it becomes:

```
Child() {

    super();

    System.out.println("Child");
}
```

However, `Parent` only declares:

```
Parent(int x)
```

There is no accessible no-argument constructor.

Therefore, compilation fails.

**Why the other options are incorrect:**

- **A:** The required superclass constructor invocation cannot be resolved.
- **C:** The error occurs at compile time.
- **D:** Java does not automatically pass `0` to the parameterized constructor.

**Concept tested:** Constructor chaining, superclass construction and implicit `super()`.

## **MCQ 7 – Method Overloading**

**Question:** What is the output?

```
class Calculator {

    void display(int x) {
        System.out.println("Integer");
    }

    void display(double x) {
        System.out.println("Double");
    }
}

public class Main {

    public static void main(String[] args) {

        Calculator c = new Calculator();

        c.display(10);
    }
}
```

A. Integer

B. Double

C. Compilation error

D. Runtime exception

Correct answer: A – Integer

**Detailed explanation:**

The literal:

```
10
```

is an `int` literal.

The compiler selects the overload:

```
void display(int x)
```

because it is directly applicable.

The `double` overload is unnecessary because an exact `int` match is available.

**Why the other options are incorrect:**

- **B:** The `double` overload is not preferred over the directly applicable `int` overload.
- **C:** The declarations are valid overloads because the parameter types differ.
- **D:** The program executes normally.

**Concept tested:** Method overloading and compile-time overload resolution.

## **MCQ 8 – Runtime Polymorphism**

**Question:** What is the output?

```
class Animal {

    void sound() {
        System.out.println("Animal");
    }
}

class Dog extends Animal {

    @Override
    void sound() {
        System.out.println("Dog");
    }
}

public class Main {

    public static void main(String[] args) {

        Animal a = new Dog();

        a.sound();
    }
}
```

A. Animal

B. Dog

C. Compilation error

D. Runtime exception

Correct answer: B – Dog

**Detailed explanation:**

The reference type is:

```
Animal
```

The actual object type is:

```
Dog
```

The `Dog` class overrides the `sound()` instance method.

When Java executes:

```
a.sound();
```

dynamic method dispatch selects the overridden implementation associated with the runtime class of the object.

Therefore, the output is:

```
Dog
```

**Why the other options are incorrect:**

- **A:** The parent method is overridden for the `Dog` implementation.
- **C:** Assigning a `Dog` object to an `Animal` reference is valid because `Dog` is a subtype of `Animal`.
- **D:** No runtime error occurs.

**Concept tested:** Method overriding, runtime object type and dynamic method dispatch.

## **MCQ 9 – Static Method Hiding**

**Question:** What is the output?

```
class Parent {

    static void display() {
        System.out.println("Parent");
    }
}

class Child extends Parent {

    static void display() {
        System.out.println("Child");
    }
}

public class Main {

    public static void main(String[] args) {

        Parent obj = new Child();

        obj.display();
    }
}
```

A. Parent

B. Child

C. Compilation error

D. Both Parent and Child

Correct answer: A – Parent

**Detailed explanation:**

Static methods belong to classes and are not dynamically dispatched as instance methods.

The reference variable is declared as:

```
Parent obj
```

The static method invocation is resolved using the compile-time reference type.

Therefore:

```
obj.display();
```

selects the `Parent` declaration.

Output:

```
Parent
```

**Why the other options are incorrect:**

- **B:** Runtime object type does not select an overridden implementation for a static method.
- **C:** Static method hiding is legal Java.
- **D:** Only one static method invocation takes place.

**Concept tested:** Static method hiding vs instance method overriding.

## **MCQ 10 – Combined OOP Principles**

**Question:** Consider:

```
interface Shape {

    double area();
}

class Circle implements Shape {

    private double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

public class Main {

    public static void main(String[] args) {

        Shape s = new Circle(2);

        System.out.printf("%.2f", s.area());
    }
}
```

Which statement is correct?

A. Compilation fails because an interface cannot reference a class object.

B. The program demonstrates abstraction, encapsulation and runtime polymorphism.

C. The program demonstrates only inheritance because `Circle` extends `Shape`.

D. The program fails because the `radius` field is private.

Correct answer: B

**Detailed explanation:**

The program demonstrates several OOP principles.

**Abstraction**

The `Shape` interface defines the required operation:

```
double area();
```

It does not specify how each shape calculates its area.

**Encapsulation**

The field:

```
private double radius;
```

cannot be directly accessed from unrelated external code.

The `Circle` class controls its use.

**Polymorphism**

```
Shape s = new Circle(2);
```

The reference type is `Shape`, but the runtime object type is `Circle`.

When:

```
s.area();
```

is invoked, Java executes the `Circle` implementation.

Output:

```
12.57
```

**Why the other options are incorrect:**

- **A:** An interface reference can refer to an object of a class that implements that interface.
- **C:** `Circle` implements `Shape`; it does not extend it as a class.
- **D:** A class can access its own private fields through its methods.

**Concept tested:** Combining abstraction, encapsulation, interface implementation and runtime polymorphism.

# **13. Final Revision – Session 4**

Use this table for quick revision after completing the detailed notes.

|Topic|Essential point|
|---|---|
|OOP|Organises software around objects|
|Class|Defines a reference type|
|Object|Runtime instance of a class|
|Reference|Value used to identify an object|
|Constructor|Initialises a newly created object|
|`this`|Refers to the current object|
|`super`|Refers to superclass members or invokes its constructor|
|`static`|Associated with the class|
|Encapsulation|Groups state and behaviour and controls access|
|Abstraction|Exposes essential operations while hiding implementation details|
|Abstract class|Can contain shared state, constructors and abstract/concrete methods|
|Interface|Defines a contract and may provide default/static/private methods|
|Inheritance|Creates a specialised class from an existing class|
|Composition|Represents a has-a relationship|
|Overloading|Same method name with different parameter lists|
|Overriding|Subclass provides a specialised inherited method implementation|
|Runtime polymorphism|Overridden instance method selection depends on runtime object type|
|Static method hiding|Static method selection is based on compile-time type|

## **14. Official Java 17 Technical References**

These are the authoritative references for the language rules covered in this session.

1. [JLS Chapter 4 – Types, Values, and Variables](https://docs.oracle.com/javase/specs/jls/se17/html/jls-4.html?utm_source=chatgpt.com) — reference types, objects and variables.
2. [JLS Chapter 6 – Names](https://docs.oracle.com/javase/specs/jls/se17/html/jls-6.html?utm_source=chatgpt.com) — declarations, scopes and accessibility.
3. [JLS Chapter 8 – Classes](https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html?utm_source=chatgpt.com) — classes, fields, methods, constructors, inheritance and overriding.
4. [JLS Chapter 9 – Interfaces](https://docs.oracle.com/javase/specs/jls/se17/html/jls-9.html?utm_source=chatgpt.com) — interfaces, default methods and interface inheritance.
5. [JLS Chapter 12 – Execution](https://docs.oracle.com/javase/specs/jls/se17/html/jls-12.html?utm_source=chatgpt.com) — object creation and initialisation.
6. [JLS Chapter 15 – Expressions](https://docs.oracle.com/javase/specs/jls/se17/html/jls-15.html?utm_source=chatgpt.com) — method invocation, overloading and runtime behaviour.
7. [Oracle Java SE 17 Documentation](https://docs.oracle.com/en/java/javase/17/?utm_source=chatgpt.com) — official Java 17 platform documentation.

# **15. Recommended Study and Practice Sequence**

To get the most from these notes, study the topics in this order:

1. **Understand classes and objects**
    
    Write a `Student` and `Employee` class. Create multiple objects and experiment with reference assignment.
    
2. **Practise constructors and** `this`
    
    Create overloaded constructors, use constructor chaining, and understand when the compiler generates a default constructor.
    
3. **Practise encapsulation**
    
    Convert public fields into private fields and introduce validation methods.
    
4. **Understand abstraction**
    
    Implement the same operation using an abstract class and then using an interface.
    
5. **Practise inheritance**
    
    Build an employee hierarchy and trace constructor execution.
    
6. **Master polymorphism**
    
    Predict outputs involving overloading, overriding, parent references, child objects and static method hiding.
    
7. **Build the combined application**
    
    Complete the payment-processing application without copying the notes.
    
8. **Attempt the MCQs again**
    
    Try answering each question before reading its explanation. Explain why the other three options are wrong.
    

**The most important learning test:** You should be able to take a requirement such as "build a payment system supporting UPI and credit cards" and independently decide which classes, interfaces, fields, constructors and methods are required—and explain why you chose that design.

That is the difference between knowing OOP definitions and being able to use OOP as a Java developer.
