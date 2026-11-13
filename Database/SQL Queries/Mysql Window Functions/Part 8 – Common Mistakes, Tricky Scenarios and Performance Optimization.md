# SQL Window Functions – Complete Beginner to Advanced Guide

# Part 8 – Common Mistakes, Tricky Scenarios and Performance Optimization

# Chapter 79: Common Mistakes When Using Window Functions

## Mistake 1: Confusing GROUP BY with PARTITION BY

### Incorrect understanding

Developers sometimes assume PARTITION BY behaves exactly like GROUP BY.

These clauses have different purposes.

### GROUP BY

```sql
SELECT
    department,
    AVG(salary) AS average_salary
FROM employees
GROUP BY department;
```

Result:

| department | average_salary |
|---|---:|
| IT | 73333.33 |
| HR | 56666.67 |
| Sales | 60000 |

Individual employee records are no longer present.

### PARTITION BY

```sql
SELECT
    employee_name,
    department,
    salary,

    AVG(salary) OVER (
        PARTITION BY department
    ) AS average_salary

FROM employees;
```

Every employee remains in the result.

### Correct understanding

- GROUP BY produces grouped results.
- PARTITION BY creates analytical partitions without collapsing individual rows.

**Interview trap:** If the question requires individual rows and group-level calculations together, consider a window function.

---

## Mistake 2: Assuming ORDER BY Inside OVER() Sorts Final Output

### Incorrect assumption

```sql
SELECT
    employee_name,
    salary,

    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS rn

FROM employees;
```

Some developers assume the result will always be displayed in salary order.

This is not guaranteed.

### Correct query

```sql
SELECT
    employee_name,
    salary,

    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS rn

FROM employees

ORDER BY salary DESC;
```

### Explanation

The two ORDER BY clauses have different purposes.

| Clause | Responsibility |
|---|---|
| ORDER BY inside OVER() | Defines the analytical sequence |
| Final ORDER BY | Defines displayed output sequence |

Always specify the final ORDER BY when presentation order matters.

---

## Mistake 3: Ignoring Default Window Frames

### Incorrect assumption

```sql
SELECT
    sale_date,
    amount,

    SUM(amount) OVER (
        ORDER BY sale_date
    ) AS total_sales

FROM sales;
```

Developers sometimes assume that every row is added individually in a deterministic row-by-row manner.

However, MySQL's default ordered aggregate frame uses RANGE semantics.

If multiple rows share the same ordering value, they can receive the same cumulative result.

### Example

| id | sale_date | amount |
|---:|---|---:|
| 1 | Jan 01 | 100 |
| 2 | Jan 02 | 200 |
| 3 | Jan 02 | 300 |
| 4 | Jan 03 | 400 |

The default RANGE calculation gives:

| id | amount | cumulative_total |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 600 |
| 3 | 300 | 600 |
| 4 | 400 | 1000 |

### Correct query for row-by-row accumulation

```sql
SELECT
    id,
    sale_date,
    amount,

    SUM(amount) OVER (
        ORDER BY sale_date, id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS cumulative_total

FROM sales;
```

Result:

| id | amount | cumulative_total |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 300 |
| 3 | 300 | 600 |
| 4 | 400 | 1000 |

**Rule:** Specify ROWS explicitly when you need physical row-by-row cumulative calculations.

---

## Mistake 4: Using ROWS and RANGE Interchangeably

### Incorrect assumption

ROWS and RANGE are equivalent.

They are not.

### Example

```sql
SUM(amount) OVER (
    ORDER BY amount
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND CURRENT ROW
)
```

Counts physical rows.

```sql
SUM(amount) OVER (
    ORDER BY amount
    RANGE BETWEEN UNBOUNDED PRECEDING
    AND CURRENT ROW
)
```

Includes peers sharing the current ordering value.

### Correct approach

Understand the business requirement.

- Previous three transactions: ROWS.
- All records up to the current ordering value, including peers: RANGE.
- Previous peer groups: GROUPS, where supported.

---

## Mistake 5: Incorrect Use of LAST_VALUE()

### Incorrect query

```sql
SELECT
    employee_name,
    salary,

    LAST_VALUE(salary) OVER (
        ORDER BY employee_id
    ) AS final_salary

FROM employees;
```

Developers may expect the final salary of the entire employee sequence.

But the default ordered frame ends at the current row's peer group.

### Correct query

