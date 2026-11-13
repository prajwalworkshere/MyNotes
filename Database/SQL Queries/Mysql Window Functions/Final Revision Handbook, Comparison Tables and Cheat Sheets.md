# SQL Window Functions – Complete Beginner to Advanced Guide

# Part 11 – Final Revision Handbook, Comparison Tables and Cheat Sheets

# Chapter 96: Complete Function Comparison Tables

## 96.1 ROW_NUMBER() vs RANK() vs DENSE_RANK()

| Feature | ROW_NUMBER() | RANK() | DENSE_RANK() |
|---|---|---|---|
| Assigns a number to each row | Yes | Yes | Yes |
| Equal values get equal numbers | No | Yes | Yes |
| Skips ranks after ties | No | Yes | No |
| Unique number per row | Yes | No | No |
| Deduplication | Best choice | Not suitable | Not suitable |
| Second distinct salary | Not suitable | Not reliable for this purpose | Best choice |
| Top N physical records | Best choice | Depends on tie requirement | Depends on tie requirement |

### Example

Input salaries:

```text
90000
80000
80000
70000
60000
```

Output:

| Salary | ROW_NUMBER | RANK | DENSE_RANK |
|---:|---:|---:|---:|
| 90000 | 1 | 1 | 1 |
| 80000 | 2 | 2 | 2 |
| 80000 | 3 | 2 | 2 |
| 70000 | 4 | 4 | 3 |
| 60000 | 5 | 5 | 4 |

**Remember:**

- ROW_NUMBER = Unique sequence.
- RANK = Competition ranking.
- DENSE_RANK = Consecutive distinct ranking.

---

## 96.2 ROWS vs RANGE vs GROUPS

| Feature | ROWS | RANGE | GROUPS |
|---|---|---|---|
| Basis | Physical row position | Ordering value | Peer-group position |
| Counts individual rows | Yes | No | No |
| Equal ORDER BY values | Separate physical rows | Peer-aware | One peer group |
| Moving three physical records | Yes | No | No |
| Value-based interval | No | Yes, where supported | No |
| Moving peer groups | No | Different semantics | Yes |
| MySQL 8.0 | Supported | Supported | Not supported |
| PostgreSQL | Supported | Supported | Supported |

### Memory trick

```text
ROWS   → How many records?
RANGE  → Which ordering values?
GROUPS → How many peer groups?
```

---

## 96.3 LAG() vs LEAD()

| Feature | LAG() | LEAD() |
|---|---|---|
| Direction | Backward | Forward |
| Retrieves | Previous value | Next value |
| Default offset | 1 | 1 |
| Default missing-row result | NULL | NULL |
| Supports custom offset | Yes | Yes |
| Supports default argument | Yes | Yes |
| Previous purchase | Suitable | Not directly |
| Next purchase | Not directly | Suitable |

---

## 96.4 FIRST_VALUE() vs LAST_VALUE() vs NTH_VALUE()

| Feature | FIRST_VALUE() | LAST_VALUE() | NTH_VALUE() |
|---|---|---|---|
| Retrieves | First value | Last value | Nth value |
| Position | First row | Last row | Specified row |
| Depends on frame | Yes | Yes | Yes |
| Common application | Initial state | Final state | Positional analysis |
| Common trap | Wrong ordering | Incorrect default frame | Nth row unavailable in frame |

---

## 96.5 Window Functions vs GROUP BY

| Feature | Window Function | GROUP BY |
|---|---|---|
| Row preservation | Yes | Generally no |
| Group calculations | Yes | Yes |
| Running totals | Yes | Not directly |
| Ranking | Yes | No direct ranking |
| Previous row access | Yes, using LAG() | No direct support |
| Moving average | Yes | Requires additional techniques |
| Summary reports | Yes, but often unnecessary | Ideal |
| Individual plus group statistics | Ideal | Requires joins/subqueries |

---

## 96.6 Window Functions vs Subqueries

