# SQL Window Functions – Complete Beginner to Advanced Guide

# Part 4 – Navigation, Value and Distribution Functions

# Chapter 33: Navigation Window Functions

## 33.1 What Are Navigation Functions?

Navigation functions allow us to access values from other rows relative to the current row.

Unlike aggregate functions, which calculate a summary, navigation functions retrieve values associated with preceding or subsequent rows.

They are extremely useful for comparing records over time.

For example:

- What was the previous transaction amount?
- What will be the next transaction amount?
- Did an employee's salary increase?
- What was the previous month's revenue?
- What was the customer's previous purchase?
- How many days passed between consecutive logins?
- Did a user's status change?

The two most important navigation functions are:

1. LAG()
2. LEAD()

---

# Chapter 34: LAG()

## 34.1 What Is LAG()?

LAG() accesses a value from a preceding row within the same window partition.

It allows us to look backward without performing a self-join.

For example, if we are currently examining the fifth transaction, LAG() can retrieve the fourth transaction's amount.

### Syntax

```sql
LAG(expression, offset, default_value)
OVER (
    PARTITION BY column_name
    ORDER BY column_name
)
```

### Components

| Component | Meaning |
|---|---|
| expression | Column or expression whose value we want |
| offset | Number of rows to move backward |
| default_value | Value returned if the requested preceding row does not exist |
| PARTITION BY | Defines the independent groups |
| ORDER BY | Defines the row sequence |

Only the expression is mandatory.

The default offset is 1.

The default value is NULL.

## 34.2 Basic Example

Dataset:

### transactions

| transaction_id | transaction_date | amount |
|---:|---|---:|
| 1 | 2026-01-01 | 100 |
| 2 | 2026-01-02 | 200 |
| 3 | 2026-01-03 | 300 |
| 4 | 2026-01-04 | 400 |
| 5 | 2026-01-05 | 500 |

Query:

```sql
SELECT
    transaction_id,
    transaction_date,
    amount,

    LAG(amount) OVER (
        ORDER BY transaction_date
    ) AS previous_amount

FROM transactions;
```

Output:

| transaction_id | amount | previous_amount |
|---:|---:|---:|
| 1 | 100 | NULL |
| 2 | 200 | 100 |
| 3 | 300 | 200 |
| 4 | 400 | 300 |
| 5 | 500 | 400 |

### How does SQL calculate this?

For the first row:

There is no preceding row.

Therefore:

```text
LAG(amount) = NULL
```

For the second row:

```text
Previous amount = 100
```

For the third row:

```text
Previous amount = 200
```

The process continues.

LAG() retrieves the value from the previous ordered row.

## 34.3 LAG() with Offset

Suppose we want the amount from two transactions earlier.

```sql
SELECT
    transaction_id,
    amount,

    LAG(amount, 2) OVER (
        ORDER BY transaction_id
    ) AS amount_two_transactions_ago

FROM transactions;
```

Output:

| transaction_id | amount | amount_two_transactions_ago |
|---:|---:|---:|
| 1 | 100 | NULL |
| 2 | 200 | NULL |
| 3 | 300 | 100 |
| 4 | 400 | 200 |
| 5 | 500 | 300 |

The offset is 2, so SQL moves two rows backward.

## 34.4 LAG() with a Default Value

```sql
SELECT
    transaction_id,
    amount,

    LAG(amount, 1, 0) OVER (
        ORDER BY transaction_id
    ) AS previous_amount

FROM transactions;
```

Output:

| transaction_id | amount | previous_amount |
|---:|---:|---:|
| 1 | 100 | 0 |
| 2 | 200 | 100 |
| 3 | 300 | 200 |
| 4 | 400 | 300 |
| 5 | 500 | 400 |

For the first row, no previous row exists.

Therefore, the default value 0 is returned.

### Important NULL distinction

Suppose an existing preceding row has an amount of NULL.

LAG() returns that NULL.

It does not automatically substitute the default argument.

The default is used when the requested preceding row does not exist.

