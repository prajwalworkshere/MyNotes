# MYSQL TRIGGERS — COMPLETE IN-DEPTH NOTES

# 1. Introduction to Triggers

## 1.1 What is a Trigger?

A **Trigger** is a database object that automatically executes a predefined block of SQL statements when a particular event occurs on a specified table.

In simple words:

A trigger is an automatic mechanism that tells the database:

**"Whenever a particular operation happens on this table, automatically perform some additional operations."**

For example, consider an employee management system.

We have an `employees` table:

| employee_id | name | salary |
|---|---|---:|
| 1 | Amit | 30000 |
| 2 | Rahul | 40000 |
| 3 | Priya | 50000 |

Suppose whenever an employee's salary changes, we want to maintain a record of:

- Employee ID
- Previous salary
- Updated salary
- Date and time of modification

Without triggers, we would need to execute two separate queries:

1. Update the employee's salary.
2. Insert the old and new salary into a history table.

For example:

```sql
UPDATE employees
SET salary = 45000
WHERE employee_id = 2;

INSERT INTO salary_history
(employee_id, old_salary, new_salary)
VALUES (2, 40000, 45000);
```

There is a problem here.

What if a developer updates the employee's salary but forgets to insert the history record?

The salary changes, but the history is not maintained.

We need a mechanism that automatically performs the second operation whenever the first operation happens.

This is exactly what a trigger does.

With a trigger, we only execute:

```sql
UPDATE employees
SET salary = 45000
WHERE employee_id = 2;
```

The trigger automatically inserts the history record.

We do not need to call the trigger manually.

## 1.2 Important Characteristics of Triggers

1. A trigger is a database object.
2. A trigger is associated with a specific table.
3. A trigger executes automatically.
4. A trigger responds to a particular database event.
5. A trigger executes when an INSERT, UPDATE or DELETE operation occurs.
6. A trigger can contain multiple SQL statements.
7. A trigger can access the old and new values of a row using OLD and NEW.
8. MySQL triggers execute once for every affected row.
9. A trigger cannot be called manually like a stored procedure.
10. A trigger cannot return a value like a stored function.

---

# 2. Why Do We Need Triggers?

Triggers are useful when certain database operations must happen automatically whenever data changes.

Let's understand the major purposes.

## 2.1 Automatic Data Validation

Suppose an employee's salary must always be greater than zero.

Without a trigger, every application that inserts an employee must validate the salary.

If one application forgets validation, invalid data might enter the database.

A BEFORE INSERT trigger can enforce this rule centrally.

Example:

```sql
DELIMITER //

CREATE TRIGGER validate_salary
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN

    IF NEW.salary <= 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Salary must be greater than zero';

    END IF;

END //

DELIMITER ;
```

Now an invalid salary is rejected even if someone inserts data directly through MySQL.

## 2.2 Maintaining Audit Records

Suppose we want to know who changed an employee's salary and what the previous salary was.

A trigger can automatically maintain an audit table.

## 2.3 Automatic Calculations

Suppose an order has:

- Quantity = 5
- Unit price = 1000

Total amount should be:

Total = Quantity × Unit Price

A BEFORE INSERT trigger can calculate the total automatically.

## 2.4 Maintaining Related Tables

Suppose a customer places an order.

We may want to automatically update the customer's order count or maintain some summary information.

## 2.5 Enforcing Business Rules

Examples:

- Prevent negative product prices.
- Prevent an account balance from becoming invalid.
- Prevent deletion of important records.
- Automatically maintain inventory.
- Record changes in important database tables.

---

# 3. Types of Triggers in MySQL

Triggers are classified using two dimensions:

1. Timing
2. Event

## 3.1 Based on Timing

There are two types.

### A. BEFORE Trigger

A BEFORE trigger executes before the associated row operation is performed.

It is useful when we want to:

- Validate incoming values.
- Modify incoming values.
- Prevent invalid operations.
- Perform calculations before storing a row.

Example:

```sql
CREATE TRIGGER before_employee_insert
BEFORE INSERT ON employees
FOR EACH ROW
SET NEW.salary = NEW.salary + 1000;
```

If the inserted salary is 30000, the stored salary becomes 31000.

### B. AFTER Trigger

An AFTER trigger executes after the associated row operation has successfully affected the row.

It is useful when we want to:

- Maintain audit logs.
- Record history.
- Update related tables.
- Archive deleted records.

Example:

```sql
CREATE TRIGGER after_employee_insert
AFTER INSERT ON employees
FOR EACH ROW
BEGIN

    INSERT INTO employee_log(message)
    VALUES ('New employee inserted');

END;
```

## 3.2 Based on Events

There are three events.

### A. INSERT

Executes when a new row is inserted.

### B. UPDATE

Executes when an existing row is updated.

### C. DELETE

Executes when an existing row is deleted.

## 3.3 All Six Trigger Types

| Trigger | Timing | Event |
|---|---|---|
| BEFORE INSERT | Before | INSERT |
| AFTER INSERT | After | INSERT |
| BEFORE UPDATE | Before | UPDATE |
| AFTER UPDATE | After | UPDATE |
| BEFORE DELETE | Before | DELETE |
| AFTER DELETE | After | DELETE |

Remember:

