# SQL NULL FUNCTIONS — COMPLETE NOTES

## MySQL 8.0+ | Theory + Syntax + Examples + Outputs + Practice Questions

## TABLE OF CONTENTS

1. Introduction to NULL in SQL
2. Demo Database and Sample Data
3. SQL NULL Functions
   - IFNULL()
   - COALESCE()
   - NULLIF()
   - ISNULL()
   - IF()
   - CASE WHEN
4. NULL Checking Operators
5. NULL Functions with Aggregate Functions
6. NULL Functions in SQL Queries
7. Solved SQL Practice Questions
8. Interview Questions and Answers
9. Common Mistakes
10. Quick Revision Cheat Sheet

---

# 1\. INTRODUCTION TO NULL IN SQL

## What is NULL?

`NULL` represents a missing, unknown, or unavailable value in a database.

Example:

```sql
SELECT NULL AS result;
```

Output:

| result |
| --- |
| NULL |

NULL does not mean:

- Zero (`0`)
- An empty string (`''`)
- A blank space (`' '`)
- The Boolean value FALSE

These values have different meanings.

```sql
SELECT
    NULL AS null_value,
    0 AS zero_value,
    '' AS empty_string,
    ' ' AS blank_space;
```

Each expression represents a different value.

## Why is NULL used?

NULL is useful when:

- An employee's salary is not yet available.
- A customer has not provided a phone number.
- A product has no recorded discount.
- A date is unknown.
- A field is optional.
- Information has not yet been entered.

## What are SQL NULL functions?

NULL functions help us handle missing values, replace NULL with a default value, select the first available value, and avoid incorrect calculations.

For example:

```sql
SELECT IFNULL(NULL, 100) AS result;
```

Output:

| result |
| --- |
| 100 |

## Important concept: NULL comparisons

You should not check NULL using the ordinary equality operator.

Incorrect:

```sql
SELECT *
FROM employees
WHERE salary = NULL;
```

Correct:

```sql
SELECT *
FROM employees
WHERE salary IS NULL;
```

To find non-NULL values:

```sql
SELECT *
FROM employees
WHERE salary IS NOT NULL;
```

### Why does `= NULL` not work?

SQL uses three-valued logic:

- TRUE
- FALSE
- UNKNOWN

Comparisons involving NULL generally produce UNKNOWN rather than TRUE or FALSE.

Therefore, use `IS NULL` and `IS NOT NULL` to check missing values.

---

# 2\. DEMO DATABASE AND SAMPLE DATA

We will use the following table throughout these notes.

## Create the table

```sql
CREATE TABLE employees (
    emp_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    department VARCHAR(50),
    salary DECIMAL(10,2),
    bonus DECIMAL(10,2),
    phone VARCHAR(20),
    city VARCHAR(50)
);
```

## Insert sample data

```
INSERT INTO employees
(emp_id, first_name, department, salary, bonus, phone, city)
VALUES
(1, 'Amit', 'IT', 65000, 5000, '9876543210', 'Mumbai'),
(2, 'Priya', 'HR', 55000, NULL, NULL, 'Pune'),
(3, 'Rahul', 'IT', 72000, 7000, '9123456780', 'Delhi'),
(4, 'Neha', 'Finance', NULL, 4000, NULL, 'Nashik'),
(5, 'Sneha', 'HR', 58000, NULL, '9000011111', NULL);
```

## View the data

```sql
SELECT * FROM employees;
```

Expected result:

| emp\_id | first\_name | department | salary | bonus | phone | city |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | Amit | IT | 65000 | 5000 | 9876543210 | Mumbai |
| 2 | Priya | HR | 55000 | NULL | NULL | Pune |
| 3 | Rahul | IT | 72000 | 7000 | 9123456780 | Delhi |
| 4 | Neha | Finance | NULL | 4000 | NULL | Nashik |
| 5 | Sneha | HR | 58000 | NULL | 9000011111 | NULL |

**Important:** The phone numbers are stored as strings, so leading zeros would be preserved if present.

---

# 3\. SQL NULL FUNCTIONS

## 3.1 IFNULL()

### Theory

`IFNULL()` checks whether an expression is NULL.

- If the expression is not NULL, it returns the original value.
- If the expression is NULL, it returns the replacement value.

`IFNULL()` is a MySQL-specific function.

