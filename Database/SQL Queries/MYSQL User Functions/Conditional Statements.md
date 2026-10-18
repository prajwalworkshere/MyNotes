# MySQL Conditional Statements — Complete Guide with Stored Functions (UDF) and Stored Procedures

MySQL supports conditional statements such as `IF`, `IF...ELSE`, `IF...ELSEIF`, and `CASE`, along with conditional functions such as `IF()`, `IFNULL()`, `NULLIF()`, and `COALESCE()`.

Below, each topic includes its syntax, a stored procedure example, a stored function example, and its use case.

Important terminology: A function created with `CREATE FUNCTION` in SQL is called a stored function. A native MySQL UDF is written in C/C++ and loaded into MySQL. The examples below use stored functions, which is likely what you mean by UDF.

## 1. `IF...THEN` statement

Use case: Execute code only when a condition is true.

Syntax:

SQL

```
IF condition THEN
    statements;
END IF;
```

### A. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE check_age(IN p_age INT)
BEGIN
    IF p_age >= 18 THEN
        SELECT 'Eligible to vote' AS result;
    END IF;
END //

DELIMITER ;
```

Execute:

SQL

```
CALL check_age(20);
```

### B. Stored function

SQL

```
DELIMITER //

CREATE FUNCTION check_age_fn(p_age INT)
RETURNS VARCHAR(30)
DETERMINISTIC
BEGIN
    IF p_age >= 18 THEN
        RETURN 'Eligible to vote';
    END IF;

    RETURN 'Not eligible';
END //

DELIMITER ;
```

Execute:

SQL

```
SELECT check_age_fn(20);
```

Output: `Eligible to vote`

## 2. `IF...THEN...ELSE`

Use case: Choose between two outcomes.

Syntax:

SQL

```
IF condition THEN
    statements_if_true;
ELSE
    statements_if_false;
END IF;
```

### A. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE check_number(IN p_num INT)
BEGIN
    IF p_num > 0 THEN
        SELECT 'Positive' AS result;
    ELSE
        SELECT 'Zero or Negative' AS result;
    END IF;
END //

DELIMITER ;
```

Execute:

SQL

```
CALL check_number(-5);
```

### B. Stored function

SQL

```
DELIMITER //

CREATE FUNCTION check_number_fn(p_num INT)
RETURNS VARCHAR(30)
DETERMINISTIC
BEGIN
    IF p_num > 0 THEN
        RETURN 'Positive';
    ELSE
        RETURN 'Zero or Negative';
    END IF;
END //

DELIMITER ;
```

Execute:

SQL

```
SELECT check_number_fn(-5);
```

Output: `Zero or Negative`

## 3. `IF...ELSEIF...ELSE`

Use case: Check multiple conditions in order.

Syntax:

SQL

```
IF condition1 THEN
    statements1;
ELSEIF condition2 THEN
    statements2;
ELSE
    default_statements;
END IF;
```

### A. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE get_grade(IN p_marks INT)
BEGIN
    IF p_marks >= 90 THEN
        SELECT 'Grade A' AS grade;
    ELSEIF p_marks >= 75 THEN
        SELECT 'Grade B' AS grade;
    ELSEIF p_marks >= 60 THEN
        SELECT 'Grade C' AS grade;
    ELSE
        SELECT 'Grade D' AS grade;
    END IF;
END //

DELIMITER ;
```

Execute:

SQL

```
CALL get_grade(80);
```

Output: `Grade B`

### B. Stored function

SQL

```
DELIMITER //

CREATE FUNCTION get_grade_fn(p_marks INT)
RETURNS VARCHAR(10)
DETERMINISTIC
BEGIN
    IF p_marks >= 90 THEN
        RETURN 'Grade A';
    ELSEIF p_marks >= 75 THEN
        RETURN 'Grade B';
    ELSEIF p_marks >= 60 THEN
        RETURN 'Grade C';
    ELSE
        RETURN 'Grade D';
    END IF;
END //

DELIMITER ;
```

Execute:

SQL

```
SELECT get_grade_fn(80);
```

Output: `Grade B`

## 4. `CASE` statement

A `CASE` statement executes statements based on matching values or conditions.

### A. Simple CASE

Use case: Match one expression against specific values.

Syntax:

SQL

```
CASE expression
    WHEN value1 THEN
        statements1;
    WHEN value2 THEN
        statements2;
    ELSE
        default_statements;
END CASE;
```

#### Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE get_day(IN p_day INT)
BEGIN
    CASE p_day
        WHEN 1 THEN SELECT 'Monday' AS day_name;
        WHEN 2 THEN SELECT 'Tuesday' AS day_name;
        WHEN 3 THEN SELECT 'Wednesday' AS day_name;
        ELSE SELECT 'Invalid day' AS day_name;
    END CASE;
END //

DELIMITER ;
```