2 timings × 3 events = 6 trigger combinations.

---

# 4. Complete Trigger Syntax

## 4.1 General Syntax

```sql
DELIMITER //

CREATE TRIGGER trigger_name
trigger_time trigger_event
ON table_name
FOR EACH ROW
BEGIN

    -- SQL statements

END //

DELIMITER ;
```

## 4.2 Explanation of Every Component

| Component | Explanation |
|---|---|
| DELIMITER // | Changes the client statement delimiter |
| CREATE TRIGGER | Creates a trigger |
| trigger_name | Name of the trigger |
| trigger_time | BEFORE or AFTER |
| trigger_event | INSERT, UPDATE or DELETE |
| ON table_name | Specifies the associated table |
| FOR EACH ROW | Executes once per affected row |
| BEGIN | Starts the compound statement |
| SQL statements | Operations executed by the trigger |
| END | Ends the compound statement |
| // | Terminates the complete CREATE TRIGGER statement |
| DELIMITER ; | Restores the normal delimiter |

## 4.3 Why Do We Use DELIMITER?

Normally, MySQL statements end with a semicolon.

However, triggers can contain multiple SQL statements.

Example:

```sql
CREATE TRIGGER example_trigger
AFTER INSERT ON employees
FOR EACH ROW
BEGIN

    INSERT INTO employee_log
    VALUES ('Employee added');

    INSERT INTO notifications
    VALUES ('New employee registered');

END;
```

The MySQL command-line client would normally interpret a semicolon as the end of the complete statement.

This creates a problem because the trigger body itself contains semicolons.

Therefore, we temporarily change the delimiter.

Correct version:

```sql
DELIMITER //

CREATE TRIGGER example_trigger
AFTER INSERT ON employees
FOR EACH ROW
BEGIN

    INSERT INTO employee_log
    VALUES ('Employee added');

    INSERT INTO notifications
    VALUES ('New employee registered');

END //

DELIMITER ;
```

Important:

`DELIMITER` is a command understood by MySQL client tools. It is not part of the stored trigger definition on the database server.

---

# 5. Understanding FOR EACH ROW

This is one of the most important concepts in triggers.

MySQL supports row-level triggers.

It does not support statement-level triggers.

`FOR EACH ROW` means that the trigger executes separately for every row affected by the triggering SQL statement.

## Example

Suppose our employees table contains:

| employee_id | name | salary |
|---|---|---:|
| 1 | Amit | 30000 |
| 2 | Rahul | 40000 |
| 3 | Priya | 50000 |

We execute:

```sql
UPDATE employees
SET salary = salary + 5000;
```

This is one UPDATE statement, but it affects three rows.

If an AFTER UPDATE trigger exists, it executes three times.

Execution:

1. First execution for Amit.
2. Second execution for Rahul.
3. Third execution for Priya.

Therefore:

**One SQL statement can activate the same trigger multiple times.**

This is called row-level trigger execution.

---

# 6. BEFORE INSERT Trigger

## 6.1 Definition

A BEFORE INSERT trigger executes before a new row is inserted into its associated table.

The main purpose is to validate or modify the incoming row.

The NEW keyword represents the values that are about to be inserted.

## 6.2 Example 1 — Automatically Increase Salary

### Step 1: Create Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2)
);
```

### Step 2: Create Trigger

```sql
DELIMITER //

CREATE TRIGGER before_employee_insert
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN

    SET NEW.salary = NEW.salary + 1000;

END //

DELIMITER ;
```

### Step 3: Insert Data

```sql
INSERT INTO employees
VALUES (1, 'Rahul', 30000);
```

### Step 4: Check Data

```sql
SELECT * FROM employees;
```

Output:

| employee_id | name | salary |
|---|---|---:|
| 1 | Rahul | 31000 |

### Execution Explanation

1. MySQL receives the INSERT statement.
2. The BEFORE INSERT trigger activates.
3. NEW.salary initially contains 30000.
4. The trigger adds 1000.
5. NEW.salary becomes 31000.
6. MySQL inserts the modified row.

The actual stored value is 31000.

## 6.3 Example 2 — Validate Employee Age

Suppose employees must be at least 18 years old.

```sql
CREATE TABLE employee_details (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    age INT
);
```

Trigger:

```sql
DELIMITER //

CREATE TRIGGER validate_employee_age
BEFORE INSERT ON employee_details
FOR EACH ROW
BEGIN

    IF NEW.age < 18 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Employee age must be at least 18';

    END IF;

END //

DELIMITER ;
```

Valid insertion:

```sql
INSERT INTO employee_details
VALUES (1, 'Amit', 25);
```

Invalid insertion:

```sql
INSERT INTO employee_details
VALUES (2, 'Rahul', 15);
```

The second insertion fails.

### Understanding SIGNAL SQLSTATE

`SIGNAL` raises an error condition explicitly.

Syntax:

```sql
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Your error message';
```

`45000` is commonly used for user-defined exceptions.

The error prevents the invalid operation from succeeding.

---

# 7. AFTER INSERT Trigger

## 7.1 Definition

An AFTER INSERT trigger executes after a new row has successfully been inserted.

It is commonly used for:

- Audit logging.
- Recording insertion history.
- Updating related tables.
- Maintaining summary information.

## 7.2 Example — Employee Insertion Log

### Step 1: Create Employee Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2)
);
```