### Syntax

```
IFNULL(expression, replacement_value)
```

### Example 1: Replace NULL with zero

```sql
SELECT IFNULL(NULL, 0) AS result;
```

Output:

| result |
| --- |
| 0 |

### Example 2: Non-NULL value

```sql
SELECT IFNULL(100, 0) AS result;
```

Output:

| result |
| --- |
| 100 |

### Example 3: Replace a missing bonus

```sql
SELECT
    first_name,
    bonus,
    IFNULL(bonus, 0) AS final_bonus
FROM employees;
```

Expected result:

| first\_name | bonus | final\_bonus |
| --- | --- | --- |
| Amit | 5000 | 5000 |
| Priya | NULL | 0 |
| Rahul | 7000 | 7000 |
| Neha | 4000 | 4000 |
| Sneha | NULL | 0 |

### Example 4: Calculate total salary and bonus

```sql
SELECT
    first_name,
    salary,
    bonus,
    IFNULL(salary, 0) + IFNULL(bonus, 0) AS total_pay
FROM employees;
```

Expected result:

| first\_name | total\_pay |
| --- | --- |
| Amit | 70000 |
| Priya | 55000 |
| Rahul | 79000 |
| Neha | 4000 |
| Sneha | 58000 |

Here, missing salary or bonus values are treated as zero.

**Important:** This is only appropriate if zero is the intended business rule. A missing salary does not necessarily mean that the employee's salary is actually zero.

### When to use IFNULL()

- Replacing missing bonuses with zero.
- Displaying default values.
- Handling optional fields.
- Preventing NULL from propagating through arithmetic expressions.

---

## 3.2 COALESCE()

### Theory

`COALESCE()` returns the first non-NULL value from a list of expressions.

It is part of standard SQL and is supported by MySQL.

Unlike `IFNULL()`, it can check more than two values.

### Syntax

```
COALESCE(value1, value2, value3, ...);
```

The function returns the first non-NULL argument. If every argument is NULL, it returns NULL.

### Example 1: First available value

```sql
SELECT COALESCE(NULL, NULL, 100, 200) AS result;
```

Output:

| result |
| --- |
| 100 |

### Example 2: All values are NULL

```sql
SELECT COALESCE(NULL, NULL, NULL) AS result;
```

Output:

| result |
| --- |
| NULL |

### Example 3: Use a default city

```sql
SELECT
    first_name,
    COALESCE(city, 'City Not Available') AS city_name
FROM employees;
```

Expected result:

| first\_name | city\_name |
| --- | --- |
| Amit | Mumbai |
| Priya | Pune |
| Rahul | Delhi |
| Neha | Nashik |
| Sneha | City Not Available |

### Example 4: Use the first available contact number

Suppose a customer table contains both a mobile number and an alternate number.

```sql
SELECT
    COALESCE(mobile, alternate_phone, 'No Contact') AS contact_number
FROM customers;
```

The function checks:

1. `mobile`
2. `alternate_phone`
3. `'No Contact'`

It returns the first non-NULL value.

### Example 5: Combine COALESCE() with salary

```sql
SELECT
    first_name,
    COALESCE(salary, 0) AS salary_value
FROM employees;
```

Expected salary values:

| first\_name | salary\_value |
| --- | --- |
| Amit | 65000 |
| Priya | 55000 |
| Rahul | 72000 |
| Neha | 0 |
| Sneha | 58000 |

### IFNULL() vs COALESCE()

| Feature | IFNULL() | COALESCE() |
| --- | --- | --- |
| Arguments | Two | Two or more |
| Returns | First argument if non-NULL; otherwise replacement | First non-NULL argument |
| Standard SQL | No, MySQL-specific | Yes |
| Multiple fallbacks | Not directly | Yes |
| Example | `IFNULL(a, 0)` | `COALESCE(a, b, 0)` |

### When to use COALESCE()

- Choosing the first available phone number.
- Providing default values.
- Handling several optional columns.
- Writing SQL that is more portable across database systems.

---

## 3.3 NULLIF()

### Theory

`NULLIF()` compares two expressions.

- If both expressions are equal, it returns NULL.
- If they are different, it returns the first expression.

### Syntax

```
NULLIF(expression1, expression2)
```

### Example 1: Equal values

