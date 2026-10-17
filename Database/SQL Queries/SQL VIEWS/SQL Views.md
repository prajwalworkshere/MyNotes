# MYSQL VIEWS — COMPLETE IN-DEPTH NOTES

**Database:** MySQL 8.0  
**Level:** Beginner to Advanced  
**Purpose:** Complete theory, syntax, rules, internal working, examples, practical queries, interview preparation, and revision.

---

# TABLE OF CONTENTS

1. Introduction to Views
2. Why Views Are Required
3. View vs Table
4. Types and Classifications of Views
5. Creating a View — `CREATE VIEW`
6. Querying a View
7. Creating Views Using Conditions
8. Views with Expressions and Calculated Columns
9. Views with Aggregate Functions
10. Views Using Multiple Tables and JOINs
11. Naming Columns in Views
12. Modifying a View — `ALTER VIEW`
13. Replacing a View — `CREATE OR REPLACE VIEW`
14. Deleting a View — `DROP VIEW`
15. Renaming Views
16. Updatable and Non-Updatable Views
17. Rules for Updatable Views
18. INSERT, UPDATE, and DELETE Through Views
19. `WITH CHECK OPTION`
20. Nested Views
21. View Algorithms — MERGE, TEMPTABLE, UNDEFINED
22. View Security — DEFINER and INVOKER
23. View Performance and Optimization
24. ORDER BY, LIMIT, GROUP BY, HAVING, DISTINCT and UNION
25. NULL Values and Views
26. Views and Base-Table Changes
27. Dependencies and Invalid Views
28. View Metadata and Inspection
29. Permissions and Access Control
30. Advantages and Disadvantages
31. Common Mistakes
32. Real-World Applications
33. Interview Questions and Detailed Answers
34. Practice Problems with Solutions
35. Final Revision Sheet

---

# 1. INTRODUCTION TO VIEWS

## 1.1 What is a View?

A **View is a named database object that represents the result of a stored SELECT query.**

In MySQL, a view behaves like a virtual table. It presents rows and columns obtained from one or more underlying tables or views.

A regular view stores its definition, not a separate, permanently maintained copy of its result rows.

### Example

Suppose we have an `employees` table:

| employee_id | name | department | salary |
|---:|---|---|---:|
| 101 | Rahul | IT | 60000 |
| 102 | Priya | HR | 45000 |
| 103 | Amit | IT | 75000 |
| 104 | Sneha | Sales | 40000 |
| 105 | Neha | HR | 52000 |

Create a view:

```sql
CREATE VIEW employee_details AS
SELECT employee_id, name, department
FROM employees;
```

Now execute:

```sql
SELECT *
FROM employee_details;
```

Output:

| employee_id | name | department |
|---:|---|---|
| 101 | Rahul | IT |
| 102 | Priya | HR |
| 103 | Amit | IT |
| 104 | Sneha | Sales |
| 105 | Neha | HR |

Notice that the view displays only three columns even though the original table contains four columns.

The `salary` column is not exposed by this view.

### Important understanding

```sql
CREATE VIEW employee_details AS
SELECT employee_id, name, department
FROM employees;
```

This statement does not normally copy all selected rows into a separate data table.

Instead, MySQL saves the query definition associated with `employee_details`.

When we execute:

```sql
SELECT *
FROM employee_details;
```

MySQL uses the view definition to obtain the relevant data.

## 1.2 Internal Working of a View

Consider this view:

```sql
CREATE VIEW it_employees AS
SELECT employee_id, name, salary
FROM employees
WHERE department = 'IT';
```

When we execute:

```sql
SELECT *
FROM it_employees;
```

The conceptual execution is:

```text
User executes SELECT
         |
         v
MySQL identifies the view
         |
         v
MySQL uses the stored SELECT definition
         |
         v
MySQL accesses the underlying employees table
         |
         v
Rows satisfying department = 'IT' are selected
         |
         v
The result is returned to the user
```

This is a conceptual explanation. MySQL's actual execution plan depends on the view algorithm, query structure, optimizer transformations, and available indexes.

## 1.3 Does a View Store Data?

For an ordinary MySQL view, the answer is **no, not as a separate materialized result**.

The view stores its definition, while the underlying tables store the actual records.

Example:

```sql
CREATE VIEW high_salary_employees AS
SELECT employee_id, name, salary
FROM employees
WHERE salary > 50000;
```

Initially, the view may return:

| employee_id | name | salary |
|---:|---|---:|
| 101 | Rahul | 60000 |
| 103 | Amit | 75000 |
| 105 | Neha | 52000 |

Now update the base table:

```sql
UPDATE employees
SET salary = 48000
WHERE employee_id = 105;
```

Query the view again:

```sql
SELECT *
FROM high_salary_employees;
```

Neha no longer appears because her salary no longer satisfies the condition.

**Conclusion:** A regular view normally reflects the current committed state visible to the query, subject to the transaction's isolation level and snapshot.

## 1.4 Important Terminology

| Term | Meaning |
|---|---|
| View | Named query presented as a virtual table |
| Base table | Underlying table containing the source data |
| View definition | Stored `SELECT` statement used by the view |
| Updatable view | A view through which certain data modifications are allowed |
| Non-updatable view | A view through which certain data modifications are prohibited |
| View column | A column exposed by the view |
| Underlying column | A column in a base table used by the view |
| Definer | Account associated with the view's definition and default security context |
| Invoker | Account executing a query against the view |
| Materialized view | A separately stored query result that must be refreshed or maintained; MySQL 8.0 does not provide native materialized views |

---

# 2. WHY ARE VIEWS REQUIRED?

## 2.1 Simplifying Complex Queries

Suppose an employee report requires a JOIN:

```sql
SELECT
    e.employee_id,
    e.name,
    d.department_name,
    e.salary
FROM employees AS e
INNER JOIN departments AS d
    ON e.department_id = d.department_id;
```

If this query is used repeatedly, we can create a view:

```sql
CREATE VIEW employee_department_report AS
SELECT
    e.employee_id,
    e.name,
    d.department_name,
    e.salary
FROM employees AS e
INNER JOIN departments AS d
    ON e.department_id = d.department_id;
```

Now:

```sql
SELECT *
FROM employee_department_report;
```

The JOIN logic does not need to be rewritten every time.

## 2.2 Hiding Unnecessary Columns

Suppose the original table contains:

```text
employee_id
name
department
salary
bank_account
```

A user who only needs employee names and departments can use a view:

```sql
CREATE VIEW employee_public AS
SELECT employee_id, name, department
FROM employees;
```

This reduces the columns exposed through that interface.

**Security note:** A view helps implement restricted access only when appropriate privileges are configured. It does not automatically prevent users with direct table access from reading the hidden columns.

## 2.3 Applying Common Filters

Suppose reports should display only active customers.

```sql
CREATE VIEW active_customers AS
SELECT customer_id, customer_name, city
FROM customers
WHERE status = 'Active';
```

Different applications can query the same view.

## 2.4 Reusing Business Logic

A company might define high-value customers using:

```sql
total_purchases >= 100000
```

Instead of duplicating the condition in multiple reports, it can define a view:

```sql
CREATE VIEW high_value_customers AS
SELECT customer_id, customer_name, total_purchases
FROM customers
WHERE total_purchases >= 100000;
```

The view provides a shared definition of the condition.

## 2.5 Logical Data Abstraction

A user can query a simplified view without knowing every detail of the underlying database schema.

However, views do not guarantee complete physical or logical independence. Changes to underlying column names, data types, and dependencies can break a view or change its behavior.

---

# 3. VIEW VS TABLE

