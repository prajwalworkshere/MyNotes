# SQL Window Functions – Complete Beginner to Advanced Guide with Practical Examples and Interview Preparation

## Part 1 – Fundamentals and Complete Syntax

# Chapter 1: Introduction to SQL Window Functions

## 1.1 What Are SQL Window Functions?

A **Window Function** is a SQL function that performs a calculation across a set of related rows, called a *window*, while preserving the individual rows in the result.

In simpler terms:

A window function allows us to perform calculations across multiple rows without combining those rows into a single result row.

It can calculate:

- Ranking of employees.
- Running totals.
- Cumulative averages.
- Department-wise salary averages.
- Previous and next transaction values.
- Month-over-month sales growth.
- Top N records within each category.
- Percentage contributions.
- Moving averages.
- Cumulative percentages.

The most important characteristic is:

**Window functions perform calculations across related rows but do not eliminate individual rows from the result.**

Let's understand why this matters.

## 1.2 Understanding the Problem Before Window Functions

Suppose we have an employee table.

### Sample table: employees

| employee_id | employee_name | department | salary |
|---:|---|---|---:|
| 101 | Amit | IT | 60000 |
| 102 | Rahul | IT | 80000 |
| 103 | Priya | IT | 70000 |
| 104 | Neha | HR | 50000 |
| 105 | Karan | HR | 65000 |
| 106 | Sneha | HR | 55000 |
| 107 | Arjun | Sales | 45000 |
| 108 | Riya | Sales | 75000 |

Suppose we want to calculate the average salary of each department.

### Approach 1: GROUP BY

```sql
SELECT
    department,
    AVG(salary) AS avg_salary
FROM employees
GROUP BY department;
```

Expected output:

| department | avg_salary |
|---|---:|
| HR | 56666.67 |
| IT | 70000.00 |
| Sales | 60000.00 |

This query works correctly.

However, notice something important:

We originally had 8 employee records, but the result contains only 3 rows.

Why?

Because GROUP BY combines rows belonging to the same group into a single result row.

This is useful when we need summarized information, but problematic when we need both:

1. Every employee's individual information.
2. The average salary of that employee's department.

Can GROUP BY directly provide both without additional joins or subqueries?

Not in the straightforward way we need.

### Approach 2: Window Function

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,
    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_avg_salary
FROM employees;
```

Expected output:

| employee_id | employee_name | department | salary | department_avg_salary |
|---:|---|---|---:|---:|
| 101 | Amit | IT | 60000 | 70000.00 |
| 102 | Rahul | IT | 80000 | 70000.00 |
| 103 | Priya | IT | 70000 | 70000.00 |
| 104 | Neha | HR | 50000 | 56666.67 |
| 105 | Karan | HR | 65000 | 56666.67 |
| 106 | Sneha | HR | 55000 | 56666.67 |
| 107 | Arjun | Sales | 45000 | 60000.00 |
| 108 | Riya | Sales | 75000 | 60000.00 |

Observe the difference:

- Original table: 8 rows.
- GROUP BY result: 3 rows.
- Window function result: 8 rows.

Every employee remains present, but each employee also receives the average salary of their department.

This is the fundamental purpose of window functions.

### Important conceptual distinction

GROUP BY answers:

"What is the average salary of each department?"

A window function answers:

"What is the average salary of the department, and how does that average relate to each individual employee?"

The second question requires individual-level information and group-level calculations simultaneously.

---

## 1.3 Why Were Window Functions Introduced?

Before window functions became available in SQL implementations, developers commonly used:

- GROUP BY queries.
- Self-joins.
- Correlated subqueries.
- Derived tables.
- Multiple nested queries.

These approaches could solve analytical problems, but often required complicated SQL.

Consider this problem:

Find every employee whose salary is greater than the average salary of their department.

A traditional approach uses a correlated subquery:

```sql
SELECT
    e.employee_id,
    e.employee_name,
    e.department,
    e.salary
