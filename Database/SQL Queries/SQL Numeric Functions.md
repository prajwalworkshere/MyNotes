**MySQL 8.0+ | Theory + Syntax + Examples + Outputs + Practice Questions**

## TABLE OF CONTENTS

1. Introduction to Numeric Functions
2. Demo Database and Sample Data
3. SQL Numeric Functions A–Z
4. How to Use Numeric Functions in Queries
5. Solved SQL Practice Questions
6. Interview Questions and Answers
7. Common Mistakes
8. Quick Revision Cheat Sheet
9. Final Learning Roadmap

---

# 1\. INTRODUCTION TO NUMERIC FUNCTIONS

## What is a Numeric Value?

A numeric value is a number used for mathematical calculations and comparisons.

Examples:

```
10
-25
99.99
3.14159
```

In SQL, numeric data is commonly stored using data types such as:

- `INT` — integers
- `DECIMAL(p,s)` — exact decimal numbers
- `FLOAT` — approximate floating-point numbers
- `DOUBLE` — approximate floating-point numbers with greater precision than typical single-precision values

## What are SQL Numeric Functions?

Numeric functions are built-in SQL functions that perform mathematical operations on numeric values.

They can round numbers, calculate absolute values, find square roots, generate random numbers, calculate powers, and perform other mathematical operations.

Example:

```sql
SELECT ABS(-25) AS result;
```

Output:

| result |
| --- |
| 25 |

## Why do we use Numeric Functions?

Numeric functions are useful for:

- Calculating percentages and discounts.
- Rounding salaries, prices, and averages.
- Calculating square roots and powers.
- Finding the absolute difference between numbers.
- Generating random values for testing.
- Calculating remainders.
- Preparing financial and statistical reports.
- Formatting numeric results for analysis.

## General Syntax

```sql
SELECT FUNCTION_NAME(number);
```

Example:

```sql
SELECT ROUND(45.678, 2) AS result;
```

Output:

| result |
| --- |
| 45.68 |

**Important:** These notes use MySQL 8.0+. Some numeric functions and their syntax differ across MySQL, PostgreSQL, SQL Server, and Oracle.

---

# 2\. DEMO DATABASE AND SAMPLE DATA

We will use the following `employees` table throughout these notes.

## Create the table

```sql
CREATE TABLE employees (
    emp_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    department VARCHAR(50),
    salary DECIMAL(10,2),
    bonus DECIMAL(10,2)
);
```

## Insert sample records

```sql
INSERT INTO employees
(emp_id, first_name, last_name, department, salary, bonus)
VALUES
(1, 'Amit', 'Sharma', 'IT', 65000.75, 5000.50),
(2, 'Priya', 'Patil', 'HR', 55000.40, 3000.25),
(3, 'Rahul', 'Verma', 'IT', 72000.90, 7000.00),
(4, 'Neha', 'Joshi', 'Finance', 60000.25, 4500.75),
(5, 'Sneha', 'Kulkarni', 'HR', 58000.60, 3500.50);
```

Check the data:

```sql
SELECT * FROM employees;
```

Expected data:

| emp\_id | first\_name | department | salary | bonus |
| --- | --- | --- | --- | --- |
| 1 | Amit | IT | 65000.75 | 5000.50 |
| 2 | Priya | HR | 55000.40 | 3000.25 |
| 3 | Rahul | IT | 72000.90 | 7000.00 |
| 4 | Neha | Finance | 60000.25 | 4500.75 |
| 5 | Sneha | HR | 58000.60 | 3500.50 |

---

# 3\. SQL NUMERIC FUNCTIONS A–Z

## A — ABS()

### Theory

`ABS()` returns the absolute value of a number. The absolute value is the non-negative magnitude of a number, ignoring its sign.

### Syntax

```sql
ABS(number);
```

### Example

```sql
SELECT ABS(-25) AS result;
```

Output:

| result |
| --- |
| 25 |

Another example:

```sql
SELECT ABS(18) AS result;
```

Output:

| result |
| --- |
| 18 |

### Real query

Find the absolute difference between salary and 60000:

```sql
SELECT
    first_name,
    salary,
    ABS(salary - 60000) AS salary_difference
FROM employees;
```

### Use

- Calculating absolute differences.
- Comparing deviations from a target.
- Working with positive magnitudes of values.

---

## C — CEIL() / CEILING()

### Theory

`CEIL()` returns the smallest integer greater than or equal to a number.

`CEILING()` is an equivalent function in MySQL.

### Syntax

```sql
CEIL(number);
CEILING(number);
```

### Example

```sql
SELECT CEIL(12.3) AS result;
```

Output:

| result |
| --- |
| 13 |

Another example:

```sql
SELECT CEIL(-12.3) AS result;
```

Output:

| result |
| --- |
| -12 |

**Important:** Ceiling does not always mean rounding away from zero. It means moving upward toward positive infinity.

