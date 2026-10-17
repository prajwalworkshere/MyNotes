

# SQL Subquery Practice — Answer Key with Outputs

**Database:** `subquery_practice`

**Tables:** `customers`, `orders`, `products`, `employees`

> Note: Outputs below show the relevant columns to keep the answer key readable. Results assume the sample data from the practice workbook and MySQL 8.0+.

---

# LEVEL 1 — EASY

## 1\. Products priced above the average price

```
SELECT *
FROM products
WHERE price > (
    SELECT AVG(price)
    FROM products
);
```

**Output:**

| product\_name | price |
| --- | --- |
| Laptop | 60000 |
| Sofa | 25000 |

The average price is ₹12,740. The query returns products priced above it.

## 2\. Product with the highest price

```
SELECT *
FROM products
WHERE price = (
    SELECT MAX(price)
    FROM products
);
```

**Output:** `Laptop` — ₹60,000.

## 3\. Product with the lowest price

```
SELECT *
FROM products
WHERE price = (
    SELECT MIN(price)
    FROM products
);
```

**Output:** `Pen` — ₹50.

## 4\. Employees earning above the average salary

```
SELECT *
FROM employees
WHERE salary > (
    SELECT AVG(salary)
    FROM employees
);
```

**Output:**

| employee\_name | salary |
| --- | --- |
| Raj | 80000 |
| Meera | 60000 |
| Kavya | 55000 |
| Isha | 50000 |
| Pooja | 70000 |

The overall average salary is ₹51,000.

Correction: Isha earns ₹50,000, which is below ₹51,000. Therefore, the correct output is:

| employee\_name | salary |
| --- | --- |
| Raj | 80000 |
| Meera | 60000 |
| Kavya | 55000 |
| Pooja | 70000 |

## 5\. Products priced exactly at the average

```
SELECT *
FROM products
WHERE price = (
    SELECT AVG(price)
    FROM products
);
```

**Output:** Empty result — no product costs ₹12,740.

## 6\. Customers who have placed orders (`IN`)

```
SELECT *
FROM customers
WHERE customer_id IN (
    SELECT customer_id
    FROM orders
);
```

**Output — customer names:**

| customer\_name |
| --- |
| Rahul |
| Priya |
| Sneha |
| Neha |
| Karan |

## 7\. Customers who have never placed orders (`NOT IN`)

```
SELECT *
FROM customers
WHERE customer_id NOT IN (
    SELECT customer_id
    FROM orders
    WHERE customer_id IS NOT NULL
);
```

**Output:**

| customer\_name |
| --- |
| Amit |
| Vijay |
| Anjali |

## 8\. Products belonging to categories containing a product priced above ₹10,000

```
SELECT *
FROM products
WHERE category IN (
    SELECT category
    FROM products
    WHERE price > 10000
);
```

**Output — product names:**

- Laptop
- Mouse
- Keyboard
- Chair
- Desk
- Monitor
- Sofa
- Headphones

These products belong to Electronics or Furniture.

## 9\. Employees in departments represented by employees earning above ₹60,000

```
SELECT *
FROM employees
WHERE department IN (
    SELECT department
    FROM employees
    WHERE salary > 60000
);
```

**Output — IT department employees:**

- Raj
- Meera
- Arjun
- Pooja

## 10\. Customers who placed an order above ₹2,000

```
SELECT *
FROM customers
WHERE customer_id IN (
    SELECT customer_id
    FROM orders
    WHERE order_amount > 2000
);
```

**Output:**

- Rahul
- Sneha
- Karan

## 11\. Customers with at least one order (`EXISTS`)

```
SELECT *
FROM customers c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.customer_id
);
```

**Output:** Rahul, Priya, Sneha, Neha and Karan.

## 12\. Customers without orders (`NOT EXISTS`)

```
SELECT *
FROM customers c
WHERE NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.customer_id
);
```

**Output:** Amit, Vijay and Anjali.

## 13\. Products with another, more expensive product in the same category

```
SELECT *
FROM products p
WHERE EXISTS (
    SELECT 1
    FROM products p2
    WHERE p2.category = p.category
      AND p2.price > p.price
);
```

**Output — product names:**

- Mouse
- Keyboard
- Chair
- Desk
- Notebook
- Monitor
- Headphones

## 14\. Employees who manage at least one employee

```
SELECT *
FROM employees e
WHERE EXISTS (
    SELECT 1
    FROM employees e2
    WHERE e2.manager_id = e.employee_id
);
```

**Output:**

| employee\_name |
| --- |
| Raj |
| Kavya |
| Isha |

## 15\. Departments with at least one employee earning above ₹50,000

```
SELECT DISTINCT e.department
FROM employees e
WHERE EXISTS (
    SELECT 1
    FROM employees e2
    WHERE e2.department = e.department
      AND e2.salary > 50000
);
```