```sql
SELECT
    employee_name,
    salary,

    LAST_VALUE(salary) OVER (
        ORDER BY employee_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS final_salary

FROM employees;
```

### Rule

Always inspect the current frame before interpreting LAST_VALUE().

---

## Mistake 6: Incorrect Ranking When Ties Exist

### Problem

Find the top three salary levels.

### Incorrect query

```sql
ROW_NUMBER() OVER (
    ORDER BY salary DESC
)
```

Filtering for the first three rows returns three physical records, not necessarily the top three distinct salary levels.

### Correct query

```sql
DENSE_RANK() OVER (
    ORDER BY salary DESC
)
```

Filtering for rank 3 includes the third distinct salary level.

### Comparison

| Requirement | Function |
|---|---|
| Exactly three records | ROW_NUMBER() |
| Top three positions, including ties | RANK() |
| Top three distinct values | DENSE_RANK() |

---

## Mistake 7: Adding a Unique Tie-Breaker to RANK() Unnecessarily

### Incorrect for preserving salary ties

```sql
RANK() OVER (
    ORDER BY salary DESC, employee_id
)
```

If employee_id is unique, two employees with identical salaries no longer have identical ordering combinations.

They will not tie.

### Correct

```sql
RANK() OVER (
    ORDER BY salary DESC
)
```

Use a unique tie-breaker for ROW_NUMBER() when deterministic selection is required.

Do not add it to a ranking expression when the business definition of a tie is salary equality.

---

## Mistake 8: Filtering a Window Function in WHERE

### Incorrect

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

### Why incorrect?

WHERE is logically evaluated before window-function calculation.

### Correct

```sql
WITH ranked_employees AS (
    SELECT
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

## Mistake 9: Incorrect PARTITION BY Definition

Suppose the requirement is to rank employees by salary within departments.

### Incorrect

```sql
RANK() OVER (
    PARTITION BY employee_id
    ORDER BY salary DESC
)
```

If employee_id is unique, each employee becomes a separate partition.

Every employee receives rank 1.

### Correct

```sql
RANK() OVER (
    PARTITION BY department
    ORDER BY salary DESC
)
```

### Lesson

Always ask:

"What defines the group within which this calculation must be independent?"

That answer determines PARTITION BY.

---

## Mistake 10: Ignoring NULL Values

### Incorrect assumption

```sql
LAG(amount, 1, 0) OVER (
    ORDER BY transaction_id
)
```

Developers may assume the default 0 replaces all NULL results.

It only replaces the result when the requested preceding row does not exist.

If that preceding row exists but contains NULL, LAG() returns NULL.

### Correct when all NULL results should become zero

```sql
COALESCE(
    LAG(amount) OVER (
        ORDER BY transaction_id
    ),
    0
)
```

However, this deliberately treats missing rows and actual NULL values identically.

---

## Mistake 11: Non-Deterministic ROW_NUMBER()

### Incorrect

```sql
ROW_NUMBER() OVER (
    ORDER BY transaction_date
)
```

If multiple transactions share the same date, their order is not fully defined.

### Correct

```sql
ROW_NUMBER() OVER (
    ORDER BY transaction_date, transaction_id
)
```

Use a unique tie-breaker whenever the business requirement needs deterministic row selection.

---

## Mistake 12: Confusing LAG() with Previous Calendar Period

### Incorrect assumption

```sql
LAG(revenue, 1) OVER (
    ORDER BY month_start
)
```

Always retrieves the previous calendar month.

It retrieves the previous row.

If March is missing, April may receive February's revenue as its previous row.

### Correct approach

For calendar comparisons, use a complete calendar series or join using the exact previous-month key.

---

## Mistake 13: Forgetting JOIN Multiplication

### Incorrect query design

Suppose one employee has three skills.

Joining employees to employee_skills creates three rows for that employee.

Applying ROW_NUMBER() after this join numbers the multiplied records.

### Correct approach

Perform the analytical calculation at the intended data grain.

For example:

```sql
WITH employee_ranking AS (
    SELECT
        employee_id,
        department_id,
        salary,

        RANK() OVER (
            PARTITION BY department_id
            ORDER BY salary DESC
        ) AS salary_rank

    FROM employees
)

SELECT
    e.employee_id,
    e.department_id,
    s.skill_name,
    e.salary_rank

FROM employee_ranking e

LEFT JOIN employee_skills s
    ON e.employee_id = s.employee_id;