### Real query

```sql
SELECT
    first_name,
    salary,
    CEIL(salary) AS rounded_salary
FROM employees;
```

Example output:

| first\_name | salary | rounded\_salary |
| --- | --- | --- |
| Amit | 65000.75 | 65001 |
| Priya | 55000.40 | 55001 |

### Use

- Calculating the number of boxes required for a quantity.
- Rounding a required count upward.
- Calculating minimum whole units needed.

---

## D — DEGREES()

### Theory

`DEGREES()` converts an angle from radians to degrees.

### Syntax

```sql
DEGREES(radians);
```

### Example

```sql
SELECT DEGREES(PI()) AS result;
```

Output:

| result |
| --- |
| 180 |

Another example:

```sql
SELECT DEGREES(PI() / 2) AS result;
```

Output:

| result |
| --- |
| 90 |

### Use

- Converting mathematical angles.
- Scientific and engineering calculations.
- Working with trigonometric values.

---

## E — EXP()

### Theory

`EXP()` returns the value of Euler's number raised to the specified power.

Mathematically:

$$
\operatorname{EXP}(x)=e^x
$$

Here, $e$ is approximately 2.71828.

### Syntax

```sql
EXP(number);
```

### Example

```sql
SELECT EXP(1) AS result;
```

Output:

| result |
| --- |
| Approximately 2.718282 |

Another example:

```sql
SELECT EXP(0) AS result;
```

Output:

| result |
| --- |
| 1 |

### Use

- Exponential growth calculations.
- Scientific calculations.
- Mathematical and statistical models.

---

## F — FLOOR()

### Theory

`FLOOR()` returns the largest integer less than or equal to a number.

### Syntax

```sql
FLOOR(number);
```

### Example

```sql
SELECT FLOOR(12.9) AS result;
```

Output:

| result |
| --- |
| 12 |

Negative example:

```sql
SELECT FLOOR(-12.3) AS result;
```

Output:

| result |
| --- |
| -13 |

### Difference between FLOOR() and CEIL()

```sql
SELECT
    FLOOR(12.7) AS floor_value,
    CEIL(12.7) AS ceil_value;
```

Output:

| floor\_value | ceil\_value |
| --- | --- |
| 12 | 13 |

### Real query

```sql
SELECT
    first_name,
    salary,
    FLOOR(salary) AS whole_salary
FROM employees;
```

### Use

- Rounding down to a whole number.
- Calculating completed units.
- Determining the number of complete groups.

---

## G — GREATEST()

### Theory

`GREATEST()` returns the largest value among two or more expressions.

### Syntax

```sql
GREATEST(value1, value2, value3, ...);
```

### Example

```sql
SELECT GREATEST(10, 25, 18, 40) AS result;
```

Output:

| result |
| --- |
| 40 |

### Real query

Find the greater value between salary and bonus for each employee:

```sql
SELECT
    first_name,
    salary,
    bonus,
    GREATEST(salary, bonus) AS greater_amount
FROM employees;
```

### Use

- Comparing multiple numeric values.
- Selecting the largest amount.
- Applying thresholds and business rules.

**MySQL note:** `GREATEST()` returns `NULL` if any argument is `NULL`.

---

## L — LEAST()

### Theory

`LEAST()` returns the smallest value among two or more expressions.

### Syntax

```sql
LEAST(value1, value2, value3, ...);
```

### Example

```sql
SELECT LEAST(10, 25, 18, 40) AS result;
```

Output:

| result |
| --- |
| 10 |

### Real query

```sql
SELECT
    first_name,
    salary,
    bonus,
    LEAST(salary, bonus) AS smaller_amount
FROM employees;
```

### Use

- Finding the minimum of multiple values.
- Applying price limits.
- Comparing amounts against a cap.

**MySQL note:** `LEAST()` returns `NULL` if any argument is `NULL`.

---

## L — LOG()

### Theory

`LOG()` calculates a logarithm.

In MySQL, `LOG(x)` calculates the natural logarithm of `x`, meaning the logarithm with base $e$.

MySQL also supports `LOG(base, number)`.

### Syntax

```sql
LOG(number);
LOG(base, number);
```

### Example 1: Natural logarithm

```sql
SELECT LOG(1) AS result;
```

Output:

| result |
| --- |
| 0 |

### Example 2: Logarithm with a base

```sql
SELECT LOG(10, 100) AS result;
```

Output:

| result |
| --- |
| 2 |

Explanation:

$$
10^2=100
$$

### Use

- Scientific calculations.
- Statistical transformations.
- Logarithmic models.

**Important:** The number and base must be valid for the logarithm. Invalid inputs can produce `NULL` or errors depending on the function and SQL mode.

---

## L — LOG10()

### Theory

`LOG10()` calculates the base-10 logarithm of a number.

