# ER Model (Entity–Relationship Model)

## 1. Introduction

The **Entity–Relationship Model**, commonly called the **ER Model**, is a **high-level conceptual data model** used to represent the structure of a database before implementing it using tables.

It was introduced by **Peter Chen in 1976**.

The ER Model helps us answer questions such as:

* What objects or things exist in the system?
* What information do we need to store about those objects?
* How are those objects related to each other?
* What rules exist between those objects?
* Which attributes uniquely identify an object?
* Is participation in a relationship mandatory or optional?

For example, consider a college database.

We may have:

* Students
* Courses
* Teachers
* Departments

And relationships such as:

* A student **enrolls in** a course.
* A teacher **teaches** a course.
* A department **offers** a course.
* A student **belongs to** a department.

The ER Model gives us a way to represent all of this conceptually.

---

# 2. Why Do We Need the ER Model?

Before creating database tables, we first need to understand the **requirements and structure of the real-world system**.

Suppose someone says:

> "Build a database for a college."

This statement is not enough to immediately create tables.

We need to determine:

```text
What information should be stored?
        ↓
What objects exist?
        ↓
What properties do they have?
        ↓
How are they connected?
        ↓
What restrictions apply?
        ↓
How should this become a database?
```

The ER Model helps us perform this conceptual design.

### Example

Suppose a college has students.

A student may have:

```text
Student
---------
Student_ID
Name
Email
Date_of_Birth
Phone
```

A student can enroll in courses:

```text
Student ───── ENROLLS ───── Course
```

A course may have:

```text
Course
---------
Course_ID
Course_Name
Credits
```

This gives us a conceptual understanding before worrying about SQL syntax such as:

```sql
CREATE TABLE Student (...);
```

Therefore:

> **ER modeling is primarily about understanding and designing the data, not writing SQL.**

---

# 3. What Does "Entity–Relationship" Mean?

The name itself contains two important concepts:

### Entity

An **entity** is a distinguishable real-world object or concept about which we want to store information.

Examples:

* Student
* Employee
* Customer
* Product
* Account
* Department
* Book

### Relationship

A **relationship** represents an association between entities.

Examples:

```text
Student ─── enrolls in ─── Course
```

```text
Employee ─── works for ─── Department
```

```text
Customer ─── places ─── Order
```

So:

```text
ENTITY = WHAT EXISTS

RELATIONSHIP = HOW THINGS ARE CONNECTED
```

---

# 4. Main Components of the ER Model

The major concepts are:

1. Entity
2. Entity Set
3. Attribute
4. Relationship
5. Relationship Set
6. Keys
7. Cardinality
8. Participation Constraints
9. Weak Entity
10. Strong Entity

We will study each carefully.

---

# 5. Entity

## Definition

An **entity** is a real-world object or concept that can be uniquely identified and about which data needs to be stored.

Examples:

* A particular student
* A particular employee
* A particular product
* A particular bank account

Consider:

```text
Student
```

This represents the concept of students.

But an individual student such as:

```text
Student_ID = 101
Name = Rahul
```

is a particular **entity instance**.

---

## 5.1 Entity Instance

An **entity instance** is one specific occurrence of an entity.

For example:

```text
Student
```

is an entity type.

These are entity instances:

```text
Student_ID    Name
-------------------
101           Rahul
102           Priya
103           Amit
```

Here:

* Student = entity type
* Rahul = one entity instance
* Priya = another entity instance
* Amit = another entity instance

---

# 6. Entity Type vs Entity Instance

This distinction is extremely important.

### Entity Type

The general definition or structure of an entity.

Example:

```text
STUDENT
```

It tells us:

> "A student exists and we need to store information about students."

### Entity Instance

A specific occurrence.

Example:

```text
Student_ID = 101
Name = Rahul
```

### Analogy

Think about a class in programming.

```text
class Student
```

is similar conceptually to an entity type.

An object such as:

```text
Student s1 = ...
```

is similar conceptually to an entity instance.

---

# 7. Entity Set

An **entity set** is a collection of similar entities of the same type.

For example:

```text
Student Entity Set
```

may contain:

```text
Student 101
Student 102
Student 103
Student 104
```

Similarly:

```text
Employee Entity Set
```

may contain all employees.

### Important terminology

In practical database discussions, people sometimes use **entity**, **entity type**, and **entity set** loosely.

For exams, however, understand the distinction:

```text
Entity Type
     ↓
Definition of a kind of entity

Entity Instance
     ↓
One particular entity

Entity Set
     ↓
Collection of entity instances
```

---

# 8. Attributes

An **attribute** is a property or characteristic that describes an entity.

For a Student:

```text
Student
---------
Student_ID
Name
Email
Phone
Date_of_Birth
```

Here:

* Student_ID → attribute
* Name → attribute
* Email → attribute
* Phone → attribute
* Date_of_Birth → attribute

Attributes provide information about entities.

---

# 9. Types of Attributes

Attributes are commonly classified as:

1. Simple attributes
2. Composite attributes
3. Single-valued attributes
4. Multivalued attributes
5. Derived attributes
6. Stored attributes
7. Key attributes
8. Null/optional attributes

These classifications are important in ER modeling.

---

# 10. Simple Attribute

A **simple attribute** is an attribute that cannot be meaningfully divided into smaller components.

Example:

```text
Age
Gender
Salary
Student_ID
```

For example:

```text
Age = 21
```

We normally do not divide `Age` into smaller meaningful attributes within the ER model.

