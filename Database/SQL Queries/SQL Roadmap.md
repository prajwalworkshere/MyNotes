# Complete SQL Learning Roadmap — Beginner to Advanced

Database: MySQL Level: Complete Beginner → Advanced Goal: Detailed notes, practical SQL skills, query execution understanding, database design, and interview preparation.

This is the complete course roadmap, organized phase by phase. We have already completed Topic 1: Database, DBMS, RDBMS and SQL Fundamentals. The next topic is Topic 2: Tables, Rows, Columns, Schemas and Relationships.

## Phase 1 — SQL Fundamentals

Building the foundation from scratch

1. Database, DBMS, RDBMS and SQL Fundamentals

2. Tables, Rows, Columns, Schemas and Relationships

3. MySQL Environment, Databases and Basic SQL Syntax

4. Data Types, NULL and Default Values

5. Creating Tables and Understanding Table Structure

## Phase 2 — Essential SQL Commands

Creating, modifying, inserting, retrieving, and filtering data

#### 6. DDL — Data Definition Language

* `CREATE`

* `ALTER`

* `DROP`

* `TRUNCATE`

* `RENAME`

#### 7. DML — Data Manipulation Language

* `INSERT`

* `UPDATE`

* `DELETE`

#### 8. DQL — Data Query Language

* `SELECT`

* `DISTINCT`

* Column aliases using `AS`

* Expressions and calculated columns

#### 9. Filtering Data

* `WHERE`

* Comparison operators

* Logical operators: `AND`, `OR`, `NOT`

* `LIKE` and wildcards

* `IN` and `NOT IN`

* `BETWEEN` and `NOT BETWEEN`

* `IS NULL` and `IS NOT NULL`

* Operator precedence

#### 10. Sorting, Limiting Results and Pagination

* `ORDER BY`

* `ASC` and `DESC`

* `LIMIT` and `OFFSET`

* Sorting by multiple columns

* Pagination techniques

## Phase 3 — Intermediate SQL and Querying

Functions, grouping, joins, nested queries, and reusable query structures

#### 11. SQL Functions

* String functions

* Numeric functions

* Date and time functions

* NULL-handling functions

* Conditional expressions using `CASE`

* Type conversion and casting

#### 12. Aggregate Functions, GROUP BY and HAVING

* `COUNT()`, `SUM()`, `AVG()`, `MIN()`, `MAX()`

* `GROUP BY`

* `HAVING`

* `WHERE` vs `HAVING`

* Grouping by multiple columns

* Aggregate-function rules and common errors

#### 13. SQL Joins

* `INNER JOIN`

* `LEFT JOIN`

* `RIGHT JOIN`

* `CROSS JOIN`

* Self joins

* Joining multiple tables

* Join conditions and aliases

* Joins vs subqueries

* Finding unmatched rows

#### 14. Subqueries

* Single-row and multiple-row subqueries

* Scalar subqueries

* Multi-column subqueries

* Correlated subqueries

* `EXISTS` and `NOT EXISTS`

* `ANY`, `SOME`, and `ALL`

* Subqueries with `SELECT`, `FROM`, and `WHERE`

#### 15. UNION and Set Operations

* `UNION`

* `UNION ALL`

* Duplicate elimination

* Column-count and data-type compatibility

* Combining query results

* Differences between joins and unions

#### 16. Common Table Expressions (CTEs)

* `WITH`

* Simple CTEs

* Multiple CTEs

* CTEs vs subqueries

* Recursive CTEs

* Hierarchical data queries

## Phase 4 — Constraints, Transactions and Database Objects

Maintaining data correctness, controlling access, and managing changes

#### 17. Constraints, Keys and Relationships

* `PRIMARY KEY`

* `FOREIGN KEY`

* `UNIQUE`

* `NOT NULL`

* `CHECK`

* `DEFAULT`

* Candidate, super, composite, and alternate keys

* Referential integrity

* `ON DELETE` and `ON UPDATE`

* One-to-one, one-to-many, and many-to-many relationships

#### 18. DCL — Data Control Language

* `GRANT`

* `REVOKE`