### Step 2: Create Log Table

```sql
CREATE TABLE employee_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    action VARCHAR(50),
    log_time DATETIME
);
```

### Step 3: Create Trigger

```sql
DELIMITER //

CREATE TRIGGER after_employee_insert
AFTER INSERT ON employees
FOR EACH ROW
BEGIN

    INSERT INTO employee_log
    (employee_id, action, log_time)

    VALUES
    (NEW.employee_id, 'INSERT', NOW());

END //

DELIMITER ;
```

### Step 4: Insert Employee

```sql
INSERT INTO employees
VALUES (101, 'Amit', 50000);
```

### Step 5: Check Log

```sql
SELECT * FROM employee_log;
```

Expected output:

| log_id | employee_id | action |
|---|---:|---|
| 1 | 101 | INSERT |

### Execution Explanation

1. The employee row is inserted.
2. AFTER INSERT trigger activates.
3. NEW.employee_id accesses the inserted employee ID.
4. A new audit record is inserted into employee_log.
5. Both operations participate in the same transaction when using transactional tables such as InnoDB.

---

# 8. BEFORE UPDATE Trigger

## 8.1 Definition

A BEFORE UPDATE trigger executes before an existing row is updated.

It is useful for validating updated values, comparing previous and incoming values, and modifying NEW values.

During UPDATE, both OLD and NEW are available.

## 8.2 Example 1 — Prevent Negative Salary

```sql
DELIMITER //

CREATE TRIGGER validate_salary_update
BEFORE UPDATE ON employees
FOR EACH ROW
BEGIN

    IF NEW.salary < 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Salary cannot be negative';

    END IF;

END //

DELIMITER ;
```

Test:

```sql
UPDATE employees
SET salary = -1000
WHERE employee_id = 101;
```

The update fails.

Explanation:

- OLD.salary contains the previous salary.
- NEW.salary contains the proposed salary.
- If the proposed salary is negative, SIGNAL raises an error.
- The update is rejected.

## 8.3 Example 2 — Automatically Update Modification Time

Table:

```sql
CREATE TABLE employee_records (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2),
    updated_at DATETIME
);
```

Trigger:

```sql
DELIMITER //

CREATE TRIGGER before_employee_update
BEFORE UPDATE ON employee_records
FOR EACH ROW
BEGIN

    SET NEW.updated_at = NOW();

END //

DELIMITER ;
```

Update:

```sql
UPDATE employee_records
SET salary = 60000
WHERE id = 1;
```

The updated_at column automatically receives the current date and time.

## 8.4 Example 3 — Prevent Salary Reduction

```sql
DELIMITER //

CREATE TRIGGER prevent_salary_reduction
BEFORE UPDATE ON employees
FOR EACH ROW
BEGIN

    IF NEW.salary < OLD.salary THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Salary reduction is not allowed';

    END IF;

END //

DELIMITER ;
```

Suppose:

OLD.salary = 50000

NEW.salary = 45000

Since:

45000 < 50000

The trigger rejects the update.

This is a practical example of using OLD and NEW together.

---

# 9. AFTER UPDATE Trigger

## 9.1 Definition

An AFTER UPDATE trigger executes after an existing row has successfully been updated.

It is commonly used to maintain history and audit records.

## 9.2 Example — Salary History

### Step 1: Create History Table

```sql
CREATE TABLE salary_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    old_salary DECIMAL(10,2),
    new_salary DECIMAL(10,2),
    changed_at DATETIME
);
```

### Step 2: Create Trigger

```sql
DELIMITER //

CREATE TRIGGER after_salary_update
AFTER UPDATE ON employees
FOR EACH ROW
BEGIN

    IF NOT (OLD.salary <=> NEW.salary) THEN

        INSERT INTO salary_history
        (
            employee_id,
            old_salary,
            new_salary,
            changed_at
        )

        VALUES
        (
            OLD.employee_id,
            OLD.salary,
            NEW.salary,
            NOW()
        );

    END IF;

END //

DELIMITER ;
```

### Step 3: Update Salary

```sql
UPDATE employees
SET salary = 60000
WHERE employee_id = 101;
```

### Step 4: Check History

```sql
SELECT * FROM salary_history;
```

Expected:

| employee_id | old_salary | new_salary |
|---|---:|---:|
| 101 | 50000 | 60000 |

### Explanation

Suppose the original salary is 50000.

We update it to 60000.

Inside the trigger:

- OLD.salary = 50000
- NEW.salary = 60000

The trigger inserts both values into salary_history.

### Why use AFTER UPDATE?

Because we want to record the change after the update operation succeeds.

Important:

An AFTER UPDATE trigger cannot modify the row being updated by assigning to NEW values. Such modifications must be performed in BEFORE UPDATE.

---

# 10. BEFORE DELETE Trigger

## 10.1 Definition

A BEFORE DELETE trigger executes before an existing row is deleted.

It is useful for:

- Preventing deletion.
- Checking deletion conditions.
- Enforcing deletion policies.
- Validating business rules.

During DELETE, only OLD is available.

## 10.2 Example — Prevent Deletion of Important Employee

