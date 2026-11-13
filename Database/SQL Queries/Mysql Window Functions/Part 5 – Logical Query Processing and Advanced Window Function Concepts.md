# SQL Window Functions – Complete Beginner to Advanced Guide

# Part 5 – Logical Query Processing and Advanced Window Function Concepts

# Chapter 47: SQL Logical Query Processing Order

## 47.1 Why Is Query Processing Order Important?

To use window functions correctly, you must understand the order in which SQL logically processes a query.

Many SQL errors happen because developers assume SQL executes clauses in the same order in which they are written.

For example, consider:

```sql
SELECT
    employee_name,
    salary,
    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS rn
FROM employees
WHERE rn <= 3;
```

This query is invalid in MySQL.

Why?

Because WHERE is logically evaluated before the SELECT list calculates the window function.

At the point WHERE is processed, the alias `rn` does not yet exist.

To understand this, let's study the logical execution order.

## 47.2 Logical Processing Order

A simplified logical processing sequence is:

| Order | SQL clause | Purpose |
|---:|---|---|
| 1 | FROM | Identify source tables |
| 2 | JOIN | Combine related tables |
| 3 | ON | Apply join conditions |
| 4 | WHERE | Filter individual input rows |
| 5 | GROUP BY | Form groups |
| 6 | Aggregate calculations | Calculate group aggregates |
| 7 | HAVING | Filter groups |
| 8 | Window functions | Perform window calculations |
| 9 | SELECT | Produce selected expressions |
| 10 | DISTINCT | Remove duplicate result rows |
| 11 | ORDER BY | Sort final results |
| 12 | LIMIT / OFFSET | Restrict returned rows |

This is a conceptual logical order, not a promise about the database's physical execution plan. The optimizer can rearrange physical operations while preserving query semantics.

## 47.3 Why Can't Window Functions Be Used Directly in WHERE?

Consider:

```sql
SELECT
    employee_name,
    salary,
    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS rn
FROM employees
WHERE rn <= 3;
```

The problem is:

1. FROM obtains employee records.
2. WHERE filters the input rows.
3. The window function is evaluated later.
4. Therefore, rn is unavailable when WHERE executes.

The same general restriction applies to using a window function directly in:

- WHERE
- GROUP BY
- HAVING

at the same query level.

**Important:** A window function can operate on the result of grouping at the same query level. For example, a window function can rank grouped department totals. The restriction is not that GROUP BY and window functions can never be combined; it is that the window function cannot be used as a same-level grouping expression.

---

# Chapter 48: Filtering Window Function Results Using CTEs

## 48.1 What Is a CTE?

A Common Table Expression (CTE) is a named temporary result set defined using the WITH clause.

It exists for the duration of the statement.

CTEs help break complicated queries into understandable steps.

They are particularly useful for filtering window function results.

### Syntax

```sql
WITH cte_name AS (
    SELECT
        column1,
        window_function() OVER (
            ORDER BY column2
        ) AS calculated_value
    FROM table_name
)

SELECT *
FROM cte_name
WHERE calculated_value = 1;
```

## 48.2 Example: Find the Top Three Employees

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

SELECT
    employee_id,
    employee_name,
    department,
    salary,
    rn

FROM ranked_employees
WHERE rn <= 3;
```

### Execution walkthrough

**Step 1:** Execute the CTE query.

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,
    ROW_NUMBER() OVER (
        PARTITION BY department
        ORDER BY salary DESC, employee_id
    ) AS rn
FROM employees;
```

This produces a result containing every employee and their department-wise row number.

**Step 2:** The CTE exposes that result as `ranked_employees`.

**Step 3:** The outer query executes:

```sql
WHERE rn <= 3
```

At this level, rn is already a column of the CTE result.

Therefore, filtering is valid.

### Important distinction

The CTE does not necessarily mean that MySQL physically creates a temporary table.

The optimizer may merge or materialize the CTE depending on the query and engine rules.

A CTE is primarily a logical query-structuring mechanism.

---

# Chapter 49: Filtering Using Derived Tables

A derived table is a subquery placed in the FROM clause.

It can also be used to filter window function results.