| Feature | Window Functions | Subqueries |
|---|---|---|
| Analytical calculations | Excellent | Possible |
| Ranking | Direct | More complicated |
| Previous row | LAG() | Often requires correlated logic |
| Group averages alongside rows | Direct | Usually requires another query or join |
| Filtering calculated results | Requires another query level in many databases | Naturally supported in outer query |
| Reusability | Multiple calculations in one SELECT | Depends on query structure |

A window function is not a replacement for every subquery.

Use the approach that expresses the requirement most clearly and performs appropriately.

---

## 96.7 Window Functions vs Self-Joins

| Requirement | Window Function | Self-Join |
|---|---|---|
| Previous row | LAG() | Join on ordering relationship |
| Next row | LEAD() | Join on ordering relationship |
| Ranking | RANK() | More complicated |
| Running total | SUM() OVER() | Often complicated |
| Hierarchical employee-manager relationship | Not directly ideal | Suitable |
| Recursive relationships | Recursive CTE | Not necessarily a simple self-join |

---

## 96.8 PARTITION BY vs GROUP BY

| PARTITION BY | GROUP BY |
|---|---|
| Part of OVER() | Query clause |
| Defines analytical partitions | Groups input records |
| Preserves individual rows | Generally collapses rows |
| Used with window functions | Used with aggregate queries |
| Supports ranking within groups | Does not directly provide ranking |
| Supports running calculations with ORDER BY and frames | Requires additional techniques |

---

## 96.9 RANK vs PERCENT_RANK vs CUME_DIST

| Feature | RANK | PERCENT_RANK | CUME_DIST |
|---|---|---|---|
| Result | Integer | Decimal | Decimal |
| Represents | Ranking position | Relative rank | Cumulative distribution |
| Formula | Competition position | (RANK - 1) / (N - 1) | Rows through peer group / N |
| Minimum result | 1 for a nonempty partition | 0 | Greater than 0 |
| Maximum result | N or lower, depending on ties | 1 for a partition with at least two rows | 1 |
| Ties | Same rank | Same percentage rank | Same cumulative distribution |
| Typical use | Employee ranking | Relative position | Distribution analysis |

---

# Chapter 97: Complete Window Function Syntax Cheat Sheet

## 97.1 General Syntax

```sql
SELECT
    column1,
    column2,

    window_function(expression) OVER (
        PARTITION BY partition_column
        ORDER BY ordering_column
        ROWS BETWEEN frame_start AND frame_end
    ) AS result

FROM table_name;
```

Remember:

- PARTITION BY is optional.
- ORDER BY is optional for functions that do not need an ordering.
- A frame is optional.
- Some functions have their own requirements and restrictions.

## 97.2 Ranking Functions

### ROW_NUMBER()

```sql
ROW_NUMBER() OVER (
    PARTITION BY department
    ORDER BY salary DESC
)
```

### RANK()

```sql
RANK() OVER (
    PARTITION BY department
    ORDER BY salary DESC
)
```

### DENSE_RANK()

```sql
DENSE_RANK() OVER (
    PARTITION BY department
    ORDER BY salary DESC
)
```

### NTILE()

```sql
NTILE(4) OVER (
    PARTITION BY department
    ORDER BY salary DESC
)
```

## 97.3 Aggregate Window Functions

### SUM()

```sql
SUM(salary) OVER (
    PARTITION BY department
)
```

### AVG()

```sql
AVG(salary) OVER (
    PARTITION BY department
)
```

### COUNT()

```sql
COUNT(*) OVER (
    PARTITION BY department
)
```

### MIN()

```sql
MIN(salary) OVER (
    PARTITION BY department
)
```

### MAX()

```sql
MAX(salary) OVER (
    PARTITION BY department
)
```

## 97.4 Navigation Functions

### LAG()

```sql
LAG(expression, offset, default_value) OVER (
    PARTITION BY group_column
    ORDER BY ordering_column
)
```

### LEAD()

```sql
LEAD(expression, offset, default_value) OVER (
    PARTITION BY group_column
    ORDER BY ordering_column
)
```

## 97.5 Value Functions

### FIRST_VALUE()

