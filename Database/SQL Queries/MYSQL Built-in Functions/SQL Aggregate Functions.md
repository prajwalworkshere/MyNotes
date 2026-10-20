# SQL AGGREGATE FUNCTIONS — COMPLETE A TO Z NOTES

**MySQL 8.0+ | Theory + Syntax + Examples + Outputs + Practice Questions + Interview Q&A**

## TABLE OF CONTENTS

1. Introduction to Aggregate Functions
2. Demo Database and Sample Data
3. Aggregate Functions A–Z
4. Understanding `GROUP BY`
5. Understanding `HAVING` vs `WHERE`
6. Using Aggregate Functions in Real Queries
7. Solved SQL Practice Questions
8. Interview Questions and Answers
9. Common Mistakes to Avoid
10. Quick Revision Cheat Sheet
11. Final Learning Roadmap

---

# 1\. INTRODUCTION TO AGGREGATE FUNCTIONS

## What is an Aggregate Function?

An aggregate function is a built-in SQL function that performs a calculation on multiple rows and returns a single result for the selected group of rows.

For example, suppose a company has the following employee salaries:

| Employee | Salary |
| --- | --- |
| Amit | 65000 |
| Priya | 55000 |
| Rahul | 72000 |
| Neha | 60000 |
| Sneha | 58000 |

We can calculate their total salary using an aggregate function.

```sql
SELECT SUM(salary) AS total_salary
FROM employees;
```

Output:

| total\_salary |
| --- |
| 310000 |

The `SUM()` function calculates the total of the salary values.

## Why Do We Use Aggregate Functions?

Aggregate functions are useful for:

- Calculating total sales, revenue, expenses, or salaries.
- Finding average marks, prices, salaries, or ratings.
- Counting employees, customers, orders, or products.
- Finding the highest and lowest values.
- Generating department-wise and city-wise reports.
- Analyzing business performance.
- Finding duplicate values and measuring data quality.
- Calculating statistical measures.
- Summarizing millions of rows into meaningful results.

## General Syntax

```sql
SELECT AGGREGATE_FUNCTION(column_name)
FROM table_name;
```

Example:

```sql
SELECT AVG(salary) AS average_salary
FROM employees;
```

Output:

| average\_salary |
| --- |
| 62000.000000 |

## The Five Most Important Aggregate Functions

| Function | Purpose | Example |
| --- | --- | --- |
| `COUNT()` | Counts rows or non-NULL values | `COUNT(*)` |
| `SUM()` | Calculates a total | `SUM(salary)` |
| `AVG()` | Calculates an average | `AVG(salary)` |
| `MIN()` | Finds the minimum value | `MIN(salary)` |
| `MAX()` | Finds the maximum value | `MAX(salary)` |

These five functions are the foundation of SQL aggregate queries.

## Aggregate Functions vs Scalar Functions

A scalar function generally returns one result for each input row. An aggregate function summarizes values across multiple rows.

**Scalar function example:**

```sql
SELECT UPPER(first_name)
FROM employees;
```

This returns one uppercase name for each employee row.

**Aggregate function example:**

```sql
SELECT COUNT(*) AS total_employees
FROM employees;
```

This returns one count for the selected set of rows.

## Important Rules

1. Most aggregate functions ignore `NULL` values in the expression they aggregate.
2. `COUNT(*)` counts rows, including rows containing `NULL` values.
3. Aggregate functions can be used with `GROUP BY` to calculate results for each group.
4. `HAVING` filters groups after aggregation.
5. `WHERE` filters individual rows before aggregation.
6. Most aggregate functions return one result for the entire selected set when `GROUP BY` is absent.
7. An aggregate query without `GROUP BY` generally returns one row even when no rows match. `COUNT(*)` returns `0`; `SUM()`, `AVG()`, `MIN()`, and `MAX()` return `NULL` when there are no applicable values.

---

# 2\. DEMO DATABASE AND SAMPLE DATA

We will use the following `employees` table throughout these notes.

## Create the table

```sql
CREATE TABLE employees (
    emp_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    city VARCHAR(50),
    department VARCHAR(50),
    salary DECIMAL(10,2)
);
```

## Insert sample records

```sql
INSERT INTO employees
(emp_id, first_name, last_name, email, city, department, salary)
VALUES
(1, 'Amit', 'Sharma', 'amit.sharma@gmail.com',
 'Mumbai', 'IT', 65000),

(2, 'Priya', 'Patil', 'priya.patil@yahoo.com',
 'Pune', 'HR', 55000),

(3, 'Rahul', 'Verma', 'rahul.verma@gmail.com',
 'Delhi', 'IT', 72000),

(4, 'Neha', 'Joshi', 'neha.joshi@outlook.com',
 'Nashik', 'Finance', 60000),

(5, 'Sneha', 'Kulkarni', 'sneha.k@gmail.com',
 'Mumbai', 'HR', 58000);
```

## View the data

```sql
SELECT *
FROM employees;
```

Expected data:

| emp\_id | first\_name | last\_name | city | department | salary |
| --- | --- | --- | --- | --- | --- |
| 1 | Amit | Sharma | Mumbai | IT | 65000 |
| 2 | Priya | Patil | Pune | HR | 55000 |
| 3 | Rahul | Verma | Delhi | IT | 72000 |
| 4 | Neha | Joshi | Nashik | Finance | 60000 |
| 5 | Sneha | Kulkarni | Mumbai | HR | 58000 |

## Summary of the Sample Data

- Total employees: 5
- Total salary: 310000
- Average salary: 62000
- Highest salary: 72000
- Lowest salary: 55000
- IT employees: 2
- HR employees: 2
- Finance employees: 1

We will use these values to verify our query results.

---

# 3\. SQL AGGREGATE FUNCTIONS A–Z

## 3.1 COUNT()

### Theory

`COUNT()` counts rows or non-NULL values, depending on the expression supplied.

It is one of the most frequently used aggregate functions in SQL.

### Syntax

```sql
COUNT(*)

COUNT(column_name)

COUNT(DISTINCT column_name)
```

### Example 1: Count all rows

```sql
SELECT COUNT(*) AS total_employees
FROM employees;
```

Output:

| total\_employees |
| --- |
| 5 |

Explanation: `COUNT(*)` counts every row returned by the query.

### Example 2: Count non-NULL values

```sql
SELECT COUNT(email) AS employees_with_email
FROM employees;
```

Output:

| employees\_with\_email |
| --- |
| 5 |

Explanation: All five employees have an email value.

If an employee's email were `NULL`, that row would not be counted by `COUNT(email)`.

### Example 3: Count distinct values

```sql
SELECT COUNT(DISTINCT city) AS unique_cities
FROM employees;
```

Output:

| unique\_cities |
| --- |
| 4 |

Explanation: The cities are Mumbai, Pune, Delhi, and Nashik. Mumbai appears twice, but is counted once.

