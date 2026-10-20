# SQL WINDOW FUNCTIONS — COMPLETE A TO Z NOTES

**MySQL 8.0+ | Theory + Syntax + Examples + Outputs + Practice Questions**

## TABLE OF CONTENTS

1. Introduction to Window Functions
2. Demo Database and Sample Data
3. Window Function Syntax
4. Ranking Functions
5. Aggregate Window Functions
6. Value and Navigation Functions
7. Window Frames
8. How to Use Window Functions in Queries
9. Solved SQL Practice Questions
10. Interview Questions and Answers
11. Common Mistakes
12. Quick Revision Cheat Sheet
13. Final Learning Roadmap

---

# 1\. INTRODUCTION TO WINDOW FUNCTIONS

## What is a Window Function?

A window function performs a calculation across a set of related rows called a **window**, while preserving the individual rows in the result.

Unlike aggregate functions such as `SUM()` and `AVG()` used with `GROUP BY`, window functions do not normally combine multiple rows into a single row.

### Example: Aggregate Function vs Window Function

Using `GROUP BY`:

```sql
SELECT department, AVG(salary) AS avg_salary
FROM employees
GROUP BY department;
```

This returns one row per department.

Using a window function:

```sql
SELECT
    first_name,
    department,
    salary,
    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_avg_salary
FROM employees;
```

This returns every employee along with their department's average salary.

## Why Do We Use Window Functions?

Window functions are useful for:

- Ranking employees by salary.
- Finding the highest-paid employee in each department.
- Calculating running totals.
- Calculating moving averages.
- Comparing the current row with the previous or next row.
- Finding the first or last value in a group.
- Calculating cumulative percentages.
- Finding differences between consecutive records.
- Identifying the top N records within each group.
- Performing analytical calculations without losing individual rows.

## Important Concepts

| Concept | Meaning |
| --- | --- |
| Window function | A function that calculates across related rows |
| `OVER()` | Defines the window used for the calculation |
| `PARTITION BY` | Divides rows into separate groups |
| `ORDER BY` | Determines the order of rows within the window |
| Window frame | Specifies which rows around the current row are included |
| Current row | The row for which the function is calculating a result |

---

# 2\. DEMO DATABASE AND SAMPLE DATA

We will use the following `employees` table throughout these notes.

## Create the Table

```sql
CREATE TABLE employees (
    emp_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    department VARCHAR(50),
    salary DECIMAL(10,2),
    hire_date DATE
);
```

## Insert Sample Records

```sql
INSERT INTO employees
(emp_id, first_name, department, salary, hire_date)
VALUES
(1, 'Amit',   'IT',      65000, '2022-01-15'),
(2, 'Priya',  'HR',      55000, '2021-06-10'),
(3, 'Rahul',  'IT',      72000, '2020-03-20'),
(4, 'Neha',   'Finance', 60000, '2023-02-01'),
(5, 'Sneha',  'HR',      58000, '2022-08-12'),
(6, 'Arjun',  'IT',      72000, '2021-11-05'),
(7, 'Karan',  'Finance', 50000, '2024-01-10'),
(8, 'Meera',  'IT',      48000, '2023-05-18');
```

## View the Data

```sql
SELECT *
FROM employees
ORDER BY emp_id;
```

Expected data:

| emp\_id | first\_name | department | salary | hire\_date |
| --- | --- | --- | --- | --- |
| 1 | Amit | IT | 65000 | 2022-01-15 |
| 2 | Priya | HR | 55000 | 2021-06-10 |
| 3 | Rahul | IT | 72000 | 2020-03-20 |
| 4 | Neha | Finance | 60000 | 2023-02-01 |
| 5 | Sneha | HR | 58000 | 2022-08-12 |
| 6 | Arjun | IT | 72000 | 2021-11-05 |
| 7 | Karan | Finance | 50000 | 2024-01-10 |
| 8 | Meera | IT | 48000 | 2023-05-18 |

---

# 3\. WINDOW FUNCTION SYNTAX

## General Syntax

```sql
FUNCTION_NAME(expression) OVER (
    PARTITION BY column_name
    ORDER BY column_name
    ROWS BETWEEN frame_start AND frame_end
);
```

The `PARTITION BY`, `ORDER BY`, and frame clauses are optional depending on the function and the calculation required.

### Example

```sql
SELECT
    first_name,
    salary,
    SUM(salary) OVER () AS total_salary
FROM employees;
```

Expected output:

| first\_name | salary | total\_salary |
| --- | --- | --- |
| Amit | 65000 | 480000 |
| Priya | 55000 | 480000 |
| Rahul | 72000 | 480000 |
| Neha | 60000 | 480000 |
| Sneha | 58000 | 480000 |
| Arjun | 72000 | 480000 |
| Karan | 50000 | 480000 |
| Meera | 48000 | 480000 |

All eight rows remain in the result. The total salary is repeated for every row.

## 3.1 OVER()

`OVER()` tells SQL to calculate a function as a window function.

```sql
SELECT
    first_name,
    salary,
    SUM(salary) OVER () AS total_salary
FROM employees;
```

Because no partition is specified, all rows belong to one window.

## 3.2 PARTITION BY

`PARTITION BY` divides the rows into separate groups.

```sql
SELECT
    first_name,
    department,
    salary,
    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_avg
FROM employees;
```

Expected output:

| first\_name | department | salary | department\_avg |
| --- | --- | --- | --- |
| Amit | IT | 65000 | 64250 |
| Priya | HR | 55000 | 56500 |
| Rahul | IT | 72000 | 64250 |
| Neha | Finance | 60000 | 55000 |
| Sneha | HR | 58000 | 56500 |
| Arjun | IT | 72000 | 64250 |
| Karan | Finance | 50000 | 55000 |
| Meera | IT | 48000 | 64250 |

The department averages are:

- IT: 64,250
- HR: 56,500
- Finance: 55,000

## 3.3 ORDER BY Inside OVER()

