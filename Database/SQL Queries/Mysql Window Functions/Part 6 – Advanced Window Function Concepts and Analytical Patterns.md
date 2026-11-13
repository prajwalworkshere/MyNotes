# SQL Window Functions – Complete Beginner to Advanced Guide

# Part 6 – Advanced Window Function Concepts and Analytical Patterns

# Chapter 62: Gaps and Islands Problems

## 62.1 What Are Gaps and Islands?

Gaps and Islands is a common SQL problem-solving pattern used to identify:

- Consecutive dates.
- Consecutive login days.
- Consecutive transaction periods.
- Continuous employment periods.
- Consecutive numerical sequences.
- Periods of uninterrupted activity.
- Missing dates or missing sequence numbers.

Before understanding the solution, understand two terms.

**Island:** A group of consecutive records that satisfy a particular condition.

**Gap:** A missing value or break between consecutive records.

### Example

Suppose we have user login records:

| user_id | login_date |
|---:|---|
| 101 | 2026-01-01 |
| 101 | 2026-01-02 |
| 101 | 2026-01-03 |
| 101 | 2026-01-07 |
| 101 | 2026-01-08 |
| 101 | 2026-01-12 |

Visual representation:

```text
Jan 01 ─ Jan 02 ─ Jan 03

Gap: Jan 04, Jan 05, Jan 06

Jan 07 ─ Jan 08

Gap: Jan 09, Jan 10, Jan 11

Jan 12
```

There are three islands:

| Island | Start date | End date | Consecutive days |
|---|---|---|---:|
| 1 | Jan 01 | Jan 03 | 3 |
| 2 | Jan 07 | Jan 08 | 2 |
| 3 | Jan 12 | Jan 12 | 1 |

Our goal is to identify these groups automatically.

## 62.2 Understanding the Main Technique

One of the most common techniques uses:

1. ROW_NUMBER()
2. Date arithmetic
3. GROUP BY

The fundamental observation is:

If dates are consecutive, subtracting the row number from each date produces the same date offset.

Consider:

| login_date | row_number | login_date minus row_number days |
|---|---:|---|
| Jan 01 | 1 | Dec 31 |
| Jan 02 | 2 | Dec 31 |
| Jan 03 | 3 | Dec 31 |
| Jan 07 | 4 | Jan 03 |
| Jan 08 | 5 | Jan 03 |
| Jan 12 | 6 | Jan 06 |

Observe:

- First three records produce Dec 31.
- Next two records produce Jan 03.
- Final record produces Jan 06.

The calculated value is constant inside each island.

Therefore, it can be used as a grouping key.

## 62.3 Complete SQL Solution: Identify Consecutive Login Islands

### Input table

```sql
CREATE TABLE user_logins (
    user_id INT,
    login_date DATE
);

INSERT INTO user_logins VALUES
(101, '2026-01-01'),
(101, '2026-01-02'),
(101, '2026-01-03'),
(101, '2026-01-07'),
(101, '2026-01-08'),
(101, '2026-01-12');
```

### Query

```sql
WITH numbered_logins AS (
    SELECT
        user_id,
        login_date,

        ROW_NUMBER() OVER (
            PARTITION BY user_id
            ORDER BY login_date
        ) AS rn

    FROM user_logins
),

grouped_logins AS (
    SELECT
        user_id,
        login_date,
        DATE_SUB(
            login_date,
            INTERVAL rn DAY
        ) AS island_group

    FROM numbered_logins
)

SELECT
    user_id,
    MIN(login_date) AS island_start,
    MAX(login_date) AS island_end,
    COUNT(*) AS consecutive_days

FROM grouped_logins

GROUP BY
    user_id,
    island_group

ORDER BY
    user_id,
    island_start;
```

### Expected output

| user_id | island_start | island_end | consecutive_days |
|---:|---|---|---:|
| 101 | 2026-01-01 | 2026-01-03 | 3 |
| 101 | 2026-01-07 | 2026-01-08 | 2 |
| 101 | 2026-01-12 | 2026-01-12 | 1 |