| Feature | Table | View |
|---|---|---|
| Purpose | Stores database records | Presents data through a query |
| Own data storage | Yes | No separately stored result rows for an ordinary view |
| Stored definition | Table structure | View query definition |
| `SELECT` | Supported | Supported |
| `INSERT` | Supported subject to constraints | Supported only for insertable views and valid operations |
| `UPDATE` | Supported subject to constraints | Supported only for updatable views |
| `DELETE` | Supported subject to constraints | Supported only for updatable views |
| `JOIN` | Can be used in queries against a table | Can be defined using JOINs |
| Aggregate functions | Can be queried over a table | Can be included in a view definition |
| Indexes | Can have indexes | Cannot have ordinary indexes created directly on a view |
| Foreign keys | Supported | No independent foreign-key constraints |
| `DROP` | `DROP TABLE` | `DROP VIEW` |
| Storage engine | Has a storage engine | Does not independently store its result rows in a storage engine |
| Reflects base-table changes | Not applicable to the table itself | Normally reflects changes visible to subsequent queries |

### Interview point

A view is not necessarily faster than the original query.

A view is primarily a logical abstraction and reuse mechanism. Its performance depends on its query definition, the optimizer, indexes on underlying tables, and the query accessing the view.

---

# 4. TYPES AND CLASSIFICATIONS OF VIEWS

There is no single exhaustive list of view types that applies to every database system. In MySQL learning, views are commonly classified by complexity and update behavior.

## 4.1 Simple View

A simple view usually selects columns from one table without grouping or aggregation.

```sql
CREATE VIEW employee_basic AS
SELECT employee_id, name, department
FROM employees;
```

Typical purposes:

- Expose selected columns.
- Simplify data access.
- Provide a restricted interface.
- Support certain INSERT, UPDATE, and DELETE operations.

A simple view is often updatable, but the exact definition and operation must satisfy MySQL's rules.

## 4.2 Complex View

A complex view uses more advanced query operations, such as grouping, aggregation, or joins.

```sql
CREATE VIEW department_summary AS
SELECT
    department,
    COUNT(*) AS total_employees,
    AVG(salary) AS average_salary
FROM employees
GROUP BY department;
```

It can simplify reporting, but it is not updatable through ordinary row-level DML in this example.

## 4.3 Updatable View

```sql
CREATE VIEW employee_names AS
SELECT employee_id, name
FROM employees;
```

A valid update through the view can modify the corresponding base-table row:

```sql
UPDATE employee_names
SET name = 'Rahul Sharma'
WHERE employee_id = 101;
```

## 4.4 Non-Updatable View

```sql
CREATE VIEW employee_count AS
SELECT COUNT(*) AS total_employees
FROM employees;
```

This view produces an aggregate value, not an ordinary mapping of individual employee rows. Its result cannot be updated as though it were a base-table row.

## 4.5 Join View

```sql
CREATE VIEW employee_department_view AS
SELECT
    e.employee_id,
    e.name,
    d.department_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.department_id = d.department_id;
```

Join views are useful for reporting. Their update behavior depends on MySQL's join-view rules.

---

# 5. CREATING A VIEW — `CREATE VIEW`

## 5.1 Basic Syntax

```sql
CREATE VIEW view_name AS
SELECT column1, column2
FROM table_name;
```

## 5.2 Full General Syntax

```sql
CREATE
    [OR REPLACE]
    [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]
    [DEFINER = user]
    [SQL SECURITY {DEFINER | INVOKER}]
VIEW view_name [(column_list)]
AS select_statement
[WITH [CASCADED | LOCAL] CHECK OPTION];
```

The optional clauses are explained later.

## 5.3 Complete Example

Create a base table:

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    department VARCHAR(30),
    salary DECIMAL(10,2),
    city VARCHAR(50)
);
```

Insert sample data:

```sql
INSERT INTO employees
    (employee_id, name, department, salary, city)
VALUES
    (101, 'Rahul', 'IT', 60000, 'Mumbai'),
    (102, 'Priya', 'HR', 45000, 'Pune'),
    (103, 'Amit', 'IT', 75000, 'Mumbai'),
    (104, 'Sneha', 'Sales', 40000, 'Delhi'),
    (105, 'Neha', 'HR', 52000, 'Pune');
```

Create the view:

```sql
CREATE VIEW employee_details AS
SELECT employee_id, name, department
FROM employees;
```

Read the view:

```sql
SELECT *
FROM employee_details;
```

The output contains `employee_id`, `name`, and `department`, but not `salary` or `city`.

### Rules

1. The view must have a valid name.
2. The defining query must be valid.
3. Referenced objects must exist when the view is created.
4. The creator must have the necessary privileges.
5. View column names must be unique.
6. A view can reference tables and other views.
7. MySQL does not allow a view to reference a `TEMPORARY` table.
8. The view definition cannot use stored-routine local variables or routine parameters as though they were permanent table columns.
9. Views have restrictions on references to variables and prepared-statement parameters in their definitions.
10. A view name must not conflict with another table or view name in the same schema.

---

# 6. QUERYING A VIEW

Once created, a view can be queried like a table.

## 6.1 Retrieve All Columns

```sql
SELECT *
FROM employee_details;
```

## 6.2 Retrieve Specific Columns

```sql
SELECT name, department
FROM employee_details;
```

## 6.3 Apply a WHERE Condition

```sql
SELECT *
FROM employee_details
WHERE department = 'IT';
```

## 6.4 Sort Results

```sql
SELECT *
FROM employee_details
ORDER BY name ASC;
```

## 6.5 Limit the Result

```sql
SELECT *
FROM employee_details
LIMIT 3;
```

## 6.6 Use DISTINCT

```sql
SELECT DISTINCT department
FROM employee_details;
```

## 6.7 Use Aggregation on a View

```sql
SELECT department, COUNT(*) AS total_employees
FROM employee_details
GROUP BY department;
```

**Important:** The view does not prevent an outer query from using additional conditions, grouping, sorting, joins, or other supported SELECT operations.

---

# 7. CREATING VIEWS WITH WHERE CONDITIONS

A view can contain a filter that defines which rows it exposes.

## 7.1 Employees Belonging to IT

```sql
CREATE VIEW it_employees AS
SELECT employee_id, name, salary
FROM employees
WHERE department = 'IT';
```

Query:

```sql
SELECT *
FROM it_employees;
```

Expected output:

| employee_id | name | salary |
|---:|---|---:|
| 101 | Rahul | 60000 |
| 103 | Amit | 75000 |

## 7.2 Employees Earning More Than 50000

```sql
CREATE VIEW high_salary_employees AS
SELECT employee_id, name, salary
FROM employees
WHERE salary > 50000;
```

Query:

```sql
SELECT *
FROM high_salary_employees;
```

Expected output:

| employee_id | name | salary |
|---:|---|---:|
| 101 | Rahul | 60000 |
| 103 | Amit | 75000 |
| 105 | Neha | 52000 |

## 7.3 Combine Conditions

```sql
CREATE VIEW mumbai_it_employees AS
SELECT employee_id, name, department, city
FROM employees
WHERE department = 'IT'
  AND city = 'Mumbai';
```

## 7.4 Apply Another Condition When Querying

```sql
SELECT *
FROM high_salary_employees
WHERE salary > 65000;
```

This returns only employees who satisfy both the view's original filter and the outer query's condition.

---

# 8. VIEWS WITH EXPRESSIONS AND CALCULATED COLUMNS

Views can expose calculated values.

## 8.1 Annual Salary

```sql
CREATE VIEW annual_salary_view AS
SELECT
    employee_id,
    name,
    salary AS monthly_salary,
    salary * 12 AS annual_salary
FROM employees;
```

Query:

```sql
SELECT *
FROM annual_salary_view;
```

For Rahul:

```text
monthly_salary = 60000
annual_salary  = 60000 * 12
               = 720000
