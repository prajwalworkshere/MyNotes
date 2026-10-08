# Data Models in DBMS

# 1. Introduction

Before creating a database, we need a way to describe:

* what data the system will store,
* how different pieces of data are related,
* what rules apply to the data,
* how users and applications will interact with the data.

A **data model** provides a conceptual framework for describing these things.

In simple words:

> **A data model is a collection of concepts used to describe the structure of a database, the relationships among data, and the constraints on that data.**

For example, suppose we are designing a college database.

We may need to represent:

```text
Student
Teacher
Course
Department
Exam
```

and relationships such as:

```text
Student → enrolls in → Course

Teacher → teaches → Course

Student → belongs to → Department
```

A data model gives us a systematic way to represent this information.

---

# 2. Why Do We Need Data Models?

Imagine someone says:

> "Create a database for a college."

That statement is not enough.

We need to determine:

* What information about students should be stored?
* What information about courses should be stored?
* Can a student enroll in multiple courses?
* Can a course have multiple students?
* Which department does a student belong to?
* What identifies a student uniquely?
* What happens if a department is deleted?

These are **data modeling questions**.

A data model helps us answer them before implementing the database.

---

# 3. Real-World Data vs Database Representation

The real world is complicated.

For example:

```text
College
│
├── Departments
│     ├── Computer Science
│     ├── Mechanical
│     └── Electronics
│
├── Students
│
├── Teachers
│
└── Courses
```

A DBMS cannot simply store the entire real world directly.

We create a simplified representation.

```text
Real World
    ↓
Data Model
    ↓
Database Design
    ↓
Database
```

This process is called **data modeling**.

---

# 4. Data Modeling

## Definition

**Data modeling** is the process of identifying data requirements and representing the data, relationships, and constraints using an appropriate data model.

For example:

```text
Real-world requirement:
Students enroll in courses.
```

We model it as:

```text
Student ───── enrolls ───── Course
```

Later, depending on the chosen model, this may become tables such as:

```text
Student
Course
Enrollment
```

---

# 5. Data Model vs Database

These two terms should not be confused.

## Data Model

A data model is a **framework or method for representing data**.

Examples:

* relational model
* hierarchical model
* network model
* ER model
* object-oriented model

## Database

A database is the **actual collection of stored data** created using a particular model.

For example:

```text
Relational Model
      ↓
Student / Course / Enrollment tables
      ↓
Actual database
```

Therefore:

> **Data model = way of organizing/describing data**

> **Database = actual stored collection of data**

---

# 6. Main Components of a Data Model

A data model generally describes three important things:

```text
Data Model
   │
   ├── Data Structure
   │
   ├── Relationships
   │
   └── Constraints
```

Let's understand each.

---

# 7. Data Structure

The model tells us how data is represented.

For example, the relational model represents data using **relations/tables**.

```text
Student
---------------------------
ID | Name  | Department
---------------------------
1  | Rahul | CS
2  | Priya | IT
```

A hierarchical model represents data using a **tree structure**.

```text
University
    |
    +--- Department
            |
            +--- Student
```

Different models organize data differently.

---

# 8. Relationships

A data model can represent relationships between pieces of data.

For example:

```text
Student ─── enrolls in ─── Course
```

A student can enroll in courses.

The relationship itself is part of the data model.

---

# 9. Constraints

A data model can also describe restrictions on data.

For example:

> Every student must have a unique student ID.

or:

> A student cannot enroll in a course that does not exist.

These rules help maintain data correctness.

---

# 10. Classification of Data Models

Data models can be classified in different ways.

A useful classification is:

```text
                         Data Models
                              │
             ┌────────────────┼────────────────┐
             ↓                ↓                ↓
       Conceptual /      Logical /        Physical /
       High-Level        Representational   Low-Level
```

Let's understand these classifications carefully.

---

# PART A — CONCEPTUAL / HIGH-LEVEL DATA MODELS

# 11. Conceptual Data Model

A **conceptual data model** describes the database at a high level, close to how humans understand the real world.

It focuses on:

* entities
* attributes
* relationships
* business rules

It does not focus on physical storage details.

The most important example is:

> **Entity-Relationship (ER) Model**