Therefore:

```text
Age
```

is a simple attribute.

---

# 11. Composite Attribute

A **composite attribute** is an attribute that can be divided into smaller meaningful components.

Consider:

```text
Name
```

Instead of treating it as one indivisible value, we can represent it as:

```text
Name
 ├── First_Name
 ├── Middle_Name
 └── Last_Name
```

Similarly:

```text
Address
 ├── House_No
 ├── Street
 ├── City
 ├── State
 └── PIN_Code
```

Therefore:

```text
Name
```

can be a composite attribute.

---

## Simple vs Composite

### Simple

```text
Age
```

Cannot meaningfully be divided.

### Composite

```text
Address
```

can be divided into:

```text
Street
City
State
PIN
```

---

# 12. Single-Valued Attribute

A **single-valued attribute** has one value for each entity instance.

Example:

```text
Student_ID
```

A student normally has one Student_ID.

For example:

```text
Rahul → Student_ID = 101
```

Another example:

```text
Date_of_Birth
```

A student normally has one date of birth.

---

# 13. Multivalued Attribute

A **multivalued attribute** can have multiple values for one entity instance.

Example:

```text
Phone_Number
```

A person may have:

```text
Phone_Number:
    9876543210
    9123456780
```

Similarly:

```text
Email
```

could theoretically have multiple values.

So:

```text
Student
   |
   └── Phone_Number
          ├── 9876543210
          └── 9123456780
```

is a multivalued attribute.

### Important

A multivalued attribute does **not** mean the attribute contains a comma-separated string such as:

```text
"9876543210, 9123456780"
```

That would generally be poor relational design.

During relational implementation, multivalued attributes are normally represented using a separate relation/table.

We will study that during **ER-to-Relational Mapping**.

---

# 14. Derived Attribute

A **derived attribute** is an attribute whose value can be calculated from other stored information.

Example:

```text
Date_of_Birth
```

can be used to derive:

```text
Age
```

For example:

```text
Date_of_Birth = 2004-05-10
```

Current age can be calculated.

Therefore:

```text
Date_of_Birth → stored attribute

Age → derived attribute
```

---

## Why prefer derived values?

If a value can be calculated, storing it separately may create inconsistency.

Suppose:

```text
Date_of_Birth = 2004-05-10
Age = 21
```

After a birthday, if we forget to update `Age`, the database can contain incorrect information.

Therefore, in many designs, the birth date is stored and age is calculated when required.

---

# 15. Stored Attribute

A **stored attribute** is an attribute whose value is directly stored.

Example:

```text
Date_of_Birth
```

is stored.

From it:

```text
Age
```

can be derived.

So:

```text
Stored → Date_of_Birth

Derived → Age
```

Stored and derived attributes are closely related concepts.

---

# 16. Key Attribute

A **key attribute** is an attribute that can uniquely identify an entity within an entity set.

For example:

```text
Student
---------
Student_ID
Name
Email
```

If `Student_ID` uniquely identifies every student:

```text
101 → Rahul
102 → Priya
103 → Amit
```

then:

```text
Student_ID
```

is a key attribute.

In an ER diagram, a key attribute is traditionally shown with an **underline**.

Conceptually:

```text
Student
   |
   ├── Student_ID   ← key
   ├── Name
   └── Email
```

Later, this concept connects directly to **primary keys** in the relational model.

---

# 17. Null or Optional Attribute

Some attributes may not have a value for every entity.

For example:

```text
Student
---------
Student_ID
Name
Phone
```

A student may not have a phone number recorded.

Therefore:

```text
Phone = NULL
```

may be possible.

This means the attribute is optional in that context.

### Important distinction

`NULL` does not necessarily mean:

* zero
* empty string
* false
* "not applicable"

`NULL` represents missing/unknown/not available information depending on the context.

This becomes especially important in SQL and relational databases.

---

# 18. Attribute Classification Example

Consider:

```text
EMPLOYEE
```

with:

```text
Employee_ID
Name
Address
Phone
Date_of_Birth
Age
Salary
```

Possible classification:

| Attribute     | Type                                        |
| ------------- | ------------------------------------------- |
| Employee_ID   | Key                                         |
| Name          | Composite                                   |
| Address       | Composite                                   |
| Phone         | Multivalued, if multiple phones are allowed |
| Date_of_Birth | Stored                                      |
| Age           | Derived                                     |
| Salary        | Simple                                      |

Remember that classification depends partly on the business requirements.

For example, `Phone` could be modeled as single-valued if the system allows only one phone number.

---

# 19. Relationships

A **relationship** represents an association between two or more entities.

Example:

```text
Student ─── enrolls in ─── Course
```

Here:

```text
Student
```

is an entity.

```text
Course
```

is an entity.

```text
Enrolls
```

is the relationship.

Another example:

```text
Employee ─── works for ─── Department
```

The relationship is:

```text
works_for
```

---

# 20. Why Are Relationships Necessary?

Suppose we only have:

```text
STUDENT

COURSE
```

We know students exist and courses exist.

But we don't know:

> Which student is taking which course?

We need a relationship:

```text
Student ─── ENROLLS ─── Course
```

Now we can represent:

```text
Rahul → DBMS
Priya → Operating Systems
Amit → DBMS
```

Therefore, relationships represent the connections between entities.

---

# 21. Relationship Instance

A relationship instance is a particular association between specific entity instances.

Suppose:

```text
Student 101 = Rahul
Course C01 = DBMS
```

If Rahul enrolls in DBMS:

