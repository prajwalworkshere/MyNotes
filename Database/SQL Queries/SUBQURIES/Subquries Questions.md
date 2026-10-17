
# SQL Subquery Practice Workbook

## Part 1: Create the practice database

```
CREATE DATABASE subquery_practice;
USE subquery_practice;
```

## Part 2: Create the tables

### Table 1: customers

```
CREATE TABLE customers (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(50),
    city VARCHAR(50),
    status VARCHAR(20)
);
```

### Table 2: orders

```
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    order_amount DECIMAL(10,2),
    order_date DATE,
    FOREIGN KEY (customer_id)
        REFERENCES customers(customer_id)
);
```

### Table 3: products

```
CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(50),
    category VARCHAR(50),
    price DECIMAL(10,2),
    stock INT
);
```

### Table 4: employees

```
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    employee_name VARCHAR(50),
    department VARCHAR(50),
    salary DECIMAL(10,2),
    manager_id INT
);
```

## Part 3: Insert sample data

### Insert customers

```
INSERT INTO customers VALUES
(1, 'Rahul', 'Mumbai', 'active'),
(2, 'Priya', 'Pune', 'active'),
(3, 'Amit', 'Delhi', 'inactive'),
(4, 'Sneha', 'Mumbai', 'active'),
(5, 'Vijay', 'Chennai', 'inactive'),
(6, 'Neha', 'Pune', 'active'),
(7, 'Karan', 'Delhi', 'active'),
(8, 'Anjali', 'Mumbai', 'inactive');
```

### Insert orders

```
INSERT INTO orders VALUES
(101, 1, 1500, '2026-01-05'),
(102, 1, 2500, '2026-01-10'),
(103, 2, 800,  '2026-01-12'),
(104, 4, 3000, '2026-02-01'),
(105, 4, 1200, '2026-02-05'),
(106, 6, 500,  '2026-02-10'),
(107, 7, 4500, '2026-02-15'),
(108, 2, 2000, '2026-03-01');
```

### Insert products

```
INSERT INTO products VALUES
(1, 'Laptop', 'Electronics', 60000, 10),
(2, 'Mouse', 'Electronics', 800, 100),
(3, 'Keyboard', 'Electronics', 1500, 50),
(4, 'Chair', 'Furniture', 5000, 20),
(5, 'Desk', 'Furniture', 12000, 10),
(6, 'Notebook', 'Stationery', 100, 200),
(7, 'Pen', 'Stationery', 50, 500),
(8, 'Monitor', 'Electronics', 15000, 15),
(9, 'Sofa', 'Furniture', 25000, 5),
(10, 'Headphones', 'Electronics', 2500, 30);
```

### Insert employees

```
INSERT INTO employees VALUES
(1, 'Raj', 'IT', 80000, NULL),
(2, 'Meera', 'IT', 60000, 1),
(3, 'Arjun', 'IT', 45000, 1),
(4, 'Kavya', 'HR', 55000, NULL),
(5, 'Rohan', 'HR', 40000, 4),
(6, 'Isha', 'Sales', 50000, NULL),
(7, 'Dev', 'Sales', 35000, 6),
(8, 'Pooja', 'IT', 70000, 1),
(9, 'Aman', 'Sales', 45000, 6),
(10, 'Tara', 'HR', 30000, 4);
```

## Part 4: Verify your tables

```
SELECT * FROM customers;
SELECT * FROM orders;
SELECT * FROM products;
SELECT * FROM employees;
```

---

# LEVEL 1 — EASY

Focus on understanding what the question asks before writing SQL.

## A. Single-value subqueries

1. Find products whose price is greater than the average price of all products.
2. Find the product with the highest price using a subquery.
3. Find the product with the lowest price using a subquery.
4. Find employees whose salary is greater than the average salary of all employees.
5. Find products whose price is equal to the average product price.

## B. IN and NOT IN

6. Find customers who have placed at least one order.
7. Find customers who have never placed an order using `NOT IN`.
8. Find products belonging to categories in which a product costs more than ₹10,000.
9. Find employees whose department is one of the departments represented by employees earning more than ₹60,000.
10. Find customers who have placed an order worth more than ₹2,000.

## C. EXISTS and NOT EXISTS

11. Find customers who have at least one order using `EXISTS`.
12. Find customers who have no orders using `NOT EXISTS`.
13. Find products for which another product exists in the same category with a higher price.
14. Find employees who have at least one other employee reporting to them.
15. Find departments that have at least one employee earning more than ₹50,000.