### Detailed execution

**Step 1: Assign row numbers**

```sql
ROW_NUMBER() OVER (
    PARTITION BY user_id
    ORDER BY login_date
)
```

Output:

| login_date | rn |
|---|---:|
| Jan 01 | 1 |
| Jan 02 | 2 |
| Jan 03 | 3 |
| Jan 07 | 4 |
| Jan 08 | 5 |
| Jan 12 | 6 |

**Step 2: Calculate the grouping key**

```sql
DATE_SUB(login_date, INTERVAL rn DAY)
```

Output:

| login_date | rn | island_group |
|---|---:|---|
| Jan 01 | 1 | Dec 31 |
| Jan 02 | 2 | Dec 31 |
| Jan 03 | 3 | Dec 31 |
| Jan 07 | 4 | Jan 03 |
| Jan 08 | 5 | Jan 03 |
| Jan 12 | 6 | Jan 06 |

**Step 3: Group records**

```sql
GROUP BY user_id, island_group
```

All records belonging to the same island are grouped together.

**Step 4: Calculate boundaries**

```sql
MIN(login_date)
MAX(login_date)
COUNT(*)
```

These produce the island's starting date, ending date and duration.

### Important limitation

The technique assumes one record per user per calendar day.

If duplicate login records exist, first deduplicate the user-date combinations.

For example:

```sql
WITH distinct_logins AS (
    SELECT DISTINCT
        user_id,
        login_date
    FROM user_logins
)
SELECT
    user_id,
    login_date,
    ROW_NUMBER() OVER (
        PARTITION BY user_id
        ORDER BY login_date
    ) AS rn
FROM distinct_logins;
```

Also, for non-daily sequences, the date arithmetic must be adjusted to the required interval.

---

# Chapter 63: Consecutive Number Sequences

Gaps and Islands is not limited to dates.

It can also identify consecutive numeric values.

### Example dataset

| number_id |
|---:|
| 1 |
| 2 |
| 3 |
| 7 |
| 8 |
| 10 |
| 11 |
| 12 |

Expected islands:

| Start number | End number | Count |
|---:|---:|---:|
| 1 | 3 | 3 |
| 7 | 8 | 2 |
| 10 | 12 | 3 |

### Query

```sql
WITH numbered_values AS (
    SELECT
        number_id,

        ROW_NUMBER() OVER (
            ORDER BY number_id
        ) AS rn

    FROM number_sequence
),

grouped_values AS (
    SELECT
        number_id,
        number_id - rn AS island_group

    FROM numbered_values
)

SELECT
    MIN(number_id) AS start_number,
    MAX(number_id) AS end_number,
    COUNT(*) AS sequence_length

FROM grouped_values

GROUP BY island_group

ORDER BY start_number;
```

### Why does this work?

Consider the first island:

| number_id | rn | number_id - rn |
|---:|---:|---:|
| 1 | 1 | 0 |
| 2 | 2 | 0 |
| 3 | 3 | 0 |

All three produce 0.

The next island:

| number_id | rn | number_id - rn |
|---:|---:|---:|
| 7 | 4 | 3 |
| 8 | 5 | 3 |

Both produce 3.

The grouping key remains constant within each consecutive sequence.

---

# Chapter 64: Finding Gaps in Dates

Sometimes we do not want to identify islands. We want to identify missing dates.

Suppose a user should log in every day.

### Dataset

| login_date |
|---|
| 2026-01-01 |
| 2026-01-02 |
| 2026-01-03 |
| 2026-01-07 |
| 2026-01-08 |

We want to identify dates missing between the first and last recorded dates.

There are two useful approaches.

## 64.1 Approach 1: Compare Consecutive Rows

```sql
WITH login_analysis AS (
    SELECT
        login_date,

        LAG(login_date) OVER (
            ORDER BY login_date
        ) AS previous_date

    FROM user_logins
)

SELECT
    previous_date,
    login_date,
    DATEDIFF(login_date, previous_date) AS gap_days

FROM login_analysis

WHERE DATEDIFF(login_date, previous_date) > 1;
```