```text
(Rahul, DBMS)
```

is one relationship instance of `ENROLLS`.

If Priya also enrolls:

```text
(Priya, DBMS)
```

is another relationship instance.

---

# 22. Relationship Set

A **relationship set** is a collection of similar relationship instances.

For example:

```text
ENROLLS
```

may contain:

```text
(Rahul, DBMS)
(Rahul, OS)
(Priya, DBMS)
(Amit, CN)
```

Together these form the relationship set.

---

# 23. Degree of a Relationship

The **degree** of a relationship is the number of entity types participating in the relationship.

Common types:

1. Unary relationship
2. Binary relationship
3. Ternary relationship
4. Higher-degree relationship

---

# 24. Unary Relationship

A **unary relationship** involves one entity type.

It is also called a **recursive relationship**.

Example:

```text
Employee
   |
   └── supervises
          |
       Employee
```

One employee supervises another employee.

Although only one entity type exists:

```text
EMPLOYEE
```

the entity participates in the relationship in different roles.

Example:

```text
Manager ─── supervises ─── Employee
```

Both are employees.

---

# 25. Binary Relationship

A **binary relationship** involves two entity types.

This is the most common type.

Example:

```text
Student ─── enrolls ─── Course
```

Two entity types participate:

```text
Student
Course
```

Therefore:

```text
Degree = 2
```

---

# 26. Ternary Relationship

A **ternary relationship** involves three entity types.

Example:

```text
Supplier
    \
     \
     SUPPLIES
     /      \
 Product   Project
```

Conceptually:

```text
Supplier ─── Supplies ─── Product
                  |
                Project
```

The relationship involves:

```text
Supplier
Product
Project
```

Therefore its degree is:

```text
3
```

---

# 27. Higher-Degree Relationship

A relationship involving more than three entity types is a higher-degree relationship.

For example:

```text
A ─┐
B ─┼── Relationship
C ─┤
D ─┘
```

Degree = 4.

However, higher-degree relationships are less common in basic database designs.

---

# 28. Roles in Relationships

An entity type may participate in a relationship in different **roles**.

This is particularly important in recursive relationships.

Example:

```text
Employee supervises Employee
```

We need to distinguish:

```text
Supervisor
      ↓
Employee

Subordinate
      ↓
Employee
```

Both are instances of the same entity type:

```text
Employee
```

but they have different roles.

---

# 29. Cardinality

One of the most important ER concepts is **cardinality**.

Cardinality specifies how many entities of one entity type can or must be associated with entities of another entity type.

Common cardinalities are:

1. One-to-One (1:1)
2. One-to-Many (1:N)
3. Many-to-One (N:1)
4. Many-to-Many (M:N)

---

# 30. One-to-One Relationship (1:1)

In a 1:1 relationship:

> One entity of A can be associated with at most one entity of B, and one entity of B can be associated with at most one entity of A.

Example:

```text
Person ─── has ─── Passport
```

Assume the business rule says:

* One person has at most one passport.
* One passport belongs to at most one person.

Then:

```text
Person 1 ───── 1 Passport
```

### Example

```text
Person 101 → Passport P1001
Person 102 → Passport P1002
```

A passport cannot belong to two people under this rule.

---

# 31. One-to-Many Relationship (1:N)

In a 1:N relationship:

> One entity of A can be associated with many entities of B, but each B entity is associated with at most one A entity.

Example:

```text
Department ─── employs ─── Employee
```

One department can have many employees:

```text
Department D1
   ├── Employee E1
   ├── Employee E2
   └── Employee E3
```

But each employee belongs to one department under this business rule.

Therefore:

```text
Department 1 ───── N Employee
```

---

# 32. Many-to-One Relationship (N:1)

This is the reverse perspective of 1:N.

Example:

```text
Employee ─── works for ─── Department
```

Many employees can work for one department.

Therefore:

```text
Employee N ───── 1 Department
```

The same relationship can be described as:

```text
Department 1 : N Employee
```

or:

```text
Employee N : 1 Department
```

They describe the same relationship from opposite directions.

---

# 33. Many-to-Many Relationship (M:N)

In an M:N relationship:

> Many entities of A can be associated with many entities of B.

Example:

```text
Student ─── enrolls in ─── Course
```

One student can enroll in many courses:

```text
Rahul → DBMS
Rahul → OS
Rahul → CN
```

One course can have many students:

```text
DBMS → Rahul
DBMS → Priya
DBMS → Amit
```

Therefore:

```text
Student M ───── N Course
```

This is a classic many-to-many relationship.

---

# 34. Cardinality Example

Suppose:

```text
DEPARTMENT
-----------
D1
D2
D3
```

and:

```text
EMPLOYEE
-----------
E1
E2
E3
E4
E5
```

Suppose:

```text
D1 → E1
D1 → E2
D1 → E3

D2 → E4
D2 → E5
```

One department has many employees.

Therefore:

```text
Department 1 : N Employee
```

---

# 35. Cardinality vs Degree

These two concepts are frequently confused.

## Degree

Degree tells us:

> How many entity types participate in the relationship?

Example:

```text
Student ─── Enrolls ─── Course
```

Two entity types:

```text
Degree = 2
```

## Cardinality

Cardinality tells us:

> How many instances can participate in the relationship?

Example:

```text
Student M : N Course
```

Therefore:

```text
Degree = 2
Cardinality = M:N
```

### Remember

```text
DEGREE → number of ENTITY TYPES

CARDINALITY → number of ENTITY INSTANCES that can be associated
```