FROM employees e
WHERE e.salary > (
    SELECT AVG(e2.salary)
    FROM employees e2
    WHERE e2.department = e.department
);
```

A window function approach:

```sql
SELECT *
FROM (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,
        AVG(salary) OVER (
            PARTITION BY department
        ) AS department_avg
    FROM employees
) AS department_analysis
WHERE salary > department_avg;
```

Both can solve the problem.

However, the window function version makes the analytical calculation explicit and allows us to reuse the calculated value in further analysis.

Window functions provide a structured way to express analytical operations directly in SQL.

### Main motivations

1. Preserve individual records while calculating group-level statistics.
2. Perform ranking without complicated self-joins.
3. Calculate running and cumulative values.
4. Compare current rows with previous or subsequent rows.
5. Perform moving-window calculations.
6. Calculate relative positions and statistical distributions.
7. Simplify analytical queries.
8. Reduce the need for multiple intermediate aggregation queries.

---

## 1.4 Window Functions vs Aggregate Functions

This is one of the most important concepts for interviews.

An ordinary aggregate function combines multiple input rows into a summarized result.

A window aggregate function calculates an aggregate across related rows but returns a value for each row.

### Example: Ordinary aggregate

```sql
SELECT
    department,
    SUM(salary) AS total_salary
FROM employees
GROUP BY department;
```

Output:

| department | total_salary |
|---|---:|
| HR | 170000 |
| IT | 210000 |
| Sales | 120000 |

### Example: Aggregate as a window function

```sql
SELECT
    employee_name,
    department,
    salary,
    SUM(salary) OVER (
        PARTITION BY department
    ) AS department_total
FROM employees;
```

Output:

| employee_name | department | salary | department_total |
|---|---|---:|---:|
| Amit | IT | 60000 | 210000 |
| Rahul | IT | 80000 | 210000 |
| Priya | IT | 70000 | 210000 |
| Neha | HR | 50000 | 170000 |
| Karan | HR | 65000 | 170000 |
| Sneha | HR | 55000 | 170000 |
| Arjun | Sales | 45000 | 120000 |
| Riya | Sales | 75000 | 120000 |

### Detailed comparison

| Feature | Aggregate function with GROUP BY | Aggregate window function |
|---|---|---|
| Purpose | Summarization | Analysis |
| Row preservation | Usually reduces rows | Preserves input rows |
| Grouping | GROUP BY | PARTITION BY |
| Individual records | Generally not retained | Retained |
| Ranking | Not directly | Supported through ranking functions |
| Running totals | Requires additional techniques | Natural use case |
| Previous row comparison | Not directly | LAG() |
| Department total per employee | Requires join/subquery | Directly supported |

**Important:** Aggregate functions can be used either as ordinary aggregates or as window aggregates.

For example:

```sql
SUM(salary)
```

is an ordinary aggregate expression.

```sql
SUM(salary) OVER (
    PARTITION BY department
)
```

is a window aggregate expression.

The OVER() clause changes how the function operates.

---

## 1.5 Window Functions vs GROUP BY vs Subqueries vs Self-Joins

### A. Window Functions vs GROUP BY

| GROUP BY | Window Functions |
|---|---|
| Creates groups and summarizes them | Creates logical windows over rows |
| Usually produces one row per group | Preserves individual input rows |
| Used for aggregate reporting | Used for analytical calculations |
| Uses aggregate functions | Uses ranking, navigation, distribution and aggregate functions |

### B. Window Functions vs Subqueries

A subquery is a query nested inside another query.

Window functions are not a replacement for all subqueries.

They solve different problems, although some analytical problems can be expressed using either technique.

Example using a subquery:

```sql
SELECT *
FROM employees
WHERE salary > (
    SELECT AVG(salary)
    FROM employees
);
```

Equivalent analytical calculation using a window function:

```sql
SELECT *
FROM (
    SELECT
        employee_id,
        employee_name,
        salary,
        AVG(salary) OVER () AS overall_average
    FROM employees
) AS salary_analysis
WHERE salary > overall_average;
```

The window function calculates the overall average alongside every employee record.

### C. Window Functions vs Self-Joins

A self-join joins a table with itself.

It is frequently used to compare related records.

For example, suppose we want to compare each employee's salary with the salary of the previous employee in salary order.

Using a self-join can become complicated because we must identify the previous row using a relationship or ordering condition.

Using LAG():

```sql
SELECT
    employee_name,
    salary,
    LAG(salary) OVER (
        ORDER BY salary
    ) AS previous_salary