Output:

| previous_date | login_date | gap_days |
|---|---|---:|
| 2026-01-03 | 2026-01-07 | 4 |

This identifies a gap between January 3 and January 7.

Missing dates:

```text
2026-01-04
2026-01-05
2026-01-06
```

However, this query identifies the gap boundaries, not every missing date.

## 64.2 Approach 2: Generate a Calendar and Find Missing Dates

In MySQL 8.0, a recursive CTE can generate a date series.

```sql
WITH RECURSIVE calendar AS (
    SELECT DATE('2026-01-01') AS calendar_date

    UNION ALL

    SELECT DATE_ADD(calendar_date, INTERVAL 1 DAY)
    FROM calendar
    WHERE calendar_date < '2026-01-08'
)

SELECT
    c.calendar_date AS missing_date

FROM calendar c

LEFT JOIN (
    SELECT DISTINCT login_date
    FROM user_logins
) l
    ON c.calendar_date = l.login_date

WHERE l.login_date IS NULL

ORDER BY c.calendar_date;
```

Output:

| missing_date |
|---|
| 2026-01-04 |
| 2026-01-05 |
| 2026-01-06 |

### Why use a calendar table?

A calendar table is extremely useful in reporting because it allows you to represent dates even when no transactions occurred.

It is commonly used for:

- Missing daily sales.
- Monthly reporting.
- Attendance analysis.
- Business-day calculations.
- Financial time series.
- Continuous date-based dashboards.

A permanent calendar table is generally preferable for large production reporting systems.

---

# Chapter 65: Sessionization

## 65.1 What Is Sessionization?

Sessionization means grouping user activities into sessions based on the time difference between consecutive events.

It is widely used in:

- Website analytics.
- Mobile application analytics.
- User behavior tracking.
- E-commerce clickstream analysis.
- Marketing analytics.

For example, suppose a website defines a new session whenever a user is inactive for more than 30 minutes.

### Example

| user_id | event_time | event |
|---:|---|---|
| 101 | 10:00 | Page view |
| 101 | 10:05 | Product view |
| 101 | 10:20 | Add to cart |
| 101 | 11:00 | Checkout |
| 101 | 11:10 | Payment |
| 101 | 12:00 | Logout |

Session rule:

If the difference between consecutive events is greater than 30 minutes, start a new session.

Expected sessions:

| Event | Time | Session |
|---|---|---:|
| Page view | 10:00 | 1 |
| Product view | 10:05 | 1 |
| Add to cart | 10:20 | 1 |
| Checkout | 11:00 | 2 |
| Payment | 11:10 | 2 |
| Logout | 12:00 | 3 |

## 65.2 Step 1: Retrieve Previous Event Time

```sql
SELECT
    user_id,
    event_time,
    event,

    LAG(event_time) OVER (
        PARTITION BY user_id
        ORDER BY event_time
    ) AS previous_event_time

FROM user_events;
```

Output:

| event_time | previous_event_time |
|---|---|
| 10:00 | NULL |
| 10:05 | 10:00 |
| 10:20 | 10:05 |
| 11:00 | 10:20 |
| 11:10 | 11:00 |
| 12:00 | 11:10 |

## 65.3 Step 2: Identify New Session Boundaries

A new session begins when:

- There is no preceding event.
- The time difference exceeds 30 minutes.

```sql
WITH event_comparison AS (
    SELECT
        user_id,
        event_time,
        event,

        LAG(event_time) OVER (
            PARTITION BY user_id
            ORDER BY event_time
        ) AS previous_event_time

    FROM user_events
)

SELECT
    user_id,
    event_time,
    event,
    previous_event_time,

    CASE
        WHEN previous_event_time IS NULL THEN 1
        WHEN TIMESTAMPDIFF(
            MINUTE,
            previous_event_time,
            event_time
        ) > 30 THEN 1
        ELSE 0
    END AS new_session_flag

FROM event_comparison;
```

Output:

| event_time | previous_event_time | new_session_flag |
|---|---|---:|
| 10:00 | NULL | 1 |
| 10:05 | 10:00 | 0 |
| 10:20 | 10:05 | 0 |
| 11:00 | 10:20 | 1 |
| 11:10 | 11:00 | 0 |
| 12:00 | 11:10 | 1 |

## 65.4 Step 3: Generate Session Numbers

We now calculate a running sum of the new-session flags.

```sql
WITH event_comparison AS (
    SELECT
        user_id,
        event_time,
        event,

        LAG(event_time) OVER (
            PARTITION BY user_id
            ORDER BY event_time
        ) AS previous_event_time

    FROM user_events
),

session_flags AS (
    SELECT
        user_id,
        event_time,
        event,

        CASE
            WHEN previous_event_time IS NULL THEN 1
            WHEN TIMESTAMPDIFF(
                MINUTE,
                previous_event_time,
                event_time
            ) > 30 THEN 1
            ELSE 0
        END AS new_session_flag

    FROM event_comparison
)

SELECT
    user_id,
    event_time,
    event,

    SUM(new_session_flag) OVER (
        PARTITION BY user_id
        ORDER BY event_time
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS session_number

FROM session_flags

ORDER BY user_id, event_time;
```

Final output:

| event_time | event | session_number |
|---|---|---:|
| 10:00 | Page view | 1 |
| 10:05 | Product view | 1 |
| 10:20 | Add to cart | 1 |
| 11:00 | Checkout | 2 |
| 11:10 | Payment | 2 |
| 12:00 | Logout | 3 |

### Why does this work?

The new-session flag is:

```text
1, 0, 0, 1, 0, 1
```

The cumulative sum is:

```text
1, 1, 1, 2, 2, 3
```

Every event receives the session number to which it belongs.

### Important sessionization edge cases

1. Use a unique event ID as an additional ordering column when timestamps can tie.
2. Deduplicate events if repeated ingestion can create duplicate records.
3. Decide whether exactly 30 minutes starts a new session.
4. Partition by user or by the actual session identity requirement.
5. Handle late-arriving events carefully.
6. Consider timezone normalization.
7. Use the business-defined inactivity threshold rather than assuming 30 minutes.

---

# Chapter 66: Consecutive Purchase Analysis

Suppose we want to identify customers who purchased on consecutive days.

### Dataset

| customer_id | purchase_date |
|---:|---|
| 101 | Jan 01 |
| 101 | Jan 02 |
| 101 | Jan 03 |
| 101 | Jan 10 |
| 102 | Jan 05 |
| 102 | Jan 06 |
| 102 | Jan 09 |

### Query

```sql
WITH purchase_history AS (
    SELECT DISTINCT
        customer_id,
        purchase_date
    FROM purchases
),

purchase_comparison AS (
    SELECT
        customer_id,
        purchase_date,

        LAG(purchase_date) OVER (
            PARTITION BY customer_id
            ORDER BY purchase_date
        ) AS previous_purchase_date

    FROM purchase_history
)

SELECT
    customer_id,
    previous_purchase_date,
    purchase_date

FROM purchase_comparison

WHERE DATEDIFF(
    purchase_date,
    previous_purchase_date
) = 1;
```

Expected output:

| customer_id | previous_purchase_date | purchase_date |
|---:|---|---|
| 101 | Jan 01 | Jan 02 |
| 101 | Jan 02 | Jan 03 |
| 102 | Jan 05 | Jan 06 |

This identifies consecutive-day purchase pairs.

### Important distinction

This query identifies pairs of consecutive purchases.

It does not return complete consecutive purchase islands.

For complete streaks, use the Gaps and Islands method from Chapter 62.

---

# Chapter 67: Cumulative Distinct Counts

## 67.1 What Is a Cumulative Distinct Count?

A cumulative distinct count calculates the number of unique values encountered up to the current row.

Example:

| transaction_id | customer_id | transaction_date |
|---:|---:|---|
| 1 | 101 | Jan 01 |
| 2 | 102 | Jan 02 |
| 3 | 101 | Jan 03 |
| 4 | 103 | Jan 04 |
| 5 | 102 | Jan 05 |
| 6 | 104 | Jan 06 |

