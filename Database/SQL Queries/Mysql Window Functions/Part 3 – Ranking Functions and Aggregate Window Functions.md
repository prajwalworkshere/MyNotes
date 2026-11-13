# SQL Window Functions – Complete Beginner to Advanced Guide

# Part 3 – Ranking Functions and Aggregate Window Functions

# Chapter 15: Ranking Window Functions

## 15.1 What Are Ranking Functions?

Ranking functions assign a numerical position or rank to rows within a result set or partition.

They are useful when we need to determine:

- Which employee has the highest salary?
- Who is the second-highest-paid employee?
- Which are the top three products in each category?
- What is the position of each student in an examination?
- How should employees be ranked when salaries are equal?
- How can we divide customers into four groups?

Ranking functions generally require ORDER BY inside OVER() because the database needs a defined ordering to assign meaningful positions.

The four major ranking functions are:

1. ROW_NUMBER()
2. RANK()
3. DENSE_RANK()
4. NTILE()

Although they all assign numerical values, their treatment of ties and groups is different.

---

# Chapter 16: ROW_NUMBER()

## 16.1 What Is ROW_NUMBER()?

ROW_NUMBER() assigns a unique sequential integer to every row within a window partition.

The numbering begins at 1 for each partition.

If PARTITION BY is not specified, numbering applies to the entire result window.

### Syntax

```sql
ROW_NUMBER() OVER (
    PARTITION BY column_name
    ORDER BY column_name
)
```

The PARTITION BY clause is optional.

The ORDER BY clause is required in MySQL for ROW_NUMBER().

## 16.2 Example: Number All Employees

```sql
SELECT
    employee_id,
    employee_name,
    salary,

    ROW_NUMBER() OVER (
        ORDER BY salary DESC, employee_id
    ) AS row_num

FROM employees;
```

Output:

| employee_id | employee_name | salary | row_num |
|---:|---|---:|---:|
| 102 | Rahul | 80000 | 1 |
| 108 | Riya | 75000 | 2 |
| 103 | Priya | 70000 | 3 |
| 105 | Karan | 65000 | 4 |
| 101 | Amit | 60000 | 5 |
| 106 | Sneha | 55000 | 6 |
| 104 | Neha | 50000 | 7 |
| 107 | Arjun | 45000 | 8 |

### Explanation

```sql
ROW_NUMBER() OVER (
    ORDER BY salary DESC, employee_id
)
```

1. SQL orders employees by salary in descending order.
2. Employee ID resolves any salary ties.
3. ROW_NUMBER() assigns 1 to the first row.
4. It assigns 2 to the second row.
5. It continues until every row has a unique number.

Even if two employees have the same salary, they receive different row numbers.

## 16.3 ROW_NUMBER() with PARTITION BY

Suppose we want to number employees separately within each department.

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,

    ROW_NUMBER() OVER (
        PARTITION BY department
        ORDER BY salary DESC, employee_id
    ) AS department_row_num

FROM employees;
```

Output:

| employee_name | department | salary | department_row_num |
|---|---|---:|---:|
| Rahul | IT | 80000 | 1 |
| Priya | IT | 70000 | 2 |
| Amit | IT | 60000 | 3 |
| Karan | HR | 65000 | 1 |
| Sneha | HR | 55000 | 2 |
| Neha | HR | 50000 | 3 |
| Riya | Sales | 75000 | 1 |
| Arjun | Sales | 45000 | 2 |

Each department starts numbering from 1.

### Why?

PARTITION BY department divides employees into separate partitions.

ROW_NUMBER() independently numbers the rows within each partition.

## 16.4 Real-World Applications

ROW_NUMBER() is particularly useful for:

- Deduplicating records.
- Selecting the latest record per customer.
- Selecting the first transaction per account.
- Finding the top N records per group.
- Generating sequential positions.
- Selecting one representative record from each group.

### Example: Find the latest transaction of every customer

Suppose:

### transactions

| transaction_id | customer_id | transaction_date | amount |
|---:|---:|---|---:|
| 1 | 101 | 2026-01-01 | 500 |
| 2 | 101 | 2026-02-01 | 700 |
| 3 | 101 | 2026-03-01 | 900 |
| 4 | 102 | 2026-01-10 | 300 |
| 5 | 102 | 2026-02-15 | 600 |

Query:

```sql
WITH ranked_transactions AS (
    SELECT
        transaction_id,
        customer_id,
        transaction_date,
        amount,

        ROW_NUMBER() OVER (
            PARTITION BY customer_id
            ORDER BY transaction_date DESC, transaction_id DESC
        ) AS rn

    FROM transactions
)