The `ORDER BY` inside `OVER()` controls the order in which the window function processes rows.

```sql
SELECT
    first_name,
    salary,
    ROW_NUMBER() OVER (
        ORDER BY salary DESC, emp_id
    ) AS row_num
FROM employees;
```

The highest salary receives row number 1. The employee ID provides a consistent tie-breaker.

**Important:** `ORDER BY` inside `OVER()` does not guarantee the final output order. Use an outer `ORDER BY` when you need sorted results.

---

# 4\. RANKING FUNCTIONS

MySQL 8.0+ supports these main ranking functions:

1. `ROW_NUMBER()`
2. `RANK()`
3. `DENSE_RANK()`
4. `NTILE()`
5. `PERCENT_RANK()`
6. `CUME_DIST()`

## 4.1 ROW\_NUMBER()

### Theory

Assigns a unique sequential number to every row within a window.

The numbering begins at 1.

If multiple rows have the same salary, they still receive different row numbers.

### Syntax

```sqlsql
ROW_NUMBER() OVER (
    PARTITION BY column_name
    ORDER BY column_name
);
```

### Example

```sql
SELECT
    first_name,
    salary,
    ROW_NUMBER() OVER (
        ORDER BY salary DESC, emp_id
    ) AS row_num
FROM employees;
```

Expected output:

| first\_name | salary | row\_num |
| --- | --- | --- |
| Rahul | 72000 | 1 |
| Arjun | 72000 | 2 |
| Amit | 65000 | 3 |
| Neha | 60000 | 4 |
| Sneha | 58000 | 5 |
| Priya | 55000 | 6 |
| Karan | 50000 | 7 |
| Meera | 48000 | 8 |

### Real Query: Number Employees Within Each Department

```sql
SELECT
    first_name,
    department,
    salary,
    ROW_NUMBER() OVER (
        PARTITION BY department
        ORDER BY salary DESC, emp_id
    ) AS department_row_num
FROM employees;
```

Each department starts numbering from 1.

### Use Cases

- Finding the top employee in each department.
- Removing duplicate rows using a ranking subquery.
- Selecting the latest record per customer.
- Creating sequential numbers within groups.

---

## 4.2 RANK()

### Theory

Assigns a rank to each row. Rows with equal ordering values receive the same rank, and the next rank skips positions.

For example:

`1, 2, 2, 4`

### Syntax

```sqlsql
RANK() OVER (
    ORDER BY column_name DESC
);
```

### Example

```sql
SELECT
    first_name,
    salary,
    RANK() OVER (
        ORDER BY salary DESC
    ) AS salary_rank
FROM employees;
```

Expected output:

| first\_name | salary | salary\_rank |
| --- | --- | --- |
| Rahul | 72000 | 1 |
| Arjun | 72000 | 1 |
| Amit | 65000 | 3 |
| Neha | 60000 | 4 |
| Sneha | 58000 | 5 |
| Priya | 55000 | 6 |
| Karan | 50000 | 7 |
| Meera | 48000 | 8 |

Both Rahul and Arjun receive rank 1. The next employee receives rank 3.

### Use Case: Find the Highest-Paid Employees

```sql
SELECT *
FROM (
    SELECT
        first_name,
        department,
        salary,
        RANK() OVER (
            PARTITION BY department
            ORDER BY salary DESC
        ) AS salary_rank
    FROM employees
) AS ranked_employees
WHERE salary_rank = 1;
```

This returns every employee tied for the highest salary in their department.

---

## 4.3 DENSE\_RANK()

### Theory

Assigns ranks like `RANK()`, but it does not skip rank numbers after ties.

Example:

- `RANK()`: 1, 1, 3, 4
- `DENSE_RANK()`: 1, 1, 2, 3

### Syntax

```sqlsql
DENSE_RANK() OVER (
    ORDER BY column_name DESC
);
```

### Example

```sql
SELECT
    first_name,
    salary,
    DENSE_RANK() OVER (
        ORDER BY salary DESC
    ) AS dense_salary_rank
FROM employees;
```

Expected output:

| first\_name | salary | dense\_salary\_rank |
| --- | --- | --- |
| Rahul | 72000 | 1 |
| Arjun | 72000 | 1 |
| Amit | 65000 | 2 |
| Neha | 60000 | 3 |
| Sneha | 58000 | 4 |
| Priya | 55000 | 5 |
| Karan | 50000 | 6 |
| Meera | 48000 | 7 |

### Use Case: Find the Second-Highest Distinct Salary

```sql
SELECT *
FROM (
    SELECT
        first_name,
        salary,
        DENSE_RANK() OVER (
            ORDER BY salary DESC
        ) AS salary_rank
    FROM employees
) AS ranked_employees
WHERE salary_rank = 2;
```

Output:

| first\_name | salary | salary\_rank |
| --- | --- | --- |
| Amit | 65000 | 2 |

---

## 4.4 NTILE()

### Theory

Divides ordered rows into a specified number of approximately equal groups, called buckets.

### Syntax

```sqlsql
NTILE(number_of_buckets) OVER (
    ORDER BY column_name
);
```

### Example

```sql
SELECT
    first_name,
    salary,
    NTILE(4) OVER (
        ORDER BY salary DESC, emp_id
    ) AS salary_quartile
FROM employees;
```

Expected output:

| first\_name | salary | salary\_quartile |
| --- | --- | --- |
| Rahul | 72000 | 1 |
| Arjun | 72000 | 1 |
| Amit | 65000 | 2 |
| Neha | 60000 | 2 |
| Sneha | 58000 | 3 |
| Priya | 55000 | 3 |
| Karan | 50000 | 4 |
| Meera | 48000 | 4 |

Here, eight employees are divided into four buckets of two rows each.

### Use Cases

- Dividing customers into spending groups.
- Creating salary quartiles.
- Segmenting sales representatives by performance.
- Creating approximately equal-sized ranking groups.

---

## 4.5 PERCENT\_RANK()

