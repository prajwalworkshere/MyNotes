# SQL Window Functions – Complete Beginner to Advanced Guide

# Part 9 – SQL Dialect Differences

# Chapter 83: Window Functions Across Different Databases

## 83.1 Why Do SQL Dialect Differences Matter?

SQL has standards, but individual database systems implement features differently.

Differences can include:

- Supported window functions.
- Window frame syntax.
- Default frame behavior.
- NULL handling.
- QUALIFY support.
- Named windows.
- DISTINCT aggregate support.
- Navigation-function options.
- Ordering syntax.
- Restrictions on window expressions.

A SQL developer should understand both standard concepts and database-specific syntax.

## 83.2 Database Comparison Overview

| Feature | MySQL 8.0 | PostgreSQL | SQL Server | Oracle | Snowflake | BigQuery |
|---|---|---|---|---|---|---|
| ROW_NUMBER | Yes | Yes | Yes | Yes | Yes | Yes |
| RANK | Yes | Yes | Yes | Yes | Yes | Yes |
| DENSE_RANK | Yes | Yes | Yes | Yes | Yes | Yes |
| NTILE | Yes | Yes | Yes | Yes | Yes | Yes |
| LAG / LEAD | Yes | Yes | Yes | Yes | Yes | Yes |
| FIRST_VALUE | Yes | Yes | Yes | Yes | Yes | Yes |
| LAST_VALUE | Yes | Yes | Yes | Yes | Yes | Yes |
| NTH_VALUE | Yes | Yes | Yes | Yes | Yes | Yes |
| PERCENT_RANK | Yes | Yes | Yes | Yes | Yes | Yes |
| CUME_DIST | Yes | Yes | Yes | Yes | Yes | Yes |
| ROWS frames | Yes | Yes | Yes | Yes | Yes | Yes |
| RANGE frames | Yes | Yes | Yes | Yes | Yes | Yes |
| GROUPS frames | No | Yes | No | Version-dependent support | Check supported syntax | No |
| Named WINDOW clause | Yes | Yes | No standard WINDOW clause | Yes | Yes | Yes |
| QUALIFY | No | No | No | No native QUALIFY | Yes | Yes |
| IGNORE NULLS options | No general support | Version/function dependent | Function/version dependent | Supported for applicable functions | Supported for applicable functions | Function dependent |

This table provides a broad compatibility overview. Always verify the exact syntax against the database version being used.

---

# Chapter 84: MySQL 8.0 Window Functions

## 84.1 Overview

MySQL introduced window-function support in MySQL 8.0.

Your learning environment uses MySQL 8.0.42, so the examples in this guide primarily use MySQL syntax.

### Important features

- ROW_NUMBER()
- RANK()
- DENSE_RANK()
- NTILE()
- LAG()
- LEAD()
- FIRST_VALUE()
- LAST_VALUE()
- NTH_VALUE()
- PERCENT_RANK()
- CUME_DIST()
- Aggregate window functions.
- Named windows.
- ROWS frames.
- RANGE frames.
- CTE-based filtering.

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
    ) AS rn

FROM employees;
```

### Important MySQL limitations

1. No QUALIFY clause.
2. GROUPS frames are not supported.
3. DISTINCT is not supported in window-function aggregate calls such as COUNT(DISTINCT ...) OVER().
4. IGNORE NULLS is not generally available for navigation/value functions.
5. Some window-function expressions cannot be nested directly.
6. Window functions cannot be used in WHERE at the same query level.
7. Named-window inheritance has specific restrictions.

### MySQL filtering pattern

```sql
WITH ranked_data AS (
    SELECT
        employee_id,
        salary,

        ROW_NUMBER() OVER (
            ORDER BY salary DESC
        ) AS rn

    FROM employees
)

SELECT *
FROM ranked_data
WHERE rn <= 3;
```

---

# Chapter 85: PostgreSQL Window Functions

## 85.1 Overview

PostgreSQL provides extensive window-function functionality.

It supports:

- Ranking functions.
- Aggregate window functions.
- Navigation functions.
- Value functions.
- Named windows.
- ROWS, RANGE and GROUPS frames.
- Advanced frame specifications.
- FILTER for applicable aggregate expressions.

### Example

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,

    DENSE_RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_rank

FROM employees;
```

## 85.2 GROUPS Frame Support

PostgreSQL supports GROUPS frames.

Example:

```sql
SELECT
    employee_id,
    salary,

    SUM(salary) OVER (
        ORDER BY salary
        GROUPS BETWEEN 1 PRECEDING
        AND CURRENT ROW
    ) AS group_total

FROM employees;
```