---

# 12. Example of Conceptual Modeling

Suppose we have:

```text
Student
Course
```

and students enroll in courses.

A conceptual representation can be:

```text
+----------+       enrolls       +----------+
| Student  | ------------------- |  Course  |
+----------+                     +----------+
```

We can then add attributes:

```text
Student
----------------
Student_ID
Name
Date_of_Birth

Course
----------------
Course_ID
Course_Name
Credits
```

This describes the real-world requirements without worrying about physical storage.

---

# 13. Why Conceptual Models Are Useful

Conceptual models are useful because they can be understood by:

* database designers
* developers
* analysts
* business users
* stakeholders

For example, a business manager may not care about:

```text
Disk page 1024
B-tree node
record offset
```

But they can understand:

```text
Customer
Order
Product
Payment
```

and relationships between them.

---

# PART B — LOGICAL / REPRESENTATIONAL DATA MODELS

# 14. Logical Data Model

A **logical data model** describes data in a form that can be implemented by a DBMS, while still hiding many physical storage details.

Common logical/representational models include:

* relational model
* hierarchical model
* network model
* object-oriented model

The exact classification can vary slightly between textbooks, but the important idea is that logical models are more implementation-oriented than conceptual models while still abstracting physical storage.

---

# 15. Relational Data Model

The **relational model** represents data using **relations**, which are commonly implemented as tables.

For example:

```text
Student
+----+--------+------------+
| ID | Name   | Department |
+----+--------+------------+
| 1  | Rahul  | CS         |
| 2  | Priya  | IT         |
| 3  | Aman   | CS         |
+----+--------+------------+
```

Here:

* table → relation
* row → tuple
* column → attribute

The relational model is the foundation of relational DBMSs.

Examples of relational DBMS products include:

* MySQL
* PostgreSQL
* Oracle Database
* Microsoft SQL Server

---

# 16. Hierarchical Data Model

The **hierarchical model** organizes data in a tree-like structure.

The structure generally follows:

```text
Parent
  |
  +--- Child
        |
        +--- Child
```

For example:

```text
University
    |
    +--- Computer Science
    |       |
    |       +--- Student 1
    |       +--- Student 2
    |
    +--- Mechanical
            |
            +--- Student 3
            +--- Student 4
```

---

# 17. Parent-Child Relationship

The hierarchical model is based primarily on parent-child relationships.

For example:

```text
University
    |
    +--- Department
            |
            +--- Student
```

Here:

```text
University → Parent
Department → Child of University
Student → Child of Department
```

A tree has a root at the top.

---

# 18. Characteristics of Hierarchical Model

Important characteristics include:

* tree structure
* root node
* parent-child relationships
* hierarchical navigation
* one-parent-oriented organization

It works especially naturally for data that is inherently hierarchical.

---

# 19. Example: File System

A simple folder structure resembles a hierarchy:

```text
Computer
   |
   +--- Documents
   |       |
   |       +--- SQL
   |       +--- DBMS
   |
   +--- Pictures
           |
           +--- Photos
```

This is conceptually similar to a hierarchical structure.

However, a modern file system is not necessarily a hierarchical DBMS. The analogy is only for understanding the structure.

---

# 20. Advantages of Hierarchical Model

### 1. Simple structure

Tree structures are easy to understand.

### 2. Efficient navigation for hierarchical data

If the relationship naturally follows parent → child paths, navigation can be efficient.

### 3. Good for one-to-many structures

For example:

```text
Department
    ↓
Students
```

---

# 21. Disadvantages of Hierarchical Model

### 1. Difficult many-to-many relationships

Suppose:

```text
Student ↔ Course
```

A student can take multiple courses and a course can have multiple students.

This does not fit naturally into a simple tree.

### 2. Structural rigidity

The data must generally follow the hierarchical organization.

### 3. Data duplication can occur

Representing relationships that do not fit the hierarchy may require duplication.

### 4. Less flexible for complex relationships

Highly interconnected data can be difficult to represent naturally.

---

# PART C — NETWORK DATA MODEL

# 22. Network Data Model

The **network model** extends the idea of hierarchical relationships by allowing a record to have multiple relationships.