### Theory

Calculates the relative rank of a row as a value between 0 and 1.

The formula is:

```
(rank - 1) / (total_rows - 1)
```

If the partition contains only one row, the result is 0.

### Syntax

```sqlsql
PERCENT_RANK() OVER (
    ORDER BY column_name
);
```

### Example

```sql
SELECT
    first_name,
    salary,
    ROUND(
        PERCENT_RANK() OVER (
            ORDER BY salary
        ),
        2
    ) AS percent_rank
FROM employees;
```

Expected output:

| first\_name | salary | percent\_rank |
| --- | --- | --- |
| Meera | 48000 | 0.00 |
| Karan | 50000 | 0.14 |
| Priya | 55000 | 0.29 |
| Sneha | 58000 | 0.43 |
| Neha | 60000 | 0.57 |
| Amit | 65000 | 0.71 |
| Rahul | 72000 | 0.86 |
| Arjun | 72000 | 0.86 |

Rows with equal salaries receive the same relative rank under this ordering.

### Use Case

Compare an employee's rank relative to the rows in a department or organization.

---

## 4.6 CUME\_DIST()

### Theory

Calculates the cumulative distribution of a row within a partition.

It returns a value greater than 0 and less than or equal to 1.

The calculation is:

```
Number of rows preceding or tied with the current row
------------------------------------------------------
Total number of rows in the partition
```

### Syntax

```sqlsql
CUME_DIST() OVER (
    ORDER BY column_name
);
```

### Example

```sql
SELECT
    first_name,
    salary,
    ROUND(
        CUME_DIST() OVER (
            ORDER BY salary
        ),
        2
    ) AS cumulative_distribution
FROM employees;
```

Expected output:

| first\_name | salary | cumulative\_distribution |
| --- | --- | --- |
| Meera | 48000 | 0.13 |
| Karan | 50000 | 0.25 |
| Priya | 55000 | 0.38 |
| Sneha | 58000 | 0.50 |
| Neha | 60000 | 0.63 |
| Amit | 65000 | 0.75 |
| Rahul | 72000 | 1.00 |
| Arjun | 72000 | 1.00 |

The two employees earning 72,000 both have a cumulative distribution of 1.00 because all rows have salaries less than or equal to 72,000.

### Use Case

- Analyzing salary distributions.
- Understanding the relative position of a value.
- Performing statistical ranking and distribution analysis.

---

# 5\. AGGREGATE WINDOW FUNCTIONS

Aggregate functions can be used as window functions by adding an `OVER()` clause.

Common examples include:

- `SUM()`
- `AVG()`
- `COUNT()`
- `MIN()`
- `MAX()`

## 5.1 SUM() OVER()

### Theory

Calculates the sum of values across the specified window while retaining individual rows.

### Syntax

```sqlsql
SUM(expression) OVER (
    PARTITION BY column_name
);
```

### Example: Total Salary of All Employees

```sql
SELECT
    first_name,
    salary,
    SUM(salary) OVER () AS total_salary
FROM employees;
```

Output for every row:

```
total_salary
480000
```

## 5.2 Department Salary Total

```sql
SELECT
    first_name,
    department,
    salary,
    SUM(salary) OVER (
        PARTITION BY department
    ) AS department_total
FROM employees;
```

Expected output:

| first\_name | department | salary | department\_total |
| --- | --- | --- | --- |
| Amit | IT | 65000 | 257000 |
| Rahul | IT | 72000 | 257000 |
| Arjun | IT | 72000 | 257000 |
| Meera | IT | 48000 | 257000 |
| Priya | HR | 55000 | 113000 |
| Sneha | HR | 58000 | 113000 |
| Neha | Finance | 60000 | 110000 |
| Karan | Finance | 50000 | 110000 |

The order of these rows is not guaranteed without an outer `ORDER BY`.

## 5.3 Running Total

A running total accumulates values as rows are processed in order.

```sql
SELECT
    first_name,
    salary,
    SUM(salary) OVER (
        ORDER BY emp_id
        ROWS BETWEEN UNBOUNDED PRECEDING
                 AND CURRENT ROW
    ) AS running_total
FROM employees
ORDER BY emp_id;
```

Expected output:

| emp\_id | first\_name | salary | running\_total |
| --- | --- | --- | --- |
| 1 | Amit | 65000 | 65000 |
| 2 | Priya | 55000 | 120000 |
| 3 | Rahul | 72000 | 192000 |
| 4 | Neha | 60000 | 252000 |
| 5 | Sneha | 58000 | 310000 |
| 6 | Arjun | 72000 | 382000 |
| 7 | Karan | 50000 | 432000 |
| 8 | Meera | 48000 | 480000 |

### Explanation

- Row 1: 65,000
- Row 2: 65,000 + 55,000 = 120,000
- Row 3: 120,000 + 72,000 = 192,000

The process continues until all salaries have been accumulated.

## 5.4 AVG() OVER()

Calculates an average without removing individual rows.

```sql
SELECT
    first_name,
    department,
    salary,
    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_avg_salary
FROM employees;
```

Use this when each employee must be compared with the average salary of their department.

## 5.5 COUNT() OVER()

Counts rows within a window.

```sql
SELECT
    first_name,
    department,
    COUNT(*) OVER (
        PARTITION BY department
    ) AS department_employee_count
FROM employees;
```

Expected output:

| first\_name | department | department\_employee\_count |
| --- | --- | --- |
| Amit | IT | 4 |
| Rahul | IT | 4 |
| Arjun | IT | 4 |
| Meera | IT | 4 |
| Priya | HR | 2 |
| Sneha | HR | 2 |
| Neha | Finance | 2 |
| Karan | Finance | 2 |

## 5.6 MIN() OVER()

Finds the minimum value within the window.

```sql
SELECT
    first_name,
    department,
    salary,
    MIN(salary) OVER (
        PARTITION BY department
    ) AS minimum_department_salary
FROM employees;
```