FROM employees;
```

This expresses the requirement more naturally.

### Comparison

| Requirement | Suitable approach |
|---|---|
| Department-wise salary totals | GROUP BY |
| Department average alongside employees | Window function |
| Previous transaction amount | LAG() |
| Next transaction amount | LEAD() |
| Employee hierarchy | Self-join or recursive CTE |
| Top three employees per department | ROW_NUMBER(), RANK() or DENSE_RANK() |
| Running account balance | SUM() OVER() |
| Find records above an overall average | Window function or subquery |

**Interview point:** Window functions are often more readable for analytical problems, but they are not automatically faster than joins or subqueries. Performance depends on the execution plan, indexes, partition sizes and data distribution.

---

## 1.6 Real-World Applications of Window Functions

Window functions are extensively used in analytics, finance, e-commerce, reporting and data engineering.

| Domain | Business requirement | Window function |
|---|---|---|
| HR | Rank employees by salary | RANK() |
| HR | Find top three employees per department | ROW_NUMBER() |
| Banking | Calculate running account balance | SUM() |
| E-commerce | Rank products by category sales | DENSE_RANK() |
| E-commerce | Find each customer's previous purchase | LAG() |
| Finance | Calculate moving averages | AVG() |
| Marketing | Calculate cumulative campaign conversions | SUM() |
| Analytics | Divide users into quartiles | NTILE() |
| Web analytics | Identify user sessions | LAG() |
| Data cleaning | Remove duplicate records | ROW_NUMBER() |
| Business intelligence | Calculate percentage contributions | SUM() OVER() |
| Time-series analysis | Calculate month-over-month growth | LAG() |

---

## 1.7 Which Databases Support Window Functions?

Window functions are supported by all major modern relational and analytical SQL platforms.

| Database | Support | Important considerations |
|---|---|---|
| MySQL | MySQL 8.0+ | Supports ranking, aggregate, navigation and value functions; no QUALIFY clause |
| PostgreSQL | Modern supported releases | Extensive window-frame support, including GROUPS and advanced frame features |
| SQL Server | Modern supported releases | Supports ROWS and RANGE, but not GROUPS |
| Oracle | Modern supported releases | Extensive analytical function support |
| Snowflake | Supported | Supports QUALIFY and advanced analytical SQL |
| BigQuery | Supported | Supports QUALIFY, named windows and advanced analytical functions |

Do not assume every database supports identical syntax or function options.

We will study the dialect-specific differences in a dedicated chapter.

---

# Chapter 2: Understanding the OVER() Clause

## 2.1 What Is OVER()?

The OVER() clause is the most important component of window functions.

It specifies the window of rows over which a window function performs its calculation.

In other words:

OVER() tells SQL which rows should participate in the calculation for the current row.

Without understanding OVER(), it is impossible to understand window functions properly.

### Basic syntax

```sql
function_name(expression) OVER (
    PARTITION BY column_name
    ORDER BY column_name
    window_frame
);
```

All components inside OVER() are optional depending on the calculation.

For example:

```sql
SELECT
    employee_name,
    salary,
    AVG(salary) OVER () AS overall_average
FROM employees;
```

Here:

- AVG(salary) is the window function.
- OVER() defines the window.
- No PARTITION BY means all rows belong to one partition.
- No ORDER BY means no ordering is specified within the window.
- No explicit frame is specified.

The result is the overall average salary repeated for every employee.

### Output

| employee_name | salary | overall_average |
|---|---:|---:|
| Amit | 60000 | 62500 |
| Rahul | 80000 | 62500 |
| Priya | 70000 | 62500 |
| Neha | 50000 | 62500 |
| Karan | 65000 | 62500 |
| Sneha | 55000 | 62500 |
| Arjun | 45000 | 62500 |
| Riya | 75000 | 62500 |

The average is:

```text
(60000 + 80000 + 70000 + 50000
 + 65000 + 55000 + 45000 + 75000) / 8