### Example

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary

FROM (
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
) AS ranked_employees

WHERE rn <= 3;
```

### CTE vs Derived Table

| CTE | Derived table |
|---|---|
| Defined using WITH | Defined inside FROM |
| Can have a descriptive name | Uses a table alias |
| Often improves readability | Useful for compact queries |
| Can be referenced multiple times in supported contexts | Usually referenced at its query level |
| May be merged or materialized depending on optimizer | May also be merged or materialized |

Both techniques are valid in MySQL 8.0.

---

# Chapter 50: QUALIFY Clause

## 50.1 What Is QUALIFY?

QUALIFY is a clause designed to filter rows based on window function results.

It makes analytical queries shorter because we do not need an additional CTE or derived table merely to filter a window result.

### Example syntax

```sql
SELECT
    employee_name,
    department,
    salary,

    ROW_NUMBER() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS rn

FROM employees

QUALIFY rn <= 3;
```

This is supported by Snowflake and BigQuery, among other analytical SQL systems.

### Important MySQL restriction

MySQL 8.0 does not support QUALIFY.

Therefore, use a CTE or derived table in MySQL.

### Comparison

| Database | QUALIFY |
|---|---|
| MySQL 8.0 | No |
| PostgreSQL | No native QUALIFY |
| SQL Server | No native QUALIFY |
| Oracle | No native QUALIFY |
| Snowflake | Yes |
| BigQuery | Yes |

**Interview point:** If an interviewer asks you to filter a ROW_NUMBER() result in MySQL, use a CTE or derived table, not QUALIFY.

---

# Chapter 51: Multiple Window Functions in a Single SELECT

A single SELECT statement can contain multiple window functions.

They can use:

- The same window definition.
- Different partition definitions.
- Different ordering definitions.
- Different frames.

### Example

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,

    ROW_NUMBER() OVER (
        PARTITION BY department
        ORDER BY salary DESC, employee_id
    ) AS row_num,

    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_rank,

    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_average,

    SUM(salary) OVER (
        PARTITION BY department
    ) AS department_total

FROM employees;
```

### Why is this useful?

We can obtain:

1. Physical row position.
2. Salary ranking.
3. Department average.
4. Department total.

All in a single analytical query.

The database may be able to reuse compatible sorting or partitioning work, although that depends on its optimizer and execution plan.

---

# Chapter 52: Multiple PARTITION BY Columns

Partitioning can be based on multiple attributes.

### Example: Country and department

```sql
SELECT
    employee_name,
    country,
    department,
    salary,

    RANK() OVER (
        PARTITION BY country, department
        ORDER BY salary DESC
    ) AS salary_rank

FROM employees;
```

Suppose the records are:

| employee_name | country | department | salary |
|---|---|---|---:|
| Amit | India | IT | 80000 |
| Rahul | India | IT | 70000 |
| Priya | India | HR | 60000 |
| Neha | USA | IT | 90000 |
| Karan | USA | IT | 85000 |

The partitions become:

```text
India + IT
India + HR
USA + IT
```

Each combination has its own ranking.

---

# Chapter 53: Window Functions with GROUP BY

Window functions can be applied after GROUP BY has produced grouped rows.

This is a very important advanced technique.

### Example: Rank Departments by Total Salary

Suppose we want to calculate the total salary of each department and then rank the departments.

```sql
SELECT
    department,
    SUM(salary) AS department_total,

    RANK() OVER (
        ORDER BY SUM(salary) DESC
    ) AS department_rank

FROM employees

GROUP BY department;
```

Expected output:

| department | department_total | department_rank |
|---|---:|---:|
| IT | 210000 | 1 |
| HR | 170000 | 2 |
| Sales | 120000 | 3 |

### Explanation

Step 1: GROUP BY groups employees by department.

Step 2: SUM(salary) calculates the total salary for each department.

Step 3: RANK() ranks the grouped department rows.

The window function operates on the grouped result, not on the original individual employee rows.

### Another example: Rank monthly revenue