This includes the current peer group and the immediately preceding peer group.

## 85.3 FILTER with Aggregate Window Functions

PostgreSQL supports FILTER for applicable aggregate functions.

```sql
SELECT
    employee_id,
    department,
    salary,

    COUNT(*) FILTER (
        WHERE salary >= 60000
    ) OVER (
        PARTITION BY department
    ) AS high_salary_count

FROM employees;
```

This counts qualifying employees within each department.

### MySQL alternative

MySQL can express the same requirement with CASE:

```sql
SELECT
    employee_id,
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

### Important PostgreSQL features

PostgreSQL also supports advanced frame options such as EXCLUDE for applicable frame types and supports GROUPS frames.

It does not provide a native QUALIFY clause.

Use a CTE or derived table to filter window results.

---

# Chapter 86: SQL Server Window Functions

## 86.1 Overview

Microsoft SQL Server supports most commonly used ranking, aggregate, navigation and value window functions.

It supports:

- ROW_NUMBER()
- RANK()
- DENSE_RANK()
- NTILE()
- LAG()
- LEAD()
- FIRST_VALUE()
- LAST_VALUE()
- Aggregate window functions.
- ROWS frames.
- RANGE frames.

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
    ) AS rn

FROM employees;
```

## 86.2 SQL Server Frame Support

SQL Server supports ROWS and RANGE, but its RANGE support is more restricted than PostgreSQL's.

In particular, do not assume that SQL Server supports arbitrary numeric or date-based RANGE offsets in the same way as databases implementing richer offset RANGE syntax.

It does not support GROUPS frames.

## 86.3 Filtering Window Results

SQL Server does not have a native QUALIFY clause.

Use a CTE:

```sql
WITH ranked_employees AS (
    SELECT
        employee_id,
        employee_name,
        salary,

        ROW_NUMBER() OVER (
            ORDER BY salary DESC
        ) AS rn

    FROM employees
)

SELECT *
FROM ranked_employees
WHERE rn <= 3;
```

## 86.4 Important SQL Server Differences

- Uses `TOP` for certain row-limiting queries.
- Uses `OFFSET ... FETCH` for paging.
- Supports IGNORE NULLS for certain functions in newer SQL Server versions, including applicable LAG/LEAD and FIRST_VALUE/LAST_VALUE functionality.
- Does not provide a standard WINDOW clause for reusable named windows.
- Uses different date and data-type functions from MySQL.

---

# Chapter 87: Oracle Window Functions

## 87.1 Overview

Oracle has a long history of analytical SQL support.

Oracle frequently refers to these features as analytic functions.

The following are supported:

- ROW_NUMBER()
- RANK()
- DENSE_RANK()
- NTILE()
- LAG()
- LEAD()
- FIRST_VALUE()
- LAST_VALUE()
- NTH_VALUE()
- PERCENT_RANK()
- CUME_DIST()

Oracle also provides additional analytical functionality beyond the functions covered in this guide.

### Example

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,

    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_rank

FROM employees;
```

## 87.2 NULL Handling

Oracle supports NULL-handling options for applicable navigation and value functions.

For example:

```sql
LAST_VALUE(salary IGNORE NULLS) OVER (
    PARTITION BY department
    ORDER BY employee_id
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND CURRENT ROW
)
```

This requests the last non-NULL salary within the frame.

Exact function and version support should be checked.

## 87.3 QUALIFY

Traditional Oracle SQL uses nested queries or CTEs for filtering window results.

Oracle Database 23ai introduced QUALIFY support.

For environments without QUALIFY, use:

```sql
WITH ranked_employees AS (
    SELECT
        employee_id,
        employee_name,
        salary,

        ROW_NUMBER() OVER (
            ORDER BY salary DESC
        ) AS rn

    FROM employees
)

SELECT *
FROM ranked_employees
WHERE rn <= 3;
```

---

# Chapter 88: Snowflake Window Functions

## 88.1 Overview

Snowflake provides extensive analytical SQL functionality.

It supports the major window functions and includes QUALIFY.

### Example using QUALIFY

```sql
SELECT
    employee_id,
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

The query returns the top three employee records in each department.

## 88.2 Why Is QUALIFY Useful?

Without QUALIFY, we would need a CTE or derived table.

With QUALIFY, the filtering condition is written directly after the window calculation.

This improves readability for many analytical queries.

## 88.3 Snowflake Considerations

- Supports named windows.
- Supports ROWS and RANGE.
- Supports advanced analytical expressions.
- Provides NULL-handling options for applicable navigation and value functions.
- Offers additional analytical functions and SQL extensions.
- Supports QUALIFY.

