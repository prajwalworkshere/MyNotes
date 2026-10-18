-- calculate_discounted_total function
DELIMITER //
CREATE FUNCTION calculate_discounted_total(
    p_quantity INT,
    p_unit_price DECIMAL(10,2),
    p_discount_percent DECIMAL(5,2)
)
RETURNS DECIMAL(12,2)
DETERMINISTIC
NO SQL
BEGIN
    DECLARE total DECIMAL(12,2);

    SET total = p_quantity * p_unit_price;

    SET total = total - (total * p_discount_percent / 100);

    RETURN total;
END //

DELIMITER ;

--  Calculate the grand total of all order items

SELECT
  SUM(
      calculate_discounted_total(
          quantity,
          unit_price,
          discount_percent
      )
  ) AS grand_total
FROM order_items;