### Example 4: Count employees in a department

```sql
SELECT COUNT(*) AS it_employees
FROM employees
WHERE department = 'IT';
```

Output:

| it\_employees |
| --- |
| 2 |

### Example 5: Count employees by department

```sql
SELECT
    department,
    COUNT(*) AS total_employees
FROM employees
GROUP BY department;
```

Output:

| department | total\_employees |
| --- | --- |
| Finance | 1 |
| HR | 2 |
| IT | 2 |

The displayed group order is not guaranteed without `ORDER BY`.

### Common Uses

- Counting customers.
- Counting employees by department.
- Counting orders by status.
- Finding the number of distinct cities.
- Counting non-NULL values in a column.
- Checking duplicate records.

### Important Difference

| Expression | Meaning |
| --- | --- |
| `COUNT(*)` | Counts all rows |
| `COUNT(column)` | Counts rows where that expression is not NULL |
| `COUNT(DISTINCT column)` | Counts distinct non-NULL values |

---

## 3.2 SUM()

### Theory

`SUM()` calculates the total of numeric values in a column or expression.

It is commonly used for revenue, salary, sales, expenses, quantities, and marks.

### Syntax

```sql
SUM(expression)
```

### Example 1: Calculate total salary

```sql
SELECT SUM(salary) AS total_salary
FROM employees;
```

Output:

| total\_salary |
| --- |
| 310000.00 |

Calculation:

```
65000 + 55000 + 72000 + 60000 + 58000 = 310000
```

### Example 2: Calculate IT department salary

```sql
SELECT SUM(salary) AS it_total_salary
FROM employees
WHERE department = 'IT';
```

Output:

| it\_total\_salary |
| --- |
| 137000.00 |

Calculation:

```
65000 + 72000 = 137000
```

### Example 3: Calculate total salary by department

```sql
SELECT
    department,
    SUM(salary) AS total_salary
FROM employees
GROUP BY department;
```

Output:

| department | total\_salary |
| --- | --- |
| Finance | 60000.00 |
| HR | 113000.00 |
| IT | 137000.00 |

### Example 4: Calculate salary after a bonus

Suppose every employee receives a bonus of 5000.

```sql
SELECT
    SUM(salary) AS total_salary,
    SUM(salary + 5000) AS total_salary_with_bonus
FROM employees;
```

Output:

| total\_salary | total\_salary\_with\_bonus |
| --- | --- |
| 310000.00 | 335000.00 |

### Important Points

- `SUM()` works with numeric expressions.
- It ignores NULL values in the aggregated expression.
- If there are no non-NULL values to sum, the result is `NULL`.
- Use `COALESCE(SUM(column_name), 0)` when a zero result is required instead of NULL.

Example:

```sql
SELECT COALESCE(SUM(salary), 0) AS total_salary
FROM employees
WHERE department = 'Marketing';
```

If no Marketing employees exist, the output is `0`.

---

## 3.3 AVG()

### Theory

`AVG()` calculates the arithmetic mean of non-NULL numeric values.

The formula is:

```
Average = Sum of values / Number of non-NULL values
```

### Syntax

```sql
AVG(expression)
```

### Example 1: Calculate average salary

```sql
SELECT AVG(salary) AS average_salary
FROM employees;
```

Output:

| average\_salary |
| --- |
| 62000.000000 |

Calculation:

```
310000 / 5 = 62000
```

### Example 2: Calculate average IT salary

```sql
SELECT AVG(salary) AS average_it_salary
FROM employees
WHERE department = 'IT';
```

Output:

| average\_it\_salary |
| --- |
| 68500.000000 |

Calculation:

```
(65000 + 72000) / 2 = 68500
```

### Example 3: Calculate average salary by department

```sql
SELECT
    department,
    AVG(salary) AS average_salary
FROM employees
GROUP BY department;
```

Output:

| department | average\_salary |
| --- | --- |
| Finance | 60000.000000 |
| HR | 56500.000000 |
| IT | 68500.000000 |

### Example 4: Find employees earning above the overall average

```sql
SELECT
    first_name,
    department,
    salary
FROM employees
WHERE salary > (
    SELECT AVG(salary)
    FROM employees
);
```

Output:

| first\_name | department | salary |
| --- | --- | --- |
| Amit | IT | 65000.00 |
| Rahul | IT | 72000.00 |

### Important Points

- `AVG()` ignores NULL values.
- The denominator is the number of non-NULL values, not necessarily the total number of rows.
- The output's displayed decimal precision can depend on the expression and client.
- `AVG()` is not the same as the median.

---

## 3.4 MIN()

### Theory

`MIN()` returns the smallest value in the selected set.

It can work with numbers, dates, and strings. For strings, the result depends on the applicable collation and sorting rules.

### Syntax

```sql
MIN(expression)
```

### Example 1: Find the lowest salary

```sql
SELECT MIN(salary) AS lowest_salary
FROM employees;
```

Output:

| lowest\_salary |
| --- |
| 55000.00 |

### Example 2: Find the lowest salary in IT

```sql
SELECT MIN(salary) AS lowest_it_salary
FROM employees
WHERE department = 'IT';
```

Output:

| lowest\_it\_salary |
| --- |
| 65000.00 |

### Example 3: Find the lowest salary by department

```sql
SELECT
    department,
    MIN(salary) AS lowest_salary
FROM employees
GROUP BY department;
```

Output:

| department | lowest\_salary |
| --- | --- |
| Finance | 60000.00 |
| HR | 55000.00 |
| IT | 65000.00 |

### Example 4: Find the earliest employee email alphabetically

```sql
SELECT MIN(email) AS first_email_alphabetically
FROM employees;
```

The result is determined by the column's collation. `MIN()` does not necessarily return the earliest email by insertion order.

### Important Points

- NULL values are ignored.
- If there are no non-NULL values, `MIN()` returns NULL.
- `MIN()` returns the minimum value, not the entire row containing that value.

To retrieve the employee with the lowest salary:

```sql
SELECT first_name, department, salary
FROM employees
WHERE salary = (
    SELECT MIN(salary)
    FROM employees
);
```

Output:

| first\_name | department | salary |
| --- | --- | --- |
| Priya | HR | 55000.00 |

If multiple employees share the lowest salary, all matching employees are returned.

---

## 3.5 MAX()

### Theory

`MAX()` returns the largest value in the selected set.

It is commonly used to find the highest salary, maximum sale, latest date, or largest quantity.

### Syntax

```sql
MAX(expression)
```

### Example 1: Find the highest salary

```sql
SELECT MAX(salary) AS highest_salary
FROM employees;
```

Output:

| highest\_salary |
| --- |
| 72000.00 |

### Example 2: Find the highest IT salary

```sql
SELECT MAX(salary) AS highest_it_salary
FROM employees
WHERE department = 'IT';
```