```sql
SELECT NULLIF(10, 10) AS result;
```

Output:

| result |
| --- |
| NULL |

### Example 2: Different values

```sql
SELECT NULLIF(10, 20) AS result;
```

Output:

| result |
| --- |
| 10 |

### Example 3: Avoid division by zero

Suppose you want to calculate profit margin as:

`profit / revenue`

If revenue is zero, division by zero must be avoided.

```sql
SELECT
    profit,
    revenue,
    profit / NULLIF(revenue, 0) AS profit_ratio
FROM sales;
```

If revenue is zero:

```sql
SELECT 100 / NULLIF(0, 0) AS result;
```

Output:

| result |
| --- |
| NULL |

If revenue is nonzero:

```sql
SELECT 100 / NULLIF(20, 0) AS result;
```

Output:

| result |
| --- |
| 5.0000 |

The exact numeric display can vary with the expression's data types.

### Example 4: Turn a zero bonus into NULL

```sql
SELECT
    first_name,
    NULLIF(bonus, 0) AS adjusted_bonus
FROM employees;
```

If a bonus is zero, the returned value becomes NULL. Existing nonzero bonuses remain unchanged.

### Important note

`NULLIF(a, b)` is conceptually equivalent to:

```
CASE
    WHEN a = b THEN NULL
    ELSE a
END
```

When the expressions involve NULL, the equality comparison is UNKNOWN, so the first expression is returned by the CASE-style logic unless equality evaluates to TRUE.

### When to use NULLIF()

- Avoiding division by zero.
- Converting special placeholder values to NULL.
- Treating zero or another sentinel as missing data.
- Preventing certain values from being included in subsequent calculations.

---

## 3.4 ISNULL()

### Theory

In MySQL, `ISNULL()` checks whether an expression is NULL.

It returns:

- `1` if the expression is NULL.
- `0` if the expression is not NULL.

### Syntax

```
ISNULL(expression)
```

### Example 1

```sql
SELECT ISNULL(NULL) AS result;
```

Output:

| result |
| --- |
| 1 |

### Example 2

```sql
SELECT ISNULL(100) AS result;
```

Output:

| result |
| --- |
| 0 |

### Example 3: Check missing bonuses

```sql
SELECT
    first_name,
    bonus,
    ISNULL(bonus) AS bonus_is_missing
FROM employees;
```

Expected result:

| first\_name | bonus | bonus\_is\_missing |
| --- | --- | --- |
| Amit | 5000 | 0 |
| Priya | NULL | 1 |
| Rahul | 7000 | 0 |
| Neha | 4000 | 0 |
| Sneha | NULL | 1 |

### Example 4: Count employees without a phone number

```sql
SELECT COUNT(*) AS employees_without_phone
FROM employees
WHERE ISNULL(phone) = 1;
```

Output:

| employees\_without\_phone |
| --- |
| 2 |

### Alternative: IS NULL

The following query produces the same result:

```sql
SELECT COUNT(*) AS employees_without_phone
FROM employees
WHERE phone IS NULL;
```

**Important:** `ISNULL()` differs across database systems. For example, SQL Server uses `ISNULL(expression, replacement)` to replace NULL, whereas MySQL uses `ISNULL(expression)` as a NULL test. Always check the database you are using.

### When to use ISNULL()

- Checking missing values in MySQL.
- Creating flags for missing data.
- Counting NULL values.
- Building conditional expressions.

---

## 3.5 IF()

### Theory

MySQL's `IF()` function evaluates a condition and returns one of two values.

- If the condition is TRUE, it returns the second argument.
- Otherwise, it returns the third argument.

### Syntax

```
IF(condition, value_if_true, value_if_false)
```

### Example 1

```sql
SELECT IF(10 > 5, 'Yes', 'No') AS result;
```

Output:

| result |
| --- |
| Yes |

### Example 2: Check whether a salary is missing

```sql
SELECT
    first_name,
    IF(salary IS NULL, 'Missing', 'Available') AS salary_status
FROM employees;
```

Expected result:

| first\_name | salary\_status |
| --- | --- |
| Amit | Available |
| Priya | Available |
| Rahul | Available |
| Neha | Missing |
| Sneha | Available |

### Example 3: Replace a NULL bonus

```sql
SELECT
    first_name,
    IF(bonus IS NULL, 0, bonus) AS final_bonus
FROM employees;
```

