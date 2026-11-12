# MYSQL TRIGGERS — PRACTICE QUESTIONS WITH COMPLETE SOLUTIONS

# BASIC LEVEL

# Q1. Create a BEFORE INSERT Trigger That Adds 2000 to Every Incoming Employee Salary

## 1. Problem Understanding

We need to create a trigger that automatically increases the salary of every employee by 2000 before inserting the employee record.

For example:

Original salary = 30000

Additional amount = 2000

Final salary = 32000

Since we need to modify incoming data before insertion, we use:

**BEFORE INSERT Trigger**

We use `NEW.salary` because NEW represents the incoming row.

## 2. Create Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2)
);
```

## 3. Create Trigger

```sql
DELIMITER //

CREATE TRIGGER add_salary_bonus
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN

    SET NEW.salary = NEW.salary + 2000;

END //

DELIMITER ;
```

## 4. Insert Data

```sql
INSERT INTO employees
VALUES
(1, 'Amit', 30000),
(2, 'Rahul', 40000),
(3, 'Priya', 50000);
```

## 5. Check Output

```sql
SELECT * FROM employees;
```

**Output:**

| employee_id | name | salary |
|---|---|---:|
| 1 | Amit | 32000 |
| 2 | Rahul | 42000 |
| 3 | Priya | 52000 |

## 6. Execution Logic

The INSERT statement contains three rows.

Because the trigger uses `FOR EACH ROW`, it executes separately for every row.

**First execution:**

```text
NEW.salary = 30000
NEW.salary = 30000 + 2000
NEW.salary = 32000
```

**Second execution:**

```text
NEW.salary = 40000
NEW.salary = 40000 + 2000
NEW.salary = 42000
```

**Third execution:**

```text
NEW.salary = 50000
NEW.salary = 50000 + 2000
NEW.salary = 52000
```

The modified values are inserted into the table.

**Concepts used:**
- BEFORE INSERT
- NEW keyword
- SET statement
- FOR EACH ROW

---

# Q2. Create a BEFORE INSERT Trigger That Prevents Employees Younger Than 18

## 1. Problem Understanding

We need to ensure that no employee younger than 18 can be inserted.

Condition:

```text
age < 18 → Reject insertion
age >= 18 → Allow insertion
```

We use:

- BEFORE INSERT
- NEW.age
- IF condition
- SIGNAL SQLSTATE

## 2. Create Table

```sql
CREATE TABLE employee_details (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    age INT
);
```

## 3. Create Trigger

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

## 4. Test Valid Insertion

```sql
INSERT INTO employee_details
VALUES (1, 'Amit', 25);
```

This insertion succeeds.

## 5. Test Invalid Insertion

```sql
INSERT INTO employee_details
VALUES (2, 'Rahul', 16);
```

**Expected error:**

```text
ERROR 1644 (45000):
Employee age must be at least 18
```

## 6. Check Output

```sql
SELECT * FROM employee_details;
```

**Output:**

| employee_id | name | age |
|---|---|---:|
| 1 | Amit | 25 |

Rahul is not inserted because his age is below 18.

## 7. Execution Logic

When MySQL receives the INSERT statement:

1. BEFORE INSERT trigger executes.
2. NEW.age contains the incoming age.
3. IF condition checks whether age is below 18.
4. If true, SIGNAL raises an error.
5. The insertion is rejected.
6. Otherwise, the insertion proceeds.

**Important:** This example assumes age is NOT NULL. If NULL must also be rejected, add:

```sql
IF NEW.age IS NULL OR NEW.age < 18 THEN
```

---

# Q3. Create an AFTER INSERT Trigger That Records Employee ID and Insertion Time

## 1. Problem Understanding

Whenever a new employee is inserted, we want to automatically record:

- Employee ID
- Insertion date and time

We need a separate logging table.

Since we want to perform an operation after successful insertion, we use:

**AFTER INSERT Trigger**

## 2. Create Employee Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2)
);
```

## 3. Create Log Table

```sql
CREATE TABLE employee_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    insertion_time DATETIME
);
```

## 4. Create Trigger

```sql
DELIMITER //

CREATE TRIGGER log_employee_insert
AFTER INSERT ON employees
FOR EACH ROW
BEGIN

    INSERT INTO employee_log
    (
        employee_id,
        insertion_time
    )

    VALUES
    (
        NEW.employee_id,
        NOW()
    );

END //

DELIMITER ;
```

## 5. Insert Employees

```sql
INSERT INTO employees
VALUES
(101, 'Amit', 30000),
(102, 'Rahul', 40000),
(103, 'Priya', 50000);
```

## 6. Check Employee Table

```sql
SELECT * FROM employees;
```

