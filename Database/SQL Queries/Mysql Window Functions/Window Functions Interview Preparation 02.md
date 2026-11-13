# SQL Window Functions – Interview Preparation Handbook

# Chapter 94: Advanced Interview Questions and Answers

## Q36. What is the default window frame when ORDER BY is specified?

**Answer:**

In MySQL 8.0, an aggregate window with ORDER BY and no explicit frame generally uses:

```sql
RANGE BETWEEN UNBOUNDED PRECEDING
AND CURRENT ROW
```

This means the current row and its peers are included.

For a window without ORDER BY, the default frame covers the entire partition.

**Why does it matter?**

Duplicate ordering values can receive the same cumulative aggregate.

**Follow-up:** How do you force row-by-row accumulation?

```sql
ROWS BETWEEN UNBOUNDED PRECEDING
AND CURRENT ROW
```

---

## Q37. Why can two rows receive the same running total?

**Answer:**

Because they may share the same ORDER BY value and therefore belong to the same peer group under a RANGE frame.

Example:

| amount | Running total |
|---:|---:|
| 100 | 100 |
| 200 | 500 |
| 200 | 500 |
| 300 | 800 |

Both 200 records are peers.

**Follow-up:** How do you prevent this?

Use ROWS with a deterministic ordering.

---

## Q38. What are peer groups?

**Answer:**

Peer groups are sets of rows that have identical values for all expressions in the window's ORDER BY clause.

They are important for:

- RANK().
- DENSE_RANK().
- PERCENT_RANK().
- CUME_DIST().
- RANGE frames.
- GROUPS frames.

Example:

```sql
RANK() OVER (
    ORDER BY salary DESC
)
```

Employees earning the same salary are peers.

**Follow-up:** Does adding employee_id to ORDER BY change peer groups?

Yes, if employee_id differs between employees, they no longer have identical complete ordering values.

---

## Q39. Can a window function be used directly in WHERE?

**Answer:**

Generally, no, not at the same query level.

Window functions are logically evaluated after WHERE.

Use a CTE, derived table or QUALIFY where supported.

**Follow-up:** Does QUALIFY work in MySQL?

No. MySQL 8.0 requires a CTE or derived table.

---

## Q40. Can window functions be nested?

**Answer:**

Window functions generally cannot be directly nested inside other window functions at the same query level.

Incorrect:

```sql
SUM(
    ROW_NUMBER() OVER (
        ORDER BY salary
    )
) OVER ()
```

Correct:

```sql
WITH numbered AS (
    SELECT
        employee_name,

        ROW_NUMBER() OVER (
            ORDER BY salary
        ) AS rn

    FROM employees
)

SELECT
    employee_name,
    rn,

    SUM(rn) OVER () AS total_rn

FROM numbered;
```

**Follow-up:** Why does the CTE work?

It creates a new query level in which the first window calculation becomes an ordinary input column.

---

## Q41. How do you calculate cumulative distinct customers in MySQL?

**Answer:**

MySQL 8.0 does not support DISTINCT inside window aggregate calls such as COUNT(DISTINCT customer_id) OVER().

Use:

1. ROW_NUMBER() partitioned by customer.
2. Mark the first occurrence.
3. Calculate a running SUM() of the flags.

**Follow-up:** Why does this work?

Every customer contributes exactly one to the cumulative count, at their first occurrence.

---

## Q42. How do you solve Gaps and Islands problems?

**Answer:**

A common method is:

```sql
ROW_NUMBER() OVER (
    PARTITION BY entity_id
    ORDER BY event_date
)
```

Then calculate a grouping key using date arithmetic.

For daily consecutive dates:

```sql
DATE_SUB(
    event_date,
    INTERVAL rn DAY
)
```

Consecutive dates produce the same grouping key.

**Follow-up:** What if duplicate dates exist?

Deduplicate the entity-date combinations before calculating row numbers, if the requirement is based on consecutive calendar days.

---

## Q43. How do you implement sessionization using window functions?

**Answer:**

Use:

1. LAG() to obtain the previous event time.
2. CASE WHEN to identify a new session.
3. Running SUM() to assign session numbers.

```sql
SUM(new_session_flag) OVER (
    PARTITION BY user_id
    ORDER BY event_time
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND CURRENT ROW
)
```

**Follow-up:** What happens when the inactivity threshold is exactly 30 minutes?

That depends on the business rule.

Use `>` if only gaps greater than 30 minutes create new sessions.

Use `>=` if gaps of exactly 30 minutes also create new sessions.

---

## Q44. What happens when ORDER BY contains NULL values?

**Answer:**

NULL values are included in the ordering, but their default placement varies across databases.

The ordering also affects peer groups and positional functions.

To make behavior predictable, explicitly control NULL placement where supported.

For MySQL:

```sql
ORDER BY salary IS NULL, salary DESC
```

This puts non-NULL salaries first.

**Follow-up:** Does NULL behave like zero?