This provides a default bonus of zero for employees whose bonus is NULL.

### Example 4: Categorize missing and available phone numbers

```sql
SELECT
    first_name,
    IF(phone IS NULL, 'No Phone', phone) AS contact
FROM employees;
```

### When to use IF()

- Simple conditional outputs.
- Displaying missing-data labels.
- Replacing NULL based on a condition.
- Creating status columns.

**Note:** `IF()` is a MySQL-specific function. Use `CASE WHEN` for a standard SQL alternative.

---

## 3.6 CASE WHEN

### Theory

`CASE WHEN` is a conditional SQL expression rather than a dedicated NULL function.

It is especially useful when you need several conditions to handle NULL and other values.

### Syntax

```
CASE
    WHEN condition1 THEN result1
    WHEN condition2 THEN result2
    ELSE default_result
END
```

### Example 1: Check a missing salary

```sql
SELECT
    first_name,
    CASE
        WHEN salary IS NULL THEN 'Salary Missing'
        ELSE 'Salary Available'
    END AS salary_status
FROM employees;
```

Expected result:

| first\_name | salary\_status |
| --- | --- |
| Amit | Salary Available |
| Priya | Salary Available |
| Rahul | Salary Available |
| Neha | Salary Missing |
| Sneha | Salary Available |

### Example 2: Replace NULL salary with zero

```sql
SELECT
    first_name,
    CASE
        WHEN salary IS NULL THEN 0
        ELSE salary
    END AS salary_value
FROM employees;
```

### Example 3: Categorize salary

```sql
SELECT
    first_name,
    CASE
        WHEN salary IS NULL THEN 'Unknown'
        WHEN salary >= 70000 THEN 'High'
        WHEN salary >= 60000 THEN 'Medium'
        ELSE 'Low'
    END AS salary_category
FROM employees;
```

Expected result:

| first\_name | salary\_category |
| --- | --- |
| Amit | Medium |
| Priya | Low |
| Rahul | High |
| Neha | Unknown |
| Sneha | Low |

The NULL condition is checked first so missing salaries are not incorrectly classified as low salaries.

### When to use CASE WHEN()

- Multiple conditions.
- Business classifications.
- Custom NULL handling.
- Writing portable SQL.

---

# 4\. NULL CHECKING OPERATORS

`IS NULL` and `IS NOT NULL` are operators, not functions. They are essential for NULL handling.

## 4.1 IS NULL

Find records containing NULL.

```sql
SELECT *
FROM employees
WHERE salary IS NULL;
```

Output:

| emp\_id | first\_name | department | salary |
| --- | --- | --- | --- |
| 4 | Neha | Finance | NULL |

## 4.2 IS NOT NULL

Find records with available salaries.

```sql
SELECT *
FROM employees
WHERE salary IS NOT NULL;
```

Expected employees:

- Amit
- Priya
- Rahul
- Sneha

## 4.3 Count NULL values

```sql
SELECT COUNT(*) AS missing_salary_count
FROM employees
WHERE salary IS NULL;
```

Output:

| missing\_salary\_count |
| --- |
| 1 |

## 4.4 Count non-NULL values

```sql
SELECT COUNT(salary) AS available_salary_count
FROM employees;
```

Output:

| available\_salary\_count |
| --- |
| 4 |

`COUNT(column_name)` ignores NULL values.

## 4.5 Count NULL and non-NULL values together

```sql
SELECT
    COUNT(*) AS total_employees,
    COUNT(salary) AS employees_with_salary,
    COUNT(*) - COUNT(salary) AS employees_without_salary
FROM employees;
```

Output:

| total\_employees | employees\_with\_salary | employees\_without\_salary |
| --- | --- | --- |
| 5 | 4 | 1 |

---

# 5\. NULL FUNCTIONS WITH AGGREGATE FUNCTIONS

NULL handling is particularly important when using aggregate functions such as `SUM()`, `AVG()`, `COUNT()`, `MIN()`, and `MAX()`.

## 5.1 SUM() ignores NULL

```sql
SELECT SUM(bonus) AS total_bonus
FROM employees;
```

Output:

| total\_bonus |
| --- |
| 16000 |

Calculation:

`5000 + 7000 + 4000 = 16000`

The two NULL bonus values are ignored.