---

# 36. Participation Constraint

Cardinality tells us the maximum number of relationships.

Participation tells us whether participation is **mandatory or optional**.

There are two major types:

1. Total participation
2. Partial participation

---

# 37. Total Participation

An entity set has **total participation** in a relationship if every entity in that set must participate in at least one relationship instance.

Example:

Suppose every employee must belong to a department.

Then:

```text
Employee
   |
   | must participate
   ↓
Works_For
   ↑
   |
Department
```

Every employee must have a department.

Therefore:

```text
Employee → Total Participation
```

In traditional ER notation, total participation is represented using a **double line** between the entity set and relationship.

---

# 38. Partial Participation

An entity set has **partial participation** if participation is optional.

Example:

Suppose not every employee manages a department.

Some employees are managers:

```text
Employee A → Manager
Employee B → Manager
```

while others are not.

Therefore:

```text
Employee → Partial Participation
```

Traditionally, partial participation is represented using a **single line**.

---

# 39. Cardinality vs Participation

This distinction is extremely important.

Suppose:

```text
Employee ─── Works_For ─── Department
```

We may have:

```text
Employee → N
Department → 1
```

This tells us the maximum relationship:

```text
Employee N : 1 Department
```

But it does not by itself fully tell us whether every employee must belong to a department.

That is participation.

So:

```text
Cardinality → maximum relationship count

Participation → whether participation is mandatory or optional
```

---

# 40. Min-Max Representation

Another way to express cardinality and participation is using **minimum and maximum cardinalities**.

We can represent participation as:

```text
(min, max)
```

Examples:

```text
(0,1)
```

means:

* minimum = 0
* maximum = 1

Therefore participation is optional and at most one relationship is allowed.

```text
(1,1)
```

means:

* minimum = 1
* maximum = 1

Therefore exactly one relationship is required.

```text
(0,N)
```

means:

* minimum = 0
* maximum = many

```text
(1,N)
```

means:

* minimum = 1
* maximum = many

This notation can express constraints very precisely.

---

# 41. Strong Entity

A **strong entity** is an entity that has its own key attribute and can be uniquely identified independently.

Example:

```text
STUDENT
---------
Student_ID  ← key
Name
Email
```

`Student_ID` uniquely identifies the student.

Therefore:

```text
Student = Strong Entity
```

A strong entity does not depend on another entity for its own identification.

---

# 42. Weak Entity

A **weak entity** is an entity that cannot be uniquely identified by its own attributes alone.

It depends on another entity, called the **owner/identifying entity**, for identification.

Example:

```text
Employee
---------
Employee_ID
Name
```

Suppose each employee can have dependents:

```text
Dependent
---------
Dependent_Name
Age
Relationship
```

Suppose `Dependent_Name` is not globally unique.

For example:

```text
Employee 101 → Child Rahul
Employee 102 → Child Rahul
```

`Rahul` alone cannot uniquely identify the dependent.

We may identify the dependent using:

```text
Employee_ID + Dependent_Name
```

Therefore:

```text
Employee
   ↓
identifies
   ↓
Dependent
```

`Dependent` can be modeled as a weak entity.

---

# 43. Why Is It Called "Weak"?

It is called weak because the entity does not have a complete key of its own that uniquely identifies its instances independently.

Its identification depends on an owner entity.

Conceptually:

```text
Strong Entity
     |
     | identifying relationship
     ↓
Weak Entity
```

---

# 44. Partial Key of a Weak Entity

A weak entity may have a **partial key**, also called a **discriminator**.

The partial key distinguishes weak entities belonging to the same owner.

Example:

```text
Employee_ID + Dependent_Name
```

Suppose:

```text
Employee 101 → Rahul
Employee 101 → Priya
Employee 102 → Rahul
```

Within Employee 101:

```text
Rahul
Priya
```

can distinguish the dependents.

But globally:

```text
Rahul
```

is not unique.

Therefore:

```text
Dependent_Name = partial key
```

and:

```text
Employee_ID + Dependent_Name = complete identification
```

---

# 45. Identifying Relationship

The relationship through which a weak entity is associated with its owner entity is called an **identifying relationship**.

Example:

```text
Employee ─── HAS ─── Dependent
```

where `Dependent` is weak.

The relationship helps identify the weak entity using the owner's key.

---

# 46. Strong Entity vs Weak Entity

| Strong Entity                                   | Weak Entity                             |
| ----------------------------------------------- | --------------------------------------- |
| Has its own identifying key                     | Does not have a complete key of its own |
| Independent identification                      | Depends on owner entity                 |
| Can exist independently in identification terms | Identification depends on owner         |
| Example: Student                                | Example: Dependent                      |
| Has primary identifying attribute(s)            | Has partial key/discriminator           |

### Important

"Dependent" is only an example.

Whether something is weak depends on the **business rules and identification requirements**, not merely on the object's name.

---

# 47. Relationship Attributes

A relationship can itself have attributes.

This is an important concept.

Consider:

```text
Student ─── Enrolls ─── Course
```

Suppose we need to store:

```text
Enrollment_Date
Grade
```

Where do these attributes belong?

They describe the **enrollment**, not the student and not the course.

For example:

```text
Student:
Student_ID
Name

Course:
Course_ID
Course_Name

Enrolls:
Enrollment_Date
Grade
```

Therefore:

```text
Student ─── Enrolls ─── Course
                 |
                 ├── Enrollment_Date
                 └── Grade
```