```sql
DELIMITER //

CREATE TRIGGER prevent_employee_delete
BEFORE DELETE ON employees
FOR EACH ROW
BEGIN

    IF OLD.employee_id = 101 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'This employee cannot be deleted';

    END IF;

END //

DELIMITER ;
```

Test:

```sql
DELETE FROM employees
WHERE employee_id = 101;
```

The operation fails.

### Why use OLD?

During deletion, the row already exists before the operation.

There is no new version of the row.

Therefore, OLD is used to access the values of the row being deleted.

---

# 11. AFTER DELETE Trigger

## 11.1 Definition

An AFTER DELETE trigger executes after a row has successfully been deleted.

It is commonly used to preserve deleted information.

## 11.2 Example — Archive Deleted Employees

### Step 1: Create Archive Table

```sql
CREATE TABLE deleted_employees (
    employee_id INT,
    name VARCHAR(50),
    salary DECIMAL(10,2),
    deleted_at DATETIME
);
```

### Step 2: Create Trigger

```sql
DELIMITER //

CREATE TRIGGER after_employee_delete
AFTER DELETE ON employees
FOR EACH ROW
BEGIN

    INSERT INTO deleted_employees
    (
        employee_id,
        name,
        salary,
        deleted_at
    )

    VALUES
    (
        OLD.employee_id,
        OLD.name,
        OLD.salary,
        NOW()
    );

END //

DELIMITER ;
```

### Step 3: Delete Employee

```sql
DELETE FROM employees
WHERE employee_id = 101;
```

### Step 4: Check Archive Table

```sql
SELECT * FROM deleted_employees;
```

The deleted employee's information is preserved.

### Execution Explanation

1. MySQL receives DELETE.
2. BEFORE DELETE trigger executes, if present.
3. The employee row is deleted.
4. AFTER DELETE trigger executes.
5. OLD values are used to archive the deleted employee.

---

# 12. OLD and NEW Keywords — Complete Theory

This is one of the most important concepts in MySQL triggers.

OLD and NEW are special row references that allow triggers to access column values of the row involved in the triggering operation.

They are not ordinary user-declared variables.

## 12.1 NEW

NEW represents the new version of a row.

It is available in:

- INSERT triggers.
- UPDATE triggers.

Examples:

```sql
NEW.employee_id
NEW.name
NEW.salary
```

In INSERT, NEW contains the incoming values.

In UPDATE, NEW contains the updated values.

## 12.2 OLD

OLD represents the existing or previous version of a row.

It is available in:

- UPDATE triggers.
- DELETE triggers.

Examples:

```sql
OLD.employee_id
OLD.name
OLD.salary
```

In UPDATE, OLD contains the values before modification.

In DELETE, OLD contains the values of the row being deleted.

## 12.3 Availability Table

| Trigger | OLD | NEW |
|---|---|---|
| BEFORE INSERT | No | Yes |
| AFTER INSERT | No | Yes |
| BEFORE UPDATE | Yes | Yes |
| AFTER UPDATE | Yes | Yes |
| BEFORE DELETE | Yes | No |
| AFTER DELETE | Yes | No |

## 12.4 Visual Understanding of UPDATE

Suppose an employee's salary changes from 40000 to 50000.

OLD represents:

```text
employee_id = 101
salary = 40000
```

NEW represents:

```text
employee_id = 101
salary = 50000
```

Therefore:

```sql
OLD.salary = 40000
NEW.salary = 50000
```

We can compare these values inside the trigger.

## 12.5 Important Rules

1. OLD is not available in INSERT triggers.
2. NEW is not available in DELETE triggers.
3. OLD values are read-only.
4. NEW values can be modified in BEFORE INSERT and BEFORE UPDATE triggers.
5. NEW values cannot be assigned in AFTER triggers.
6. Both OLD and NEW are available during UPDATE.

Memory trick:

- INSERT → NEW
- UPDATE → OLD + NEW
- DELETE → OLD

---

# 13. Trigger Execution Order

Suppose an INSERT statement is executed.

The general execution sequence is:

BEFORE INSERT → Row insertion → AFTER INSERT

For UPDATE:

BEFORE UPDATE → Row update → AFTER UPDATE

For DELETE:

BEFORE DELETE → Row deletion → AFTER DELETE

## Multiple Triggers

MySQL supports multiple triggers with the same timing and event on a table.

For example, we can have two BEFORE INSERT triggers:

1. Validate salary.
2. Set default department.

Both can execute for the same inserted row.

## FOLLOWS

Specifies that the newly created trigger executes after an existing trigger.

Example:

```sql
CREATE TRIGGER trigger_two
BEFORE INSERT ON employees
FOR EACH ROW
FOLLOWS trigger_one
BEGIN

    SET NEW.salary = NEW.salary + 1000;

END;
```

## PRECEDES

Specifies that the newly created trigger executes before an existing trigger.

Example:

```sql
CREATE TRIGGER trigger_two
BEFORE INSERT ON employees
FOR EACH ROW
PRECEDES trigger_one
BEGIN

    SET NEW.salary = NEW.salary + 1000;

END;
```

These keywords control the order of triggers with the same timing and event.

---

# 14. Conditional Statements Inside Triggers