Instead of a strict tree:

```text
       A
       |
       B
       |
       C
```

the network model can represent a graph-like structure:

```text
       A
      / \
     B   C
      \ /
       D
```

This allows more complex relationships.

---

# 23. Why Was the Network Model Introduced?

Hierarchical models can become difficult when data has many-to-many relationships.

For example:

```text
Student ↔ Course
```

One student:

```text
Student 1
   ├── Course A
   ├── Course B
   └── Course C
```

One course:

```text
Course A
   ├── Student 1
   ├── Student 2
   └── Student 3
```

This creates a network of relationships rather than a simple tree.

---

# 24. Characteristics of Network Model

The network model:

* represents records as nodes
* represents relationships through links
* supports more complex relationships than a simple hierarchy
* can represent many-to-many relationships
* is more flexible than the hierarchical model

---

# 25. Hierarchical vs Network Model

| Feature      | Hierarchical                            | Network                         |
| ------------ | --------------------------------------- | ------------------------------- |
| Structure    | Tree                                    | Graph/network-like              |
| Parent       | Generally one parent-oriented structure | Multiple relationships possible |
| Many-to-many | Difficult                               | Better supported                |
| Flexibility  | Lower                                   | Higher                          |
| Navigation   | Hierarchical paths                      | Multiple paths                  |

Memory:

```text
Hierarchical → Tree

Network → Graph-like structure
```

---

# PART D — RELATIONAL MODEL

# 26. Relational Model

The **relational model** is one of the most important data models for SQL and DBMS.

It represents data using relations.

In practice, relations are commonly represented as tables.

Example:

```text
Student
+------------+----------+------------+
| Student_ID | Name     | Department |
+------------+----------+------------+
| 101        | Rahul    | CS         |
| 102        | Priya    | IT         |
| 103        | Aman     | CS         |
+------------+----------+------------+
```

---

# 27. Basic Terminology of Relational Model

We will study these concepts in much greater depth later, but they are important here.

### Relation

A table-like structure representing a set of tuples.

### Tuple

A row in a relation.

Example:

```text
101 | Rahul | CS
```

### Attribute

A column.

Example:

```text
Student_ID
Name
Department
```

### Domain

The set of valid values for an attribute.

For example:

```text
Age → valid integer values within defined rules
```

These concepts form the foundation of relational databases.

---

# 28. Why Is the Relational Model So Important?

The relational model became highly influential because it provides:

* a relatively simple tabular representation
* a strong mathematical foundation
* powerful query capabilities
* well-defined integrity constraints
* separation between logical structure and physical implementation
* support for declarative querying through SQL

This is why SQL is primarily associated with relational databases.

---

# 29. Relational Model Example

Suppose we have:

```text
Student
+-----+--------+
| ID  | Name   |
+-----+--------+
| 101 | Rahul  |
| 102 | Priya  |
+-----+--------+
```

and:

```text
Course
+-----+------------+
| ID  | Name       |
+-----+------------+
| C01 | DBMS       |
| C02 | SQL        |
+-----+------------+
```

To represent enrollment, we can use another relation:

```text
Enrollment
+------------+-----------+
| Student_ID | Course_ID |
+------------+-----------+
| 101        | C01       |
| 101        | C02       |
| 102        | C01       |
+------------+-----------+
```

This allows many-to-many relationships to be represented cleanly.

---

# PART E — OBJECT-ORIENTED DATA MODEL

# 30. Object-Oriented Data Model

The **object-oriented data model** represents data using objects, similar to object-oriented programming.

An object can contain:

* data/state
* behavior/methods

For example:

```text
Student Object
--------------------
student_id
name
marks

calculateGrade()
updateMarks()
```

The model attempts to combine data and behavior in objects.

---

# 31. Why Object-Oriented Databases?

Some applications work with complex objects that are not naturally represented as simple tables.

Examples include:

* CAD systems
* engineering applications
* multimedia
* scientific systems
* complex simulations

For example, an engineering application might work with a complex object containing:

```text
3D geometry
Dimensions
Materials
Methods
Relationships
```

Representing such structures directly as objects can be useful.

---

# 32. Important Object-Oriented Concepts