```sql
FIRST_VALUE(expression) OVER (
    PARTITION BY group_column
    ORDER BY ordering_column
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND UNBOUNDED FOLLOWING
)
```

### LAST_VALUE()

```sql
LAST_VALUE(expression) OVER (
    PARTITION BY group_column
    ORDER BY ordering_column
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND UNBOUNDED FOLLOWING
)
```

### NTH_VALUE()

```sql
NTH_VALUE(expression, N) OVER (
    PARTITION BY group_column
    ORDER BY ordering_column
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND UNBOUNDED FOLLOWING
)
```

## 97.6 Distribution Functions

### PERCENT_RANK()

```sql
PERCENT_RANK() OVER (
    PARTITION BY group_column
    ORDER BY ordering_column
)
```

### CUME_DIST()

```sql
CUME_DIST() OVER (
    PARTITION BY group_column
    ORDER BY ordering_column
)
```

## 97.7 Named Window Syntax

```sql
SELECT
    employee_name,

    SUM(salary) OVER department_window AS total_salary,

    AVG(salary) OVER department_window AS average_salary

FROM employees

WINDOW department_window AS (
    PARTITION BY department
);
```

---

# Chapter 98: Window Frame Syntax Cheat Sheet

## 98.1 Running Total

```sql
SUM(amount) OVER (
    ORDER BY transaction_date
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND CURRENT ROW
)
```

## 98.2 Complete Partition

```sql
SUM(amount) OVER (
    ROWS BETWEEN UNBOUNDED PRECEDING
    AND UNBOUNDED FOLLOWING
)
```

## 98.3 Moving Average of Three Rows

```sql
AVG(amount) OVER (
    ORDER BY transaction_date
    ROWS BETWEEN 2 PRECEDING
    AND CURRENT ROW
)
```

## 98.4 Moving Average of Five Rows

```sql
AVG(amount) OVER (
    ORDER BY transaction_date
    ROWS BETWEEN 4 PRECEDING
    AND CURRENT ROW
)
```

## 98.5 Current and Next Two Rows

```sql
SUM(amount) OVER (
    ORDER BY transaction_date
    ROWS BETWEEN CURRENT ROW
    AND 2 FOLLOWING
)
```

## 98.6 Remaining Total

```sql
SUM(amount) OVER (
    ORDER BY transaction_date
    ROWS BETWEEN CURRENT ROW
    AND UNBOUNDED FOLLOWING
)
```

## 98.7 Cumulative Peer-Group Total

```sql
SUM(amount) OVER (
    ORDER BY amount
    RANGE BETWEEN UNBOUNDED PRECEDING
    AND CURRENT ROW
)
```

## 98.8 Current and Previous Peer Groups

PostgreSQL and other GROUPS-supporting systems:

```sql
SUM(amount) OVER (
    ORDER BY amount
    GROUPS BETWEEN 1 PRECEDING
    AND CURRENT ROW
)
```

Not valid in MySQL 8.0.

---

# Chapter 99: Frequently Used SQL Query Patterns

| Business requirement | Recommended query pattern |
|---|---|
| Highest employee salary per department | RANK() + PARTITION BY |
| Exactly one highest-paid employee | ROW_NUMBER() + PARTITION BY |
| Second-highest distinct salary | DENSE_RANK() |
| Top three employees | ROW_NUMBER() |
| Top three salary levels | DENSE_RANK() |
| Include ties at top three positions | RANK() |
| Department average | AVG() OVER(PARTITION BY) |
| Department total | SUM() OVER(PARTITION BY) |
| Global average | AVG() OVER() |
| Running total | SUM() + ROWS |
| Moving average | AVG() + bounded ROWS |
| Previous transaction | LAG() |
| Next transaction | LEAD() |
| First transaction | FIRST_VALUE() |
| Last transaction | LAST_VALUE() + full frame |
| Third value | NTH_VALUE() + full frame |
| Month-over-month growth | LAG() + percentage formula |
| Percentage contribution | SUM() OVER() |
| Cumulative percentage | Running SUM() / overall SUM() |
| Deduplication | ROW_NUMBER() |
| Duplicate identification | COUNT() OVER(PARTITION BY) |
| Latest record | ROW_NUMBER() descending |
| Salary changes | LAG() + comparison |
| Consecutive dates | Gaps and Islands |
| Missing dates | Calendar + LEFT JOIN |
| Sessionization | LAG() + CASE + running SUM() |
| Cumulative distinct count in MySQL | First-occurrence flag + SUM() |
| Highest-selling product per region | RANK() + PARTITION BY |
| Repeat customer | COUNT() OVER() |
| Account balance | Running SUM() + opening balance |