For IT, the minimum salary is 48,000.

## 5.7 MAX() OVER()

Finds the maximum value within the window.

```sql
SELECT
    first_name,
    department,
    salary,
    MAX(salary) OVER (
        PARTITION BY department
    ) AS maximum_department_salary
FROM employees;
```

For IT, the maximum salary is 72,000.

## 5.8 Calculate Each Employee's Percentage of Department Payroll

```sql
SELECT
    first_name,
    department,
    salary,
    ROUND(
        salary * 100.0 /
        SUM(salary) OVER (
            PARTITION BY department
        ),
        2
    ) AS payroll_percentage
FROM employees;
```

This measures the employee's salary as a percentage of their department's total salary.

For Amit:

```
65000 / 257000 * 100 = 25.29%
```

---

# 6\. VALUE AND NAVIGATION FUNCTIONS

These functions retrieve values from particular rows within a window.

The main functions are:

- `LAG()`
- `LEAD()`
- `FIRST_VALUE()`
- `LAST_VALUE()`
- `NTH_VALUE()`

## 6.1 LAG()

### Theory

Returns a value from a preceding row without requiring a self-join.

### Syntax

```sqlsql
LAG(expression, offset, default_value) OVER (
    PARTITION BY column_name
    ORDER BY column_name
);
```

The offset and default value are optional. The default offset is 1.

### Example: Compare Salary with the Previous Employee

```sql
SELECT
    emp_id,
    first_name,
    salary,
    LAG(salary) OVER (
        ORDER BY emp_id
    ) AS previous_salary
FROM employees
ORDER BY emp_id;
```

Expected output:

| emp\_id | first\_name | salary | previous\_salary |
| --- | --- | --- | --- |
| 1 | Amit | 65000 | NULL |
| 2 | Priya | 55000 | 65000 |
| 3 | Rahul | 72000 | 55000 |
| 4 | Neha | 60000 | 72000 |
| 5 | Sneha | 58000 | 60000 |
| 6 | Arjun | 72000 | 58000 |
| 7 | Karan | 50000 | 72000 |
| 8 | Meera | 48000 | 50000 |

The first row has no preceding row, so its previous salary is `NULL`.

### Calculate the Salary Difference

```sql
SELECT
    emp_id,
    first_name,
    salary,
    LAG(salary) OVER (
        ORDER BY emp_id
    ) AS previous_salary,
    salary - LAG(salary) OVER (
        ORDER BY emp_id
    ) AS salary_difference
FROM employees
ORDER BY emp_id;
```

For Priya, the difference is:

```
55000 - 65000 = -10000
```

### Use Cases

- Comparing current and previous sales.
- Calculating month-to-month growth.
- Identifying changes between consecutive records.
- Comparing an employee with the previous employee in a department.

---

## 6.2 LEAD()

### Theory

Returns a value from a following row.

It is the opposite of `LAG()`.

### Syntax

```sqlsql
LEAD(expression, offset, default_value) OVER (
    PARTITION BY column_name
    ORDER BY column_name
);
```

### Example

```sql
SELECT
    emp_id,
    first_name,
    salary,
    LEAD(salary) OVER (
        ORDER BY emp_id
    ) AS next_salary
FROM employees
ORDER BY emp_id;
```

Expected output:

| emp\_id | first\_name | salary | next\_salary |
| --- | --- | --- | --- |
| 1 | Amit | 65000 | 55000 |
| 2 | Priya | 55000 | 72000 |
| 3 | Rahul | 72000 | 60000 |
| 4 | Neha | 60000 | 58000 |
| 5 | Sneha | 58000 | 72000 |
| 6 | Arjun | 72000 | 50000 |
| 7 | Karan | 50000 | 48000 |
| 8 | Meera | 48000 | NULL |

### Use Cases

- Comparing current and next month's sales.
- Finding upcoming events.
- Calculating differences between consecutive records.
- Analyzing time-series data.

---

## 6.3 FIRST\_VALUE()

### Theory

Returns the first value in the window frame.

### Syntax

```sqlsql
FIRST_VALUE(expression) OVER (
    PARTITION BY column_name
    ORDER BY column_name
);
```

### Example: Highest Salary in Each Department

```sql
SELECT
    first_name,
    department,
    salary,
    FIRST_VALUE(salary) OVER (
        PARTITION BY department
        ORDER BY salary DESC, emp_id
    ) AS highest_salary
FROM employees;
```

Every employee receives the highest salary in their department.

For IT, the result is 72,000.

For HR, the result is 58,000.

For Finance, the result is 60,000.

## 6.4 LAST\_VALUE()

### Theory

Returns the last value in the window frame.

A common mistake is assuming it always returns the final value in the entire partition. Its behavior depends on the frame.

### Example: Find the Lowest Salary in Each Department

```sql
SELECT
    first_name,
    department,
    salary,
    LAST_VALUE(salary) OVER (
        PARTITION BY department
        ORDER BY salary DESC, emp_id
        ROWS BETWEEN UNBOUNDED PRECEDING
                 AND UNBOUNDED FOLLOWING
    ) AS lowest_salary
FROM employees;
```

Expected department values:

| department | lowest\_salary |
| --- | --- |
| IT | 48000 |
| HR | 55000 |
| Finance | 50000 |

The explicit frame includes every row in the department, allowing `LAST_VALUE()` to return the final salary in the ordered partition.

## 6.5 NTH\_VALUE()

### Theory

Returns the value at a specified position in the window frame.

The position starts at 1.

### Syntax

```sqlsql
NTH_VALUE(expression, n) OVER (
    PARTITION BY column_name
    ORDER BY column_name
    ROWS BETWEEN UNBOUNDED PRECEDING
             AND UNBOUNDED FOLLOWING
);
```

### Example: Find the Second-Highest Salary in Each Department