```

Rank employees before joining the one-to-many skills table.

---

## Mistake 14: Assuming COUNT(DISTINCT) OVER() Works Everywhere

### Problem

```sql
COUNT(DISTINCT customer_id) OVER ()
```

This is not supported in MySQL 8.0.

### Correct workaround

Use ROW_NUMBER() to identify the first occurrence of each customer, then use SUM() over the first-occurrence flags.

This technique was demonstrated in Chapter 67.

---

## Mistake 15: Using an Incorrect Frame for Moving Averages

Suppose the requirement is the current and previous two transactions.

### Incorrect

```sql
AVG(amount) OVER (
    ORDER BY transaction_date
    ROWS BETWEEN 3 PRECEDING AND CURRENT ROW
)
```

This can include four rows: three preceding plus the current row.

### Correct

```sql
AVG(amount) OVER (
    ORDER BY transaction_date
    ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
)
```

### Memory trick

If the current row is included:

```text
Required frame size = N

PRECEDING count = N - 1
```

For five rows:

```sql
ROWS BETWEEN 4 PRECEDING AND CURRENT ROW
```

---

# Chapter 80: Performance Optimization of Window Functions

Window functions are powerful, but they can be expensive when used on large datasets.

To optimize them, we need to understand how database engines execute analytical operations.

## 80.1 How Does a Database Process a Window Function Internally?

Consider:

```sql
SELECT
    employee_id,
    department,
    salary,

    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_rank

FROM employees;
```

A simplified conceptual execution process is:

<CodeBlock language="text" editable>
1. Read rows from the source table
           |
           v
2. Apply JOIN and WHERE conditions
           |
           v
3. Form the required input row set
           |
           v
4. Organize rows by partition and ordering
           |
           v
5. Evaluate the window function
           |
           v
6. Produce the calculated result
           |
           v
7. Apply final ORDER BY and LIMIT
</CodeBlock>

The database may use indexes, sorting algorithms, temporary structures and internal window-processing operators.

The exact implementation varies between engines.

---

## 80.2 Sorting Overhead

Sorting is one of the major costs associated with window functions.

Consider:

```sql
RANK() OVER (
    PARTITION BY department
    ORDER BY salary DESC
)
```

The engine needs rows organized by:

```text
department
    ↓
salary DESC
```

If an appropriate ordering is not already available, the engine may perform a sort.

For a large dataset, sorting can require substantial CPU, memory and temporary storage.

### Optimization

Consider a composite index:

```sql
CREATE INDEX idx_employee_department_salary
ON employees(department, salary);
```

This index can potentially help queries filtering or accessing employees in department-and-salary order.

However, do not assume that the index eliminates every sort.

The optimizer must determine that the index ordering is usable for the particular query and execution plan.

For descending ordering, MySQL's index behavior and the optimizer's chosen access path should be verified using EXPLAIN.

### Important consideration

A composite index on:

```sql
(department, salary)
```

may help with:

```sql
PARTITION BY department
ORDER BY salary
```

But it is not guaranteed to satisfy:

```sql
PARTITION BY department
ORDER BY salary DESC, employee_id
```

without additional sorting.

---

## 80.3 Partitioning Overhead

Consider:

```sql
SUM(amount) OVER (
    PARTITION BY customer_id
)
```

The engine must calculate separate aggregates for different customer partitions.

If there are millions of customers, the engine must manage a large number of partitions.

If one partition contains an extremely large number of records, it can also create memory and processing challenges.

### Optimization

1. Filter irrelevant records before window evaluation.
2. Avoid unnecessary partition columns.
3. Select only required columns.
4. Ensure partition keys have appropriate data types and statistics.
5. Avoid unnecessarily large historical ranges.
6. Consider pre-aggregation when detailed rows are not required.

### Example

Less efficient logical input:

```sql
SELECT
    customer_id,
    transaction_id,
    transaction_date,
    amount,

    SUM(amount) OVER (
        PARTITION BY customer_id
    ) AS customer_total

FROM transactions;
```

If we only need customers from 2026, filter earlier:

```sql
SELECT
    customer_id,
    transaction_id,
    transaction_date,
    amount,

    SUM(amount) OVER (
        PARTITION BY customer_id
    ) AS customer_total

FROM transactions

WHERE transaction_date >= '2026-01-01'
  AND transaction_date < '2027-01-01';