```sql
SELECT
    YEAR(order_date) AS sales_year,
    MONTH(order_date) AS sales_month,
    SUM(order_amount) AS monthly_revenue,

    DENSE_RANK() OVER (
        PARTITION BY YEAR(order_date)
        ORDER BY SUM(order_amount) DESC
    ) AS monthly_rank

FROM orders

GROUP BY
    YEAR(order_date),
    MONTH(order_date);
```

This ranks months by revenue within each year.

---

# Chapter 54: Window Functions with JOINs

Window functions can be used with joined tables.

The important point is that window functions operate on the rows produced by the FROM and JOIN operations after the applicable filtering and grouping steps.

### Example: Department Names with Employee Ranking

Suppose we have:

**employees**

| employee_id | employee_name | department_id | salary |
|---:|---|---:|---:|
| 1 | Amit | 10 | 80000 |
| 2 | Rahul | 10 | 70000 |
| 3 | Priya | 20 | 60000 |
| 4 | Neha | 20 | 50000 |

**departments**

| department_id | department_name |
|---:|---|
| 10 | IT |
| 20 | HR |

Query:

```sql
SELECT
    e.employee_id,
    e.employee_name,
    d.department_name,
    e.salary,

    RANK() OVER (
        PARTITION BY e.department_id
        ORDER BY e.salary DESC
    ) AS salary_rank

FROM employees e

JOIN departments d
    ON e.department_id = d.department_id;
```

Output:

| employee_name | department_name | salary | salary_rank |
|---|---|---:|---:|
| Amit | IT | 80000 | 1 |
| Rahul | IT | 70000 | 2 |
| Priya | HR | 60000 | 1 |
| Neha | HR | 50000 | 2 |

### Important JOIN trap

If a JOIN produces duplicate rows, the window function sees those duplicate rows.

For example, joining employees to a one-to-many table of employee skills can multiply employee records.

Ranking after that JOIN may produce unexpected rankings.

Always understand the cardinality of your joins before applying window functions.

---

# Chapter 55: Window Functions with CASE WHEN

CASE WHEN is used to introduce conditional logic into SQL expressions.

It can be combined with ranking, aggregation and navigation functions.

### Example: Classify Employee Salary

```sql
SELECT
    employee_name,
    salary,

    CASE
        WHEN salary >= 70000 THEN 'High'
        WHEN salary >= 50000 THEN 'Medium'
        ELSE 'Low'
    END AS salary_category

FROM employees;
```

### Example: Calculate Department-Wise High-Salary Count

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
    ) AS high_salary_count

FROM employees;
```

This calculates the number of employees earning at least 60000 in each department.

### Example: Compare Current and Previous Salary

```sql
WITH salary_history AS (
    SELECT
        employee_id,
        employee_name,
        effective_date,
        salary,

        LAG(salary) OVER (
            PARTITION BY employee_id
            ORDER BY effective_date
        ) AS previous_salary

    FROM employee_salary_history
)

SELECT
    employee_id,
    employee_name,
    effective_date,
    salary,
    previous_salary,

    CASE
        WHEN previous_salary IS NULL THEN 'First Record'
        WHEN salary > previous_salary THEN 'Increased'
        WHEN salary < previous_salary THEN 'Decreased'
        ELSE 'Unchanged'
    END AS salary_status

FROM salary_history;
```

This is a common business reporting pattern.

Note that if a previous salary can legitimately be NULL, the `previous_salary IS NULL` test cannot distinguish that from the absence of a preceding row. In that case, use an additional ROW_NUMBER() or record-existence indicator.

---

# Chapter 56: Handling Duplicate Records with ROW_NUMBER()

One of the most important real-world uses of ROW_NUMBER() is deduplication.

## 56.1 What Is Deduplication?

Deduplication means identifying and handling records that represent the same logical entity or event.

For example, an imported customer dataset might contain:

| customer_id | email | customer_name | created_at |
|---:|---|---|---|
| 1 | amit@example.com | Amit | Jan 01 |
| 2 | amit@example.com | Amit | Jan 05 |
| 3 | priya@example.com | Priya | Jan 02 |
| 4 | rahul@example.com | Rahul | Jan 03 |
| 5 | rahul@example.com | Rahul | Jan 06 |

Suppose we want to retain only the latest record for each email.

### Query

```sql
WITH ranked_customers AS (
    SELECT
        customer_id,
        email,
        customer_name,
        created_at,

        ROW_NUMBER() OVER (
            PARTITION BY email
            ORDER BY created_at DESC, customer_id DESC
        ) AS rn

    FROM customers
)