SELECT
    transaction_id,
    customer_id,
    transaction_date,
    amount

FROM ranked_transactions
WHERE rn = 1;
```

Output:

| transaction_id | customer_id | transaction_date | amount |
|---:|---:|---|---:|
| 3 | 101 | 2026-03-01 | 900 |
| 5 | 102 | 2026-02-15 | 600 |

### Explanation

For customer 101:

```text
March 01 → rn = 1
February 01 → rn = 2
January 01 → rn = 3
```

For customer 102:

```text
February 15 → rn = 1
January 10 → rn = 2
```

The outer query selects rn = 1.

**Important:** The additional transaction_id ordering makes the choice deterministic when multiple transactions share the same timestamp.

---

# Chapter 17: RANK()

## 17.1 What Is RANK()?

RANK() assigns ranks to rows according to their ordering values.

If two or more rows have equal ordering values, they receive the same rank.

However, RANK() leaves gaps after tied positions.

This is also known as competition ranking.

### Syntax

```sql
RANK() OVER (
    PARTITION BY column_name
    ORDER BY column_name DESC
)
```

## 17.2 Example: Rank Employees by Salary

Use the following dataset:

| employee_id | employee_name | salary |
|---:|---|---:|
| 1 | Amit | 90000 |
| 2 | Rahul | 80000 |
| 3 | Priya | 80000 |
| 4 | Neha | 70000 |
| 5 | Karan | 60000 |

Query:

```sql
SELECT
    employee_name,
    salary,

    RANK() OVER (
        ORDER BY salary DESC
    ) AS salary_rank

FROM employees;
```

Output:

| employee_name | salary | salary_rank |
|---|---:|---:|
| Amit | 90000 | 1 |
| Rahul | 80000 | 2 |
| Priya | 80000 | 2 |
| Neha | 70000 | 4 |
| Karan | 60000 | 5 |

### Why does Neha receive rank 4?

Let's understand the ranking positions.

| Employee | Position |
|---|---:|
| Amit | 1 |
| Rahul | 2 |
| Priya | 3 |
| Neha | 4 |
| Karan | 5 |

However, Rahul and Priya have equal salaries.

They both receive rank 2.

The next rank is calculated based on the number of rows preceding the next distinct ordering group.

Therefore:

```text
Amit   → Rank 1
Rahul  → Rank 2
Priya  → Rank 2
Neha   → Rank 4
Karan  → Rank 5
```

Rank 3 is skipped.

### Important point

RANK() does not mean assigning the next available rank.

It means assigning the position of the row, accounting for ties.

---

# Chapter 18: DENSE_RANK()

## 18.1 What Is DENSE_RANK()?

DENSE_RANK() assigns the same rank to rows with equal ordering values, but unlike RANK(), it does not leave gaps.

The next distinct ordering value receives the next consecutive rank.

### Syntax

```sql
DENSE_RANK() OVER (
    PARTITION BY column_name
    ORDER BY column_name DESC
)
```

## 18.2 Example

Using the same dataset:

| employee_name | salary |
|---|---:|
| Amit | 90000 |
| Rahul | 80000 |
| Priya | 80000 |
| Neha | 70000 |
| Karan | 60000 |

Query:

```sql
SELECT
    employee_name,
    salary,

    DENSE_RANK() OVER (
        ORDER BY salary DESC
    ) AS dense_salary_rank