If you also want to replace actual NULL values, use COALESCE():

```sql
SELECT
    transaction_id,
    amount,

    COALESCE(
        LAG(amount) OVER (
            ORDER BY transaction_id
        ),
        0
    ) AS previous_amount

FROM transactions;
```

However, this treats both a missing preceding row and an actual NULL value as zero.

Only do this when that is the intended business rule.

## 34.5 LAG() with PARTITION BY

Suppose multiple customers have transactions.

### Dataset

| transaction_id | customer_id | transaction_date | amount |
|---:|---:|---|---:|
| 1 | 101 | Jan 01 | 500 |
| 2 | 101 | Jan 05 | 700 |
| 3 | 101 | Jan 10 | 900 |
| 4 | 102 | Jan 02 | 300 |
| 5 | 102 | Jan 06 | 600 |
| 6 | 102 | Jan 12 | 800 |

Query:

```sql
SELECT
    transaction_id,
    customer_id,
    transaction_date,
    amount,

    LAG(amount) OVER (
        PARTITION BY customer_id
        ORDER BY transaction_date, transaction_id
    ) AS previous_amount

FROM transactions;
```

Output:

| transaction_id | customer_id | amount | previous_amount |
|---:|---:|---:|---:|
| 1 | 101 | 500 | NULL |
| 2 | 101 | 700 | 500 |
| 3 | 101 | 900 | 700 |
| 4 | 102 | 300 | NULL |
| 5 | 102 | 600 | 300 |
| 6 | 102 | 800 | 600 |

### Why?

Because each customer forms an independent partition.

The first transaction of each customer has no preceding transaction within that customer's partition.

This is a very common real-world pattern.

## 34.6 Comparing Current and Previous Salary

```sql
SELECT
    employee_id,
    employee_name,
    salary,

    LAG(salary) OVER (
        ORDER BY employee_id
    ) AS previous_salary

FROM employees;
```

To calculate the difference:

```sql
WITH salary_comparison AS (
    SELECT
        employee_id,
        employee_name,
        salary,

        LAG(salary) OVER (
            ORDER BY employee_id
        ) AS previous_salary

    FROM employees
)

SELECT
    employee_id,
    employee_name,
    salary,
    previous_salary,

    salary - previous_salary AS salary_difference

FROM salary_comparison;
```

Interpretation:

| Difference | Meaning |
|---|---|
| Positive | Salary increased |
| Negative | Salary decreased |
| Zero | Salary remained unchanged |
| NULL | No previous record, or previous salary was NULL |

## 34.7 Detecting Changes Between Consecutive Records

Suppose we track employee job roles over time.

### employee_history

| employee_id | effective_date | job_role |
|---:|---|---|
| 101 | 2026-01-01 | Developer |
| 101 | 2026-02-01 | Developer |
| 101 | 2026-03-01 | Senior Developer |
| 101 | 2026-04-01 | Manager |

Query:

```sql
WITH role_history AS (
    SELECT
        employee_id,
        effective_date,
        job_role,

        LAG(job_role) OVER (
            PARTITION BY employee_id
            ORDER BY effective_date
        ) AS previous_role

    FROM employee_history
)

SELECT
    employee_id,
    effective_date,
    previous_role,
    job_role

FROM role_history
WHERE NOT (job_role <=> previous_role);
```

`<=>` is MySQL's NULL-safe equality operator.

The query identifies rows where the role differs from the preceding role, including NULL transitions.

For portability across databases, a NULL-safe comparison can instead be written using explicit NULL conditions.

Output:

| employee_id | effective_date | previous_role | job_role |
|---:|---|---|---|
| 101 | 2026-01-01 | NULL | Developer |
| 101 | 2026-03-01 | Developer | Senior Developer |
| 101 | 2026-04-01 | Senior Developer | Manager |

The February record is excluded because the job role did not change.

---

# Chapter 35: LEAD()

## 35.1 What Is LEAD()?

