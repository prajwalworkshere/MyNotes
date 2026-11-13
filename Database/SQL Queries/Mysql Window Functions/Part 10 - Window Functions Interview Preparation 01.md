# SQL Window Functions – Complete Interview Preparation Handbook

# Chapter 92: Beginner Interview Questions and Answers

## Q1. What are SQL window functions?

**Answer:**

Window functions perform calculations across a set of related rows called a window while preserving individual rows in the result.

Unlike GROUP BY, they do not normally collapse multiple records into one summary record.

Example:

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

Every employee remains in the output, along with the average salary of their department.

**Underlying theory:** Window functions allow individual-level and group-level analysis in the same result.

**Common incorrect answer:** Window functions are simply aggregate functions.

They also include ranking, navigation, value and distribution functions.

**Follow-up:** Can a window function be used without an aggregate function?

Yes. ROW_NUMBER(), RANK(), LAG() and other functions are window functions without being aggregate functions.

---

## Q2. What is the purpose of the OVER() clause?

**Answer:**

OVER() defines the window over which a window function performs its calculation.

It can contain:

- PARTITION BY
- ORDER BY
- A window frame

Example:

```sql
SUM(salary) OVER (
    PARTITION BY department
)
```

The function calculates department-wise salary totals without removing employee records.

**Common incorrect answer:** OVER() is used only for ranking.

It is also essential for aggregate, navigation, value and distribution window functions.

**Follow-up:** What happens if OVER() is empty?

All rows belong to one partition, and there is no specified ordering or explicit frame.

---

## Q3. What is PARTITION BY?

**Answer:**

PARTITION BY divides the input rows into independent logical groups.

The window function operates separately within each group.

Example:

```sql
RANK() OVER (
    PARTITION BY department
    ORDER BY salary DESC
)
```

Each department receives its own salary ranking.

**Common incorrect answer:** PARTITION BY reduces the number of rows.

It does not.

**Follow-up:** Can we use multiple columns in PARTITION BY?

Yes.

```sql
PARTITION BY department, job_role
```

Each unique combination creates an independent partition.

---

## Q4. What is the difference between PARTITION BY and GROUP BY?

**Answer:**

GROUP BY groups rows and generally produces one result row per group.

PARTITION BY defines analytical groups while preserving individual records.

Example:

```sql
SELECT
    department,
    AVG(salary)
FROM employees
GROUP BY department;
```

Returns one row per department.

```sql
SELECT
    employee_name,
    department,
    AVG(salary) OVER (
        PARTITION BY department
    )
FROM employees;
```

Returns every employee.

**Follow-up:** Can GROUP BY and window functions be used together?

Yes. Window functions can calculate over the grouped result.

---

## Q5. What is the difference between ORDER BY inside OVER() and final ORDER BY?

**Answer:**

ORDER BY inside OVER() determines the logical order used for analytical calculations.

The final ORDER BY determines the order in which the result is displayed.

Example:

```sql
SELECT
    employee_name,
    salary,

    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS rn

FROM employees

ORDER BY employee_name;
```

The row numbers are assigned by salary, but the final output is displayed alphabetically.

**Common incorrect answer:** ORDER BY inside OVER() automatically sorts the final result.

**Follow-up:** Can both ORDER BY clauses be different?

Yes.

---

## Q6. What are the major categories of window functions?

**Answer:**

The major categories are:

| Category | Functions |
|---|---|
| Ranking | ROW_NUMBER(), RANK(), DENSE_RANK(), NTILE() |
| Aggregate | SUM(), AVG(), COUNT(), MIN(), MAX() |
| Navigation | LAG(), LEAD() |
| Value | FIRST_VALUE(), LAST_VALUE(), NTH_VALUE() |
| Distribution | PERCENT_RANK(), CUME_DIST() |

These categories cover the principal functions studied in this guide.

---

## Q7. Does a window function always require PARTITION BY?

**Answer:**

No.

PARTITION BY is optional.

If omitted, all rows in the input window belong to one partition.

Example:

```sql
SELECT
    employee_name,
    salary,

    AVG(salary) OVER () AS overall_average

FROM employees;
```

This calculates the overall average salary.

**Follow-up:** What is the difference between OVER() and OVER(PARTITION BY department)?

The first calculates across the entire result window; the second calculates independently within each department.

---

## Q8. Does every window function require ORDER BY?

**Answer:**

No.