Execute:

SQL

```
CALL get_day(2);
```

#### Stored function

SQL

```
DELIMITER //

CREATE FUNCTION get_day_fn(p_day INT)
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    CASE p_day
        WHEN 1 THEN RETURN 'Monday';
        WHEN 2 THEN RETURN 'Tuesday';
        WHEN 3 THEN RETURN 'Wednesday';
        ELSE RETURN 'Invalid day';
    END CASE;
END //

DELIMITER ;
```

Execute:

SQL

```
SELECT get_day_fn(2);
```

Output: `Tuesday`

### B. Searched CASE

Use case: Evaluate multiple Boolean conditions.

Syntax:

SQL

```
CASE
    WHEN condition1 THEN
        statements1;
    WHEN condition2 THEN
        statements2;
    ELSE
        default_statements;
END CASE;
```

#### Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE check_salary(IN p_salary DECIMAL(10,2))
BEGIN
    CASE
        WHEN p_salary >= 100000 THEN
            SELECT 'High Salary' AS category;
        WHEN p_salary >= 50000 THEN
            SELECT 'Medium Salary' AS category;
        ELSE
            SELECT 'Low Salary' AS category;
    END CASE;
END //

DELIMITER ;
```

#### Stored function

SQL

```
DELIMITER //

CREATE FUNCTION check_salary_fn(p_salary DECIMAL(10,2))
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    CASE
        WHEN p_salary >= 100000 THEN RETURN 'High Salary';
        WHEN p_salary >= 50000 THEN RETURN 'Medium Salary';
        ELSE RETURN 'Low Salary';
    END CASE;
END //

DELIMITER ;
```

Execute:

SQL

```
SELECT check_salary_fn(75000);
```

Output: `Medium Salary`

Note: In a stored program, a `CASE` statement must have an `END CASE`. If no condition matches and there is no `ELSE`, MySQL raises an error.

## 5. `IF()` function

Use case: Return one value when a condition is true and another when false.

Syntax:

SQL

```
IF(condition, value_if_true, value_if_false)
```

### A. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE check_pass(
    IN p_marks INT,
    OUT p_result VARCHAR(10)
)
BEGIN
    SET p_result = IF(p_marks >= 40, 'Pass', 'Fail');
END //

DELIMITER ;
```

Execute:

SQL

```
CALL check_pass(75, @result);
SELECT @result;
```

Output: `Pass`

### B. Stored function

SQL

```
DELIMITER //

CREATE FUNCTION check_pass_fn(p_marks INT)
RETURNS VARCHAR(10)
DETERMINISTIC
RETURN IF(p_marks >= 40, 'Pass', 'Fail') //

DELIMITER ;
```

Execute:

SQL

```
SELECT check_pass_fn(75);
```

Output: `Pass`

### C. Normal SQL query

SQL

```
SELECT IF(10 > 5, 'Yes', 'No') AS result;
```

Output: `Yes`

## 6. `CASE` expression

Use case: Return a value based on multiple conditions in a regular SQL query.

Syntax:

SQL

```
CASE
    WHEN condition1 THEN result1
    WHEN condition2 THEN result2
    ELSE default_result
END
```

### A. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE calculate_bonus(
    IN p_salary DECIMAL(10,2),
    OUT p_bonus DECIMAL(10,2)
)
BEGIN
    SET p_bonus = CASE
        WHEN p_salary >= 100000 THEN 10000
        WHEN p_salary >= 50000 THEN 5000
        ELSE 1000
    END;
END //

DELIMITER ;
```

Execute:

SQL

```
CALL calculate_bonus(75000, @bonus);
SELECT @bonus;
```

Output: `5000`

### B. Stored function

SQL

```
DELIMITER //

CREATE FUNCTION calculate_bonus_fn(p_salary DECIMAL(10,2))
RETURNS DECIMAL(10,2)
DETERMINISTIC
RETURN CASE
    WHEN p_salary >= 100000 THEN 10000
    WHEN p_salary >= 50000 THEN 5000
    ELSE 1000
END //

DELIMITER ;
```

Execute:

SQL

```
SELECT calculate_bonus_fn(75000);
```

Output: `5000.00`

### C. Normal SQL query

SQL

```
SELECT
    name,
    CASE
        WHEN salary >= 100000 THEN 'High'
        WHEN salary >= 50000 THEN 'Medium'
        ELSE 'Low'
    END AS salary_category