LEAD() accesses a value from a subsequent row within the same window partition.

It is the opposite of LAG().

LAG() moves backward.

LEAD() moves forward.

### Syntax

```sql
LEAD(expression, offset, default_value)
OVER (
    PARTITION BY column_name
    ORDER BY column_name
)
```

## 35.2 Basic Example

```sql
SELECT
    transaction_id,
    amount,

    LEAD(amount) OVER (
        ORDER BY transaction_id
    ) AS next_amount

FROM transactions;
```

Output:

| transaction_id | amount | next_amount |
|---:|---:|---:|
| 1 | 100 | 200 |
| 2 | 200 | 300 |
| 3 | 300 | 400 |
| 4 | 400 | 500 |
| 5 | 500 | NULL |

For the final row, no subsequent row exists.

Therefore, LEAD() returns NULL.

## 35.3 LEAD() with Offset

```sql
SELECT
    transaction_id,
    amount,

    LEAD(amount, 2) OVER (
        ORDER BY transaction_id
    ) AS amount_two_rows_ahead

FROM transactions;
```

Output:

| transaction_id | amount | amount_two_rows_ahead |
|---:|---:|---:|
| 1 | 100 | 300 |
| 2 | 200 | 400 |
| 3 | 300 | 500 |
| 4 | 400 | NULL |
| 5 | 500 | NULL |

## 35.4 LEAD() with Default

```sql
SELECT
    transaction_id,
    amount,

    LEAD(amount, 1, 0) OVER (
        ORDER BY transaction_id
    ) AS next_amount

FROM transactions;
```

The final row receives 0 because no next row exists.

## 35.5 LAG() vs LEAD()

| Feature | LAG() | LEAD() |
|---|---|---|
| Direction | Previous row | Next row |
| Offset | Backward | Forward |
| Default behavior | NULL if no preceding row | NULL if no following row |
| Common application | Previous transaction | Next transaction |
| Growth comparison | Current vs previous | Current vs next |
| Time-series analysis | Historical comparison | Future comparison |

---

# Chapter 36: Month-over-Month Growth Using LAG()

Month-over-month growth measures how much a metric has changed compared with the previous month.

### Formula

```text
MoM Growth (%) =
(Current Month Revenue - Previous Month Revenue)
-------------------------------------------------- × 100
             Previous Month Revenue
```

### Dataset: monthly_sales

| month_start | revenue |
|---|---:|
| 2026-01-01 | 10000 |
| 2026-02-01 | 12000 |
| 2026-03-01 | 15000 |
| 2026-04-01 | 13500 |
| 2026-05-01 | 18000 |

Query:

```sql
WITH monthly_comparison AS (
    SELECT
        month_start,
        revenue,

        LAG(revenue) OVER (
            ORDER BY month_start
        ) AS previous_month_revenue

    FROM monthly_sales
)

SELECT
    month_start,
    revenue,
    previous_month_revenue,

    ROUND(
        (revenue - previous_month_revenue)
        * 100.0 / NULLIF(previous_month_revenue, 0),
        2
    ) AS mom_growth_percentage

FROM monthly_comparison;
```

Output:

| month_start | revenue | previous_month_revenue | mom_growth_percentage |
|---|---:|---:|---:|
| Jan | 10000 | NULL | NULL |
| Feb | 12000 | 10000 | 20.00 |
| Mar | 15000 | 12000 | 25.00 |
| Apr | 13500 | 15000 | -10.00 |
| May | 18000 | 13500 | 33.33 |

### Explanation

February:

```text
(12000 - 10000) / 10000 × 100

= 20%
```

April:

```text
(13500 - 15000) / 15000 × 100

= -10%
```

A negative result indicates a decline.

### Why NULLIF()?

If the previous month's revenue is zero, division by zero must be avoided.

```sql
NULLIF(previous_month_revenue, 0)
```

Returns NULL when the previous revenue is zero.

This prevents an invalid division.

### Important business consideration

LAG() retrieves the previous available row, not automatically the previous calendar month.

