# SQL Window Functions – Complete Beginner to Advanced Guide

# Part 2 – Window Frames: The Most Important and Commonly Misunderstood Concept

# Chapter 6: Understanding Window Frames

## 6.1 What Is a Window Frame?

A window frame is a subset of rows within a window partition that a window function uses to perform its calculation for the current row.

This distinction is extremely important:

- PARTITION BY determines which rows belong to the same partition.
- ORDER BY determines the logical ordering of rows.
- The window frame determines which rows within that partition are included in the calculation for the current row.

A partition can contain 100 rows, but the frame for a particular row might contain only 3 rows.

The frame can also contain the entire partition.

### Example

Consider this sales table:

### sales

| sale_id | sale_date | amount |
|---:|---|---:|
| 1 | 2026-01-01 | 100 |
| 2 | 2026-01-02 | 200 |
| 3 | 2026-01-03 | 300 |
| 4 | 2026-01-04 | 400 |
| 5 | 2026-01-05 | 500 |

Suppose we want a running total.

```sql
SELECT
    sale_id,
    sale_date,
    amount,

    SUM(amount) OVER (
        ORDER BY sale_date
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS running_total

FROM sales;
```

Output:

| sale_id | sale_date | amount | running_total |
|---:|---|---:|---:|
| 1 | Jan 01 | 100 | 100 |
| 2 | Jan 02 | 200 | 300 |
| 3 | Jan 03 | 300 | 600 |
| 4 | Jan 04 | 400 | 1000 |
| 5 | Jan 05 | 500 | 1500 |

How is this calculated?

For each current row, SQL determines the frame:

| Current row | Rows included in frame | Calculation |
|---|---|---|
| 1 | 1 | 100 |
| 2 | 1, 2 | 100 + 200 |
| 3 | 1, 2, 3 | 100 + 200 + 300 |
| 4 | 1, 2, 3, 4 | 100 + 200 + 300 + 400 |
| 5 | 1, 2, 3, 4, 5 | 100 + 200 + 300 + 400 + 500 |

The frame changes for each current row.

That is the key idea behind window frames.

## 6.2 Why Do We Need Window Frames?

Without frames, we would have limited control over which rows participate in a calculation.

Consider the following requirements:

1. Calculate total sales from the beginning until the current day.
2. Calculate the average sales of the current day and previous two days.
3. Calculate the total of the current row and next two rows.
4. Calculate the average of the entire department.
5. Calculate the total from the current row until the final row.

These requirements need different sets of rows.

Window frames allow us to define those sets.

### Common applications

| Requirement | Frame |
|---|---|
| Running total | UNBOUNDED PRECEDING to CURRENT ROW |
| Moving average of 3 rows | 2 PRECEDING to CURRENT ROW |
| Current and next 3 rows | CURRENT ROW to 3 FOLLOWING |
| Entire partition | UNBOUNDED PRECEDING to UNBOUNDED FOLLOWING |
| Remaining total | CURRENT ROW to UNBOUNDED FOLLOWING |

---

# Chapter 7: Understanding Frame Syntax

## 7.1 Complete Frame Syntax

```sql
function_name(expression) OVER (
    PARTITION BY partition_column
    ORDER BY ordering_column

    frame_type BETWEEN frame_start AND frame_end
);
```

The frame type can be:

- ROWS
- RANGE
- GROUPS, where supported

The frame boundaries can include:

- UNBOUNDED PRECEDING
- n PRECEDING
- CURRENT ROW
- n FOLLOWING
- UNBOUNDED FOLLOWING

Example:

```sql
SELECT
    sale_date,
    amount,

    SUM(amount) OVER (
        ORDER BY sale_date
        ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
    ) AS moving_total

FROM sales;
```

This calculates the sum of the current row and the two preceding rows.

## 7.2 Understanding Frame Boundaries

Frame boundaries determine where the frame begins and ends.

### A. UNBOUNDED PRECEDING

Means the first row of the partition.

```sql
ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
```

The frame begins at the beginning of the partition.

Used for:

- Running totals.
- Cumulative averages.
- Cumulative counts.
- Cumulative percentages.

### B. n PRECEDING

Means a specified number of rows before the current row when used with ROWS.

Example:

```sql
ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
```

Includes:

- Two preceding rows.
- One current row.