We want to count how many unique customers have appeared up to each transaction.

Expected output:

| transaction_id | customer_id | cumulative_unique_customers |
|---:|---:|---:|
| 1 | 101 | 1 |
| 2 | 102 | 2 |
| 3 | 101 | 2 |
| 4 | 103 | 3 |
| 5 | 102 | 3 |
| 6 | 104 | 4 |

## 67.2 Important Limitation of COUNT(DISTINCT) OVER()

Not every database allows:

```sql
COUNT(DISTINCT customer_id) OVER (...)
```

MySQL 8.0 does not support DISTINCT in window-function aggregate calls.

Therefore, we need a workaround.

## 67.3 Solution: Mark First Occurrence and Use Running SUM()

The logic:

1. Identify the first occurrence of each customer.
2. Assign 1 to the first occurrence.
3. Assign 0 to subsequent occurrences.
4. Calculate a running sum of those flags.

### Query

```sql
WITH customer_occurrences AS (
    SELECT
        transaction_id,
        customer_id,
        transaction_date,

        ROW_NUMBER() OVER (
            PARTITION BY customer_id
            ORDER BY transaction_date, transaction_id
        ) AS customer_occurrence

    FROM transactions
),

first_occurrence_flags AS (
    SELECT
        transaction_id,
        customer_id,
        transaction_date,

        CASE
            WHEN customer_occurrence = 1 THEN 1
            ELSE 0
        END AS new_customer_flag

    FROM customer_occurrences
)

SELECT
    transaction_id,
    customer_id,
    transaction_date,

    SUM(new_customer_flag) OVER (
        ORDER BY transaction_date, transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS cumulative_unique_customers

FROM first_occurrence_flags

ORDER BY transaction_date, transaction_id;
```

### Explanation

ROW_NUMBER() is partitioned by customer_id.

For each customer:

- First appearance receives 1.
- Later appearances receive 2, 3, etc.

The CASE expression converts the first occurrence into a flag.

The running SUM() counts the first occurrences encountered so far.

### Important limitation

This calculates cumulative distinct customers across the complete result.

For a cumulative distinct count per department, country or another dimension, the first-occurrence definition and cumulative window must be aligned with the required partition.

Also, cumulative distinct counts are different from counting distinct customers independently within a fixed calendar month.

---

# Chapter 68: Identifying Changes in Historical Data

Historical data frequently stores multiple versions of the same entity.

Examples:

- Employee salary history.
- Customer address history.
- Product price history.
- Account status history.
- Subscription plan history.

Window functions help identify changes between consecutive versions.

## 68.1 Example: Employee Salary History

### salary_history

| employee_id | effective_date | salary |
|---:|---|---:|
| 101 | Jan 01 | 50000 |
| 101 | Feb 01 | 50000 |
| 101 | Mar 01 | 55000 |
| 101 | Apr 01 | 60000 |
| 102 | Jan 01 | 45000 |
| 102 | Feb 01 | 48000 |

### Query

```sql
WITH salary_comparison AS (
    SELECT
        employee_id,
        effective_date,
        salary,

        LAG(salary) OVER (
            PARTITION BY employee_id
            ORDER BY effective_date
        ) AS previous_salary

    FROM salary_history
)

SELECT
    employee_id,
    effective_date,
    previous_salary,
    salary AS current_salary,

    salary - previous_salary AS salary_change

FROM salary_comparison

WHERE previous_salary IS NULL
   OR salary <> previous_salary;
```

Expected output:

| employee_id | effective_date | previous_salary | current_salary | salary_change |
|---:|---|---:|---:|---:|
| 101 | Jan 01 | NULL | 50000 | NULL |
| 101 | Mar 01 | 50000 | 55000 | 5000 |
| 101 | Apr 01 | 55000 | 60000 | 5000 |
| 102 | Jan 01 | NULL | 45000 | NULL |
| 102 | Feb 01 | 45000 | 48000 | 3000 |

