# ShopPractice Database Schema

## 1. Database Overview

Database name: `shoppractice`

This database represents a simple e-commerce shopping system. It contains four tables:

| Table         | Purpose                                                                         |
| ------------- | ------------------------------------------------------------------------------- |
| `customers`   | Stores customer information                                                     |
| `products`    | Stores product details and prices                                               |
| `orders`      | Stores orders placed by customers                                               |
| `order_items` | Stores products included in each order, their quantities, prices, and discounts |

## 2. Schema Diagram

### customers

🔑 `customer_id` — Primary Key

`first_name`, `last_name`, `email`, `city`, `signup_date`, `status`

One customer can place many orders

### orders

🔑 `order_id` — Primary Key

🔗 `customer_id` — Foreign Key

`order_date`, `status`, `shipping_fee`

One order can contain many order items

### order_items

🔑 `order_item_id` — Primary Key

🔗 `order_id` — Foreign Key

🔗 `product_id` — Foreign Key

`quantity`, `unit_price`, `discount_percent`

Each order item refers to one product

### products

🔑 `product_id` — Primary Key

`product_name`, `category`, `price`, `stock_quantity`, `supplier_name`, `product_status`

The key relationships are:

```
customers  1 ───────< many orders

orders     1 ───────< many order_items

products   1 ───────< many order_items
```

`order_items` acts as a bridge between `orders` and `products`. This allows an order to contain multiple products, and a product to appear in multiple orders.

## 3. Table schemas

### A. `customers`

Stores customer details.

| Column        | Data type      | Key / constraint   | Meaning              |
| ------------- | -------------- | ------------------ | -------------------- |
| `customer_id` | `INT`          | PK, AUTO_INCREMENT | Unique customer ID   |
| `first_name`  | `VARCHAR(50)`  | NOT NULL           | First name           |
| `last_name`   | `VARCHAR(50)`  | NOT NULL           | Last name            |
| `email`       | `VARCHAR(100)` | UNIQUE, NOT NULL   | Unique email address |
| `city`        | `VARCHAR(50)`  | —                  | Customer's city      |
| `signup_date` | `DATE`         | —                  | Registration date    |
| `status`      | `VARCHAR(20)`  | —                  | Customer status      |

SQL definition:

```
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    city VARCHAR(50),
    signup_date DATE,
    status VARCHAR(20)
);
```

### B. `products`

Stores information about products available in the shop.

| Column           | Data type       | Key / constraint   | Meaning           |
| ---------------- | --------------- | ------------------ | ----------------- |
| `product_id`     | `INT`           | PK, AUTO_INCREMENT | Unique product ID |
| `product_name`   | `VARCHAR(100)`  | NOT NULL           | Product name      |
| `category`       | `VARCHAR(50)`   | —                  | Product category  |
| `price`          | `DECIMAL(10,2)` | NOT NULL           | Product price     |
| `stock_quantity` | `INT`           | DEFAULT 0          | Available stock   |
| `supplier_name`  | `VARCHAR(100)`  | —                  | Supplier name     |
| `product_status` | `VARCHAR(30)`   | —                  | Product status    |

SQL definition:

```
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    supplier_name VARCHAR(100),
    product_status VARCHAR(30)
);
```

### C. `orders`

Stores orders placed by customers.

| Column         | Data type      | Key / constraint             | Meaning                       |
| -------------- | -------------- | ---------------------------- | ----------------------------- |
| `order_id`     | `INT`          | PK, AUTO_INCREMENT           | Unique order ID               |
| `customer_id`  | `INT`          | FK → `customers.customer_id` | Customer who placed the order |
| `order_date`   | `DATE`         | —                            | Date the order was placed     |
| `status`       | `VARCHAR(20)`  | —                            | Order status                  |
| `shipping_fee` | `DECIMAL(8,2)` | DEFAULT 0.00                 | Shipping charge               |

SQL definition:

```
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    order_date DATE,
    status VARCHAR(20),
    shipping_fee DECIMAL(8,2) DEFAULT 0.00,
    FOREIGN KEY (customer_id)
        REFERENCES customers(customer_id)
);
```

Important: `customer_id` is a foreign key here because each order is associated with a customer.

### D. `order_items`

Stores the individual products belonging to each order.

| Column             | Data type       | Key / constraint           | Meaning                               |
| ------------------ | --------------- | -------------------------- | ------------------------------------- |
| `order_item_id`    | `INT`           | PK, AUTO_INCREMENT         | Unique order-item ID                  |
| `order_id`         | `INT`           | FK → `orders.order_id`     | Related order                         |
| `product_id`       | `INT`           | FK → `products.product_id` | Related product                       |
| `quantity`         | `INT`           | NOT NULL                   | Quantity purchased                    |
| `unit_price`       | `DECIMAL(10,2)` | NOT NULL                   | Price per unit recorded for this item |
| `discount_percent` | `DECIMAL(5,2)`  | DEFAULT 0.00               | Discount percentage                   |

SQL definition:

```
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    discount_percent DECIMAL(5,2) DEFAULT 0.00,

    FOREIGN KEY (order_id)
        REFERENCES orders(order_id),

    FOREIGN KEY (product_id)
        REFERENCES products(product_id)
);
```

## 4. How the relationships work

### Relationship 1: Customers and orders

Suppose customer `customer_id = 17` places two orders.

```
customers
+-------------+------------+
| customer_id | first_name |
+-------------+------------+
| 17          | Vivaan     |
+-------------+------------+
          |
          | customer_id
          v
orders
+----------+-------------+
| order_id | customer_id|
+----------+-------------+
| 1        | 17          |
| 15       | 17          |
+----------+-------------+
```

Both orders belong to customer 17 because both rows contain `customer_id = 17`.

### Relationship 2: Orders and order items

```
orders
+----------+
| order_id |
+----------+
| 1        |
+----------+
     |
     v
order_items
+---------------+----------+------------+----------+
| order_item_id | order_id | product_id | quantity |
+---------------+----------+------------+----------+
| 1             | 1        | 5          | 2        |
| 2             | 1        | 10         | 3        |
+---------------+----------+------------+----------+
```

Order 1 contains two different order-item records.

### Relationship 3: Products and order items

```
products
+------------+----------------+
| product_id | product_name   |
+------------+----------------+
| 5          | Shoes Model 5  |
| 10         | Keyboard Model 10 |
+------------+----------------+
          ^             ^
          |             |
      order_items.product_id
```

The `product_id` in `order_items` identifies which product was purchased.

## 5. Most important JOIN query

Use this query to see the customer, order, and product information together.

```
SELECT
    c.customer_id,
    c.first_name,
    c.last_name,
    o.order_id,
    o.order_date,
    o.status AS order_status,
    p.product_name,
    oi.quantity,
    oi.unit_price,
    oi.discount_percent
FROM customers c
JOIN orders o
    ON c.customer_id = o.customer_id
JOIN order_items oi
    ON o.order_id = oi.order_id
JOIN products p
    ON oi.product_id = p.product_id;
```

How the joins work:

1. `customers` joins to `orders` using `customer_id`.
2. `orders` joins to `order_items` using `order_id`.
3. `order_items` joins to `products` using `product_id`.

This is the central query pattern for practicing multi-table SQL joins.

## 6. Useful schema-inspection commands

```
USE shoppractice;

SHOW TABLES;

DESCRIBE customers;
DESCRIBE products;
DESCRIBE orders;
DESCRIBE order_items;

SHOW CREATE TABLE customers;
SHOW CREATE TABLE products;
SHOW CREATE TABLE orders;
SHOW CREATE TABLE order_items;
```

`DESCRIBE` displays column definitions, while `SHOW CREATE TABLE` displays the complete table definition, including constraints.