An object-oriented data model may support concepts such as:

### Objects

Represent entities/data objects.

### Classes

Define the structure/behavior of objects.

### Encapsulation

Combines data and operations.

### Inheritance

Allows one class to inherit properties/behavior from another.

### Polymorphism

Allows related objects to respond differently to the same operation.

These concepts come from object-oriented programming and can be incorporated into object-oriented database systems.

---

# PART F — OBJECT-RELATIONAL MODEL

# 33. Object-Relational Data Model

The **object-relational model** combines concepts from:

* relational databases
* object-oriented systems

The goal is to retain the strengths of the relational model while supporting more complex data types and object-like features.

Conceptually:

```text
Relational Model
       +
Object-Oriented Features
       ↓
Object-Relational Model
```

---

# 34. Why Object-Relational Models?

Traditional relational tables are excellent for structured tabular data.

But some applications need more complex types.

For example:

```text
Address
----------------
Street
City
State
Country
```

Instead of treating every component independently, a DBMS may support a more structured type.

This is useful when applications need richer data structures while still benefiting from relational concepts.

---

# PART G — PHYSICAL DATA MODELS

# 35. Physical Data Model

A **physical data model** describes how data is actually represented and stored in a computer system.

It is concerned with implementation details such as:

* files
* pages
* blocks
* indexes
* record formats
* storage structures
* access paths

For example:

```text
Logical:
Student(id, name, marks)

Physical:
Data file
   ↓
Pages
   ↓
Records
   ↓
Index
```

The physical model is much closer to actual storage implementation than a conceptual model.

---

# 36. Logical vs Physical Data Model

### Logical

Focuses on:

```text
What data exists?
How is it logically related?
```

### Physical

Focuses on:

```text
How is the data stored?
How is it accessed efficiently?
```

Therefore:

```text
Logical → Structure and meaning

Physical → Implementation and storage
```

---

# 37. Summary of Major Data Models

```text id="0sgq0q"
                     DATA MODELS
                         │
       ┌─────────────────┼─────────────────┐
       ↓                 ↓                 ↓
 Conceptual          Logical          Physical
       │                 │                 │
       ↓                 ↓                 ↓
      ER       Relational / Hierarchical  Storage
               Network / Object-oriented
```

---

# 38. Comparison of Major Models

| Model             | Basic Structure          | Best Suited For                          |
| ----------------- | ------------------------ | ---------------------------------------- |
| Hierarchical      | Tree                     | Hierarchical data                        |
| Network           | Graph-like links         | Complex interconnected data              |
| Relational        | Tables                   | General structured business data         |
| ER                | Entities + relationships | Conceptual database design               |
| Object-oriented   | Objects                  | Complex object-based applications        |
| Object-relational | Tables + object features | Relational systems needing complex types |

---

# 39. ER Model vs Relational Model

This distinction is extremely important because we will study both later.

## ER Model

Used primarily for:

> **Conceptual database design**

It represents:

```text
Entity
Attribute
Relationship
```

Example:

```text
Student ─── enrolls ─── Course
```

---

## Relational Model

Used for:

> **Logical representation of data in relations/tables**

Example:

```text
Student
Course
Enrollment
```

So:

```text
Real World
    ↓
ER Model
    ↓
Relational Design
    ↓
Tables
```

This is a very common database design workflow.

---

# 40. Data Model and Levels of Abstraction

Connect this topic with the previous **Three-Schema Architecture**.

A useful conceptual relationship is:

```text
Three-Schema Architecture
        │
        ├── External
        ├── Conceptual
        └── Internal
```

while data modeling concerns how data is represented at these conceptual/logical/physical perspectives.

For example:

```text
Real World
    ↓
Conceptual Model
    ↓
Logical Model
    ↓
Physical Model
    ↓
Database Implementation
```

These concepts are related, but they are **not identical**.

Do not treat:

```text
Conceptual data model
```

and:

```text
Conceptual level of three-schema architecture
```

as automatically interchangeable terms.

They are closely related in purpose, but they describe different aspects of database design/architecture.

---

# 41. Database Design Process

Data models become especially useful when designing a database.

A simplified design process is:

```text
Requirements
     ↓
Conceptual Design
     ↓
ER Model
     ↓
Logical Design
     ↓
Relational Model
     ↓
Physical Design
     ↓
Database Implementation
```

Let's understand this.

---

# 42. Step 1 — Requirements

First determine what the system needs.

For example:

> A college needs to store student information, course information, and student enrollments.

---

# 43. Step 2 — Conceptual Design

Identify major entities and relationships:

```text
Student
Course
Enrollment
```

Relationship:

```text
Student ─── enrolls in ─── Course
```

This can be represented using an ER model.

---

# 44. Step 3 — Logical Design

Convert the conceptual design into a logical structure.

For a relational database:

```text
Student
--------------------
Student_ID
Name
Department_ID

Course
--------------------
Course_ID
Course_Name

Enrollment
--------------------
Student_ID
Course_ID
```

---

# 45. Step 4 — Physical Design

Determine implementation details such as:

* indexes
* storage structures
* access paths
* partitioning where appropriate
* physical organization

For example:

```sql
CREATE INDEX idx_student_name
ON Student(name);
```

The logical design may remain the same while physical implementation is optimized.

---

# 46. Step 5 — Implementation

Finally, SQL commands can be used to create the database objects.

For example:

```sql
CREATE TABLE Student (
    student_id INT PRIMARY KEY,
    name VARCHAR(100),
    department_id INT
);
```

The database now has an actual implementation.

---

# 47. Why Data Modeling Is Important

A poorly designed data model can cause:

* duplicated data
* inconsistent data
* difficult queries
* poor performance
* complicated maintenance
* integrity problems
* security problems
* difficulty scaling the system

A good data model provides a solid foundation for the database.

---

# 48. Example: Poor vs Good Modeling

Suppose we store enrollment information like this:

```text
Student_ID | Student_Name | Course1 | Course2 | Course3
```

This design creates problems.

What if a student takes:

```text
5 courses?
```

Do we add:

```text
Course4
Course5
```

What if another student takes 10?

This structure is rigid.

A relational design can instead use:

```text
Student
Course
Enrollment
```

with multiple enrollment rows:

```text
Student_ID | Course_ID
-----------+----------
101        | C01
101        | C02
101        | C03
102        | C01
```

This is more flexible.

The relational design also works naturally with normalization, which we will study later.

---

# 49. Data Model Selection

There is no single data model that is automatically best for every problem.

The choice depends on:

* type of data
* relationships
* application requirements
* query patterns
* scalability
* consistency requirements
* development environment
* performance requirements

For example:

### Traditional business application

A relational model is often a strong choice.

### Highly hierarchical information

A hierarchical representation may be natural.

### Highly interconnected graph data

A graph-oriented model may be more appropriate.

### Complex objects

An object-oriented or object-relational approach may be useful.

---

# 50. Advantages of Data Models

Data models provide:

### 1. Better understanding

They make complex requirements easier to understand.

### 2. Better communication

Developers, analysts, and users can discuss the same structure.

### 3. Better database design

Problems can be identified before implementation.

### 4. Reduced redundancy

A good model can help identify unnecessary duplication.

### 5. Better integrity

Relationships and constraints can be explicitly defined.

### 6. Easier maintenance

A well-structured design is easier to modify.

### 7. Better scalability

Good modeling decisions can help systems grow without becoming unnecessarily complex.

---

# 51. Limitations of Data Modeling

Data modeling also has challenges.

### 1. Real-world complexity

The real world cannot always be represented perfectly.

### 2. Modeling decisions involve trade-offs

A design that is excellent for one workload may be less suitable for another.

### 3. Requirements can change

A model may need modification as business requirements evolve.

### 4. Over-modeling

Making a design unnecessarily complicated can make the database harder to maintain.

### 5. Under-modeling

Oversimplifying the real-world requirements can cause missing information and integrity problems.

Therefore, modeling is about finding an appropriate representation rather than representing every real-world detail.

---

# 52. Important Terminology