* Users and privileges

* Database access control

#### 19. TCL — Transaction Control Language

* Transactions

* `START TRANSACTION`

* `COMMIT`

* `ROLLBACK`

* `SAVEPOINT`

* ACID properties

* Autocommit

* Isolation levels and transaction anomalies

* Locks and concurrent transactions

#### 20. Views, Temporary Tables and Materialized-View Alternatives

* `CREATE VIEW`

* `ALTER VIEW`

* `DROP VIEW`

* Updatable views

* Views vs tables

* Temporary tables

* MySQL approaches to materializing query results

#### 21. Advanced Data Modification

* Advanced `INSERT`

* `INSERT ... SELECT`

* `UPDATE` using joins and subqueries

* `DELETE` using joins and subqueries

* Upsert patterns

* `INSERT ... ON DUPLICATE KEY UPDATE`

* `REPLACE`

* MySQL alternatives to `MERGE`

## Phase 5 — Advanced SQL and Performance

Analytical queries, indexes, execution plans, programming objects, and optimization

#### 22. Window Functions

* Window functions vs aggregate functions

* `OVER()` and `PARTITION BY`

* Window `ORDER BY`

* `ROW_NUMBER()`

* `RANK()` and `DENSE_RANK()`

* `LAG()` and `LEAD()`

* Running totals and moving averages

* Window frames

* Top-N records per group

#### 23. Indexes and Query Execution Plans

* What indexes are and why they matter

* B-tree indexes

* Primary, secondary, composite, and unique indexes

* Index selectivity and column order

* Covering indexes

* Index trade-offs

* `EXPLAIN` and `EXPLAIN ANALYZE`

* Full table scans

* Query optimization and performance analysis

#### 24. Stored Procedures, Functions, Triggers and Cursors

* Stored procedures

* Parameters: `IN`, `OUT`, `INOUT`

* Stored functions

* Delimiters and compound statements

* Variables and control flow

* `IF`, `CASE`, loops

* Triggers

* Cursors

* Error handling and handlers

* When to use and avoid database-side programming

#### 25. Normalization, Functional Dependencies and Database Design

* Database anomalies

* Functional dependencies

* First Normal Form (1NF)

* Second Normal Form (2NF)

* Third Normal Form (3NF)

* Boyce–Codd Normal Form (BCNF)

* Keys and dependencies

* Normalization vs denormalization

* ER diagrams and schema design

#### 26. Advanced SQL Patterns

* Ranking and top-N problems

* Gaps and islands

* Running totals and cumulative calculations

* Pivoting and conditional aggregation

* Hierarchical and recursive queries

* JSON functions and JSON data

* Deduplication

* Date-range and time-series analysis

* Advanced analytical query patterns

#### 27. SQL Security and Optimization

* SQL injection

* Prepared statements and parameterized queries

* Least-privilege access

* Query optimization

* Sargable conditions

* Index-aware filtering

* Avoiding unnecessary scans and sorting

* Reading and improving execution plans

* Common performance mistakes

## Phase 6 — Practical Projects and Interview Preparation

Apply everything in realistic databases and interview-style problems

#### 28. Employee and Student Database Projects

* Design tables and relationships

* Insert realistic sample data

* Write filtering, grouping, and join queries

* Analyze employee salaries and student results

* Apply constraints and database design principles

#### 29. Banking, E-commerce and Sales Database Projects

* Accounts and transactions

* Customers, products, and orders

* Order items and payments

* Sales reports and revenue analysis

* Transactions and data integrity

* Multi-table business queries

#### 30. SQL Practice: Easy to Advanced

* Basic retrieval and filtering

* Joins and aggregations

* Subqueries and CTEs

* Window functions

* Real-world reporting problems

* Query-writing challenges with complete solutions and dry runs

#### 31. SQL Interview Preparation

* Fundamental theory questions

* Predict-the-output questions

* Query-writing questions

* Debugging incorrect SQL

* Joins, subqueries, and window-function problems

* Keys, normalization, and relationships

* Indexing and optimization scenarios

* Transactions and ACID

* Real-world case studies and mock interviews
.