## 5.2 AVG() ignores NULL

```sql
SELECT AVG(bonus) AS average_bonus
FROM employees;
```

Output:

| average\_bonus |
| --- |
| 5333.333333 |

Calculation:

`16000 / 3 = 5333.333333...`

The denominator is three because only three bonus values are non-NULL.

## 5.3 Difference between COUNT(\*) and COUNT(column)

```sql
SELECT
    COUNT(*) AS total_rows,
    COUNT(bonus) AS recorded_bonuses
FROM employees;
```

Output:

| total\_rows | recorded\_bonuses |
| --- | --- |
| 5 | 3 |

- `COUNT(*)` counts all rows.
- `COUNT(bonus)` counts only rows where bonus is not NULL.

## 5.4 Using IFNULL() with SUM()

```sql
SELECT SUM(IFNULL(bonus, 0)) AS total_bonus
FROM employees;
```

Output:

| total\_bonus |
| --- |
| 16000 |

In this example, the result is the same as `SUM(bonus)` because SUM already ignores NULL values.

However, the expressions can behave differently in other contexts, particularly when every value is NULL.

## 5.5 SUM() when every value is NULL

```sql
SELECT SUM(value) AS total
FROM (
    SELECT NULL AS value
    UNION ALL
    SELECT NULL
) AS data_values;
```

Output:

| total |
| --- |
| NULL |

Now replace NULL with zero before aggregation:

```sql
SELECT SUM(IFNULL(value, 0)) AS total
FROM (
    SELECT NULL AS value
    UNION ALL
    SELECT NULL
) AS data_values;
```

Output:

| total |
| --- |
| 0 |

The difference is important:

- `SUM(column)` returns NULL if there are no non-NULL values.
- `SUM(IFNULL(column, 0))` returns zero if rows exist but every value is NULL.
- If the input contains no rows at all, `SUM()` still returns NULL.

## 5.6 AVG() with and without NULL replacement

```sql
SELECT
    AVG(bonus) AS average_recorded_bonus,
    AVG(IFNULL(bonus, 0)) AS average_bonus_treating_missing_as_zero
FROM employees;
```

Expected result, rounded for readability:

| average\_recorded\_bonus | average\_bonus\_treating\_missing\_as\_zero |
| --- | --- |
| 5333.33 | 3200.00 |

Explanation:

- `AVG(bonus)` calculates `16000 / 3`.
- `AVG(IFNULL(bonus, 0))` calculates `16000 / 5`.

These answers are different because the second query treats missing bonuses as zero. Choose the approach that matches your business rules.

---

# 6\. NULL FUNCTIONS IN SQL QUERIES

## 6.1 Using NULL functions in SELECT

```sql
SELECT
    first_name,
    IFNULL(city, 'Unknown') AS city
FROM employees;
```

This changes the displayed output, not the stored city value.

## 6.2 Using NULL conditions in WHERE

Find employees with missing phone numbers:

```sql
SELECT *
FROM employees
WHERE phone IS NULL;
```

## 6.3 Using COALESCE() in ORDER BY

```sql
SELECT first_name, city
FROM employees
ORDER BY COALESCE(city, 'ZZZ');
```

This places the NULL city according to the sort order of the replacement string. It is a simple technique, but the exact ordering depends on the chosen replacement and collation.

## 6.4 Using NULL functions in GROUP BY

Count employees by city, including employees whose city is missing.

```sql
SELECT
    COALESCE(city, 'Unknown') AS city_name,
    COUNT(*) AS total_employees
FROM employees
GROUP BY COALESCE(city, 'Unknown');
```

Expected result:

| city\_name | total\_employees |
| --- | --- |
| Delhi | 1 |
| Mumbai | 1 |
| Nashik | 1 |
| Pune | 1 |
| Unknown | 1 |

## 6.5 Using NULL functions in UPDATE

Suppose NULL bonuses should be stored as zero.

Preview the changes first:

```sql
SELECT
    first_name,
    bonus,
    IFNULL(bonus, 0) AS proposed_bonus
FROM employees;
```

Update the stored data:

```
UPDATE employees
SET bonus = 0
WHERE bonus IS NULL;
```

The `UPDATE` permanently changes matching records.

**Important:** Only do this if the business rules define a missing bonus as zero. Otherwise, you may lose the distinction between an unknown bonus and a genuine zero bonus.