| employee_id | name | salary |
|---|---|---:|
| 101 | Amit | 30000 |
| 102 | Rahul | 40000 |
| 103 | Priya | 50000 |

## 7. Check Log Table

```sql
SELECT * FROM employee_log;
```

**Expected output:**

| log_id | employee_id | insertion_time |
|---:|---:|---|
| 1 | 101 | Current timestamp |
| 2 | 102 | Current timestamp |
| 3 | 103 | Current timestamp |

## 8. Execution Logic

The INSERT statement inserts three employees.

For each inserted employee:

1. AFTER INSERT trigger activates.
2. NEW.employee_id retrieves the inserted employee ID.
3. NOW() retrieves the current date and time.
4. The trigger inserts these values into employee_log.

**Important concepts:**

- AFTER INSERT
- NEW keyword
- NOW() function
- INSERT inside a trigger
- Automatic audit logging

---

# Q4. Create a BEFORE UPDATE Trigger That Prevents Salary Reduction

## 1. Problem Understanding

We need to prevent an employee's salary from becoming lower than the previous salary.

Suppose:

| Previous Salary | Updated Salary | Result |
|---:|---:|---|
| 30000 | 35000 | Allowed |
| 40000 | 40000 | Allowed |
| 50000 | 45000 | Rejected |

Since we need to compare old and new values, we use:

- OLD.salary
- NEW.salary

## 2. Create Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2)
);
```

## 3. Insert Sample Data

```sql
INSERT INTO employees
VALUES
(1, 'Amit', 30000),
(2, 'Rahul', 40000),
(3, 'Priya', 50000);
```

## 4. Create Trigger

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

## 5. Test Case 1 — Salary Increase

```sql
UPDATE employees
SET salary = 35000
WHERE employee_id = 1;
```

**Result:** Successful.

Reason:

```text
OLD.salary = 30000
NEW.salary = 35000

35000 < 30000 → FALSE
```

## 6. Test Case 2 — Salary Reduction

```sql
UPDATE employees
SET salary = 35000
WHERE employee_id = 2;
```

**Expected error:**

```text
Salary reduction is not allowed
```

Reason:

```text
OLD.salary = 40000
NEW.salary = 35000

35000 < 40000 → TRUE
```

The trigger rejects the update.

## 7. Important Concept

During UPDATE:

- OLD represents the existing value.
- NEW represents the proposed updated value.

This is one of the most common uses of OLD and NEW.

---

# Q5. Create an AFTER DELETE Trigger That Stores Deleted Employee Information in an Archive Table

## 1. Problem Understanding

Whenever an employee is deleted from the employees table, we want to preserve their information in a separate archive table.

For example:

Original employee:

| employee_id | name | salary |
|---|---|---:|
| 101 | Amit | 40000 |

After deletion:

- Employee is removed from employees.
- Employee details are inserted into deleted_employees.

We use:

**AFTER DELETE Trigger**

During DELETE, we use OLD because the deleted row's values are available through OLD.

## 2. Create Employee Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2)
);
```

## 3. Create Archive Table

```sql
CREATE TABLE deleted_employees (
    archive_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    name VARCHAR(50),
    salary DECIMAL(10,2),
    deleted_at DATETIME
);
```

## 4. Insert Sample Data

```sql
INSERT INTO employees
VALUES
(101, 'Amit', 40000),
(102, 'Rahul', 50000),
(103, 'Priya', 60000);
```

## 5. Create Trigger

```sql
DELIMITER //

CREATE TRIGGER archive_deleted_employee
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

## 6. Delete Employee

```sql
DELETE FROM employees
WHERE employee_id = 101;
```

## 7. Check Employees Table

```sql
SELECT * FROM employees;
```

| employee_id | name | salary |
|---|---|---:|
| 102 | Rahul | 50000 |
| 103 | Priya | 60000 |

## 8. Check Archive Table

```sql
SELECT * FROM deleted_employees;
```

| employee_id | name | salary |
|---|---|---:|
| 101 | Amit | 40000 |

## 9. Execution Logic

1. DELETE statement is executed.
2. The employee row is deleted.
3. AFTER DELETE trigger activates.
4. OLD.employee_id accesses the deleted employee ID.
5. OLD.name accesses the deleted employee name.
6. OLD.salary accesses the deleted employee salary.
7. These values are inserted into the archive table.

**Important:** With InnoDB, the deletion and archival insertion are part of the same transaction. If the statement fails, both changes are rolled back.

---

# INTERMEDIATE LEVEL

# Q6. Create an AFTER UPDATE Trigger That Records Employee Salary History

## 1. Problem Understanding

We need to maintain the salary history of employees.

Whenever an employee's salary changes, record:

- Employee ID
- Old salary
- New salary
- Change date and time

Example:

Before update:

| employee_id | salary |
|---|---:|
| 101 | 40000 |

After update:

| employee_id | salary |
|---|---:|
| 101 | 50000 |

History should contain:

| employee_id | old_salary | new_salary |
|---|---:|---:|
| 101 | 40000 | 50000 |

We use AFTER UPDATE because we want to record successful updates.

## 2. Create Employee Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2)
);
```