SELECT
    customer_id,
    email,
    customer_name,
    created_at

FROM ranked_customers
WHERE rn = 1;
```

Output:

| customer_id | email | customer_name | created_at |
|---:|---|---|---|
| 2 | amit@example.com | Amit | Jan 05 |
| 3 | priya@example.com | Priya | Jan 02 |
| 5 | rahul@example.com | Rahul | Jan 06 |

### Explanation

PARTITION BY email groups duplicate email addresses.

ORDER BY created_at DESC puts the latest record first.

ROW_NUMBER() assigns 1 to the latest record.

The outer query retains only rn = 1.

### Important warning

Before deleting duplicates, verify:

- Which columns define a duplicate?
- Which record should be retained?
- What happens when timestamps are equal?
- Are foreign keys referencing duplicate rows?
- Are there NULL values in the identifying columns?

A query that is correct for duplicate email addresses may be incorrect for duplicate customer names.

The definition of a duplicate must come from the business requirement.

---

# Chapter 57: Handling NULL Values in Window Functions

NULL values can affect window calculations in several different ways.

We need to distinguish three situations:

1. NULL in the source data.
2. NULL generated because a preceding or following row does not exist.
3. NULL generated because the requested frame does not contain the required positional row.

### Example 1: NULL in LAG()

```sql
SELECT
    id,
    amount,

    LAG(amount, 1, 0) OVER (
        ORDER BY id
    ) AS previous_amount

FROM sales;
```

If the previous row exists but its amount is NULL, LAG() returns NULL, not zero.

The default argument only handles the absence of the requested row.

### Example 2: Replace NULL values

```sql
SELECT
    id,
    amount,

    COALESCE(
        LAG(amount) OVER (
            ORDER BY id
        ),
        0
    ) AS previous_amount

FROM sales;
```

COALESCE() returns the first non-NULL expression.

### Example 3: NULL-safe comparisons in MySQL

```sql
SELECT
    employee_id,
    old_salary,
    new_salary,

    CASE
        WHEN old_salary <=> new_salary THEN 'Same'
        ELSE 'Different'
    END AS comparison_result

FROM salary_changes;
```

The MySQL `<=>` operator treats two NULL values as equal.

For cross-database SQL, use an explicit NULL-safe comparison instead.

---

# Chapter 58: Window Functions Without PARTITION BY

When PARTITION BY is omitted, the whole input result belongs to one window partition.

### Example: Global Ranking

```sql
SELECT
    employee_name,
    salary,

    RANK() OVER (
        ORDER BY salary DESC
    ) AS global_rank

FROM employees;
```

All employees are ranked together.

### Example: Overall Average

```sql
SELECT
    employee_name,
    salary,

    AVG(salary) OVER () AS overall_average

FROM employees;
```

Every employee receives the overall average.

### Example: Percentage Contribution

```sql
SELECT
    product_id,
    revenue,

    ROUND(
        revenue * 100.0 / SUM(revenue) OVER (),
        2
    ) AS revenue_percentage

FROM product_sales;
```

Every product is compared against total revenue across the entire result window.

---

# Chapter 59: Window Functions Without ORDER BY

Not all window functions need ORDER BY.

For example:

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

This is valid because the requirement is to calculate a department-level total.

No row sequence is needed.

Similarly:

```sql
AVG(salary) OVER ()
```

Calculates the overall average.

However, ranking and navigation functions require an ordering to establish meaningful row positions.

**Interview rule:** Use ORDER BY whenever the calculation depends on sequence, position or cumulative progression.

---

# Chapter 60: Combining Ranking and Aggregate Window Functions

We can combine ranking and aggregate calculations to create more informative analytical results.

### Example: Rank employees and calculate their department salary contribution

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,

    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_rank,

    SUM(salary) OVER (
        PARTITION BY department
    ) AS department_total,

    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_average,

    ROUND(
        salary * 100.0 /
        SUM(salary) OVER (
            PARTITION BY department
        ),
        2
    ) AS salary_contribution

FROM employees;
```