These are **relationship attributes**.

---

# 48. Why Can't Grade Be a Student Attribute?

Suppose:

```text
Rahul
```

takes:

```text
DBMS
Operating Systems
Computer Networks
```

Rahul may receive:

```text
DBMS → A
OS → B+
CN → A+
```

So Rahul does not have one single grade.

The grade depends on:

```text
Student + Course
```

Therefore:

```text
Grade
```

belongs to the relationship:

```text
Enrolls(Student, Course)
```

rather than simply to Student.

This is a very important modeling principle:

> An attribute should be associated with the entity or relationship that it actually describes.

---

# 49. Relationship Attributes and Many-to-Many Relationships

Relationship attributes are especially common with M:N relationships.

Example:

```text
Student M ─── Enrolls ─── N Course
```

Relationship attributes:

```text
Enrollment_Date
Grade
Semester
```

Conceptually:

```text
Student
   |
   |
 Enrolls
   |
   ├── Enrollment_Date
   ├── Grade
   └── Semester
   |
   |
Course
```

Later, during relational mapping, this often becomes a separate relation such as:

```text
ENROLLMENT
-----------
Student_ID
Course_ID
Enrollment_Date
Grade
Semester
```

---

# 50. Recursive Relationship

A recursive relationship occurs when an entity type is related to itself.

Example:

```text
Employee ─── supervises ─── Employee
```

The entity type appears on both sides.

But the roles are different:

```text
Supervisor
    ↓
Employee

Subordinate
    ↓
Employee
```

Another example:

```text
Person ─── married_to ─── Person
```

The same entity type participates twice.

---

# 51. Relationship Constraints

A relationship may have rules such as:

```text
One employee must belong to exactly one department.
```

or:

```text
A department may have zero or many employees.
```

or:

```text
Every student must enroll in at least one course.
```

or:

```text
A course can have many students.
```

These rules are called **constraints**.

A good ER design should capture important business constraints.

---

# 52. Business Rules

ER modeling begins with understanding **business rules**.

A business rule is a rule describing how the real-world system works.

Examples:

### Rule 1

> Every employee belongs to exactly one department.

This suggests:

```text
Employee → Department
```

with appropriate cardinality and participation.

### Rule 2

> A department can have many employees.

This gives:

```text
Department 1 : N Employee
```

### Rule 3

> A student can enroll in multiple courses.

This contributes to:

```text
Student M : N Course
```

### Rule 4

> A course can be taken by multiple students.

Again:

```text
Student M : N Course
```

Business rules are therefore the foundation of correct ER modeling.

---

# 53. Example: College ER Model

Let's build a conceptual model step by step.

Suppose the requirements are:

1. A college has students.
2. A student has an ID, name, and email.
3. A college offers courses.
4. A course has an ID, name, and credits.
5. A student can enroll in multiple courses.
6. A course can have multiple students.
7. Each enrollment stores enrollment date and grade.

---

## Step 1: Identify Entities

From the requirements:

```text
Student
Course
```

are entities.

---

## Step 2: Identify Attributes

Student:

```text
Student_ID
Name
Email
```

Course:

```text
Course_ID
Course_Name
Credits
```

---

## Step 3: Identify Relationship

Students enroll in courses:

```text
Student ─── Enrolls ─── Course
```

---

## Step 4: Determine Cardinality

A student can enroll in many courses.

A course can have many students.

Therefore:

```text
Student M : N Course
```

---

## Step 5: Identify Relationship Attributes

Enrollment contains:

```text
Enrollment_Date
Grade
```

Therefore:

```text
Student
   |
   |
 Enrolls
   |
   ├── Enrollment_Date
   └── Grade
   |
   |
Course
```

This is a conceptual ER design.

---

# 54. Another Example: Banking System

Suppose we are designing a bank database.

Possible entities:

```text
Customer
Account
Branch
Loan
```

Possible relationships:

```text
Customer ─── owns ─── Account

Customer ─── takes ─── Loan

Branch ─── maintains ─── Account
```

Possible attributes:

### Customer

```text
Customer_ID
Name
Address
Phone
```

### Account

```text
Account_Number
Account_Type
Balance
```

### Branch

```text
Branch_ID
Branch_Name
Location
```

### Loan

```text
Loan_ID
Amount
Loan_Type
```

Then we determine:

* cardinality
* participation
* keys
* relationship attributes
* possible weak entities

based on the actual business rules.

---

# 55. ER Diagram Notation

An **ER diagram (ERD)** is the graphical representation of an ER model.

The ER model is the conceptual modeling approach.

The ER diagram is the visual representation.

Traditional Chen notation commonly uses:

```text
Entity       → Rectangle

Relationship → Diamond

Attribute    → Oval

Key          → Underlined attribute

Multivalued  → Double oval

Derived      → Dashed oval

Weak Entity  → Double rectangle

Identifying
Relationship → Double diamond
```

These symbols are important for exams.

---

# 56. Basic ER Diagram Example

Conceptually:

```text
       (Student_ID)
            |
         [STUDENT]
            |
            |
        <ENROLLS>
            |
            |
         [COURSE]
            |
       (Course_ID)
```

Where:

```text
[STUDENT] → Entity
<ENROLLS> → Relationship
(Course_ID) → Attribute
```

Traditional Chen notation uses rectangles, diamonds, and ovals rather than the ASCII notation above.

---

# 57. Attribute Notation

Traditional Chen notation:

### Simple attribute