No. NULL represents an unknown or missing value.

---

## Q45. How do you handle ties when selecting the top three employees?

**Answer:**

First clarify the business requirement.

| Requirement | Function |
|---|---|
| Exactly three employees | ROW_NUMBER() |
| Include ties at the third row position | RANK() |
| Top three distinct salary levels | DENSE_RANK() |

**Follow-up:** Why can these produce different row counts?

Because their treatment of tied ordering values is different.

---

## Q46. Can window functions be used after JOIN operations?

**Answer:**

Yes.

Window functions operate on the result rows produced by FROM and JOIN, after the relevant WHERE and grouping stages.

However, JOIN multiplication can affect rankings and aggregates.

**Follow-up:** How do you avoid accidental multiplication?

Perform the analytical calculation at the intended data grain, or pre-aggregate/deduplicate the joined data before applying the window.

---

## Q47. Can aggregate window functions be used with GROUP BY?

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

The window function operates on grouped department records.

**Follow-up:** Can you calculate a window aggregate on the original employee rows and then group by that result at the same level?

Not directly. Use another query level when needed.

---

## Q48. How does indexing improve window-function performance?

**Answer:**

An appropriate index can reduce the amount of data read and may help satisfy partitioning or ordering requirements.

For example:

```sql
CREATE INDEX idx_customer_date
ON transactions(customer_id, transaction_date);
```

This may help customer-based chronological analysis.

However, an index does not guarantee that the database will eliminate sorting.

**Follow-up:** How do you verify whether an index helps?

Use EXPLAIN and EXPLAIN ANALYZE and compare actual execution performance.

---

## Q49. Why are window functions sometimes expensive?

**Answer:**

They can require:

- Sorting large datasets.
- Organizing partitions.
- Maintaining frame information.
- Processing many rows.
- Intermediate memory or temporary storage.

Large partitions and incompatible ordering definitions can increase processing costs.

**Follow-up:** How do you optimize them?

Reduce unnecessary input rows, use suitable indexes, avoid unnecessary columns, reuse compatible window definitions and inspect execution plans.

---

## Q50. Are window functions always faster than self-joins?

**Answer:**

No.

Window functions are often more readable for ranking, navigation and cumulative calculations.

However, actual performance depends on the database optimizer, data distribution, indexes and execution plan.

A well-indexed join may outperform a window-based alternative for some requirements.

**Follow-up:** Which approach would you choose for finding the previous transaction?

Usually LAG(), because it expresses the requirement directly.

---

# Chapter 95: Tricky and Scenario-Based Interview Questions

## Q51. A table contains three employees with the same highest salary. How many rows will RANK() = 1 return?

**Answer:**

Three rows.

All employees with the same highest ordering value receive rank 1.

Example:

```sql
RANK() OVER (
    ORDER BY salary DESC
)
```

Filtering for rank 1 returns all three.

**Common incorrect answer:** One row.

That would be a possible ROW_NUMBER() result, not the RANK() result.

**Follow-up:** How would you return exactly one?

Use ROW_NUMBER() with a unique tie-breaker.

---

## Q52. There are ten rows. What does NTILE(4) produce?

**Answer:**

The rows are divided into bucket sizes:

```text
Bucket 1 → 3 rows
Bucket 2 → 3 rows
Bucket 3 → 2 rows
Bucket 4 → 2 rows
```

The extra rows are allocated to the earlier buckets.

**Follow-up:** Does NTILE(4) guarantee equal salary intervals?

No. It aims for approximately equal row counts.

---

## Q53. Why does LAST_VALUE() sometimes return the current row's value instead of the final value?

**Answer:**

Because the default ordered frame usually ends at the current row's peer group.

The last value of that frame may be the current row.

To retrieve the final partition value:

```sql
LAST_VALUE(salary) OVER (
    ORDER BY employee_id
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND UNBOUNDED FOLLOWING
)
```

**Follow-up:** Would FIRST_VALUE() have the same issue?

It also depends on the frame, but the first row generally remains the beginning of a growing ordered frame.

---

## Q54. You calculate LAG(revenue), but February is missing. What does March retrieve?

**Answer:**

March retrieves the previous available row, which may be January.

LAG() operates on rows, not calendar periods.

**Correct approach:** Generate a complete monthly calendar and join the revenue data to it, or use an exact month-key join.

**Follow-up:** Is a 12-row offset always correct for year-over-year growth?

Only when the input contains one properly ordered record for every consecutive month.

---

## Q55. Two transactions have exactly the same timestamp. How do you ensure a deterministic running balance?

**Answer:**

Add a stable unique ordering column.

```sql
SUM(amount) OVER (
    PARTITION BY account_id
    ORDER BY transaction_timestamp, transaction_id
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND CURRENT ROW
)
```

The transaction ID resolves the timestamp tie.

**Follow-up:** What if the business requires transactions to be ordered by an externally defined sequence?

Use that sequence rather than assuming transaction ID always represents business chronology.