```sql
SELECT
    first_name,
    department,
    salary,
    NTH_VALUE(salary, 2) OVER (
        PARTITION BY department
        ORDER BY salary DESC, emp_id
        ROWS BETWEEN UNBOUNDED PRECEDING
                 AND UNBOUNDED FOLLOWING
    ) AS second_salary
FROM employees;
```

For IT, the second ordered salary is 72,000 because Rahul and Arjun are tied. The `emp_id` tie-breaker determines their order.

For HR, the second salary is 55,000.

For Finance, the second salary is 50,000.

**Important:** `NTH_VALUE()` returns the nth row's value under the specified ordering, not necessarily the nth distinct value.

---

# 7\. WINDOW FRAMES

## What Is a Window Frame?

A window frame defines which rows around the current row are included in a window calculation.

It is particularly important for:

- Running totals.
- Moving averages.
- Cumulative calculations.
- `FIRST_VALUE()` and `LAST_VALUE()`.
- Aggregate window functions.

## Common Frame Boundaries

| Boundary | Meaning |
| --- | --- |
| `UNBOUNDED PRECEDING` | Start at the first row of the partition |
| `n PRECEDING` | Start or end a specified number of rows before the current row |
| `CURRENT ROW` | The current row, subject to frame semantics |
| `n FOLLOWING` | A specified number of rows after the current row |
| `UNBOUNDED FOLLOWING` | End at the last row of the partition |

## 7.1 Running Total Using ROWS

```sql
SELECT
    emp_id,
    first_name,
    salary,
    SUM(salary) OVER (
        ORDER BY emp_id
        ROWS BETWEEN UNBOUNDED PRECEDING
                 AND CURRENT ROW
    ) AS running_total
FROM employees
ORDER BY emp_id;
```

The frame expands from the first row through the current row.

## 7.2 Moving Average of the Current and Previous Two Employees

```sql
SELECT
    emp_id,
    first_name,
    salary,
    ROUND(
        AVG(salary) OVER (
            ORDER BY emp_id
            ROWS BETWEEN 2 PRECEDING
                     AND CURRENT ROW
        ),
        2
    ) AS moving_average
FROM employees
ORDER BY emp_id;
```

For employee 3, the calculation uses employees 1, 2, and 3:

```
(65000 + 55000 + 72000) / 3
= 64000
```

For the first row, only one row is available. For the second row, two rows are available.

## 7.3 ROWS vs RANGE

### ROWS

Counts physical rows relative to the current row.

```
ROWS BETWEEN 1 PRECEDING AND CURRENT ROW
```

This includes the current row and the immediately preceding row.

### RANGE

Uses ordering values and peer rows, which have equal ordering values, to determine the frame.

```
RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
```

When ordering by salary, rows tied on salary can be included together in the frame.

**Interview tip:** Use `ROWS` when you want an exact number of physical rows. Use `RANGE` when the frame should be based on ordering values and peer groups.

---

# 8\. HOW WINDOW FUNCTIONS ARE USED IN SQL QUERIES

## 8.1 Window Function in SELECT

```sql
SELECT
    first_name,
    salary,
    RANK() OVER (
        ORDER BY salary DESC
    ) AS salary_rank
FROM employees;
```

Displays each employee's salary rank.

## 8.2 Window Function with PARTITION BY

```sql
SELECT
    first_name,
    department,
    salary,
    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS department_rank
FROM employees;
```

Ranking restarts for each department.

## 8.3 Filtering Window Results

In MySQL 8.0, window functions cannot be used directly in the `WHERE` clause of the same query block.

Use a subquery or CTE.

### Example: Top Two Employees in Each Department

```
WITH ranked_employees AS (
    SELECT
        first_name,
        department,
        salary,
        ROW_NUMBER() OVER (
            PARTITION BY department
            ORDER BY salary DESC, emp_id
        ) AS rn
    FROM employees
)
SELECT *
FROM ranked_employees
WHERE rn <= 2;
```

This returns up to two employees per department.

Use `RANK()` instead of `ROW_NUMBER()` if you want to include everyone tied at the second rank.

## 8.4 Window Functions with GROUP BY

You can aggregate rows first and then apply a window function to the grouped results.

```sql
SELECT
    department,
    SUM(salary) AS department_salary,
    RANK() OVER (
        ORDER BY SUM(salary) DESC
    ) AS department_rank
FROM employees
GROUP BY department;
```

Expected output:

| department | department\_salary | department\_rank |
| --- | --- | --- |
| IT | 257000 | 1 |
| HR | 113000 | 2 |
| Finance | 110000 | 3 |

## 8.5 Window Functions with CTEs

A common table expression makes complex analytical queries easier to read.

```
WITH department_stats AS (
    SELECT
        first_name,
        department,
        salary,
        AVG(salary) OVER (
            PARTITION BY department
        ) AS department_avg
    FROM employees
)
SELECT *
FROM department_stats
WHERE salary > department_avg;
```

This returns employees earning more than their department's average salary.

## 8.6 Window Functions with CASE

```sql
SELECT
    first_name,
    salary,
    CASE
        WHEN salary >= 70000 THEN 'High'
        WHEN salary >= 55000 THEN 'Medium'
        ELSE 'Low'
    END AS salary_category
FROM employees;
```

`CASE` is not a window function, but it can be combined with window calculations to classify analytical results.

---

# 9\. SOLVED SQL PRACTICE QUESTIONS

Use the `employees` table created earlier.

## Question 1: Display each employee's salary rank.

```sql
SELECT
    first_name,
    salary,
    RANK() OVER (
        ORDER BY salary DESC
    ) AS salary_rank
FROM employees;
```

## Question 2: Find the highest-paid employee.

```
WITH ranked AS (
    SELECT
        first_name,
        department,
        salary,
        RANK() OVER (
            ORDER BY salary DESC
        ) AS rnk
    FROM employees
)
SELECT *
FROM ranked
WHERE rnk = 1;
```

Expected result: Rahul and Arjun, each earning 72,000.

## Question 3: Find the highest-paid employee in each department.