= 500000 / 8

= 62500
```

Every row receives the same result because all rows participate in the same window.

## 2.2 Complete Breakdown of Window Function Syntax

Consider:

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,

    SUM(salary) OVER (
        PARTITION BY department
        ORDER BY salary
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS running_salary

FROM employees;
```

Let's break it down.

| Component | Meaning |
|---|---|
| SUM() | Aggregate calculation |
| salary | Expression being aggregated |
| OVER() | Defines the analytical window |
| PARTITION BY department | Separates rows by department |
| ORDER BY salary | Defines the logical order inside each partition |
| ROWS | Specifies a physical-row-based frame |
| BETWEEN | Defines the frame boundaries |
| UNBOUNDED PRECEDING | Starts from the first row in the partition |
| AND | Separates the starting and ending boundaries |
| CURRENT ROW | Ends the frame at the current row |

The query calculates a running salary total within each department.

We will study window frames in depth in Part 2.

---

# Chapter 3: PARTITION BY – Dividing Rows into Independent Groups

## 3.1 What Is PARTITION BY?

PARTITION BY divides the rows of a result set into separate logical groups called partitions.

A window function then performs its calculation independently within each partition.

It is conceptually similar to GROUP BY in that it identifies groups based on column values.

However, there is one major difference:

PARTITION BY does not collapse the individual rows.

### Syntax

```sql
function_name(expression) OVER (
    PARTITION BY column_name
);
```

### Example 1: Department-wise average salary

```sql
SELECT
    employee_name,
    department,
    salary,
    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_average
FROM employees;
```

### Expected output

| employee_name | department | salary | department_average |
|---|---|---:|---:|
| Amit | IT | 60000 | 70000 |
| Rahul | IT | 80000 | 70000 |
| Priya | IT | 70000 | 70000 |
| Neha | HR | 50000 | 56666.67 |
| Karan | HR | 65000 | 56666.67 |
| Sneha | HR | 55000 | 56666.67 |
| Arjun | Sales | 45000 | 60000 |
| Riya | Sales | 75000 | 60000 |

### How SQL logically evaluates this calculation

**Step 1:** SQL obtains the rows from employees.

**Step 2:** PARTITION BY department divides the rows.

```text
IT Partition
--------------------------------
Amit     60000
Rahul    80000
Priya    70000

HR Partition
--------------------------------
Neha     50000
Karan    65000
Sneha    55000

Sales Partition
--------------------------------
Arjun    45000
Riya     75000
```

**Step 3:** AVG(salary) calculates an average separately for each partition.

IT:

```text
(60000 + 80000 + 70000) / 3 = 70000
```

HR:

```text
(50000 + 65000 + 55000) / 3 = 56666.67
```

Sales:

```text
(45000 + 75000) / 2 = 60000
```

**Step 4:** The result is assigned to every row belonging to its respective partition.

Notice that SQL does not remove any employee.

## 3.2 Multiple Columns in PARTITION BY

We can partition using multiple columns.

This is useful when a single column does not uniquely identify the analytical group.

### Syntax

```sql
OVER (
    PARTITION BY column1, column2, column3
)
```

### Example: Partition by department and job role

Suppose we have the following table:

### employees

| employee_id | employee_name | department | job_role | salary |
|---:|---|---|---|---:|
| 1 | Amit | IT | Developer | 60000 |
| 2 | Rahul | IT | Developer | 80000 |
| 3 | Priya | IT | Tester | 50000 |
| 4 | Neha | HR | Recruiter | 45000 |
| 5 | Karan | HR | Recruiter | 55000 |
| 6 | Sneha | HR | Manager | 75000 |

Query:

```sql
SELECT
    employee_name,
    department,
    job_role,
    salary,

    AVG(salary) OVER (
        PARTITION BY department, job_role
    ) AS group_average

FROM employees;
```

Output:

| employee_name | department | job_role | salary | group_average |
|---|---|---|---:|---:|
| Amit | IT | Developer | 60000 | 70000 |
| Rahul | IT | Developer | 80000 | 70000 |
| Priya | IT | Tester | 50000 | 50000 |
| Neha | HR | Recruiter | 45000 | 50000 |
| Karan | HR | Recruiter | 55000 | 50000 |
| Sneha | HR | Manager | 75000 | 75000 |

### Why?

Because SQL forms partitions using the combination of both columns.

```text
(IT, Developer)
(IT, Tester)
(HR, Recruiter)
(HR, Manager)
```

Each unique combination becomes a separate partition.

## 3.3 PARTITION BY vs GROUP BY

| Feature | PARTITION BY | GROUP BY |
|---|---|---|
| Function | Defines window partitions | Groups rows for aggregation |
| Preserves individual rows | Yes | Generally no |
| Used inside | OVER() | SELECT query |
| Ranking support | Yes | No direct ranking operation |
| Running totals | Yes, with ordering and frames | Not directly |
| Multiple columns | Yes | Yes |
| Aggregate calculations | Yes | Yes |

Remember:

```sql
SELECT
    department,
    AVG(salary)
FROM employees
GROUP BY department;
```

Produces one row per department.

But:

```sql
SELECT
    employee_name,
    department,
    AVG(salary) OVER (
        PARTITION BY department
    )
FROM employees;
```

Preserves every employee.

## 3.4 What Happens Without PARTITION BY?

When PARTITION BY is omitted, all rows in the window belong to one partition.

Example:

```sql
SELECT
    employee_name,
    salary,
    SUM(salary) OVER () AS total_salary
FROM employees;
```

The total salary of the entire table is calculated and repeated for every employee.

This is particularly useful for:

- Overall averages.
- Overall totals.
- Percentage contribution.
- Global rankings.
- Comparisons against the entire dataset.

## 3.5 Important Interview Traps

**Trap 1: PARTITION BY does not sort rows.**

It only divides rows into logical groups.

**Trap 2: PARTITION BY does not reduce the number of rows.**

Unlike GROUP BY, it preserves the individual rows.

**Trap 3: Multiple partition columns create groups based on combinations.**

PARTITION BY department, job_role does not independently create one partition for department and another for job_role.

**Trap 4: NULL values in partition columns are grouped together.**

Rows with NULL in the same partitioning column combination belong to the same partition.

Example:

```sql
SELECT
    employee_id,
    department,
    COUNT(*) OVER (
        PARTITION BY department
    ) AS department_employee_count
FROM employees;
```

Employees with a NULL department are counted together in a NULL department partition.

---

# Chapter 4: ORDER BY Inside OVER()

## 4.1 What Is ORDER BY in a Window Function?

ORDER BY inside OVER() determines the logical sequence of rows within a window.

This sequence is important for functions that depend on row positions or ordering.

Examples:

- ROW_NUMBER()
- RANK()
- DENSE_RANK()
- LAG()
- LEAD()
- Running totals.
- Moving averages.
- FIRST_VALUE().
- LAST_VALUE().

### Syntax

```sql
function_name(expression) OVER (
    PARTITION BY column_name
    ORDER BY column_name ASC
);
```

Remember:

ORDER BY inside OVER() is different from ORDER BY at the end of the SQL query.

## 4.2 Example: Ranking Employees by Salary

```sql
SELECT
    employee_name,
    department,
    salary,
    ROW_NUMBER() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_position
FROM employees;
```

Output:

| employee_name | department | salary | salary_position |
|---|---|---:|---:|
| Rahul | IT | 80000 | 1 |
| Priya | IT | 70000 | 2 |
| Amit | IT | 60000 | 3 |
| Karan | HR | 65000 | 1 |
| Sneha | HR | 55000 | 2 |
| Neha | HR | 50000 | 3 |
| Riya | Sales | 75000 | 1 |
| Arjun | Sales | 45000 | 2 |

Within each department:

1. Employees are logically ordered by salary descending.
2. ROW_NUMBER() assigns sequential numbers.
3. The numbering starts again for each partition.

## 4.3 ASC vs DESC

ASC means ascending order.

```sql
ORDER BY salary ASC
```

Smallest salary first.

DESC means descending order.