FROM employees;
```

Output:

| employee_name | salary | dense_salary_rank |
|---|---:|---:|
| Amit | 90000 | 1 |
| Rahul | 80000 | 2 |
| Priya | 80000 | 2 |
| Neha | 70000 | 3 |
| Karan | 60000 | 4 |

### Why?

The distinct salary values are:

```text
90000
80000
70000
60000
```

There are four distinct salary levels.

Therefore:

```text
90000 → Rank 1
80000 → Rank 2
70000 → Rank 3
60000 → Rank 4
```

Both employees earning 80000 receive rank 2.

No rank is skipped.

---

# Chapter 19: ROW_NUMBER vs RANK vs DENSE_RANK

This is one of the most frequently asked SQL interview questions.

Use the same dataset to understand all three.

### Input

| employee_id | employee_name | salary |
|---:|---|---:|
| 1 | Amit | 90000 |
| 2 | Rahul | 80000 |
| 3 | Priya | 80000 |
| 4 | Neha | 70000 |
| 5 | Karan | 60000 |
| 6 | Sneha | 60000 |

Query:

```sql
SELECT
    employee_name,
    salary,

    ROW_NUMBER() OVER (
        ORDER BY salary DESC, employee_id
    ) AS row_num,

    RANK() OVER (
        ORDER BY salary DESC
    ) AS salary_rank,

    DENSE_RANK() OVER (
        ORDER BY salary DESC
    ) AS dense_rank

FROM employees;
```

Output:

| employee_name | salary | ROW_NUMBER | RANK | DENSE_RANK |
|---|---:|---:|---:|---:|
| Amit | 90000 | 1 | 1 | 1 |
| Rahul | 80000 | 2 | 2 | 2 |
| Priya | 80000 | 3 | 2 | 2 |
| Neha | 70000 | 4 | 4 | 3 |
| Karan | 60000 | 5 | 5 | 4 |
| Sneha | 60000 | 6 | 5 | 4 |

### Understand the logic visually

ROW_NUMBER():

```text
1, 2, 3, 4, 5, 6
```

Every row gets a unique number.

RANK():

```text
1, 2, 2, 4, 5, 5
```

Ties receive the same rank, and gaps appear.

DENSE_RANK():

```text
1, 2, 2, 3, 4, 4
```

Ties receive the same rank, but gaps do not appear.

### Comparison table

| Feature | ROW_NUMBER() | RANK() | DENSE_RANK() |
|---|---|---|---|
| Unique number per row | Yes | No | No |
| Equal values receive equal result | No | Yes | Yes |
| Gaps after ties | Not applicable | Yes | No |
| Useful for deduplication | Yes | No | No |
| Useful for top N physical records | Yes | Sometimes, depending on tie requirements | Sometimes |
| Useful for Nth distinct value | No | No | Yes |

## 19.1 When Should You Use Each Function?

| Requirement | Recommended function |
|---|---|
| Assign a unique sequential number | ROW_NUMBER() |
| Select exactly one record from each group | ROW_NUMBER() |
| Rank employees with tied salaries | RANK() |
| Find the second-highest distinct salary | DENSE_RANK() |
| Find the top three distinct salary levels | DENSE_RANK() |
| Include all employees tied within the top three positions | RANK() |
| Find exactly the top three physical records | ROW_NUMBER() |

---

# Chapter 20: NTILE()

## 20.1 What Is NTILE()?

NTILE(n) distributes the rows in an ordered partition into approximately equal-sized groups called buckets.

The number of buckets is specified as the argument.

For example:

```sql
NTILE(4)
```

Divides the rows into four buckets.

These buckets are often used for:

- Quartile analysis.
- Customer segmentation.
- Employee performance grouping.
- Sales performance classification.
- Dividing users into percentile-based groups.

### Syntax

```sql
NTILE(number_of_buckets) OVER (
    PARTITION BY column_name
    ORDER BY column_name
)
```

## 20.2 Example: Divide Employees into Four Quartiles

Dataset:

| employee_id | employee_name | salary |
|---:|---|---:|
| 1 | Amit | 90000 |
| 2 | Rahul | 80000 |
| 3 | Priya | 70000 |
| 4 | Neha | 60000 |
| 5 | Karan | 50000 |
| 6 | Sneha | 40000 |
| 7 | Arjun | 30000 |
| 8 | Riya | 20000 |

Query:

```sql
SELECT
    employee_name,
    salary,

    NTILE(4) OVER (
        ORDER BY salary DESC
    ) AS salary_quartile