```
WITH ranked AS (
    SELECT
        first_name,
        department,
        salary,
        RANK() OVER (
            PARTITION BY department
            ORDER BY salary DESC
        ) AS rnk
    FROM employees
)
SELECT *
FROM ranked
WHERE rnk = 1;
```

Expected result:

- IT: Rahul and Arjun
- HR: Sneha
- Finance: Neha

## Question 4: Find the second-highest distinct salary.

```
WITH ranked AS (
    SELECT
        first_name,
        salary,
        DENSE_RANK() OVER (
            ORDER BY salary DESC
        ) AS rnk
    FROM employees
)
SELECT *
FROM ranked
WHERE rnk = 2;
```

Expected result: Amit, salary 65,000.

## Question 5: Find the second-highest distinct salary in each department.

```
WITH ranked AS (
    SELECT
        first_name,
        department,
        salary,
        DENSE_RANK() OVER (
            PARTITION BY department
            ORDER BY salary DESC
        ) AS rnk
    FROM employees
)
SELECT *
FROM ranked
WHERE rnk = 2;
```

Expected results:

| first\_name | department | salary |
| --- | --- | --- |
| Arjun or Rahul, depending on the distinct salary interpretation | IT | 72000 |
| Priya | HR | 55000 |
| Karan | Finance | 50000 |

**Correction for the IT department:** Rahul and Arjun tie for the highest salary. Since the second-highest _distinct_ IT salary is 65,000, the correct IT result is Amit, not Rahul or Arjun.

Correct expected results:

| first\_name | department | salary |
| --- | --- | --- |
| Amit | IT | 65000 |
| Priya | HR | 55000 |
| Karan | Finance | 50000 |

## Question 6: Number employees within each department.

```sql
SELECT
    first_name,
    department,
    ROW_NUMBER() OVER (
        PARTITION BY department
        ORDER BY emp_id
    ) AS employee_number
FROM employees;
```

## Question 7: Calculate the running total of salaries.

```sql
SELECT
    emp_id,
    first_name,
    salary,
    SUM(salary) OVER (
        ORDER BY emp_id
        ROWS BETWEEN UNBOUNDED PRECEDING
                 AND CURRENT ROW
    ) AS running_total
FROM employees
ORDER BY emp_id;
```

## Question 8: Calculate the average salary of each department.

```sql
SELECT
    first_name,
    department,
    salary,
    AVG(salary) OVER (
        PARTITION BY department
    ) AS avg_salary
FROM employees;
```

## Question 9: Find employees earning above their department's average.

```
WITH stats AS (
    SELECT
        first_name,
        department,
        salary,
        AVG(salary) OVER (
            PARTITION BY department
        ) AS avg_salary
    FROM employees
)
SELECT *
FROM stats
WHERE salary > avg_salary;
```

Expected employees:

- IT: Rahul and Arjun
- HR: Sneha
- Finance: Neha

## Question 10: Compare every salary with the previous employee's salary.

```sql
SELECT
    emp_id,
    first_name,
    salary,
    LAG(salary) OVER (
        ORDER BY emp_id
    ) AS previous_salary
FROM employees
ORDER BY emp_id;
```

## Question 11: Calculate the difference between the current and previous salary.

```sql
SELECT
    emp_id,
    first_name,
    salary,
    salary - LAG(salary) OVER (
        ORDER BY emp_id
    ) AS salary_difference
FROM employees
ORDER BY emp_id;
```

## Question 12: Divide employees into four salary groups.

```sql
SELECT
    first_name,
    salary,
    NTILE(4) OVER (
        ORDER BY salary DESC, emp_id
    ) AS salary_group
FROM employees;
```

## Question 13: Find the minimum and maximum salary for each department.

```sql
SELECT
    first_name,
    department,
    salary,
    MIN(salary) OVER (
        PARTITION BY department
    ) AS min_salary,
    MAX(salary) OVER (
        PARTITION BY department
    ) AS max_salary
FROM employees;
```

## Question 14: Count the employees in each department without grouping the output.

```sql
SELECT
    first_name,
    department,
    COUNT(*) OVER (
        PARTITION BY department
    ) AS department_count
FROM employees;
```

## Question 15: Find the top two salaries in each department, including ties.

```
WITH ranked AS (
    SELECT
        first_name,
        department,
        salary,
        RANK() OVER (
            PARTITION BY department
            ORDER BY salary DESC
        ) AS rnk
    FROM employees
)
SELECT *
FROM ranked
WHERE rnk <= 2;
```

Because tied salaries share a rank, this can return more than two employees for a department.

## Question 16: Find the first employee hired in each department.

```
WITH ordered_employees AS (
    SELECT
        first_name,
        department,
        hire_date,
        ROW_NUMBER() OVER (
            PARTITION BY department
            ORDER BY hire_date, emp_id
        ) AS rn
    FROM employees
)
SELECT *
FROM ordered_employees
WHERE rn = 1;
```

## Question 17: Find each employee's percentage of the company's total payroll.

```sql
SELECT
    first_name,
    salary,
    ROUND(
        salary * 100.0 / SUM(salary) OVER (),
        2
    ) AS payroll_percentage
FROM employees;
```

## Question 18: Calculate a moving average of three rows.

```sql
SELECT
    emp_id,
    first_name,
    salary,
    ROUND(
        AVG(salary) OVER (
            ORDER BY emp_id
            ROWS BETWEEN 2 PRECEDING
                     AND CURRENT ROW
        ),
        2
    ) AS moving_avg
FROM employees
ORDER BY emp_id;
```

## Question 19: Find the next employee's salary.

```sql
SELECT
    emp_id,
    first_name,
    salary,
    LEAD(salary) OVER (
        ORDER BY emp_id
    ) AS next_salary
FROM employees
ORDER BY emp_id;
```

## Question 20: Rank departments by total payroll.