---

# Chapter 100: Important Mathematical Formulas

## 100.1 Average

\[
AVG = \frac{\text{Sum of non-NULL values}}{\text{Count of non-NULL values}}
\]

## 100.2 Percentage Contribution

\[
\text{Contribution} =
\frac{\text{Individual Value}}{\text{Group Total}}\times100
\]

## 100.3 Cumulative Percentage

\[
\text{Cumulative Percentage} =
\frac{\text{Cumulative Total}}{\text{Overall Total}}\times100
\]

## 100.4 Month-over-Month Growth

\[
MoM =
\frac{\text{Current Month}-\text{Previous Month}}
{\text{Previous Month}}\times100
\]

## 100.5 Year-over-Year Growth

\[
YoY =
\frac{\text{Current Period}-\text{Previous Year Period}}
{\text{Previous Year Period}}\times100
\]

## 100.6 PERCENT_RANK

\[
PERCENT\_RANK = \frac{RANK-1}{N-1}
\]

For a single-row partition, PERCENT_RANK() returns 0.

## 100.7 CUME_DIST

\[
CUME\_DIST =
\frac{\text{Rows through current peer group}}{\text{Total Rows}}
\]

## 100.8 Median

For sorted values:

- Odd N: Middle value.
- Even N: Average of the two middle values.

## 100.9 NTILE Bucket Distribution

For N rows and B buckets:

\[
\text{Base bucket size} = \left\lfloor\frac{N}{B}\right\rfloor
\]

The remainder is allocated to the earlier buckets.

## 100.10 Running Account Balance

\[
\text{Balance}_n =
\text{Opening Balance}+
\sum_{i=1}^{n}\text{Transaction}_i
\]

---

# Chapter 101: The 20 Most Important Interview Traps

| No. | Interview trap | Correct understanding |
|---:|---|---|
| 1 | PARTITION BY is equivalent to GROUP BY | It preserves rows |
| 2 | OVER() is only for ranking | It supports multiple categories |
| 3 | Window ORDER BY sorts final output | It controls analytical ordering |
| 4 | ROW_NUMBER preserves ties | It gives different row numbers |
| 5 | RANK never skips numbers | It skips after ties |
| 6 | DENSE_RANK is the same as RANK | It does not skip ranks |
| 7 | NTILE creates equal value ranges | It creates approximately equal row counts |
| 8 | RANGE and ROWS are equivalent | Their frame semantics differ |
| 9 | GROUPS counts physical rows | It counts peer groups |
| 10 | Default ordered SUM always processes one row at a time | Peers may be included together |
| 11 | LAST_VALUE returns the partition's last value | It returns the last value in the frame |
| 12 | LAG always returns the previous calendar period | It returns the previous row |
| 13 | LAG default replaces actual NULL values | It replaces missing-row results |
| 14 | FIRST_VALUE is the same as MIN | One uses ordering, the other value comparison |
| 15 | Window functions work directly in WHERE | They need another query level |
| 16 | MySQL supports QUALIFY | MySQL 8.0 does not |
| 17 | All databases support GROUPS | They do not |
| 18 | COUNT(DISTINCT) OVER works everywhere | It is database-dependent |
| 19 | Window functions are always faster than joins | Performance depends on the execution plan |
| 20 | A CTE always creates a physical temporary table | Optimizer behavior varies |

---

# Chapter 102: Complete Mental Model for Solving Window Function Problems

When you receive an analytical SQL problem, follow this sequence.

## Step 1: Identify the required output