---

# 7\. SOLVED SQL PRACTICE QUESTIONS

Use the `employees` table created earlier.

## Question 1: Find employees whose salary is NULL.

```sql
SELECT *
FROM employees
WHERE salary IS NULL;
```

Answer: Neha.

## Question 2: Find employees whose phone number is available.

```sql
SELECT *
FROM employees
WHERE phone IS NOT NULL;
```

Answer: Amit, Rahul, and Sneha.

## Question 3: Display NULL bonuses as zero.

```sql
SELECT
    first_name,
    IFNULL(bonus, 0) AS bonus
FROM employees;
```

Expected bonus values:

| first\_name | bonus |
| --- | --- |
| Amit | 5000 |
| Priya | 0 |
| Rahul | 7000 |
| Neha | 4000 |
| Sneha | 0 |

## Question 4: Display the city or 'Not Provided'.

```sql
SELECT
    first_name,
    COALESCE(city, 'Not Provided') AS city
FROM employees;
```

Answer: Sneha's city is displayed as `Not Provided`.

## Question 5: Find the first available value among three values.

```sql
SELECT COALESCE(NULL, NULL, 'SQL', 'MySQL') AS result;
```

Output:

| result |
| --- |
| SQL |

## Question 6: Convert zero to NULL.

```sql
SELECT NULLIF(0, 0) AS result;
```

Output: NULL.

## Question 7: Count employees with missing bonuses.

```sql
SELECT COUNT(*) AS missing_bonus_count
FROM employees
WHERE bonus IS NULL;
```

Output: `2`.

## Question 8: Count employees with recorded salaries.

```sql
SELECT COUNT(salary) AS recorded_salary_count
FROM employees;
```

Output: `4`.

## Question 9: Find the total recorded bonus.

```sql
SELECT SUM(bonus) AS total_bonus
FROM employees;
```

Output: `16000`.

## Question 10: Calculate the average recorded salary.

```sql
SELECT AVG(salary) AS average_salary
FROM employees;
```

Output:

| average\_salary |
| --- |
| 62500.000000 |

Calculation:

`(65000 + 55000 + 72000 + 58000) / 4 = 62500`

## Question 11: Calculate salary plus bonus, treating NULL as zero.

```sql
SELECT
    first_name,
    IFNULL(salary, 0) + IFNULL(bonus, 0) AS total_pay
FROM employees;
```

Output:

| first\_name | total\_pay |
| --- | --- |
| Amit | 70000 |
| Priya | 55000 |
| Rahul | 79000 |
| Neha | 4000 |
| Sneha | 58000 |

## Question 12: Display whether each employee has a salary.

```sql
SELECT
    first_name,
    IF(salary IS NULL, 'Missing', 'Available') AS salary_status
FROM employees;
```

Answer: Neha is marked `Missing`; the other employees are marked `Available`.

## Question 13: Find employees whose salary is greater than 60000.

```sql
SELECT *
FROM employees
WHERE salary > 60000;
```

Answer: Amit and Rahul.

A NULL salary does not satisfy this condition because the comparison is UNKNOWN.

## Question 14: Count employees by salary availability.

```sql
SELECT
    CASE
        WHEN salary IS NULL THEN 'Missing'
        ELSE 'Available'
    END AS salary_status,
    COUNT(*) AS total_employees
FROM employees
GROUP BY
    CASE
        WHEN salary IS NULL THEN 'Missing'
        ELSE 'Available'
    END;
```

Expected result:

| salary\_status | total\_employees |
| --- | --- |
| Available | 4 |
| Missing | 1 |

## Question 15: Find employees whose city is missing.

```sql
SELECT first_name
FROM employees
WHERE city IS NULL;
```

Answer: Sneha.

## Question 16: Display a fallback contact value.

```sql
SELECT
    first_name,
    COALESCE(phone, 'Contact Not Available') AS contact
FROM employees;
```

Employees without a phone number receive the fallback text.

## Question 17: Replace NULL salary with zero only in the result.

```sql
SELECT
    first_name,
    COALESCE(salary, 0) AS salary
FROM employees;
```

This does not modify the stored salary.

## Question 18: Calculate total salary, treating missing salaries as zero.