If March is missing from the dataset, LAG() for April might retrieve February.

For true calendar month-over-month comparisons, construct a complete month series and join the sales data to it, filling missing months according to the business rule.

---

# Chapter 37: Year-over-Year Growth Using LAG()

Year-over-year growth compares a value against the same period in the previous year.

If data is at monthly granularity, a 12-row offset can retrieve the corresponding month from the previous year, provided every month is represented exactly once.

### Formula

```text
YoY Growth (%) =
(Current Period - Previous Year's Same Period)
------------------------------------------------ × 100
       Previous Year's Same Period
```

### Query

```sql
WITH yearly_comparison AS (
    SELECT
        month_start,
        revenue,

        LAG(revenue, 12) OVER (
            ORDER BY month_start
        ) AS previous_year_revenue

    FROM monthly_sales
)

SELECT
    month_start,
    revenue,
    previous_year_revenue,

    ROUND(
        (revenue - previous_year_revenue)
        * 100.0 / NULLIF(previous_year_revenue, 0),
        2
    ) AS yoy_growth_percentage

FROM yearly_comparison;
```

This assumes the dataset contains a complete monthly series.

If months are missing or the data is irregular, use a calendar join or an explicit self-join on matching month and year relationships instead of relying blindly on an offset of 12.

---

# Chapter 38: FIRST_VALUE()

## 38.1 What Is FIRST_VALUE()?

FIRST_VALUE() returns the expression value from the first row of the current window frame.

It is useful for identifying:

- The first transaction amount.
- The initial salary.
- The first purchase date.
- The starting account balance.
- The first recorded status.

### Syntax

```sql
FIRST_VALUE(expression) OVER (
    PARTITION BY column_name
    ORDER BY column_name
    frame_definition
)
```

## 38.2 Basic Example

```sql
SELECT
    transaction_id,
    transaction_date,
    amount,

    FIRST_VALUE(amount) OVER (
        ORDER BY transaction_date, transaction_id
    ) AS first_transaction_amount

FROM transactions;
```

Output:

| transaction_id | amount | first_transaction_amount |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 100 |
| 3 | 300 | 100 |
| 4 | 400 | 100 |
| 5 | 500 | 100 |

The first row in the frame is the earliest transaction.

## 38.3 FIRST_VALUE() Per Customer

```sql
SELECT
    customer_id,
    transaction_date,
    amount,

    FIRST_VALUE(amount) OVER (
        PARTITION BY customer_id
        ORDER BY transaction_date, transaction_id
    ) AS first_purchase_amount

FROM transactions;
```

Every transaction of a customer receives that customer's first purchase amount.

### Important frame consideration

With the common default frame for an ordered window, the first row generally remains the first row of the growing frame.

Therefore, FIRST_VALUE() frequently returns the expected initial value without needing a full-partition frame.

However, always understand the frame being used.

---

# Chapter 39: LAST_VALUE()

## 39.1 What Is LAST_VALUE()?

LAST_VALUE() returns the expression value from the last row of the current frame.

It is useful for:

- Final transaction amount.
- Latest salary.
- Final recorded status.
- Most recent account balance.
- Last purchase date.

### Syntax

```sql
LAST_VALUE(expression) OVER (
    PARTITION BY column_name
    ORDER BY column_name
    frame_definition
)
```

## 39.2 The Common LAST_VALUE() Mistake

Incorrect for retrieving the final value of the entire partition:

```sql
SELECT
    transaction_id,
    amount,

    LAST_VALUE(amount) OVER (
        ORDER BY transaction_id
    ) AS last_amount

FROM transactions;
```

With unique transaction IDs and the usual default ordered frame in MySQL, the output is:

| transaction_id | amount | last_amount |
|---:|---:|---:|
| 1 | 100 | 100 |
| 2 | 200 | 200 |
| 3 | 300 | 300 |
| 4 | 400 | 400 |
| 5 | 500 | 500 |

Why?

Because the frame ends at the current row's peer group.