## 3. Create Salary History Table

```sql
CREATE TABLE salary_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    old_salary DECIMAL(10,2),
    new_salary DECIMAL(10,2),
    changed_at DATETIME
);
```

## 4. Insert Sample Data

```sql
INSERT INTO employees
VALUES
(101, 'Amit', 40000),
(102, 'Rahul', 50000);
```

## 5. Create Trigger

```sql
DELIMITER //

CREATE TRIGGER salary_history_trigger
AFTER UPDATE ON employees
FOR EACH ROW
BEGIN

    IF OLD.salary <> NEW.salary THEN

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

## 6. Update Salary

```sql
UPDATE employees
SET salary = 60000
WHERE employee_id = 101;
```

## 7. Check Salary History

```sql
SELECT * FROM salary_history;
```

**Output:**

| history_id | employee_id | old_salary | new_salary |
|---:|---:|---:|---:|
| 1 | 101 | 40000 | 60000 |

## 8. Explanation

The trigger compares:

```sql
OLD.salary <> NEW.salary
```

If the salary is different, the history record is inserted.

If the salary remains the same, no history record is created.

**Advanced improvement:** The comparison above does not correctly detect every change involving NULL. Q12 covers the correct NULL-safe implementation.

---

# Q7. Create a Trigger That Prevents Insertion or Update of Products with Negative Prices

## 1. Problem Understanding

We need to ensure that product prices are never negative.

This rule must be applied to two operations:

1. INSERT — prevent a negative initial price.
2. UPDATE — prevent a negative updated price.

Therefore, we need two triggers:

- BEFORE INSERT
- BEFORE UPDATE

## 2. Create Products Table

```sql
CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(50),
    price DECIMAL(10,2)
);
```

## 3. BEFORE INSERT Trigger

```sql
DELIMITER //

CREATE TRIGGER validate_product_insert
BEFORE INSERT ON products
FOR EACH ROW
BEGIN

    IF NEW.price < 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Product price cannot be negative';

    END IF;

END //

DELIMITER ;
```

## 4. BEFORE UPDATE Trigger

```sql
DELIMITER //

CREATE TRIGGER validate_product_update
BEFORE UPDATE ON products
FOR EACH ROW
BEGIN

    IF NEW.price < 0 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Product price cannot be negative';

    END IF;

END //

DELIMITER ;
```

## 5. Test INSERT

Valid:

```sql
INSERT INTO products
VALUES (1, 'Keyboard', 1500);
```

Invalid:

```sql
INSERT INTO products
VALUES (2, 'Mouse', -500);
```

The second INSERT is rejected.

## 6. Test UPDATE

```sql
UPDATE products
SET price = -100
WHERE product_id = 1;
```

The update is rejected.

## 7. Important Concept

Why do we need two triggers?

Because INSERT and UPDATE are different database events.

A BEFORE INSERT trigger does not execute during UPDATE.

Similarly, a BEFORE UPDATE trigger does not execute during INSERT.

**Important improvement:** If zero prices are also invalid, change the condition to:

```sql
IF NEW.price <= 0 THEN
```

For a simple price rule, a CHECK constraint is generally preferable:

```sql
ALTER TABLE products
ADD CONSTRAINT chk_product_price
CHECK (price >= 0);
```

---

# Q8. Automatically Calculate Order Total Using Quantity × Price During INSERT and UPDATE

## 1. Problem Understanding

We have an order_items table containing:

- Quantity
- Unit price
- Total price

Formula:

Total Price = Quantity × Unit Price

Whenever a new order item is inserted or its quantity/unit price is updated, total_price must be recalculated.

We need:

1. BEFORE INSERT
2. BEFORE UPDATE

## 2. Create Table

```sql
CREATE TABLE order_items (
    item_id INT PRIMARY KEY,
    product_name VARCHAR(50),
    quantity INT,
    unit_price DECIMAL(10,2),
    total_price DECIMAL(10,2)
);
```

## 3. BEFORE INSERT Trigger

```sql
DELIMITER //

CREATE TRIGGER calculate_total_insert
BEFORE INSERT ON order_items
FOR EACH ROW
BEGIN

    SET NEW.total_price =
        NEW.quantity * NEW.unit_price;