The February 1 record for employee 101 is excluded because the salary did not change.

### Important NULL caveat

If salary itself can be NULL, use a NULL-safe comparison.

For MySQL:

```sql
WHERE NOT (salary <=> previous_salary)
```

This detects any difference, including transitions between NULL and non-NULL values.

## 68.2 Detect Changes Across Multiple Columns

Suppose we track:

- Department.
- Salary.
- Job title.

We want to identify any change between consecutive records.

```sql
WITH employee_changes AS (
    SELECT
        employee_id,
        effective_date,
        department,
        salary,
        job_title,

        LAG(department) OVER (
            PARTITION BY employee_id
            ORDER BY effective_date
        ) AS previous_department,

        LAG(salary) OVER (
            PARTITION BY employee_id
            ORDER BY effective_date
        ) AS previous_salary,

        LAG(job_title) OVER (
            PARTITION BY employee_id
            ORDER BY effective_date
        ) AS previous_job_title

    FROM employee_history
)

SELECT
    employee_id,
    effective_date,
    department,
    salary,
    job_title

FROM employee_changes

WHERE NOT (
    department <=> previous_department
)
OR NOT (
    salary <=> previous_salary
)
OR NOT (
    job_title <=> previous_job_title
);
```

This detects a change in at least one tracked attribute.

## 68.3 Identify the Type of Change

```sql
WITH employee_changes AS (
    SELECT
        employee_id,
        effective_date,
        department,
        salary,

        LAG(department) OVER (
            PARTITION BY employee_id
            ORDER BY effective_date
        ) AS previous_department,

        LAG(salary) OVER (
            PARTITION BY employee_id
            ORDER BY effective_date
        ) AS previous_salary

    FROM employee_history
)

SELECT
    employee_id,
    effective_date,

    CASE
        WHEN NOT (department <=> previous_department)
         AND NOT (salary <=> previous_salary)
            THEN 'Department and Salary Changed'

        WHEN NOT (department <=> previous_department)
            THEN 'Department Changed'

        WHEN NOT (salary <=> previous_salary)
            THEN 'Salary Changed'

        ELSE 'No Change'
    END AS change_type

FROM employee_changes;
```

This technique is useful for audit reports and historical change detection.

---

# Chapter 69: Running Account Balances

## 69.1 What Is a Running Account Balance?

A running account balance calculates the account balance after each transaction.

Suppose:

- Deposits are positive.
- Withdrawals are negative.
- Opening balance is 1000.

### Transactions

| transaction_id | account_id | transaction_date | amount |
|---:|---:|---|---:|
| 1 | 101 | Jan 01 | 500 |
| 2 | 101 | Jan 02 | -200 |
| 3 | 101 | Jan 03 | 300 |
| 4 | 101 | Jan 04 | -100 |
| 5 | 101 | Jan 05 | 400 |

### Query

```sql
SELECT
    transaction_id,
    account_id,
    transaction_date,
    amount,

    1000 + SUM(amount) OVER (
        PARTITION BY account_id
        ORDER BY transaction_date, transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS running_balance

FROM account_transactions;
```

Output:

| transaction_id | amount | running_balance |
|---:|---:|---:|
| 1 | 500 | 1500 |
| 2 | -200 | 1300 |
| 3 | 300 | 1600 |
| 4 | -100 | 1500 |
| 5 | 400 | 1900 |

### Detailed logic

Initial balance:

```text
1000
```

Transaction 1:

```text
1000 + 500 = 1500
```

Transaction 2:

```text
1500 - 200 = 1300
```

Transaction 3:

```text
1300 + 300 = 1600
```

Transaction 4:

```text
1600 - 100 = 1500
```

Transaction 5:

```text
1500 + 400 = 1900
```

### Important production considerations

1. Use a stable transaction ordering.
2. Include account_id in PARTITION BY.
3. Handle opening balances correctly.
4. Define how reversals and corrections are represented.
5. Consider late-arriving transactions.
6. Use DECIMAL rather than FLOAT for financial amounts.
7. Avoid calculating a balance from a partial transaction history without an appropriate opening balance.