```

## 8.2 CASE Expression

```sql
CREATE VIEW salary_category AS
SELECT
    employee_id,
    name,
    salary,
    CASE
        WHEN salary >= 70000 THEN 'High'
        WHEN salary >= 50000 THEN 'Medium'
        ELSE 'Low'
    END AS salary_level
FROM employees;
```

The calculated `salary_level` is produced by the query. It is not a separately stored column in `employees`.

## 8.3 String Functions

```sql
CREATE VIEW employee_name_view AS
SELECT
    employee_id,
    CONCAT(name, ' - ', department) AS employee_label
FROM employees;
```

## 8.4 Date and Numeric Expressions

A view can also use expressions such as:

```sql
SELECT
    amount,
    amount * 0.18 AS tax_amount,
    amount * 1.18 AS amount_with_tax
FROM orders;
```

The same principle applies to date calculations and other supported expressions.

**Updatability warning:** A calculated output column is generally not directly updatable through the view because it does not correspond to one underlying column. The presence of expressions can also affect whether the view is updatable.

---

# 9. VIEWS WITH AGGREGATE FUNCTIONS

Views can contain aggregate functions, including `COUNT()`, `SUM()`, `AVG()`, `MIN()`, and `MAX()`.

## 9.1 Department-Wise Employee Count

```sql
CREATE VIEW department_employee_count AS
SELECT
    department,
    COUNT(*) AS total_employees
FROM employees
GROUP BY department;
```

Expected output:

| department | total_employees |
|---|---:|
| HR | 2 |
| IT | 2 |
| Sales | 1 |

## 9.2 Department-Wise Salary Summary

```sql
CREATE VIEW department_salary_summary AS
SELECT
    department,
    COUNT(*) AS employee_count,
    SUM(salary) AS total_salary,
    AVG(salary) AS average_salary,
    MIN(salary) AS minimum_salary,
    MAX(salary) AS maximum_salary
FROM employees
GROUP BY department;
```

## 9.3 Apply HAVING Inside the View

```sql
CREATE VIEW large_departments AS
SELECT
    department,
    COUNT(*) AS total_employees
FROM employees
GROUP BY department
HAVING COUNT(*) >= 2;
```

This view contains only departments with at least two employees.

## 9.4 Apply Conditions Outside the View

```sql
SELECT *
FROM department_salary_summary
WHERE average_salary > 45000;
```

The outer condition filters the already-defined summary result.

### Important rules

- `COUNT(*)` counts rows.
- `COUNT(column_name)` counts non-NULL values in that column.
- `SUM()` and `AVG()` ignore NULL values in the aggregated expression.
- `MIN()` and `MAX()` ignore NULL values.
- An aggregate view is generally not updatable.
- Grouped results represent groups, not individual base-table rows.

---

# 10. VIEWS USING MULTIPLE TABLES AND JOINS

Views can encapsulate JOIN logic.

Consider two tables.

```sql
CREATE TABLE departments (
    department_id INT PRIMARY KEY,
    department_name VARCHAR(50) NOT NULL
);
```

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    department_id INT,
    salary DECIMAL(10,2),
    FOREIGN KEY (department_id)
        REFERENCES departments(department_id)
);
```

Insert data:

```sql
INSERT INTO departments
VALUES
    (1, 'IT'),
    (2, 'HR'),
    (3, 'Sales');
```

```sql
INSERT INTO employees
VALUES
    (101, 'Rahul', 1, 60000),
    (102, 'Priya', 2, 45000),
    (103, 'Amit', 1, 75000),
    (104, 'Sneha', 3, 40000);
```

## 10.1 INNER JOIN View

```sql
CREATE VIEW employee_department_view AS
SELECT
    e.employee_id,
    e.name,
    d.department_name,
    e.salary
FROM employees AS e
INNER JOIN departments AS d
    ON e.department_id = d.department_id;
```

Query:

```sql
SELECT *
FROM employee_department_view;
```

## 10.2 LEFT JOIN View

```sql
CREATE VIEW employee_all_departments AS
SELECT
    d.department_id,
    d.department_name,
    e.employee_id,
    e.name
FROM departments AS d
LEFT JOIN employees AS e
    ON d.department_id = e.department_id;
```

A department without employees can still appear, with NULL values for the employee columns.

## 10.3 Three-Table JOIN View

```sql
CREATE VIEW order_details_view AS
SELECT
    o.order_id,
    c.customer_name,
    p.product_name,
    o.quantity,
    p.price,
    o.quantity * p.price AS total_amount
FROM orders AS o
INNER JOIN customers AS c
    ON o.customer_id = c.customer_id
INNER JOIN products AS p
    ON o.product_id = p.product_id;
```

This example assumes the referenced tables and columns exist.

Query:

```sql
SELECT *
FROM order_details_view
WHERE total_amount > 1000;
```

### Join-view updatability

Do not assume that a JOIN view is always read-only or always updatable.

MySQL has specific rules for join views. A view may be updatable with respect to one underlying table under certain conditions, but a single modification statement cannot update multiple underlying tables through a join view. Outer joins and ambiguous row mappings can also affect update behavior.

---

# 11. NAMING COLUMNS IN VIEWS

Column names exposed by a view can be defined in two ways.

## 11.1 Use Aliases in the SELECT Query

```sql
CREATE VIEW employee_salary_view AS
SELECT
    employee_id AS id,
    name AS employee_name,
    salary * 12 AS annual_salary
FROM employees;
```

Query:

```sql
SELECT *
FROM employee_salary_view;
```

The exposed column names are `id`, `employee_name`, and `annual_salary`.

## 11.2 Supply a Column List

```sql
CREATE VIEW employee_view (id, employee_name, annual_salary) AS
SELECT
    employee_id,
    name,
    salary * 12
FROM employees;
```

The column list assigns names to the output columns in order.

The number of names in the list must match the number of columns returned by the SELECT statement.

## 11.3 Avoid Duplicate Column Names

A view must expose uniquely named columns.

Incorrect:

```sql
CREATE VIEW invalid_employee_view AS
SELECT
    employee_id,
    name,
    salary AS name
FROM employees;
```

The output would contain duplicate column names, so MySQL rejects this definition.

Correct:

```sql
CREATE VIEW valid_employee_view AS
SELECT
    employee_id,
    name,
    salary AS employee_salary
FROM employees;
```

---

# 12. MODIFYING A VIEW — ALTER VIEW

The `ALTER VIEW` statement modifies a view's definition or applicable view characteristics.

## Syntax

```sql
ALTER VIEW view_name AS
SELECT ...;
```

The general syntax also supports optional clauses such as `ALGORITHM`, `DEFINER`, and `SQL SECURITY`.

## Example

Suppose the original view is:

```sql
CREATE VIEW employee_details AS
SELECT employee_id, name, department
FROM employees;
```

Now modify it:

```sql
ALTER VIEW employee_details AS
SELECT employee_id, name, department, salary
FROM employees;
```

Query:

```sql
SELECT *
FROM employee_details;
```

The view now exposes four columns.

### Important rules

1. `ALTER VIEW` changes the stored query definition.
2. It does not change the structure of the underlying table.
3. It does not create a new view with a different name.
4. The new definition must be valid.
5. Changing exposed columns can break applications that depend on the old view structure.
6. Required privileges must be available.

---

# 13. REPLACING A VIEW — CREATE OR REPLACE VIEW

`CREATE OR REPLACE VIEW` creates a view if it does not exist or replaces the existing view definition if it does.

## Syntax

```sql
CREATE OR REPLACE VIEW view_name AS
SELECT ...;
```

Example:

```sql
CREATE OR REPLACE VIEW employee_details AS
SELECT employee_id, name, department, city
FROM employees;
```

This replaces the earlier definition of `employee_details`.

### ALTER VIEW vs CREATE OR REPLACE VIEW