### Syntax

```sql
LOG10(number);
```

### Example

```sql
SELECT LOG10(1000) AS result;
```

Output:

| result |
| --- |
| 3 |

Explanation:

$$
10^3=1000
$$

### Use

- Scientific calculations.
- Working with powers of ten.
- Logarithmic data analysis.

---

## L — LOG2()

### Theory

`LOG2()` calculates the base-2 logarithm of a number.

### Syntax

```sql
LOG2(number);
```

### Example

```sql
SELECT LOG2(8) AS result;
```

Output:

| result |
| --- |
| 3 |

Explanation:

$$
2^3=8
$$

### Use

- Binary calculations.
- Information theory.
- Computer science and algorithm analysis.

---

## M — MOD()

### Theory

`MOD()` returns the remainder after dividing one number by another.

### Syntax

```sql
MOD(number, divisor);
```

The `%` operator can also be used in MySQL.

### Example

```sql
SELECT MOD(10, 3) AS result;
```

Output:

| result |
| --- |
| 1 |

Explanation:

10 divided by 3 gives a quotient of 3 and a remainder of 1.

### Another example

```sql
SELECT MOD(20, 5) AS result;
```

Output:

| result |
| --- |
| 0 |

### Real query: Find even employee IDs

```sql
SELECT *
FROM employees
WHERE MOD(emp_id, 2) = 0;
```

Expected employee IDs:

| emp\_id |
| --- |
| 2 |
| 4 |

### Real query: Find odd employee IDs

```sql
SELECT *
FROM employees
WHERE MOD(emp_id, 2) = 1;
```

Expected employee IDs:

| emp\_id |
| --- |
| 1 |
| 3 |
| 5 |

### Use

- Finding even and odd numbers.
- Divisibility checks.
- Repeating schedules and grouping patterns.

**Important:** Division or modulo by zero is invalid or produces `NULL` depending on the expression and SQL mode.

---

## P — PI()

### Theory

`PI()` returns the mathematical constant pi.

### Syntax

```sql
PI();
```

### Example

```sql
SELECT PI() AS result;
```

Output:

| result |
| --- |
| Approximately 3.141593 |

### Real query: Calculate the area of a circle

The area formula is:

$$
A=\pi r^2
$$

```sql
SELECT PI() * POWER(5, 2) AS circle_area;
```

Output:

| circle\_area |
| --- |
| Approximately 78.539816 |

### Use

- Geometry calculations.
- Scientific calculations.
- Circle area and circumference formulas.

---

## P — POWER()

### Theory

`POWER()` raises a number to a specified exponent.

`POW()` is a synonym in MySQL.

### Syntax

```sql
POWER(number, exponent);
POW(number, exponent);
```

### Example

```sql
SELECT POWER(2, 3) AS result;
```

Output:

| result |
| --- |
| 8 |

Explanation:

$$
2^3=2\times2\times2=8
$$

### Another example

```sql
SELECT POWER(5, 2) AS result;
```

Output:

| result |
| --- |
| 25 |

### Real query

```sql
SELECT
    first_name,
    POWER(emp_id, 2) AS employee_id_squared
FROM employees;
```

### Use

- Exponents and mathematical formulas.
- Area and volume calculations.
- Scientific and financial models.

---

## R — RADIANS()

### Theory

`RADIANS()` converts an angle from degrees to radians.

### Syntax

```sql
RADIANS(degrees);
```

### Example

```sql
SELECT RADIANS(180) AS result;
```

Output:

| result |
| --- |
| Approximately 3.141593 |

Another example:

```sql
SELECT RADIANS(90) AS result;
```

Output:

| result |
| --- |
| Approximately 1.570796 |

### Use

- Trigonometric calculations.
- Scientific applications.
- Converting degree-based measurements for mathematical functions.

---

## R — RAND()

### Theory

`RAND()` generates a pseudo-random floating-point value greater than or equal to 0 and less than 1.

### Syntax

```sql
RAND();
RAND(seed);
```

### Example

```sql
SELECT RAND() AS result;
```

Output:

A different pseudo-random value may be returned each time, for example:

| result |
| --- |
| 0.713245 |

The exact output is not fixed.

### Example with a seed

```sql
SELECT RAND(10) AS result;
```

A supplied seed makes the sequence reproducible under the same conditions, but do not treat it as a cryptographic random generator.

### Real query: Generate random values

```sql
SELECT
    emp_id,
    first_name,
    RAND() AS random_value
FROM employees;
```

### Real query: Select a random employee

```sql
SELECT *
FROM employees
ORDER BY RAND()
LIMIT 1;
```

### Use

- Test-data generation.
- Randomized demonstrations.
- Simple random sampling for small datasets.

**Performance warning:** `ORDER BY RAND()` can be expensive on large tables. Use a more suitable sampling strategy for large production datasets.