---

# Chapter 70: Identifying Repeat Customers

A repeat customer is generally a customer who has made more than one purchase.

There are different business definitions:

- More than one transaction ever.
- More than one transaction in a month.
- More than one transaction after the first purchase.
- Purchases on different calendar days.

### Example: Identify customers with multiple transactions

```sql
SELECT
    transaction_id,
    customer_id,
    transaction_date,
    amount,

    COUNT(*) OVER (
        PARTITION BY customer_id
    ) AS total_transactions

FROM transactions;
```

To filter repeat customers:

```sql
WITH customer_activity AS (
    SELECT
        transaction_id,
        customer_id,
        transaction_date,
        amount,

        COUNT(*) OVER (
            PARTITION BY customer_id
        ) AS total_transactions

    FROM transactions
)

SELECT *
FROM customer_activity
WHERE total_transactions > 1;
```

### Example: Identify repeat purchases after the first purchase

```sql
WITH customer_activity AS (
    SELECT
        transaction_id,
        customer_id,
        transaction_date,
        amount,

        ROW_NUMBER() OVER (
            PARTITION BY customer_id
            ORDER BY transaction_date, transaction_id
        ) AS purchase_number

    FROM transactions
)

SELECT *
FROM customer_activity
WHERE purchase_number > 1;
```

This identifies every purchase after the customer's first recorded purchase.

---

# Chapter 71: Finding the Highest-Selling Product Per Region

Suppose we have a sales table:

| region | product | total_sales |
|---|---|---:|
| North | Laptop | 100000 |
| North | Phone | 80000 |
| North | Tablet | 50000 |
| South | Laptop | 60000 |
| South | Phone | 90000 |
| South | Tablet | 40000 |

We want the highest-selling product per region.

```sql
WITH product_ranking AS (
    SELECT
        region,
        product,
        total_sales,

        RANK() OVER (
            PARTITION BY region
            ORDER BY total_sales DESC
        ) AS sales_rank

    FROM regional_product_sales
)

SELECT
    region,
    product,
    total_sales

FROM product_ranking
WHERE sales_rank = 1;
```

Output:

| region | product | total_sales |
|---|---|---:|
| North | Laptop | 100000 |
| South | Phone | 90000 |

If two products tie for the highest sales, both are returned.

---

# Chapter 72: Advanced Use of Multiple Window Definitions

Sometimes we need different analytical calculations over the same dataset.

For example:

- Global ranking.
- Department ranking.
- Department average.
- Department running total.
- Previous employee salary in a particular order.