Output:

| highest\_it\_salary |
| --- |
| 72000.00 |

### Example 3: Find the highest salary by department

```sql
SELECT
    department,
    MAX(salary) AS highest_salary
FROM employees
GROUP BY department;
```

Output:

| department | highest\_salary |
| --- | --- |
| Finance | 60000.00 |
| HR | 58000.00 |
| IT | 72000.00 |

### Example 4: Find the employee with the highest salary

```sql
SELECT first_name, department, salary
FROM employees
WHERE salary = (
    SELECT MAX(salary)
    FROM employees
);
```

Output:

| first\_name | department | salary |
| --- | --- | --- |
| Rahul | IT | 72000.00 |

### Important Points

- NULL values are ignored.
- `MAX()` returns the maximum value, not the entire row.
- Use a subquery or an appropriate `ORDER BY` and `LIMIT` query to retrieve the associated employee record.

---

## 3.6 GROUP\_CONCAT()

### Theory

`GROUP_CONCAT()` combines values from multiple rows into one string.

It is especially useful for reports that need a list of names, products, categories, or IDs for each group.

**MySQL-specific function:** Other databases have different list-aggregation functions.

### Syntax

```sql
GROUP_CONCAT(expression)
```

With a separator:

```
GROUP_CONCAT(
    expression
    SEPARATOR 'separator'
)
```

With distinct values and ordering:

```
GROUP_CONCAT(
    DISTINCT expression
    ORDER BY expression
    SEPARATOR ', '
)
```

### Example 1: Combine all employee names

```sql
SELECT GROUP_CONCAT(first_name) AS employee_names
FROM employees;
```

The result contains all five names in a single string. Their order is not guaranteed unless `ORDER BY` is specified inside `GROUP_CONCAT()`.

### Example 2: Combine employee names by department

```sql
SELECT
    department,
    GROUP_CONCAT(
        first_name
        ORDER BY first_name
        SEPARATOR ', '
    ) AS employee_names
FROM employees
GROUP BY department;
```

Output:

| department | employee\_names |
| --- | --- |
| Finance | Neha |
| HR | Priya, Sneha |
| IT | Amit, Rahul |

### Example 3: Combine distinct cities

```sql
SELECT GROUP_CONCAT(
    DISTINCT city
    ORDER BY city
    SEPARATOR ', '
) AS cities
FROM employees;
```

Output:

| cities |
| --- |
| Delhi, Mumbai, Nashik, Pune |

### Example 4: Create a department report

```sql
SELECT
    department,
    COUNT(*) AS total_employees,
    GROUP_CONCAT(
        first_name
        ORDER BY first_name
        SEPARATOR ', '
    ) AS employee_names
FROM employees
GROUP BY department;
```

Output:

| department | total\_employees | employee\_names |
| --- | --- | --- |
| Finance | 1 | Neha |
| HR | 2 | Priya, Sneha |
| IT | 2 | Amit, Rahul |

### Important Points

- `GROUP_CONCAT()` ignores NULL expressions.
- `DISTINCT` removes duplicate values.
- `ORDER BY` inside the function controls the order of concatenated values.
- MySQL limits the result length using `group_concat_max_len`, whose default is commonly 1024 bytes.
- Large concatenated results may be truncated if the configured limit is reached.
- Avoid treating the resulting string as a substitute for properly structured relational data.

---

## 3.7 STDDEV\_POP()

### Theory

`STDDEV_POP()` calculates the population standard deviation of numeric values.

Standard deviation measures how far values tend to spread around their mean.

A lower standard deviation indicates values are more closely clustered around the mean; a higher value indicates greater spread.

### Syntax


```sql
STDDEV_POP(expression)
```

### Example

```sql
SELECT STDDEV_POP(salary) AS population_stddev
FROM employees;
```

For the sample salaries, the approximate output is:

| population\_stddev |
| --- |
| 5949.789916 |

### When to Use It

- Measuring salary variation.
- Analyzing population-level datasets.
- Studying the spread of sales or marks.

### Important Difference

`STDDEV_POP()` treats the selected data as the entire population. `STDDEV_SAMP()` treats it as a sample and uses a different denominator.

---

## 3.8 STDDEV\_SAMP()

### Theory

`STDDEV_SAMP()` calculates the sample standard deviation.

It is useful when the available records represent a sample from a larger population.

### Syntax

```sql
STDDEV_SAMP(expression)
```

### Example

```sql
SELECT STDDEV_SAMP(salary) AS sample_stddev
FROM employees;
```

Approximate output:

| sample\_stddev |
| --- |
| 6658.328118 |

### Population vs Sample Standard Deviation

| Function | Purpose |
| --- | --- |
| `STDDEV_POP()` | Standard deviation of the full population |
| `STDDEV_SAMP()` | Estimated standard deviation based on a sample |

For a single non-NULL observation, `STDDEV_SAMP()` returns NULL because there is insufficient information to estimate sample variation.

---

## 3.9 VAR\_POP()

### Theory

`VAR_POP()` calculates the population variance of numeric values.

Variance is the average squared distance of values from the population mean.

### Syntax

```sql
VAR_POP(expression)
```

### Example

```sql
SELECT VAR_POP(salary) AS population_variance
FROM employees;
```

Approximate output:

| population\_variance |
| --- |
| 35399999.999999 |

Small differences in the displayed decimals may occur because of numeric representation.

### Use

- Statistical reporting.
- Measuring variability in sales or salaries.
- Comparing the spread of different datasets.

---

## 3.10 VAR\_SAMP()

### Theory

`VAR_SAMP()` calculates sample variance.

It divides the sum of squared deviations by one less than the number of non-NULL observations.

### Syntax

```sql
VAR_SAMP(expression)
```

### Example

```sql
SELECT VAR_SAMP(salary) AS sample_variance
FROM employees;
```

Approximate output:

| sample\_variance |
| --- |
| 44300000.000000 |

### Difference Between Variance Functions

| Function | Meaning |
| --- | --- |
| `VAR_POP()` | Population variance |
| `VAR_SAMP()` | Sample variance |

MySQL also supports `VARIANCE()` as a synonym for population variance.

---

## 3.11 BIT\_AND()

### Theory

`BIT_AND()` performs a bitwise AND operation across the values in a group.

It is an advanced aggregate function generally used with integer values or bitmasks.

### Syntax

```sql
BIT_AND(expression)
```

### Example

```sql
SELECT BIT_AND(value) AS result
FROM (
    SELECT 7 AS value
    UNION ALL
    SELECT 3
    UNION ALL
    SELECT 1
) AS numbers;
```

Output:

| result |
| --- |
| 1 |

Explanation:

```
7 = 111
3 = 011
1 = 001
---------
    001 = 1
```

Only the bits that are set in every value remain set.

### Use