| Term                    | Meaning                                                       |
| ----------------------- | ------------------------------------------------------------- |
| Data Model              | Framework for describing data, relationships, and constraints |
| Data Modeling           | Process of designing the representation                       |
| Conceptual Model        | High-level representation close to real-world requirements    |
| Logical Model           | Logical representation suitable for implementation            |
| Physical Model          | Representation of physical storage/implementation             |
| Relational Model        | Represents data using relations/tables                        |
| Hierarchical Model      | Tree-based representation                                     |
| Network Model           | Network/graph-like relationship representation                |
| ER Model                | Entities, attributes, and relationships                       |
| Object-Oriented Model   | Represents data as objects                                    |
| Object-Relational Model | Combines relational and object-oriented features              |

---

# 53. Common Exam and Interview Questions

## Q1. What is a data model?

A data model is a collection of concepts used to describe the structure of a database, relationships among data, and constraints on the data.

---

## Q2. Why is a data model required?

It provides a systematic way to represent real-world information and helps design the database before implementation.

---

## Q3. What are the major types of data models?

Common classifications include:

* conceptual/high-level models
* logical/representational models
* physical/low-level models

Specific models include:

* ER
* relational
* hierarchical
* network
* object-oriented
* object-relational

---

## Q4. What is the relational model?

The relational model represents data using relations, commonly implemented as tables consisting of rows and columns.

---

## Q5. What is the hierarchical model?

It organizes data in a tree-like parent-child structure.

---

## Q6. What is the network model?

It represents data using interconnected records and supports more complex relationships than a strict hierarchy.

---

## Q7. What is an ER model?

The Entity-Relationship model represents real-world entities, their attributes, and relationships between them.

---

## Q8. What is a physical data model?

It describes how data is physically stored and accessed, including storage structures, indexes, files, and related implementation details.

---

## Q9. Difference between conceptual and logical data models?

### Conceptual

Focuses on:

```text
Real-world entities
Relationships
Business requirements
```

### Logical

Focuses on:

```text
Logical data structures
Keys
Relationships
Constraints
Implementation-oriented representation
```

---

## Q10. Which model does SQL primarily work with?

SQL is primarily associated with the **relational model**.

---

# 54. Quick Revision Sheet

## Data Model

```text
Framework for describing:
    ↓
Data
Relationships
Constraints
```

## Main Classification

```text
Data Models
│
├── Conceptual / High-Level
│       └── ER Model
│
├── Logical / Representational
│       ├── Relational
│       ├── Hierarchical
│       ├── Network
│       ├── Object-Oriented
│       └── Object-Relational
│
└── Physical / Low-Level
        └── Storage representation
```

## Main Structures

```text
Hierarchical → Tree

Network → Graph-like relationships

Relational → Tables

ER → Entities + Attributes + Relationships

Object-Oriented → Objects

Physical → Storage structures
```

---

# 55. Most Important Comparison

| Concept               | Main Idea                          |
| --------------------- | ---------------------------------- |
| ER Model              | Model the real world conceptually  |
| Relational Model      | Represent data as relations/tables |
| Hierarchical Model    | Represent data as a tree           |
| Network Model         | Represent interconnected records   |
| Object-Oriented Model | Represent data as objects          |
| Physical Model        | Represent how data is stored       |

---

# 56. Final Mental Model

Start with the real world:

```text
REAL WORLD
    │
    │
    ↓
Requirements
    │
    ↓
Conceptual Model
    │
    │  Entities
    │  Attributes
    │  Relationships
    ↓
Logical Model
    │
    │  Tables
    │  Keys
    │  Constraints
    ↓
Physical Model
    │
    │  Files
    │  Pages
    │  Indexes
    │  Storage
    ↓
DATABASE
```

And remember the most important idea:

> **A data model is the language/framework we use to describe how data is structured, how data items are related, and what rules govern them.**

The different models provide different ways of looking at and organizing data:

```text
ER          → Real-world conceptual design
Relational  → Tables and relations
Hierarchical→ Tree
Network     → Interconnected structure
Object      → Objects
Physical    → Storage implementation
```

For our SQL/DBMS journey, the most important path is:

```text
Real World
    ↓
ER Model
    ↓
Relational Model
    ↓
Tables + Keys + Constraints
    ↓
Relational Algebra
    ↓
SQL
```

This path is especially important because it connects **database design theory** to the SQL you will eventually write.