```

**Important:** This changes the meaning of the calculation to a 2026-only total. If you need a full historical customer total while displaying only 2026 records, calculate the window before applying the date filter, using an outer query or CTE.

---

## 80.4 Indexing Strategies

Indexes can improve access patterns, filtering and sometimes ordering.

They do not automatically make every window function faster.

### Example 1: Department-wise salary ranking

```sql
CREATE INDEX idx_department_salary
ON employees(department, salary);
```

Potentially useful for:

```sql
SELECT
    employee_id,
    department,
    salary,

    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_rank

FROM employees;
```

### Example 2: Customer transaction history

```sql
CREATE INDEX idx_customer_transaction_date
ON transactions(customer_id, transaction_date);
```

Potentially useful for:

```sql
LAG(amount) OVER (
    PARTITION BY customer_id
    ORDER BY transaction_date
)
```

### Example 3: Latest transaction per customer

```sql
CREATE INDEX idx_customer_latest
ON transactions(
    customer_id,
    transaction_date,
    transaction_id
);
```

This index can help with customer-based ordering and access.

### Index design principles

| Requirement | Possible index structure |
|---|---|
| Department-wise ranking | (department, salary) |
| Customer transaction sequence | (customer_id, transaction_date, transaction_id) |
| Employee history | (employee_id, effective_date) |
| Order history | (customer_id, order_date, order_id) |
| Account balance | (account_id, transaction_date, transaction_id) |

The exact index should reflect filtering, ordering, selectivity, data distribution and the actual execution plan.

---

## 80.5 Memory Usage and Temporary Tables

Window calculations may require intermediate processing structures.

For example:

```sql
SUM(amount) OVER (
    PARTITION BY customer_id
    ORDER BY transaction_date
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND CURRENT ROW
)
```

The engine may need to:

- Organize rows.
- Maintain partition information.
- Access preceding or following rows.
- Calculate frame aggregates.
- Store intermediate values.

When the working set exceeds available memory, some operations may use temporary disk storage.

This can significantly increase execution time.

### Optimization

- Reduce the input row count.
- Avoid unnecessary columns.
- Avoid redundant window specifications.
- Use selective filters.
- Process data at an appropriate level of aggregation.
- Monitor temporary-table and sort behavior.
- Avoid extremely wide intermediate rows.

---

## 80.6 Large Partitions

Consider:

```sql
SUM(amount) OVER (
    PARTITION BY customer_id
)
```

Suppose one customer has 50 million transactions.

That customer's partition is extremely large.

Large partitions may require more processing resources, especially for functions that need access to a wide frame or future rows.

### Possible solutions

1. Restrict the analysis period when business rules permit.
2. Pre-aggregate older transactions.
3. Maintain periodic balance snapshots.
4. Use partitioned historical storage where supported.
5. Split processing into suitable independent batches when logically valid.

**Important:** Do not arbitrarily split a running total if the cumulative calculation must remain continuous. You must carry forward the previous batch's ending total.

---

## 80.7 Multiple Window Functions and Sorting Reuse

Consider:

```sql
SELECT
    employee_id,
    department,
    salary,

    ROW_NUMBER() OVER (
        PARTITION BY department
        ORDER BY salary DESC, employee_id
    ) AS rn,

    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_rank,

    DENSE_RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS dense_rank

FROM employees;
```

These functions use compatible ordering requirements, although the tie-breaker means the first window has a different ordering definition from the other two.

A database may reuse some ordering work where specifications are compatible.

However, different partitioning or ordering definitions may require additional sorting.

### Optimization strategy

Group calculations that use the same window specification.

Example:

```sql
SELECT
    employee_id,
    department,
    salary,

    SUM(salary) OVER department_window AS total_salary,

    AVG(salary) OVER department_window AS average_salary,

    MIN(salary) OVER department_window AS minimum_salary,

    MAX(salary) OVER department_window AS maximum_salary

FROM employees

WINDOW department_window AS (
    PARTITION BY department
);
```

This improves maintainability and may help the optimizer recognize compatible specifications.

It does not guarantee a particular physical execution plan.

---

## 80.8 CTE Materialization Considerations

CTEs improve query readability, but they can affect performance.

Consider:

```sql
WITH employee_analysis AS (
    SELECT
        employee_id,
        department,
        salary,

        ROW_NUMBER() OVER (
            PARTITION BY department
            ORDER BY salary DESC
        ) AS rn

    FROM employees
)