Total maximum frame size: 3 rows.

### C. CURRENT ROW

Refers to the current row.

Its exact behavior depends on the frame type.

With ROWS, it identifies the current physical row.

With RANGE and GROUPS, it can include the current row's peer group.

We will examine this difference shortly.

### D. n FOLLOWING

Means a specified number of rows after the current row with ROWS.

Example:

```sql
ROWS BETWEEN CURRENT ROW AND 2 FOLLOWING
```

Includes the current row and the next two rows.

### E. UNBOUNDED FOLLOWING

Means the final row of the partition.

Example:

```sql
ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING
```

Includes the current row and all subsequent rows.

### Summary

| Boundary | Meaning |
|---|---|
| UNBOUNDED PRECEDING | Beginning of partition |
| 3 PRECEDING | Three rows before current row with ROWS |
| 2 PRECEDING | Two rows before current row with ROWS |
| CURRENT ROW | Current row or its peers, depending on frame type |
| 2 FOLLOWING | Two rows after current row with ROWS |
| 3 FOLLOWING | Three rows after current row with ROWS |
| UNBOUNDED FOLLOWING | End of partition |

## 7.3 Valid Frame Combinations

Examples:

```sql
ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
```

```sql
ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
```

```sql
ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING
```

```sql
ROWS BETWEEN 1 PRECEDING AND 2 FOLLOWING
```

```sql
ROWS BETWEEN UNBOUNDED PRECEDING
AND UNBOUNDED FOLLOWING
```

A frame cannot have a starting boundary that logically occurs after its ending boundary.

For example, this is invalid:

```sql
ROWS BETWEEN 2 FOLLOWING AND 2 PRECEDING
```

---

# Chapter 8: ROWS vs RANGE vs GROUPS

This is one of the most important window-function interview topics.

All three specify a window frame, but they define frame membership differently.

## 8.1 ROWS – Physical Row-Based Frame

ROWS identifies a frame using physical row positions relative to the current row in the ordered partition.

It does not treat equal ordering values as one unit.

### Syntax

```sql
ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
```

### Example dataset

| id | amount |
|---:|---:|
| 1 | 100 |
| 2 | 200 |
| 3 | 200 |
| 4 | 300 |
| 5 | 400 |

Query:

```sql
SELECT
    id,
    amount,

    SUM(amount) OVER (
        ORDER BY amount, id
        ROWS BETWEEN 1 PRECEDING AND CURRENT ROW
    ) AS rolling_total

FROM sales;
```

Output:

| id | amount | rolling_total |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 300 |
| 3 | 200 | 400 |
| 4 | 300 | 500 |
| 5 | 400 | 700 |

Explanation:

For id 3:

- Previous physical row is id 2.
- Current row is id 3.
- Calculation = 200 + 200 = 400.

The two rows are treated separately even though their amounts are equal.

### When to use ROWS

Use ROWS when your requirement refers to a specific number of records.

Examples:

- Current and previous 3 transactions.
- Last 7 records.
- Moving average over 5 observations.
- Running total with deterministic row ordering.

---

## 8.2 RANGE – Logical Value-Based Frame

RANGE determines frame membership using the values of the ORDER BY expression.

Rows with equal ordering values are called peers.

In a RANGE frame, CURRENT ROW generally includes all rows in the current row's peer group.

This is the important difference from ROWS.

### Example

| id | amount |
|---:|---:|
| 1 | 100 |
| 2 | 200 |
| 3 | 200 |
| 4 | 300 |
| 5 | 400 |

Query:

```sql
SELECT
    id,
    amount,

    SUM(amount) OVER (
        ORDER BY amount
        RANGE BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS cumulative_total

FROM sales;
```

Expected output:

| id | amount | cumulative_total |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 500 |
| 3 | 200 | 500 |
| 4 | 300 | 800 |
| 5 | 400 | 1200 |

### Why does id 2 receive 500?

Because RANGE includes the complete peer group.

The peer group for amount = 200 contains:

```text
id 2 → 200
id 3 → 200
```

The cumulative calculation is:

```text
100 + 200 + 200 = 500
```

Both records receive 500.

### When to use RANGE

Use RANGE when the calculation should be based on ordering values rather than a fixed number of physical records.

Examples:

- Include all employees earning the same salary in a cumulative salary calculation.
- Calculate totals through a particular ordered value.
- Use a value-based interval frame where supported.

### Important dialect restriction

MySQL supports RANGE frames, including numeric and temporal offset forms under applicable restrictions.

However, its RANGE offset syntax has limitations regarding the ORDER BY expression and supported data types.

Do not assume that every database supports identical RANGE offset syntax.

---

## 8.3 GROUPS – Peer-Group-Based Frame

GROUPS identifies frames using groups of rows that share the same ORDER BY values.

Unlike ROWS, which counts individual rows, GROUPS counts peer groups.

Unlike RANGE, GROUPS uses a specified number of peer groups as the frame boundaries.

### Important compatibility note

GROUPS is supported by PostgreSQL and some other modern SQL implementations, but it is not supported by MySQL 8.0 or SQL Server.

The following is PostgreSQL-style SQL, not MySQL 8 SQL.

### Example dataset

| id | amount |
|---:|---:|
| 1 | 100 |
| 2 | 200 |
| 3 | 200 |
| 4 | 300 |
| 5 | 400 |
| 6 | 400 |

There are four peer groups:

```text
Group 1: amount = 100
Group 2: amount = 200
Group 3: amount = 300
Group 4: amount = 400
```

Query:

```sql
SELECT
    id,
    amount,

    SUM(amount) OVER (
        ORDER BY amount
        GROUPS BETWEEN 1 PRECEDING AND CURRENT ROW
    ) AS group_total

FROM sales;
```

Expected output:

| id | amount | group_total |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 500 |
| 3 | 200 | 500 |
| 4 | 300 | 700 |
| 5 | 400 | 1100 |
| 6 | 400 | 1100 |

Explanation:

For the amount 300 peer group:

- Previous peer group: 200, 200.
- Current peer group: 300.

Calculation:

```text
200 + 200 + 300 = 700
```

For the amount 400 peer group:

```text
300 + 400 + 400 = 1100
```

### When to use GROUPS

Use GROUPS when the requirement explicitly refers to preceding or following peer groups rather than individual records.

---

## 8.4 Complete Comparison: ROWS vs RANGE vs GROUPS

| Feature | ROWS | RANGE | GROUPS |
|---|---|---|---|
| Based on | Physical row positions | ORDER BY values | Peer-group positions |
| Counts individual records | Yes | No, value-based | No, peer-group-based |
| Equal ORDER BY values | Treated as separate rows | Included together when peers are in frame | Treated as one group |
| Supports preceding/following | Yes | Yes, subject to syntax and restrictions | Yes, where supported |
| Useful for moving N-row calculations | Yes | Not generally | No |
| Useful for value-based intervals | No | Yes | No |
| Useful for moving peer-group calculations | No | Different semantics | Yes |
| MySQL 8.0 | Supported | Supported | Not supported |
| PostgreSQL | Supported | Supported | Supported |
| SQL Server | Supported | Supported, with restrictions | Not supported |

### Most important interview answer

ROWS counts physical rows.

RANGE uses the values of the ordering expression and includes peers according to the frame boundaries.

GROUPS counts groups of rows having equal ORDER BY values.

---

# Chapter 9: Understanding Default Window Frames

## 9.1 What Is a Default Frame?

When we write an aggregate window function with ORDER BY but do not explicitly specify a frame, the database uses its default frame rules.

This is dangerous because developers sometimes assume that the function calculates over the entire partition.

That assumption is not always correct.

### MySQL 8 default behavior

For a window aggregate with ORDER BY and no explicit frame, MySQL uses a default frame equivalent to:

```sql
RANGE BETWEEN UNBOUNDED PRECEDING
AND CURRENT ROW
```

This includes the current row's peers.

For a window without ORDER BY, the default frame includes the entire partition.

Other databases have broadly similar standard-oriented defaults for many ordered window aggregates, but function-specific and dialect-specific details must be checked.

## 9.2 Example Without ORDER BY

```sql
SELECT
    sale_id,
    amount,

    SUM(amount) OVER () AS total_sales

FROM sales;
```

Output:

| sale_id | amount | total_sales |
|---:|---:|---:|
| 1 | 100 | 1500 |
| 2 | 200 | 1500 |
| 3 | 300 | 1500 |
| 4 | 400 | 1500 |
| 5 | 500 | 1500 |