A trigger can contain procedural SQL statements.

These include:

- IF
- ELSEIF
- ELSE
- CASE
- WHILE
- REPEAT
- LOOP

## 14.1 IF Statement

Syntax:

```sql
IF condition THEN

    statements;

ELSEIF condition THEN

    statements;

ELSE

    statements;

END IF;
```

Example:

```sql
DELIMITER //

CREATE TRIGGER salary_category
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN

    IF NEW.salary < 30000 THEN

        SET NEW.category = 'Junior';

    ELSEIF NEW.salary < 70000 THEN

        SET NEW.category = 'Mid Level';

    ELSE

        SET NEW.category = 'Senior';

    END IF;

END //

DELIMITER ;
```

Assume the employees table has a category column.

Explanation:

- Salary below 30000 gets Junior.
- Salary between 30000 and 69999.99 gets Mid Level.
- Salary of 70000 or more gets Senior.

## 14.2 CASE Statement

Syntax:

```sql
CASE

    WHEN condition1 THEN
        statements;

    WHEN condition2 THEN
        statements;

    ELSE
        statements;

END CASE;
```

CASE is useful when multiple conditions must be checked.

## 14.3 Loops

MySQL also supports WHILE, REPEAT and LOOP in compound statements.

However, loops are rarely necessary in normal triggers because the trigger already executes once for each affected row.

Avoid unnecessary looping because it can increase execution time.

---

# 15. Variables Inside Triggers

Triggers can declare local variables using DECLARE.

Syntax:

```sql
DECLARE variable_name datatype;
```

Important:

DECLARE statements must appear at the beginning of their compound block, before executable statements.

Example:

```sql
DELIMITER //

CREATE TRIGGER calculate_bonus
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN

    DECLARE bonus DECIMAL(10,2);

    SET bonus = NEW.salary * 0.10;

    SET NEW.salary = NEW.salary + bonus;

END //

DELIMITER ;
```

Suppose salary is 50000.

Calculation:

Bonus = 50000 × 10 / 100

Bonus = 5000

Final salary = 50000 + 5000

Final salary = 55000

## DECLARE vs SET

| DECLARE | SET |
|---|---|
| Declares a local variable | Assigns a value |
| Defines its datatype | Changes its value |
| Used at the beginning of a block | Used in executable statements |

---

# 16. Practical Application — Audit Logging

Audit logging means maintaining a history of changes made to database records.

A typical audit record contains:

- Record ID
- Previous value
- New value
- Action type
- Date and time

## Complete Example

### Step 1: Create Employee Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2)
);
```

### Step 2: Create Audit Table

```sql
CREATE TABLE employee_audit (
    audit_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    old_salary DECIMAL(10,2),
    new_salary DECIMAL(10,2),
    action_type VARCHAR(20),
    action_time DATETIME
);
```

### Step 3: Create AFTER UPDATE Trigger

```sql
DELIMITER //

CREATE TRIGGER employee_salary_audit
AFTER UPDATE ON employees
FOR EACH ROW
BEGIN

    IF NOT (OLD.salary <=> NEW.salary) THEN

        INSERT INTO employee_audit
        (
            employee_id,
            old_salary,
            new_salary,
            action_type,
            action_time
        )

        VALUES
        (
            OLD.employee_id,
            OLD.salary,
            NEW.salary,
            'UPDATE',
            NOW()
        );

    END IF;

END //

DELIMITER ;
```

### Step 4: Insert Employee

```sql
INSERT INTO employees
VALUES (1, 'Amit', 40000);
```

### Step 5: Update Salary

```sql
UPDATE employees
SET salary = 50000
WHERE employee_id = 1;
```

### Step 6: Check Audit Table

```sql
SELECT * FROM employee_audit;
```

Expected:

| employee_id | old_salary | new_salary | action_type |
|---|---:|---:|---|
| 1 | 40000 | 50000 | UPDATE |

The trigger automatically maintains the history.

---

# 17. Practical Application — Automatic Calculations

Suppose an order contains:

- Quantity
- Unit price
- Total price

Formula:

Total Price = Quantity × Unit Price

## Create Table

```sql
CREATE TABLE order_items (
    item_id INT PRIMARY KEY,
    product_name VARCHAR(50),
    quantity INT,
    unit_price DECIMAL(10,2),
    total_price DECIMAL(10,2)
);
```

## Create BEFORE INSERT Trigger

```sql
DELIMITER //

CREATE TRIGGER calculate_order_total
BEFORE INSERT ON order_items
FOR EACH ROW
BEGIN

    SET NEW.total_price =
        NEW.quantity * NEW.unit_price;

END //

DELIMITER ;
```

## Insert Data

```sql
INSERT INTO order_items
VALUES (1, 'Keyboard', 2, 1500, 0);
```

## Output

```sql
SELECT * FROM order_items;
```

| item_id | product_name | quantity | unit_price | total_price |
|---|---|---:|---:|---:|
| 1 | Keyboard | 2 | 1500 | 3000 |

The trigger calculates total_price automatically.

We should also create a BEFORE UPDATE trigger so that the total remains correct when quantity or unit_price changes.

```sql
DELIMITER //