| `ALTER VIEW` | `CREATE OR REPLACE VIEW` |
|---|---|
| Modifies an existing view | Creates the view if absent or replaces it if present |
| Requires the view to exist | Does not require it to exist beforehand |
| Does not create a missing view | Can create a missing view |
| Useful for changing an existing definition | Useful for repeatable deployment scripts |

### Important warning

Replacing a view changes its stored definition and can affect dependent views, applications, and permissions. Review the consequences before changing a production view.

---

# 14. DELETING A VIEW — DROP VIEW

`DROP VIEW` removes the view definition.

## Syntax

```sql
DROP VIEW view_name;
```

Example:

```sql
DROP VIEW employee_details;
```

The view is removed, but the underlying `employees` table and its records remain.

## 14.1 Prevent an Error if the View Does Not Exist

```sql
DROP VIEW IF EXISTS employee_details;
```

## 14.2 Drop Multiple Views

```sql
DROP VIEW IF EXISTS
    employee_details,
    it_employees,
    department_summary;
```

### Important rules

- `DROP VIEW` removes the view, not its underlying table.
- It does not delete the rows stored in the base table.
- If another view depends on the dropped view, that dependent view may become invalid.
- A dependent view is not automatically guaranteed to be removed with its dependency.
- You need the appropriate privileges to drop the view.

---

# 15. RENAMING VIEWS

MySQL does not provide a dedicated `RENAME VIEW` statement in the same way some database systems do.

A common approach is to create the new view, verify it, and then remove the old view.

Example:

```sql
CREATE VIEW employee_details_new AS
SELECT employee_id, name, department
FROM employees;
```

Verify:

```sql
SELECT *
FROM employee_details_new;
```

After verifying the replacement and handling dependencies:

```sql
DROP VIEW employee_details;
```

This sequence does not automatically migrate all dependencies or permissions. Plan such changes carefully in production.

---

# 16. UPDATABLE AND NON-UPDATABLE VIEWS

This is one of the most important topics in MySQL Views.

## 16.1 What Is an Updatable View?

An updatable view permits certain data-modification operations through the view.

Example:

```sql
CREATE VIEW employee_basic AS
SELECT employee_id, name, salary
FROM employees;
```

Update:

```sql
UPDATE employee_basic
SET salary = 65000
WHERE employee_id = 101;
```

This modifies the underlying employee record if the operation is permitted.

## 16.2 What Is a Non-Updatable View?

A non-updatable view does not permit certain modifications because MySQL cannot safely map the operation to the underlying rows.

Example:

```sql
CREATE VIEW department_summary AS
SELECT
    department,
    AVG(salary) AS average_salary
FROM employees
GROUP BY department;
```

The row representing IT is an aggregate of multiple employee records. Assigning a new average directly through the view would not specify how individual employee salaries should change.

## 16.3 Common Non-Updatable Constructs

The following constructs make a view non-updatable under MySQL's view rules:

- Aggregate functions such as `COUNT()`, `SUM()`, and `AVG()`.
- `DISTINCT`.
- `GROUP BY`.
- `HAVING`.
- `UNION` or `UNION ALL`.
- Certain subqueries, including a subquery in the select list.
- Other constructs that prevent MySQL from mapping view rows unambiguously to underlying rows.

The exact rules are operation-dependent. Do not use a simplified classification as a substitute for MySQL's documented updatability rules.

---

# 17. RULES FOR UPDATABLE VIEWS

## Rule 1: The View Must Map to Underlying Rows

MySQL must be able to determine which underlying rows an operation affects.

A simple projection often satisfies this requirement:

```sql
CREATE VIEW employee_view AS
SELECT employee_id, name, salary
FROM employees;
```

## Rule 2: Aggregate Results Cannot Be Updated as Individual Rows

```sql
CREATE VIEW employee_summary AS
SELECT department, COUNT(*) AS total
FROM employees
GROUP BY department;
```

The grouped result is not an ordinary editable representation of each employee.

## Rule 3: DISTINCT Prevents Ordinary View Updatability

```sql
CREATE VIEW employee_departments AS
SELECT DISTINCT department
FROM employees;
```

The output represents unique department values rather than a one-to-one mapping of employee rows.

## Rule 4: UNION Views Are Not Updatable

```sql
CREATE VIEW all_people AS
SELECT name FROM employees
UNION
SELECT customer_name FROM customers;
```

This combines results from separate SELECT statements and is not an updatable view.

## Rule 5: Some JOIN Views Are Updatable

A join view may be updatable with respect to an individual underlying table, depending on its definition and the operation.

```sql
CREATE VIEW employee_department_view AS
SELECT
    e.employee_id,
    e.name,
    d.department_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.department_id = d.department_id;
```

Do not assume all modifications will work through this view.

## Rule 6: A View Cannot Be Used to Bypass Constraints

If an underlying table requires a non-NULL column or has a foreign key, modifications through a view must still satisfy the relevant constraints.

## Rule 7: The User Needs the Required Privileges

Even if a view is updatable, the operation can fail if the relevant account lacks the necessary privileges.

## Rule 8: Insertability Has Additional Requirements

A view can be updatable for UPDATE operations but not necessarily insertable for every possible INSERT operation.

For example, an INSERT may fail if a required base-table column is omitted and has neither a default value nor an automatically generated value.

---

# 18. INSERT, UPDATE, AND DELETE THROUGH VIEWS

## 18.1 UPDATE Through a View

Create:

```sql
CREATE VIEW employee_update_view AS
SELECT employee_id, name, salary
FROM employees;
```

Update:

```sql
UPDATE employee_update_view
SET salary = 80000
WHERE employee_id = 103;
```

Verify:

```sql
SELECT *
FROM employees
WHERE employee_id = 103;
```

The underlying row should reflect the salary change if the operation succeeds.

## 18.2 INSERT Through a View

Suppose the base table has:

```text
employee_id — primary key
name        — NOT NULL
department
salary
city
```

View:

```sql
CREATE VIEW employee_insert_view AS
SELECT employee_id, name, department
FROM employees;
```

An insert may succeed if omitted base-table columns can legally receive their default or NULL values:

```sql
INSERT INTO employee_insert_view
    (employee_id, name, department)
VALUES
    (106, 'Karan', 'IT');
```

If `salary` or `city` were defined as `NOT NULL` without a default or automatic value, the insert could fail.

## 18.3 DELETE Through a View

```sql
DELETE FROM employee_insert_view
WHERE employee_id = 106;
```

If the view is deletable and the operation is permitted, the corresponding base-table record is deleted.

**Warning:** Deleting through a view deletes underlying data. It does not merely hide a row from the view.

## 18.4 Can We Insert into a View That Has a WHERE Condition?

Consider:

```sql
CREATE VIEW it_employees AS
SELECT employee_id, name, department, salary
FROM employees
WHERE department = 'IT';
```

Attempt:

```sql
INSERT INTO it_employees
    (employee_id, name, department, salary)
VALUES
    (107, 'Karan', 'HR', 50000);
```

Without a suitable `CHECK OPTION`, MySQL may permit an insert that creates a row in the underlying table but does not satisfy the view's filter. The inserted row would not appear in `it_employees`.

This is why `WITH CHECK OPTION` is important.

---

# 19. WITH CHECK OPTION

## 19.1 What Is CHECK OPTION?

`WITH CHECK OPTION` restricts modifications made through an updatable view so that inserted or updated rows satisfy the view's defining condition.

It is particularly useful for views containing a `WHERE` clause.

## 19.2 Syntax

```sql
CREATE VIEW view_name AS
SELECT ...
FROM table_name
WHERE condition
WITH CHECK OPTION;
```

The full syntax allows:

```sql
WITH [CASCADED | LOCAL] CHECK OPTION
```