**Security warning:** Do not use `RAND()` for passwords, tokens, or cryptographic secrets.

---

## R — ROUND()

### Theory

`ROUND()` rounds a number to a specified number of decimal places.

If the decimal-place argument is omitted, MySQL rounds to the nearest integer.

### Syntax

```sql
ROUND(number);
ROUND(number, decimal_places);
```

### Example 1: Round to an integer

```sql
SELECT ROUND(12.6) AS result;
```

Output:

| result |
| --- |
| 13 |

### Example 2: Round to two decimal places

```sql
SELECT ROUND(12.3456, 2) AS result;
```

Output:

| result |
| --- |
| 12.35 |

### Example 3: Round to one decimal place

```sql
SELECT ROUND(45.678, 1) AS result;
```

Output:

| result |
| --- |
| 45.7 |

### Example 4: Round to the left of the decimal point

```sql
SELECT ROUND(1234, -2) AS result;
```

Output:

| result |
| --- |
| 1200 |

### Real query

```sql
SELECT
    first_name,
    salary,
    ROUND(salary, 0) AS rounded_salary
FROM employees;
```

### Use

- Displaying salaries and prices.
- Calculating rounded averages.
- Preparing reports.
- Formatting numerical results.

**Important:** Rounding behavior can depend on the numeric type and value representation. For financial calculations, use an appropriate `DECIMAL` data type.

---

## S — SIGN()

### Theory

`SIGN()` identifies whether a number is negative, zero, or positive.

### Syntax

```sql
SIGN(number);
```

### Example

```sql
SELECT
    SIGN(-25) AS negative_value,
    SIGN(0) AS zero_value,
    SIGN(25) AS positive_value;
```

Output:

| negative\_value | zero\_value | positive\_value |
| --- | --- | --- |
| -1 | 0 | 1 |

### Real query

```sql
SELECT
    salary,
    SIGN(salary - 60000) AS salary_status
FROM employees;
```

Interpretation:

- `-1`: salary is below 60000.
- `0`: salary equals 60000.
- `1`: salary is above 60000.

### Use

- Identifying positive and negative differences.
- Comparing values against a target.
- Simple numerical classification.

---

## S — SQRT()

### Theory

`SQRT()` returns the square root of a number.

The square root of a number is a value that, when multiplied by itself, produces the original number.

### Syntax

```sql
SQRT(number);
```

### Example

```sql
SELECT SQRT(64) AS result;
```

Output:

| result |
| --- |
| 8 |

Another example:

```sql
SELECT SQRT(2) AS result;
```

Output:

| result |
| --- |
| Approximately 1.414214 |

### Use

- Geometry.
- Scientific calculations.
- Statistical formulas.
- Mathematical models.

**Important:** A negative input does not produce a real-number square root; MySQL returns `NULL` for invalid real-domain input.

---

## T — TAN()

### Theory

`TAN()` returns the tangent of an angle expressed in radians.

### Syntax

```sql
TAN(radians);
```

### Example

```sql
SELECT TAN(0) AS result;
```

Output:

| result |
| --- |
| 0 |

### Example using degrees

Convert the angle to radians first:

```sql
SELECT TAN(RADIANS(45)) AS result;
```

Output:

| result |
| --- |
| Approximately 1 |

### Use

- Trigonometry.
- Engineering calculations.
- Scientific applications.

---

## T — TRUNCATE()

### Theory

`TRUNCATE()` cuts off digits beyond a specified decimal position without rounding them.

### Syntax

```sql
TRUNCATE(number, decimal_places);
```

### Example 1

```sql
SELECT TRUNCATE(12.9876, 2) AS result;
```

Output:

| result |
| --- |
| 12.98 |

### Example 2

```sql
SELECT TRUNCATE(12.9876, 0) AS result;
```

Output:

| result |
| --- |
| 12 |

### Example 3: Negative decimal places

```sql
SELECT TRUNCATE(1234, -2) AS result;
```

Output:

| result |
| --- |
| 1200 |

### Difference between ROUND() and TRUNCATE()

```sql
SELECT
    ROUND(12.987, 2) AS rounded_value,
    TRUNCATE(12.987, 2) AS truncated_value;
```

Output:

| rounded\_value | truncated\_value |
| --- | --- |
| 12.99 | 12.98 |

### Use

- Removing unwanted decimal digits.
- Preparing numerical displays.
- Truncating values according to reporting requirements.

**Important:** Truncating a displayed value does not automatically make it appropriate for financial calculations. Understand the required rounding policy before using it in monetary calculations.

---

# 4\. ADDITIONAL MATHEMATICAL FUNCTIONS

The following MySQL functions are useful for specialized calculations.

## 4.1 ACOS()

Returns the inverse cosine of a number.

```sql
SELECT ACOS(1) AS result;
```

Output:

| result |
| --- |
| 0 |