FROM employees;
```

Output:

| employee_name | salary | salary_quartile |
|---|---:|---:|
| Amit | 90000 | 1 |
| Rahul | 80000 | 1 |
| Priya | 70000 | 2 |
| Neha | 60000 | 2 |
| Karan | 50000 | 3 |
| Sneha | 40000 | 3 |
| Arjun | 30000 | 4 |
| Riya | 20000 | 4 |

### Explanation

There are 8 employees.

We requested four buckets.

```text
8 / 4 = 2
```

Each bucket receives two employees.

```text
Bucket 1 → Highest salaries
Bucket 2 → Next highest salaries
Bucket 3 → Next highest salaries
Bucket 4 → Lowest salaries
```

## 20.3 What If Rows Cannot Be Divided Equally?

Suppose we have 10 records and request four buckets.

```sql
NTILE(4)
```

The database distributes the records as evenly as possible.

The output bucket sizes are:

| Bucket | Number of rows |
|---|---:|
| 1 | 3 |
| 2 | 3 |
| 3 | 2 |
| 4 | 2 |

The earlier buckets receive the extra rows.

### Important distinction

NTILE(4) does not guarantee that the numerical ranges of the values in each bucket are equal.

It divides ordered rows into approximately equal-sized groups.

If salary values are highly uneven, the salary ranges of the buckets may be very different.

## 20.4 NTILE() with PARTITION BY

```sql
SELECT
    employee_name,
    department,
    salary,

    NTILE(4) OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS department_quartile

FROM employees;
```

Each department is divided independently into four buckets.

If a department has fewer than four employees, some bucket numbers will not appear.

---

# Chapter 21: Finding Highest, Second-Highest and Nth-Highest Salaries

These are classic interview problems.

## 21.1 Find the Highest-Paid Employee

```sql
WITH ranked_employees AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        ROW_NUMBER() OVER (
            ORDER BY salary DESC, employee_id
        ) AS rn

    FROM employees
)

SELECT *
FROM ranked_employees
WHERE rn = 1;
```

This returns exactly one employee.

If multiple employees share the highest salary, the employee ID tie-breaker determines which one is selected.

If the requirement is to return all employees tied for the highest salary, use:

```sql
WITH ranked_employees AS (
    SELECT
        employee_id,
        employee_name,
        salary,

        RANK() OVER (
            ORDER BY salary DESC
        ) AS salary_rank

    FROM employees
)

SELECT *
FROM ranked_employees
WHERE salary_rank = 1;
```

## 21.2 Find the Highest-Paid Employee in Every Department

```sql
WITH ranked_employees AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        RANK() OVER (
            PARTITION BY department
            ORDER BY salary DESC
        ) AS salary_rank

    FROM employees
)

SELECT *
FROM ranked_employees
WHERE salary_rank = 1;
```

This returns all employees tied for the highest salary within each department.

For exactly one employee per department, use ROW_NUMBER() with a deterministic tie-breaker.

## 21.3 Find the Top Three Employees in Every Department

```sql
WITH ranked_employees AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        ROW_NUMBER() OVER (
            PARTITION BY department
            ORDER BY salary DESC, employee_id
        ) AS rn

    FROM employees
)

SELECT *
FROM ranked_employees
WHERE rn <= 3;
```

This returns a maximum of three employees per department.

If the requirement is to include all employees tied within the top three positions, use:

```sql
WITH ranked_employees AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        RANK() OVER (
            PARTITION BY department
            ORDER BY salary DESC
        ) AS salary_rank

    FROM employees
)

SELECT *
FROM ranked_employees
WHERE salary_rank <= 3;
```

If the requirement is the top three distinct salary levels, use DENSE_RANK():

```sql
WITH ranked_employees AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        DENSE_RANK() OVER (
            PARTITION BY department
            ORDER BY salary DESC
        ) AS salary_rank

    FROM employees
)