## 19.3 Example Without CHECK OPTION

```sql
CREATE VIEW it_employees AS
SELECT employee_id, name, department, salary
FROM employees
WHERE department = 'IT';
```

Attempt:

```sql
INSERT INTO it_employees
    (employee_id, name, department, salary)
VALUES
    (108, 'Karan', 'HR', 50000);
```

The row may be inserted into `employees`, but it will not appear in `it_employees`.

The reason is:

```text
Inserted row:
department = HR
       |
       v
View condition:
department = IT
       |
       v
Condition is false
       |
       v
The row is not visible through the view
```

## 19.4 Example With CHECK OPTION

```sql
CREATE OR REPLACE VIEW it_employees AS
SELECT employee_id, name, department, salary
FROM employees
WHERE department = 'IT'
WITH CHECK OPTION;
```

Now try:

```sql
INSERT INTO it_employees
    (employee_id, name, department, salary)
VALUES
    (109, 'Karan', 'HR', 50000);
```

MySQL rejects the operation because the resulting row would not satisfy the view condition.

A valid insert is:

```sql
INSERT INTO it_employees
    (employee_id, name, department, salary)
VALUES
    (110, 'Karan', 'IT', 50000);
```

## 19.5 CHECK OPTION with UPDATE

Suppose:

```sql
UPDATE it_employees
SET department = 'HR'
WHERE employee_id = 101;
```

If employee 101 is currently in IT, this update violates the view's check condition and is rejected.

## 19.6 LOCAL vs CASCADED CHECK OPTION

These options matter when views are built on other views.

Consider the following definitions:

```sql
CREATE VIEW it_employees AS
SELECT employee_id, name, department, salary
FROM employees
WHERE department = 'IT'
WITH CHECK OPTION;
```

Then:

```sql
CREATE VIEW it_high_salary AS
SELECT employee_id, name, department, salary
FROM it_employees
WHERE salary > 50000
WITH LOCAL CHECK OPTION;
```

The outer view has the condition:

```sql
salary > 50000
```

The underlying view has the condition:

```sql
department = 'IT'
```

### LOCAL CHECK OPTION

Checks the condition associated with the current view. It does not independently enforce the conditions of underlying views that do not themselves enforce those conditions through their own check options.

### CASCADED CHECK OPTION

Checks the current view's condition and the relevant conditions of underlying views in the view chain.

When `CHECK OPTION` is specified without `LOCAL` or `CASCADED`, the default is `CASCADED`.

### Important

- `CHECK OPTION` applies to insertions and updates through an updatable view.
- It does not make a non-updatable view updatable.
- It does not replace table constraints.
- It does not control modifications made directly to the base table.

---

# 20. NESTED VIEWS

A nested view is a view defined using another view.

## Example

First create:

```sql
CREATE VIEW employee_basic AS
SELECT employee_id, name, department, salary
FROM employees;
```

Now create a second view based on it:

```sql
CREATE VIEW high_salary_it_employees AS
SELECT employee_id, name, salary
FROM employee_basic
WHERE department = 'IT'
  AND salary > 50000;
```

Query:

```sql
SELECT *
FROM high_salary_it_employees;
```

Conceptually:

```text
employees
    |
    v
employee_basic
    |
    v
high_salary_it_employees
    |
    v
User's SELECT
```

### Advantages

- Reuse common query logic.
- Separate concerns into understandable layers.
- Provide different interfaces over the same data.

### Disadvantages

- Deep nesting can make debugging difficult.
- Performance depends on the combined query and optimizer behavior.
- Changing an underlying view can affect dependent views.
- Check-option behavior becomes more complicated across nested views.

---

# 21. VIEW ALGORITHMS

MySQL supports three view algorithm options:

1. `MERGE`
2. `TEMPTABLE`
3. `UNDEFINED`

These options influence how MySQL processes a view.

## 21.1 MERGE Algorithm

With `MERGE`, MySQL attempts to combine the view's SELECT definition with the outer query.

Example:

```sql
CREATE ALGORITHM = MERGE
VIEW it_employees AS
SELECT employee_id, name, salary
FROM employees
WHERE department = 'IT';
```

Query:

```sql
SELECT name
FROM it_employees
WHERE salary > 60000;
```

Conceptually, the optimizer can work with the combined conditions:

```sql
SELECT name
FROM employees
WHERE department = 'IT'
  AND salary > 60000;
```

This is an illustration of the logical combination, not a promise that every query will be rewritten into exactly this text.

### Advantages

- Can allow conditions to be optimized together.
- Avoids requiring a separate materialized intermediate result solely because of the view.
- Often suitable for simple views.

### Limitations

Some view definitions cannot use `MERGE`, such as definitions containing constructs that require materialization or cannot be merged.

## 21.2 TEMPTABLE Algorithm

With `TEMPTABLE`, MySQL materializes the view's result into an internal temporary table for processing the outer query.

Example:

```sql
CREATE ALGORITHM = TEMPTABLE
VIEW department_summary AS
SELECT department, AVG(salary) AS average_salary
FROM employees
GROUP BY department;
```

Query:

```sql
SELECT *
FROM department_summary
WHERE average_salary > 50000;
```

Conceptually:

```text
employees
    |
    v
Execute grouped view query
    |
    v
Produce an internal temporary result
    |
    v
Apply outer query to that result
    |
    v
Return output
```

The temporary result is internal to query execution; it does not turn the view into a permanently stored table.

### Limitations

- Temporary result creation can require extra work and memory or disk space.
- The optimizer cannot always push outer conditions into the view query as freely as it can with a merged view.
- A TEMPTABLE view is not updatable.

## 21.3 UNDEFINED Algorithm

```sql
CREATE ALGORITHM = UNDEFINED
VIEW employee_view AS
SELECT employee_id, name, salary
FROM employees;
```

This lets MySQL choose the appropriate algorithm when possible.

`UNDEFINED` is the default if no algorithm is specified.

### Comparison

| Feature | MERGE | TEMPTABLE | UNDEFINED |
|---|---|---|---|
| Meaning | Combines the view query with the outer query when possible | Materializes the view result internally | Lets MySQL choose |
| Temporary intermediate result required by the algorithm | No | Yes | Depends on chosen strategy |
| Supports updatability | May, depending on the definition | No | Depends on the resulting strategy and definition |
| Best use | Suitable simple views | Suitable definitions requiring materialization | General default |

**Important:** If a requested algorithm is incompatible with the view definition, MySQL may issue a warning and use a different algorithm, depending on the circumstances. Check the result with `SHOW WARNINGS` and inspect the view definition rather than assuming the requested algorithm was applied.

---

# 22. VIEW SECURITY — DEFINER AND INVOKER

Views can provide a controlled interface to database objects. MySQL provides two security contexts.

## 22.1 SQL SECURITY DEFINER

This is the default security mode when no other mode is specified.

The view executes with the privileges of the account specified as its definer.

Example:

```sql
CREATE
    DEFINER = CURRENT_USER
    SQL SECURITY DEFINER
VIEW employee_public AS
SELECT employee_id, name, department
FROM employees;
```

A user who has permission to query the view may be able to retrieve its permitted data without having direct SELECT permission on the underlying table, provided the definer and privilege configuration allow it.

## 22.2 SQL SECURITY INVOKER

The view executes with the privileges of the account invoking it.

Example:

```sql
CREATE
    SQL SECURITY INVOKER
VIEW employee_public AS
SELECT employee_id, name, department
FROM employees;
```

The invoking account must have the necessary privileges on the underlying objects used by the view.

## 22.3 DEFINER vs INVOKER