Aggregate window functions such as SUM(), AVG() and COUNT() can operate without ORDER BY.

Example:

```sql
COUNT(*) OVER (
    PARTITION BY department
)
```

However, meaningful ranking and positional navigation generally require an ordering.

**Follow-up:** What happens if ROW_NUMBER() is used without ORDER BY?

In MySQL, it is permitted, but the row numbering is nondeterministic because no ordering is defined.

---

## Q9. What is ROW_NUMBER()?

**Answer:**

ROW_NUMBER() assigns a unique sequential integer to every row in a partition.

Example:

```sql
SELECT
    employee_name,
    salary,

    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS rn

FROM employees;
```

Output numbers are 1, 2, 3, 4 and so on.

Even employees with identical salaries receive different row numbers.

**Follow-up:** What is its most common practical use?

Deduplication and selecting the latest record per entity.

---

## Q10. What is the difference between RANK() and DENSE_RANK()?

**Answer:**

Both assign the same rank to tied ordering values.

The difference is how they handle the next rank.

Example:

| Salary | RANK | DENSE_RANK |
|---:|---:|---:|
| 90000 | 1 | 1 |
| 80000 | 2 | 2 |
| 80000 | 2 | 2 |
| 70000 | 4 | 3 |

RANK() skips positions after ties.

DENSE_RANK() does not skip positions.

**Follow-up:** Which function is appropriate for finding the third-highest distinct salary?

DENSE_RANK().

---

## Q11. What is NTILE()?

**Answer:**

NTILE(n) divides ordered rows into approximately equal-sized buckets.

Example:

```sql
NTILE(4) OVER (
    ORDER BY salary DESC
)
```

Divides employees into four buckets.

It is commonly used for quartile analysis and customer segmentation.

**Common incorrect answer:** NTILE(4) creates four equal salary ranges.

It creates approximately equal numbers of rows, not equal value ranges.

---

## Q12. What is a window frame?

**Answer:**

A window frame is the subset of rows within a partition that a frame-sensitive window calculation uses for the current row.

Example:

```sql
SUM(amount) OVER (
    ORDER BY transaction_date
    ROWS BETWEEN 2 PRECEDING
    AND CURRENT ROW
)
```

Includes the current row and two preceding rows.

**Follow-up:** What is the difference between a partition and a frame?

A partition is the larger logical group; a frame is the subset used for the current row.

---

## Q13. What is a running total?

**Answer:**

A running total is a cumulative sum calculated from the beginning of an ordered partition up to the current row.

```sql
SUM(amount) OVER (
    ORDER BY transaction_date
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND CURRENT ROW
)
```

It is used in financial balances, sales reporting and cumulative metrics.

**Follow-up:** How do you calculate a running total separately for each account?

Add:

```sql
PARTITION BY account_id
```

---

## Q14. What is LAG()?

**Answer:**

LAG() retrieves a value from a preceding row in the current window partition.

```sql
LAG(salary) OVER (
    ORDER BY employee_id
)
```

This retrieves the previous employee's salary in employee ID order.

It is useful for historical comparisons and time-series analysis.

**Follow-up:** How do you retrieve the value from two rows earlier?

```sql
LAG(salary, 2) OVER (
    ORDER BY employee_id
)
```

---

## Q15. What is LEAD()?

**Answer:**

LEAD() retrieves a value from a subsequent row.

```sql
LEAD(salary) OVER (
    ORDER BY employee_id
)
```

It is the forward-looking counterpart of LAG().

**Follow-up:** What happens at the last row?

If no subsequent row exists, LEAD() returns NULL unless a default argument is supplied.

---

## Q16. What is FIRST_VALUE()?

**Answer:**

FIRST_VALUE() returns the value from the first row of the current window frame.

```sql
FIRST_VALUE(salary) OVER (
    PARTITION BY department
    ORDER BY employee_id
)
```

It can be used to retrieve the first recorded salary within each department's ordered partition.

**Follow-up:** Does FIRST_VALUE() depend on the frame?

Yes. It returns the first value in the current frame.

---

## Q17. What is LAST_VALUE()?

**Answer:**

LAST_VALUE() returns the value from the last row of the current window frame.

It does not automatically return the last value of the entire partition.

To retrieve the final value of the entire partition:

```sql
LAST_VALUE(salary) OVER (
    ORDER BY employee_id
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND UNBOUNDED FOLLOWING
)
```