SELECT *
FROM employee_analysis
WHERE rn = 1;
```

The optimizer may merge or materialize CTEs depending on the database and query structure.

In MySQL, window-function-containing query blocks have merging restrictions, so an intermediate result may be materialized.

Materialization can require temporary storage.

### Optimization

1. Avoid unnecessary CTE layers.
2. Avoid selecting unused columns.
3. Filter the input as early as logically possible.
4. Inspect EXPLAIN and EXPLAIN ANALYZE.
5. Compare equivalent query formulations where useful.

### Important

Do not remove a CTE simply because you believe it is slower.

A clear CTE may be more maintainable and can have comparable performance.

Always measure.

---

## 80.9 Window Functions vs Joins and Correlated Subqueries

Window functions are not always faster than alternative SQL approaches.

### Example: Department Average

Window function:

```sql
SELECT
    employee_id,
    department,
    salary,

    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_average

FROM employees;
```

Alternative using a grouped derived table:

```sql
SELECT
    e.employee_id,
    e.department,
    e.salary,
    d.department_average

FROM employees e

JOIN (
    SELECT
        department,
        AVG(salary) AS department_average

    FROM employees

    GROUP BY department
) d

ON e.department = d.department;
```

Both produce department-level averages alongside employee records, subject to NULL join semantics.

The window version is often more concise.

However, actual performance depends on indexes, table size, optimizer choices and execution plans.

### When window functions are preferable

- Running totals.
- Previous and next row analysis.
- Ranking.
- Moving averages.
- Cumulative metrics.
- Group statistics alongside detailed rows.

### When other techniques may be preferable

- Simple grouped reports.
- Recursive hierarchical traversal.
- Existence checks.
- Specialized anti-joins.
- Cases where a grouped result is the only required output.
- Operations better supported by a database-specific aggregate or index access pattern.

---

# Chapter 81: Understanding Execution Plans

Execution plans reveal how the database intends to execute a query.

They are essential for performance engineering.

## 81.1 EXPLAIN in MySQL

Example:

```sql
EXPLAIN
SELECT
    employee_id,
    department,
    salary,

    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_rank

FROM employees;
```

EXPLAIN provides information such as:

- Access method.
- Possible indexes.
- Selected index.
- Estimated row count.
- Filtering information.
- Temporary-table or sorting-related operations where exposed by the plan.

## 81.2 EXPLAIN ANALYZE

MySQL 8.0.18+ supports EXPLAIN ANALYZE for supported statements.

```sql
EXPLAIN ANALYZE
SELECT
    employee_id,
    department,
    salary,

    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS salary_rank

FROM employees;
```

It executes the query and reports actual execution information, including timing and row counts for plan operators.

Use it carefully on large production workloads because the query actually runs.

## 81.3 What Should You Look For?

| Plan characteristic | Why it matters |
|---|---|
| Full table scan | May be appropriate or expensive depending on data size and filtering |
| Index range scan | Can reduce rows read |
| Sort operation | May be expensive for large datasets |
| Temporary table | May indicate intermediate storage |
| Actual row count | Helps validate estimates |
| Execution time | Measures real cost |
| Repeated scans | May indicate unnecessary work |

### Performance investigation process

1. Run EXPLAIN.
2. Identify the access path.
3. Check estimated row counts.
4. Inspect sort and temporary-table operations.
5. Run EXPLAIN ANALYZE where safe.
6. Compare estimated and actual rows.
7. Test candidate indexes.
8. Measure again.

Do not optimize based only on query appearance.

---

# Chapter 82: Advanced Performance Checklist

Before deploying a complex analytical query, ask:

| Question | Why it matters |
|---|---|
| Can unnecessary input rows be filtered first? | Reduces processing volume |
| Is PARTITION BY correct? | Avoids incorrect grouping |
| Is ORDER BY deterministic? | Ensures reliable results |
| Is a unique tie-breaker required? | Prevents unstable row selection |
| Is the frame explicit? | Avoids accidental default behavior |
| Are multiple windows compatible? | May reduce repeated sorting |
| Are indexes appropriate? | Can improve access and ordering |
| Are partitions extremely large? | May create memory pressure |
| Are there unnecessary columns? | Reduces intermediate row width |
| Does the execution plan show expensive sorting? | Identifies optimization opportunities |
| Is the CTE materialized? | May affect temporary storage |
| Have actual execution times been measured? | Confirms whether optimization worked |