| Feature | DEFINER | INVOKER |
|---|---|---|
| Execution privilege context | View definer | Calling account |
| Default mode | Yes | No |
| Can provide controlled access without direct base-table privileges | Yes, if configured correctly | Generally requires the invoker's underlying-object privileges |
| Main concern | Managing the definer account and its privileges | Managing privileges of every invoking account |

### Security rules

- Creating a view requires appropriate privileges.
- Querying a view requires appropriate view privileges.
- Updating a view requires the relevant modification privileges.
- The security context also affects access to underlying objects.
- A missing or improperly configured definer can cause access problems.
- Views should expose only the data required for the intended task.
- Do not assume that `SQL SECURITY DEFINER` automatically makes a view safe; privilege design is essential.

---

# 23. VIEW PERFORMANCE AND OPTIMIZATION

## 23.1 Are Views Faster Than Tables?

Not automatically.

A normal view does not store its own persistent result rows. It provides a reusable query definition.

Its performance depends on:

- The complexity of the defining query.
- The underlying tables.
- Indexes on the underlying tables.
- JOIN conditions.
- Filtering conditions.
- GROUP BY and sorting operations.
- The view algorithm.
- The outer query.
- MySQL's execution plan.

## 23.2 Indexes on Base Tables

Suppose a view filters employees by department:

```sql
CREATE VIEW it_employees AS
SELECT employee_id, name, salary
FROM employees
WHERE department = 'IT';
```

An index on the underlying table may help:

```sql
CREATE INDEX idx_employees_department
ON employees(department);
```

This index belongs to `employees`, not to the view.

Whether it improves performance depends on the data distribution, query, and execution plan.

## 23.3 EXPLAIN

Use `EXPLAIN` to inspect how MySQL plans to execute a query.

```sql
EXPLAIN
SELECT *
FROM it_employees
WHERE salary > 60000;
```

You can compare this plan with a similar direct query against `employees`.

## 23.4 Avoid Unnecessary Complexity

Deeply nested views can make execution plans and debugging difficult.

Good practice:

- Keep views focused.
- Use meaningful names.
- Avoid exposing unnecessary columns.
- Index underlying tables appropriately.
- Inspect important queries with `EXPLAIN`.
- Test realistic data volumes before making performance claims.

---

# 24. IMPORTANT QUERY CLAUSES INSIDE VIEWS

## 24.1 ORDER BY

Example:

```sql
CREATE VIEW employee_sorted AS
SELECT employee_id, name, salary
FROM employees
ORDER BY salary DESC;
```

Query:

```sql
SELECT *
FROM employee_sorted;
```

MySQL allows `ORDER BY` in a view definition, subject to view rules.

However, the order of rows is not a general guarantee for every outer query. An outer `ORDER BY` takes precedence when specified.

For guaranteed sorting, write:

```sql
SELECT *
FROM employee_sorted
ORDER BY salary DESC;
```

## 24.2 LIMIT

Example:

```sql
CREATE VIEW top_three_employees AS
SELECT employee_id, name, salary
FROM employees
ORDER BY salary DESC
LIMIT 3;
```

This view represents a query restricted to three rows.

Use caution when combining it with outer filtering or sorting: the view's row restriction affects the result, and the final order should be specified in the outer query if required.

## 24.3 GROUP BY and HAVING

```sql
CREATE VIEW department_summary AS
SELECT department, COUNT(*) AS total_employees
FROM employees
GROUP BY department
HAVING COUNT(*) >= 2;
```

This is useful for reusable reports but is not an ordinary updatable view.

## 24.4 DISTINCT

```sql
CREATE VIEW unique_departments AS
SELECT DISTINCT department
FROM employees;
```

This exposes distinct department values.

Because the output no longer maps unambiguously to individual employee records, the view is not updatable.

## 24.5 UNION

```sql
CREATE VIEW all_names AS
SELECT name AS person_name
FROM employees

UNION

SELECT customer_name AS person_name
FROM customers;
```

Both SELECT statements must return compatible column counts and types.

The resulting view combines the result sets and removes duplicates because it uses `UNION`, not `UNION ALL`.

This is useful for read-only reporting across related or separate data sources.

---

# 25. NULL VALUES AND VIEWS

Views follow the same NULL semantics as ordinary SELECT queries.

## 25.1 Filtering NULL Values

```sql
CREATE VIEW employees_without_city AS
SELECT employee_id, name, city
FROM employees
WHERE city IS NULL;
```

Use `IS NULL`, not `= NULL`.

## 25.2 COUNT(*) vs COUNT(column)

```sql
CREATE VIEW employee_count_summary AS
SELECT
    COUNT(*) AS total_rows,
    COUNT(city) AS employees_with_city
FROM employees;
```

- `COUNT(*)` counts all rows.
- `COUNT(city)` counts rows in which `city` is not NULL.

## 25.3 COALESCE

```sql
CREATE VIEW employee_city_display AS
SELECT
    employee_id,
    name,
    COALESCE(city, 'Unknown') AS city_display
FROM employees;
```

This substitutes `'Unknown'` when `city` is NULL.

The expression changes the displayed result, not the underlying `city` value.

---

# 26. VIEWS AND BASE-TABLE CHANGES

## 26.1 Inserting New Rows

A regular view normally reflects newly inserted rows when they satisfy the defining query and are visible to the querying transaction.

## 26.2 Updating Existing Rows

If an update changes whether a row satisfies the view condition, that row may enter or leave the view's result.

## 26.3 Deleting Rows

Deleting a base-table row removes it from results produced by views that depended on that row.

## 26.4 Renaming or Dropping Base Columns

A view can become invalid or fail to execute if the underlying columns it references are renamed or removed.

Do not assume that MySQL will automatically rewrite every dependent view after a schema change.

## 26.5 Dropping a Base Table

Dropping an underlying table can leave dependent views unusable.

Before schema migrations, inspect dependencies and test the affected views.

---

# 27. VIEW DEPENDENCIES AND INVALID VIEWS

Consider:

```sql
CREATE VIEW employee_basic AS
SELECT employee_id, name
FROM employees;
```

Then:

```sql
CREATE VIEW employee_names AS
SELECT name
FROM employee_basic;
```

The second view depends on the first, and the first depends on `employees`.

```text
employees
    |
    v
employee_basic
    |
    v
employee_names
```

If the `employees` table is dropped or its relevant columns are changed, one or both views may fail.

Best practices:

1. Track view dependencies.
2. Test schema migrations in a development environment.
3. Use `SHOW CREATE VIEW` to inspect definitions.
4. Check dependent views after changing a base table.
5. Recreate or alter affected views when necessary.
6. Avoid dropping production objects without a dependency review.

---

# 28. VIEW METADATA AND INSPECTION

## 28.1 List Tables and Views

```sql
SHOW FULL TABLES;
```

Only views:

```sql
SHOW FULL TABLES
WHERE Table_type = 'VIEW';
```

## 28.2 Inspect the Definition

```sql
SHOW CREATE VIEW employee_details;
```

This displays the saved view definition and relevant creation characteristics.

## 28.3 Inspect Columns

```sql
DESC employee_details;
```

This displays the view's exposed columns.

## 28.4 Inspect INFORMATION_SCHEMA

```sql
SELECT
    TABLE_SCHEMA,
    TABLE_NAME,
    VIEW_DEFINITION,
    IS_UPDATABLE,
    CHECK_OPTION,
    SECURITY_TYPE,
    DEFINER
FROM INFORMATION_SCHEMA.VIEWS
WHERE TABLE_SCHEMA = DATABASE();
```

Useful metadata includes:

- `TABLE_SCHEMA`: schema containing the view.
- `TABLE_NAME`: view name.
- `VIEW_DEFINITION`: defining SELECT query, when available.
- `IS_UPDATABLE`: whether the view is considered updatable.
- `CHECK_OPTION`: check-option configuration.
- `SECURITY_TYPE`: security context.
- `DEFINER`: account associated with the definition.