This produces a useful employee analytics report.

---

# Chapter 61: Important Advanced Query Design Patterns

## 61.1 Calculate First and Last Transaction Per Customer

```sql
SELECT
    customer_id,
    transaction_date,
    amount,

    FIRST_VALUE(transaction_date) OVER (
        PARTITION BY customer_id
        ORDER BY transaction_date, transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS first_transaction_date,

    LAST_VALUE(transaction_date) OVER (
        PARTITION BY customer_id
        ORDER BY transaction_date, transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS last_transaction_date

FROM transactions;
```

## 61.2 Compare Each Transaction with the Previous Transaction

```sql
SELECT
    customer_id,
    transaction_date,
    amount,

    LAG(amount) OVER (
        PARTITION BY customer_id
        ORDER BY transaction_date, transaction_id
    ) AS previous_amount,

    amount - LAG(amount) OVER (
        PARTITION BY customer_id
        ORDER BY transaction_date, transaction_id
    ) AS amount_difference

FROM transactions;
```

## 61.3 Calculate Running Account Balance

Assume deposits are positive and withdrawals are negative.

```sql
SELECT
    account_id,
    transaction_date,
    transaction_id,
    amount,

    SUM(amount) OVER (
        PARTITION BY account_id
        ORDER BY transaction_date, transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS running_balance

FROM account_transactions;
```

If the account has an opening balance stored separately, add it to the cumulative transaction total or include it as an opening-balance row in the ordered transaction stream.

## 61.4 Find the Latest Status of Every Order

```sql
WITH ranked_status AS (
    SELECT
        order_id,
        status,
        status_date,

        ROW_NUMBER() OVER (
            PARTITION BY order_id
            ORDER BY status_date DESC, status_id DESC
        ) AS rn

    FROM order_status_history
)

SELECT
    order_id,
    status,
    status_date

FROM ranked_status
WHERE rn = 1;
```

## 61.5 Identify Consecutive Records

Suppose we want to identify users whose login dates are consecutive.

```sql
WITH login_analysis AS (
    SELECT
        user_id,
        login_date,

        LAG(login_date) OVER (
            PARTITION BY user_id
            ORDER BY login_date
        ) AS previous_login

    FROM user_logins
)

SELECT
    user_id,
    login_date,
    previous_login

FROM login_analysis
WHERE DATEDIFF(login_date, previous_login) = 1;
```

This identifies login records occurring exactly one calendar day after the previous login.

This simple query assumes that duplicate login dates have already been removed or intentionally retained. If multiple logins per user per day exist, deduplicate to one row per user per date before applying the comparison.

---

# Part 5 – Quick Revision

| Concept | Important rule |
|---|---|
| WHERE | Evaluated before window functions |
| HAVING | Filters grouped rows before window calculations |
| CTE | Allows filtering calculated window values |
| Derived table | Another way to filter window results |
| QUALIFY | Filters window results directly where supported |
| Multiple window functions | Allowed in one SELECT |
| Multiple partitions | Supported |
| GROUP BY + window functions | Window functions can operate on grouped results |
| JOIN + window functions | Window calculations see joined rows |
| ROW_NUMBER() | Useful for deduplication |
| LAG() | Useful for historical comparison |
| LEAD() | Useful for subsequent-row analysis |
| CASE WHEN | Enables conditional window aggregation |

## Part 5 – Interview Checkpoint

Make sure you can explain:

1. Why WHERE cannot directly reference a same-level window function.
2. How to filter ROW_NUMBER() using a CTE.
3. How a derived table solves the same problem.
4. What QUALIFY does.
5. Why MySQL does not support QUALIFY.
6. How GROUP BY and window functions work together.
7. How JOIN cardinality affects window calculations.
8. How to deduplicate records.
9. How to identify the latest record per customer.
10. How to combine LAG() and CASE WHEN.
11. How NULL values affect navigation functions.
12. How to calculate global and partition-level metrics.