```text
       (Age)
         |
      [STUDENT]
```

### Key attribute

The attribute is underlined:

```text
(Student_ID)
```

### Composite attribute

```text
             (Name)
            /      \
     (First)      (Last)
```

### Multivalued attribute

Represented using a **double oval**.

### Derived attribute

Represented using a **dashed oval**.

---

# 58. Entity vs Attribute

This is a common modeling confusion.

Consider:

```text
Student
```

and:

```text
Student_Name
```

Student is an entity.

Student_Name is an attribute.

Why?

Because:

```text
Student
```

represents an object/concept whose information we store.

Whereas:

```text
Student_Name
```

describes that object.

---

# 59. Entity vs Attribute Depends on Context

The same real-world concept can sometimes be modeled differently depending on requirements.

For example:

```text
Address
```

could be an attribute of Customer:

```text
Customer
   |
   └── Address
```

But if the system needs to store detailed information about addresses and associate multiple customers or properties with addresses, `Address` might become an entity.

Therefore:

> Whether something is an entity or attribute depends on the information requirements and business rules.

There is no universal rule saying a particular noun must always be an entity.

---

# 60. Entity vs Relationship

Consider:

```text
Student
Course
Enrolls
```

Here:

```text
Student → Entity
Course → Entity
Enrolls → Relationship
```

Why isn't `Enrolls` an entity?

Because it represents the association between student and course.

However, when converting an M:N relationship into the relational model, the relationship may become a separate relation/table.

This is why conceptual modeling and relational implementation should not be confused.

---

# 61. Attribute vs Relationship

Consider:

```text
Employee ─── works_for ─── Department
```

`works_for` is a relationship because it connects two entities.

But:

```text
Employee
   |
   └── Salary
```

`Salary` is an attribute because it describes the employee.

---

# 62. Relationship Type vs Relationship Instance

### Relationship Type

The general definition:

```text
ENROLLS
```

### Relationship Instance

A specific occurrence:

```text
(Rahul, DBMS)
```

Therefore:

```text
Relationship Type
       ↓
General definition

Relationship Instance
       ↓
Specific association
```

---

# 63. ER Model vs ER Diagram

These are related but not identical.

### ER Model

The conceptual modeling framework containing:

* entities
* attributes
* relationships
* constraints
* keys
* participation
* cardinality

### ER Diagram

The graphical representation of that model.

So:

```text
ER Model
   ↓
Conceptual structure

ER Diagram
   ↓
Visual representation
```

---

# 64. ER Model vs Relational Model

These are also different.

## ER Model

Used primarily for conceptual database design.

It represents:

```text
Entities
Attributes
Relationships
Constraints
```

## Relational Model

Represents data using:

```text
Relations (tables)
Tuples (rows)
Attributes (columns)
Keys
Constraints
```

Example:

### ER concept

```text
Student ─── Enrolls ─── Course
```

### Relational implementation

```text
STUDENT
----------------
Student_ID
Name
Email

COURSE
----------------
Course_ID
Course_Name

ENROLLMENT
----------------
Student_ID
Course_ID
Grade
```

ER modeling usually comes before relational implementation.

---

# 65. ER Model and Database Design Process

A simplified database design process can be thought of as:

```text
Real World
    ↓
Requirements
    ↓
Business Rules
    ↓
ER Model
    ↓
ER Diagram
    ↓
Relational Model
    ↓
Tables
    ↓
SQL Implementation
```

For example:

```text
College requirements
        ↓
Identify Student and Course
        ↓
Create Enrolls relationship
        ↓
Determine M:N
        ↓
Add enrollment attributes
        ↓
Map into relations
        ↓
Create SQL tables
```

This is why ER modeling is an important bridge between requirements and SQL.

---

# 66. Important ER Modeling Questions

Whenever you are given a problem and asked to create an ER model, follow this process.

## Step 1 — Find the entities

Ask:

> What objects/concepts need to be stored?

Example:

```text
Student
Teacher
Course
Department
```

---

## Step 2 — Find attributes

Ask:

> What information describes each entity?

For Student:

```text
Student_ID
Name
Email
DOB
```

---

## Step 3 — Find candidate keys

Ask:

> What uniquely identifies each entity?

Example:

```text
Student_ID
```

---

## Step 4 — Find relationships

Ask:

> How are the entities connected?

Example:

```text
Student → enrolls → Course
```

---

## Step 5 — Determine relationship degree

Ask:

> How many entity types participate?

```text
Student + Course
```

Therefore:

```text
Binary
```

---

## Step 6 — Determine cardinality

Ask:

> How many instances can be associated?

Example:

```text
Student M : N Course
```

---

## Step 7 — Determine participation

Ask:

> Is participation mandatory or optional?

Example:

> Every employee must belong to a department.

Then employee participation is total.

---

## Step 8 — Find relationship attributes

Ask:

> Does the relationship itself have information?

Example:

```text
Enrollment_Date
Grade
```

---

## Step 9 — Check for weak entities

Ask:

> Does any entity depend on another entity for identification?

Example:

```text
Employee → Dependent
```

---

# 67. Common ER Modeling Mistakes

## Mistake 1: Making every noun an entity

Suppose:

> "Each student has a name."

The nouns are:

```text
Student
Name
```

But:

```text
Student → Entity
Name → Attribute
```

Not every noun becomes an entity.

---

## Mistake 2: Making every verb a relationship without analysis

A verb can suggest a relationship, but the actual business meaning must be examined.

Example:

> Employee receives salary.