- Bitmask analysis.
- Permission flags.
- Integer-based feature flags.

---

## 3.12 BIT\_OR()

### Theory

`BIT_OR()` performs a bitwise OR operation across values in a group.

A bit remains set if it is set in at least one input value.

### Syntax

```sql
BIT_OR(expression)
```

### Example

```sql
SELECT BIT_OR(value) AS result
FROM (
    SELECT 4 AS value
    UNION ALL
    SELECT 2
    UNION ALL
    SELECT 1
) AS numbers;
```

Output:

| result |
| --- |
| 7 |

Explanation:

```
4 = 100
2 = 010
1 = 001
---------
    111 = 7
```

### Use

Combining permission bits or feature flags.

---

## 3.13 BIT\_XOR()

### Theory

`BIT_XOR()` performs a bitwise exclusive OR operation across the values in a group.

For each bit, the result is set when the number of set input bits is odd.

### Syntax

```sql
BIT_XOR(expression)
```

### Example

```sql
SELECT BIT_XOR(value) AS result
FROM (
    SELECT 5 AS value
    UNION ALL
    SELECT 3
) AS numbers;
```

Output:

| result |
| --- |
| 6 |

Explanation:

```
5 = 101
3 = 011
---------
    110 = 6
```

### Use

- Bitwise calculations.
- Specialized checksum and flag-processing tasks.

---

## 3.14 JSON\_ARRAYAGG()

### Theory

`JSON_ARRAYAGG()` collects values from multiple rows into a JSON array.

It is useful when building JSON output from relational data.

### Syntax

```sql
JSON_ARRAYAGG(expression)
```

### Example

```sql
SELECT JSON_ARRAYAGG(first_name) AS employee_names
FROM employees;
```

The result is a JSON array containing the employee names, for example:

```
["Amit", "Priya", "Rahul", "Neha", "Sneha"]
```

The order of elements is not guaranteed unless the relevant query and database version provide a supported ordering mechanism.

### Example: Build a JSON array for each department

```sql
SELECT
    department,
    JSON_ARRAYAGG(first_name) AS employee_names
FROM employees
GROUP BY department;
```

Each department returns its own JSON array.

### Use

- JSON API responses.
- Reporting and application integration.
- Grouping relational values into structured JSON.

---

## 3.15 JSON\_OBJECTAGG()

### Theory

`JSON_OBJECTAGG()` combines key-value pairs from multiple rows into a JSON object.

### Syntax

```sql
JSON_OBJECTAGG(key, value)
```

### Example

```sql
SELECT JSON_OBJECTAGG(
    first_name,
    salary
) AS employee_salary_map
FROM employees;
```

Example result:

```
{
  "Amit": 65000,
  "Priya": 55000,
  "Rahul": 72000,
  "Neha": 60000,
  "Sneha": 58000
}
```

The actual JSON representation and key ordering may vary.

### Important Points

- Keys should be suitable for JSON object keys.
- Duplicate keys require care; the resulting value for a duplicated key is not a reliable way to preserve multiple records.
- If you need every row, a JSON array is usually more appropriate.

---

## 3.16 APPROX\_COUNT\_DISTINCT() — Database Compatibility Note

Some database systems provide approximate distinct-count aggregates for large analytical datasets.

MySQL 8.0 does not provide a built-in aggregate named `APPROX_COUNT_DISTINCT()`.

In MySQL, use the exact calculation:

```sql
SELECT COUNT(DISTINCT city) AS unique_cities
FROM employees;
```

In a database that supports approximate distinct counting, the corresponding function can estimate the number of unique values with lower computational cost in suitable workloads.

**Remember:** Do not use a function merely because it appears in another database's documentation. Verify support in your SQL dialect.

---

# 4\. UNDERSTANDING GROUP BY

## What Is GROUP BY?

`GROUP BY` divides rows into groups that share the same value or combination of values.

Aggregate functions then calculate a separate result for each group.

Without `GROUP BY`, an aggregate query generally summarizes the entire selected dataset.

With `GROUP BY`, it summarizes each group separately.

## Example 1: Total salary without GROUP BY

```sql
SELECT SUM(salary) AS total_salary
FROM employees;
```

Output:

| total\_salary |
| --- |
| 310000.00 |

One result is returned for all employees.

## Example 2: Total salary with GROUP BY

```sql
SELECT
    department,
    SUM(salary) AS total_salary
FROM employees
GROUP BY department;
```

Output:

| department | total\_salary |
| --- | --- |
| Finance | 60000.00 |
| HR | 113000.00 |
| IT | 137000.00 |

A separate result is returned for each department.

## GROUP BY Multiple Columns

Suppose we want to calculate the number of employees for every city and department combination.

```sql
SELECT
    city,
    department,
    COUNT(*) AS total_employees
FROM employees
GROUP BY city, department;
```

Output:

| city | department | total\_employees |
| --- | --- | --- |
| Delhi | IT | 1 |
| Mumbai | HR | 1 |
| Mumbai | IT | 1 |
| Nashik | Finance | 1 |
| Pune | HR | 1 |

Each distinct combination becomes a separate group.

## GROUP BY with Multiple Aggregates

```sql
SELECT
    department,
    COUNT(*) AS total_employees,
    SUM(salary) AS total_salary,
    AVG(salary) AS average_salary,
    MIN(salary) AS minimum_salary,
    MAX(salary) AS maximum_salary
FROM employees
GROUP BY department;
```

Output:

| department | total\_employees | total\_salary | average\_salary | minimum\_salary | maximum\_salary |
| --- | --- | --- | --- | --- | --- |
| Finance | 1 | 60000 | 60000 | 60000 | 60000 |
| HR | 2 | 113000 | 56500 | 55000 | 58000 |
| IT | 2 | 137000 | 68500 | 65000 | 72000 |

This is a typical department-level reporting query.

## Important GROUP BY Rule

When selecting aggregate results alongside ordinary columns, those ordinary columns generally need to be included in `GROUP BY` or otherwise be valid under the database's grouping rules.

Correct:

```sql
SELECT department, SUM(salary)
FROM employees
GROUP BY department;
```

Problematic in standard SQL:

```sql
SELECT department, first_name, SUM(salary)
FROM employees
GROUP BY department;
```

The query asks for one `first_name` per department without specifying which employee should be selected. MySQL with `ONLY_FULL_GROUP_BY` enabled rejects this query.

Do not disable grouping safeguards simply to make an ambiguous query run.

---

# 5\. HAVING VS WHERE

Both `WHERE` and `HAVING` filter results, but they work at different stages.

## WHERE

`WHERE` filters individual rows before grouping and aggregation.

Example: Find employees earning more than 60000.

```sql
SELECT *
FROM employees
WHERE salary > 60000;
```

Output:

| first\_name | salary |
| --- | --- |
| Amit | 65000 |
| Rahul | 72000 |