CREATE TRIGGER update_order_total
BEFORE UPDATE ON order_items
FOR EACH ROW
BEGIN

    SET NEW.total_price =
        NEW.quantity * NEW.unit_price;

END //

DELIMITER ;
```

---

# 18. Practical Application — Inventory Management

Consider an e-commerce application.

Whenever an order is placed, stock should decrease.

Suppose:

Product: Keyboard

Available stock: 50

Order quantity: 5

After order:

Available stock: 45

## Step 1: Create Products Table

```sql
CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(50),
    stock INT
);
```

## Step 2: Create Orders Table

```sql
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    product_id INT,
    quantity INT
);
```

## Step 3: Insert Products

```sql
INSERT INTO products
VALUES
(1, 'Keyboard', 50),
(2, 'Mouse', 100);
```

## Step 4: BEFORE INSERT Trigger

```sql
DELIMITER //

CREATE TRIGGER check_stock
BEFORE INSERT ON orders
FOR EACH ROW
BEGIN

    DECLARE available_stock INT;

    SELECT stock
    INTO available_stock
    FROM products
    WHERE product_id = NEW.product_id
    FOR UPDATE;

    IF available_stock IS NULL THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Product does not exist';

    END IF;

    IF NEW.quantity <= 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Quantity must be positive';

    END IF;

    IF available_stock < NEW.quantity THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Insufficient stock';

    END IF;

END //

DELIMITER ;
```

## Step 5: AFTER INSERT Trigger

```sql
DELIMITER //

CREATE TRIGGER reduce_stock
AFTER INSERT ON orders
FOR EACH ROW
BEGIN

    UPDATE products
    SET stock = stock - NEW.quantity
    WHERE product_id = NEW.product_id;

END //

DELIMITER ;
```

## Step 6: Place an Order

```sql
INSERT INTO orders
VALUES (101, 1, 5);
```

## Step 7: Check Stock

```sql
SELECT * FROM products;
```

Expected:

| product_id | product_name | stock |
|---|---|---:|
| 1 | Keyboard | 45 |
| 2 | Mouse | 100 |

### Important Production Considerations

A real inventory system must also handle:

1. Order updates.
2. Order cancellations.
3. Refunds.
4. Concurrent purchases.
5. Transaction consistency.
6. Foreign-key relationships.
7. Stock restoration.

This example teaches the trigger mechanism. A production-grade order workflow should carefully coordinate all related operations within transactions.

---

# 19. Trigger Restrictions and Limitations

This section is especially important for interviews.

## 19.1 Cannot Return Result Sets

Triggers cannot directly return result sets to the client.

A SELECT INTO statement is permitted because it retrieves a value into a local variable.

Example:

```sql
SELECT salary
INTO emp_salary
FROM employees
WHERE employee_id = 101;
```

## 19.2 Cannot Explicitly Control Transactions

Triggers cannot execute transaction-control statements such as:

```sql
START TRANSACTION;
COMMIT;
ROLLBACK;
```

They execute within the transaction context of the triggering statement.

## 19.3 Cannot Modify the Table That Activated Them

A trigger cannot modify the same table already being used by the statement that invoked the trigger.

For example, an INSERT on employees cannot activate a trigger that attempts to UPDATE employees.

However, the trigger can modify another table.

## 19.4 Cannot Use Dynamic SQL

Prepared statements are not permitted inside triggers.

## 19.5 No Statement-Level Triggers

MySQL supports only row-level triggers.

There is no FOR EACH STATEMENT trigger.

## 19.6 Cannot Be Called Manually

A trigger cannot be executed using CALL.

It executes automatically when its associated event occurs.

## 19.7 Trigger Execution Is Synchronous

The statement that activates a trigger waits for its execution to complete.

A slow trigger makes the original INSERT, UPDATE or DELETE operation slower.

## 19.8 Foreign-Key Cascades Do Not Activate Triggers

When a foreign-key cascade deletes or updates related rows, triggers on those cascaded rows are not activated by the cascading operation.

---

# 20. Error Handling Inside Triggers

MySQL supports condition handlers inside triggers.

Important constructs:

- DECLARE HANDLER
- CONTINUE HANDLER
- EXIT HANDLER
- SIGNAL
- RESIGNAL

## 20.1 SIGNAL

Raises an error explicitly.

```sql
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Invalid operation';
```

## 20.2 EXIT HANDLER

An EXIT handler terminates execution of the current compound block when the specified condition occurs.

Example:

```sql
DELIMITER //

CREATE TRIGGER example_handler
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'An error occurred';

    END;

    IF NEW.salary < 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Salary cannot be negative';

    END IF;

END //

DELIMITER ;
```

## 20.3 CONTINUE HANDLER

A CONTINUE handler handles the condition and continues execution after the statement that caused the condition.

Syntax:

```sql
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
BEGIN

    -- Error handling statements