All rows are included.

## 9.3 Example With ORDER BY but Without an Explicit Frame

```sql
SELECT
    sale_id,
    amount,

    SUM(amount) OVER (
        ORDER BY amount
    ) AS running_total

FROM sales;
```

For unique amounts, the result is:

| sale_id | amount | running_total |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 300 |
| 3 | 300 | 600 |
| 4 | 400 | 1000 |
| 5 | 500 | 1500 |

This looks like a ROWS-based running total.

But when duplicates appear, the result can change.

## 9.4 Default Frame with Duplicate ORDER BY Values

Dataset:

| id | amount |
|---:|---:|
| 1 | 100 |
| 2 | 200 |
| 3 | 200 |
| 4 | 300 |
| 5 | 400 |

Query:

```sql
SELECT
    id,
    amount,

    SUM(amount) OVER (
        ORDER BY amount
    ) AS running_total

FROM sales;
```

Output:

| id | amount | running_total |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 500 |
| 3 | 200 | 500 |
| 4 | 300 | 800 |
| 5 | 400 | 1200 |

Why?

Because the default frame uses RANGE semantics.

Both rows having amount = 200 are peers.

The complete peer group participates in the cumulative calculation.

### How to force individual row-by-row accumulation

```sql
SELECT
    id,
    amount,

    SUM(amount) OVER (
        ORDER BY amount, id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS running_total

FROM sales;
```

Output:

| id | amount | running_total |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 300 |
| 3 | 200 | 500 |
| 4 | 300 | 800 |
| 5 | 400 | 1200 |

This explicitly requests physical row-based accumulation.

### Interview rule

Whenever an aggregate window function uses ORDER BY, ask yourself:

"Do I want peer-group-based behavior or individual row-by-row behavior?"

Then specify RANGE or ROWS explicitly when necessary.

---

# Chapter 10: Advanced Frame Examples

## 10.1 Running Total

A running total calculates the accumulated value from the beginning of the partition up to the current row.

```sql
SELECT
    sale_date,
    amount,

    SUM(amount) OVER (
        ORDER BY sale_date
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS running_total

FROM sales;
```

## 10.2 Moving Average of Three Rows

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

For Jan 04:

```text
(200 + 300 + 400) / 3 = 300
```

For Jan 05:

```text
(300 + 400 + 500) / 3 = 400
```

Notice that the first two rows have smaller frames because there are not yet three available records.

## 10.3 Current Row and Next Two Rows

```sql
SELECT
    sale_id,
    amount,

    SUM(amount) OVER (
        ORDER BY sale_id
        ROWS BETWEEN CURRENT ROW
        AND 2 FOLLOWING
    ) AS next_three_total

FROM sales;
```

Output:

| sale_id | amount | next_three_total |
|---:|---:|---:|
| 1 | 100 | 600 |
| 2 | 200 | 900 |
| 3 | 300 | 1200 |
| 4 | 400 | 900 |
| 5 | 500 | 500 |

For row 2:

```text
200 + 300 + 400 = 900
```

For row 4:

```text
400 + 500 = 900
```

The frame does not extend beyond the partition.

## 10.4 Entire Partition

```sql
SELECT
    sale_id,
    amount,

    SUM(amount) OVER (
        ORDER BY sale_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS complete_total

FROM sales;
```

Every row receives 1500.

The ORDER BY determines the ordering, but the frame includes the entire partition.

## 10.5 Remaining Total

```sql
SELECT
    sale_id,
    amount,

    SUM(amount) OVER (
        ORDER BY sale_id
        ROWS BETWEEN CURRENT ROW
        AND UNBOUNDED FOLLOWING
    ) AS remaining_total

FROM sales;
```

Output:

| sale_id | amount | remaining_total |
|---:|---:|---:|
| 1 | 100 | 1500 |
| 2 | 200 | 1400 |
| 3 | 300 | 1200 |
| 4 | 400 | 900 |
| 5 | 500 | 500 |

For row 3:

```text
300 + 400 + 500 = 1200
```

This is useful for:

- Remaining account obligations.
- Future scheduled payments.
- Remaining inventory quantities.
- Remaining sales targets.

---

# Chapter 11: The LAST_VALUE() Problem