## HAVING

`HAVING` filters groups after aggregation.

Example: Find departments whose average salary is greater than 60000.

```sql
SELECT
    department,
    AVG(salary) AS average_salary
FROM employees
GROUP BY department
HAVING AVG(salary) > 60000;
```

Output:

| department | average\_salary |
| --- | --- |
| IT | 68500.00 |

## Main Differences

| Feature | WHERE | HAVING |
| --- | --- | --- |
| Filters | Individual rows | Groups |
| Applied conceptually | Before grouping | After grouping |
| Common use | Filter rows by salary, city, date | Filter groups by count, sum, or average |
| Aggregate conditions | Generally not directly allowed | Commonly used |
| Can be used without GROUP BY? | Yes | Yes, but without grouping it filters the single aggregate result group |

## Example: WHERE and HAVING Together

Requirement: Calculate the total salary of employees earning more than 55000 in each department, and return departments with a resulting total greater than 100000.

```sql
SELECT
    department,
    SUM(salary) AS total_salary
FROM employees
WHERE salary > 55000
GROUP BY department
HAVING SUM(salary) > 100000;
```

Output:

| department | total\_salary |
| --- | --- |
| HR | 58000.00 |
| IT | 137000.00 |

Correction: HR has only Sneha remaining after the `WHERE` filter, so its total is 58000 and does not exceed 100000. Therefore, the correct output is:

| department | total\_salary |
| --- | --- |
| IT | 137000.00 |

Explanation:

1. `WHERE salary > 55000` removes Priya, who earns exactly 55000.
2. `GROUP BY department` forms department groups from the remaining rows.
3. `SUM(salary)` calculates each group's total.
4. `HAVING SUM(salary) > 100000` retains only the IT group.

---

# 6\. USING AGGREGATE FUNCTIONS IN REAL QUERIES

## 6.1 Aggregate Functions with ORDER BY

Requirement: Display departments ordered by total salary from highest to lowest.

```sql
SELECT
    department,
    SUM(salary) AS total_salary
FROM employees
GROUP BY department
ORDER BY total_salary DESC;
```

Output:

| department | total\_salary |
| --- | --- |
| IT | 137000.00 |
| HR | 113000.00 |
| Finance | 60000.00 |

`DESC` means descending order. `ASC` means ascending order.

## 6.2 Aggregate Functions with DISTINCT

Requirement: Count unique departments.

```sql
SELECT COUNT(DISTINCT department) AS unique_departments
FROM employees;
```

Output:

| unique\_departments |
| --- |
| 3 |

You can also use `DISTINCT` with other aggregates where supported and meaningful.

Example:

```sql
SELECT SUM(DISTINCT salary) AS distinct_salary_total
FROM employees;
```

This sums each distinct salary value once. It does not calculate the usual total payroll if multiple employees have the same salary.

## 6.3 Aggregate Functions with CASE

Requirement: Count IT and HR employees in the same result row.

```sql
SELECT
    SUM(CASE WHEN department = 'IT' THEN 1 ELSE 0 END)
        AS it_employees,
    SUM(CASE WHEN department = 'HR' THEN 1 ELSE 0 END)
        AS hr_employees
FROM employees;
```

Output:

| it\_employees | hr\_employees |
| --- | --- |
| 2 | 2 |

This technique is commonly called conditional aggregation.

## 6.4 Conditional SUM()

Requirement: Calculate the total salary for IT and HR separately.

```sql
SELECT
    SUM(CASE
        WHEN department = 'IT' THEN salary
        ELSE 0
    END) AS it_salary,
    SUM(CASE
        WHEN department = 'HR' THEN salary
        ELSE 0
    END) AS hr_salary
FROM employees;
```

Output:

| it\_salary | hr\_salary |
| --- | --- |
| 137000.00 | 113000.00 |

## 6.5 Aggregate Functions with Subqueries

Requirement: Find employees earning more than the company's average salary.

```sql
SELECT
    first_name,
    salary
FROM employees
WHERE salary > (
    SELECT AVG(salary)
    FROM employees
);
```

Output:

| first\_name | salary |
| --- | --- |
| Amit | 65000.00 |
| Rahul | 72000.00 |

The subquery calculates the overall average, and the outer query compares every employee's salary against it.

## 6.6 Aggregate Functions with ROUND()

Requirement: Display average salary rounded to two decimal places.

```sql
SELECT ROUND(AVG(salary), 2) AS average_salary
FROM employees;
```

Output:

| average\_salary |
| --- |
| 62000.00 |

`ROUND()` is a numeric function, not an aggregate function. It is used here to format the aggregate result.

## 6.7 Aggregate Functions with NULL

Consider this example:

```sql
SELECT
    COUNT(*) AS total_rows,
    COUNT(salary) AS salaries_present,
    SUM(salary) AS total_salary,
    AVG(salary) AS average_salary
FROM employees;
```

For the current sample data, all salaries are present, so both counts are 5.

If a salary were NULL:

- `COUNT(*)` would still count that row.
- `COUNT(salary)` would not count that row.
- `SUM(salary)` would ignore that NULL salary.
- `AVG(salary)` would average the non-NULL salaries only.

## 6.8 Aggregate Functions with ROLLUP

`WITH ROLLUP` can produce subtotal and grand-total rows for grouped results in MySQL.

Example:

```sql
SELECT
    department,
    SUM(salary) AS total_salary
FROM employees
GROUP BY department WITH ROLLUP;
```

The result includes department totals and a grand-total row whose `department` value is NULL.

The grand total is:

```
310000.00
```

Use `ROLLUP` for subtotal reports. Be aware that NULL in a rollup result can represent a subtotal or grand total rather than a missing source value.

---

# 7\. SOLVED SQL PRACTICE QUESTIONS

Use the `employees` table created in Section 2.

## Question 1: Count all employees.

```sql
SELECT COUNT(*) AS total_employees
FROM employees;
```

Answer:

| total\_employees |
| --- |
| 5 |

## Question 2: Calculate the total salary of all employees.

```sql
SELECT SUM(salary) AS total_salary
FROM employees;
```

Answer:

| total\_salary |
| --- |
| 310000.00 |

## Question 3: Find the average salary.

```sql
SELECT AVG(salary) AS average_salary
FROM employees;
```

Answer:

| average\_salary |
| --- |
| 62000.000000 |

## Question 4: Find the highest salary.

```sql
SELECT MAX(salary) AS highest_salary
FROM employees;
```

Answer:

| highest\_salary |
| --- |
| 72000.00 |

## Question 5: Find the lowest salary.

```sql
SELECT MIN(salary) AS lowest_salary
FROM employees;
```

Answer:

| lowest\_salary |
| --- |
| 55000.00 |

## Question 6: Count employees in each department.