END //

DELIMITER ;
```

## 4. BEFORE UPDATE Trigger

```sql
DELIMITER //

CREATE TRIGGER calculate_total_update
BEFORE UPDATE ON order_items
FOR EACH ROW
BEGIN

    SET NEW.total_price =
        NEW.quantity * NEW.unit_price;

END //

DELIMITER ;
```

## 5. Insert Data

```sql
INSERT INTO order_items
VALUES
(1, 'Keyboard', 2, 1500, 0),
(2, 'Mouse', 3, 500, 0);
```

## 6. Check Output

```sql
SELECT * FROM order_items;
```

| item_id | product_name | quantity | unit_price | total_price |
|---|---|---:|---:|---:|
| 1 | Keyboard | 2 | 1500 | 3000 |
| 2 | Mouse | 3 | 500 | 1500 |

## 7. Update Data

```sql
UPDATE order_items
SET quantity = 5
WHERE item_id = 1;
```

## 8. Check Updated Output

```sql
SELECT * FROM order_items;
```

| item_id | product_name | quantity | unit_price | total_price |
|---|---|---:|---:|---:|
| 1 | Keyboard | 5 | 1500 | 7500 |
| 2 | Mouse | 3 | 500 | 1500 |

## 9. Explanation

During INSERT:

```text
quantity = 2
unit_price = 1500

total_price = 2 × 1500

total_price = 3000
```

During UPDATE:

```text
quantity = 5
unit_price = 1500

total_price = 5 × 1500

total_price = 7500
```

Both triggers calculate the total before the row is stored.

**Important:** In production, also validate that quantity and unit_price are not NULL or negative.

---

# Q9. Maintain a Summary Table Containing the Total Number of Students

## 1. Problem Understanding

Suppose we have a students table.

We also want a separate summary table that always maintains the total number of students.

Whenever a student is inserted:

Total count increases by 1.

Whenever a student is deleted:

Total count decreases by 1.

We need:

- AFTER INSERT
- AFTER DELETE

We also need to consider UPDATE only if the operation changes student identity in a way that affects the counting rule. An ordinary UPDATE of a student's name does not change the total.

## 2. Create Students Table

```sql
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(50)
);
```

## 3. Create Summary Table

```sql
CREATE TABLE student_summary (
    total_students INT NOT NULL
);
```

Initialize the summary:

```sql
INSERT INTO student_summary
VALUES (0);
```

## 4. AFTER INSERT Trigger

```sql
DELIMITER //

CREATE TRIGGER increase_student_count
AFTER INSERT ON students
FOR EACH ROW
BEGIN

    UPDATE student_summary
    SET total_students = total_students + 1;

END //

DELIMITER ;
```

## 5. AFTER DELETE Trigger

```sql
DELIMITER //

CREATE TRIGGER decrease_student_count
AFTER DELETE ON students
FOR EACH ROW
BEGIN

    UPDATE student_summary
    SET total_students = total_students - 1;

END //

DELIMITER ;
```

## 6. Insert Students

```sql
INSERT INTO students
VALUES
(1, 'Amit'),
(2, 'Rahul'),
(3, 'Priya');
```

## 7. Check Summary

```sql
SELECT * FROM student_summary;
```

Output:

| total_students |
|---:|
| 3 |

## 8. Delete a Student

```sql
DELETE FROM students
WHERE student_id = 2;
```

## 9. Check Summary Again

```sql
SELECT * FROM student_summary;
```

Output:

| total_students |
|---:|
| 2 |

## 10. Execution Logic

Initially:

```text
total_students = 0
```

After first insertion:

```text
0 + 1 = 1
```

After second insertion:

```text
1 + 1 = 2
```

After third insertion:

```text
2 + 1 = 3
```

After deletion:

```text
3 - 1 = 2
```

**Important:** The summary table should be initialized from the actual student count if it is created after students already exist.

For example:

```sql
UPDATE student_summary
SET total_students = (
    SELECT COUNT(*)
    FROM students
);
```

This ensures that the initial summary matches the actual data.

---

# Q10. Prevent Deletion of a Student Whose ID Is 101

## 1. Problem Understanding

We want to prevent deletion of a particular student.

Condition:

```text
student_id = 101 → Deletion rejected

student_id != 101 → Deletion allowed
```

We use:

**BEFORE DELETE Trigger**

During DELETE, we use OLD because the row being deleted is represented by OLD.

## 2. Create Table

```sql
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(50)
);
```

## 3. Insert Sample Data

```sql
INSERT INTO students
VALUES
(101, 'Amit'),
(102, 'Rahul'),
(103, 'Priya');
```

## 4. Create Trigger

```sql
DELIMITER //

