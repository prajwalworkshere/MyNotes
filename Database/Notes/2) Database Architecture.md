# Topic 2 — Database Architecture

## 1. What is Database Architecture?

**Database architecture** describes how a database system is structured, how its components are organized, and how users/applications interact with the database.

In simple terms:

> **Database architecture tells us where the database is located, where the application runs, how users access the database, and how different components communicate.**

A typical database system can look like:

```text
User
  ↓
Application
  ↓
Database System
  ↓
Database
```

A real-world system can be more complex:

```text
                    USERS
                      ↓
              ┌──────────────┐
              │  Application │
              └──────┬───────┘
                     ↓
              ┌──────────────┐
              │ Application  │
              │    Server    │
              └──────┬───────┘
                     ↓
              ┌──────────────┐
              │ Database     │
              │    Server    │
              └──────┬───────┘
                     ↓
              ┌──────────────┐
              │   Database   │
              └──────────────┘
```

---

# 2. Why Do We Need Database Architecture?

Imagine an e-commerce application.

Thousands or millions of users may access:

* Products
* Customers
* Orders
* Payments
* Inventory

If every user directly accesses the database, the system can become difficult to:

* Secure
* Maintain
* Scale
* Monitor
* Optimize

Database architecture helps separate responsibilities.

For example:

```text
User
 ↓
Frontend
 ↓
Backend / Application Server
 ↓
Database Server
 ↓
Storage
```

Each layer has a specific responsibility.

---

# 3. Main Types of Database Architecture

The main architectures are:

1. **1-Tier Architecture**
2. **2-Tier Architecture**
3. **3-Tier Architecture**

They are also called:

* Single-tier architecture
* Two-tier architecture
* Three-tier architecture

---

# 4. 1-Tier Architecture

## Definition

In **1-tier architecture**, the user, application, DBMS, and database are essentially on the same system/environment.

```text
┌──────────────────────────────┐
│            User              │
│              ↓               │
│         Application          │
│              ↓               │
│            DBMS              │
│              ↓               │
│          Database            │
└──────────────────────────────┘
```

There is no separate application server or database server communicating over a network.

## Example

Suppose you install MySQL on your computer and use a database client directly.

```text
Your Computer
│
├── Database Client
├── DBMS
└── Database
```

This is a simple form of single-tier architecture.

## Uses

Mostly used for:

* Local development
* Learning
* Testing
* Standalone applications
* Small/simple systems

## Advantages

* Simple
* Easy to develop
* Easy to configure
* No network communication required

## Disadvantages

* Poor scalability
* Weak separation of responsibilities
* Security can be harder to manage
* Not suitable for large multi-user applications

---

# 5. 2-Tier Architecture

## Definition

In **2-tier architecture**, there are two major layers:

```text
Client
   ↓
Database Server
```

The client communicates directly with the database server.

## Architecture

```text
┌──────────────┐
│    Client    │
│ Application  │
└──────┬───────┘
       │
       │ SQL / Database Protocol
       ↓
┌──────────────┐
│   Database   │
│    Server    │
│    + DBMS    │
└──────────────┘
```

The client contains the application/presentation logic, while the server manages the database.

---

# 6. Example of 2-Tier Architecture

Imagine a desktop application used by employees:

```text
Employee
   ↓
Desktop Application
   ↓
MySQL Database Server
```

The application might send:

```sql
SELECT *
FROM employees;
```

to the database server.

The database server executes the query and returns the result.

---

# 7. Components of 2-Tier Architecture

## Tier 1 — Client

The client generally contains:

* User interface
* Application logic
* Database connectivity code

## Tier 2 — Database Server

The database server contains:

* DBMS
* Database
* Query processing
* Transaction management
* Storage management
* Security mechanisms

So:

```text
CLIENT
  │
  │ Request
  ↓
DATABASE SERVER
  │
  ├── DBMS
  ├── Query Processor
  ├── Transaction Manager
  └── Database
```

---

# 8. Problems with 2-Tier Architecture

Consider a large application:

```text
1000 Users
    ↓
Desktop Applications
    ↓
Database Server
```

Every client may contain application logic.

This creates several problems.

## 1. Maintenance

If application logic changes, many client applications may need to be updated.

## 2. Security

Clients communicate directly with the database.

This can expose database access more broadly than desired.

## 3. Scalability

As the number of clients increases, managing direct database connections can become difficult.

## 4. Business Logic Duplication

Different client applications may implement the same business rules separately.

This leads us to **3-tier architecture**.

---

# 9. 3-Tier Architecture

## Definition

In **3-tier architecture**, an intermediate application layer is introduced.