## 11.1 Understanding the Problem

LAST_VALUE() returns the value from the last row of the current window frame.

The critical point is:

**It returns the last value in the frame, not automatically the last value in the entire partition.**

Consider:

| id | amount |
|---:|---:|
| 1 | 100 |
| 2 | 200 |
| 3 | 300 |
| 4 | 400 |
| 5 | 500 |

Query:

```sql
SELECT
    id,
    amount,

    LAST_VALUE(amount) OVER (
        ORDER BY id
    ) AS last_amount

FROM sales;
```

In MySQL, the default ordered frame ends at the current row's peer group.

Because id is unique, the result is:

| id | amount | last_amount |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 200 |
| 3 | 300 | 300 |
| 4 | 400 | 400 |
| 5 | 500 | 500 |

Many beginners expect every row to return 500.

That expectation is incorrect because the default frame does not extend to the end of the partition.

## 11.2 Correct Way to Retrieve the Last Value of the Entire Partition

```sql
SELECT
    id,
    amount,

    LAST_VALUE(amount) OVER (
        ORDER BY id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS final_amount

FROM sales;
```

Output:

| id | amount | final_amount |
|---:|---:|---:|
| 1 | 100 | 500 |
| 2 | 200 | 500 |
| 3 | 300 | 500 |
| 4 | 400 | 500 |
| 5 | 500 | 500 |

Now the frame includes the entire partition.

The last row in every frame is row 5.

Therefore, every row receives 500.

## 11.3 FIRST_VALUE() vs LAST_VALUE()

```sql
SELECT
    id,
    amount,

    FIRST_VALUE(amount) OVER (
        ORDER BY id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS first_amount,

    LAST_VALUE(amount) OVER (
        ORDER BY id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS last_amount

FROM sales;
```

Output:

| id | amount | first_amount | last_amount |
|---:|---:|---:|---:|
| 1 | 100 | 100 | 500 |
| 2 | 200 | 100 | 500 |
| 3 | 300 | 100 | 500 |
| 4 | 400 | 100 | 500 |
| 5 | 500 | 100 | 500 |

### Interview takeaway

When using LAST_VALUE(), always inspect the frame.

If you want the last value of the entire partition, explicitly use:

```sql
ROWS BETWEEN UNBOUNDED PRECEDING
AND UNBOUNDED FOLLOWING
```

---

# Chapter 12: RANGE BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING

This frame is particularly useful when we want to calculate values from the current ordering value to the end of the partition.

### Dataset

| id | amount |
|---:|---:|
| 1 | 100 |
| 2 | 200 |
| 3 | 200 |
| 4 | 300 |
| 5 | 400 |

Query:

```sql
SELECT
    id,
    amount,

    SUM(amount) OVER (
        ORDER BY amount
        RANGE BETWEEN CURRENT ROW
        AND UNBOUNDED FOLLOWING
    ) AS future_total

FROM sales;
```

Output:

| id | amount | future_total |
|---:|---:|---:|
| 1 | 100 | 1200 |
| 2 | 200 | 1100 |
| 3 | 200 | 1100 |
| 4 | 300 | 700 |
| 5 | 400 | 400 |

### Explanation

For amount 100:

```text
100 + 200 + 200 + 300 + 400 = 1200
```

For amount 200:

```text
200 + 200 + 300 + 400 = 1100
```

Both 200 rows belong to the same peer group, so both receive the same result.

For amount 300:

```text
300 + 400 = 700
```

For amount 400:

```text
400
```

This calculation is useful for:

- Remaining cumulative sales.
- Future transaction totals.
- Value-based forward accumulation.
- Calculating amounts from a threshold to the end of an ordered dataset.

---

# Chapter 13: NULL Values and Window Frames

NULL handling depends on the function and the database's ordering rules.

Consider:

| id | amount |
|---:|---:|
| 1 | 100 |
| 2 | NULL |
| 3 | 200 |
| 4 | NULL |
| 5 | 300 |

## 13.1 NULL in SUM()

```sql
SELECT
    id,
    amount,

    SUM(amount) OVER (
        ORDER BY id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS running_total

FROM sales;
```

Conceptually, the result is:

| id | amount | running_total |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | NULL | 100 |
| 3 | 200 | 300 |
| 4 | NULL | 300 |
| 5 | 300 | 600 |