SELECT *
FROM ranked_employees
WHERE salary_rank <= 3;
```

These three queries can produce different row counts.

## 21.4 Find the Second-Highest Salary

There are two interpretations.

**Interpretation 1: Second-highest distinct salary**

Use DENSE_RANK():

```sql
WITH salary_ranking AS (
    SELECT
        employee_id,
        employee_name,
        salary,

        DENSE_RANK() OVER (
            ORDER BY salary DESC
        ) AS salary_rank

    FROM employees
)

SELECT *
FROM salary_ranking
WHERE salary_rank = 2;
```

**Interpretation 2: Second physical row after sorting**

Use ROW_NUMBER():

```sql
WITH salary_ranking AS (
    SELECT
        employee_id,
        employee_name,
        salary,

        ROW_NUMBER() OVER (
            ORDER BY salary DESC, employee_id
        ) AS rn

    FROM employees
)

SELECT *
FROM salary_ranking
WHERE rn = 2;
```

Always clarify whether the interviewer wants the second record or the second distinct salary.

## 21.5 Find the Nth-Highest Distinct Salary

```sql
WITH salary_ranking AS (
    SELECT
        employee_id,
        employee_name,
        salary,

        DENSE_RANK() OVER (
            ORDER BY salary DESC
        ) AS salary_rank

    FROM employees
)

SELECT *
FROM salary_ranking
WHERE salary_rank = 3;
```

This returns employees earning the third-highest distinct salary.

To change N, change the rank condition.

---

# Chapter 22: Aggregate Window Functions

## 22.1 What Are Aggregate Window Functions?

Aggregate window functions perform aggregate calculations across a window while preserving the individual rows.

The five essential aggregate functions are:

1. SUM()
2. AVG()
3. COUNT()
4. MIN()
5. MAX()

These are ordinary SQL aggregate functions that can also be used with OVER().

### Syntax

```sql
aggregate_function(expression) OVER (
    PARTITION BY column_name
    ORDER BY column_name
    frame_definition
)
```

The result depends on the partition, ordering and frame.

---

# Chapter 23: SUM() as a Window Function

## 23.1 What Is SUM()?

SUM() calculates the total of non-NULL numeric values in the current window frame.

When used as a window function, it returns a sum for every current row.

### Example 1: Department-Wise Total Salary

```sql
SELECT
    employee_name,
    department,
    salary,

    SUM(salary) OVER (
        PARTITION BY department
    ) AS department_total_salary