Instead of:

```text
Client → Database
```

we have:

```text
Client
   ↓
Application Server
   ↓
Database Server
```

Therefore:

> **3-tier architecture separates presentation, application/business logic, and data management into three distinct layers.**

---

# 10. Three Layers of 3-Tier Architecture

## Layer 1 — Presentation Layer

This is what the user interacts with.

Examples:

* Web browser
* Mobile application
* Desktop UI

```text
User
 ↓
UI
```

Its primary responsibility is **presentation and user interaction**.

Examples:

```text
Login Page
Product Page
Shopping Cart
Dashboard
```

---

# 11. Layer 2 — Application / Business Logic Layer

This is the middle layer.

It processes requests from the presentation layer.

It contains things such as:

* Business rules
* Validation
* Authentication logic
* Authorization logic
* Application processing
* API endpoints

For example:

```text
User:
"Buy 2 laptops"

        ↓

Application Server

        ↓

Check:
Is product available?
Is user authenticated?
Is quantity valid?
Calculate price
Create order

        ↓

Database
```

The application server then communicates with the database.

---

# 12. Layer 3 — Data Layer

This layer manages persistent data.

It generally contains:

* Database
* DBMS
* Tables
* Indexes
* Stored data

Examples:

* MySQL
* PostgreSQL
* Oracle
* SQL Server

The application server sends database requests to this layer.

---

# 13. Complete 3-Tier Architecture

```text
                 USER
                   │
                   ↓
        ┌─────────────────────┐
        │  Presentation Layer │
        │                     │
        │ Web / Mobile / UI   │
        └──────────┬──────────┘
                   │
                   ↓
        ┌─────────────────────┐
        │ Application Layer   │
        │                     │
        │ Business Logic      │
        │ APIs                │
        │ Validation          │
        └──────────┬──────────┘
                   │
                   ↓
        ┌─────────────────────┐
        │     Data Layer      │
        │                     │
        │ DBMS + Database     │
        └─────────────────────┘
```

---

# 14. Real-World Example

Consider an online shopping website.

## Step 1 — User

The user clicks:

**"Place Order"**

## Step 2 — Presentation Layer

The website sends the request to the backend.

```text
Browser
   ↓
POST /orders
```

## Step 3 — Application Layer

The backend processes the request.

It might check:

```text
Is the user logged in?
Is the product available?
Is quantity valid?
What is the price?
Calculate total.
```

## Step 4 — Data Layer

The application sends database operations:

```sql
SELECT stock
FROM products
WHERE product_id = 101;
```

Then perhaps:

```sql
INSERT INTO orders (...);
```

The database processes those operations and returns the result.

---

# 15. Why 3-Tier Architecture Is Better

The major advantage is **separation of concerns**.

Each layer has a different responsibility.

| Layer        | Main Responsibility         |
| ------------ | --------------------------- |
| Presentation | User interaction            |
| Application  | Business logic              |
| Data         | Data storage and management |

## Advantages

### 1. Maintainability

You can modify one layer without necessarily rewriting everything.

### 2. Security

Users don't need direct access to the database.

```text
User
 ↓
Application
 ↓
Database
```

instead of:

```text
User
 ↓
Database
```

### 3. Scalability

Application servers can often be scaled independently.

For example:

```text
                 Load Balancer
                      ↓
             ┌────────┴────────┐
             ↓                 ↓
        App Server 1      App Server 2
             │                 │
             └────────┬────────┘
                      ↓
                  Database
```

### 4. Easier Maintenance

Business logic is centralized in the application layer rather than duplicated across clients.

---

# 16. 1-Tier vs 2-Tier vs 3-Tier

| Feature                      | 1-Tier             | 2-Tier             | 3-Tier                    |
| ---------------------------- | ------------------ | ------------------ | ------------------------- |
| Layers                       | 1                  | 2                  | 3                         |
| Client                       | Same environment   | Direct DB client   | Presentation layer        |
| Application Server           | No                 | Usually no         | Yes                       |
| DB Direct Access from Client | Yes                | Yes                | Usually no                |
| Separation                   | Low                | Medium             | High                      |
| Scalability                  | Low                | Medium             | High                      |
| Common Use                   | Local apps/testing | Client-server apps | Modern web/mobile systems |

---

# 17. Tier vs Layer

This is an important interview concept.

People often use **tier** and **layer** interchangeably, but conceptually they are not exactly the same.

## Layer

A **layer** refers mainly to a logical separation of responsibilities.

Example:

```text
Presentation Layer
Business Layer
Data Layer
```

## Tier