**Common incorrect answer:** LAST_VALUE() always returns the final partition value.

**Follow-up:** Why does the default frame cause problems?

Because the ordered default frame commonly ends at the current row's peer group.

---

## Q18. What is NTH_VALUE()?

**Answer:**

NTH_VALUE() retrieves the value from a specified position in the current frame.

Example:

```sql
NTH_VALUE(salary, 3) OVER (
    ORDER BY salary DESC
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND UNBOUNDED FOLLOWING
)
```

Returns the salary from the third row of the complete partition.

**Follow-up:** Is N positional numbering zero-based?

No. NTH_VALUE() uses one-based positions.

---

## Q19. What is PERCENT_RANK()?

**Answer:**

PERCENT_RANK() calculates the relative rank of a row.

Formula:

```text
(RANK - 1) / (N - 1)
```

Its output ranges from 0 to 1.

For a single-row partition, the result is 0.

**Follow-up:** Does it use ROW_NUMBER()?

No. It uses the rank, so tied values can receive the same percent rank.

---

## Q20. What is CUME_DIST()?

**Answer:**

CUME_DIST() calculates the proportion of rows whose ordering values are at or before the current row's peer group in the specified ordering.

Formula:

```text
Number of rows through current peer group
------------------------------------------
              Total rows
```

Its result is greater than zero and at most one for a nonempty partition.

**Follow-up:** How is it different from PERCENT_RANK()?

PERCENT_RANK() uses rank position, whereas CUME_DIST() uses the cumulative number of rows, including peers.

---

# Chapter 93: Intermediate Interview Questions and Answers

## Q21. How do you find the highest-paid employee in every department?

**Answer:**

Use RANK() with PARTITION BY department.

```sql
WITH ranked AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        RANK() OVER (
            PARTITION BY department
            ORDER BY salary DESC
        ) AS rn

    FROM employees
)

SELECT *
FROM ranked
WHERE rn = 1;
```

RANK() preserves ties.

**Follow-up:** How would you return exactly one employee per department?

Use ROW_NUMBER() with a deterministic tie-breaker.

---

## Q22. How do you find the second-highest distinct salary?

**Answer:**

Use DENSE_RANK().

```sql
WITH ranked AS (
    SELECT
        employee_name,
        salary,

        DENSE_RANK() OVER (
            ORDER BY salary DESC
        ) AS salary_rank

    FROM employees
)

SELECT *
FROM ranked
WHERE salary_rank = 2;
```

DENSE_RANK() skips duplicate salary values when determining distinct positions.

**Follow-up:** How would you handle a table containing only one distinct salary?

The query returns no row because no second distinct salary exists.

---

## Q23. How do you calculate a moving average?

**Answer:**

Use AVG() with a bounded ROWS frame.

```sql
SELECT
    transaction_date,
    amount,

    AVG(amount) OVER (
        ORDER BY transaction_date
        ROWS BETWEEN 2 PRECEDING
        AND CURRENT ROW
    ) AS moving_average

FROM transactions;
```

The calculation uses up to three rows.

**Follow-up:** How do you calculate a seven-row moving average?

Use:

```sql
ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
```

---

## Q24. How do you calculate month-over-month growth?

**Answer:**

Use LAG() to retrieve previous-period revenue.

```sql
WITH revenue_analysis AS (
    SELECT
        month_start,
        revenue,

        LAG(revenue) OVER (
            ORDER BY month_start
        ) AS previous_revenue

    FROM monthly_sales
)

SELECT
    month_start,
    revenue,

    ROUND(
        (revenue - previous_revenue)
        * 100.0 /
        NULLIF(previous_revenue, 0),
        2
    ) AS growth_percentage

FROM revenue_analysis;
```

**Follow-up:** What if a month is missing?

Use a calendar table or month-key join. LAG() retrieves the previous row, not necessarily the previous calendar month.

---

## Q25. How do you find employees earning above their department average?

**Answer:**

Calculate AVG() OVER(PARTITION BY department) in a CTE and filter in the outer query.

```sql
WITH employee_analysis AS (
    SELECT
        employee_name,
        department,
        salary,

        AVG(salary) OVER (
            PARTITION BY department
        ) AS department_average

    FROM employees
)

SELECT *
FROM employee_analysis
WHERE salary > department_average;
```

**Follow-up:** Can you use the window expression directly in WHERE?

Not at the same query level.

---

## Q26. Can we use multiple window functions in a single SELECT?