CREATE TRIGGER prevent_student_delete
BEFORE DELETE ON students
FOR EACH ROW
BEGIN

    IF OLD.student_id = 101 THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Student with ID 101 cannot be deleted';

    END IF;

END //

DELIMITER ;
```

## 5. Test Case 1 — Delete Student 102

```sql
DELETE FROM students
WHERE student_id = 102;
```

Result: Successful.

## 6. Test Case 2 — Delete Student 101

```sql
DELETE FROM students
WHERE student_id = 101;
```

Expected error:

```text
Student with ID 101 cannot be deleted
```

## 7. Check Final Data

```sql
SELECT * FROM students;
```

| student_id | student_name |
|---:|---|
| 101 | Amit |
| 103 | Priya |

Student 101 remains in the table.

---

# ADVANCED LEVEL

# Q11. Create an Inventory Management Trigger That Checks Available Stock and Reduces It After an Order Is Placed

## 1. Problem Understanding

Suppose we have an e-commerce system.

A customer places an order for a particular product.

Before accepting the order, we must check whether sufficient stock is available.

If stock is available:

- Allow order insertion.
- Reduce stock after insertion.

If stock is insufficient:

- Reject the order.
- Do not reduce stock.

Example:

Available stock = 50

Order quantity = 5

Remaining stock = 45

## 2. Create Products Table

```sql
CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(50),
    stock INT NOT NULL
);
```

## 3. Create Orders Table

```sql
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id)
    REFERENCES products(product_id)
);
```

## 4. Insert Sample Products

```sql
INSERT INTO products
VALUES
(1, 'Keyboard', 50),
(2, 'Mouse', 100),
(3, 'Monitor', 10);
```

## 5. BEFORE INSERT Trigger — Validate Stock

```sql
DELIMITER //

CREATE TRIGGER check_product_stock
BEFORE INSERT ON orders
FOR EACH ROW
BEGIN

    DECLARE available_stock INT DEFAULT NULL;

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
        SET MESSAGE_TEXT = 'Order quantity must be positive';

    END IF;

    IF available_stock < NEW.quantity THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Insufficient stock available';

    END IF;

END //

DELIMITER ;
```

## 6. AFTER INSERT Trigger — Reduce Stock

```sql
DELIMITER //

CREATE TRIGGER reduce_product_stock
AFTER INSERT ON orders
FOR EACH ROW
BEGIN

    UPDATE products
    SET stock = stock - NEW.quantity
    WHERE product_id = NEW.product_id;

END //

DELIMITER ;
```

## 7. Place an Order

```sql
INSERT INTO orders
VALUES (101, 1, 5);
```

## 8. Check Orders

```sql
SELECT * FROM orders;
```

| order_id | product_id | quantity |
|---:|---:|---:|
| 101 | 1 | 5 |

## 9. Check Products

```sql
SELECT * FROM products;
```

| product_id | product_name | stock |
|---:|---|---:|
| 1 | Keyboard | 45 |
| 2 | Mouse | 100 |
| 3 | Monitor | 10 |

## 10. Test Insufficient Stock

Monitor stock = 10.

Try ordering 20:

```sql
INSERT INTO orders
VALUES (102, 3, 20);
```

Expected error:

```text
Insufficient stock available
```

The order is rejected.

The stock remains 10.

## 11. Important Execution Logic

For a valid order:

1. INSERT statement starts.
2. BEFORE INSERT trigger executes.
3. Available stock is retrieved.
4. Stock is compared with requested quantity.
5. If sufficient, validation completes.
6. Order row is inserted.
7. AFTER INSERT trigger executes.
8. Product stock is reduced.

### Important Production Consideration

This example covers order insertion. A complete inventory system must also handle:

- Order quantity updates.
- Order cancellations.
- Order deletion.
- Concurrent purchases.
- Refunds and stock restoration.

All related tables should use InnoDB, and stock validation and modification must be protected by correct transaction and locking logic.

---

# Q12. Maintain Salary History While Correctly Handling NULL Values

## 1. Problem Understanding

We need to maintain salary history whenever an employee's salary changes.

However, there is a special problem.

SQL handles NULL differently from ordinary values.

Consider:

```sql
NULL <> 50000
```

The result is UNKNOWN, not TRUE.

Therefore, this condition is not reliable:

```sql
IF OLD.salary <> NEW.salary THEN
```

It might fail to detect a change from NULL to 50000 or from 50000 to NULL.

## 2. Understanding the NULL-Safe Equality Operator

MySQL provides:

```sql
<=>
```

This is called the NULL-safe equality operator.

| Expression | Result |
|---|---|
| `10 <=> 10` | TRUE |
| `10 <=> 20` | FALSE |
| `NULL <=> NULL` | TRUE |
| `NULL <=> 10` | FALSE |
| `10 <=> NULL` | FALSE |

To detect whether values are different, we use:

```sql
NOT (OLD.salary <=> NEW.salary)
```

## 3. Create Employee Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2) NULL
);
```