```sql
SELECT SUM(IFNULL(salary, 0)) AS total_salary
FROM employees;
```

Output:

| total\_salary |
| --- |
| 250000 |

## Question 19: Count rows with NULL city or NULL phone.

```sql
SELECT COUNT(*) AS incomplete_records
FROM employees
WHERE city IS NULL
   OR phone IS NULL;
```

Output:

| incomplete\_records |
| --- |
| 3 |

The matching employees are Priya, Neha, and Sneha.

## Question 20: Display a cleaned employee record.

```sql
SELECT
    emp_id,
    COALESCE(first_name, 'Unknown') AS first_name,
    COALESCE(department, 'Unassigned') AS department,
    COALESCE(salary, 0) AS salary,
    COALESCE(bonus, 0) AS bonus,
    COALESCE(phone, 'No Phone') AS phone,
    COALESCE(city, 'Unknown') AS city
FROM employees;
```

This query replaces missing values in the output without modifying the table.

---

# 8\. INTERVIEW QUESTIONS AND ANSWERS

## Q1. What is NULL in SQL?

NULL represents a missing, unknown, or unavailable value. It is not equivalent to zero or an empty string.

## Q2. What are NULL functions?

NULL functions handle missing values by replacing them, choosing alternatives, or converting particular values into NULL.

Examples include `IFNULL()`, `COALESCE()`, and `NULLIF()`.

## Q3. What is the difference between IFNULL() and COALESCE()?

- `IFNULL()` checks one expression and provides one fallback.
- `COALESCE()` returns the first non-NULL value from two or more expressions.

```sql
SELECT IFNULL(NULL, 10);
```

Output: `10`.

```sql
SELECT COALESCE(NULL, NULL, 10, 20);
```

Output: `10`.

## Q4. What is NULLIF()?

`NULLIF(a, b)` returns NULL if `a` equals `b`; otherwise, it returns `a`.

```sql
SELECT NULLIF(5, 5);
```

Output: NULL.

## Q5. What is the difference between IS NULL and ISNULL()?

In MySQL:

- `IS NULL` is a NULL-checking operator.
- `ISNULL(expression)` is a function returning `1` for NULL and `0` otherwise.

Both can be used to test for missing values.

## Q6. Why does `salary = NULL` not work?

SQL comparisons involving NULL generally evaluate to UNKNOWN. Use:

```
WHERE salary IS NULL
```

## Q7. Does COUNT(\*) count NULL values?

`COUNT(*)` counts rows, including rows with NULL values.

`COUNT(column)` counts only non-NULL values in that column.

## Q8. Does SUM() ignore NULL?

Yes. `SUM()` ignores NULL values. If there are no non-NULL values to aggregate, the result is NULL.

## Q9. Does AVG() ignore NULL?

Yes. `AVG()` calculates the average using non-NULL values only.

## Q10. How do you replace NULL with zero?

```sql
SELECT IFNULL(salary, 0)
FROM employees;
```

Alternatively:

```sql
SELECT COALESCE(salary, 0)
FROM employees;
```

## Q11. How do you prevent division by zero?

```sql
SELECT profit / NULLIF(revenue, 0) AS profit_ratio
FROM sales;
```

When revenue is zero, the denominator becomes NULL.

## Q12. What happens if every argument in COALESCE() is NULL?

The result is NULL.

```sql
SELECT COALESCE(NULL, NULL, NULL);
```

## Q13. What is the difference between NULL and an empty string?

NULL means missing or unknown. An empty string is a known string containing zero characters.

```sql
SELECT
    NULL IS NULL AS null_check,
    '' IS NULL AS empty_string_check;
```

Output:

| null\_check | empty\_string\_check |
| --- | --- |
| 1 | 0 |

## Q14. Can NULL values be used in arithmetic?

Yes, but arithmetic expressions involving NULL generally return NULL.

```sql
SELECT 100 + NULL AS result;
```

Output: NULL.

## Q15. Does IFNULL() permanently update a table?

No. It changes the value returned by the query. To permanently update records, use an `UPDATE` statement.

## Q16. What is the difference between IF() and CASE WHEN?

`IF()` is a MySQL-specific function for a simple true/false condition. `CASE WHEN` supports multiple conditions and is standard SQL.

## Q17. Can COALESCE() handle multiple columns?

Yes.