```sql
ORDER BY salary DESC
```

Largest salary first.

For ranking the highest salary first, DESC is generally appropriate.

For calculating a chronological running balance, ASC on the transaction timestamp is generally appropriate.

## 4.4 Multiple Columns in ORDER BY

Suppose multiple employees receive the same salary.

If we order only by salary, their relative order may be unspecified.

To make the result deterministic, add a unique tie-breaker.

```sql
SELECT
    employee_id,
    employee_name,
    salary,
    ROW_NUMBER() OVER (
        ORDER BY salary DESC, employee_id ASC
    ) AS row_num
FROM employees;
```

Here:

1. Salary determines the primary ordering.
2. Employee ID resolves salary ties.

This is particularly important for ROW_NUMBER().

### Important distinction: ranking ties

For ROW_NUMBER(), the database assigns a different row number to each row, even if ordering values are identical.

For RANK() and DENSE_RANK(), rows with equal ordering values receive the same rank.

Adding a unique column to the ORDER BY of RANK() can eliminate ties.

For example:

```sql
RANK() OVER (
    ORDER BY salary DESC
)
```

Preserves salary ties.

But:

```sql
RANK() OVER (
    ORDER BY salary DESC, employee_id
)
```

Usually makes every employee's ordering combination unique, so employees no longer tie.

Do not add a unique tie-breaker blindly when the business requirement is to rank by salary and preserve equal-salary ties.

## 4.5 Window ORDER BY vs Final ORDER BY

This is an extremely important distinction.

Consider:

```sql
SELECT
    employee_name,
    salary,
    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS row_num
FROM employees;
```

The window ORDER BY determines row-number assignment.

It does not guarantee the order in which the final result is displayed.

If we want the displayed output sorted by salary, write:

```sql
SELECT
    employee_name,
    salary,
    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS row_num
FROM employees
ORDER BY salary DESC;
```

### Comparison

| ORDER BY location | Purpose |
|---|---|
| Inside OVER() | Determines logical window ordering |
| At the end of the query | Determines final output ordering |

**Interview trap:** Never assume that ORDER BY inside OVER() automatically sorts the final result.

## 4.6 What Happens Without ORDER BY?

Not every window function requires ORDER BY.

Example:

```sql
SELECT
    employee_name,
    salary,
    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_average
FROM employees;
```

This is valid.

It calculates a department-level average.

However, omitting ORDER BY changes the meaning of certain calculations.

For example:

```sql
SUM(salary) OVER (
    PARTITION BY department
)
```

Calculates the complete department salary total for every row.

Whereas:

```sql
SUM(salary) OVER (
    PARTITION BY department
    ORDER BY salary
)
```

Uses an ordered window and, in MySQL, normally introduces the default ordered frame. This can produce a cumulative calculation with peer-group behavior.

We will examine this in the dedicated window-frames chapter.

## 4.7 NULL Values in Window ORDER BY

SQL databases can have different default NULL ordering rules.

For example, PostgreSQL and MySQL do not use identical default NULL placement in every sort direction.

To avoid ambiguity, specify NULL placement explicitly where supported.

PostgreSQL:

```sql
ORDER BY salary DESC NULLS LAST
```

MySQL 8:

```sql
ORDER BY
    salary IS NULL,
    salary DESC
```

This sorts non-NULL salaries first, followed by NULL salaries.

**Important:** The NULL placement rules of the database should always be considered when ranking or navigating records containing NULL ordering values.

---

# Chapter 5: Named Windows and the WINDOW Clause

## 5.1 What Is a Named Window?

When several window functions use the same PARTITION BY and ORDER BY definitions, repeating the same window specification can make a query unnecessarily long.

SQL supports named windows so that a window definition can be declared once and reused.

A named window gives a reusable name to a window specification.

### Syntax

```sql
SELECT
    column_name,

    function1() OVER window_name,
    function2() OVER window_name

FROM table_name

WINDOW window_name AS (
    PARTITION BY column_name
    ORDER BY column_name
);
```

## 5.2 Example Without a Named Window