---

# Chapter 89: BigQuery Window Functions

## 89.1 Overview

Google BigQuery is a cloud data warehouse with extensive analytical SQL capabilities.

It supports:

- Ranking functions.
- Aggregate window functions.
- Navigation functions.
- Value functions.
- Distribution functions.
- Named windows.
- ROWS and RANGE frames.
- QUALIFY.

### Example

```sql
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

QUALIFY salary_rank = 1;
```

This returns all employees tied for the highest salary in each department.

## 89.2 BigQuery-Specific Considerations

BigQuery has specialized analytical functions and additional syntax.

For example, it supports navigation functions with options such as IGNORE NULLS where applicable.

BigQuery also provides additional functions useful for statistical and analytical processing.

### Important distinction

BigQuery's RANGE frames have their own restrictions, particularly regarding numeric ordering expressions for numeric offset frames.

Do not assume all date-based RANGE syntax is portable.

For date-based moving windows, BigQuery developers commonly use date conversion or other suitable date-based query techniques.

---

# Chapter 90: Important Cross-Database Syntax Comparisons

## 90.1 QUALIFY vs CTE

### MySQL

```sql
WITH ranked AS (
    SELECT
        employee_id,
        salary,

        ROW_NUMBER() OVER (
            ORDER BY salary DESC
        ) AS rn

    FROM employees
)

SELECT *
FROM ranked
WHERE rn = 1;
```

### Snowflake / BigQuery

```sql
SELECT
    employee_id,
    salary,

    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS rn

FROM employees

QUALIFY rn = 1;
```

### Oracle 23ai+

```sql
SELECT
    employee_id,
    salary,

    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS rn

FROM employees

QUALIFY rn = 1;
```

---

## 90.2 Conditional Aggregation

### MySQL

```sql
SUM(
    CASE
        WHEN salary >= 60000 THEN 1
        ELSE 0
    END
) OVER (
    PARTITION BY department
)
```

### PostgreSQL

```sql
COUNT(*) FILTER (
    WHERE salary >= 60000
) OVER (
    PARTITION BY department
)
```

### SQL Server

```sql
SUM(
    CASE
        WHEN salary >= 60000 THEN 1
        ELSE 0
    END
) OVER (
    PARTITION BY department
)
```

---

## 90.3 NULL-Safe Comparison

### MySQL

```sql
old_salary <=> new_salary
```

### PostgreSQL

```sql
old_salary IS NOT DISTINCT FROM new_salary
```

### SQL Server

Use explicit NULL checks or an appropriate NULL-safe comparison expression.

### Oracle

Use explicit NULL checks or an appropriate NULL-safe comparison expression.

Do not assume that MySQL's `<=>` operator works in every SQL database.

---

# Chapter 91: Final Database Compatibility Cheat Sheet

| Feature | MySQL 8.0 | PostgreSQL | SQL Server | Oracle | Snowflake | BigQuery |
|---|---|---|---|---|---|---|
| Ranking functions | Yes | Yes | Yes | Yes | Yes | Yes |
| Aggregate windows | Yes | Yes | Yes | Yes | Yes | Yes |
| LAG / LEAD | Yes | Yes | Yes | Yes | Yes | Yes |
| Value functions | Yes | Yes | Yes | Yes | Yes | Yes |
| Distribution functions | Yes | Yes | Yes | Yes | Yes | Yes |
| Named windows | Yes | Yes | No standard WINDOW clause | Yes | Yes | Yes |
| ROWS | Yes | Yes | Yes | Yes | Yes | Yes |
| RANGE | Yes | Yes | Yes | Yes | Yes | Yes |
| GROUPS | No | Yes | No | Version-dependent | Check version | No |
| QUALIFY | No | No | No | 23ai+ | Yes | Yes |
| FILTER for aggregate windows | No | Yes | No equivalent syntax | No equivalent standard syntax | Supports applicable syntax | Supports applicable syntax |
| COUNT(DISTINCT) OVER | No | Version-dependent restrictions | No general support | Version-dependent restrictions | Supported for applicable functions | Supported with applicable restrictions |

### Final compatibility advice

When writing production SQL for a specific database:

1. Check the function's availability.
2. Check the database version.
3. Check default frame behavior.
4. Check NULL ordering and NULL-handling options.
5. Check offset RANGE restrictions.
6. Check whether GROUPS is supported.
7. Check whether QUALIFY is available.
8. Check whether DISTINCT is supported for the intended window aggregate.
9. Test the query with duplicate ordering values.
10. Validate the output against expected business results.