It does not necessarily extend to the final row of the partition.

## 39.3 Correct Way

```sql
SELECT
    transaction_id,
    amount,

    LAST_VALUE(amount) OVER (
        ORDER BY transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS final_amount

FROM transactions;
```

Output:

| transaction_id | amount | final_amount |
|---:|---:|---:|
| 1 | 100 | 500 |
| 2 | 200 | 500 |
| 3 | 300 | 500 |
| 4 | 400 | 500 |
| 5 | 500 | 500 |

The entire partition is included in the frame.

Therefore, the final value is 500 for every row.

---

# Chapter 40: NTH_VALUE()

## 40.1 What Is NTH_VALUE()?

NTH_VALUE() retrieves the value of an expression from the Nth row of the current window frame.

For example:

```sql
NTH_VALUE(salary, 2)
```

Returns the value from the second row of the frame.

The N argument is one-based.

That means:

```text
NTH_VALUE(column, 1) → First row
NTH_VALUE(column, 2) → Second row
NTH_VALUE(column, 3) → Third row
```

### Syntax

```sql
NTH_VALUE(expression, N) OVER (
    PARTITION BY column_name
    ORDER BY column_name
    frame_definition
)
```

## 40.2 Example: Retrieve the Second Transaction Amount

```sql
SELECT
    transaction_id,
    amount,

    NTH_VALUE(amount, 2) OVER (
        ORDER BY transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS second_transaction_amount

FROM transactions;
```

Output:

| transaction_id | amount | second_transaction_amount |
|---:|---:|---:|
| 1 | 100 | 200 |
| 2 | 200 | 200 |
| 3 | 300 | 200 |
| 4 | 400 | 200 |
| 5 | 500 | 200 |

The second row of the complete partition has an amount of 200.

### Why specify the complete frame?

Without an explicit full-partition frame, early rows may not yet have the Nth row inside their current frame.

For an ordered growing frame, the second row is unavailable in the frame of the first row.

Therefore, the result can initially be NULL.

For consistent retrieval of the Nth value from the complete partition, specify:

```sql
ROWS BETWEEN UNBOUNDED PRECEDING
AND UNBOUNDED FOLLOWING
```

---

# Chapter 41: FIRST_VALUE vs LAST_VALUE vs NTH_VALUE

| Feature | FIRST_VALUE | LAST_VALUE | NTH_VALUE |
|---|---|---|---|
| Purpose | First value in frame | Last value in frame | Nth value in frame |
| Position | First | Last | Specified position |
| Requires ORDER BY for meaningful positional analysis | Yes | Yes | Yes |
| Frame affects result | Yes | Yes | Yes |
| Typical use | Initial value | Final value | Specific positional value |
| Common mistake | Incorrect ordering | Assuming entire partition is used | Forgetting that the Nth row may not yet be in the frame |

---

# Chapter 42: Distribution Functions

## 42.1 What Are Distribution Functions?

Distribution functions describe the relative position of rows within an ordered dataset.

They are useful in statistical analysis, benchmarking, performance comparisons and customer segmentation.

The major distribution functions are:

1. PERCENT_RANK()
2. CUME_DIST()
3. NTILE()

NTILE() was introduced in the ranking chapter. Here, we will examine its distribution meaning.

---

# Chapter 43: PERCENT_RANK()

## 43.1 What Is PERCENT_RANK()?

PERCENT_RANK() calculates the relative rank of a row within a partition.

Its value ranges from 0 to 1.

### Formula

\[
PERCENT\_RANK = \frac{RANK - 1}{N - 1}
\]

Where:

- RANK = rank of the current row.
- N = number of rows in the partition.

For a partition containing one row, the result is 0.

### Syntax

```sql
PERCENT_RANK() OVER (
    PARTITION BY column_name
    ORDER BY column_name
)
```

## 43.2 Example

Dataset:

| employee_name | salary |
|---|---:|
| Amit | 90000 |
| Rahul | 80000 |
| Priya | 70000 |
| Neha | 60000 |
| Karan | 50000 |