```sql
SELECT
    department,
    SUM(salary) AS total_salary,
    DENSE_RANK() OVER (
        ORDER BY SUM(salary) DESC
    ) AS department_rank
FROM employees
GROUP BY department;
```

Expected result:

| department | total\_salary | department\_rank |
| --- | --- | --- |
| IT | 257000 | 1 |
| HR | 113000 | 2 |
| Finance | 110000 | 3 |

---

# 10\. INTERVIEW QUESTIONS AND ANSWERS

## Q1. What is a window function?

A window function performs a calculation across related rows without collapsing them into a single row per group.

Example:

```sql
SELECT
    first_name,
    SUM(salary) OVER () AS total_salary
FROM employees;
```

## Q2. What is the difference between GROUP BY and a window function?

- `GROUP BY` generally returns one row per group.
- A window function normally preserves individual rows while calculating over a group.

## Q3. What is the purpose of OVER()?

`OVER()` defines the window for a function.

```
SUM(salary) OVER ()
```

Calculates the sum across the entire result set visible to the window.

## Q4. What is PARTITION BY?

It divides rows into independent groups for window calculations.

```
AVG(salary) OVER (
    PARTITION BY department
)
```

Calculates an average separately for each department.

## Q5. Difference between ROW\_NUMBER(), RANK(), and DENSE\_RANK()?

| Function | Example with tied values |
| --- | --- |
| `ROW_NUMBER()` | 1, 2, 3, 4 |
| `RANK()` | 1, 1, 3, 4 |
| `DENSE_RANK()` | 1, 1, 2, 3 |

`ROW_NUMBER()` always assigns a different number to each row. `RANK()` skips ranks after ties. `DENSE_RANK()` does not skip ranks.

## Q6. What is the difference between LAG() and LEAD()?

- `LAG()` accesses a preceding row.
- `LEAD()` accesses a following row.

## Q7. Can a window function be used directly in WHERE?

Not in the same query block in MySQL 8.0.

Use a CTE or subquery to calculate the window result and then filter it.

## Q8. Can window functions be combined with aggregate functions?

Yes.

```sql
SELECT
    department,
    SUM(salary) AS total_salary,
    RANK() OVER (
        ORDER BY SUM(salary) DESC
    ) AS department_rank
FROM employees
GROUP BY department;
```

## Q9. What is a running total?

A cumulative sum calculated in a defined row order.

```
SUM(salary) OVER (
    ORDER BY emp_id
    ROWS BETWEEN UNBOUNDED PRECEDING
             AND CURRENT ROW
)
```

## Q10. What is a moving average?

An average calculated over a moving window of rows.

```
AVG(salary) OVER (
    ORDER BY emp_id
    ROWS BETWEEN 2 PRECEDING
             AND CURRENT ROW
)
```

## Q11. What is the difference between ROWS and RANGE?

- `ROWS` defines frames by physical row positions.
- `RANGE` defines frames using ordering values and peer groups.

## Q12. What does NTILE(4) do?

It divides ordered rows into four approximately equal-sized buckets.

## Q13. What is PERCENT\_RANK()?

It calculates the relative rank using:

```
(rank - 1) / (number_of_rows - 1)
```

For a single-row partition, the result is 0.

## Q14. What is CUME\_DIST()?

It calculates the proportion of rows whose ordering values are less than or equal to the current row's ordering value, including peers.

## Q15. Why can LAST\_VALUE() return an unexpected result?

Because it returns the last value in the current window frame, not necessarily the last row in the entire partition.

Use an explicit frame when you need the entire partition:

```
ROWS BETWEEN UNBOUNDED PRECEDING
         AND UNBOUNDED FOLLOWING
```

## Q16. How do you find the top three employees in each department?

```
WITH ranked AS (
    SELECT
        first_name,
        department,
        salary,
        ROW_NUMBER() OVER (
            PARTITION BY department
            ORDER BY salary DESC, emp_id
        ) AS rn
    FROM employees
)
SELECT *
FROM ranked
WHERE rn <= 3;
```

This returns up to three employees per department.

## Q17. How do you find duplicate records using window functions?

Suppose you want to identify repeated email addresses:

```
WITH duplicates AS (
    SELECT
        email,
        ROW_NUMBER() OVER (
            PARTITION BY email
            ORDER BY emp_id
        ) AS rn
    FROM employees
)
SELECT *
FROM duplicates
WHERE rn > 1;
```

This identifies rows beyond the first row for each email. The sample data contains no duplicate emails.

## Q18. Are window functions available in MySQL 5.7?

No. MySQL 8.0 introduced support for window functions. MySQL 5.7 requires alternative approaches for these calculations.

## Q19. What is the difference between FIRST\_VALUE() and MIN()?

- `FIRST_VALUE()` returns the value from the first row according to the specified ordering and frame.
- `MIN()` returns the smallest value in the window.

These can differ when the ordering is not based on the same expression as the value being retrieved.

## Q20. Does ORDER BY inside OVER() sort the final result?

No. It defines the ordering used by the window function. Use a separate outer `ORDER BY` to guarantee the final display order.

---

# 11\. COMMON MISTAKES

## Mistake 1: Forgetting PARTITION BY

Incorrect for department-specific ranking:

```
RANK() OVER (
    ORDER BY salary DESC
)
```

This ranks employees across the entire result set.

Correct:

```
RANK() OVER (
    PARTITION BY department
    ORDER BY salary DESC
)
```

## Mistake 2: Using ROW\_NUMBER() When Ties Must Be Preserved

If everyone tied for the highest salary must be returned, use `RANK()` rather than relying on `ROW_NUMBER()`.

## Mistake 3: Using Window Functions Directly in WHERE

Incorrect in the same MySQL query block:

```sql
SELECT *
FROM employees
WHERE ROW_NUMBER() OVER (
    ORDER BY salary DESC
) <= 3;
```

Correct approach:

```
WITH ranked AS (
    SELECT
        *,
        ROW_NUMBER() OVER (
            ORDER BY salary DESC
        ) AS rn
    FROM employees
)
SELECT *
FROM ranked
WHERE rn <= 3;
```