Depending on the system, `Salary` may simply be an attribute:

```text
Employee
   |
   └── Salary
```

It doesn't automatically mean there must be a `Receives` relationship.

---

## Mistake 3: Putting relationship attributes on entities

Example:

```text
Student ─── Enrolls ─── Course
```

If:

```text
Grade
```

depends on the student-course enrollment, it should not simply be stored as a Student attribute.

Correct conceptual placement:

```text
Enrolls
  |
  └── Grade
```

---

## Mistake 4: Confusing degree with cardinality

Incorrect thinking:

> "M:N means degree 2."

No.

`M:N` is cardinality.

A relationship between Student and Course has:

```text
Degree = 2
Cardinality = M:N
```

---

## Mistake 5: Assuming every relationship is binary

Many relationships are binary, but unary and ternary relationships also exist.

Examples:

```text
Employee ─── supervises ─── Employee
```

is unary.

```text
Supplier ─── Supplies ─── Product ─── Project
```

can represent a ternary relationship involving three entity types.

---

# 68. ER Modeling Example — University

Let's build a slightly larger example.

### Requirements

A university has departments.

Each department offers many courses.

Students belong to departments.

Students enroll in courses.

Teachers teach courses.

A student can enroll in many courses.

A course can have many students.

An enrollment has:

* enrollment date
* grade

---

## Entities

```text
Department
Student
Course
Teacher
```

---

## Relationships

```text
Department ─── offers ─── Course

Student ─── belongs_to ─── Department

Student ─── enrolls ─── Course

Teacher ─── teaches ─── Course
```

---

## Cardinalities

Possible business rules:

```text
Department 1 : N Course

Department 1 : N Student

Student M : N Course

Teacher 1 : N Course
```

But the exact cardinalities must come from the actual requirements.

For example, if a course can have multiple teachers, then:

```text
Teacher M : N Course
```

could be appropriate.

This demonstrates an important principle:

> Never choose cardinality based merely on what "usually happens." Derive it from the stated business rules.

---

# 69. Important Principle: ER Model Represents Meaning

An ER model is not simply a collection of boxes and diamonds.

Its purpose is to capture the **meaning of the real-world system**.

A technically valid-looking diagram can still be a bad design if it does not correctly represent the business rules.

For example, suppose:

> A student can enroll in multiple sections of the same course across different semesters.

A simplistic:

```text
Student M:N Course
```

may not capture all the required information.

We may need concepts such as:

```text
Course
Section
Semester
Enrollment
```

The correct model depends on the actual requirements.

---

# 70. ER Model and Constraints

A good ER model captures important constraints such as:

### Uniqueness

```text
Student_ID must uniquely identify a student.
```

### Cardinality

```text
One department can have many employees.
```

### Mandatory participation

```text
Every employee must belong to a department.
```

### Optional participation

```text
An employee may or may not manage a department.
```

### Identification dependency

```text
A dependent requires an employee for identification.
```

These constraints make the model meaningful.

---

# 71. ER Model: Complete Conceptual Picture

Think of the ER model as:

```text
                    ER MODEL
                       |
       ┌───────────────┼────────────────┐
       ↓               ↓                ↓
    ENTITIES       ATTRIBUTES       RELATIONSHIPS
       |               |                |
   Student         Student_ID        Enrolls
   Course          Name              Works_For
   Employee        Email             Teaches
       |               |                |
       └───────────────┼────────────────┘
                       ↓
                  CONSTRAINTS
                       |
          ┌────────────┼────────────┐
          ↓            ↓            ↓
      Cardinality  Participation   Keys
```

This is the core of ER modeling.

---

# 72. Quick Comparison of Important Terms

| Term                   | Meaning                                           |
| ---------------------- | ------------------------------------------------- |
| Entity                 | Real-world object/concept                         |
| Entity Type            | Definition of a kind of entity                    |
| Entity Instance        | One specific entity                               |
| Entity Set             | Collection of entity instances                    |
| Attribute              | Property of an entity                             |
| Key Attribute          | Attribute that uniquely identifies an entity      |
| Relationship           | Association between entities                      |
| Relationship Type      | General definition of an association              |
| Relationship Instance  | One specific association                          |
| Relationship Set       | Collection of relationship instances              |
| Degree                 | Number of participating entity types              |
| Cardinality            | Number of entity instances that can participate   |
| Participation          | Whether participation is mandatory/optional       |
| Strong Entity          | Independently identifiable entity                 |
| Weak Entity            | Entity dependent on another for identification    |
| Partial Key            | Attribute that partially identifies a weak entity |
| Relationship Attribute | Attribute describing a relationship               |

---

# 73. Most Important Differences

## Entity vs Entity Instance

```text
Entity Type → Student

Entity Instance → Student 101
```

---

## Entity vs Attribute

```text
Student → Entity

Student_Name → Attribute
```

---

## Attribute vs Relationship

```text
Salary → Attribute of Employee

Works_For → Relationship between Employee and Department
```

---

## Degree vs Cardinality

```text
Degree → number of entity types

Cardinality → number of entity instances associated
```

---

## Cardinality vs Participation

```text
Cardinality → how many

Participation → whether participation is required
```

---

## Strong vs Weak Entity

```text
Strong → independently identifiable

Weak → identification depends on owner entity
```

---

# 74. Important Exam Questions

## Q1. What is an ER Model?

The ER Model is a high-level conceptual data model used to represent entities, their attributes, relationships, and constraints in a real-world system.

