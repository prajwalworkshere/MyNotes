

# Part 7 – Practical Real-World SQL Problems

The next chapter contains 35 fully worked problems using **MySQL 8.0.42**, with sample tables, expected outputs, query logic, execution walkthroughs, alternatives, and interview traps.

# Chapter 74: Practical SQL Window Function Problem-Solving Handbook

**35 Practical Problems | MySQL 8.0.42**

We will use realistic business scenarios and progressively increase the difficulty.

For consistency:

- Most employee problems use the `employees` table.
- Sales problems use the `sales` table.
- Transaction problems use the `transactions` table.
- Customer problems use the `customers` table.
- Login and session problems use separate login and activity datasets.

## Problem-Solving Approach

Follow these eight steps for every question:

1. Understand what the question is asking.
2. Identify the required partition.
3. Identify the required ordering.
4. Select the appropriate window function.
5. Decide whether a window frame is necessary.
6. Write the SQL query.
7. Verify the expected output.
8. Consider edge cases and alternative approaches.

---

# Chapter 75: Basic and Intermediate Problems

## Problem 1: Find the Highest-Paid Employee in Each Department

**Problem statement:** Find the employee receiving the highest salary in every department. If multiple employees share the highest salary, return all of them.

### Sample input

|employee_id|employee_name|department|salary|
|---|---|---|---|
|101|Amit|IT|60000|
|102|Rahul|IT|80000|
|103|Priya|IT|80000|
|104|Neha|HR|50000|
|105|Karan|HR|65000|
|106|Sneha|HR|55000|
|107|Arjun|Sales|45000|
|108|Riya|Sales|75000|

### SQL solution

```sql
WITH employee_ranking AS (
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
)

SELECT
    employee_id,
    employee_name,
    department,
    salary

FROM employee_ranking
WHERE salary_rank = 1;
```

### Expected output

|employee_id|employee_name|department|salary|
|---|---|---|---|
|102|Rahul|IT|80000|
|103|Priya|IT|80000|
|105|Karan|HR|65000|
|108|Riya|Sales|75000|

### Explanation

- `PARTITION BY department` creates independent department groups.
- `ORDER BY salary DESC` puts the highest salary first.
- `RANK()` assigns rank 1 to the highest salary.
- `WHERE salary_rank = 1` returns every employee tied for first place.

**Alternative:** Use `ROW_NUMBER()` if the requirement is exactly one employee per department.

**Interview trap:** Using `ROW_NUMBER()` without understanding the tie requirement can accidentally exclude employees.

---

## Problem 2: Find the Top Three Employees in Every Department

**Problem statement:** Return exactly three employees per department, ordered by salary from highest to lowest.

### SQL solution

```sql
WITH employee_ranking AS (
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

FROM employee_ranking
WHERE rn <= 3;
```

### Expected output

|employee_name|department|salary|rn|
|---|---|---|---|
|Rahul|IT|80000|1|
|Priya|IT|80000|2|
|Amit|IT|60000|3|
|Karan|HR|65000|1|
|Sneha|HR|55000|2|
|Neha|HR|50000|3|
|Riya|Sales|75000|1|
|Arjun|Sales|45000|2|

### Explanation

`ROW_NUMBER()` generates a unique position within each department.

The outer query selects the first three positions.

Sales contains only two employees, so it returns two records.

**Alternative:** Use `RANK()` when ties at the third position should also be included.

---

## Problem 3: Find the Second-Highest Distinct Salary

**Problem statement:** Find employees receiving the second-highest distinct salary across the organization.

### SQL solution

```sql
WITH salary_ranking AS (
    SELECT
        employee_id,
        employee_name,
        salary,

        DENSE_RANK() OVER (
            ORDER BY salary DESC
        ) AS salary_rank

    FROM employees
)

SELECT *
FROM salary_ranking
WHERE salary_rank = 2;
```

### Expected output

|employee_name|salary|salary_rank|
|---|---|---|
|Amit|60000|2|

This example assumes the highest distinct salary is 80000 and the next is 60000.

### Explanation

`DENSE_RANK()` ranks distinct salary levels without gaps.

If several employees receive the highest salary, they all receive rank 1.

The next distinct salary receives rank 2.

**Alternative:**

```sql
SELECT *
FROM employees
WHERE salary = (
    SELECT MAX(salary)
    FROM employees
    WHERE salary < (
        SELECT MAX(salary)
        FROM employees
    )
);
```

**Interview trap:** Clarify whether the question asks for the second row or the second distinct salary.

---

## Problem 4: Find the Nth-Highest Salary in Every Department

**Problem statement:** Find employees receiving the third-highest distinct salary within their department.

### SQL solution

```sql
WITH salary_ranking AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        DENSE_RANK() OVER (
            PARTITION BY department
            ORDER BY salary DESC
        ) AS salary_rank

    FROM employees
)

SELECT *
FROM salary_ranking
WHERE salary_rank = 3;
```