```sql
SELECT COALESCE(phone, alternate_phone, emergency_phone, 'No Contact')
FROM customers;
```

It returns the first non-NULL value.

## Q18. Why should missing salary not always be replaced with zero?

A missing salary may mean the value is unknown, whereas zero means the salary is actually zero. Replacing NULL with zero can affect averages, reports, and business decisions.

---

# 9\. COMMON MISTAKES TO AVOID

## Mistake 1: Comparing NULL with equals

Incorrect:

```
WHERE salary = NULL
```

Correct:

```
WHERE salary IS NULL
```

## Mistake 2: Assuming NULL is zero

NULL is unknown or missing. Zero is a valid numeric value.

## Mistake 3: Confusing NULL with an empty string

```sql
SELECT
    NULL IS NULL AS is_null,
    '' IS NULL AS is_empty_string_null;
```

Output: `1` and `0`.

## Mistake 4: Ignoring NULL in arithmetic

```sql
SELECT 100 + NULL;
```

The result is NULL.

Handle missing operands only when the intended business rule supports a fallback.

## Mistake 5: Replacing every NULL with zero

This may produce misleading averages, totals, or reports.

Choose a default based on the meaning of the data.

## Mistake 6: Assuming COUNT(column) counts all rows

```sql
SELECT COUNT(bonus)
FROM employees;
```

This excludes NULL bonuses.

Use `COUNT(*)` when you want to count rows.

## Mistake 7: Forgetting that COALESCE() returns the first non-NULL value

```sql
SELECT COALESCE(NULL, 0, 100);
```

Output: `0`, not `100`.

## Mistake 8: Confusing database-specific syntax

MySQL's `ISNULL(expression)` tests whether a value is NULL. SQL Server's `ISNULL(expression, replacement)` replaces NULL.

Check the syntax for your database engine.

---

# 10\. QUICK REVISION CHEAT SHEET

| Function / Operator | Purpose | Example | Output |
| --- | --- | --- | --- |
| `IFNULL()` | Replace NULL with a fallback | `IFNULL(NULL, 10)` | `10` |
| `COALESCE()` | Return first non-NULL value | `COALESCE(NULL, 5, 10)` | `5` |
| `NULLIF()` | Return NULL when values are equal | `NULLIF(5, 5)` | `NULL` |
| `ISNULL()` | Test for NULL in MySQL | `ISNULL(NULL)` | `1` |
| `IF()` | Conditional result | `IF(1=1,'Yes','No')` | `Yes` |
| `CASE WHEN` | Handle multiple conditions | `CASE WHEN 1=1 THEN 'Yes' ELSE 'No' END` | `Yes` |
| `IS NULL` | Find missing values | `salary IS NULL` | TRUE for NULL |
| `IS NOT NULL` | Find available values | `salary IS NOT NULL` | TRUE for non-NULL |
| `COUNT(*)` | Count all rows | `COUNT(*)` | Includes NULL-containing rows |
| `COUNT(column)` | Count non-NULL values | `COUNT(salary)` | Excludes NULL |
| `SUM(column)` | Add non-NULL values | `SUM(bonus)` | Ignores NULL |
| `AVG(column)` | Average non-NULL values | `AVG(salary)` | Ignores NULL |

---

# 11\. FINAL LEARNING ROADMAP

Learn NULL handling in this order:

1. Understand NULL, zero, empty strings, and three-valued logic.
2. Master `IS NULL` and `IS NOT NULL`.
3. Learn `IFNULL()` and `COALESCE()`.
4. Understand `NULLIF()` and division-by-zero prevention.
5. Practice `IF()` and `CASE WHEN`.
6. Combine NULL handling with `COUNT()`, `SUM()`, and `AVG()`.
7. Practice missing-data analysis using `WHERE`, `GROUP BY`, and `UPDATE`.

## Most important concepts for interviews

- `IFNULL()` vs `COALESCE()`
- `NULLIF()` and division by zero
- `IS NULL` vs `= NULL`
- `COUNT(*)` vs `COUNT(column)`
- How NULL affects arithmetic and aggregate functions
- NULL vs zero vs empty string
- MySQL-specific functions vs standard SQL

**Best practice:** Handle NULL according to the meaning of your data. Replacing missing values with zero or a default string is not always correct.

**End of SQL NULL Functions Notes.**