FROM employees;
```

Output:

| employee_name | department | salary | department_total_salary |
|---|---|---:|---:|
| Amit | IT | 60000 | 210000 |
| Rahul | IT | 80000 | 210000 |
| Priya | IT | 70000 | 210000 |
| Neha | HR | 50000 | 170000 |
| Karan | HR | 65000 | 170000 |
| Sneha | HR | 55000 | 170000 |
| Arjun | Sales | 45000 | 120000 |
| Riya | Sales | 75000 | 120000 |

## 23.2 Running Total Using SUM()

A running total is the cumulative sum of values from the beginning of the ordered partition to the current row.

```sql
SELECT
    sale_id,
    sale_date,
    amount,

    SUM(amount) OVER (
        ORDER BY sale_date, sale_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS running_total

FROM sales;
```

Example:

| sale_id | amount | running_total |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 300 |
| 3 | 300 | 600 |
| 4 | 400 | 1000 |
| 5 | 500 | 1500 |

## 23.3 Running Total Per Customer

```sql
SELECT
    customer_id,
    transaction_date,
    amount,

    SUM(amount) OVER (
        PARTITION BY customer_id
        ORDER BY transaction_date, transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS customer_running_total

FROM transactions;
```

Every customer has an independent running total.

This is useful in banking and account analysis.

---

# Chapter 24: AVG() as a Window Function

## 24.1 What Is AVG()?

AVG() calculates the arithmetic mean of non-NULL values within the current window frame.

Formula:

```text
Average = Sum of non-NULL values / Count of non-NULL values
```

### Example 1: Department-Wise Average

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

Every employee receives their department's average salary.

## 24.2 Cumulative Average

A cumulative average calculates the average from the beginning of the partition up to the current row.

```sql
SELECT
    sale_date,
    amount,

    AVG(amount) OVER (
        ORDER BY sale_date
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS cumulative_average

FROM sales;
```

Output:

| sale_date | amount | cumulative_average |
|---|---:|---:|
| Jan 01 | 100 | 100.00 |
| Jan 02 | 200 | 150.00 |
| Jan 03 | 300 | 200.00 |
| Jan 04 | 400 | 250.00 |
| Jan 05 | 500 | 300.00 |

For Jan 04:

```text
(100 + 200 + 300 + 400) / 4

= 250
```

## 24.3 Moving Average

A moving average calculates an average over a limited frame that moves with the current row.

Example: Three-row moving average.

```sql
SELECT
    sale_date,
    amount,

    AVG(amount) OVER (
        ORDER BY sale_date
        ROWS BETWEEN 2 PRECEDING
        AND CURRENT ROW
    ) AS moving_average

FROM sales;
```

Output:

| sale_date | amount | moving_average |
|---|---:|---:|
| Jan 01 | 100 | 100.00 |
| Jan 02 | 200 | 150.00 |
| Jan 03 | 300 | 200.00 |
| Jan 04 | 400 | 300.00 |
| Jan 05 | 500 | 400.00 |

### Difference between cumulative and moving averages

| Cumulative average | Moving average |
|---|---|
| Starts at the beginning of the partition | Uses a limited moving frame |
| Frame usually grows | Frame usually has a fixed maximum size |
| Useful for long-term trends | Useful for smoothing short-term fluctuations |
| UNBOUNDED PRECEDING | n PRECEDING |

---

# Chapter 25: COUNT() as a Window Function

## 25.1 What Is COUNT()?

COUNT() counts rows or non-NULL expression values in the current frame.

There are two important forms.

```sql
COUNT(*)
```

Counts all rows.

```sql
COUNT(column_name)
```

Counts only non-NULL values.

### Example: Count Employees in Each Department

```sql
SELECT
    employee_name,
    department,

    COUNT(*) OVER (
        PARTITION BY department
    ) AS employee_count

FROM employees;
```

Output:

| employee_name | department | employee_count |
|---|---|---:|
| Amit | IT | 3 |
| Rahul | IT | 3 |
| Priya | IT | 3 |
| Neha | HR | 3 |
| Karan | HR | 3 |
| Sneha | HR | 3 |
| Arjun | Sales | 2 |
| Riya | Sales | 2 |

## 25.2 Running COUNT()

```sql
SELECT
    transaction_id,
    transaction_date,
    amount,

    COUNT(*) OVER (
        ORDER BY transaction_date, transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS transaction_number

FROM transactions;
```

This counts the number of transactions encountered up to the current row.

## 25.3 COUNT(*) vs COUNT(column)

Dataset:

| id | amount |
|---:|---:|
| 1 | 100 |
| 2 | NULL |
| 3 | 200 |
| 4 | NULL |
| 5 | 300 |

Query:

```sql
SELECT
    id,
    amount,

    COUNT(*) OVER () AS total_rows,

    COUNT(amount) OVER () AS non_null_amounts

FROM sales;
```

Output:

| id | amount | total_rows | non_null_amounts |
|---:|---:|---:|---:|
| 1 | 100 | 5 | 3 |
| 2 | NULL | 5 | 3 |
| 3 | 200 | 5 | 3 |
| 4 | NULL | 5 | 3 |
| 5 | 300 | 5 | 3 |

---

# Chapter 26: MIN() and MAX() as Window Functions

## 26.1 MIN()

MIN() returns the smallest non-NULL value in the current frame.

Example:

```sql
SELECT
    employee_name,
    department,
    salary,

    MIN(salary) OVER (
        PARTITION BY department
    ) AS minimum_department_salary

FROM employees;
```

Every employee receives the minimum salary of their department.

## 26.2 MAX()

MAX() returns the largest non-NULL value in the current frame.

```sql
SELECT
    employee_name,
    department,
    salary,

    MAX(salary) OVER (
        PARTITION BY department
    ) AS maximum_department_salary

FROM employees;
```

Every employee receives the maximum salary of their department.

## 26.3 Running Maximum

```sql
SELECT
    sale_date,
    amount,

    MAX(amount) OVER (
        ORDER BY sale_date
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS highest_sale_so_far

FROM sales;
```

Output:

| sale_date | amount | highest_sale_so_far |
|---|---:|---:|
| Jan 01 | 100 | 100 |
| Jan 02 | 200 | 200 |
| Jan 03 | 300 | 300 |
| Jan 04 | 250 | 300 |
| Jan 05 | 500 | 500 |

This is useful for:

- Highest sales achieved so far.
- Peak stock price so far.
- Maximum account balance.
- Highest performance score to date.

## 26.4 Running Minimum

```sql
SELECT
    sale_date,
    amount,

    MIN(amount) OVER (
        ORDER BY sale_date
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS lowest_sale_so_far

FROM sales;
```

This calculates the minimum value encountered so far.

---

# Chapter 27: Percentage Contribution

One of the most useful applications of window aggregates is calculating the contribution of each row to its group or the overall total.

## 27.1 Percentage Contribution to Department Salary

Formula:

```text
Percentage Contribution =
    (Employee Salary / Department Total Salary) * 100
```

SQL:

```sql
SELECT
    employee_name,
    department,
    salary,

    SUM(salary) OVER (
        PARTITION BY department
    ) AS department_total,

    ROUND(
        salary * 100.0 /
        SUM(salary) OVER (
            PARTITION BY department
        ),
        2
    ) AS contribution_percentage

FROM employees;
```

Example for IT:

| employee_name | salary | department_total | contribution_percentage |
|---|---:|---:|---:|
| Amit | 60000 | 210000 | 28.57 |
| Rahul | 80000 | 210000 | 38.10 |
| Priya | 70000 | 210000 | 33.33 |

The percentages add up to approximately 100%.

## 27.2 Percentage Contribution to Overall Sales

```sql
SELECT
    product_id,
    sales_amount,

    ROUND(
        sales_amount * 100.0 /
        SUM(sales_amount) OVER (),
        2
    ) AS overall_contribution

FROM product_sales;
```

Here there is no PARTITION BY.

Therefore, every product is compared against the total sales of the entire result window.

---

# Chapter 28: Comparing Individual Values Against Group Averages

Suppose we want to identify employees who earn more than the average salary of their department.

```sql
WITH salary_analysis AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        AVG(salary) OVER (
            PARTITION BY department
        ) AS department_average

    FROM employees
)

SELECT
    employee_id,
    employee_name,
    department,
    salary,
    department_average

FROM salary_analysis
WHERE salary > department_average;
```

### Explanation

First, the CTE calculates the average salary for each department.

Then, the outer query compares each employee's salary against the calculated average.

This pattern is extremely useful because window functions cannot generally be filtered in the WHERE clause of the same SELECT query where they are calculated.

We will study the logical processing order in detail in Part 5.

---

# Chapter 29: Cumulative Percentage

Cumulative percentage measures how much of the total has been accumulated up to the current row.

### Formula

```text
Cumulative Percentage =
    (Cumulative Total / Overall Total) * 100
```

### Example

Dataset:

| product | sales |
|---|---:|
| A | 400 |
| B | 300 |
| C | 200 |
| D | 100 |

Query:

```sql
SELECT
    product,
    sales,

    SUM(sales) OVER (
        ORDER BY sales DESC, product
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS cumulative_sales,

    ROUND(
        100.0 *
        SUM(sales) OVER (
            ORDER BY sales DESC, product
            ROWS BETWEEN UNBOUNDED PRECEDING
            AND CURRENT ROW
        ) /
        SUM(sales) OVER (),
        2
    ) AS cumulative_percentage

FROM product_sales;
```

Output:

| product | sales | cumulative_sales | cumulative_percentage |
|---|---:|---:|---:|
| A | 400 | 400 | 40.00 |
| B | 300 | 700 | 70.00 |
| C | 200 | 900 | 90.00 |
| D | 100 | 1000 | 100.00 |

### Applications

- Pareto analysis.
- Revenue contribution.
- Product prioritization.
- Customer value analysis.
- Cumulative performance reporting.

---

# Chapter 30: Conditional Aggregation Using Window Functions

Conditional aggregation means calculating aggregate values only for rows satisfying a particular condition.

We can combine CASE WHEN with aggregate window functions.

### Example: Total IT Salary

```sql
SELECT
    employee_name,
    department,
    salary,

    SUM(
        CASE
            WHEN department = 'IT'
            THEN salary
            ELSE 0
        END
    ) OVER () AS total_it_salary

FROM employees;
```

The total IT salary is calculated across the entire window and repeated for every row.

### Example: Department-wise high-salary employee count

Suppose we define a high-salary employee as someone earning at least 60000.

```sql
SELECT
    employee_name,
    department,
    salary,

    SUM(
        CASE
            WHEN salary >= 60000 THEN 1
            ELSE 0
        END
    ) OVER (
        PARTITION BY department
    ) AS high_salary_employee_count

FROM employees;
```

This calculates the number of high-salary employees within each department.

### Why use SUM(CASE WHEN...)?

Because the CASE expression converts qualifying rows into 1 and non-qualifying rows into 0.

SUM() then adds the indicators.

An alternative in MySQL is:

```sql
COUNT(
    CASE
        WHEN salary >= 60000 THEN 1
    END
) OVER (
    PARTITION BY department
)
```

Both approaches are useful.

---

# Chapter 31: Combining Multiple Aggregate Window Functions

We can calculate several analytical metrics in the same SELECT statement.

```sql
SELECT
    employee_name,
    department,
    salary,

    SUM(salary) OVER (
        PARTITION BY department
    ) AS department_total,

    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_average,

    MIN(salary) OVER (
        PARTITION BY department
    ) AS minimum_salary,

    MAX(salary) OVER (
        PARTITION BY department
    ) AS maximum_salary,

    COUNT(*) OVER (
        PARTITION BY department
    ) AS employee_count

FROM employees;
```

This is particularly useful for analytical reporting.

It calculates five metrics without requiring five separate GROUP BY queries and joins.

---

# Chapter 32: Aggregate Window Function Quick Reference

| Function | Purpose | Example |
|---|---|---|
| SUM() | Total | SUM(salary) OVER (...) |
| AVG() | Average | AVG(salary) OVER (...) |
| COUNT(*) | Count all rows | COUNT(*) OVER (...) |
| COUNT(column) | Count non-NULL values | COUNT(salary) OVER (...) |
| MIN() | Minimum value | MIN(salary) OVER (...) |
| MAX() | Maximum value | MAX(salary) OVER (...) |

## Common patterns

| Requirement | Pattern |
|---|---|
| Overall total | SUM(value) OVER () |
| Department total | SUM(value) OVER (PARTITION BY department) |
| Running total | SUM(value) OVER (ORDER BY date ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) |
| Cumulative average | AVG(value) OVER (ORDER BY date ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) |
| Moving average | AVG(value) OVER (ORDER BY date ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) |
| Group count | COUNT(*) OVER (PARTITION BY group_column) |
| Running count | COUNT(*) OVER (ORDER BY date ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) |
| Group maximum | MAX(value) OVER (PARTITION BY group_column) |
| Group minimum | MIN(value) OVER (PARTITION BY group_column) |
| Percentage contribution | value * 100.0 / SUM(value) OVER (...) |
| Cumulative percentage | SUM(value) OVER (ORDER BY ...) * 100.0 / SUM(value) OVER () |

## Part 3 – Interview Checkpoint

You should now be able to explain:

1. Why ROW_NUMBER() always assigns unique row numbers.
2. Why RANK() skips ranks.
3. Why DENSE_RANK() does not skip ranks.
4. Why NTILE() does not necessarily create equal value ranges.
5. How to select the highest-paid employee per department.
6. How to find the second-highest distinct salary.
7. How to calculate a running total.
8. How to calculate a moving average.
9. How COUNT(*) differs from COUNT(column).
10. How to calculate percentage contribution.
11. How to calculate cumulative percentages.
12. How CASE WHEN can be combined with window aggregates.