### Explanation

- Each department gets an independent ranking.
- `DENSE_RANK()` ranks distinct salary levels.
- Rank 3 identifies the third-highest distinct salary level.

### Generalization

```sql
WHERE salary_rank = N;
```

Replace `N` with the required salary position.

**Edge case:** A department with fewer than N distinct salary values will not produce a result.

---

## Problem 5: Calculate Department-Wise Average Salary

**Problem statement:** Display every employee alongside their department's average salary.

### SQL solution

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,

    ROUND(
        AVG(salary) OVER (
            PARTITION BY department
        ),
        2
    ) AS department_average

FROM employees;
```

### Expected output

|employee_name|department|salary|department_average|
|---|---|---|---|
|Amit|IT|60000|73333.33|
|Rahul|IT|80000|73333.33|
|Priya|IT|80000|73333.33|
|Neha|HR|50000|56666.67|
|Karan|HR|65000|56666.67|
|Sneha|HR|55000|56666.67|
|Arjun|Sales|45000|60000.00|
|Riya|Sales|75000|60000.00|

### Explanation

The average is calculated independently within each department.

The individual employee rows are preserved.

**Alternative:** `GROUP BY` and `JOIN` can also solve this problem, but the window function is more direct.

---

## Problem 6: Find Employees Earning Above Their Department Average

**Problem statement:** Return employees whose salaries exceed their department's average salary.

### SQL solution

```sql
WITH salary_analysis AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        AVG(salary) OVER (
            PARTITION BY department
        ) AS department_average

    FROM employees
)