Query:

```sql
SELECT
    employee_name,
    salary,

    PERCENT_RANK() OVER (
        ORDER BY salary DESC
    ) AS percent_rank

FROM employees;
```

Output:

| employee_name | salary | rank | percent_rank |
|---|---:|---:|---:|
| Amit | 90000 | 1 | 0.00 |
| Rahul | 80000 | 2 | 0.25 |
| Priya | 70000 | 3 | 0.50 |
| Neha | 60000 | 4 | 0.75 |
| Karan | 50000 | 5 | 1.00 |

### Calculation

There are five rows.

For Rahul:

\[
\frac{2-1}{5-1} = \frac{1}{4} = 0.25
\]

For Priya:

\[
\frac{3-1}{5-1} = \frac{2}{4} = 0.50
\]

For Karan:

\[
\frac{5-1}{5-1} = 1.00
\]

### Interpretation

| Value | Meaning |
|---|---|
| 0.00 | First-ranked position |
| 0.25 | Relative rank of 25% |
| 0.50 | Relative rank of 50% |
| 0.75 | Relative rank of 75% |
| 1.00 | Last-ranked position |

**Important:** PERCENT_RANK() is based on RANK(), not ROW_NUMBER(). Ties can therefore produce identical percentage ranks.

---

# Chapter 44: CUME_DIST()

## 44.1 What Is CUME_DIST()?

CUME_DIST() calculates the cumulative distribution of a row within its partition.

It represents the proportion of rows whose ordering values are less than or equal to the current row's ordering value, according to the specified ordering direction.

### Formula

\[
CUME\_DIST = \frac{\text{Number of rows preceding or tied with current row}}{N}
\]

Where:

- N = total rows in the partition.
- The numerator includes the current row and its peers.

### Syntax

```sql
CUME_DIST() OVER (
    PARTITION BY column_name
    ORDER BY column_name
)
```

## 44.2 Example

Dataset:

| employee_name | salary |
|---|---:|
| Amit | 90000 |
| Rahul | 80000 |
| Priya | 70000 |
| Neha | 60000 |
| Karan | 50000 |

Query:

```sql
SELECT
    employee_name,
    salary,

    CUME_DIST() OVER (
        ORDER BY salary DESC
    ) AS cumulative_distribution

FROM employees;
```

Output:

| employee_name | salary | cume_dist |
|---|---:|---:|
| Amit | 90000 | 0.20 |
| Rahul | 80000 | 0.40 |
| Priya | 70000 | 0.60 |
| Neha | 60000 | 0.80 |
| Karan | 50000 | 1.00 |

### Calculation

There are five rows.

For the first row:

\[
\frac{1}{5} = 0.20
\]

For the third row:

\[
\frac{3}{5} = 0.60
\]

For the final row:

\[
\frac{5}{5} = 1.00
\]

## 44.3 Understanding Ties in CUME_DIST()

Dataset:

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

    CUME_DIST() OVER (
        ORDER BY salary DESC
    ) AS cumulative_distribution