**Output:**

| department |
| --- |
| IT |
| HR |

---

# LEVEL 2 — MEDIUM

## 16\. Products priced above at least one Electronics product (`ANY`)

```
SELECT *
FROM products
WHERE price > ANY (
    SELECT price
    FROM products
    WHERE category = 'Electronics'
);
```

**Output — product names:**

Laptop, Mouse, Keyboard, Chair, Desk, Monitor, Sofa, Headphones.

The condition is true when a product costs more than at least one Electronics product.

## 17\. Products priced above every Electronics product (`ALL`)

```
SELECT *
FROM products
WHERE price > ALL (
    SELECT price
    FROM products
    WHERE category = 'Electronics'
);
```

**Output:** Empty result.

The most expensive Electronics product is the Laptop at ₹60,000. No product costs more than ₹60,000.

## 18\. Employees earning more than at least one HR employee

```
SELECT *
FROM employees
WHERE salary > ANY (
    SELECT salary
    FROM employees
    WHERE department = 'HR'
);
```

**Output — employee names:**

Raj, Meera, Arjun, Kavya, Rohan, Isha, Dev, Pooja and Aman.

## 19\. Employees earning more than every HR employee

```
SELECT *
FROM employees
WHERE salary > ALL (
    SELECT salary
    FROM employees
    WHERE department = 'HR'
);
```

**Output:**

| employee\_name | salary |
| --- | --- |
| Raj | 80000 |
| Meera | 60000 |
| Pooja | 70000 |

## 20\. Products priced below every Furniture product

```
SELECT *
FROM products
WHERE price < ALL (
    SELECT price
    FROM products
    WHERE category = 'Furniture'
);
```

**Output — product names:**

Mouse, Keyboard, Notebook, Pen and Headphones.

## 21\. Employees earning above their own department's average

```
SELECT *
FROM employees e
WHERE salary > (
    SELECT AVG(e2.salary)
    FROM employees e2
    WHERE e2.department = e.department
);
```

**Output:**

| employee\_name | salary |
| --- | --- |
| Raj | 80000 |
| Pooja | 70000 |
| Kavya | 55000 |
| Isha | 50000 |
| Aman | 45000 |

## 22\. Products priced above their category average

```
SELECT *
FROM products p
WHERE price > (
    SELECT AVG(p2.price)
    FROM products p2
    WHERE p2.category = p.category
);
```

**Output — product names:**

Laptop, Monitor, Sofa, Desk and Notebook.

## 23\. Customers with an order above the average order amount

```
SELECT *
FROM customers c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.customer_id
      AND o.order_amount > (
          SELECT AVG(order_amount)
          FROM orders
      )
);
```

**Output:** Rahul, Sneha and Karan.

The average order amount is ₹2,000.

## 24\. Customers who have placed more than one order

```
SELECT *
FROM customers c
WHERE (
    SELECT COUNT(*)
    FROM orders o
    WHERE o.customer_id = c.customer_id
) > 1;
```

**Output:**

| customer\_name |
| --- |
| Rahul |
| Priya |
| Sneha |

## 25\. Employees earning more than their manager

```
SELECT *
FROM employees e
WHERE e.manager_id IS NOT NULL
  AND e.salary > (
      SELECT m.salary
      FROM employees m
      WHERE m.employee_id = e.manager_id
  );
```

**Output:** Empty result. No employee earns more than their assigned manager.

## 26\. Customer or customers who placed the highest-value single order

```
SELECT *
FROM customers
WHERE customer_id IN (
    SELECT customer_id
    FROM orders
    WHERE order_amount = (
        SELECT MAX(order_amount)
        FROM orders
    )
);
```

**Output:** Karan — the highest order is ₹4,500.

## 27\. Customers whose total order amount exceeds ₹3,000

```
SELECT *
FROM customers c
WHERE (
    SELECT SUM(o.order_amount)
    FROM orders o
    WHERE o.customer_id = c.customer_id
) > 3000;
```

**Output:**

| customer\_name | total |
| --- | --- |
| Rahul | 4000 |
| Sneha | 4200 |
| Karan | 4500 |

## 28\. Department with the highest average salary

```
SELECT department, AVG(salary) AS avg_salary
FROM employees
GROUP BY department
HAVING AVG(salary) = (
    SELECT MAX(dept_avg)
    FROM (
        SELECT AVG(salary) AS dept_avg
        FROM employees
        GROUP BY department
    ) AS d
);
```

**Output:**

| department | avg\_salary |
| --- | --- |
| IT | 63750 |

## 29\. Products priced above the average Electronics price

```
SELECT *
FROM products
WHERE price > (
    SELECT AVG(price)
    FROM products
    WHERE category = 'Electronics'
);
```

