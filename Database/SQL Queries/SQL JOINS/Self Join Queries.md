# SQL SELF JOIN — Examples with Tables, Queries and Outputs

A SELF JOIN is used when rows in the same table are related to other rows in that table.

We'll use one `employees` table for several examples, then explore other real-world scenarios.

## Sample table: `employees`

| employee_id | employee_name | manager_id | department_id | salary |
| ----------- | ------------- | ---------- | ------------- | ------ |
| 1           | Amit          | NULL       | 10            | 90000  |
| 2           | Priya         | 1          | 10            | 70000  |
| 3           | Rahul         | 1          | 20            | 65000  |
| 4           | Neha          | 2          | 10            | 50000  |
| 5           | Karan         | 2          | 20            | 45000  |
| 6           | Sneha         | 3          | 20            | 60000  |

## Example 1: Employee and manager names

Question: Display every employee with their manager's name.

``` SQL
SELECT
    e.employee_name AS employee,
    m.employee_name AS manager
FROM employees AS e
LEFT JOIN employees AS m
    ON e.manager_id = m.employee_id;
```

Output:

| employee | manager |
| -------- | ------- |
| Amit     | NULL    |
| Priya    | Amit    |
| Rahul    | Amit    |
| Neha     | Priya   |
| Karan    | Priya   |
| Sneha    | Rahul   |

Explanation: `e` represents the employee and `m` represents the manager. `LEFT JOIN` keeps Amit even though he has no manager.

## Example 2: Find employees who have the same manager

Question: Find pairs of employees who report to the same manager.

``` SQL
SELECT
    e1.employee_name AS employee1,
    e2.employee_name AS employee2,
    e1.manager_id
FROM employees AS e1
INNER JOIN employees AS e2
    ON e1.manager_id = e2.manager_id
WHERE e1.employee_id < e2.employee_id
  AND e1.manager_id IS NOT NULL;
```

Output:

| employee1 | employee2 | manager_id |
| --------- | --------- | ---------- |
| Priya     | Rahul     | 1          |
| Neha      | Karan     | 2          |

Explanation:

- `e1.manager_id = e2.manager_id` finds employees with the same manager.
- `e1.employee_id < e2.employee_id` prevents duplicate pairs and prevents an employee from matching themselves.
- `e1.manager_id IS NOT NULL` excludes employees who have no manager.

Why use `<` instead of `!=`? Because without it, both `(Priya, Rahul)` and `(Rahul, Priya)` would appear.

## Example 3: Find employees earning more than their managers

Question: Which employees have a higher salary than their managers?

``` SQL
SELECT
    e.employee_name AS employee,
    e.salary AS employee_salary,
    m.employee_name AS manager,
    m.salary AS manager_salary
FROM employees AS e
INNER JOIN employees AS m
    ON e.manager_id = m.employee_id
WHERE e.salary > m.salary;
```

Output:

| employee | employee_salary | manager | manager_salary |
| -------- | --------------- | ------- | -------------- |
| None     | —               | —       | —              |

With the sample data, no rows are returned because every employee earns less than their manager.

Let's modify the example to make it meaningful: if Neha's salary increases to `80000`, the output will include:

| employee | employee_salary | manager | manager_salary |
| -------- | --------------- | ------- | -------------- |
| Neha     | 80000           | Priya   | 70000          |

Key idea: SELF JOIN lets you compare values between two related rows, even when both rows come from the same table.

## Example 4: Find employees in the same department

Question: Display pairs of different employees who work in the same department.

```SQL
SELECT
    e1.employee_name AS employee1,
    e2.employee_name AS employee2,
    e1.department_id
FROM employees AS e1
INNER JOIN employees AS e2
    ON e1.department_id = e2.department_id
WHERE e1.employee_id < e2.employee_id;
```

Output:

| employee1 | employee2 | department_id |
| --------- | --------- | ------------- |
| Amit      | Priya     | 10            |
| Amit      | Neha      | 10            |
| Priya     | Neha      | 10            |
| Rahul     | Karan     | 20            |
| Rahul     | Sneha     | 20            |
| Karan     | Sneha     | 20            |

Explanation: The join matches rows with the same department ID. The employee ID condition eliminates self-matches and reversed duplicates.

## Example 5: Find employees earning more than another employee in the same department

Question: Find pairs where the first employee earns more than the second employee in the same department.

```SQL
SELECT
    e1.employee_name AS higher_paid_employee,
    e1.salary AS salary1,
    e2.employee_name AS lower_paid_employee,
    e2.salary AS salary2,
    e1.department_id
FROM employees AS e1
INNER JOIN employees AS e2
    ON e1.department_id = e2.department_id
WHERE e1.salary > e2.salary;
```

Output:

| higher_paid_employee | salary1 | lower_paid_employee | salary2 | department_id |
| -------------------- | ------- | ------------------- | ------- | ------------- |
| Amit                 | 90000   | Priya               | 70000   | 10            |
| Amit                 | 90000   | Neha                | 50000   | 10            |
| Priya                | 70000   | Neha                | 50000   | 10            |
| Rahul                | 65000   | Karan               | 45000   | 20            |
| Rahul                | 65000   | Sneha               | 60000   | 20            |
| Sneha                | 60000   | Karan               | 45000   | 20            |

Notice that `Sneha` appears as the higher-paid employee compared with Karan because her salary is 60000 versus Karan's 45000.

## Example 6: Find employees who share the same salary

Question: Find different employees earning exactly the same salary.

```SQL
SELECT
    e1.employee_name AS employee1,
    e2.employee_name AS employee2,
    e1.salary
FROM employees AS e1
INNER JOIN employees AS e2
    ON e1.salary = e2.salary
WHERE e1.employee_id < e2.employee_id;
```

With our current sample data, the query returns no rows, because all six salaries are different.

If two employees earn 50000, their names and salary will appear as a pair.

## Example 7: Find each employee's senior colleague

Question: Find employees who joined earlier than another employee in the same department.

For this example, assume the table also has a `joining_date` column.

```SQL
SELECT
    e1.employee_name AS senior_employee,
    e1.joining_date AS senior_joining_date,
    e2.employee_name AS junior_employee,
    e2.joining_date AS junior_joining_date
FROM employees AS e1
INNER JOIN employees AS e2
    ON e1.department_id = e2.department_id
WHERE e1.joining_date < e2.joining_date;
```

Explanation:

- Both aliases refer to the same employees table.
- The department condition limits comparisons to colleagues in the same department.
- The date condition finds the employee who joined earlier.

This is useful for seniority comparisons and team analysis.

## Example 8: Product and its related product

Suppose a `products` table contains `product_id`, `product_name`, and `related_product_id`.

| product_id | product_name | related_product_id |
| ---------- | ------------ | ------------------ |
| 101        | Laptop       | 102                |
| 102        | Mouse        | NULL               |
| 103        | Keyboard     | 102                |
| 104        | Monitor      | 101                |

Question: Display each product and its related product.

```SQL
SELECT
    p.product_name AS product,
    r.product_name AS related_product
FROM products AS p
LEFT JOIN products AS r
    ON p.related_product_id = r.product_id;
```

Output:

| product  | related_product |
| -------- | --------------- |
| Laptop   | Mouse           |
| Mouse    | NULL            |
| Keyboard | Mouse           |
| Monitor  | Laptop          |

Explanation: `p` represents the original product, while `r` represents the related product. This pattern is useful for product recommendations, category hierarchies, and linked records.

## Example 9: Category and parent category

Suppose a `categories` table has `category_id`, `category_name`, and `parent_category_id`.

| category_id | category_name | parent_category_id |
| ----------- | ------------- | ------------------ |
| 1           | Electronics   | NULL               |
| 2           | Computers     | 1                  |
| 3           | Laptops       | 2                  |
| 4           | Phones        | 1                  |

Question: Display every category with its immediate parent category.

```SQL
SELECT
    c.category_name AS category,
    p.category_name AS parent_category
FROM categories AS c
LEFT JOIN categories AS p
    ON c.parent_category_id = p.category_id;
```

Output:

| category    | parent_category |
| ----------- | --------------- |
| Electronics | NULL            |
| Computers   | Electronics     |
| Laptops     | Computers       |
| Phones      | Electronics     |

This is a common hierarchical data pattern.

## Example 10: Find duplicate names in the same table

Suppose a `customers` table contains multiple records and you want to find different customer IDs with the same name.

```SQL
SELECT
    c1.customer_name,
    c1.customer_id AS customer1_id,
    c2.customer_id AS customer2_id
FROM customers AS c1
INNER JOIN customers AS c2
    ON c1.customer_name = c2.customer_name
WHERE c1.customer_id < c2.customer_id;
```

Explanation: The same table is matched by customer name. The ID condition ensures each duplicate pair appears only once.

Note that identical names do not necessarily mean the records are true duplicates; other fields may need to be checked.

## Quick revision

| Use case                     | Join condition                               |
| ---------------------------- | -------------------------------------------- |
| Employee → Manager           | `e.manager_id = m.employee_id`               |
| Employees with same manager  | `e1.manager_id = e2.manager_id`              |
| Employees in same department | `e1.department_id = e2.department_id`        |
| Equal salaries               | `e1.salary = e2.salary`                      |
| Compare salaries             | Join related rows, then compare with `WHERE` |
| Product → Related product    | `p.related_product_id = r.product_id`        |
| Category → Parent category   | `c.parent_category_id = p.category_id`       |
| Duplicate names              | `c1.customer_name = c2.customer_name`        |

The most important takeaway: SELF JOIN is not a separate JOIN keyword. It is the technique of joining a table to itself using aliases. You can use `INNER JOIN`, `LEFT JOIN`, and other join types depending on which rows you need.