```sql
SELECT
    department,
    COUNT(*) AS total_employees
FROM employees
GROUP BY department
ORDER BY department;
```

Answer:

| department | total\_employees |
| --- | --- |
| Finance | 1 |
| HR | 2 |
| IT | 2 |

## Question 7: Calculate the total salary of each department.

```sql
SELECT
    department,
    SUM(salary) AS total_salary
FROM employees
GROUP BY department
ORDER BY department;
```

Answer:

| department | total\_salary |
| --- | --- |
| Finance | 60000.00 |
| HR | 113000.00 |
| IT | 137000.00 |

## Question 8: Find the average salary of each department.

```sql
SELECT
    department,
    AVG(salary) AS average_salary
FROM employees
GROUP BY department
ORDER BY department;
```

Answer:

| department | average\_salary |
| --- | --- |
| Finance | 60000.00 |
| HR | 56500.00 |
| IT | 68500.00 |

## Question 9: Find departments with more than one employee.

```sql
SELECT
    department,
    COUNT(*) AS total_employees
FROM employees
GROUP BY department
HAVING COUNT(*) > 1;
```

Answer:

| department | total\_employees |
| --- | --- |
| HR | 2 |
| IT | 2 |

## Question 10: Find employees earning more than the overall average.

```sql
SELECT
    first_name,
    salary
FROM employees
WHERE salary > (
    SELECT AVG(salary)
    FROM employees
);
```

Answer:

| first\_name | salary |
| --- | --- |
| Amit | 65000.00 |
| Rahul | 72000.00 |

## Question 11: Find the department with the highest total salary.

```sql
SELECT
    department,
    SUM(salary) AS total_salary
FROM employees
GROUP BY department
ORDER BY total_salary DESC
LIMIT 1;
```

Answer:

| department | total\_salary |
| --- | --- |
| IT | 137000.00 |

This returns one department. If multiple departments tie for the highest total, `LIMIT 1` returns only one of them.

## Question 12: Count employees in Mumbai.

```sql
SELECT COUNT(*) AS mumbai_employees
FROM employees
WHERE city = 'Mumbai';
```

Answer:

| mumbai\_employees |
| --- |
| 2 |

## Question 13: Calculate the total salary of employees in Mumbai.

```sql
SELECT SUM(salary) AS mumbai_total_salary
FROM employees
WHERE city = 'Mumbai';
```

Answer:

| mumbai\_total\_salary |
| --- |
| 123000.00 |

## Question 14: Find departments with an average salary greater than 60000.

```sql
SELECT
    department,
    AVG(salary) AS average_salary
FROM employees
GROUP BY department
HAVING AVG(salary) > 60000;
```

Answer:

| department | average\_salary |
| --- | --- |
| IT | 68500.00 |

## Question 15: Count unique cities.

```sql
SELECT COUNT(DISTINCT city) AS unique_cities
FROM employees;
```

Answer:

| unique\_cities |
| --- |
| 4 |

## Question 16: Find the employee or employees earning the minimum salary.

```sql
SELECT
    first_name,
    department,
    salary
FROM employees
WHERE salary = (
    SELECT MIN(salary)
    FROM employees
);
```

Answer:

| first\_name | department | salary |
| --- | --- | --- |
| Priya | HR | 55000.00 |

## Question 17: Find the employee or employees earning the maximum salary.

```sql
SELECT
    first_name,
    department,
    salary
FROM employees
WHERE salary = (
    SELECT MAX(salary)
    FROM employees
);
```

Answer:

| first\_name | department | salary |
| --- | --- | --- |
| Rahul | IT | 72000.00 |

## Question 18: Display employee count, total salary, minimum salary, and maximum salary in one query.

```sql
SELECT
    COUNT(*) AS total_employees,
    SUM(salary) AS total_salary,
    MIN(salary) AS minimum_salary,
    MAX(salary) AS maximum_salary
FROM employees;
```

Answer:

| total\_employees | total\_salary | minimum\_salary | maximum\_salary |
| --- | --- | --- | --- |
| 5 | 310000.00 | 55000.00 | 72000.00 |

## Question 19: Find the number of employees earning more than 60000 in each department.

```sql
SELECT
    department,
    COUNT(*) AS total_employees
FROM employees
WHERE salary > 60000
GROUP BY department;
```

Answer:

| department | total\_employees |
| --- | --- |
| IT | 2 |

Explanation: Both IT employees earn more than 60000. Amit earns 65000 and Rahul earns 72000.

## Question 20: Find departments whose total salary exceeds 100000.

```sql
SELECT
    department,
    SUM(salary) AS total_salary
FROM employees
GROUP BY department
HAVING SUM(salary) > 100000;
```

Answer:

| department | total\_salary |
| --- | --- |
| HR | 113000.00 |
| IT | 137000.00 |

## Question 21: Calculate the percentage of employees in each department.

```sql
SELECT
    department,
    COUNT(*) AS total_employees,
    ROUND(
        COUNT(*) * 100.0 /
        (SELECT COUNT(*) FROM employees),
        2
    ) AS employee_percentage
FROM employees
GROUP BY department;
```

Answer:

| department | total\_employees | employee\_percentage |
| --- | --- | --- |
| Finance | 1 | 20.00 |
| HR | 2 | 40.00 |
| IT | 2 | 40.00 |

## Question 22: Count employees by city and department.

```sql
SELECT
    city,
    department,
    COUNT(*) AS total_employees
FROM employees
GROUP BY city, department
ORDER BY city, department;
```

Answer:

| city | department | total\_employees |
| --- | --- | --- |
| Delhi | IT | 1 |
| Mumbai | HR | 1 |
| Mumbai | IT | 1 |
| Nashik | Finance | 1 |
| Pune | HR | 1 |

## Question 23: Display department-wise minimum, maximum, and average salary.

```sql
SELECT
    department,
    MIN(salary) AS minimum_salary,
    MAX(salary) AS maximum_salary,
    ROUND(AVG(salary), 2) AS average_salary
FROM employees
GROUP BY department;
```

Answer:

| department | minimum\_salary | maximum\_salary | average\_salary |
| --- | --- | --- | --- |
| Finance | 60000.00 | 60000.00 | 60000.00 |
| HR | 55000.00 | 58000.00 | 56500.00 |
| IT | 65000.00 | 72000.00 | 68500.00 |

## Question 24: Display all department names and employee names in one row per department.

```sql
SELECT
    department,
    GROUP_CONCAT(
        first_name
        ORDER BY first_name
        SEPARATOR ', '
    ) AS employees
FROM employees
GROUP BY department;
```

Answer:

| department | employees |
| --- | --- |
| Finance | Neha |
| HR | Priya, Sneha |
| IT | Amit, Rahul |

## Question 25: Find departments where every employee earns more than 55000.

```sql
SELECT
    department,
    MIN(salary) AS minimum_salary
FROM employees
GROUP BY department
HAVING MIN(salary) > 55000;
```