**Output:**

| product\_name | price |
| --- | --- |
| Laptop | 60000 |
| Sofa | 25000 |

The average Electronics price is ₹15,960.

## 30\. Employees earning the highest salary in their department

```
SELECT *
FROM employees e
WHERE salary = (
    SELECT MAX(e2.salary)
    FROM employees e2
    WHERE e2.department = e.department
);
```

**Output:**

| employee\_name | department | salary |
| --- | --- | --- |
| Raj | IT | 80000 |
| Kavya | HR | 55000 |
| Isha | Sales | 50000 |

## 31\. Customers whose total order amount exceeds ₹3,000 — derived table

```
SELECT *
FROM (
    SELECT customer_id,
           SUM(order_amount) AS total
    FROM orders
    GROUP BY customer_id
) AS t
WHERE total > 3000;
```

**Output:**

| customer\_id | total |
| --- | --- |
| 1 | 4000 |
| 4 | 4200 |
| 7 | 4500 |

## 32\. Departments with an average salary above ₹50,000

```
SELECT *
FROM (
    SELECT department,
           AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department
) AS d
WHERE avg_salary > 50000;
```

**Output:**

| department | avg\_salary |
| --- | --- |
| IT | 63750 |

## 33\. Categories containing more than two products

```
SELECT *
FROM (
    SELECT category,
           COUNT(*) AS product_count
    FROM products
    GROUP BY category
) AS p
WHERE product_count > 2;
```

**Output:**

| category | product\_count |
| --- | --- |
| Electronics | 5 |
| Furniture | 3 |

## 34\. Customers who have placed at least two orders

```
SELECT *
FROM (
    SELECT customer_id,
           COUNT(*) AS order_count
    FROM orders
    GROUP BY customer_id
) AS t
WHERE order_count >= 2;
```

**Output:**

| customer\_id | order\_count |
| --- | --- |
| 1 | 2 |
| 2 | 2 |
| 4 | 2 |

## 35\. Departments whose highest salary exceeds ₹60,000

```
SELECT *
FROM (
    SELECT department,
           MAX(salary) AS highest_salary
    FROM employees
    GROUP BY department
) AS d
WHERE highest_salary > 60000;
```

**Output:**

| department | highest\_salary |
| --- | --- |
| IT | 80000 |

---

# LEVEL 3 — HARD

## 36\. Highest-paid employee in each department

```
SELECT *
FROM employees e
WHERE NOT EXISTS (
    SELECT 1
    FROM employees e2
    WHERE e2.department = e.department
      AND e2.salary > e.salary
);
```

**Output:** Raj, Kavya and Isha.

## 37\. Most expensive product in each category

```
SELECT *
FROM products p
WHERE NOT EXISTS (
    SELECT 1
    FROM products p2
    WHERE p2.category = p.category
      AND p2.price > p.price
);
```

**Output:**

| product\_name | category | price |
| --- | --- | --- |
| Laptop | Electronics | 60000 |
| Sofa | Furniture | 25000 |
| Notebook | Stationery | 100 |

## 38\. Customers whose every order exceeds ₹1,000, excluding customers without orders

```
SELECT *
FROM customers c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.customer_id
)
AND NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.customer_id
      AND o.order_amount <= 1000
);
```

**Output:** Rahul, Sneha and Karan.

## 39\. Employees earning more than every other employee in their department

```
SELECT *
FROM employees e
WHERE NOT EXISTS (
    SELECT 1
    FROM employees e2
    WHERE e2.department = e.department
      AND e2.employee_id <> e.employee_id
      AND e2.salary >= e.salary
);
```

**Output:** Raj, Kavya and Isha.

## 40\. Departments where no employee earns below ₹40,000

```
SELECT DISTINCT e.department
FROM employees e
WHERE NOT EXISTS (
    SELECT 1
    FROM employees e2
    WHERE e2.department = e.department
      AND e2.salary < 40000
);
```

**Output:**

| department |
| --- |
| IT |

## 41\. Customers whose total order amount exceeds the average customer total

```
SELECT t.customer_id, t.total
FROM (
    SELECT customer_id,
           SUM(order_amount) AS total
    FROM orders
    GROUP BY customer_id
) AS t
WHERE t.total > (
    SELECT AVG(customer_total)
    FROM (
        SELECT SUM(order_amount) AS customer_total
        FROM orders
        GROUP BY customer_id
    ) AS totals
);
```

**Output:**

| customer\_id | total |
| --- | --- |
| 1 | 4000 |
| 4 | 4200 |
| 7 | 4500 |

The average total among customers with orders is ₹3,200.

## 42\. Products priced above their category average with below-average stock