FROM employees;
```

## 7. `IFNULL()`

Use case: Replace `NULL` with a default value.

Syntax:

SQL

```
IFNULL(expression, replacement_value)
```

### A. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE get_commission(
    IN p_commission DECIMAL(10,2),
    OUT p_result DECIMAL(10,2)
)
BEGIN
    SET p_result = IFNULL(p_commission, 0);
END //

DELIMITER ;
```

Execute:

SQL

```
CALL get_commission(NULL, @result);
SELECT @result;
```

Output: `0.00`

### B. Stored function

SQL

```
DELIMITER //

CREATE FUNCTION get_commission_fn(p_commission DECIMAL(10,2))
RETURNS DECIMAL(10,2)
DETERMINISTIC
RETURN IFNULL(p_commission, 0) //

DELIMITER ;
```

Execute:

SQL

```
SELECT get_commission_fn(NULL);
```

Output: `0.00`

## 8. `NULLIF()`

Use case: Return `NULL` when two expressions are equal.

Syntax:

SQL

```
NULLIF(expression1, expression2)
```

### A. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE safe_quantity(
    IN p_qty INT,
    OUT p_result INT
)
BEGIN
    SET p_result = NULLIF(p_qty, 0);
END //

DELIMITER ;
```

Execute:

SQL

```
CALL safe_quantity(0, @result);
SELECT @result;
```

Output: `NULL`

### B. Stored function

SQL

```
DELIMITER //

CREATE FUNCTION safe_quantity_fn(p_qty INT)
RETURNS INT
DETERMINISTIC
RETURN NULLIF(p_qty, 0) //

DELIMITER ;
```

Execute:

SQL

```
SELECT safe_quantity_fn(0);
```

Output: `NULL`

### C. Prevent division by zero

SQL

```
SELECT total_amount / NULLIF(quantity, 0) AS unit_price
FROM sales;
```

When `quantity` is zero, the result is `NULL` instead of a division-by-zero error.

## 9. `COALESCE()`

Use case: Return the first non-`NULL` value from a list.

Syntax:

SQL

```
COALESCE(value1, value2, value3, ...)
```

### A. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE get_contact(
    IN p_phone VARCHAR(20),
    IN p_alternate VARCHAR(20),
    OUT p_result VARCHAR(30)
)
BEGIN
    SET p_result = COALESCE(
        p_phone,
        p_alternate,
        'Not Available'
    );
END //

DELIMITER ;
```

Execute:

SQL

```
CALL get_contact(NULL, '9876543210', @result);
SELECT @result;
```

Output: `9876543210`

### B. Stored function

SQL

```
DELIMITER //

CREATE FUNCTION get_contact_fn(
    p_phone VARCHAR(20),
    p_alternate VARCHAR(20)
)
RETURNS VARCHAR(30)
DETERMINISTIC
RETURN COALESCE(
    p_phone,
    p_alternate,
    'Not Available'
) //

DELIMITER ;
```

Execute:

SQL

```
SELECT get_contact_fn(NULL, '9876543210');
```

Output: `9876543210`

## 10. Conditional statements in `UPDATE`

Use case: Update values based on different conditions.

### A. Using `IF()`

SQL

```
UPDATE employees
SET bonus = IF(salary >= 50000, 5000, 1000);
```

### B. Using `CASE`

SQL

```
UPDATE employees
SET bonus = CASE
    WHEN salary >= 100000 THEN 10000
    WHEN salary >= 50000 THEN 5000
    ELSE 1000
END;
```

### C. Inside a stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE update_bonus()
BEGIN
    UPDATE employees
    SET bonus = CASE
        WHEN salary >= 100000 THEN 10000
        WHEN salary >= 50000 THEN 5000
        ELSE 1000
    END;
END //

DELIMITER ;
```

Execute:

SQL

```
CALL update_bonus();
```

### D. Inside a stored function?

A stored function cannot perform an `UPDATE` on a regular table in this way. Use a stored procedure for table modifications.

Important: These `UPDATE` examples affect every row unless you add a `WHERE` clause.

## 11. Conditional filtering with `WHERE`

These operators are used directly in SQL queries, stored functions, and stored procedures.

| Operator  | Meaning                                | Example                                  |
| --------- | -------------------------------------- | ---------------------------------------- |
| `AND`     | Both conditions must be true           | `salary > 30000 AND age > 20`            |
| `OR`      | At least one condition must be true    | `department = 'IT' OR department = 'HR'` |
| `NOT`     | Negates a condition                    | `NOT (salary > 50000)`                   |
| `IN`      | Matches values in a list               | `department IN ('IT', 'HR')`             |
| `BETWEEN` | Checks an inclusive range              | `salary BETWEEN 30000 AND 80000`         |
| `IS NULL` | Checks for `NULL`                      | `commission IS NULL`                     |
| `LIKE`    | Matches a text pattern                 | `name LIKE 'A%'`                         |
| `EXISTS`  | Checks whether a subquery returns rows | `EXISTS (SELECT 1 ...)`                  |

### A. Normal query

SQL

```
SELECT *
FROM employees
WHERE department IN ('IT', 'HR')
  AND salary BETWEEN 30000 AND 80000;
