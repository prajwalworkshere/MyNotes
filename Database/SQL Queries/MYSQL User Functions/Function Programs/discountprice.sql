-- Create a MySQL UDF-style function
-- MySQL supports stored functions created using CREATE FUNCTION. The following function calculates the total price after discount for an order item.

DELIMITER //
CREATE FUNCTION discounted_price(d_units INT,d_price DECIMAL(10,2),d_discount DECIMAL(10,2))
RETURNS DECIMAL(10,2)
DETERMINISTIC 
NO SQL
BEGIN
    DECLARE FinalPrice DECIMAL(10,2);
    SET FinalPrice= d_units*(d_price-(d_price*(d_discount/100));
    RETURN FinalPrice ;
END //
DELIMITER ;


-- USE WITH SELECT STATEMENT

SELECT calculate_discounted_total(2, 500.00, 10.00)
AS final_amount;

-- Use the function with your table

SELECT
    order_item_id,
    order_id,
    product_id,
    quantity,
    unit_price,
    discount_percent,
    calculate_discounted_total(
        quantity,
        unit_price,
        discount_percent
    ) AS final_amount
FROM order_items;