END;
```

## 20.4 RESIGNAL

RESIGNAL passes an error condition onward from inside a handler, optionally modifying its properties.

It is useful when we want to perform some error handling but preserve the original error.

Important:

Do not suppress errors casually. It can lead to incorrect or inconsistent database state.

---

# 21. Managing Triggers

## 21.1 Display All Triggers

```sql
SHOW TRIGGERS;
```

## 21.2 Display Triggers for a Table

```sql
SHOW TRIGGERS
LIKE 'employees';
```

## 21.3 View Trigger Definition

```sql
SHOW CREATE TRIGGER after_employee_insert;
```

## 21.4 Using INFORMATION_SCHEMA

```sql
SELECT *
FROM INFORMATION_SCHEMA.TRIGGERS;
```

To retrieve important details:

```sql
SELECT
    TRIGGER_NAME,
    EVENT_MANIPULATION,
    EVENT_OBJECT_TABLE,
    ACTION_TIMING,
    ACTION_STATEMENT
FROM INFORMATION_SCHEMA.TRIGGERS;
```

## 21.5 Drop a Trigger

```sql
DROP TRIGGER trigger_name;
```

Safer version:

```sql
DROP TRIGGER IF EXISTS trigger_name;
```

## 21.6 Modify an Existing Trigger

MySQL does not provide an ALTER TRIGGER statement to modify its body.

To modify an existing trigger:

1. Drop the existing trigger.
2. Create the trigger again with the updated definition.

Example:

```sql
DROP TRIGGER IF EXISTS after_employee_insert;
```

Then recreate it.

---

# 22. Trigger vs Stored Procedure vs Function vs Event

| Feature | Trigger | Procedure | Function | Event |
|---|---|---|---|---|
| Execution | Automatic | Explicit | Explicit | Scheduled |
| Associated with table | Yes | No | No | No |
| Parameters | No explicit parameters | IN, OUT, INOUT | IN parameters | No routine parameters |
| Return value | No | No direct function return | Yes | No |
| Main purpose | React to table changes | Execute database operations | Calculate and return values | Execute scheduled operations |

Examples:

Trigger:

```sql
CREATE TRIGGER example_trigger
AFTER INSERT ON employees
FOR EACH ROW
SET @message = 'Employee added';
```

Procedure:

```sql
CALL calculate_bonus(101);
```

Function:

```sql
SELECT calculate_tax(5000);
```

Event:

```sql
CREATE EVENT daily_cleanup
ON SCHEDULE EVERY 1 DAY
DO
    DELETE FROM temporary_logs
    WHERE created_at < NOW() - INTERVAL 30 DAY;