The input must be in the range -1 to 1 for a real result.

## 4.2 ASIN()

Returns the inverse sine of a number.

```sql
SELECT ASIN(0) AS result;
```

Output:

| result |
| --- |
| 0 |

## 4.3 ATAN()

Returns the inverse tangent of a number.

```sql
SELECT ATAN(1) AS result;
```

Output:

| result |
| --- |
| Approximately 0.785398 |

This is approximately $\pi/4$ radians.

## 4.4 ATAN2()

Calculates the angle from two coordinates.

```sql
SELECT ATAN2(1, 1) AS result;
```

Output:

| result |
| --- |
| Approximately 0.785398 |

## 4.5 COS()

Returns the cosine of an angle in radians.

```sql
SELECT COS(0) AS result;
```

Output:

| result |
| --- |
| 1 |

## 4.6 COT()

Returns the cotangent of an angle in radians.

```sql
SELECT COT(PI() / 4) AS result;
```

Output:

| result |
| --- |
| Approximately 1 |

## 4.7 SIN()

Returns the sine of an angle in radians.

```sql
SELECT SIN(0) AS result;
```

Output:

| result |
| --- |
| 0 |

## 4.8 CONV()

Converts a number between different number bases.

### Syntax

```sql
CONV(number, from_base, to_base);
```

Example:

```sql
SELECT CONV('1010', 2, 10) AS result;
```

Output:

| result |
| --- |
| 10 |

Explanation: Binary `1010` equals decimal `10`.

## 4.9 CRC32()

Calculates a CRC-32 checksum.

```sql
SELECT CRC32('hello') AS result;
```

This returns a numeric checksum. It is deterministic for the same input, but it is not a cryptographic hash.

Use: Basic integrity checks and certain technical data-processing tasks.

## 4.10 FORMAT()

Formats a number with a specified number of decimal places and grouping separators.

### Syntax

```sql
FORMAT(number, decimal_places);
```

Example:

```sql
SELECT FORMAT(1234567.891, 2) AS result;
```

Output under an English-style locale:

| result |
| --- |
| 1,234,567.89 |

**Important:** `FORMAT()` returns a string, not a numeric value. Use it mainly for presentation rather than further arithmetic.

---

# 5\. HOW NUMERIC FUNCTIONS ARE USED IN SQL QUERIES

## 5.1 Using a Numeric Function in SELECT

Requirement: Display salaries rounded to two decimal places.

```sql
SELECT
    first_name,
    ROUND(salary, 2) AS rounded_salary
FROM employees;
```

Explanation: `ROUND()` changes the returned representation of the salary, not the stored value.

## 5.2 Using a Numeric Function in WHERE

Requirement: Find employees whose salary exceeds 60000.

```sql
SELECT *
FROM employees
WHERE salary > 60000;
```

Numeric functions can also be used in conditions:

```sql
SELECT *
FROM employees
WHERE MOD(emp_id, 2) = 0;
```

This returns employees with even IDs.

## 5.3 Using Numeric Functions in ORDER BY

Requirement: Sort employees by salary rounded to the nearest whole number.

```sql
SELECT
    first_name,
    salary
FROM employees
ORDER BY ROUND(salary, 0) DESC;
```

## 5.4 Using Numeric Functions with Arithmetic

Requirement: Calculate the salary after adding the bonus.

```sql
SELECT
    first_name,
    salary,
    bonus,
    salary + bonus AS total_compensation
FROM employees;
```

Example output:

| first\_name | salary | bonus | total\_compensation |
| --- | --- | --- | --- |
| Amit | 65000.75 | 5000.50 | 70001.25 |
| Priya | 55000.40 | 3000.25 | 58000.65 |

## 5.5 Calculating a Percentage

Requirement: Calculate a 10% bonus based on salary.

```sql
SELECT
    first_name,
    salary,
    ROUND(salary * 0.10, 2) AS calculated_bonus
FROM employees;
```

Example output:

| first\_name | salary | calculated\_bonus |
| --- | --- | --- |
| Amit | 65000.75 | 6500.08 |
| Priya | 55000.40 | 5500.04 |

## 5.6 Using Numeric Functions with Aggregates

Requirement: Calculate the average salary rounded to two decimal places.

```sql
SELECT ROUND(AVG(salary), 2) AS average_salary
FROM employees;
```

Expected output:

| average\_salary |
| --- |
| 62019.58 |

`AVG()` is an aggregate function, while `ROUND()` is a numeric function. They can be combined in the same expression.

## 5.7 Using Numeric Functions in UPDATE

Requirement: Round stored salaries to whole numbers.

```
UPDATE employees
SET salary = ROUND(salary, 0);
```

This modifies the stored values. It is different from using `ROUND()` in a `SELECT` query.

**Warning:** The example affects every row. Back up important data and use a suitable `WHERE` condition when only selected records should change.

