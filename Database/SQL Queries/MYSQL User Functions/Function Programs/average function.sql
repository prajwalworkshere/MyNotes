-- Average Price Stored Function

use shoppractice;
DELIMITER //
CREATE FUNCTION AVG_PRICE()
RETURNS DECIMAL(10,2)
-- when using select use reads sql data 
READS SQL DATA
BEGIN
	DECLARE AVGPRICE DECIMAL(10,2);
	SELECT AVG(unit_price)
	INTO AVGPRICE
	FROM order_items;
	RETURN AVGPRICE;
END //
DELIMITER ;
SELECT AVG_PRICE() AS RESULTS;