```

The MySQL event scheduler must be enabled for scheduled events to execute.

---

# 23. Advantages of Triggers

1. Automation: Operations execute automatically.
2. Consistency: Common rules can be enforced centrally.
3. Audit logging: Changes can be recorded automatically.
4. Data integrity: Invalid data can be rejected.
5. Reduced duplication: Multiple applications can share database-level behavior.
6. Centralized enforcement: The database does not depend entirely on application-side validation.

# 24. Disadvantages of Triggers

1. Hidden execution: Developers may not know that additional operations are happening.
2. Debugging difficulty: Errors can be difficult to trace.
3. Performance overhead: Additional operations increase execution time.
4. Complexity: Too many triggers make maintenance difficult.
5. Unexpected side effects: A simple INSERT may affect several other tables.
6. Testing complexity: Trigger behavior must be tested with the statements that activate it.

---

# 25. Best Practices

1. Keep triggers small and focused.
2. Use meaningful trigger names.
3. Prefer constraints for simple declarative validation.
4. Avoid unnecessary calculations.
5. Avoid excessive trigger chaining.
6. Use OLD and NEW correctly.
7. Handle NULL values carefully.
8. Document trigger behavior.
9. Test multi-row INSERT, UPDATE and DELETE operations.
10. Test transaction rollback behavior.
11. Consider concurrent transactions.
12. Avoid placing large business workflows inside triggers.
13. Handle INSERT, UPDATE and DELETE when maintaining derived information.
14. Ensure related tables use appropriate storage engines.
15. Review triggers whenever the associated table structure changes.

---

# 26. Important Interview Questions and Answers

## Q1. What is a trigger?

A trigger is a database object associated with a table that automatically executes a predefined SQL block when a specified INSERT, UPDATE or DELETE event occurs.

## Q2. What are the six types of triggers in MySQL?

BEFORE INSERT, AFTER INSERT, BEFORE UPDATE, AFTER UPDATE, BEFORE DELETE and AFTER DELETE.

## Q3. Difference between BEFORE and AFTER triggers?

BEFORE triggers execute before the row operation and are useful for validation and modification.

AFTER triggers execute after the row operation succeeds and are useful for audit logging and related operations.

## Q4. What is OLD?

OLD represents the previous or existing row values.

It is available in UPDATE and DELETE triggers.

## Q5. What is NEW?

NEW represents the incoming or updated row values.

It is available in INSERT and UPDATE triggers.

## Q6. Can we modify OLD values?

No. OLD is read-only.

## Q7. Can we modify NEW values?

Yes, in BEFORE INSERT and BEFORE UPDATE triggers.

## Q8. Does MySQL support statement-level triggers?

No. MySQL supports row-level triggers only.

## Q9. What does FOR EACH ROW mean?

The trigger executes separately for each row affected by the triggering statement.

## Q10. Can we create multiple triggers for the same event?

Yes. MySQL supports multiple triggers for the same table, timing and event.

## Q11. What is DELIMITER?

DELIMITER changes the statement-ending delimiter in MySQL client tools so semicolons inside compound statements do not terminate the complete trigger definition prematurely.

## Q12. Can we call a trigger manually?

No. Triggers execute automatically.

## Q13. Can a trigger call a stored procedure?

Yes, subject to stored routine restrictions.

## Q14. Can a trigger return a value?

No. A trigger does not return a function-style value.

## Q15. Can we prevent an INSERT using a trigger?

Yes. A BEFORE INSERT trigger can raise an error using SIGNAL SQLSTATE.

## Q16. What happens if a trigger produces an error?

For InnoDB tables, the triggering statement fails and its transactional changes are rolled back.

## Q17. Can we create a trigger on a view?

No. MySQL triggers are associated with tables.

## Q18. Can we modify a trigger using ALTER TRIGGER?

No. Drop and recreate it.

## Q19. What are FOLLOWS and PRECEDES?

They control execution order between multiple triggers with the same timing and event.

## Q20. Are triggers activated by foreign-key cascades?

No. Foreign-key cascaded actions do not activate triggers on the affected rows.

## Q21. Can a trigger update the table that activated it?

No. MySQL does not permit a trigger to modify the table already being used by the statement that activated it.

## Q22. What is the most common application of triggers?

Audit logging, data validation, automatic calculations and maintaining related data.

---

# 27. Practice Questions

## Basic Level

Q1. Create a BEFORE INSERT trigger that adds 2000 to every incoming employee salary.

Q2. Create a BEFORE INSERT trigger that prevents employees younger than 18.

Q3. Create an AFTER INSERT trigger that records employee ID and insertion time.

Q4. Create a BEFORE UPDATE trigger that prevents salary reduction.

Q5. Create an AFTER DELETE trigger that stores deleted employee information in an archive table.

## Intermediate Level

Q6. Create an AFTER UPDATE trigger that records employee salary history.

Q7. Create a trigger that prevents insertion or update of products with negative prices.

Q8. Automatically calculate order total using quantity × price during INSERT and UPDATE.

Q9. Maintain a summary table containing the total number of students.

Q10. Prevent deletion of a student whose ID is 101.

## Advanced Level

Q11. Create an inventory management trigger that checks available stock and reduces it after an order is placed.

Q12. Maintain salary history while correctly handling NULL values.

Q13. Maintain total sales using INSERT, UPDATE and DELETE triggers.

Q14. Automatically classify employees based on salary.

Q15. Create multiple BEFORE INSERT triggers and control their order using FOLLOWS and PRECEDES.

---

# 28. Final Revision Sheet

## Six Trigger Types

| Trigger | Main purpose |
|---|---|
| BEFORE INSERT | Validate or modify incoming data |
| AFTER INSERT | Perform actions after insertion |
| BEFORE UPDATE | Validate or modify updated data |
| AFTER UPDATE | Maintain update history |
| BEFORE DELETE | Validate or prevent deletion |
| AFTER DELETE | Archive deleted information |

## OLD and NEW

| Operation | OLD | NEW |
|---|---|---|
| INSERT | No | Yes |
| UPDATE | Yes | Yes |
| DELETE | Yes | No |

## Important Syntax

Create a trigger:

```sql
CREATE TRIGGER trigger_name
BEFORE INSERT ON table_name
FOR EACH ROW
BEGIN

    -- Statements

END;
```

Modify NEW:

```sql
SET NEW.salary = NEW.salary + 1000;
```

Access OLD:

```sql
OLD.salary
```

Access NEW:

```sql
NEW.salary
```

Raise an error:

```sql
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Invalid operation';
```

Drop a trigger:

```sql
DROP TRIGGER IF EXISTS trigger_name;
```

Display triggers:

```sql
SHOW TRIGGERS;
```

Display trigger definition:

```sql
SHOW CREATE TRIGGER trigger_name;
```

---

# 29. Most Important Concepts to Remember

1. A trigger executes automatically.
2. A trigger is associated with a table.
3. MySQL supports six basic trigger combinations.
4. MySQL supports row-level triggers only.
5. FOR EACH ROW is mandatory.
6. INSERT uses NEW.
7. UPDATE uses OLD and NEW.
8. DELETE uses OLD.
9. NEW can be modified only in BEFORE triggers.
10. AFTER triggers are commonly used for audit logging.
11. Multiple triggers can exist for the same timing and event.
12. FOLLOWS and PRECEDES control execution order.
13. Triggers cannot be called manually.
14. Triggers cannot return result sets.
15. Triggers cannot explicitly commit or roll back transactions.
16. Triggers cannot modify the table that activated them.
17. Foreign-key cascades do not activate triggers.
18. Triggers can call stored procedures subject to restrictions.
19. Trigger execution is synchronous.
20. Prefer constraints whenever simple declarative constraints are sufficient.

## Final Conceptual Understanding

The entire trigger chapter revolves around four important ideas:

**BEFORE:** Execute before the row operation. Useful for validation and modification.

**AFTER:** Execute after the row operation succeeds. Useful for logging and maintaining related data.

**OLD:** Represents previous row values.

**NEW:** Represents incoming or updated row values.

Once these four ideas are clear, the remaining trigger concepts become much easier to understand.

---

**End of MySQL Triggers Chapter.**