These calculations can use different windows.

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,

    RANK() OVER (
        ORDER BY salary DESC
    ) AS global_rank,

    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS department_rank,

    AVG(salary) OVER (
        PARTITION BY department
    ) AS department_average,

    SUM(salary) OVER (
        PARTITION BY department
        ORDER BY employee_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS department_running_total,

    LAG(salary) OVER (
        PARTITION BY department
        ORDER BY employee_id
    ) AS previous_employee_salary

FROM employees;
```

### Understanding the five different calculations

| Expression | What it calculates |
|---|---|
| Global RANK | Salary position across all employees |
| Department RANK | Salary position within the department |
| Department AVG | Average salary of the department |
| Department running total | Accumulated salary in employee ID order |
| LAG | Previous employee's salary in employee ID order |

This is an example of an analytical query that combines multiple window definitions without requiring separate joins.

---

# Chapter 73: Advanced Concepts – What Window Functions Cannot Do Directly

Understanding limitations is just as important as understanding capabilities.

## 73.1 Nested Window Functions

You generally cannot directly nest one window function inside another window function at the same query level.

Incorrect:

```sql
SELECT
    employee_name,

    SUM(
        ROW_NUMBER() OVER (
            ORDER BY salary
        )
    ) OVER ()

FROM employees;
```

Instead, use separate query levels.

Correct:

```sql
WITH employee_numbers AS (
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

    SUM(rn) OVER () AS total_row_numbers

FROM employee_numbers;
```

The first query level calculates row numbers.

The second query level calculates the aggregate window over those results.

## 73.2 Window Functions Cannot Generally Be Used Directly in WHERE

Incorrect:

```sql
SELECT
    employee_name,
    salary,
    ROW_NUMBER() OVER (
        ORDER BY salary DESC
    ) AS rn

FROM employees

WHERE rn = 1;
```

Correct:

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
WHERE rn = 1;
```

## 73.3 DISTINCT Limitations

Some window aggregates cannot directly perform distinct calculations.

For example, MySQL 8.0 does not support:

```sql
COUNT(DISTINCT customer_id) OVER ()
```

Use first-occurrence marking or an alternative grouped subquery.

## 73.4 Window Function Ordering Must Be Deterministic

Incorrect:

```sql
ROW_NUMBER() OVER (
    ORDER BY salary DESC
)
```

If several employees have the same salary, their individual row-number assignments are not guaranteed to remain stable.

Better:

```sql
ROW_NUMBER() OVER (
    ORDER BY salary DESC, employee_id
)
```

Use a unique tie-breaker when the business requirement needs a reproducible row sequence.

---

# Part 6 – Advanced Pattern Revision

| Problem | Main technique |
|---|---|
| Consecutive dates | ROW_NUMBER() + date subtraction |
| Consecutive numbers | ROW_NUMBER() + value subtraction |
| Identify gaps | LAG() + DATEDIFF() |
| Generate missing dates | Recursive CTE + LEFT JOIN |
| Sessionization | LAG() + CASE + running SUM() |
| Consecutive purchases | LAG() + DATEDIFF() |
| Cumulative distinct count | ROW_NUMBER() + first-occurrence flag + SUM() |
| Detect salary changes | LAG() + comparison |
| Historical attribute changes | Multiple LAG() functions |
| Running account balance | SUM() OVER() |
| Repeat customers | COUNT() OVER() or ROW_NUMBER() |
| Highest-selling product | RANK() or DENSE_RANK() |
| Latest record | ROW_NUMBER() |
| Nested analytical calculations | Multiple query levels using CTEs |

## Important problem-solving tricks

### Trick 1: Need the previous row?

Think:

```sql
LAG()
```

### Trick 2: Need the next row?

Think:

```sql
LEAD()
```

### Trick 3: Need to find a sequence break?

Think:

```sql
LAG() + DATEDIFF()
```

### Trick 4: Need to identify consecutive groups?

Think:

```sql
ROW_NUMBER() + GROUP BY
```

### Trick 5: Need a cumulative calculation?

Think:

```sql
SUM() OVER (
    ORDER BY ...
    ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
)
```

### Trick 6: Need to detect historical changes?

Think:

```sql
LAG() + CASE WHEN
```

### Trick 7: Need to calculate unique values cumulatively in MySQL?

Think:

```text
ROW_NUMBER()
    ↓
First occurrence flag
    ↓
Running SUM()
```

### Trick 8: Need to find the latest record per entity?

Think:

```sql
ROW_NUMBER() OVER (
    PARTITION BY entity_id
    ORDER BY timestamp DESC
)
```

### Trick 9: Need to find the top N within each group?

Think:

```sql
RANK()
```

or:

```sql
ROW_NUMBER()
```

depending on whether ties should be retained.

---

# Part 6 – Self-Assessment

Before proceeding to practical interview problems, make sure you can solve these independently:

1. Identify groups of consecutive dates.
2. Identify gaps between transactions.
3. Find missing dates in a calendar.
4. Calculate session numbers using a 30-minute inactivity threshold.
5. Identify customers with purchases on consecutive days.
6. Calculate cumulative distinct customer counts in MySQL.
7. Detect employee salary changes.
8. Detect changes across multiple historical attributes.
9. Calculate account balances after every transaction.
10. Find customers who made repeat purchases.
11. Find the highest-selling product per region.
12. Explain why nested window functions require another query level.