The metadata visible to an account depends on privileges and server configuration.

---

# 29. PERMISSIONS AND ACCESS CONTROL

Views can be used to grant users access to a limited representation of data.

Suppose a user should read employee names and departments but not salaries.

Create the view:

```sql
CREATE VIEW employee_public AS
SELECT employee_id, name, department
FROM employees;
```

A database administrator can grant SELECT on the view:

```sql
GRANT SELECT
ON your_database.employee_public
TO 'report_user'@'localhost';
```

The user must have the appropriate privileges to connect and query the view. Under `SQL SECURITY DEFINER`, the view's execution context must also be configured correctly.

### Important

Do not grant direct SELECT privileges on the underlying `employees` table if the intention is to prevent the user from reading its salary column.

Test the actual permissions using the intended account.

---

# 30. ADVANTAGES AND DISADVANTAGES

## Advantages

1. **Simplification:** Complex queries become reusable objects.
2. **Security:** Views can expose only selected columns or rows.
3. **Consistency:** Common business logic can be defined centrally.
4. **Abstraction:** Consumers need not understand the full schema.
5. **Maintainability:** Query logic can be reused across reports and applications.
6. **Convenience:** Users can query a view using familiar SELECT syntax.

## Disadvantages and Limitations

1. Ordinary views do not permanently store their result rows.
2. Views do not automatically make queries faster.
3. Some views are not updatable.
4. Complex nested views can be difficult to debug.
5. Underlying schema changes can break views.
6. Incorrect security configuration can expose sensitive data.
7. Views cannot have ordinary indexes created directly on them in MySQL.
8. A view can hide complexity without eliminating the underlying execution cost.
9. Changing the view's column structure can break dependent applications.
10. MySQL does not natively provide persistent materialized views in the same way some other database systems do.

---

# 31. COMMON MISTAKES

## Mistake 1: Treating a View Like a Permanent Data Copy

Incorrect assumption:

> If I create a view today, it permanently stores today's result.

Correction: An ordinary view stores its definition. Its query normally reads underlying data when executed.

## Mistake 2: Assuming Every View Is Updatable

A view containing `GROUP BY`, aggregate functions, or `UNION` is not ordinarily updatable.

## Mistake 3: Forgetting CHECK OPTION

A filtered view can allow modifications that make a row disappear from the view unless the relevant check option is applied.

## Mistake 4: Assuming ORDER BY Always Guarantees the Final Output Order

Specify `ORDER BY` in the outermost query when the final row order matters.

## Mistake 5: Dropping a View Instead of a Table

```sql
DROP VIEW employee_details;
```

removes the view definition.

```sql
DROP TABLE employees;
```

removes the table and its stored records.

These are very different operations.

## Mistake 6: Assuming a View Automatically Hides Data from Every User

If a user has direct access to the underlying table, the user may still query columns hidden by the view.

## Mistake 7: Assuming a Join View Can Update Multiple Tables at Once

MySQL's join-view rules restrict modifications. Do not assume one UPDATE statement through a join view can modify multiple underlying tables.

## Mistake 8: Using Duplicate View Column Names

Give each output column a unique name.

## Mistake 9: Assuming Views Are Always Faster

Performance must be evaluated using the underlying query and execution plan.

## Mistake 10: Forgetting Dependencies

A change to a base table or another view can affect dependent views.

---

# 32. REAL-WORLD APPLICATIONS

## 32.1 Employee Reporting

```sql
CREATE VIEW employee_report AS
SELECT employee_id, name, department, salary
FROM employees;
```

## 32.2 Public Customer Information

```sql
CREATE VIEW customer_public AS
SELECT customer_id, customer_name, city
FROM customers;
```

## 32.3 Active Customers

```sql
CREATE VIEW active_customers AS
SELECT customer_id, customer_name, city
FROM customers
WHERE status = 'Active';
```

## 32.4 Sales Summary

```sql
CREATE VIEW sales_summary AS
SELECT
    customer_id,
    COUNT(*) AS total_orders,
    SUM(total_amount) AS total_sales
FROM orders
GROUP BY customer_id;
```

## 32.5 Order Details

```sql
CREATE VIEW order_details AS
SELECT
    o.order_id,
    c.customer_name,
    o.order_date,
    o.total_amount
FROM orders AS o
INNER JOIN customers AS c
    ON o.customer_id = c.customer_id;
```

## 32.6 High-Value Customers

```sql
CREATE VIEW high_value_customers AS
SELECT customer_id, customer_name, total_purchases
FROM customers
WHERE total_purchases >= 100000;
```

These examples demonstrate how views can provide consistent interfaces for reporting, filtering, and data access.

---

# 33. INTERVIEW QUESTIONS AND DETAILED ANSWERS

## Q1. What is a View in MySQL?

A view is a named database object defined by a SELECT query. It presents data from underlying tables or views as a virtual table. An ordinary view stores the query definition rather than a separate, persistent copy of its result rows.

## Q2. Does a View Store Data?

An ordinary MySQL view does not store its result rows as an independent materialized dataset. It stores a query definition that is used to retrieve data from underlying objects.

## Q3. What Happens When Base-Table Data Changes?

A subsequent query against the view normally reflects the changed data visible to that query, subject to transaction isolation and the view's filtering logic.

## Q4. Can We Insert Data into a View?

Yes, if the view is insertable, the INSERT statement is valid, the required underlying values can be supplied or generated, and the necessary privileges and constraints are satisfied.

## Q5. Can We Update a View?

Yes, if the view is updatable for the operation being attempted. The modification affects the underlying table rather than an independent copy of the view result.

## Q6. Why Is a GROUP BY View Generally Not Updatable?

Because each output row represents an aggregate of multiple source rows. MySQL cannot treat the summary row as an ordinary row that maps directly to one base-table record.

## Q7. What Is WITH CHECK OPTION?

It prevents inserts and updates through an updatable view from producing rows that violate the applicable view condition.

## Q8. What Is the Difference Between LOCAL and CASCADED CHECK OPTION?

`LOCAL` checks the current view's condition. `CASCADED` checks the current condition and relevant conditions in the underlying view chain. When the modifier is omitted, MySQL defaults to `CASCADED`.

## Q9. What Is the Difference Between MERGE and TEMPTABLE?

`MERGE` combines the view definition with the outer query when possible. `TEMPTABLE` materializes the view's result into an internal temporary table for processing. A TEMPTABLE view is not updatable.

## Q10. What Is SQL SECURITY DEFINER?

It specifies that the view executes with the privileges of the account identified as the definer, subject to the relevant privilege configuration.

## Q11. What Is SQL SECURITY INVOKER?

It specifies that the view executes using the privileges of the account that invokes it.

## Q12. Can a View Be Created from Another View?

Yes. A view can reference another view, creating a nested view structure. Dependencies and performance should be considered.

## Q13. Can We Create an Index Directly on a View?

No. MySQL 8.0 does not support ordinary indexes created directly on views. You can index the underlying tables where appropriate.

## Q14. Can a View Have ORDER BY?

Yes, a view definition can include ORDER BY, subject to the applicable rules. However, the final output order is not guaranteed for every outer query unless the outermost query specifies ORDER BY.

## Q15. What Is the Difference Between ALTER VIEW and CREATE OR REPLACE VIEW?

`ALTER VIEW` changes an existing view. `CREATE OR REPLACE VIEW` creates the view if it does not exist or replaces the existing definition if it does.

## Q16. What Happens When We Drop a View?

The view definition is removed. Its underlying tables and stored records remain. Dependent views may become invalid and should be checked.

## Q17. What Is the Difference Between a View and a Materialized View?

An ordinary view stores a query definition and derives its results from underlying data when queried. A materialized view stores a query result separately and requires some mechanism to refresh or maintain that result. MySQL 8.0 does not provide native materialized views.