SUM() ignores NULL input values.

The NULL row still participates in the window frame, but its amount does not contribute to the sum.

## 13.2 NULL in COUNT()

```sql
SELECT
    id,
    amount,

    COUNT(*) OVER (
        ORDER BY id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS row_count,

    COUNT(amount) OVER (
        ORDER BY id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS non_null_count

FROM sales;
```

Output:

| id | amount | row_count | non_null_count |
|---:|---:|---:|---:|
| 1 | 100 | 1 | 1 |
| 2 | NULL | 2 | 1 |
| 3 | 200 | 3 | 2 |
| 4 | NULL | 4 | 2 |
| 5 | 300 | 5 | 3 |

Remember:

- COUNT(*) counts rows.
- COUNT(column) counts non-NULL values.

## 13.3 Important NULL Considerations

1. NULL values can participate in partitions.
2. NULL values can participate in ordering.
3. SUM(), AVG(), MIN(), MAX() generally ignore NULL input values.
4. COUNT(column) ignores NULL.
5. COUNT(*) counts all rows in the frame.
6. NULL ordering varies across database systems.
7. Navigation functions may return NULL when no preceding or following row exists.
8. A default argument in LAG() or LEAD() handles a missing row, not necessarily a NULL value in an existing row.

The last distinction is particularly important and will be covered in the navigation-functions chapter.

---

# Chapter 14: Database-Specific Window Frame Support

| Feature | MySQL 8.0 | PostgreSQL | SQL Server | Oracle | Snowflake | BigQuery |
|---|---|---|---|---|---|---|
| ROWS | Yes | Yes | Yes | Yes | Yes | Yes |
| RANGE | Yes | Yes | Yes, with restrictions | Yes | Yes | Yes |
| GROUPS | No | Yes | No | Supported in modern versions | Check supported syntax | No |
| UNBOUNDED PRECEDING | Yes | Yes | Yes | Yes | Yes | Yes |
| UNBOUNDED FOLLOWING | Yes | Yes | Yes | Yes | Yes | Yes |
| CURRENT ROW | Yes | Yes | Yes | Yes | Yes | Yes |
| Named windows | Yes | Yes | No standard WINDOW clause | Supported | Yes | Yes |

**Important:** This is a learning-oriented compatibility overview. Exact support for offset RANGE frames, GROUPS, exclusions, navigation-function frames and named-window inheritance varies by database release and function. Consult the target engine's documentation for production SQL.

---

# Part 2 – Window Frame Quick Revision

## 1. Core definitions

- Window: The set of rows used by a window function.
- Partition: A logical group of rows.
- Frame: The subset of a partition considered for the current row.
- Peer group: Rows sharing the same ORDER BY values.

## 2. Important frame patterns

| Requirement | Syntax |
|---|---|
| Running total | ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW |
| Moving total of 3 rows | ROWS BETWEEN 2 PRECEDING AND CURRENT ROW |
| Moving average of 5 rows | ROWS BETWEEN 4 PRECEDING AND CURRENT ROW |
| Current plus next 2 rows | ROWS BETWEEN CURRENT ROW AND 2 FOLLOWING |
| Remaining total | ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING |
| Entire partition | ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING |
| Current and previous peer groups | GROUPS BETWEEN 1 PRECEDING AND CURRENT ROW |

## 3. Five important interview rules

1. ROWS counts physical rows.
2. RANGE uses ordering values and peer-group semantics.
3. GROUPS counts peer groups.
4. Default ordered aggregate frames can include all current-row peers.
5. LAST_VALUE() does not automatically mean the final value of the entire partition.

## 4. Self-assessment questions

Before moving ahead, make sure you can answer these without memorizing:

1. Why can two rows with the same ORDER BY value receive the same running total?
2. Why does ROWS produce a different result from RANGE?
3. What is a peer group?
4. What happens when an ordered SUM() has no explicit frame?
5. Why can LAST_VALUE() return the current row's value?
6. How can we calculate the last value of an entire partition?
7. What is the difference between a frame and a partition?
8. What happens when a frame reaches the beginning or end of a partition?
9. Does SUM() include NULL values?
10. Why should a deterministic ordering column be used for ROWS-based calculations?