## 4. Create History Table

```sql
CREATE TABLE salary_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    old_salary DECIMAL(10,2),
    new_salary DECIMAL(10,2),
    changed_at DATETIME
);
```

## 5. Insert Sample Data

```sql
INSERT INTO employees
VALUES
(1, 'Amit', 40000),
(2, 'Rahul', NULL),
(3, 'Priya', 50000);
```

## 6. Create Trigger

```sql
DELIMITER //

CREATE TRIGGER salary_history_null_safe
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

## 7. Test Case 1 — Normal Salary Change

```sql
UPDATE employees
SET salary = 45000
WHERE employee_id = 1;
```

History:

| employee_id | old_salary | new_salary |
|---:|---:|---:|
| 1 | 40000 | 45000 |

## 8. Test Case 2 — NULL to a Value

```sql
UPDATE employees
SET salary = 30000
WHERE employee_id = 2;
```

History:

| employee_id | old_salary | new_salary |
|---:|---:|---:|
| 2 | NULL | 30000 |

The change is correctly detected.

## 9. Test Case 3 — Value to NULL

```sql
UPDATE employees
SET salary = NULL
WHERE employee_id = 3;
```

History:

| employee_id | old_salary | new_salary |
|---:|---:|---:|
| 3 | 50000 | NULL |

The change is also correctly detected.

## 10. Test Case 4 — NULL to NULL

```sql
UPDATE employees
SET salary = NULL
WHERE employee_id = 2;
```

No new history record is created because the value has not changed.

## 11. Final History Query

```sql
SELECT * FROM salary_history;
```

Expected logical records:

| employee_id | old_salary | new_salary |
|---:|---:|---:|
| 1 | 40000 | 45000 |
| 2 | NULL | 30000 |
| 3 | 50000 | NULL |

**Important concept:** The NULL-safe equality operator is extremely useful in audit triggers and comparisons involving nullable columns.

---

# Q13. Maintain Total Sales Using INSERT, UPDATE and DELETE Triggers

## 1. Problem Understanding

Suppose we have a sales table.

We want to maintain a summary table containing the total sales amount.

Whenever a sale is:

- Inserted → Increase total sales.
- Updated → Adjust total sales.
- Deleted → Decrease total sales.

We need three triggers:

1. AFTER INSERT
2. AFTER UPDATE
3. AFTER DELETE

## 2. Create Sales Table

```sql
CREATE TABLE sales (
    sale_id INT PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL
);
```

## 3. Create Summary Table

```sql
CREATE TABLE sales_summary (
    total_sales DECIMAL(15,2) NOT NULL
);
```

Initialize:

```sql
INSERT INTO sales_summary
VALUES (0);
```

## 4. AFTER INSERT Trigger

```sql
DELIMITER //

CREATE TRIGGER update_total_after_insert
AFTER INSERT ON sales
FOR EACH ROW
BEGIN

    UPDATE sales_summary
    SET total_sales = total_sales + NEW.amount;

END //

DELIMITER ;
```

Logic:

The new sale increases total sales.

Formula:

```text
New Total = Old Total + NEW.amount
```

## 5. AFTER UPDATE Trigger

```sql
DELIMITER //

CREATE TRIGGER update_total_after_update
AFTER UPDATE ON sales
FOR EACH ROW
BEGIN

    UPDATE sales_summary
    SET total_sales =
        total_sales - OLD.amount + NEW.amount;

END //

DELIMITER ;
```

Logic:

Suppose the old sale amount is 1000 and the updated amount is 1500.

We first remove the old amount from the summary and then add the new amount.

Formula:

```text
New Total = Old Total - OLD.amount + NEW.amount
```

## 6. AFTER DELETE Trigger

```sql
DELIMITER //

CREATE TRIGGER update_total_after_delete
AFTER DELETE ON sales
FOR EACH ROW
BEGIN

    UPDATE sales_summary
    SET total_sales = total_sales - OLD.amount;

END //