---

## Q2. What is an entity?

An entity is a distinguishable real-world object or concept about which information is stored.

---

## Q3. What is an attribute?

An attribute is a property or characteristic describing an entity.

---

## Q4. What is a relationship?

A relationship represents an association between entities.

---

## Q5. What is cardinality?

Cardinality specifies how many instances of one entity can be associated with instances of another entity.

Common types are:

```text
1:1
1:N
N:1
M:N
```

---

## Q6. What is participation?

Participation specifies whether entities are required to participate in a relationship.

It may be:

```text
Total
Partial
```

---

## Q7. What is a weak entity?

A weak entity is an entity that cannot be uniquely identified using its own attributes alone and depends on an owner entity for identification.

---

## Q8. What is a relationship attribute?

An attribute that describes the association between entities rather than one entity alone.

Example:

```text
Grade
Enrollment_Date
```

for:

```text
Student ─── Enrolls ─── Course
```

---

## Q9. What is a recursive relationship?

A recursive relationship occurs when an entity type participates in a relationship with itself.

Example:

```text
Employee ─── supervises ─── Employee
```

---

## Q10. What is the difference between ER Model and ER Diagram?

The ER Model is the conceptual modeling framework; an ER Diagram is its graphical representation.

---

# 75. Interview-Level Understanding

### Question:

> Why do we use an ER model before creating tables?

Because the ER model helps us understand the real-world requirements and identify entities, attributes, relationships, keys, cardinalities, and participation constraints before implementing the database using relations/tables.

---

### Question:

> Can an attribute become an entity?

Yes.

Whether something is an attribute or an entity depends on the requirements.

For example, `Address` may initially be an attribute of Customer, but if the system needs to maintain independent information about addresses, it may be modeled as an entity.

---

### Question:

> Can a relationship have attributes?

Yes.

For example:

```text
Student ─── Enrolls ─── Course
```

may have:

```text
Grade
Enrollment_Date
Semester
```

as relationship attributes.

---

### Question:

> Why are M:N relationships important?

Because many real-world relationships are naturally many-to-many.

For example:

```text
Students ↔ Courses
```

A student can take many courses and a course can have many students.

When implementing this in a relational database, the M:N relationship is generally transformed into an associative/intermediate relation.

---

# 76. A Complete Mental Model

When you see a database problem, think like this:

```text
REAL WORLD
    ↓
"What things exist?"
    ↓
ENTITIES
    ↓
"What describes them?"
    ↓
ATTRIBUTES
    ↓
"What uniquely identifies them?"
    ↓
KEYS
    ↓
"How are they connected?"
    ↓
RELATIONSHIPS
    ↓
"How many can be connected?"
    ↓
CARDINALITY
    ↓
"Is participation mandatory?"
    ↓
PARTICIPATION
    ↓
"Does the relationship have its own data?"
    ↓
RELATIONSHIP ATTRIBUTES
    ↓
"Does any entity depend on another for identification?"
    ↓
WEAK ENTITY
```

This is the fundamental thinking process behind ER modeling.

---

# 77. Final Revision

## ER Model

```text
High-level conceptual model
```

### Main concepts

```text
Entity
Attribute
Relationship
Key
Cardinality
Participation
Strong Entity
Weak Entity
```

### Entity examples

```text
Student
Employee
Customer
Product
```

### Attribute examples

```text
Name
Email
Salary
Date_of_Birth
```

### Relationship examples

```text
Student ─── Enrolls ─── Course

Employee ─── Works_For ─── Department
```

### Cardinalities

```text
1 : 1
1 : N
N : 1
M : N
```

### Participation

```text
Total
Partial
```

### Attribute types

```text
Simple
Composite
Single-valued
Multivalued
Stored
Derived
Key
Optional
```

### Relationship degree

```text
Unary
Binary
Ternary
Higher-degree
```

### Weak entity

```text
Cannot be completely identified independently
        ↓
Depends on owner entity
        ↓
Uses partial key + owner key
```

---

# 78. One-Page ER Model Cheat Sheet

```text
                    ER MODEL
                       │
       ┌───────────────┼────────────────┐
       │               │                │
    ENTITY         ATTRIBUTE        RELATIONSHIP
       │               │                │
   Student           Name             Enrolls
   Employee          Email            Works_For
   Course            Salary           Teaches
       │               │                │
       │         ┌─────┼─────┐          │
       │         │     │     │          │
       │       Simple Composite      Degree
       │       Single  Multi         Unary
       │       Derived Stored        Binary
       │                              Ternary
       │
       ├── Strong Entity
       │
       └── Weak Entity
       
RELATIONSHIP CONSTRAINTS
        │
   ┌────┴─────┐
   ↓          ↓
Cardinality Participation
   │          │
 1:1        Total
 1:N        Partial
 N:1
 M:N
```

---

# 79. Final Takeaway

The **ER Model** is the conceptual blueprint of a database.

It allows us to translate a real-world system into a structured representation:

```text
Real-world system
       ↓
Entities
       ↓
Attributes
       ↓
Relationships
       ↓
Keys
       ↓
Cardinality
       ↓
Participation
       ↓
ER Model
       ↓
ER Diagram
       ↓
Relational Tables
       ↓
SQL Database
```

The most important thing to remember is:

> **ER modeling is not about drawing boxes and diamonds randomly. It is about correctly representing the real-world objects, their properties, their relationships, and the business rules governing them.**

Once the ER model is correct, converting it into a relational database becomes much more systematic.