## Mistake 4: Forgetting a Tie-Breaker

This may assign row numbers in an unpredictable order when salaries are equal:

```
ROW_NUMBER() OVER (
    ORDER BY salary DESC
)
```

For repeatable numbering, add a unique tie-breaker:

```
ROW_NUMBER() OVER (
    ORDER BY salary DESC, emp_id
)
```

## Mistake 5: Confusing the Window Order with the Output Order

Always use an outer `ORDER BY` if the displayed rows must be sorted.

## Mistake 6: Misunderstanding LAST\_VALUE()

Without the correct frame, `LAST_VALUE()` might return the current row's value rather than the final value in the partition.

## Mistake 7: Forgetting NULL Values

`LAG()` and `LEAD()` return `NULL` when the requested preceding or following row does not exist and no default is supplied.

Example:

```
LAG(salary, 1, 0) OVER (
    ORDER BY emp_id
)
```

This uses 0 when no preceding row exists. Note that an existing row containing a `NULL` salary still returns `NULL`.

## Mistake 8: Using a Running Total Without Defining a Stable Order

If the order matters, specify an appropriate `ORDER BY` and a deterministic tie-breaker.

## Mistake 9: Assuming Every Ranking Function Returns Unique Ranks

`RANK()`, `DENSE_RANK()`, `PERCENT_RANK()`, and `CUME_DIST()` can return the same value for tied rows.

## Mistake 10: Ignoring Performance

Window functions may require sorting or buffering rows. Large datasets can benefit from appropriate indexes, reduced input row sets, and careful query design.

---

# 12\. QUICK REVISION CHEAT SHEET

| Function | Purpose | Example |
| --- | --- | --- |
| `ROW_NUMBER()` | Unique sequential row number | `ROW_NUMBER() OVER (ORDER BY salary DESC)` |
| `RANK()` | Rank with gaps after ties | `RANK() OVER (ORDER BY salary DESC)` |
| `DENSE_RANK()` | Rank without gaps after ties | `DENSE_RANK() OVER (ORDER BY salary DESC)` |
| `NTILE(n)` | Divide rows into buckets | `NTILE(4) OVER (ORDER BY salary)` |
| `PERCENT_RANK()` | Relative rank from 0 to 1 | `PERCENT_RANK() OVER (ORDER BY salary)` |
| `CUME_DIST()` | Cumulative distribution | `CUME_DIST() OVER (ORDER BY salary)` |
| `SUM() OVER()` | Sum across a window | `SUM(salary) OVER ()` |
| `AVG() OVER()` | Average across a window | `AVG(salary) OVER (PARTITION BY department)` |
| `COUNT() OVER()` | Count rows in a window | `COUNT(*) OVER (PARTITION BY department)` |
| `MIN() OVER()` | Minimum value in a window | `MIN(salary) OVER ()` |
| `MAX() OVER()` | Maximum value in a window | `MAX(salary) OVER ()` |
| `LAG()` | Access a preceding row | `LAG(salary) OVER (ORDER BY emp_id)` |
| `LEAD()` | Access a following row | `LEAD(salary) OVER (ORDER BY emp_id)` |
| `FIRST_VALUE()` | First value in the frame | `FIRST_VALUE(salary) OVER (ORDER BY salary DESC)` |
| `LAST_VALUE()` | Last value in the frame | `LAST_VALUE(salary) OVER (ORDER BY salary)` |
| `NTH_VALUE()` | Value at a specified position | `NTH_VALUE(salary, 2) OVER (ORDER BY salary DESC)` |

## Important Syntax Patterns

### Entire Result Set

```
SUM(salary) OVER ()
```

### Per-Group Calculation

```
SUM(salary) OVER (
    PARTITION BY department
)
```

### Running Total

```
SUM(salary) OVER (
    ORDER BY emp_id
    ROWS BETWEEN UNBOUNDED PRECEDING
             AND CURRENT ROW
)
```

### Top N Per Group

```
WITH ranked AS (
    SELECT
        *,
        ROW_NUMBER() OVER (
            PARTITION BY department
            ORDER BY salary DESC, emp_id
        ) AS rn
    FROM employees
)
SELECT *
FROM ranked
WHERE rn <= 3;
```

### Previous Row

```
LAG(salary) OVER (
    ORDER BY emp_id
)
```

### Next Row

```
LEAD(salary) OVER (
    ORDER BY emp_id
)
```

---

# 13\. FINAL LEARNING ROADMAP

Learn window functions in this order:

1. **Foundation:** `OVER()`, `PARTITION BY`, and `ORDER BY`.
2. **Ranking:** `ROW_NUMBER()`, `RANK()`, and `DENSE_RANK()`.
3. **Aggregate windows:** `SUM()`, `AVG()`, `COUNT()`, `MIN()`, and `MAX()`.
4. **Analytical navigation:** `LAG()` and `LEAD()`.
5. **Value functions:** `FIRST_VALUE()`, `LAST_VALUE()`, and `NTH_VALUE()`.
6. **Distribution:** `NTILE()`, `PERCENT_RANK()`, and `CUME_DIST()`.
7. **Frames:** `ROWS`, `RANGE`, running totals, and moving averages.
8. **Advanced queries:** CTEs, subqueries, grouped aggregates, and top N per group.

## Most Important Functions for SQL Interviews

Master these first:

- `ROW_NUMBER()`
- `RANK()`
- `DENSE_RANK()`
- `SUM() OVER()`
- `AVG() OVER()`
- `LAG()`
- `LEAD()`
- `FIRST_VALUE()`
- `LAST_VALUE()`
- `NTILE()`

**Best practice:** Do not memorize window functions in isolation. Practise combining them with `SELECT`, `WHERE` through a subquery or CTE, `GROUP BY`, `ORDER BY`, aggregate functions, and `CASE` expressions.

**End of SQL Window Functions Notes.**