A **tier** generally refers to a physically or logically separated deployment boundary.

Example:

```text
Tier 1 → Client machine
Tier 2 → Application server
Tier 3 → Database server
```

### Key Difference

> **Layer = responsibility**

> **Tier = deployment/location separation**

They often correspond, but they don't have to.

---

# 18. Database Architecture vs DBMS Architecture

Do not confuse these two.

## Database Architecture

Usually discusses how the **application/client and database system are organized and communicate**.

Examples:

```text
1-Tier
2-Tier
3-Tier
```

## DBMS Architecture

Can refer to the **internal organization of a DBMS**, such as:

```text
Query Processor
Storage Manager
Transaction Manager
Buffer Manager
Disk Storage
```

These are related but different concepts.

---

# 19. Three-Schema Architecture

There is another important concept called **Three-Schema Architecture**.

> **Do not confuse it with 3-tier architecture.**

Three-schema architecture is about **data abstraction and database views**, not about client/application/server deployment.

It consists of:

```text
External Level
      ↓
Conceptual Level
      ↓
Internal Level
```

---

# 20. External Level

The **external level** describes what individual users or applications see.

Examples:

```text
Student View
Employee View
Manager View
```

Different users can have different views of the same database.

---

# 21. Conceptual Level

The **conceptual level** describes the overall logical structure of the database.

For example:

```text
STUDENT
COURSE
ENROLLMENT
TEACHER
```

It describes the database logically without focusing on how the data is physically stored.

---

# 22. Internal Level

The **internal level** describes how data is physically stored.

Examples:

* Files
* Pages
* Index structures
* Storage structures

---

# 23. 3-Tier vs Three-Schema Architecture

This distinction is **very important for exams and interviews**.

| 3-Tier Architecture               | Three-Schema Architecture            |
| --------------------------------- | ------------------------------------ |
| Application architecture          | DBMS/data architecture               |
| Presentation                      | External schema                      |
| Application/business logic        | Conceptual schema                    |
| Data layer                        | Internal schema                      |
| Focuses on application deployment | Focuses on data abstraction          |
| Used in client/server systems     | Used to explain database abstraction |

Therefore:

> **3-tier architecture is NOT the same as three-schema architecture.**

Do not memorize:

```text
3-Tier = External + Conceptual + Internal
```

That is **wrong**.

---

# 24. Key Mental Model

Remember the progression:

## 1-Tier

```text
User
 ↓
Application + DB
```

## 2-Tier

```text
Client
 ↓
Database Server
```

## 3-Tier

```text
Client
 ↓
Application Server
 ↓
Database Server
```

And separately:

## Three-Schema Architecture

```text
External
   ↓
Conceptual
   ↓
Internal
```

---

# 25. Interview Questions

## Q1. What is database architecture?

Database architecture defines how users, applications, DBMS components, and databases are organized and how they communicate.

## Q2. What is 2-tier architecture?

A client directly communicates with a database server.

```text
Client → Database Server
```

## Q3. What is 3-tier architecture?

The system separates presentation, application/business logic, and data management.

```text
Client → Application Server → Database Server
```

## Q4. Why is 3-tier architecture preferred for large applications?

Because it provides better:

* Separation of concerns
* Security
* Maintainability
* Scalability
* Centralized business logic

## Q5. Is 3-tier architecture the same as three-schema architecture?

**No.**

3-tier architecture concerns **application/system architecture**.

Three-schema architecture concerns **database abstraction**.

---

# 26. Final Revision Diagram

```text
                 DATABASE ARCHITECTURE
                          │
             ┌────────────┼────────────┐
             ↓            ↓            ↓
          1-Tier        2-Tier       3-Tier
             │            │            │
         App + DB      Client → DB   Client
                                      ↓
                                  Application
                                      ↓
                                   Database
```

And separately:

```text
              THREE-SCHEMA ARCHITECTURE
                         │
                     External
                         ↓
                    Conceptual
                         ↓
                      Internal
```

---

# 27. Final Takeaway

The most important things to remember from **Database Architecture** are:

1. **1-Tier** → Application and database are essentially in the same environment.
2. **2-Tier** → Client directly communicates with the database server.
3. **3-Tier** → Client communicates with an application server, which communicates with the database server.
4. **3-tier architecture improves separation, security, maintainability, and scalability.**
5. **Layer ≠ Tier** — layer focuses on responsibility, while tier focuses on deployment separation.
6. **3-tier architecture ≠ Three-Schema Architecture.**
7. **Three-Schema Architecture** consists of:

   * External Level
   * Conceptual Level
   * Internal Level