Ask:

- Do I need every individual record?
- Do I need one row per group?
- Do I need the highest or lowest record?
- Do I need a running calculation?
- Do I need to compare consecutive records?

## Step 2: Identify the partition

Ask:

"What defines an independent group?"

Examples:

```sql
PARTITION BY department
```

```sql
PARTITION BY customer_id
```

```sql
PARTITION BY account_id
```

## Step 3: Identify the ordering

Ask:

"What determines the sequence?"

Examples:

```sql
ORDER BY salary DESC
```

```sql
ORDER BY transaction_date, transaction_id
```

## Step 4: Select the function

| Requirement | Function |
|---|---|
| Unique position | ROW_NUMBER |
| Competition rank | RANK |
| Distinct rank | DENSE_RANK |
| Buckets | NTILE |
| Running total | SUM |
| Moving average | AVG |
| Previous value | LAG |
| Next value | LEAD |
| First value | FIRST_VALUE |
| Last value | LAST_VALUE |
| Nth value | NTH_VALUE |
| Relative rank | PERCENT_RANK |
| Cumulative distribution | CUME_DIST |

## Step 5: Determine whether a frame is needed

Ask:

- Entire partition?
- Current row?
- Previous N rows?
- Next N rows?
- Current peer group?
- Entire remaining partition?

## Step 6: Determine whether another query level is necessary

If you need to filter the calculated window result, use:

- CTE.
- Derived table.
- QUALIFY, where supported.

## Step 7: Check edge cases

Ask:

- What if values are duplicated?
- What if values are NULL?
- What if there is only one record?
- What if a group has fewer records than N?
- What if dates are missing?
- What if ordering values are tied?
- What if the partition is empty?

## Step 8: Verify the output

Never trust a complex analytical query simply because it executes successfully.

Manually verify a few rows and compare the expected business logic.

---

# Chapter 103: Final SQL Window Functions Revision Notes

## 103.1 Fundamentals

- Window functions calculate across related rows.
- Individual rows are preserved.
- OVER() defines the window.
- PARTITION BY divides the window into groups.
- ORDER BY defines logical ordering.
- A frame restricts the rows used for the current calculation.

## 103.2 Ranking

- ROW_NUMBER(): Unique sequential number.
- RANK(): Ties share rank, gaps appear.
- DENSE_RANK(): Ties share rank, no gaps.
- NTILE(): Approximately equal-sized buckets.

## 103.3 Aggregates

- SUM(): Total.
- AVG(): Average.
- COUNT(): Count.
- MIN(): Minimum.
- MAX(): Maximum.

## 103.4 Navigation

- LAG(): Previous row.
- LEAD(): Next row.

## 103.5 Value Functions

- FIRST_VALUE(): First frame value.
- LAST_VALUE(): Last frame value.
- NTH_VALUE(): Nth frame value.

## 103.6 Distribution

- PERCENT_RANK(): Relative rank.
- CUME_DIST(): Cumulative distribution.

## 103.7 Frames

- ROWS: Physical row positions.
- RANGE: Ordering-value-based frame.
- GROUPS: Peer-group positions.
- UNBOUNDED PRECEDING: Partition beginning.
- UNBOUNDED FOLLOWING: Partition ending.
- CURRENT ROW: Current row or peers, depending on frame type.

## 103.8 Query Processing

- Window functions are evaluated after WHERE, GROUP BY and HAVING in the logical processing model.
- They generally cannot be filtered directly in WHERE at the same query level.
- Use CTEs or derived tables.
- QUALIFY is available in some databases, but not MySQL 8.0.

## 103.9 Advanced Patterns

- Gaps and Islands.
- Sessionization.
- Running account balances.
- Consecutive transaction analysis.
- Cumulative distinct counts.
- Historical change detection.
- Deduplication.
- Percentage contribution.

## 103.10 Performance

- Sorting can be expensive.
- Large partitions can consume resources.
- Composite indexes may help.
- Compatible windows may share processing.
- CTEs may be materialized.
- Execution plans should be inspected.
- Actual performance must be measured.