```sql
SELECT
    employee_name,
    department,
    salary,

    ROW_NUMBER() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS row_num,

    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_rank,

    DENSE_RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS dense_salary_rank

FROM employees;
```

Notice that the same window specification is repeated three times.

## 5.3 Same Example Using a Named Window

```sql
SELECT
    employee_name,
    department,
    salary,

    ROW_NUMBER() OVER department_window AS row_num,

    RANK() OVER department_window AS salary_rank,

    DENSE_RANK() OVER department_window AS dense_salary_rank

FROM employees

WINDOW department_window AS (
    PARTITION BY department
    ORDER BY salary DESC
);
```

The result is logically equivalent.

### Benefits

1. Reduces repetition.
2. Improves query readability.
3. Makes analytical definitions easier to maintain.
4. Reduces the chance of accidentally using inconsistent partition or ordering definitions.

### Important distinction

A named window is a reusable window definition, not a stored result.

It does not create a temporary table or permanently store calculations.

The database still executes the window functions as part of the query.

## 5.4 Multiple Named Windows

We can define multiple named windows in a query.

```sql
SELECT
    employee_name,
    department,
    salary,

    ROW_NUMBER() OVER salary_window AS salary_position,

    SUM(salary) OVER department_window AS department_total,

    AVG(salary) OVER department_window AS department_average

FROM employees

WINDOW
    salary_window AS (
        PARTITION BY department
        ORDER BY salary DESC
    ),

    department_window AS (
        PARTITION BY department
    );
```

Here:

- salary_window performs ordered analysis within departments.
- department_window calculates complete department-level statistics.

These windows serve different analytical purposes.

## 5.5 Named Window Inheritance

Some database systems support extending an existing named window.

For example, MySQL allows a named window to be referenced and extended under its window inheritance rules.

Example:

```sql
SELECT
    employee_name,
    department,
    salary,

    SUM(salary) OVER (
        base_window
        ORDER BY salary
    ) AS running_salary

FROM employees

WINDOW base_window AS (
    PARTITION BY department
);
```

This uses the base partition definition and adds an ordering definition.

### Important MySQL restriction

A named window cannot simply be extended in every possible way.

In particular:

- A referenced window cannot already contain a frame when another frame is added.
- A referenced window with an ORDER BY cannot be given another ORDER BY.
- Repeated or conflicting window clauses are not valid.

Check the target database's named-window inheritance rules before using more complicated definitions.

---

# Part 1 – Foundational Quick Revision

| Concept | Meaning |
|---|---|
| Window function | Performs calculations across related rows while preserving individual rows |
| OVER() | Defines the window |
| PARTITION BY | Divides rows into independent logical groups |
| ORDER BY inside OVER() | Defines logical ordering within a window |
| Final ORDER BY | Controls displayed result ordering |
| No PARTITION BY | All rows belong to one partition |
| No ORDER BY | No logical sequence is specified |
| Named window | Reusable window specification |
| GROUP BY | Groups and summarizes rows |
| Window aggregate | Calculates an aggregate without collapsing individual rows |

## Fundamental Syntax

```sql
SELECT
    column1,
    column2,

    function_name(expression) OVER (
        PARTITION BY partition_column
        ORDER BY ordering_column
        ROWS BETWEEN frame_start AND frame_end
    ) AS calculated_column

FROM table_name;
```

Remember that PARTITION BY, ORDER BY and the frame are optional components depending on the calculation.

## Five Fundamental Rules

1. A window function preserves individual rows in its input result.
2. PARTITION BY defines independent analytical groups.
3. ORDER BY inside OVER() defines logical ordering, not final presentation order.
4. A window function generally cannot be used directly in WHERE, GROUP BY or HAVING at the same query level.
5. An ordered aggregate window may have a default frame that changes the calculation. Never ignore frame behavior.

---

**End of Part 1 – Fundamentals and Complete Syntax**

Next: **Part 2 – Window Frames: The Most Important and Commonly Misunderstood Concept**, covering ROWS, RANGE, GROUPS, physical rows, peer groups, default frames, frame boundaries, duplicate ordering values, NULLs, running totals, moving averages and the difficult LAST_VALUE() behavior.