```
SELECT *
FROM products p
WHERE p.price > (
    SELECT AVG(p2.price)
    FROM products p2
    WHERE p2.category = p.category
)
AND p.stock < (
    SELECT AVG(stock)
    FROM products
);
```

**Output:**

| product\_name | price | stock |
| --- | --- | --- |
| Laptop | 60000 | 10 |
| Sofa | 25000 | 5 |

The overall average stock is 94 units.

## 43\. Employees earning above both their department average and overall average

```
SELECT *
FROM employees e
WHERE e.salary > (
    SELECT AVG(e2.salary)
    FROM employees e2
    WHERE e2.department = e.department
)
AND e.salary > (
    SELECT AVG(salary)
    FROM employees
);
```

**Output:**

| employee\_name | salary |
| --- | --- |
| Raj | 80000 |
| Pooja | 70000 |
| Kavya | 55000 |

## 44\. Customers with an above-average order and at least two orders

```
SELECT *
FROM customers c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.customer_id
      AND o.order_amount > (
          SELECT AVG(order_amount)
          FROM orders
      )
)
AND (
    SELECT COUNT(*)
    FROM orders o
    WHERE o.customer_id = c.customer_id
) >= 2;
```

**Output:** Rahul and Sneha.

## 45\. Products priced above the Electronics average but below the most expensive Furniture product

```
SELECT *
FROM products
WHERE price > (
    SELECT AVG(price)
    FROM products
    WHERE category = 'Electronics'
)
AND price < (
    SELECT MAX(price)
    FROM products
    WHERE category = 'Furniture'
);
```

**Output:** Empty result.

The Electronics average is ₹15,960, and the maximum Furniture price is ₹25,000. No product satisfies both conditions.

## 46\. Second-highest salary without `LIMIT`

```
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

**Output:**

| employee\_name | salary |
| --- | --- |
| Pooja | 70000 |

## 47\. Second-most-expensive product without `LIMIT`

```
SELECT *
FROM products
WHERE price = (
    SELECT MAX(price)
    FROM products
    WHERE price < (
        SELECT MAX(price)
        FROM products
    )
);
```

**Output:**

| product\_name | price |
| --- | --- |
| Sofa | 25000 |

## 48\. Second-highest salary in each department

```
SELECT *
FROM employees e
WHERE e.salary = (
    SELECT MAX(e2.salary)
    FROM employees e2
    WHERE e2.department = e.department
      AND e2.salary < (
          SELECT MAX(e3.salary)
          FROM employees e3
          WHERE e3.department = e.department
      )
);
```

**Output:**

| employee\_name | department | salary |
| --- | --- | --- |
| Pooja | IT | 70000 |
| Rohan | HR | 40000 |
| Aman | Sales | 45000 |

## 49\. Customers who ordered in every distinct month represented in the orders table

```
SELECT c.customer_id, c.customer_name
FROM customers c
WHERE (
    SELECT COUNT(DISTINCT DATE_FORMAT(o.order_date, '%Y-%m'))
    FROM orders o
    WHERE o.customer_id = c.customer_id
) = (
    SELECT COUNT(DISTINCT DATE_FORMAT(order_date, '%Y-%m'))
    FROM orders
);
```

**Output:** Empty result.

The orders table contains January, February and March 2026. No customer placed orders in all three months.

## 50\. Department or departments with the highest average salary, without `LIMIT`

```
SELECT department, AVG(salary) AS avg_salary
FROM employees
GROUP BY department
HAVING AVG(salary) >= ALL (
    SELECT AVG(salary)
    FROM employees
    GROUP BY department
);
```

**Output:**

| department | avg\_salary |
| --- | --- |
| IT | 63750 |

---

# Final Revision Table

| Subquery type | When to use |
| --- | --- |
| `IN` | Match values in a list |
| `NOT IN` | Exclude values in a list |
| `=` | Compare with one value |
| `>`, `<`, `>=`, `<=` | Compare with a scalar result |
| `ANY` | Condition is true for at least one value |
| `ALL` | Condition is true for every value |
| `EXISTS` | At least one matching row exists |
| `NOT EXISTS` | No matching row exists |
| Correlated subquery | Inner query refers to the outer query |
| Non-correlated subquery | Inner query works independently |
| Derived table | Query a result produced in the `FROM` clause |
| Aggregate subquery | Compare against `AVG()`, `MAX()`, `MIN()`, `SUM()` or `COUNT()` |

## How to practise

1. Read a question and identify its key requirement.
2. Write your SQL query without looking at this answer key.
3. Execute the query in MySQL.
4. Compare your result with the expected output.
5. If the results differ, check the comparison operator, grouping, correlation condition and treatment of `NULL`.

**Important:** Several questions can be solved with joins or other SQL techniques too. The goal here is to practise recognizing and writing subqueries.