Answer:

| department | minimum\_salary |
| --- | --- |
| Finance | 60000.00 |
| IT | 65000.00 |

Explanation: The minimum salary in each qualifying department must be greater than 55000. HR is excluded because Priya earns exactly 55000.

---

# 8\. COMMON INTERVIEW QUESTIONS AND ANSWERS

## Q1. What are aggregate functions in SQL?

Aggregate functions summarize values across multiple rows and return a result for the selected set or each group.

Examples include `COUNT()`, `SUM()`, `AVG()`, `MIN()`, and `MAX()`.

## Q2. What is the difference between COUNT(\*) and COUNT(column)?

- `COUNT(*)` counts all rows.
- `COUNT(column)` counts rows where the column expression is not NULL.

Example:

```sql
SELECT COUNT(*), COUNT(email)
FROM employees;
```

Both return 5 because every employee currently has an email value.

## Q3. What is the difference between COUNT(column) and COUNT(DISTINCT column)?

`COUNT(column)` counts all non-NULL values, including duplicates.

`COUNT(DISTINCT column)` counts unique non-NULL values.

Example:

```sql
SELECT COUNT(city), COUNT(DISTINCT city)
FROM employees;
```

Output:

| COUNT(city) | COUNT(DISTINCT city) |
| --- | --- |
| 5 | 4 |

## Q4. What is the difference between SUM() and AVG()?

- `SUM()` calculates a total.
- `AVG()` calculates the arithmetic mean of non-NULL values.

```sql
SELECT SUM(salary), AVG(salary)
FROM employees;
```

Output:

| SUM(salary) | AVG(salary) |
| --- | --- |
| 310000.00 | 62000.000000 |

## Q5. What is the difference between MIN() and MAX()?

- `MIN()` returns the smallest value.
- `MAX()` returns the largest value.

```sql
SELECT MIN(salary), MAX(salary)
FROM employees;
```

Output:

| MIN(salary) | MAX(salary) |
| --- | --- |
| 55000.00 | 72000.00 |

## Q6. What is GROUP BY?

`GROUP BY` groups rows with matching values and allows aggregate functions to calculate a result for each group.

```sql
SELECT department, COUNT(*)
FROM employees
GROUP BY department;
```

## Q7. What is the difference between WHERE and HAVING?

`WHERE` filters individual rows before grouping. `HAVING` filters groups after aggregation.

```sql
SELECT department, AVG(salary)
FROM employees
WHERE salary > 50000
GROUP BY department
HAVING AVG(salary) > 60000;
```

## Q8. Can we use aggregate functions without GROUP BY?

Yes.

```sql
SELECT SUM(salary)
FROM employees;
```

This calculates the total salary across all selected rows.

## Q9. Can we use aggregate functions in WHERE?

In ordinary aggregate queries, you cannot directly use an aggregate result in `WHERE`.

Incorrect:

```sql
SELECT department, AVG(salary)
FROM employees
WHERE AVG(salary) > 60000
GROUP BY department;
```

Correct:

```sql
SELECT department, AVG(salary)
FROM employees
GROUP BY department
HAVING AVG(salary) > 60000;
```

A subquery is another option when the aggregate must be computed separately.

## Q10. Do aggregate functions ignore NULL values?

Most standard aggregate functions ignore NULL values in the expression being aggregated.

`COUNT(*)` is different because it counts rows regardless of NULL values in individual columns.

`COUNT(column)` counts only non-NULL values.

## Q11. What happens when SUM() receives only NULL values?

`SUM()` returns NULL if there are no non-NULL values to sum.

Use `COALESCE()` if the application requires zero instead.

```sql
SELECT COALESCE(SUM(salary), 0)
FROM employees;
```

## Q12. Can we use multiple aggregate functions in one query?

Yes.

```sql
SELECT
    COUNT(*) AS employee_count,
    SUM(salary) AS total_salary,
    AVG(salary) AS average_salary,
    MIN(salary) AS minimum_salary,
    MAX(salary) AS maximum_salary
FROM employees;
```

## Q13. How do you find the second-highest salary?

One approach is to find the maximum salary below the overall maximum.

```sql
SELECT MAX(salary) AS second_highest_salary
FROM employees
WHERE salary < (
    SELECT MAX(salary)
    FROM employees
);
```

Output:

| second\_highest\_salary |
| --- |
| 65000.00 |

This finds the second-highest **distinct salary** when the data contains at least two distinct salary values.

## Q14. How do you find duplicate values?

For duplicate departments:

```sql
SELECT
    department,
    COUNT(*) AS occurrences
FROM employees
GROUP BY department
HAVING COUNT(*) > 1;
```

Output:

| department | occurrences |
| --- | --- |
| HR | 2 |
| IT | 2 |

## Q15. What is conditional aggregation?

Conditional aggregation calculates different summaries based on conditions, often using `CASE` inside an aggregate function.

```sql
SELECT
    SUM(CASE WHEN department = 'IT' THEN 1 ELSE 0 END)
        AS it_count,
    SUM(CASE WHEN department = 'HR' THEN 1 ELSE 0 END)
        AS hr_count
FROM employees;
```

## Q16. What is the difference between STDDEV\_POP() and STDDEV\_SAMP()?

- `STDDEV_POP()` calculates population standard deviation.
- `STDDEV_SAMP()` calculates sample standard deviation.

The sample version uses a different denominator because it estimates population variability from a sample.

## Q17. What is the difference between VAR\_POP() and VAR\_SAMP()?

- `VAR_POP()` calculates population variance.
- `VAR_SAMP()` calculates sample variance.

Both measure variability using squared deviations from the mean.

## Q18. Can aggregate functions be nested?

Directly nesting ordinary aggregate functions at the same query level is generally not allowed.

Problematic:

```sql
SELECT AVG(SUM(salary))
FROM employees;
```

Instead, calculate the inner aggregate in a subquery and aggregate its results in the outer query.

## Q19. What is GROUP\_CONCAT()?

In MySQL, `GROUP_CONCAT()` combines multiple row values into one string, optionally supporting distinct values, ordering, and a separator.

## Q20. What is the difference between GROUP BY and ORDER BY?

- `GROUP BY` groups rows for aggregation.
- `ORDER BY` sorts the resulting rows.

They serve different purposes and are often used together.

---

# 9\. COMMON MISTAKES TO AVOID

## Mistake 1: Confusing COUNT(\*) and COUNT(column)

Do not assume these expressions always return the same result.

```sql
SELECT COUNT(*), COUNT(salary)
FROM employees;
```

They differ whenever some rows have NULL salaries.

## Mistake 2: Using WHERE to Filter an Aggregate

Incorrect:

```sql
SELECT department, SUM(salary)
FROM employees
WHERE SUM(salary) > 100000
GROUP BY department;
```