FROM employees;
```

Output:

| employee_name | salary | cume_dist |
|---|---:|---:|
| Amit | 90000 | 0.20 |
| Rahul | 80000 | 0.60 |
| Priya | 80000 | 0.60 |
| Neha | 70000 | 0.80 |
| Karan | 60000 | 1.00 |

Why do Rahul and Priya both receive 0.60?

Because the cumulative distribution includes all rows in the current peer group.

There are three rows with salary greater than or equal to 80000:

```text
Amit
Rahul
Priya
```

Therefore:

\[
\frac{3}{5} = 0.60
\]

---

# Chapter 45: PERCENT_RANK vs CUME_DIST vs NTILE

| Feature | PERCENT_RANK | CUME_DIST | NTILE |
|---|---|---|---|
| Purpose | Relative ranking | Cumulative distribution | Divide rows into buckets |
| Output | 0 to 1 | Greater than 0 and up to 1 | Integer bucket number |
| Formula | (RANK - 1) / (N - 1) | Rows through current peer group / N | Approximately equal row groups |
| Ties | Same rank can produce same result | Peers receive same cumulative result | Peers may be assigned different buckets |
| First value | 0 | At least 1/N | 1 |
| Final value | 1, for a nonempty partition | 1 | Last bucket number may be less than requested if there are too few rows |
| Application | Relative ranking | Cumulative distribution | Quartiles and segmentation |

### Important conceptual difference

PERCENT_RANK() answers:

"Where does this row stand relative to the ranking positions?"

CUME_DIST() answers:

"What proportion of rows is at or before this row in the specified ordering?"

NTILE() answers:

"Which approximately equal-sized bucket does this row belong to?"

---

# Chapter 46: Function Compatibility and Important Restrictions

| Function | MySQL 8.0 | PostgreSQL | SQL Server | Oracle | Snowflake | BigQuery |
|---|---|---|---|---|---|---|
| LAG() | Yes | Yes | Yes | Yes | Yes | Yes |
| LEAD() | Yes | Yes | Yes | Yes | Yes | Yes |
| FIRST_VALUE() | Yes | Yes | Yes | Yes | Yes | Yes |
| LAST_VALUE() | Yes | Yes | Yes | Yes | Yes | Yes |
| NTH_VALUE() | Yes | Yes | Yes | Yes | Yes | Yes |
| PERCENT_RANK() | Yes | Yes | Yes | Yes | Yes | Yes |
| CUME_DIST() | Yes | Yes | Yes | Yes | Yes | Yes |
| NTILE() | Yes | Yes | Yes | Yes | Yes | Yes |

### Important NULL handling differences

Some database systems provide options such as:

```sql
IGNORE NULLS
```

or:

```sql
RESPECT NULLS
```

for certain navigation and value functions.

These options are not universally supported.

For example, MySQL 8.0 does not provide a general IGNORE NULLS option for these functions.

Its supported behavior should not be confused with Oracle or other systems that provide additional NULL-handling syntax.

---

# Part 4 – Complete Function Reference

| Category | Function | Purpose |
|---|---|---|
| Navigation | LAG() | Previous row value |
| Navigation | LEAD() | Next row value |
| Value | FIRST_VALUE() | First value in frame |
| Value | LAST_VALUE() | Last value in frame |
| Value | NTH_VALUE() | Nth value in frame |
| Distribution | PERCENT_RANK() | Relative ranking |
| Distribution | CUME_DIST() | Cumulative distribution |
| Ranking | NTILE() | Divide rows into buckets |

## Essential formulas

**Month-over-month growth**

```text
(Current Month - Previous Month)
--------------------------------- × 100
       Previous Month
```

**Year-over-year growth**

```text
(Current Period - Previous Year Period)
---------------------------------------- × 100
          Previous Year Period
```

**Percentage contribution**

```text
Individual Value
---------------- × 100
  Group Total
```

**Cumulative percentage**

```text
Cumulative Total
---------------- × 100
 Overall Total
```

**PERCENT_RANK**

```text
(RANK - 1) / (N - 1)
```

**CUME_DIST**

```text
Rows up to and including current peer group
--------------------------------------------
                Total Rows
```

## Part 4 – Interview Checkpoint

You should now be able to:

1. Retrieve the previous row using LAG().
2. Retrieve the next row using LEAD().
3. Retrieve values two or more rows backward or forward.
4. Understand the difference between a missing row and an actual NULL value.
5. Calculate month-over-month growth.
6. Calculate year-over-year growth.
7. Detect changes between consecutive records.
8. Retrieve the first value of a partition.
9. Retrieve the last value of a complete partition.
10. Understand why LAST_VALUE() can produce unexpected results.
11. Retrieve the Nth value of a window frame.
12. Explain the formulas for PERCENT_RANK() and CUME_DIST().
13. Distinguish between distribution and bucket-based ranking.