## Q18. Are Views Faster Than Direct Queries?

Not necessarily. A view can simplify query writing, but performance depends on the definition, indexes, execution plan, optimizer, and data volume.

## Q19. Can a View Hide Sensitive Columns?

Yes, it can expose only selected columns. For this to serve as a security control, access privileges must be configured so users cannot bypass the view through direct access to the underlying table.

## Q20. Can We Use JOINs in a View?

Yes. A view can contain JOINs involving multiple tables or other views. Its updatability depends on MySQL's view rules.

---

# 34. PRACTICE PROBLEMS WITH SOLUTIONS

Use the `employees` table defined earlier unless a problem introduces other tables.

## Problem 1: Create a Basic View

**Question:** Create a view containing employee ID, name, and department.

```sql
CREATE VIEW employee_basic AS
SELECT employee_id, name, department
FROM employees;
```

Explanation: Only the three specified columns are exposed.

## Problem 2: Create an IT Employee View

```sql
CREATE VIEW it_employees AS
SELECT employee_id, name, salary
FROM employees
WHERE department = 'IT';
```

Explanation: The view returns only employees whose department is IT.

## Problem 3: Find Employees Earning More Than 50000 Through a View

```sql
SELECT *
FROM it_employees
WHERE salary > 50000;
```

Explanation: The view's condition and the outer query's condition both apply.

## Problem 4: Create an Annual Salary View

```sql
CREATE VIEW annual_salary AS
SELECT
    employee_id,
    name,
    salary * 12 AS yearly_salary
FROM employees;
```

Explanation: The yearly salary is calculated from the monthly salary.

## Problem 5: Create a Department-Wise Count View

```sql
CREATE VIEW department_count AS
SELECT department, COUNT(*) AS total_employees
FROM employees
GROUP BY department;
```

Explanation: The view summarizes employees by department and is not ordinarily updatable.

## Problem 6: Create a Department-Wise Average Salary View

```sql
CREATE VIEW department_average_salary AS
SELECT department, AVG(salary) AS average_salary
FROM employees
GROUP BY department;
```

## Problem 7: Modify a View

```sql
ALTER VIEW employee_basic AS
SELECT employee_id, name, department, city
FROM employees;
```

Explanation: The existing view now exposes the city column as well.

## Problem 8: Replace a View

```sql
CREATE OR REPLACE VIEW employee_basic AS
SELECT employee_id, name, salary
FROM employees;
```

Explanation: The definition is replaced with the new SELECT query.

## Problem 9: Update Data Through a View

```sql
UPDATE employee_basic
SET name = 'Rahul Sharma'
WHERE employee_id = 101;
```

Explanation: If the view is updatable and the operation is permitted, the underlying employee row is modified.

## Problem 10: Create a Filtered View with CHECK OPTION

```sql
CREATE OR REPLACE VIEW it_employees AS
SELECT employee_id, name, department, salary
FROM employees
WHERE department = 'IT'
WITH CHECK OPTION;
```

Explanation: Modifications through the view must preserve the IT condition.

## Problem 11: Create a View Using JOIN

Assume the `departments` table contains `department_id` and `department_name`.

```sql
CREATE VIEW employee_department_view AS
SELECT
    e.employee_id,
    e.name,
    d.department_name
FROM employees AS e
INNER JOIN departments AS d
    ON e.department_id = d.department_id;
```

Explanation: The view combines employee data with department names.

## Problem 12: Find the Highest Salary by Department

```sql
CREATE VIEW highest_salary_by_department AS
SELECT
    department,
    MAX(salary) AS highest_salary
FROM employees
GROUP BY department;
```

## Problem 13: Create a View with CASE

```sql
CREATE VIEW employee_salary_level AS
SELECT
    employee_id,
    name,
    salary,
    CASE
        WHEN salary >= 70000 THEN 'High'
        WHEN salary >= 50000 THEN 'Medium'
        ELSE 'Low'
    END AS salary_level
FROM employees;
```

## Problem 14: Inspect a View Definition

```sql
SHOW CREATE VIEW employee_basic;
```

Explanation: This displays the stored query definition and related creation characteristics.

## Problem 15: Delete a View Safely

```sql
DROP VIEW IF EXISTS employee_basic;
```

Explanation: Removes the view if it exists without dropping the underlying table.

## Problem 16: Create a View with DISTINCT

```sql
CREATE VIEW department_list AS
SELECT DISTINCT department
FROM employees;
```

Explanation: The view returns unique department values. It is not ordinarily updatable.

## Problem 17: Create a View with HAVING

```sql
CREATE VIEW departments_with_multiple_employees AS
SELECT department, COUNT(*) AS total_employees
FROM employees
GROUP BY department
HAVING COUNT(*) > 1;
```

Explanation: Only departments with more than one employee are included.

## Problem 18: Query a Nested View

Assume `employee_basic` exposes `employee_id`, `name`, `department`, and `salary`.

```sql
CREATE VIEW high_salary_employees AS
SELECT employee_id, name, department, salary
FROM employee_basic
WHERE salary > 50000;
```

Query:

```sql
SELECT *
FROM high_salary_employees
ORDER BY salary DESC;
```

Explanation: The second view uses the first view and adds a salary filter.

---

# 35. FINAL REVISION SHEET

## Essential Syntax

### Create a view

```sql
CREATE VIEW view_name AS
SELECT ...
FROM table_name;
```

### Create or replace a view

```sql
CREATE OR REPLACE VIEW view_name AS
SELECT ...;
```

### Alter a view

```sql
ALTER VIEW view_name AS
SELECT ...;
```

### Query a view

```sql
SELECT *
FROM view_name;
```

### Drop a view

```sql
DROP VIEW view_name;
```

### Drop safely

```sql
DROP VIEW IF EXISTS view_name;
```

### Inspect definition

```sql
SHOW CREATE VIEW view_name;
```

### Inspect columns

```sql
DESC view_name;
```

### Create an updatable filtered view with a check option

```sql
CREATE VIEW view_name AS
SELECT ...
FROM table_name
WHERE condition
WITH CHECK OPTION;
```

### Inspect view metadata

```sql
SELECT *
FROM INFORMATION_SCHEMA.VIEWS
WHERE TABLE_SCHEMA = DATABASE();
```

## Most Important Rules to Remember

1. A regular MySQL view is a named SELECT definition, not a separately stored result table.
2. A view can be based on one or multiple tables or other views.
3. The view's exposed columns can differ from the base table's columns.
4. A view can simplify queries and help restrict access.
5. A view does not automatically make queries faster.
6. Some views are updatable; others are not.
7. Aggregate, grouped, DISTINCT, and UNION views are not ordinarily updatable.
8. `WITH CHECK OPTION` helps prevent modifications that violate view conditions.
9. `LOCAL` and `CASCADED` determine how check conditions apply across nested views.
10. `MERGE`, `TEMPTABLE`, and `UNDEFINED` control or influence view processing.
11. `DEFINER` and `INVOKER` control the security context.
12. `ALTER VIEW` changes an existing view definition.
13. `CREATE OR REPLACE VIEW` creates or replaces a view definition.
14. `DROP VIEW` removes the view, not the underlying table.
15. Base-table schema changes can invalidate dependent views.
16. A view's final result order should be specified with an outer `ORDER BY` whenever ordering matters.
17. MySQL does not provide native materialized views in the same manner as some other database systems.
18. Views cannot have ordinary indexes created directly on them; underlying tables can be indexed.

**Final concept:** A MySQL view is a reusable, named query that presents underlying data through a virtual table interface. To use views correctly, understand not only how to create and query them, but also their updatability rules, security context, check options, algorithms, performance characteristics, and dependencies.