Correct:

```sql
SELECT department, SUM(salary)
FROM employees
GROUP BY department
HAVING SUM(salary) > 100000;
```

## Mistake 3: Selecting Ungrouped Columns

Problematic:

```sql
SELECT department, first_name, SUM(salary)
FROM employees
GROUP BY department;
```

There is no defined single employee name for a department with multiple employees.

Group by the necessary columns or use an appropriate subquery or window function.

## Mistake 4: Assuming NULL Means Zero

`NULL` represents a missing or unknown value, not necessarily zero.

```sql
SELECT COALESCE(SUM(salary), 0)
FROM employees
WHERE department = 'Marketing';
```

This returns zero if no non-NULL salary values are available for the selected group.

## Mistake 5: Forgetting That AVG() Ignores NULL

The average is calculated using non-NULL values, not automatically all rows.

## Mistake 6: Expecting MIN() or MAX() to Return a Complete Row

```sql
SELECT MAX(salary)
FROM employees;
```

This returns the maximum salary only.

To retrieve the employee details, use a suitable subquery or ranking query.

## Mistake 7: Using DISTINCT Unnecessarily

```sql
SELECT SUM(DISTINCT salary)
FROM employees;
```

This does not calculate the normal total salary if two employees earn the same amount. It adds each distinct salary once.

## Mistake 8: Forgetting ORDER BY

The order of groups is not guaranteed without an explicit ordering requirement.

```sql
SELECT department, COUNT(*)
FROM employees
GROUP BY department
ORDER BY department;
```

## Mistake 9: Using GROUP\_CONCAT() for Unlimited Data

MySQL limits the output length of `GROUP_CONCAT()`. A long result may be truncated, depending on configuration.

## Mistake 10: Mixing Up Population and Sample Statistics

Use population statistics when the data represents the entire population being studied. Use sample statistics when estimating variability from a sample.

---

# 10\. QUICK REVISION CHEAT SHEET

| Function | Purpose | Example | Result |
| --- | --- | --- | --- |
| `COUNT(*)` | Count all rows | `COUNT(*)` | 5 |
| `COUNT(column)` | Count non-NULL values | `COUNT(email)` | 5 |
| `COUNT(DISTINCT column)` | Count unique non-NULL values | `COUNT(DISTINCT city)` | 4 |
| `SUM()` | Total numeric values | `SUM(salary)` | 310000 |
| `AVG()` | Average numeric values | `AVG(salary)` | 62000 |
| `MIN()` | Minimum value | `MIN(salary)` | 55000 |
| `MAX()` | Maximum value | `MAX(salary)` | 72000 |
| `GROUP_CONCAT()` | Combine group values into a string | `GROUP_CONCAT(first_name)` | Names in one string |
| `STDDEV_POP()` | Population standard deviation | `STDDEV_POP(salary)` | Statistical result |
| `STDDEV_SAMP()` | Sample standard deviation | `STDDEV_SAMP(salary)` | Statistical result |
| `VAR_POP()` | Population variance | `VAR_POP(salary)` | Statistical result |
| `VAR_SAMP()` | Sample variance | `VAR_SAMP(salary)` | Statistical result |
| `BIT_AND()` | Bitwise AND aggregation | `BIT_AND(value)` | Bitwise result |
| `BIT_OR()` | Bitwise OR aggregation | `BIT_OR(value)` | Bitwise result |
| `BIT_XOR()` | Bitwise XOR aggregation | `BIT_XOR(value)` | Bitwise result |
| `JSON_ARRAYAGG()` | Aggregate values into a JSON array | `JSON_ARRAYAGG(first_name)` | JSON array |
| `JSON_OBJECTAGG()` | Aggregate key-value pairs into a JSON object | `JSON_OBJECTAGG(first_name, salary)` | JSON object |

## Essential Clauses Used with Aggregates

| Clause | Purpose | Example |
| --- | --- | --- |
| `WHERE` | Filter rows | `WHERE salary > 60000` |
| `GROUP BY` | Create groups | `GROUP BY department` |
| `HAVING` | Filter groups | `HAVING COUNT(*) > 1` |
| `ORDER BY` | Sort results | `ORDER BY SUM(salary) DESC` |
| `LIMIT` | Restrict result rows | `LIMIT 3` |
| `DISTINCT` | Remove duplicates | `COUNT(DISTINCT city)` |

## Standard Aggregate Query Pattern

```sql
SELECT
    grouping_column,
    COUNT(*) AS total_rows,
    SUM(numeric_column) AS total_value,
    AVG(numeric_column) AS average_value,
    MIN(numeric_column) AS minimum_value,
    MAX(numeric_column) AS maximum_value
FROM table_name
WHERE row_condition
GROUP BY grouping_column
HAVING aggregate_condition
ORDER BY total_value DESC
LIMIT 10;
```

This pattern is useful for many business reports.

---

# 11\. FINAL LEARNING ROADMAP

For interviews and practical SQL work, learn aggregate functions in the following order.

### Level 1: Basic Aggregates

Master these first:

1. `COUNT()`
2. `SUM()`
3. `AVG()`
4. `MIN()`
5. `MAX()`

### Level 2: Grouping and Filtering

Practice combining aggregates with:

1. `GROUP BY`
2. `HAVING`
3. `WHERE`
4. `ORDER BY`
5. `DISTINCT`

### Level 3: Practical Reporting

Learn how to solve:

1. Department-wise salary reports.
2. City-wise customer counts.
3. Highest and lowest salary problems.
4. Duplicate-value detection.
5. Conditional counts and sums.
6. Percentage calculations.
7. Second-highest and top-N value problems.

### Level 4: Advanced Aggregates

Explore:

1. `GROUP_CONCAT()`
2. `STDDEV_POP()` and `STDDEV_SAMP()`
3. `VAR_POP()` and `VAR_SAMP()`
4. `BIT_AND()`, `BIT_OR()`, and `BIT_XOR()`
5. `JSON_ARRAYAGG()` and `JSON_OBJECTAGG()`

## The 10 Most Important Concepts for SQL Interviews

- `COUNT(*)` vs `COUNT(column)`
- `COUNT(DISTINCT column)`
- `SUM()` and `AVG()`
- `MIN()` and `MAX()`
- `GROUP BY`
- `WHERE` vs `HAVING`
- NULL handling in aggregates
- Conditional aggregation with `CASE`
- Subqueries involving aggregate functions
- Finding the highest, lowest, and second-highest values

## Final Best Practice

Do not memorize aggregate functions alone. Practice combining them with `SELECT`, `WHERE`, `GROUP BY`, `HAVING`, `ORDER BY`, `CASE`, and subqueries.

Always verify NULL handling, grouping rules, and the exact SQL dialect before applying a query in a production database.

**End of SQL Aggregate Functions Notes.**