---

# LEVEL 2 — MEDIUM

Focus on comparisons, correlation, grouping, and filtering aggregate results.

## A. ANY and ALL

16. Find products whose price is greater than at least one Electronics product's price using `ANY`.
17. Find products whose price is greater than every Electronics product's price using `ALL`.
18. Find employees earning more than at least one employee in the HR department.
19. Find employees earning more than every employee in the HR department.
20. Find products whose price is less than every Furniture product's price.

## B. Correlated subqueries

21. Find employees who earn more than the average salary of their own department.
22. Find products priced above the average price of their own category.
23. Find customers whose order amount is greater than the average order amount across all orders.
24. Find customers who have placed more than one order using a correlated subquery.
25. Find employees who earn more than their manager.

## C. Aggregate subqueries

26. Find the customer or customers who placed the highest-value single order.
27. Find customers whose total order amount is greater than ₹3,000.
28. Find the department with the highest average salary.
29. Find products priced above the average price of products in the Electronics category.
30. Find employees whose salary equals the highest salary in their department.

## D. Derived tables

31. Calculate the total amount ordered by each customer, then show only customers whose total exceeds ₹3,000.
32. Calculate the average salary per department, then display departments whose average salary exceeds ₹50,000.
33. Calculate the number of products in each category, then display categories with more than two products.
34. Calculate each customer's number of orders, then display customers who have placed at least two orders.
35. Calculate each department's highest salary, then show departments whose highest salary exceeds ₹60,000.

---

# LEVEL 3 — HARD

These questions combine multiple subquery concepts. Work through them carefully.

## A. Correlated subqueries and NOT EXISTS

36. Find the highest-paid employee in each department using a correlated subquery.
37. Find products that are the most expensive in their category.
38. Find customers whose every order is worth more than ₹1,000. Do not include customers who have no orders.
39. Find employees who earn more than all employees in their department other than themselves.
40. Find departments in which no employee earns less than ₹40,000.

## B. Multiple subqueries

41. Find customers whose total order amount is greater than the average total order amount per customer among customers who have orders.
42. Find products whose price is greater than the average price of their category and whose stock is below the average stock of all products.
43. Find employees whose salary is greater than the average salary of their department and greater than the overall average salary.
44. Find customers who have placed an order above the average order amount and who have placed at least two orders.
45. Find products that are more expensive than the average price of Electronics products but cheaper than the most expensive Furniture product.

## C. Challenge questions

46. Find the second-highest salary without using `LIMIT`.
47. Find the second-most-expensive product without using `LIMIT`.
48. Find employees whose salary is the second-highest salary in their department.
49. Find customers who have placed orders in every month represented in the orders table. Consider distinct calendar months.
50. Find the department or departments with the highest average salary without using `LIMIT`.

---

# Part 5: Additional practice rules

- Try to solve each question using a subquery first.
- For `IN`, focus on matching values from a list.
- For `=`, `>`, and `<`, check whether the subquery returns a single value or multiple rows.
- For `ANY`, remember that the comparison must be true for at least one value.
- For `ALL`, remember that the comparison must be true for every value.
- For `EXISTS` and `NOT EXISTS`, focus on whether matching rows exist.
- For correlated subqueries, look for references to columns from the outer query.
- For derived tables, remember that the subquery in the `FROM` clause needs an alias in MySQL.
- Check how `NULL`, duplicate values, ties, and customers without orders affect your results.

## Suggested learning plan

- Day 1: Questions 1–15.
- Day 2: Questions 16–25.
- Day 3: Questions 26–35.
- Day 4: Questions 36–45.
- Day 5: Questions 46–50 and revision.

**Goal:** Before writing a query, identify what the question is asking for, then choose the subquery pattern that fits. You do not need to memorize all 50 queries.

## Your subquery decision guide

Use this quick reference while solving the workbook.

| If the question asks… | Start by considering… |
| --- | --- |
| Match a list of IDs | `IN` |
| Exclude IDs from a list | `NOT IN` or `NOT EXISTS` |
| Compare with one average or maximum | `=`, `>`, or `<` |
| Compare with at least one value | `ANY` |
| Compare with every value | `ALL` |
| Check for a matching record | `EXISTS` |
| Check that no matching record exists | `NOT EXISTS` |
| Compare each row with its own group | Correlated subquery |
| Calculate totals, then filter them | Derived table |