---

# 6\. SOLVED SQL PRACTICE QUESTIONS

Use the `employees` table created earlier.

## Question 1: Display salaries rounded to the nearest integer.

```sql
SELECT
    first_name,
    ROUND(salary, 0) AS rounded_salary
FROM employees;
```

## Question 2: Display salaries rounded to two decimal places.

```sql
SELECT
    first_name,
    ROUND(salary, 2) AS rounded_salary
FROM employees;
```

## Question 3: Find the absolute difference between salary and 60000.

```sql
SELECT
    first_name,
    ABS(salary - 60000) AS salary_difference
FROM employees;
```

## Question 4: Find employees with even IDs.

```sql
SELECT *
FROM employees
WHERE MOD(emp_id, 2) = 0;
```

Expected IDs: `2`, `4`.

## Question 5: Find employees with odd IDs.

```sql
SELECT *
FROM employees
WHERE MOD(emp_id, 2) = 1;
```

Expected IDs: `1`, `3`, `5`.

## Question 6: Calculate a 5% bonus for every employee.

```sql
SELECT
    first_name,
    salary,
    ROUND(salary * 0.05, 2) AS bonus_5_percent
FROM employees;
```

## Question 7: Calculate total compensation.

```sql
SELECT
    first_name,
    salary,
    bonus,
    salary + bonus AS total_compensation
FROM employees;
```

## Question 8: Find the greater value between salary and bonus.

```sql
SELECT
    first_name,
    GREATEST(salary, bonus) AS greater_amount
FROM employees;
```

## Question 9: Find the smaller value between salary and bonus.

```sql
SELECT
    first_name,
    LEAST(salary, bonus) AS smaller_amount
FROM employees;
```

## Question 10: Display the square of every employee ID.

```sql
SELECT
    emp_id,
    POWER(emp_id, 2) AS squared_id
FROM employees;
```

Expected values: `1`, `4`, `9`, `16`, `25`.

## Question 11: Find the square root of each employee ID.

```sql
SELECT
    emp_id,
    SQRT(emp_id) AS square_root
FROM employees;
```

## Question 12: Round salary upward to the next integer.

```sql
SELECT
    first_name,
    CEIL(salary) AS salary_ceiling
FROM employees;
```

## Question 13: Round salary downward to an integer.

```sql
SELECT
    first_name,
    FLOOR(salary) AS salary_floor
FROM employees;
```

## Question 14: Truncate salary to one decimal place.

```sql
SELECT
    first_name,
    TRUNCATE(salary, 1) AS truncated_salary
FROM employees;
```

## Question 15: Display the sign of the difference between salary and 60000.

```sql
SELECT
    first_name,
    SIGN(salary - 60000) AS salary_status
FROM employees;
```

## Question 16: Calculate the average salary rounded to two decimal places.

```sql
SELECT
    ROUND(AVG(salary), 2) AS average_salary
FROM employees;
```

Expected output:

| average\_salary |
| --- |
| 62019.58 |

## Question 17: Find employees whose salary is divisible by 1000.

```sql
SELECT *
FROM employees
WHERE MOD(salary, 1000) = 0;
```

With the sample data, no employee salary is an exact multiple of 1000.

## Question 18: Calculate a 12% salary increase.

```sql
SELECT
    first_name,
    salary,
    ROUND(salary * 1.12, 2) AS salary_after_increase
FROM employees;
```

## Question 19: Calculate the total compensation after a 10% salary increase, keeping the bonus unchanged.

```sql
SELECT
    first_name,
    ROUND(salary * 1.10 + bonus, 2) AS revised_compensation
FROM employees;
```

## Question 20: Display the salary difference from the highest salary.

```sql
SELECT
    first_name,
    salary,
    (SELECT MAX(salary) FROM employees) - salary AS difference_from_highest
FROM employees;
```

## Question 21: Format salaries with two decimal places and separators.

```sql
SELECT
    first_name,
    FORMAT(salary, 2) AS formatted_salary
FROM employees;
```

## Question 22: Calculate the area of a circle with radius 10.

```sql
SELECT PI() * POWER(10, 2) AS circle_area;
```

Expected result: approximately `314.159265`.

## Question 23: Convert binary 1111 to decimal.

```sql
SELECT CONV('1111', 2, 10) AS decimal_value;
```

Output:

| decimal\_value |
| --- |
| 15 |

## Question 24: Find the maximum salary and round it to two decimal places.

```sql
SELECT ROUND(MAX(salary), 2) AS maximum_salary
FROM employees;
```

Expected output:

| maximum\_salary |
| --- |
| 72000.90 |

## Question 25: Calculate each employee's bonus as a percentage of salary.

```sql
SELECT
    first_name,
    ROUND((bonus / salary) * 100, 2) AS bonus_percentage
FROM employees
WHERE salary <> 0;
```