DELIMITER ;
```

Logic:

The deleted sale amount must be removed from the summary.

Formula:

```text
New Total = Old Total - OLD.amount
```

## 7. Test INSERT

```sql
INSERT INTO sales
VALUES
(1, 1000),
(2, 2000),
(3, 3000);
```

Check summary:

```sql
SELECT * FROM sales_summary;
```

Output:

| total_sales |
|---:|
| 6000 |

## 8. Test UPDATE

```sql
UPDATE sales
SET amount = 4000
WHERE sale_id = 1;
```

Calculation:

```text
Old Total = 6000

Old Sale = 1000
New Sale = 4000

New Total = 6000 - 1000 + 4000

New Total = 9000
```

Check:

```sql
SELECT * FROM sales_summary;
```

Output:

| total_sales |
|---:|
| 9000 |

## 9. Test DELETE

```sql
DELETE FROM sales
WHERE sale_id = 2;
```

Calculation:

```text
Old Total = 9000
Deleted Sale = 2000

New Total = 9000 - 2000

New Total = 7000
```

Check:

```sql
SELECT * FROM sales_summary;
```

Output:

| total_sales |
|---:|
| 7000 |

## 10. Important Concept

All three operations must be handled.

If we create only an INSERT trigger, the summary becomes incorrect whenever someone updates or deletes an existing sale.

This is a very important principle when maintaining derived data using triggers.

---

# Q14. Automatically Classify Employees Based on Salary

## 1. Problem Understanding

We want to assign an employee category automatically based on salary.

Rules:

| Salary | Category |
|---:|---|
| Below 30000 | Junior |
| 30000 to 69999.99 | Mid Level |
| 70000 and above | Senior |

Whenever an employee is inserted or their salary is updated, the category must be recalculated.

Therefore, we need:

- BEFORE INSERT
- BEFORE UPDATE

We can use IF / ELSEIF / ELSE.

## 2. Create Employee Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2),
    category VARCHAR(30)
);
```

## 3. BEFORE INSERT Trigger

```sql
DELIMITER //

CREATE TRIGGER classify_employee_insert
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

## 4. BEFORE UPDATE Trigger

```sql
DELIMITER //

CREATE TRIGGER classify_employee_update
BEFORE UPDATE ON employees
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

## 5. Insert Employees

```sql
INSERT INTO employees
(employee_id, name, salary)
VALUES
(1, 'Amit', 25000),
(2, 'Rahul', 50000),
(3, 'Priya', 80000);
```

## 6. Check Output

```sql
SELECT * FROM employees;
```

| employee_id | name | salary | category |
|---:|---|---:|---|
| 1 | Amit | 25000 | Junior |
| 2 | Rahul | 50000 | Mid Level |
| 3 | Priya | 80000 | Senior |

## 7. Update Salary

```sql
UPDATE employees
SET salary = 90000
WHERE employee_id = 2;
```

Check:

```sql
SELECT * FROM employees;
```

Updated row:

| employee_id | name | salary | category |
|---:|---|---:|---|
| 2 | Rahul | 90000 | Senior |

## 8. Explanation

The trigger checks the salary whenever an INSERT or UPDATE occurs.

For salary 25000:

```text
25000 < 30000
Category = Junior
```

For salary 50000:

```text
50000 < 30000 → FALSE
50000 < 70000 → TRUE

Category = Mid Level
```

For salary 80000:

```text
80000 < 30000 → FALSE
80000 < 70000 → FALSE

Category = Senior
```

**Important:** This example assumes salary is not NULL. A production implementation should explicitly validate NULL and negative salary values.

---

# Q15. Create Multiple BEFORE INSERT Triggers and Control Their Order Using FOLLOWS and PRECEDES

## 1. Problem Understanding

MySQL allows multiple triggers with the same timing and event on a table.

Suppose we want to create two BEFORE INSERT triggers:

**Trigger 1: add_bonus**

Adds 1000 to the incoming salary.

**Trigger 2: calculate_final_salary**

Adds another 10% to the salary after the first trigger has executed.

We want to control their execution order using FOLLOWS.

## 2. Create Employee Table

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2)
);
```

## 3. Create First Trigger

```sql
DELIMITER //

CREATE TRIGGER add_bonus
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN

    SET NEW.salary = NEW.salary + 1000;

END //

DELIMITER ;
```

This trigger adds 1000.

## 4. Create Second Trigger Using FOLLOWS

```sql
DELIMITER //

CREATE TRIGGER calculate_final_salary
BEFORE INSERT ON employees
FOR EACH ROW
FOLLOWS add_bonus
BEGIN

    SET NEW.salary = NEW.salary * 1.10;

END //

DELIMITER ;
```

Important:

`FOLLOWS add_bonus` means that `calculate_final_salary` executes after `add_bonus`.

## 5. Insert Employee

```sql
INSERT INTO employees
VALUES (1, 'Amit', 30000);
```

## 6. Understand Execution Order

Initially:

```text
Salary = 30000
```

First trigger executes:

```text
add_bonus

