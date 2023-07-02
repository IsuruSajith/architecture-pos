INSERT INTO customer (name, address, contact) VALUES ('Kasun', 'Galle', '078-1234567');

INSERT INTO `order` (datetime) VALUES (NOW());
INSERT INTO `order` (datetime) VALUES (NOW());

INSERT INTO item (code, description, qty, unit_price) VALUES ('123456', 'Test Item 1', 10, 1250.00);
INSERT INTO item (code, description, qty, unit_price) VALUES ('456789', 'Test Item 2', 20, 2250.00);

INSERT INTO order_detail (order_id, item_code, unit_price, qty) VALUES (1, '123456', 1250.00, 5);
INSERT INTO order_detail (order_id, item_code, unit_price, qty) VALUES (1, '456789', 2250.00, 2);
INSERT INTO order_detail (order_id, item_code, unit_price, qty) VALUES (2, '456789', 1250.00, 3);