**Answer:**

Yes.

```sql
SELECT
    employee_name,
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

    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_average

FROM employees;
```

Different window functions can use different window definitions.

**Follow-up:** Can the database reuse sorting work?

It may reuse compatible ordering operations, depending on the optimizer.

---

## Q27. Can a window function be used with GROUP BY?

**Answer:**

Yes.

Example:

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

GROUP BY produces department totals.

RANK() ranks those grouped rows.

**Follow-up:** Does the window function operate on the original employee rows?

No. At this query level, it operates on the grouped result.

---

## Q28. How do you find the latest record for each customer?

**Answer:**

Use ROW_NUMBER() partitioned by customer ID and ordered by date descending.

```sql
WITH latest_records AS (
    SELECT
        *,
        ROW_NUMBER() OVER (
            PARTITION BY customer_id
            ORDER BY updated_at DESC, record_id DESC
        ) AS rn

    FROM customer_history
)

SELECT *
FROM latest_records
WHERE rn = 1;
```

**Follow-up:** What happens when two records have the same timestamp?

The additional unique record ID resolves the tie.

---

## Q29. What is the difference between ROWS and RANGE?

**Answer:**

ROWS uses physical row positions.

RANGE uses ordering values and includes peers according to the frame boundaries.

Example:

```sql
ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
```

Calculates a physical row-by-row running total.

```sql
RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
```

Includes the current ordering value's peer group.

**Follow-up:** Which should you use for the last three transactions?

ROWS.

---

## Q30. What is the difference between ROWS, RANGE and GROUPS?

**Answer:**

- ROWS counts physical rows.
- RANGE defines frames using ordering values.
- GROUPS counts peer groups.

GROUPS is not supported by MySQL 8.0.

**Follow-up:** Which database supports GROUPS?

PostgreSQL supports GROUPS frames.

---

## Q31. What is the difference between LAG() and LEAD()?

**Answer:**

LAG() retrieves a preceding row's value.

LEAD() retrieves a subsequent row's value.

Both accept an expression, an optional offset and an optional default.

**Follow-up:** Can they be partitioned by customer?

Yes.

```sql
LAG(amount) OVER (
    PARTITION BY customer_id
    ORDER BY transaction_date
)
```

---

## Q32. What is the difference between FIRST_VALUE() and MIN()?

**Answer:**

FIRST_VALUE() retrieves the expression from the first row in the frame according to the window ordering.

MIN() retrieves the smallest non-NULL expression value in the frame.

These may produce different results.

Example:

| date | amount |
|---|---:|
| Jan 01 | 500 |
| Jan 02 | 200 |
| Jan 03 | 400 |

```sql
FIRST_VALUE(amount) OVER (
    ORDER BY date
)
```

Returns 500.

```sql
MIN(amount) OVER ()
```

Returns 200.

**Follow-up:** Can FIRST_VALUE() return NULL?

Yes, if the first row's expression is NULL.

---

## Q33. Why is LAST_VALUE() often misunderstood?

**Answer:**

Because developers assume it returns the final value of the complete partition.

It actually returns the value from the last row of the current frame.

The ordered default frame generally ends at the current row's peer group.

**Follow-up:** How do you retrieve the final partition value?

Use UNBOUNDED FOLLOWING as the frame ending boundary.

---

## Q34. How do you identify duplicate records using window functions?

**Answer:**

Use COUNT() OVER() or ROW_NUMBER().

To identify duplicates:

```sql
COUNT(*) OVER (
    PARTITION BY email
)
```

To retain only one record:

```sql
ROW_NUMBER() OVER (
    PARTITION BY email
    ORDER BY created_at DESC
)
```

**Follow-up:** What is the difference between identifying duplicates and deduplicating?

Identifying reports duplicate groups; deduplicating selects the record to retain.

---

## Q35. What is the difference between a CTE and a derived table when filtering window functions?

**Answer:**

Both can provide an outer query level where the calculated window result can be filtered.

CTEs use WITH.

Derived tables are written inside FROM.

Example:

```sql
WITH ranked AS (
    SELECT
        employee_name,

        ROW_NUMBER() OVER (
            ORDER BY salary DESC
        ) AS rn

    FROM employees
)

SELECT *
FROM ranked
WHERE rn <= 3;
```

**Follow-up:** Does a CTE always materialize?

No. Materialization depends on database implementation and optimizer rules.

