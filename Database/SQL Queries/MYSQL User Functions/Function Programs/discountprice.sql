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