30000 + 1000 = 31000
```

Second trigger executes:

```text
calculate_final_salary

31000 × 1.10 = 34100
```

Final salary:

```text
34100
```

## 7. Check Output

```sql
SELECT * FROM employees;
```

| employee_id | name | salary |
|---:|---|---:|
| 1 | Amit | 34100 |

## 8. Understanding PRECEDES

PRECEDES is used when we want the newly created trigger to execute before an existing trigger.

Example:

```sql
DELIMITER //

CREATE TRIGGER calculate_final_salary
BEFORE INSERT ON employees
FOR EACH ROW
PRECEDES add_bonus
BEGIN

    SET NEW.salary = NEW.salary * 1.10;

END //

DELIMITER ;
```

If this trigger is created before add_bonus in the execution order, the calculation becomes:

Initially:

```text
30000
```

First:

```text
30000 × 1.10 = 33000
```

Second:

```text
33000 + 1000 = 34000
```

Final salary:

```text
34000
```

## 9. Difference Between FOLLOWS and PRECEDES

| Keyword | Meaning |
|---|---|
| FOLLOWS | Execute after the specified trigger |
| PRECEDES | Execute before the specified trigger |

**Important:** When using FOLLOWS or PRECEDES, the referenced trigger must already exist and must have the same timing and event.

---

# FINAL REVISION — ALL 15 QUESTIONS

| Q.No. | Problem | Trigger Type / Types | Main Concept |
|---|---|---|---|
| Q1 | Add 2000 to salary | BEFORE INSERT | NEW |
| Q2 | Prevent age below 18 | BEFORE INSERT | IF, SIGNAL |
| Q3 | Employee insertion log | AFTER INSERT | NEW, NOW() |
| Q4 | Prevent salary reduction | BEFORE UPDATE | OLD, NEW |
| Q5 | Archive deleted employee | AFTER DELETE | OLD |
| Q6 | Salary history | AFTER UPDATE | OLD, NEW |
| Q7 | Prevent negative price | BEFORE INSERT, BEFORE UPDATE | Validation |
| Q8 | Calculate order total | BEFORE INSERT, BEFORE UPDATE | Automatic calculation |
| Q9 | Maintain student count | AFTER INSERT, AFTER DELETE | Summary table |
| Q10 | Prevent student deletion | BEFORE DELETE | OLD, SIGNAL |
| Q11 | Inventory management | BEFORE INSERT, AFTER INSERT | Stock validation |
| Q12 | NULL-safe salary history | AFTER UPDATE | `<=>` |
| Q13 | Maintain total sales | AFTER INSERT, AFTER UPDATE, AFTER DELETE | Summary maintenance |
| Q14 | Employee classification | BEFORE INSERT, BEFORE UPDATE | IF, ELSEIF |
| Q15 | Control multiple triggers | BEFORE INSERT | FOLLOWS, PRECEDES |

# IMPORTANT CONCEPTS USED IN THESE SOLUTIONS

## 1. NEW Keyword

Used to access incoming or updated row values.

```sql
NEW.salary
NEW.employee_id
NEW.name
```

## 2. OLD Keyword

Used to access previous or deleted row values.

```sql
OLD.salary
OLD.employee_id
OLD.name
```

## 3. SIGNAL

Used to raise a custom error.

```sql
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Invalid operation';
```

## 4. NOW()

Returns the current date and time.

```sql
NOW()
```

## 5. NULL-Safe Comparison

```sql
OLD.salary <=> NEW.salary
```

Returns TRUE when both values are equal, including when both are NULL.

## 6. FOLLOWS

Executes a newly created trigger after another trigger.

## 7. PRECEDES

Executes a newly created trigger before another trigger.

---

# FINAL LEARNING CHECKLIST

- [ ] Understand all six trigger types.
- [ ] Understand OLD and NEW.
- [ ] Understand FOR EACH ROW.
- [ ] Understand BEFORE vs AFTER.
- [ ] Understand DELIMITER.
- [ ] Understand IF and SIGNAL.
- [ ] Understand local variables using DECLARE.
- [ ] Understand audit logging.
- [ ] Understand automatic calculations.
- [ ] Understand summary table maintenance.
- [ ] Understand NULL-safe comparison.
- [ ] Understand FOLLOWS and PRECEDES.
- [ ] Understand trigger restrictions.
- [ ] Practice all 15 questions independently.

**Recommended practice approach:** First write each trigger without referring to the solution. Then execute it, inspect the table, and test both valid and invalid inputs. This will build actual trigger-writing skills rather than just syntax familiarity.