SELECT *
FROM salary_analysis
WHERE salary > department_average;
```

### Expected output

|employee_name|department|salary|department_average|
|---|---|---|---|
|Rahul|IT|80000|73333.33|
|Priya|IT|80000|73333.33|
|Karan|HR|65000|56666.67|
|Riya|Sales|75000|60000.00|

### Explanation

The CTE first calculates the department average.

The outer query compares each employee's salary with that average.

**Interview point:** This is a common example of why window results are filtered in another query level.

---

## Problem 7: Calculate Running Sales Total

**Problem statement:** Calculate cumulative sales in chronological order.

### Sample input

|sale_id|sale_date|amount|
|---|---|---|
|1|Jan 01|100|
|2|Jan 02|200|
|3|Jan 03|300|
|4|Jan 04|400|
|5|Jan 05|500|

### SQL solution

```sql
SELECT
    sale_id,
    sale_date,
    amount,

    SUM(amount) OVER (
        ORDER BY sale_date, sale_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS running_total

FROM sales;
```

### Expected output

|sale_id|amount|running_total|
|---|---|---|
|1|100|100|
|2|200|300|
|3|300|600|
|4|400|1000|
|5|500|1500|

### Explanation

The frame begins at the first sale and ends at the current row.

The cumulative total increases with each subsequent sale.

**Important:** Use a unique ordering key to ensure deterministic results when sale dates are identical.

---

## Problem 8: Calculate Moving Average of the Last Three Transactions

**Problem statement:** Calculate a three-row moving average of transaction amounts.

### SQL solution

```sql
SELECT
    transaction_id,
    transaction_date,
    amount,

    ROUND(
        AVG(amount) OVER (
            ORDER BY transaction_date, transaction_id
            ROWS BETWEEN 2 PRECEDING
            AND CURRENT ROW
        ),
        2
    ) AS moving_average

FROM transactions;
```

### Expected output

For amounts 100, 200, 300, 400, 500:

|transaction_id|amount|moving_average|
|---|---|---|
|1|100|100.00|
|2|200|150.00|
|3|300|200.00|
|4|400|300.00|
|5|500|400.00|

### Explanation

For transaction 4:

```text
(200 + 300 + 400) / 3 = 300
```

The frame contains the current transaction and two preceding transactions.

**Edge case:** At the beginning of the dataset, fewer than three rows are available. `AVG()` calculates using the available non-NULL values.

---

## Problem 9: Calculate Percentage Contribution of Each Employee

**Problem statement:** Calculate each employee's percentage contribution to the total salary of their department.

### SQL solution

```sql
SELECT
    employee_name,
    department,
    salary,

    SUM(salary) OVER (
        PARTITION BY department
    ) AS department_total,

    ROUND(
        salary * 100.0 /
        SUM(salary) OVER (
            PARTITION BY department
        ),
        2
    ) AS contribution_percentage

FROM employees;
```

### Expected output

|employee_name|department|salary|contribution_percentage|
|---|---|---|---|
|Amit|IT|60000|27.27|
|Rahul|IT|80000|36.36|
|Priya|IT|80000|36.36|
|Neha|HR|50000|29.41|
|Karan|HR|65000|38.24|
|Sneha|HR|55000|32.35|
|Arjun|Sales|45000|37.50|
|Riya|Sales|75000|62.50|

### Explanation

For Amit:

```text
60000 / 220000 × 100 = 27.27%
```

The total is calculated separately for each department.

**Interview application:** Revenue contribution, cost distribution and employee compensation analysis.

---

## Problem 10: Find the Previous Transaction Amount for Each Customer

**Problem statement:** Retrieve each customer's previous transaction amount.

### SQL solution

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

### Expected output

|transaction_id|customer_id|amount|previous_amount|
|---|---|---|---|
|1|101|500|NULL|
|2|101|700|500|
|3|101|900|700|
|4|102|300|NULL|
|5|102|600|300|

### Explanation

`PARTITION BY customer_id` prevents one customer's transaction from becoming another customer's previous transaction.

`LAG()` retrieves the preceding amount within each customer partition.

---

## Problem 11: Calculate Month-over-Month Revenue Growth

**Problem statement:** Calculate the percentage increase or decrease in revenue compared with the previous month.

### Sample input

|month_start|revenue|
|---|---|
|Jan 01|10000|
|Feb 01|12000|
|Mar 01|15000|
|Apr 01|13500|

### SQL solution

```sql
WITH revenue_comparison AS (
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
    previous_revenue,

    ROUND(
        (revenue - previous_revenue)
        * 100.0 /
        NULLIF(previous_revenue, 0),
        2
    ) AS growth_percentage

FROM revenue_comparison;
```

### Expected output

|month_start|revenue|previous_revenue|growth_percentage|
|---|---|---|---|
|Jan 01|10000|NULL|NULL|
|Feb 01|12000|10000|20.00|
|Mar 01|15000|12000|25.00|
|Apr 01|13500|15000|-10.00|

### Explanation

`LAG()` retrieves the preceding month's revenue.

The formula compares current revenue against previous revenue.

**Important:** This assumes one row per calendar month with no missing months. If months are missing, use a calendar series to align the comparison correctly.

---

## Problem 12: Find the First and Last Transaction of Each Customer

**Problem statement:** For each transaction, display the first and last transaction dates of its customer.

### SQL solution

```sql
SELECT
    customer_id,
    transaction_id,
    transaction_date,
    amount,

    FIRST_VALUE(transaction_date) OVER (
        PARTITION BY customer_id
        ORDER BY transaction_date, transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS first_transaction,

    LAST_VALUE(transaction_date) OVER (
        PARTITION BY customer_id
        ORDER BY transaction_date, transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND UNBOUNDED FOLLOWING
    ) AS last_transaction

FROM transactions;
```

### Explanation

- `FIRST_VALUE()` returns the first date in the complete customer partition.
- `LAST_VALUE()` returns the final date in the complete customer partition.
- The explicit frame ensures both functions use the complete partition.

**Interview trap:** Omitting the full-partition frame from `LAST_VALUE()` can produce unexpected results.

---

## Problem 13: Find Customers with More Than One Purchase

**Problem statement:** Return customers who have made at least two purchases.

### SQL solution

```sql
WITH customer_activity AS (
    SELECT
        transaction_id,
        customer_id,
        transaction_date,
        amount,

        COUNT(*) OVER (
            PARTITION BY customer_id
        ) AS purchase_count

    FROM transactions
)

SELECT *
FROM customer_activity
WHERE purchase_count >= 2;
```

### Explanation

`COUNT(*)` calculates the total number of transactions for each customer.

The outer query filters customers with two or more purchases.

**Alternative:**

```sql
SELECT
    customer_id,
    COUNT(*) AS purchase_count
FROM transactions
GROUP BY customer_id
HAVING COUNT(*) >= 2;
```

Use the window-function approach when individual transaction records must also be displayed.

---

## Problem 14: Find the Latest Record for Every Customer

**Problem statement:** Return only the latest transaction for every customer.

### SQL solution

```sql
WITH latest_transactions AS (
    SELECT
        transaction_id,
        customer_id,
        transaction_date,
        amount,

        ROW_NUMBER() OVER (
            PARTITION BY customer_id
            ORDER BY transaction_date DESC, transaction_id DESC
        ) AS rn

    FROM transactions
)

SELECT
    transaction_id,
    customer_id,
    transaction_date,
    amount

FROM latest_transactions
WHERE rn = 1;
```

### Explanation

1. Partition transactions by customer.
2. Sort each partition newest first.
3. Assign row number 1 to the latest transaction.
4. Filter for row number 1.

**Edge case:** The transaction ID tie-breaker resolves duplicate timestamps deterministically.

---

## Problem 15: Divide Employees into Four Performance Groups

**Problem statement:** Divide employees into four approximately equal-sized groups according to salary, from highest to lowest.

### SQL solution

```sql
SELECT
    employee_id,
    employee_name,
    salary,

    NTILE(4) OVER (
        ORDER BY salary DESC, employee_id
    ) AS performance_group

FROM employees;
```

### Explanation

`NTILE(4)` distributes the ordered employee rows into four buckets.

- Bucket 1: Higher salaries.
- Bucket 2: Next salary group.
- Bucket 3: Next salary group.
- Bucket 4: Lower salaries.

**Important:** `NTILE()` divides records into approximately equal-sized buckets, not equal salary ranges.

---

# Chapter 76: Intermediate and Advanced Real-World Problems

## Problem 16: Calculate Year-over-Year Revenue Growth

**Problem statement:** Compare monthly revenue with revenue from the same month in the previous year.

### Sample input

|month_start|revenue|
|---|---|
|2025-01-01|10000|
|2025-02-01|12000|
|2025-03-01|15000|
|2026-01-01|13000|
|2026-02-01|15000|
|2026-03-01|18000|

### SQL solution

```sql
WITH revenue_analysis AS (
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
        * 100.0 /
        NULLIF(previous_year_revenue, 0),
        2
    ) AS yoy_growth

FROM revenue_analysis;
```

### Important correction for sparse monthly datasets

The sample contains only six months, so an offset of 12 will not match January 2025 with January 2026.

For irregular or incomplete data, use a month-key self-join instead:

```sql
SELECT
    current_month.month_start,
    current_month.revenue,
    previous_month.revenue AS previous_year_revenue,

    ROUND(
        (current_month.revenue - previous_month.revenue)
        * 100.0 /
        NULLIF(previous_month.revenue, 0),
        2
    ) AS yoy_growth

FROM monthly_sales current_month

LEFT JOIN monthly_sales previous_month
    ON previous_month.month_start =
       DATE_SUB(current_month.month_start, INTERVAL 1 YEAR);
```

### Expected output

|month_start|revenue|previous_year_revenue|yoy_growth|
|---|---|---|---|
|2026-01-01|13000|10000|30.00|
|2026-02-01|15000|12000|25.00|
|2026-03-01|18000|15000|20.00|

### Explanation

January:

```text
(13000 - 10000) / 10000 × 100 = 30%
```

The self-join solution is useful when missing months make row offsets unreliable.

---

## Problem 17: Calculate Cumulative Sales Percentage

**Problem statement:** Calculate cumulative sales and the cumulative percentage contribution of each product.

### Sample input

|product|sales|
|---|---|
|Laptop|400|
|Phone|300|
|Tablet|200|
|Watch|100|

### SQL solution

```sql
SELECT
    product,
    sales,

    SUM(sales) OVER (
        ORDER BY sales DESC, product
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS cumulative_sales,

    ROUND(
        100.0 *
        SUM(sales) OVER (
            ORDER BY sales DESC, product
            ROWS BETWEEN UNBOUNDED PRECEDING
            AND CURRENT ROW
        ) /
        SUM(sales) OVER (),
        2
    ) AS cumulative_percentage

FROM product_sales;
```

### Expected output

|product|sales|cumulative_sales|cumulative_percentage|
|---|---|---|---|
|Laptop|400|400|40|
|Phone|300|700|70|
|Tablet|200|900|90|
|Watch|100|1000|100|

### Explanation

The query calculates a running total and divides it by overall sales.

This is commonly used in Pareto analysis.

---

## Problem 18: Find the Median Salary

**Problem statement:** Calculate the median salary of employees.

Median is the middle value after sorting salaries.

For an odd number of records, it is the middle value.

For an even number of records, it is the average of the two middle values.

### Sample input

|employee_id|salary|
|---|---|
|1|20000|
|2|30000|
|3|40000|
|4|50000|
|5|60000|

Median:

```text
40000
```

### SQL solution for MySQL 8.0

```sql
WITH ordered_salaries AS (
    SELECT
        salary,

        ROW_NUMBER() OVER (
            ORDER BY salary
        ) AS rn,

        COUNT(*) OVER () AS total_rows

    FROM employees
    WHERE salary IS NOT NULL
)

SELECT
    AVG(salary) AS median_salary

FROM ordered_salaries

WHERE rn IN (
    FLOOR((total_rows + 1) / 2),
    FLOOR((total_rows + 2) / 2)
);
```

### Expected output

|median_salary|
|---|
|40000|

### Explanation

For five salaries:

```text
Lower middle position = 3
Upper middle position = 3
```

For six salaries:

```text
Lower middle position = 3
Upper middle position = 4
```

`AVG()` calculates the median from the selected middle value or values.

### Important edge cases

- NULL salaries are excluded.
- An empty dataset returns NULL.
- If all salaries are equal, the median is that salary.
- Use an appropriate numeric type to preserve precision.

---

## Problem 19: Find the 90th Percentile Salary

**Problem statement:** Find the salary value at the 90th percentile using a discrete nearest-rank method.

### SQL solution

```sql
WITH ordered_salaries AS (
    SELECT
        salary,

        ROW_NUMBER() OVER (
            ORDER BY salary
        ) AS rn,

        COUNT(*) OVER () AS total_rows

    FROM employees
    WHERE salary IS NOT NULL
)

SELECT
    MIN(salary) AS percentile_90

FROM ordered_salaries

WHERE rn = CEIL(total_rows * 0.90);
```

### Explanation

The nearest-rank method uses:

```text
Position = CEIL(N × percentile)
```

For 10 values:

```text
CEIL(10 × 0.90) = 9
```

The ninth ordered value is selected.

### Important distinction

Different percentile definitions produce different results.

For example, a continuous percentile can interpolate between two observations.

PostgreSQL offers `PERCENTILE_CONT()`, while MySQL 8.0 does not provide the same built-in ordered-set aggregate syntax.

Therefore, always clarify the percentile method before comparing results across databases.

---

## Problem 20: Find Duplicate Customer Records

**Problem statement:** Identify customer records having the same email address.

### Sample input

|customer_id|email|customer_name|
|---|---|---|
|1|amit@example.com|Amit|
|2|amit@example.com|Amit|
|3|priya@example.com|Priya|
|4|rahul@example.com|Rahul|
|5|rahul@example.com|Rahul|

### SQL solution

```sql
SELECT
    customer_id,
    email,
    customer_name,

    COUNT(*) OVER (
        PARTITION BY email
    ) AS duplicate_count

FROM customers;
```

To retrieve only duplicates:

```sql
WITH duplicate_analysis AS (
    SELECT
        customer_id,
        email,
        customer_name,

        COUNT(*) OVER (
            PARTITION BY email
        ) AS duplicate_count

    FROM customers
)

SELECT *
FROM duplicate_analysis
WHERE duplicate_count > 1;
```

### Expected output

|customer_id|email|duplicate_count|
|---|---|---|
|1|amit@example.com|2|
|2|amit@example.com|2|
|4|rahul@example.com|2|
|5|rahul@example.com|2|

### Explanation

Every email address forms a partition.

`COUNT(*)` determines how many records exist in each partition.

**Edge case:** NULL emails are also grouped together. If NULL should not count as a duplicate identity, filter those records or define a different business key.

---

## Problem 21: Deduplicate Customers and Retain the Latest Record

**Problem statement:** Keep the most recently updated customer record for every email address.

### SQL solution

```sql
WITH ranked_customers AS (
    SELECT
        customer_id,
        email,
        customer_name,
        updated_at,

        ROW_NUMBER() OVER (
            PARTITION BY email
            ORDER BY updated_at DESC, customer_id DESC
        ) AS rn

    FROM customers
    WHERE email IS NOT NULL
)

SELECT
    customer_id,
    email,
    customer_name,
    updated_at

FROM ranked_customers
WHERE rn = 1;
```

### Explanation

1. Partition records by email.
2. Sort by `updated_at` descending.
3. Resolve timestamp ties using `customer_id`.
4. Retain row number 1.

**Important:** This query selects the surviving records. It does not physically delete duplicates.

Always verify the selected rows before performing DELETE operations.

---

## Problem 22: Detect Salary Changes Between Consecutive Records

**Problem statement:** Find every historical record where an employee's salary differs from the preceding recorded salary.

### Sample input

|employee_id|effective_date|salary|
|---|---|---|
|101|Jan 01|50000|
|101|Feb 01|50000|
|101|Mar 01|55000|
|101|Apr 01|60000|

### SQL solution

```sql
WITH salary_history AS (
    SELECT
        employee_id,
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
    effective_date,
    previous_salary,
    salary AS current_salary

FROM salary_history

WHERE NOT (salary <=> previous_salary);
```

### Expected output

|employee_id|effective_date|previous_salary|current_salary|
|---|---|---|---|
|101|Jan 01|NULL|50000|
|101|Mar 01|50000|55000|
|101|Apr 01|55000|60000|

### Explanation

`LAG()` retrieves the preceding salary.

The MySQL NULL-safe equality operator `<=>` allows comparison even when one value is NULL.

The February record is excluded because its salary remains unchanged.

---

## Problem 23: Calculate Running Account Balance

**Problem statement:** Calculate the account balance after every deposit and withdrawal.

### Sample input

|transaction_id|account_id|amount|
|---|---|---|
|1|101|500|
|2|101|-200|
|3|101|300|
|4|101|-100|

Opening balance = 1000.

### SQL solution

```sql
SELECT
    transaction_id,
    account_id,
    amount,

    1000 + SUM(amount) OVER (
        PARTITION BY account_id
        ORDER BY transaction_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS account_balance

FROM account_transactions;
```

### Expected output

|transaction_id|amount|account_balance|
|---|---|---|
|1|500|1500|
|2|-200|1300|
|3|300|1600|
|4|-100|1500|

### Explanation

The opening balance is added to the cumulative transaction sum.

For multiple accounts, the opening balance must be supplied separately for each account.

---

## Problem 24: Identify Customers with Consecutive Purchase Days

**Problem statement:** Find customers who made purchases on consecutive calendar days.

### SQL solution

```sql
WITH distinct_purchases AS (
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

    FROM distinct_purchases
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

### Explanation

- `DISTINCT` ensures one record per customer per day.
- `LAG()` retrieves the preceding purchase day.
- `DATEDIFF()` checks whether the difference is exactly one day.

**Alternative:** Use Gaps and Islands to identify complete purchase streaks.

---

## Problem 25: Find Gaps Between Consecutive Login Dates

**Problem statement:** Find users whose consecutive recorded login dates are separated by more than one day.

### SQL solution

```sql
WITH login_comparison AS (
    SELECT
        user_id,
        login_date,

        LAG(login_date) OVER (
            PARTITION BY user_id
            ORDER BY login_date
        ) AS previous_login

    FROM (
        SELECT DISTINCT
            user_id,
            login_date
        FROM user_logins
    ) AS distinct_logins
)

SELECT
    user_id,
    previous_login,
    login_date,

    DATEDIFF(login_date, previous_login) AS gap_days

FROM login_comparison

WHERE DATEDIFF(login_date, previous_login) > 1;
```

### Expected output

|user_id|previous_login|login_date|gap_days|
|---|---|---|---|
|101|2026-01-03|2026-01-07|4|

### Explanation

The query detects gaps between observed login dates.

A gap of four days means three calendar dates were missed between the two observed logins.

---

## Problem 26: Calculate Session Numbers for User Activity

**Problem statement:** Create a new session whenever a user is inactive for more than 30 minutes.

### Sample input

|user_id|event_time|
|---|---|
|101|10:00|
|101|10:05|
|101|10:20|
|101|11:00|
|101|11:10|
|101|12:00|

### SQL solution

```sql
WITH event_comparison AS (
    SELECT
        user_id,
        event_time,

        LAG(event_time) OVER (
            PARTITION BY user_id
            ORDER BY event_time
        ) AS previous_event

    FROM user_events
),

session_flags AS (
    SELECT
        user_id,
        event_time,

        CASE
            WHEN previous_event IS NULL THEN 1

            WHEN TIMESTAMPDIFF(
                MINUTE,
                previous_event,
                event_time
            ) > 30 THEN 1

            ELSE 0
        END AS new_session

    FROM event_comparison
)

SELECT
    user_id,
    event_time,

    SUM(new_session) OVER (
        PARTITION BY user_id
        ORDER BY event_time
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS session_number

FROM session_flags;
```

### Expected output

|user_id|event_time|session_number|
|---|---|---|
|101|10:00|1|
|101|10:05|1|
|101|10:20|1|
|101|11:00|2|
|101|11:10|2|
|101|12:00|3|

### Explanation

`LAG()` retrieves the previous event.

`CASE WHEN` marks the start of a new session.

`SUM()` calculates a cumulative session number.

**Interview trap:** The rule is strictly greater than 30 minutes. If exactly 30 minutes should create a new session, change `>` to `>=`.

---

## Problem 27: Find the Highest-Selling Product in Each Region

**Problem statement:** Identify the product with the highest total sales in every region.

### SQL solution

```sql
WITH product_ranking AS (
    SELECT
        region,
        product_id,
        total_sales,

        RANK() OVER (
            PARTITION BY region
            ORDER BY total_sales DESC
        ) AS sales_rank

    FROM regional_product_sales
)

SELECT
    region,
    product_id,
    total_sales

FROM product_ranking
WHERE sales_rank = 1;
```

### Explanation

`RANK()` independently ranks products in each region.

The highest-selling product receives rank 1.

Ties are retained.

**Important:** If your source contains individual sales transactions rather than pre-aggregated product totals, aggregate by region and product before applying the ranking function.

---

## Problem 28: Calculate Each Customer's First Purchase Date and Repeat Purchase Count

**Problem statement:** Display each transaction alongside the customer's first purchase date and the number of purchases made after the first purchase.

### SQL solution

```sql
WITH customer_analysis AS (
    SELECT
        transaction_id,
        customer_id,
        transaction_date,
        amount,

        FIRST_VALUE(transaction_date) OVER (
            PARTITION BY customer_id
            ORDER BY transaction_date, transaction_id
            ROWS BETWEEN UNBOUNDED PRECEDING
            AND UNBOUNDED FOLLOWING
        ) AS first_purchase_date,

        ROW_NUMBER() OVER (
            PARTITION BY customer_id
            ORDER BY transaction_date, transaction_id
        ) AS purchase_number,

        COUNT(*) OVER (
            PARTITION BY customer_id
        ) AS total_purchases

    FROM transactions
)

SELECT
    transaction_id,
    customer_id,
    transaction_date,
    first_purchase_date,
    purchase_number,
    total_purchases,
    total_purchases - 1 AS repeat_purchase_count

FROM customer_analysis;
```

### Explanation

- `FIRST_VALUE()` identifies the first purchase date.
- `ROW_NUMBER()` assigns purchase sequence numbers.
- `COUNT()` calculates total purchases.
- Subtracting one gives the number of purchases after the first.

**Important:** This assumes the first purchase is defined by the earliest recorded transaction.

---

## Problem 29: Find the Maximum Salary So Far Within Each Department

**Problem statement:** For every employee, show the highest salary encountered so far when employees are processed in employee ID order.

### SQL solution

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,

    MAX(salary) OVER (
        PARTITION BY department
        ORDER BY employee_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS maximum_salary_so_far

FROM employees;
```

### Example output

|employee_id|employee_name|department|salary|maximum_salary_so_far|
|---|---|---|---|---|
|101|Amit|IT|60000|60000|
|102|Rahul|IT|80000|80000|
|103|Priya|IT|70000|80000|

### Explanation

`MAX()` examines the growing frame and returns the maximum salary encountered up to the current employee.

This is useful for tracking record highs over time.

---

## Problem 30: Find Employees Whose Salary Is Greater Than Their Previous Employee's Salary

**Problem statement:** Within each department, identify employees whose salary is greater than the salary of the preceding employee when employees are ordered by employee ID.

### SQL solution

```sql
WITH salary_comparison AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        LAG(salary) OVER (
            PARTITION BY department
            ORDER BY employee_id
        ) AS previous_salary

    FROM employees
)

SELECT
    employee_id,
    employee_name,
    department,
    salary,
    previous_salary

FROM salary_comparison
WHERE salary > previous_salary;
```

### Expected output

|employee_name|department|salary|previous_salary|
|---|---|---|---|
|Rahul|IT|80000|60000|
|Priya|IT|80000|80000|
|Karan|HR|65000|50000|
|Riya|Sales|75000|45000|

### Explanation

`LAG()` retrieves the preceding employee's salary within the department.

The outer query compares the current salary against the previous salary.

**Important:** This problem compares employees by employee ID, not by salary rank. Changing `ORDER BY` changes the business meaning of the question.

---

# Chapter 77: Five Additional Advanced Challenge Problems

These additional problems take the collection beyond the required 30.

## Problem 31: Find Employees Whose Salary Is Above the Overall Average but Below the Department Maximum

### SQL solution

```sql
WITH employee_analysis AS (
    SELECT
        employee_id,
        employee_name,
        department,
        salary,

        AVG(salary) OVER () AS overall_average,

        MAX(salary) OVER (
            PARTITION BY department
        ) AS department_maximum

    FROM employees
)

SELECT *
FROM employee_analysis

WHERE salary > overall_average
  AND salary < department_maximum;
```

**Logic:** The employee must earn more than the organization-wide average but less than the maximum salary in their department.

---

## Problem 32: Find the First Transaction After a Gap of More Than Seven Days

### SQL solution

```sql
WITH transaction_history AS (
    SELECT
        transaction_id,
        customer_id,
        transaction_date,
        amount,

        LAG(transaction_date) OVER (
            PARTITION BY customer_id
            ORDER BY transaction_date, transaction_id
        ) AS previous_transaction_date

    FROM transactions
)

SELECT *
FROM transaction_history

WHERE DATEDIFF(
    transaction_date,
    previous_transaction_date
) > 7;
```

**Logic:** `LAG()` retrieves the previous transaction date. `DATEDIFF()` checks whether the inactivity gap exceeds seven days.

---

## Problem 33: Calculate Department-Wise Cumulative Salary Contribution

### SQL solution

```sql
SELECT
    employee_id,
    employee_name,
    department,
    salary,

    SUM(salary) OVER (
        PARTITION BY department
        ORDER BY salary DESC, employee_id
        ROWS BETWEEN UNBOUNDED PRECEDING
        AND CURRENT ROW
    ) AS cumulative_salary,

    ROUND(
        100.0 *
        SUM(salary) OVER (
            PARTITION BY department
            ORDER BY salary DESC, employee_id
            ROWS BETWEEN UNBOUNDED PRECEDING
            AND CURRENT ROW
        ) /
        SUM(salary) OVER (
            PARTITION BY department
        ),
        2
    ) AS cumulative_contribution

FROM employees;
```

**Logic:** Calculate cumulative salaries in descending salary order and divide by the total salary of the department.

---

## Problem 34: Find Customers Whose Latest Purchase Is Greater Than Their Previous Purchase

### SQL solution

```sql
WITH purchase_history AS (
    SELECT
        transaction_id,
        customer_id,
        transaction_date,
        amount,

        ROW_NUMBER() OVER (
            PARTITION BY customer_id
            ORDER BY transaction_date DESC, transaction_id DESC
        ) AS rn,

        LEAD(amount) OVER (
            PARTITION BY customer_id
            ORDER BY transaction_date DESC, transaction_id DESC
        ) AS previous_purchase_amount

    FROM transactions
)

SELECT
    customer_id,
    transaction_date,
    amount AS latest_purchase,
    previous_purchase_amount

FROM purchase_history

WHERE rn = 1
  AND amount > previous_purchase_amount;
```

**Logic:** The latest transaction is first in descending date order. `LEAD()` retrieves the next row in that ordering, which represents the preceding transaction chronologically.

---

## Problem 35: Calculate the Number of Days Between First and Last Purchase

### SQL solution

```sql
SELECT
    customer_id,
    transaction_id,
    transaction_date,

    DATEDIFF(
        MAX(transaction_date) OVER (
            PARTITION BY customer_id
        ),
        MIN(transaction_date) OVER (
            PARTITION BY customer_id
        )
    ) AS purchase_period_days

FROM transactions;
```

**Logic:** `MIN()` and `MAX()` calculate the earliest and latest transaction dates for each customer. `DATEDIFF()` calculates the number of days between them.

---

# Chapter 78: Practical Problem-Solving Summary

## Window Function Problem Reference

|Problem|Main function or technique|
|---|---|
|1|RANK()|
|2|ROW_NUMBER()|
|3|DENSE_RANK()|
|4|DENSE_RANK() + PARTITION BY|
|5|AVG() OVER()|
|6|AVG() + CTE|
|7|SUM() + running frame|
|8|AVG() + moving frame|
|9|SUM() + percentage calculation|
|10|LAG()|
|11|LAG() + growth formula|
|12|FIRST_VALUE() + LAST_VALUE()|
|13|COUNT()|
|14|ROW_NUMBER()|
|15|NTILE()|
|16|LAG() or month-key self-join|
|17|Running SUM()|
|18|ROW_NUMBER() + COUNT()|
|19|ROW_NUMBER() + CEIL()|
|20|COUNT() OVER()|
|21|ROW_NUMBER() + deduplication|
|22|LAG() + comparison|
|23|Running SUM()|
|24|LAG() + DATEDIFF()|
|25|LAG() + DATEDIFF()|
|26|LAG() + CASE + SUM()|
|27|RANK()|
|28|FIRST_VALUE() + ROW_NUMBER() + COUNT()|
|29|MAX() + running frame|
|30|LAG() + comparison|
|31|AVG() + MAX()|
|32|LAG() + DATEDIFF()|
|33|Running SUM() + percentage|
|34|ROW_NUMBER() + LEAD()|
|35|MIN() + MAX() + DATEDIFF()|

## Important Interview Problem-Solving Strategy

When you encounter a complex SQL question, identify the required operation first.

|Question wording|Think about|
|---|---|
|Highest, lowest, top N|RANK(), DENSE_RANK(), ROW_NUMBER()|
|Previous, preceding|LAG()|
|Next, subsequent|LEAD()|
|First, last|FIRST_VALUE(), LAST_VALUE()|
|Nth position|NTH_VALUE(), ROW_NUMBER()|
|Cumulative, running|SUM() with ROWS frame|
|Moving, rolling|ROWS with preceding/following|
|Average of group|AVG() OVER(PARTITION BY)|
|Percentage contribution|SUM() OVER()|
|Relative distribution|PERCENT_RANK(), CUME_DIST()|
|Buckets, quartiles|NTILE()|
|Consecutive, streak|Gaps and Islands|
|Inactive period, session|LAG() + CASE + SUM()|
|Duplicate, latest record|ROW_NUMBER()|
|Historical change|LAG() + comparison|
|Missing dates|Calendar table + anti-join|

## Final Interview Checklist

Before submitting a window-function query, verify:

1. Is the `PARTITION BY` column correct?
2. Is the `ORDER BY` sequence correct?
3. Is a unique tie-breaker required?
4. Should ties be retained?
5. Is `ROW_NUMBER()`, `RANK()`, or `DENSE_RANK()` appropriate?
6. Is an explicit `ROWS` frame required?
7. Does `LAST_VALUE()` need a full-partition frame?
8. Are NULL values handled correctly?
9. Are missing dates or months affecting the calculation?
10. Does the expected output match the business requirement?

**Key takeaway:** Window functions are not just ranking functions. They are powerful tools for solving analytical SQL problems involving ranking, comparisons, cumulative calculations, moving averages, historical changes, customer behavior, sessionization, and business reporting.

**Database version:** MySQL 8.0.42.