```

### B. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE get_employees()
BEGIN
    SELECT *
    FROM employees
    WHERE department IN ('IT', 'HR')
      AND salary BETWEEN 30000 AND 80000;
END //

DELIMITER ;
```

Execute:

SQL

```
CALL get_employees();
```

### C. Stored function

Stored functions can use conditions to calculate and return values, but they cannot return an arbitrary result set like a procedure. For example:

SQL

```
DELIMITER //

CREATE FUNCTION is_high_salary(p_salary DECIMAL(10,2))
RETURNS BOOLEAN
DETERMINISTIC
RETURN p_salary >= 50000 //

DELIMITER ;
```

Execute:

SQL

```
SELECT is_high_salary(60000);
```

Output: `1` (true).

## 12. Conditional logic with `EXISTS`

Use case: Check whether a related record exists in another table.

### A. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE check_department(IN p_dept_id INT)
BEGIN
    IF EXISTS (
        SELECT 1
        FROM departments
        WHERE department_id = p_dept_id
    ) THEN
        SELECT 'Department exists' AS result;
    ELSE
        SELECT 'Department does not exist' AS result;
    END IF;
END //

DELIMITER ;
```

Execute:

SQL

```
CALL check_department(10);
```

### B. Stored function

SQL

```
DELIMITER //

CREATE FUNCTION department_exists_fn(p_dept_id INT)
RETURNS BOOLEAN
READS SQL DATA
BEGIN
    RETURN EXISTS (
        SELECT 1
        FROM departments
        WHERE department_id = p_dept_id
    );
END //

DELIMITER ;
```

Execute:

SQL

```
SELECT department_exists_fn(10);
```

Output: `1` if the department exists; otherwise `0`.

## 13. `WHILE` loop

Use case: Repeat statements while a condition is true.

### A. Stored procedure

SQL

```
DELIMITER //

CREATE PROCEDURE sum_numbers(
    IN p_n INT,
    OUT p_total INT
)
BEGIN
    DECLARE i INT DEFAULT 1;
    SET p_total = 0;

    WHILE i <= p_n DO
        SET p_total = p_total + i;
        SET i = i + 1;
    END WHILE;
END //

DELIMITER ;
```

Execute:

SQL

```
CALL sum_numbers(5, @total);
SELECT @total;
```

Output: `15`

### B. Stored function

SQL

```
DELIMITER //

CREATE FUNCTION sum_numbers_fn(p_n INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE total INT DEFAULT 0;

    WHILE i <= p_n DO
        SET total = total + i;
        SET i = i + 1;
    END WHILE;

    RETURN total;
END //

DELIMITER ;
```

Execute:

SQL

```
SELECT sum_numbers_fn(5);
```

Output: `15`

## 14. Quick comparison: stored function vs. procedure

| Feature                        | Stored function             | Stored procedure                                    |
| ------------------------------ | --------------------------- | --------------------------------------------------- |
| Create using                   | `CREATE FUNCTION`           | `CREATE PROCEDURE`                                  |
| Execute using                  | `SELECT function_name(...)` | `CALL procedure_name(...)`                          |
| Return value                   | `RETURN value`              | `OUT` parameter or result set                       |
| `IF...ELSE`                    | Yes                         | Yes                                                 |
| `CASE` statement               | Yes                         | Yes                                                 |
| `IF()` and `CASE` expressions  | Yes                         | Yes                                                 |
| `IFNULL`, `NULLIF`, `COALESCE` | Yes                         | Yes                                                 |
| `WHILE` loop                   | Yes                         | Yes                                                 |
| Modify regular table data      | Restricted                  | Yes, subject to permissions and normal restrictions |

## Final rules to remember

1. Stored function: Use `RETURN` to return a calculated value.

2. Stored procedure: Use `OUT` parameters to return values or `SELECT` to return a result set.

3. Normal SQL query: Use `IF()` or a `CASE` expression rather than a procedural `IF...THEN` block.

4. Stored function restrictions: Functions have restrictions on statements they can execute, including table modifications and result-set generation.

5. Native UDF: A C/C++ MySQL UDF has a different implementation model from a stored function created with SQL.