The `WHERE` condition avoids division by zero.

---

# 7\. COMMON INTERVIEW QUESTIONS AND ANSWERS

## Q1. What are SQL numeric functions?

Numeric functions are built-in functions that perform mathematical operations on numeric data.

Examples include `ABS()`, `ROUND()`, `CEIL()`, `FLOOR()`, `MOD()`, and `POWER()`.

## Q2. What is the difference between ROUND() and TRUNCATE()?

`ROUND()` rounds a number to the specified decimal position.

`TRUNCATE()` cuts off digits beyond the specified decimal position without rounding.

```sql
SELECT
    ROUND(12.987, 2) AS rounded_value,
    TRUNCATE(12.987, 2) AS truncated_value;
```

Output:

| rounded\_value | truncated\_value |
| --- | --- |
| 12.99 | 12.98 |

## Q3. What is the difference between CEIL() and FLOOR()?

- `CEIL()` returns the smallest integer greater than or equal to a number.
- `FLOOR()` returns the largest integer less than or equal to a number.

```sql
SELECT CEIL(5.2), FLOOR(5.2);
```

Output: `6`, `5`.

## Q4. What does ABS() do?

It returns the absolute value of a number.

```sql
SELECT ABS(-100);
```

Output: `100`.

## Q5. What is MOD() used for?

It returns the remainder after division.

```sql
SELECT MOD(17, 5);
```

Output: `2`.

## Q6. How do you calculate a percentage in SQL?

Multiply the value by the percentage divided by 100.

```sql
SELECT salary * 10 / 100 AS ten_percent
FROM employees;
```

For presentation, use `ROUND()` when appropriate.

## Q7. What is the difference between POWER() and SQRT()?

`POWER(x, y)` calculates $x^y$.

`SQRT(x)` calculates the square root of `x`, equivalent mathematically to $x^{1/2}$ for nonnegative real inputs.

## Q8. What does RAND() return?

It returns a pseudo-random floating-point value from 0 inclusive to 1 exclusive.

## Q9. What is the difference between FORMAT() and ROUND()?

`ROUND()` returns a rounded numeric result.

`FORMAT()` produces a formatted string with grouping separators and a specified number of decimal places.

Use `ROUND()` for calculations and `FORMAT()` mainly for presentation.

## Q10. What is the difference between numeric functions and aggregate functions?

Numeric functions generally operate on individual values or mathematical expressions.

Examples: `ABS()`, `ROUND()`, `SQRT()`.

Aggregate functions summarize values across multiple rows.

Examples: `SUM()`, `AVG()`, `COUNT()`, `MIN()`, `MAX()`.

They can be combined:

```sql
SELECT ROUND(AVG(salary), 2) AS average_salary
FROM employees;
```

## Q11. How do you find even numbers in SQL?

```sql
SELECT *
FROM employees
WHERE MOD(emp_id, 2) = 0;
```

## Q12. How do you calculate a salary increase of 15%?

```sql
SELECT
    first_name,
    salary,
    ROUND(salary * 1.15, 2) AS increased_salary
FROM employees;
```

## Q13. What does SIGN() return?

It returns `-1` for a negative value, `0` for zero, and `1` for a positive value.

## Q14. What does PI() return?

It returns the mathematical constant pi, approximately `3.141593`.

## Q15. Why should DECIMAL be used for financial data?

`DECIMAL` represents decimal values exactly within its declared precision and scale, making it suitable for many monetary calculations. Floating-point types are approximate and can introduce representation errors.

---

# 8\. COMMON MISTAKES TO AVOID

## Mistake 1: Confusing ROUND() with TRUNCATE()

```sql
SELECT ROUND(9.876, 2);
SELECT TRUNCATE(9.876, 2);
```

Outputs: `9.88` and `9.87`.

## Mistake 2: Assuming CEIL() always rounds away from zero

```sql
SELECT CEIL(-5.7);
```

Output: `-5`.

`CEIL()` moves toward positive infinity.

## Mistake 3: Using FLOAT for exact financial amounts

Floating-point values are approximate.

For money, prefer an appropriate `DECIMAL(p,s)` type.

## Mistake 4: Dividing by zero

Avoid expressions such as:

```sql
SELECT salary / 0
FROM employees;
```

Use an appropriate condition or `NULLIF()`:

```sql
SELECT salary / NULLIF(bonus, 0) AS salary_bonus_ratio
FROM employees;
```

`NULLIF(bonus, 0)` returns `NULL` when the bonus is zero, avoiding division by zero.

## Mistake 5: Treating FORMAT() as a numeric result

```sql
SELECT FORMAT(12345.678, 2);
```

Output: `12,345.68` under an English-style locale.

This is a string intended for display, so do not rely on it for further arithmetic.

## Mistake 6: Forgetting that trigonometric functions use radians

Incorrect if you intend 45 degrees:

```sql
SELECT TAN(45);
```

Correct:

```sql
SELECT TAN(RADIANS(45));
```

## Mistake 7: Expecting RAND() to return the same value each time

```sql
SELECT RAND();
```

The result is not fixed. Supply a seed when reproducibility is needed for a suitable test.

## Mistake 8: Ignoring NULL values

Many numeric functions return `NULL` when their input is `NULL`.

```sql
SELECT ABS(NULL);
```

Output: `NULL`.

Use `COALESCE()` if a meaningful fallback is appropriate:

```sql
SELECT ABS(COALESCE(bonus, 0))
FROM employees;
```

Only use a fallback such as zero when it correctly represents the meaning of the missing data.

---

# 9\. QUICK REVISION CHEAT SHEET

| Function | Purpose | Example | Output |
| --- | --- | --- | --- |
| `ABS()` | Absolute value | `ABS(-10)` | `10` |
| `CEIL()` | Round upward | `CEIL(4.2)` | `5` |
| `CEILING()` | Alias for CEIL | `CEILING(4.2)` | `5` |
| `FLOOR()` | Round downward | `FLOOR(4.8)` | `4` |
| `ROUND()` | Round a number | `ROUND(4.567, 2)` | `4.57` |
| `TRUNCATE()` | Cut off decimal digits | `TRUNCATE(4.567, 2)` | `4.56` |
| `MOD()` | Remainder | `MOD(10, 3)` | `1` |
| `POWER()` | Raise to a power | `POWER(2, 3)` | `8` |
| `POW()` | Alias for POWER | `POW(2, 3)` | `8` |
| `SQRT()` | Square root | `SQRT(81)` | `9` |
| `SIGN()` | Sign of a number | `SIGN(-5)` | `-1` |
| `GREATEST()` | Largest expression | `GREATEST(2, 9, 5)` | `9` |
| `LEAST()` | Smallest expression | `LEAST(2, 9, 5)` | `2` |
| `RAND()` | Pseudo-random number | `RAND()` | Varies |
| `PI()` | Pi constant | `PI()` | Approximately `3.141593` |
| `EXP()` | Exponential function | `EXP(0)` | `1` |
| `LOG()` | Natural logarithm | `LOG(1)` | `0` |
| `LOG10()` | Base-10 logarithm | `LOG10(100)` | `2` |
| `LOG2()` | Base-2 logarithm | `LOG2(8)` | `3` |
| `RADIANS()` | Degrees to radians | `RADIANS(180)` | Approximately `3.141593` |
| `DEGREES()` | Radians to degrees | `DEGREES(PI())` | `180` |
| `SIN()` | Sine | `SIN(0)` | `0` |
| `COS()` | Cosine | `COS(0)` | `1` |
| `TAN()` | Tangent | `TAN(0)` | `0` |
| `ACOS()` | Inverse cosine | `ACOS(1)` | `0` |
| `ASIN()` | Inverse sine | `ASIN(0)` | `0` |
| `ATAN()` | Inverse tangent | `ATAN(0)` | `0` |
| `ATAN2()` | Angle from two coordinates | `ATAN2(1,1)` | Approximately `0.785398` |
| `COT()` | Cotangent | `COT(PI()/4)` | Approximately `1` |
| `CONV()` | Convert number bases | `CONV('1010',2,10)` | `10` |
| `CRC32()` | CRC-32 checksum | `CRC32('hello')` | Numeric checksum |
| `FORMAT()` | Format for display | `FORMAT(1234.5,2)` | `1,234.50` |

---

# 10\. FINAL LEARNING ROADMAP

Learn numeric functions in this order.

1. **Basic functions:** `ABS()`, `ROUND()`, `CEIL()`, `FLOOR()`.
2. **Remainders and powers:** `MOD()`, `POWER()`, `SQRT()`.
3. **Comparisons:** `GREATEST()`, `LEAST()`, `SIGN()`.
4. **Percentages and financial calculations:** arithmetic operators, `ROUND()`, `DECIMAL`.
5. **Advanced mathematics:** `LOG()`, `EXP()`, `PI()`, trigonometric functions.
6. **Specialized functions:** `CONV()`, `CRC32()`, `FORMAT()`.
7. **Practical queries:** combine numeric functions with `SELECT`, `WHERE`, `GROUP BY`, `HAVING`, `ORDER BY`, and aggregate functions.

## The 10 most important numeric functions for interviews

- `ABS()`
- `ROUND()`
- `CEIL()`
- `FLOOR()`
- `MOD()`
- `POWER()`
- `SQRT()`
- `TRUNCATE()`
- `GREATEST()`
- `LEAST()`

**Best practice:** Do not memorize functions alone. Practice combining them with real tables, arithmetic expressions, aggregate functions, conditional logic, and filtering.

**End of SQL Numeric Functions Notes.**
