CREATE DATABASE sales_db;

USE sales_db;

CREATE TABLE order_items (
    order_item_id INT NOT NULL PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    discount_percent DECIMAL(5,2) NOT NULL
);

INSERT INTO order_items
(order_item_id, order_id, product_id, quantity,
 unit_price, discount_percent)
VALUES
(1, 101, 201, 2, 500.00, 10.00),
(2, 101, 202, 1, 1200.00, 5.00),
(3, 102, 203, 3, 250.00, 0.00),
(4, 102, 204, 2, 750.00, 15.00),
(5, 103, 205, 4, 100.00, 20.00),
(6, 103, 206, 1, 2000.00, 10.00),
(7, 104, 207, 5, 150.00, 5.00),
(8, 104, 208, 2, 900.00, 12.00),
(9, 105, 209, 1, 3000.00, 25.00),
(10, 105, 210, 3, 400.00, 8.00);


SELECT * FROM order_items;
